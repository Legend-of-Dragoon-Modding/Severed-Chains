package legend.game.submap;

import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.ModelPart10;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.TmdWithId;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
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
  public int primitiveCount_0c;
  public TmdObjTable1c.Primitive[] primitives_10;
  /** One entry per primitive */
  public CollisionPrimitiveInfo0c[] primitiveInfo_14;
  /** One entry per vertex per primitive */
  public CollisionVertexInfo0c[] vertexInfo_18;
//  public TmdWithId tmdPtr_1c;
  public final ModelPart10 dobj2Ptr_20 = new ModelPart10();
  public final GsCOORDINATE2 coord2Ptr_24 = new GsCOORDINATE2();

  private final int[] collisionAndTransitions_800cb460 = new int[64];

  /** The collision primitive that the player is currently within */
  private int collidedPrimitiveIndex_800cbd94;
  private final Vector3f cachedPlayerMovement_800cbd98 = new Vector3f();

  private boolean playerCollisionLatch_800cbe34;

  private final int[] collisionPrimitiveIndices_800cbe48 = new int[8];

  public float dartRotationAfterCollision_800d1a84;
  public boolean dartRotationWasUpdated_800d1a8c;

  private boolean collisionLoaded_800f7f14;

  public Obj debugObj;
  public int[] debugIndices;

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

  public void clearCollisionAndTransitionInfo() {
    Arrays.fill(this.collisionAndTransitions_800cb460, 0);
  }

  /**
   * @return <ul>
   *   <li>0x8 - blocked</li>
   *   <li>0x10 - map transition</li>
   *   <li>0x20 - shop/inn</li>
   *   <li>Bits 16-21 - submap scene for 0x10</li>
   *   <li>Bits 22-31 - submap cut for 0x10</li>
   * </ul>
   */
  @Method(0x800e6730L)
  public int getCollisionAndTransitionInfo(final int collisionPrimitiveIndex) {
    // This did unsigned comparison, so -1 was >= 0x40
    if(collisionPrimitiveIndex < 0 || collisionPrimitiveIndex >= 0x40) {
      return 0;
    }

    return this.collisionAndTransitions_800cb460[collisionPrimitiveIndex];
  }

  @Method(0x800e675cL)
  public void setCollisionAndTransitionInfo(final int a0) {
    this.collisionAndTransitions_800cb460[(a0 >> 8 & 0xfc) / 4] = a0;
  }

  @Method(0x800e866cL)
  private void checkForSteepCollision() {
    //LAB_800e86a4
    for(int i = 0; i < this.primitiveCount_0c; i++) {
      final float y = Math.abs(this.normals_08[i].y);
      this.primitiveInfo_14[i].flatEnoughToWalkOn_01 = y > 0x400;
    }

    //LAB_800e86f0
  }

  /**
   * @return Collision primitive index that this model is within
   */
  @Method(0x800e88a0L)
  public int handleCollisionAndMovement(final boolean isNpc, final Vector3f position, final Vector3f movement) {
    if(isNpc) {
      return this.handleMovementAndCollision(position.x, position.y, position.z, movement);
    }

    //LAB_800e88d8
    if(!this.playerCollisionLatch_800cbe34) {
      this.playerCollisionLatch_800cbe34 = true;

      //LAB_800e8908
      this.collidedPrimitiveIndex_800cbd94 = this.handleMovementAndCollision(position.x, position.y, position.z, movement);
      this.cachedPlayerMovement_800cbd98.set(movement);
    } else {
      //LAB_800e8954
      movement.set(this.cachedPlayerMovement_800cbd98);
    }

    //LAB_800e897c
    //LAB_800e8980
    return this.collidedPrimitiveIndex_800cbd94;
  }

  @Method(0x800e8990L)
  public int FUN_800e8990(final float x, final float z) {
    final Vector3f vec = new Vector3f();

    int farthestIndex = 0;
    float farthest = Float.MAX_VALUE;

    //LAB_800e89b8
    for(int i = 0; i < this.primitiveCount_0c; i++) {
      this.getMiddleOfCollisionPrimitive(i, vec);

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
    if(a1.size() < this.primitiveCount_0c * 5 * 0xc) {
      LOGGER.warn("Submap file too short, padding with 0's");
      final byte[] newData = new byte[this.primitiveCount_0c * 5 * 0xc];
      a1.copyFrom(0, newData, 0, a1.size());
      a1 = new FileData(newData);
    }

    this.primitiveInfo_14 = new CollisionPrimitiveInfo0c[this.primitiveCount_0c];
    this.vertexInfo_18 = new CollisionVertexInfo0c[this.primitiveCount_0c * 4];

    final FileData finalA1 = a1;
    Arrays.setAll(this.primitiveInfo_14, i -> new CollisionPrimitiveInfo0c(finalA1.slice(i * 0xc, 0xc)));
    Arrays.setAll(this.vertexInfo_18, i -> new CollisionVertexInfo0c(finalA1.slice((this.primitiveCount_0c + i) * 0xc, 0xc)));
  }

  @Method(0x800e8bd8L)
  private void prepareCollisionModel() {
    final TmdObjTable1c objTable = this.dobj2Ptr_20.tmd_08;
    this.verts_04 = objTable.vert_top_00;
    this.normals_08 = new Vector3f[objTable.normal_top_08.length];
    Arrays.setAll(this.normals_08, i -> new Vector3f(objTable.normal_top_08[i].x * 4096.0f, objTable.normal_top_08[i].y * 4096.0f, objTable.normal_top_08[i].z * 4096.0f));
    this.primitiveCount_0c = objTable.n_primitive_14;
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

    this.checkForSteepCollision();
  }

  @Method(0x800e8e50L)
  public void unloadCollision() {
    this.collisionLoaded_800f7f14 = false;
    this.playerCollisionLatch_800cbe34 = false;

    if(this.debugObj != null) {
      this.debugObj.delete();
      this.debugObj = null;
      this.debugIndices = null;
    }
  }

  public void tick() {
    this.playerCollisionLatch_800cbe34 = false;
    this.dartRotationWasUpdated_800d1a8c = false;
  }

  @Method(0x800e9018L)
  public int FUN_800e9018(final float x, final float y, final float z, final boolean checkSteepness) {
    int collisionPrimitiveIndexCount = 0;

    //LAB_800e9040
    for(int collisionPrimitiveIndex = 0; collisionPrimitiveIndex < this.primitiveCount_0c; collisionPrimitiveIndex++) {
      final CollisionPrimitiveInfo0c collisionInfo = this.primitiveInfo_14[collisionPrimitiveIndex];
      if(!checkSteepness || collisionInfo.flatEnoughToWalkOn_01) {
        //LAB_800e9078
        //LAB_800e90a0
        boolean v0 = true;
        for(int vertexIndex = 0; vertexIndex < collisionInfo.vertexCount_00; vertexIndex++) {
          final CollisionVertexInfo0c vertexInfo = this.vertexInfo_18[collisionInfo.vertexInfoOffset_02 + vertexIndex];

          if(vertexInfo.x_00 * x + vertexInfo.z_02 * z + vertexInfo._04 < 0) {
            //LAB_800e910c
            v0 = false;
            break;
          }
        }

        //LAB_800e90f0
        if(v0) {
          this.collisionPrimitiveIndices_800cbe48[collisionPrimitiveIndexCount] = collisionPrimitiveIndex;
          collisionPrimitiveIndexCount++;
        }
      }

      //LAB_800e9104
    }

    //LAB_800e9114
    if(collisionPrimitiveIndexCount == 0) {
      return -1;
    }

    if(collisionPrimitiveIndexCount == 1) {
      return this.collisionPrimitiveIndices_800cbe48[0];
    }

    //LAB_800e9134
    float min = Float.MAX_VALUE;
    int minIndex = -1;

    //LAB_800e9164
    for(int i = 0; i < collisionPrimitiveIndexCount; i++) {
      final int collisionPrimitiveIndex = this.collisionPrimitiveIndices_800cbe48[i];
      final CollisionPrimitiveInfo0c t5 = this.primitiveInfo_14[collisionPrimitiveIndex];

      float v1;
      if(this.normals_08[collisionPrimitiveIndex].y != 0) {
        v1 = -(this.normals_08[collisionPrimitiveIndex].x * x + this.normals_08[collisionPrimitiveIndex].z * z + t5._08) / this.normals_08[collisionPrimitiveIndex].y;
      } else {
        v1 = 0;
      }

      v1 -= y - 20;
      if(v1 > 0 && v1 < min) {
        minIndex = collisionPrimitiveIndex;
        min = v1;
      }

      //LAB_800e91ec
    }

    //LAB_800e91fc
    if(min == Float.MAX_VALUE) {
      //LAB_800e920c
      return -1;
    }

    //LAB_800e9210
    //LAB_800e9214
    return minIndex;
  }

  @Method(0x800e92dcL)
  public void getMiddleOfCollisionPrimitive(final int primitiveIndex, final Vector3f out) {
    out.zero();

    if(!this.collisionLoaded_800f7f14 || primitiveIndex < 0 || primitiveIndex >= this.primitiveCount_0c) {
      //LAB_800e9318
      return;
    }

    //LAB_800e932c
    final CollisionPrimitiveInfo0c primitiveInfo = this.primitiveInfo_14[primitiveIndex];

    final TmdObjTable1c.Primitive primitive = this.getPrimitiveForOffset(primitiveInfo.primitiveOffset_04);
    final int packetOffset = primitiveInfo.primitiveOffset_04 - primitive.offset();
    final int packetIndex = packetOffset / (primitive.width() + 4);
    final int remainder = packetOffset % (primitive.width() + 4);
    final byte[] packet = primitive.data()[packetIndex];

    final int count = primitiveInfo.vertexCount_00;

    //LAB_800e937c
    for(int i = 0; i < count; i++) {
      out.add(this.verts_04[IoHelper.readUShort(packet, remainder + 2 + i * 2)]);
    }

    //LAB_800e93e0
    out.div(count);
  }

  /**
   * @return Collision primitive index that this model is within
   */
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
    for(int primitiveIndex = 0; primitiveIndex < this.primitiveCount_0c; primitiveIndex++) {
      if(this.primitiveInfo_14[primitiveIndex].flatEnoughToWalkOn_01) {
        //LAB_800e9594
        boolean found = false;
        for(int i = 0; i < this.primitiveInfo_14[primitiveIndex].vertexCount_00; i++) {
          final CollisionVertexInfo0c vertexInfo = this.vertexInfo_18[this.primitiveInfo_14[primitiveIndex].vertexInfoOffset_02 + i];

          if(vertexInfo.x_00 * x + vertexInfo.z_02 * z + vertexInfo._04 < 0) {
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
        final float v1 = (-normal.x * x - normal.z * z - this.primitiveInfo_14[primitiveIndex]._08) / normal.y - t6;

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

      if(!this.collisionLoaded_800f7f14 || primitiveIndex < 0 || primitiveIndex >= this.primitiveCount_0c) {
        //LAB_800e9774
        final CollisionPrimitiveInfo0c ss2 = this.primitiveInfo_14[primitiveIndex];
        final TmdObjTable1c.Primitive primitive = this.getPrimitiveForOffset(ss2.primitiveOffset_04);
        final int packetOffset = ss2.primitiveOffset_04 - primitive.offset();
        final int packetIndex = packetOffset / (primitive.width() + 4);
        final int remainder = packetOffset % (primitive.width() + 4);
        final byte[] packet = primitive.data()[packetIndex];

        //LAB_800e97c4
        for(int i = 0; i < this.primitiveInfo_14[primitiveIndex].vertexCount_00; i++) {
          sp0x28.add(this.verts_04[IoHelper.readUShort(packet, remainder + 2 + i * 2)]);
        }

        //LAB_800e9828
        sp0x28.div(this.primitiveInfo_14[primitiveIndex].vertexCount_00);
      }

      //LAB_800e9870
      movement.x = Math.round(sp0x28.x - x);
      movement.z = Math.round(sp0x28.z - z);

      final Vector3f normal = this.normals_08[primitiveIndex];
      movement.y = (-normal.x * sp0x28.x - normal.z * sp0x28.z - this.primitiveInfo_14[primitiveIndex]._08) / normal.y;
    } else {
      //LAB_800e990c
      t0 = 0;

      //LAB_800e992c
      for(int primitiveIndex = 0; primitiveIndex < this.primitiveCount_0c; primitiveIndex++) {
        if(this.primitiveInfo_14[primitiveIndex].flatEnoughToWalkOn_01) {
          //LAB_800e9988
          boolean found = false;
          for(int vertexIndex = 0; vertexIndex < this.primitiveInfo_14[primitiveIndex].vertexCount_00; vertexIndex++) {
            final CollisionVertexInfo0c vertexInfo = this.vertexInfo_18[this.primitiveInfo_14[primitiveIndex].vertexInfoOffset_02 + vertexIndex];
            if(vertexInfo.x_00 * endX + vertexInfo.z_02 * endZ + vertexInfo._04 < 0) {
              //LAB_800e99f4
              found = true;
              break;
            }
          }

          //LAB_800e99d8
          if(!found) {
            this.collisionPrimitiveIndices_800cbe48[t0] = primitiveIndex;
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

          final float v1 = (-normal.x * endX - normal.z * endZ - this.primitiveInfo_14[primitiveIndex]._08) / normal.y - t6;
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
        final CollisionPrimitiveInfo0c struct = this.primitiveInfo_14[s3];

        //LAB_800e9b50
        for(s1 = 0; s1 < struct.vertexCount_00; s1++) {
          final CollisionVertexInfo0c vertexInfo = this.vertexInfo_18[struct.vertexInfoOffset_02 + s1];
          if(vertexInfo._08 != 0) {
            if(Math.abs((vertexInfo.x_00 * endX + vertexInfo.z_02 * endZ + vertexInfo._04) / 0x400) < 10) {
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
        final CollisionPrimitiveInfo0c struct = this.primitiveInfo_14[s3];

        // This causes Dart to move up/down a slope
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
      if((this.getCollisionAndTransitionInfo(s4) & 0x20) != 0) {
        return -1;
      }

      //LAB_800e9ca0
      a1 = -1;
      for(int i = 1; i < 4; i++) {
        final float endX2 = x + movement.x * i;
        final float endZ2 = z + movement.z * i;

        //LAB_800e9ce8
        for(int vertexIndex = 0; vertexIndex < this.primitiveInfo_14[s4].vertexCount_00; vertexIndex++) {
          final CollisionVertexInfo0c vertexInfo = this.vertexInfo_18[this.primitiveInfo_14[s4].vertexInfoOffset_02 + vertexIndex];

          if(vertexInfo._08 != 0) {
            if((vertexInfo.x_00 * endX2 + vertexInfo.z_02 * endZ2 + vertexInfo._04) / 0x400 <= 0) {
              a1 = vertexIndex;
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
        final CollisionVertexInfo0c vertexInfo = this.vertexInfo_18[this.primitiveInfo_14[s4].vertexInfoOffset_02 + a1];
        final float angle1 = MathHelper.atan2(endZ - z, endX - x);
        float angle2 = MathHelper.atan2(-vertexInfo.x_00, vertexInfo.z_02);
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

          int collisionPrimitiveCount = 0;

          //LAB_800ea064
          for(int i = 0; i < this.primitiveCount_0c; i++) {
            final CollisionPrimitiveInfo0c collisionPrimitive = this.primitiveInfo_14[i];

            if(collisionPrimitive.flatEnoughToWalkOn_01) {
              //LAB_800ea0c4
              boolean found = false;
              for(int vertexIndex = 0; vertexIndex < collisionPrimitive.vertexCount_00; vertexIndex++) {
                final CollisionVertexInfo0c vertexInfo2 = this.vertexInfo_18[collisionPrimitive.vertexInfoOffset_02 + vertexIndex];
                if(vertexInfo2.x_00 * offsetX + vertexInfo2.z_02 * offsetZ + vertexInfo2._04 < 0) {
                  //LAB_800ea130
                  found = true;
                  break;
                }
              }

              //LAB_800ea114
              if(!found) {
                this.collisionPrimitiveIndices_800cbe48[collisionPrimitiveCount] = i;
                collisionPrimitiveCount++;
              }
            }

            //LAB_800ea128
          }

          //LAB_800ea138
          if(collisionPrimitiveCount == 0) {
            s2 = -1;
          } else if(collisionPrimitiveCount == 1) {
            s2 = this.collisionPrimitiveIndices_800cbe48[0];
          } else {
            //LAB_800ea158
            float t1 = Float.MAX_VALUE;
            int t2 = -1;

            //LAB_800ea17c
            for(int i = 0; i < collisionPrimitiveCount; i++) {
              final int primitiveIndex = this.collisionPrimitiveIndices_800cbe48[i];
              final Vector3f normal = this.normals_08[primitiveIndex];

              final float v1_0 = (-normal.x * offsetX - normal.z * offsetZ - this.primitiveInfo_14[primitiveIndex]._08) / normal.y - t6;
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

        if(Math.abs(y - (-normal.x * offsetX - normal.z * offsetZ - this.primitiveInfo_14[s2]._08) / normal.y) >= 50) {
          return -1;
        }

        movement.y = (-normal.x * offsetX - normal.z * offsetZ - this.primitiveInfo_14[s2]._08) / normal.y;
        movement.x = offsetX - x;
        movement.z = offsetZ - z;

        return s2;
      }

      if(s3 < 0) {
        return -1;
      }

      final Vector3f normal = this.normals_08[s3];

      if(Math.abs(y - (-normal.x * endX - normal.z * endZ - this.primitiveInfo_14[s3]._08) / normal.y) >= 50) {
        return -1;
      }

      //LAB_800e9df4
      final CollisionPrimitiveInfo0c struct = this.primitiveInfo_14[s3];

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
