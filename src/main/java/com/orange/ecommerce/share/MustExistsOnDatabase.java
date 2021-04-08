package com.orange.ecommerce.share;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {MustExistsOnDatabaseValidator.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface MustExistsOnDatabase {

    String message() default "Valor não existe no banco!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String fieldName();
    boolean isOptionalAttribute();
    Class<?> domainClass();

}
