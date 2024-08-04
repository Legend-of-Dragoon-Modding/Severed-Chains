package legend.game.combat.effects;

import legend.game.scripting.ScriptState;

public interface Effect<T extends EffectManagerParams<T>> {
  void tick(final ScriptState<EffectManagerData6c<T>> state);
  void render(final ScriptState<EffectManagerData6c<T>> state);
  void destroy(final ScriptState<EffectManagerData6c<T>> state);
}
