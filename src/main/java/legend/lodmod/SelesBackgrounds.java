package legend.lodmod;

import legend.core.renderer.Texture;
import legend.game.modding.events.submap.SubmapEnvironmentTextureEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Disc 1 artwork packs; original assets remain the fallback. */
final class SelesBackgrounds {
  private static final Logger LOGGER = LogManager.getLogger(SelesBackgrounds.class);
  private static final Path DIRECTORY = Path.of("gfx", "backgrounds", "seles-overlook");

  private SelesBackgrounds() { }

  static void replace(final SubmapEnvironmentTextureEvent event) {
    final int width, height, layers;
    final Path directory;
    switch(event.submapCut) {
      case 5 -> { directory = Path.of("gfx/backgrounds/disc1/cut-5"); width = 1536; height = 1024; layers = 3; }
      case 6 -> { directory = Path.of("gfx/backgrounds/disc1/cut-6"); width = 1536; height = 2048; layers = 2; }
      case 7 -> { directory = Path.of("gfx/backgrounds/disc1/cut-7"); width = 1536; height = 1536; layers = 6; }
      case 9 -> { directory = DIRECTORY; width = 2048; height = 1424; layers = 4; }
      case 10 -> { directory = Path.of("gfx/backgrounds/disc1/cut-10"); width = 1536; height = 2048; layers = 5; }
      case 12 -> { directory = Path.of("gfx/backgrounds/disc1/cut-12"); width = 2560; height = 1536; layers = 14; }
      case 13 -> { directory = Path.of("gfx/backgrounds/disc1/cut-13"); width = 1536; height = 2048; layers = 4; }
      case 14 -> { directory = Path.of("gfx/backgrounds/disc1/cut-14"); width = 1536; height = 1024; layers = 3; }
      case 15 -> { directory = Path.of("gfx/backgrounds/disc1/cut-15"); width = 2560; height = 1024; layers = 13; }
      case 16 -> { directory = Path.of("gfx/backgrounds/disc1/cut-16"); width = 2048; height = 1536; layers = 9; }
      case 17 -> { directory = Path.of("gfx/backgrounds/disc1/cut-17"); width = 2560; height = 1024; layers = 10; }
      case 19 -> { directory = Path.of("gfx/backgrounds/disc1/cut-19"); width = 1536; height = 2048; layers = 13; }
      case 20 -> { directory = Path.of("gfx/backgrounds/disc1/cut-20"); width = 1536; height = 2048; layers = 16; }
      case 21 -> { directory = Path.of("gfx/backgrounds/disc1/cut-21"); width = 2560; height = 1536; layers = 21; }
      case 22 -> { directory = Path.of("gfx/backgrounds/disc1/cut-22"); width = 1536; height = 1024; layers = 17; }
      case 23 -> { directory = Path.of("gfx/backgrounds/disc1/cut-23"); width = 1536; height = 2048; layers = 11; }
      case 24 -> { directory = Path.of("gfx/backgrounds/disc1/cut-24"); width = 1536; height = 1024; layers = 15; }
      case 25 -> { directory = Path.of("gfx/backgrounds/disc1/cut-25"); width = 1536; height = 1024; layers = 13; }
      case 26 -> { directory = Path.of("gfx/backgrounds/disc1/cut-26"); width = 1536; height = 1024; layers = 16; }
      case 27 -> { directory = Path.of("gfx/backgrounds/disc1/cut-27"); width = 1536; height = 1024; layers = 16; }
      case 28 -> { directory = Path.of("gfx/backgrounds/disc1/cut-28"); width = 1536; height = 1024; layers = 13; }
      case 29 -> { directory = Path.of("gfx/backgrounds/disc1/cut-29"); width = 1536; height = 1024; layers = 16; }
      case 31 -> { directory = Path.of("gfx/backgrounds/disc1/cut-31"); width = 2048; height = 1536; layers = 17; }
      case 37 -> { directory = Path.of("gfx/backgrounds/disc1/cut-37"); width = 2560; height = 1024; layers = 13; }
      case 38 -> { directory = Path.of("gfx/backgrounds/disc1/cut-38"); width = 2560; height = 1536; layers = 7; }
      case 39 -> { directory = Path.of("gfx/backgrounds/disc1/cut-39"); width = 2560; height = 1536; layers = 11; }
      case 40 -> { directory = Path.of("gfx/backgrounds/disc1/cut-40"); width = 1536; height = 1024; layers = 8; }
      case 41 -> { directory = Path.of("gfx/backgrounds/disc1/cut-41"); width = 1536; height = 1024; layers = 9; }
      case 42 -> { directory = Path.of("gfx/backgrounds/disc1/cut-42"); width = 1536; height = 1024; layers = 1; }
      case 43 -> { directory = Path.of("gfx/backgrounds/disc1/cut-43"); width = 1536; height = 1024; layers = 4; }
      case 44 -> { directory = Path.of("gfx/backgrounds/disc1/cut-44"); width = 2560; height = 1024; layers = 7; }
      case 45 -> { directory = Path.of("gfx/backgrounds/disc1/cut-45"); width = 1536; height = 2048; layers = 5; }
      case 46 -> { directory = Path.of("gfx/backgrounds/disc1/cut-46"); width = 1536; height = 2048; layers = 17; }
      case 47 -> { directory = Path.of("gfx/backgrounds/disc1/cut-47"); width = 1536; height = 2048; layers = 2; }
      case 48 -> { directory = Path.of("gfx/backgrounds/disc1/cut-48"); width = 2560; height = 1536; layers = 2; }
      case 49 -> { directory = Path.of("gfx/backgrounds/disc1/cut-49"); width = 1536; height = 2048; layers = 5; }
      case 50 -> { directory = Path.of("gfx/backgrounds/disc1/cut-50"); width = 1536; height = 1024; layers = 5; }
      case 51 -> { directory = Path.of("gfx/backgrounds/disc1/cut-51"); width = 1536; height = 1024; layers = 1; }
      case 52 -> { directory = Path.of("gfx/backgrounds/disc1/cut-52"); width = 2048; height = 1028; layers = 10; }
      case 53 -> { directory = Path.of("gfx/backgrounds/disc1/cut-53"); width = 1536; height = 1024; layers = 7; }
      case 54 -> { directory = Path.of("gfx/backgrounds/disc1/cut-54"); width = 1536; height = 2048; layers = 7; }
      case 66 -> { directory = Path.of("gfx/backgrounds/disc1/cut-66"); width = 2048; height = 1536; layers = 17; }
      case 69 -> { directory = Path.of("gfx/backgrounds/disc1/cut-69"); width = 2560; height = 1536; layers = 16; }
      case 86 -> { directory = Path.of("gfx/backgrounds/disc1/cut-86"); width = 1536; height = 1536; layers = 12; }
      case 91 -> { directory = Path.of("gfx/backgrounds/disc1/cut-91"); width = 2560; height = 1536; layers = 20; }
      case 92 -> { directory = Path.of("gfx/backgrounds/disc1/cut-92"); width = 2560; height = 1536; layers = 19; }
      case 90 -> { directory = Path.of("gfx/backgrounds/disc1/cut-90"); width = 2560; height = 1536; layers = 19; }
      case 56 -> { directory = Path.of("gfx/backgrounds/disc1/cut-56"); width = 1536; height = 1024; layers = 3; }
      case 624 -> { directory = Path.of("gfx/backgrounds/disc1/cut-624"); width = 2048; height = 1024; layers = 5; }
      case 625 -> { directory = Path.of("gfx/backgrounds/disc1/cut-625"); width = 2048; height = 1536; layers = 10; }
      case 640 -> { directory = Path.of("gfx/backgrounds/disc1/cut-640"); width = 1536; height = 1024; layers = 2; }
      case 641 -> { directory = Path.of("gfx/backgrounds/disc1/cut-641"); width = 1536; height = 1024; layers = 8; }
      case 642 -> { directory = Path.of("gfx/backgrounds/disc1/cut-642"); width = 1540; height = 1024; layers = 5; }
      case 643 -> { directory = Path.of("gfx/backgrounds/disc1/cut-643"); width = 1536; height = 1024; layers = 2; }
      case 644 -> { directory = Path.of("gfx/backgrounds/disc1/cut-644"); width = 1536; height = 1024; layers = 3; }
      case 645 -> { directory = Path.of("gfx/backgrounds/disc1/cut-645"); width = 1536; height = 1024; layers = 2; }
      case 675 -> { directory = Path.of("gfx/backgrounds/disc1/cut-675"); width = 2048; height = 1024; layers = 7; }
      case 676 -> { directory = Path.of("gfx/backgrounds/disc1/cut-676"); width = 1536; height = 1536; layers = 3; }
      case 677 -> { directory = Path.of("gfx/backgrounds/disc1/cut-677"); width = 2048; height = 1024; layers = 3; }
      case 694 -> { directory = Path.of("gfx/backgrounds/disc1/cut-694"); width = 1536; height = 1024; layers = 2; }
      case 695 -> { directory = Path.of("gfx/backgrounds/disc1/cut-695"); width = 1536; height = 1024; layers = 17; }
      case 703 -> { directory = Path.of("gfx/backgrounds/disc1/cut-703"); width = 3072; height = 1024; layers = 5; }
      case 704 -> { directory = Path.of("gfx/backgrounds/disc1/cut-704"); width = 3072; height = 1024; layers = 8; }
      case 725 -> { directory = Path.of("gfx/backgrounds/disc1/cut-725"); width = 2048; height = 1028; layers = 9; }
      case 742 -> { directory = Path.of("gfx/backgrounds/disc1/cut-742"); width = 1536; height = 2048; layers = 4; }
      case 78 -> { directory = Path.of("gfx/backgrounds/disc1/cut-78"); width = 2048; height = 1536; layers = 15; }
      case 85 -> { directory = Path.of("gfx/backgrounds/disc1/cut-85"); width = 2048; height = 1024; layers = 10; }
      case 76 -> { directory = Path.of("gfx/backgrounds/disc1/cut-76"); width = 1536; height = 1024; layers = 4; }
      case 75 -> { directory = Path.of("gfx/backgrounds/disc1/cut-75"); width = 1536; height = 1024; layers = 5; }
      case 83 -> { directory = Path.of("gfx/backgrounds/disc1/cut-83"); width = 2048; height = 1024; layers = 4; }
      case 623 -> { directory = Path.of("gfx/backgrounds/disc1/cut-623"); width = 2560; height = 1536; layers = 1; }
      case 72 -> { directory = Path.of("gfx/backgrounds/disc1/cut-72"); width = 1536; height = 1024; layers = 4; }
      case 77 -> { directory = Path.of("gfx/backgrounds/disc1/cut-77"); width = 2560; height = 1536; layers = 6; }
      case 82 -> { directory = Path.of("gfx/backgrounds/disc1/cut-82"); width = 2560; height = 2048; layers = 10; }
      case 81 -> { directory = Path.of("gfx/backgrounds/disc1/cut-81"); width = 2048; height = 1536; layers = 7; }
      case 73 -> { directory = Path.of("gfx/backgrounds/disc1/cut-73"); width = 2048; height = 1024; layers = 1; }
      case 74 -> { directory = Path.of("gfx/backgrounds/disc1/cut-74"); width = 2048; height = 1024; layers = 15; }
      case 84 -> { directory = Path.of("gfx/backgrounds/disc1/cut-84"); width = 2048; height = 1028; layers = 7; }
      case 65 -> { directory = Path.of("gfx/backgrounds/disc1/cut-65"); width = 1536; height = 1024; layers = 2; }
      case 98 -> { directory = Path.of("gfx/backgrounds/disc1/cut-98"); width = 1536; height = 1024; layers = 1; }
      case 101 -> { directory = Path.of("gfx/backgrounds/disc1/cut-101"); width = 1536; height = 1024; layers = 1; }
      case 105 -> { directory = Path.of("gfx/backgrounds/disc1/cut-105"); width = 1536; height = 1024; layers = 7; }
      case 112 -> { directory = Path.of("gfx/backgrounds/disc1/cut-112"); width = 2560; height = 1536; layers = 21; }
      case 113 -> { directory = Path.of("gfx/backgrounds/disc1/cut-113"); width = 2560; height = 1536; layers = 21; }
      case 633 -> { directory = Path.of("gfx/backgrounds/disc1/cut-633"); width = 1536; height = 1024; layers = 1; }
      case 726 -> { directory = Path.of("gfx/backgrounds/disc1/cut-726"); width = 1536; height = 1024; layers = 2; }
      case 106 -> { directory = Path.of("gfx/backgrounds/disc1/cut-106"); width = 3072; height = 1024; layers = 11; }
      case 107 -> { directory = Path.of("gfx/backgrounds/disc1/cut-107"); width = 1536; height = 2048; layers = 9; }
      case 108 -> { directory = Path.of("gfx/backgrounds/disc1/cut-108"); width = 1536; height = 1540; layers = 8; }
      case 110 -> { directory = Path.of("gfx/backgrounds/disc1/cut-110"); width = 1536; height = 2048; layers = 9; }
      case 114 -> { directory = Path.of("gfx/backgrounds/disc1/cut-114"); width = 3072; height = 1024; layers = 4; }
      case 115 -> { directory = Path.of("gfx/backgrounds/disc1/cut-115"); width = 2056; height = 1024; layers = 11; }
      case 116 -> { directory = Path.of("gfx/backgrounds/disc1/cut-116"); width = 1536; height = 2048; layers = 1; }
      case 117 -> { directory = Path.of("gfx/backgrounds/disc1/cut-117"); width = 1540; height = 1540; layers = 11; }
      case 118 -> { directory = Path.of("gfx/backgrounds/disc1/cut-118"); width = 2048; height = 1536; layers = 6; }
      case 119 -> { directory = Path.of("gfx/backgrounds/disc1/cut-119"); width = 2560; height = 1536; layers = 17; }
      case 122 -> { directory = Path.of("gfx/backgrounds/disc1/cut-122"); width = 1540; height = 1024; layers = 2; }
      case 123 -> { directory = Path.of("gfx/backgrounds/disc1/cut-123"); width = 1540; height = 1028; layers = 6; }
      case 130 -> { directory = Path.of("gfx/backgrounds/disc1/cut-130"); width = 1536; height = 1024; layers = 3; }
      case 131 -> { directory = Path.of("gfx/backgrounds/disc1/cut-131"); width = 2560; height = 1536; layers = 9; }
      case 132 -> { directory = Path.of("gfx/backgrounds/disc1/cut-132"); width = 1536; height = 1024; layers = 10; }
      case 135 -> { directory = Path.of("gfx/backgrounds/disc1/cut-135"); width = 1536; height = 1536; layers = 7; }
      case 136 -> { directory = Path.of("gfx/backgrounds/disc1/cut-136"); width = 1536; height = 1536; layers = 5; }
      case 656 -> { directory = Path.of("gfx/backgrounds/disc1/cut-656"); width = 1536; height = 1024; layers = 1; }
      case 716 -> { directory = Path.of("gfx/backgrounds/disc1/cut-716"); width = 1536; height = 1024; layers = 1; }
      case 140 -> { directory = Path.of("gfx/backgrounds/disc1/cut-140"); width = 2048; height = 1028; layers = 7; }
      case 646 -> { directory = Path.of("gfx/backgrounds/disc1/cut-646"); width = 2052; height = 1028; layers = 8; }
      case 638 -> { directory = Path.of("gfx/backgrounds/disc1/cut-638"); width = 2048; height = 1536; layers = 4; }
      case 634 -> { directory = Path.of("gfx/backgrounds/disc1/cut-634"); width = 1536; height = 1024; layers = 3; }
      case 639 -> { directory = Path.of("gfx/backgrounds/disc1/cut-639"); width = 3072; height = 1536; layers = 10; }
      case 152 -> { directory = Path.of("gfx/backgrounds/disc1/cut-152"); width = 1536; height = 1024; layers = 4; }
      case 153 -> { directory = Path.of("gfx/backgrounds/disc1/cut-153"); width = 2048; height = 1024; layers = 5; }
      case 156 -> { directory = Path.of("gfx/backgrounds/disc1/cut-156"); width = 3072; height = 1024; layers = 11; }
      case 157 -> { directory = Path.of("gfx/backgrounds/disc1/cut-157"); width = 1536; height = 1536; layers = 16; }
      case 160 -> { directory = Path.of("gfx/backgrounds/disc1/cut-160"); width = 1536; height = 1024; layers = 7; }
      case 161 -> { directory = Path.of("gfx/backgrounds/disc1/cut-161"); width = 1536; height = 1024; layers = 1; }
      case 162 -> { directory = Path.of("gfx/backgrounds/disc1/cut-162"); width = 1536; height = 1028; layers = 2; }
      case 163 -> { directory = Path.of("gfx/backgrounds/disc1/cut-163"); width = 1536; height = 1536; layers = 15; }
      case 164 -> { directory = Path.of("gfx/backgrounds/disc1/cut-164"); width = 2048; height = 2048; layers = 8; }
      case 8 -> { directory = Path.of("gfx/backgrounds/disc1/cut-8"); width = 2560; height = 2048; layers = 5; }
      case 11 -> { directory = Path.of("gfx/backgrounds/disc1/cut-11"); width = 1536; height = 1536; layers = 6; }
      case 740 -> { directory = Path.of("gfx/backgrounds/disc1/cut-740"); width = 1540; height = 1024; layers = 2; }
      case 741 -> { directory = Path.of("gfx/backgrounds/disc1/cut-741"); width = 2048; height = 1024; layers = 1; }
      case 697 -> { directory = Path.of("gfx/backgrounds/disc1/cut-697"); width = 3072; height = 2176; layers = 2; }
      case 18 -> { directory = Path.of("gfx/backgrounds/disc1/cut-18"); width = 2048; height = 2048; layers = 20; }
      case 33 -> { directory = Path.of("gfx/backgrounds/disc1/cut-33"); width = 1536; height = 2048; layers = 10; }
      case 34 -> { directory = Path.of("gfx/backgrounds/disc1/cut-34"); width = 1536; height = 1024; layers = 6; }
      case 35 -> { directory = Path.of("gfx/backgrounds/disc1/cut-35"); width = 1536; height = 1024; layers = 1; }
      case 36 -> { directory = Path.of("gfx/backgrounds/disc1/cut-36"); width = 2048; height = 1024; layers = 1; }
      case 173 -> { directory = Path.of("gfx/backgrounds/disc1/cut-173"); width = 1536; height = 2048; layers = 11; }
      case 174 -> { directory = Path.of("gfx/backgrounds/disc1/cut-174"); width = 2048; height = 1024; layers = 11; }
      case 178 -> { directory = Path.of("gfx/backgrounds/disc1/cut-178"); width = 1536; height = 2048; layers = 15; }
      case 179 -> { directory = Path.of("gfx/backgrounds/disc1/cut-179"); width = 2560; height = 1536; layers = 11; }
      case 182 -> { directory = Path.of("gfx/backgrounds/disc1/cut-182"); width = 3072; height = 1536; layers = 16; }
      case 183 -> { directory = Path.of("gfx/backgrounds/disc1/cut-183"); width = 1536; height = 1024; layers = 2; }
      case 187 -> { directory = Path.of("gfx/backgrounds/disc1/cut-187"); width = 3072; height = 1024; layers = 9; }
      case 188 -> { directory = Path.of("gfx/backgrounds/disc1/cut-188"); width = 1536; height = 1536; layers = 9; }
      case 743 -> { directory = Path.of("gfx/backgrounds/disc1/cut-743"); width = 1536; height = 1536; layers = 9; }
      case 189 -> { directory = Path.of("gfx/backgrounds/disc1/cut-189"); width = 1536; height = 2048; layers = 11; }
      case 190 -> { directory = Path.of("gfx/backgrounds/disc1/cut-190"); width = 2048; height = 1536; layers = 9; }
      case 191 -> { directory = Path.of("gfx/backgrounds/disc1/cut-191"); width = 2048; height = 1024; layers = 7; }
      case 196 -> { directory = Path.of("gfx/backgrounds/disc1/cut-196"); width = 1536; height = 1536; layers = 12; }
      case 199 -> { directory = Path.of("gfx/backgrounds/disc1/cut-199"); width = 1536; height = 1536; layers = 8; }
      case 200 -> { directory = Path.of("gfx/backgrounds/disc1/cut-200"); width = 1536; height = 1536; layers = 1; }
      case 696 -> { directory = Path.of("gfx/backgrounds/disc1/cut-696"); width = 2560; height = 1536; layers = 6; }
      case 70 -> { directory = Path.of("gfx/backgrounds/disc1/cut-70"); width = 2048; height = 1024; layers = 11; }
      case 80 -> { directory = Path.of("gfx/backgrounds/disc1/cut-80"); width = 1540; height = 1024; layers = 7; }
      case 631 -> { directory = Path.of("gfx/backgrounds/disc1/cut-631"); width = 2560; height = 1536; layers = 7; }
      case 67 -> { directory = Path.of("gfx/backgrounds/disc1/cut-67"); width = 2048; height = 1024; layers = 9; }
      case 89 -> { directory = Path.of("gfx/backgrounds/disc1/cut-89"); width = 1536; height = 2048; layers = 20; }
      case 88 -> { directory = Path.of("gfx/backgrounds/disc1/cut-88"); width = 2048; height = 1024; layers = 16; }
      case 628 -> { directory = Path.of("gfx/backgrounds/disc1/cut-628"); width = 1536; height = 1024; layers = 8; }
      case 93 -> { directory = Path.of("gfx/backgrounds/disc1/cut-93"); width = 1536; height = 1536; layers = 11; }
      case 94 -> { directory = Path.of("gfx/backgrounds/disc1/cut-94"); width = 1536; height = 1208; layers = 4; }
      case 71 -> { directory = Path.of("gfx/backgrounds/disc1/cut-71"); width = 2560; height = 1536; layers = 22; }
      case 58 -> { directory = Path.of("gfx/backgrounds/disc1/cut-58"); width = 2048; height = 1536; layers = 17; }
      case 95 -> { directory = Path.of("gfx/backgrounds/disc1/cut-95"); width = 2560; height = 1536; layers = 21; }
      case 99 -> { directory = Path.of("gfx/backgrounds/disc1/cut-99"); width = 1536; height = 1024; layers = 9; }
      case 100 -> { directory = Path.of("gfx/backgrounds/disc1/cut-100"); width = 1536; height = 1024; layers = 9; }
      case 102 -> { directory = Path.of("gfx/backgrounds/disc1/cut-102"); width = 2048; height = 1024; layers = 14; }
      case 103 -> { directory = Path.of("gfx/backgrounds/disc1/cut-103"); width = 2048; height = 1024; layers = 16; }
      case 104 -> { directory = Path.of("gfx/backgrounds/disc1/cut-104"); width = 1536; height = 1024; layers = 5; }
      case 96 -> { directory = Path.of("gfx/backgrounds/disc1/cut-96"); width = 2560; height = 1536; layers = 21; }
      case 97 -> { directory = Path.of("gfx/backgrounds/disc1/cut-97"); width = 2560; height = 1536; layers = 13; }
      case 626 -> { directory = Path.of("gfx/backgrounds/disc1/cut-626"); width = 2048; height = 1024; layers = 15; }
      case 627 -> { directory = Path.of("gfx/backgrounds/disc1/cut-627"); width = 2048; height = 1024; layers = 16; }
      case 632 -> { directory = Path.of("gfx/backgrounds/disc1/cut-632"); width = 1536; height = 1024; layers = 9; }
      case 674 -> { directory = Path.of("gfx/backgrounds/disc1/cut-674"); width = 2048; height = 1024; layers = 6; }
      case 109 -> { directory = Path.of("gfx/backgrounds/disc1/cut-109"); width = 2048; height = 1536; layers = 20; }
      case 111 -> { directory = Path.of("gfx/backgrounds/disc1/cut-111"); width = 1536; height = 1540; layers = 3; }
      case 120 -> { directory = Path.of("gfx/backgrounds/disc1/cut-120"); width = 1540; height = 1028; layers = 9; }
      case 121 -> { directory = Path.of("gfx/backgrounds/disc1/cut-121"); width = 1540; height = 1536; layers = 9; }
      case 134 -> { directory = Path.of("gfx/backgrounds/disc1/cut-134"); width = 1536; height = 1536; layers = 11; }
      case 141 -> { directory = Path.of("gfx/backgrounds/disc1/cut-141"); width = 2048; height = 1536; layers = 8; }
      case 142 -> { directory = Path.of("gfx/backgrounds/disc1/cut-142"); width = 2560; height = 1024; layers = 16; }
      case 143 -> { directory = Path.of("gfx/backgrounds/disc1/cut-143"); width = 1536; height = 1028; layers = 20; }
      case 144 -> { directory = Path.of("gfx/backgrounds/disc1/cut-144"); width = 3072; height = 1024; layers = 15; }
      case 145 -> { directory = Path.of("gfx/backgrounds/disc1/cut-145"); width = 3072; height = 1024; layers = 22; }
      case 146 -> { directory = Path.of("gfx/backgrounds/disc1/cut-146"); width = 2048; height = 1024; layers = 12; }
      case 147 -> { directory = Path.of("gfx/backgrounds/disc1/cut-147"); width = 1536; height = 2048; layers = 4; }
      case 148 -> { directory = Path.of("gfx/backgrounds/disc1/cut-148"); width = 1536; height = 1536; layers = 16; }
      case 149 -> { directory = Path.of("gfx/backgrounds/disc1/cut-149"); width = 2564; height = 1028; layers = 12; }
      case 150 -> { directory = Path.of("gfx/backgrounds/disc1/cut-150"); width = 3072; height = 1024; layers = 13; }
      case 151 -> { directory = Path.of("gfx/backgrounds/disc1/cut-151"); width = 2048; height = 2048; layers = 21; }
      case 637 -> { directory = Path.of("gfx/backgrounds/disc1/cut-637"); width = 1536; height = 1024; layers = 9; }
      case 636 -> { directory = Path.of("gfx/backgrounds/disc1/cut-636"); width = 1536; height = 1024; layers = 17; }
      case 647 -> { directory = Path.of("gfx/backgrounds/disc1/cut-647"); width = 1536; height = 1024; layers = 1; }
      case 155 -> { directory = Path.of("gfx/backgrounds/disc1/cut-155"); width = 2048; height = 2048; layers = 19; }
      case 158 -> { directory = Path.of("gfx/backgrounds/disc1/cut-158"); width = 2052; height = 1544; layers = 18; }
      case 159 -> { directory = Path.of("gfx/backgrounds/disc1/cut-159"); width = 2048; height = 1536; layers = 8; }
      case 165 -> { directory = Path.of("gfx/backgrounds/disc1/cut-165"); width = 1536; height = 1032; layers = 13; }
      case 166 -> { directory = Path.of("gfx/backgrounds/disc1/cut-166"); width = 3072; height = 1024; layers = 11; }
      case 32 -> { directory = Path.of("gfx/backgrounds/disc1/cut-32"); width = 1536; height = 2048; layers = 10; }
      case 30 -> { directory = Path.of("gfx/backgrounds/disc1/cut-30"); width = 1536; height = 1024; layers = 6; }
      case 172 -> { directory = Path.of("gfx/backgrounds/disc1/cut-172"); width = 1536; height = 2048; layers = 8; }
      case 175 -> { directory = Path.of("gfx/backgrounds/disc1/cut-175"); width = 2048; height = 1536; layers = 18; }
      case 176 -> { directory = Path.of("gfx/backgrounds/disc1/cut-176"); width = 2048; height = 1536; layers = 13; }
      case 177 -> { directory = Path.of("gfx/backgrounds/disc1/cut-177"); width = 2560; height = 1536; layers = 5; }
      case 180 -> { directory = Path.of("gfx/backgrounds/disc1/cut-180"); width = 2048; height = 1540; layers = 20; }
      case 181 -> { directory = Path.of("gfx/backgrounds/disc1/cut-181"); width = 2048; height = 1536; layers = 16; }
      case 192 -> { directory = Path.of("gfx/backgrounds/disc1/cut-192"); width = 1536; height = 2048; layers = 19; }
      case 193 -> { directory = Path.of("gfx/backgrounds/disc1/cut-193"); width = 1536; height = 2048; layers = 21; }
      case 194 -> { directory = Path.of("gfx/backgrounds/disc1/cut-194"); width = 1544; height = 2048; layers = 20; }
      case 195 -> { directory = Path.of("gfx/backgrounds/disc1/cut-195"); width = 2048; height = 1536; layers = 12; }
      case 197 -> { directory = Path.of("gfx/backgrounds/disc1/cut-197"); width = 2048; height = 1024; layers = 17; }
      case 198 -> { directory = Path.of("gfx/backgrounds/disc1/cut-198"); width = 2048; height = 1536; layers = 1; }
      case 658 -> { directory = Path.of("gfx/backgrounds/disc1/cut-658"); width = 1536; height = 1536; layers = 12; }
      case 659 -> { directory = Path.of("gfx/backgrounds/disc1/cut-659"); width = 2048; height = 1024; layers = 6; }
      case 660 -> { directory = Path.of("gfx/backgrounds/disc1/cut-660"); width = 2560; height = 1536; layers = 6; }
      case 68 -> { directory = Path.of("gfx/backgrounds/disc1/cut-68"); width = 2048; height = 1540; layers = 18; }
      case 79 -> { directory = Path.of("gfx/backgrounds/disc1/cut-79"); width = 2048; height = 1536; layers = 12; }
      case 87 -> { directory = Path.of("gfx/backgrounds/disc1/cut-87"); width = 1536; height = 2048; layers = 12; }
      case 657 -> { directory = Path.of("gfx/backgrounds/disc1/cut-657"); width = 1536; height = 1024; layers = 6; }
      case 57 -> { directory = Path.of("gfx/backgrounds/disc1/cut-57"); width = 2048; height = 1028; layers = 9; }
      case 133 -> { directory = Path.of("gfx/backgrounds/disc1/cut-133"); width = 1536; height = 2048; layers = 9; }
      case 154 -> { directory = Path.of("gfx/backgrounds/disc1/cut-154"); width = 1536; height = 1032; layers = 16; }
      case 171 -> { directory = Path.of("gfx/backgrounds/disc1/cut-171"); width = 1544; height = 2048; layers = 16; }
      default -> { return; }
    }
    if(event.disk != 1 || event.foregrounds.length != layers || event.background != null) {
      return;
    }

    for(final Texture foreground : event.foregrounds) {
      if(foreground != null) {
        return;
      }
    }

    final String[] names = names(layers);
    final Texture[] textures = new Texture[names.length];
    try {
      if(!validPack(directory, width, height, layers)) {
        return;
      }
      for(int i = 0; i < names.length; i++) {
        textures[i] = Texture.png("Disc 1 cut " + event.submapCut + ' ' + names[i], directory.resolve(names[i]));
      }
    } catch(final IOException | RuntimeException e) {
      for(final Texture texture : textures) {
        if(texture != null) {
          texture.delete();
        }
      }
      LOGGER.warn("Could not load Disc 1 scenery replacement; using original scenery", e);
      return;
    }

    event.background = textures[0];
    System.arraycopy(textures, 1, event.foregrounds, 0, layers);
  }

  static boolean validPack(final Path directory) throws IOException {
    return validPack(directory, 2048, 1424, 4);
  }

  private static String[] names(final int layers) {
    final String[] names = new String[layers + 1];
    names[0] = "background.png";
    for(int i = 0; i < layers; i++) names[i + 1] = "foreground-" + i + ".png";
    return names;
  }

  static boolean validPack(final Path directory, final int width, final int height, final int layers) throws IOException {
    final String[] names = names(layers);
    for(final String name : names) {
      if(!Files.isRegularFile(directory.resolve(name))) {
        return false;
      }
    }
    for(final String name : names) {
      final var image = ImageIO.read(directory.resolve(name).toFile());
      if(image == null || image.getWidth() != width || image.getHeight() != height || !image.getColorModel().hasAlpha()) {
        throw new IOException("Expected a " + width + 'x' + height + " RGBA texture: " + name);
      }
    }
    return true;
  }
}
