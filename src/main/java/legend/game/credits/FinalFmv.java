package legend.game.credits;

import de.jcm.discordgamesdk.activity.Activity;
import legend.core.memory.Method;
import legend.game.EngineState;
import legend.game.fmv.Fmv;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import legend.lodmod.LodMod;

/** Plays the final cutscene tying everything up */
public class FinalFmv extends EngineState<FinalFmv> {
  private int ticks;

  public FinalFmv() {
    super(LodMod.FINAL_FMV_STATE_TYPE.get());
  }

  @Override
  public String getLocationForSave() {
    return "Final FMV";
  }

  @Override
  public FileData writeSaveData() {
    return null;
  }

  @Override
  public boolean advancesTime() {
    return false;
  }

  @Override
  @Method(0x800d9e08L)
  public void tick() {
    super.tick();

    if(this.ticks++ > 94) {
      Fmv.playCurrentFmv(17, LodMod.CREDITS_STATE_TYPE.get());
    }
  }

  @Override
  public void updateDiscordRichPresence(final GameState52c gameState, final Activity activity) {
    super.updateDiscordRichPresence(gameState, activity);
    activity.setState("Final Cutscene");
  }
}
