
package io.github.mightguy.cloud.manager.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.github.mightguy.cloud.manager.config.LightningContext;
import io.github.mightguy.cloud.manager.constraints.ValidCollectionName.CollectionValidator;
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
import org.springframework.util.CollectionUtils;


/**
 * ValidCollectionName annotation for validation of Collection name/ Each custom annotation must
 * have message, groups, payload and targetField methods like in this annotation.
 */
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CollectionValidator.class)
public @interface ValidCollectionName {

  String message() default Constants.INVALID_COLLECTION;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * CollectionValidator class responsible for validating CollectionName.
   */
  public static class CollectionValidator implements
      ConstraintValidator<ValidCollectionName, String> {

    @Autowired
    private LightningContext lightningContext;

    @Override
    public final boolean isValid(String value, ConstraintValidatorContext context) {
      return isValidCollection(value);
    }

    private boolean isValidCollection(String value) {
      return !StringUtils.isEmpty(value)
          && !CollectionUtils.isEmpty(lightningContext.getCollectionListClusterMap()
              .get(lightningContext.getDummyActiveCluster())) && lightningContext
          .getCollectionListClusterMap().get(lightningContext.getDummyActiveCluster())
          .contains(value);
    }
  }

}
