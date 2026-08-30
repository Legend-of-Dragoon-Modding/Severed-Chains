package legend.game.saves.serializers;

import legend.core.gpu.Rect4i;
import legend.core.memory.types.IntRef;
import legend.core.tags.IntTag;
import legend.core.tags.ListTag;
import legend.core.tags.MapTag;
import legend.core.tags.RawTag;
import legend.core.tags.RegistryIdTag;
import legend.core.tags.StringTag;
import legend.core.tags.Tag;
import legend.game.EngineState;
import legend.game.characters.CharacterData2c;
import legend.game.characters.CharacterTemplate;
import legend.game.inventory.Equipment;
import legend.game.inventory.Good;
import legend.game.inventory.ItemStack;
import legend.game.saves.Campaign;
import legend.game.saves.CampaignType;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.InventoryEntry;
import legend.game.saves.SaveVersion;
import legend.game.saves.SavedGame;
import legend.game.saves.SeveredSavedGame;
import legend.game.textures.PngWriter;
import legend.game.textures.TexturePacker;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryId;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.REGISTRIES;

public final class V10Serializer {
  private V10Serializer() { }

  public static SavedGame fromV10(final SaveVersion version, final Campaign campaign, final String filename, final FileData data) {
    final IntRef offset = new IntRef();
    final MapTag tag = new MapTag();
    tag.deserialize(data, offset);

    final String name = tag.get("saveName").asString().get();
    final RegistryId campaignTypeId = tag.get("campaignTypeId").asRegistryId().get();
    final String locationName = tag.get("locationName").asString().get();

    final MapTag atlas = tag.get("atlas").asMap();
    final int atlasWidth = atlas.get("width").asInt().get();
    final int atlasHeight = atlas.get("height").asInt().get();
    final FileData atlasData = new FileData(atlas.get("data").asRaw().get());

    final ConfigCollection config = new ConfigCollection();
    final SeveredSavedGame savedGame = new SeveredSavedGame(campaign, version.name, filename, name, campaignTypeId, config, atlasData, atlasWidth, atlasHeight);

    final ListTag scriptDataTag = tag.get("scriptData").asList();
    for(int i = 0; i < savedGame.scriptData.length; i++) {
      savedGame.scriptData[i] = scriptDataTag.get(i).asInt().get();
    }

    final ListTag activePartyTag = tag.get("activeParty").asList();
    for(int i = 0; i < activePartyTag.size(); i++) {
      savedGame.activeParty.add(activePartyTag.get(i).asInt().get());
    }

    savedGame.gold = tag.get("gold").asInt().get();
    savedGame.chapterIndex = tag.get("chapterIndex").asInt().get();
    savedGame.stardust = tag.get("stardust").asInt().get();
    savedGame.timestamp = tag.get("timestamp").asInt().get();

    savedGame._b0 = tag.get("_b0").asInt().get();
    savedGame.battleCount = tag.get("battleCount").asInt().get();
    savedGame.turnCount = tag.get("turnCount").asInt().get();

    final ListTag scriptFlags2Tag = tag.get("scriptFlags2").asList();
    for(int i = 0; i < savedGame.scriptFlags2.count(); i++) {
      savedGame.scriptFlags2.setRaw(i, scriptFlags2Tag.get(i).asInt().get());
    }

    final ListTag scriptFlags1Tag = tag.get("scriptFlags1").asList();
    for(int i = 0; i < savedGame.scriptFlags1.count(); i++) {
      savedGame.scriptFlags1.setRaw(i, scriptFlags1Tag.get(i).asInt().get());
    }

    final ListTag wmapFlagsTag = tag.get("wmapFlags").asList();
    for(int i = 0; i < savedGame.wmapFlags.count(); i++) {
      savedGame.wmapFlags.setRaw(i, wmapFlagsTag.get(i).asInt().get());
    }

    final ListTag visitedLocationsTag = tag.get("visitedLocations").asList();
    for(int i = 0; i < savedGame.visitedLocations.count(); i++) {
      savedGame.visitedLocations.setRaw(i, visitedLocationsTag.get(i).asInt().get());
    }

    final ListTag _1a4Tag = tag.get("_1a4").asList();
    for(int i = 0; i < savedGame._1a4.length; i++) {
      savedGame._1a4[i] = _1a4Tag.get(i).asInt().get();
    }

    final ListTag chestFlagsTag = tag.get("chestFlags").asList();
    for(int i = 0; i < savedGame.chestFlags.length; i++) {
      savedGame.chestFlags[i] = chestFlagsTag.get(i).asInt().get();
    }

    final ListTag equips = tag.get("equipment").asList();
    for(int i = 0; i < equips.size(); i++) {
      final MapTag equip = equips.get(i).asMap();
      savedGame.equipmentIds.add(equip.get("equipmentId").asRegistryId().get());
    }

    final ListTag items = tag.get("items").asList();
    for(int i = 0; i < items.size(); i++) {
      final MapTag item = items.get(i).asMap();
      final RegistryId itemId = item.get("itemId").asRegistryId().get();
      final int size = item.get("size").asInt().get();
      final int durability = item.get("durability").asInt().get();
      final Tag extraData = item.get("extraData");

      savedGame.itemIds.add(new InventoryEntry(itemId, size, durability, extraData));
    }

    final ListTag goodsTag = tag.get("goods").asList();
    for(int i = 0; i < goodsTag.size(); i++) {
      final MapTag goodTag = goodsTag.get(i).asMap();
      savedGame.goodsIds.add(goodTag.get("goodId").asRegistryId().get());
    }

    final ListTag charactersTag = tag.get("characters").asList();
    for(int charIndex = 0; charIndex < charactersTag.size(); charIndex++) {
      final MapTag characterTag = charactersTag.get(charIndex).asMap();
      final RegistryId templateId = characterTag.get("templateId").asRegistryId().get();
      final CharacterTemplate template = REGISTRIES.characterTemplates.getEntry(templateId).get();
      savedGame.characters.add(template.deserialize(characterTag));
      savedGame.charPortraits.add(new Rect4i(characterTag.get("portraitX").asInt().get(), characterTag.get("portraitY").asInt().get(), characterTag.get("portraitW").asInt().get(), characterTag.get("portraitH").asInt().get()));
    }

    final RegistryId engineStateId = tag.get("engineStateId").asRegistryId().get();
    final Tag engineStateData = tag.get("engineStateData");

    ConfigStorage.loadConfig(config, ConfigStorageLocation.SAVE, data.slice(offset.get()));

    savedGame.locationName = locationName;
    savedGame.engineState = engineStateId;
    savedGame.engineStateData = engineStateData;

    return savedGame;
  }

