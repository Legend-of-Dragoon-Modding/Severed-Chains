package legend.game.scripting;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ScriptParams.class)
public @interface ScriptParam {
  Direction direction();
  Type type();
  String name();
  String description() default "";
  /** If true, this param causes a branch in script execution */
  Branch branch() default Branch.NONE;

  enum Direction {
    IN,
    OUT,
    BOTH,
  }

  enum Type {
    INT,
    INT_ARRAY,
    BOOL,
    BOOL_ARRAY,
    /** Lower 5 bits are what bit to set (i.e. 1 << n), upper 3 bits is the index into flags array */
    FLAG,
    /** Lower 5 bits are what bit to set (i.e. 1 << n), upper 3 bits is the index into flags array */
    FLAG_ARRAY,
    STRING,
    REG,
    ENUM,
    ANY,
  }

  enum Branch {
    NONE,
    JUMP,
    SUBROUTINE,
    REENTRY,
    FORK_JUMP,
    FORK_REENTRY,
  }
}
