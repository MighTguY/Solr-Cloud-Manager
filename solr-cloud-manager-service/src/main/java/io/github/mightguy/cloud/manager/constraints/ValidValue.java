/*
 * Copyright (c) 2018 Walmart Co. All rights reserved.
 */

package io.github.mightguy.cloud.manager.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.github.mightguy.cloud.manager.constraints.ValidValue.ValidateValue;
import io.github.mightguy.cloud.manager.util.Constants;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.apache.solr.common.StringUtils;

/**
 * ValidUser annotation for validation of user name/password Each custom annotation must have
 * message, groups, payload and targetField methods like in this annotation.
 */

@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ValidateValue.class)
public @interface ValidValue {

  String message() default Constants.INVALID_VALUE;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * ClusterValidator class responsible for validating CollectionName.
   */
  public static class ValidateValue implements
      ConstraintValidator<ValidValue, String> {


    @Override
    public final boolean isValid(String value, ConstraintValidatorContext context) {
      return isValidUser(value);
    }

    private boolean isValidUser(String value) {
      return !StringUtils.isEmpty(value);
    }
  }
}
