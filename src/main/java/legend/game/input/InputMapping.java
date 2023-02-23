package legend.game.input;

import java.util.ArrayList;
import java.util.List;

import static legend.game.Scus94491BpeSegment.keyRepeat;
import static legend.game.Scus94491BpeSegment_800b._800bee90;
import static legend.game.Scus94491BpeSegment_800b._800bee94;
import static legend.game.Scus94491BpeSegment_800b._800bee98;

public class InputMapping {
  public List<InputBinding> bindings = new ArrayList<>();
  private InputControllerData controllerData;
  private boolean anyActivityThisFrame;
  private boolean anyActivity;
  public void update() {
    this.controllerData.updateState();

    this.anyActivityThisFrame = false;
    this.anyActivity = false;
    for(final InputBinding binding : this.bindings) {
      binding.update();
      if(this.controllerData.getPlayerSlot() == 1) {
        this.updateLegacyInput(binding);
      }

      if(binding.getState() == InputBindingState.PRESSED_THIS_FRAME) {
        this.anyActivityThisFrame = true;
        this.anyActivity = true;
      } else if(binding.getState() == InputBindingState.PRESSED) {
        this.anyActivity = true;
      }
    }
  }

  private void updateLegacyInput(final InputBinding binding) {
    final int hexCode = binding.getHexCode();
    if(hexCode == -1) {
      return;
    }

    if(binding.getState() == InputBindingState.PRESSED_THIS_FRAME) {

      _800bee90.or(hexCode);
      _800bee94.or(hexCode);
      _800bee98.or(hexCode);

      keyRepeat.put(hexCode, 0);

    } else if(binding.getState() == InputBindingState.RELEASED_THIS_FRAME) {
      _800bee90.and(~hexCode);
      _800bee94.and(~hexCode);
      _800bee98.and(~hexCode);

      keyRepeat.remove(hexCode);
    }
  }

  public boolean hasActivityThisFrame() {
    return this.anyActivityThisFrame;
  }

  public boolean hasActivity() {
    return this.anyActivity;
  }

  public InputControllerData getControllerData() {
    return this.controllerData;
  }

  public void setControllerData(final InputControllerData controllerData) {
    this.controllerData = controllerData;
    this.bindings = ControllerDatabase.GetBindings(controllerData.getGlfwJoystickGUID());
    for (final InputBinding binding : this.bindings) {
      binding.setTargetController(controllerData);
    }
  }
}
