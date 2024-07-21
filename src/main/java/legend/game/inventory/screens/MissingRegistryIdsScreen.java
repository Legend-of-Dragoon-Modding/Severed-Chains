package legend.game.inventory.screens;

import legend.game.SItem;
import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.ListBox;
import legend.game.types.MessageBoxResult;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;

public class MissingRegistryIdsScreen extends MenuScreen {
  private final String prompt;

  public MissingRegistryIdsScreen(final Map<RegistryId, Set<RegistryId>> missingIds, final Consumer<MessageBoxResult> onResult) {
    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.prompt = "Your game is missing data required for this save. It may have been created with different mods, or the mods may have changed. Are you sure you want to load this save?";

    this.addControl(new Background());

    final ListBox<String> list = this.addControl(new ListBox<>(String::toString, null, null, null));
    list.setPos(20, 95);
    list.setSize(308, 105);
    this.setFocus(list);

    missingIds.keySet().stream().sorted((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.toString(), o2.toString())).forEach(registryId -> {
      list.add(registryId.toString());

      missingIds.get(registryId).stream().sorted((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.toString(), o2.toString())).forEach(entryId -> {
        list.add("  " + entryId);
      });
    });

    final Button yes = this.setDefaultButton(InputAction.BUTTON_SOUTH, this.addControl(new Button("Yes")));
    yes.setPos(199, 210);
    yes.onPressed(() -> {
      startFadeEffect(2, 10);
      playMenuSound(1);
      this.deferAction(() -> {
        this.getStack().popScreen();
        onResult.accept(MessageBoxResult.YES);
      });
    });

    final Button no = this.setDefaultButton(InputAction.BUTTON_EAST, this.addControl(new Button("No")));
    no.setPos(109, 210);
    no.onPressed(() -> {
      startFadeEffect(2, 10);
      playMenuSound(3);
      this.deferAction(() -> {
        this.getStack().popScreen();
        onResult.accept(MessageBoxResult.NO);
      });
    });
  }

  @Override
  protected void render() {
    SItem.renderCentredText(this.prompt, 188, 25, TextColour.BROWN, 300);
  }
}
