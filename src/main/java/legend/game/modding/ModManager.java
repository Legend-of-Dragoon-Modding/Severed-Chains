package legend.game.modding;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModManager {
  private static final Logger LOGGER = LogManager.getFormatterLogger(ModManager.class);

  private ClassLoader modClassLoader;
  private URL[] modUrls;

  private final Map<String, Class<?>> modClasses = new HashMap<>();
  private final Map<String, Object> modInstances = new HashMap<>();

  public void loadMods() throws IOException {
    final Path modsDir = Path.of("./mods");
    Files.createDirectories(modsDir);

    LOGGER.info("Scanning for mods...");
    final Collection<URL> urlList = new ArrayList<>();

    try(final DirectoryStream<Path> jars = Files.newDirectoryStream(modsDir, "*.jar")) {
      for(final Path jar : jars) {
        urlList.add(jar.toUri().toURL());
      }
    }

    this.modUrls = urlList.toArray(new URL[0]);
    this.modClassLoader = new URLClassLoader(this.modUrls, ModManager.class.getClassLoader());

    final Reflections reflections = new Reflections(this.addModsToReflectionsConfig(new ConfigurationBuilder()));
    final Set<Class<?>> listeners = reflections.getTypesAnnotatedWith(Mod.class);

    for(final Class<?> modClass : listeners) {
      final Mod modAnnotation = modClass.getDeclaredAnnotation(Mod.class);

      if(this.modClasses.containsKey(modAnnotation.id())) {
        LOGGER.error("Duplicate mod ID %s! Skipping.", modAnnotation.id());
        continue;
      }

      LOGGER.info("Found mod: %s", modAnnotation.id());
      this.modClasses.put(modAnnotation.id(), modClass);
    }
  }

  public void instantiateMods() {
    this.modClasses.forEach((key, value) -> {
      try {
        this.modInstances.put(key, value.getDeclaredConstructor().newInstance());
        LOGGER.info("Loaded mod: %s", key);
      } catch(final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
        LOGGER.warn("FAILED TO LOAD MOD: %s", key);
        LOGGER.warn("Exception:", ex);
      }
    });
  }

  public ConfigurationBuilder addModsToReflectionsConfig(ConfigurationBuilder builder) {
    if(!System.getProperty("scdk", "").isEmpty()) {
      builder = builder.addUrls(this.getClass().getClassLoader().getResource(""));
    }

    return builder.addClassLoaders(this.modClassLoader).addUrls(this.modUrls);
  }
}
