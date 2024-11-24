package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Math;
import org.joml.Matrix4f;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;

public class RainEffect08 implements Effect<EffectManagerParams.VoidType> {
  public int count_00;
  public RaindropEffect0c[] raindropArray_04;

  private final float displayWidthModifier;

  public Obj obj;
  public final Matrix4f transforms = new Matrix4f();

  public RainEffect08(final int count, final float displayWidthModifier) {
    this.count_00 = count;
    this.raindropArray_04 = new RaindropEffect0c[count];
    Arrays.setAll(this.raindropArray_04, RaindropEffect0c::new);
    this.displayWidthModifier = displayWidthModifier;
  }

  @Override
  @Method(0x80109000L)
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    final RaindropEffect0c[] rainArray = this.raindropArray_04;

    //LAB_80109038
    for(int i = 0; i < this.count_00; i++) {
      final float endpointShiftX = MathHelper.sin(manager.params_10.rot_10.x) * 32.0f * manager.params_10.scale_16.x * rainArray[i].speed_0a;
      final float endpointShiftY = MathHelper.cos(manager.params_10.rot_10.x) * 32.0f * manager.params_10.scale_16.x * rainArray[i].speed_0a;
      rainArray[i].pos1_06.x = rainArray[i].pos0_02.x;
      rainArray[i].pos1_06.y = rainArray[i].pos0_02.y;
      rainArray[i].pos0_02.x = MathHelper.floorMod(rainArray[i].pos0_02.x + endpointShiftX, 512 * this.displayWidthModifier);
      rainArray[i].pos0_02.y = MathHelper.floorMod(rainArray[i].pos0_02.y + endpointShiftY, 256);
    }
    //LAB_80109110
  }

  @Override
  @Method(0x80108e40L)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    final RaindropEffect0c[] rainArray = this.raindropArray_04;

    if(this.obj == null) {
      this.obj = new PolyBuilder("Rain effect", GL_TRIANGLE_STRIP)
        .addVertex(0.0f, 0.0f, 0.0f)
        .monochrome(0.0f)
        .addVertex(1.0f, 0.0f, 0.0f)
        .monochrome(0.0f)
        .addVertex(0.0f, 1.0f, 0.0f)
        .monochrome(1.0f)
        .addVertex(1.0f, 1.0f, 0.0f)
        .monochrome(1.0f)
        .build();
    }

    //LAB_80108e84
    for(int i = 0; i < this.count_00; i++) {
      if(Math.abs(Math.abs(rainArray[i].pos0_02.y + rainArray[i].pos0_02.x) - Math.abs(rainArray[i].pos1_06.y + rainArray[i].pos1_06.x)) <= 180) {
        final float offsetX = GPU.getOffsetX() - 256 * this.displayWidthModifier;
        final float offsetY = GPU.getOffsetY() - 128;
        rainArray[i].pos0_02.add(offsetX, offsetY);
        rainArray[i].pos1_06.add(offsetX, offsetY);
        RENDERER.queueLine(this.obj, this.transforms, 120.0f, rainArray[i].pos1_06, rainArray[i].pos0_02)
          .translucency(Translucency.of(manager.params_10.flags_00 >>> 28 & 0x3))
          .colour(manager.params_10.colour_1c.x / 255.0f, manager.params_10.colour_1c.y / 255.0f, manager.params_10.colour_1c.z / 255.0f);
        rainArray[i].pos0_02.sub(offsetX, offsetY);
        rainArray[i].pos1_06.sub(offsetX, offsetY);
      }
      //LAB_80108f6c
    }
    //LAB_80108f84
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    if(this.obj != null) {
      this.obj.delete();
      this.obj = null;
    }
  }
}
