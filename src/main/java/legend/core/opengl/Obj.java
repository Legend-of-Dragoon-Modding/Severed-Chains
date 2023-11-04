package legend.core.opengl;

import legend.game.types.Translucency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class Obj {
  private static final Logger LOGGER = LogManager.getFormatterLogger();
  private static boolean shouldLog = true;

  private static final List<Obj> objList = new ArrayList<>();
  private final String name;
  protected boolean deleted;

  public static void setShouldLog(final boolean shouldLog) {
    Obj.shouldLog = shouldLog;
  }

  public Obj(final String name) {
    this.name = name;
    objList.add(this);
  }

  public void delete() {
    this.deleted = true;
    objList.remove(this);
  };

  public static void clearObjList() {
    for(int i = objList.size() - 1; i >= 0; i--) {
      if(shouldLog) {
        LOGGER.warn("Leaked: %s", objList.get(i).name);
      }
      objList.get(i).delete();
    }
  }

  public abstract boolean shouldRender(@Nullable final Translucency translucency);
  public abstract void render(@Nullable final Translucency translucency);
}
