package com.github.enesusta.validator.email;

import com.github.enesusta.validator.core.FieldValidator;
import com.github.enesusta.validator.core.Validator;

import java.lang.reflect.Field;

public class EmailValidator implements Validator {

    @Override
    public final boolean isValid(final Object object) throws IllegalAccessException {

        final Class<?> clazz = object.getClass();
        final Field[] fields = clazz.getDeclaredFields();
        final FieldValidator fieldValidator = new EmailFieldValidator(object);
        boolean hasAnyFalse = false;

        for (Field field : fields)
            if (isAnnotationPresentWithEmailAnnotation(field))
                if (!fieldValidator.isFieldValid(field))
                    hasAnyFalse = true;

        return !hasAnyFalse;
    }
}