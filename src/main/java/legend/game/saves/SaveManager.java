package legend.game.saves;

import com.github.slugify.Slugify;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import legend.game.EngineState;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.campaigns.CampaignType;
import legend.game.saves.types.RetailSaveDisplay;
import legend.game.types.ActiveStatsa0;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import legend.game.unpacker.GrowableFileData;
import legend.game.unpacker.Unpacker;
import legend.lodmod.LodMod;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.bootMods;
import static legend.core.GameEngine.bootRegistries;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public final class SaveManager {
  private static final Logger LOGGER = LogManager.getFormatterLogger(SaveManager.class);

  private static final Slugify slug = Slugify.builder().underscoreSeparator(true).customReplacement("'", "").build();

  private final Path dir = Paths.get("saves");
  private final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.dsav");

  private final Map<Function<FileData, FileData>, SaveDeserializer> deserializers = new HashMap<>();
  private final int serializerMagic;
  private final SaveSerializer serializer;

  private FileData saveIcons;

  public SaveManager(final int serializerMagic, final SaveSerializer serializer) {
    this.serializerMagic = serializerMagic;
    this.serializer = serializer;
  }

  public void init() {
    this.saveIcons = Unpacker.loadFile("characters/portraits.png");
  }

  public FileData getSaveIcons() {
    return this.saveIcons;
  }

  public void registerDeserializer(final Function<FileData, FileData> matcher, final SaveDeserializer deserializer) {
    this.deserializers.put(matcher, deserializer);
  }

  public String generateCampaignName() {
    if(!Files.exists(this.dir.resolve("New Campaign"))) {
      return "New Campaign";
    }

    for(int i = 2; ; i++) {
      if(!Files.exists(this.dir.resolve("New Campaign " + i))) {
        return "New Campaign " + i;
      }
    }
  }

  public boolean campaignExists(final String campaign) {
    return Files.exists(this.dir.resolve(campaign));
  }

  public String slugName(final String name) {
    return slug.slugify(name);
  }

  public boolean saveExists(final String campaign, final String saveName) {
    return Files.exists(this.dir.resolve(campaign).resolve(this.slugName(saveName) + ".dsav"));
  }

  public String generateSaveName(final String campaign) {
    for(int i = 1; ; i++) {
      if(!this.saveExists(campaign, "Save " + i)) {
        return "Save " + i;
      }
    }
  }

  /** Look for saves from before campaigns were a thing */
  public List<Path> findUncategorizedSaves() {
    try(final Stream<Path> stream = Files.list(this.dir)) {
      return stream
        .filter(file -> !Files.isDirectory(file) && this.matcher.matches(file.getFileName()))
        .toList();
    } catch(final IOException e) {
      LOGGER.error("Failed to list uncategorized saves");
    }

    return List.of();
  }

  public List<Path> findMemcards() {
    try(final Stream<Path> stream = Files.list(this.dir)) {
      return stream
        .filter(file -> !Files.isDirectory(file) && this.isMemcard(file))
        .sorted()
        .toList();
    } catch(final IOException e) {
      LOGGER.error("Failed to list memcards");
    }

    return List.of();
  }

  private boolean isMemcard(final Path path) {
    final FileData data;
    try {
      data = new FileData(Files.readAllBytes(path));
    } catch(final IOException e) {
      return false;
    }

    if(data.size() != 0x2_0000) {
      return false;
    }

    if(data.readUShort(0x0) != 0x434d) { // MC
      return false;
    }

    for(int i = 0; i < 16; i++) {
      final int offset = (i + 1) * 0x80;
      if(data.readByte(offset) == 0x51 && "BASCUS-94491drgn".equals(data.readFixedLengthAscii(offset + 0xa, 0x10))) {
        return true;
      }
    }

    return false;
  }

  public void moveCategorizedSaves(final String campaignName) throws IOException {
    final List<Path> saves = this.findUncategorizedSaves();

    if(!saves.isEmpty()) {
      LOGGER.info("Upgrading legacy saves to campaign");

      final Path campaignDir = this.dir.resolve(campaignName);
      Files.createDirectories(campaignDir);

      for(final Path save : saves) {
        Files.move(save, campaignDir.resolve(save.getFileName()));
      }
    }
  }

  public void splitMemcards(final String campaignName) throws IOException, InvalidSaveException, SaveFailedException {
    final List<Path> memcards = this.findMemcards();

    if(!memcards.isEmpty()) {
      LOGGER.info("Converting memcards to campaign");

      bootMods(Set.of(CoreMod.MOD_ID, LodMod.MOD_ID));
      bootRegistries();

      final Path campaignDir = this.dir.resolve(campaignName);
      Files.createDirectories(campaignDir);

      final List<SavedGame<RetailSaveDisplay>> saves = new ArrayList<>();

      for(final Path memcard : memcards) {
        this.splitMemcard(campaignName, new FileData(Files.readAllBytes(memcard)), saves);
      }

      saves.sort(Comparator.comparingInt(save -> save.state.timestamp_a0));
      final Object2IntMap<String> indices = new Object2IntOpenHashMap<>();

      final Instant now = Instant.now();

      for(int i = 0; i < saves.size(); i++) {
        final SavedGame<RetailSaveDisplay> save = saves.get(i);
        save.state.syncIds();

        final EngineState dummyEngineState = new EngineState(null) {
          @Override
          public String getLocationForSave() {
            return save.display.location;
          }

          @Override
          public FileData writeSaveData() {
            return null;
          }

          @Override
          public void tick() {

          }
        };

        indices.mergeInt(save.display.location, 1, Integer::sum);

        final ActiveStatsa0[] activeStats = new ActiveStatsa0[9];
        Arrays.setAll(activeStats, n -> new ActiveStatsa0());

        try {
          loadCharacterStats(save.state, activeStats);
        } catch(final Exception e) {
          LOGGER.error("Failed to convert save %d", i);
          continue;
        }

        save.state.syncIds();

        final Path file = this.newSave(save.display.location + ' ' + indices.getInt(save.display.location), save.state, activeStats, LodMod.LEGEND_OF_DRAGOON_CAMPAIGN_TYPE.get(), dummyEngineState);
        Files.setLastModifiedTime(file, FileTime.from(now.minus(saves.size() - i, ChronoUnit.SECONDS)));
      }

      for(final Path memcard : memcards) {
        Files.delete(memcard);
      }
    }
  }

  private void splitMemcard(final String campaignName, final FileData memcard, final List<SavedGame<RetailSaveDisplay>> saves) throws InvalidSaveException {
    for(int i = 1; i <= 15; i++) {
      final int offset = i * 0x80;
      if(memcard.readByte(offset) == 0x51 && "BASCUS-94491drgn".equals(memcard.readFixedLengthAscii(offset + 0xa, 0x10))) {
        //noinspection unchecked
        saves.add((SavedGame<RetailSaveDisplay>)this.loadGame(campaignName, "", memcard.slice(i * 0x2000, 0x720)));
      }
    }
  }

  private List<Path> getCampaigns() {
    try {
      Files.createDirectories(this.dir);
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }

    try(final Stream<Path> stream = Files.list(this.dir)) {
      return stream
        .filter(Files::isDirectory)
        .filter(path -> {
          try(final Stream<Path> children = Files.list(path)) {
            return children.anyMatch(child -> child.toString().endsWith(".dsav"));
          } catch(final IOException e) {
            throw new RuntimeException(e);
          }
        })
        .sorted(Comparator.comparingLong((final Path path) -> {
          try(final Stream<Path> children = this.childrenSortedByDate(path)) {
            return children
              .findFirst()
              .map(this::getModifiedTime)
              .orElse(0L);
          } catch(final IOException e) {
            throw new RuntimeException(e);
          }
        }).reversed())
        .map(Path::getFileName)
        .collect(Collectors.toList());
    } catch(final IOException e) {
      throw new RuntimeException("Failed to get save files", e);
    }
  }

  private List<Path> getSaves(final String campaign) {
    final Path campaignPath = this.dir.resolve(campaign);

    try {
      Files.createDirectories(campaignPath);
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }

    try(final Stream<Path> children = this.childrenSortedByDate(campaignPath)) {
      return children
        .map(Path::getFileName)
        .collect(Collectors.toList());
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Stream<Path> childrenSortedByDate(final Path path) throws IOException {
    return Files.list(path)
      .filter(file -> !Files.isDirectory(file) && this.matcher.matches(file.getFileName()))
      .sorted(Comparator.comparingLong(this::getModifiedTime).reversed());
  }

  private long getModifiedTime(final Path path) {
    try {
      return Files.getLastModifiedTime(path).to(TimeUnit.MILLISECONDS);
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean hasCampaigns() {
    if(!Files.isDirectory(this.dir)) {
      return false;
    }

    try(final Stream<Path> stream = Files.list(this.dir)) {
      return stream
        .filter(Files::isDirectory)
        .anyMatch(path -> {
          try(final Stream<Path> children = Files.list(path)) {
            return children.anyMatch(child -> child.toString().endsWith(".dsav"));
          } catch(final IOException e) {
            throw new RuntimeException(e);
          }
        });
    } catch(final IOException e) {
      LOGGER.error("Failed to get save files", e);
      return false;
    }
  }

  public Path overwriteSave(final String fileName, final String saveName, final GameState52c gameState, final ActiveStatsa0[] activeStats, final CampaignType campaignType, final EngineState<?> engineState) throws SaveFailedException {
    try {
      ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.CAMPAIGN, Path.of("saves", gameState_800babc8.campaignName, "campaign_config.dcnf"));

      final FileData data = new GrowableFileData(1024);
      data.writeInt(0x0, this.serializerMagic);
      final int length = this.serializer.serializer(saveName, data.slice(0x4), gameState, activeStats, campaignType, engineState);

      final Path dir = this.dir.resolve(gameState.campaignName);
      final Path file = dir.resolve(fileName + ".dsav");

      final byte[] out = new byte[length + 0x4];
      data.read(out);

      Files.createDirectories(dir);
      Files.write(file, out, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
      return file;
    } catch(final IOException e) {
      throw new SaveFailedException("Failed to save game", e);
    }
  }

  public Path newSave(final String saveName, final GameState52c state, final ActiveStatsa0[] activeStats, final CampaignType campaignType, final EngineState<?> engineState) throws SaveFailedException {
    return this.overwriteSave(slug.slugify(saveName), saveName, state, activeStats, campaignType, engineState);
  }

  public SavedGame<?> loadGame(final String campaign, final String filename) throws InvalidSaveException {
    final Path file = this.dir.resolve(campaign).resolve(filename + ".dsav");

    if(!Files.exists(file)) {
      throw new InvalidSaveException("No saved game " + filename);
    }

    try {
      final FileData data = new FileData(Files.readAllBytes(file));
      return this.loadGame(campaign, filename, data);
    } catch(final Exception e) {
      throw new InvalidSaveException("Failed to load saved game " + filename, e);
    }
  }

  public SavedGame<?> loadGame(final String campaign, final String filename, final FileData data) throws InvalidSaveException {
    //LAB_80109e38
    for(final var entry : this.deserializers.entrySet()) {
      final FileData slice = entry.getKey().apply(data);

      if(slice != null) {
        final SavedGame<?> savedGame = entry.getValue().deserialize(filename, slice);
        savedGame.state.campaignName = campaign;
        return savedGame;
      }
    }

    throw new InvalidSaveException("Invalid save data " + filename);
  }

  public List<Campaign> loadAllCampaigns() {
    final List<Campaign> campaigns = new ArrayList<>();

    campaignLoop:
    for(final Path child : this.getCampaigns()) {
      final String filename = child.getFileName().toString();

      final List<SavedGame<?>> saves = this.loadAllSaves(filename);

      if(!saves.isEmpty()) {
        // Display the first valid save, and use the first save if no valid ones are found
        for(final SavedGame<?> save : saves) {
          if(save.isValid()) {
            campaigns.add(new Campaign(filename, save));
            continue campaignLoop;
          }
        }

        campaigns.add(new Campaign(filename, saves.getFirst()));
      }
    }

    return campaigns;
  }

  public List<SavedGame<?>> loadAllSaves(final String campaign) {
    final List<SavedGame<?>> saves = new ArrayList<>();

    for(final Path child : this.getSaves(campaign)) {
      final String filename = child.getFileName().toString();
      final String name = filename.substring(0, filename.lastIndexOf('.'));

      try {
        saves.add(this.loadGame(campaign, name));
      } catch(final InvalidSaveException e) {
        saves.add(SavedGame.invalid(name));
      }
    }

    return saves;
  }

  public void deleteCampaign(final String campaignName) throws IOException {
    FileUtils.deleteDirectory(this.dir.resolve(campaignName).toFile());
  }

  public void deleteSave(final String campaignName, final String saveName) throws IOException {
    Files.delete(this.dir.resolve(campaignName).resolve(saveName + ".dsav"));
  }
}