  public static void toV10(final String name, final FileData data, final IntRef offset, final CampaignType campaignType, final EngineState<?> engineState, final GameState52c gameState) {
    final TexturePacker packer = new TexturePacker("Save " + name);

    for(final CharacterData2c character : gameState.charData_32c) {
      packer.add(character.template.getRegistryId(), character.template.loadPortrait());
    }

    final byte[] atlas = packer.packToBytes(512, 512);
    final ByteBuffer buffer = BufferUtils.createByteBuffer(atlas.length);
    buffer.put(0, atlas);
    final byte[] compressed = PngWriter.compress(buffer, 512, 512);

    final MapTag tag = new MapTag();

    tag.set("saveName", new StringTag(name));
    tag.set("campaignTypeId", new RegistryIdTag(campaignType.getRegistryId()));
    tag.set("locationName", new StringTag(engineState.getLocation(gameState)));

    final MapTag atlasTag = new MapTag();
    tag.set("atlas", atlasTag);
    atlasTag.set("width", new IntTag(512));
    atlasTag.set("height", new IntTag(512));
    atlasTag.set("data", new RawTag(compressed));

    final ListTag scriptDataTag = new ListTag();
    tag.set("scriptData", scriptDataTag);
    for(final int scriptData : gameState.scriptData_08) {
      scriptDataTag.add(new IntTag(scriptData));
    }

    final ListTag activePartyTag = new ListTag();
    tag.set("activeParty", activePartyTag);
    for(final int charIndex : gameState.charIds_88) {
      activePartyTag.add(new IntTag(charIndex));
    }

    tag.set("gold", new IntTag(gameState.gold_94));
    tag.set("chapterIndex", new IntTag(gameState.chapterIndex_98));
    tag.set("stardust", new IntTag(gameState.stardust_9c));
    tag.set("timestamp", new IntTag(gameState.timestamp_a0));

    tag.set("_b0", new IntTag(gameState._b0));
    tag.set("battleCount", new IntTag(gameState.battleCount_b4));
    tag.set("turnCount", new IntTag(gameState.turnCount_b8));

    final ListTag scriptFlags2Tag = new ListTag();
    tag.set("scriptFlags2", scriptFlags2Tag);
    for(int i = 0; i < gameState.scriptFlags2_bc.count(); i++) {
      scriptFlags2Tag.add(new IntTag(gameState.scriptFlags2_bc.getRaw(i)));
    }

    final ListTag scriptFlags1Tag = new ListTag();
    tag.set("scriptFlags1", scriptFlags1Tag);
    for(int i = 0; i < gameState.scriptFlags1_13c.count(); i++) {
      scriptFlags1Tag.add(new IntTag(gameState.scriptFlags1_13c.getRaw(i)));
    }

    final ListTag wmapFlagsTag = new ListTag();
    tag.set("wmapFlags", wmapFlagsTag);
    for(int i = 0; i < gameState.wmapFlags_15c.count(); i++) {
      wmapFlagsTag.add(new IntTag(gameState.wmapFlags_15c.getRaw(i)));
    }

    final ListTag visitedLocationsTag = new ListTag();
    tag.set("visitedLocations", visitedLocationsTag);
    for(int i = 0; i < gameState.visitedLocations_17c.count(); i++) {
      visitedLocationsTag.add(new IntTag(gameState.visitedLocations_17c.getRaw(i)));
    }

    final ListTag _1a4Tag = new ListTag();
    tag.set("_1a4", _1a4Tag);
    for(final int _1a4 : gameState._1a4) {
      _1a4Tag.add(new IntTag(_1a4));
    }

    final ListTag chestFlagsTag = new ListTag();
    tag.set("chestFlags", chestFlagsTag);
    for(final int chestFlag : gameState.chestFlags_1c4) {
      chestFlagsTag.add(new IntTag(chestFlag));
    }

    final ListTag equipsTag = new ListTag();
    tag.set("equipment", equipsTag);
    for(final Equipment equipment : gameState.equipment_1e8) {
      final MapTag equipTag = new MapTag();
      equipsTag.add(equipTag);
      equipTag.set("equipmentId", new RegistryIdTag(equipment));
    }

    final ListTag itemsTag = new ListTag();
    tag.set("items", itemsTag);
    for(final ItemStack stack : gameState.items_2e9) {
      final MapTag itemTag = new MapTag();
      itemsTag.add(itemTag);
      itemTag.set("itemId", new RegistryIdTag(stack.getItem()));
      itemTag.set("size", new IntTag(stack.getSize()));
      itemTag.set("durability", new IntTag(stack.getCurrentDurability()));

      final Tag extraData = stack.getExtraData();

      if(extraData != null) {
        itemTag.set("extraData", extraData);
      }
    }

    final ListTag goodsTag = new ListTag();
    tag.set("goods", goodsTag);
    for(final Good good : gameState.goods_19c) {
      final MapTag goodTag = new MapTag();
      goodsTag.add(goodTag);
      goodTag.set("goodId", new RegistryIdTag(good.getRegistryId()));
    }

    final ListTag charactersTag = new ListTag();
    tag.set("characters", charactersTag);
    for(int i = 0; i < gameState.charData_32c.size(); i++) {
      final MapTag characterTag = new MapTag();
      charactersTag.add(characterTag);

      final CharacterData2c character = gameState.charData_32c.get(i);
      characterTag.set("templateId", new RegistryIdTag(character.template));
      character.template.serialize(character, characterTag);

      final Rect4i rect = packer.getRect(character.template.getRegistryId());
      characterTag.set("portraitX", new IntTag(rect.x));
      characterTag.set("portraitY", new IntTag(rect.y));
      characterTag.set("portraitW", new IntTag(rect.w));
      characterTag.set("portraitH", new IntTag(rect.h));
    }

    tag.set("engineStateId", new RegistryIdTag(engineState.type));
    tag.set("engineStateData", engineState.writeSaveData(gameState));

    tag.serialize(data, offset);

    ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.SAVE, data, offset);
  }
}
