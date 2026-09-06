package legend.game.saves;

import legend.core.lang.TextComponent;

public class ConfigPreset {
  public final TextComponent name;
  public final ConfigCollection config;

  public ConfigPreset(final TextComponent name, final ConfigCollection config) {
    this.name = name;
    this.config = config;
  }
}
