package com.github.enesusta.validator.fals;

import com.github.enesusta.validator.core.FieldValidator;

import java.lang.reflect.Field;

public final class FalseFieldValidator implements FieldValidator {

    private final Object object;

    public FalseFieldValidator(final Object object) {
        this.object = object;
    }

    @Override
    public final boolean isFieldValid(final Field field) throws IllegalAccessException {
        field.setAccessible(true);
        return !((boolean) field.get(object));
    }
}