package legend.lodmod;

import legend.game.inventory.SpellStats0c;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;

import static legend.lodmod.LodMod.id;
import static legend.lodmod.LodSpells.ALBERT_GASPLESS;
import static legend.lodmod.LodSpells.ALBERT_WING_BLASTER;
import static legend.lodmod.LodSpells.ASTRAL_DRAIN;
import static legend.lodmod.LodSpells.ATOMIC_MIND;
import static legend.lodmod.LodSpells.BLOSSOM_STORM;
import static legend.lodmod.LodSpells.BLUE_SEA_DRAGON;
import static legend.lodmod.LodSpells.DARK_DRAGON;
import static legend.lodmod.LodSpells.DEATH_DIMENSION;
import static legend.lodmod.LodSpells.DEMONS_GATE;
import static legend.lodmod.LodSpells.DIAMOND_DUST;
import static legend.lodmod.LodSpells.EXPLOSION;
import static legend.lodmod.LodSpells.FINAL_BURST;
import static legend.lodmod.LodSpells.FLAMESHOT;
import static legend.lodmod.LodSpells.FREEZING_RING;
import static legend.lodmod.LodSpells.GASPLESS;
import static legend.lodmod.LodSpells.GATES_OF_HEAVEN;
import static legend.lodmod.LodSpells.GOLDEN_DRAGON;
import static legend.lodmod.LodSpells.GRAND_STREAM;
import static legend.lodmod.LodSpells.JADE_DRAGON;
import static legend.lodmod.LodSpells.METEOR_STRIKE;
import static legend.lodmod.LodSpells.MIRANDA_GATES_OF_HEAVEN;
import static legend.lodmod.LodSpells.MIRANDA_MOON_LIGHT;
import static legend.lodmod.LodSpells.MIRANDA_STAR_CHILDREN;
import static legend.lodmod.LodSpells.MOON_LIGHT;
import static legend.lodmod.LodSpells.RAINBOW_BREATH;
import static legend.lodmod.LodSpells.RED_EYED_DRAGON;
import static legend.lodmod.LodSpells.ROSE_STORM;
import static legend.lodmod.LodSpells.STAR_CHILDREN;
import static legend.lodmod.LodSpells.THUNDER_GOD;
import static legend.lodmod.LodSpells.THUNDER_KID;
import static legend.lodmod.LodSpells.VIOLET_DRAGON;
import static legend.lodmod.LodSpells.WHITE_SILVER_DRAGON;
import static legend.lodmod.LodSpells.WING_BLASTER;

public final class Legacy {
  private Legacy() { }

  public static final RegistryId[] CHAR_IDS = {
    id("dart"),
    id("lavitz"),
    id("shana"),
    id("rose"),
    id("haschel"),
    id("albert"),
    id("meru"),
    id("kongol"),
    id("miranda"),
  };

  public static int indexForCharId(@Nullable final RegistryId id) {
    if(id != null) {
      for(int i = 0; i < CHAR_IDS.length; i++) {
        if(CHAR_IDS[i].equals(id)) {
          return i;
        }
      }
    }

    return -1;
  }

