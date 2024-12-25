package legend.game.combat.particles;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import legend.game.combat.types.BattleObject;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.combat.SEffe.FUN_800cfc20;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;

public class LineParticle extends ParticleEffectData98 {
  private final Matrix4f identity = new Matrix4f();

  public LineParticle(final ParticleManager manager, @Nullable final BattleObject parentBobj, final ParticleEffectData98Inner24 effectInner, final int type, final int count) {
    super(manager, parentBobj, effectInner, type, count);
  }

  @Override
  protected void init(final int flags) {
    this.countParticleSub_54 = (short)flags;

    //LAB_80101cb0
    for(int i = 0; i < this.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 inst = this.particleArray_68[i];
      inst.subParticlePositionsArray_44 = new Vector3f[this.countParticleSub_54];
      Arrays.setAll(inst.subParticlePositionsArray_44, n -> new Vector3f().set(inst.particlePosition_50));
    }
  }

  @Override
  protected void reinitInstanceType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectInstance94 inst) {
    //LAB_80101160
    //LAB_80101170
    for(int i = 0; i < this.countParticleSub_54; i++) {
      inst.subParticlePositionsArray_44[i].set(inst.particlePosition_50);
    }
  }

  @Override
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state) {
    final EffectManagerData6c<EffectManagerParams.ParticleType> manager = state.innerStruct_00;
    this.countFramesRendered_52++;

    if(this.countParticleInstance_50 != 0) {
      final ParticleMetrics48 particleMetrics = new ParticleMetrics48();

      //LAB_800fd8dc
      for(int i = 0; i < this.countParticleInstance_50; i++) {
        final ParticleEffectInstance94 inst = this.particleArray_68[i];

        if(inst.tick(manager)) {
          //LAB_800fd918
          for(int j = this.countParticleSub_54 - 1; j > 0; j--) {
            inst.subParticlePositionsArray_44[j].set(inst.subParticlePositionsArray_44[j - 1]);
          }

          //LAB_800fd950
          inst.subParticlePositionsArray_44[0].set(inst.particlePosition_50);
          inst.beforeRender(manager);

          final Vector3f colour = new Vector3f();
          inst.tickAttributes(manager, colour);

          final Vector3f colourMod = new Vector3f();
          if((this.effectInner_08.particleInnerStuff_1c & 0x1000_0000) == 0 || (inst.flags_90 & 0x8) == 0) {
            //LAB_800fd9f4
            colourMod.set(0, 0, 0);
          } else {
            colourMod.set(colour).negate().div(2.0f);
          }

          //LAB_800fda00
          inst.flags_90 = inst.flags_90 & 0xffff_fff7 | (~(inst.flags_90 >>> 3) & 0x1) << 3;

          //LAB_800fda58
          //LAB_800fda90
          MathHelper.clamp(colour.add(colourMod), 0.0f, 0.5f);

          //LAB_800fdac8
          if((inst.flags_90 & 0x6) != 0) {
            particleMetrics.flags_00 = manager.params_10.flags_00 & 0x67ff_ffff | (inst.flags_90 >>> 1 & 0x3) << 28;
          } else {
            particleMetrics.flags_00 = manager.params_10.flags_00;
          }

          //LAB_800fdb14
          final Vector3f stepColour = new Vector3f();
          stepColour.set(colour).div(this.countParticleSub_54);

          float z = FUN_800cfc20(inst.managerRotation_68, inst.managerTranslation_2c, inst.subParticlePositionsArray_44[0], particleMetrics.p0) / 4;

          if(z + manager.params_10.z_22 >= 0xa0) {
            if(z + manager.params_10.z_22 >= 0xffe) {
              z = 0xffe - manager.params_10.z_22;
            }

            //LAB_800fdbc0
            particleMetrics.z_04 = z;

            //LAB_800fdbe4
            for(int k = 0; k < this.countParticleSub_54 - 1; k++) {
              particleMetrics.colour0_40.set(colour.x, colour.y, colour.z);
              colour.sub(stepColour);
              particleMetrics.colour1_44.set(colour.x, colour.y, colour.z);

              FUN_800cfc20(inst.managerRotation_68, inst.managerTranslation_2c, inst.subParticlePositionsArray_44[k], particleMetrics.p1);
              this.renderLineParticles(manager, particleMetrics);
              particleMetrics.p0.set(particleMetrics.p1);
            }
          }
        }
      }
    }

    //LAB_800fdd44
    if(this.scaleOrUseEffectAcceleration_6c) {
      this.effectAcceleration_70.mul(this.scaleParticleAcceleration_80);
    }

    //LAB_800fdda0
  }

  @Method(0x800fce10L)
  protected void renderLineParticles(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleMetrics48 particleMetrics) {
    if(particleMetrics.flags_00 >= 0) {
      final Obj obj = new PolyBuilder("Line particle", GL_TRIANGLE_STRIP)
        .translucency(Translucency.B_PLUS_F)
        .addVertex(0.0f, 0.0f, 0.0f)
        .rgb(particleMetrics.colour0_40)
        .addVertex(1.0f, 0.0f, 0.0f)
        .addVertex(0.0f, 1.0f, 0.0f)
        .rgb(particleMetrics.colour1_44)
        .addVertex(1.0f, 1.0f, 0.0f)
        .build();

      RENDERER.queueLine(obj, this.identity, particleMetrics.z_04 + manager.params_10.z_22, particleMetrics.p0, particleMetrics.p1)
        .screenspaceOffset(GPU.getOffsetX(), GPU.getOffsetY());

      obj.delete(); // Mark for deletion after this frame
    }

    //LAB_800fcf08
  }
}
