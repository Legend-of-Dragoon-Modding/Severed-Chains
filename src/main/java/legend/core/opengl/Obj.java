package legend.core.opengl;

import legend.game.types.Translucency;

import javax.annotation.Nullable;

public interface Obj {
  boolean shouldRender(@Nullable final Translucency translucency);
  void render(@Nullable final Translucency translucency);
  void delete();
}
