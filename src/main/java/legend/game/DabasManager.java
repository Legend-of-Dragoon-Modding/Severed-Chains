package legend.game;

import org.legendofdragoon.dabas.core.FileData;
import org.legendofdragoon.dabas.core.memory.types.IntRef;
import org.legendofdragoon.dabas.game.types.Save60;

import static legend.core.GameEngine.CONFIG;
import static legend.lodmod.LodConfig.DABAS_SAVE_DATA;

public final class DabasManager {
  private DabasManager() { }

  public static boolean hasSave() {
    return CONFIG.hasConfig(DABAS_SAVE_DATA.get());
  }

  public static Save60 load() {
    final FileData data = new FileData(CONFIG.getConfig(DABAS_SAVE_DATA.get()));
    final IntRef offset = new IntRef();

    final Save60 save = new Save60();
    save.load(data, offset);
    return save;
  }

  public static void save(final Save60 save) {
    final FileData data = new FileData(CONFIG.getConfig(DABAS_SAVE_DATA.get()));
    final IntRef offset = new IntRef();

    save.save(data, offset);

    CONFIG.setConfig(DABAS_SAVE_DATA.get(), data.getBytes());
  }
}
