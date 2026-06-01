package legend.game.inventory;

import javax.annotation.Nullable;

public class UseItemResponse {
  public boolean success;
  /** The translation key to display when using the item. If null, no messagebox will be displayed. */
  @Nullable
  public String text;

  public void success(final String text) {
    this.success = true;
    this.text = text;
  }

  public void success() {
    this.success = true;
    this.text = null;
  }

  public void failure(final String text) {
    this.success = false;
    this.text = text;
  }

  public void failure() {
    this.success = false;
    this.text = null;
  }
}
