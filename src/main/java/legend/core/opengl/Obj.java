package legend.core.opengl;

import legend.game.types.Translucency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class Obj {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Obj.class);
  private static boolean shouldLog = true;

  private static final List<Obj> objList = new ArrayList<>();
  public final String name;
  protected boolean deleted;
  /** This Obj won't be deleted on state transition */
  public boolean persistent;

  public static void setShouldLog(final boolean shouldLog) {
    Obj.shouldLog = shouldLog;
  }

  public Obj(final String name) {
    this.name = name;
    objList.add(this);
  }

  public void delete() {
    this.deleted = true;
  }

  protected abstract void performDelete();

  public static void deleteObjects() {
    for(int i = objList.size() - 1; i >= 0; i--) {
      final Obj obj = objList.get(i);

      if(obj.deleted) {
        obj.performDelete();
        objList.remove(i);
      }
    }
  }

  public static void clearObjList(final boolean clearPersistent) {
    for(int i = objList.size() - 1; i >= 0; i--) {
      final Obj obj = objList.get(i);

      if(!obj.persistent || clearPersistent) {
        if(shouldLog) {
          LOGGER.warn("Leaked: %s", obj.name);
        }

        obj.delete();
        objList.remove(i);
      }
    }
  }

  public boolean useBackfaceCulling() {
    return true;
  }

  public abstract boolean hasTexture();
  public abstract boolean hasTexture(final int index);
  public abstract boolean hasTranslucency();
  public abstract boolean hasTranslucency(final int index);
  public abstract boolean shouldRender(@Nullable final Translucency translucency);
  public abstract boolean shouldRender(@Nullable final Translucency translucency, final int layer);
  public abstract int getLayers();
  public abstract void render(final int layer, final int startVertex, final int vertexCount);
  public abstract void render(@Nullable final Translucency translucency, final int layer, final int startVertex, final int vertexCount);

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ' ' + this.name;
  }
}
