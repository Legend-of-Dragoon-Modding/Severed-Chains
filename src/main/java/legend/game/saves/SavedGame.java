package legend.game.saves;

import legend.game.saves.campaigns.CampaignType;
import legend.game.saves.types.SaveDisplay;
import legend.game.saves.types.SaveType;
import legend.game.types.GameState52c;
import legend.lodmod.LodMod;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Map;
import java.util.Set;

public final class SavedGame<Display extends SaveDisplay> {
  public String fileName;
  public String saveName;
  public final RegistryDelegate<CampaignType> campaignType;
  public final RegistryDelegate<SaveType<Display>> saveType;
  public final Display display;
  public final GameState52c state;
  public final ConfigCollection config;
  public final Map<RegistryId, Set<RegistryId>> ids;

  public SavedGame(final String fileName, final String saveName, final RegistryDelegate<CampaignType> campaignType, final RegistryDelegate<SaveType<Display>> saveType, final Display display, final GameState52c state, final ConfigCollection config, final Map<RegistryId, Set<RegistryId>> ids) {
    this.fileName = fileName;
    this.saveName = saveName;
    this.campaignType = campaignType;
    this.saveType = saveType;
    this.display = display;
    this.state = state;
    this.config = config;
    this.ids = ids;
  }

  public static SavedGame<?> invalid(final String fileName) {
    return new SavedGame<>(fileName, fileName, null, LodMod.RETAIL_SAVE_TYPE, null, null, null, null);
  }

  public boolean isValid() {
    return this.state != null;
  }

  @Override
  public String toString() {
    return this.saveName;
  }
}
