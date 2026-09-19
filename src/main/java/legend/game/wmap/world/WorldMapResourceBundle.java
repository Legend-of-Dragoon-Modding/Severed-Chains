package legend.game.wmap.world;

import legend.game.characters.CharacterData2c;
import legend.game.unpacker.FileData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Region-owned CPU asset requests. WMap adopts their results on its render thread. */
public interface WorldMapResourceBundle {
  CompletableFuture<List<FileData>> uiTextures();
  CompletableFuture<List<FileData>> transportTextures();
  CompletableFuture<List<FileData>> transportModel(int slot);
  CompletableFuture<List<FileData>> leaderModel(CharacterData2c character);
  /** A null result disables the MCQ background. */
  CompletableFuture<FileData> background();
  void loadMusic(int chapter);
  default boolean musicReady() {
    return true;
  }
  /** Empty assets mean no location sound bank; index zero denotes an independent region. */
  CompletableFuture<List<FileData>> locationSoundFiles(int index);

  /** Optional replacement for the preset's global texture/layout profile. */
  @Nullable
  default WorldMapPresentationProfile layout() {
    return null;
  }

  default boolean locationSounds() {
    return true;
  }
}
