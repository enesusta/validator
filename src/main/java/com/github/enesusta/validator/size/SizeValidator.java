package com.github.enesusta.validator.size;

import com.github.enesusta.validator.core.FieldValidator;
import com.github.enesusta.validator.core.Validator;

import java.lang.reflect.Field;

public class SizeValidator implements Validator {

    @Override
    public final boolean isValid(final Object object) throws IllegalAccessException {

        final Class<?> clazz = object.getClass();
        final Field[] fields = clazz.getDeclaredFields();
        final FieldValidator fieldValidator = new SizeFieldValidator(object);
        boolean hasAnyFalse = false;

        for (Field field : fields)
            if (isAnnotationPresentWithSizeAnnotation(field))
                if (!fieldValidator.isFieldValid(field))
                    hasAnyFalse = true;

        return !hasAnyFalse;
    }
}