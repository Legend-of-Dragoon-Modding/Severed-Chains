package legend.game.scripting;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ScriptEnums.class)
public @interface ScriptEnum {
  Class<? extends Enum<?>> value();
}
