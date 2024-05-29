Like what you see? Send me a tip! You can also subscribe to our [YouTube channel](https://www.youtube.com/@legend-of-dragoon). We do devstreams most Wednesdays at 8:00PM Atlantic Time.

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/W7W4HFVW9)

# Legend of Dragoon Java

A project to reverse engineer Legend of Dragoon into a high-level language with a modding API. This is not an emulator, but assembly code disassembled and rewritten in Java.

### Current Progress

- Game engine is fully functional with a few minor glitches that don't negatively affect gameplay
- Modding API is actively in development
- Game is fully playable with no known crashes

### Interested in playing?

Visit our player guide here! https://legendofdragoon.org/projects/severed-chains/

### Interested in the code?

Visit our discord and drop into the [#modding channel](https://discord.com/channels/307164262063669248/318595603636551701)!

A strong knowledge of Java and MIPS assembly is recommended. If you are interested in contributing (or just curious), the following steps should get you up and running:
1. Install a git client and ensure the installation includes command line integration
2. Clone this repository to your local computer using git
3. Copy your ISOs or BINs of the LoD disks into the `isos` directory.
4. Open your local copy of this repository in your IDE (IntelliJ recommended)
5. Gradle should automatically attempt to configure the project and download all dependencies. If it doesn't, expand the gradle tab and click refresh. This process should succeed; resolve any errors if it does not. (lack of command line git can cause issues here)
6. Run the project

Note: Java 21 is required. It is **strongly** recommended to run with assertions enabled.

### Controls ###

Controllers and gamepads are fully supported. Keyboard controls may be changed in the in-game options menu.

Default keyboard controls:
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
- F11 - pause
- F12 - open debug tools (developer features - can easily cause crashes)
- DEL - kill sounds (rarely, a sound may get stuck playing)
- Tab - VRAM viewer

To set up a controller, simply connect it before or after starting the game,
and select it from the controller dropdown in the in-game options menu.

**NOTE**: There are known issues with using DS4windows, and possibly other controller emulators. Severed Chains supports 1800+ controllers out of the box so it's very likely you can just plug in your controller, set it up, and play. If you find a controller that isn't in our controller database, please contact us and we'll work with you to get it added. If you do use DS4windows, make sure your controller isn't hidden and close DS4windows.

### Copyright Information

Even though it is not an emulator, Legend of Dragoon Java can not be played without the user providing the LoD disk images. Assets are extracted from the ROMs at runtime. This codebase does not include any official Legend of Dragoon code or assets.
