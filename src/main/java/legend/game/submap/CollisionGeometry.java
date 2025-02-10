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
  private static final Logger LOGGER = LogManager.getFormatterLogger(CollisionGeometry.class);

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

  public float playerRotationAfterCollision_800d1a84;
  /** Converted to an int so we can count how many frames it should be active for 60 FPS */
  public int playerRotationWasUpdated_800d1a8c;
  public boolean playerRunning;

  private boolean collisionLoaded_800f7f14;

  public Obj debugObj;
  public Obj debugLines;
  public Vector3f[] debugVertices;

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
  public int checkCollision(final boolean isNpc, final Vector3f position, final Vector3f movement) {
    if(isNpc) {
      return this.handleCollision(position.x, position.y, position.z, movement);
    }

    //LAB_800e88d8
    if(!this.playerCollisionLatch_800cbe34) {
      this.playerCollisionLatch_800cbe34 = true;

      this.playerRunning = movement.x * movement.x + movement.z * movement.z > 64.0f;

      //LAB_800e8908
      this.collidedPrimitiveIndex_800cbd94 = this.handleCollision(position.x, position.y, position.z, movement);
      this.cachedPlayerMovement_800cbd98.set(movement);

      if(this.collidedPrimitiveIndex_800cbd94 != -1) {
        if(this.playerRotationWasUpdated_800d1a8c == 0) {
          this.playerRotationWasUpdated_800d1a8c = this.smap.tickMultiplier();
          this.playerRotationAfterCollision_800d1a84 = MathHelper.floorMod(MathHelper.atan2(movement.x, movement.z) + MathHelper.PI, MathHelper.TWO_PI);
        }
      }
    } else {
      //LAB_800e8954
      movement.set(this.cachedPlayerMovement_800cbd98);
    }

    //LAB_800e897c
    //LAB_800e8980
    return this.collidedPrimitiveIndex_800cbd94;
  }

  @Method(0x800e8990L)
  public int getClosestCollisionPrimitive(final float x, final float y, final float z) {
    final Vector3f vec = new Vector3f();

    int closestIndex = 0;
    float closest = Float.MAX_VALUE;

    //LAB_800e89b8
    for(int i = 0; i < this.primitiveCount_0c; i++) {
      this.getMiddleOfCollisionPrimitive(i, vec);

      //LAB_800e8ae4
      final float dx = x - vec.x;
      final float dy = y - vec.y;
      final float dz = z - vec.z;
      final float distSqr = dx * dx + dy * dy + dz * dz;
      if(closest > distSqr) {
        closest = distSqr;
        closestIndex = i;
      }

      //LAB_800e8b2c
    }

    //LAB_800e8b34
    return closestIndex;
  }

  @Method(0x800e8b40L)
  private void loadCollisionInfo(FileData a1) {
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
    this.loadCollisionInfo(a2);

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
      this.debugLines.delete();
      this.debugLines = null;
      this.debugVertices = null;
    }
  }

  public void tick() {
    this.playerCollisionLatch_800cbe34 = false;

    if(this.playerRotationWasUpdated_800d1a8c > 0) {
      this.playerRotationWasUpdated_800d1a8c--;
    }
  }

  @Method(0x800e9018L)
  public int getCollisionPrimitiveAtPoint(final float x, final float y, final float z, final boolean checkSteepness, final boolean checkY) {
    int collisionPrimitiveIndexCount = 0;

    //LAB_800e9040
    for(int collisionPrimitiveIndex = 0; collisionPrimitiveIndex < this.primitiveCount_0c; collisionPrimitiveIndex++) {
      final CollisionPrimitiveInfo0c collisionInfo = this.primitiveInfo_14[collisionPrimitiveIndex];

      // This method did not check the Y value at all, meaning if you had collision primitives on
      // top of each other (like in Kazas) you could get stuck on one at a very different depth
      if(!checkSteepness || collisionInfo.flatEnoughToWalkOn_01) {
        // Calculate the Y position of the geometry at the given point

        // IMPORTANT NOTE:
        // If the primitive is perfectly vertical (i.e. a ladder), then the Y normal will be 0, breaking
        // the calculation. In this case, we can not calculate the Y position for any XZ coordinate because
        // the XZ plane is infinitely small. We have to instead assume that the primitive extends up and
        // down to infinity, and ignore the Y check altogether, falling back to only the XZ check.
        // This was a fix for #1077

        // NOTE #2:
        // checkY was added as a fix for #1176 - we only need to check Y for the player. Magma Fish
        // encounters were broken because they jump too high over the collision geometry and their
        // collision primitives weren't getting set as expected.

        final float primitiveY;
        if(checkY && this.normals_08[collisionPrimitiveIndex].y != 0.0f) {
          primitiveY = -(this.normals_08[collisionPrimitiveIndex].x * x + this.normals_08[collisionPrimitiveIndex].z * z + collisionInfo._08) / this.normals_08[collisionPrimitiveIndex].y;
        } else {
          primitiveY = y;
        }

        if(Math.abs(primitiveY - y) < 50.0f) {
          //LAB_800e9078
          //LAB_800e90a0
          boolean found = false;
          for(int vertexIndex = 0; vertexIndex < collisionInfo.vertexCount_00; vertexIndex++) {
            final CollisionVertexInfo0c vertexInfo = this.vertexInfo_18[collisionInfo.vertexInfoOffset_02 + vertexIndex];

            if(vertexInfo.x_00 * x + vertexInfo.z_02 * z + vertexInfo._04 < 0) {
              //LAB_800e910c
              found = true;
              break;
            }
          }

          //LAB_800e90f0
          if(!found) {
            this.collisionPrimitiveIndices_800cbe48[collisionPrimitiveIndexCount] = collisionPrimitiveIndex;
            collisionPrimitiveIndexCount++;
          }
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
    float minY = Float.MAX_VALUE;
    int minIndex = -1;

    //LAB_800e9164
    for(int i = 0; i < collisionPrimitiveIndexCount; i++) {
      final int collisionPrimitiveIndex = this.collisionPrimitiveIndices_800cbe48[i];
      final CollisionPrimitiveInfo0c collisionInfo = this.primitiveInfo_14[collisionPrimitiveIndex];

      float collisionY;
      if(this.normals_08[collisionPrimitiveIndex].y != 0) {
        collisionY = -(this.normals_08[collisionPrimitiveIndex].x * x + this.normals_08[collisionPrimitiveIndex].z * z + collisionInfo._08) / this.normals_08[collisionPrimitiveIndex].y;
      } else {
        collisionY = 0;
      }

      collisionY -= y - 20;
      if(collisionY > 0 && collisionY < minY) {
        minIndex = collisionPrimitiveIndex;
        minY = collisionY;
      }

      //LAB_800e91ec
    }

    //LAB_800e91fc
    if(minY == Float.MAX_VALUE) {
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
  private int handleCollision(final float x, final float y, final float z, final Vector3f movement) {
    if(this.smap.smapLoadingStage_800cb430 != SubmapState.RENDER_SUBMAP_12 && this.smap.smapLoadingStage_800cb430 != SubmapState.WAIT_FOR_FADE_IN) {
      return -1;
    }

    if(flEq(movement.x, 0.0f) && flEq(movement.z, 0.0f)) {
      return -1;
    }

    //LAB_800e94a4
    final int distanceMultiplier;
    if(movement.x * movement.x + movement.z * movement.z > 64.0f) {
      distanceMultiplier = 12;
    } else {
      //LAB_800e94e4
      distanceMultiplier = 4;
    }

    //LAB_800e94ec
    //LAB_800e960cdd
    final int currentPrimitiveIndex = this.getCollisionPrimitiveAtPoint(x, y, z, true, true);

    //LAB_800e9710
    if(currentPrimitiveIndex == -1) {
      final int closestPrimitiveIndex = this.getClosestCollisionPrimitive(x, y, z);

      //LAB_800e975c
      //LAB_800e9764
      final Vector3f middle = new Vector3f();
      this.getMiddleOfCollisionPrimitive(closestPrimitiveIndex, middle);
      final Vector3f normal = this.normals_08[closestPrimitiveIndex];

      //LAB_800e9870
      movement.x = Math.round(middle.x - x);
      movement.z = Math.round(middle.z - z);
      movement.y = -(normal.x * middle.x + normal.z * middle.z + this.primitiveInfo_14[closestPrimitiveIndex]._08) / normal.y;

      //LAB_800ea390
      //LAB_800ea3b4
      //LAB_800ea3e0
      return closestPrimitiveIndex;
    }

    final float endX = x + movement.x;
    final float endZ = z + movement.z;

    //LAB_800e990c
    final int destinationPrimitiveIndex = this.getCollisionPrimitiveAtPoint(endX, y, endZ, true, true);

    //LAB_800e9afc
    if(destinationPrimitiveIndex >= 0) {
      final CollisionPrimitiveInfo0c destinationPrimitive = this.primitiveInfo_14[destinationPrimitiveIndex];

      //LAB_800e9b50
      // Check if movement would place the sObj within 10 units of a boundary
      int nearBoundary = -1;
      for(int vertexIndex = 0; vertexIndex < destinationPrimitive.vertexCount_00; vertexIndex++) {
        final CollisionVertexInfo0c vertexInfo = this.vertexInfo_18[destinationPrimitive.vertexInfoOffset_02 + vertexIndex];
        if(vertexInfo.boundary_08 && Math.abs((vertexInfo.x_00 * endX + vertexInfo.z_02 * endZ + vertexInfo._04) / 0x400) < 10) {
          nearBoundary = vertexIndex;
          break;
        }
      }

      if(nearBoundary == -1) {
        final Vector3f normal = this.normals_08[destinationPrimitiveIndex];

        // This allows the sObj to move up/down a moderate slope
        if(Math.abs(y + (normal.x * endX + normal.z * endZ + destinationPrimitive._08) / normal.y) < 50) {
          //LAB_800e9e64
          movement.y = -(normal.x * (x + movement.x) + normal.z * (z + movement.z) + destinationPrimitive._08) / normal.y;

          //LAB_800ea390
          //LAB_800ea3b4
          //LAB_800ea3e0
          return destinationPrimitiveIndex;
        }
      }
    }

    //LAB_800e9c58
    // This disables "sliding" along a boundary for shop/inn primitives
    if((this.getCollisionAndTransitionInfo(currentPrimitiveIndex) & 0x20) != 0) {
      return -1;
    }

    //LAB_800e9ca0
    // Check if movement would place the sObj out-of-bounds
    int onBoundary = -1;
    for(int i = 1; i < 4 && onBoundary == -1; i++) {
      final float endX2 = x + movement.x * i;
      final float endZ2 = z + movement.z * i;

      //LAB_800e9ce8
      for(int vertexIndex = 0; vertexIndex < this.primitiveInfo_14[currentPrimitiveIndex].vertexCount_00; vertexIndex++) {
        final CollisionVertexInfo0c vertexInfo = this.vertexInfo_18[this.primitiveInfo_14[currentPrimitiveIndex].vertexInfoOffset_02 + vertexIndex];

        if(vertexInfo.boundary_08 && (vertexInfo.x_00 * endX2 + vertexInfo.z_02 * endZ2 + vertexInfo._04) / 0x400 <= 0) {
          onBoundary = vertexIndex;
          break;
        }
      }
    }

    // Handle "sliding" movement along a boundary
    if(onBoundary != -1) {
      //LAB_800e9e78

      //LAB_800e9e7c
      final CollisionVertexInfo0c vertexInfo = this.vertexInfo_18[this.primitiveInfo_14[currentPrimitiveIndex].vertexInfoOffset_02 + onBoundary];
      final float angle1 = MathHelper.atan2(endZ - z, endX - x);
      float angle2 = MathHelper.atan2(-vertexInfo.x_00, vertexInfo.z_02);
      float angleDeltaAbs = Math.abs(angle1 - angle2);
      if(angleDeltaAbs > MathHelper.PI) {
        angleDeltaAbs = MathHelper.TWO_PI - angleDeltaAbs;
      }

      //LAB_800e9f38
      // Stop movement if approaching the boundary at a nearly perpendicular angle (73 to 107 degrees)
      final float baseAngle = MathHelper.PI / 2.0f; // 90 degrees
      final float deviation = 0.29670597283903602807702743064306f; // (+/-) 17 degrees
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
        direction = -1;
      }

      //LAB_800e9fbc
      final float angleStep = 0.09817477f * direction; // 5.625 degrees

      //LAB_800e9fd0

      //LAB_800e9ff4
      // Adjust approach angle until new destination is in-bounds
      // Stop movement if +/- 39.375 degrees would still place the sObj out-of-bounds
      int s2 = -1;
      float offsetX = 0.0f;
      float offsetZ = 0.0f;
      for(int i = 0; i < 8 && s2 == -1; i++) {
        final float sin = MathHelper.sin(angle2);
        final float cos = MathHelper.cosFromSin(sin, angle2);
        offsetX = x + cos * distanceMultiplier;
        offsetZ = z + sin * distanceMultiplier;

        s2 = this.getCollisionPrimitiveAtPoint(offsetX, y, offsetZ, true, true);
        angle2 += angleStep;

        //LAB_800ea22c
      }

      //LAB_800ea254
      if(s2 < 0) {
        return -1;
      }

      //LAB_800ea234
      final Vector3f normal = this.normals_08[s2];

      // Stop movement up/down a steep slope
      if(Math.abs(y + (normal.x * offsetX + normal.z * offsetZ + this.primitiveInfo_14[s2]._08) / normal.y) >= 50) {
        return -1;
      }

      movement.x = offsetX - x;
      movement.z = offsetZ - z;
      movement.y = -(normal.x * offsetX + normal.z * offsetZ + this.primitiveInfo_14[s2]._08) / normal.y;

      return s2;
    }

    if(destinationPrimitiveIndex < 0) {
      return -1;
    }

    final Vector3f normal = this.normals_08[destinationPrimitiveIndex];

    // Stop movement up/down a steep slope
    if(Math.abs(y + (normal.x * endX + normal.z * endZ + this.primitiveInfo_14[destinationPrimitiveIndex]._08) / normal.y) >= 50) {
      return -1;
    }

    //LAB_800e9df4
    final CollisionPrimitiveInfo0c struct = this.primitiveInfo_14[destinationPrimitiveIndex];

    //LAB_800e9e64
    movement.y = -(normal.x * (x + movement.x) + normal.z * (z + movement.z) + struct._08) / normal.y;

    //LAB_800ea390
    //LAB_800ea3b4
    //LAB_800ea3e0
    return destinationPrimitiveIndex;
  }
}
