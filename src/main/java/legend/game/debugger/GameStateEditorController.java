package legend.game.debugger;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import legend.game.modding.coremod.CoreMod;
import legend.game.types.EquipmentSlot;
import legend.game.types.Flags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.REGISTRIES;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class GameStateEditorController {

  private static final Logger LOGGER = LogManager.getFormatterLogger(GameStateEditorController.class);

  String[] characters = {"Dart", "Lavitz", "Shana", "Rose", "Haschel", "Albert", "Meru", "Kongol", "???"};
  String[] characterData = {"EXP", "Party Flags", "HP", "MP", "SP", "Dragoon EXP", "Status", "Level", "Dragoon Level", "Equipment Slot 1", "Equipment Slot 2", "Equipment Slot 3", "Equipment Slot 4", "Equipment Slot 5", "Addition", "Addition Level Slot 1", "Addition Level Slot 2", "Addition Level Slot 3", "Addition Level Slot 4", "Addition Level Slot 5", "Addition Level Slot 6", "Addition Level Slot 7", "Addition Level Slot 8", "Addition EXP Slot 1", "Addition EXP Slot 2", "Addition EXP Slot 3", "Addition EXP Slot 4", "Addition EXP Slot 5", "Addition EXP Slot 6", "Addition EXP Slot 7", "Addition EXP Slot 8"};
  @FXML
  public TextField textData1;
  @FXML
  public ComboBox<Integer> getScriptData;
  @FXML
  public TextField textScriptData;
  @FXML
  public ComboBox<Integer> getParty;
  @FXML
  public TextField textParty;
  @FXML
  public TextField textGold;
  @FXML
  public TextField textChapter;
  @FXML
  public TextField textStardust;
  @FXML
  public TextField textTimestamp;
  @FXML
  public TextField textSubmapScene;
  @FXML
  public TextField textSubmapCut;
  @FXML
  public TextField textScriptEngine1;
  @FXML
  public TextField textScriptEngine2;
  @FXML
  public TextField textScriptEngine3;
  @FXML
  public ComboBox<Integer> getScriptFlags1;
  @FXML
  public TextField textScriptFlags1;
  @FXML
  public ComboBox<Integer> getScriptFlags2;
  @FXML
  public TextField textScriptFlags2;
  @FXML
  public ComboBox<Integer> getData2;
  @FXML
  public TextField textData2;
  @FXML
  public ComboBox<Integer> getData3;
  @FXML
  public TextField textData3;
  @FXML
  public ComboBox<Integer> getGoods;
  @FXML
  public TextField textGoods;
  @FXML
  public ComboBox<Integer> getData4;
  @FXML
  public TextField textData4;
  @FXML
  public ComboBox<Integer> getChestFlags;
  @FXML
  public TextField textChestFlags;
  @FXML
  public Spinner<Integer> getEquipment;
  @FXML
  public TextField textEquipment;
  @FXML
  public Spinner<Integer> getItems;
  @FXML
  public TextField textItems;
  @FXML
  public ComboBox<String> getCharacter;
  @FXML
  public ComboBox<String> getCharacterData;
  @FXML
  public TextField textCharacterData;
  @FXML
  public TextField textPathIndex;
  @FXML
  public TextField textDotIndex;
  @FXML
  public TextField textDotOffset;
  @FXML
  public TextField textFacing;
  @FXML
  public TextField textAreaIndex;

  public void initialize() {
    this.textData1.setText(String.format("%#x", gameState_800babc8._04));

    for(int i = 0; i < 0x20; i++) {
      this.getScriptData.getItems().add(i);
    }
    this.getScriptData.getSelectionModel().select(0);
    this.getScriptData();

    for(int i = 0; i < 3; i++) {
      this.getParty.getItems().add(i);
    }
    this.getParty.getSelectionModel().select(0);
    this.getParty();

    this.textGold.setText(String.valueOf(gameState_800babc8.gold_94));
    this.textChapter.setText(String.valueOf(gameState_800babc8.chapterIndex_98));
    this.textStardust.setText(String.valueOf(gameState_800babc8.stardust_9c));
    this.textTimestamp.setText(String.valueOf(gameState_800babc8.timestamp_a0));
    this.textSubmapScene.setText(String.valueOf(gameState_800babc8.submapScene_a4));
    this.textSubmapCut.setText(String.valueOf(gameState_800babc8.submapCut_a8));
    this.textScriptEngine1.setText(String.format("%#x", gameState_800babc8._b0));
    this.textScriptEngine2.setText(String.format("%#x", gameState_800babc8._b4));
    this.textScriptEngine3.setText(String.format("%#x", gameState_800babc8._b8));

    for(int i = 0; i < 0x20; i++) {
      this.getScriptFlags1.getItems().add(i);
    }
    this.getScriptFlags1.getSelectionModel().select(0);
    this.getScriptFlags1();

    for(int i = 0; i < 8; i++) {
      this.getScriptFlags2.getItems().add(i);
    }
    this.getScriptFlags2.getSelectionModel().select(0);
    this.getScriptFlags2();

    for(int i = 0; i < 8; i++) {
      this.getData2.getItems().add(i);
    }
    this.getData2.getSelectionModel().select(0);
    this.getData2();

    for(int i = 0; i < 8; i++) {
      this.getData3.getItems().add(i);
    }
    this.getData3.getSelectionModel().select(0);
    this.getData3();

    this.getGoods.getItems().add(0);
    this.getGoods.getItems().add(1);
    this.getGoods.getSelectionModel().select(0);
    this.getGoods();

    for(int i = 0; i < 8; i++) {
      this.getData4.getItems().add(i);
    }
    this.getData4.getSelectionModel().select(0);
    this.getData4();

    for(int i = 0; i < 8; i++) {
      this.getChestFlags.getItems().add(i);
    }
    this.getChestFlags.getSelectionModel().select(0);
    this.getChestFlags();

    this.getEquipment.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 254, 0));
    this.getEquipment.valueProperty().addListener((observable, oldValue, newValue) -> {
      if(!oldValue.equals(newValue)) {
        this.getEquipment();
      }
    });
    this.getEquipment();

    this.getItems.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, CONFIG.getConfig(CoreMod.INVENTORY_SIZE_CONFIG.get()), 0));
    this.getItems.valueProperty().addListener((observable, oldValue, newValue) -> {
      if(!oldValue.equals(newValue)) {
        this.getItems();
      }
    });
    this.getItems();

    for(final String character : this.characters) {
      this.getCharacter.getItems().add(character);
    }
    this.getCharacter.getSelectionModel().select(0);

    for(final String characterDatum : this.characterData) {
      this.getCharacterData.getItems().add(characterDatum);
    }
    this.getCharacterData.getSelectionModel().select(0);
    this.getCharacter();

    this.textPathIndex.setText(String.format("%#x", gameState_800babc8.pathIndex_4d8));
    this.textDotIndex.setText(String.format("%#x", gameState_800babc8.dotIndex_4da));
    this.textDotOffset.setText(String.valueOf(gameState_800babc8.dotOffset_4dc));
    this.textFacing.setText(String.valueOf(gameState_800babc8.facing_4dd));
    this.textAreaIndex.setText(String.format("%#x", gameState_800babc8.directionalPathIndex_4de));
  }

  private int parseHexOrDec(final String inputText, final int oldValue) {
    int newValue;
    try {
      newValue = inputText.toUpperCase().trim().startsWith("0X") ? (int)Long.parseLong(inputText.toUpperCase().trim().substring(2), 16) : Integer.parseInt(inputText);
    } catch(final NumberFormatException e) {
      newValue = oldValue;
      LOGGER.warn("Bad value entered: %s. Restoring previous value: %d", inputText, newValue);
    }

    return newValue;
  }

  private void parseHexOrDec(final String inputText, final int index, final Flags gameStateData) {
    int newValue;
    try {
      newValue = inputText.toUpperCase().trim().startsWith("0X") ? (int)Long.parseLong(inputText.toUpperCase().trim().substring(2), 16) : Integer.parseInt(inputText);
    } catch(final NumberFormatException e) {
      newValue = gameStateData.getRaw(index);
      LOGGER.warn("Bad value entered: %s. Restoring previous value: %d", inputText, newValue);
    }

    gameStateData.setRaw(index, newValue);
  }

  @FXML
  public void getData1() {
    this.textData1.setText(String.format("%#x", gameState_800babc8._04));
  }

  @FXML
  public void setData1() {
    gameState_800babc8._04 = this.parseHexOrDec(this.textData1.getText(), gameState_800babc8._04);
  }

  @FXML
  public void getScriptData() {
    this.textScriptData.setText(String.format("%#x", gameState_800babc8.scriptData_08[this.getScriptData.getSelectionModel().getSelectedIndex()]));
  }

  @FXML
  public void setScriptData() {
    gameState_800babc8.scriptData_08[this.getScriptData.getSelectionModel().getSelectedIndex()] = this.parseHexOrDec(this.textScriptData.getText(), gameState_800babc8.scriptData_08[this.getScriptData.getSelectionModel().getSelectedIndex()]);
  }

  @FXML
  public void getParty() {
    this.textParty.setText(String.valueOf(gameState_800babc8.charIds_88[this.getParty.getSelectionModel().getSelectedIndex()]));
  }

  @FXML
  public void setParty() {
    gameState_800babc8.charIds_88[this.getParty.getSelectionModel().getSelectedIndex()] = Integer.parseInt(this.textParty.getText());
  }

  @FXML
  public void getGold() {
    this.textGold.setText(String.valueOf(gameState_800babc8.gold_94));
  }

  @FXML
  public void setGold() {
    gameState_800babc8.gold_94 = Integer.parseInt(this.textGold.getText());
  }

  @FXML
  public void getChapter() {
    this.textChapter.setText(String.valueOf(gameState_800babc8.chapterIndex_98));
  }

  @FXML
  public void setChapter() {
    gameState_800babc8.chapterIndex_98 = Integer.parseInt(this.textChapter.getText());
  }

  @FXML
  public void getStardust() {
    this.textStardust.setText(String.valueOf(gameState_800babc8.stardust_9c));
  }

  @FXML
  public void setStardust() {
    gameState_800babc8.stardust_9c = Integer.parseInt(this.textStardust.getText());
  }

  @FXML
  public void getTimestamp() {
    this.textTimestamp.setText(String.valueOf(gameState_800babc8.timestamp_a0));
  }

  @FXML
  public void setTimestamp() {
    gameState_800babc8.timestamp_a0 = Integer.parseInt(this.textTimestamp.getText());
  }

  @FXML
  public void getSubmapScene() {
    this.textSubmapScene.setText(String.valueOf(gameState_800babc8.submapScene_a4));
  }

  @FXML
  public void setSubmapScene() {
    gameState_800babc8.submapScene_a4 = Integer.parseInt(this.textSubmapScene.getText());
  }

  @FXML
  public void getSubmapCut() {
    this.textSubmapCut.setText(String.valueOf(gameState_800babc8.submapCut_a8));
  }

  @FXML
  public void setSubmapCut() {
    gameState_800babc8.submapCut_a8 = Integer.parseInt(this.textSubmapCut.getText());
  }

  @FXML
  public void getScriptEngine1() {
    this.textScriptEngine1.setText(String.format("%#x", gameState_800babc8._b0));
  }

  @FXML
  public void setScriptEngine1() {
    gameState_800babc8._b0 = this.parseHexOrDec(this.textScriptEngine1.getText(), gameState_800babc8._b0);
  }

  @FXML
  public void getScriptEngine2() {
    this.textScriptEngine2.setText(String.format("%#x", gameState_800babc8._b4));
  }

  @FXML
  public void setScriptEngine2() {
    gameState_800babc8._b4 = this.parseHexOrDec(this.textScriptEngine2.getText(), gameState_800babc8._b4);
  }

  @FXML
  public void getScriptEngine3() {
    this.textScriptEngine3.setText(String.format("%#x", gameState_800babc8._b8));
  }

  @FXML
  public void setScriptEngine3() {
    gameState_800babc8._b8 = this.parseHexOrDec(this.textScriptEngine3.getText(), gameState_800babc8._b8);
  }

  @FXML
  public void getScriptFlags1() {
    this.textScriptFlags1.setText(String.format("%#x", gameState_800babc8.scriptFlags2_bc.getRaw(this.getScriptFlags1.getSelectionModel().getSelectedIndex())));
  }

  @FXML
  public void setScriptFlags1() {
    this.parseHexOrDec(this.textScriptFlags1.getText(), this.getScriptFlags1.getSelectionModel().getSelectedIndex(), gameState_800babc8.scriptFlags2_bc);
  }

  @FXML
  public void getScriptFlags2() {
    this.textScriptFlags2.setText(String.format("%#x", gameState_800babc8.scriptFlags1_13c.getRaw(this.getScriptFlags2.getSelectionModel().getSelectedIndex())));
  }

  @FXML
  public void setScriptFlags2() {
    this.parseHexOrDec(this.textScriptFlags2.getText(), this.getScriptFlags2.getSelectionModel().getSelectedIndex(), gameState_800babc8.scriptFlags1_13c);
  }

  @FXML
  public void getData2() {
    this.textData2.setText(String.format("%#x", gameState_800babc8.wmapFlags_15c.getRaw(this.getData2.getSelectionModel().getSelectedIndex())));
  }

  @FXML
  public void setData2() {
    this.parseHexOrDec(this.textData2.getText(), this.getData2.getSelectionModel().getSelectedIndex(), gameState_800babc8.wmapFlags_15c);
  }

  @FXML
  public void getData3() {
    this.textData3.setText(String.format("%#x", gameState_800babc8.visitedLocations_17c.getRaw(this.getData3.getSelectionModel().getSelectedIndex())));
  }

  @FXML
  public void setData3() {
    this.parseHexOrDec(this.textData3.getText(), this.getData3.getSelectionModel().getSelectedIndex(), gameState_800babc8.visitedLocations_17c);
  }

  @FXML
  public void getGoods() {
    this.textGoods.setText(String.format("%#x", gameState_800babc8.goods_19c[this.getGoods.getSelectionModel().getSelectedIndex()]));
  }

  @FXML
  public void setGoods() {
    gameState_800babc8.goods_19c[this.getGoods.getSelectionModel().getSelectedIndex()] = this.parseHexOrDec(this.textGoods.getText(), gameState_800babc8.goods_19c[this.getGoods.getSelectionModel().getSelectedIndex()]);
  }

  @FXML
  public void getData4() {
    this.textData4.setText(String.format("%#x", gameState_800babc8._1a4[this.getData4.getSelectionModel().getSelectedIndex()]));
  }

  @FXML
  public void setData4() {
    gameState_800babc8._1a4[this.getData4.getSelectionModel().getSelectedIndex()] = this.parseHexOrDec(this.textData4.getText(), gameState_800babc8._1a4[this.getData4.getSelectionModel().getSelectedIndex()]);
  }

  @FXML
  public void getChestFlags() {
    this.textChestFlags.setText(String.format("%#x", gameState_800babc8.chestFlags_1c4[this.getChestFlags.getSelectionModel().getSelectedIndex()]));
  }

  @FXML
  public void setChestFlags() {
    gameState_800babc8.chestFlags_1c4[this.getChestFlags.getSelectionModel().getSelectedIndex()] = this.parseHexOrDec(this.textChestFlags.getText(), gameState_800babc8.chestFlags_1c4[this.getChestFlags.getSelectionModel().getSelectedIndex()]);
  }

  public void getEquipment() {
    if(!gameState_800babc8.equipment_1e8.isEmpty()) {
      this.textEquipment.setText(gameState_800babc8.equipment_1e8.get(this.getEquipment.getValue()).getRegistryId().toString());
    } else {
      this.textEquipment.clear();
    }
  }

  @FXML
  public void setEquipment() {
    if(this.getEquipment.getValue() >= gameState_800babc8.equipment_1e8.size()) {
      gameState_800babc8.equipment_1e8.add(REGISTRIES.equipment.getEntry(this.textEquipment.getText()).get());
    } else {
      gameState_800babc8.equipment_1e8.set(this.getEquipment.getValue(), REGISTRIES.equipment.getEntry(this.textEquipment.getText()).get());
    }
  }

  public void getItems() {
    if(!gameState_800babc8.items_2e9.isEmpty()) {
      this.textItems.setText(gameState_800babc8.items_2e9.get(this.getItems.getValue()).getRegistryId().toString());
    } else {
      this.textEquipment.clear();
    }
  }

  @FXML
  public void setItems() {
    if(this.getItems.getValue() >= gameState_800babc8.items_2e9.size()) {
      gameState_800babc8.items_2e9.add(REGISTRIES.items.getEntry(this.textItems.getText()).get());
    } else {
      gameState_800babc8.items_2e9.set(this.getItems.getValue(), REGISTRIES.items.getEntry(this.textItems.getText()).get());
    }
  }

  @FXML
  public void getCharacter() {
    this.textCharacterData.setText(this.getCharacterStats());
  }

  @FXML
  public void getCharacterData() {
    this.textCharacterData.setText(this.getCharacterStats());
  }

  public String getCharacterStats() {
    return switch(this.getCharacterData.getSelectionModel().getSelectedIndex()) {
      case 0 -> String.valueOf(gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].xp_00);
      case 1 -> String.format("%#x", gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].partyFlags_04);
      case 2 -> String.valueOf(gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].hp_08);
      case 3 -> String.valueOf(gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].mp_0a);
      case 4 -> String.valueOf(gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].sp_0c);
      case 5 -> String.valueOf(gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].dlevelXp_0e);
      case 6 -> String.valueOf(gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].status_10);
      case 7 -> String.valueOf(gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].level_12);
      case 8 -> String.valueOf(gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].dlevel_13);
      case 9, 10, 11, 12, 13 -> gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].equipment_14.get(EquipmentSlot.fromLegacy(this.getCharacterData.getSelectionModel().getSelectedIndex() - 9)).getRegistryId().toString();
      case 14 -> String.valueOf(gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].selectedAddition_19);
      case 15, 16, 17, 18, 19, 20, 21, 22 -> String.valueOf(gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].additionLevels_1a[this.getCharacterData.getSelectionModel().getSelectedIndex() - 15]);
      case 23, 24, 25, 26, 27, 28, 29, 30 -> String.valueOf(gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].additionXp_22[this.getCharacterData.getSelectionModel().getSelectedIndex() - 23]);
      default -> "";
    };
  }

  @FXML
  public void setCharacterData() {
    switch(this.getCharacterData.getSelectionModel().getSelectedIndex()) {
      case 0 -> gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].xp_00 = Integer.parseInt(this.textCharacterData.getText());
      case 1 -> gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].partyFlags_04 = this.parseHexOrDec(this.textCharacterData.getText(), gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].partyFlags_04);
      case 2 -> gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].hp_08 = Integer.parseInt(this.textCharacterData.getText());
      case 3 -> gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].mp_0a = Integer.parseInt(this.textCharacterData.getText());
      case 4 -> gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].sp_0c = Integer.parseInt(this.textCharacterData.getText());
      case 5 -> gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].dlevelXp_0e = Integer.parseInt(this.textCharacterData.getText());
      case 6 -> gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].status_10 = Integer.parseInt(this.textCharacterData.getText());
      case 7 -> gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].level_12 = Integer.parseInt(this.textCharacterData.getText());
      case 8 -> gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].dlevel_13 = Integer.parseInt(this.textCharacterData.getText());
      case 9, 10, 11, 12, 13 -> gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].equipment_14.put(EquipmentSlot.fromLegacy(this.getCharacterData.getSelectionModel().getSelectedIndex() - 9), REGISTRIES.equipment.getEntry(this.textCharacterData.getText()).get());
      case 14 -> gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].selectedAddition_19 = Integer.parseInt(this.textCharacterData.getText());
      case 15, 16, 17, 18, 19, 20, 21, 22 -> gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].additionLevels_1a[this.getCharacterData.getSelectionModel().getSelectedIndex() - 15] = Integer.parseInt(this.textCharacterData.getText());
      case 23, 24, 25, 26, 27, 28, 29, 30 -> gameState_800babc8.charData_32c[this.getCharacter.getSelectionModel().getSelectedIndex()].additionXp_22[this.getCharacterData.getSelectionModel().getSelectedIndex() - 23] = Integer.parseInt(this.textCharacterData.getText());
    }
  }

  @FXML
  public void getPathIndex() {
    this.textPathIndex.setText(String.format("%#x", gameState_800babc8.pathIndex_4d8));
  }

  @FXML
  public void setPathIndex() {
    gameState_800babc8.pathIndex_4d8 = this.parseHexOrDec(this.textPathIndex.getText(), gameState_800babc8.pathIndex_4d8);
  }

  @FXML
  public void getDotIndex() {
    this.textDotIndex.setText(String.format("%#x", gameState_800babc8.dotIndex_4da));
  }

  @FXML
  public void setDotIndex() {
    gameState_800babc8.dotIndex_4da = this.parseHexOrDec(this.textDotIndex.getText(), gameState_800babc8.dotIndex_4da);
  }

  @FXML
  public void getDotOffset() {
    this.textDotOffset.setText(String.valueOf(gameState_800babc8.dotOffset_4dc));
  }

  @FXML
  public void setDotOffset() {
    gameState_800babc8.dotOffset_4dc = Float.parseFloat(this.textDotOffset.getText());
  }

  @FXML
  public void getFacing() {
    this.textFacing.setText(String.valueOf(gameState_800babc8.facing_4dd));
  }

  @FXML
  public void setFacing() {
    gameState_800babc8.facing_4dd = Integer.parseInt(this.textFacing.getText());
  }

  @FXML
  public void getAreaIndex() {
    this.textAreaIndex.setText(String.format("%#x", gameState_800babc8.directionalPathIndex_4de));
  }

  @FXML
  public void setAreaIndex() {
    gameState_800babc8.directionalPathIndex_4de = this.parseHexOrDec(this.textAreaIndex.getText(), gameState_800babc8.directionalPathIndex_4de);
  }
}
