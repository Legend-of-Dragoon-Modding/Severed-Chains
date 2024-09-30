package legend.lodmod.items;

import legend.game.Scus94491BpeSegment_8002;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.ScriptState;

import java.util.function.BiFunction;

public class SunRhapsodyItem extends RecoverHpItem {
  public SunRhapsodyItem() {
    super(34, 25, false, 100);
  }

  @Override
  protected BiFunction<Integer, Integer, Integer> getRecoveryMethod() {
    return Scus94491BpeSegment_8002::addMp;
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 2;
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.storage_44[8] = 0xfadf82; // Colour
    user.storage_44[28] = targetBentIndex;
    user.storage_44[30] = user.index;
  }
}
