package legend.game.saves;

import legend.game.EngineState;
import legend.game.saves.campaigns.CampaignType;
import legend.game.types.ActiveStatsa0;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;

public interface SaveSerializer {
  int serializer(final String name, final FileData data, final GameState52c gameState, final ActiveStatsa0[] activeStats, final CampaignType campaignType, final EngineState<?> engineState);
}
