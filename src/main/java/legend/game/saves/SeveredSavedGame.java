package legend.game.saves;

import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.controls.SeveredSaveCard;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.List;

public class SeveredSavedGame extends SavedGame {
  public final String locationName;
  public final List<CharStats> charStats = new ArrayList<>();

  public SeveredSavedGame(final String fileName, final String saveName, final String locationName, final RegistryId campaignType, final RegistryId engineState, final FileData engineStateData, final GameState52c gameState, final ConfigCollection config) {
    super(fileName, saveName, campaignType, engineState, engineStateData, gameState, config);
    this.locationName = locationName;
  }

  @Override
  public Control createSaveCard() {
    return new SeveredSaveCard(this);
  }

  public static final class CharStats {
    public final int maxHp;
    public final int maxMp;

    public CharStats(final int maxHp, final int maxMp) {
      this.maxHp = maxHp;
      this.maxMp = maxMp;
    }
  }
}
