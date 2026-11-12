package legend.game.inventory.screens;

import legend.game.types.MessageBoxResult;

public class CycleMessageBoxResult {
  public MessageBoxResult messageBoxResult;
  public int cycleValue;

  CycleMessageBoxResult(final MessageBoxResult result) {
    this.messageBoxResult = result;
  }

  CycleMessageBoxResult(final MessageBoxResult result, final int cycleValue) {
    this.messageBoxResult = result;
    this.cycleValue = cycleValue;
  }
}
