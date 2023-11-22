package legend.game.wmap;

import legend.core.opengl.MeshObj;
import org.joml.Vector3f;

import java.util.Arrays;

public class MapState100 {
  /**
   * <ol start="0">
   *   <li>South Serdio</li>
   *   <li>North Serdio</li>
   *   <li>Tiberoa</li>
   *   <li>Illisa Bay</li>
   *   <li>Mille Seseau</li>
   *   <li>Gloriano</li>
   *   <li>Death Frontier</li>
   *   <li>Endiness</li>
   * </ol>
   * 800c6798
   */
  public int continentIndex_00;
  // /** 800c679c */
  // public int _04; // was just a copy of continentIndex_00
  /** 800c67a0 */
  public int locationCount_08;
  /** The number of paths on the continent * 2 (one for positive and negative directions) (800c67a4) */
  public int directionalPathCount_0c;
  /** 800c67a8 */
  public int locationIndex_10;
  /** The section of the path that the player is on, plus the direction (800c67aa) */
  public int directionalPathIndex_12;
  /** The section of the path that the player is on (800c67ac) */
  public int pathIndex_14;
  /** The path dot the player is on (800c67ae) */
  public int dotIndex_16;
  /** The distance the player is from the dot (range: 0-3) (800c67b0) */
  public float dotOffset_18;

  public final MeshObj[][] pathBigDotObjs = new MeshObj[3][3];
  public MeshObj pathSmallDotObj;

  /** +1 - left, -1 - right (800c67b4) */
  public int facing_1c;
  /** Previous as in travel index ("plot") order, not based on direction of travel. (800c67b8) */
  public final Vector3f prevDotPos_20 = new Vector3f();
  /** Next as in travel index ("plot") order, not based on direction of travel. (800c67c8) */
  public final Vector3f nextDotPos_30 = new Vector3f();
  /**
   * 800c67d8
   * Array of temp positions of the second or second to last small dot of a path, depending on direction of approch of segment
   */
  public final Vector3f[] tempPathSegmentStartOffsets_40 = new Vector3f[7];
  /**
   * 800c6848
   * Seems to be the dot on a path segment that the player is entering that is used
   * to orient and move the player in case of discrepancy in direction.
   */
  public final Vector3f dotPositionForPlayerOrientationCorrectionAtIntersection = new Vector3f();
  /** 800c6858 */
  public float playerDestAngle_c0;
  /** 800c685a */
  public float previousPlayerRotation_c2;
  /** 800c685c */
  public int submapCut_c4;
  /** 800c685e */
  public int submapScene_c6;
  /** 800c6860 */
  public short submapCut_c8;
  /** 800c6862 */
  public short submapScene_ca;

  /** 800c6868 */
  public boolean disableInput_d0;
  /** 800c686c */
  public int _d4;
  /** 800c6870 */
  public int _d8;
  /**
   * 800c6874
   * Array of temp indices of paths branching off a location point
   */
  public final int[] tempPathSegmentIndices_dc = new int[7];
  /**
   * 800c6890
   * What path segment Dart is moving into. Segment order is absolute based on order of paths,
   * not Dart's travel direction.
   * <ol start="0">
   *   <li>Current</li>
   *   <li>Previous</li>
   *   <li>Next</li>
   * </ol>
   */
  public int pathSegmentPlayerMovingInto_f8;
  /**
   * 800c6894
   * Describes the type of path segment endpoint that Dart is at.
   * <ol start="0">
   *   <li>Not at endpoint</li>
   *   <li>Terminal endpoint</li>
   *   <li>Intersection</li>
   * </ol>
   */
  public int pathSegmentEndpointTypeCrossed_fc;

  public MapState100() {
    Arrays.setAll(this.tempPathSegmentStartOffsets_40, i -> new Vector3f());
  }
}
