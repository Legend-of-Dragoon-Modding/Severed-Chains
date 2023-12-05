package legend.game.wmap;

import legend.core.gpu.Rect4i;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.game.types.Translucency;
import org.joml.Vector3f;

import java.util.Arrays;

public class WmapMenuTextHighlight40 {
  public Obj highlight;
  public final MV transforms = new MV();

  public WMapTextHighlightSubRectVertexColours10[] subRectVertexColoursArray_00;
  public Translucency[] tpagePacket_04;

  public Rect4i[] rects_1c;
  // public final long[] _20 = new long[2]; // Unused

  public int columnCount_28;
  public int rowCount_2c;
  public int subRectCount_30;
  public float currentBrightness_34;
  // Unneeded now
  // public float previousBrightness_36;
  public float x_38;
  public float y_3a;
  public boolean transparency_3c;
  public float z_3e;
  public int type_3f;

  public WmapMenuTextHighlight40(final float brightness, final Vector3f baseColour, final Rect4i fullRect, final int columnCount, final int rowCount, final int type, final boolean transparency, final Translucency transparencyMode, final float z) {
    int horizontalRectIndex = 0;
    int verticalRectIndex = 0;
    int x;
    int y;

    this.columnCount_28 = columnCount;
    this.rowCount_2c = rowCount;
    this.subRectCount_30 = columnCount * rowCount;
    this.currentBrightness_34 = brightness;
    this.x_38 = 0;
    this.y_3a = 0;
    this.transparency_3c = transparency;
    this.z_3e = z;

    // Types 2 and 4 are the only ones used by retail; 0 would be a single-color rect
    if(type == 0) {
      this.subRectVertexColoursArray_00 = new WMapTextHighlightSubRectVertexColours10[] { new WMapTextHighlightSubRectVertexColours10() };
    } else {
      //LAB_800cd4fc
      this.subRectVertexColoursArray_00 = new WMapTextHighlightSubRectVertexColours10[this.subRectCount_30];
      Arrays.setAll(this.subRectVertexColoursArray_00, i -> new WMapTextHighlightSubRectVertexColours10());
    }

    //LAB_800cd534
    this.initializeWmapTextHighlightTypeAndColour(this, type, baseColour);

    //LAB_800cd578
    //LAB_800cd594
    this.tpagePacket_04 = null;

    //LAB_800cd600
    this.rects_1c = new Rect4i[this.subRectCount_30];
    Arrays.setAll(this.rects_1c, i -> new Rect4i());

    final int w = fullRect.w / columnCount;
    final int h = fullRect.h / rowCount;

    //LAB_800cd6b8
    if(transparency) {
      //LAB_800cd6cc
      //LAB_800cd6e8
      this.tpagePacket_04 = new Translucency[this.subRectCount_30];
    }

    //LAB_800cd748
    //LAB_800cd74c
    //LAB_800cd768
    //LAB_800cd7d0
    //LAB_800cd82c
    for(int i = 0; i < this.subRectCount_30; i++) {
      //LAB_800cd850
      if(transparency) {
        this.tpagePacket_04[i] = transparencyMode;
      }

      //LAB_800cd8e8
      x = fullRect.x + w * horizontalRectIndex - 160;
      y = fullRect.y + h * verticalRectIndex - 120;

      this.rects_1c[i].set(x, y, w, h);

      if(horizontalRectIndex < columnCount - 1) {
        //LAB_800cdb6c
        horizontalRectIndex++;
      } else {
        horizontalRectIndex = 0;

        if(verticalRectIndex < rowCount - 1) {
          verticalRectIndex++;
        }
      }
    }
    //LAB_800ce094
    //LAB_800ce0a8
  }

  public void setBrightness(final float brightness) {
    this.currentBrightness_34 = brightness;
  }

  private void initializeWmapTextHighlightTypeAndColour(final WmapMenuTextHighlight40 highlight, final int type, final Vector3f baseColour) {
    highlight.type_3f = type;
    highlight.shadeWmapTextHighlightSubRectVertices(type, highlight.columnCount_28, highlight.rowCount_2c, baseColour);
  }

