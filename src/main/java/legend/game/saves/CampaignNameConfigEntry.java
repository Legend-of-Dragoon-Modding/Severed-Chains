package legend.game.saves;

public class CampaignNameConfigEntry extends StringConfigEntry {
  public CampaignNameConfigEntry() {
    super("", 1, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }
}
