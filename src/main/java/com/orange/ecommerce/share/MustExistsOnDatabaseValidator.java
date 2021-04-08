package com.orange.ecommerce.share;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

import static java.util.Objects.isNull;

public class MustExistsOnDatabaseValidator implements ConstraintValidator<MustExistsOnDatabase, Object> {

    @PersistenceContext
    private EntityManager manager;

    private String fieldName;
    private boolean isOptionalAttribute;
    private Class<?> domainClass;

    @Override
    public void initialize(MustExistsOnDatabase params) {
        fieldName = params.fieldName();
        isOptionalAttribute = params.isOptionalAttribute();
        domainClass = params.domainClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if(isOptionalAttribute && isNull(value)) return true;

        Query query = manager.createQuery(String.format("SELECT 1 FROM %s WHERE LOWER(%s) = LOWER(:value)", domainClass.getName(), fieldName));
        query.setParameter("value", value);
        Optional<?> result = query.getResultList().stream().findFirst();

        return result.isPresent();
    }
}
