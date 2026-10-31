package legend.game;

import legend.core.DebugHelper;
import legend.core.platform.input.InputKey;
import legend.game.modding.coremod.CoreMod;
import legend.game.submap.EnvironmentForegroundTextureMetrics;
import legend.game.submap.EnvironmentRenderingMetrics24;
import legend.game.submap.RetailSubmap;
import legend.game.submap.SMap;
import legend.game.title.Ttle;
import legend.lodmod.LodEngineStateTypes;
import org.junit.jupiter.api.Test;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static legend.core.GameEngine.*;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_8005.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.opengl.GL11C.*;

/** Captures the game's own final framebuffer after rendering, before SDL swaps it. */
class SelesSequenceTest {
  private int walkWaypoint;
  private int actionWaypoint = -1;
  private boolean liftRequested, liftMoving, liftDone;
  private final boolean postBattle = Boolean.parseBoolean(System.getenv().getOrDefault("DISC1_POST_BATTLE", "false"));
  private final String routeName = System.getenv().getOrDefault("DISC1_ROUTE", "");
  private final java.util.Set<InputKey> walkKeys = java.util.EnumSet.noneOf(InputKey.class);

  private void walkRoute(final SMap map, final int cut) throws Exception {
    // Let SDL key changes settle between the basement's movement ticks.
    if(cut == 90 && (int)field(SCRIPTS, "currentTicks") != 0) return;
    if(cut == 20 && this.routeName.equals("tower-guards")) return;
    // Original connected paths; scripts jump from 25 to 31 and from 17 to 6.
    final int[] route = cut == 90 && this.routeName.equals("right-exit") ? new int[] {1, 81, 95, 70, 40, 43, 44, 46, 53, 35, 49, 97, 116, 82, 118, 74, 122, 121, 60, 119, 123, 72, 3, 2} : cut == 90 ? new int[] {1, 81, 95, 70, 40, 43, 44, 46, 53, 68, 57, 8, 10, 4, 58, 59, 62, 86, 66, 107, 117, 89, 67, 9, 11} : cut == 92 ? new int[] {11, 28, 46, 43, 60, 91, 70, 90, 93, 84, 1, 17, 22, 34, 26, 34, 22, 17, 1, 84, 93, 90, 70, 91, 60, 43, 46, 28, 11, 12} : cut == 91 ? new int[] {0, 53, 47, 48, 39, 49, 50} : cut == 86 && this.routeName.equals("market-return") ? new int[] {7, 8, 16, 15, 60, 27, 37} : cut == 86 && this.routeName.equals("lower-door") ? new int[] {3, 4, 5, 31, 26, 32, 38, 39, 40} : cut == 86 ? new int[] {3, 4, 5, 31, 48, 53, 55, 54, 18, 21, 22} : cut == 69 && this.routeName.equals("west-street") ? new int[] {8, 40, 38, 42, 16, 28, 35, 5} : cut == 69 ? new int[] {20, 0, 13, 44, 1, 9, 19, 33, 24, 46, 45, 40, 8, 3} : cut == 66 ? new int[] {40, 10, 48, 5, 6, 38, 33, 36, 31, 0, 44, 2, 1, 49} : cut == 54 ? new int[] {34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 18, 35, 38, 37, 24, 17, 16, 14, 13, 12, 11, 15, 9, 8, 7, 5, 4, 3, 19, 2, 1, 0, 6} : cut == 53 && this.postBattle ? new int[] {1, 21, 18, 11, 20, 19, 14, 5, 6, 8, 9, 3, 10, 12, 13, 16, 15, 7} : cut == 53 ? new int[] {2} : cut == 52 ? new int[] {33, 34, 8, 14, 16, 37, 38, 39, 40, 41, 42, 36, 6, 5, 43, 18, 1, 13, 21, 31, 23, 22, 12, 25, 26, 20, 27, 28, 2} : cut == 51 ? new int[] {10, 21, 15, 11, 7, 22, 17, 8, 18, 5, 4, 1, 13, 16, 3, 14, 9, 6, 19, 2, 23, 0, 20} : cut == 50 ? new int[] {4, 75, 74, 49, 6, 57, 58, 7, 62, 63, 64, 65, 66, 8, 71, 50, 71, 32, 66, 65, 9, 81, 92, 67, 78, 10, 101, 105, 91, 11, 100, 52, 102, 12, 54, 51, 54, 36, 102, 52, 100, 35, 91, 105, 101, 34, 78, 67, 92, 81, 33, 65, 64, 63, 62, 31, 58, 57, 30, 49, 74, 75, 4, 5} : cut == 49 && this.routeName.equals("upper-exit") ? new int[] {45, 37, 19, 46, 22, 21, 6, 0, 35, 14, 49, 50, 51, 52, 17, 7, 18, 25, 12, 10, 48, 44, 27, 28, 29, 30, 31, 32, 33, 34, 11, 1} : cut == 49 ? new int[] {38, 5, 3, 43, 42, 40, 23, 47, 8, 13, 26, 0, 6, 21, 22, 46, 19, 37, 20} : cut == 48 ? new int[] {37, 36, 35, 33, 83, 88, 87, 73, 52, 102, 101, 100, 99, 98, 96, 95, 97, 34, 29, 15, 17, 19, 20, 21, 22, 23, 22, 21, 20, 19, 17, 15, 14, 71, 70, 69, 68, 67, 66, 65, 64, 63, 62, 60, 58, 57, 12, 11, 10, 9} : cut == 47 ? new int[] {12, 67, 68, 69, 64, 5, 54, 55, 51, 52, 48, 49, 41, 43, 38, 39, 35, 36, 32, 33, 28, 29, 15, 18, 61, 14, 57, 59, 8, 9, 10} : cut == 46 && this.routeName.equals("slide") ? new int[] {8, 0, 1, 2, 3, 4, 5, 60, 61, 86, 85, 83, 82, 81, 80, 89, 90, 91, 92, 93, 94, 54, 62, 6} : cut == 46 ? new int[] {8, 0, 1, 2, 3, 63, 64, 65, 66, 67, 68, 69, 70, 72, 73, 74, 75, 76, 77, 78, 79, 89, 90, 91, 92, 93, 94, 54, 62, 6} : cut == 45 ? new int[] {0, 2, 3, 7, 9, 10, 11, 12, 13, 14, 15, 16, 20, 21, 23, 24, 25, 27, 28, 29, 30, 31, 26, 19} : cut == 44 ? new int[] {20, 4, 3, 2, 5, 7, 16, 19, 22, 25, 23, 17, 21, 24, 6} : cut == 43 && this.routeName.equals("crossing") ? new int[] {6, 9, 4, 8, 3, 10, 11, 12, 5, 0, 14, 2} : cut == 40 && this.routeName.equals("cliff-return") ? new int[] {15, 22, 17, 12, 18, 31, 19, 13, 32, 33, 2, 16, 23, 24, 14} : cut == 41 ? new int[] {1, 3, 9, 5, 15, 16, 6, 7, 11, 19, 11, 7, 6, 16, 15, 5, 9, 3, 1, 0} : cut == 40 && this.routeName.equals("shed") ? new int[] {24, 23, 16, 2, 33, 32, 13, 19, 31, 18, 12, 17, 22, 15, 30} : cut == 42 ? new int[] {0, 7, 1, 2, 3, 2, 1, 7, 0, 5} : cut == 40 ? new int[] {5, 26, 28, 27, 8, 33, 2, 16, 23, 24, 14} : cut == 39 && this.routeName.equals("farmhouse") ? new int[] {33, 31, 29, 14, 28, 26, 17, 32, 15, 1, 11, 7, 5, 4, 0} : cut == 43 ? new int[] {6, 9, 4, 8, 9, 6, 7} : cut == 39 ? new int[] {17, 26, 28, 14, 29, 31, 33, 13} : cut == 38 ? new int[] {31, 22, 17, 20, 23, 32, 27, 12, 33, 29, 19, 21, 18, 2} : cut == 13 && this.routeName.equals("escape") ? new int[] {14, 13, 30, 1, 16, 17} : cut == 15 && this.routeName.equals("escape-hall") ? new int[] {33, 27, 17, 40, 32, 43, 29, 28, 31, 35, 26, 30, 23, 46, 22, 15, 16, 1, 24, 2, 19, 4, 10} : cut == 23 && this.routeName.equals("escape-stairs") ? new int[] {20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 8, 9, 10, 6, 3, 2, 1, 0, 27} : cut == 20 && this.routeName.equals("lower-descent") ? new int[] {7, 8, 64, 70, 65, 12, 11, 37, 33, 29, 30, 31, 38, 39, 47, 73, 68, 53, 52, 51, 22, 21} : cut == 25 && this.routeName.equals("lift-after-cell") ? new int[] {33, 37, 28, 6, 17, 18, 32} : cut == 24 && this.routeName.equals("cell-exit") ? new int[] {14, 11, 4, 37, 1, 36, 2, 26} : cut == 640 ? new int[] {0, 13, 4, 1, 4, 5, 6, 7, 8, 11, 12, 11, 8, 7, 6, 5, 4, 13, 0, 2} : cut == 24 && (this.routeName.equals("side-cell") || this.routeName.equals("cell-exit")) ? new int[] {2, 36, 1, 37, 4, 11, 14, 28} : cut == 25 && this.routeName.equals("cell-return") ? new int[] {18, 17, 6, 28, 37, 33, 36} : cut == 20 && this.routeName.equals("upper-descent") ? new int[] {0, 81, 80, 78, 76, 3, 2, 77, 14, 13, 69, 70, 64, 8, 7, 6} : cut == 28 && this.routeName.equals("lower-bridge") ? new int[] {21, 35, 36, 11, 5, 27, 14, 4, 23} : cut == 29 && this.routeName.equals("lower-bridge") ? new int[] {13, 27, 25, 24, 22} : cut == 645 ? new int[] {0, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 11, 10, 9, 8, 7, 6, 4, 3, 2, 0, 1} : cut == 27 && this.routeName.equals("lower-bridge") ? new int[] {39, 38, 10, 15, 26, 21, 34, 33, 32, 31, 30, 28} : cut == 27 ? new int[] {26, 15, 10, 38, 39, 20} : cut == 29 && this.routeName.equals("hall-exit") ? new int[] {1, 36, 16, 31, 10, 19, 4, 5, 8, 7, 33, 14, 11} : cut == 644 ? new int[] {0, 1, 2, 5, 12, 16, 9, 16, 15, 16, 12, 5, 2, 1, 0, 3} : cut == 29 ? new int[] {0, 26, 9, 16, 36, 1, 18} : cut == 28 ? new int[] {4, 14, 7, 6, 17, 22, 19} : cut == 20 && this.routeName.equals("key-return") ? new int[] {20, 4, 57, 40, 60, 59, 66, 74, 3, 76, 78, 80, 81, 0, 1} : cut == 31 ? new int[] {0, 15, 16, 17, 18, 19, 20, 10, 3, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 3, 10, 20, 19, 18, 17, 16, 15, 0, 2} : cut == 20 && this.routeName.equals("upper-ascent") ? new int[] {69, 13, 14, 77, 84, 76, 3, 74, 66, 59, 60, 40, 57, 4, 20, 5} : cut == 25 ? new int[] {2, 15, 29, 31, 3, 1, 17, 18, 32} : cut == 26 && this.routeName.equals("tower-exit") ? new int[] {0, 21, 11, 9, 38, 40, 33, 36} : cut == 641 ? new int[] {2, 3, 4, 5, 6, 7, 8, 10, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 27, 25, 30, 28, 32, 31, 30, 29, 27, 26, 22, 23, 20, 19, 18, 17, 16, 15, 14, 13, 10, 8, 7, 6, 5, 4, 2, 0} : cut == 26 && this.routeName.equals("cell-spur") ? new int[] {15, 4, 31, 3, 1, 6, 11, 21, 0, 8, 29} : cut == 642 ? new int[] {1, 3, 4, 12, 6, 14, 15, 17, 15, 14, 6, 12, 8, 10, 9, 7, 4, 3, 1, 0} : cut == 26 ? new int[] {2, 12, 28, 31, 4, 15, 17} : cut == 24 ? new int[] {0, 5, 6, 38, 9, 12, 30} : cut == 20 && this.routeName.equals("lift-ascent") ? new int[] {28, 48, 46, 45, 49, 29, 33, 37, 58, 14, 13, 12, 65, 70, 64, 8, 7, 6} : cut == 21 ? new int[] {28, 27, 2, 42, 72, 39, 47, 14, 10, 36, 29, 21, 31, 32, 33, 44, 7, 1, 3, 5, 38, 4, 15, 34, 11, 12, 23, 17, 16, 24, 19, 13, 30, 37, 26, 45, 46, 47, 39, 72, 42, 2, 27, 28, 40} : cut == 20 ? new int[] {22, 51, 52, 53, 68, 73, 47, 39, 38, 31, 30, 29, 49, 45, 46, 48, 28, 27} : cut == 15 && this.routeName.equals("east-passage") ? new int[] {4, 19, 2, 24, 1, 16, 15, 22, 46, 23, 30, 26, 35, 31, 28, 29, 43, 32, 40, 17, 27, 33, 34} : cut == 19 ? new int[] {15, 7, 10, 40, 10, 7, 15, 16} : cut == 16 && !this.routeName.isEmpty() ? new int[] {24, 21, 20, 19, 18, 17, 16, 10, 11} : cut == 15 && !this.routeName.isEmpty() ? new int[] {45, 39, 41, 42} : cut == 17 ? new int[] {11, 3, 5, 43, 49, 7, 4, 10, 48, 50, 44, 6, 44, 50, 48, 10, 4, 7, 49, 43, 5, 3, 11, 2} : cut == 23 && this.routeName.equals("storage-spur") ? new int[] {23, 24, 25, 26, 25, 24, 23, 22} : cut == 23 ? new int[] {0, 1, 2, 3, 6, 9, 8, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21} : cut == 15 && this.postBattle ? new int[] {3, 13, 12, 11, 18} : cut == 15 ? new int[] {4, 19, 44, 3, 13, 12, 11, 18} : cut == 16 && this.postBattle ? new int[] {21, 24, 25} : cut == 16 ? new int[] {28, 13, 15, 16, 17, 18, 19, 20, 21} : cut == 7 ? new int[] {2, 3, 4, 5, 6, 7, 20, 19, 18, 17, 0, 8, 9, 10, 11, 12, 13} : cut == 6 ? new int[] {11, 14, 12, 20, 18, 19, 10, 9, 3, 8, 4, 7, 5, 6, 2, 0, 13, 15, 16} : new int[] {22, 55, 56, 57, 58, 27, 26, 25, 31, 69, 68, 67, 66, 63, 79, 62, 60, 59, 18, 17, 6, 75, 76, 77, 78, 2, 1, 0};
    if(this.walkWaypoint >= route.length) return;
    final var player = map.sobjs_800c6880[0].innerStruct_00;
    // This jumping room keeps ignoreCollision set while its script handles ordinary walking.
    if(cut == 50 && player.movementType_170 != 0 || (cut == 31 || cut == 38 || cut == 39 || cut == 40 || cut == 41 || cut == 42 || cut == 43 || cut == 44 || cut == 45 || cut == 46 || cut == 47 || cut == 51 || cut == 52 || cut == 53 || cut == 54 || cut == 66 || cut == 69 || cut == 86 || cut == 91 || cut == 92 || cut == 90 || cut == 48 || cut == 49) && player.ignoreCollision_172 != 0) {
      for(final InputKey key : this.walkKeys) Input.sendKeyUp(key);
      this.walkKeys.clear();
      return;
    }
    if(cut == 41 && this.walkWaypoint == 10 && !legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.get(1)) {
      for(final InputKey key : this.walkKeys) Input.sendKeyUp(key);
      this.walkKeys.clear();
      if(player.showAlertIndicator_194) Input.sendKeyPress(InputKey.RETURN);
      else this.walkWaypoint = 9;
      return;
    }
    final boolean scriptedAction = cut == 23 && this.routeName.equals("escape-stairs") && this.walkWaypoint == 13 || cut == 20 && this.routeName.equals("lower-descent") && this.walkWaypoint == 7 || cut == 20 && this.routeName.equals("upper-descent") && this.walkWaypoint == 7 || cut == 20 && this.routeName.equals("key-return") && this.walkWaypoint == 13 || cut == 20 && this.routeName.equals("upper-ascent") && this.walkWaypoint == 5 || cut == 20 && this.routeName.equals("lift-ascent") && this.walkWaypoint == 8 || cut == 17 && this.walkWaypoint == 12 || cut == 23 && this.routeName.isEmpty() && this.walkWaypoint == 5 || cut == 21 && (this.walkWaypoint == 5 || this.walkWaypoint == 39);
    if(scriptedAction && this.actionWaypoint != this.walkWaypoint) {
      this.actionWaypoint = this.walkWaypoint;
      this.liftRequested = this.liftMoving = this.liftDone = false;
    }
    if(scriptedAction && !this.liftDone) {
      for(final InputKey key : this.walkKeys) Input.sendKeyUp(key);
      this.walkKeys.clear();
      if(!this.liftRequested && player.showAlertIndicator_194) {
        Input.sendKeyPress(InputKey.RETURN);
        this.liftRequested = true;
      }
      if(this.liftRequested && player.ignoreCollision_172 != 0) this.liftMoving = true;
      if(this.liftMoving && player.ignoreCollision_172 == 0) this.liftDone = true;
      if(cut == 20 && this.routeName.equals("key-return") && legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.get(0x65)) this.liftDone = true;
      return;
    }
    final var geometry = (legend.game.submap.CollisionGeometry)field(map, "collisionGeometry_800cbe08");
    final var position = map.sobjs_800c6880[0].innerStruct_00.model_00.coord2_14.coord.transfer;
    final var target = new org.joml.Vector3f();
    if(cut == 44 && this.walkWaypoint == 7 && position.z > -385 && !legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.get(0xd1)) {
      Input.sendKeyPress(InputKey.RETURN);
    }
    final int primitive = route[this.walkWaypoint];
    if(cut == 48 && primitive == 23 && player.showAlertIndicator_194) {
      for(final InputKey key : this.walkKeys) Input.sendKeyUp(key);
      this.walkKeys.clear();
      Input.sendKeyPress(InputKey.RETURN);
      this.walkWaypoint++;
      return;
    }
    geometry.getMiddleOfCollisionPrimitive(primitive, target);
    if(cut == 49 && primitive == 18) { target.x = -65; target.z = 145; }
    if(cut == 20 && this.routeName.equals("key-return") && primitive == 81) geometry.getMiddleOfCollisionPrimitive(0, target);
    if(cut == 17 && primitive == 6) { target.x = -472; target.z = -199; }
    if(cut == 20 && this.routeName.equals("lift-ascent") && primitive == 37) { target.x = -668; target.z = -181; }
    if(cut == 20 && this.routeName.equals("upper-ascent") && primitive == 84) { target.x = -500; target.z = -81; }
    if(cut == 20 && this.routeName.equals("lower-descent") && primitive == 11) { target.x = -664; target.z = -273; }
    final int current = geometry.getCollisionPrimitiveAtPoint(position.x, position.y, position.z, true, true);
    if((cut == 41 && primitive == 19 || cut == 23 && this.routeName.equals("escape-stairs") && primitive == 10 || cut == 20 && this.routeName.equals("lower-descent") && primitive == 11 || cut == 20 && this.routeName.equals("upper-descent") && primitive == 2 || cut == 20 && this.routeName.equals("key-return") && primitive == 81 || (cut == 17 || cut == 23) && primitive == 6 || cut == 20 && this.routeName.equals("lift-ascent") && primitive == 37 || cut == 20 && this.routeName.equals("upper-ascent") && primitive == 84 || cut == 21 && (primitive == 72 || primitive == 39) ? player.showAlertIndicator_194 : position.distanceSquared(target) < (cut == 50 && primitive == 51 ? 1600 : 225)) || (cut == 49 && primitive == 32 || cut == 50 && java.util.Set.of(0, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 27, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 56).contains(primitive) || cut == 47 && java.util.Set.of(5, 55, 52, 49, 43, 39, 36, 33, 29, 18, 14, 59).contains(primitive) || cut == 46 && (primitive == 5 || primitive == 85) || cut == 42 && primitive == 3 || cut == 43 && (primitive == 8 || this.routeName.equals("crossing") && primitive == 12) || cut == 39 && primitive == 23 || cut == 641 && java.util.Set.of(21, 26, 23, 24, 29, 25, 31, 28).contains(primitive) || cut == 625 && (primitive == 25 || primitive == 17) || cut == 6 && primitive == 13 || cut == 23 && primitive == 26 || cut == 19 && primitive == 40 || cut == 16 && !this.routeName.isEmpty() && primitive == 20) && current == primitive) {
      if(cut == 41 && primitive == 19) {
        for(final InputKey key : this.walkKeys) Input.sendKeyUp(key);
        this.walkKeys.clear();
      }
      this.walkWaypoint++;
      return;
    }
    final var from = new org.joml.Vector2f();
    final var to = new org.joml.Vector2f();
    legend.core.Transformations.toScreenspace(position, new legend.core.gte.MV(), from);
    legend.core.Transformations.toScreenspace(target, new legend.core.gte.MV(), to);
    float dx = to.x - from.x, dy = to.y - from.y;
    if(cut == 50 || cut == 49 || cut == 51 || cut == 52 || cut == 53 || cut == 54 || cut == 66 || cut == 69 || cut == 86 || cut == 91 || cut == 92 || cut == 90 || cut == 48 || cut == 47 || cut == 46 || cut == 45 || cut == 44 || cut == 41 || cut == 42 || cut == 40 || cut == 43 || cut == 39 || cut == 6 || cut == 7 || cut == 16 || cut == 15 || cut == 17 || cut == 23 || cut == 19 || cut == 20 || cut == 21 || cut == 24 || cut == 26 || cut == 642 || cut == 641 || cut == 25 || cut == 31 || cut == 28 || cut == 29 || cut == 644 || cut == 645 || cut == 640 || cut == 27) {
      // Perspective foreshortens this long ravine; steer in the game's input plane.
      final var basis = (legend.core.gte.MV)field(map, "screenToWorldMatrix_800cbd40");
      final float wx = target.x - position.x, wz = target.z - position.z;
      if(basis.m02 == 0) { dx = wx; dy = -wz; }
      else {
        final float determinant = basis.m00 * basis.m22 - basis.m20 * basis.m02;
        dx = (wx * basis.m22 - wz * basis.m20) / determinant;
        dy = -(wz * basis.m00 - wx * basis.m02) / determinant;
      }
    }
    final java.util.Set<InputKey> desired = java.util.EnumSet.noneOf(InputKey.class);
    if(Math.abs(dx) > Math.abs(dy) * 0.4f) desired.add(dx < 0 ? InputKey.LEFT : InputKey.RIGHT);
    if(Math.abs(dy) > Math.abs(dx) * 0.4f) desired.add(dy < 0 ? InputKey.UP : InputKey.DOWN);
    for(final InputKey key : this.walkKeys) if(!desired.contains(key)) Input.sendKeyUp(key);
    for(final InputKey key : desired) if(!this.walkKeys.contains(key)) Input.sendKeyDown(key);
    this.walkKeys.clear();
    this.walkKeys.addAll(desired);
  }

