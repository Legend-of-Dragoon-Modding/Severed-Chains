package legend.game.credits;

import de.jcm.discordgamesdk.activity.Activity;
import legend.core.memory.Method;
import legend.game.EngineState;
import legend.game.fmv.Fmv;
import legend.game.types.GsRVIEW2;
import legend.game.unpacker.FileData;
import legend.lodmod.LodEngineStateTypes;

/** Plays the final cutscene tying everything up */
public class FinalFmv extends EngineState<FinalFmv> {
  private int ticks;

  public FinalFmv() {
    super(LodEngineStateTypes.FINAL_FMV.get());
  }

  @Override
  public FileData writeSaveData() {
    return null;
  }

  @Override
  public void readSaveData(final FileData data) {

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
      Fmv.playCurrentFmv(17, LodEngineStateTypes.CREDITS.get());
    }
  }

  @Override
  public void updateDiscordRichPresence(final Activity activity) {
    super.updateDiscordRichPresence(activity);
    activity.setState("Final Cutscene");
  }

  @Override
  public GsRVIEW2 getCamera() {
    return null;
  }
}
