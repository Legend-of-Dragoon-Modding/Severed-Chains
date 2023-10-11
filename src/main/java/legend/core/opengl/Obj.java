package legend.core.opengl;

import legend.game.types.Translucency;

import javax.annotation.Nullable;

public interface Obj {
  void render(@Nullable final Translucency translucency);
}
