package legend.game.combat;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.net.BattleAction;
import legend.game.net.NetClient;
import legend.game.scripting.ScriptState;

import static legend.core.GameEngine.SCRIPTS;

public class ClientBattleController implements BattleController {
  private final NetClient client;
  private final int charSlot;

  private ScriptState<? extends BattleEntity27c> currentBent;

  /** Actions sent from another client */
  private BattleAction action;

  public ClientBattleController(final NetClient client, final int charSlot) {
    this.client = client;
    this.charSlot = charSlot;
  }

  public void startTurn(final int bentId) {
    this.currentBent = SCRIPTS.getState(bentId, BattleEntity27c.class);
    System.out.println("Starting turn " + this.currentBent.name);
  }

  public void handleAction(final BattleAction action) {
    this.action = action;
  }

  @Override
  public void startTurn(final ScriptState<? extends BattleEntity27c> bent) {

  }

  @Override
  public void action(final BattleAction action) {

  }

  @Override
  public boolean canTakeAction() {
    return this.currentBent != null && this.currentBent.innerStruct_00.charSlot_276 == this.charSlot;
  }

  @Override
  public BattleAction getAction() {
    return this.action;
  }
}
