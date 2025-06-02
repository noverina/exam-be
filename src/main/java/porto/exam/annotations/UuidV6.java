package porto.exam.annotations;

import org.hibernate.annotations.IdGeneratorType;
import porto.exam.utils.UuidV6Generator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(FIELD)
@IdGeneratorType(UuidV6Generator.class)
public @interface UuidV6 {
}
