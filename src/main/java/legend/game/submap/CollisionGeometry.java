package legend.game.submap;

import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.ModelPart10;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.TmdWithId;
import legend.core.memory.Method;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.core.MathHelper.flEq;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;

public class CollisionGeometry {
  private static final Logger LOGGER = LogManager.getFormatterLogger();

  private final SMap smap;

//  public TmdObjTable1c[] objTableArrPtr_00;
  public Vector3f[] verts_04;
  public Vector3f[] normals_08;
  public int count_0c;
  public TmdObjTable1c.Primitive[] primitives_10;
  public SomethingStructSub0c_1[] ptr_14;
  public SomethingStructSub0c_2[] ptr_18;
//  public TmdWithId tmdPtr_1c;
  public final ModelPart10 dobj2Ptr_20 = new ModelPart10();
  public final GsCOORDINATE2 coord2Ptr_24 = new GsCOORDINATE2();

  private int cachedCollisionResult_800cbd94;
  private final Vector3f cachedPlayerMovement_800cbd98 = new Vector3f();

  private boolean playerCollisionLatch_800cbe34;

  private final int[] collisionPrimitiveIndices_800cbe48 = new int[8];

  public float dartRotationAfterCollision_800d1a84;
  public boolean dartRotationWasUpdated_800d1a8c;

  private boolean collisionLoaded_800f7f14;

  public CollisionGeometry(final SMap smap) {
    this.smap = smap;
  }

  public TmdObjTable1c.Primitive getPrimitiveForOffset(final int offset) {
    int primitivesIndex;
    for(primitivesIndex = 0; primitivesIndex < this.primitives_10.length - 1; primitivesIndex++) {
      if(this.primitives_10[primitivesIndex + 1].offset() > offset) {
        break;
      }
    }

    return this.primitives_10[primitivesIndex];
  }

  @Method(0x800e866cL)
  private void FUN_800e866c() {
    //LAB_800e86a4
    for(int i = 0; i < this.count_0c; i++) {
      final float y = Math.abs(this.normals_08[i].y);
      this.ptr_14[i].bool_01 = y > 0x400;
    }

    //LAB_800e86f0
  }

  @Method(0x800e88a0L)
  public int handleCollisionAndMovement(final boolean isNpc, final Vector3f position, final Vector3f movement) {
    if(isNpc) {
      return this.handleMovementAndCollision(position.x, position.y, position.z, movement);
    }

    //LAB_800e88d8
    if(!this.playerCollisionLatch_800cbe34) {
      this.playerCollisionLatch_800cbe34 = true;

      //LAB_800e8908
      this.cachedCollisionResult_800cbd94 = this.handleMovementAndCollision(position.x, position.y, position.z, movement);
      this.cachedPlayerMovement_800cbd98.set(movement);
    } else {
      //LAB_800e8954
      movement.set(this.cachedPlayerMovement_800cbd98);
    }

    //LAB_800e897c
    //LAB_800e8980
    return this.cachedCollisionResult_800cbd94;
  }

  @Method(0x800e8990L)
  public int FUN_800e8990(final float x, final float z) {
    final Vector3f vec = new Vector3f();

    int farthestIndex = 0;
    float farthest = Float.MAX_VALUE;

    //LAB_800e89b8
    for(int i = 0; i < this.count_0c; i++) {
      vec.zero();

      //LAB_800e89e0
      if(this.collisionLoaded_800f7f14) {
        //LAB_800e89f8
        final SomethingStructSub0c_1 struct2 = this.ptr_14[i];
        final TmdObjTable1c.Primitive primitive = this.getPrimitiveForOffset(struct2.primitivesOffset_04);
        final int packetOffset = struct2.primitivesOffset_04 - primitive.offset();
        final int packetIndex = packetOffset / (primitive.width() + 4);
        final int remainder = packetOffset % (primitive.width() + 4);
        final byte[] packet = primitive.data()[packetIndex];

        //LAB_800e8a38
        for(int t0 = 0; t0 < struct2.count_00; t0++) {
          vec.add(this.verts_04[IoHelper.readUShort(packet, remainder + 2 + t0 * 2)]);
        }

        //LAB_800e8a9c
        vec.div(struct2.count_00);
      }

      //LAB_800e8ae4
      final float dx = x - vec.x;
      final float dz = z - vec.z;
      final float distSqr = dx * dx + dz * dz;
      if(distSqr < farthest) {
        farthest = distSqr;
        farthestIndex = i;
      }

      //LAB_800e8b2c
    }

    //LAB_800e8b34
    return farthestIndex;
  }

