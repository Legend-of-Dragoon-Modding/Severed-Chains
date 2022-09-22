package legend.core.memory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Method {
  long value();
  boolean ignoreExtraParams() default false;
}
