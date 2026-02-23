package legend.turnorder;

import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.ui.BattleMenuStruct58;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.events.RenderEvent;
import legend.game.modding.events.battle.BattleEndedEvent;
import legend.game.modding.events.battle.BattleEntityTurnEvent;
import legend.game.modding.events.gamestate.GameLoadedEvent;
import legend.game.saves.ConfigRegistryEvent;
import legend.game.scripting.ScriptState;
import legend.game.types.Renderable58;
import legend.game.ui.UiBox;
import legend.lodmod.LodMod;
import org.legendofdragoon.modloader.Mod;
import org.legendofdragoon.modloader.events.EventListener;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.DEFAULT_FONT;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.RENDERER;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.Menus.uploadRenderable;
import static legend.game.SItem.renderManualCharacterPortrait;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_8004.simpleRandSeed_8004dd44;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Text.renderText;
import static legend.game.Text.textZ_800bdf00;
import static legend.game.combat.bent.BattleEntity27c.FLAG_400;
import static legend.game.combat.bent.BattleEntity27c.FLAG_CURRENT_TURN;
import static legend.turnorder.TurnOrderConfigs.SHOW_TURN_ORDER;

@Mod(id = TurnOrderMod.MOD_ID, version = "^3.0.0")
public class TurnOrderMod {
  public static final String MOD_ID = "turn_order";

  public static RegistryId id(final String entryId) {
    return new RegistryId(MOD_ID, entryId);
  }

  private static final FontOptions FONT = new FontOptions().size(0.45f);

  private UiBox background;
  private boolean visible;

  private final List<TurnOrder> sortedBents = new ArrayList<>();
  private final List<TurnOrder> turns = new ArrayList<>();

  public TurnOrderMod() {
    EVENTS.register(this);
  }

  @EventListener
  public void onRegisterConfig(final ConfigRegistryEvent event) {
    TurnOrderConfigs.register(event);
  }

  @EventListener
  public void onGameLoaded(final GameLoadedEvent event) {
    this.background = new UiBox();
  }

  @EventListener
  public void battleEndedEvent(final BattleEndedEvent event) {
    this.visible = false;
  }

  @EventListener
  public void onTurnStarted(final BattleEntityTurnEvent<?> event) {
    this.visible = true;

    this.sortedBents.clear();
    this.turns.clear();
    int processedBents = 0;

    for(int bentIndex = 0; bentIndex < battleState_8006e398.getAliveBentCount(); bentIndex++) {
      this.turns.add(new TurnOrder(battleState_8006e398.aliveBents_e78.get(bentIndex).innerStruct_00));
    }

    final int oldSeed = simpleRandSeed_8004dd44;

    while(!this.turns.isEmpty() && processedBents < 6) {
      int highestTurnValue = 0;
      int highestIndex = 0;
      for(int i = 0; i < this.turns.size(); i++) {
        final TurnOrder turnOrder = this.turns.get(i);
        final int turnValue = turnOrder.turnValue;

        if(highestTurnValue <= turnValue) {
          highestTurnValue = turnValue;
          highestIndex = i;
        }
      }

      if(highestTurnValue > 0xd9) {
        final TurnOrder turnOrder = this.turns.get(highestIndex);
        turnOrder.turnValue -= 0xd9;
        this.sortedBents.add(turnOrder);
        processedBents++;
      }

      for(int i = 0; i < this.turns.size(); i++) {
        final TurnOrder turnOrder = this.turns.get(i);
        turnOrder.turnValue += Math.round(turnOrder.bent.stats.getStat(LodMod.SPEED_STAT.get()).get() * (simpleRand() / (float)0xffff * 0.2f + 0.9f));
      }
    }

    simpleRandSeed_8004dd44 = oldSeed;

    for(int bentIndex = 0; bentIndex < battleState_8006e398.getAliveBentCount(); bentIndex++) {
      final ScriptState<? extends BattleEntity27c> state = battleState_8006e398.aliveBents_e78.get(bentIndex);

      if(state.hasAnyFlag(FLAG_400 | FLAG_CURRENT_TURN)) {
        this.sortedBents.addFirst(new TurnOrder(state.innerStruct_00));
      }
    }

    if(battleState_8006e398.getForcedTurnBent() != null) {
      this.sortedBents.addFirst(TurnOrder.interrupt(battleState_8006e398.getForcedTurnBent().innerStruct_00));
    }

    final float x = 5.5f;
    float longestName = 0.0f;
    for(int bentIndex = 0; bentIndex < this.sortedBents.size(); bentIndex++) {
      final TurnOrder turn = this.sortedBents.get(bentIndex);
      final float textWidth = DEFAULT_FONT.textWidth(turn.bent.getName()) * FONT.getSize();
      longestName = Math.max(textWidth, longestName);
    }

    final float boxWidth = Math.max(longestName + x + 15.0f, 50.0f);
    this.background.setSize(Math.round(boxWidth), 67);
  }

