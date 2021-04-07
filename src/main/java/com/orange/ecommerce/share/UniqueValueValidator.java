package com.orange.ecommerce.share;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UniqueValueValidator implements ConstraintValidator<UniqueValue, Object> {

    @PersistenceContext
    private EntityManager manager;

    private String fieldName;
    private Class<?> domainClass;

    @Override
    public void initialize(UniqueValue params) {
        fieldName = params.fieldName();
        domainClass = params.domainClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Query query = manager.createQuery(String.format("SELECT 1 FROM %s WHERE %s = LOWER(:value)", domainClass.getName(), fieldName));
        query.setParameter("value", value);
        List<?> resultList = query.getResultList();

        return resultList.isEmpty();
    }
}
