# Legend of Dragoon Java

A project to reverse engineer Legend of Dragoon into a high-level language with a modding API. This is not an emulator, but assembly code disassembled and rewritten in Java.

### Current Progress

- PS1 bios reverse engineered
- PS1 kernel reverse engineered
- Hardware emulation layer
  - GPU (currently a simple software renderer, about 65% support)
  - SPU (supports most simple ops, no reverb)
  - MDEC (functional but colour output is wrong; major syncing issues)
  - CDROM (good support)
  - GTE (mostly functional)
  - Joypad (support in progress, getting fairly good)
  - Memcard (some support, mostly rewritten and unnecessary in game)

The kernel and bios are functional and fully capable of bootstrapping the LoD executable. The game boots and the intro FMV plays with major glitches. The main menu loads but is non-functional due to controller input not yet being functional.

### Getting Started

The Java version is still in early development and not ready for playing. A strong knowledge of Java and MIPS assembly is recommended. If you are interested in contributing (or just curious), the following steps should get you up and running:
1. Clone the lod-core repository
2. Deploy lod-core to your maven local repository
3. Clone this repository
4. Copy a PS1 bios into the root directory and rename it to `bios.rom` (the no$psx bios is recommended, I'm not sure if official bioses will work)
5. Copy an ISO of the first LoD disk into the `isos` directory and rename it to `1.iso`

Note: Java 17 is required and both repositories use gradle. It is **strongly** recommended to run with assertions enabled.

### Controls ###

Controls are currently hardcoded to the keyboard.
- D-pad - arrow keys
- Shape buttons - WASD
- Start - enter
- Select - space
- L1 - Q
- L2 - 1
- L3 - Z
- R1 - E
- R2 - 3
- R3 - C

### Copyright Information

Even though it is not an emulator, Legend of Dragoon Java can not be played without the user providing a PS1 bios and LoD ISOs. Assets are extracted from the ROMs at runtime. This codebase does not include any official Legend of Dragoon code or assets.
