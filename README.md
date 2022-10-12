# Legend of Dragoon Java

A project to reverse engineer Legend of Dragoon into a high-level language with a modding API. This is not an emulator, but assembly code disassembled and rewritten in Java.

### Current Progress

- Game engine is mostly functional with a few major graphical glitches that don't negatively affect gameplay
- Currently uses a simple software renderer that is not very efficient but runs at full FPS even on fairly weak hardware
- No audio support yet
- No FMV support yet
- First disk fully playable
- Second disk playable but not fully tested
- Third/fourth disks may be playable, very little testing

### Getting Started

The Java version is still in early development and not ready for playing. A strong knowledge of Java and MIPS assembly is recommended. If you are interested in contributing (or just curious), the following steps should get you up and running:
1. Install a git client and ensure the installation includes command line integration
2. Clone this repository to your local computer using git
3. Copy your ISOs of the LoD disks into the `isos` directory and rename them to `1.iso`, `2.iso`, etc. Note: if you have BINs instead of ISOs, it's very likely you can change the file extension to ISO and they will work.
4. Open your local copy of this repository in your IDE (IntelliJ recommended)
5. Gradle should automatically attempt to configure the project and download all dependencies. If it doesn't, expand the gradle tab and click refresh. This process should succeed; resolve any errors if it does not. (lack of command line git can cause issues here)
6. Run the project

Note: Java 17 is required. It is **strongly** recommended to run with assertions enabled.

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
- Plus/minus - increase/decrease FPS
- CTRL+plus/mins - increase/decrese resolution scale (beta feature)
- F12 - open debug tools (developer feature - can easily cause crashes)

To use a controller, open the config.conf file and set controller_config=true. The next time the code is run, it will list all controllers it detects and will prompt you to select the one you wish to use. Only one controller can be registered for use at any given time. If the game crashes after setting up a controller, delete its GUID from the config file and restart the game.

**NOTE**: For now, only Xbox controllers are officially supported. Other controllers may work but are not guaranteed. More work will be done here in the future.



### Copyright Information

Even though it is not an emulator, Legend of Dragoon Java can not be played without the user providing the LoD ISOs. Assets are extracted from the ROMs at runtime. This codebase does not include any official Legend of Dragoon code or assets.