  /**
   * Only types 2 and 4 are used by retail
   * <ol start="0">
   *   <li>Flat, single sub-rect</li>
   *   <li>Flat, multiple sub-rects</li>
   *   <li>Gradient, horizontal, multiple sub-rects</li>
   *   <li>Gradient, vertical, multiple sub-rects</li>
   *   <li>Gradient, free-form blob, multiple sub-rects</li>
   * </ol>
   */
  public void shadeWmapTextHighlightSubRectVertices(final int type, final int horizontalRectCount, final int verticalRectCount, final Vector3f baseColour) {
    int subRectIndex;
    final Vector3f colour0 = new Vector3f();
    final Vector3f colour1 = new Vector3f();
    final Vector3f colour2 = new Vector3f();
    final Vector3f colour3 = new Vector3f();
    final ColourBlending20 blending = new ColourBlending20();

    switch(type) {
      case 0 -> {
        this.subRectVertexColoursArray_00[0].topLeft_00.set(colour0);
        this.subRectVertexColoursArray_00[0].topRight_04.set(colour1);
        this.subRectVertexColoursArray_00[0].bottomLeft_08.set(colour2);
        this.subRectVertexColoursArray_00[0].bottomRight_0c.set(colour3);
      }

      case 1 -> {
        blending.colour0Start_00 = colour0;
        blending.colour0End_04 = colour1;
        blending.colour1Start_08 = colour2;
        blending.colour1End_0c = colour3;
        subRectIndex = 0;

        //LAB_800cf32c
        for(int i = 0; i < verticalRectCount; i++) {
          //LAB_800cf34c
          //LAB_800cf350
          for(int j = 0; j < horizontalRectCount; j++) {
            final WMapTextHighlightSubRectVertexColours10 subRect = this.subRectVertexColoursArray_00[subRectIndex];

            //LAB_800cf370
            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount - i;
            blending.blendColours(subRect.topLeft_00);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount - 1 - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount - i;
            blending.blendColours(subRect.topRight_04);

            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount - 1 - i;
            blending.blendColours(subRect.bottomLeft_08);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount - 1 - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount - 1 - i;
            blending.blendColours(subRect.bottomRight_0c);

            subRectIndex++;
          }
          //LAB_800cf54c
        }
      }

      //LAB_800cf564
      case 2 -> {
        blending.colour0Start_00 = colour0;
        blending.colour0End_04 = colour1;
        blending.colour1Start_08 = baseColour;
        blending.colour1End_0c = baseColour;
        subRectIndex = 0;

        //LAB_800cf5a4
        for(int i = 0; i < verticalRectCount / 2; i++) {
          //LAB_800cf5d8
          //LAB_800cf5dc
          for(int j = 0; j < horizontalRectCount; j++) {
            final WMapTextHighlightSubRectVertexColours10 subRect = this.subRectVertexColoursArray_00[subRectIndex];

            //LAB_800cf5fc
            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount / 2 - i;
            blending.blendColours(subRect.topLeft_00);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount - 1 - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount / 2 - i;
            blending.blendColours(subRect.topRight_04);

            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount / 2 - 1 - i;
            blending.blendColours(subRect.bottomLeft_08);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount - 1 - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount / 2 - 1 - i;
            blending.blendColours(subRect.bottomRight_0c);

            subRectIndex++;
          }
          //LAB_800cf820
        }

        //LAB_800cf838
        blending.colour0Start_00 = baseColour;
        blending.colour0End_04 = baseColour;
        blending.colour1Start_08 = colour2;
        blending.colour1End_0c = colour3;

        //LAB_800cf870
        for(int i = 0; i < verticalRectCount / 2; i++) {
          //LAB_800cf8a4
          //LAB_800cf8a8
          for(int j = 0; j < horizontalRectCount; j++) {
            final WMapTextHighlightSubRectVertexColours10 subRect = this.subRectVertexColoursArray_00[subRectIndex];

            //LAB_800cf8c8
            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount / 2 - i;
            blending.blendColours(subRect.topLeft_00);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount - 1 - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount / 2 - i;
            blending.blendColours(subRect.topRight_04);

            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount / 2 - 1 - i;
            blending.blendColours(subRect.bottomLeft_08);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount - 1 - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount / 2 - 1 - i;
            blending.blendColours(subRect.bottomRight_0c);

            subRectIndex++;
          }
          //LAB_800cfaec
        }
      }

      //LAB_800cfb04
      case 3 -> {
        blending.colour0Start_00 = colour0;
        blending.colour0End_04 = baseColour;
        blending.colour1Start_08 = colour2;
        blending.colour1End_0c = baseColour;
        subRectIndex = 0;

        //LAB_800cfb50
        for(int i = 0; i < verticalRectCount; i++) {
          //LAB_800cfb70
          //LAB_800cfb74
          for(int j = 0; j < horizontalRectCount / 2; j++) {
            final WMapTextHighlightSubRectVertexColours10 subRect = this.subRectVertexColoursArray_00[subRectIndex];

            //LAB_800cfba8
            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount - i;
            blending.blendColours(subRect.topLeft_00);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - 1 - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount - i;
            blending.blendColours(subRect.topRight_04);

            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount - 1 - i;
            blending.blendColours(subRect.bottomLeft_08);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - 1 - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount - 1 - i;
            blending.blendColours(subRect.bottomRight_0c);

            subRectIndex++;
          }

          //LAB_800cfdcc
          subRectIndex += horizontalRectCount / 2;
        }

        //LAB_800cfe14
        blending.colour0Start_00 = baseColour;
        blending.colour0End_04 = colour1;
        blending.colour1Start_08 = baseColour;
        blending.colour1End_0c = colour3;
        subRectIndex = horizontalRectCount / 2;

        //LAB_800cfe7c
        for(int i = 0; i < verticalRectCount; i++) {
          //LAB_800cfe9c
          //LAB_800cfea0
          for(int j = 0; j < horizontalRectCount / 2; j++) {
            final WMapTextHighlightSubRectVertexColours10 subRect = this.subRectVertexColoursArray_00[subRectIndex];

            //LAB_800cfed4
            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount - i;
            blending.blendColours(subRect.topLeft_00);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - 1 - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount - i;
            blending.blendColours(subRect.topRight_04);

            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount - 1 - i;
            blending.blendColours(subRect.bottomLeft_08);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - 1 - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount - 1 - i;
            blending.blendColours(subRect.bottomRight_0c);

            subRectIndex++;
          }

          //LAB_800d00f8
          subRectIndex += horizontalRectCount / 2;
        }
      }

      //LAB_800d0140
      case 4 -> {
        final Vector3f blendedColour0 = new Vector3f();
        final Vector3f blendedColour1 = new Vector3f();
        final Vector3f blendedColour2 = new Vector3f();
        final Vector3f blendedColour3 = new Vector3f();
        blending.colour0Start_00 = colour0;
        blending.colour0End_04 = colour1;
        blending.colour1Start_08 = colour2;
        blending.colour1End_0c = colour3;
        blending.colourEndRatio_10 = horizontalRectCount / 2;
        blending.colourStartRatio_14 = horizontalRectCount / 2;
        blending.colour1Ratio_18 = 0;
        blending.colour0Ratio_1c = verticalRectCount;
        blending.blendColours(blendedColour0);
        blending.colourEndRatio_10 = 0;
        blending.colourStartRatio_14 = horizontalRectCount;
        blending.colour1Ratio_18 = verticalRectCount / 2;
        blending.colour0Ratio_1c = verticalRectCount / 2;
        blending.blendColours(blendedColour1);
        blending.colourEndRatio_10 = horizontalRectCount;
        blending.colourStartRatio_14 = 0;
        blending.colour1Ratio_18 = verticalRectCount / 2;
        blending.colour0Ratio_1c = verticalRectCount / 2;
        blending.blendColours(blendedColour2);
        blending.colourEndRatio_10 = horizontalRectCount / 2;
        blending.colourStartRatio_14 = horizontalRectCount / 2;
        blending.colour1Ratio_18 = verticalRectCount;
        blending.colour0Ratio_1c = 0;
        blending.blendColours(blendedColour3);

        blending.colour0Start_00 = colour0;
        blending.colour0End_04 = blendedColour0;
        blending.colour1Start_08 = blendedColour1;
        blending.colour1End_0c = baseColour;
        subRectIndex = 0;

        //LAB_800d0334
        for(int i = 0; i < verticalRectCount / 2; i++) {
          //LAB_800d0368
          //LAB_800d036c
          for(int j = 0; j < horizontalRectCount / 2; j++) {
            final WMapTextHighlightSubRectVertexColours10 subRect = this.subRectVertexColoursArray_00[subRectIndex];

            //LAB_800d03a0
            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount / 2 - i;
            blending.blendColours(subRect.topLeft_00);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - 1 - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount / 2 - i;
            blending.blendColours(subRect.topRight_04);

            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount / 2 - 1 - i;
            blending.blendColours(subRect.bottomLeft_08);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - 1 - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount / 2 - 1 - i;
            blending.blendColours(subRect.bottomRight_0c);

            subRectIndex++;
          }

          //LAB_800d060c
          subRectIndex += horizontalRectCount / 2;
        }

        //LAB_800d0654
        blending.colour0Start_00 = blendedColour0;
        blending.colour0End_04 = colour1;
        blending.colour1Start_08 = baseColour;
        blending.colour1End_0c = blendedColour2;
        subRectIndex = horizontalRectCount / 2;

        //LAB_800d06b4
        for(int i = 0; i < verticalRectCount / 2; i++) {
          //LAB_800d06e8
          //LAB_800d06ec
          for(int j = 0; j < horizontalRectCount / 2; j++) {
            final WMapTextHighlightSubRectVertexColours10 subRect = this.subRectVertexColoursArray_00[subRectIndex];

            //LAB_800d0720
            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount / 2 - i;
            blending.blendColours(subRect.topLeft_00);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - 1 - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount / 2 - i;
            blending.blendColours(subRect.topRight_04);

            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount / 2 - 1 - i;
            blending.blendColours(subRect.bottomLeft_08);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - 1 - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount / 2 - 1 - i;
            blending.blendColours(subRect.bottomRight_0c);

            subRectIndex++;
          }

          //LAB_800d098c
          subRectIndex += horizontalRectCount / 2;
        }

        //LAB_800d09d4
        blending.colour0Start_00 = blendedColour1;
        blending.colour0End_04 = baseColour;
        blending.colour1Start_08 = colour2;
        blending.colour1End_0c = blendedColour3;
        subRectIndex = horizontalRectCount * verticalRectCount / 2;

        //LAB_800d0a40
        for(int i = 0; i < verticalRectCount / 2; i++) {
          //LAB_800d0a74
          //LAB_800d0a78
          for(int j = 0; j < horizontalRectCount / 2; j++) {
            final WMapTextHighlightSubRectVertexColours10 subRect = this.subRectVertexColoursArray_00[subRectIndex];

            //LAB_800d0aac
            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount / 2 - i;
            blending.blendColours(subRect.topLeft_00);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - 1 - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount / 2 - i;
            blending.blendColours(subRect.topRight_04);

            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount / 2 - 1 - i;
            blending.blendColours(subRect.bottomLeft_08);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - 1 - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount / 2 - 1 - i;
            blending.blendColours(subRect.bottomRight_0c);

            subRectIndex++;
          }

          //LAB_800d0d18
          subRectIndex += horizontalRectCount / 2;
        }

        //LAB_800d0d60
        blending.colour0Start_00 = baseColour;
        blending.colour0End_04 = blendedColour2;
        blending.colour1Start_08 = blendedColour3;
        blending.colour1End_0c = colour3;
        subRectIndex = horizontalRectCount * verticalRectCount / 2 + horizontalRectCount / 2;

        //LAB_800d0df0
        for(int i = 0; i < verticalRectCount / 2; i++) {
          //LAB_800d0e24
          //LAB_800d0e28
          for(int j = 0; j < horizontalRectCount / 2; j++) {
            final WMapTextHighlightSubRectVertexColours10 subRect = this.subRectVertexColoursArray_00[subRectIndex];

            //LAB_800d0e5c
            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount / 2 - i;
            blending.blendColours(subRect.topLeft_00);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - 1 - j;
            blending.colour1Ratio_18 = i;
            blending.colour0Ratio_1c = verticalRectCount / 2 - i;
            blending.blendColours(subRect.topRight_04);

            blending.colourEndRatio_10 = j;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount / 2 - 1 - i;
            blending.blendColours(subRect.bottomLeft_08);

            blending.colourEndRatio_10 = j + 1;
            blending.colourStartRatio_14 = horizontalRectCount / 2 - 1 - j;
            blending.colour1Ratio_18 = i + 1;
            blending.colour0Ratio_1c = verticalRectCount / 2 - 1 - i;
            blending.blendColours(subRect.bottomRight_0c);

            subRectIndex++;
          }

          //LAB_800d10c8
          subRectIndex += horizontalRectCount / 2;
        }
      }
      //LAB_800d1110
    }
    //LAB_800d1118
  }

  public void delete() {
    if(this.highlight != null) {
      this.highlight.delete();
      this.highlight = null;
    }
  }
}
