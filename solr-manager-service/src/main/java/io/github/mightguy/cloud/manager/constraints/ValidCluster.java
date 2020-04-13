
package io.github.mightguy.cloud.manager.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.github.mightguy.cloud.manager.config.LightningContext;
import io.github.mightguy.cloud.manager.constraints.ValidCluster.ClusterValidator;
import io.github.mightguy.cloud.manager.util.Constants;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.apache.solr.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * ValidCollectionName annotation for validation of Collection name/ Each custom annotation must
 * have message, groups, payload and targetField methods like in this annotation.
 */
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ClusterValidator.class)
public @interface ValidCluster {

  String message() default Constants.INVALID_CLUSTER;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * ClusterValidator class responsible for validating CollectionName.
   */
  public static class ClusterValidator implements
      ConstraintValidator<ValidCluster, String> {

    @Autowired
    private LightningContext lightningContext;

    @Override
    public final boolean isValid(String value, ConstraintValidatorContext context) {
      if (isValidCluster(value)) {
        lightningContext.setDummyActiveCluster(value);
        lightningContext.reload(value);
        return true;
      }
      return false;
    }

    private boolean isValidCluster(String value) {
      return !StringUtils.isEmpty(value) && lightningContext.getAppConfig().getClusters()
          .containsKey(value);
    }
  }

}
