package legend.core.opengl;

import legend.game.types.Translucency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class Obj {
  private static final Logger LOGGER = LogManager.getFormatterLogger();

  protected static final List<Obj> objList = new ArrayList<>();
  private final String name;

  public Obj(final String name) {
    this.name = name;
    objList.add(this);
  }

  public static void reportLeaks() {
    if(!Obj.objList.isEmpty()) {
      for(final Obj obj : Obj.objList) {
        LOGGER.info(obj.name);
      }
    }
  }

  public abstract boolean shouldRender(@Nullable final Translucency translucency);
  public abstract void render(@Nullable final Translucency translucency);
  public abstract void delete();
}
