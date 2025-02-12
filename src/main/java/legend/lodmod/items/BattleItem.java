package legend.lodmod.items;

import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.deff.DeffPackage;
import legend.game.combat.effects.ScriptDeffManualLoadingEffect;
import legend.game.inventory.Item;
import legend.game.scripting.FlowControl;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptStackFrame;
import legend.game.scripting.ScriptState;
import legend.game.unpacker.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.nio.file.Path;

import static legend.core.GameEngine.REGISTRIES;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.combat.Battle.deffManager_800c693c;

/** Convenience class for easily using vanilla item effects */
public abstract class BattleItem extends Item {
  private static final Logger LOGGER = LogManager.getFormatterLogger(BattleItem.class);

  private int deffLoadingStage;

  public BattleItem(final int icon, final int price) {
    super(icon, price);
  }

  @Override
  public FlowControl useInBattle(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    return switch(this.deffLoadingStage) {
      // Initial load
      case 0 -> {
        this.deffLoadingStage = 1;
        this.loadDeff(user, this.getDeffEntrypoint(), this.getDeffParam(targetBentIndex));

        this.injectScript(user, this.getUseItemScriptPath(), this.getUseItemScriptEntrypoint(), () -> {
          this.useItemScriptLoaded(user, targetBentIndex);
          this.deffLoadingStage = 2;
        });

        yield FlowControl.PAUSE_AND_REWIND;
      }

      // Wait for load
      case 1 -> FlowControl.PAUSE_AND_REWIND;

      // Loaded, carry on
      default -> {
        this.deffLoadingStage = 0;
        yield FlowControl.CONTINUE;
      }
    };
  }

  protected Path getUseItemScriptPath() {
    return Loader.resolve("throw_item");
  }

  protected int getUseItemScriptEntrypoint() {
    return 0;
  }

  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {

  }

  protected int getDeffEntrypoint() {
    return 0;
  }

  protected int getDeffParam(final int targetBentIndex) {
    return targetBentIndex;
  }

  protected RegistryId getDeffId() {
    return this.getRegistryId();
  }

  protected void loadDeff(final ScriptState<? extends BattleEntity27c> user, final int entrypoint, final int param) {
    LOGGER.info("%s loading deff %s", this, this.getDeffId());

    final RegistryDelegate<DeffPackage> deff = REGISTRIES.deff.getEntry(this.getDeffId());

    if(!deff.isValid()) {
      throw new RuntimeException(this + " trying to load non-existent DEFF " + this.getDeffId());
    }

    ((Battle)currentEngineState_8004dd04).allocateDeffEffectManager(user, 0, user.index, param, entrypoint, new ScriptDeffManualLoadingEffect());
    deff.get().load();

    deffManager_800c693c.flags_20 |= 0x40_0000;
  }

  protected void injectScript(final ScriptState<? extends BattleEntity27c> user, final Path path, final int entrypoint, final Runnable onLoad) {
    Loader.loadFile(path, data -> {
      final ScriptFile file = new ScriptFile("throw_item", data.getBytes());
      user.pushFrame(new ScriptStackFrame(file, file.getEntry(entrypoint)));
      user.context.commandOffset_0c = user.frame().offset;
      onLoad.run();
    });
  }
}