  @Method(0x800e8b40L)
  private void FUN_800e8b40(FileData a1) {
    if(a1.size() < this.count_0c * 5 * 0xc) {
      LOGGER.warn("Submap file too short, padding with 0's");
      final byte[] newData = new byte[this.count_0c * 5 * 0xc];
      a1.copyFrom(0, newData, 0, a1.size());
      a1 = new FileData(newData);
    }

    this.ptr_14 = new SomethingStructSub0c_1[this.count_0c];
    this.ptr_18 = new SomethingStructSub0c_2[this.count_0c * 4];

    final FileData finalA1 = a1;
    Arrays.setAll(this.ptr_14, i -> new SomethingStructSub0c_1(finalA1.slice(i * 0xc, 0xc)));
    Arrays.setAll(this.ptr_18, i -> new SomethingStructSub0c_2(finalA1.slice((this.count_0c + i) * 0xc, 0xc)));
  }

  @Method(0x800e8bd8L)
  private void prepareCollisionModel() {
    final TmdObjTable1c objTable = this.dobj2Ptr_20.tmd_08;
    this.verts_04 = objTable.vert_top_00;
    this.normals_08 = new Vector3f[objTable.normal_top_08.length];
    Arrays.setAll(this.normals_08, i -> new Vector3f(objTable.normal_top_08[i].x * 4096.0f, objTable.normal_top_08[i].y * 4096.0f, objTable.normal_top_08[i].z * 4096.0f));
    this.count_0c = objTable.n_primitive_14;
    this.primitives_10 = objTable.primitives_10;
  }

  @Method(0x800e8c50L)
  private void loadCollisionModel(final TmdWithId tmd) {
    this.dobj2Ptr_20.tmd_08 = tmd.tmd.objTable[0];
    this.prepareCollisionModel();
  }

  @Method(0x800e8cd0L)
  public void loadCollision(final TmdWithId tmd, final FileData a2) {
    GsInitCoordinate2(null, this.coord2Ptr_24);

    this.dobj2Ptr_20.coord2_04 = this.coord2Ptr_24;
    this.dobj2Ptr_20.attribute_00 = 0x4000_0000;

    this.loadCollisionModel(tmd);
    this.FUN_800e8b40(a2);

    this.collisionLoaded_800f7f14 = true;

    this.FUN_800e866c();
  }

  @Method(0x800e8e50L)
  public void unloadCollision() {
    this.collisionLoaded_800f7f14 = false;
    this.playerCollisionLatch_800cbe34 = false;

    if(this.dobj2Ptr_20.obj != null) {
      this.dobj2Ptr_20.obj.delete();
      this.dobj2Ptr_20.obj = null;
    }
  }

  public void tick() {
    this.playerCollisionLatch_800cbe34 = false;
    this.dartRotationWasUpdated_800d1a8c = false;
  }

