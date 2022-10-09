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
1. Install a git client and ensure the installation includes command line integration
2. Clone this repository to your local computer using git
3. Copy your ISOs of the LoD disks into the `isos` directory and rename them to `1.iso`, `2.iso`, etc. Note: if you have BINs instead of ISOs, it's very likely you can change the file extension to ISO and they will work.
4. Open your local copy of this repository in your IDE (IntelliJ recommended)
5. Gradle should automatically attempt to configure the project and download all dependencies. If it doesn't, expand the gradle tab and click refresh. This process should succeed; resolve any errors if it does not. (lack of command line git can cause issues here)
6. run the project

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

To use a controller, open the config.conf file and set controller_config=true. The next time the code is run, it will list all controllers it detects and will prompt you to select the one you wish to use. Only one controller can be registered for use at any given time. 
NOTE: Controller support is currently only set up for XBox controllers. Other controllers are not guaranteed

### Copyright Information

Even though it is not an emulator, Legend of Dragoon Java can not be played without the user providing the LoD ISOs. Assets are extracted from the ROMs at runtime. This codebase does not include any official Legend of Dragoon code or assets.
