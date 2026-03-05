package legend.lodmod;

import legend.core.GameEngine;
import legend.game.characters.CharacterTemplate;
import legend.game.characters.RegisterCharacterTemplatesEvent;
import legend.lodmod.characters.AlbertTemplate;
import legend.lodmod.characters.DartTemplate;
import legend.lodmod.characters.HaschelTemplate;
import legend.lodmod.characters.KongolTemplate;
import legend.lodmod.characters.LavitzTemplate;
import legend.lodmod.characters.MeruTemplate;
import legend.lodmod.characters.MirandaTemplate;
import legend.lodmod.characters.RoseTemplate;
import legend.lodmod.characters.ShanaTemplate;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

public final class LodCharacterTemplates {
  private LodCharacterTemplates() { }

  private static final Registrar<CharacterTemplate, RegisterCharacterTemplatesEvent> REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.characterTemplates, LodMod.MOD_ID);

  public static final RegistryDelegate<DartTemplate> DART = REGISTRAR.register("dart", DartTemplate::new);
  public static final RegistryDelegate<LavitzTemplate> LAVITZ = REGISTRAR.register("lavitz", LavitzTemplate::new);
  public static final RegistryDelegate<ShanaTemplate> SHANA = REGISTRAR.register("shana", ShanaTemplate::new);
  public static final RegistryDelegate<ShanaTemplate> SHANT = REGISTRAR.register("shant", ShanaTemplate::new);
  public static final RegistryDelegate<RoseTemplate> ROSE = REGISTRAR.register("rose", RoseTemplate::new);
  public static final RegistryDelegate<HaschelTemplate> HASCHEL = REGISTRAR.register("haschel", HaschelTemplate::new);
  public static final RegistryDelegate<AlbertTemplate> ALBERT = REGISTRAR.register("albert", AlbertTemplate::new);
  public static final RegistryDelegate<MeruTemplate> MERU = REGISTRAR.register("meru", MeruTemplate::new);
  public static final RegistryDelegate<KongolTemplate> KONGOL = REGISTRAR.register("kongol", KongolTemplate::new);
  public static final RegistryDelegate<MirandaTemplate> MIRANDA = REGISTRAR.register("miranda", MirandaTemplate::new);

  static void register(final RegisterCharacterTemplatesEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