  @Test
  void recordScriptedSequence() throws Exception {
    final int cut = Integer.parseInt(System.getenv().getOrDefault("DISC1_CUT", "9"));
    assertTrue(this.routeName.isEmpty() || cut == 69 && this.routeName.equals("west-street") || cut == 86 && this.routeName.equals("market-return") || cut == 90 && this.routeName.equals("right-exit") || cut == 86 && this.routeName.equals("lower-door") || cut == 49 && this.routeName.equals("upper-exit") || cut == 46 && this.routeName.equals("slide") || cut == 43 && this.routeName.equals("crossing") || cut == 40 && this.routeName.equals("cliff-return") || cut == 42 && this.routeName.equals("chop") || cut == 40 && this.routeName.equals("shed") || cut == 39 && this.routeName.equals("farmhouse") || cut == 13 && this.routeName.equals("escape") || cut == 15 && (this.routeName.equals("escape-boss") || this.routeName.equals("escape-victory")) || cut == 15 && this.routeName.equals("escape-hall") || cut == 23 && this.routeName.equals("escape-stairs") || cut == 20 && this.routeName.equals("lower-descent") || cut == 24 && (this.routeName.equals("side-cell") || this.routeName.equals("cell-exit")) || cut == 20 && this.routeName.equals("upper-descent") || cut == 28 && this.routeName.equals("lower-bridge") || cut == 27 && (this.routeName.equals("reunion-return") || this.routeName.equals("escape") || (this.routeName.equals("storage-door") || this.routeName.equals("lower-bridge"))) || cut == 29 && (this.routeName.equals("hall-exit") || this.routeName.equals("lower-bridge")) || cut == 25 && (this.routeName.equals("lift-return") || this.routeName.equals("cell-return") || this.routeName.equals("lift-after-cell")) || cut == 26 && (this.routeName.equals("cell-spur") || this.routeName.equals("tower-exit")) || cut == 20 && (this.routeName.equals("lift-ascent") || this.routeName.equals("tower-guards") || this.routeName.equals("upper-ascent") || this.routeName.equals("key-return")) || cut == 23 && this.routeName.equals("storage-spur") || cut == 15 && (this.routeName.equals("upper-walkway") || this.routeName.equals("east-passage")) || cut == 16 && this.routeName.equals("rear-exit"));
    assertFalse(this.postBattle && !this.routeName.isEmpty());
    final boolean preview = "preview".equals(System.getenv("SELES_CAPTURE"));
    final boolean original = "original".equals(System.getenv("SELES_CAPTURE"));
    final Path output = Path.of(cut == 9 ? "build/seles/captures" : "build/disc1/captures/cut-" + cut + (this.postBattle ? "-post-battle" : this.routeName.isEmpty() ? "" : "-" + this.routeName), preview ? "preview" : original ? "original" : "modern");
    Files.createDirectories(output);
    // A shorter rerun must not leave old frames or a stale success report behind.
    try(final var existing = Files.list(output)) {
      for(final Path file : existing.filter(p -> p.getFileName().toString().matches("frame-\\d+\\.png|alignment\\.csv|completion\\.txt")).toList()) {
        Files.delete(file);
      }
    }
    Files.deleteIfExists(output.resolve("next-scene.txt"));
    Files.deleteIfExists(output.resolve("battle-state.txt"));
    Files.deleteIfExists(output.getParent().resolve(cut == 9 ? "verification.txt" : "verification.json"));
    final Path background = Path.of(cut == 9 ? "gfx/backgrounds/seles-overlook/background.png" : "gfx/backgrounds/disc1/cut-" + cut + "/background.png");
    final Path disabled = background.resolveSibling("background.capture-disabled.png");
    assertFalse(Files.exists(disabled));
    if(original && Files.exists(background)) {
      Files.move(background, disabled);
    }
    Thread engine = null;
    final AtomicInteger frames = new AtomicInteger();
    final CompletableFuture<Void> finished = new CompletableFuture<>();
    final List<String> trace = new ArrayList<>();
    trace.add("frame,cut,camera,player,layer,startX,startY,destX,destY,hidden,z,zFlags");
    try {
      engine = Bootstrapper.loadEngine();
      CONFIG.setConfig(CoreMod.MASTER_VOLUME_CONFIG.get(), 0.0f);
      CONFIG.setConfig(CoreMod.FMV_VOLUME_CONFIG.get(), 0.0f);
      CONFIG.setConfig(CoreMod.AUTO_TEXT_CONFIG.get(), true);
      CONFIG.setConfig(CoreMod.AUTO_TEXT_DELAY_CONFIG.get(), 0.0f);
      // Scenery walking captures use the existing no-random-encounters setting.
      if(cut == 54 || cut == 624 || cut == 5 || cut == 625 || cut == 6 || cut == 7) CONFIG.setConfig(CoreMod.ENCOUNTER_RATE_CONFIG.get(), legend.game.submap.EncounterRateMode.NONE);
      DebugHelper.sleep(1000);
      press();
      Wait.waitForFmvToStart();
      press();
      press();
      Wait.waitForFmvToStop();
      Wait.waitForEngineState(Ttle.class);
      Wait.waitFor(() -> {
        try {
          final var ready = Ttle.class.getDeclaredField("copyrightObj");
          ready.setAccessible(true);
          return ready.get(currentEngineState_8004dd04) != null;
        } catch(final ReflectiveOperationException e) {
          throw new AssertionError(e);
        }
      }, 30000, "Title assets ready for cleanup");
      onRender(() -> {
        try {
          final var release = Ttle.class.getDeclaredMethod("deallocate");
          release.setAccessible(true);
          release.invoke(currentEngineState_8004dd04);
        } catch(final ReflectiveOperationException e) {
          throw new AssertionError(e);
        }
        Harness.createFreshGameState();
        if(cut == 48) legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0xe, true);
        if(cut == 53 && this.postBattle) {
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0xf, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x10, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0x76, true);
        }
        if(cut == 44) {
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0xd3, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0xd, true);
        }
        if(cut == 43 && this.routeName.equals("crossing")) {
          for(final int flag : new int[] {1, 0xcc, 0xcd, 0xce, 0xcf, 0xd0, 0xd2}) legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(flag, true);
        }
        if(cut == 40 && this.routeName.equals("cliff-return") || cut == 42 && this.routeName.equals("chop")) {
          for(final int flag : new int[] {1, 0xcc, 0xcd, 0xce, 0xcf, 0xd2}) legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(flag, true);
        }
        if(cut == 41) {
          for(final int flag : new int[] {0xcc, 0xcd, 0xcf, 0xd2}) legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(flag, true);
        }
        if(cut == 40 && this.routeName.equals("shed")) {
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0xcc, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0xcf, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0xd2, true);
        }
        if(cut == 42) legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0xcf, true);
        if(cut == 39 && this.routeName.equals("farmhouse")) {
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0xcb, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0xcf, true);
        }
        if(cut == 13 && this.routeName.equals("escape")) {
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0xcd, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x39c, true);
        }
        if((cut == 28 || cut == 29) && this.routeName.equals("lower-bridge")) {
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x9, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0xa, true);
        }
        if(cut == 24 && (this.routeName.equals("side-cell") || this.routeName.equals("cell-exit"))) {
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x7, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x9, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0xa, true);
        }
        if(cut == 22) {
          final var state = legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
          state.scriptFlags2_bc.set(0x7, true);
          state.scriptFlags2_bc.set(0x9, true);
          state.scriptFlags1_13c.set(0xd0, true);
          state.scriptFlags1_13c.set(0xd1, true);
          state.scriptFlags1_13c.set(0xd2, true);
        }
        if(cut == 27 && (this.routeName.equals("reunion-return") || this.routeName.equals("escape") || (this.routeName.equals("storage-door") || this.routeName.equals("lower-bridge")))) {
          final var state = legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
          state.scriptFlags2_bc.set(0x9, true);
          state.scriptFlags1_13c.set(0xd0, true);
          state.scriptFlags1_13c.set(0xd1, true);
          state._1a4[3] |= 1 << 6;
          if(this.routeName.equals("escape") || (this.routeName.equals("storage-door") || this.routeName.equals("lower-bridge"))) state.scriptFlags1_13c.set(0xd2, true);
          if((this.routeName.equals("storage-door") || this.routeName.equals("lower-bridge"))) state.scriptFlags2_bc.set(0xa, true);
        }
        if(cut == 643) {
          final var state = legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
          state.scriptFlags2_bc.set(0x7, true);
          state.scriptFlags2_bc.set(0x9, true);
          state.scriptFlags1_13c.set(0xd0, true);
          state.scriptFlags1_13c.set(0xd1, true);
          state._1a4[0] |= 1 << 0x13;
        }
        if(cut == 12) {
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x280, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x281, true);
        }
        if(cut == 16 && this.postBattle) legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0xd3, true);
        if(cut == 27 && this.postBattle) {
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0xd0, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x9, true);
        }
        if(cut == 15) legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x5, true);
        if(cut == 15 && (this.routeName.equals("escape-boss") || this.routeName.equals("escape-victory"))) {
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x7, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x9, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0xa, true);
          if(this.routeName.equals("escape-victory")) legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0xb, true);
        }
        if(cut == 26 || cut == 31 || cut == 28 || cut == 29 || cut == 644 || cut == 645 || cut == 640 || cut == 27 || cut == 25 && !this.routeName.isEmpty() || cut == 20 && (this.routeName.equals("tower-guards") || this.routeName.equals("upper-ascent") || this.routeName.equals("key-return") || this.routeName.equals("upper-descent"))) legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x7, true);
        if(cut == 20 && this.routeName.equals("lower-descent")) {
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x7, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x9, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0xa, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0xd6, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0x65, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0x68, true);
        }
        if(cut == 20 && this.routeName.equals("upper-ascent")) {
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0xd6, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0x68, true);
        }
        if(cut == 20 && (this.routeName.equals("key-return") || this.routeName.equals("upper-descent"))) {
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x9, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0xd6, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0x68, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0x69, true);
        }
        if(cut == 20 && this.routeName.equals("upper-descent")) {
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0x65, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0xa, true);
        }
        if(cut == 23 && (this.postBattle || this.routeName.equals("escape-stairs"))) legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0x48, true);
        if(cut == 15 && (this.postBattle || this.routeName.equals("escape-hall"))) legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.set(0x47, true);
        if(cut == 16 && this.routeName.equals("rear-exit")) legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x5, true);
        Harness.setSimpleRandSeed(12345);
        Harness.setMersenneTwisterSeed(12345);
        submapCut_80052c30 = cut;
        submapScene_80052c34 = Integer.parseInt(System.getenv().getOrDefault("DISC1_SCENE", "0"));
        RENDERER.window().events().onDraw(() -> {
          if(finished.isDone()) {
            return;
          }
          try {
            if(!(currentEngineState_8004dd04 instanceof final SMap map)) {
              if(frames.get() > 0) {
                assertTrue(cut == 54 || cut == 53 || cut == 44 || cut == 13 && this.routeName.equals("escape") || cut == 10 || cut == 725 || cut == 12 || cut == 695 || cut == 7 || cut == 16 || cut == 15 || cut == 23 || cut == 20 || cut == 21 || cut == 24 || cut == 26 || cut == 31 || cut == 29 || cut == 27, "Unexpected engine transition");
                assertEquals(cut == 54 || cut == 44 || cut == 13 && this.routeName.equals("escape") || cut == 12 || cut == 695 || cut == 7 ? "WMap" : "Battle", currentEngineState_8004dd04.getClass().getSimpleName());
                if(cut == 15) Files.writeString(output.resolve("battle-state.txt"), "guard46=" + legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.get(0x46) + ",guard47=" + legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.get(0x47));
                Files.writeString(output.resolve("completion.txt"), "Exited cut " + cut + " to engine " + currentEngineState_8004dd04.getClass().getSimpleName() + " after " + frames.get() + " frames");
                finished.complete(null);
              }
              return;
            }
            // Match the game's battle-return script variable in this disposable replay.
            if(cut == 24 && this.postBattle && frames.get() == 0) legend.game.Scus94491BpeSegment_800b._800bd7b0 = -4;
            if(submapCut_80052c30 != cut && frames.get() > 0) {
              Files.writeString(output.resolve("next-scene.txt"), Integer.toString(submapScene_80052c34));
              Files.writeString(output.resolve("completion.txt"), "Exited cut " + cut + " to " + submapCut_80052c30 + " after " + frames.get() + " frames");
              if(cut == 9) assertEquals(10, submapCut_80052c30, "Opening must exit to cut 10");
              final int expectedNext = switch(cut) { case 675 -> 676; case 676 -> 677; case 677 -> 703; case 703 -> 704; case 704 -> 9; case 624 -> 5; case 5 -> 625; case 625 -> 6; case 6 -> 7; case 12 -> 742; default -> submapCut_80052c30; };
              assertEquals(expectedNext, submapCut_80052c30, "Story order changed");
              finished.complete(null);
              return;
            }
            // The opening cutscene runs while submapFullyLoaded remains false.
            if(map.submap == null || map.sobjs_800c6880[0] == null || field(map.submap, "backgroundTexture") == null) {
              return;
            }
            // Asset upload may finish one frame before scripts initialize the pan.
            // Anchor both recordings to the same scripted pose, not load timing.
            if(cut == 9 && frames.get() == 0) {
              final var layers = (EnvironmentForegroundTextureMetrics[])field(map.submap, "envForegroundMetrics_800cb590");
              if(layers[0].startX != -150 || layers[0].destX != -149) {
                return;
              }
            }
            if(frames.get() == 0) {
              if((cut == 28 || cut == 29) && this.routeName.equals("lower-bridge")) {
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x9, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0xa, true);
        }
        if(cut == 24 && (this.routeName.equals("side-cell") || this.routeName.equals("cell-exit"))) {
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x7, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0x9, true);
          legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.set(0xa, true);
        }
        if(cut == 22) {
                final var metrics = (EnvironmentRenderingMetrics24[])field(map.submap, "envRenderMetrics_800cb710");
                final int backgroundCount = (Integer)field(map.submap, "envBackgroundTextureCount_800cb57c");
                if((int)field(SCRIPTS, "currentTicks") != 0 || metrics[backgroundCount + 1].zFlags_22 != 33168 || metrics[backgroundCount + 11].zFlags_22 != 32998) return;
              }
              if((cut == 5 || cut == 625 || cut == 6 || cut == 7 || cut == 15 || cut == 17 || cut == 23 || cut == 19 || cut == 20 || cut == 21 || cut == 24 || cut == 26 || cut == 642 || cut == 641 || cut == 25 || cut == 31 || cut == 28 || cut == 29 || cut == 644 || cut == 645 || cut == 640 || cut == 27 || cut == 56 || cut == 13 || cut == 38 || cut == 39 || cut == 40 || cut == 41 || cut == 42 || cut == 43 || cut == 44 || cut == 45 || cut == 46 || cut == 47 || cut == 51 || cut == 52 || cut == 53 || cut == 54 || cut == 66 || cut == 69 || cut == 86 || cut == 91 || cut == 92 || cut == 90 || cut == 48 || cut == 50 || cut == 49) && !(cut == 39 && this.routeName.equals("farmhouse")) && (int)field(SCRIPTS, "currentTicks") != 0) return;
              // The first interpolated camera step occurs once, after scripts initialize.
              if(cut == 23 && this.routeName.equals("escape-stairs")) {
                final var metrics = (EnvironmentRenderingMetrics24[])field(map.submap, "envRenderMetrics_800cb710");
                final int backgroundCount = (Integer)field(map.submap, "envBackgroundTextureCount_800cb57c");
                if(metrics[backgroundCount + 7].zFlags_22 != 33208) return;
              }
              // Idle loading poses can persist for different numbers of render frames.
              if(cut == 51) {
                final var metrics = (EnvironmentRenderingMetrics24[])field(map.submap, "envRenderMetrics_800cb710");
                final int backgroundCount = (Integer)field(map.submap, "envBackgroundTextureCount_800cb57c");
                if(metrics[backgroundCount].zFlags_22 != 16384) return;
              }
              if(cut == 46) {
                final var metrics = (EnvironmentRenderingMetrics24[])field(map.submap, "envRenderMetrics_800cb710");
                final int backgroundCount = (Integer)field(map.submap, "envBackgroundTextureCount_800cb57c");
                if(metrics[backgroundCount + 1].zFlags_22 != 33042) return;
              }
              final float[] anchor = switch(cut) {
                case 675 -> new float[] {-37.5f, 12};
                case 676 -> new float[] {0, 52.5f};
                case 677 -> new float[] {30, -4};
                case 703 -> new float[] {-196, -11.5f};
                case 704 -> new float[] {-6, -4};
                case 742 -> new float[] {0, -36};
                case 694 -> new float[] {-4, -8};
                case 695 -> new float[] {0, -8};
                case 624 -> new float[] {-64, -8};
                case 14 -> new float[] {0, -8};
                case 37 -> new float[] {-126.5f, 6};
                case 25 -> this.routeName.isEmpty() ? new float[] {-4, 8} : null;
                case 641 -> new float[] {0, -8};
                case 644 -> new float[] {-4, -8};
                case 31 -> new float[] {-64, -72};
                case 39 -> this.routeName.isEmpty() ? new float[] {-128, -72} : null;
                case 43 -> new float[] {-4, 8};
                case 48 -> new float[] {128, 72};
                case 66 -> new float[] {64, -72};
                case 69 -> this.routeName.isEmpty() ? new float[] {-74, -72} : new float[] {0, 26.75f};
                case 91 -> new float[] {0, -72};
                case 90 -> new float[] {128, -72};
                case 42 -> new float[] {0, -8};
                case 28 -> this.routeName.isEmpty() ? new float[] {0, 8} : null;
                case 29 -> this.routeName.isEmpty() ? new float[] {0, 0} : null;
                case 16 -> this.routeName.equals("rear-exit") ? null : this.postBattle ? new float[] {64, -72} : new float[] {-64, -51};
                default -> null;
              };
              final var camera = (org.joml.Vector2f)field(map.submap, "screenOffset");
              if(cut == 92 && Math.abs(camera.y + 35.70f) > 0.05f) return;
              if(cut == 86 && this.routeName.equals("market-return") && Math.abs(camera.y + 24.31f) > 0.05f) return;
              if(cut == 86 && !this.routeName.equals("market-return") && Math.abs(camera.y + 44.47f) > 0.05f) return;
              if(cut == 54 && Math.abs(camera.y + 118.70f) > 0.05f) return;
              if(cut == 49 && this.routeName.equals("upper-exit") && Math.abs(camera.y - 64.10f) > 0.01f) return;
              if(cut == 20 && this.routeName.equals("upper-ascent") && Math.abs(camera.y - 44.54f) > 0.01f) return;
              if(cut == 20 && this.routeName.equals("lower-descent") && Math.abs(camera.y - 34.19f) > 0.01f) return;
              if(cut == 7 && Math.abs(camera.y - 33.45f) > 0.01f) return;
              if(cut == 19 && Math.abs(camera.y + 72.85f) > 0.01f) return;
              if(cut == 23 && this.routeName.isEmpty() && camera.y != -136) return;
              if(anchor != null && (camera.x != anchor[0] || camera.y != anchor[1])) return;
            }
            if(cut == 10 && frames.get() == 0) {
              final var position = map.sobjs_800c6880[0].innerStruct_00.model_00.coord2_14.coord.transfer;
              if(position.x != -93.75f || position.y != -311.5f || position.z != 527.5f) return;
            }
            if(cut == 39 && this.routeName.equals("farmhouse") && frames.get() == 0 && map.sobjs_800c6880[0].innerStruct_00.model_00.coord2_14.coord.transfer.x != -561) return;
            if(cut == 38 && frames.get() == 0) {
              final var position = map.sobjs_800c6880[0].innerStruct_00.model_00.coord2_14.coord.transfer;
              if(position.x != 820.75f || position.z != 794.75f) return;
            }
            if(cut == 41 && frames.get() == 0) {
              final var position = map.sobjs_800c6880[0].innerStruct_00.model_00.coord2_14.coord.transfer;
              if(position.x != 44 || position.z != 88) return;
            }
            if(cut == 27 && this.routeName.equals("reunion-return") && frames.get() == 0) {
              final var position = map.sobjs_800c6880[0].innerStruct_00.model_00.coord2_14.coord.transfer;
              if(position.x != -261 || position.y != -38 || position.z != 37) return;
            }
            if(cut == 725 && frames.get() == 0 && map.sobjs_800c6880[0].innerStruct_00.model_00.coord2_14.coord.transfer.y != -0.4375f) return;
            final int frame = frames.getAndIncrement();
            if(frame == 0) verifyTextures(map, cut, original);
            if((cut == 90 || cut == 92 || cut == 91 || cut == 86 || cut == 41 && legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.get(0xce) || cut == 50 || cut == 49 || cut == 51 || cut == 52 || cut == 53 || cut == 54 || cut == 66 || cut == 69 || cut == 86 || cut == 91 || cut == 92 || cut == 90 || cut == 48 || cut == 47 || cut == 46 || cut == 45 || cut == 44 || cut == 42 || cut == 40 || cut == 43 || cut == 39 && legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.get(0xcb) || cut == 38 && legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.get(0xc8) || cut == 13 && this.routeName.equals("escape") || cut == 625 || cut == 6 || cut == 7 || cut == 16 || cut == 15 || cut == 17 || cut == 23 || cut == 19 || cut == 20 || cut == 21 || cut == 25 || cut == 24 || cut == 26 || cut == 642 || cut == 641 || cut == 25 || cut == 31 || cut == 28 || cut == 29 || cut == 644 || cut == 645 || cut == 640 || cut == 27 || cut == 13 || cut == 38 || cut == 39 || cut == 40 || cut == 41 || cut == 42 || cut == 43 || cut == 44 || cut == 45 || cut == 46 || cut == 47 || cut == 51 || cut == 52 || cut == 53 || cut == 54 || cut == 66 || cut == 69 || cut == 86 || cut == 91 || cut == 92 || cut == 90 || cut == 48 || cut == 50 || cut == 49) && frame == 120) {
              final var geometry = (legend.game.submap.CollisionGeometry)field(map, "collisionGeometry_800cbe08");
              final var points = new ArrayList<String>();
              for(int i = 0; i < geometry.primitiveCount_0c; i++) {
                final var center = new org.joml.Vector3f();
                final var screen = new org.joml.Vector2f();
                geometry.getMiddleOfCollisionPrimitive(i, center);
                legend.core.Transformations.toScreenspace(center, new legend.core.gte.MV(), screen);
                points.add(i + "," + center.x + "," + center.z + "," + screen.x + "," + screen.y + "," + geometry.getCollisionAndTransitionInfo(i));
                final var info = geometry.primitiveInfo_14[i];
                final var primitive = geometry.getPrimitiveForOffset(info.primitiveOffset_04);
                final int offset = info.primitiveOffset_04 - primitive.offset();
                final byte[] packet = primitive.data()[offset / (primitive.width() + 4)];
                final var vertices = new ArrayList<Integer>();
                for(int v = 0; v < info.vertexCount_00; v++) vertices.add(legend.core.IoHelper.readUShort(packet, offset % (primitive.width() + 4) + 2 + v * 2));
                points.set(points.size() - 1, points.getLast() + "," + vertices);
              }
              Files.write(output.resolve("walkmesh.csv"), points);
              final var vertexPoints = new ArrayList<String>();
              for(int v = 0; v < geometry.verts_04.length; v++) {
                final var vertex = geometry.verts_04[v];
                vertexPoints.add(v + "," + vertex.x + "," + vertex.y + "," + vertex.z);
              }
              Files.write(output.resolve("vertices.csv"), vertexPoints);
            }
            // The village approach hands movement to the player after the opening.
            if(cut == 10 && frame == 120) Input.sendKeyDown(InputKey.DOWN);
            if(cut == 13 && this.routeName.isEmpty() && frame == 1200) Input.sendKeyDown(InputKey.UP);
            if(cut == 14 && frame % 10 == 0 && map.sobjs_800c6880[0].innerStruct_00.showAlertIndicator_194) Input.sendKeyPress(InputKey.RETURN);
            if(cut == 10 && frame >= 600 && frame % 30 == 0 && map.sobjs_800c6880[0].innerStruct_00.showAlertIndicator_194) Input.sendKeyPress(InputKey.RETURN);
            if(cut == 10 && frame == 1800) Input.sendKeyUp(InputKey.DOWN);
            if(cut == 12 && frame == 120) Input.sendKeyDown(InputKey.UP);
            if(cut == 12 && frame == 720) {
              Input.sendKeyUp(InputKey.UP);
              Input.sendKeyDown(InputKey.RIGHT);
            }
            if(cut == 12 && frame == 920) {
              Input.sendKeyUp(InputKey.RIGHT);
              Input.sendKeyDown(InputKey.UP);
            }
            if(cut == 624 && frame == 120) Input.sendKeyDown(InputKey.LEFT);
            if(cut == 624 && frame == 240) Input.sendKeyDown(InputKey.UP);
            if(cut == 624 && frame == 300) {
              Input.sendKeyUp(InputKey.LEFT);
            }
            if(cut == 5 && frame == 120) {
              Input.sendKeyDown(InputKey.UP);
              Input.sendKeyDown(InputKey.LEFT);
            }
            if(cut == 5 && frame == 220) Input.sendKeyUp(InputKey.UP);
            if(cut == 5 && frame == 360) {
              Input.sendKeyUp(InputKey.LEFT);
              Input.sendKeyDown(InputKey.DOWN);
            }
            if(cut == 5 && frame == 400) Input.sendKeyDown(InputKey.LEFT);
            if((cut == 90 || cut == 92 || cut == 91 || cut == 86 || cut == 41 && legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.get(0xce) || cut == 50 || cut == 49 || cut == 51 || cut == 52 || cut == 53 || cut == 54 || cut == 66 || cut == 69 || cut == 86 || cut == 91 || cut == 92 || cut == 90 || cut == 48 || cut == 47 || cut == 46 || cut == 45 || cut == 44 || cut == 42 || cut == 40 || cut == 43 || cut == 39 && legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.get(0xcb) || cut == 38 && legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags1_13c.get(0xc8) || cut == 13 && this.routeName.equals("escape") || cut == 625 || cut == 6 || cut == 7 || cut == 15 || cut == 17 || cut == 23 || cut == 19 || cut == 20 || cut == 21 || cut == 26 || cut == 642 || cut == 641 || cut == 31 || cut == 28 || cut == 29 || cut == 644 || cut == 645 || cut == 640) && frame >= 120 || cut == 16 && frame >= 1200) walkRoute(map, cut);
            if(cut != 9 || frame % 30 == 0) {
              record(trace, map, frame, cut);
            }
            if(cut == 25 && !this.routeName.isEmpty() && frame >= 120) walkRoute(map, cut);
            if(cut == 27 && (this.routeName.equals("storage-door") || this.routeName.equals("lower-bridge")) && frame >= 120) walkRoute(map, cut);
            if(cut == 24 && (this.routeName.equals("side-cell") || this.routeName.equals("cell-exit")) && frame >= 120) walkRoute(map, cut);
            if(cut == 24 && this.postBattle && legend.game.Scus94491BpeSegment_800b.gameState_800babc8.scriptFlags2_bc.get(0x7) && map.sobjs_800c6880[0].innerStruct_00.ignoreCollision_172 == 0) walkRoute(map, cut);
            if(frame % 120 == 0 || cut == 91 && (frame == 180 || frame == 210) || cut == 86 && frame == 300 || cut == 51 && (frame == 180 || frame == 210) || cut == 24 && (frame == 150 || frame == 168 || frame == 176) || cut == 27 && (frame == 156 || frame == 160) || cut == 29 && frame == 168 || (cut == 28 || cut == 29 || cut == 644 || cut == 645 || cut == 640 || cut == 27) && frame % 30 == 0 || cut == 641 && frame % 60 == 0 || cut == 26 && (frame == 156 || frame == 168 || frame == 208) || (cut == 5 || cut == 15) && frame == 180) {
              final var walkingPlayer = map.sobjs_800c6880[0].innerStruct_00;
              Files.writeString(output.resolve("walking-state.txt"), "waypoint=" + this.walkWaypoint + ", player=" + walkingPlayer.model_00.coord2_14.coord.transfer + ", alert=" + walkingPlayer.showAlertIndicator_194 + ", scripted=" + walkingPlayer.ignoreCollision_172 + ", action=" + this.actionWaypoint + ", requested=" + this.liftRequested + ", moving=" + this.liftMoving + ", done=" + this.liftDone);
              capture(output.resolve(String.format("frame-%04d.png", frame)));
            }
            if(preview && frame == 600) {
              SCRIPTS.pause();
              System.out.println("SELES PREVIEW READY: close the game window when finished viewing.");
              finished.complete(null);
            }
            if("true".equals(System.getenv("DISC1_INSPECT")) && frame == 120) {
              Files.writeString(output.resolve("completion.txt"), "Inspection only; full gameplay verification pending for cut " + cut);
              finished.complete(null);
            }
            if(frame == (cut == 9 ? 2400 : cut == 10 ? 3600 : cut == 90 ? 1200 : 12000)) {
              Files.writeString(output.resolve("completion.txt"), "Still in cut " + cut + " after " + frame + " frames; inspect whether awaiting movement/input");
              finished.completeExceptionally(new AssertionError("Opening sequence did not finish"));
            }
          } catch(final Throwable e) {
            finished.completeExceptionally(e);
          }
        });
        Harness.transitionToEngineState(LodEngineStateTypes.SUBMAP.get());
      });
      finished.get(cut == 9 ? 120 : 240, TimeUnit.SECONDS);
      assertTrue(frames.get() > 0);
      if(preview) {
        engine.join();
      }
    } finally {
      Files.write(output.resolve("alignment.csv"), trace);
      if(original && Files.exists(disabled)) {
        Files.move(disabled, background);
      }
      if(engine != null && engine.isAlive()) {
        if(cut == 10) Input.sendKeyUp(InputKey.DOWN);
        if(cut == 12) {
          Input.sendKeyUp(InputKey.UP);
          Input.sendKeyUp(InputKey.RIGHT);
        }
        if(cut == 624 || cut == 5 || cut == 625 || cut == 6 || cut == 7) {
          Input.sendKeyUp(InputKey.LEFT);
          Input.sendKeyUp(InputKey.UP);
          if(cut == 5 || cut == 625 || cut == 6 || cut == 7) Input.sendKeyUp(InputKey.DOWN);
          if(cut == 625 || cut == 6 || cut == 7) Input.sendKeyUp(InputKey.RIGHT);
        }
        if(cut == 13) Input.sendKeyUp(InputKey.UP);
        for(final InputKey key : this.walkKeys) Input.sendKeyUp(key);
        onRender(() -> RENDERER.window().close());
        engine.join(5000);
      }
    }
  }

  static void record(final List<String> trace, final SMap map, final int frame, final int cut) throws Exception {
    final var foregrounds = (EnvironmentForegroundTextureMetrics[])field(map.submap, "envForegroundMetrics_800cb590");
    final var metrics = (EnvironmentRenderingMetrics24[])field(map.submap, "envRenderMetrics_800cb710");
    final var camera = field(map.submap, "screenOffset");
    final var player = map.sobjs_800c6880[0].innerStruct_00.model_00.coord2_14.coord.transfer;
    final int backgroundCount = (Integer)field(map.submap, "envBackgroundTextureCount_800cb57c");
    final int foregroundCount = (Integer)field(map.submap, "envForegroundTextureCount_800cb580");
    for(int i = 0; i < foregroundCount; i++) {
      final var layer = foregrounds[i];
      trace.add(frame + "," + cut + ",\"" + camera + "\",\"" + player + "\"," + i + ',' + layer.startX + ',' + layer.startY
        + ',' + layer.destX + ',' + layer.destY + ',' + layer.hidden_08 + ',' + metrics[i + backgroundCount].z_20 + ',' + metrics[i + backgroundCount].zFlags_22);
    }
  }

  private static Object field(final Object object, final String name) throws Exception {
    final var field = object.getClass().getDeclaredField(name);
    field.setAccessible(true);
    return field.get(object);
  }

  static void verifyTextures(final SMap map, final int cut, final boolean original) throws Exception {
    final var textures = new ArrayList<legend.core.renderer.Texture>();
    textures.add((legend.core.renderer.Texture)field(map.submap, "backgroundTexture"));
    textures.addAll(java.util.Arrays.asList((legend.core.renderer.Texture[])field(map.submap, "foregroundTextures")));
    final Path source = Path.of(cut == 9 ? "build/seles/original" : "build/disc1/original/cut-" + cut);
    for(int i = 0; i < textures.size(); i++) {
      final var texture = textures.get(i);
      final String name = i == 0 ? "background" : "foreground-" + (i - 1);
      final var expected = ImageIO.read(source.resolve(name + ".png").toFile());
      assertNotNull(expected, name + " must decode");
      if(texture == null) {
        assertTrue(original && i > 0, "Only unused original foreground slots may lack a GPU texture");
        assertEquals(textures.getFirst().width, expected.getWidth());
        assertEquals(textures.getFirst().height, expected.getHeight());
        for(int y = 0; y < expected.getHeight(); y++) {
          for(int x = 0; x < expected.getWidth(); x++) {
            assertEquals(0, expected.getRGB(x, y), "Unused slot must be fully empty RGBA: " + name);
          }
        }
        continue;
      }
      assertEquals(expected.getWidth() * (original ? 1 : 4), texture.width, name);
      assertEquals(expected.getHeight() * (original ? 1 : 4), texture.height, name);
      {
        final var pixelsExpected = original ? expected : ImageIO.read(Path.of(cut == 9
          ? "gfx/backgrounds/seles-overlook" : "gfx/backgrounds/disc1/cut-" + cut).resolve(name + ".png").toFile());
        assertNotNull(pixelsExpected, name + " installed PNG must decode");
        assertEquals(texture.width, pixelsExpected.getWidth());
        assertEquals(texture.height, pixelsExpected.getHeight());
        final var pixels = BufferUtils.createByteBuffer(texture.width * texture.height * 4);
        texture.use();
        glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
        final int[] wanted = pixelsExpected.getRGB(0, 0, texture.width, texture.height, null, 0, texture.width);
        for(int p = 0; p < wanted.length; p++) wanted[p] = Integer.rotateLeft(wanted[p], 8);
        final int[] actual = new int[wanted.length];
        pixels.order(java.nio.ByteOrder.BIG_ENDIAN).asIntBuffer().get(actual);
        assertArrayEquals(wanted, actual, name + " exact GPU RGBA pixels");
      }
    }
  }

  static void capture(final Path output) throws Exception {
    final int width = RENDERER.window().getWidth(), height = RENDERER.window().getHeight();
    final var pixels = BufferUtils.createByteBuffer(width * height * 4);
    glReadBuffer(GL_BACK);
    glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
    final var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for(int y = 0; y < height; y++) {
      for(int x = 0; x < width; x++) {
        final int p = (y * width + x) * 4;
        image.setRGB(x, height - 1 - y, (pixels.get(p) & 255) << 16 | (pixels.get(p + 1) & 255) << 8 | pixels.get(p + 2) & 255);
      }
    }
    ImageIO.write(image, "png", output.toFile());
  }

  private static void press() {
    Input.sendKeyPress(InputKey.RETURN);
    DebugHelper.sleep(250);
  }

  static void onRender(final Runnable task) throws Exception {
    final CompletableFuture<Void> done = new CompletableFuture<>();
    RENDERER.addTask(() -> {
      try { task.run(); done.complete(null); }
      catch(final Throwable e) { done.completeExceptionally(e); }
    });
    done.get(30, TimeUnit.SECONDS);
  }
}
