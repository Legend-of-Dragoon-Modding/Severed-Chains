package legend.game.unpacker;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.xxhash.XXH3State;
import org.lwjgl.util.xxhash.XXHash;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public final class SobjTransformer {
  private SobjTransformer() {}

  private static final Logger LOGGER = LogManager.getFormatterLogger(SobjTransformer.class);

  public static void transform(final PathNode root, final Transformations transformations, final Set<String> flags) {
    final XXH3State xxhash = XXHash.XXH3_createState();

    if(xxhash == null) {
      LOGGER.log(Level.ERROR, "Failed to initialize hash state. Skipping");

      return;
    }

    final Long2ObjectArrayMap<Model> models = new Long2ObjectArrayMap<>();

    for(int drgnIndex = 1; drgnIndex <= 4; drgnIndex++) {
      final PathNode drgn = root.children.get("SECT").children.get("DRGN2" + drgnIndex + ".BIN");

      if(drgn == null) {
        continue;
      }

      final int mrgEntries = countMrg(drgn);

      final ByteBuffer buffer = BufferUtils.createByteBuffer(40968);

      for(int map = 5; map < mrgEntries; map += 3) {
        final PathNode folder = drgn.children.get(Integer.toString(map));

        if(folder == null) {
          continue;
        }

        final int modelAndAnimCount = countMrg(folder) - 3;

        for(int modelIndex = 0; modelIndex < modelAndAnimCount; modelIndex += 33) {
          final PathNode model = folder.children.get(Integer.toString(modelIndex));

          if(model != null) {
            XXHash.XXH3_64bits_reset(xxhash);

            buffer.clear();
            buffer.put(model.data.data, model.data.offset, Math.min(model.data.size(), 40968));
            buffer.flip();

            XXHash.XXH3_64bits_update(xxhash, buffer);

            final long modelHash = XXHash.XXH3_64bits_digest(xxhash);

            if (!models.containsKey(modelHash)) {
              models.put(modelHash, new Model(hashName(modelHash), model.data));
            }

            final PathNode textures = folder.children.get("textures");

            if(textures != null) {
              final PathNode texture = textures.children.get(Integer.toString(modelIndex / 33));

              if(texture != null) {
                XXHash.XXH3_64bits_reset(xxhash);

                buffer.clear();
                buffer.put(texture.data.data, texture.data.offset, Math.min(texture.data.size(), 40968));
                buffer.flip();

                XXHash.XXH3_64bits_update(xxhash, buffer);

                final long textureHash = XXHash.XXH3_64bits_digest(xxhash);

                final Model m = models.get(modelHash);
                if (!m.textures.containsKey(textureHash)) {
                  m.textures.put(textureHash, new Sobj(Long.toHexString(textureHash), texture.data));
                }
              }
            }

            for(int animationIndex = 1; animationIndex <= 32; animationIndex++) {
              final PathNode animation = folder.children.get(Integer.toString(modelIndex + animationIndex));

              if(animation != null && animation.data.real()) {
                XXHash.XXH3_64bits_reset(xxhash);

                buffer.clear();
                buffer.put(animation.data.data, animation.data.offset, Math.min(animation.data.size(), 40968));
                buffer.flip();

                XXHash.XXH3_64bits_update(xxhash, buffer);

                final long animationHash = XXHash.XXH3_64bits_digest(xxhash);

                final Model m = models.get(modelHash);

                if (!m.animations.containsKey(animationHash)) {
                  m.animations.put(animationHash, new Sobj(Long.toHexString(animationHash), animation.data));
                }
              }
            }

            if(modelHash == 0xcde376968929e010L ||
              modelHash == 0x8b0b1d7e962202d7L ||
              modelHash == 0x3558c02f033d9b6fL) {

              folder.flags.add("ReplaceDart");
            }
          }
        }
      }
    }

    final PathNode sobjFolder = new PathNode("sobj", "sobj", null, root);

    root.addChild(sobjFolder);

    for(final Model entry : models.values()) {
      final PathNode folder = createChild(sobjFolder, entry.name, null);

      final PathNode model = createChild(folder, "model", entry.file);

      final PathNode textures = createChild(folder, "textures", null);

      for(final Sobj textureEntry : entry.textures.values()) {
        final PathNode texture = createChild(textures, textureEntry.name, textureEntry.file);
      }

      final PathNode animations = createChild(folder, "animations", null);

      for(final Sobj animationEntry : entry.animations.values()) {
        final PathNode animation = createChild(animations, animationEntry.name, animationEntry.file);
      }
    }
  }

  private static String hashName(final long hash) {
    final Long2ObjectArrayMap<String> nameMap = new Long2ObjectArrayMap<>();

    nameMap.put(0x2318c28acc7d59efL, "Dart (close up)");
    nameMap.put(0xcde376968929e010L, "Dart (no soles)");
    nameMap.put(0x8b0b1d7e962202d7L, "Dart (soles 1");
    nameMap.put(0x3558c02f033d9b6fL, "Dart (soles 2)");
    nameMap.put(0x2d17b98b32afc6f2L, "Dart (running from Feyrbrand)");
    nameMap.put(0x25d4447842f22185L, "Dart (running from Feyrbrand 2)");
    nameMap.put(0x5a358d4acc8b8cf0L, "Dart (somewhat low quality)");
    nameMap.put(0x40c6cec6b3940c3fL, "Dart (double texture??)");
    nameMap.put(0x3cf93fb2988971dcL, "Dart (Dragoon)");
    nameMap.put(0x325fea7d6ed3e706L, "Dart (in minecart Shrine of Shirley)");
    nameMap.put(0x7c0894a11e988dfeL, "Dart with Axe");
    nameMap.put(0x5a972bcfa814bfceL, "Dart and Shana");


    nameMap.put(0x2daa60764ae0a13aL, "Lavitz");
    nameMap.put(0x9e365762aa7be500L, "Shana");
    nameMap.put(0x87a6063f04ef20c4L, "Shana 2");
    nameMap.put(0x9fc0bbb17badd24bL, "Shana (child)");
    nameMap.put(0x7ef35313566f3e8bL, "Rose Dragoon");
    nameMap.put(0x173c4d0979ebbfL, "Rose and Dart falling");
    nameMap.put(0x3f0d42bf15e02f5dL, "Haschel");
    nameMap.put(0x49d0132c58b16217L, "Haschel 2");
    nameMap.put(0x52f743343013d3d1L, "Haschel and Gehrich");
    nameMap.put(0x5db1b2d4eee8485dL, "Albert");
    nameMap.put(0x9d5f59959da80af2L, "Albert (captured)");
    nameMap.put(0x7e44e1f10695254cL, "Meru");
    nameMap.put(0x8f611ab5872306e6L, "Meru (Wingly wings)");
    nameMap.put(0x3ca6e705c06299f7L, "Miranda");
    nameMap.put(0x5a2b32ffa4b0f5L, "Miranda 2");
    nameMap.put(0x6f54a9e7b320dc31L, "Miranda 3");
    nameMap.put(0x7d9aaf9ec7ed9bfcL, "Miranda 4");

    nameMap.put(0x38c2b0d765c89480L, "Coolon");

    nameMap.put(0x3fc42ed3671d0e5aL, "Black monster");

    nameMap.put(0x1ac4389e8046bfdeL, "Lloyd (armor)");
    nameMap.put(0x81e6a86a3a2dd056L, "Lloyd (armor 2)");
    nameMap.put(0x81c158de102ec244L, "Lloyd (hood)");

    nameMap.put(0x33bfe49a1525a95bL, "Ulara Caron");
    nameMap.put(0x1cb0c49047bbeac4L, "Ulara Wingly 1");
    nameMap.put(0x4b001871a0d79794L, "Ulara Wingly (2d)");

    nameMap.put(0x1d4108395e1de259L, "King Albert (Bale)");

    nameMap.put(0x2da677315e4c3dfaL, "Dabas");
    nameMap.put(0x9d91f2c3bdfcd97bL, "Dabas 2");

    nameMap.put(0x2e950c3372819bb3L, "Seventh Fort Commander");

    nameMap.put(0x4d2cc7fee5596924L, "Kanzas");
    nameMap.put(0x3dca0c2eb8faf40eL, "Belzac");

    nameMap.put(0x9fc676ae4149ed01L, "Lenus (Dragoon)");
    nameMap.put(0x7944916d54092L, "Face");
    nameMap.put(0xc8b2e0d4b6257486L, "No Face");

    nameMap.put(0x3e5ead26f12e25dcL, "Melbu Frahma (2d)");

    nameMap.put(0x3e6f42cea8ee85f3L, "Forest of Winglies Wingly");
    nameMap.put(0x7b836238311f452eL, "Green");
    nameMap.put(0x93ebf98ef9944964L, "Blue");
    nameMap.put(0x25a5357dbfd02dffL, "Forest of Winglies Elder");

    nameMap.put(0x1b8624a91e184955L, "Portcullis");
    nameMap.put(0x1be28eee7e9b4fefL, "Bird");
    nameMap.put(0x5b5b016b54021e1fL, "Bird 2");
    nameMap.put(0x1cb0ebb452fcf4faL, "Nest of Dragon roots");
    nameMap.put(0x6b27867ae02b56b3L, "Nest of Dragon flower");
    nameMap.put(0x1d5fd9275f827a50L, "Unconscious Shana (Melbu 2d)");
    nameMap.put(0x83d74c6098ef18d9L, "Lohan minigame bird hat dude");
    nameMap.put(0x1fa70222ecb29c41L, "Lohan minigame selector?");
    nameMap.put(0x3fae2b0845dd62b9L, "Lohan minigame selector? (double texture)");
    nameMap.put(0x2d40b463c3388596L, "Lohan minigame tree Left (monkeys)");
    nameMap.put(0x2e0eac6688747234L, "Lohan minigame tree Right (monkeys)");
    nameMap.put(0x47ad85415a08e852L, "Lohan minigame shrub (monkeys)");
    nameMap.put(0x3f7865f2db2ca77fL, "Lohan minigame conveyor belt");
    nameMap.put(0x5b8c7d78340a2771L, "Lohan minigame spinner");
    nameMap.put(0x29a784ca8e66eb06L, "Lohan bottle Merchant");
    nameMap.put(0x5d7a469a287efb8eL, "Hero Competition Ginger");
    nameMap.put(0x96e63b15b68c9f13L, "Hero Competition Danton");
    nameMap.put(0x4ce57031016a482cL, "Lohan Sanator");
    nameMap.put(0x2b4996c6495f1d8eL, "Queen Fury vegetable (Green Spinach)");
    nameMap.put(0x12f188788a343c6eL, "Queen Fury vegetable (White Radish)");
    nameMap.put(0x3bcb329925e27ca9L, "Queen Fury Sailor");
    nameMap.put(0x6d8467995cb2e811L, "Queen Fury Kayla");
    nameMap.put(0x2c00a9d02b183e02L, "Marshlands water splash");
    nameMap.put(0x2d9e8547e7a2b8ebL, "Fletz Guard");
    nameMap.put(0x97bc21af6290806aL, "Fletz Guard 2");
    nameMap.put(0x3c6c7c51fc348ca3L, "Fletz ball dancing pair (2d)");
    nameMap.put(0x62feccb4326af60cL, "Fletz ball dancing pair (2d 2)");
    nameMap.put(0x5dfcb1de8ac60bffL, "Fletz Libria");
    nameMap.put(0x8ed05839b786fa5bL, "Fletz Kaffi");
    nameMap.put(0x9a17a2d2f4bb7835L, "Fletz Nello");
    nameMap.put(0x46e49ffb28ff5a29L, "Fletz Princess Lisa");
    nameMap.put(0x88ba7aba7348003bL, "Fletz Painting Bridge");
    nameMap.put(0x7bed628c7b769335L, "Tiberoa Innkeeper");
    nameMap.put(0x9aae80c3c65b4f43L, "Tiberoa NPC");
    nameMap.put(0x24e355705179851cL, "Tiberoa NPC 2");
    nameMap.put(0x27aa8c4f282a9debL, "Tiberoa NPC (old)");
    nameMap.put(0x28b72f5fe90b60e2L, "Tiberoa NPC (old 2)");
    nameMap.put(0x2ea37368e8dcd6feL, "Sandora Soldier");
    nameMap.put(0x2fc65d0646ae9dcdL, "Sandora Soldier (mounted)");
    nameMap.put(0x5df10165b5384fe2L, "Sandora Soldier Bale Knight fighting");
    nameMap.put(0x2f209c721c6a8468L, "Hellena Warden (2d)");
    nameMap.put(0x8be218479a80db68L, "Hellena Warden (2d) behind");
    nameMap.put(0x9b5b24feec3f63d0L, "Hellena Warden (2d) side right");
    nameMap.put(0x42a78fc6f8a6f9b4L, "Hellena Warden (2d) side left");
    nameMap.put(0x42eb61ca6e123bc9L, "Hellena Prisoner");
    nameMap.put(0x9ea86d9ec18a251L, "Hellena Lift");
    nameMap.put(0x17b4110a17f47da2L, "Fruegel");
    nameMap.put(0x87cccbc9bde8f40L, "Fruegel 2");
    nameMap.put(0x3a5e14116d82697aL, "Claire");
    nameMap.put(0x4fc0041bf5678934L, "Claire (adult)");
    nameMap.put(0x9b8c72f593bdbfb8L, "Claire (Neet)");
    nameMap.put(0x3aa9d8fabfecd150L, "Chest 2");
    nameMap.put(0x3e3382370bccbceeL, "Ghost Ship Nanny");
    nameMap.put(0x17e2c948b2f5c668L, "Ghost Ship Ghost sprite");
    nameMap.put(0x9d19c36935a51c0fL, "Ghost Ship Captain");
    nameMap.put(0x4e7c0cf91df551deL, "Wingly wings");
    nameMap.put(0x4f911e3bd3b6ee01L, "Weapons on ground (possibly Lloyd scene?");
    nameMap.put(0x4fbbe8ba9f3381c5L, "Mappi");
    nameMap.put(0x5a2eeb25710c46d2L, "Aglis door");
    nameMap.put(0x5e7d5a7455a3a890L, "Aglis Kraken");
    nameMap.put(0x5e25e531215e38e0L, "Shana's Father");
    nameMap.put(0x8c9371b321abe843L, "Shana's Mother");
    nameMap.put(0x6a1a90b7c57ab664L, "Pelpee");
    nameMap.put(0x6a1b8a0e5e24181L, "Shrine of Shirley minecart");
    nameMap.put(0x8a67b3ec07e04184L, "Shrine of Shirley ladder");
    nameMap.put(0x6c0edde90c7a3b2fL, "Arrow");
    nameMap.put(0x6c7c169dd0ed3a2bL, "Kamuy Warrior Blue Shirt");
    nameMap.put(0x6c0091443370919fL, "Bouquet");
    nameMap.put(0x6ceac29b4ee83f90L, "Seles Forest Wolf");
    nameMap.put(0x18d2e9201024b10eL, "Seles Commander");
    nameMap.put(0x6ee216d115d76e05L, "Emperor Doel");
    nameMap.put(0x9eb2462bd01287c1L, "Emperor Doel (mounted)");
    nameMap.put(0x6f0dd2657da78d67L, "Rogue NPC");
    nameMap.put(0x8a61482f3db3d047L, "Rogue NPC 2");
    nameMap.put(0x7b2e31e749a9bceaL, "Zenebatos Lapto (2d)");
    nameMap.put(0x39b4d0c6f8d488a3L, "Zenebatos Lapto (2d big)");
    nameMap.put(0x7cfd4d9827c1968bL, "Zieg Unconscious");
    nameMap.put(0x7f88140f878163bdL, "Monster Plant");
    nameMap.put(0x8b769f7b1db73f44L, "Death Frontier Whirpool effect");
    nameMap.put(0x8d9d705c8b3e101aL, "Barrens Wyvern");
    nameMap.put(0x8db85e8578f91219L, "Drake the Bandit");
    nameMap.put(0x193a48e613cfe088L, "Drake the Bandit (2d)");
    nameMap.put(0x8e2e1082206df706L, "Home of Gigantos Rock head");
    nameMap.put(0x9ddd7eebe0128d41L, "Home of Gigantos Sliding Stairs");
    nameMap.put(0x14e001000723c203L, "Home of Gigantos Bandit (Bow)");
    nameMap.put(0x203cde9d42adee88L, "Home of Gigantos Round Door");
    nameMap.put(0x8e7a2f8d1d43598L, "Faust Falling Rock");
    nameMap.put(0x9b519a67e81307baL, "Black Castle Chairlift");
    nameMap.put(0x67d91209fc8be6caL, "Black Castle Lift");
    nameMap.put(0x9be57a3c152511b8L, "Donau NPC");
    nameMap.put(0x20db8f84b980ae4cL, "Pete");
    nameMap.put(0x29e80af633a4d0c4L, "Bishop Dille");
    nameMap.put(0x34b4bc7b638a051eL, "Moon Minitos");
    nameMap.put(0x44a83529704de15fL, "Soul Eater");
    nameMap.put(0x49baab59af47d25cL, "Kamuy");
    nameMap.put(0x51d35d1a101be953L, "Moon Mirror");
    nameMap.put(0x58acc98fdea592f3L, "Bale NPC");
    nameMap.put(0x62d523106be2c8a1L, "Tree");
    nameMap.put(0x66acc7f1a655e4c5L, "Queen Theresa");
    nameMap.put(0x66fba27ae69e7ea0L, "Sandworm");
    nameMap.put(0x68d3cfa1adc3caebL, "Boat");
    nameMap.put(0x82da0f0d9af3190fL, "Boat (Furni)");
    nameMap.put(0x68e13b0fe5660b77L, "Mouse (Hellena?)");
    nameMap.put(0x72d0cbd55141e4e1L, "Kadessa Spinning Head");
    nameMap.put(0x190d41d14c4e3080L, "Dragon Block Staff");
    nameMap.put(0x292d49aac83dea2L, "Luanna");
    nameMap.put(0x370f1710e131f48aL, "Martel");

    return nameMap.getOrDefault(hash, Long.toHexString(hash));
  }

  private static PathNode createChild(final PathNode parent, final String segment, final FileData data) {
    final PathNode node = new PathNode(parent.fullPath + '/' + segment, segment, data, parent);

    parent.addChild(node);

    return node;
  }

  private static int countMrg(final PathNode parent) {
    final PathNode mrg = parent.children.get("mrg");

    if(mrg == null) {
      return 0;
    }

    final String mrgContents = mrg.data.readFixedLengthAscii(0x0, mrg.data.size());

    return (int)mrgContents.lines().count();
  }


  private static class Model {
    final String name;
    final FileData file;
    final Long2ObjectArrayMap<Sobj> textures = new Long2ObjectArrayMap<>();
    final Long2ObjectArrayMap<Sobj> animations = new Long2ObjectArrayMap<>();

    Model(final String name, final FileData file) {
      this.name = name;
      this.file = file;
    }
  }

  private static class Sobj {
    String name;
    FileData file;

    Sobj(final String name, final FileData file) {
      this.name = name;
      this.file = file;
    }
  }
}
