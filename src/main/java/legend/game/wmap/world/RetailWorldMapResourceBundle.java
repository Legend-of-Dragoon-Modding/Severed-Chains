package legend.game.wmap.world;

import legend.game.characters.CharacterData2c;
import legend.game.unpacker.FileData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static legend.game.DrgnFiles.loadDrgnDir;
import static legend.game.DrgnFiles.loadDrgnFile;

/** All native file numbers and leader-template compatibility live in this adapter. */
public final class RetailWorldMapResourceBundle implements WorldMapResourceBundle {
  public static final RetailWorldMapResourceBundle INSTANCE = new RetailWorldMapResourceBundle();
  public static final RetailWorldMapResourceBundle INDEPENDENT = new RetailWorldMapResourceBundle(false);
  private final boolean nativeBackground;

  private RetailWorldMapResourceBundle() {
    this(true);
  }

  private RetailWorldMapResourceBundle(final boolean nativeBackground) {
    this.nativeBackground = nativeBackground;
  }

  @Override
  public CompletableFuture<List<FileData>> uiTextures() {
    return loadDrgnDir(0, 5695);
  }

  @Override
  public CompletableFuture<List<FileData>> transportTextures() {
    return loadDrgnDir(0, 5713);
  }

  @Override
  public CompletableFuture<List<FileData>> transportModel(final int slot) {
    if(slot < 1 || slot > 3) throw new IllegalArgumentException("Transport slot must be 1 through 3");
    return loadDrgnDir(0, 5714 + slot);
  }

  @Override
  public CompletableFuture<List<FileData>> leaderModel(final CharacterData2c character) {
    final CompletableFuture<List<FileData>> result = new CompletableFuture<>();
    character.template.loadWorldMapModel(character, result::complete);
    return result;
  }

  @Override
  public CompletableFuture<FileData> background() {
    return this.nativeBackground ? loadDrgnFile(0, 5696) : CompletableFuture.completedFuture(null);
  }

  @Override
  public void loadMusic(final int chapter) {
    legend.game.sound.Audio.loadWmapMusic(chapter);
  }

  @Override
  public boolean musicReady() {
    return (legend.game.sound.Audio.getLoadedAudioFiles() & 0x80) == 0;
  }

  @Override
  public CompletableFuture<List<FileData>> locationSoundFiles(final int index) {
    return index == 0 ? CompletableFuture.completedFuture(List.of()) : loadDrgnDir(0, 5740 + index);
  }
}
