package legend.lodmod.items;

import legend.game.characters.Element;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.Item;
import legend.game.scripting.FlowControl;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptStackFrame;
import legend.game.scripting.ScriptState;
import legend.game.unpacker.Unpacker;

import java.io.IOException;
import java.nio.file.Path;

import static legend.core.GameEngine.SCRIPTS;

public class AttackItem extends Item {
  private final boolean targetAll;
  private final Element element;
  private final int damageMultiplier;
  private int loadingStage;

  public AttackItem(final int icon, final int price, final boolean targetAll, final Element element, final int damageMultiplier) {
    super(icon, price);
    this.targetAll = targetAll;
    this.element = element;
    this.damageMultiplier = damageMultiplier;
  }

  @Override
  public boolean canBeUsed(final UsageLocation location) {
    return location == UsageLocation.BATTLE;
  }

  @Override
  public boolean canTarget(final TargetType type) {
    return type == TargetType.ENEMIES || type == TargetType.ALL && this.targetAll;
  }

  @Override
  public Element getAttackElement() {
    return this.element;
  }

  @Override
  public int getAttackDamageMultiplier(final BattleEntity27c user, final BattleEntity27c target) {
    return this.damageMultiplier;
  }

  @Override
  public FlowControl useInBattle(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    return switch(this.loadingStage) {
      // Initial load
      case 0 -> {
        this.loadingStage = 1;
        final Path path = Path.of("./patches/scripts/use_attack_item.txt");

        Unpacker.loadFile(path, data -> {
          try {
            final String source = data.readFixedLengthAscii(0, data.size());
            final byte[] compiled = SCRIPTS.compile(path, source);
            final ScriptFile file = new ScriptFile("use_attack_item", compiled);

            user.storage_44[28] = targetBentIndex;
            user.storage_44[29] = 3;
            user.storage_44[30] = user.index;
            user.pushFrame(new ScriptStackFrame(file, file.getEntry(1)));
            user.context.commandOffset_0c = user.frame().offset;
            this.loadingStage = 2;
          } catch(final IOException e) {
            throw new RuntimeException("Failed to load use_attack_item", e);
          }
        });

        yield FlowControl.PAUSE_AND_REWIND;
      }

      // Wait for load
      case 1 -> FlowControl.PAUSE_AND_REWIND;

      // Loaded, carry on
      default -> {
        this.loadingStage = 0;
        yield FlowControl.CONTINUE;
      }
    };
  }
}
