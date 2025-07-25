package legend.game.fmv;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.audio.GenericSource;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.Texture;
import legend.core.platform.WindowEvents;
import legend.core.platform.input.ButtonInputActivation;
import legend.core.platform.input.InputActivation;
import legend.core.platform.input.InputBindings;
import legend.core.platform.input.InputButton;
import legend.core.platform.input.InputClass;
import legend.core.platform.input.InputKey;
import legend.core.platform.input.KeyInputActivation;
import legend.core.platform.input.ScancodeInputActivation;
import legend.core.spu.XaAdpcm;
import legend.game.EngineState;
import legend.game.EngineStateEnum;
import legend.game.i18n.I18n;
import legend.game.modding.coremod.CoreMod;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.DISCORD;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.RENDERER;
import static legend.game.SItem.UI_WHITE;
import static legend.game.Scus94491BpeSegment_8002.adjustRumbleOverTime;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.sssqResetStuff;
import static legend.game.Scus94491BpeSegment_8002.startRumbleIntensity;
import static legend.game.Scus94491BpeSegment_8002.stopRumble;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd20;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.submapId_800bd808;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_FMV_SKIP;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;

public final class Fmv {
  private Fmv() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Fmv.class);

  private static final int[] _80052d6c = {0, 4, 7, 15};
  private static final String[][] diskFmvs_80052d7c = {
    {"\\STR\\DEMOH.IKI", "\\STR\\DEMO2.IKI", "\\STR\\OPENH.IKI", "\\STR\\WAR1H.IKI"},
    {"\\STR\\TVRH.IKI", "\\STR\\GOAST.IKI", "\\STR\\ROZEH.IKI"},
    {"\\STR\\TREEH.IKI", "\\STR\\WAR2H.IKI", "\\STR\\BLACKH.IKI", "\\STR\\DRAGON1.IKI", "\\STR\\DENIN.IKI", "\\STR\\DENIN2.IKI", "\\STR\\DRAGON2.IKI", "\\STR\\DEIASH.IKI"},
    {"\\STR\\MOONH.IKI", "\\STR\\ENDING1H.IKI", "\\STR\\ENDING2H.IKI"}
  };

  private static final ZeroRunLengthAc ESCAPE_CODE = new ZeroRunLengthAc(BitStreamCode._000001___________, true, false);
  private static final ZeroRunLengthAc END_OF_BLOCK = new ZeroRunLengthAc(BitStreamCode._10_______________, MdecCode.MDEC_END_OF_DATA_TOP6, MdecCode.MDEC_END_OF_DATA_BOTTOM10, false, true);

  private static final ZeroRunLengthAcLookup AC_VARIABLE_LENGTH_CODES_MPEG1 = new ZeroRunLengthAcLookup.Builder()
    //  Code        "Run" "Level"
    ._11s              (0 ,  1)
    ._011s             (1 ,  1)
    ._0100s            (0 ,  2)
    ._0101s            (2 ,  1)
    ._00101s           (0 ,  3)
    ._00110s           (4 ,  1)
    ._00111s           (3 ,  1)
    ._000100s          (7 ,  1)
    ._000101s          (6 ,  1)
    ._000110s          (1 ,  2)
    ._000111s          (5 ,  1)
    ._0000100s         (2 ,  2)
    ._0000101s         (9 ,  1)
    ._0000110s         (0 ,  4)
    ._0000111s         (8 ,  1)
    ._00100000s        (13,  1)
    ._00100001s        (0 ,  6)
    ._00100010s        (12,  1)
    ._00100011s        (11,  1)
    ._00100100s        (3 ,  2)
    ._00100101s        (1 ,  3)
    ._00100110s        (0 ,  5)
    ._00100111s        (10,  1)
    ._0000001000s      (16,  1)
    ._0000001001s      (5 ,  2)
    ._0000001010s      (0 ,  7)
    ._0000001011s      (2 ,  3)
    ._0000001100s      (1 ,  4)
    ._0000001101s      (15,  1)
    ._0000001110s      (14,  1)
    ._0000001111s      (4 ,  2)
    ._000000010000s    (0 , 11)
    ._000000010001s    (8 ,  2)
    ._000000010010s    (4 ,  3)
    ._000000010011s    (0 , 10)
    ._000000010100s    (2 ,  4)
    ._000000010101s    (7 ,  2)
    ._000000010110s    (21,  1)
    ._000000010111s    (20,  1)
    ._000000011000s    (0 ,  9)
    ._000000011001s    (19,  1)
    ._000000011010s    (18,  1)
    ._000000011011s    (1 ,  5)
    ._000000011100s    (3 ,  3)
    ._000000011101s    (0 ,  8)
    ._000000011110s    (6 ,  2)
    ._000000011111s    (17,  1)
    ._0000000010000s   (10,  2)
    ._0000000010001s   (9 ,  2)
    ._0000000010010s   (5 ,  3)
    ._0000000010011s   (3 ,  4)
    ._0000000010100s   (2 ,  5)
    ._0000000010101s   (1 ,  7)
    ._0000000010110s   (1 ,  6)
    ._0000000010111s   (0 , 15)
    ._0000000011000s   (0 , 14)
    ._0000000011001s   (0 , 13)
    ._0000000011010s   (0 , 12)
    ._0000000011011s   (26,  1)
    ._0000000011100s   (25,  1)
    ._0000000011101s   (24,  1)
    ._0000000011110s   (23,  1)
    ._0000000011111s   (22,  1)
    ._00000000010000s  (0 , 31)
    ._00000000010001s  (0 , 30)
    ._00000000010010s  (0 , 29)
    ._00000000010011s  (0 , 28)
    ._00000000010100s  (0 , 27)
    ._00000000010101s  (0 , 26)
    ._00000000010110s  (0 , 25)
    ._00000000010111s  (0 , 24)
    ._00000000011000s  (0 , 23)
    ._00000000011001s  (0 , 22)
    ._00000000011010s  (0 , 21)
    ._00000000011011s  (0 , 20)
    ._00000000011100s  (0 , 19)
    ._00000000011101s  (0 , 18)
    ._00000000011110s  (0 , 17)
    ._00000000011111s  (0 , 16)
    ._000000000010000s (0 , 40)
    ._000000000010001s (0 , 39)
    ._000000000010010s (0 , 38)
    ._000000000010011s (0 , 37)
    ._000000000010100s (0 , 36)
    ._000000000010101s (0 , 35)
    ._000000000010110s (0 , 34)
    ._000000000010111s (0 , 33)
    ._000000000011000s (0 , 32)
    ._000000000011001s (1 , 14)
    ._000000000011010s (1 , 13)
    ._000000000011011s (1 , 12)
    ._000000000011100s (1 , 11)
    ._000000000011101s (1 , 10)
    ._000000000011110s (1 ,  9)
    ._000000000011111s (1 ,  8)
    ._0000000000010000s(1 , 18)
    ._0000000000010001s(1 , 17)
    ._0000000000010010s(1 , 16)
    ._0000000000010011s(1 , 15)
    ._0000000000010100s(6 ,  3)
    ._0000000000010101s(16,  2)
    ._0000000000010110s(15,  2)
    ._0000000000010111s(14,  2)
    ._0000000000011000s(13,  2)
    ._0000000000011001s(12,  2)
    ._0000000000011010s(11,  2)
    ._0000000000011011s(31,  1)
    ._0000000000011100s(30,  1)
    ._0000000000011101s(29,  1)
    ._0000000000011110s(28,  1)
    ._0000000000011111s(27,  1)
    .add(ESCAPE_CODE)
    .add(END_OF_BLOCK)
    .build();

  private static final int[] reverseZigzag = {
     0,  1,  8, 16,  9,  2,  3, 10,
    17, 24, 32, 25, 18, 11,  4,  5,
    12, 19, 26, 33, 40, 48, 41, 34,
    27, 20, 13,  6,  7, 14, 21, 28,
    35, 42, 49, 56, 57, 50, 43, 36,
    29, 22, 15, 23, 30, 37, 44, 51,
    58, 59, 52, 45, 38, 31, 39, 46,
    53, 60, 61, 54, 47, 55, 62, 63,
  };

  private static final int[] quantizationMatrix = {
     2, 16, 19, 22, 26, 27, 29, 34,
    16, 16, 22, 24, 27, 29, 34, 37,
    19, 22, 26, 27, 29, 34, 34, 38,
    22, 22, 26, 27, 29, 34, 37, 40,
    22, 26, 27, 29, 32, 35, 40, 48,
    26, 27, 29, 32, 35, 40, 48, 58,
    26, 27, 29, 34, 38, 46, 56, 69,
    27, 29, 35, 38, 46, 56, 69, 83,
  };

  private static final int[] PSX_DEFAULT_COSINE_MATRIX = {
    23170,  23170,  23170,  23170,  23170,  23170,  23170,  23170,
    32138,  27245,  18204,   6392,  -6393, -18205, -27246, -32139,
    30273,  12539, -12540, -30274, -30274, -12540,  12539,  30273,
    27245,  -6393, -32139, -18205,  18204,  32138,   6392, -27246,
    23170, -23171, -23171,  23170,  23170, -23171, -23171,  23170,
    18204, -32139,   6392,  27245, -27246,  -6393,  32138, -18205,
    12539, -30274,  30273, -12540, -12540,  30273, -30274,  12539,
     6392, -18205,  27245, -32139,  32138, -27246,  18204,  -6393,
  };

  private static Runnable oldRenderer;
  private static int oldFps;
  private static int sector;
  private static int frame;

  private static float volume = 1.0f;
  private static GenericSource source;

  private static WindowEvents.InputClassChanged inputClassChanged;
  private static WindowEvents.KeyPressed keyPress;
  private static WindowEvents.ButtonPressed buttonPressed;
  private static WindowEvents.InputActionPressed inputActionPressed;
  private static WindowEvents.Click click;
  private static boolean shouldStop;

  private static Obj texturedObj;
  private static Texture displayTexture;
  private static final Vector2i oldProjectionSize = new Vector2i();
  private static EngineState.RenderMode oldRenderMode;

  private static RumbleData[] rumbleData;
  private static int rumbleFrames;

  private static InputClass currentInputSource;
  private static int skipTextFramesRemained;
  private static String skipText;
  private static InputButton skipButton;
  private static InputKey skipKey;
  private static InputKey skipScancode;
  private static boolean isKeyboardInput;
  private static boolean isControllerInput;

  public static boolean isPlaying;

  public static void playCurrentFmv(final int fmvIndex, final EngineStateEnum afterFmvState) {
    sssqResetStuff();

    submapId_800bd808 = -1;

    rumbleData = RumbleData.load(Loader.loadFile("SECT/DRGN0.BIN/5721/" + fmvIndex));
    rumbleFrames = 0;

    isPlaying = true;
    Fmv.play(diskFmvs_80052d7c[drgnBinIndex_800bc058 - 1][fmvIndex - _80052d6c[drgnBinIndex_800bc058 - 1]], true);
    engineStateOnceLoaded_8004dd24 = afterFmvState;
  }

  private static void displaySkipText() {
    if(skipText != null) {
      renderText(skipText, 10.0f, 10.0f, UI_WHITE);
    }
  }

  private static void setSkipText(final String text, final InputClass inputSource) {
    skipText = text;
    currentInputSource = inputSource;
    skipTextFramesRemained = 60;
  }

  private static void handleSkipText() {
    if(isKeyboardInput) {
      if(skipKey != null) {
        setSkipText(I18n.translate("lod_core.config.fmv.skip_keyboard", PLATFORM.getKeyName(skipKey)), InputClass.KEYBOARD);
      } else if(skipScancode != null) {
        setSkipText(I18n.translate("lod_core.config.fmv.skip_keyboard", PLATFORM.getScancodeName(skipScancode)), InputClass.KEYBOARD);
      }

      isKeyboardInput = false;
    } else if(isControllerInput) {
      setSkipText(I18n.translate("lod_core.config.fmv.skip_controller", skipButton.codepoint), InputClass.GAMEPAD);
      isControllerInput = false;
    }

    if(skipTextFramesRemained > 0) {
      skipTextFramesRemained--;
      if(skipTextFramesRemained == 0) {
        skipText = null;
        currentInputSource = null;
      }
    }
  }

  private static boolean isValidSkipInput(final InputClass source) {
    return skipText != null && currentInputSource == source;
  }

  private static void play(final String file, final boolean doubleSpeed) {
    shouldStop = false;

    final byte[] data = new byte[2352];
    final SectorHeader header = new SectorHeader(data);
    final VideoSector video = new VideoSector(data);

    final int sectorCount = doubleSpeed ? 10 : 5; // (150|75) sectors per second / 15 frames per second
    final byte[] demuxedRaw = new byte[2016 * sectorCount];
    final ByteBuffer demuxed = ByteBuffer.wrap(demuxedRaw);
    final FrameHeader frameHeader = new FrameHeader(demuxedRaw);

    final FileData fileData = Loader.loadFile(file);
    sector = 0;
    frame = 0;
    skipText = null;

    oldFps = RENDERER.window().getFpsLimit();
    oldProjectionSize.set(RENDERER.getNativeWidth(), RENDERER.getNativeHeight());
    oldRenderMode = RENDERER.getRenderMode();
    RENDERER.setRenderMode(EngineState.RenderMode.PERSPECTIVE);
    RENDERER.setProjectionSize(320, 240);

    source = AUDIO_THREAD.addSource(new GenericSource(AL_FORMAT_STEREO16, 37800));
    volume = CONFIG.getConfig(CoreMod.FMV_VOLUME_CONFIG.get()) * CONFIG.getConfig(CoreMod.MASTER_VOLUME_CONFIG.get());

    skipButton = null;
    skipKey = null;
    skipScancode = null;

    final List<InputActivation> activations = InputBindings.getActivationsForAction(INPUT_ACTION_FMV_SKIP.get());

    for(final InputActivation activation : activations) {
      if(activation instanceof final ButtonInputActivation button) {
        skipButton = button.button;
      } else if(activation instanceof final KeyInputActivation key) {
        skipKey = key.key;
      } else if(activation instanceof final ScancodeInputActivation key) {
        skipScancode = key.key;
      }
    }

    keyPress = RENDERER.events().onKeyPress((window, key, scancode, mods, repeat) -> {
      if(!isControllerInput && !shouldStop && !repeat) {
        isKeyboardInput = true;
      }
    });

    buttonPressed = RENDERER.events().onButtonPress((window, action, repeat) -> {
      if(!isKeyboardInput && !shouldStop && !repeat) {
        isControllerInput = true;
      }
    });

    inputActionPressed = RENDERER.events().onInputActionPressed((window, action, repeat) -> {
      if(action == INPUT_ACTION_FMV_SKIP.get() && isValidSkipInput(window.getInputClass())) {
        shouldStop = true;
      }
    });

    click = RENDERER.events().onMouseRelease((window, x, y, button, mods) -> {
      if(isValidSkipInput(InputClass.MOUSE)) {
        shouldStop = true;
      } else {
        setSkipText(I18n.translate("lod_core.config.fmv.skip_mouse"), InputClass.MOUSE);
      }
    });

    oldRenderer = RENDERER.setRenderCallback(() -> {
      if(shouldStop) {
        stop();
      }

      RENDERER.window().setFpsLimit(15 * Config.getGameSpeedMultiplier());
      PLATFORM.setInputTickRate(15 * Config.getGameSpeedMultiplier());

      int demuxedSize = 0;

      // Demultiplex the sectors
      Arrays.fill(demuxedRaw, (byte)0);
      for(int sectorIndex = 0, videoSectorIndex = 0; sectorIndex < sectorCount; sectorIndex++, sector++) {
        fileData.read(sector * data.length, data, 0, data.length);

        if(header.submode.isEof()) {
          stop();
          return;
        }

        if(header.submode.getType() == SectorHeader.TYPE.DATA) {
          if(sectorIndex == 0) {
            demuxedSize = video.getDemuxedSize();
          }

          video.readSector(demuxedRaw, videoSectorIndex++);
        }

        if(header.submode.getType() == SectorHeader.TYPE.AUDIO) {
          final short[] decodedXaAdpcm = XaAdpcm.decode(data, data[19]);

          // Halve the volume
          for(int i = 0; i < decodedXaAdpcm.length; i++) {
            decodedXaAdpcm[i] >>= 1;
            decodedXaAdpcm[i] *= volume;
          }

          synchronized(source) {
            if(source.canBuffer()) {
              source.bufferOutput(decodedXaAdpcm);
            }
          }
        }
      }

      demuxed.position(10);
      final int blockW = (frameHeader.getWidth() + 15) / 16;
      final int blockH = (frameHeader.getHeight() + 15) / 16;
      final int chromaW = blockW * 8;
      final int chromaH = blockH * 8;
      final int lumaW = blockW * 16;
      final int lumaH = blockH * 16;

      final int macroblockCount = blockW * blockH;
      final int uncompressedSize = macroblockCount * 6 * 2;
      final ByteBuffer initialBlockCodes = ByteBuffer.allocate(uncompressedSize);

      // Decompress initial block codes
      outer:
      while(initialBlockCodes.position() < uncompressedSize) {
        final int flags = demuxed.get();
        int mask = 1;

        for(int bit = 0; bit < 8; bit++) {
          if((flags & mask) == 0) {
            initialBlockCodes.put(demuxed.get());
          } else {
            final int copySize = (demuxed.get() & 0xff) + 3;
            int copyOffset = demuxed.get() & 0xff;

            if((copyOffset & 0x80) != 0) {
              copyOffset = (copyOffset & 0x7f) << 8 | demuxed.get() & 0xff;
            }

            copyOffset++;

            for(int i = 0; i < copySize; i++) {
              initialBlockCodes.put(initialBlockCodes.get(initialBlockCodes.position() - copyOffset));
            }
          }

          if(initialBlockCodes.position() >= uncompressedSize) {
            break outer;
          }

          mask <<= 1;
        }
      }

      final int[] chromaMacroBlockOffsetLookup = new int[blockW * blockH];
      final int[] lumaBlockOffsetLookup = new int[blockW * blockH * 4];

      // build a table that holds the starting index of every (macro)block
      // in the output buffer so we don't have to do this calculation during decoding
      {
        int macroblockIndex = 0;
        for(int macroblockX = 0; macroblockX < blockW; macroblockX++) {
          for(int macroblockY = 0; macroblockY < blockH; macroblockY++) {
            chromaMacroBlockOffsetLookup[macroblockIndex] = macroblockX * 8 + macroblockY * 8 * chromaW;
            int blockIndex = 0;
            for(int blockX = 0; blockX < 2; blockX++) {
              for(int blockY = 0; blockY < 2; blockY++) {
                lumaBlockOffsetLookup[macroblockIndex * 4 + blockIndex] = macroblockX * 16 + blockY * 8 + (macroblockY * 16 + blockX * 8) * lumaW;
                blockIndex++;
              }
            }
            macroblockIndex++;
          }
        }
      }

      // Dequantize and apply inverse discrete cosine transform
      final VariableLengthCode vlc = new VariableLengthCode();
      final ArrayBitReader bitReader = new ArrayBitReader(demuxedRaw, demuxedSize, true, 10 + frameHeader.getCompressedCodesSize());
      final int[] cr = new int[chromaW * chromaH];
      final int[] cb = new int[chromaW * chromaH];
      final int[] luma = new int[lumaW * lumaH];

      for(int macroblockIndex = 0; macroblockIndex < macroblockCount; macroblockIndex++) {
        for(int blockIndex = 0; blockIndex < 6; blockIndex++) { // for Cr, Cb, Y1, Y2, Y3, Y4
          final int[] coefficients = new int[64];
          int vectorPos = 0;

          final int initialCode = (initialBlockCodes.get(macroblockIndex * 6 + blockIndex) & 0xff) << 8 | initialBlockCodes.get(macroblockIndex * 6 + blockIndex + uncompressedSize / 2) & 0xff;
          final int dc = initialCode << 22 >> 22; // 10-bit signed
          final int blockQuant = initialCode >>> 10; // 6-bit unsigned

          int nonZeroCount;
          if(dc != 0) {
            coefficients[0] = dc * quantizationMatrix[0];
            nonZeroCount = 1;
          } else {
            nonZeroCount = 0;
          }

          while(getNextVlc(vlc, bitReader)) {
            vectorPos += vlc.zeroes + 1;

            if(vlc.coefficient != 0) {
              final int zigzagPos = reverseZigzag[vectorPos];
              coefficients[zigzagPos] = vlc.coefficient * quantizationMatrix[zigzagPos] * blockQuant + 4 >> 3; // (int)Math.round(i / 8.0)
              nonZeroCount++;
            }
          }

          if(vectorPos > 63) {
            throw new RuntimeException("Too many AC coefficients codes (" + vectorPos + ')');
          }

          final int[] outputBuffer;
          int iOutOffset;
          final int iOutWidth;
          switch(blockIndex) {
            case 0 -> {
              outputBuffer = cr;
              iOutOffset = chromaMacroBlockOffsetLookup[macroblockIndex];
              iOutWidth = chromaW;
            }
            case 1 -> {
              outputBuffer = cb;
              iOutOffset = chromaMacroBlockOffsetLookup[macroblockIndex];
              iOutWidth = chromaW;
            }
            default -> {
              outputBuffer = luma;
              iOutOffset = lumaBlockOffsetLookup[macroblockIndex * 4 + blockIndex - 2];
              iOutWidth = lumaW;
            }
          }

          if(nonZeroCount == 0) {
            for(int i = 0; i < 8; i++, iOutOffset += iOutWidth) {
              Arrays.fill(outputBuffer, iOutOffset, iOutOffset + 8, 0);
            }
          } else {
            idct(coefficients, 0, coefficients);

            // TODO: have IDCT write to the destination location directly
            for(int i = 0, iSrcOfs = 0; i < 8; i++, iSrcOfs += 8, iOutOffset += iOutWidth) {
              System.arraycopy(coefficients, iSrcOfs, outputBuffer, iOutOffset, 8);
            }
          }
        }
      }

      // Build YCbCr pixel array
      final int[] framePixels = new int[frameHeader.getWidth() * frameHeader.getHeight()];
      readDecodedRgb(chromaW, lumaW, cr, cb, luma, frameHeader.getWidth(), frameHeader.getHeight(), framePixels, 0, frameHeader.getWidth());

      if(displayTexture == null || displayTexture.width != frameHeader.getWidth() || displayTexture.height != frameHeader.getHeight()) {
        if(displayTexture != null) {
          displayTexture.delete();
        }

        displayTexture = Texture.filteredEmpty(frameHeader.getWidth(), frameHeader.getHeight());
      }

      if(texturedObj == null) {
        texturedObj = new QuadBuilder("FMV")
          .bpp(Bpp.BITS_24)
          .size(1.0f, 1.0f)
          .build();
      }

      displayTexture.use();
      displayTexture.data(0, 0, frameHeader.getWidth(), frameHeader.getHeight(), framePixels);

      final MV transforms = new MV();
      transforms.scaling(320.0f, frameHeader.getHeight(), 1.0f);
      transforms.transfer.set(0.0f, (240.0f - frameHeader.getHeight()) / 2.0f, 100.0f);

      RENDERER.queueOrthoModel(texturedObj, transforms, QueuedModelStandard.class)
        .texture(displayTexture)
      ;

      if(rumbleData != null) {
        for(final RumbleData rumble : rumbleData) {
          if(rumble.frame == frame) {
            final EngineStateEnum oldEngineState = engineState_8004dd20;
            engineState_8004dd20 = EngineStateEnum.FMV_09;
            startRumbleIntensity(0, rumble.initialIntensity);
            adjustRumbleOverTime(0, rumble.endingIntensity, rumble.duration);
            rumbleFrames = rumble.duration;
            engineState_8004dd20 = oldEngineState;
          }
        }

        rumbleFrames--;
        if(rumbleFrames == 0) {
          stopRumble(0);
        }
      }

      handleSkipText();
      displaySkipText();
      frame++;

      DISCORD.tick();
    });
  }

  public static void stop() {
    isPlaying = false;
    RENDERER.setRenderCallback(() -> {
      if(texturedObj != null) {
        texturedObj.delete();
        texturedObj = null;
      }

      if(displayTexture != null) {
        displayTexture.delete();
        displayTexture = null;
      }

      if(inputClassChanged != null) {
        RENDERER.events().removeInputClassChanged(inputClassChanged);
        inputClassChanged = null;
      }

      if(inputActionPressed != null) {
        RENDERER.events().removeInputActionPressed(inputActionPressed);
        inputActionPressed = null;
      }

      if(keyPress != null) {
        RENDERER.events().removeKeyPress(keyPress);
        keyPress = null;
      }

      if(click != null) {
        RENDERER.events().removeMouseRelease(click);
        click = null;
      }

      if(buttonPressed != null) {
        RENDERER.events().removeButtonPress(buttonPressed);
        buttonPressed = null;
      }

      RENDERER.setRenderCallback(oldRenderer);
      RENDERER.window().setFpsLimit(oldFps);
      PLATFORM.setInputTickRate(oldFps);
      RENDERER.setRenderMode(oldRenderMode);
      RENDERER.setProjectionSize(oldProjectionSize.x, oldProjectionSize.y);
      oldRenderer = null;

      AUDIO_THREAD.removeSource(source);
      source = null;

      rumbleData = null;
    });
  }

  private static boolean getNextVlc(final VariableLengthCode vlc, final ArrayBitReader bitReader) {
    final int value = bitReader.peekUnsignedBits(BitStreamCode.LONGEST_BITSTREAM_CODE_17BITS);

    final ZeroRunLengthAc ac = AC_VARIABLE_LENGTH_CODES_MPEG1.lookup(value);
    bitReader.skipBits(ac.getBitLength());

    if(ac.isEndOfBlock()) {
      return false;
    }

    if(ac.isEscapeCode()) {
      vlc.zeroes = bitReader.readUnsignedBits(6);
      vlc.coefficient = bitReader.readSignedBits(10);
      return true;
    }

    vlc.zeroes = ac.getMdecCodeCopy().getTop6Bits();
    vlc.coefficient = ac.getMdecCodeCopy().getBottom10Bits();
    return true;
  }

  private static final long[] _aTemp = new long[64];
  private static void idct(final int[] idctMatrix, final int iOutputOffset, final int[] output) {
    long tempSum;
    int x;
    int y;
    int i;

    for(x = 0; x < 8; x++) {
      for(y = 0; y < 8; y++) {
        tempSum = 0;

        for(i = 0; i < 8; i++) {
          tempSum += (PSX_DEFAULT_COSINE_MATRIX[i * 8 + y] * idctMatrix[x + i * 8]);
        }

        _aTemp[x + y * 8] = tempSum;
      }
    }

    for(x = 0; x < 8; x++) {
      for(y = 0; y < 8; y++) {
        tempSum = 0;

        for(i = 0; i < 8; i++) {
          tempSum += _aTemp[i + y * 8] * PSX_DEFAULT_COSINE_MATRIX[x + i * 8];
        }

        output[iOutputOffset + x + y * 8] = (int)MathHelper.shrRound(tempSum, 32);
      }
    }
  }

  private static void readDecodedRgb(final int chromaW, final int lumaW, final int[] cr, final int[] cb, final int[] luma, final int destW, final int destH, final int[] dest, final int outStart, final int outStride) {
    final PsxYCbCr_int psxycc = new PsxYCbCr_int();
    final RGB rgb1 = new RGB(), rgb2 = new RGB(), rgb3 = new RGB(), rgb4 = new RGB();

    final int W_x2 = lumaW * 2, iOutStride_x2 = outStride * 2;

    final int iDestWidthSub1 = destW - 1;
    final int iDestHeightSub1 = destH - 1;

    int iLumaLineOfsStart = 0, iChromaLineOfsStart = 0, iDestLineOfsStart = outStart;
    int iY = 0;
    for(; iY < iDestHeightSub1; iY += 2, iLumaLineOfsStart += W_x2, iChromaLineOfsStart += chromaW, iDestLineOfsStart += iOutStride_x2) {
      // writes 2 lines at a time
      int iSrcLumaOfs1 = iLumaLineOfsStart, iSrcLumaOfs2 = iLumaLineOfsStart + lumaW, iSrcChromaOfs = iChromaLineOfsStart, iDestOfs1 = iDestLineOfsStart, iDestOfs2 = iDestLineOfsStart + outStride;

      int iX = 0;
      for(; iX < iDestWidthSub1; iX += 2, iSrcChromaOfs++) {
        psxycc.cr = cr[iSrcChromaOfs];
        psxycc.cb = cb[iSrcChromaOfs];

        psxycc.y1 = luma[iSrcLumaOfs1++];
        psxycc.y2 = luma[iSrcLumaOfs1++];
        psxycc.y3 = luma[iSrcLumaOfs2++];
        psxycc.y4 = luma[iSrcLumaOfs2++];

        psxycc.toRgb(rgb1, rgb2, rgb3, rgb4);

        dest[iDestOfs1++] = rgb1.toArgb();
        dest[iDestOfs1++] = rgb2.toArgb();
        dest[iDestOfs2++] = rgb3.toArgb();
        dest[iDestOfs2++] = rgb4.toArgb();
      }

      if(iX < destW) {
        // if the width is odd, add 2 pixels
        psxycc.cr = cr[iSrcChromaOfs];
        psxycc.cb = cb[iSrcChromaOfs];

        psxycc.y1 = luma[iSrcLumaOfs1];
        psxycc.y2 = luma[iSrcLumaOfs1];
        psxycc.y3 = luma[iSrcLumaOfs2];
        psxycc.y4 = luma[iSrcLumaOfs2];

        psxycc.toRgb(rgb1, rgb2, rgb3, rgb4); // rgb2,4 ignored

        dest[iDestOfs1] = rgb1.toArgb();
        dest[iDestOfs2] = rgb3.toArgb();
      }
    }

    if(iY < destH) {
      // if the height is odd, write 1 line
      int iSrcLumaOfs1 = iLumaLineOfsStart, iSrcLumaOfs2 = iLumaLineOfsStart + lumaW, iSrcChromaOfs = iChromaLineOfsStart, iDestOfs1 = iDestLineOfsStart;

      int iX = 0;
      for(; iX < iDestWidthSub1; iX += 2, iSrcChromaOfs++) {
        psxycc.cr = cr[iSrcChromaOfs];
        psxycc.cb = cb[iSrcChromaOfs];

        psxycc.y1 = luma[iSrcLumaOfs1++];
        psxycc.y2 = luma[iSrcLumaOfs1++];
        psxycc.y3 = luma[iSrcLumaOfs2++];
        psxycc.y4 = luma[iSrcLumaOfs2++];

        psxycc.toRgb(rgb1, rgb2, rgb3, rgb4); // rgb3,4 ignored

        dest[iDestOfs1++] = rgb1.toArgb();
        dest[iDestOfs1++] = rgb2.toArgb();
      }

      if(iX < destW) {
        // if the width is odd, add 1 pixel
        psxycc.cr = cr[iSrcChromaOfs];
        psxycc.cb = cb[iSrcChromaOfs];

        psxycc.y1 = luma[iSrcLumaOfs1];
        psxycc.y2 = luma[iSrcLumaOfs1];
        psxycc.y3 = luma[iSrcLumaOfs2];
        psxycc.y4 = luma[iSrcLumaOfs2];

        psxycc.toRgb(rgb1, rgb2, rgb3, rgb4); // rgb2,3,4 ignored

        dest[iDestOfs1] = rgb1.toArgb();
      }
    }
  }
}