  @Method(0x800e9018L)
  public int FUN_800e9018(final float x, final float y, final float z, final int a3) {
    int t2 = 0;

    //LAB_800e9040
    for(int i = 0; i < this.count_0c; i++) {
      final SomethingStructSub0c_1 a1 = this.ptr_14[i];
      if(a3 != 1 || a1.bool_01) {
        //LAB_800e9078
        //LAB_800e90a0
        boolean v0 = true;
        for(int n = 0; n < a1.count_00; n++) {
          final SomethingStructSub0c_2 a0 = this.ptr_18[a1._02 + n];

          if(a0.x_00 * x + a0.z_02 * z + a0._04 < 0) {
            //LAB_800e910c
            v0 = false;
            break;
          }
        }

        //LAB_800e90f0
        if(v0) {
          this.collisionPrimitiveIndices_800cbe48[t2] = i;
          t2++;
        }
      }

      //LAB_800e9104
    }

    //LAB_800e9114
    if(t2 == 0) {
      return -1;
    }

    if(t2 == 1) {
      return this.collisionPrimitiveIndices_800cbe48[0];
    }

    //LAB_800e9134
    float t0 = Float.MAX_VALUE;
    int t3 = -1;

    //LAB_800e9164
    for(int i = 0; i < t2; i++) {
      final int a3_0 = this.collisionPrimitiveIndices_800cbe48[i];
      final SomethingStructSub0c_1 t5 = this.ptr_14[a3_0];

      float v1 = -this.normals_08[a3_0].x * x - this.normals_08[a3_0].z * z - t5._08;

      if(this.normals_08[a3_0].y != 0) {
        v1 = v1 / this.normals_08[a3_0].y;
      } else {
        v1 = 0;
      }

      v1 -= y - 20;
      if(v1 > 0 && v1 < t0) {
        t3 = a3_0;
        t0 = v1;
      }

      //LAB_800e91ec
    }

    //LAB_800e91fc
    if(t0 == Float.MAX_VALUE) {
      //LAB_800e920c
      return -1;
    }

    //LAB_800e9210
    //LAB_800e9214
    return t3;
  }

  @Method(0x800e92dcL)
  public void get3dAverageOfSomething(final int index, final Vector3f out) {
    out.zero();

    if(!this.collisionLoaded_800f7f14 || index < 0 || index >= this.count_0c) {
      //LAB_800e9318
      return;
    }

    //LAB_800e932c
    final SomethingStructSub0c_1 ss2 = this.ptr_14[index];

    final TmdObjTable1c.Primitive primitive = this.getPrimitiveForOffset(ss2.primitivesOffset_04);
    final int packetOffset = ss2.primitivesOffset_04 - primitive.offset();
    final int packetIndex = packetOffset / (primitive.width() + 4);
    final int remainder = packetOffset % (primitive.width() + 4);
    final byte[] packet = primitive.data()[packetIndex];

    final int count = ss2.count_00;

    //LAB_800e937c
    for(int i = 0; i < count; i++) {
      out.add(this.verts_04[IoHelper.readUShort(packet, remainder + 2 + i * 2)]);
    }

    //LAB_800e93e0
    out.div(count);
  }

