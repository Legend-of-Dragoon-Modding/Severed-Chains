package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.function.Consumer;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.combat.Battle.seed_800fa754;
import static legend.game.combat.SEffe.scriptGetScriptedObjectPos;
import static legend.game.combat.SEffe.transformWorldspaceToScreenspace;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;

public class AdditionStarburstEffect10 implements Effect<EffectManagerParams.VoidType> {
  private static final Vector3i[] completedAdditionStarburstTranslationMagnitudes_800c6d94 = {
    new Vector3i(360, 210, 210),
    new Vector3i(210,  60, 210),
    new Vector3i(360, 210, 210),
    new Vector3i( 60, 210, 210),
  };
  private static final Vector3f[] completedAdditionStarburstAngleModifiers_800c6dac = {
    new Vector3f(0, MathHelper.psxDegToRad(-16), 0),
    new Vector3f(MathHelper.psxDegToRad(-16), 0, 0),
    new Vector3f(0, MathHelper.psxDegToRad(16), 0),
    new Vector3f(0, MathHelper.psxDegToRad(16), 0),
  };

  private final int type;
  private final int parentIndex_00;
  /** ushort */
  private final int rayCount_04;

  private final AdditionStarburstEffectRay10[] rayArray_0c;

  /**
   * <ol start="0">
   *   <li>{@link #renderAdditionHitStarburst}</li>
   *   <li>{@link #renderAdditionCompletedStarburst}</li>
   *   <li>{@link #renderAdditionCompletedStarburst}</li>
   * </ol>
   */
  public final Consumer<ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>>>[] additionStarburstRenderers_800c6dc4 = new Consumer[3];
  {
    this.additionStarburstRenderers_800c6dc4[0] = this::renderAdditionHitStarburst;
    this.additionStarburstRenderers_800c6dc4[1] = this::renderAdditionCompletedStarburst;
    this.additionStarburstRenderers_800c6dc4[2] = this::renderAdditionCompletedStarburst;
  }

  private final MV transforms = new MV();

  public AdditionStarburstEffect10(final int type, final int parentIndex, final int rayCount) {
    this.type = type;
    this.parentIndex_00 = parentIndex;
    this.rayCount_04 = rayCount;
    this.rayArray_0c = new AdditionStarburstEffectRay10[rayCount];

    //LAB_800d1ac4
    for(int rayNum = 0; rayNum < rayCount; rayNum++) {
      this.rayArray_0c[rayNum] = new AdditionStarburstEffectRay10(seed_800fa754.nextFloat(MathHelper.TWO_PI), (short)(seed_800fa754.nextInt(31)), (short)(seed_800fa754.nextInt(21) + 10), MathHelper.psxDegToRad(seed_800fa754.nextInt(11) - 5));
    }
  }

