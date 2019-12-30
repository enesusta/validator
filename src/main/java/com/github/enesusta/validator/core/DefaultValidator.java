package com.github.enesusta.validator.core;

import com.github.enesusta.validator.email.EmailFieldValidator;
import com.github.enesusta.validator.max.MaxFieldValidator;
import com.github.enesusta.validator.min.MinFieldValidator;
import com.github.enesusta.validator.nonnull.NonNullFieldValidator;
import com.github.enesusta.validator.negative.NegativeFieldValidator;
import com.github.enesusta.validator.positive.PositiveFieldValidator;
import com.github.enesusta.validator.size.SizeFieldValidator;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class DefaultValidator implements Validator {


    @Override
    public final boolean isValid(final Object object) throws IllegalAccessException {

        final Class<?> clazz = object.getClass();
        final Field[] fields = clazz.getDeclaredFields();

        final FieldValidator nullValidator = new NonNullFieldValidator(object);
        final FieldValidator positiveValidator = new PositiveFieldValidator(object);
        final FieldValidator negativeValidator = new NegativeFieldValidator(object);
        final FieldValidator sizeValidator = new SizeFieldValidator(object);
        final FieldValidator maxValidator = new MaxFieldValidator(object);
        final FieldValidator minValidator = new MinFieldValidator(object);
        final FieldValidator emailValidator = new EmailFieldValidator(object);


        boolean[] valid = prepareValidationArray();
        boolean[] nullBooleans = prepareValidationArray();
        boolean[] positiveBooleans = prepareValidationArray();
        boolean[] negativeBooleans = prepareValidationArray();
        boolean[] sizeBooleans = prepareValidationArray();
        boolean[] maxBooleans = prepareValidationArray();
        boolean[] minBooleans = prepareValidationArray();
        boolean[] emailBooleans = prepareValidationArray();


        byte counter = (byte) 0;
        for (Field field : fields) {

            field.setAccessible(true);
            if (isAnnotationPresentWithNonNullAnnotation(field))
                nullBooleans[counter] = nullValidator.isFieldValid(field);
            if (isAnnotationPresentWithPositiveAnnotation(field))
                positiveBooleans[counter] = positiveValidator.isFieldValid(field);
            if (isAnnotationPresentWithNegativeAnnotation(field))
                negativeBooleans[counter] = negativeValidator.isFieldValid(field);
            if (isAnnotationPresentWithSizeAnnotation(field))
                sizeBooleans[counter] = sizeValidator.isFieldValid(field);
            if (isAnnotationPresentWithMaxAnnotation(field))
                maxBooleans[counter] = maxValidator.isFieldValid(field);
            if (isAnnotationPresentWithMinAnnotation(field))
                minBooleans[counter] = minValidator.isFieldValid(field);
            if (isAnnotationPresentWithEmailAnnotation(field))
                emailBooleans[counter] = emailValidator.isFieldValid(field);

            counter++;
        }

        final Queue<Callable<Boolean>> callableQueue = new PriorityQueue<>();

        final Callable<Boolean> nullBooleansCallable = hasAny(nullBooleans);
        final Callable<Boolean> positiveBooleansCallable = hasAny(positiveBooleans);
        final Callable<Boolean> negativeBooleansCallable = hasAny(negativeBooleans);
        final Callable<Boolean> sizeBooleansCallable = hasAny(sizeBooleans);
        final Callable<Boolean> maxBooleansCallable = hasAny(maxBooleans);
        final Callable<Boolean> minBooleansCallable = hasAny(minBooleans);
        final Callable<Boolean> emailBooleansCallable = hasAny(emailBooleans);

        callableQueue.offer(nullBooleansCallable);
        callableQueue.offer(positiveBooleansCallable);
        callableQueue.offer(negativeBooleansCallable);
        callableQueue.offer(sizeBooleansCallable);
        callableQueue.offer(maxBooleansCallable);
        callableQueue.offer(minBooleansCallable);
        callableQueue.offer(emailBooleansCallable);

        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        final ExecutorService service = Executors.newFixedThreadPool(availableProcessors);
        List<Future<Boolean>> futureList = null;

        try {
            futureList = service.invokeAll(callableQueue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        service.shutdown();



        /** valid[0] = hasAnyFalse(nullBooleans);
         valid[1] = hasAnyFalse(positiveBooleans);
         valid[2] = hasAnyFalse(negativeBooleans);
         valid[3] = hasAnyFalse(sizeBooleans);
         valid[4] = hasAnyFalse(maxBooleans);
         valid[5] = hasAnyFalse(minBooleans);
         valid[6] = hasAnyFalse(emailBooleans);
         */
        return hasAnyFalse(valid);
    }

    private boolean[] prepareValidationArray() {
        boolean[] booleans = new boolean[10];
        Arrays.fill(booleans, true);
        return booleans;
    }


    private boolean hasAnyFalse(final boolean[] booleans) {
        boolean valid = true;
        for (boolean aBoolean : booleans) {
            if (!aBoolean) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    private Callable<Boolean> hasAny(final boolean[] booleans) {
        return () -> {
            boolean valid = true;
            for (final boolean aBoolean : booleans) {
                if (!aBoolean) {
                    valid = false;
                    break;
                }
            }
            return valid;
        };
    }
}
