package legend.game.combat;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.net.NetClient;
import legend.game.scripting.ScriptState;

import static legend.core.GameEngine.SCRIPTS;

public class ClientBattleController implements BattleController {
  private final NetClient client;
  private final int charSlot;

  private ScriptState<? extends BattleEntity27c> currentBent;

  public ClientBattleController(final NetClient client, final int charSlot) {
    this.client = client;
    this.charSlot = charSlot;
  }

  public void startTurn(final int bentId) {
    this.currentBent = SCRIPTS.getState(bentId, BattleEntity27c.class);
    System.out.println("Starting turn " + this.currentBent.name);
  }

  @Override
  public void startTurn(final ScriptState<? extends BattleEntity27c> bent) {

  }

  @Override
  public boolean canTakeAction() {
    return this.currentBent.innerStruct_00.charSlot_276 == this.charSlot;
  }
}
