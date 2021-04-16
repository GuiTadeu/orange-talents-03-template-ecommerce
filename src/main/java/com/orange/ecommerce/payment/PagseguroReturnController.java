package com.orange.ecommerce.payment;

import com.orange.ecommerce.product.ProductBuy;
import com.orange.ecommerce.product.ProductBuyRepository;
import com.orange.ecommerce.share.MailSender;
import com.orange.ecommerce.share.ValidationErrorsDTO;
import com.orange.ecommerce.user.User;
import com.orange.ecommerce.user.UserRepository;
import io.swagger.annotations.Api;
import javassist.tools.web.BadHttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import java.util.logging.Logger;

import static com.orange.ecommerce.payment.PaymentStatus.PAID;
import static com.orange.ecommerce.payment.PaymentStatus.PENDING;

@Api
@RestController
public class PagseguroReturnController {

    private final ProductBuyRepository productBuyRepository;
    private final GatewayTransactionRepository gatewayTransactionRepository;
    private final UserRepository userRepository;

    public PagseguroReturnController(ProductBuyRepository productBuyRepository,
                                  GatewayTransactionRepository gatewayTransactionRepository,
                                  UserRepository userRepository) {
        this.productBuyRepository = productBuyRepository;
        this.gatewayTransactionRepository = gatewayTransactionRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/gateway/pagseguro/productBuy/{buyId}/return")
    public ResponseEntity<?> productPurchasedReturn(@PathVariable Long buyId, @RequestBody @Valid GatewayReturn gatewayResponse,
                                                       PagseguroGeneralStatusChecker pagseguroPaymentStatus, Principal principal) throws Exception {
        User loggedUser = userRepository.findByEmail(principal.getName()).orElseThrow(BadHttpRequest::new);
        ProductBuy productBuy = productBuyRepository.findById(buyId).orElseThrow(BadHttpRequest::new);
        Optional<GatewayTransaction> existingTransaction = gatewayTransactionRepository.findTopByPaymentIdOrderByCreatedAtDesc(gatewayResponse.getPaymentId());

        if(existingTransaction.isPresent()) {
            GatewayTransaction foundTransaction = existingTransaction.get();

            if(foundTransaction.isGatewaySuccess(pagseguroPaymentStatus)) {
                var validationDTO = new ValidationErrorsDTO();
                validationDTO.addError("Changing a successful transaction is not allowed!");
                return ResponseEntity.badRequest().body(validationDTO);
            }

            GatewayTransaction newTransaction = foundTransaction.toNewTransaction(gatewayResponse.getStatus());
            return checkTransactionStatusAndExecute(newTransaction, pagseguroPaymentStatus, loggedUser);
        }

        GatewayTransaction newTransaction = gatewayResponse.toModel(productBuy);
        return checkTransactionStatusAndExecute(newTransaction, pagseguroPaymentStatus, loggedUser);
    }

    private ResponseEntity<?> checkTransactionStatusAndExecute(GatewayTransaction transaction, GatewayStatusChecker checker, User loggedUser) throws Exception {

        ProductBuy productBuy = transaction.getProductBuy();

        if(transaction.isGatewaySuccess(checker)) {
            gatewayTransactionRepository.save(transaction);

            productBuy.setStatus(PAID);
            productBuyRepository.save(productBuy);

            invoice(productBuy.getId(), transaction.getBuyerId());
            ranking(productBuy.getId(), transaction.getSellerId());
            sendSuccessMail(loggedUser, transaction);

            return ResponseEntity.ok(new NewGatewayTransactionDTO(transaction));
        }

        if(transaction.isGatewayError(checker)) {
            gatewayTransactionRepository.save(transaction);

            productBuy.setStatus(PENDING);
            productBuyRepository.save(productBuy);

            sendErrorMail(loggedUser, transaction);

            return ResponseEntity.ok(new NewGatewayTransactionDTO(transaction));
        }

        return ResponseEntity.badRequest().build();
    }

    private void sendSuccessMail(User loggedUser, GatewayTransaction transaction) {
        var sender = new MailSender()
                .setFrom(loggedUser.getEmail())
                .setTo("orange@gmail.com")
                .setSubject("Compra finalizada com sucesso!")
                .setText(String.format("A sua compra no %s do produto %s foi aprovada!",
                        transaction.getPaymentMethodName(), transaction.getProductName()))
                .send();
    }

    private void sendErrorMail(User loggedUser, GatewayTransaction transaction) {
        var returnUrl = String.format("/gateway/%s/productBuy/%s/return",
                transaction.getPaymentMethodCode(), transaction.getProductBuyId());

        var tryAgainUrl = String.format("%s?buyId=%s&redirectUrl=%s",
                transaction.getPaymentMethodUrl(), transaction.getProductBuyId(), returnUrl);

        var sender = new MailSender()
                .setFrom(loggedUser.getEmail())
                .setTo("orange@gmail.com")
                .setSubject("Problema no pagamento da compra!")
                .setText(String.format("A sua compra no %s do produto %s não foi aprovada! Tente novamente: %s",
                        transaction.getPaymentMethodName(), transaction.getProductName(), tryAgainUrl))
                .send();
    }

    private void invoice(Long buyId, Long buyerId) {
        var logger = Logger.getLogger("global");
        logger.info("Enviando dados para nota fiscal...");
    }

    private void ranking(Long buyId, Long sellerId) {
        var logger = Logger.getLogger("global");
        logger.info("Enviando dados para ranking...");
    }
}
