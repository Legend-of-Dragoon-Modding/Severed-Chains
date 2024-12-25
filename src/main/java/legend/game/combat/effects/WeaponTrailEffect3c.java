package legend.game.combat.effects;

import legend.core.QueuedModelStandard;
import legend.core.gte.MV;
import legend.core.gte.TmdObjTable1c;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.types.BattleObject;
import legend.game.scripting.ScriptState;
import legend.game.types.Model124;
import legend.game.types.Translucency;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.combat.SEffe.transformWorldspaceToScreenspace;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;

public class WeaponTrailEffect3c implements Effect<EffectManagerParams.WeaponTrailType> {
  private int currentSegmentIndex_00 = -1;
  private final int dobjIndex_08;
  /** ubyte */
  private int segmentCount_0e = 20;
  public final Vector3f largestVertex_10 = new Vector3f();
  public final Vector3f smallestVertex_20 = new Vector3f();
  private final Model124 parentModel_30;
  private final WeaponTrailEffectSegment2c[] segments_34 = new WeaponTrailEffectSegment2c[65];
  private WeaponTrailEffectSegment2c currentSegment_38;

  private final MV transforms = new MV();

  public WeaponTrailEffect3c(final int dobjIndex, final BattleObject parent) {
    Arrays.setAll(this.segments_34, WeaponTrailEffectSegment2c::new);
    this.dobjIndex_08 = dobjIndex;

    if(BattleObject.EM__.equals(parent.magic_00)) {
      this.parentModel_30 = ((ModelEffect13c)((EffectManagerData6c<?>)parent).effect_44).model_10;
    } else {
      //LAB_800ce7f8
      this.parentModel_30 = ((BattleEntity27c)parent).model_148;
    }
  }

  @Method(0x800cdcecL)
  private void getVertexMinMaxByComponent(final Model124 model, final int dobjIndex, final Vector3f smallestVertRef, final Vector3f largestVertRef, final EffectManagerData6c<EffectManagerParams.WeaponTrailType> manager) {
    float largest = -Float.MAX_VALUE;
    float smallest = Float.MAX_VALUE;
    final TmdObjTable1c tmd = model.modelParts_00[dobjIndex].tmd_08;

    //LAB_800cdd24
    for(int i = 0; i < tmd.n_vert_04; i++) {
      final Vector3f vert = tmd.vert_top_00[i];
      final float val = vert.get(manager.params_10.vertexComponent_24);

      if(val >= largest) {
        largest = val;
        largestVertRef.set(vert);
        //LAB_800cdd7c
      } else if(val <= smallest) {
        smallest = val;
        smallestVertRef.set(vert);
      }
      //LAB_800cddbc
    }
  }

  @Method(0x800cdde4L)
  private WeaponTrailEffectSegment2c getRootSegment() {
    WeaponTrailEffectSegment2c segment = this.currentSegment_38;

    //LAB_800cddfc
    while(segment.previousSegmentRef_24 != null) {
      segment = segment.previousSegmentRef_24;
    }

    //LAB_800cde14
    return segment;
  }

  @Method(0x800cde1cL)
  private WeaponTrailEffectSegment2c FUN_800cde1c() {
    WeaponTrailEffectSegment2c segment = this.segments_34[0];

    int segmentIndex = 0;
    //LAB_800cde3c
    while(segment._03) {
      segmentIndex++;
      segment = this.segments_34[segmentIndex];
    }

    //LAB_800cde50
    if(segmentIndex == 64) {
      segment = this.getRootSegment();
      segment._03 = false;

      if(segment.nextSegmentRef_28 != null) {
        segment.nextSegmentRef_28.previousSegmentRef_24 = null;
      }
    }

    //LAB_800cde80
    //LAB_800cde84
    return segment;
  }

