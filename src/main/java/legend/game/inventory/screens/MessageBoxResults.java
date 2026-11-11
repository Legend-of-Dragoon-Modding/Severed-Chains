package legend.game.inventory.screens;

import legend.game.types.MessageBoxResult;

public class MessageBoxResults {
  public MessageBoxResult messageBoxResult;
  public int cycleValue;

  MessageBoxResults(final MessageBoxResult result) {
    this.messageBoxResult = result;
  }

  MessageBoxResults(final MessageBoxResult result, final int cycleValue) {
    this.messageBoxResult = result;
    this.cycleValue = cycleValue;
  }
}
