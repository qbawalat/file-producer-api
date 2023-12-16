package pl.qbawalat.file.relations.resolver.api.file.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;
import pl.qbawalat.file.relations.resolver.api.file.validator.MultipartFileValidator;

@Documented
@Constraint(validatedBy = MultipartFileValidator.class)
@Target({ElementType.TYPE_USE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileTypeConstraint {
    String message() default "Unsupported file type.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
