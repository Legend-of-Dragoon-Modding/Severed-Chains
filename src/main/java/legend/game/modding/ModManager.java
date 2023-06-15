package legend.game.modding;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ModManager {
  private static final Logger LOGGER = LogManager.getFormatterLogger(ModManager.class);

  private final Map<String, URL> allModUrls = new HashMap<>();
  private final Map<String, Class<?>> allModClasses = new HashMap<>();
  private final Set<String> allModIds = Collections.unmodifiableSet(this.allModClasses.keySet());

  private final List<URL> loadedModUrls = new ArrayList<>();
  private ClassLoader loadedModClassLoader;
  private final Map<String, ModContainer> loadedModInstances = new HashMap<>();
  private final Collection<ModContainer> unmodifiableLoadedModInstances = Collections.unmodifiableCollection(this.loadedModInstances.values());

  public ModManager(final Consumer<Access> access) {
    access.accept(new Access());
  }

  public Set<String> getAllModIds() {
    return this.allModIds;
  }

  public boolean isLoaded(final String modId) {
    return this.loadedModInstances.containsKey(modId);
  }

  public boolean isReady(final String modId) {
    return this.loadedModInstances.containsKey(modId) && this.loadedModInstances.get(modId).state.isReady();
  }

  public Collection<ModContainer> getLoadedMods() {
    return this.unmodifiableLoadedModInstances;
  }

  public class Access {
    private Access() { }

    public void findMods() throws IOException {
      final Path modsDir = Path.of("./mods");
      Files.createDirectories(modsDir);

      LOGGER.info("Scanning for mods...");
      final Collection<URL> urlList = new ArrayList<>();

      try(final DirectoryStream<Path> jars = Files.newDirectoryStream(modsDir, "*.jar")) {
        for(final Path jar : jars) {
          urlList.add(jar.toUri().toURL());
        }
      }

      final ClassLoader allModClassLoader = new URLClassLoader(urlList.toArray(URL[]::new), ModManager.class.getClassLoader());
      ModManager.this.allModUrls.clear();

      final Reflections reflections = new Reflections(
        new ConfigurationBuilder()
          .addUrls(this.getClass().getClassLoader().getResource(""))
          .addClassLoaders(this.getClass().getClassLoader()).addUrls(ClasspathHelper.forPackage("legend"))
          .addClassLoaders(allModClassLoader).addUrls(urlList)
      );

      final Set<Class<?>> modClasses = reflections.getTypesAnnotatedWith(Mod.class);

      for(final Class<?> modClass : modClasses) {
        final Mod modAnnotation = modClass.getDeclaredAnnotation(Mod.class);

        if(ModManager.this.allModClasses.containsKey(modAnnotation.id())) {
          LOGGER.error("Duplicate mod ID %s! Skipping.", modAnnotation.id());
          continue;
        }

        LOGGER.info("Found mod: %s", modAnnotation.id());
        ModManager.this.allModClasses.put(modAnnotation.id(), modClass);
        ModManager.this.allModUrls.put(modAnnotation.id(), modClass.getProtectionDomain().getCodeSource().getLocation());
      }
    }

    /**
     * Load all found mods
     */
    public void loadMods() {
      this.loadMods(ModManager.this.allModIds);
    }

    /**
     * Load all mods in the list and return a list of missing mods
     */
    public Set<String> loadMods(final Set<String> modIds) {
      ModManager.this.loadedModUrls.clear();

      final Set<String> missingModIds = new HashSet<>();
      for(final String modId : modIds) {
        if(ModManager.this.allModClasses.containsKey(modId)) {
          this.instantiateMod(modId);
        } else {
          missingModIds.add(modId);
        }
      }

      ModManager.this.loadedModClassLoader = new URLClassLoader(ModManager.this.loadedModUrls.toArray(URL[]::new), ModManager.class.getClassLoader());
      return missingModIds;
    }

    private void instantiateMod(final String modId) {
      try {
        final ModContainer modContainer = new ModContainer(modId, ModManager.this.allModClasses.get(modId).getDeclaredConstructor().newInstance());
        ModManager.this.loadedModInstances.put(modId, modContainer);
        ModManager.this.loadedModUrls.add(ModManager.this.allModUrls.get(modId));
        LOGGER.info("Loaded mod: %s", modId);
      } catch(final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
        LOGGER.warn("FAILED TO LOAD MOD: %s", modId);
        LOGGER.warn("Exception:", ex);
      }
    }

    public void loadingComplete() {
      for(final ModContainer container : ModManager.this.loadedModInstances.values()) {
        container.state = ModState.READY;
      }
    }

    public void reset() {
      ModManager.this.loadedModUrls.clear();
      ModManager.this.loadedModClassLoader = null;
      ModManager.this.loadedModInstances.clear();
    }
  }

  public ConfigurationBuilder addModsToReflectionsConfig(final ConfigurationBuilder builder) {
    return builder
      .addUrls(this.getClass().getClassLoader().getResource("")) // Find mods in the current project (finds CoreMod in SC, mod in SCDK)
      .addClassLoaders(this.getClass().getClassLoader()).addUrls(ClasspathHelper.forPackage("legend")) // Finds CoreMod in SCDK
      .addClassLoaders(this.loadedModClassLoader).addUrls(this.loadedModUrls); // Finds mods in mods folder
  }
}