  @Override
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }

  @Override
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    this.additionStarburstRenderers_800c6dc4[this.type].accept(state);
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }

  /** If a secondary script is specified, modifies the translations of the starburst rays by the secondary script's translation. */
  @Method(0x800d1194L)
  private void modifyAdditionStarburstTranslation(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final AdditionStarburstEffect10 starburstEffect, final Vector2f outTranslation) {
    if(starburstEffect.parentIndex_00 == -1) {
      outTranslation.zero();
    } else {
      //LAB_800d11c4
      final Vector3f scriptTranslation = new Vector3f();
      scriptGetScriptedObjectPos(starburstEffect.parentIndex_00, scriptTranslation);
      scriptTranslation.add(manager.params_10.trans_04);
      transformWorldspaceToScreenspace(scriptTranslation, outTranslation);
    }

    //LAB_800d120c
  }

  @Method(0x800d1220L)
  public void renderAdditionHitStarburst(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    final float[] baseAngle = {MathHelper.psxDegToRad(-16), MathHelper.psxDegToRad(16)};

    final PolyBuilder builder = new PolyBuilder("Addition starburst", GL_TRIANGLES)
      .translucency(Translucency.B_PLUS_F);

    //LAB_800d128c
    for(int rayNum = 0; rayNum < this.rayCount_04; rayNum++) {
      final AdditionStarburstEffectRay10 ray = this.rayArray_0c[rayNum];

      //LAB_800d12a4
      for(int i = 0; i < 2; i++) {
        float angleModifier = baseAngle[i] + ray.angleModifier_0a;
        int translationScale = 30 + ray.endpointTranslationMagnitude_06;

        float angle = ray.angle_02 + angleModifier;
        float sin = MathHelper.sin(angle);
        float cos = MathHelper.cosFromSin(sin, angle);
        float x2 = cos * translationScale;
        float y2 = sin * translationScale;

        float sin2 = MathHelper.sin(ray.angle_02);
        float cos2 = MathHelper.cosFromSin(sin2, ray.angle_02);
        float x3 = cos2 * translationScale;
        float y3 = sin2 * translationScale;

        angleModifier = baseAngle[i] + ray.angleModifier_0a;
        translationScale = 210 + ray.endpointTranslationMagnitude_06;

        angle = ray.angle_02 + angleModifier;
        sin = MathHelper.sin(angle);
        cos = MathHelper.cosFromSin(sin, angle);
        final float x0 = cos * translationScale;
        final float y0 = sin * translationScale;

        sin2 = MathHelper.sin(ray.angle_02);
        cos2 = MathHelper.cosFromSin(sin2, ray.angle_02);
        final float x1 = cos2 * translationScale;
        final float y1 = sin2 * translationScale;

        final Vector2f translation = new Vector2f();
        this.modifyAdditionStarburstTranslation(manager, this, translation);
        x2 += translation.x;
        y2 += translation.y;
        x3 += translation.x;
        y3 += translation.y;

        builder
          .addVertex(x0, y0, 0.0f)
          .monochrome(0.0f)
          .addVertex(x1, y1, 0.0f)
          .rgb(manager.params_10.colour_1c.x / 255.0f, manager.params_10.colour_1c.y / 255.0f, manager.params_10.colour_1c.z / 255.0f)
          .addVertex(x2, y2, 0.0f)
          .monochrome(0.0f)
          .addVertex(x1, y1, 0.0f)
          .rgb(manager.params_10.colour_1c.x / 255.0f, manager.params_10.colour_1c.y / 255.0f, manager.params_10.colour_1c.z / 255.0f)
          .addVertex(x2, y2, 0.0f)
          .monochrome(0.0f)
          .addVertex(x3, y3, 0.0f)
          .monochrome(0.0f);
      }
    }

    final Obj obj = builder.build();
    obj.delete();

    this.transforms.transfer.set(GPU.getOffsetX(), GPU.getOffsetY(), 120.0f);
    RENDERER.queueOrthoModel(obj, this.transforms, QueuedModelStandard.class);
  }

  @Method(0x800d15d8L)
  public void renderAdditionCompletedStarburst(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    final float[] xArray = new float[3];
    final float[] yArray = new float[3];

    final PolyBuilder builder = new PolyBuilder("Addition completed starburst", GL_TRIANGLES)
      .translucency(Translucency.B_PLUS_F);

    //LAB_800d16fc
    for(int rayNum = 0; rayNum < this.rayCount_04; rayNum++) {
      final AdditionStarburstEffectRay10 ray = this.rayArray_0c[rayNum];

      ray.endpointTranslationMagnitude_06 += ray.endpointTranslationMagnitudeVelocity_08;

      //LAB_800d1728
      for(int i = 0; i < 4; i++) {
        final Vector2f translation = new Vector2f();
        this.modifyAdditionStarburstTranslation(manager, this, translation);

        //LAB_800d174c
        for(int j = 0; j < 3; j++) {
          final int translationScale = Math.max(0, completedAdditionStarburstTranslationMagnitudes_800c6d94[i].get(j) - ray.endpointTranslationMagnitude_06);

          //LAB_800d1784
          final float angle = ray.angle_02 + completedAdditionStarburstAngleModifiers_800c6dac[i].get(j);
          final float sin = MathHelper.sin(angle);
          final float cos = MathHelper.cosFromSin(sin, angle);
          xArray[j] = cos * translationScale + translation.x;
          yArray[j] = sin * translationScale + translation.y;
        }

        builder
          .addVertex(xArray[0], yArray[0], 0.0f)
          .monochrome(0.0f)
          .addVertex(xArray[1], yArray[1], 0.0f)
          .monochrome(0.0f)
          .addVertex(xArray[2], yArray[2], 0.0f)
          .rgb(manager.params_10.colour_1c.x / 255.0f, manager.params_10.colour_1c.y / 255.0f, manager.params_10.colour_1c.z / 255.0f);
      }
    }

    final Obj obj = builder.build();
    obj.delete();

    this.transforms.transfer.set(GPU.getOffsetX(), GPU.getOffsetY(), 120.0f);
    RENDERER.queueOrthoModel(obj, this.transforms, QueuedModelStandard.class);
  }
}