  @Override
  @Method(0x800cde94L)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.WeaponTrailType>> state) {
    final EffectManagerData6c<EffectManagerParams.WeaponTrailType> manager = state.innerStruct_00;

    // Prevent garbage trails from rendering across the screen
    final int renderCoordThreshold = 5000;
    boolean renderCoordThresholdExceeded;

    if(this.currentSegment_38 != null) {
      final Vector3i colour = new Vector3i(manager.params_10.colour_1c).mul(0x100);
      final Vector3i colourStep = new Vector3i(colour).div(this.segmentCount_0e);
      WeaponTrailEffectSegment2c segment = this.currentSegment_38;

      final Vector2f v0 = new Vector2f();
      transformWorldspaceToScreenspace(segment.endpointCoords_04[0], v0);
      renderCoordThresholdExceeded = Math.abs(v0.x) > renderCoordThreshold || Math.abs(v0.y) > renderCoordThreshold;

      final Vector2f v2 = new Vector2f();
      final float z = transformWorldspaceToScreenspace(segment.endpointCoords_04[1], v2) / 4.0f;
      renderCoordThresholdExceeded = renderCoordThresholdExceeded || Math.abs(v2.x) > renderCoordThreshold || Math.abs(v2.y) > renderCoordThreshold;

      //LAB_800cdf94
      final PolyBuilder builder = new PolyBuilder("Weapon trail", GL_TRIANGLES);

      segment = segment.previousSegmentRef_24;
      for(int i = 0; i < this.segmentCount_0e && segment != null; i++) {
        final Vector2f v1 = new Vector2f();
        transformWorldspaceToScreenspace(segment.endpointCoords_04[0], v1);
        renderCoordThresholdExceeded = renderCoordThresholdExceeded || Math.abs(v1.x) > renderCoordThreshold || Math.abs(v1.y) > renderCoordThreshold;
        final Vector2f v3 = new Vector2f();
        transformWorldspaceToScreenspace(segment.endpointCoords_04[1], v3);
        renderCoordThresholdExceeded = renderCoordThresholdExceeded || Math.abs(v3.x) > renderCoordThreshold || Math.abs(v3.y) > renderCoordThreshold;

        if(!renderCoordThresholdExceeded) {
          builder
            .translucency(Translucency.B_PLUS_F)
            .addVertex(v0.x, v0.y, 0.0f)
            .monochrome(0.0f)
            .addVertex(v1.x, v1.y, 0.0f)
            .addVertex(v2.x, v2.y, 0.0f)
            .rgb((colour.x >>> 8) / 255.0f, (colour.y >>> 8) / 255.0f, (colour.z >>> 8) / 255.0f)
            .addVertex(v1.x, v1.y, 0.0f)
            .monochrome(0.0f)
            .addVertex(v2.x, v2.y, 0.0f)
            .rgb((colour.x >>> 8) / 255.0f, (colour.y >>> 8) / 255.0f, (colour.z >>> 8) / 255.0f);

          colour.sub(colourStep);

          builder
            .addVertex(v3.x, v3.y, 0.0f)
            .rgb((colour.x >>> 8) / 255.0f, (colour.y >>> 8) / 255.0f, (colour.z >>> 8) / 255.0f);
        }

        //LAB_800ce14c
        v0.x = v1.x;
        v0.y = v1.y;
        v2.x = v3.x;
        v2.y = v3.y;
        segment = segment.previousSegmentRef_24;
        renderCoordThresholdExceeded =
          Math.abs(v0.x) > renderCoordThreshold ||
          Math.abs(v0.y) > renderCoordThreshold ||
          Math.abs(v2.x) > renderCoordThreshold ||
          Math.abs(v2.y) > renderCoordThreshold;
      }

      float zFinal = z + manager.params_10.z_22;
      if(zFinal >= 0xa0) {
        if(zFinal >= 0xffe) {
          zFinal = 0xffe;
        }

        final Obj obj = builder.build();
        obj.delete(); // mark for deletion at end of frame

        //LAB_800ce138
        this.transforms.transfer.set(GPU.getOffsetX(), GPU.getOffsetY(), zFinal);
        RENDERER.queueOrthoModel(obj, this.transforms, QueuedModelStandard.class);
      }

      //LAB_800ce1a0
      //LAB_800ce1a4
    }

    //LAB_800ce230
  }

  @Override
  @Method(0x800ce254L)
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.WeaponTrailType>> state) {
    final EffectManagerData6c<EffectManagerParams.WeaponTrailType> manager = state.innerStruct_00;

    this.currentSegmentIndex_00++;
    if(this.currentSegmentIndex_00 == 0) {
      this.getVertexMinMaxByComponent(this.parentModel_30, this.dobjIndex_08, this.smallestVertex_20, this.largestVertex_10, manager);
      return;
    }

    //LAB_800ce2c4
    WeaponTrailEffectSegment2c segment = this.FUN_800cde1c();

    if(this.currentSegment_38 != null) {
      this.currentSegment_38.nextSegmentRef_28 = segment;
    }

    //LAB_800ce2e4
    segment._03 = true;
    segment.nextSegmentRef_28 = null;
    segment.previousSegmentRef_24 = this.currentSegment_38;
    this.currentSegment_38 = segment;

    //LAB_800ce320
    for(int i = 0; i < 2; i++) {
      final MV perspectiveTransformMatrix = new MV();
      GsGetLw(this.parentModel_30.modelParts_00[this.dobjIndex_08].coord2_04, perspectiveTransformMatrix);
      (i == 0 ? this.smallestVertex_20 : this.largestVertex_10).mul(perspectiveTransformMatrix, segment.endpointCoords_04[i]); // Yes, I hate me for this syntax too
      segment.endpointCoords_04[i].add(perspectiveTransformMatrix.transfer);
    }

    //LAB_800ce3e0
    segment = this.currentSegment_38;
    while(segment != null) {
      this.applyWeaponTrailScaling(segment.endpointCoords_04[1], segment.endpointCoords_04[0], 1.0f, 0.25f);
      segment = segment.previousSegmentRef_24;
    }

    //LAB_800ce404
    //LAB_800ce40c
    int s6;
    for(int i = 0; i < 2; i++) {
      segment = this.currentSegment_38;
      s6 = 0;

      //LAB_800ce41c
      while(segment != null) {
        if(segment.nextSegmentRef_28 != null) {
          if(segment.previousSegmentRef_24 != null) {
            WeaponTrailEffectSegment2c previousSegment = segment.previousSegmentRef_24;

            //LAB_800ce444
            final WeaponTrailEffectSegment2c[] sp0x50 = new WeaponTrailEffectSegment2c[2];
            for(int j = 0; j < 2; j++) {
              final WeaponTrailEffectSegment2c v0 = this.FUN_800cde1c();
              sp0x50[j] = v0;
              v0._03 = true;
              v0.endpointCoords_04[0].set(previousSegment.endpointCoords_04[0]).sub(segment.endpointCoords_04[0]).div(3).add(segment.endpointCoords_04[0]);
              v0.endpointCoords_04[1].set(previousSegment.endpointCoords_04[1]).sub(segment.endpointCoords_04[1]).div(3).add(segment.endpointCoords_04[1]);
              previousSegment = segment.nextSegmentRef_28;
            }

            sp0x50[0].previousSegmentRef_24 = segment.previousSegmentRef_24;
            sp0x50[1].previousSegmentRef_24 = sp0x50[0];
            sp0x50[1].nextSegmentRef_28 = segment.nextSegmentRef_28;
            sp0x50[0].nextSegmentRef_28 = sp0x50[1];
            segment.nextSegmentRef_28.previousSegmentRef_24 = sp0x50[1];
            segment.previousSegmentRef_24.nextSegmentRef_28 = sp0x50[0];
            segment._03 = false;
            segment = segment.previousSegmentRef_24;
            s6++;
            if(s6 > i * 2 || segment == null) {
              break;
            }
          }
        }

        //LAB_800ce630
        segment = segment.previousSegmentRef_24;
      }

      //LAB_800ce640
    }

    //LAB_800ce650
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.WeaponTrailType>> state) {

  }

  @Method(0x800ce83cL)
  public void setWeaponTrailSegmentCount(int segmentCount) {
    segmentCount *= 4;
    if((segmentCount & 0xff) > 0x40) {
      segmentCount = 0x40;
    }

    this.segmentCount_0e = segmentCount;
  }

  @Method(0x800ce880L)
  public void applyWeaponTrailScaling(final Vector3f largestVertex, final Vector3f smallestVertex, final float largestVertexDiffScale, final float smallestVertexDiffScale) {
    final Vector3f vertexDiff = new Vector3f();
    final Vector3f scaledVertexDiff = new Vector3f();
    vertexDiff.set(largestVertex).sub(smallestVertex);

    scaledVertexDiff.set(
      vertexDiff.x * largestVertexDiffScale,
      vertexDiff.y * largestVertexDiffScale,
      vertexDiff.z * largestVertexDiffScale
    );

    largestVertex.set(smallestVertex).add(scaledVertexDiff);

    scaledVertexDiff.set(
      vertexDiff.x * smallestVertexDiffScale,
      vertexDiff.y * smallestVertexDiffScale,
      vertexDiff.z * smallestVertexDiffScale
    );

    smallestVertex.add(scaledVertexDiff);
  }
}