  @Method(0x800e9430L)
  private int handleMovementAndCollision(final float x, final float y, final float z, final Vector3f movement) {
    int a1;
    int s1;
    int s2;
    final int s4;

    if(this.smap.smapLoadingStage_800cb430 != SubmapState.RENDER_SUBMAP_12) {
      return -1;
    }

    if(flEq(movement.x, 0.0f) && flEq(movement.z, 0.0f)) {
      return -1;
    }

    final Vector3f sp0x28 = new Vector3f();
    int s3 = 0;

    //LAB_800e94a4
    final int distanceMultiplier;
    if(movement.x * movement.x + movement.z * movement.z > 64.0f) {
      distanceMultiplier = 12;
    } else {
      //LAB_800e94e4
      distanceMultiplier = 4;
    }

    //LAB_800e94ec
    final float endX = x + movement.x;
    final float endZ = z + movement.z;
    final float t6 = y - 20;
    int t0 = 0;

    //LAB_800e9538
    for(int primitiveIndex = 0; primitiveIndex < this.count_0c; primitiveIndex++) {
      if(this.ptr_14[primitiveIndex].bool_01) {
        //LAB_800e9594
        boolean found = false;
        for(int i = 0; i < this.ptr_14[primitiveIndex].count_00; i++) {
          final SomethingStructSub0c_2 struct = this.ptr_18[this.ptr_14[primitiveIndex]._02 + i];

          if(struct.x_00 * x + struct.z_02 * z + struct._04 < 0) {
            //LAB_800e9604
            found = true;
            break;
          }
        }

        //LAB_800e95e8
        if(!found) {
          this.collisionPrimitiveIndices_800cbe48[t0] = primitiveIndex;
          t0++;
        }
      }

      //LAB_800e95fc
    }

    //LAB_800e960c
    if(t0 == 0) {
      s4 = -1;
    } else if(t0 == 1) {
      s4 = this.collisionPrimitiveIndices_800cbe48[0];
    } else {
      //LAB_800e962c
      float t1 = Float.MAX_VALUE;
      int t2 = -1;

      //LAB_800e965c
      for(int i = 0; i < t0; i++) {
        final int primitiveIndex = this.collisionPrimitiveIndices_800cbe48[i];
        final Vector3f normal = this.normals_08[primitiveIndex];
        final float v1 = (-normal.x * x - normal.z * z - this.ptr_14[primitiveIndex]._08) / normal.y - t6;

        if(v1 > 0 && v1 < t1) {
          t2 = primitiveIndex;
          t1 = v1;
        }

        //LAB_800e96e8
      }

      //LAB_800e96f8
      if(t1 != Float.MAX_VALUE) {
        s4 = t2;
      } else {
        //LAB_800e9708
        s4 = -1;
      }

      //LAB_800e970c
    }

    //LAB_800e9710
    if(s4 < 0) {
      final int primitiveIndex = this.FUN_800e8990(x, z);

      //LAB_800e975c
      //LAB_800e9764
      sp0x28.zero();

      if(!this.collisionLoaded_800f7f14 || primitiveIndex < 0 || primitiveIndex >= this.count_0c) {
        //LAB_800e9774
        final SomethingStructSub0c_1 ss2 = this.ptr_14[primitiveIndex];
        final TmdObjTable1c.Primitive primitive = this.getPrimitiveForOffset(ss2.primitivesOffset_04);
        final int packetOffset = ss2.primitivesOffset_04 - primitive.offset();
        final int packetIndex = packetOffset / (primitive.width() + 4);
        final int remainder = packetOffset % (primitive.width() + 4);
        final byte[] packet = primitive.data()[packetIndex];

        //LAB_800e97c4
        for(int i = 0; i < this.ptr_14[primitiveIndex].count_00; i++) {
          sp0x28.add(this.verts_04[IoHelper.readUShort(packet, remainder + 2 + i * 2)]);
        }

        //LAB_800e9828
        sp0x28.div(this.ptr_14[primitiveIndex].count_00);
      }

      //LAB_800e9870
      movement.x = Math.round(sp0x28.x - x);
      movement.z = Math.round(sp0x28.z - z);

      final Vector3f normal = this.normals_08[primitiveIndex];
      movement.y = (-normal.x * sp0x28.x - normal.z * sp0x28.z - this.ptr_14[primitiveIndex]._08) / normal.y;
    } else {
      //LAB_800e990c
      t0 = 0;

      //LAB_800e992c
      for(int n = 0; n < this.count_0c; n++) {
        if(this.ptr_14[n].bool_01) {
          //LAB_800e9988
          boolean found = false;
          for(int i = 0; i < this.ptr_14[n].count_00; i++) {
            final SomethingStructSub0c_2 struct = this.ptr_18[this.ptr_14[n]._02 + i];
            if(struct.x_00 * endX + struct.z_02 * endZ + struct._04 < 0) {
              //LAB_800e99f4
              found = true;
              break;
            }
          }

          //LAB_800e99d8
          if(!found) {
            this.collisionPrimitiveIndices_800cbe48[t0] = n;
            t0++;
          }
        }

        //LAB_800e99ec
      }

      //LAB_800e99fc
      if(t0 == 0) {
        s3 = -1;
      } else if(t0 == 1) {
        s3 = this.collisionPrimitiveIndices_800cbe48[0];
      } else {
        //LAB_800e9a1c
        float t1 = Float.MAX_VALUE;
        int t2 = -1;

        //LAB_800e9a4c
        for(int n = 0; n < t0; n++) {
          final int primitiveIndex = this.collisionPrimitiveIndices_800cbe48[n];
          final Vector3f normal = this.normals_08[primitiveIndex];

          final float v1 = (-normal.x * endX - normal.z * endZ - this.ptr_14[primitiveIndex]._08) / normal.y - t6;
          if(v1 > 0 && v1 < t1) {
            t2 = primitiveIndex;
            t1 = v1;
          }

          //LAB_800e9ad4
        }

        //LAB_800e9ae4
        if(t1 != Float.MAX_VALUE) {
          s3 = t2;
        } else {
          //LAB_800e9af4
          s3 = -1;
        }
      }

      //LAB_800e9afc
      int v0 = -1;
      if(s3 >= 0) {
        final SomethingStructSub0c_1 struct = this.ptr_14[s3];

        //LAB_800e9b50
        for(s1 = 0; s1 < struct.count_00; s1++) {
          final SomethingStructSub0c_2 struct2 = this.ptr_18[struct._02 + s1];
          if(struct2._08 != 0) {
            if(Math.abs((struct2.x_00 * endX + struct2.z_02 * endZ + struct2._04) / 0x400) < 10) {
              v0 = s1;
              break;
            }
          }
        }
      }

      //LAB_800e9bbc
      //LAB_800e9bc0
      if(s3 >= 0 && v0 < 0) {
        final Vector3f normal = this.normals_08[s3];
        final SomethingStructSub0c_1 struct = this.ptr_14[s3];

        if(Math.abs(y - (-normal.x * endX - normal.z * endZ - struct._08) / normal.y) < 50) {
          //LAB_800e9e64
          movement.y = (-normal.x * (x + movement.x) - normal.z * (z + movement.z) - struct._08) / normal.y;

          //LAB_800ea390
          //LAB_800ea3b4
          if(!this.dartRotationWasUpdated_800d1a8c) {
            this.dartRotationWasUpdated_800d1a8c = true;
            this.dartRotationAfterCollision_800d1a84 = MathHelper.floorMod(MathHelper.atan2(movement.x, movement.z) + MathHelper.PI, MathHelper.TWO_PI);
          }

          //LAB_800ea3e0
          return s3;
        }
      }

      //LAB_800e9c58
      if((this.smap.getCollisionAndTransitionInfo(s4) & 0x20) != 0) {
        return -1;
      }

      //LAB_800e9ca0
      a1 = -1;
      for(int i = 1; i < 4; i++) {
        final float endX2 = x + movement.x * i;
        final float endZ2 = z + movement.z * i;

        //LAB_800e9ce8
        for(int a1_0 = 0; a1_0 < this.ptr_14[s4].count_00; a1_0++) {
          final SomethingStructSub0c_2 struct = this.ptr_18[this.ptr_14[s4]._02 + a1_0];

          if(struct._08 != 0) {
            if((struct.x_00 * endX2 + struct.z_02 * endZ2 + struct._04) / 0x400 <= 0) {
              a1 = a1_0;
              break;
            }
          }
        }

        //LAB_800e9d44
        //LAB_800e9d48
        if(a1 >= 0) {
          break;
        }
      }

      if(a1 >= 0) {
        //LAB_800e9e78
        s2 = s4;

        //LAB_800e9e7c
        final SomethingStructSub0c_2 struct = this.ptr_18[this.ptr_14[s4]._02 + a1];
        final float angle1 = MathHelper.atan2(endZ - z, endX - x);
        float angle2 = MathHelper.atan2(-struct.x_00, struct.z_02);
        float angleDeltaAbs = Math.abs(angle1 - angle2);
        if(angleDeltaAbs > MathHelper.PI) {
          angleDeltaAbs = MathHelper.TWO_PI - angleDeltaAbs;
        }

        //LAB_800e9f38
        // About 73 to 107 degrees (90 +- 17)
        final float baseAngle = MathHelper.PI / 2.0f; // 90 degrees
        final float deviation = 0.29670597283903602807702743064306f; // 17 degrees
        if(angleDeltaAbs >= baseAngle - deviation && angleDeltaAbs <= baseAngle + deviation) {
          return -1;
        }

        if(angleDeltaAbs > baseAngle) {
          if(angle2 > 0) {
            angle2 -= MathHelper.PI;
          } else {
            //LAB_800e9f6c
            angle2 += MathHelper.PI;
          }
        }

        //LAB_800e9f70
        final float angleDelta = angle2 - angle1;

        //LAB_800e9f98
        final int direction;
        if(angleDelta > 0 && angleDelta < MathHelper.PI / 2.0f || angleDelta < -MathHelper.PI) {
          //LAB_800e9fb4
          direction = 1;
        } else {
          direction = 0;
        }

        //LAB_800e9fbc
        final float angleStep;
        if(direction == 0) {
          angleStep = -0.09817477f; // 5.625 degrees
        } else {
          angleStep = 0.09817477f; // 5.625 degrees
        }

        //LAB_800e9fd0
        angle2 -= angleStep;

        //LAB_800e9ff4
        s1 = 8;
        float offsetX;
        float offsetZ;
        do {
          angle2 += angleStep;

          final float sin = MathHelper.sin(angle2);
          final float cos = MathHelper.cosFromSin(sin, angle2);
          offsetX = x + cos * distanceMultiplier;
          offsetZ = z + sin * distanceMultiplier;

          s1--;
          if(s1 <= 0) {
            break;
          }

          t0 = 0;

          //LAB_800ea064
          for(int i = 0; i < this.count_0c; i++) {
            final SomethingStructSub0c_1 a1_0 = this.ptr_14[i];

            if(a1_0.bool_01) {
              //LAB_800ea0c4
              boolean found = false;
              for(int n = 0; n < a1_0.count_00; n++) {
                final SomethingStructSub0c_2 a0_0 = this.ptr_18[a1_0._02 + n];
                if(a0_0.x_00 * offsetX + a0_0.z_02 * offsetZ + a0_0._04 < 0) {
                  //LAB_800ea130
                  found = true;
                  break;
                }
              }

              //LAB_800ea114
              if(!found) {
                this.collisionPrimitiveIndices_800cbe48[t0] = i;
                t0++;
              }
            }

            //LAB_800ea128
          }

          //LAB_800ea138
          if(t0 == 0) {
            s2 = -1;
          } else if(t0 == 1) {
            s2 = this.collisionPrimitiveIndices_800cbe48[0];
          } else {
            //LAB_800ea158
            float t1 = Float.MAX_VALUE;
            int t2 = -1;

            //LAB_800ea17c
            for(int i = 0; i < t0; i++) {
              final int primitiveIndex = this.collisionPrimitiveIndices_800cbe48[i];
              final Vector3f normal = this.normals_08[primitiveIndex];

              final float v1_0 = (-normal.x * offsetX - normal.z * offsetZ - this.ptr_14[primitiveIndex]._08) / normal.y - t6;
              if(v1_0 > 0 && v1_0 < t1) {
                t2 = primitiveIndex;
                t1 = v1_0;
              }

              //LAB_800ea204
            }

            //LAB_800ea214
            if(t1 != Float.MAX_VALUE) {
              s2 = t2;
            } else {
              //LAB_800ea224
              s2 = -1;
            }
          }

          //LAB_800ea22c
        } while(s2 < 0);

        //LAB_800ea254
        if(s2 < 0) {
          return -1;
        }

        //LAB_800ea234
        final Vector3f normal = this.normals_08[s2];

        if(Math.abs(y - (-normal.x * offsetX - normal.z * offsetZ - this.ptr_14[s2]._08) / normal.y) >= 50) {
          return -1;
        }

        movement.y = (-normal.x * offsetX - normal.z * offsetZ - this.ptr_14[s2]._08) / normal.y;
        movement.x = offsetX - x;
        movement.z = offsetZ - z;

        return s2;
      }

      if(s3 < 0) {
        return -1;
      }

      final Vector3f normal = this.normals_08[s3];

      if(Math.abs(y - (-normal.x * endX - normal.z * endZ - this.ptr_14[s3]._08) / normal.y) >= 50) {
        return -1;
      }

      //LAB_800e9df4
      final SomethingStructSub0c_1 struct = this.ptr_14[s3];

      //LAB_800e9e64
      movement.y = (-normal.x * (x + movement.x) - normal.z * (z + movement.z) - struct._08) / normal.y;
    }

    //LAB_800ea390
    //LAB_800ea3b4
    if(!this.dartRotationWasUpdated_800d1a8c) {
      this.dartRotationWasUpdated_800d1a8c = true;
      this.dartRotationAfterCollision_800d1a84 = MathHelper.floorMod(MathHelper.atan2(movement.x, movement.z) + MathHelper.PI, MathHelper.TWO_PI);
    }

    //LAB_800ea3e0
    return s3;
  }
}
