package io.github.mightguy.cloud.manager.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.github.mightguy.cloud.manager.constraints.ValidValue.ValidateValue;
import io.github.mightguy.cloud.manager.model.request.ClusterInitializationType;
import io.github.mightguy.cloud.manager.model.request.InitializationRequestDetails;
import io.github.mightguy.cloud.manager.util.Constants;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.springframework.util.StringUtils;

/**
 * ValidUser annotation for validation of user name/password Each custom annotation must have
 * message, groups, payload and targetField methods like in this annotation.
 */

@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ValidateValue.class)
public @interface ValidInitializationPayload {

  String message() default Constants.INVALID_VALUE;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * ClusterValidator class responsible for validating CollectionName.
   */
  public static class ValidateValue implements
      ConstraintValidator<ValidInitializationPayload, InitializationRequestDetails> {


    @Override
    public final boolean isValid(InitializationRequestDetails payload,
        ConstraintValidatorContext context) {
      return ((payload.getType().equals(ClusterInitializationType.GIT)
          && (payload.getGithubDetails() == null
          || StringUtils.isEmpty(payload.getGithubDetails().getGithubProjectName())
          || StringUtils.isEmpty(payload.getGithubDetails().getGithubPassword())
          || StringUtils.isEmpty(payload.getGithubDetails().getGithubUsername())
          || StringUtils.isEmpty(payload.getGithubDetails().getGithubRepoURL())))
          ||
          ((payload.getType().equals(ClusterInitializationType.LOCAL)) && (
              payload.getLocalDetails() == null
                  || StringUtils.isEmpty(payload.getLocalDetails().getLocalFilePath())
          )));
    }

  }
}
