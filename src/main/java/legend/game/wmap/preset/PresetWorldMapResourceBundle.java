package legend.game.wmap.preset;

import legend.game.characters.CharacterData2c;
import legend.game.unpacker.FileData;
import legend.game.wmap.world.WorldMapPresentationProfile;
import legend.game.wmap.world.WorldMapResourceBundle;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/** Confined package files replace selected region resources without engine file-number knowledge. */
public final class PresetWorldMapResourceBundle implements WorldMapResourceBundle {
  private final Path root;
  private final WorldMapPreset.Resources spec;
  private final WorldMapResourceBundle fallback;

  public PresetWorldMapResourceBundle(final Path root, final WorldMapPreset.Resources spec, final WorldMapResourceBundle fallback) {
    this.root = root;
    this.spec = spec;
    this.fallback = fallback;
  }

  private FileData read(final String path) {
    try {
      return new FileData(WorldMapPresetAssets.read(this.root, path));
    } catch(final IOException failure) {
      throw new CompletionException("Cannot load world map resource " + path, failure);
    }
  }

  private CompletableFuture<List<FileData>> files(final List<String> paths) {
    return CompletableFuture.supplyAsync(() -> paths.stream().map(this::read).toList());
  }

  private CompletableFuture<List<FileData>> model(final WorldMapPreset.ModelFiles model) {
    return CompletableFuture.supplyAsync(() -> {
      final List<FileData> files = new ArrayList<>();
      files.add(this.read(model.model()));
      files.add(model.texture() == null ? new FileData(new byte[0]) : this.read(model.texture()));
      model.animations().forEach(path -> files.add(this.read(path)));
      return List.copyOf(files);
    });
  }

  @Override
  public CompletableFuture<List<FileData>> uiTextures() {
    return this.spec.uiTextures() == null ? this.fallback.uiTextures() : this.files(this.spec.uiTextures());
  }

  @Override
  public CompletableFuture<List<FileData>> transportTextures() {
    if(this.spec.transportTextures() == null) return this.fallback.transportTextures();
    return this.files(this.spec.transportTextures()).thenApply(textures -> {
      final List<FileData> files = new ArrayList<>();
      files.add(new FileData(new byte[0]));
      files.addAll(textures);
      return List.copyOf(files);
    });
  }

  @Override
  public CompletableFuture<List<FileData>> transportModel(final int slot) {
    if(slot < 1 || slot > 3) throw new IllegalArgumentException("Transport slot must be 1 through 3");
    return this.spec.transports() == null ? this.fallback.transportModel(slot) : this.model(this.spec.transports().get(slot - 1));
  }

  @Override
  public CompletableFuture<List<FileData>> leaderModel(final CharacterData2c character) {
    return this.spec.leader() == null ? this.fallback.leaderModel(character) : this.model(this.spec.leader());
  }

  @Override
  public CompletableFuture<FileData> background() {
    if(this.spec.omitBackground()) return CompletableFuture.completedFuture(null);
    return this.spec.background() == null ? this.fallback.background() : CompletableFuture.supplyAsync(() -> this.read(this.spec.background()));
  }

  @Override
  public void loadMusic(final int chapter) {
    if(this.spec.music() == null) {
      this.fallback.loadMusic(chapter);
      return;
    }
    switch(this.spec.music()) {
      case RETAIL_CHAPTER -> legend.game.sound.Audio.loadWmapMusic(chapter);
      case FIXED_CHAPTER -> legend.game.sound.Audio.loadWmapMusic(this.spec.musicChapter());
      case SILENT -> legend.game.sound.Audio.stopMusicSequence();
      case KEEP -> { }
    }
  }

  @Override
  public WorldMapPresentationProfile layout() {
    return this.spec.layout() == null ? this.fallback.layout() : this.spec.layout().resolve();
  }

  @Override
  public boolean musicReady() {
    if(this.spec.music() == null) return this.fallback.musicReady();
    return switch(this.spec.music()) {
      case RETAIL_CHAPTER, FIXED_CHAPTER -> legend.game.wmap.world.RetailWorldMapResourceBundle.INSTANCE.musicReady();
      case SILENT, KEEP -> true;
    };
  }

  @Override
  public CompletableFuture<List<FileData>> locationSoundFiles(final int index) {
    if(this.spec.omitLocationSounds()) return CompletableFuture.completedFuture(List.of());
    if(this.spec.locationSoundFiles() == null) return this.fallback.locationSoundFiles(index);
    final WorldMapPreset.SoundFiles sound = this.spec.locationSoundFiles();
    return CompletableFuture.supplyAsync(() -> List.of(this.read(sound.header()), new FileData(new byte[0]), this.read(sound.indices()), this.read(sound.sequence()), this.read(sound.bank())));
  }

  @Override
  public boolean locationSounds() {
    return !this.spec.omitLocationSounds() && this.fallback.locationSounds();
  }
}
