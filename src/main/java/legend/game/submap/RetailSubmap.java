package legend.game.submap;

import legend.core.gpu.Rect4i;
import legend.core.memory.types.IntRef;
import legend.game.scripting.ScriptFile;
import legend.game.tim.Tim;
import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.types.CContainer;
import legend.game.types.NewRootStruct;
import legend.game.types.TmdAnimationFile;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static legend.core.GameEngine.GPU;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;

public class RetailSubmap extends Submap {
  public final int cut;
  private final NewRootStruct newRoot;

  public RetailSubmap(final int cut, final NewRootStruct newRoot) {
    this.cut = cut;
    this.newRoot = newRoot;
  }

  @Override
  public void loadAssets(final Runnable onLoaded) {
    final IntRef drgnIndex = new IntRef();
    final IntRef fileIndex = new IntRef();

    this.newRoot.getDrgnFile(this.cut, drgnIndex, fileIndex);

    if(drgnIndex.get() == 1 || drgnIndex.get() == 2 || drgnIndex.get() == 3 || drgnIndex.get() == 4) {
      final List<FileData> assets = new ArrayList<>();
      final List<FileData> scripts = new ArrayList<>();
      final List<FileData> textures = new ArrayList<>();
      final AtomicInteger loaded = new AtomicInteger();

      final Runnable prepare = () -> {
        this.prepareAssets(assets, scripts, textures);
        onLoaded.run();
      };

      loadDrgnDir(drgnIndex.get() + 2, fileIndex.get() + 1, files -> this.handleAsyncLoad(files, assets, loaded, 3, prepare));
      loadDrgnDir(drgnIndex.get() + 2, fileIndex.get() + 2, files -> this.handleAsyncLoad(files, scripts, loaded, 3, prepare));
      Unpacker.loadDirectory("SECT/DRGN%d.BIN/%d/textures".formatted(20 + drgnIndex.get(), fileIndex.get() + 1), files -> this.handleAsyncLoad(files, textures, loaded, 3, prepare));
    }
  }

  private void prepareAssets(final List<FileData> assets, final List<FileData> scripts, final List<FileData> textures) {
    final int objCount = scripts.size() - 2;

    this.script = new ScriptFile("Submap controller", scripts.get(0).getBytes());

    for(int objIndex = 0; objIndex < objCount; objIndex++) {
      final byte[] scriptData = scripts.get(objIndex + 1).getBytes();

      final FileData submapModel = assets.get(objIndex * 33);

      final IntRef drgnIndex = new IntRef();
      final IntRef fileIndex = new IntRef();
      this.newRoot.getDrgnFile(submapCut_80052c30, drgnIndex, fileIndex);

      final SubmapObject obj = new SubmapObject();
      obj.script = new ScriptFile("Submap object %d (DRGN%d/%d/%d)".formatted(objIndex, drgnIndex.get(), fileIndex.get() + 2, objIndex + 1), scriptData);

      if(submapModel.hasVirtualSize() && submapModel.real()) {
        obj.model = new CContainer("Submap object %d (DRGN%d/%d/%d)".formatted(objIndex, drgnIndex.get(), fileIndex.get() + 1, objIndex * 33), new FileData(submapModel.getBytes()));
      } else {
        obj.model = null;
      }

      for(int animIndex = objIndex * 33 + 1; animIndex < (objIndex + 1) * 33; animIndex++) {
        final FileData data = assets.get(animIndex);

        // This is a stupid fix for a stupid retail bug where almost all
        // sobj animations in DRGN24.938 are symlinked to a PXL file
        // GH#292
        if(data.readInt(0) == 0x11) {
          obj.animations.add(null);
          continue;
        }

        obj.animations.add(new TmdAnimationFile(data));
      }

      this.objects.add(obj);
    }

    // Get models that are symlinked
    for(int objIndex = 0; objIndex < objCount; objIndex++) {
      final SubmapObject obj = this.objects.get(objIndex);

      if(obj.model == null) {
        final FileData submapModel = assets.get(objIndex * 33);

        obj.model = this.objects.get(submapModel.realFileIndex() / 33).model;
      }
    }

    for(final FileData file : textures) {
      if(file.real()) {
        this.pxls.add(new Tim(file));
      } else {
        this.pxls.add(null);
      }
    }

    this.loadTextures();
  }

  @Override
  public void restoreAssets() {
    this.loadTextures();
  }

  private void handleAsyncLoad(final List<FileData> files, final List<FileData> dest, final AtomicInteger counter, final int expectedCount, final Runnable onLoaded) {
    dest.addAll(files);

    if(counter.incrementAndGet() == expectedCount) {
      onLoaded.run();
    }
  }

  private void loadTextures() {
    this.uvAdjustments.clear();

    int x = 576;
    int y = 256;
    for(int pxlIndex = 0; pxlIndex < this.pxls.size(); pxlIndex++) {
      final Tim tim = this.pxls.get(pxlIndex);

      if(tim != null) {
        final Rect4i imageRect = tim.getImageRect();
        final Rect4i clutRect = tim.getClutRect();

        imageRect.x = x;
        imageRect.y = y;
        clutRect.x = x;
        clutRect.y = y + imageRect.h;

        GPU.uploadData15(imageRect, tim.getImageData());
        GPU.uploadData15(clutRect, tim.getClutData());

        this.uvAdjustments.add(this.createUvAdjustments(pxlIndex, x, y));

        x += tim.getImageRect().w;

        if(x >= 768) {
          x = 576;
          y += 128;
        }
      } else {
        this.uvAdjustments.add(UvAdjustmentMetrics14.NONE);
      }
    }
  }

  private UvAdjustmentMetrics14 createUvAdjustments(final int index, final int x, final int y) {
    final int clutX = x / 16;
    final int clutY = y + 112;
    final int tpageX = x / 64;
    final int tpageY = y / 256;
    final int u = x % 64 * 4;
    final int v = y % 256;
    final int clut = clutX | clutY << 6;
    final int tpage = tpageX | tpageY << 4;
    final int uv = u | v << 8;

    return new UvAdjustmentMetrics14(index + 1, clut << 16, 0x3c0ffff, tpage << 16, 0xffe0ffff, uv);
  }
}
