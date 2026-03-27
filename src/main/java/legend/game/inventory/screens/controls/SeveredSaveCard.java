package legend.game.inventory.screens.controls;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gpu.Rect4i;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.Texture;
import legend.core.platform.input.InputAction;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.InputPropagation;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.SeveredSavedGame;
import legend.game.textures.ImageLoader;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static legend.core.GameEngine.RENDERER;
import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.UI_TEXT_CENTERED;
import static legend.game.SItem.getTimestampPart;
import static legend.game.Text.renderText;
import static legend.game.sound.Audio.playMenuSound;

public class SeveredSaveCard extends BlankSaveCard {
  private static final FontOptions TINY = new FontOptions().set(UI_TEXT).size(0.4f);

  private final Glyph highlight;

  private final SeveredSavedGame savedGame;
  private final IntList charIndices = new IntArrayList();

  private int scroll;
  private int selectedCharacter;

  private final Texture texture;
  private final Obj obj;

  private final IntList charVertices = new IntArrayList();
  private final MV transforms = new MV();

  public SeveredSaveCard(final SeveredSavedGame savedGame) {
    this.savedGame = savedGame;

    final ByteBuffer buffer = BufferUtils.createByteBuffer(savedGame.atlas.size());
    savedGame.atlas.read(0, buffer, 0, savedGame.atlas.size());
    this.texture = Texture.fromImage(ImageLoader.safeLoadImage(buffer));

    this.charIndices.addAll(savedGame.activeParty);

    final QuadBuilder builder = new QuadBuilder("Save card " + savedGame.saveName);

    for(int i = 0; i < savedGame.characters.size(); i++) {
      if(!this.charIndices.contains(i) && savedGame.characters.get(i).inParty()) {
        this.charIndices.add(i);
      }

      this.charVertices.add(builder.currentQuadIndex() * 4);

      final Rect4i charPortrait = savedGame.charPortraits.get(i);

      builder.add();
      builder.bpp(Bpp.BITS_24);
      builder.posSize(48.0f, 48.0f);
      builder.uv((float)charPortrait.x / this.texture.width, (float)charPortrait.y / this.texture.height);
      builder.uvSize((float)charPortrait.w / this.texture.width, (float)charPortrait.h / this.texture.height);
    }

    this.obj = builder.build();

    this.highlight = this.addControl(Glyph.glyph(0x83));
    this.highlight.setY(8);

    final DragoonSpirits dragoonSpirits = this.addControl(new DragoonSpirits(this.savedGame.goodsIds));
    dragoonSpirits.setPos(205, 27);

    this.setSelectedCharacter(0);
  }

  public void setSelectedCharacter(final int index) {
    this.selectedCharacter = index;
    this.highlight.setX(26 + (index - this.scroll) * 52);
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(this.savedGame != null) {
      if(action == CoreMod.INPUT_ACTION_MENU_LEFT.get()) {
        if(this.selectedCharacter > 0) {
          if(this.selectedCharacter - this.scroll - 1 < 0) {
            this.scroll--;
          }

          playMenuSound(1);
          this.setSelectedCharacter(this.selectedCharacter - 1);
        }

        return InputPropagation.HANDLED;
      }

      if(action == CoreMod.INPUT_ACTION_MENU_RIGHT.get()) {
        if(this.selectedCharacter < this.charIndices.size() - 1) {
          if(this.selectedCharacter - this.scroll + 1 > 2) {
            this.scroll++;
          }

          playMenuSound(1);
          this.setSelectedCharacter(this.selectedCharacter + 1);
        }

        return InputPropagation.HANDLED;
      }
    }

    return super.inputActionPressed(action, repeat);
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.savedGame != null) {
      for(int i = 0; i < 3; i++) {
        if(this.scroll + i < this.charIndices.size()) {
          final int charIndex = this.charIndices.getInt(this.scroll + i);
          final int vertex = this.charVertices.getInt(charIndex);

          this.transforms.transfer.set(x + 18 + i * 52, y + 8, this.getZ() * 4);
          RENDERER.queueOrthoModel(this.obj, this.transforms, QueuedModelStandard.class)
            .texture(this.texture)
            .vertices(vertex, 4);
        }
      }

      renderText(this.savedGame.version, x - 3, y + 2, TINY);
      renderText(this.savedGame.locationName, x + 258, y + 47, UI_TEXT_CENTERED);

      final int charIndex = this.charIndices.getInt(this.selectedCharacter);
      this.savedGame.characters.get(charIndex).render(this.savedGame, x, y);
      this.renderNumber(245, y + 17, this.savedGame.gold, 8);
      this.renderNumber(306, y + 17, getTimestampPart(this.savedGame.timestamp, 0), 3);
      this.renderCharacter(324, y + 17, 10);
      this.renderNumber(330, y + 17, getTimestampPart(this.savedGame.timestamp, 1), 2, 0x1);
      this.renderCharacter(342, y + 17, 10);
      this.renderNumber(348, y + 17, getTimestampPart(this.savedGame.timestamp, 2), 2, 0x1);
      this.renderNumber(348, y + 34, this.savedGame.stardust, 2);
    }
  }
}
