package legend.lodmod;

import legend.core.GameEngine;
import legend.game.characters.CharacterData2c;
import legend.game.characters.CharacterTemplate;
import legend.game.tim.Tim;
import legend.game.types.CContainer;
import legend.game.types.GameState52c;
import legend.game.types.TmdAnimationFile;
import legend.game.unpacker.FileData;
import legend.game.wmap.registries.RegisterWorldMapAvatarsEvent;
import legend.game.wmap.registries.WorldMapAvatarEntry;
import legend.game.wmap.world.WorldMapAvatar;
import legend.game.wmap.world.WorldMapAvatarAssets;
import legend.game.wmap.world.WorldMapPoint;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import static legend.game.DrgnFiles.loadDrgnDir;

/** Optional visuals; no provider changes route movement, encounters, sounds, or transport permissions. */
public final class LodWorldMapAvatars {
  private LodWorldMapAvatars() { }

  private static final Registrar<WorldMapAvatarEntry, RegisterWorldMapAvatarsEvent> REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.worldMapAvatars, LodMod.MOD_ID);
  public static final RegistryDelegate<WorldMapAvatarEntry> LEADER = character("wmap_avatar_leader", state -> state.getCharacterBySlot(0));
  public static final RegistryDelegate<WorldMapAvatarEntry> DART = character("wmap_avatar_dart", LodCharacterTemplates.DART);
  public static final RegistryDelegate<WorldMapAvatarEntry> LAVITZ = character("wmap_avatar_lavitz", LodCharacterTemplates.LAVITZ);
  public static final RegistryDelegate<WorldMapAvatarEntry> SHANA = character("wmap_avatar_shana", LodCharacterTemplates.SHANA);
  public static final RegistryDelegate<WorldMapAvatarEntry> ROSE = character("wmap_avatar_rose", LodCharacterTemplates.ROSE);
  public static final RegistryDelegate<WorldMapAvatarEntry> HASCHEL = character("wmap_avatar_haschel", LodCharacterTemplates.HASCHEL);
  public static final RegistryDelegate<WorldMapAvatarEntry> ALBERT = character("wmap_avatar_albert", LodCharacterTemplates.ALBERT);
  public static final RegistryDelegate<WorldMapAvatarEntry> MERU = character("wmap_avatar_meru", LodCharacterTemplates.MERU);
  public static final RegistryDelegate<WorldMapAvatarEntry> KONGOL = character("wmap_avatar_kongol", LodCharacterTemplates.KONGOL);
  public static final RegistryDelegate<WorldMapAvatarEntry> MIRANDA = character("wmap_avatar_miranda", LodCharacterTemplates.MIRANDA);
  public static final RegistryDelegate<WorldMapAvatarEntry> SHIP = transport("wmap_avatar_ship", 1, new WorldMapPoint(2, 2, 2), 1);
  public static final RegistryDelegate<WorldMapAvatarEntry> COOLON = transport("wmap_avatar_coolon", 2, new WorldMapPoint(0.5f, 0.5f, 0.5f), 0);
  public static final RegistryDelegate<WorldMapAvatarEntry> TELEPORT = transport("wmap_avatar_teleport", 3, new WorldMapPoint(1, 1, 1), 0);

  private static RegistryDelegate<WorldMapAvatarEntry> character(final String name, final Supplier<? extends CharacterTemplate> template) {
    return character(name, state -> state.getCharacterBySlot(0), template);
  }

  private static RegistryDelegate<WorldMapAvatarEntry> character(final String name, final Function<GameState52c, CharacterData2c> character) {
    return character(name, character, null);
  }

  private static RegistryDelegate<WorldMapAvatarEntry> character(final String name, final Function<GameState52c, CharacterData2c> character, final Supplier<? extends CharacterTemplate> template) {
    return REGISTRAR.register(name, () -> new WorldMapAvatarEntry(id -> new WorldMapAvatar(state -> {
      final CompletableFuture<List<FileData>> files = new CompletableFuture<>();
      try {
        final CharacterData2c selected = character.apply(state);
        (template == null ? selected.template : template.get()).loadWorldMapModel(selected, files::complete);
      } catch(final RuntimeException exception) {
        files.completeExceptionally(exception);
      }
      return files.orTimeout(30, TimeUnit.SECONDS).thenApply(loaded -> new WorldMapAvatarAssets(new CContainer(name, loaded.get(0)), new Tim(loaded.get(1)), List.of(new TmdAnimationFile(loaded.get(2)), new TmdAnimationFile(loaded.get(3)), new TmdAnimationFile(loaded.get(4))), new WorldMapPoint(0.5f, 0.4f, 0.5f), 1, 0, 1, 2, 0));
    })));
  }

  private static RegistryDelegate<WorldMapAvatarEntry> transport(final String name, final int slot, final WorldMapPoint scale, final float shadow) {
    return REGISTRAR.register(name, () -> new WorldMapAvatarEntry(id -> new WorldMapAvatar(state -> loadDrgnDir(0, 5714 + slot).thenApply(files -> {
      final List<TmdAnimationFile> animations = new ArrayList<>();
      for(int i = 2; i < Math.min(16, files.size()); i++) {
        if(files.get(i).size() != 0) animations.add(new TmdAnimationFile(files.get(i)));
      }
      return new WorldMapAvatarAssets(new CContainer(name, files.get(0)), null, animations, scale, shadow, 0, 0, 0, slot);
    }))));
  }

  static void register(final RegisterWorldMapAvatarsEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