  @EventListener
  public void onRender(final RenderEvent event) {
    if(this.visible && currentEngineState_8004dd04 instanceof final Battle battle && !battle.isBattleDisabled() && CONFIG.getConfig(SHOW_TURN_ORDER.get())) {
      final float xOffset = RENDERER.getWidescreenOrthoOffsetX();
      final float x = 5.5f;

      final int oldZ = textZ_800bdf00;
      textZ_800bdf00 = 30;

      for(int bentIndex = 0; bentIndex < this.sortedBents.size(); bentIndex++) {
        final TurnOrder turn = this.sortedBents.get(bentIndex);

        final BattleMenuStruct58 menu = battle.hud.battleMenu_800c6c34;
        boolean targeted = false;

        if(menu.displayTargetArrowAndName_4c) {
          if(menu.combatantIndex_54 == -1) { // target all
            targeted = (menu.targetType_50 == 0 && turn.bent instanceof PlayerBattleEntity) || (menu.targetType_50 == 1 && turn.bent instanceof MonsterBattleEntity);
          } else if(menu.targetType_50 == 0) { // player
            targeted = turn.bent == battleState_8006e398.playerBents_e40.get(menu.combatantIndex_54).innerStruct_00;
          } else if(menu.targetType_50 == 1) { // monster
            targeted = turn.bent == battleState_8006e398.aliveMonsterBents_ebc.get(menu.combatantIndex_54).innerStruct_00;
          }
        }

        if(turn.interrupt) {
          FONT.colour(TextColour.RED);
        } else if(targeted) {
          FONT.colour(TextColour.YELLOW);
        } else if(bentIndex == 0) {
          FONT.colour(TextColour.WHITE);
        } else {
          FONT.colour(TextColour.LIGHT_GREY);
        }

        final float y = 14.0f + bentIndex * 8.0f;
        renderText(DEFAULT_FONT, turn.bent.getName(), x + 9.5f - xOffset, y, FONT);

        if(turn.bent instanceof final PlayerBattleEntity player) {
          final Renderable58 portrait = renderManualCharacterPortrait(player.charId_272, (int)(x - xOffset) + 5, (int)y - 6, 0);
          portrait.clut_30 = (500 + player.charId_272 & 0x1ff) << 6 | 0x2b;
          portrait.z_3c = 30.0f;
          portrait.widthScale = 0.45f;
          portrait.heightScale_38 = 0.45f;
          uploadRenderable(portrait, 0, 0);
        } else {
          final Renderable58 skull = ItemIcon.SKULL.renderManual((int)(x - xOffset) + 5, (int)y - 6, 0);
          skull.z_3c = 30.0f;
          skull.widthScale = 0.45f;
          skull.heightScale_38 = 0.45f;
          uploadRenderable(skull, 0, 0);
        }
      }

      this.background.setPos(Math.round(x - 2 - xOffset), 4);
      this.background.render();
      FONT.colour(TextColour.YELLOW);
      renderText("Turn Order", x + 1 - xOffset, 6, FONT);

      textZ_800bdf00 = oldZ;
    }
  }
}