  public static final int[][] RETAIL_HP = {
    {30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330, 413, 496, 579, 662, 745, 828, 911, 994, 1077, 1160, 1272, 1384, 1496, 1608, 1720, 1832, 1944, 2056, 2168, 2280, 2399, 2518, 2637, 2756, 2875, 2994, 3113, 3232, 3351, 3470, 3729, 3988, 4247, 4506, 4765, 5024, 5283, 5542, 5801, 6060, 6220, 6380, 6540, 6700, 6860, 7020, 7180, 7340, 7500},
    {35, 67, 100, 133, 166, 199, 231, 264, 297, 330, 363, 454, 545, 636, 728, 819, 910, 1002, 1093, 1184, 1276, 1399, 1522, 1645, 1768, 1892, 2015, 2138, 2261, 2384, 2508, 2638, 2769, 2900, 3031, 3162, 3293, 3424, 3555, 3686, 3817, 4101, 4386, 4671, 4956, 5241, 5526, 5811, 6096, 6381, 6666, 6842, 7018, 7194, 7370, 7546, 7722, 7898, 8074, 8250},
    {24, 48, 72, 96, 120, 144, 168, 192, 216, 240, 264, 330, 396, 463, 529, 596, 662, 728, 795, 861, 928, 1017, 1107, 1196, 1286, 1376, 1465, 1555, 1644, 1734, 1824, 1919, 2014, 2109, 2204, 2300, 2395, 2490, 2585, 2680, 2776, 2983, 3190, 3397, 3604, 3812, 4019, 4226, 4433, 4640, 4848, 4976, 5104, 5232, 5360, 5488, 5616, 5744, 5872, 6000},
    {21, 42, 63, 84, 105, 126, 147, 168, 189, 210, 231, 289, 347, 405, 463, 521, 579, 637, 695, 753, 812, 890, 968, 1047, 1125, 1204, 1282, 1360, 1439, 1517, 1596, 1679, 1762, 1845, 1929, 2012, 2095, 2179, 2262, 2345, 2429, 2610, 2791, 2972, 3154, 3335, 3516, 3698, 3879, 4060, 4242, 4354, 4466, 4578, 4690, 4802, 4914, 5026, 5138, 5250},
    {27, 54, 81, 108, 135, 162, 189, 216, 243, 270, 297, 371, 446, 521, 595, 670, 745, 819, 894, 969, 1044, 1144, 1245, 1346, 1447, 1548, 1648, 1749, 1850, 1951, 2052, 2159, 2266, 2373, 2480, 2587, 2694, 2801, 2908, 3015, 3123, 3356, 3589, 3822, 4055, 4288, 4521, 4754, 4987, 5220, 5454, 5598, 5742, 5886, 6030, 6174, 6318, 6462, 6606, 6750},
    {35, 67, 100, 133, 166, 199, 231, 264, 297, 330, 363, 454, 545, 636, 728, 819, 910, 1002, 1093, 1184, 1276, 1399, 1522, 1645, 1768, 1892, 2015, 2138, 2261, 2384, 2508, 2638, 2769, 2900, 3031, 3162, 3293, 3424, 3555, 3686, 3817, 4101, 4386, 4671, 4956, 5241, 5526, 5811, 6096, 6381, 6666, 6842, 7018, 7194, 7370, 7546, 7722, 7898, 8074, 8250},
    {18, 36, 54, 72, 90, 108, 126, 144, 162, 180, 198, 247, 297, 347, 397, 447, 496, 546, 596, 646, 696, 763, 830, 897, 964, 1032, 1099, 1166, 1233, 1300, 1368, 1439, 1510, 1582, 1653, 1725, 1796, 1867, 1939, 2010, 2082, 2237, 2392, 2548, 2703, 2859, 3014, 3169, 3325, 3480, 3636, 3732, 3828, 3924, 4020, 4116, 4212, 4308, 4404, 4500},
    {45, 83, 121, 160, 198, 237, 275, 313, 352, 390, 429, 536, 644, 752, 860, 968, 1076, 1184, 1292, 1400, 1508, 1653, 1799, 1944, 2090, 2236, 2381, 2527, 2672, 2818, 2964, 3118, 3273, 3428, 3582, 3737, 3892, 4046, 4201, 4356, 4511, 4847, 5184, 5521, 5857, 6194, 6531, 6867, 7204, 7541, 7878, 8086, 8294, 8502, 8710, 8918, 9126, 9334, 9542, 9750},
    {24, 48, 72, 96, 120, 144, 168, 192, 216, 240, 264, 330, 396, 463, 529, 596, 662, 728, 795, 861, 928, 1017, 1107, 1196, 1286, 1376, 1465, 1555, 1644, 1734, 1824, 1919, 2014, 2109, 2204, 2300, 2395, 2490, 2585, 2680, 2776, 2983, 3190, 3397, 3604, 3812, 4019, 4226, 4433, 4640, 4848, 4976, 5104, 5232, 5360, 5488, 5616, 5744, 5872, 6000},
  };

  @SuppressWarnings("unchecked")
  public static final RegistryDelegate<SpellStats0c>[][] CHARACTER_SPELLS = new RegistryDelegate[][] {
    {FLAMESHOT, EXPLOSION, FINAL_BURST, RED_EYED_DRAGON},
    {WING_BLASTER, BLOSSOM_STORM, GASPLESS, JADE_DRAGON},
    {MOON_LIGHT, STAR_CHILDREN, GATES_OF_HEAVEN, WHITE_SILVER_DRAGON},
    {ASTRAL_DRAIN, DEATH_DIMENSION, DEMONS_GATE, DARK_DRAGON},
    {ATOMIC_MIND, THUNDER_KID, THUNDER_GOD, VIOLET_DRAGON},
    {ALBERT_WING_BLASTER, ROSE_STORM, ALBERT_GASPLESS, JADE_DRAGON},
    {FREEZING_RING, RAINBOW_BREATH, DIAMOND_DUST, BLUE_SEA_DRAGON},
    {GRAND_STREAM, METEOR_STRIKE, GOLDEN_DRAGON},
    {MIRANDA_MOON_LIGHT, MIRANDA_STAR_CHILDREN, MIRANDA_GATES_OF_HEAVEN, WHITE_SILVER_DRAGON},
  };
}
