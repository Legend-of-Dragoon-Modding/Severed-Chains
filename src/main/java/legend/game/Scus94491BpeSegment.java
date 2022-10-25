package legend.game;

import it.unimi.dsi.fastutil.ints.Int2BooleanMap;
import it.unimi.dsi.fastutil.ints.Int2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import javafx.application.Application;
import javafx.application.Platform;
import legend.core.Config;
import legend.core.DebugHelper;
import legend.core.Hardware;
import legend.core.IoHelper;
import legend.core.cdrom.CdlFILE;
import legend.core.cdrom.FileLoadingInfo;
import legend.core.gpu.Bpp;
import legend.core.gpu.Gpu;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.GpuCommandSetMaskBit;
import legend.core.gpu.GpuCommandUntexturedQuad;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.COLOUR;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.ConsumerRef;
import legend.core.memory.types.FunctionRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.TriFunctionRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.combat.Bttl_800c;
import legend.game.combat.Bttl_800d;
import legend.game.combat.Bttl_800e;
import legend.game.combat.Bttl_800f;
import legend.game.combat.SBtld;
import legend.game.combat.SEffe;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.BattleStruct18cb0;
import legend.game.combat.types.BttlScriptData6cSubBase1;
import legend.game.combat.types.BttlScriptData6cSubBase2;
import legend.game.combat.types.EffectManagerData6c;
import legend.game.debugger.Debugger;
import legend.game.modding.events.EventManager;
import legend.game.modding.events.scripting.ScriptAllocatedEvent;
import legend.game.modding.events.scripting.ScriptDeallocatedEvent;
import legend.game.modding.events.scripting.ScriptTickEvent;
import legend.game.types.CharacterData2c;
import legend.game.types.ExtendedTmd;
import legend.game.types.FileEntry08;
import legend.game.types.GsOT_TAG;
import legend.game.types.LoadingOverlay0c;
import legend.game.types.McqHeader;
import legend.game.types.MrgEntry;
import legend.game.types.MrgFile;
import legend.game.types.RunningScript;
import legend.game.types.ScriptFile;
import legend.game.types.ScriptState;
import legend.game.types.SoundFile;
import legend.game.types.SpuStruct08;
import legend.game.types.SpuStruct28;
import legend.game.types.SshdFile;
import legend.game.types.SssqFile;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import legend.game.types.WorldObject210;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.function.Function;

import static legend.core.Hardware.CDROM;
import static legend.core.Hardware.GPU;
import static legend.core.Hardware.MEMORY;
import static legend.core.Hardware.SPU;
import static legend.core.MemoryHelper.getConsumerAddress;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.SInit.initFileEntries;
import static legend.game.SMap.FUN_800e5934;
import static legend.game.SMap.FUN_800edb8c;
import static legend.game.SMap.chapterTitleCardMrg_800c6710;
import static legend.game.Scus94491BpeSegment_8002.FUN_800201c8;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020360;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020ed8;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022590;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a058;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bb38;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bda4;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c178;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c184;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c86c;
import static legend.game.Scus94491BpeSegment_8002.SquareRoot0;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8002.renderTextboxes;
import static legend.game.Scus94491BpeSegment_8002.sssqResetStuff;
import static legend.game.Scus94491BpeSegment_8003.ClearImage;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b0d0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003c5e0;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsDefDispBuff;
import static legend.game.Scus94491BpeSegment_8003.GsInitGraph;
import static legend.game.Scus94491BpeSegment_8003.GsSortClear;
import static legend.game.Scus94491BpeSegment_8003.GsSwapDispBuff;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.SetDispMask;
import static legend.game.Scus94491BpeSegment_8003.VSync;
import static legend.game.Scus94491BpeSegment_8003.bzero;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.setGp0_2c;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c1f8;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c390;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c3f0;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c8dc;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004cb0c;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004cd50;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004cf8c;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d034;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d2fc;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d41c;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d52c;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d648;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d78c;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d91c;
import static legend.game.Scus94491BpeSegment_8004.SsSetRVol;
import static legend.game.Scus94491BpeSegment_8004._8004dd00;
import static legend.game.Scus94491BpeSegment_8004._8004dd0c;
import static legend.game.Scus94491BpeSegment_8004._8004dd48;
import static legend.game.Scus94491BpeSegment_8004._8004ddcc;
import static legend.game.Scus94491BpeSegment_8004._8004ddd0;
import static legend.game.Scus94491BpeSegment_8004._8004ddd4;
import static legend.game.Scus94491BpeSegment_8004._8004ddd8;
import static legend.game.Scus94491BpeSegment_8004._8004f2a8;
import static legend.game.Scus94491BpeSegment_8004._8004f5d4;
import static legend.game.Scus94491BpeSegment_8004._8004f658;
import static legend.game.Scus94491BpeSegment_8004._8004f65c;
import static legend.game.Scus94491BpeSegment_8004._8004f664;
import static legend.game.Scus94491BpeSegment_8004._8004f698;
import static legend.game.Scus94491BpeSegment_8004._8004f6a4;
import static legend.game.Scus94491BpeSegment_8004._8004f6e4;
import static legend.game.Scus94491BpeSegment_8004._8004f6e8;
import static legend.game.Scus94491BpeSegment_8004._8004f6ec;
import static legend.game.Scus94491BpeSegment_8004._8004fa98;
import static legend.game.Scus94491BpeSegment_8004._8004fb00;
import static legend.game.Scus94491BpeSegment_8004._8004ff10;
import static legend.game.Scus94491BpeSegment_8004._8004ff14;
import static legend.game.Scus94491BpeSegment_8004.callback_8004dbc0;
import static legend.game.Scus94491BpeSegment_8004.currentlyLoadingFileEntry_8004dd04;
import static legend.game.Scus94491BpeSegment_8004.drgnFiles_8004dda0;
import static legend.game.Scus94491BpeSegment_8004.fileCount_8004ddc8;
import static legend.game.Scus94491BpeSegment_8004.fileLoadingCallbackIndex_8004ddc4;
import static legend.game.Scus94491BpeSegment_8004.fileLoadingCallbacks_8004dddc;
import static legend.game.Scus94491BpeSegment_8004.initSound;
import static legend.game.Scus94491BpeSegment_8004.isSpuDmaTransferInProgress;
import static legend.game.Scus94491BpeSegment_8004.loadSshdAndSoundbank;
import static legend.game.Scus94491BpeSegment_8004.loadedOverlayIndex_8004dd10;
import static legend.game.Scus94491BpeSegment_8004.loadingGameStateOverlay_8004dd08;
import static legend.game.Scus94491BpeSegment_8004.loadingOverlay_8004dd1e;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndexOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.overlayQueueIndex_8004dd14;
import static legend.game.Scus94491BpeSegment_8004.overlayQueueIndex_8004dd18;
import static legend.game.Scus94491BpeSegment_8004.overlaysLoadedCount_8004dd1c;
import static legend.game.Scus94491BpeSegment_8004.overlays_8004db88;
import static legend.game.Scus94491BpeSegment_8004.previousMainCallbackIndex_8004dd28;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_8004.reinitOrderingTableBits_8004dd38;
import static legend.game.Scus94491BpeSegment_8004.renderFlags_8004dd36;
import static legend.game.Scus94491BpeSegment_8004.scriptFunctions_8004e098;
import static legend.game.Scus94491BpeSegment_8004.scriptPtrs_8004de58;
import static legend.game.Scus94491BpeSegment_8004.scriptStateUpperBound_8004de4c;
import static legend.game.Scus94491BpeSegment_8004.scriptSubFunctions_8004e29c;
import static legend.game.Scus94491BpeSegment_8004.setCdVolume;
import static legend.game.Scus94491BpeSegment_8004.setMainVolume;
import static legend.game.Scus94491BpeSegment_8004.setSpuDmaCompleteCallback;
import static legend.game.Scus94491BpeSegment_8004.simpleRandSeed_8004dd44;
import static legend.game.Scus94491BpeSegment_8004.sssqFadeIn;
import static legend.game.Scus94491BpeSegment_8004.sssqGetTempo;
import static legend.game.Scus94491BpeSegment_8004.sssqPitchShift;
import static legend.game.Scus94491BpeSegment_8004.sssqSetReverbType;
import static legend.game.Scus94491BpeSegment_8004.sssqSetTempo;
import static legend.game.Scus94491BpeSegment_8004.sssqTick;
import static legend.game.Scus94491BpeSegment_8004.sssqUnloadPlayableSound;
import static legend.game.Scus94491BpeSegment_8004.swapDisplayBuffer_8004dd40;
import static legend.game.Scus94491BpeSegment_8004.syncFrame_8004dd3c;
import static legend.game.Scus94491BpeSegment_8004.width_8004dd34;
import static legend.game.Scus94491BpeSegment_8005._80050068;
import static legend.game.Scus94491BpeSegment_8005._800500e8;
import static legend.game.Scus94491BpeSegment_8005._800500f8;
import static legend.game.Scus94491BpeSegment_8005._80050104;
import static legend.game.Scus94491BpeSegment_8005._8005019c;
import static legend.game.Scus94491BpeSegment_8005._800501bc;
import static legend.game.Scus94491BpeSegment_8005._8005a1e0;
import static legend.game.Scus94491BpeSegment_8005._8005a1e4;
import static legend.game.Scus94491BpeSegment_8005._8005a1ea;
import static legend.game.Scus94491BpeSegment_8005.heapHead_8005a2a0;
import static legend.game.Scus94491BpeSegment_8005.heapTail_8005a2a4;
import static legend.game.Scus94491BpeSegment_8005.loadingOverlays_8005a2a8;
import static legend.game.Scus94491BpeSegment_8005.orderingTableTags_8005a398;
import static legend.game.Scus94491BpeSegment_8005.orderingTables_8005a370;
import static legend.game.Scus94491BpeSegment_8005.sin_cos_80054d0c;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_8006._8006f284;
import static legend.game.Scus94491BpeSegment_8007._8007a3a8;
import static legend.game.Scus94491BpeSegment_8007._8007a3c0;
import static legend.game.Scus94491BpeSegment_8007.joypadInput_8007a39c;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.joypadRepeat_8007a3a0;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.CdlFILE_800bb4c8;
import static legend.game.Scus94491BpeSegment_800b.RunningScript_800bc070;
import static legend.game.Scus94491BpeSegment_800b.SInitBinLoaded_800bbad0;
import static legend.game.Scus94491BpeSegment_800b._800babc0;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bb104;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bc91c;
import static legend.game.Scus94491BpeSegment_800b._800bc94c;
import static legend.game.Scus94491BpeSegment_800b._800bc960;
import static legend.game.Scus94491BpeSegment_800b._800bc974;
import static legend.game.Scus94491BpeSegment_800b._800bc980;
import static legend.game.Scus94491BpeSegment_800b._800bc9a8;
import static legend.game.Scus94491BpeSegment_800b._800bca68;
import static legend.game.Scus94491BpeSegment_800b._800bca6c;
import static legend.game.Scus94491BpeSegment_800b._800bd0f0;
import static legend.game.Scus94491BpeSegment_800b._800bd0fc;
import static legend.game.Scus94491BpeSegment_800b._800bd108;
import static legend.game.Scus94491BpeSegment_800b._800bd610;
import static legend.game.Scus94491BpeSegment_800b._800bd680;
import static legend.game.Scus94491BpeSegment_800b._800bd6e8;
import static legend.game.Scus94491BpeSegment_800b._800bd6f8;
import static legend.game.Scus94491BpeSegment_800b._800bd700;
import static legend.game.Scus94491BpeSegment_800b._800bd704;
import static legend.game.Scus94491BpeSegment_800b._800bd708;
import static legend.game.Scus94491BpeSegment_800b._800bd70c;
import static legend.game.Scus94491BpeSegment_800b._800bd710;
import static legend.game.Scus94491BpeSegment_800b._800bd714;
import static legend.game.Scus94491BpeSegment_800b._800bd740;
import static legend.game.Scus94491BpeSegment_800b._800bd758;
import static legend.game.Scus94491BpeSegment_800b._800bd768;
import static legend.game.Scus94491BpeSegment_800b._800bd774;
import static legend.game.Scus94491BpeSegment_800b._800bd780;
import static legend.game.Scus94491BpeSegment_800b._800bd781;
import static legend.game.Scus94491BpeSegment_800b._800bd782;
import static legend.game.Scus94491BpeSegment_800b._800bd808;
import static legend.game.Scus94491BpeSegment_800b._800bdc34;
import static legend.game.Scus94491BpeSegment_800b._800bee90;
import static legend.game.Scus94491BpeSegment_800b._800bee94;
import static legend.game.Scus94491BpeSegment_800b._800bee98;
import static legend.game.Scus94491BpeSegment_800b._800bf0cf;
import static legend.game.Scus94491BpeSegment_800b._800bf0d8;
import static legend.game.Scus94491BpeSegment_800b._800bf0e0;
import static legend.game.Scus94491BpeSegment_800b.currentlyLoadingFileInfo_800bb468;
import static legend.game.Scus94491BpeSegment_800b.doubleBufferFrame_800bb108;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.drgnMrg_800bc060;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.fileLoadingInfoArray_800bbad8;
import static legend.game.Scus94491BpeSegment_800b.fileSize_800bb464;
import static legend.game.Scus94491BpeSegment_800b.fileSize_800bb48c;
import static legend.game.Scus94491BpeSegment_800b.fileTransferDest_800bb488;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.loadedDrgnFiles_800bcf78;
import static legend.game.Scus94491BpeSegment_800b.numberOfTransfers_800bb490;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.scriptEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.scriptState_800bc0c0;
import static legend.game.Scus94491BpeSegment_800b.scriptsDisabled_800bc0b9;
import static legend.game.Scus94491BpeSegment_800b.scriptsTickDisabled_800bc0b8;
import static legend.game.Scus94491BpeSegment_800b.soundFileArr_800bcf80;
import static legend.game.Scus94491BpeSegment_800b.soundMrgPtr_800bd748;
import static legend.game.Scus94491BpeSegment_800b.soundMrgPtr_800bd76c;
import static legend.game.Scus94491BpeSegment_800b.soundMrgSshdPtr_800bd784;
import static legend.game.Scus94491BpeSegment_800b.soundMrgSssqPtr_800bd788;
import static legend.game.Scus94491BpeSegment_800b.soundbank_800bd778;
import static legend.game.Scus94491BpeSegment_800b.spu28Arr_800bca78;
import static legend.game.Scus94491BpeSegment_800b.spu28Arr_800bd110;
import static legend.game.Scus94491BpeSegment_800b.sssqChannelIndex_800bd0f8;
import static legend.game.Scus94491BpeSegment_800b.sssqTempoScale_800bd100;
import static legend.game.Scus94491BpeSegment_800b.sssqTempo_800bd104;
import static legend.game.Scus94491BpeSegment_800b.timHeader_800bc2e0;
import static legend.game.Scus94491BpeSegment_800b.transferDest_800bb460;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.DISPENV_800c34b0;
import static legend.game.Scus94491BpeSegment_800c.PSDIDX_800c34d4;
import static legend.game.Scus94491BpeSegment_800d.sceaTexture_800d05c4;
import static legend.game.combat.Bttl_800c.FUN_800c7304;
import static legend.game.combat.Bttl_800c.FUN_800c882c;
import static legend.game.combat.Bttl_800c.FUN_800c8cf0;
import static legend.game.combat.Bttl_800c.FUN_800c90b0;
import static legend.game.combat.Bttl_800c._800c6768;
import static legend.game.combat.Bttl_800c.charCount_800c677c;
import static legend.game.combat.Bttl_800d.FUN_800d8f10;
import static legend.game.combat.SBtld._80109a98;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_LEFT_X;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_A;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_B;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_BACK;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_START;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_X;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_Y;
import static org.lwjgl.glfw.GLFW.GLFW_HAT_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_HAT_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_HAT_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_HAT_UP;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_EQUAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F12;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ADD;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_SUBTRACT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_L;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_MINUS;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_CONTROL;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickAxes;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickButtons;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickGUID;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickHats;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickName;

public final class Scus94491BpeSegment {
  private Scus94491BpeSegment() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment.class);

  public static final IntRef orderingTableBits_1f8003c0 = MEMORY.ref(4, 0x1f8003c0L, IntRef::new);
  public static final IntRef zShift_1f8003c4 = MEMORY.ref(4, 0x1f8003c4L, IntRef::new);
  public static final IntRef orderingTableSize_1f8003c8 = MEMORY.ref(4, 0x1f8003c8L, IntRef::new);
  public static final IntRef zMax_1f8003cc = MEMORY.ref(4, 0x1f8003ccL, IntRef::new);
  public static final Pointer<UnboundedArrayRef<GsOT_TAG>> tags_1f8003d0 = MEMORY.ref(4, 0x1f8003d0L, Pointer.of(4, UnboundedArrayRef.of(4, GsOT_TAG::new)));

  public static final Value gpuPacketAddr_1f8003d8 = MEMORY.new MemoryValue(4, 0x1f8003d8L) {
    @Override
    public long get() {
      throw new RuntimeException("NOPE");
    }
  };
  public static final ShortRef centreScreenX_1f8003dc = MEMORY.ref(2, 0x1f8003dcL, ShortRef::new);
  public static final ShortRef centreScreenY_1f8003de = MEMORY.ref(2, 0x1f8003deL, ShortRef::new);
  public static final IntRef displayWidth_1f8003e0 = MEMORY.ref(4, 0x1f8003e0L, IntRef::new);
  public static final IntRef displayHeight_1f8003e4 = MEMORY.ref(4, 0x1f8003e4L, IntRef::new);
  public static final IntRef zOffset_1f8003e8 = MEMORY.ref(4, 0x1f8003e8L, IntRef::new);
  public static final Value _1f8003ec = MEMORY.ref(2, 0x1f8003ecL);
  public static final Value _1f8003ee = MEMORY.ref(2, 0x1f8003eeL);

  public static final Pointer<BattleStruct18cb0> _1f8003f4 = MEMORY.ref(4, 0x1f8003f4L, Pointer.deferred(4, BattleStruct18cb0::new));
  public static final Value _1f8003f8 = MEMORY.ref(4, 0x1f8003f8L);
  public static final Value _1f8003fc = MEMORY.ref(4, 0x1f8003fcL);

  public static final Value _80010000 = MEMORY.ref(4, 0x80010000L);
  public static final Value _80010004 = MEMORY.ref(4, 0x80010004L);

  public static final Value _80010250 = MEMORY.ref(4, 0x80010250L);

  public static final Value _80010320 = MEMORY.ref(4, 0x80010320L);

  public static final COLOUR colour_80010328 = MEMORY.ref(1, 0x80010328L, COLOUR::new);
  public static final Value _8001032c = MEMORY.ref(1, 0x8001032cL);
  public static final Value _80010334 = MEMORY.ref(1, 0x80010334L);

  public static final ExtendedTmd extendedTmd_800103d0 = MEMORY.ref(4, 0x800103d0L, ExtendedTmd::new);
  public static final TmdAnimationFile tmdAnimFile_8001051c = MEMORY.ref(4, 0x8001051cL, TmdAnimationFile::new);

  /** TIM */
  public static final Value _80010544 = MEMORY.ref(4, 0x80010544L);

  public static final Value ovalBlobTimHeader_80010548 = MEMORY.ref(4, 0x80010548L);

  public static final ArrayRef<RECT> rectArray28_80010770 = MEMORY.ref(4, 0x80010770L, ArrayRef.of(RECT.class, 28, 8, RECT::new));

  public static final Value _80010868 = MEMORY.ref(4, 0x80010868L);

  /** TODO 0x60-byte struct */
  public static final Value _800108b0 = MEMORY.ref(4, 0x800108b0L);

  public static final CString cdName_80011700 = MEMORY.ref(6, 0x80011700L, CString::new);

  public static final Value _80011db0 = MEMORY.ref(4, 0x80011db0L);
  public static final Value _80011db4 = MEMORY.ref(4, 0x80011db4L);
  public static final Value _80011db8 = MEMORY.ref(4, 0x80011db8L);
  public static final Value _80011dbc = MEMORY.ref(4, 0x80011dbcL);

  public static final Value heap_8011e210 = MEMORY.ref(4, 0x8011e210L);

  public static boolean[] scriptLog = new boolean[0x48];

  private static final Int2ObjectMap<Function<RunningScript, String>> scriptFunctionDescriptions = new Int2ObjectOpenHashMap<>();

  static {
    scriptFunctionDescriptions.put(0, r -> "pause;");
    scriptFunctionDescriptions.put(1, r -> "rewind;");
    scriptFunctionDescriptions.put(2, r -> {
      final int waitFrames = r.params_20.get(0).deref().get();

      if(waitFrames != 0) {
        return "wait %d (p0) frames;".formatted(waitFrames);
      } else {
        return "wait complete - continue;";
      }
    });
    scriptFunctionDescriptions.put(3, r -> {
      final int operandA = r.params_20.get(0).deref().get();
      final int operandB = r.params_20.get(1).deref().get();
      final int op = (int)r.opParam_18.get();

      return (switch(op) {
        case 0 -> "if 0x%x (p0) <= 0x%x (p1)? %s;";
        case 1 -> "if 0x%x (p0) = 0x%x (p1)? %s;";
        case 2 -> "if 0x%x (p0) == 0x%x (p1)? %s;";
        case 3 -> "if 0x%x (p0) != 0x%x (p1)? %s;";
        case 4 -> "if 0x%x (p0) > 0x%x (p1)? %s;";
        case 5 -> "if 0x%x (p0) >= 0x%x (p1)? %s;";
        case 6 -> "if 0x%x (p0) & 0x%x (p1)? %s;";
        case 7 -> "if 0x%x (p0) !& 0x%x (p1)? %s;";
        default -> "illegal cmp 3";
      }).formatted(operandA, operandB, scriptCompare(r, operandA, operandB, op) != 0 ? "yes - continue" : "no - rewind");
    });
    scriptFunctionDescriptions.put(4, r -> {
      final int operandB = r.params_20.get(0).deref().get();
      final int op = (int)r.opParam_18.get();

      return (switch(op) {
        case 0 -> "if 0 <= 0x%x (p1)? %s;";
        case 1 -> "if 0 = 0x%x (p1)? %s;";
        case 2 -> "if 0 == 0x%x (p1)? %s;";
        case 3 -> "if 0 != 0x%x (p1)? %s;";
        case 4 -> "if 0 > 0x%x (p1)? %s;";
        case 5 -> "if 0 >= 0x%x (p1)? %s;";
        case 6 -> "if 0 & 0x%x (p1)? %s;";
        case 7 -> "if 0 !& 0x%x (p1)? %s;";
        default -> "illegal cmp 4";
      }).formatted(operandB, scriptCompare(r, 0, operandB, op) != 0 ? "yes - continue" : "no - rewind");
    });
    scriptFunctionDescriptions.put(8, r -> "*0x%08x (p1) = 0x%x (p0);".formatted(r.params_20.get(0).getPointer(), r.params_20.get(0).deref().get()));
    scriptFunctionDescriptions.put(10, r -> "memcpy(0x%08x (p1), 0x%08x (p2), %d (p0));".formatted(r.params_20.get(1).getPointer(), r.params_20.get(2).getPointer(), r.params_20.get(0).deref().get()));
    scriptFunctionDescriptions.put(12, r -> "*0x%08x (p0) = 0;".formatted(r.params_20.get(0).getPointer()));
    scriptFunctionDescriptions.put(16, r -> "*0x%08x (p1) &= 0x%x (p0);".formatted(r.params_20.get(1).getPointer(), r.params_20.get(0).deref().get()));
    scriptFunctionDescriptions.put(17, r -> "*0x%08x (p1) |= 0x%x (p0);".formatted(r.params_20.get(1).getPointer(), r.params_20.get(0).deref().get()));
    scriptFunctionDescriptions.put(18, r -> "*0x%08x (p1) ^= 0x%x (p0);".formatted(r.params_20.get(1).getPointer(), r.params_20.get(0).deref().get()));
    scriptFunctionDescriptions.put(19, r -> "*0x%08x (p2) &|= 0x%x (p0), 0x%x (p1);".formatted(r.params_20.get(2).getPointer(), r.params_20.get(0).deref().get(), r.params_20.get(1).deref().get()));
    scriptFunctionDescriptions.put(20, r -> "~*0x%08x (p0);".formatted(r.params_20.get(0).getPointer()));
    scriptFunctionDescriptions.put(21, r -> "*0x%08x (p1) <<= 0x%x (p0);".formatted(r.params_20.get(1).getPointer(), r.params_20.get(0).deref().get()));
    scriptFunctionDescriptions.put(22, r -> "*0x%08x (p1) >>= 0x%x (p0);".formatted(r.params_20.get(1).getPointer(), r.params_20.get(0).deref().get()));
    scriptFunctionDescriptions.put(24, r -> "*0x%08x (p1) += 0x%x (p0);".formatted(r.params_20.get(1).getPointer(), r.params_20.get(0).deref().get()));
    scriptFunctionDescriptions.put(25, r -> "*0x%08x (p1) -= 0x%x (p0);".formatted(r.params_20.get(1).getPointer(), r.params_20.get(0).deref().get()));
    scriptFunctionDescriptions.put(26, r -> "*0x%08x (p1) = 0x%x (p0) - 0x%x (p1);".formatted(r.params_20.get(1).getPointer(), r.params_20.get(0).deref().get(), r.params_20.get(1).deref().get()));
    scriptFunctionDescriptions.put(27, r -> "*0x%08x (p0) ++;".formatted(r.params_20.get(0).getPointer()));
    scriptFunctionDescriptions.put(28, r -> "*0x%08x (p0) --;".formatted(r.params_20.get(0).getPointer()));
    scriptFunctionDescriptions.put(29, r -> "-*0x%08x (p0);".formatted(r.params_20.get(0).getPointer()));
    scriptFunctionDescriptions.put(30, r -> "|*0x%08x| (p0);".formatted(r.params_20.get(0).getPointer()));
    scriptFunctionDescriptions.put(32, r -> "*0x%08x (p1) *= 0x%x (p0);".formatted(r.params_20.get(1).getPointer(), r.params_20.get(0).deref().get()));
    scriptFunctionDescriptions.put(33, r -> "*0x%08x (p1) /= 0x%x (p0);".formatted(r.params_20.get(1).getPointer(), r.params_20.get(0).deref().get()));
    scriptFunctionDescriptions.put(34, r -> "*0x%08x (p1) = 0x%x (p0) / 0x%x (p1);".formatted(r.params_20.get(1).getPointer(), r.params_20.get(0).deref().get(), r.params_20.get(1).deref().get()));
    scriptFunctionDescriptions.put(35, r -> "*0x%08x (p1) %%= 0x%x (p0);".formatted(r.params_20.get(1).getPointer(), r.params_20.get(0).deref().get()));
    scriptFunctionDescriptions.put(36, r -> "*0x%08x (p1) = 0x%x (p0) %% 0x%x (p1);".formatted(r.params_20.get(1).getPointer(), r.params_20.get(0).deref().get(), r.params_20.get(1).deref().get()));
    scriptFunctionDescriptions.put(43, scriptFunctionDescriptions.get(35));
    scriptFunctionDescriptions.put(44, scriptFunctionDescriptions.get(36));
    scriptFunctionDescriptions.put(48, r -> "*0x%08x (p1) = sqrt(0x%x (p0));".formatted(r.params_20.get(1).getPointer(), r.params_20.get(0).deref().get()));
    scriptFunctionDescriptions.put(50, r -> "*0x%08x (p1) = sin(0x%x (p0));".formatted(r.params_20.get(1).getPointer(), r.params_20.get(0).deref().get()));
    scriptFunctionDescriptions.put(51, r -> "*0x%08x (p1) = cos(0x%x (p0));".formatted(r.params_20.get(1).getPointer(), r.params_20.get(0).deref().get()));
    scriptFunctionDescriptions.put(52, r -> "*0x%08x (p2) = ratan2(0x%x (p0), 0x%x (p1));".formatted(r.params_20.get(2).getPointer(), r.params_20.get(0).deref().get(), r.params_20.get(1).deref().get()));
    scriptFunctionDescriptions.put(56, r -> "subfunc(%d (pp) (%08x));".formatted(r.opParam_18.get(), scriptSubFunctions_8004e29c.get((int)r.opParam_18.get()).getPointer()));
    scriptFunctionDescriptions.put(64, r -> "jmp 0x%x (p0);".formatted(r.params_20.get(0).getPointer() - r.scriptState_04.deref().scriptPtr_14.getPointer()));
    scriptFunctionDescriptions.put(65, r -> {
      final int operandA = r.params_20.get(0).deref().get();
      final int operandB = r.params_20.get(1).deref().get();
      final int op = (int)r.opParam_18.get();
      final long dest = r.params_20.get(2).getPointer() - r.scriptState_04.deref().scriptPtr_14.getPointer();

      return (switch(op) {
        case 0 -> "if 0x%x (p0) <= 0x%x (p1)? %s;";
        case 1 -> "if 0x%x (p0) = 0x%x (p1)? %s;";
        case 2 -> "if 0x%x (p0) == 0x%x (p1)? %s;";
        case 3 -> "if 0x%x (p0) != 0x%x (p1)? %s;";
        case 4 -> "if 0x%x (p0) > 0x%x (p1)? %s;";
        case 5 -> "if 0x%x (p0) >= 0x%x (p1)? %s;";
        case 6 -> "if 0x%x (p0) & 0x%x (p1)? %s;";
        case 7 -> "if 0x%x (p0) !& 0x%x (p1)? %s;";
        default -> "illegal cmp 65";
      }).formatted(operandA, operandB, scriptCompare(r, operandA, operandB, op) != 0 ? "yes - jmp 0x%x (p2)".formatted(dest) : "no - continue");
    });
    scriptFunctionDescriptions.put(66, r -> {
      final int operandB = r.params_20.get(0).deref().get();
      final int op = (int)r.opParam_18.get();
      final long dest = r.params_20.get(1).getPointer() - r.scriptState_04.deref().scriptPtr_14.getPointer();

      return (switch(op) {
        case 0 -> "if 0 <= 0x%x (p1)? %s;";
        case 1 -> "if 0 = 0x%x (p1)? %s;";
        case 2 -> "if 0 == 0x%x (p1)? %s;";
        case 3 -> "if 0 != 0x%x (p1)? %s;";
        case 4 -> "if 0 > 0x%x (p1)? %s;";
        case 5 -> "if 0 >= 0x%x (p1)? %s;";
        case 6 -> "if 0 & 0x%x (p1)? %s;";
        case 7 -> "if 0 !& 0x%x (p1)? %s;";
        default -> "illegal cmp 66";
      }).formatted(operandB, scriptCompare(r, 0, operandB, op) != 0 ? "yes - jmp 0x%x (p1)".formatted(dest) : "no - continue");
    });
    scriptFunctionDescriptions.put(72, r -> "func 0x%x (p0);".formatted(r.params_20.get(0).getPointer() - r.scriptState_04.deref().scriptPtr_14.getPointer()));
    scriptFunctionDescriptions.put(73, r -> "return;");
    scriptFunctionDescriptions.put(74, r -> {
      final long a = r.params_20.get(1).getPointer();
      final int b = r.params_20.get(0).deref().get();
      final long ptr = a + MEMORY.ref(4, a + b * 4).getSigned() * 4;
      return "func 0x%x (p1 + p1[p0 * 4] * 4);".formatted(ptr - r.scriptState_04.deref().scriptPtr_14.getPointer());
    });
  }

  @Method(0x80011dc0L)
  public static void spuTimerInterruptCallback() {
    sssqTick();

    if(mainCallbackIndex_8004dd20.get() == 0x3L) {
      gameState_800babc8.timestamp_a0.set(0);
    } else {
      gameState_800babc8.timestamp_a0.incr();
    }
  }

  public static final boolean SYNCHRONOUS = true;
  private static boolean dumping;
  private static boolean loading;

  private static int fpsLimit = 30;

  private static final Path state = Paths.get("./state.ddmp");

  private static final float controllerDeadzone = Config.controllerDeadzone();
  private static int controllerId = -1;
  private static boolean inputPulse;
  private static final Int2IntMap keyRepeat = new Int2IntOpenHashMap();
  private static final Int2BooleanMap controllerEdgeTriggers = new Int2BooleanOpenHashMap();

  private static void handleControllerInput() {
    if(controllerId != -1) {
      final FloatBuffer axes = glfwGetJoystickAxes(controllerId);
      final float axisX = axes.get(GLFW_GAMEPAD_AXIS_LEFT_X);
      final float axisY = axes.get(GLFW_GAMEPAD_AXIS_LEFT_Y);
      final float axisL = axes.get(GLFW_GAMEPAD_AXIS_LEFT_TRIGGER);
      final float axisR = axes.get(GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER);

      final ByteBuffer hats = glfwGetJoystickHats(controllerId);
      final int dpad = hats.get(0);

      if(axisX > controllerDeadzone || (dpad & GLFW_HAT_RIGHT) != 0) {
        controllerPress(0x2000);
      } else if(axisX < -controllerDeadzone || (dpad & GLFW_HAT_LEFT) != 0) {
        controllerPress(0x8000);
      } else {
        controllerRelease(0x2000);
        controllerRelease(0x8000);
      }

      if(axisY < -controllerDeadzone || (dpad & GLFW_HAT_UP) != 0) {
        controllerPress(0x1000);
      } else if(axisY > controllerDeadzone || (dpad & GLFW_HAT_DOWN) != 0) {
        controllerPress(0x4000);
      } else {
        controllerRelease(0x1000);
        controllerRelease(0x4000);
      }

      if(axisL > controllerDeadzone) {
        controllerPress(0x1);
      } else {
        controllerRelease(0x1);
      }

      if(axisR > controllerDeadzone) {
        controllerPress(0x2);
      } else {
        controllerRelease(0x2);
      }

      final ByteBuffer buttons = glfwGetJoystickButtons(controllerId);

      if(buttons.get(GLFW_GAMEPAD_BUTTON_A) != 0) {
        controllerPress(0x20);
      } else {
        controllerRelease(0x20);
      }

      if(buttons.get(GLFW_GAMEPAD_BUTTON_B) != 0) {
        controllerPress(0x40);
      } else {
        controllerRelease(0x40);
      }

      if(buttons.get(GLFW_GAMEPAD_BUTTON_X) != 0) {
        controllerPress(0x80);
      } else {
        controllerRelease(0x80);
      }

      if(buttons.get(GLFW_GAMEPAD_BUTTON_Y) != 0) {
        controllerPress(0x10);
      } else {
        controllerRelease(0x10);
      }

      if(buttons.get(GLFW_GAMEPAD_BUTTON_LEFT_BUMPER) != 0) {
        controllerPress(0x4);
      } else {
        controllerRelease(0x4);
      }

      if(buttons.get(GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER) != 0) {
        controllerPress(0x8);
      } else {
        controllerRelease(0x8);
      }

      if(buttons.get(GLFW_GAMEPAD_BUTTON_START) != 0) {
        controllerPress(0x800);
      } else {
        controllerRelease(0x800);
      }

      if(buttons.get(GLFW_GAMEPAD_BUTTON_BACK) != 0) {
        controllerPress(0x100);
      } else {
        controllerRelease(0x100);
      }

      if(buttons.get(GLFW_GAMEPAD_BUTTON_LEFT_THUMB) != 0) {
        controllerPress(0x200);
      } else {
        controllerRelease(0x200);
      }

      if(buttons.get(GLFW_GAMEPAD_BUTTON_RIGHT_THUMB) != 0) {
        controllerPress(0x400);
      } else {
        controllerRelease(0x400);
      }
    }
  }

  private static void controllerPress(final int input) {
    if(!controllerEdgeTriggers.getOrDefault(input, false)) {
      _800bee90.oru(input);
      _800bee94.oru(input);
      _800bee98.oru(input);
      keyRepeat.put(input, 0);
      controllerEdgeTriggers.put(input, true);
    }
  }

  private static void controllerRelease(final int input) {
    if(controllerEdgeTriggers.getOrDefault(input, false)) {
      _800bee90.and(~input);
      _800bee94.and(~input);
      _800bee98.and(~input);
      keyRepeat.remove(input);
      controllerEdgeTriggers.remove(input);
    }
  }

  @Method(0x80011e1cL)
  public static void gameLoop() {
    GPU.window().setFpsLimit(fpsLimit);

    GPU.events().onKeyPress((window, key, scancode, mods) -> {
      if(key == GLFW_KEY_S && (mods & GLFW_MOD_CONTROL) != 0) {
        dumping = true;
      }

      if(key == GLFW_KEY_L && (mods & GLFW_MOD_CONTROL) != 0) {
        loading = true;
      }

      if(key == GLFW_KEY_MINUS || key == GLFW_KEY_KP_SUBTRACT) {
        if((mods & GLFW_MOD_CONTROL) == 0) {
          if(fpsLimit > 5) {
            fpsLimit -= 5;
            GPU.window().setFpsLimit(fpsLimit);
          }
        } else {
          if(GPU.getRenderScale() > 1) {
            GPU.rescaleVram(GPU.getRenderScale() - 1);
          }
        }
      }

      if(key == GLFW_KEY_EQUAL || key == GLFW_KEY_KP_ADD) {
        if((mods & GLFW_MOD_CONTROL) == 0) {
          fpsLimit += 5;
          GPU.window().setFpsLimit(fpsLimit);
        } else {
          GPU.rescaleVram(GPU.getRenderScale() + 1);
        }
      }

      if(key == GLFW_KEY_F12) {
        if(!Debugger.isRunning()) {
          try {
            Platform.setImplicitExit(false);
            new Thread(() -> Application.launch(Debugger.class)).start();
          } catch(final Exception e) {
            LOGGER.info("Failed to start script debugger", e);
          }
        } else {
          Platform.runLater(Debugger::show);
        }
      }
    });

    final Int2IntMap gamepadKeyMap = new Int2IntOpenHashMap();
    gamepadKeyMap.put(GLFW_KEY_SPACE, 0x100); // Select
    gamepadKeyMap.put(GLFW_KEY_Z, 0x200); // L3
    gamepadKeyMap.put(GLFW_KEY_C, 0x400); // R3
    gamepadKeyMap.put(GLFW_KEY_ENTER, 0x800); // Start
    gamepadKeyMap.put(GLFW_KEY_UP, 0x1000); // Up
    gamepadKeyMap.put(GLFW_KEY_RIGHT, 0x2000); // Right
    gamepadKeyMap.put(GLFW_KEY_DOWN, 0x4000); // Down
    gamepadKeyMap.put(GLFW_KEY_LEFT, 0x8000); // Left
    gamepadKeyMap.put(GLFW_KEY_1, 0x01); // L2
    gamepadKeyMap.put(GLFW_KEY_3, 0x02); // R2
    gamepadKeyMap.put(GLFW_KEY_Q, 0x04); // L1
    gamepadKeyMap.put(GLFW_KEY_E, 0x08); // R1
    gamepadKeyMap.put(GLFW_KEY_W, 0x10); // Triangle
    gamepadKeyMap.put(GLFW_KEY_D, 0x40); // Circle
    gamepadKeyMap.put(GLFW_KEY_S, 0x20); // Cross
    gamepadKeyMap.put(GLFW_KEY_A, 0x80); // Square

    GPU.window().events.onKeyPress((window, key, scancode, mods) -> {
      if(mods != 0) {
        return;
      }

      final int input = gamepadKeyMap.get(key);

      if(input != 0) {
        _800bee90.oru(input);
        _800bee94.oru(input);
        _800bee98.oru(input);

        keyRepeat.put(input, 0);
      }
    });

    GPU.window().events.onKeyRelease((window, key, scancode, mods) -> {
      final int input = gamepadKeyMap.get(key);

      if(input != 0) {
        _800bee90.and(~input);
        _800bee94.and(~input);
        _800bee98.and(~input);

        keyRepeat.remove(input);
      }
    });

    final String controllerGuid = Config.controllerGuid();
    for(int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
      if(controllerGuid.equals(glfwGetJoystickGUID(i))) {
        System.out.println("Using gamepad " + glfwGetJoystickName(i));
        controllerId = i;
        break;
      }
    }

    GPU.window().events.onControllerConnected((window, id) -> {
      if(controllerGuid.equals(glfwGetJoystickGUID(id))) {
        controllerId = id;
      }
    });

    GPU.window().events.onControllerDisconnected((window, id) -> {
      if(controllerId == id) {
        controllerId = -1;
      }
    });

    final Runnable r = () -> {
      EventManager.INSTANCE.clearStaleRefs();

      handleControllerInput();

      joypadPress_8007a398.setu(_800bee94);
      joypadInput_8007a39c.setu(_800bee90);
      joypadRepeat_8007a3a0.setu(_800bee98);

      startFrame();
      FUN_80011f6c();
      loadFiles();
      executeLoadersAndScripts();
      FUN_8001b410();
      FUN_80013778();

      // SPU stuff
      FUN_8001aa24();

      // Textboxes? Other things?
      FUN_8002a058();
      renderTextboxes();

      FUN_80020ed8();
      _800bb0fc.addu(0x1L);
      endFrame();

      _800bee94.setu(0);
      _800bee98.setu(0);

      if(inputPulse) {
        for(final var entry : keyRepeat.int2IntEntrySet()) {
          if(entry.getIntValue() >= 2) { //TODO adjust for frame rate
            _800bee98.oru(entry.getIntKey());
          }

          entry.setValue(entry.getIntValue() + 1);
        }
      }

      inputPulse = !inputPulse;

      handleSaveStates();
    };

    if(SYNCHRONOUS) {
      GPU.r = r;
    }

    while(Hardware.isAlive()) {
      if(!SYNCHRONOUS) {
        r.run();
      }

      DebugHelper.sleep(1);
    }

    Platform.exit();
  }

  private static void handleSaveStates() {
    if(dumping) {
      LOGGER.info("Pausing execution to dump save state...");
      try(final FileChannel channel = FileChannel.open(state, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.READ)) {
        final ByteBuffer buf = channel.map(FileChannel.MapMode.READ_WRITE, 0, 32 * 1024 * 1024);
        Hardware.dump(buf);

        for(int i = 0; i < scriptStatePtrArr_800bc1c0.length(); i++) {
          final Pointer<?> ptr = scriptStatePtrArr_800bc1c0.get(i).deref().innerStruct_00;

          if(ptr.isNull()) {
            IoHelper.write(buf, "");
          } else {
            IoHelper.write(buf, ptr.deref().getClass().getName());

            if(ptr.deref() instanceof EffectManagerData6c) {
              final Pointer<BttlScriptData6cSubBase1> ptr1 = ((EffectManagerData6c)ptr.deref())._44;

              if(ptr1.isNull()) {
                IoHelper.write(buf, "");
              } else {
                IoHelper.write(buf, ptr1.deref().getClass().getName());
              }

              Pointer<BttlScriptData6cSubBase2> ptr2 = ((EffectManagerData6c)ptr.deref())._58;

              while(!ptr2.isNull()) {
                IoHelper.write(buf, ptr2.deref().getClass().getName());
                ptr2 = ptr2.deref()._00;
              }

              IoHelper.write(buf, "");
            }
          }
        }

        LOGGER.info("Save state complete");
      } catch(final Throwable throwable) {
        LOGGER.error("Failed to dump save state", throwable);
      }

      dumping = false;
    }

    if(loading) {
      try(final FileChannel channel = FileChannel.open(state, StandardOpenOption.READ)) {
        LOGGER.info("Pausing execution to load save state...");
        final ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, Files.size(state));
        Hardware.load(buf);

        for(int i = 0; i < scriptStatePtrArr_800bc1c0.length(); i++) {
          final Pointer<ScriptState<?>> ptr = scriptStatePtrArr_800bc1c0.get(i);

          final String clsName = IoHelper.readString(buf);

          if(!clsName.isEmpty()) {
            final Class<?> cls = Class.forName(clsName);
            final Constructor<?> ctor = cls.getConstructor(Value.class);
            ptr.set(ScriptState.of(ref -> {
              try {
                return (MemoryRef)ctor.newInstance(ref);
              } catch(final Exception e) {
                throw new RuntimeException(e);
              }
            }).apply(MEMORY.ref(4, ptr.getPointer())));

            if(EffectManagerData6c.class.equals(cls)) {
              final EffectManagerData6c data6c = ptr.deref().innerStruct_00.derefAs(EffectManagerData6c.class);

              final String clsName1 = IoHelper.readString(buf);

              if(!clsName1.isEmpty()) {
                final Class<?> cls1 = Class.forName(clsName1);
                final Constructor<?> ctor1 = cls1.getConstructor(Value.class);
                data6c._44.set((BttlScriptData6cSubBase1)ctor1.newInstance(MEMORY.ref(4, data6c._44.getPointer())));
              }

              Pointer<BttlScriptData6cSubBase2> ptr2 = data6c._58;
              String clsName2 = IoHelper.readString(buf);

              while(!clsName2.isEmpty()) {
                final Class<?> cls2 = Class.forName(clsName2);
                final Constructor<?> ctor2 = cls2.getConstructor(Value.class);
                ptr2.set((BttlScriptData6cSubBase2)ctor2.newInstance(MEMORY.ref(4, ptr2.getPointer())));

                ptr2 = ptr2.deref()._00;
                clsName2 = IoHelper.readString(buf);
              }
            }
          }
        }

        LOGGER.info("Load state complete");
      } catch(final Throwable throwable) {
        LOGGER.error("Failed to load save state", throwable);
      }

      loading = false;
    }
  }

  @Method(0x80011ec8L)
  public static void executeLoadersAndScripts() {
    if(loadSstrmAndSmap() != 0) {
      callback_8004dbc0.get((int)mainCallbackIndex_8004dd20.get()).callback_00.deref().run();

      tickScripts();
      renderScriptObjects();
    }
  }

  @Method(0x80011f6cL)
  public static void FUN_80011f6c() {
    //LAB_80011f88
    for(int i = 0; i < 16; i++) {
      if(_8005a1ea.offset(i * 0xcL).getSigned() != 0) {
        _8005a1ea.offset(i * 0xcL).subu(0x1L);

        if(_8005a1ea.offset(i * 0xcL).get() == 0) {
          if(_8005a1e4.offset(i * 0xcL).get() == 0) {
            //LAB_80011fdc
            free(_8005a1e0.offset(i * 0xcL).get());
          } else {
            FUN_80012444(_8005a1e0.offset(i * 0xcL).get(), _8005a1e4.offset(i * 0xcL).get());
          }
        }
      }

      //LAB_80011ffc
      //LAB_80012000
    }
  }

  @Method(0x80012094L)
  public static void allocateHeap(long address, long size) {
    LOGGER.info("Allocating memory manager at %08x (0x%x bytes)", address, size);

    size = size - 0x18L & 0xffff_fffcL;
    address = address + 0x3L & 0xffff_fffcL;

    MEMORY.ref(4, address).offset(0x00L).setu(0);
    MEMORY.ref(4, address).offset(0x04L).setu(0xcL);
    MEMORY.ref(2, address).offset(0x08L).setu(0x3L);
    MEMORY.ref(4, address).offset(0x0cL).setu(address);
    MEMORY.ref(4, address).offset(0x10L).setu(size);
    MEMORY.ref(2, address).offset(0x14L).setu(0);
    MEMORY.ref(4, address).offset(size).offset(0x0cL).setu(address).addu(0xcL);
    MEMORY.ref(4, address).offset(size).offset(0x10L).setu(0);
    MEMORY.ref(2, address).offset(size).offset(0x14L).setu(0x3L);

    heapHead_8005a2a0.setu(address).addu(0xcL);
    heapTail_8005a2a4.setu(address).addu(0xcL).addu(size);
  }

  private static int allocations;

  static {
    Hardware.registerLoadStateListener(Scus94491BpeSegment::recalculateHeap);
  }

  public static void recalculateHeap() {
    allocations = 0;

    Value entry = heapHead_8005a2a0.deref(4);
    long entryType = entry.offset(2, 0x8L).get();

    do {
      final long size = entry.offset(4, 0x4L).get();

      if(entryType != 0) {
        allocations++;
      }

      entry = entry.offset(size);
      entryType = entry.offset(2, 0x8L).get();
    } while(entryType != 3);

    LOGGER.info("Recalculated allocations: %d", allocations);
  }

  public static void dumpHeap() {
    Value entry = heapHead_8005a2a0.deref(4);
    long entryType = entry.offset(2, 0x8L).get();

    do {
      final long size = entry.offset(4, 0x4L).get();

      final String type = switch((int)entryType) {
        case 0 -> "free space";
        case 1 -> "allocated on head";
        case 2 -> "allocated on tail";
        case 3 -> "end of list";
        default -> "unknown type " + entryType;
      };

      LOGGER.error("%08x (0x%x bytes), %s", entry.getAddress() + 0xcL, size, type);

      entry = entry.offset(size);
      entryType = entry.offset(2, 0x8L).get();
    } while(entryType != 3);
  }

  @Method(0x800120f0L)
  public static long mallocHead(long size) {
    size = size + 0xfL & 0xffff_fffcL;

    long currentEntry = heapHead_8005a2a0.get();
    long entryType = MEMORY.ref(2, currentEntry).offset(0x8L).get();

    //LAB_80012120
    while(entryType != 0x3L) {
      final long spaceAvailable = MEMORY.ref(4, currentEntry).offset(0x4L).get();

      if(entryType == 0) {
        if(spaceAvailable >= size) {
          MEMORY.ref(2, currentEntry).offset(0x8L).setu(0x1L);

          if(size + 0xcL < spaceAvailable) {
            MEMORY.ref(2, currentEntry).offset(size).offset(0x8L).setu(0);
            MEMORY.ref(4, currentEntry).offset(0x4L).setu(size);
            MEMORY.ref(4, currentEntry).offset(size).offset(0x0L).setu(currentEntry);
            MEMORY.ref(4, currentEntry).offset(size).offset(0x4L).setu(spaceAvailable - size);
            MEMORY.ref(4, currentEntry).offset(spaceAvailable).setu(currentEntry + size);
          }

          allocations++;
          LOGGER.info("Allocating 0x%x bytes on head @ %08x (%d total)", size, currentEntry + 0xcL, allocations);
          return currentEntry + 0xcL;
        }
      }

      //LAB_80012170
      currentEntry += MEMORY.ref(4, currentEntry).offset(0x4L).get();
      entryType = MEMORY.ref(2, currentEntry).offset(0x8L).get();
    }

    //LAB_8001218c
    dumpHeap();
    throw new RuntimeException("Failed to allocate entry on linked list (size 0x" + Long.toHexString(size) + ')');
  }

  @Method(0x80012194L)
  public static long mallocTail(long size) {
    size = size + 0xfL & 0xffff_fffcL;

    Value currentEntry = heapTail_8005a2a4;
    Value nextEntry = heapTail_8005a2a4.deref(4);
    long entryType = nextEntry.deref(2).offset(0x8L).get();
    // Known entry types:
    // 0: empty space?
    // 2: used?
    // 3: end of list?

    //LAB_800121cc
    while(entryType != 0x3L) {
      final long spaceAvailable = nextEntry.deref(4).offset(0x4L).get();
      if(entryType == 0 && spaceAvailable >= size) {
        if(spaceAvailable > size + 0xcL) {
          currentEntry.deref(2).offset(-size).offset(0x8L).setu(0x2L); // Mark as used
          nextEntry.deref(2).offset(0x8L).setu(0); // Mark as empty space
          nextEntry.deref(4).offset(0x4L).setu(spaceAvailable - size);
          nextEntry.deref(4).offset(-size).offset(spaceAvailable).setu(nextEntry);
          currentEntry.deref(4).offset(-size).offset(0x4L).setu(size);
          currentEntry.deref(4).setu(currentEntry.get() - size);

          allocations++;
          LOGGER.info("Allocating 0x%x bytes on tail @ %08x (%d total)", size, currentEntry.get() - size + 0xcL, allocations);
          return currentEntry.get() - size + 0xcL;
        }

        //LAB_80012214
        nextEntry.deref(2).offset(0x8L).setu(0x2L); // Mark as used

        allocations++;
        LOGGER.info("Allocating 0x%x bytes on tail @ %08x (%d total)", size, nextEntry.get() + 0xcL, allocations);
        return nextEntry.get() + 0xcL;
      }

      //LAB_80012220
      currentEntry = nextEntry;
      nextEntry = nextEntry.deref(4);
      entryType = nextEntry.deref(2).offset(0x8L).get();
    }

    //LAB_8001223c
    dumpHeap();
    throw new RuntimeException("Failed to allocate entry on linked list (size 0x" + Long.toHexString(size) + ')');
  }

  @Method(0x80012244L)
  public static long FUN_80012244(final long address, long size) {
    if(address == 0) {
      return 0;
    }

    long s1 = address - 0xcL;
    size += 0xfL;
    size &= 0xffff_fffcL;
    long t1 = MEMORY.ref(4, s1).offset(0x4L).get();
    long v1 = MEMORY.ref(4, s1).get();
    final long s2;
    final long s0;
    if(MEMORY.ref(2, v1).offset(0x8L).get() == 0) {
      //LAB_800122a0
      s0 = v1;
      s2 = Math.min(size, t1);
      t1 += MEMORY.ref(4, v1).offset(0x4L).get();
    } else {
      //LAB_800122b0
      s0 = s1;
      s2 = 0;
    }

    //LAB_800122b4
    v1 = s1 + MEMORY.ref(4, s1).offset(0x4L).get();
    if(MEMORY.ref(2, v1).offset(0x8L).get() == 0) {
      t1 += MEMORY.ref(4, v1).offset(0x4L).get();
    }

    //LAB_800122e0
    final long t2 = t1 - size;
    if(t1 >= size) {
      if(s2 != 0) {
        long t0 = s0 + 0xcL;
        long a3 = s1 + 0xcL;
        long a2 = (s2 - 0xcL) / 4;
        if(t0 >= a3) {
          //LAB_8001233c
          if(s1 < s0) {
            //LAB_80012358
            while(a2-- > 0) {
              MEMORY.ref(4, t0).offset(a2 * 4).setu(MEMORY.ref(4, a3).offset(a2 * 4));
            }
          }
        } else {
          //LAB_80012314
          while(a2-- > 0) {
            MEMORY.ref(4, t0).setu(MEMORY.ref(4, a3));
            a3 += 0x4L;
            t0 += 0x4L;
          }
        }

        //LAB_8001237c
      }

      //LAB_80012380
      MEMORY.ref(2, s0).offset(0x8L).setu(0x1L);

      if(t2 >= 0xcL) {
        MEMORY.ref(2, s0).offset(size).offset(0x8L).setu(0);
        MEMORY.ref(4, s0).offset(0x4L).setu(size);
        MEMORY.ref(4, s0).offset(size).setu(s0);
        MEMORY.ref(4, s0).offset(size).offset(0x4L).setu(t2);
        MEMORY.ref(4, s0).offset(size).offset(t2).setu(s0 + size);
      } else {
        //LAB_800123b0
        MEMORY.ref(4, s0).offset(0x4L).setu(t1);
        MEMORY.ref(4, s0).offset(t1).setu(s0);
      }

      //LAB_800123b8
      return s0 + 0xcL;
    }

    //LAB_800123c0
    final long dataAddress = mallocHead(size - 0xcL);
    if(dataAddress == 0) {
      //LAB_800123dc
      return 0;
    }

    s1 += 0xcL;

    //LAB_800123e4
    v1 = s1;

    //LAB_800123f8
    for(int i = 0; i < (s2 - 0xcL) / 4; i++) {
      MEMORY.ref(4, dataAddress).offset(i * 4L).setu(MEMORY.ref(4, v1));
      v1 += 0x4L;

      //LAB_8001240c
    }

    free(s1);

    //LAB_8001242c
    return dataAddress;
  }

  @Method(0x80012444L)
  public static long FUN_80012444(final long address, final long size) {
    long v0;
    long v1;
    long a0 = address;
    long a1 = size;
    long a2;
    long a3;
    final long t0;
    final long t1;
    long s0;
    long s1;
    long s2;
    long s3;
    final long s4;
    s1 = a0;
    if(s1 == 0) {
      return 0;
    }
    s2 = a1;
    s1 = s1 - 0xcL;
    s2 = s2 + 0xfL;
    v0 = -0x4L;
    s2 = s2 & v0;
    s3 = MEMORY.ref(4, s1).offset(0x4L).get();

    if(s2 < s3) {
      v1 = s2;
    } else {
      v1 = s3;
    }

    //LAB_80012494
    s4 = v1;
    a0 = s2 - 0xcL;
    s0 = mallocTail(a0);
    if(s0 != 0) {
      s0 = s0 - 0xcL;
      a1 = s0 + 0xcL;
      if(s1 < s0) {
        a0 = s1 + 0xcL;
        v1 = s4 - 0xcL;
        v1 = v1 >>> 2;
        v1--;

        //LAB_800124d0
        while((int)v1 >= 0) {
          v0 = MEMORY.ref(4, a0).offset(0x0L).get();
          MEMORY.ref(4, a1).offset(0x0L).setu(v0);
          a0 = a0 + 0x4L;
          a1 = a1 + 0x4L;

          //LAB_800124e4
          v1 = v1 - 0x1L;
        }

        a0 = s1 + 0xcL;
        free(a0);

        //LAB_80012740
        return s0 + 0xcL;
      }

      //LAB_800124f8
      a0 = s0 + 0xcL;
      a1 = 0;
      a2 = a1;
      free(a0);
    }

    //LAB_80012508
    v1 = MEMORY.ref(4, s1).offset(0x0L).get();

    v0 = MEMORY.ref(2, v1).offset(0x8L).get();

    if(v0 != 0) {
      s0 = s1;
    } else {
      s0 = v1;
      v0 = MEMORY.ref(4, s0).offset(0x4L).get();
      s3 = s3 + v0;
    }

    //LAB_80012530
    v0 = MEMORY.ref(4, s1).offset(0x4L).get();

    v1 = s1 + v0;
    v0 = MEMORY.ref(2, v1).offset(0x8L).get();

    if(v0 == 0) {
      v0 = MEMORY.ref(4, v1).offset(0x4L).get();
      s3 = s3 + v0;
    }

    //LAB_8001255c
    if(s3 < s2) {
      t1 = s3 - s2;
      //LAB_800126d8
      a0 = s2 - 0xcL;
      a1 = 0;
      a2 = a1;
      v0 = mallocTail(a0);
      s0 = v0;
      if(s0 == 0) {
        //LAB_800126f4
        return 0;
      }

      s0 = s0 - 0xcL;
      //LAB_800126fc
      a1 = s0 + 0xcL;
      v1 = s1 + 0xcL;
      a0 = s4 - 0xcL;
      a0 = a0 >>> 2;
      a0--;

      //LAB_80012710
      while((int)a0 >= 0) {
        v0 = MEMORY.ref(4, v1).offset(0x0L).get();
        MEMORY.ref(4, a1).offset(0x0L).setu(v0);
        v1 = v1 + 0x4L;
        a1 = a1 + 0x4L;

        //LAB_80012724
        a0 = a0 - 0x1L;
      }

      a0 = s1 + 0xcL;

      //LAB_80012734
      a1 = 0;
      a2 = a1;
      free(a0);

      //LAB_80012740
      v0 = s0 + 0xcL;
    } else {
      t1 = s3 - s2;
      t0 = s0 + t1;
      if(t1 >= 0xcL) {
        if(t0 == s1) {
          a3 = t0 + 0xcL;
        } else {
          a3 = t0 + 0xcL;
          a2 = s1 + 0xcL;
          a1 = s4 - 0xcL;
          if(a3 >= a2) {
            a1 = a1 >>> 2;
            //LAB_800125c0
            v1 = t0 + 0xcL;
            v0 = s1 + 0xcL;
            if(v0 < v1) {
              v0 = a1;
              if((int)v0 <= 0) {
                a1 = a1 - 0x1L;
              } else {
                a1 = a1 - 0x1L;

                //LAB_800125e0
                do {
                  a0 = a1;
                  v0 = a1 << 2;
                  v1 = v0 + a3;
                  v0 = v0 + a2;
                  v0 = MEMORY.ref(4, v0).offset(0x0L).get();

                  MEMORY.ref(4, v1).offset(0x0L).setu(v0);
                  a1 = a1 - 0x1L;
                } while((int)a0 > 0);
              }
            }
          } else {
            a1 = a1 >>> 2;
            v0 = a1;
            a1 = a1 - 0x1L;
            if((int)v0 > 0) {
              //LAB_80012598
              do {
                v0 = MEMORY.ref(4, a2).offset(0x0L).get();
                MEMORY.ref(4, a3).offset(0x0L).setu(v0);
                a2 = a2 + 0x4L;
                a3 = a3 + 0x4L;
                v0 = a1;
                a1 = a1 - 0x1L;
              } while((int)v0 > 0);
            }
          }
        }

        //LAB_80012604
        MEMORY.ref(2, s0).offset(0x8L).setu(0);

        //LAB_80012608
        v0 = 0x2L;
        MEMORY.ref(2, t0).offset(0x8L).setu(v0);
        MEMORY.ref(4, s0).offset(0x4L).setu(t1);
        v0 = s0 + t1;
        MEMORY.ref(4, v0).offset(0x0L).setu(s0);
        MEMORY.ref(4, t0).offset(0x4L).setu(s2);
        v0 = t0 + s2;
        MEMORY.ref(4, v0).offset(0x0L).setu(t0);
        v0 = t0 + 0xcL;
      } else {
        //LAB_80012630
        if(s0 == s1) {
          a3 = s0 + 0xcL;
        } else {
          a3 = s0 + 0xcL;
          a2 = s1 + 0xcL;
          a1 = s4 - 0xcL;
          if(a3 >= a2) {
            a1 = a1 >>> 2;

            //LAB_80012680
            v1 = s0 + 0xcL;
            v0 = s1 + 0xcL;
            if(v0 >= v1) {
              v0 = a1;
            } else {
              v0 = a1;
              if((int)v0 <= 0) {
                a1 = a1 - 0x1L;
              } else {
                a1 = a1 - 0x1L;

                //LAB_8001269c
                do {
                  a0 = a1;
                  v0 = a1 << 2;
                  v1 = v0 + a3;
                  v0 = v0 + a2;
                  v0 = MEMORY.ref(4, v0).offset(0x0L).get();
                  MEMORY.ref(4, v1).offset(0x0L).setu(v0);
                  a1 = a1 - 0x1L;
                } while((int)a0 > 0);
              }
            }
          } else {
            a1 = a1 >>> 2;
            v0 = a1;
            a1 = a1 - 0x1L;
            if((int)v0 > 0) {
              //LAB_80012658
              do {
                v0 = MEMORY.ref(4, a2).offset(0x0L).get();
                MEMORY.ref(4, a3).offset(0x0L).setu(v0);
                a2 = a2 + 0x4L;
                a3 = a3 + 0x4L;
                v0 = a1;
                a1 = a1 - 0x1L;
              } while((int)v0 > 0);
            }
          }
        }

        //LAB_800126c0
        v0 = 0x2L;

        //LAB_800126c4
        MEMORY.ref(2, s0).offset(0x8L).setu(v0);
        MEMORY.ref(4, s0).offset(0x4L).setu(s3);
        v0 = s0 + s3;
        MEMORY.ref(4, v0).offset(0x0L).setu(s0);
        v0 = s0 + 0xcL;
      }
    }

    //LAB_80012744
    return v0;
  }

  @Method(0x80012764L)
  public static void free(long address) {
    allocations--;
    LOGGER.info("Deallocating %08x (%d remaining)", address, allocations);

    if(allocations < 0) {
      LOGGER.error("Negative allocations!", new Throwable());
    }

    address -= 0xcL;
    long a1 = MEMORY.ref(4, address).offset(0x4L).get(); // Remaining size?
    MEMORY.ref(2, address).offset(0x8L).setu(0); // Entry type?

    if(MEMORY.ref(2, address).offset(a1).offset(0x8L).get() == 0) {
      a1 += MEMORY.ref(4, address).offset(a1).offset(0x4L).get();
    }

    //LAB_80012794
    final long v1 = MEMORY.ref(4, address).get();
    if(MEMORY.ref(2, v1).offset(0x8L).get() == 0) {
      a1 += MEMORY.ref(4, v1).offset(0x4L).get();
      address = v1;
    }

    //LAB_800127bc
    MEMORY.ref(4, address).offset(0x4L).setu(a1);
    MEMORY.ref(4, address).offset(a1).setu(address);
  }

  @Method(0x800127ccL)
  public static long FUN_800127cc(final long address, final long a1, long a2) {
    _8004dd00.addu(0x1L);

    if(_8004dd00.get() >= 0x10L) {
      _8004dd00.setu(0);
    }

    //LAB_800127f0
    long a3 = _8004dd00.get();
    long v1 = _8005a1e0.offset(a3 * 0xcL).getAddress();

    //LAB_8001281c
    for(; a3 < 0x10L; a3++) {
      if(MEMORY.ref(4, v1).offset(0x4L).get() == 0) {
        //LAB_80012888
        if((int)a2 <= 0) {
          a2 = 0x1L;
        }

        //LAB_80012894
        MEMORY.ref(4, v1).offset(0x0L).setu(address);
        MEMORY.ref(4, v1).offset(0x4L).setu(a1);
        MEMORY.ref(2, v1).offset(0xaL).setu(a2);
        return a3;
      }

      v1 = v1 + 0xcL;
    }

    //LAB_80012840
    v1 = _8005a1e0.getAddress();

    //LAB_80012860
    for(a3 = 0; a3 < _8004dd00.get(); a3++) {
      if(MEMORY.ref(4, v1).offset(0x4L).get() == 0) {
        //LAB_80012888
        if((int)a2 <= 0) {
          a2 = 0x1L;
        }

        //LAB_80012894
        MEMORY.ref(4, v1).offset(0x0L).setu(address);
        MEMORY.ref(4, v1).offset(0x4L).setu(a1);
        MEMORY.ref(2, v1).offset(0xaL).setu(a2);
        return a3;
      }

      v1 = v1 + 0xcL;
    }

    //LAB_80012880
    return -0x1L;
  }

  @Method(0x800128a8L)
  public static long FUN_800128a8(final long a0) {
    if(a0 != 0) {
      return MEMORY.ref(4, a0).offset(-0x8L).get() - 0xcL;
    }

    //LAB_800128bc
    return 0;
  }

  @Method(0x800128c4L)
  public static long loadSstrmAndSmap() {
    if(!loadingOverlay_8004dd1e.get()) {
      //LAB_80012910
      //LAB_80012900
      while(overlayQueueIndex_8004dd14.get() != overlayQueueIndex_8004dd18.get() && loadingOverlays_8005a2a8.get(overlayQueueIndex_8004dd18.get()).loadedCallback_04.isNull()) {
        overlayQueueIndex_8004dd18.incr().and(0xf);
        //LAB_80012910
      }

      //LAB_80012930
      if(!loadingOverlay_8004dd1e.get()) {
        if(overlaysLoadedCount_8004dd1c.get() == 0) {
          if(overlayQueueIndex_8004dd14.get() != overlayQueueIndex_8004dd18.get()) {
            loadingOverlay_8004dd1e.set(true);
            loadedOverlayIndex_8004dd10.set(loadingOverlays_8005a2a8.get(overlayQueueIndex_8004dd18.get()).overlayIndex_00.get());
            loadFile(overlays_8004db88.get(loadedOverlayIndex_8004dd10.get()), _80010004.get(), getMethodAddress(Scus94491BpeSegment.class, "overlayLoadedCallback", long.class, long.class, long.class), overlayQueueIndex_8004dd18.get(), 0x11L);
            overlayQueueIndex_8004dd18.incr().and(0xf);
          }
        }
      }
    }

    //LAB_800129c0
    //LAB_800129c4
    if(mainCallbackIndexOnceLoaded_8004dd24.getSigned() != -1) {
      if(loadingGameStateOverlay_8004dd08.get() != 0) {
        return 0;
      }

      pregameLoadingStage_800bb10c.setu(0);
      previousMainCallbackIndex_8004dd28.setu(mainCallbackIndex_8004dd20);
      mainCallbackIndex_8004dd20.set(mainCallbackIndexOnceLoaded_8004dd24);
      mainCallbackIndexOnceLoaded_8004dd24.setu(-1);
      FUN_80019710();
      vsyncMode_8007a3b8.setu(0x2L);
      loadGameStateOverlay((int)mainCallbackIndex_8004dd20.getSigned());

      if(mainCallbackIndex_8004dd20.get() == 0x6L) { // Starting combat
        FUN_8001c4ec();
      }
    }

    //LAB_80012a34
    //LAB_80012a38
    if(loadingGameStateOverlay_8004dd08.get() == 0 || (callback_8004dbc0.get((int)mainCallbackIndex_8004dd20.get()).uint_0c.get() & 0xff00L) != 0) {
      //LAB_80012a6c
      return 1;
    }

    //LAB_80012a70
    return 0;
  }

  @Method(0x80012a84L)
  public static long loadGameStateOverlay(final int callbackIndex) {
    final FileEntry08 entry = callback_8004dbc0.get(callbackIndex).entry_04.derefNullable();

    if(entry == null || entry.getAddress() == currentlyLoadingFileEntry_8004dd04.getPointer()) {
      //LAB_80012ac0
      FUN_80012bd4(callbackIndex);
      loadingGameStateOverlay_8004dd08.setu(0);
      return 0x1L;
    }

    //LAB_80012ad8
    currentlyLoadingFileEntry_8004dd04.set(entry);
    loadingGameStateOverlay_8004dd08.setu(0x1L);
    loadFile(entry, _80010000.get(), getMethodAddress(Scus94491BpeSegment.class, "gameStateOverlayLoadedCallback", long.class, long.class, long.class), callbackIndex, 0x11L);
    return 0;
  }

  /**
   * Supporting overlays that can be loaded at any time like S_ITEM, etc.
   *
   * @param overlayIndex <ol start="0">
   *                       <li>S_INIT</li>
   *                       <li>S_BTLD</li>
   *                       <li>S_ITEM</li>
   *                       <li>S_EFFE</li>
   *                       <li>S_STRM</li>
   *                     </ol>
   */
  @Method(0x80012b1cL)
  public static void loadSupportOverlay(final int overlayIndex, final ConsumerRef<Integer> overlayMethod, final int callbackParam) {
    if(loadedOverlayIndex_8004dd10.get() == overlayIndex && !loadingOverlay_8004dd1e.get()) {
      overlaysLoadedCount_8004dd1c.incr();
      overlayMethod.run(callbackParam);
      return;
    }

    //LAB_80012b6c
    //LAB_80012b70
    final LoadingOverlay0c loadingOverlay = loadingOverlays_8005a2a8.get(overlayQueueIndex_8004dd14.get());
    loadingOverlay.overlayIndex_00.set(overlayIndex);
    loadingOverlay.loadedCallback_04.set(overlayMethod);
    loadingOverlay.callbackParam_08.set(callbackParam);
    overlayQueueIndex_8004dd14.incr().and(0xf);

    //LAB_80012ba4
  }

  @Method(0x80012bb4L)
  public static void decrementOverlayCount() {
    if(overlaysLoadedCount_8004dd1c.get() > 0) {
      overlaysLoadedCount_8004dd1c.decr();
    }
  }

  @Method(0x80012bd4L)
  public static void FUN_80012bd4(final int callbackIndex) {
    if(_8004dd0c.get() != 0) {
      _8004dd0c.setu(0);
      return;
    }

    //LAB_80012bf0
    final long v0 = callback_8004dbc0.get(callbackIndex).ptr_08.get();
    if(v0 != 0) {
      bzero(MEMORY.ref(4, v0).offset(0x0L).get(), (int)MEMORY.ref(4, v0).offset(0x4L).get());
    }
  }

  @Method(0x80012c48L)
  public static void gameStateOverlayLoadedCallback(final long address, final long fileSize, final long callbackIndex) {
    loadingGameStateOverlay_8004dd08.setu(0);
  }

  @Method(0x80012c54L)
  public static void overlayLoadedCallback(final long address, final long fileSize, final long overlayQueueIndex) {
    loadingOverlay_8004dd1e.set(false);
    removeLoadedOverlayFromQueue((int)overlayQueueIndex);
  }

  @Method(0x80012c7cL)
  public static void removeLoadedOverlayFromQueue(final int startingIndex) {
    //LAB_80012cd0
    for(int i = startingIndex; i != overlayQueueIndex_8004dd14.get(); i = i + 1 & 0xf) {
      final LoadingOverlay0c loadingOverlay = loadingOverlays_8005a2a8.get(i);

      if(loadedOverlayIndex_8004dd10.get() == loadingOverlay.overlayIndex_00.get()) {
        if(!loadingOverlay.loadedCallback_04.isNull()) {
          overlaysLoadedCount_8004dd1c.incr();
          loadingOverlay.loadedCallback_04.deref().run(loadingOverlay.callbackParam_08.get());
        }

        //LAB_80012d14
        loadingOverlay.overlayIndex_00.set(-1);
        loadingOverlay.loadedCallback_04.clear();
      }
    }

    //LAB_80012d30
  }

  @Method(0x80012d58L)
  public static void startFrame() {
    doubleBufferFrame_800bb108.set(PSDIDX_800c34d4.get());

    tags_1f8003d0.set(orderingTableTags_8005a398.get(PSDIDX_800c34d4.get()));
    gpuPacketAddr_1f8003d8.setu(_8007a3c0.offset(PSDIDX_800c34d4.get() * 0x20400L).getAddress());
  }

  @Method(0x80012df8L)
  public static void endFrame() {
    if(renderFlags_8004dd36.get(0x2L) == 0) { // Height: 240
      GPU.queueCommand(3, new GpuCommandSetMaskBit(false, Gpu.DRAW_PIXELS.ALWAYS));
      GPU.queueCommand(orderingTableSize_1f8003c8.get() - 1, new GpuCommandSetMaskBit(true, Gpu.DRAW_PIXELS.ALWAYS));
    }

    //LAB_80012e8c
    syncFrame_8004dd3c.deref().run();
    swapDisplayBuffer_8004dd40.deref().run();
  }

  @Method(0x80012eccL)
  public static void syncFrame() {
    if(renderFlags_8004dd36.get(0x2L) == 0) { // Height: 240
      //LAB_80012efc
      VSync((int)vsyncMode_8007a3b8.getSigned());
    } else {
      VSync(0);
      FUN_8003b0d0();
    }

    //LAB_80012f14
  }

  /**
   * {@link Scus94491BpeSegment_8004#syncFrame_8004dd3c} is changed to this for one frame end then switched back when the graphics mode changes
   */
  @Method(0x80012f24L)
  public static void syncFrame_reinit() {
    final long flags;
    if(renderFlags_8004dd36.get(0x3L) == 0) { // Height: 240, not interlaced
      flags = 0b110100L;
    } else {
      flags = 0b110101L;
    }

    //LAB_80012f5c
    final long use24BitColour = renderFlags_8004dd36.get() >>> 0x2L & 0x1L;
    final long height480 = renderFlags_8004dd36.get() & 0x2L;
    final int orderingTableBits = reinitOrderingTableBits_8004dd38.get();

    _800babc0.setu(0);
    _800bb104.setu(0);
    _8007a3a8.setu(0);

    final RECT rect1 = new RECT((short)0, (short)16, (short)width_8004dd34.get(), (short)240);
    final RECT rect2 = new RECT((short)0, (short)256, (short)width_8004dd34.get(), (short)240);

    orderingTables_8005a370.get(0).length_00.set(orderingTableBits);
    orderingTables_8005a370.get(1).length_00.set(orderingTableBits);

    orderingTableBits_1f8003c0.set(orderingTableBits);
    zShift_1f8003c4.set(14 - orderingTableBits);
    orderingTableSize_1f8003c8.set(1 << orderingTableBits);
    zMax_1f8003cc.set((1 << orderingTableBits) - 2);

    VSync(0);
    SetDispMask(0);
    VSync(0);

    final int displayHeight;
    if(height480 == 0) {
      //LAB_80013040
      GsDefDispBuff((short)0, (short)16, (short)0, (short)256);
      displayHeight = 240;
    } else {
      GsDefDispBuff((short)0, (short)16, (short)0, (short)16);
      displayHeight = 480;
    }

    //LAB_80013060
    GsInitGraph((short)width_8004dd34.get(), (short)displayHeight, (short)flags, true, use24BitColour != 0);

    if(width_8004dd34.get() == 384L) {
      DISPENV_800c34b0.screen.x.set((short)9);
    }

    //LAB_80013080
    ClearImage(rect1, (byte)0, (byte)0, (byte)0);
    ClearImage(rect2, (byte)0, (byte)0, (byte)0);
    FUN_8003c5e0();
    setProjectionPlaneDistance(320);

    VSync(0);
    SetDispMask(1);

    syncFrame_8004dd3c.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment.class, "syncFrame")).cast(RunnableRef::new));

    if(use24BitColour == 0) {
      swapDisplayBuffer_8004dd40.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment.class, "swapDisplayBuffer_24bpp")).cast(RunnableRef::new));
    } else {
      swapDisplayBuffer_8004dd40.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment.class, "swapDisplayBuffer_15bpp")).cast(RunnableRef::new));
    }
  }

  @Method(0x80013148L)
  public static void swapDisplayBuffer_24bpp() {
    GsSwapDispBuff();

    if(renderFlags_8004dd36.get(0x2L) == 0) { // Height: 240
      GsSortClear((int)_8007a3a8.get(), (int)_800bb104.get(), (int)_800babc0.get());
    }
  }

  @Method(0x800131e0L)
  public static void swapDisplayBuffer_15bpp() {
    GsSwapDispBuff();
  }

  @Method(0x80013200L)
  public static void setWidthAndFlags(final int width, final int flags) {
    if(width != displayWidth_1f8003e0.get() || flags != renderFlags_8004dd36.get()) {
      // Change the syncFrame callback to the reinitializer for a frame to reinitialize everything with the new size/flags
      syncFrame_8004dd3c.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment.class, "syncFrame_reinit")).cast(RunnableRef::new));
      width_8004dd34.setu(width);
      renderFlags_8004dd36.setu(flags);
    }
  }

  @Method(0x8001324cL)
  public static void setDepthResolution(final int orderingTableBits) {
    if(orderingTableBits_1f8003c0.get() != orderingTableBits) {
      syncFrame_8004dd3c.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment.class, "syncFrame_reinit")).cast(RunnableRef::new));
      reinitOrderingTableBits_8004dd38.set(orderingTableBits);
    }

    //LAB_80013274
  }

  @Method(0x800133acL)
  public static int simpleRand() {
    final long v1;
    if(simpleRandSeed_8004dd44.get(0xfffL) == 0) {
      v1 = VSync(-1) * 9; // If seed is 0, seed with vblanks
    } else {
      v1 = simpleRandSeed_8004dd44.get();
    }

    //LAB_800133dc
    simpleRandSeed_8004dd44.setu(v1 * 9 + 0x3711);
    return (int)(simpleRandSeed_8004dd44.get() / 2 & 0xffff);
  }

  @Method(0x80013404L)
  public static int FUN_80013404(final long a0, final long a1, final long a2) {
    long v0;
    long v1;
    v1 = (long)(int)a0 * (int)a2 & 0xffff_ffffL;
    v0 = 0x1L - a2;
    v1 = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
    v0 = v1 + a1 * 0x2L;

    if(a2 > 0) {
      v0 = (int)v0 / (int)a2;
    } else {
      v0 = (int)v0 >= 0 ? -1 : 1;
    }

    return (int)v0 >> 1;
  }

  @Method(0x80013434L)
  public static <T extends MemoryRef> void qsort(final ArrayRef<T> data, final int count, final int stride, final BiFunctionRef<T, T, Long> comparator) {
    //LAB_8001347c
    // Find the first Mersenne number >= count
    int s4 = 0;
    while(s4 * 2 < count) {
      s4 += s4 + 1;
    }

    //LAB_80013490
    //LAB_800134a4
    while(s4 > 0) {
      final int s5 = s4;
      int s3 = s4;

      //LAB_800134b8
      while(s3 < count) {
        int s2 = s3 - s5;

        //LAB_800134c4
        while(s2 >= 0) {
          final T s0 = data.get(s2);
          final T s1 = data.get(s2 + s5);

          if(comparator.run(s0, s1) <= 0) {
            break;
          }

          //LAB_80013500
          // Swap values
          for(int i = 0; i < stride; i += 4) {
            final long v1 = MEMORY.get(s0.getAddress() + i, 4);
            final long v0 = MEMORY.get(s1.getAddress() + i, 4);
            MEMORY.set(s0.getAddress() + i, 4, v0);
            MEMORY.set(s1.getAddress() + i, 4, v1);
          }

          //LAB_80013524
          s2 -= s5;
        }

        //LAB_80013530
        s3++;
      }

      //LAB_80013540
      s4 >>= 1;
    }

    //LAB_8001354c
  }

  @Method(0x8001357cL)
  public static void queueGpuPacket(final long previousElement, final long newElement) {
    throw new RuntimeException("NOPE");
//    MEMORY.ref(3, newElement).setu(MEMORY.ref(3, previousElement));
//    MEMORY.ref(3, previousElement).setu(newElement);
  }

  @Method(0x80013598L)
  public static short rsin(final long angleDiv360Maybe) {
    return (short)sin_cos_80054d0c.offset(2, 0x0L).offset((angleDiv360Maybe & 0xfffL) * 4).getSigned();
  }

  @Method(0x800135b8L)
  public static short rcos(final long angleDiv360Maybe) {
    return (short)sin_cos_80054d0c.offset(2, 0x2L).offset((angleDiv360Maybe & 0xfffL) * 4).getSigned();
  }

  /**
   * 800135d8
   *
   * Copies the first n bytes of src to dest.
   *
   * @param dest Pointer to copy destination memory block
   * @param src Pointer to copy source memory block
   * @param length Number of bytes copied
   *
   * @return Pointer to destination (dest)
   */
  @Method(0x800135d8L)
  public static long memcpy(final long dest, final long src, final int length) {
    MEMORY.memcpy(dest, src, length);
    return dest;
  }

  @Method(0x80013658L)
  public static void fillMemory(final long address, final long fill, final long size) {
    // Fill in 4-byte segments if possible
    if((address & 0x3L) == 0 && (size & 0x3L) == 0) {
      for(int i = 0; i < size; i += 4) {
        MEMORY.set(address + i, 4, fill);
      }

      return;
    }

    for(int i = 0; i < size; i++) {
      MEMORY.set(address + i, (byte)fill);
    }
  }

  @Method(0x800136dcL)
  public static void scriptStartEffect(final long effectType, final long frames) {
    //LAB_800136f4
    scriptEffect_800bb140.type_00.set(effectType);
    scriptEffect_800bb140.totalFrames_08.set((int)frames > 0 ? frames : 0xfL);
    scriptEffect_800bb140.startTime_04.set(VSync(-1));

    if(_8004dd48.offset(effectType * 2).get() == 0x2L) {
      scriptEffect_800bb140.blue1_0c.set(0);
      scriptEffect_800bb140.green1_10.set(0);
      scriptEffect_800bb140.blue0_14.set(0);
      scriptEffect_800bb140.red1_18.set(0);
      scriptEffect_800bb140.green0_1c.set(0);
      scriptEffect_800bb140.red0_20.set(0);
    }

    scriptEffect_800bb140._24.set(_8004dd48.offset(effectType * 2).get());

    //LAB_80013768
  }

  /**
   * This handles the lightning flashes/darkening, the scene fade-in, etc.
   */
  @Method(0x80013778L)
  public static void FUN_80013778() {
    final long v1 = Math.min(scriptEffect_800bb140.totalFrames_08.get(), (VSync(-1) - scriptEffect_800bb140.startTime_04.get()) / 2);

    //LAB_800137d0
    final long colour;
    if(scriptEffect_800bb140.totalFrames_08.get() == 0) {
      colour = 0;
    } else {
      final long a1 = scriptEffect_800bb140._24.get();
      if(a1 == 0x1L) {
        //LAB_80013818
        colour = v1 * 255 / scriptEffect_800bb140.totalFrames_08.get();
      } else if((int)a1 < 0x2L) {
        if(a1 != 0) {
          scriptEffect_800bb140.type_00.set(0);
          scriptEffect_800bb140._24.set(0);
        }

        colour = 0;

        //LAB_80013808
      } else if(a1 != 0x2L) {
        scriptEffect_800bb140.type_00.set(0);
        scriptEffect_800bb140._24.set(0);
        colour = 0;
      } else { // a1 == 2
        //LAB_8001383c
        colour = v1 * 255 / scriptEffect_800bb140.totalFrames_08.get() ^ 0xffL;

        if(colour == 0) {
          //LAB_80013874
          scriptEffect_800bb140._24.set(0);
        }
      }
    }

    //LAB_80013880
    //LAB_80013884
    _800bb168.setu(colour);

    if(colour != 0) {
      //LAB_800138f0
      //LAB_80013948
      switch((int)scriptEffect_800bb140.type_00.get()) {
        case 1, 2 -> drawFullScreenRect(colour, Translucency.B_MINUS_F);
        case 3, 4 -> drawFullScreenRect(colour, Translucency.B_PLUS_F);

        case 5 -> {
          for(int s1 = 0; s1 < 8; s1++) {
            //LAB_800138f8
            for(int s0 = 0; s0 < 6; s0++) {
              FUN_80013d78(colour - (0xcL - (s0 + s1)) * 11, s1, s0);
            }
          }
        }

        case 6 -> {
          for(int s1 = 0; s1 < 8; s1++) {
            //LAB_80013950
            for(int s0 = 0; s0 < 6; s0++) {
              FUN_80013d78(colour - (s1 + s0) * 11, s1, s0);
            }
          }
        }
      }
    }

    //caseD_0
    //LAB_80013994

    // This causes the bright flash of light from the lightning, etc.
    if(scriptEffect_800bb140.red0_20.get() != 0 || scriptEffect_800bb140.green0_1c.get() != 0 || scriptEffect_800bb140.blue0_14.get() != 0) {
      //LAB_800139c4
      GPU.queueCommand(39, new GpuCommandQuad()
        .translucent(Translucency.B_PLUS_F)
        .rgb((int)scriptEffect_800bb140.red0_20.get(), (int)scriptEffect_800bb140.green0_1c.get(), (int)scriptEffect_800bb140.blue0_14.get())
        .pos(-centreScreenX_1f8003dc.get(), -centreScreenY_1f8003de.get(), displayWidth_1f8003e0.get() + 1, displayHeight_1f8003e4.get() + 1)
      );
    }

    //LAB_80013adc

    // This causes the screen darkening from the lightning, etc.
    if(scriptEffect_800bb140.red1_18.get() != 0 || scriptEffect_800bb140.green1_10.get() != 0 || scriptEffect_800bb140.blue1_0c.get() != 0) {
      //LAB_80013b10
      GPU.queueCommand(39, new GpuCommandQuad()
        .translucent(Translucency.B_MINUS_F)
        .rgb((int)scriptEffect_800bb140.red1_18.get(), (int)scriptEffect_800bb140.green1_10.get(), (int)scriptEffect_800bb140.blue1_0c.get())
        .pos(-centreScreenX_1f8003dc.get(), -centreScreenY_1f8003de.get(), displayWidth_1f8003e0.get() + 1, displayHeight_1f8003e4.get() + 1)
      );
    }

    //LAB_80013c20
  }

  @Method(0x80013c3cL)
  public static void drawFullScreenRect(final long colour, final Translucency transMode) {
    final GpuCommandUntexturedQuad packet = new GpuCommandUntexturedQuad();
    packet.translucent(transMode);
    packet.monochrome((int)colour);
    packet.pos(-centreScreenX_1f8003dc.get(), -centreScreenY_1f8003de.get(), displayWidth_1f8003e0.get() + 1, displayHeight_1f8003e4.get() + 1);
    GPU.queueCommand(30, packet);
  }

  @Method(0x80013d78L)
  public static void FUN_80013d78(final long a0, final long a1, final long a2) {
    assert false;
  }

  @Method(0x8001486cL)
  public static void decompressCurrentFile() {
    final FileLoadingInfo file = currentlyLoadingFileInfo_800bb468;

    if(!file.used.get()) {
      return;
    }

    if((file.type.get() & 1) == 0) {
      file.used.set(false);
      return;
    }

    LOGGER.info("Decompressing file %s...", file.namePtr.deref().get());

    final long v1 = file.type.get() & 0b110L;

    long transferDest;
    //LAB_800148b8
    if(v1 == 0) { // Decompress to file transfer dest
      //LAB_800148ec
      transferDest = file.transferDest.get();
      fileSize_800bb464.setu(Scus94491.decompress(transferDest_800bb460.get(), transferDest));
      free(transferDest_800bb460.get());
    } else {
      //LAB_8001491c
      transferDest = transferDest_800bb460.deref(4).offset(-0x8L).get();

      if(transferDest < 0x8000_0000L) {
        throw new RuntimeException("Illegal transfer destination 0x" + Long.toHexString(transferDest));
      }

      fileSize_800bb464.setu(Scus94491.decompress(transferDest_800bb460.get(), transferDest));

      final long address = FUN_80012244(transferDest, fileSize_800bb464.get());

      //LAB_80014984
      if(address != 0) {
        transferDest = address;
      }
    }

    //LAB_800149a4
    //LAB_800149a8
    if(transferDest < 0x8000_0000L) {
      throw new RuntimeException("Illegal transfer destination for decompression 0x" + Long.toHexString(transferDest));
    }

    transferDest_800bb460.setu(transferDest);
    file.used.set(false);

    switch(file.namePtr.deref().get()) {
      case "\\OVL\\SMAP.OV_" -> MEMORY.addFunctions(SMap.class);
      case "\\OVL\\S_STRM.OV_" -> MEMORY.addFunctions(SStrm.class);
      case "\\OVL\\TTLE.OV_" -> MEMORY.addFunctions(Ttle.class);
      case "\\OVL\\S_ITEM.OV_" -> MEMORY.addFunctions(SItem.class);
      case "\\OVL\\S_INIT.OV_" -> MEMORY.addFunctions(SInit.class);
      case "\\OVL\\WMAP.OV_" -> MEMORY.addFunctions(WMap.class);
      case "\\OVL\\BTTL.OV_" -> {
        MEMORY.addFunctions(Bttl_800c.class);
        MEMORY.addFunctions(Bttl_800d.class);
        MEMORY.addFunctions(Bttl_800e.class);
        MEMORY.addFunctions(Bttl_800f.class);
      }
      case "\\OVL\\S_BTLD.OV_" -> MEMORY.addFunctions(SBtld.class);
      case "\\OVL\\S_EFFE.OV_" -> MEMORY.addFunctions(SEffe.class);
      case "\\SECT\\DRGN0.BIN" -> { }
      default -> throw new RuntimeException("Loaded unknown file " + file.namePtr.deref().get());
    }

    //LAB_800149b4
  }

  @Method(0x800149ccL)
  public static long FUN_800149cc() {
    final FileLoadingInfo file = fileLoadingInfoArray_800bbad8.get(0);

    if(!file.used.get()) {
      //LAB_80014a74
      if(_8004ddd8.get() != 0x1L) {
        //LAB_80014b1c
        if(_8004ddd4.get() != 0x1L) {
          if(_8004ddd4.get() != 0x2L) {
            return 0;
          }

          if(_8004ddd0.get() == 0x2L) {
            fileLoadingCallbackIndex_8004ddc4.set(11);
            _8004ddd0.setu(0x1L);
            return 0x1L;
          }

          //LAB_80014b7c
          fileLoadingCallbackIndex_8004ddc4.set(7);
        } else {
          //LAB_80014b3c
          if(_8004ddd0.get() != 0x2L) {
            //LAB_80014b88
            _8004ddd4.setu(0);
            return 0x1L;
          }

          //LAB_80014b80
          fileLoadingCallbackIndex_8004ddc4.set(11);
        }

        _8004ddd0.setu(_8004ddd4);
        _8004ddd4.setu(0);
        return 0x1L;
      }

      if(_8004ddd0.get() == 0x2L) {
        fileLoadingCallbackIndex_8004ddc4.set(11);
        _8004ddd0.setu(0x1L);
        return 0x1L;
      }

      //LAB_80014aac
      if(_800bf0cf.get() >= 2) {
        _800bf0cf.setu(1);

        //LAB_80014ad0
        setCdVolume(0, 0);
        _800bf0e0.setu(0);
        return 0x1L;
      }

      //LAB_80014ae8
      if(_800bf0d8.get() != 0) {
        //LAB_80014af8
        FUN_800edb8c();

        //LAB_80014b00
        return 0x1L;
      }

      //LAB_80014b08
      //LAB_80014b10
      fileLoadingCallbackIndex_8004ddc4.set(26);
      _8004ddd0.setu(0x1L);
      return 0x1L;
    }

    if(_8004ddd0.get() == 0x2L) {
      fileLoadingCallbackIndex_8004ddc4.set(11);
      _8004ddd0.setu(0x1L);
      return 0x1L;
    }

    //LAB_80014a10
    if(_800bf0cf.get() >= 2) {
      _800bf0cf.setu(1);
      setCdVolume(0, 0);
      _800bf0e0.setu(0);
      return 0x1L;
    }

    //LAB_80014a3c
    if(_800bf0d8.get() != 0) {
      FUN_800edb8c();
      return 0x1L;
    }

    if(_8004ddcc.get() == 0) {
      fileLoadingCallbackIndex_8004ddc4.set(1);
      _8004ddd0.setu(0x1L);

      //LAB_80014b90
      return 0x1L;
    }

    return 0;
  }

  @Method(0x80014ba0L)
  public static long allocateFileTransferDest() {
    final FileLoadingInfo file = fileLoadingInfoArray_800bbad8.get(0);

    fileSize_800bb48c.setu(file.size.get());
    numberOfTransfers_800bb490.setu((file.size.get() + 0x7ffL) / 0x800L);

    switch(file.type.get() & 0b111) {
      case 0 -> {
        if(file.transferDest.get() < 0x8000_0000L) {
          throw new RuntimeException("Illegal transfer destination for decompression 0x" + Long.toHexString(file.transferDest.get()));
        }

        fileTransferDest_800bb488.setu(file.transferDest);
        return 0;
      }

      case 1, 4 -> {
        final long size = numberOfTransfers_800bb490.get() * 0x800L;
        final long transferDest = mallocHead(size);

        if(transferDest == 0) {
          return -0x1L;
        }

        if(transferDest < 0x8000_0000L) {
          throw new RuntimeException("Illegal transfer destination for decompression 0x" + Long.toHexString(transferDest));
        }

        fileTransferDest_800bb488.setu(transferDest);
        return 0;
      }

      case 5 -> {
        final long s1 = numberOfTransfers_800bb490.get() * 0x0800L;
        final long size = numberOfTransfers_800bb490.get() * 0x1000L + 0x100L;
        final long dest = mallocHead(size);

        if(dest == 0) {
          return -0x1L;
        }

        final long transferDest = dest + size - s1;
        MEMORY.ref(4, transferDest).offset(-0x8L).setu(dest);
        MEMORY.ref(4, transferDest).offset(-0x4L).setu(size);

        if(transferDest < 0x8000_0000L) {
          throw new RuntimeException("Illegal transfer destination for decompression 0x" + Long.toHexString(transferDest));
        }

        fileTransferDest_800bb488.setu(transferDest);
        return 0;
      }

      case 3 -> {
        final long s1 = numberOfTransfers_800bb490.get() * 0x0800L;
        final long size = numberOfTransfers_800bb490.get() * 0x1000L + 0x100L;
        final long dest = mallocTail(size);

        //LAB_80014c98
        if(dest == 0) {
          return -0x1L;
        }

        final long transferDest = dest + size - s1;
        MEMORY.ref(4, transferDest).offset(-0x8L).setu(dest);
        MEMORY.ref(4, transferDest).offset(-0x4L).setu(size);

        if(transferDest < 0x8000_0000L) {
          throw new RuntimeException("Illegal transfer destination for decompression 0x" + Long.toHexString(transferDest));
        }

        fileTransferDest_800bb488.setu(transferDest);
        return 0;
      }

      case 2 -> {
        final long size = numberOfTransfers_800bb490.get() * 0x800L;
        final long transferDest = mallocTail(size);

        //LAB_80014cd0
        if(transferDest == 0) {
          return -0x1L;
        }

        if(transferDest < 0x8000_0000L) {
          throw new RuntimeException("Illegal transfer destination for decompression 0x" + Long.toHexString(transferDest));
        }

        fileTransferDest_800bb488.setu(transferDest);
        return 0;
      }

      default -> {
        file.used.set(false);
        popFirstFileIfUnused();
        fileLoadingCallbackIndex_8004ddc4.set(0);
        return -0x1L;
      }
    }
  }

  @Method(0x80014d20L)
  public static void loadFiles() {
    executeFileLoadingStage();
    decompressCurrentFile();
    executeCurrentlyLoadingFileCallback();
  }

  @Method(0x80014d50L)
  public static void executeFileLoadingStage() {
    if(!SInitBinLoaded_800bbad0.get()) {
      return;
    }

    final int callbackIndex = fileLoadingCallbackIndex_8004ddc4.get();
    if(callbackIndex != 0) {
      LOGGER.info("File loading callback index %d", callbackIndex);
    }

    fileLoadingCallbacks_8004dddc.get(callbackIndex).deref().run();
    FUN_8002c86c();

    //LAB_80014d94
  }

  @Method(0x80014da4L)
  public static void executeCurrentlyLoadingFileCallback() {
    final FileLoadingInfo file = currentlyLoadingFileInfo_800bb468;

    if(!file.callback.isNull()) {
      LOGGER.info("Executing file callback %08x (param %08x)", file.callback.getPointer(), file.callbackParam.get());
      file.callback.deref().run(transferDest_800bb460.get(), fileSize_800bb464.get(), (long)file.callbackParam.get());
      file.callback.clear();
    }
  }

  @Method(0x80014df0L)
  public static long loadQueuedFile() {
    if(allocateFileTransferDest() != 0) {
      return 0;
    }

    final FileLoadingInfo file = fileLoadingInfoArray_800bbad8.get(0);

    LOGGER.info("Loading file %s to %08x", file.namePtr.deref().get(), fileTransferDest_800bb488.get());

    CDROM.readFromDisk(file.pos, (int)numberOfTransfers_800bb490.get(), fileTransferDest_800bb488.get());

    fileLoadingCallbackIndex_8004ddc4.set(2);

    return 1;
  }

  @Method(0x80014e54L)
  public static long FUN_80014e54() {
    FUN_80014ef4();
    fileLoadingInfoArray_800bbad8.get(0).used.set(false);

    popFirstFileIfUnused();
    fileLoadingCallbackIndex_8004ddc4.set(0);
    return 0;
  }

  @Method(0x80014ef4L)
  public static void FUN_80014ef4() {
    if(fileTransferDest_800bb488.get() < 0x8000_0000L) {
      throw new RuntimeException("Illegal transfer destination for decompression 0x" + Long.toHexString(fileTransferDest_800bb488.get()));
    }

    transferDest_800bb460.setu(fileTransferDest_800bb488);
    fileSize_800bb464.setu(fileSize_800bb48c);
    currentlyLoadingFileInfo_800bb468.set(fileLoadingInfoArray_800bbad8.get(0));
  }

  @Method(0x800151a0L)
  public static void popFirstFileIfUnused() {
    if(fileLoadingInfoArray_800bbad8.get(0).used.get()) {
      return;
    }

    fileCount_8004ddc8.decr();

    //LAB_800151d4
    for(int i = 0; i < fileCount_8004ddc8.get(); i++) {
      fileLoadingInfoArray_800bbad8.get(i).set(fileLoadingInfoArray_800bbad8.get(i + 1));
    }

    //LAB_80015230
    fileLoadingInfoArray_800bbad8.get(fileCount_8004ddc8.get()).used.set(false);

    //LAB_80015244
  }

  @Method(0x8001524cL)
  public static long loadFile(final FileEntry08 entry, final long transferDest, final long callback, final long callbackParam, final long param_5) {
    final long s0 = FUN_800155b8(entry.fileIndex_00.get(), transferDest, param_5);

    if(s0 < 0) {
      return -0x1L;
    }

    final FileLoadingInfo file = addFile();
    if(file == null) {
      assert false : "File stack overflow";
      return -0x1L;
    }

    file.callback.set(MEMORY.ref(4, callback).cast(TriConsumerRef::new));
    file.transferDest.setu(transferDest);
    file.namePtr.set(entry.name_04.deref());
    file.callbackParam.set((int)callbackParam);
    file.type.set((short)s0);
    file.used.set(true);
    setLoadingFilePosAndSizeFromFile(file, entry);

    LOGGER.info(file);

    return 0;
  }

  @Method(0x80015310L)
  public static long loadDrgnBinFile(final int index, final int fileIndex, final long fileTransferDest, final long callback, final long callbackParam, final long param_6) {
    final int drgnIndex = Math.min(index, 2);

    //LAB_80015388
    //LAB_8001538c
    final long s2 = FUN_800155b8(drgnFiles_8004dda0.get(drgnIndex).fileIndex_00.get(), fileTransferDest, param_6);
    if(s2 < 0) {
      return -0x1L;
    }

    LOGGER.info("Queueing DRGN%d file %d (if you're a programmer), %d (if you're a zamboni)", index, fileIndex, fileIndex + 1);

    final FileLoadingInfo file = addFile();
    if(file == null) {
      //LAB_800153d4
      assert false : "File stack overflow";
      return -0x1L;
    }

    //LAB_800153dc
    file.callback.set(MEMORY.ref(4, callback).cast(TriConsumerRef::new));
    file.transferDest.setu(fileTransferDest);
    file.namePtr.set(drgnFiles_8004dda0.get(drgnIndex).name_04.deref());
    file.callbackParam.set((int)callbackParam);
    file.type.set((short)s2);
    file.used.set(true);
    getDrgnFilePos(file, drgnIndex, fileIndex);

    LOGGER.info(file);

    //LAB_80015424
    return 0;
  }

  @Method(0x8001557cL)
  @Nullable
  public static FileLoadingInfo addFile() {
    if(fileCount_8004ddc8.get() >= 44) {
      LOGGER.error("File stack overflow");
      return null;
    }

    final FileLoadingInfo file = fileLoadingInfoArray_800bbad8.get(fileCount_8004ddc8.get());
    fileCount_8004ddc8.incr();

    return file;
  }

  @Method(0x800155b8L)
  public static long FUN_800155b8(final long a0, final long fileTransferDest, long a2) {
    if(a0 < 0) {
      return -0x1L;
    }

    a2 = a2 & 0xffffffefL | 0x8L;

    //LAB_800155d0
    if(fileTransferDest == 0 && (a2 & 0x8000L) == 0) {
      //LAB_800155ec
      if((a2 & 0x6L) == 0) {
        a2 |= 0x2L;
      }

      return a2;
    }

    //LAB_800155e4
    //LAB_800155fc
    return a2 & 0xfffffff9L;
  }

  @Method(0x80015604L)
  public static void setLoadingFilePosAndSizeFromFile(final FileLoadingInfo loadingFile, final FileEntry08 entry) {
    final CdlFILE file = CdlFILE_800bb4c8.get(entry.fileIndex_00.get());
    loadingFile.pos.set(file.pos);
    loadingFile.size.set(file.size.get());
  }

  @Method(0x80015644L)
  public static long getDrgnFilePos(final FileLoadingInfo file, final int drgnIndex, final int fileIndex) {
    final long sector = CdlFILE_800bb4c8.get(drgnFiles_8004dda0.get(drgnIndex).fileIndex_00.get()).pos.pack();

    final MrgEntry entry = drgnMrg_800bc060.get(drgnIndex).deref().entries.get(fileIndex);

    file.pos.unpack(sector + entry.offset.get());
    file.size.set((int)entry.size.get());

    return 0;
  }

  @Method(0x800156f4L)
  public static void FUN_800156f4(final long a0) {
    _8004ddcc.setu(a0 == 0 ? 0 : 1);
  }

  @Method(0x80015704L)
  public static long FUN_80015704(long a0, long a1) {
    a1 = a1 - 0x1L;
    long a2 = MEMORY.ref(4, a0).offset(0x4L).get() * 0x8L + 0x8L;
    a0 = a0 + a1 * 0x8L;

    //LAB_80015724
    while((int)a1 >= 0) {
      final long v0 = MEMORY.ref(4, a0).offset(0xcL).get() + 0x3L & 0xffff_fffcL;
      final long v1 = MEMORY.ref(4, a0).offset(0x8L).get() + v0;

      if((int)a2 < (int)v1) {
        a2 = v1;
      }

      //LAB_80015748
      a0 = a0 - 0x8L;
      a1 = a1 - 0x1L;
    }

    //LAB_80015754
    return a2;
  }

  @Method(0x8001575cL)
  public static void tickScripts() {
    executeScriptFrame();
    executeScriptTickers();
    scriptStateUpperBound_8004de4c.setu(0x9L);
  }

  @Method(0x800157b8L)
  public static void renderScriptObjects() {
    executeScriptRenderers();
  }

  @Method(0x80015800L)
  public static int findFreeScriptState() {
    scriptStateUpperBound_8004de4c.addu(0x1L);

    if(scriptStateUpperBound_8004de4c.get() >= 0x48L) {
      scriptStateUpperBound_8004de4c.setu(0x9L);
    }

    //LAB_80015824
    //LAB_8001584c
    for(int i = (int)scriptStateUpperBound_8004de4c.get(); i < 0x48; i++) {
      if(scriptStatePtrArr_800bc1c0.get(i).getPointer() == scriptState_800bc0c0.getAddress()) {
        //LAB_800158c0
        scriptStateUpperBound_8004de4c.setu(i);
        return i;
      }
    }

    //LAB_8001586c
    //LAB_80015898
    for(int i = 0x9; i < scriptStateUpperBound_8004de4c.get(); i++) {
      if(scriptStatePtrArr_800bc1c0.get(i).getPointer() == scriptState_800bc0c0.getAddress()) {
        //LAB_800158c0
        scriptStateUpperBound_8004de4c.setu(i);
        return i;
      }
    }

    //LAB_800158b8
    return -1;
  }

  @Method(0x800158ccL)
  public static <T extends MemoryRef> int allocateScriptState(final long innerStructSize, final Function<Value, T> type) {
    final int index = findFreeScriptState();

    if(index < 0) {
      return -1;
    }

    return allocateScriptState(index, innerStructSize, false, null, 0, type);
  }

  /**
   * @return index, or -1 on failure to allocate memory
   */
  @Method(0x80015918L)
  public static <T extends MemoryRef> int allocateScriptState(final int index, final long innerStructSize, final boolean allocateOnHead, @Nullable final CString a3, final long a4, final Function<Value, T> type) {
    LOGGER.info("Allocating script index %d (0x%x bytes)", index, innerStructSize);

    final long linkedListAddress;
    if(allocateOnHead) {
      linkedListAddress = mallocHead(innerStructSize + 0x100L);
    } else {
      //LAB_80015954
      linkedListAddress = mallocTail(innerStructSize + 0x100L);
    }

    //LAB_80015968
    if(linkedListAddress == 0) {
      return -1;
    }

    bzero(linkedListAddress, (int)(innerStructSize + 0x100));

    final ScriptState<T> scriptState = MEMORY.ref(4, linkedListAddress, ScriptState.of(type));

    //LAB_80015978
    scriptStatePtrArr_800bc1c0.get(index).set(scriptState);

    if(innerStructSize != 0) {
      scriptState.innerStruct_00.setPointer(linkedListAddress + 0x100L);
    } else {
      scriptState.innerStruct_00.clear();
    }

    //LAB_800159c0
    scriptState.storage_44.get(0).set(index);
    scriptState.storage_44.get(1).set(-1);
    scriptState.storage_44.get(2).set(-1);
    scriptState.storage_44.get(3).set(-1);
    scriptState.storage_44.get(4).set(-1);
    scriptState.storage_44.get(5).set(-1);
    scriptState.storage_44.get(6).set(-1);
    scriptState.ui_60.set(0x080f_0000L);
    scriptState.storage_44.get(8).set(-1);
    scriptState.storage_44.get(9).set(-1);
    scriptState.storage_44.get(10).set(-1);
    scriptState.storage_44.get(11).set(-1);
    scriptState.storage_44.get(12).set(-1);
    scriptState.storage_44.get(13).set(-1);
    scriptState.storage_44.get(14).set(-1);
    scriptState.storage_44.get(15).set(-1);
    scriptState.storage_44.get(16).set(-1);
    scriptState.storage_44.get(17).set(-1);
    scriptState.storage_44.get(18).set(-1);
    scriptState.storage_44.get(19).set(-1);
    scriptState.storage_44.get(20).set(-1);
    scriptState.storage_44.get(21).set(-1);
    scriptState.storage_44.get(22).set(-1);
    scriptState.storage_44.get(23).set(-1);
    scriptState.storage_44.get(24).set(-1);

    //LAB_800159f8
    //LAB_80015a14
    scriptState.typePtr_f8.setNullable(a3);
    scriptState.ui_fc.set(a4);

    EventManager.INSTANCE.postEvent(new ScriptAllocatedEvent(index));

    //LAB_80015a34
    return index;
  }

  @Method(0x80015a68L)
  public static <T extends MemoryRef> void setScriptTicker(final int index, @Nullable final TriConsumerRef<Integer, ScriptState<T>, T> callback) {
    final ScriptState<T> struct = (ScriptState<T>)scriptStatePtrArr_800bc1c0.get(index).deref();

    if(callback == null) {
      //LAB_80015aa0
      struct.ticker_04.clear();
      struct.ui_60.or(0x0004_0000L);
    } else {
      struct.ticker_04.set(callback);
      struct.ui_60.and(0xfffb_ffffL);
    }
  }

  @Method(0x80015ab4L)
  public static <T extends MemoryRef> void setScriptRenderer(final int index, @Nullable final TriConsumerRef<Integer, ScriptState<T>, T> callback) {
    final ScriptState<T> struct = (ScriptState<T>)scriptStatePtrArr_800bc1c0.get(index).deref();

    if(callback == null) {
      //LAB_80015aec
      struct.renderer_08.clear();
      struct.ui_60.or(0x0008_0000L);
    } else {
      struct.renderer_08.set(callback);
      struct.ui_60.and(0xfff7_ffffL);
    }
  }

  @Method(0x80015b00L)
  public static <T extends MemoryRef> void setScriptDestructor(final int index, @Nullable final TriConsumerRef<Integer, ScriptState<T>, T> callback) {
    final ScriptState<T> struct = (ScriptState<T>)scriptStatePtrArr_800bc1c0.get(index).deref();

    if(callback == null) {
      //LAB_80015b38
      struct.destructor_0c.clear();
      struct.ui_60.or(0x0800_0000L);
    } else {
      struct.destructor_0c.set(callback);
      struct.ui_60.and(0xf7ff_ffffL);
    }
  }

  @Method(0x80015b4cL)
  public static <T extends MemoryRef> void setScriptTempTicker(final int index, @Nullable final TriFunctionRef<Integer, ScriptState<T>, T, Long> callback) {
    final ScriptState<T> struct = (ScriptState<T>)scriptStatePtrArr_800bc1c0.get(index).deref();

    if(callback == null) {
      //LAB_80015b80
      struct.tempTicker_10.clear();
      struct.ui_60.and(0xfbff_ffffL);
    } else {
      struct.tempTicker_10.set(callback);
      struct.ui_60.or(0x0400_0000L);
    }
  }

  @Method(0x80015b98L)
  public static void loadScriptFile(final int index, @Nullable final ScriptFile script, final String friendlyName, final int length) {
    loadScriptFile(index, script, 0, friendlyName, length);
  }

  @Method(0x80015bb8L)
  public static void loadScriptFile(final int index, @Nullable final ScriptFile script, final long offsetIndex, final String friendlyName, final int length) {
    final ScriptState<?> struct = scriptStatePtrArr_800bc1c0.get(index).deref();

    if(script != null) {
      LOGGER.info("Loading script index %d from 0x%08x (entry point 0x%x)", index, script.getAddress(), offsetIndex);

      struct.scriptPtr_14.set(script);
      struct.commandPtr_18.set(script.offsetArr_00.get((int)offsetIndex).deref());
      struct.ui_60.and(0xfffd_ffffL);
    } else {
      LOGGER.info("Clearing script index %d", index);

      struct.scriptPtr_14.clear();
      struct.commandPtr_18.clear();
      struct.ui_60.or(0x2_0000L);
    }
  }

  @Method(0x80015c20L)
  public static void deallocateScriptState(final int scriptIndex) {
    LOGGER.info("Deallocating script state %d", scriptIndex);

    EventManager.INSTANCE.postEvent(new ScriptDeallocatedEvent(scriptIndex));

    final ScriptState<MemoryRef> scriptState = scriptStatePtrArr_800bc1c0.get(scriptIndex).derefAs(ScriptState.classFor(MemoryRef.class));
    if((scriptState.ui_60.get() & 0x810_0000L) == 0) {
      try {
        scriptState.destructor_0c.deref().run(scriptIndex, scriptState, scriptState.innerStruct_00.derefNullableAs(MemoryRef.class));
      } catch(final NullPointerException e) {
        LOGGER.error("Script %d destructor was null", scriptIndex);
        throw e;
      }
    }

    //LAB_80015c70
    scriptStatePtrArr_800bc1c0.get(scriptIndex).set(scriptState_800bc0c0);
    free(scriptState.getAddress());
  }

  @Method(0x80015c9cL)
  public static void deallocateScriptChildren(final int scriptIndex) {
    LOGGER.info("Deallocating script %d children", scriptIndex);

    final ScriptState<?> scriptState = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref();

    int a0_0 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().storage_44.get(6).get();

    //LAB_80015cdc
    while(a0_0 >= 0) {
      final int s0 = scriptStatePtrArr_800bc1c0.get(a0_0).deref().storage_44.get(6).get();
      deallocateScriptState(a0_0);
      a0_0 = s0;
    }

    //LAB_80015d04
    scriptState.storage_44.get(6).set(-1);
    scriptState.ui_60.and(0xffdf_ffffL);
  }

  @Method(0x80015d38L)
  public static void deallocateScriptAndChildren(final int scriptIndex) {
    deallocateScriptChildren(scriptIndex);
    deallocateScriptState(scriptIndex);
  }

  @Method(0x80015d74L)
  public static int scriptFork(final int parentScriptIndex) {
    final int childScriptIndex = findFreeScriptState();

    LOGGER.info("Forking script %d to %d", parentScriptIndex, childScriptIndex);

    if(childScriptIndex < 0 || allocateScriptState(childScriptIndex, 0, false, null, 0, null) < 0) {
      //LAB_80015dd4
      return -1;
    }

    //LAB_80015ddc
    final ScriptState<?> parentScript = scriptStatePtrArr_800bc1c0.get(parentScriptIndex).deref();
    final ScriptState<?> childScript = scriptStatePtrArr_800bc1c0.get(childScriptIndex).deref();
    childScript.ui_60.set(parentScript.ui_60.get() | 0x10_0000L); // Child
    parentScript.ui_60.or(0x20_0000L); // Parent

    //LAB_80015e0c
    for(int i = 0; i < 25; i++) {
      childScript.storage_44.get(8 + i).set(parentScript.storage_44.get(8 + i).get());
    }

    childScript.storage_44.get(5).set(parentScriptIndex);
    childScript.storage_44.get(6).set(parentScript.storage_44.get(6).get());
    parentScript.storage_44.get(6).set(childScriptIndex);
    childScript.scriptPtr_14.set(parentScript.scriptPtr_14.deref());
    childScript.commandPtr_18.set(parentScript.commandPtr_18.deref());

    //LAB_80015e4c
    return childScriptIndex;
  }

  /** Deallocates child and assumes its identity? */
  @Method(0x80015e68L)
  public static IntRef scriptConsumeChild(final int scriptIndex) {
    final ScriptState<?> a3 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref();

    final int childIndex = a3.storage_44.get(6).get();
    if(childIndex < 0) {
      throw new RuntimeException("Null command");
    }

    LOGGER.info("Consuming script %d child %d", scriptIndex, childIndex);

    final ScriptState<?> child = scriptStatePtrArr_800bc1c0.get(childIndex).deref();
    if((child.ui_60.get() & 0x20_0000L) != 0) { // Is parent
      a3.ui_60.or(0x20_0000L);
      a3.storage_44.get(6).set(child.storage_44.get(6).get());
      scriptStatePtrArr_800bc1c0.get(child.storage_44.get(6).get()).deref().storage_44.get(5).set(scriptIndex);
    } else {
      //LAB_80015ef0
      a3.storage_44.get(6).set(-1);
      a3.ui_60.and(0xffdf_ffffL);
    }

    //LAB_80015f08
    //LAB_80015f14
    for(int i = 0; i < 25; i++) {
      a3.storage_44.get(8 + i).set(child.storage_44.get(8 + i).get());
    }

    a3.scriptPtr_14.set(child.scriptPtr_14.deref());
    a3.commandPtr_18.set(child.commandPtr_18.deref());
    deallocateScriptState(childIndex);

    //LAB_80015f54
    return child.commandPtr_18.deref();
  }

  @Method(0x80015f64L)
  public static long scriptNotImplemented(final RunningScript a0) {
    assert false;
    return 0x2L;
  }

  @Method(0x80015f6cL)
  public static void executeScriptFrame() {
    long v0;
    long v1;

    if(scriptsTickDisabled_800bc0b8.get() || scriptsDisabled_800bc0b9.get()) {
      return;
    }

    RunningScript_800bc070.ui_1c.set(0);

    //LAB_80015fd8
    for(int index = 0; index < 0x48; index++) {
      final ScriptState<WorldObject210> scriptState = scriptStatePtrArr_800bc1c0.get(index).derefAs(ScriptState.classFor(WorldObject210.class));

      if(scriptState.getAddress() != scriptState_800bc0c0.getAddress() && (scriptState.ui_60.get() & 0x12_0000L) == 0) {
        RunningScript_800bc070.scriptStateIndex_00.set(index);
        RunningScript_800bc070.scriptState_04.set(scriptState);
        RunningScript_800bc070.commandPtr_0c.set(scriptState.commandPtr_18.deref());
        RunningScript_800bc070.opPtr_08.set(scriptState.commandPtr_18.deref());

        if(scriptLog[index]) {
          System.err.printf("Exec script index %d (%08x)%n", index, RunningScript_800bc070.scriptState_04.deref().scriptPtr_14.getPointer());
        }

        long ret;
        //LAB_80016018
        do {
          final long opCommand = RunningScript_800bc070.commandPtr_0c.deref().get();
          RunningScript_800bc070.opIndex_10.set(opCommand & 0xffL);
          RunningScript_800bc070.paramCount_14.set(opCommand >>> 8 & 0xffL);
          RunningScript_800bc070.opParam_18.set(opCommand >>> 16);

          if(scriptLog[index]) {
            System.err.println(Long.toHexString(RunningScript_800bc070.commandPtr_0c.getPointer() - RunningScript_800bc070.scriptState_04.deref().scriptPtr_14.getPointer()) + " (" + RunningScript_800bc070.commandPtr_0c.deref() + ')');
            System.err.printf("param[p] = %x%n", opCommand >>> 16);
          }

          RunningScript_800bc070.commandPtr_0c.incr();

          //LAB_80016050
          for(int paramIndex = 0; paramIndex < RunningScript_800bc070.paramCount_14.get(); paramIndex++) {
            final int childCommand = RunningScript_800bc070.commandPtr_0c.deref().get();
            final int paramType = childCommand >>> 24;
            final int param0 = childCommand >>> 16 & 0xff;
            final int param1 = childCommand >>> 8 & 0xff;
            final int param2 = childCommand & 0xff;

            RunningScript_800bc070.commandPtr_0c.incr();
            final long commandPtr = RunningScript_800bc070.commandPtr_0c.getPointer();

            if(paramType == 0x1) {
              //LAB_800161f4
              final IntRef p = RunningScript_800bc070.commandPtr_0c.deref();
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
              RunningScript_800bc070.commandPtr_0c.incr();
            } else if(paramType == 0x2) {
              //LAB_80016200
              final IntRef p = RunningScript_800bc070.scriptState_04.deref().storage_44.get(param2);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0x3) {
              //LAB_800160cc
              //LAB_8001620c
              final long a0_0 = RunningScript_800bc070.scriptState_04.deref().storage_44.get(param2).get();
              final long a1_0 = scriptStatePtrArr_800bc1c0.get((int)a0_0).deref().storage_44.get(param1).get();
              final IntRef p = scriptStatePtrArr_800bc1c0.get((int)a1_0).deref().storage_44.get(param0);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0x4) {
              //LAB_80016258
              final long a0_0 = RunningScript_800bc070.scriptState_04.deref().storage_44.get(param2).get();
              final long a1_0 = param1 + RunningScript_800bc070.scriptState_04.deref().storage_44.get(param0).get();
              final IntRef p = scriptStatePtrArr_800bc1c0.get((int)a0_0).deref().storage_44.get((int)a1_0);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0x5) {
              //LAB_80016290
              final IntRef p = scriptPtrs_8004de58.get(param2).deref();
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0x6) {
              //LAB_800162a4
              final IntRef p = scriptPtrs_8004de58.get(RunningScript_800bc070.scriptState_04.deref().storage_44.get(param1).get() + param2).deref();
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0x7) {
              //LAB_800162d0
              final long a0_0 = RunningScript_800bc070.scriptState_04.deref().storage_44.get(param1).get();
              final IntRef p = MEMORY.ref(4, scriptPtrs_8004de58.get(param2).getPointer() + a0_0 * 0x4L, IntRef::new);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0x8) {
              //LAB_800160e8
              //LAB_800162f4
              v0 = RunningScript_800bc070.scriptState_04.deref().storage_44.get(param1).get();
              final long a1_0 = RunningScript_800bc070.scriptState_04.deref().storage_44.get(param0).get();
              final IntRef p = MEMORY.ref(4, scriptPtrs_8004de58.get((int)(param2 + v0)).getPointer() + a1_0 * 0x4L, IntRef::new);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0x9) {
              //LAB_80016328
              v1 = RunningScript_800bc070.opPtr_08.getPointer() + (short)childCommand * 0x4L;
              final IntRef p = MEMORY.ref(4, v1, IntRef::new);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0xa) {
              //LAB_80016118
              //LAB_80016334
              v0 = RunningScript_800bc070.opPtr_08.getPointer() + ((short)childCommand + RunningScript_800bc070.scriptState_04.deref().storage_44.get(param0).get()) * 0x4L;
              final IntRef p = MEMORY.ref(4, v0, IntRef::new);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0xb) {
              //LAB_80016360
              v0 = RunningScript_800bc070.opPtr_08.getPointer() + (RunningScript_800bc070.scriptState_04.deref().storage_44.get(param0).get() + (short)childCommand) * 0x4L;
              final long a0_0 = RunningScript_800bc070.opPtr_08.getPointer() + (MEMORY.ref(4, v0).get() + (short)childCommand) * 0x4L;
              final IntRef p = MEMORY.ref(4, a0_0, IntRef::new);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0xc) {
              //LAB_800163a0
              RunningScript_800bc070.commandPtr_0c.incr();
              v0 = commandPtr + MEMORY.ref(4, commandPtr).offset(RunningScript_800bc070.scriptState_04.deref().storage_44.get(param2).get() * 0x4L).get() * 0x4L + RunningScript_800bc070.scriptState_04.deref().storage_44.get(param1).get() * 0x4L;
              final IntRef p = MEMORY.ref(4, v0, IntRef::new);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0xd) {
              //LAB_800163e8
              final IntRef p = scriptStatePtrArr_800bc1c0.get(RunningScript_800bc070.scriptState_04.deref().storage_44.get(param2).get()).deref().storage_44.get(param1 + param0);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0xe) {
              //LAB_80016418
              final IntRef p = scriptPtrs_8004de58.get(param1 + param2).deref();
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0xf) {
              //LAB_8001642c
              final IntRef p = MEMORY.ref(4, scriptPtrs_8004de58.get(param2).getPointer() + param1 * 0x4L, IntRef::new);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0x10) {
              //LAB_80016180
              //LAB_8001643c
              final IntRef p = MEMORY.ref(4, scriptPtrs_8004de58.get(param2 + RunningScript_800bc070.scriptState_04.deref().storage_44.get(param1).get()).getPointer() + param0 * 0x4L, IntRef::new);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0x11) {
              //LAB_80016468
//              ScriptStruct_800bc070.params_20.get(paramIndex).set(scriptPtrs_8004de58.get(opCommand * 0x4L).deref(4).offset(ScriptStruct_800bc070.scriptState_04.deref().ui_44.get(param0).get() * 0x4L).cast(UnsignedIntRef::new));
              assert false;
            } else if(paramType == 0x12) {
              //LAB_80016138
              //LAB_8001648c
//              ScriptStruct_800bc070.params_20.get(paramIndex).set(scriptPtrs_8004de58.offset((param2 + param1) * 0x4L).deref(4).offset(param0 * 0x4L).cast(UnsignedIntRef::new));
              assert false;
            } else if(paramType == 0x13) {
              //LAB_800164a4
              v1 = RunningScript_800bc070.opPtr_08.getPointer() + ((short)childCommand + param0) * 4;
              final IntRef p = MEMORY.ref(4, v1, IntRef::new);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0x14) {
              //LAB_800164b4
              v1 = RunningScript_800bc070.opPtr_08.getPointer() + (short)childCommand * 0x4L;

              //LAB_800164cc
              v0 = MEMORY.ref(4, v1).offset(param0 * 0x4L).getSigned() * 0x4L;

              //LAB_800164d4
              final IntRef p = MEMORY.ref(4, v1 + v0, IntRef::new);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0x15) {
              //LAB_800161a0
              //LAB_800164e0
              RunningScript_800bc070.commandPtr_0c.incr();
              v0 = commandPtr + MEMORY.ref(4, commandPtr).offset(RunningScript_800bc070.scriptState_04.deref().storage_44.get(param2).get() * 0x4L).get() * 0x4L + param1 * 0x4L;

              //LAB_80016580
              final IntRef p = MEMORY.ref(4, v0, IntRef::new);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0x16) {
              //LAB_80016518
              RunningScript_800bc070.commandPtr_0c.incr();
              v0 = commandPtr + MEMORY.ref(4, commandPtr).offset(param2 * 0x4L).get() * 0x4L + RunningScript_800bc070.scriptState_04.deref().storage_44.get(param1).get() * 0x4L;
              final IntRef p = MEMORY.ref(4, v0, IntRef::new);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else if(paramType == 0x17) {
              //LAB_800161d4
              //LAB_8001654c
              RunningScript_800bc070.commandPtr_0c.incr();
              v0 = commandPtr + MEMORY.ref(4, commandPtr).offset(param2 * 0x4L).get() * 0x4L + param1 * 0x4L;
              final IntRef p = MEMORY.ref(4, v0, IntRef::new);
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p);
            } else { // Treated as an immediate if not a valid op
              //LAB_80016574
              final IntRef p = RunningScript_800bc070.commandPtr_0c.deref();
              if(scriptLog[index]) {
                System.err.printf("params[%d] = %s%n", paramIndex, p);
              }
              RunningScript_800bc070.params_20.get(paramIndex).set(p).decr();
            }

            //LAB_80016584
          }

          EventManager.INSTANCE.postEvent(new ScriptTickEvent(index));

          final int opIndex = (int)RunningScript_800bc070.opIndex_10.get();
          final FunctionRef<RunningScript, Long> callback = scriptFunctions_8004e098.get(opIndex).deref();

          if(scriptLog[index]) {
            if(scriptFunctionDescriptions.containsKey(opIndex)) {
              System.err.println(scriptFunctionDescriptions.get(opIndex).apply(RunningScript_800bc070));
            } else {
              System.err.printf("Running callback %d (%08x)%n", opIndex, callback.getAddress());
            }
          }

          //LAB_80016598
          ret = callback.run(RunningScript_800bc070);

          if(scriptLog[index]) {
            if(ret == 1) {
              System.err.println("Pausing");
            } else if(ret != 0) {
              System.err.println("Rewinding and pausing");
            }
          }

          // Returning 0 continues execution
          // Returning 1 pauses execution until the next frame
          // Returning anything else pauses execution and repeats the same instruction next frame
          if(ret == 0 || ret == 0x1L) {
            //LAB_800165e8
            RunningScript_800bc070.opPtr_08.set(RunningScript_800bc070.commandPtr_0c.deref());
          }
        } while(ret == 0);

        //LAB_800165f4
        if(scriptState.getAddress() != scriptState_800bc0c0.getAddress()) {
          scriptState.commandPtr_18.set(RunningScript_800bc070.opPtr_08.deref());
        }
      }

      //LAB_80016614
    }

    //LAB_80016624
  }

  @Method(0x8001664cL)
  public static long scriptCompare(final RunningScript a0, final long operandA, final long operandB, final long op) {
    return switch((int)op) {
      case 0x0 -> (int)operandA <= (int)operandB ? 1 : 0;
      case 0x1 -> (int)operandA < (int)operandB ? 1 : 0;
      case 0x2 -> operandA == operandB ? 1 : 0;
      case 0x3 -> operandA != operandB ? 1 : 0;
      case 0x4 -> (int)operandA > (int)operandB ? 1 : 0;
      case 0x5 -> (int)operandA >= (int)operandB ? 1 : 0;
      case 0x6 -> operandA & operandB;
      case 0x7 -> (operandA & operandB) == 0 ? 1 : 0;
      default -> 0;
    };
  }

  /** Stop execution for this frame, resume next frame */
  @Method(0x800166d0L)
  public static long scriptPause(final RunningScript a0) {
    return 0x1L;
  }

  /** Stop execution for this frame, resume next frame and repeat same command */
  @Method(0x800166d8L)
  public static long scriptRewindAndPause(final RunningScript a0) {
    return 0x2L;
  }

  /**
   * Subtracts 1 from work array value 0 if nonzero
   *
   * @return 0 if value is already 0; 2 if value was decremented
   */
  @Method(0x800166e0L)
  public static long scriptWait(final RunningScript a0) {
    if(a0.params_20.get(0).deref().get() != 0) {
      a0.params_20.get(0).deref().decr();
      return 0x2L;
    }

    return 0;
  }

  /**
   * <p>Compares work array values 0 and 2 based on an operand stored in the parent param</p>
   *
   * <p>
   *   Operations:
   *   <ol start="0">
   *     <li>Less than or equal to</li>
   *     <li>Less than</li>
   *     <li>Equal</li>
   *     <li>Inequal</li>
   *     <li>Greater than</li>
   *     <li>Greater than or equal to</li>
   *     <li>And</li>
   *     <li>Nand</li>
   *   </ol>
   * </p>
   *
   * @return 0 if comparison succeeds, otherwise return 2
   */
  @Method(0x8001670cL)
  public static long scriptCompare(final RunningScript a0) {
    return scriptCompare(a0, a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.opParam_18.get()) == 0 ? 2 : 0;
  }

  /** Same as {@link Scus94491BpeSegment#scriptCompare(RunningScript)} with first param set to 0 */
  @Method(0x80016744L)
  public static long scriptCompare0(final RunningScript a0) {
    return scriptCompare(a0, 0, a0.params_20.get(0).deref().get(), a0.opParam_18.get()) < 1 ? 2 : 0;
  }

  /**
   * Set work array value 1 to value 0
   *
   * @return 0
   */
  @Method(0x80016774L)
  public static long scriptMove(final RunningScript a0) {
    a0.params_20.get(1).deref().set(a0.params_20.get(0).deref());
    return 0;
  }

  /** Pretty sure this is _supposed_ to be a swap */
  @Method(0x80016790L)
  public static long FUN_80016790(final RunningScript a0) {
    final int v1 = a0.params_20.get(0).deref().get();
    a0.params_20.get(1).deref().set(v1);
    a0.params_20.get(0).deref().set(v1);
    return 0;
  }

  /**
   * Copy block of memory at work array parameter 1 to block of memory at work array parameter 2. Word count is at work array parameter 0.
   *
   * @return 0
   */
  @Method(0x800167bcL)
  public static long scriptMemCopy(final RunningScript a0) {
    final Pointer<IntRef> dest = a0.params_20.get(2);
    final Pointer<IntRef> src = a0.params_20.get(1);
    long count = a0.params_20.get(0).deref().get() << 2 >> 2;

    if(dest.getPointer() < src.getPointer()) {
      count--;

      //LAB_800167e8
      while((int)count >= 0) {
        dest.deref().set(src.deref());
        src.incr();
        dest.incr();
        count--;
      }

      return 0;
    }

    //LAB_8001680c
    if(src.getPointer() < dest.getPointer()) {
      count--;

      dest.add(count * 0x4L);
      src.add(count * 0x4L);

      //LAB_8001682c
      while((int)count >= 0) {
        dest.deref().set(src.deref());
        src.decr();
        dest.decr();
        count--;
      }
    }

    //LAB_80016848
    return 0;
  }

  @Method(0x80016854L)
  public static long scriptSetZero(final RunningScript a0) {
    a0.params_20.get(0).deref().set(0);
    return 0;
  }

  @Method(0x80016868L)
  public static long scriptAnd(final RunningScript a0) {
    a0.params_20.get(1).deref().and(a0.params_20.get(0).deref());
    return 0;
  }

  @Method(0x8001688cL)
  public static long scriptOr(final RunningScript a0) {
    a0.params_20.get(1).deref().or(a0.params_20.get(0).deref());
    return 0;
  }

  @Method(0x800168b0L)
  public static long scriptXor(final RunningScript a0) {
    a0.params_20.get(1).deref().xor(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800168d4L)
  public static long scriptAndOr(final RunningScript a0) {
    a0.params_20.get(2).deref()
      .and(a0.params_20.get(0).deref().get())
      .or(a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x80016900L)
  public static long scriptNot(final RunningScript a0) {
    a0.params_20.get(0).deref().not();
    return 0;
  }

  /**
   * Shift work array value 1 left by value 0 bits
   *
   * @return 0
   */
  @Method(0x80016920L)
  public static long scriptShiftLeft(final RunningScript a0) {
    a0.params_20.get(1).deref().shl(a0.params_20.get(0).deref());
    return 0;
  }

  /**
   * Shift work array value 1 right (arithmetic) by value 0 bits
   *
   * @return 0
   */
  @Method(0x80016944L)
  public static long scriptShiftRightArithmetic(final RunningScript a0) {
    a0.params_20.get(1).deref().set(a0.params_20.get(1).deref().get() >> a0.params_20.get(0).deref().get());
    return 0;
  }

  /**
   * Increment work array value 1 by value 0 (overflow allowed)
   *
   * @return 0
   */
  @Method(0x80016968L)
  public static long scriptAdd(final RunningScript a0) {
    a0.params_20.get(1).deref().add(a0.params_20.get(0).deref());
    return 0;
  }

  /**
   * Decrement work array value 1 by value 0 (overflow allowed)
   *
   * @return 0
   */
  @Method(0x8001698cL)
  public static long scriptSubtract(final RunningScript a0) {
    a0.params_20.get(1).deref().sub(a0.params_20.get(0).deref());
    return 0;
  }

  @Method(0x800169b0L)
  public static long scriptSubtract2(final RunningScript a0) {
    a0.params_20.get(1).deref().set(a0.params_20.get(0).deref().get() - a0.params_20.get(1).deref().get());
    return 0;
  }

  /**
   * Increment work array value 0 by 1
   *
   * @return 0
   */
  @Method(0x800169d4L)
  public static long scriptIncrementBy1(final RunningScript a0) {
    a0.params_20.get(0).deref().incr();
    return 0;
  }

  /**
   * Decrement work array value 0 by 1
   *
   * @return 0
   */
  @Method(0x800169f4L)
  public static long scriptDecrementBy1(final RunningScript a0) {
    a0.params_20.get(0).deref().decr();
    return 0;
  }

  @Method(0x80016a14L)
  public static long scriptNegate(final RunningScript a0) {
    a0.params_20.get(0).deref().set(-a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x80016a34L)
  public static long scriptAbs(final RunningScript a0) {
    //LAB_80016a50
    a0.params_20.get(0).deref().abs();
    return 0;
  }

  /**
   * Multiply work array value 1 by value 0 (overflow allowed)
   *
   * @return 0
   */
  @Method(0x80016a5cL)
  public static long scriptMultiply(final RunningScript a0) {
    a0.params_20.get(1).deref().mul(a0.params_20.get(0).deref());
    return 0;
  }

  /**
   * Divide work array value 1 by value 0
   *
   * @return 0
   */
  @Method(0x80016a84L)
  public static long scriptDivide(final RunningScript a0) {
    final int divisor = a0.params_20.get(0).deref().get();

    if(divisor == 0) {
      if(a0.params_20.get(1).deref().get() >= 0) {
        a0.params_20.get(1).deref().set(-1);
      } else {
        a0.params_20.get(1).deref().set(1);
      }

      return 0;
    }

    a0.params_20.get(1).deref().div(divisor);
    return 0;
  }

  @Method(0x80016ab0L)
  public static long scriptDivide2(final RunningScript a0) {
    a0.params_20.get(1).deref().set(a0.params_20.get(0).deref().get() / a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x80016adcL)
  public static long scriptMod(final RunningScript a0) {
    a0.params_20.get(1).deref().mod(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x80016b04L)
  public static long scriptMod2(final RunningScript a0) {
    a0.params_20.get(1).deref().set(a0.params_20.get(0).deref().get() % a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x80016b2cL)
  public static long FUN_80016b2c(final RunningScript a0) {
    a0.params_20.get(1).deref().set((a0.params_20.get(1).deref().get() >> 4) * (a0.params_20.get(0).deref().get() >> 4) >> 4);
    return 0;
  }

  @Method(0x80016b5cL)
  public static long FUN_80016b5c(final RunningScript a0) {
    a0.params_20.get(1).deref().set(((a0.params_20.get(1).deref().get() << 4) / a0.params_20.get(0).deref().get()) << 8);
    return 0;
  }

  @Method(0x80016b8cL)
  public static long FUN_80016b8c(final RunningScript a0) {
    a0.params_20.get(1).deref().set(((a0.params_20.get(0).deref().get() << 4) / a0.params_20.get(1).deref().get()) << 8);
    return 0;
  }

  /**
   * Calculate square root of work array value 0 and store in value 1
   *
   * @return 0
   */
  @Method(0x80016bbcL)
  public static long scriptSquareRoot(final RunningScript a0) {
    a0.params_20.get(1).deref().set(SquareRoot0(a0.params_20.get(0).deref().get()));
    return 0;
  }

  @Method(0x80016c00L)
  public static long FUN_80016c00(final RunningScript a0) {
    a0.params_20.get(1).deref().set(a0.params_20.get(0).deref().get() * simpleRand() >>> 16);
    return 0;
  }

  @Method(0x80016c4cL)
  public static long scriptSin(final RunningScript a0) {
    a0.params_20.get(1).deref().set((int)sin_cos_80054d0c.offset(2, (a0.params_20.get(0).deref().get() & 0xfffL) * 0x4L).getSigned());
    return 0;
  }

  @Method(0x80016c80L)
  public static long scriptCos(final RunningScript a0) {
    a0.params_20.get(1).deref().set((int)sin_cos_80054d0c.offset(2, (a0.params_20.get(0).deref().get() & 0xfffL) * 0x4L).offset(0x2L).getSigned());
    return 0;
  }

  @Method(0x80016cb4L)
  public static long scriptRatan2(final RunningScript a0) {
    a0.params_20.get(2).deref().set(ratan2(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get()));
    return 0;
  }

  /**
   * Executes the sub-function at {@link legend.game.Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} denoted by the parent param
   *
   * @return The value that the sub-function returns
   */
  @Method(0x80016cfcL)
  public static long scriptExecuteSubFunc(final RunningScript a0) {
    try {
      return scriptSubFunctions_8004e29c.get((int)a0.opParam_18.get()).deref().run(a0);
    } catch(final UnsupportedOperationException e) {
      throw new RuntimeException("Script subfunc %d error".formatted(a0.opParam_18.get()), e);
    }
  }

  /**
   * Jump to the value at work array element 0
   *
   * @return 0
   */
  @Method(0x80016d38L)
  public static long scriptJump(final RunningScript a0) {
    a0.commandPtr_0c.set(a0.params_20.get(0).deref());
    return 0;
  }

  /**
   * <p>Compares value at work array element 0 to element 1 using operation denoted by parent param. If true, jumps to value at element 2.</p>
   *
   * <p>
   *   Operations:
   *   <ol start="0">
   *     <li>Less than or equal to</li>
   *     <li>Less than</li>
   *     <li>Equal</li>
   *     <li>Inequal</li>
   *     <li>Greater than</li>
   *     <li>Greater than or equal to</li>
   *     <li>And</li>
   *     <li>Nand</li>
   *   </ol>
   * </p>
   *
   * @return 0
   */
  @Method(0x80016d4cL)
  public static long scriptConditionalJump(final RunningScript a0) {
    if(scriptCompare(a0, a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.opParam_18.get()) != 0) {
      a0.commandPtr_0c.set(a0.params_20.get(2).deref());
    }

    //LAB_80016d8c
    return 0;
  }


  /**
   * <p>Compares constant 0 to work array element 0 using operation denoted by parent param. If true, jumps to value at element 1.</p>
   *
   * <p>
   *   Operations:
   *   <ol start="0">
   *     <li>Less than or equal to</li>
   *     <li>Less than</li>
   *     <li>Equal</li>
   *     <li>Inequal</li>
   *     <li>Greater than</li>
   *     <li>Greater than or equal to</li>
   *     <li>And</li>
   *     <li>Nand</li>
   *   </ol>
   * </p>
   *
   * @return 0
   */
  @Method(0x80016da0L)
  public static long scriptConditionalJump0(final RunningScript a0) {
    if(scriptCompare(a0, 0, a0.params_20.get(0).deref().get(), a0.opParam_18.get()) != 0) {
      a0.commandPtr_0c.set(a0.params_20.get(1).deref());
    }

    //LAB_80016dd8
    return 0;
  }

  /**
   * Decrements param0 and jumps to param1 if param0 > 0... maybe used for do...while loops?
   */
  @Method(0x80016decL)
  public static long FUN_80016dec(final RunningScript a0) {
    a0.params_20.get(0).deref().decr();

    if(a0.params_20.get(0).deref().get() != 0) {
      a0.commandPtr_0c.set(a0.params_20.get(1).deref());
    }

    //LAB_80016e14
    return 0;
  }

  @Method(0x80016e1cL)
  public static long FUN_80016e1c(final RunningScript a0) {
    a0.commandPtr_0c.set(MEMORY.ref(4, a0.params_20.get(1).getPointer()).offset(MEMORY.ref(4, a0.params_20.get(1).getPointer()).offset(a0.params_20.get(0).deref().get() * 0x4L).get() * 0x4L).cast(IntRef::new));
    return 0;
  }

  /**
   * Pushes the current command to the command stack and jumps to the value at work array element 0.
   *
   * @return 0
   */
  @Method(0x80016e50L)
  public static long scriptJumpAndLink(final RunningScript a0) {
    final ScriptState<?> struct = a0.scriptState_04.deref();

    for(int i = struct.commandStack_1c.length() - 1; i > 0; i--) {
      struct.commandStack_1c.get(i).setNullable(struct.commandStack_1c.get(i - 1).derefNullable());
    }

    struct.commandStack_1c.get(0).set(a0.commandPtr_0c.deref());
    a0.commandPtr_0c.set(a0.params_20.get(0).deref());

    return 0;
  }

  /**
   * Return from a JumpAndLink
   *
   * @return 0
   */
  @Method(0x80016f28L)
  public static long scriptJumpReturn(final RunningScript a0) {
    final ScriptState<?> struct = a0.scriptState_04.deref();

    a0.commandPtr_0c.set(struct.commandStack_1c.get(0).deref());

    for(int i = 0; i < struct.commandStack_1c.length() - 1; i++) {
      struct.commandStack_1c.get(i).setNullable(struct.commandStack_1c.get(i + 1).derefNullable());
    }

    struct.commandStack_1c.get(struct.commandStack_1c.length() - 1).clear();

    return 0;
  }

  @Method(0x80016ffcL)
  public static long scriptJumpAndLinkTable(final RunningScript a0) {
    final ScriptState<?> struct = a0.scriptState_04.deref();

    for(int i = struct.commandStack_1c.length() - 1; i > 0; i--) {
      struct.commandStack_1c.get(i).setNullable(struct.commandStack_1c.get(i - 1).derefNullable());
    }

    struct.commandStack_1c.get(0).set(a0.commandPtr_0c.deref());

    // Equivalent to a + a[b] * 4
    final long v1 = a0.params_20.get(1).getPointer();
    a0.commandPtr_0c.set(MEMORY.ref(4, v1 + MEMORY.ref(4, v1).offset(a0.params_20.get(0).deref().get() * 0x4L).getSigned() * 0x4L, IntRef::new));
    return 0;
  }

  @Method(0x800170f4L)
  public static long scriptDeallocateSelf(final RunningScript a0) {
    deallocateScriptChildren(a0.scriptStateIndex_00.get());
    deallocateScriptState(a0.scriptStateIndex_00.get());
    return 0x2L;
  }

  @Method(0x80017138L)
  public static long scriptDeallocateChildren(final RunningScript a0) {
    deallocateScriptAndChildren(a0.scriptStateIndex_00.get());
    return 2;
  }

  @Method(0x80017160L)
  public static long scriptDeallocateOther(final RunningScript a0) {
    deallocateScriptChildren(a0.params_20.get(0).deref().get());
    deallocateScriptState(a0.params_20.get(0).deref().get());
    return a0.params_20.get(0).deref().get() == a0.scriptStateIndex_00.get() ? 2 : 0;
  }

  /** Forks the script and jumps to an address */
  @Method(0x800171c0L)
  public static long scriptForkAndJump(final RunningScript a0) {
    scriptFork(a0.params_20.get(0).deref().get());
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref();
    state.commandPtr_18.set(a0.params_20.get(1).deref());
    state._c4.set(a0.params_20.get(2).deref().get());
    return 0;
  }

  /** Forks the script and jumps to an entry point */
  @Method(0x80017234L)
  public static long scriptForkAndReenter(final RunningScript s0) {
    scriptFork(s0.params_20.get(0).deref().get());
    final ScriptState<?> a0 = scriptStatePtrArr_800bc1c0.get(s0.params_20.get(0).deref().get()).deref();
    a0.commandPtr_18.set(a0.scriptPtr_14.deref().offsetArr_00.get(s0.params_20.get(1).deref().get()).deref());
    a0._c4.set(s0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x800172c0L)
  public static long scriptConsumeChild(final RunningScript a0) {
    a0.commandPtr_0c.set(scriptConsumeChild(a0.scriptStateIndex_00.get()));
    return 0;
  }

  @Method(0x800172f4L)
  public static long FUN_800172f4(final RunningScript a0) {
    return 0;
  }

  @Method(0x800172fcL)
  public static long FUN_800172fc(final RunningScript a0) {
    return 0;
  }

  @Method(0x80017304L)
  public static long FUN_80017304(final RunningScript a0) {
    return 0;
  }

  @Method(0x8001730cL)
  public static long FUN_8001730c(final RunningScript a0) {
    //LAB_80017314
    int i;
    for(i = 0; i < 10; i++) {
      if(a0.scriptState_04.deref().commandStack_1c.get(i).isNull()) {
        break;
      }
    }

    //LAB_80017338
    a0.params_20.get(0).deref().set(i);
    return 0;
  }

  @Method(0x8001734cL)
  public static long scriptRewindAndPause2(final RunningScript a0) {
    return 2;
  }

  @Method(0x80017354L)
  public static long FUN_80017354(final RunningScript a0) {
    gameState_800babc8.indicatorsDisabled_4e3.set(a0.params_20.get(0).deref().get() != 0 ? 1 : 0);
    return 0;
  }

  @Method(0x80017374L)
  public static long FUN_80017374(final RunningScript a0) {
    a0.params_20.get(0).deref().set(gameState_800babc8.indicatorsDisabled_4e3.get() != 0 ? 1 : 0);
    return 0;
  }

  /**
   * <p>Sets or clears a bit in the flags 1 array at {@link Scus94491BpeSegment_800b#gameState_800babc8#scriptFlags1_13c}.</p>
   * <p>If work array element 1 is non-zero, the bit is set. If it's 0, the bit is cleared.</p>
   * <p>The lower 5 bits of work array element 0 is what bit to set (i.e. 1 << n), and the upper 3 bits is the index into the array.</p>
   *
   * @return 0
   */
  @Method(0x80017390L)
  public static long scriptSetGlobalFlag1(final RunningScript a0) {
    final long shift = a0.params_20.get(0).deref().get() & 0x1fL;
    final int index = a0.params_20.get(0).deref().get() >>> 5;

    final ArrayRef<UnsignedIntRef> flags;
    if(index < 8) {
      flags = gameState_800babc8.scriptFlags1_13c;
    } else if(index < 16) {
      flags = gameState_800babc8._15c;
    } else {
      throw new RuntimeException("Are there more flags?");
    }

    if(a0.params_20.get(1).deref().get() != 0) {
      flags.get(index % 8).or(0x1L << shift);
    } else {
      //LAB_800173dc
      flags.get(index % 8).and(~(0x1L << shift));
    }

    //LAB_800173f4
    return 0;
  }

  /**
   * <p>Reads a bit in the flags 1 array at {@link Scus94491BpeSegment_800b#gameState_800babc8#scriptFlags1_13c}.</p>
   * <p>If the flag is set, a 1 is stored as the value of the work array element 1; otherwise, 0 is stored.</p>
   * <p>The lower 5 bits of work array element 0 is what bit to read (i.e. 1 << n), and the upper 3 bits is the index into the array.</p>
   *
   * @return 0
   */
  @Method(0x800173fcL)
  public static long scriptReadGlobalFlag1(final RunningScript a0) {
    final int value = a0.params_20.get(0).deref().get();

    // This is a fix for a bug in one of the game scripts - it ends up reading a flag that's massively out of bounds
    if(value == -1) {
      a0.params_20.get(1).deref().set(0);
      return 0;
    }

    final int shift = value & 0x1f;
    final int index = value >>> 5;

    a0.params_20.get(1).deref().set((gameState_800babc8.scriptFlags1_13c.get(index).get() & 0x1L << shift) != 0 ? 1 : 0);

    return 0;
  }

  @Method(0x80017440L)
  public static long scriptSetGlobalFlag2(final RunningScript a0) {
    final long a1 = a0.params_20.get(0).deref().get() & 0x1fL;
    final long a2 = a0.params_20.get(0).deref().get() >>> 5;

    if(a0.params_20.get(1).deref().get() != 0) {
      gameState_800babc8.scriptFlags2_bc.get((int)a2).or(0x1L << a1);
    } else {
      //LAB_8001748c
      gameState_800babc8.scriptFlags2_bc.get((int)a2).and(~(0x1L << a1));
    }

    //LAB_800174a4
    if((gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 != 0) {
      final CharacterData2c charData = gameState_800babc8.charData_32c.get(0);
      charData.dlevelXp_0e.set(0x7fff);
      charData.dlevel_13.set(0x5);
    }

    //LAB_800174d0
    return 0;
  }

  /**
   * <p>Reads a bit in the flags 2 array at {@link Scus94491BpeSegment_800b#gameState_800babc8#scriptFlags2_bc}.</p>
   * <p>If the flag is set, a 1 is stored as the value of the work array element 1; otherwise, 0 is stored.</p>
   * <p>The lower 5 bits of work array element 0 is what bit to read (i.e. 1 << n), and the upper 3 bits is the index into the array.</p>
   *
   * @return 0
   */
  @Method(0x800174d8L)
  public static long scriptReadGlobalFlag2(final RunningScript a0) {
    final long shift = a0.params_20.get(0).deref().get() & 0x1fL;
    final int index = a0.params_20.get(0).deref().get() >>> 5;

    a0.params_20.get(1).deref().set((gameState_800babc8.scriptFlags2_bc.get(index).get() & 0x1L << shift) != 0 ? 1 : 0);

    return 0;
  }

  @Method(0x8001751cL)
  public static long scriptStartEffect(final RunningScript a0) {
    scriptStartEffect(a0.params_20.get(0).deref().get(), Math.max(1, a0.params_20.get(1).deref().get()));
    return 0;
  }

  @Method(0x80017564L)
  public static long FUN_80017564(final RunningScript a0) {
    if(fileCount_8004ddc8.get() == 0) {
      return 1;
    }

    return 2;
  }

  @Method(0x80017584L)
  public static long FUN_80017584(final RunningScript a0) {
    FUN_8002bb38(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800175b4L)
  public static long FUN_800175b4(final RunningScript a0) {
    final long shift = a0.params_20.get(1).deref().get() & 0x1fL;
    final long index = a0.params_20.get(1).deref().get() >>> 5;

    if(a0.params_20.get(2).deref().get() != 0) {
      MEMORY.ref(4, a0.params_20.get(0).getPointer()).offset(index * 0x4L).oru(0x1L << shift);
    } else {
      //LAB_800175fc
      MEMORY.ref(4, a0.params_20.get(0).getPointer()).offset(index * 0x4L).and(~(0x1L << shift));
    }

    //LAB_80017614
    if((int)gameState_800babc8.dragoonSpirits_19c.get(0).get() < 0) {
      gameState_800babc8.charData_32c.get(0).dlevel_13.set(5);
      gameState_800babc8.charData_32c.get(0).dlevelXp_0e.set(0x7fff);
    }

    //LAB_80017640
    return 0;
  }

  @Method(0x80017648L)
  public static long FUN_80017648(final RunningScript a0) {
    final long shift = a0.params_20.get(1).deref().get() & 0x1fL;
    final long index = a0.params_20.get(1).deref().get() >>> 5;

    a0.params_20.get(2).deref().set((MEMORY.ref(4, a0.params_20.get(0).getPointer()).offset(index * 0x4L).get() & 0x1L << shift) > 0 ? 1 : 0);

    return 0;
  }

  @Method(0x80017688L)
  public static long FUN_80017688(final RunningScript a0) {
    FUN_8002bda4(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x800176c0L)
  public static long FUN_800176c0(final RunningScript a0) {
    FUN_8002c178(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800176ecL)
  public static long FUN_800176ec(final RunningScript a0) {
    FUN_8002c184();
    return 0;
  }

  @Method(0x8001770cL)
  public static void executeScriptTickers() {
    if(scriptsTickDisabled_800bc0b8.get() || scriptsDisabled_800bc0b9.get()) {
      return;
    }

    //LAB_80017750
    for(int i = 0; i < 0x48; i++) {
      final ScriptState<MemoryRef> scriptState = (ScriptState<MemoryRef>)scriptStatePtrArr_800bc1c0.get(i).deref();
      if(scriptState.getAddress() != scriptState_800bc0c0.getAddress()) {
        if((scriptState.ui_60.get() & 0x14_0000L) == 0) {
          scriptState.ticker_04.deref().run(i, scriptState, scriptState.innerStruct_00.derefNullable());
        }
      }

      //LAB_80017788
    }

    //LAB_800177ac
    for(int i = 0; i < 0x48; i++) {
      final ScriptState<MemoryRef> scriptState = (ScriptState<MemoryRef>)scriptStatePtrArr_800bc1c0.get(i).deref();
      if(scriptState.getAddress() != scriptState_800bc0c0.getAddress()) {
        if((scriptState.ui_60.get() & 0x410_0000L) == 0x400_0000L) {
          if(scriptState.tempTicker_10.deref().run(i, scriptState, scriptState.innerStruct_00.derefNullable()) != 0) {
            setScriptTempTicker(i, null);
          }
        }
      }

      //LAB_800177f8
    }

    //LAB_80017808
  }

  @Method(0x80017820L)
  public static void executeScriptRenderers() {
    if(scriptsDisabled_800bc0b9.get()) {
      return;
    }

    //LAB_80017854
    for(int i = 0; i < 0x48; i++) {
      final ScriptState<MemoryRef> scriptState = (ScriptState<MemoryRef>)scriptStatePtrArr_800bc1c0.get(i).deref();
      if(scriptState.getAddress() != scriptState_800bc0c0.getAddress()) {
        if((scriptState.ui_60.get() & 0x18_0000L) == 0) {
          scriptState.renderer_08.deref().run(i, scriptState, scriptState.innerStruct_00.derefNullable());
        }
      }

      //LAB_80017888
    }

    //LAB_80017898
  }

  @Method(0x800178b0L)
  public static void loadSceaLogo() {
    if(SInitBinLoaded_800bbad0.get()) {
      loadDrgnBinFile(0, 5737, 0, getMethodAddress(Scus94491BpeSegment.class, "loadSceaLogoTexture", long.class, long.class, long.class), 1, 5);
    } else {
      loadSceaLogoTexture(sceaTexture_800d05c4.getAddress(), 0, 0);
    }
  }

  @Method(0x80017924L)
  public static void loadSceaLogoTexture(final long address, final long param_2, final long param_3) {
    timHeader_800bc2e0.set(parseTimHeader(MEMORY.ref(4, address).offset(0x4L)));
    final TimHeader header = timHeader_800bc2e0;

    final RECT imageRect = new RECT();
    imageRect.set((short)640, (short)0, header.getImageRect().w.get(), header.getImageRect().h.get());
    LoadImage(imageRect, header.getImageAddress());

    if(header.hasClut()) {
      final RECT clutRect = new RECT();
      clutRect.set((short)640, (short)255, header.getClutRect().w.get(), header.getClutRect().h.get());
      LoadImage(clutRect, header.getClutAddress());
    }

    if(param_3 != 0) {
      FUN_800127cc(address, 0, 1);
    }
  }

  /**
   * Draws the TIM image located at {@link Scus94491BpeSegment_800b#timHeader_800bc2e0}
   *
   * NOTE: elements are added in reverse order
   */
  @Method(0x80017a3cL)
  public static void drawSceaLogo(final int colour) {
    final TimHeader tim = timHeader_800bc2e0;

    final GpuCommandQuad packet = new GpuCommandQuad();
    packet.bpp(Bpp.BITS_8);
    packet.monochrome(colour);
    packet.pos(-tim.getImageRect().w.get(), -tim.getImageRect().h.get() / 2, tim.getImageRect().w.get() * 2, tim.getImageRect().h.get());
    packet.clut(640, 255);
    packet.vramPos(640, 0);
    GPU.queueCommand(41, packet);
  }

  /**
   * Flags:
   * bit 1: deallocate archive after decompression
   * bit 3: allocate on head instead of tail
   */
  @Method(0x80017fe4L)
  public static long decompress(final long archiveAddress, final long destinationAddress, final long callback, final long callbackParam, final long flags) {
    final long address;
    if(destinationAddress == 0) {
      if((flags & 0x4L) != 0) {
        address = mallocHead(MEMORY.ref(4, archiveAddress).get());
      } else {
        //LAB_8001803c
        address = mallocTail(MEMORY.ref(4, archiveAddress).get());
      }

      //LAB_8001804c
      if(address == 0) {
        return -0x1L;
      }
    } else {
      address = destinationAddress;
    }

    //LAB_8001805c
    //LAB_80018060
    final long size = Scus94491.decompress(archiveAddress, address);

    if((flags & 0x1L) != 0) {
      free(archiveAddress);
    }

    //LAB_80018088
    MEMORY.ref(4, callback).call(address, size, callbackParam);

    //LAB_8001809c
    return 0;
  }

  @Method(0x800180c0L)
  public static long loadMcq(final McqHeader mcq, final long x, final long y) {
    if((x & 0x3fL) != 0 || (y & 0xffL) != 0) {
      //LAB_800180e0
      throw new RuntimeException("Invalid MCQ");
    }

    //LAB_800180e8
    if(mcq.magic_00.get() != McqHeader.MAGIC_1 && mcq.magic_00.get() != McqHeader.MAGIC_2) {
      throw new RuntimeException("Invalid MCQ");
    }

    //LAB_80018104
    if(mcq.vramHeight_0a.get() != 256) {
      throw new RuntimeException("Invalid MCQ");
    }

    LoadImage(new RECT((short)x, (short)y, mcq.vramWidth_08.get(), mcq.vramHeight_0a.get()), mcq.getImageDataAddress());

    //LAB_8001813c
    return 0;
  }

  @Method(0x8001814cL)
  public static void renderMcq(final McqHeader mcq, final int vramOffsetX, final int vramOffsetY, int x, int y, final int z, final int colour) {
    final int width = mcq.screenWidth_14.get();
    final int height = mcq.screenHeight_16.get();
    int clutX = mcq.clutX_0c.get() + vramOffsetX;
    int clutY = mcq.clutY_0e.get() + vramOffsetY;
    int u = mcq.u_10.get() + vramOffsetX;
    int v = mcq.v_12.get() + vramOffsetY;
    int vramX = u & 0x3c0;
    final int vramY = v & 0x100;
    u = u * 4 & 0xfc;

    if(mcq.magic_00.get() == McqHeader.MAGIC_2) {
      x += mcq.screenOffsetX_28.get();
      y += mcq.screenOffsetY_2a.get();
    }

    //LAB_800181e4
    //LAB_80018350
    //LAB_8001836c
    for(int chunkX = 0; chunkX < width; chunkX += 16) {
      //LAB_80018380
      for(int chunkY = 0; chunkY < height; chunkY += 16) {
        GPU.queueCommand(z, new GpuCommandQuad()
          .bpp(Bpp.BITS_4)
          .translucent(Translucency.HALF_B_PLUS_HALF_F)
          .clut(clutX, clutY)
          .vramPos(vramX, vramY)
          .monochrome(colour)
          .pos(x + chunkX, y + chunkY, 16, 16)
          .uv(u, v)
        );

        v = v + 16 & 0xf0;

        if(v == 0) {
          u = u + 16 & 0xf0;

          if(u == 0) {
            vramX = vramX + 64;
          }
        }

        //LAB_80018434
        clutY = clutY + 1 & 0xff;

        if(clutY == 0) {
          clutX = clutX + 16;
        }

        //LAB_80018444
        clutY = clutY | vramY;
      }
    }

    //LAB_80018464
    //LAB_8001846c
  }

  @Method(0x80018508L)
  public static void FUN_80018508() {
    if(whichMenu_800bdc38.get() != 0) {
      FUN_80022590();
    }

    //LAB_8001852c
    if(_800bc91c.get() != 0x5L || _800bc974.get() == 0x3L) {
      //LAB_80018550
      if(whichMenu_800bdc38.get() == 0) {
        pregameLoadingStage_800bb10c.addu(0x1L);
      }
    } else {
      //LAB_80018574
      final long v1 = _8004f2a8.get();

      if(v1 == 0xaL) {
        //LAB_800185a8
        loadGameStateOverlay(5);
        _8004f2a8.addu(0x1L);
      } else if(v1 == 0xbL) {
        //LAB_800185c4
        if(loadingGameStateOverlay_8004dd08.get() == 0) {
          _8004f2a8.setu(0xcL);
        }
      } else if(v1 == 0xcL) {
        //LAB_800185e0
        FUN_800e5934();

        if(whichMenu_800bdc38.get() == 0) {
          _8004dd0c.setu(0x1L);
          _8004f2a8.setu(0);
          pregameLoadingStage_800bb10c.addu(0x1L);
        }
      } else {
        //LAB_80018618
        if(fileCount_8004ddc8.get() != 0) {
          _8004f2a8.setu(0);
        } else {
          //LAB_80018630
          _8004f2a8.addu(0x1L);
        }
      }
    }

    //LAB_80018644
  }

  @Method(0x80018658L)
  public static void FUN_80018658() {
    FUN_800186a0();
  }

  @Method(0x800186a0L)
  public static void FUN_800186a0() {
    if(_800bc94c.get() != 0) {
      FUN_80018744();
      _8004f5d4.get((int)pregameLoadingStage_800bb10c.get()).deref().run();

      if(_800bc94c.get() != 0) {
        FUN_8001890c();
      }
    } else {
      //LAB_8001870c
      _8004f5d4.get((int)pregameLoadingStage_800bb10c.get()).deref().run();
    }

    //LAB_80018734
  }

  @Method(0x80018744L)
  public static void FUN_80018744() {
    if((_800bc960.get() & 0x400L) != 0) {
      if((_800bc960.get() & 0x8L) == 0 && FUN_800187cc() != 0) {
        _800bc960.oru(0x8L);
      }

      //LAB_80018790
      if((_800bc960.get() & 0x4L) == 0 && FUN_8001886c() != 0) {
        _800bc960.oru(0x4L);
      }
    }

    //LAB_800187b0
    FUN_800c7304();
  }

  @Method(0x800187ccL)
  public static long FUN_800187cc() {
    //LAB_80018800
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      if(FUN_800c90b0(scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(charSlot).get()).deref().innerStruct_00.derefAs(BattleObject27c.class).combatantIndex_26c.get()) == 0) {
        return 0;
      }
    }

    //LAB_80018850
    //LAB_80018854
    return 1;
  }

  @Method(0x8001886cL)
  public static long FUN_8001886c() {
    //LAB_800188a0
    for(int i = 0; i < _800c6768.get(); i++) {
      if(FUN_800c90b0(scriptStatePtrArr_800bc1c0.get(_8006e398.bobjIndices_e50.get(i).get()).deref().innerStruct_00.derefAs(BattleObject27c.class).combatantIndex_26c.get()) == 0) {
        return 0;
      }
    }

    //LAB_800188f0
    //LAB_800188f4
    return 1;
  }

  @Method(0x8001890cL)
  public static void FUN_8001890c() {
    FUN_800d8f10();
    FUN_800c7304();
    FUN_800c8cf0();
    FUN_800c882c();
  }

  @Method(0x80018944L)
  public static void waitForFilesToLoad() {
    if(fileCount_8004ddc8.get() == 0 && overlayQueueIndex_8004dd14.get() == overlayQueueIndex_8004dd18.get() && loadingGameStateOverlay_8004dd08.get() == 0) {
      pregameLoadingStage_800bb10c.addu(0x1L);
    }

    //LAB_80018990
  }

  @Method(0x80018998L)
  public static void FUN_80018998() {
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800189b0L)
  public static void FUN_800189b0() {
    if(_800bc91c.get() == 0x5L && _800bc974.get() != 0x3L) {
      FUN_800e5934();
    }

    //LAB_800189e4
    //LAB_800189e8
    if(fileCount_8004ddc8.get() == 0 && overlayQueueIndex_8004dd14.get() == overlayQueueIndex_8004dd18.get() && loadingGameStateOverlay_8004dd08.get() == 0) {
      FUN_800201c8(0x6L);
      pregameLoadingStage_800bb10c.setu(0);
      vsyncMode_8007a3b8.setu(0x2L);
      mainCallbackIndexOnceLoaded_8004dd24.setu(_800bc91c.get());
    }

    //LAB_80018a4c
  }

  @Method(0x80018a5cL)
  public static void FUN_80018a5c(final long x, final long y, final long s4, final long a3, final long a4, final long a5, final long a6, @Nullable Translucency transMode, final COLOUR colour, final long a9, final long a10) {
    final long w = Math.abs(a4 - s4);
    final long h = Math.abs(a5 - a3);
    final long packet = gpuPacketAddr_1f8003d8.get();
    setGp0_2c(packet);
    gpuPacketAddr_1f8003d8.addu(0x28L);

    if(transMode == null) {
      gpuLinkedListSetCommandTransparency(packet, false);
      transMode = Translucency.HALF_B_PLUS_HALF_F;
    } else {
      //LAB_80018b30
      gpuLinkedListSetCommandTransparency(packet, true);
    }

    //LAB_80018b38
    MEMORY.ref(1, packet).offset(0x4L).setu(colour.getR());
    MEMORY.ref(1, packet).offset(0x5L).setu(colour.getG());
    MEMORY.ref(1, packet).offset(0x6L).setu(colour.getB());

    if((short)a9 != 0x1000 || (short)a10 != (short)a9) {
      //LAB_80018b90
      final long sp10 = x + (short)w / 2;
      final long sp12 = y + (short)h / 2;
      final long a2 = (short)w * (short)a9 >> 13;
      final long a1 = (short)h * (short)a10 >> 13;
      MEMORY.ref(2, packet).offset(0x08L).setu(sp10 - a2);
      MEMORY.ref(2, packet).offset(0x0aL).setu(sp12 - a1);
      MEMORY.ref(2, packet).offset(0x10L).setu(sp10 + a2);
      MEMORY.ref(2, packet).offset(0x12L).setu(sp12 - a1);
      MEMORY.ref(2, packet).offset(0x18L).setu(sp10 - a2);
      MEMORY.ref(2, packet).offset(0x1aL).setu(sp12 + a1);
      MEMORY.ref(2, packet).offset(0x20L).setu(sp10 + a2);
      MEMORY.ref(2, packet).offset(0x22L).setu(sp12 + a1);
    } else {
      //LAB_80018c38
      MEMORY.ref(2, packet).offset(0x08L).setu(x);
      MEMORY.ref(2, packet).offset(0x0aL).setu(y);
      MEMORY.ref(2, packet).offset(0x10L).setu(x + w);
      MEMORY.ref(2, packet).offset(0x12L).setu(y);
      MEMORY.ref(2, packet).offset(0x18L).setu(x);
      MEMORY.ref(2, packet).offset(0x1aL).setu(y + h);
      MEMORY.ref(2, packet).offset(0x20L).setu(x + w);
      MEMORY.ref(2, packet).offset(0x22L).setu(y + h);
    }

    //LAB_80018c60
    MEMORY.ref(1, packet).offset(0x0cL).setu(s4);
    MEMORY.ref(1, packet).offset(0x0dL).setu(a3);
    MEMORY.ref(1, packet).offset(0x14L).setu(a4);
    MEMORY.ref(1, packet).offset(0x15L).setu(a3);
    MEMORY.ref(1, packet).offset(0x1cL).setu(s4);
    MEMORY.ref(1, packet).offset(0x1dL).setu(a5);
    MEMORY.ref(1, packet).offset(0x24L).setu(a4);
    MEMORY.ref(1, packet).offset(0x25L).setu(a5);

    final int a2 = (short)a6 / 16;
    final int a1 = (short)a6 % 16;
    final int clutY;
    final int clutX;
    if(a2 >= 4) {
      clutX = (a2 << 4) + 832;
      clutY = a1 + 304;
    } else {
      clutX = (a2 << 4) + 704;
      clutY = a1 + 496;
    }

    //LAB_80018cf0
    MEMORY.ref(2, packet).offset(0x0eL).setu(clutY << 6 | (clutX & 0x3f0) >> 4);
    MEMORY.ref(2, packet).offset(0x16L).setu(GetTPage(Bpp.BITS_4, transMode, 704, 256));
    queueGpuPacket(tags_1f8003d0.deref().get(2).getAddress(), packet);
  }

  /**Some kind of intermediate rect render method**/
  @Method(0x80018d60L)
  public static void FUN_80018d60(final long displayX, final long displayY, final long originU, final long originV, final long width, final long height, final long a6, @Nullable final Translucency transMode, final COLOUR colour, final long a9) {
    FUN_80018a5c((short)displayX, (short)displayY, originU & 0xffL, originV & 0xffL, originU + width & 0xffL, originV + height & 0xffL, (short)a6, transMode, colour, (short)a9, (short)a9);
  }

  @Method(0x80018decL)
  public static void FUN_80018dec(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, @Nullable final Translucency transMode, final COLOUR colour, final long a9, final long a10) {
    FUN_80018a5c(a0, a1, a2, a3, a2 + a4, a5 + a3, a6, transMode, colour, a9, a10);
  }

  @Method(0x80018e84L)
  public static void FUN_80018e84() {
    long v0;
    long v1;
    long a0;
    long s0;
    long s1;
    long s2;
    final long[] sp0x30 = {0x42L, 0x43L, _80010320.offset(0x0L).get(), _80010320.offset(0x4L).get()};

    //LAB_80018f04
    s1 = _8004f658.get();
    while(s1 != 0) {
      do {
        MEMORY.ref(1, s1).offset(0x3L).subu(0x1L);

        if(MEMORY.ref(1, s1).offset(0x3L).get() != 0) {
          s2 = 0;
          break;
        }

        v0 = MEMORY.ref(4, s1).offset(0x8L).get();

        if(v0 == 0) {
          //LAB_80018f48
          _8004f658.setu(MEMORY.ref(4, s1).offset(0xcL).get());
        } else {
          //LAB_80018f50
          MEMORY.ref(4, v0).offset(0xcL).setu(0);
        }

        //LAB_80018f54
        s0 = MEMORY.ref(4, s1).offset(0xcL).get();

        if(s0 != 0) {
          if(MEMORY.ref(4, s1).offset(0x8L).get() == 0) {
            MEMORY.ref(4, s0).offset(0x8L).setu(0);
            _8004f658.setu(MEMORY.ref(4, s1).offset(0xcL).get());
          } else {
            //LAB_80018f84
            v1 = MEMORY.ref(4, s1).offset(0xcL).get();
            MEMORY.ref(4, v1).offset(0x8L).setu(MEMORY.ref(4, s1).offset(0x8L).get());
            v1 = MEMORY.ref(4, s1).offset(0x8L).get();
            MEMORY.ref(4, v1).offset(0xcL).setu(MEMORY.ref(4, s1).offset(0xcL).get());
          }
        }

        //LAB_80018fa0
        free(MEMORY.ref(4, s1).offset(0x4L).get());
        free(s1);
        if(s0 == 0) {
          return;
        }
        s1 = s0;
      } while(true);

      //LAB_80018fd0
      s0 = MEMORY.ref(4, s1).offset(0x4L).get();

      //LAB_80018fd4
      do {
        MEMORY.ref(1, s0).offset(0x8L).addu(0x1L);

        if(MEMORY.ref(1, s0).offset(0x8L).getSigned() >= 0) {
          v0 = MEMORY.ref(2, s0).offset(0xcL).get() >>> 8;
          v1 = v0 & 0xfL;
          if(v1 == 0x1L) {
            //LAB_800190d4
            a0 = MEMORY.ref(2, s0).offset(0xcL).get() >>> 15;
            v1 = a0 + 0x1L;
            v1 = v1 & 0xffL;
            v1 = v1 << 15;
            MEMORY.ref(2, s0).offset(0xcL).and(0x7fffL).oru(v1);
            MEMORY.ref(1, s0).offset(0xcL).setu(sp0x30[(int)a0]);

            if(MEMORY.ref(1, s0).offset(0x8L).getSigned() == 0xaL) {
              MEMORY.ref(1, s0).offset(0x9L).setu(simpleRand() % 10 + 2);
              v1 = MEMORY.ref(2, s0).offset(0xcL).get() >>> 8;
              v1 = v1 & 0xfL;
              v1 = v1 + 0x1L;
              v1 = v1 & 0xfL;
              v1 = v1 << 8;
              MEMORY.ref(2, s0).offset(0xcL).and(0xf0ffL).oru(v1);
            }
          } else {
            if(v1 >= 0x2L) {
              //LAB_80019028
              if(v1 == 0x2L) {
                //LAB_80019164
                MEMORY.ref(1, s0).offset(0x9L).subu(0x1L);

                if(MEMORY.ref(1, s0).offset(0x9L).get() == 0) {
                  //LAB_80019180
                  MEMORY.ref(1, s0).offset(0x9L).setu(0x8L);
                  v1 = MEMORY.ref(2, s0).offset(0xcL).get() >>> 8;
                  v1 = v1 & 0xfL;
                  v1 = v1 + 0x1L;
                  v1 = v1 & 0xfL;
                  v1 = v1 << 8;

                  //LAB_800191a4
                  MEMORY.ref(2, s0).offset(0xcL).and(0xf0ffL).oru(v1);
                }
              } else {
                if(v1 == 0x3L) {
                  //LAB_800191b8
                  MEMORY.ref(1, s0).offset(0x9L).subu(0x1L);

                  if((MEMORY.ref(1, s0).offset(0x9L).get() & 0x1L) != 0) {
                    MEMORY.ref(2, s0).offset(0x2L).addu(0x1L);
                  }

                  //LAB_800191e4
                  MEMORY.ref(2, s0).offset(0x6L).subu(0x200L);

                  if(MEMORY.ref(1, s0).offset(0x9L).getSigned() == 0) {
                    MEMORY.ref(1, s0).offset(0x8L).setu(-100);
                  }
                }
              }
            } else {
              if(v1 == 0) {
                //LAB_80019040
                MEMORY.ref(1, s0).offset(0x9L).subu(0x1L);

                if(MEMORY.ref(2, s0).offset(0x4L).get() != 0) {
                  MEMORY.ref(2, s0).offset(0x4L).subu(0x492L);
                  MEMORY.ref(2, s0).offset(0x6L).subu(0x492L);
                } else {
                  //LAB_80019084
                  MEMORY.ref(2, s0).offset(0xcL).and(0x8fffL);
                }

                //LAB_80019094
                if(MEMORY.ref(1, s0).offset(0x9L).getSigned() == 0) {
                  MEMORY.ref(2, s0).offset(0xcL).and(0x7fffL);
                  MEMORY.ref(1, s0).offset(0x8L).setu(0);
                  v1 = MEMORY.ref(2, s0).offset(0xcL).get() >>> 8;
                  v1 = v1 & 0xfL;
                  v1 = v1 + 0x1L;
                  v1 = v1 & 0xfL;
                  v1 = v1 << 8;
                  MEMORY.ref(2, s0).offset(0xcL).and(0xf0ffL).oru(v1);
                }
              }
            }
          }

          //LAB_80019208
          FUN_80018a5c(
            MEMORY.ref(2, s0).offset(0x0L).getSigned(),
            MEMORY.ref(2, s0).offset(0x2L).getSigned(),
            MEMORY.ref(1, s0).offset(0xbL).get(),
            0x40L,
            MEMORY.ref(1, s0).offset(0xbL).get() + 0x7L,
            0x4fL,
            MEMORY.ref(1, s0).offset(0xcL).get(),
            Translucency.of((int)((MEMORY.ref(2, s0).offset(0xcL).get() >>> 12 & 0x7) - 1)),
            colour_80010328,
            MEMORY.ref(2, s0).offset(0x4L).get() + 0x1000,
            MEMORY.ref(2, s0).offset(0x6L).get() + 0x1000
          );
        }

        //LAB_80019294
        s2 = s2 + 0x1L;
        s0 = s0 + 0xeL;
      } while(s2 < 0x8L);

      s1 = MEMORY.ref(4, s1).offset(0xcL).get();
    }

    //LAB_800192b4
  }

  @Method(0x800192d8L)
  public static void FUN_800192d8(long a0, final long a1) {
    final long s0 = mallocTail(0x10L);
    MEMORY.ref(1, s0).offset(0x3L).setu(0x34L);
    MEMORY.ref(4, s0).offset(0x4L).setu(mallocTail(0x70L));
    MEMORY.ref(4, s0).offset(0x8L).setu(0);
    MEMORY.ref(4, s0).offset(0xcL).setu(_8004f658.get());
    MEMORY.ref(1, s0).offset(0x0L).setu(0x61L);
    MEMORY.ref(1, s0).offset(0x1L).setu(0x6cL);
    MEMORY.ref(1, s0).offset(0x2L).setu(0x64L);

    final long v0 = MEMORY.ref(4, s0).offset(0xcL).get();
    if(v0 != 0) {
      MEMORY.ref(4, v0).offset(0x8L).setu(s0);
    }

    //LAB_800193b4
    _8004f658.setu(s0);

    //LAB_800193dc
    long v1 = MEMORY.ref(4, s0).offset(0x4L).get();
    for(int i = 0; i < 8; i++) {
      MEMORY.ref(2, v1).offset(0x0L).setu(a0 - 0x6L);
      MEMORY.ref(2, v1).offset(0x2L).setu(a1 - 0x10L);
      MEMORY.ref(2, v1).offset(0x4L).setu(0x1ffeL);
      MEMORY.ref(2, v1).offset(0x6L).setu(0x1ffeL);
      MEMORY.ref(1, v1).offset(0x8L).setu(~i);
      MEMORY.ref(1, v1).offset(0x9L).setu(0x14L - i);
      MEMORY.ref(1, v1).offset(0xbL).setu(_8001032c.offset(i).get());
      MEMORY.ref(2, v1).offset(0xcL).and(0xf0ffL);
      MEMORY.ref(1, v1).offset(0xcL).setu(0x4aL);
      MEMORY.ref(2, v1).offset(0xcL).oru(0x2000L).and(0x2fffL);
      a0 = a0 + _80010334.offset(i).get();
      v1 = v1 + 0xeL;
    }
  }

  @Method(0x80019470L)
  public static void FUN_80019470() {
    long s0 = _8004f658.get();

    //LAB_80019494
    while(s0 != 0) {
      final long s1 = MEMORY.ref(4, s0).offset(0xcL).get();
      free(MEMORY.ref(4, s0).offset(0x4L).get());
      free(s0);
      s0 = s1;
    }

    _8004f658.setu(0);

    //LAB_800194c8
  }

  @Method(0x800194dcL)
  public static void FUN_800194dc() {
    initFileEntries(_8004f65c.reinterpret(UnboundedArrayRef.of(0x8, FileEntry08::new)));
  }

  @Method(0x80019500L)
  public static void FUN_80019500() {
    initSound();
    sssqFadeIn(0, 0);
    FUN_8004c3f0(0x8L);
    sssqSetReverbType(0x3L);
    SsSetRVol(0x30, 0x30);

    //LAB_80019548
    for(int i = 0; i < 13; i++) {
      FUN_8001aa44(i);
    }

    FUN_8001aa64();
    FUN_8001aa78();
    FUN_8001aa90();

    //LAB_80019580
    for(int i = 0; i < 32; i++) {
      spu28Arr_800bd110.get(i)._00.set(0);
      spu28Arr_800bd110.get(i)._1c.set(0);
    }

    //LAB_800195a8
    for(int i = 0; i < 13; i++) {
      soundFileArr_800bcf80.get(i).used_00.set(false);
    }

    //LAB_800195c8
    for(int i = 0; i < 7; i++) {
      _800bd610.offset(i * 0x10L).setu(0);
    }

    sssqTempoScale_800bd100.setu(0x100L);
    _800bd780.setu(0);
    _800bd781.setu(0);
  }

  @Method(0x80019710L)
  public static void FUN_80019710() {
    if(mainCallbackIndex_8004dd20.get() != 0x5L && previousMainCallbackIndex_8004dd28.get() == 0x5L) {
      sssqResetStuff();
      free(soundMrgSshdPtr_800bd784.getPointer());
      free(soundMrgSssqPtr_800bd788.getPointer());
      _800bd780.setu(0);
    }

    //LAB_8001978c
    //LAB_80019790
    if(mainCallbackIndex_8004dd20.get() != 0x6L && previousMainCallbackIndex_8004dd28.get() == 0x6L) {
      sssqResetStuff();

      //LAB_800197c0
      for(int i = 0; i < 3; i++) {
        free(_800bc980.offset(i * 0xcL).offset(0x4L).get());
      }

      if(_800bd780.get() == 0x1L) {
        free(soundMrgSshdPtr_800bd784.getPointer());
        free(soundMrgSssqPtr_800bd788.getPointer());
        _800bd780.setu(0);
      }
    }

    //LAB_80019824
    //LAB_80019828
    switch((int)mainCallbackIndex_8004dd20.get()) {
      case 2 -> {
        setMainVolume(0x7f, 0x7f);
        sssqResetStuff();
        FUN_8001aa90();

        //LAB_80019a00
        if(drgnBinIndex_800bc058.get() == 0x1L) {
          // Load main menu background music
          loadMusicPackage(1, 0);
        } else {
          loadMusicPackage(98, 0);
        }
      }

      case 5 -> {
        sssqResetStuff();

        if(_800bd780.get() != 0x1L) {
          //LAB_80019978
          soundMrgSshdPtr_800bd784.set(MEMORY.ref(4, mallocTail(0x650L), SshdFile::new));
          soundMrgSssqPtr_800bd788.set(MEMORY.ref(4, mallocTail(0x5c30L), SssqFile::new));
          _800bd780.setu(0x1L);
        }
      }

      case 6 -> {
        sssqResetStuff();

        final long s4 = FUN_8001a810() * 0x2L - 0x1L;

        //LAB_800198e8
        for(int i = 0; i < 3; i++) {
          if(i == 0) {
            _800bc980.offset(i * 0xcL).offset(1, 0x1L).setu(0);
          } else {
            //LAB_800198f8
            _800bc980.offset(i * 0xcL).offset(1, 0x1L).setu(_8004f664.offset(i).offset(s4));
          }

          //LAB_80019908
          _800bc980.offset(i * 0xcL).offset(4, 0x4L).setu(mallocTail(_8004f6a4.offset(_800bc980.offset(i * 0xcL).offset(1, 0x1L).get() * 0x4L).get()));
          _800bc980.offset(i * 0xcL).offset(4, 0x8L).setu(_8004f6a4.offset(_800bc980.offset(i * 0xcL).offset(1, 0x1L).get() * 0x4L));
        }

        if(_800bd780.get() != 0x1L && encounterId_800bb0f8.get() == 443) { // Melbu
          //LAB_80019978
          soundMrgSshdPtr_800bd784.set(MEMORY.ref(4, mallocTail(0x650L), SshdFile::new));
          soundMrgSssqPtr_800bd788.set(MEMORY.ref(4, mallocTail(0x5c30L), SssqFile::new));
          _800bd780.setu(0x1L);
        }
      }

      case 0xf -> {
        FUN_8004d91c(0x1L);
        FUN_8004d034((int)_800bd0f0.offset(0x8L).getSigned(), 0);
        FUN_8004c390((int)_800bd0f0.offset(0x8L).getSigned());
        sssqUnloadPlayableSound(soundFileArr_800bcf80.get(11).playableSoundIndex_10.get());
        sssqUnloadPlayableSound(soundFileArr_800bcf80.get(0).playableSoundIndex_10.get());
      }

      case 0xc -> {
        sssqResetStuff();

        //LAB_80019a00
        loadMusicPackage(60, 0);
      }

      case 0xa -> {
        unloadSoundFile(0);
        sssqResetStuff();
      }

      case 4, 7, 8, 9, 0xb -> {
        sssqResetStuff();
      }
    }

    //case 3, d, e
    //LAB_80019a20
    //LAB_80019a24
    if(mainCallbackIndex_8004dd20.get() != 0x5L) {
      _800bd808.setu(-0x1L);
    }

    //LAB_80019a3c
  }

  /**
   * @param soundIndex 1: up/down, 2: choose menu option, 3: ...
   */
  @Method(0x80019a60L)
  public static void playSound(final int index, final int soundIndex, final long a2, final long a3, final short a4, final short a5) {
    if(!soundFileArr_800bcf80.get(index).used_00.get() || soundFileArr_800bcf80.get(index).playableSoundIndex_10.get() == -1) {
      return;
    }

    switch(index) {
      case 0 -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x1L) != 0) {
          return;
        }
      }

      case 8 -> {
        //LAB_80019bd4
        if((loadedDrgnFiles_800bcf78.get() & 0x2L) != 0 || mainCallbackIndex_8004dd20.get() != 0x5L) {
          return;
        }
      }

      case 9 -> {
        //LAB_80019bd4
        if((loadedDrgnFiles_800bcf78.get() & 0x4L) != 0 || mainCallbackIndex_8004dd20.get() != 0x6L) {
          return;
        }
      }

      case 0xa -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x20L) != 0) {
          return;
        }
      }

      case 0xc -> {
        //LAB_80019bd4
        if((loadedDrgnFiles_800bcf78.get() & 0x8000L) != 0 || mainCallbackIndex_8004dd20.get() != 0x8L) {
          return;
        }
      }
    }

    //LAB_80019be0
    //LAB_80019c00
    final SoundFile spu1c = soundFileArr_800bcf80.get(index);

    for(int i = 0; i < 32; i++) {
      if(spu28Arr_800bd110.get(i)._00.get() == 0) {
        if(index == 0x8L) {
          //LAB_80019b54
          FUN_8001a714(3, index, soundIndex, i, spu1c.playableSoundIndex_10.get(), spu1c.ptr_08.get() + soundIndex * 0x2L, MEMORY.ref(1, spu1c.ptr_0c.get()).offset(soundIndex).get(), (short)-1, (short)-1, (short)-1, a5, a4, -1);
        } else {
          FUN_8001a714(3, index, soundIndex, i, spu1c.playableSoundIndex_10.get(), spu1c.ptr_08.get() + soundIndex * 0x2L, 0, (short)-1, (short)-1, (short)-1, a5, a4, -1);
        }

        break;
      }
    }

    //LAB_80019c70
  }

  @Method(0x80019c80L)
  public static void FUN_80019c80(final long a0, final long a1, final long a2) {
    long v0;
    long v1;
    long s0;
    long s1;
    final long s2;
    final long s5;
    s1 = 0;
    s5 = -0x8000L;
    s2 = a2 & 0x1L;
    v0 = 0x800c_0000L;
    s0 = v0 - 0x3658L;

    //LAB_80019cc4
    do {
      if(MEMORY.ref(1, s0).offset(0x2L).get() == a0 && MEMORY.ref(1, s0).offset(0x3L).get() == a1) {
        FUN_8004d78c((short)(s1 | s5));
        if(s2 == 0) {
          break;
        }
      }

      //LAB_80019cfc
      s1 = s1 + 0x1L;
      s0 = s0 + 0x8L;
    } while((int)s1 < 0x18L);

    //LAB_80019d0c
    s1 = 0;
    v0 = 0x800c_0000L;
    v1 = v0 - 0x2ef0L;

    //LAB_80019d1c
    do {
      if(MEMORY.ref(1, v1).offset(0x0L).get() == 0x4L && MEMORY.ref(4, v1).offset(0xcL).get() == a1 && MEMORY.ref(4, v1).offset(0x8L).get() == a0) {
        MEMORY.ref(1, v1).offset(0x0L).setu(0);
        MEMORY.ref(4, v1).offset(0x1cL).setu(0);
      }

      //LAB_80019d54
      s1 = s1 + 0x1L;
      v1 = v1 + 0x28L;
    } while((int)s1 < 0x20L);

    v0 = (int)a2 >> 1;
    v0 = v0 & 0x1L;
    if(v0 != 0) {
      v0 = 0x800c_0000L;
      v0 = v0 - 0x2ef0L;
      v1 = v0;
      final long a0_0 = v1 + 0x500L;

      //LAB_80019d84
      do {
        //LAB_80019db4
        if(MEMORY.ref(1, v1).offset(0x0L).get() == 0x3L && (MEMORY.ref(2, v1).offset(0x20L).getSigned() != 0 || MEMORY.ref(2, v1).offset(0x24L).getSigned() != 0) || MEMORY.ref(4, v1).offset(0x1cL).get() != 0) {
          //LAB_80019dc4
          if(MEMORY.ref(4, v1).offset(0xcL).get() == a1 && MEMORY.ref(4, v1).offset(0x8L).get() == a0) {
            MEMORY.ref(1, v1).offset(0x0L).setu(0);
            MEMORY.ref(4, v1).offset(0x1cL).setu(0);
          }
        }

        //LAB_80019dec
        v1 = v1 + 0x28L;
      } while((int)v1 < (int)a0_0);
    }

    //LAB_80019dfc
  }

  @Method(0x80019e24L)
  public static void FUN_80019e24(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6) {
    final BattleObject27c v1 = scriptStatePtrArr_800bc1c0.get((int)a1).deref().innerStruct_00.derefAs(BattleObject27c.class);

    long t1 = 0;
    if(a0 == 0x1L) {
      //LAB_80019e68
      for(int i = 0; i < 3; i++) {
        if(soundFileArr_800bcf80.get((int)_800500f8.offset(i * 0x4L).get())._02.get() == v1.charIndex_272.get()) {
          //LAB_80019ea4
          t1 = _800500f8.offset(i * 0x4L).get();
          break;
        }
      }
    } else {
      //LAB_80019f18
      //LAB_80019f30
      for(int i = 0; i < 4; i++) {
        if(soundFileArr_800bcf80.get((int)_800500e8.offset(i * 0x4L).get())._02.get() == v1.charIndex_272.get()) {
          //LAB_80019ea4
          t1 = _800500e8.offset(i * 0x4L).get();
          break;
        }

        if(i == 0x3L) {
          return;
        }
      }
    }

    //LAB_80019f70
    //LAB_80019f74
    //LAB_80019f7c
    for(int i = 0; i < 32; i++) {
      if(spu28Arr_800bd110.get(i)._00.get() == 0) {
        //LAB_80019eac
        final SoundFile soundFile = soundFileArr_800bcf80.get((int)t1);

        FUN_8001a714(
          (int)a0,
          (int)t1,
          (int)a2,
          i,
          soundFile.playableSoundIndex_10.get(),
          soundFile.ptr_08.get() + a2 * 0x2L,
          0,
          (short)-1,
          (short)-1,
          (short)-1,
          (short)a6,
          (short)a5,
          (int)a1
        );
        break;
      }
    }

    //LAB_80019f9c
  }

  @Method(0x80019facL)
  public static void FUN_80019fac(final int a0, final long a1, final int soundIndex, final short a3, final short a4) {
    int soundFileIndex = 0;
    int t4 = 0;

    //LAB_80019fdc
    for(int i = 0; i < _800c6768.get(); i++) {
      final int bobjIndex = _8006e398.bobjIndices_e50.get(i).get();

      if(scriptStatePtrArr_800bc1c0.get(bobjIndex).deref().innerStruct_00.derefAs(BattleObject27c.class).charIndex_272.get() == a1) {
        //LAB_8001a070
        t4 = bobjIndex;
        break;
      }
    }

    //LAB_8001a018
    if(a0 == 0x1L) {
      //LAB_8001a034
      for(int i = 0; i < 3; i++) {
        final int v1 = (int)_800500f8.offset(i * 0x4L).get();

        if(soundFileArr_800bcf80.get(v1)._02.get() == a1) {
          soundFileIndex = v1;
          break;
        }
      }
    } else {
      //LAB_8001a0e4
      //LAB_8001a0f4
      for(int i = 0; i < 4; i++) {
        final int v1 = (int)_800500e8.offset(i * 0x4L).get();

        if(soundFileArr_800bcf80.get(v1)._02.get() == a1) {
          //LAB_8001a078
          soundFileIndex = v1;
          break;
        }
      }
    }

    //LAB_8001a128
    //LAB_8001a12c
    //LAB_8001a134
    for(int i = 0; i < 32; i++) {
      final SpuStruct28 v1 = spu28Arr_800bd110.get(i);

      if(v1._00.get() == 0) {
        //LAB_8001a080
        final var v0 = soundFileArr_800bcf80.get(soundFileIndex);
        FUN_8001a714(a0, soundFileIndex, soundIndex, i, v0.playableSoundIndex_10.get(), v0.ptr_08.get() + soundIndex * 2, 0, (short)-1, (short)-1, (short)-1, a4, a3, t4);
        break;
      }
    }

    //LAB_8001a154
  }

  @Method(0x8001a164L)
  public static void FUN_8001a164(final long a0, final long a1, final long soundIndex, final long a3) {
    //LAB_8001a1a8
    for(int i = 0; i < 24; i++) {
      final SpuStruct08 s0 = _800bc9a8.get(i);
      if(s0.soundIndex_03.get() == soundIndex && s0._04.get() == a1) {
        FUN_8004d78c(0xffff_8000L | i);
        if((a3 & 0x1L) == 0) {
          break;
        }
      }

      //LAB_8001a1e0
    }

    //LAB_8001a1f4
    if((a3 >> 1 & 0x1L) != 0) {
      //LAB_8001a208
      for(int i = 0; i < 32; i++) {
        final SpuStruct28 v1 = spu28Arr_800bd110.get(i);
        //LAB_8001a238
        if(v1._00.get() != 0 && (v1._20.get() != 0 || v1._24.get() != 0) && v1.soundIndex_0c.get() == soundIndex && v1._04.get() == a1) {
          v1._00.set(0);
          v1._1c.set(0);
        }

        //LAB_8001a260
      }
    }

    //LAB_8001a270
  }

  @Method(0x8001a4e8L)
  public static void FUN_8001a4e8() {
    //LAB_8001a50c
    for(int i = 0; i < 32; i++) {
      final SpuStruct28 spu28 = spu28Arr_800bd110.get(i);

      if(spu28._00.get() != 0 && spu28._00.get() != 4) {
        if(spu28._24.get() != 0) {
          spu28._24.decr();

          if(spu28._24.get() <= 0) {
            FUN_8001a5fc(i);

            spu28._24.set((short)0);
            if(spu28._20.get() == 0) {
              spu28._00.set(0);
            }
          }
          //LAB_8001a564
        } else if(spu28._20.get() != 0) {
          spu28._22.decr();

          if(spu28._22.get() <= 0) {
            FUN_8001a5fc(i);

            if(spu28._20.get() != 0) {
              spu28._22.set(spu28._20);
            }
          }
        } else {
          //LAB_8001a5b0
          FUN_8001a5fc(i);

          if(spu28._1c.get() != 0) {
            spu28._00.set(4);
          } else {
            //LAB_8001a5d0
            spu28._00.set(0);
          }
        }
      }

      //LAB_8001a5d4
    }
  }

  @Method(0x8001a5fcL)
  public static void FUN_8001a5fc(final int a0) {
    final short s0;

    if(spu28Arr_800bd110.get(a0).pitchShiftVolRight_16.get() == -1 && spu28Arr_800bd110.get(a0).pitchShiftVolLeft_18.get() == -1 && spu28Arr_800bd110.get(a0).pitch_1a.get() == -1) {
      s0 = (short)FUN_8004d648(
        spu28Arr_800bd110.get(a0).playableSoundIndex_10.get(),
        spu28Arr_800bd110.get(a0)._12.get(),
        spu28Arr_800bd110.get(a0)._14.get()
      );
    } else {
      s0 = (short)sssqPitchShift(
        spu28Arr_800bd110.get(a0).playableSoundIndex_10.get(),
        spu28Arr_800bd110.get(a0)._12.get(),
        spu28Arr_800bd110.get(a0)._14.get(),
        spu28Arr_800bd110.get(a0).pitchShiftVolLeft_18.get(),
        spu28Arr_800bd110.get(a0).pitchShiftVolRight_16.get(),
        spu28Arr_800bd110.get(a0).pitch_1a.get()
      );
    }

    if(s0 != -0x1L) {
      _800bc9a8.get(s0).soundFileIndex_02.set((byte)spu28Arr_800bd110.get(a0).soundFileIndex_08.get());
      _800bc9a8.get(s0).soundIndex_03.set((byte)spu28Arr_800bd110.get(a0).soundIndex_0c.get());
      _800bc9a8.get(s0)._04.set(spu28Arr_800bd110.get(a0)._04.get());
    }

    //LAB_8001a704
  }

  @Method(0x8001a714L)
  public static void FUN_8001a714(final int a0, final int soundFileIndex, final int soundIndex, final int a3, final short playableSoundIndex, final long a5, final long a6, final short pitchShiftVolRight, final short pitchShiftVolLeft, final short pitch, final short a10, final short a11, final int a12) {
    final SpuStruct28 spu28 = spu28Arr_800bd110.get(a3);
    spu28._00.set(a0);
    spu28._04.set(a12);
    spu28.soundFileIndex_08.set(soundFileIndex);
    spu28.soundIndex_0c.set(soundIndex);
    spu28.playableSoundIndex_10.set(playableSoundIndex);
    spu28._12.set((short)MEMORY.ref(1, a5).offset(0x0L).get());
    spu28._14.set((short)MEMORY.ref(1, a5).offset(0x1L).get());
    spu28.pitchShiftVolRight_16.set(pitchShiftVolRight);
    spu28.pitchShiftVolLeft_18.set(pitchShiftVolLeft);
    spu28.pitch_1a.set(pitch);
    spu28._1c.set(a6 & 0xffL);
    spu28._20.set(a10);
    spu28._22.set((short)1);
    spu28._24.set(a11);

    long a1_1 = _800bd680.offset(0x50L).getAddress();
    long a2_1 = _800bd680.offset(0x3cL).getAddress();

    //LAB_8001a7b4
    for(int i = 3; i >= 0; i--) {
      MEMORY.ref(4, a1_1).offset(0x00L).setu(MEMORY.ref(4, a2_1).offset(0x00L));
      MEMORY.ref(4, a1_1).offset(0x04L).setu(MEMORY.ref(4, a2_1).offset(0x04L));
      MEMORY.ref(4, a1_1).offset(0x08L).setu(MEMORY.ref(4, a2_1).offset(0x08L));
      MEMORY.ref(4, a1_1).offset(0x0cL).setu(MEMORY.ref(4, a2_1).offset(0x0cL));
      MEMORY.ref(4, a1_1).offset(0x10L).setu(MEMORY.ref(4, a2_1).offset(0x10L));
      a1_1 -= 0x14L;
      a2_1 -= 0x14L;
    }

    _800bd680.setu(soundFileIndex);
    _800bd680.offset(0x4L).setu(soundIndex);
    _800bd680.offset(0x10L).setu(a6 & 0xffL);
  }

  @Method(0x8001a810L)
  public static long FUN_8001a810() {
    //LAB_8001a828
    int a2 = 0;
    long emptyCharSlots = 0;
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final int charIndex = gameState_800babc8.charIndex_88.get(charSlot).get();

      if(charIndex == -1) {
        emptyCharSlots++;
      } else {
        //LAB_8001a840
        a2 = charIndex;
      }

      //LAB_8001a844
    }

    if(emptyCharSlots == 2) {
      return _8004f698.get(a2).get();
    }

    //LAB_8001a878
    //LAB_8001a880
    final byte[] sp = new byte[26];

    //LAB_8001a8b0
    for(int i = 0; i < 3; i++) {
      if(gameState_800babc8.charIndex_88.get(i).get() != -1) {
        //LAB_8001a934
        _800bd6e8.get(i).set(gameState_800babc8.charIndex_88.get(i).get());
      } else {
        //LAB_8001a8cc
        long v1 = 7;
        for(int charIndex = 0; charIndex < 9; charIndex++) {
          if(v1 != gameState_800babc8.charIndex_88.get(0).get()) {
            if(v1 != gameState_800babc8.charIndex_88.get(1).get()) {
              if(v1 != gameState_800babc8.charIndex_88.get(2).get()) {
                if(v1 != _800bd6e8.get(0).get()) {
                  if(v1 != _800bd6e8.get(1).get()) {
                    //LAB_8001a92c
                    _800bd6e8.get(i).set(v1);
                    break;
                  }
                }
              }
            }
          }

          //LAB_8001a914
          v1--;
        }
      }

      //LAB_8001a938
      //LAB_8001a93c
    }

    //LAB_8001a954
    //LAB_8001a97c
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      if(_800bd6e8.get(charSlot).get() != 0) {
        //LAB_8001a998
        for(int i = 0; i < 26; i++) {
          final long a1 = _8004f664.offset(i * 0x2L).getAddress(); //TODO
          final long v1 = _800bd6e8.get(charSlot).get();

          if(v1 == MEMORY.ref(1, a1).offset(0x0L).get() || v1 == MEMORY.ref(1, a1).offset(0x1L).get()) {
            //LAB_8001a9bc
            sp[i]++;
          }

          //LAB_8001a9d0
        }
      }

      //LAB_8001a9e0
    }

    //LAB_8001a9f8
    for(int i = 0; i < 26; i++) {
      if(sp[i] == 2) {
        return i;
      }
    }

    //LAB_8001aa1c
    throw new RuntimeException("Shouldn't get here");
  }

  @Method(0x8001aa24L)
  public static void FUN_8001aa24() {
    FUN_8001a4e8();
  }

  @Method(0x8001aa44L)
  public static void FUN_8001aa44(final int index) {
    soundFileArr_800bcf80.get(index).used_00.set(false);
  }

  @Method(0x8001aa64L)
  public static void FUN_8001aa64() {
    _800bd0f0.setu(0);
    _800bd6f8.setu(0);
  }

  @Method(0x8001aa78L)
  public static void FUN_8001aa78() {
    _800bca68.setu(0);
    _800bca6c.setu(0x7f00L);
  }

  @Method(0x8001aa90L)
  public static void FUN_8001aa90() {
    //LAB_8001aaa4
    for(int i = 0; i < 24; i++) {
      _800bc9a8.get(i)._00.set(0xffff);
    }
  }

  @Method(0x8001ab34L) // Button press (actually I think this is sound?)
  public static long FUN_8001ab34(final RunningScript a0) {
    playSound(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), (short)a0.params_20.get(4).deref().get(), (short)a0.params_20.get(5).deref().get());
    return 0;
  }

  @Method(0x8001ab98L)
  public static long FUN_8001ab98(final RunningScript a0) {
    FUN_80019c80(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x8001abd0L)
  public static long FUN_8001abd0(final RunningScript a0) {
    FUN_80019e24(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get());
    return 0;
  }

  @Method(0x8001ac48L)
  public static long FUN_8001ac48(final RunningScript a0) {
    FUN_8001a164(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get());
    return 0;
  }

  @Method(0x8001ac88L)
  public static long FUN_8001ac88(final RunningScript a0) {
    FUN_80019fac(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), (short)a0.params_20.get(3).deref().get(), (short)a0.params_20.get(4).deref().get());
    return 0;
  }

  @Method(0x8001acd8L)
  public static long FUN_8001acd8(final RunningScript a0) {
    FUN_8001a164(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get());
    return 0;
  }

  @Method(0x8001ad18L)
  public static void FUN_8001ad18() {
    //LAB_8001ad2c
    for(int i = 0; i < 32; i++) {
      spu28Arr_800bd110.get(i)._00.set(0);
      spu28Arr_800bd110.get(i)._1c.set(0);
    }

    FUN_8004d91c(0x1L);
  }

  @Method(0x8001ad5cL)
  public static long FUN_8001ad5c(final RunningScript a0) {
    //LAB_8001ad70
    for(int i = 0; i < 32; i++) {
      final SpuStruct28 struct = spu28Arr_800bd110.get(i);
      struct._00.set(0);
      struct._1c.set(0);
    }

    FUN_8004d91c(0x1L);
    return 0;
  }

  @Method(0x8001ada0L)
  public static void FUN_8001ada0() {
    FUN_8004cf8c((int)sssqChannelIndex_800bd0f8.get());
  }

  @Method(0x8001adc8L)
  public static long FUN_8001adc8(final RunningScript a0) {
    FUN_8004cf8c((int)sssqChannelIndex_800bd0f8.get());
    return 0;
  }

  @Method(0x8001ae18L)
  public static long FUN_8001ae18(final RunningScript a0) {
    FUN_8004d034((int)sssqChannelIndex_800bd0f8.get(), 2);
    return 0;
  }

  @Method(0x8001ae68L)
  public static long FUN_8001ae68(final RunningScript a0) {
    FUN_8004d034((int)sssqChannelIndex_800bd0f8.get(), 2);
    return 0;
  }

  @Method(0x8001ae90L)
  public static void FUN_8001ae90() {
    if(_800bd0f0.get() == 0x2L) {
      FUN_8004d034((int)sssqChannelIndex_800bd0f8.get(), 0);
    }
  }

  @Method(0x8001aec8L)
  public static long FUN_8001aec8(final RunningScript a0) {
    if(_800bd0f0.getSigned() == 0x2L) {
      FUN_8004d034((int)sssqChannelIndex_800bd0f8.getSigned(), 0);
    }

    //LAB_8001aef0
    return 0;
  }

  @Method(0x8001af00L)
  public static void FUN_8001af00(final long a0) {
    FUN_8004cf8c((int)_800bd610.offset(a0 * 0x10L).offset(0xcL).getSigned());
  }

  @Method(0x8001b0f0L)
  public static long FUN_8001b0f0(final RunningScript a0) {
    a0.params_20.get(0).deref().set((int)sssqTempoScale_800bd100.get());
    return 0;
  }

  @Method(0x8001b118L)
  public static long FUN_8001b118(final RunningScript a0) {
    sssqTempoScale_800bd100.setu(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x8001b134L)
  public static long FUN_8001b134(final RunningScript a0) {
    return 0;
  }

  @Method(0x8001b13cL)
  public static long FUN_8001b13c(final RunningScript a0) {
    return 0;
  }

  @Method(0x8001b144L)
  public static long FUN_8001b144(final RunningScript a0) {
    return 0;
  }

  @Method(0x8001b14cL)
  public static long FUN_8001b14c(final RunningScript a0) {
    setMainVolume((short)a0.params_20.get(0).deref().get(), (short)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x8001b17cL)
  public static long FUN_8001b17c(final RunningScript a0) {
    FUN_8001b1a8((short)a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x8001b1a8L)
  public static void FUN_8001b1a8(final long a0) {
    FUN_8004c8dc((int)sssqChannelIndex_800bd0f8.get(), (short)a0);
    _800bd108.setu(a0);
  }

  @Method(0x8001b1ecL)
  public static long FUN_8001b1ec(final RunningScript a0) {
    a0.params_20.get(0).deref().set((int)_800bd108.getSigned());
    return 0;
  }

  @Method(0x8001b208L)
  public static long FUN_8001b208(final RunningScript a0) {
    //LAB_8001b22c
    for(int i = 0; i < 13; i++) {
      final SoundFile s0 = soundFileArr_800bcf80.get(i);

      if(s0.used_00.get()) {
        FUN_8004cb0c(s0.playableSoundIndex_10.get(), (short)a0.params_20.get(0).deref().get());
      }

      //LAB_8001b250
    }

    return 0;
  }

  @Method(0x8001b27cL)
  public static long FUN_8001b27c(final RunningScript a0) {
    sssqFadeIn((short)a0.params_20.get(0).deref().get(), (short)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x8001b2acL)
  public static long FUN_8001b2ac(final RunningScript a0) {
    //TODO GH#3
    if(true) {
      return 0;
    }

    FUN_8004d2fc((int)_800bd0f0.offset(2, 0x8L).getSigned(), (short)a0.params_20.get(0).deref().get(), (short)a0.params_20.get(1).deref().get());
    _800bd0f0.offset(2, 0x18L).setu(a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x8001b310L)
  public static long FUN_8001b310(final RunningScript a0) {
    FUN_8004cd50((short)a0.params_20.get(0).deref().get());
    return 0;
  }

  /**
   * Something to do with sequenced audio
   */
  @Method(0x8001b33cL)
  public static long FUN_8001b33c(final RunningScript a0) {
    //TODO GH#3
    if(true) {
      return 0;
    }

    FUN_8004d41c((int)_800bd0f0.offset(2, 0x8L).getSigned(), (short)a0.params_20.get(0).deref().get(), (short)a0.params_20.get(1).deref().get());
    _800bd0f0.offset(2, 0x18L).setu(a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x8001b3a0L)
  public static long FUN_8001b3a0(final RunningScript a0) {
    a0.params_20.get(0).deref().set((short)FUN_8004d52c((short)sssqChannelIndex_800bd0f8.getSigned()));
    return 0;
  }

  @Method(0x8001b3e4L)
  public static long FUN_8001b3e4() {
    if(soundFileArr_800bcf80.get(11).used_00.get()) {
      return soundFileArr_800bcf80.get(11)._02.get();
    }

    //LAB_8001b408
    return 0;
  }

  @Method(0x8001b410L)
  public static void FUN_8001b410() {
    if(_8004f6e4.getSigned() == -0x1L) {
      return;
    }

    renderCombatDissolveEffect();

    if(loadingGameStateOverlay_8004dd08.get() == 0x1L) {
      return;
    }

    if(_800bd740.getSigned() >= 0) {
      _800bd740.sub(0x1L);
      return;
    }

    //LAB_8001b460
    if(_800bd700.get() != 0) {
      FUN_8001c5bc();
    }

    //LAB_8001b480
    if(_800bc960.get(0x2L) != 0) {
      if(_8004f6ec.get() == 0) {
        _8004f6ec.setu(0x1L);
        FUN_8001c594(0x1L, 0x6L);
        scriptStartEffect(0x1L, 0x1L);
      }
    }

    //LAB_8001b4c0
    if(_8004f6ec.get() != 0) {
      //LAB_8001b4d4
      if(_8004f6ec.get() >= 0x7L) {
        if(loadingGameStateOverlay_8004dd08.get() == 0) {
          _8004f6e4.setu(-0x1L);
          _800bc960.oru(0x1L);
        }
      }

      //LAB_8001b518
      _8004f6ec.addu(0x1L);
    }

    //LAB_8001b528
    _8004f6e8.addu(0x1L);

    //LAB_8001b53c
  }

  @Method(0x8001b54cL)
  public static void renderCombatDissolveEffect() {
    FUN_8001b92c();

    final int sp10 = -displayWidth_1f8003e0.get() / 2;
    final int sp14 = -displayHeight_1f8003e4.get() / 2;
    final int a0 = displayHeight_1f8003e4.get() / 8;
    final int v0 = 100 / a0;

    if(v0 == _800bd714.get()) {
      _800bd714.setu(0);
      _800bd710.addu(0x1L);
      final int v1 = a0 - 1;
      if(v1 < _800bd710.get()) {
        _800bd710.setu(v1);
      }
    }

    //LAB_8001b608
    final long fp = _800bd700.getAddress();
    int sp30 = 512;

    //LAB_8001b620
    for(int sp18 = 0; sp18 <= _800bd710.get(); sp18++) {
      final int sp24 = sp30 >> 8;
      int sp2c = sp10;
      final int s5 = displayHeight_1f8003e4.get() - ((int)_800bd710.get() + 1) * 8 + sp18 * 8;

      //LAB_8001b664
      for(int sp1c = 0; sp1c < displayWidth_1f8003e0.get() / 32 * 4; sp1c++) {
        final int s6 = sp1c * 8;

        //LAB_8001b6a4
        for(int s7 = 0; s7 <= 0; s7++) {
          int s3 = rand() % 4;
          if((rand() & 1) != 0) {
            s3 = -s3;
          }

          //LAB_8001b6dc
          final int s2 = rand() % 6;
          final int s4;
          if(s6 < 249) {
            s4 = 0;
            //LAB_8001b720
          } else if(s6 < 505) {
            s4 = 4;
          } else {
            s4 = 8;
          }

          final int left = sp2c + s3;
          final int top = sp14 + s5 + s2 + sp24;

          //LAB_8001b734
          final long packet = gpuPacketAddr_1f8003d8.get();
          gpuPacketAddr_1f8003d8.addu(0x28L);
          MEMORY.ref(1, packet).offset(0x3L).setu(0x9L);
          MEMORY.ref(4, packet).offset(0x4L).setu(0x2c80_8080L);
          gpuLinkedListSetCommandTransparency(packet, true);
          MEMORY.ref(2, packet).offset(0x08L).setu(left); // X0
          MEMORY.ref(2, packet).offset(0x0aL).setu(top); // Y0
          MEMORY.ref(2, packet).offset(0x10L).setu(left + 8); // X1
          MEMORY.ref(2, packet).offset(0x12L).setu(top); // Y1
          MEMORY.ref(2, packet).offset(0x18L).setu(left); // X2
          MEMORY.ref(2, packet).offset(0x1aL).setu(top + 8); // Y2
          MEMORY.ref(2, packet).offset(0x20L).setu(left + 8); // X3
          MEMORY.ref(2, packet).offset(0x22L).setu(top + 8); // Y3

          if(doubleBufferFrame_800bb108.get() == 0) {
            MEMORY.ref(1, packet).offset(0x0cL).setu(s6); // U0
            MEMORY.ref(1, packet).offset(0x0dL).setu(s5 + 16); // V0
            MEMORY.ref(1, packet).offset(0x14L).setu(s6 + 7); // U1
            MEMORY.ref(1, packet).offset(0x15L).setu(s5 + 16); // V1
            MEMORY.ref(1, packet).offset(0x1cL).setu(s6); // U2
            MEMORY.ref(1, packet).offset(0x1dL).setu(s5 + 24); // V2
            MEMORY.ref(1, packet).offset(0x24L).setu(s6 + 7); // U3
            MEMORY.ref(1, packet).offset(0x25L).setu(s5 + 24); // V3
            MEMORY.ref(2, packet).offset(0x16L).setu(s4 + 0x100); // TPAGE
          } else {
            //LAB_8001b818
            MEMORY.ref(1, packet).offset(0x0cL).setu(s6); // U0
            MEMORY.ref(1, packet).offset(0x0dL).setu(s5); // V0
            MEMORY.ref(1, packet).offset(0x14L).setu(s6 + 7); // U1
            MEMORY.ref(1, packet).offset(0x15L).setu(s5); // V1
            MEMORY.ref(1, packet).offset(0x1cL).setu(s6); // U2
            MEMORY.ref(1, packet).offset(0x1dL).setu(s5 + 8); // V2
            MEMORY.ref(1, packet).offset(0x24L).setu(s6 + 7); // U3
            MEMORY.ref(1, packet).offset(0x25L).setu(s5 + 8); // V3
            MEMORY.ref(2, packet).offset(0x16L).setu(s4 + 0x110); // TPAGE
          }

          //LAB_8001b868
          MEMORY.ref(1, packet).offset(0x4L).setu((int)MEMORY.ref(4, fp).offset(0x8L).get() >> 8); // R
          MEMORY.ref(1, packet).offset(0x5L).setu((int)MEMORY.ref(4, fp).offset(0x8L).get() >> 8); // G
          MEMORY.ref(1, packet).offset(0x6L).setu((int)MEMORY.ref(4, fp).offset(0x8L).get() >> 8); // B
          queueGpuPacket(tags_1f8003d0.deref().get(6).getAddress(), packet);
        }

        sp2c += 8;
      }

      //LAB_8001b8b8
      sp30 += 512;
    }

    _800bd714.addu(0x1L);
    FUN_8001bbcc(sp10, sp14);
  }

  @Method(0x8001b92cL)
  public static void FUN_8001b92c() {
    final int width = displayWidth_1f8003e0.get();
    final int height = displayHeight_1f8003e4.get();
    final int left = -width / 2;
    final int right = width / 2;
    final int top = -height / 2;
    final int bottom = height / 2;

    long packet = gpuPacketAddr_1f8003d8.get();
    MEMORY.ref(1, packet).offset(0x03L).setu(0x5L);
    MEMORY.ref(4, packet).offset(0x04L).setu(0x2880_8080L);
    MEMORY.ref(1, packet).offset(0x04L).setu(1);
    MEMORY.ref(1, packet).offset(0x05L).setu(1);
    MEMORY.ref(1, packet).offset(0x06L).setu(1);
    MEMORY.ref(2, packet).offset(0x08L).setu(left - 32);
    MEMORY.ref(2, packet).offset(0x0aL).setu(top - 32);
    MEMORY.ref(2, packet).offset(0x0cL).setu(right + 32);
    MEMORY.ref(2, packet).offset(0x0eL).setu(top - 32);
    MEMORY.ref(2, packet).offset(0x10L).setu(left - 32);
    MEMORY.ref(2, packet).offset(0x12L).setu(top + 4);
    MEMORY.ref(2, packet).offset(0x14L).setu(right + 32);
    MEMORY.ref(2, packet).offset(0x16L).setu(top + 4);
    queueGpuPacket(tags_1f8003d0.deref().get(6).getAddress(), packet);
    gpuPacketAddr_1f8003d8.addu(0x18L);

    packet = gpuPacketAddr_1f8003d8.get();
    MEMORY.ref(1, packet).offset(0x03L).setu(0x5L);
    MEMORY.ref(4, packet).offset(0x04L).setu(0x2880_8080L);
    MEMORY.ref(1, packet).offset(0x04L).setu(1);
    MEMORY.ref(1, packet).offset(0x05L).setu(1);
    MEMORY.ref(1, packet).offset(0x06L).setu(1);
    MEMORY.ref(2, packet).offset(0x08L).setu(left - 32);
    MEMORY.ref(2, packet).offset(0x0aL).setu(bottom + 32);
    MEMORY.ref(2, packet).offset(0x0cL).setu(right + 32);
    MEMORY.ref(2, packet).offset(0x0eL).setu(bottom + 32);
    MEMORY.ref(2, packet).offset(0x10L).setu(left - 32);
    MEMORY.ref(2, packet).offset(0x12L).setu(bottom - 4);
    MEMORY.ref(2, packet).offset(0x14L).setu(right + 32);
    MEMORY.ref(2, packet).offset(0x16L).setu(bottom - 4);
    queueGpuPacket(tags_1f8003d0.deref().get(6).getAddress(), packet);
    gpuPacketAddr_1f8003d8.addu(0x18L);

    packet = gpuPacketAddr_1f8003d8.get();
    MEMORY.ref(1, packet).offset(0x03L).setu(0x5L);
    MEMORY.ref(4, packet).offset(0x04L).setu(0x2880_8080L);
    MEMORY.ref(1, packet).offset(0x04L).setu(1);
    MEMORY.ref(1, packet).offset(0x05L).setu(1);
    MEMORY.ref(1, packet).offset(0x06L).setu(1);
    MEMORY.ref(2, packet).offset(0x08L).setu(left - 32);
    MEMORY.ref(2, packet).offset(0x0aL).setu(top);
    MEMORY.ref(2, packet).offset(0x0cL).setu(left + 4);
    MEMORY.ref(2, packet).offset(0x0eL).setu(top);
    MEMORY.ref(2, packet).offset(0x10L).setu(left - 32);
    MEMORY.ref(2, packet).offset(0x12L).setu(bottom);
    MEMORY.ref(2, packet).offset(0x14L).setu(left + 4);
    MEMORY.ref(2, packet).offset(0x16L).setu(bottom);
    queueGpuPacket(tags_1f8003d0.deref().get(6).getAddress(), packet);
    gpuPacketAddr_1f8003d8.addu(0x18L);

    packet = gpuPacketAddr_1f8003d8.get();
    MEMORY.ref(1, packet).offset(0x03L).setu(0x5L);
    MEMORY.ref(4, packet).offset(0x04L).setu(0x2880_8080L);
    MEMORY.ref(1, packet).offset(0x04L).setu(1);
    MEMORY.ref(1, packet).offset(0x05L).setu(1);
    MEMORY.ref(1, packet).offset(0x06L).setu(1);
    MEMORY.ref(2, packet).offset(0x08L).setu(right + 32);
    MEMORY.ref(2, packet).offset(0x0aL).setu(top);
    MEMORY.ref(2, packet).offset(0x0cL).setu(right - 4);
    MEMORY.ref(2, packet).offset(0x0eL).setu(top);
    MEMORY.ref(2, packet).offset(0x10L).setu(right + 32);
    MEMORY.ref(2, packet).offset(0x12L).setu(bottom);
    MEMORY.ref(2, packet).offset(0x14L).setu(right - 4);
    MEMORY.ref(2, packet).offset(0x16L).setu(bottom);
    queueGpuPacket(tags_1f8003d0.deref().get(6).getAddress(), packet);
    gpuPacketAddr_1f8003d8.addu(0x18L);
  }

  @Method(0x8001bbccL)
  public static void FUN_8001bbcc(final int x, final int y) {
    FUN_8001b92c();

    final long s2 = _800bd700.getAddress();
    long packet;
    if(doubleBufferFrame_800bb108.get() != 0) {
      //LAB_8001bf3c
      packet = gpuPacketAddr_1f8003d8.get();
      MEMORY.ref(1, packet).offset(0x03L).setu(0x9L);
      MEMORY.ref(4, packet).offset(0x04L).setu(0x2c80_8080L);
      gpuLinkedListSetCommandTransparency(packet, false);
      MEMORY.ref(1, packet).offset(0x04L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
      MEMORY.ref(1, packet).offset(0x05L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
      MEMORY.ref(1, packet).offset(0x06L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
      MEMORY.ref(2, packet).offset(0x08L).setu(x + 128);
      MEMORY.ref(2, packet).offset(0x0aL).setu(y);
      MEMORY.ref(1, packet).offset(0x0cL).setu(0);
      MEMORY.ref(1, packet).offset(0x0dL).setu(0);
      MEMORY.ref(2, packet).offset(0x10L).setu(x + 383);
      MEMORY.ref(2, packet).offset(0x12L).setu(y);
      MEMORY.ref(1, packet).offset(0x14L).setu(255);
      MEMORY.ref(1, packet).offset(0x15L).setu(0);
      MEMORY.ref(2, packet).offset(0x16L).setu(0x112);
      MEMORY.ref(2, packet).offset(0x18L).setu(x + 128);
      MEMORY.ref(2, packet).offset(0x1aL).setu(y - 1 + displayHeight_1f8003e4.get());
      MEMORY.ref(1, packet).offset(0x1cL).setu(0);
      MEMORY.ref(1, packet).offset(0x1dL).setu(239);
      MEMORY.ref(2, packet).offset(0x20L).setu(x + 383);
      MEMORY.ref(2, packet).offset(0x22L).setu(y - 1 + displayHeight_1f8003e4.get());
      MEMORY.ref(1, packet).offset(0x24L).setu(255);
      MEMORY.ref(1, packet).offset(0x25L).setu(239);
      queueGpuPacket(tags_1f8003d0.deref().get(6).getAddress(), packet);
      gpuPacketAddr_1f8003d8.addu(0x28L);

      packet = gpuPacketAddr_1f8003d8.get();
      MEMORY.ref(1, packet).offset(0x03L).setu(0x9L);
      MEMORY.ref(4, packet).offset(0x04L).setu(0x2c80_8080L);
      gpuLinkedListSetCommandTransparency(packet, false);
      MEMORY.ref(1, packet).offset(0x04L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
      MEMORY.ref(1, packet).offset(0x05L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
      MEMORY.ref(1, packet).offset(0x06L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
      MEMORY.ref(2, packet).offset(0x08L).setu(x);
      MEMORY.ref(2, packet).offset(0x0aL).setu(y);
      MEMORY.ref(1, packet).offset(0x0cL).setu(0);
      MEMORY.ref(1, packet).offset(0x0dL).setu(0);
      MEMORY.ref(2, packet).offset(0x10L).setu(x + 255);
      MEMORY.ref(2, packet).offset(0x12L).setu(y);
      MEMORY.ref(1, packet).offset(0x14L).setu(255);
      MEMORY.ref(1, packet).offset(0x15L).setu(0);
      MEMORY.ref(2, packet).offset(0x16L).setu(0x110);
      MEMORY.ref(2, packet).offset(0x18L).setu(x);
      MEMORY.ref(2, packet).offset(0x1aL).setu(y - 1 + displayHeight_1f8003e4.get());
      MEMORY.ref(1, packet).offset(0x1cL).setu(0);
      MEMORY.ref(1, packet).offset(0x1dL).setu(239);
      MEMORY.ref(2, packet).offset(0x20L).setu(x + 255);
      MEMORY.ref(2, packet).offset(0x22L).setu(y - 1 + displayHeight_1f8003e4.get());
      MEMORY.ref(1, packet).offset(0x24L).setu(255);
      MEMORY.ref(1, packet).offset(0x25L).setu(239);
      queueGpuPacket(tags_1f8003d0.deref().get(6).getAddress(), packet);
      gpuPacketAddr_1f8003d8.addu(0x28L);

      if(displayWidth_1f8003e0.get() == 640) {
        packet = gpuPacketAddr_1f8003d8.get();
        MEMORY.ref(1, packet).offset(0x03L).setu(0x9L);
        MEMORY.ref(4, packet).offset(0x04L).setu(0x2c80_8080L);
        gpuLinkedListSetCommandTransparency(packet, false);
        MEMORY.ref(1, packet).offset(0x04L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
        MEMORY.ref(1, packet).offset(0x05L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
        MEMORY.ref(1, packet).offset(0x06L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
        MEMORY.ref(2, packet).offset(0x08L).setu(x + 256);
        MEMORY.ref(2, packet).offset(0x0aL).setu(y);
        MEMORY.ref(1, packet).offset(0x0cL).setu(0);
        MEMORY.ref(1, packet).offset(0x0dL).setu(0);
        MEMORY.ref(2, packet).offset(0x10L).setu(x + 511);
        MEMORY.ref(2, packet).offset(0x12L).setu(y);
        MEMORY.ref(1, packet).offset(0x14L).setu(255);
        MEMORY.ref(1, packet).offset(0x15L).setu(0);
        MEMORY.ref(2, packet).offset(0x16L).setu(0x114);
        MEMORY.ref(2, packet).offset(0x18L).setu(x + 256);
        MEMORY.ref(2, packet).offset(0x1aL).setu(y - 1 + displayHeight_1f8003e4.get());
        MEMORY.ref(1, packet).offset(0x1cL).setu(0);
        MEMORY.ref(1, packet).offset(0x1dL).setu(239);
        MEMORY.ref(2, packet).offset(0x20L).setu(x + 511);
        MEMORY.ref(2, packet).offset(0x22L).setu(y - 1 + displayHeight_1f8003e4.get());
        MEMORY.ref(1, packet).offset(0x24L).setu(255);
        MEMORY.ref(1, packet).offset(0x25L).setu(239);
        queueGpuPacket(tags_1f8003d0.deref().get(6).getAddress(), packet);
        gpuPacketAddr_1f8003d8.addu(0x28L);

        packet = gpuPacketAddr_1f8003d8.get();
        MEMORY.ref(1, packet).offset(0x03L).setu(0x9L);
        MEMORY.ref(4, packet).offset(0x04L).setu(0x2c80_8080L);
        gpuLinkedListSetCommandTransparency(packet, false);
        MEMORY.ref(1, packet).offset(0x04L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
        MEMORY.ref(1, packet).offset(0x05L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
        MEMORY.ref(1, packet).offset(0x06L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
        MEMORY.ref(2, packet).offset(0x08L).setu(x + 384);
        MEMORY.ref(2, packet).offset(0x0aL).setu(y);
        MEMORY.ref(1, packet).offset(0x0cL).setu(0);
        MEMORY.ref(1, packet).offset(0x0dL).setu(0);
        MEMORY.ref(2, packet).offset(0x10L).setu(x + 639);
        MEMORY.ref(2, packet).offset(0x12L).setu(y);
        MEMORY.ref(1, packet).offset(0x14L).setu(255);
        MEMORY.ref(1, packet).offset(0x15L).setu(0);
        MEMORY.ref(2, packet).offset(0x16L).setu(0x116);
        MEMORY.ref(2, packet).offset(0x18L).setu(x + 384);
        MEMORY.ref(2, packet).offset(0x1aL).setu(y - 1 + displayHeight_1f8003e4.get());
        MEMORY.ref(1, packet).offset(0x1cL).setu(0);
        MEMORY.ref(1, packet).offset(0x1dL).setu(239);
        MEMORY.ref(2, packet).offset(0x20L).setu(x + 639);
        MEMORY.ref(2, packet).offset(0x22L).setu(y - 1 + displayHeight_1f8003e4.get());
        MEMORY.ref(1, packet).offset(0x24L).setu(255);
        MEMORY.ref(1, packet).offset(0x25L).setu(239);
        queueGpuPacket(tags_1f8003d0.deref().get(6).getAddress(), packet);
        gpuPacketAddr_1f8003d8.addu(0x28L);
      }
    } else {
      packet = gpuPacketAddr_1f8003d8.get();
      MEMORY.ref(1, packet).offset(0x3L).setu(0x9L);
      MEMORY.ref(4, packet).offset(0x4L).setu(0x2c80_8080L);
      gpuLinkedListSetCommandTransparency(packet, false);
      MEMORY.ref(1, packet).offset(0x4L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
      MEMORY.ref(1, packet).offset(0x5L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
      MEMORY.ref(1, packet).offset(0x6L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
      MEMORY.ref(2, packet).offset(0x8L).setu(x + 128);
      MEMORY.ref(2, packet).offset(0xaL).setu(y);
      MEMORY.ref(1, packet).offset(0xcL).setu(0);
      MEMORY.ref(1, packet).offset(0xdL).setu(16);
      MEMORY.ref(2, packet).offset(0x10L).setu(x + 383);
      MEMORY.ref(2, packet).offset(0x12L).setu(y);
      MEMORY.ref(1, packet).offset(0x14L).setu(255);
      MEMORY.ref(1, packet).offset(0x15L).setu(16);
      MEMORY.ref(2, packet).offset(0x16L).setu(0x102);
      MEMORY.ref(2, packet).offset(0x18L).setu(x + 128);
      MEMORY.ref(2, packet).offset(0x1aL).setu(y - 1 + displayHeight_1f8003e4.get());
      MEMORY.ref(1, packet).offset(0x1cL).setu(0);
      MEMORY.ref(1, packet).offset(0x1dL).setu(255);
      MEMORY.ref(2, packet).offset(0x20L).setu(x + 383);
      MEMORY.ref(2, packet).offset(0x22L).setu(y - 1 + displayHeight_1f8003e4.get());
      MEMORY.ref(1, packet).offset(0x24L).setu(255);
      MEMORY.ref(1, packet).offset(0x25L).setu(255);
      queueGpuPacket(tags_1f8003d0.deref().get(6).getAddress(), packet);
      gpuPacketAddr_1f8003d8.addu(0x28L);

      packet = gpuPacketAddr_1f8003d8.get();
      MEMORY.ref(1, packet).offset(0x3L).setu(0x9L);
      MEMORY.ref(4, packet).offset(0x4L).setu(0x2c80_8080L);
      gpuLinkedListSetCommandTransparency(packet, false);
      MEMORY.ref(1, packet).offset(0x4L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
      MEMORY.ref(1, packet).offset(0x5L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
      MEMORY.ref(1, packet).offset(0x6L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
      MEMORY.ref(2, packet).offset(0x8L).setu(x);
      MEMORY.ref(2, packet).offset(0xaL).setu(y);
      MEMORY.ref(1, packet).offset(0xcL).setu(0);
      MEMORY.ref(1, packet).offset(0xdL).setu(16);
      MEMORY.ref(2, packet).offset(0x10L).setu(x + 255);
      MEMORY.ref(2, packet).offset(0x12L).setu(y);
      MEMORY.ref(1, packet).offset(0x14L).setu(255);
      MEMORY.ref(1, packet).offset(0x15L).setu(16);
      MEMORY.ref(2, packet).offset(0x16L).setu(0x100);
      MEMORY.ref(2, packet).offset(0x18L).setu(x);
      MEMORY.ref(2, packet).offset(0x1aL).setu(y - 1 + displayHeight_1f8003e4.get());
      MEMORY.ref(1, packet).offset(0x1cL).setu(0);
      MEMORY.ref(1, packet).offset(0x1dL).setu(255);
      MEMORY.ref(2, packet).offset(0x20L).setu(x + 255);
      MEMORY.ref(2, packet).offset(0x22L).setu(y - 1 + displayHeight_1f8003e4.get());
      MEMORY.ref(1, packet).offset(0x24L).setu(255);
      MEMORY.ref(1, packet).offset(0x25L).setu(255);
      queueGpuPacket(tags_1f8003d0.deref().get(6).getAddress(), packet);
      gpuPacketAddr_1f8003d8.addu(0x28L);

      if(displayWidth_1f8003e0.get() == 640) {
        packet = gpuPacketAddr_1f8003d8.get();
        MEMORY.ref(1, packet).offset(0x03L).setu(0x9L);
        MEMORY.ref(4, packet).offset(0x04L).setu(0x2c80_8080L);
        gpuLinkedListSetCommandTransparency(packet, false);
        MEMORY.ref(1, packet).offset(0x04L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
        MEMORY.ref(1, packet).offset(0x05L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
        MEMORY.ref(1, packet).offset(0x06L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
        MEMORY.ref(2, packet).offset(0x08L).setu(x + 256);
        MEMORY.ref(2, packet).offset(0x0aL).setu(y);
        MEMORY.ref(1, packet).offset(0x0cL).setu(0);
        MEMORY.ref(1, packet).offset(0x0dL).setu(16);
        MEMORY.ref(2, packet).offset(0x10L).setu(x + 511);
        MEMORY.ref(2, packet).offset(0x12L).setu(y);
        MEMORY.ref(1, packet).offset(0x14L).setu(255);
        MEMORY.ref(1, packet).offset(0x15L).setu(16);
        MEMORY.ref(2, packet).offset(0x16L).setu(0x104);
        MEMORY.ref(2, packet).offset(0x18L).setu(x + 256);
        MEMORY.ref(2, packet).offset(0x1aL).setu(y - 1 + displayHeight_1f8003e4.get());
        MEMORY.ref(1, packet).offset(0x1cL).setu(0);
        MEMORY.ref(1, packet).offset(0x1dL).setu(255);
        MEMORY.ref(2, packet).offset(0x20L).setu(x + 511);
        MEMORY.ref(2, packet).offset(0x22L).setu(y - 1 + displayHeight_1f8003e4.get());
        MEMORY.ref(1, packet).offset(0x24L).setu(255);
        MEMORY.ref(1, packet).offset(0x25L).setu(255);
        queueGpuPacket(tags_1f8003d0.deref().get(6).getAddress(), packet);
        gpuPacketAddr_1f8003d8.addu(0x28L);

        packet = gpuPacketAddr_1f8003d8.get();
        MEMORY.ref(1, packet).offset(0x03L).setu(0x9L);
        MEMORY.ref(4, packet).offset(0x04L).setu(0x2c80_8080L);
        gpuLinkedListSetCommandTransparency(packet, false);
        MEMORY.ref(1, packet).offset(0x04L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
        MEMORY.ref(1, packet).offset(0x05L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
        MEMORY.ref(1, packet).offset(0x06L).setu((int)MEMORY.ref(4, s2).offset(0x8L).get() >> 8);
        MEMORY.ref(2, packet).offset(0x08L).setu(x + 384);
        MEMORY.ref(2, packet).offset(0x0aL).setu(y);
        MEMORY.ref(1, packet).offset(0x0cL).setu(0);
        MEMORY.ref(1, packet).offset(0x0dL).setu(16);
        MEMORY.ref(2, packet).offset(0x10L).setu(x + 639);
        MEMORY.ref(2, packet).offset(0x12L).setu(y);
        MEMORY.ref(1, packet).offset(0x14L).setu(255);
        MEMORY.ref(1, packet).offset(0x15L).setu(16);
        MEMORY.ref(2, packet).offset(0x16L).setu(0x106);
        MEMORY.ref(2, packet).offset(0x18L).setu(x + 384);
        MEMORY.ref(2, packet).offset(0x1aL).setu(y - 1 + displayHeight_1f8003e4.get());
        MEMORY.ref(1, packet).offset(0x1cL).setu(0);
        MEMORY.ref(1, packet).offset(0x1dL).setu(255);
        MEMORY.ref(2, packet).offset(0x20L).setu(x + 639);
        MEMORY.ref(2, packet).offset(0x22L).setu(y - 1 + displayHeight_1f8003e4.get());
        MEMORY.ref(1, packet).offset(0x24L).setu(255);
        MEMORY.ref(1, packet).offset(0x25L).setu(255);
        queueGpuPacket(tags_1f8003d0.deref().get(6).getAddress(), packet);
        gpuPacketAddr_1f8003d8.addu(0x28L);
      }
    }

    //LAB_8001c26c
    packet = gpuPacketAddr_1f8003d8.get();
    MEMORY.ref(1, packet).offset(0x3L).setu(0x1L);
    MEMORY.ref(4, packet).offset(0x4L).setu(0xe100_0100L);
    queueGpuPacket(tags_1f8003d0.deref().get(6).getAddress(), packet);
    gpuPacketAddr_1f8003d8.addu(0x8L);

    packet = gpuPacketAddr_1f8003d8.get();
    MEMORY.ref(1, packet).offset(0x3L).setu(0x1L);
    MEMORY.ref(4, packet).offset(0x4L).setu(0xe100_0110L);
    queueGpuPacket(tags_1f8003d0.deref().get(6).getAddress(), packet);
    gpuPacketAddr_1f8003d8.addu(0x8L);
  }

  @Method(0x8001c4ecL)
  public static void FUN_8001c4ec() {
    chapterTitleCardMrg_800c6710.clear();
    _8004f6ec.setu(0);
    playSound(0, 16, 0, 0, (short)0, (short)0);
    vsyncMode_8007a3b8.setu(0x2L);
    _800bd740.setu(0x2L);
    _800bd700.setu(0);
    _800bd704.setu(0);
    _800bd708.setu(0x8000L);
    _800bd714.setu(0);
    _800bd710.setu(0);
    _8007a3a8.setu(0);
    _800bb104.setu(0);
    _800babc0.setu(0);
    _8004f6e4.setu(0x1L);
    _8004f6e8.setu(0);
  }

  @Method(0x8001c594L)
  public static void FUN_8001c594(final long a0, final long a1) {
    _800bd700.setu(a0);
    _800bd704.setu(a1);
    _800bd708.setu(0x8000L);
    _800bd70c.setu(0x8000L / a1);
  }

  @Method(0x8001c5bcL)
  public static void FUN_8001c5bc() {
    if(_800bd700.get() == 0x1L) {
      _800bd704.subu(0x1L);
      _800bd708.subu(_800bd70c);

      if(_800bd704.get() == 0) {
        _800bd700.setu(0);
      }
    }
  }

  @Method(0x8001c60cL)
  public static long FUN_8001c60c() {
    final long s0 = FUN_8001b3e4();
    final long a1;

    //LAB_8001c63c
    jmp_8001c7a0:
    {
      long a3 = 0;
      do {
        if(_800bd808.get() == _8004fb00.offset(1, a3 * 0x8L).get()) {
          //LAB_8001c680
          long v1 = 0;
          do {
            if(_800bd808.get() != 0x39L || _8004fb00.offset(4, a3 * 0x8L).offset(0x4L).deref(2).offset(v1).getSigned() == submapCut_80052c30.get()) {
              //LAB_8001c6ac
              if((gameState_800babc8._1a4.get(0).get() & 0x1L) == 0 || _8004fb00.offset(4, a3 * 0x8L).offset(0x4L).deref(2).offset(v1).getSigned() == submapCut_80052c30.get() && (gameState_800babc8._1a4.get((int)(a3 / 0x20)).get() & 0x1L << a3) != 0) {
                //LAB_8001c7c0
                a1 = _8004fb00.offset(2, a3 * 0x8L).offset(0x2L).getSigned();
                break jmp_8001c7a0;
              }
            }

            //LAB_8001c6e4
            v1 += 0x2L;
          } while(_8004fb00.offset(4, a3 * 0x8L).offset(0x4L).deref(2).offset(v1).getSigned() != -0x1L);
        }

        //LAB_8001c700
        a3++;
      } while(_8004fb00.offset(1, a3 * 0x8L).get() != 0x63L || _8004fb00.offset(2, a3 * 0x8L).offset(0x2L).getSigned() != 0x63L);

      long a0 = 0;

      //LAB_8001c728
      do {
        if(_800bd808.get() == _8004fa98.offset(1, a0).get()) {
          long v1 = _8004fa98.offset(4, a0).offset(0x4L).get();

          //LAB_8001c748
          do {
            if(MEMORY.ref(2, v1).getSigned() == submapCut_80052c30.get()) {
              //LAB_8001c7d8
              return FUN_8001c84c(s0, _8004fa98.offset(2, a0).offset(0x2L).getSigned());
            }

            v1 += 0x2L;
          } while(MEMORY.ref(2, v1).getSigned() != -0x1L);
        }

        //LAB_8001c76c
        a0 += 0x8L;
      } while(_8004fa98.offset(1, a0).get() != 0x63L || _8004fa98.offset(2, a0).offset(0x2L).getSigned() != 0x63L);

      a1 = FUN_8001c874();
    }

    //LAB_8001c7a0
    final long v1 = FUN_8001c84c(s0, a1);
    if(v1 != -0x2L) {
      return v1;
    }

    //LAB_8001c7ec
    if((FUN_8004d52c((int)sssqChannelIndex_800bd0f8.getSigned()) & 0x1L) == 0) {
      return -0x2L;
    }

    //LAB_8001c808
    return -0x3L;
  }

  @Method(0x8001c84cL)
  public static long FUN_8001c84c(final long a0, final long a1) {
    if(a0 != a1) {
      return a1;
    }

    if(a0 == -0x1L) {
      return -0x1L;
    }

    return -0x2L;
  }

  @Method(0x8001c874L)
  public static long FUN_8001c874() {
    if(_800bd808.get() == 0x38L) {
      for(int i = 0; ; i += 0x8) {
        if(_8004ff10.offset(i).get() == submapCut_80052c30.get()) {
          return _8004ff14.offset(i).get();
        }
      }
    }

    //LAB_8001c8bc
    return _80050068.offset(_800bd808.get() * 0x2L).getSigned();
  }

  @Method(0x8001cae0L)
  public static void FUN_8001cae0(final long address, final long fileSize, final long param) {
    long v0;
    long v1;
    long a0 = address;
    long a1;
    long a2;
    long s0;
    long s1;
    long s2;
    long s3;
    final long s4;
    long s5;
    long s6;
    final long s7;
    s7 = a0;
    v0 = 0x800c_0000L;
    s3 = 0;
    MEMORY.ref(4, v0).offset(-0x2898L).setu(s7);
    v0 = 0x800c_0000L;
    s4 = v0 - 0x3080L;
    v0 = 0x8005_0000L;
    s1 = v0 + 0xf8L;
    s6 = s3;
    v0 = 0x800c_0000L;
    s5 = v0 - 0x3680L;

    //LAB_8001cb34
    do {
      a1 = 0x3L;
      v0 = s3 << a1;
      v0 = s7 + v0;
      v0 = MEMORY.ref(4, v0).offset(0x8L).get();
      v1 = MEMORY.ref(4, s1).offset(0x0L).get();
      s2 = s7 + v0;
      v0 = v1 << a1;
      v0 = v0 - v1;
      v1 = MEMORY.ref(4, s2).offset(0x8L).get();
      v0 = v0 << 2;
      v1 = s2 + v1;
      v1 = MEMORY.ref(2, v1).offset(0x0L).get();
      v0 = v0 + s4;
      MEMORY.ref(2, v0).offset(0x2L).setu(v1);
      v1 = MEMORY.ref(4, s1).offset(0x0L).get();
      a0 = s2;
      v0 = v1 << a1;
      v0 = v0 - v1;
      v0 = v0 << 2;
      v1 = MEMORY.ref(4, s5).offset(0x4L).get();
      v0 = v0 + s4;
      MEMORY.ref(4, v0).offset(0x4L).setu(v1);
      v0 = FUN_80015704(a0, a1);
      a0 = MEMORY.ref(4, s1).offset(0x0L).get();
      a1 = s2;
      v1 = a0 << 3;
      v1 = v1 - a0;
      v1 = v1 << 2;
      v1 = v1 + s4;
      a0 = MEMORY.ref(4, v1).offset(0x4L).get();
      a2 = v0;
      memcpy(a0, a1, (int)a2);
      v0 = MEMORY.ref(4, s1).offset(0x0L).get();

      v1 = v0 << 3;
      v1 = v1 - v0;
      v1 = v1 << 2;
      v1 = v1 + s4;
      s0 = MEMORY.ref(4, v1).offset(0x4L).get();

      v0 = MEMORY.ref(4, s0).offset(0x10L).get();

      v0 = s0 + v0;
      MEMORY.ref(4, v1).offset(0x8L).setu(v0);
      v1 = MEMORY.ref(4, s1).offset(0x0L).get();

      v0 = v1 << 3;
      v0 = v0 - v1;
      v1 = MEMORY.ref(4, s0).offset(0x8L).get();
      v0 = v0 << 2;
      v1 = s0 + v1;
      v1 = MEMORY.ref(2, v1).offset(0x0L).get();
      v0 = v0 + s4;
      MEMORY.ref(2, v0).offset(0x2L).setu(v1);

      if((int)s3 >= 0) {
        if((int)s3 < 0x2L) {
          a0 = 0x8002_0000L;

          //LAB_8001cc30
          a0 = a0 - 0x1734L;
          setSpuDmaCompleteCallback(a0);
        } else {
          a0 = 0x8002_0000L;
          v0 = 0x2L;
          if(s3 == v0) {
            //LAB_8001cc38
            a0 = 0x8002_0000L;
            a0 = a0 - 0x172cL;

            //LAB_8001cc40
            setSpuDmaCompleteCallback(a0);
          }
        }
      }

      //LAB_8001cc48
      v0 = 0x8005_0000L;
      v0 = v0 + 0x190L;

      //LAB_8001cc50
      v0 = s6 + v0;
      s6 = s6 + 0x4L;
      s5 = s5 + 0xcL;
      a2 = MEMORY.ref(4, v0).offset(0x0L).get();
      a0 = MEMORY.ref(4, s2).offset(0x20L).get();
      a1 = MEMORY.ref(4, s0).offset(0x18L).get();
      a0 = s2 + a0;
      a1 = s0 + a1;
      v0 = loadSshdAndSoundbank(a0, MEMORY.ref(4, a1, SshdFile::new), a2);
      a0 = MEMORY.ref(4, s1).offset(0x0L).get();
      s3 = s3 + 0x1L;
      v1 = a0 << 3;
      v1 = v1 - a0;
      v1 = v1 << 2;
      v1 = v1 + s4;
      MEMORY.ref(2, v1).offset(0x10L).setu(v0);
      v1 = MEMORY.ref(4, s1).offset(0x0L).get();

      v0 = v1 << 3;
      v0 = v0 - v1;
      v0 = v0 << 2;
      v0 = v0 + s4;
      v1 = 0x1L;
      MEMORY.ref(2, v0).offset(0x0L).setu(v1);
      s1 = s1 + 0x4L;
    } while((int)s3 < 0x3L);
  }

  @Method(0x8001cce8L)
  public static void FUN_8001cce8(final int bobjIndex, final int type) {
    //TODO GH#3
    if(true) return;

    final BattleObject27c s2 = scriptStatePtrArr_800bc1c0.get(bobjIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);

    //LAB_8001cd3c
    int i;
    for(i = 0; i < 3; i++) {
      final SoundFile soundFile = soundFileArr_800bcf80.get((int)_800500f8.offset(i * 0x4L).get());

      if(soundFile._02.get() == s2.charIndex_272.get()) {
        break;
      }
    }

    //LAB_8001cd78
    final SoundFile soundFile = soundFileArr_800bcf80.get((int)_800500f8.offset(i * 0x4L).get());
    sssqUnloadPlayableSound(soundFile.playableSoundIndex_10.get());
    soundFile.used_00.set(false);

    loadedDrgnFiles_800bcf78.oru(0x8L);

    final int fileIndex;
    if(type != 0) {
      //LAB_8001ce44
      fileIndex = 1298 + s2.charIndex_272.get();
    } else if(s2.charIndex_272.get() != 0 || (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 == 0) {
      //LAB_8001ce18
      fileIndex = 1307 + s2.charIndex_272.get();
    } else {
      fileIndex = 1307;
    }

    //LAB_8001ce70
    loadDrgnBinFile(0, fileIndex, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001ce98", long.class, long.class, long.class), i, 0x4L);
  }

  @Method(0x8001ce98L)
  public static void FUN_8001ce98(final long address, final long fileSize, final long param) {
    assert false;
  }

  @Method(0x8001d068L)
  public static void FUN_8001d068(final int scriptIndex, final long a1) {
    final BattleObject27c s1 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);

    unloadSoundFile(3);
    unloadSoundFile(6);

    //TODO GH#3
    if(true) return;

    if(a1 == 0) {
      //LAB_8001d0e0
      loadedDrgnFiles_800bcf78.oru(0x40L);
      if(s1.charIndex_272.get() != 0 || (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 == 0) {
        //LAB_8001d134
        loadDrgnBinFile(0, 1317 + s1.charIndex_272.get(), 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001e98c", long.class, long.class, long.class), 0, 0x4L);
      } else {
        loadDrgnBinFile(0, 1328, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001e98c", long.class, long.class, long.class), 0, 0x4L);
      }
    } else if(a1 == 1) {
      //LAB_8001d164
      FUN_8001d2d8();
    } else if(a1 == 2) {
      //LAB_8001d174
      loadedDrgnFiles_800bcf78.oru(0x40L);

      //LAB_8001d1a8
      loadDrgnBinFile(0, 1327, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001e98c", long.class, long.class, long.class), 0, 0x4L);
    }

    //LAB_8001d1b0
  }

  @Method(0x8001d1c4L)
  public static void FUN_8001d1c4() {
    loadedDrgnFiles_800bcf78.oru(0x10L);

    final int encounterId = encounterId_800bb0f8.get();
    final int fileIndex;
    if(encounterId == 0x186) {
      //LAB_8001d21c
      fileIndex = 1290;
    } else if(encounterId == 0x1af) {
      //LAB_8001d244
      fileIndex = 1296;
      //LAB_8001d208
    } else if(encounterId == 0x1bb) {
      //LAB_8001d270
      fileIndex = 1292;
    } else {
      //LAB_8001d298
      fileIndex = 778 + encounterId_800bb0f8.get();
    }

    //LAB_8001d2c0
    loadDrgnBinFile(0, fileIndex, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001d51c", long.class, long.class, long.class), 0, 0x4L);
  }

  @Method(0x8001d2d8L)
  public static void FUN_8001d2d8() {
    loadedDrgnFiles_800bcf78.oru(0x10L);
    final int encounterId = encounterId_800bb0f8.get();
    if(encounterId == 0x1af) {
      //LAB_8001d394
      if(_8006f284.get() == 0) {
        loadDrgnBinFile(0, 1296, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001d51c", long.class, long.class, long.class), 0, 0x4L);
      } else {
        //LAB_8001d3d0
        loadDrgnBinFile(0, 1297, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001d51c", long.class, long.class, long.class), 0, 0x4L);
      }
    } else {
      if(encounterId == 0x186) {
        //LAB_8001d330
        if(_8006f284.get() != 0) {
          //LAB_8001d370
          loadDrgnBinFile(0, 1291, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001d51c", long.class, long.class, long.class), 0, 0x4L);
        } else {
          loadDrgnBinFile(0, 1290, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001d51c", long.class, long.class, long.class), 0, 0x4L);
        }
        //LAB_8001d31c
      } else if(encounterId == 0x1bb) {
        //LAB_8001d3f8
        final long v1 = _8006f284.get();
        if(v1 == 0) {
          //LAB_8001d43c
          loadDrgnBinFile(0, 1292, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001d51c", long.class, long.class, long.class), 0, 0x4L);
          //LAB_8001d424
        } else if(v1 == 0x1L) {
          //LAB_8001d464
          loadDrgnBinFile(0, 1293, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001d51c", long.class, long.class, long.class), 0, 0x4L);
        } else if(v1 == 0x4L) {
          //LAB_8001d490
          loadDrgnBinFile(0, 1294, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001d51c", long.class, long.class, long.class), 0, 0x4L);
        } else if(v1 == 0x6L) {
          //LAB_8001d4b8
          loadDrgnBinFile(0, 1295, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001d51c", long.class, long.class, long.class), 0, 0x4L);
        }
      } else {
        //LAB_8001d4dc
        loadDrgnBinFile(0, 778 + encounterId, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001d51c", long.class, long.class, long.class), 0, 0x4L);
      }
    }

    //LAB_8001d50c
  }

  @Method(0x8001d51cL)
  public static void FUN_8001d51c(final long address, final long fileSize, final long param) {
    long v0;
    long v1;
    long a0 = address;
    long a1;
    long a2;
    long s0;
    long s1;
    long s2;
    long s3;
    final long s4;
    final long s5;
    long s6;

    final byte[] sp0x10 = new byte[4];
    final byte[] sp0x18 = new byte[4];

    s5 = a0;
    s6 = 0x5_a1e0L;
    a1 = soundFileArr_800bcf80.getAddress();

    //LAB_8001d588
    for(int i = 0; i < 4; i++) {
      MEMORY.ref(2, a1).offset(_800500e8.offset(i * 0x4L).get() * 0x1cL).offset(0x2L).setu(-0x1L);
      MEMORY.ref(2, a1).offset(_800500e8.offset(i * 0x4L).get() * 0x1cL).setu(0);
    }

    a0 = 0x800c_0000L;
    v1 = MEMORY.ref(4, s5).offset(0x4L).get();
    v0 = 0x800c_0000L;
    MEMORY.ref(4, v0).offset(-0x28a8L).setu(s5);
    MEMORY.ref(4, a0).offset(-0x2884L).setu(v1);

    //LAB_8001d5f8
    for(int i = 0; i < MEMORY.ref(4, s5).offset(0x4L).get(); i++) {
      s2 = s5 + MEMORY.ref(4, s5).offset(i * 0x8L).offset(0x8L).get();

      if(MEMORY.ref(4, s2).offset(0x24L).get() >= 0xbL) {
        sp0x10[i] = 1;
      }

      //LAB_8001d624
    }

    //LAB_8001d63c

    //LAB_8001d658
    s1 = MEMORY.ref(4, s5).offset(0x4L).get();
    while((int)s1 > 0) {
      v1 = s1 - 0x1L;

      if(sp0x10[(int)v1] == 1) {
        sp0x18[(int)v1] = 1;
        break;
      }

      //LAB_8001d678
      if(s1 == 0x1L) {
        FUN_8001eaa0();
        return;
      }

      //LAB_8001d690
      s1 = v1;
    }

    //LAB_8001d698
    s4 = soundFileArr_800bcf80.getAddress();
    s3 = _800500e8.getAddress();

    //LAB_8001d6c0
    for(int i = 0; i < MEMORY.ref(4, s5).offset(0x4L).get(); i++) {
      s2 = s5 + MEMORY.ref(4, s5).offset(i * 0x8L).offset(0x8L).get();
      a1 = MEMORY.ref(4, s2).offset(0x24L).get();

      if(a1 <= 0x30L) {
        if(sp0x18[i] == 1) {
          FUN_8001eaa0();
        }
      } else {
        //LAB_8001d704
        a0 = 0x3L;
        v1 = 0xc750L;
        a2 = MEMORY.ref(4, s2).offset(0x24L).get();
        _800bd774.setu(a1);

        //LAB_8001d718
        do {
          if(a2 >= v1) {
            break;
          }
          a0 = a0 - 0x1L;
          v1 = v1 - 0x4270L;
        } while((int)a0 >= 0);

        //LAB_8001d72c
        v0 = FUN_80015704(s2, 0x3L);
        v0 = mallocTail(v0);
        MEMORY.ref(4, s4).offset(MEMORY.ref(4, s3).get() * 0x1cL).offset(0x4L).setu(v0);
        v0 = FUN_80015704(s2, 0x3L);
        a0 = MEMORY.ref(4, s4).offset(MEMORY.ref(4, s3).get() * 0x1cL).offset(0x4L).get();
        memcpy(a0, s2, (int)v0);

        v1 = s4 + MEMORY.ref(4, s3).get() * 0x1cL;
        s0 = MEMORY.ref(4, v1).offset(0x4L).get();
        MEMORY.ref(4, v1).offset(0x8L).setu(s0 + MEMORY.ref(4, s0).offset(0x10L).get());

        v1 = s0 + MEMORY.ref(4, s0).offset(0x8L).get();
        v1 = MEMORY.ref(2, v1).offset(0x0L).get();
        MEMORY.ref(2, s4).offset(MEMORY.ref(4, s3).get() * 0x1cL).offset(0x2L).setu(v1);

        if(sp0x18[i] != 1) {
          a0 = getMethodAddress(Scus94491BpeSegment.class, "FUN_8001ea98");
        } else {
          //LAB_8001d804
          a0 = getMethodAddress(Scus94491BpeSegment.class, "FUN_8001eaa0");
        }

        //LAB_8001d80c
        setSpuDmaCompleteCallback(a0);
        a0 = s2 + MEMORY.ref(4, s2).offset(0x20L).get();
        a1 = s0 + MEMORY.ref(4, s0).offset(0x18L).get();
        v0 = loadSshdAndSoundbank(a0, MEMORY.ref(4, a1, SshdFile::new), s6);
        MEMORY.ref(2, s4).offset(MEMORY.ref(4, s3).get() * 0x1cL).offset(0x10L).setu(v0);
        v1 = MEMORY.ref(4, s2).offset(0x24L).get();
        v1 += v1 & 0xfL;
        s6 = s6 + v1;
        FUN_8004cb0c(MEMORY.ref(2, s4).offset(MEMORY.ref(4, s3).get() * 0x1cL).offset(0x10L).getSigned(), 0x7fL);
        MEMORY.ref(2, s4).offset(MEMORY.ref(4, s3).get() * 0x1cL).setu(0x1L);
      }

      //LAB_8001d894
      //LAB_8001d898
      s3 = s3 + 0x4L;
    }

    //LAB_8001d8ac
  }

  @Method(0x8001d9d0L)
  public static void FUN_8001d9d0() {
    final long struct = _80109a98.offset(encounterId_800bb0f8.get() * 0x10L).getAddress();

    if(MEMORY.ref(1, struct).offset(0x1L).get() != 0xffL) {
//      loadedDrgnFiles_800bcf78.oru(0x80L); //TODO GH#3
      _800bd782.addu(0x1L);
      if(true) return;

      final int fileIndex;
      final long callback;
      final long callbackParam;
      if((MEMORY.ref(1, struct).offset(0x1L).get() & 0x1fL) == 0x13L) {
        unloadSoundFile(8);
        fileIndex = 732;
        callback = getMethodAddress(Scus94491BpeSegment.class, "musicPackageLoadedCallback", long.class, long.class, long.class);
        callbackParam = 0x2_dc00L;
      } else {
        //LAB_8001da58
        fileIndex = (int)_800501bc.get((int)(MEMORY.ref(1, struct).offset(0x1L).get() & 0x1fL)).get();
        callback = getMethodAddress(Scus94491BpeSegment.class, "FUN_8001fb44", long.class, long.class, long.class);
        callbackParam = 0;
      }

      //LAB_8001daa4
      loadDrgnBinFile(0, fileIndex, 0, callback, callbackParam, 0x4L);
    }

    //LAB_8001daac
  }

  @Method(0x8001dabcL)
  public static void musicPackageLoadedCallback(final long addressPtr, final long fileSize, final long a2) {
    //TODO GH#3
    _800bd782.addu(0x1L);
    if(true) return;

    LOGGER.info("Music package %d loaded", a2 >> 8);

    final MrgFile soundMrg = MEMORY.ref(4, addressPtr, MrgFile::new);
    soundMrgPtr_800bd76c.set(soundMrg);

    if(mainCallbackIndex_8004dd20.get() == 0x5L || mainCallbackIndex_8004dd20.get() == 0x6L && encounterId_800bb0f8.get() == 443) { // Melbu
      //LAB_8001db1c
      memcpy(soundMrgSshdPtr_800bd784.getPointer(), soundMrg.getFile(3), (int)soundMrg.entries.get(3).size.get());
      memcpy(soundMrgSssqPtr_800bd788.getPointer(), soundMrg.getFile(2), (int)soundMrg.entries.get(2).size.get());

      _800bd0fc.setu(a2);
      soundFileArr_800bcf80.get(11).spuRamOffset_14.set(0);
      soundFileArr_800bcf80.get(11)._02.set((short)MEMORY.ref(2, soundMrg.getFile(0)).get());
      soundFileArr_800bcf80.get(11)._18.set((int)(MEMORY.ref(1, soundMrg.getFile(1)).get() - 0x1L));
      setSpuDmaCompleteCallback(getMethodAddress(Scus94491BpeSegment.class, "FUN_8001f810"));
      soundFileArr_800bcf80.get(11).playableSoundIndex_10.set(loadSshdAndSoundbank(soundMrg.getFile(4), soundMrgSshdPtr_800bd784.deref(), 0x2_1f70L + soundFileArr_800bcf80.get(11).spuRamOffset_14.get()));
      soundFileArr_800bcf80.get(11).used_00.set(true);
      soundFileArr_800bcf80.get(11).spuRamOffset_14.add(soundMrg.entries.get(4).size.get());
      sssqChannelIndex_800bd0f8.setu(FUN_8004c1f8(soundFileArr_800bcf80.get(11).playableSoundIndex_10.get(), soundMrgSssqPtr_800bd788.deref()));
      _800bd781.setu(0x1L);
    } else {
      //LAB_8001dbf0
      if(soundMrg.count.get() == 0x5L) {
        soundFileArr_800bcf80.get(11).soundMrgPtr_04.set(MEMORY.ref(4, mallocTail(soundMrg.entries.get(4).offset.get()), MrgFile::new));
        memcpy(soundFileArr_800bcf80.get(11).soundMrgPtr_04.getPointer(), soundMrg.getAddress(), (int)soundMrg.entries.get(4).offset.get());
        _800bd0fc.setu(a2);
        soundFileArr_800bcf80.get(11).spuRamOffset_14.set(0);
        soundFileArr_800bcf80.get(11)._02.set((short)MEMORY.ref(2, soundFileArr_800bcf80.get(11).soundMrgPtr_04.deref().getFile(0)).get());
        soundFileArr_800bcf80.get(11)._18.set((int)(MEMORY.ref(1, soundMrg.getFile(1)).get() - 0x1L));
        setSpuDmaCompleteCallback(getMethodAddress(Scus94491BpeSegment.class, "FUN_8001f810"));
        soundFileArr_800bcf80.get(11).playableSoundIndex_10.set(loadSshdAndSoundbank(soundMrg.getFile(4), soundFileArr_800bcf80.get(11).soundMrgPtr_04.deref().getFile(3, SshdFile::new), 0x2_1f70L + soundFileArr_800bcf80.get(11).spuRamOffset_14.get()));
        soundFileArr_800bcf80.get(11).used_00.set(true);
        soundFileArr_800bcf80.get(11).spuRamOffset_14.add(soundMrg.entries.get(4).size.get());
        sssqChannelIndex_800bd0f8.setu(FUN_8004c1f8(soundFileArr_800bcf80.get(11).playableSoundIndex_10.get(), soundFileArr_800bcf80.get(11).soundMrgPtr_04.deref().getFile(2, SssqFile::new)));
      } else {
        //LAB_8001dcdc
        soundFileArr_800bcf80.get(11).soundMrgPtr_04.set(MEMORY.ref(4, mallocTail(soundMrg.entries.get(3).offset.get()), MrgFile::new));
        memcpy(soundFileArr_800bcf80.get(11).soundMrgPtr_04.getPointer(), soundMrg.getAddress(), (int)soundMrg.entries.get(3).offset.get());
        _800bd0fc.setu(a2);
        soundFileArr_800bcf80.get(11)._02.set((short)MEMORY.ref(2, soundFileArr_800bcf80.get(11).soundMrgPtr_04.deref().getFile(0)).get());

        final long callback;
        if((a2 & 0x1L) == 0) {
          callback = getMethodAddress(Scus94491BpeSegment.class, "FUN_8001fa18");
        } else {
          //LAB_8001dd3c
          callback = getMethodAddress(Scus94491BpeSegment.class, "FUN_8001fab4");
        }

        //LAB_8001dd44
        setSpuDmaCompleteCallback(callback);

        soundFileArr_800bcf80.get(11).playableSoundIndex_10.set(loadSshdAndSoundbank(soundMrg.getFile(3), soundFileArr_800bcf80.get(11).soundMrgPtr_04.deref().getFile(2, SshdFile::new), 0x2_1f70L));
        soundFileArr_800bcf80.get(11).used_00.set(true);
        sssqChannelIndex_800bd0f8.setu(FUN_8004c1f8(soundFileArr_800bcf80.get(11).playableSoundIndex_10.get(), soundFileArr_800bcf80.get(11).soundMrgPtr_04.deref().getFile(1, SssqFile::new)));
      }

      //LAB_8001dd98
      _800bd781.setu(0);
    }

    //LAB_8001dda0
    FUN_8001b1a8(0x28L);
    _800bd0f0.setu(0x2L);
  }

  @Method(0x8001ddd8L)
  public static void FUN_8001ddd8() {
    free(soundMrgPtr_800bd76c.getPointer());
    FUN_8004cf8c((int)sssqChannelIndex_800bd0f8.getSigned());
    _800bd610.offset(2, 0x60L).setu(0x2L);
    final long a2 = _800bd610.offset(4, 0x64L).get();
    final long v0 = a2 + MEMORY.ref(4, a2).offset(0x10L).get();
    final long a1 = a2 + MEMORY.ref(4, a2).offset(0x18L).get();
    _800bd610.offset(2, 0x6cL).setu(FUN_8004c1f8(soundFileArr_800bcf80.get((int)MEMORY.ref(2, v0).getSigned()).playableSoundIndex_10.get(), MEMORY.ref(4, a1, SssqFile::new)));
    _800bd782.addu(0x1L);
    loadedDrgnFiles_800bcf78.and(0xffff_ff7fL);
  }

  @Method(0x8001de84L)
  public static void FUN_8001de84(final int a0) {
    unloadSoundFile(1);
    unloadSoundFile(3);
    unloadSoundFile(4);
    unloadSoundFile(5);
    unloadSoundFile(6);

    if(_80109a98.offset(encounterId_800bb0f8.get() * 0x10L).offset(1, 0x1L).get() != 0xffL) {
      FUN_800201c8(0x6L);

      final long v1 = _8005019c.offset(1, _80109a98.offset(encounterId_800bb0f8.get() * 0x10L).offset(1, 0x1L).get() & 0x1fL).get();
      if(v1 == 0xcL) {
        FUN_8001fcf4(696);
      } else if(v1 == 0xdL) {
        //LAB_8001df68
        FUN_8001fcf4(697);
      } else if(v1 == 0xeL) {
        //LAB_8001df70
        FUN_8001fcf4(698);
      } else if(v1 == 0xfL) {
        //LAB_8001df78
        FUN_8001fcf4(699);
        //LAB_8001df44
      } else if(v1 == 0x56L) {
        //LAB_8001df84
        FUN_8001fcf4(700);
      } else if(v1 == 0x58L) {
        //LAB_8001df80
        FUN_8001fcf4(701);
      }

      //LAB_8001df8c
      unloadSoundFile(8);
      FUN_8001d9d0();
    }

    //LAB_8001df9c
    loadDrgnBinFile(0, (int)_80050104.offset(FUN_8001a810() * 0x4L).get(), 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001cae0", long.class, long.class, long.class), 0, 0x4L);
    loadedDrgnFiles_800bcf78.oru(0x8L);
    FUN_8001d1c4();
    decrementOverlayCount();
  }

  //TODO verify this method
  @Method(0x8001e010L)
  public static void FUN_8001e010(final long a0) {
    final long v1;
    final long a1;
    final long s0;
    if(a0 == 0) {
      //LAB_8001e054
      FUN_80020360(spu28Arr_800bd110, spu28Arr_800bca78);
      FUN_8001ad18();
      unloadSoundFile(8);
      unloadSoundFile(8);

//      loadedDrgnFiles_800bcf78.oru(0x80L); TODO GH#3
      _800bd782.addu(0x1L);
//      loadDrgnBinFile(0, 5815, 0, getMethodAddress(Scus94491BpeSegment.class, "musicPackageLoadedCallback", long.class, long.class, long.class), 5815 * 0x100L, 0x4L);

      //LAB_8001e044
    } else if(a0 == 0x1L) {
      //LAB_8001e094
      FUN_8001ad18();
      unloadSoundFile(8);
      unloadSoundFile(8);

      //LAB_8001e0bc
//      loadedDrgnFiles_800bcf78.oru(0x80L); TODO GH#3
      _800bd782.addu(0x1L);
//      loadDrgnBinFile(0, 5900, 0, getMethodAddress(Scus94491BpeSegment.class, "musicPackageLoadedCallback", long.class, long.class, long.class), 5900 * 0x100L, 0x4L);
    } else if((int)a0 == -0x1L) {
      //LAB_8001e0f8
      if(_800bdc34.get() != 0) {
        if(mainCallbackIndex_8004dd20.get() == 0x8L && gameState_800babc8._4e4.get() != 0) {
          sssqResetStuff();
          unloadSoundFile(8);

          //LAB_8001e23c
//          loadedDrgnFiles_800bcf78.oru(0x80L); TODO GH#3
          _800bd782.addu(0x1L);
//          loadDrgnBinFile(0, 5850, 0, getMethodAddress(Scus94491BpeSegment.class, "musicPackageLoadedCallback", long.class, long.class, long.class), 5850 * 0x100L, 0x4L);
        }
      } else {
        //LAB_8001e160
        FUN_800201c8(0x6L);
        unloadSoundFile(8);
        v1 = mainCallbackIndex_8004dd20.get();
        if(v1 == 0x5L) {
          //LAB_8001e1ac
          s0 = FUN_8001c60c();
          if((int)s0 == -0x2L || (int)s0 == -0x3L) {
            if((int)s0 == -0x2L) {
              //LAB_8001e1f4
              FUN_8001ada0();
            }
            //LAB_8001e1dc
          } else if((int)s0 < -0x1L || s0 != a0) {
            //LAB_8001e20c
            unloadSoundFile(8);
            a1 = 5815 + s0 * 5;

            //LAB_8001e23c
//            loadedDrgnFiles_800bcf78.oru(0x80L); TODO GH#3
            _800bd782.addu(0x1L);
//            loadDrgnBinFile(0, a1, 0, getMethodAddress(Scus94491BpeSegment.class, "musicPackageLoadedCallback", long.class, long.class, long.class), a1 * 0x100L, 0x4L);
          } else {
            FUN_8001ae90();

            //LAB_8001e200
            _800bd782.setu(0x1L);
          }
        } else if(v1 == 0x6L) {
          //LAB_8001e264
          sssqResetStuff();
        } else if(v1 == 0x8L) {
          unloadSoundFile(8);
        }
      }

      //LAB_8001e26c
      FUN_8001ad18();
      FUN_80020360(spu28Arr_800bca78, spu28Arr_800bd110);

      //LAB_8001e288
    }
  }

  @Method(0x8001e29cL)
  public static void unloadSoundFile(final int fileIndex) {
    switch(fileIndex) {
      case 0 -> {
        if(soundFileArr_800bcf80.get(0).used_00.get()) {
          sssqUnloadPlayableSound(soundFileArr_800bcf80.get(0).playableSoundIndex_10.get());
          free(soundFileArr_800bcf80.get(0).soundMrgPtr_04.getPointer());
          soundFileArr_800bcf80.get(0).used_00.set(false);
        }
      }

      case 1 -> {
        //LAB_8001e324
        for(int i = 0; i < 3; i++) {
          if(soundFileArr_800bcf80.get((int)_800500f8.offset(i * 0x4L).get()).used_00.get()) {
            sssqUnloadPlayableSound(soundFileArr_800bcf80.get((int)_800500f8.offset(i * 0x4L).get()).playableSoundIndex_10.get());
            soundFileArr_800bcf80.get((int)_800500f8.offset(i * 0x4L).get()).used_00.set(false);
          }

          //LAB_8001e374
        }
      }

      case 2 -> {
        if(soundFileArr_800bcf80.get(4).used_00.get()) {
          sssqUnloadPlayableSound(soundFileArr_800bcf80.get(4).playableSoundIndex_10.get());
          free(soundFileArr_800bcf80.get(4).soundMrgPtr_04.getPointer());
          soundFileArr_800bcf80.get(4).used_00.set(false);
        }
      }

      case 3 -> {
        //LAB_8001e3dc
        for(int i = 0; i < 4; i++) {
          if(soundFileArr_800bcf80.get((int)_800500e8.offset(i * 0x4L).get()).used_00.get()) {
            free(soundFileArr_800bcf80.get((int)_800500e8.offset(i * 0x4L).get()).soundMrgPtr_04.getPointer());
            sssqUnloadPlayableSound(soundFileArr_800bcf80.get((int)_800500e8.offset(i * 0x4L).get()).playableSoundIndex_10.get());
            soundFileArr_800bcf80.get((int)_800500e8.offset(i * 0x4L).get()).used_00.set(false);
          }

          //LAB_8001e450
        }
      }

      case 4 -> {
        if(soundFileArr_800bcf80.get(8).used_00.get()) {
          sssqUnloadPlayableSound(soundFileArr_800bcf80.get(8).playableSoundIndex_10.get());
          free(soundFileArr_800bcf80.get(8).soundMrgPtr_04.getPointer());
          soundFileArr_800bcf80.get(8).used_00.set(false);
        }
      }

      case 5 -> {
        if(soundFileArr_800bcf80.get(9).used_00.get()) {
          sssqUnloadPlayableSound(soundFileArr_800bcf80.get(9).playableSoundIndex_10.get());
          free(soundFileArr_800bcf80.get(9).soundMrgPtr_04.getPointer());
          soundFileArr_800bcf80.get(9).used_00.set(false);
        }
      }

      case 6, 7 -> {
        if(soundFileArr_800bcf80.get(10).used_00.get()) {
          sssqUnloadPlayableSound(soundFileArr_800bcf80.get(10).playableSoundIndex_10.get());
          free(soundFileArr_800bcf80.get(10).soundMrgPtr_04.getPointer());
          soundFileArr_800bcf80.get(10).used_00.set(false);
        }
      }

      case 8 -> {
        if(_800bd0f0.getSigned() != 0) {
          FUN_8004d034((int)sssqChannelIndex_800bd0f8.getSigned(), 0x1L);
          FUN_8004c390((int)sssqChannelIndex_800bd0f8.getSigned());

          if(_800bd781.get() == 0) {
            free(soundFileArr_800bcf80.get(11).soundMrgPtr_04.getPointer());
          }

          //LAB_8001e56c
          _800bd0f0.setu(0);
        }

        //LAB_8001e570
        if(soundFileArr_800bcf80.get(11).used_00.get()) {
          sssqUnloadPlayableSound(soundFileArr_800bcf80.get(11).playableSoundIndex_10.get());
          soundFileArr_800bcf80.get(11).used_00.set(false);
        }
      }

      case 9 -> {
        if(soundFileArr_800bcf80.get(12).used_00.get()) {
          sssqUnloadPlayableSound(soundFileArr_800bcf80.get(12).playableSoundIndex_10.get());
          free(soundFileArr_800bcf80.get(12).soundMrgPtr_04.getPointer());
          soundFileArr_800bcf80.get(12).used_00.set(false);
        }
      }
    }

    //LAB_8001e5d4
  }

  @Method(0x8001e5ecL)
  public static void loadDRGN0_mrg_62802_sounds() {
    loadedDrgnFiles_800bcf78.oru(0x1L);
    loadDrgnBinFile(0, 5739, 0, getMethodAddress(Scus94491BpeSegment.class, "DRGN0_mrg_62802_loaded", long.class, long.class, long.class), 0, 0x4L);
  }

  /**
   * 0: unknown, 2-byte file (00 00)
   * 1: unknown, 0x64 byte file, counts up from 0000 to 0033
   * 2: SShd file
   * 3: Soundbank (has some map and battle sounds)
   */
  @Method(0x8001e694L)
  public static void DRGN0_mrg_62802_loaded(final long address, final long fileSize, final long a2) {
    FUN_800156f4(0x1L);

    final MrgFile mrg = MEMORY.ref(4, address, MrgFile::new);

    soundbank_800bd778.setu(mallocHead(mrg.entries.get(3).size.get()));
    memcpy(soundbank_800bd778.get(), mrg.getFile(3), (int)mrg.entries.get(3).size.get());

    soundFileArr_800bcf80.get(0).soundMrgPtr_04.set(MEMORY.ref(4, mallocTail(mrg.entries.get(3).offset.get()), MrgFile::new));
    memcpy(soundFileArr_800bcf80.get(0).soundMrgPtr_04.getPointer(), address, (int)mrg.entries.get(3).offset.get());

    free(address);

    soundFileArr_800bcf80.get(0).ptr_08.set(soundFileArr_800bcf80.get(0).soundMrgPtr_04.deref().getFile(1));
    setSpuDmaCompleteCallback(getMethodAddress(Scus94491BpeSegment.class, "unloadSoundbank_800bd778"));

    soundFileArr_800bcf80.get(0).playableSoundIndex_10.set(loadSshdAndSoundbank(soundbank_800bd778.get(), soundFileArr_800bcf80.get(0).soundMrgPtr_04.deref().getFile(2, SshdFile::new), 0x1010L));
    soundFileArr_800bcf80.get(0).used_00.set(true);
  }

  @Method(0x8001e780L)
  public static void unloadSoundbank_800bd778() {
    free(soundbank_800bd778.get());
    FUN_800156f4(0);
    loadedDrgnFiles_800bcf78.and(0xffff_fffeL);
  }

  @Method(0x8001e8ccL)
  public static void FUN_8001e8cc() {
    // empty
  }

  @Method(0x8001e8d4L)
  public static void FUN_8001e8d4() {
    free(_800bd768.get());
    loadedDrgnFiles_800bcf78.and(0xffff_fff7L);
  }

  @Method(0x8001e918L)
  public static long FUN_8001e918(final RunningScript a0) {
    return 0;
  }

  @Method(0x8001e920L)
  public static long FUN_8001e920(final RunningScript script) {
    FUN_8001cce8(script.params_20.get(0).deref().get(), script.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x8001e98cL)
  public static void FUN_8001e98c(final long address, final long fileSize, final long param) {
    final MrgFile mrg = MEMORY.ref(4, address, MrgFile::new);
    soundMrgPtr_800bd748.set(mrg);
    soundFileArr_800bcf80.get(4).soundMrgPtr_04.setPointer(mallocTail(mrg.entries.get(3).offset.get()));
    memcpy(soundFileArr_800bcf80.get(4).soundMrgPtr_04.getPointer(), mrg.getAddress(), (int)mrg.entries.get(3).offset.get());

    final MrgFile s0 = soundFileArr_800bcf80.get(4).soundMrgPtr_04.deref();
    soundFileArr_800bcf80.get(4).ptr_08.set(s0.getFile(1));
    soundFileArr_800bcf80.get(4)._02.set((short)MEMORY.ref(2, s0.getFile(0)).get());
    setSpuDmaCompleteCallback(getMethodAddress(Scus94491BpeSegment.class, "FUN_8001ea5c"));

    final short soundIndex = loadSshdAndSoundbank(mrg.getFile(3), mrg.getFile(2, SshdFile::new), 0x5_a1e0);
    soundFileArr_800bcf80.get(4).playableSoundIndex_10.set(soundIndex);
    FUN_8004cb0c(soundIndex, 0x7fL);
    soundFileArr_800bcf80.get(4).used_00.set(true);
  }

  @Method(0x8001ea5cL)
  public static void FUN_8001ea5c() {
    free(soundMrgPtr_800bd748.getPointer());
    loadedDrgnFiles_800bcf78.and(0xffff_ffbfL);
  }

  @Method(0x8001ea98L)
  public static void FUN_8001ea98() {
    // no-op
  }

  @Method(0x8001eaa0L)
  public static void FUN_8001eaa0() {
    free(_800bd758.get());
    loadedDrgnFiles_800bcf78.and(0xffff_ffefL);
  }

  @Method(0x8001eadcL)
  public static void FUN_8001eadc(final int index) {
    loadedDrgnFiles_800bcf78.oru(0x2L);

    //TODO GH#3
    loadedDrgnFiles_800bcf78.and(0xffff_fffdL);
    _800bd782.addu(0x1L);
    if(true) return;

    loadDrgnBinFile(0, 5750 + index, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001eb38", long.class, long.class, long.class), 0, 0x4L);
  }

  @Method(0x8001eb38L)
  public static void FUN_8001eb38(final long address, final long fileSize, final long a2) {
    final MrgFile mrg = MEMORY.ref(4, address, MrgFile::new);
    soundMrgPtr_800bd748.set(mrg);

    final MrgFile mrg2 = MEMORY.ref(4, mallocTail(mrg.entries.get(4).offset.get()), MrgFile::new);
    soundFileArr_800bcf80.get(8).soundMrgPtr_04.set(mrg2);

    memcpy(mrg2.getAddress(), mrg.getAddress(), (int)mrg.entries.get(4).offset.get());

    soundFileArr_800bcf80.get(8).ptr_08.set(mrg2.getFile(2)); //TODO this might be an SSsq
    soundFileArr_800bcf80.get(8).ptr_0c.set(mrg2.getFile(1));
    soundFileArr_800bcf80.get(8)._02.set((short)MEMORY.ref(2, mrg2.getFile(0)).get());
    setSpuDmaCompleteCallback(getMethodAddress(Scus94491BpeSegment.class, "FUN_8001ec18"));
    soundFileArr_800bcf80.get(8).playableSoundIndex_10.set(loadSshdAndSoundbank(mrg.getFile(4), mrg2.getFile(3, SshdFile::new), 0x4_de90L));
    FUN_8004cb0c(soundFileArr_800bcf80.get(8).playableSoundIndex_10.get(), 0x7fL);
    soundFileArr_800bcf80.get(8).used_00.set(true);
  }

  @Method(0x8001ec18L)
  public static void FUN_8001ec18() {
    free(soundMrgPtr_800bd748.getPointer());
    loadedDrgnFiles_800bcf78.and(0xffff_fffdL);
    _800bd782.addu(0x1L);
  }

  @Method(0x8001ecccL)
  public static long FUN_8001eccc(final RunningScript a0) {
    //TODO GH#3
    sssqResetStuff();
    _800bd782.addu(0x1L);
    if(true) return 0;

    loadedDrgnFiles_800bcf78.oru(0x4L);
    sssqResetStuff();
    loadDrgnBinFile(0, 2437 + a0.params_20.get(0).deref().get() * 3, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001d8d8", long.class, long.class, long.class), 0, 0x4L);
    return 0;
  }

  @Method(0x8001eea8L)
  public static void FUN_8001eea8(final int index) {
    loadedDrgnFiles_800bcf78.oru(0x8000L);
    loadDrgnBinFile(0, 5740 + index, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001eefc", long.class, long.class, long.class), 0, 0x4L);
  }

  @Method(0x8001eefcL)
  public static void FUN_8001eefc(final long address, final long size, final long param) {
    long v0;
    final long s0;
    final long s1;

    soundMrgPtr_800bd748.set(MEMORY.ref(4, address, MrgFile::new));
    v0 = mallocTail(MEMORY.ref(4, address).offset(0x28L).get());
    s1 = soundFileArr_800bcf80.getAddress();
    MEMORY.ref(4, s1).offset(0x154L).setu(v0);
    memcpy(v0, address, (int)MEMORY.ref(4, address).offset(0x28L).get());
    s0 = MEMORY.ref(4, s1).offset(0x154L).get();

    v0 = s0 + MEMORY.ref(4, s0).offset(0x18L).get();
    MEMORY.ref(4, s1).offset(0x158L).setu(v0);
    v0 = s0 + MEMORY.ref(4, s0).offset(0x8L).get();
    v0 = MEMORY.ref(2, v0).offset(0x0L).get();
    MEMORY.ref(2, s1).offset(0x152L).setu(v0);
    setSpuDmaCompleteCallback(getMethodAddress(Scus94491BpeSegment.class, "FUN_8001efcc"));
    v0 = loadSshdAndSoundbank(address + MEMORY.ref(4, address).offset(0x28L).get(), MEMORY.ref(4, s0 + MEMORY.ref(4, s0).offset(0x20L).get(), SshdFile::new), 0x4_de90L);
    MEMORY.ref(2, s1).offset(0x160L).setu(v0);
    FUN_8004cb0c((short)v0, 0x7fL);
    MEMORY.ref(2, s1).offset(0x150L).setu(0x1L);
  }

  @Method(0x8001efccL)
  public static void FUN_8001efcc() {
    free(soundMrgPtr_800bd748.getPointer());
    loadedDrgnFiles_800bcf78.and(0xffff_7fffL);
  }

  @Method(0x8001f070L)
  public static long FUN_8001f070(final RunningScript a0) {
    loadedDrgnFiles_800bcf78.oru(0x20L);
    unloadSoundFile(6);
    loadDrgnBinFile(0, 1841 + a0.params_20.get(0).deref().get(), 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001f0dc", long.class, long.class, long.class), 0, 0x4L);
    return 0;
  }

  @Method(0x8001f0dcL)
  public static void FUN_8001f0dc(final long address, final long fileSize, final long param) {
    final MrgFile mrg1 = MEMORY.ref(4, address, MrgFile::new);
    soundMrgPtr_800bd748.set(mrg1);
    soundFileArr_800bcf80.get(10).soundMrgPtr_04.setPointer(mallocTail(mrg1.entries.get(3).offset.get()));
    memcpy(soundFileArr_800bcf80.get(10).soundMrgPtr_04.getPointer(), mrg1.getAddress(), (int)mrg1.entries.get(3).offset.get());
    final MrgFile mrg2 = soundFileArr_800bcf80.get(10).soundMrgPtr_04.deref();
    soundFileArr_800bcf80.get(10).ptr_08.set(mrg2.getFile(1));
    soundFileArr_800bcf80.get(10)._02.set((short)MEMORY.ref(2, mrg2.getFile(0)).get());
    setSpuDmaCompleteCallback(0);
    soundFileArr_800bcf80.get(10).playableSoundIndex_10.set(loadSshdAndSoundbank(mrg1.getFile(3), mrg2.getFile(2, SshdFile::new), 0x6_6930L));

    //LAB_8001f17c
    while(isSpuDmaTransferInProgress()) {
      DebugHelper.sleep(1);
    }

    final long s0 = soundFileArr_800bcf80.getAddress();
    FUN_8004cb0c(MEMORY.ref(2, s0).offset(0x128L).getSigned(), 0x7fL);
    MEMORY.ref(2, s0).offset(0x118L).setu(0x1L);
    free(soundMrgPtr_800bd748.getPointer());
    loadedDrgnFiles_800bcf78.and(0xffff_ffdfL);
  }

  @Method(0x8001f250L)
  public static long FUN_8001f250(final RunningScript a0) {
    //TODO GH#3

//    loadedDrgnFiles_800bcf78.oru(0x1_0000L);
    unloadSoundFile(7);
//    loadDrgnBinFile(0, 1897 + a0.params_20.get(0).deref().get(), 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001f2c0", long.class, long.class, long.class), 0, 0x4L);
    return 0;
  }

  @Method(0x8001f2c0L)
  public static void FUN_8001f2c0(final long address, final long fileSize, final long param) {
    //TODO GH#3
    loadedDrgnFiles_800bcf78.and(0xfffe_ffffL);
    if(true) {
      return;
    }

    long v0;
    final long s0;
    final long s1;
    final MrgFile mrg = MEMORY.ref(4, address, MrgFile::new);
    soundMrgPtr_800bd748.set(mrg);
    v0 = mallocTail(MEMORY.ref(4, address).offset(0x20L).get());
    s1 = soundFileArr_800bcf80.getAddress();
    MEMORY.ref(4, s1).offset(0x11cL).setu(v0);
    memcpy(v0, address, (int)MEMORY.ref(4, address).offset(0x20L).get());
    s0 = MEMORY.ref(4, s1).offset(0x11cL).get();
    v0 = s0 + MEMORY.ref(4, s0).offset(0x10L).get();
    MEMORY.ref(4, s1).offset(0x120L).setu(v0);
    v0 = s0 + MEMORY.ref(4, s0).offset(0x8L).get();
    v0 = MEMORY.ref(2, v0).offset(0x0L).get();
    MEMORY.ref(2, s1).offset(0x11aL).setu(v0);
    setSpuDmaCompleteCallback(getMethodAddress(Scus94491BpeSegment.class, "FUN_8001f390"));
    v0 = loadSshdAndSoundbank(address + MEMORY.ref(4, address).offset(0x20L).get(), MEMORY.ref(4, s0 + MEMORY.ref(4, s0).offset(0x18L).get(), SshdFile::new), 0x6_6930L);
    MEMORY.ref(2, s1).offset(0x128L).setu(v0);
    FUN_8004cb0c(v0, 0x7fL);
    MEMORY.ref(2, s1).offset(0x118L).setu(0x1L);
  }

  @Method(0x8001f390L)
  public static void FUN_8001f390() {
    free(soundMrgPtr_800bd748.getPointer());
    loadedDrgnFiles_800bcf78.and(0xfffe_ffffL);
  }

  /**
   * Loads an audio MRG from DRGN0. File index is 5815 + index * 5.
   *
   * @param index <ol start="0">
   *   <li>Inventory music</li>
   *   <li>Main menu music</li>
   *   <li>Seems to be the previous one and the next one combined?</li>
   *   <li>Battle music, more?</li>
   *   <li>Same as previous</li>
   *   <li>...</li>
   * </ol>
   */
  @Method(0x8001f3d0L)
  public static void loadMusicPackage(final long index, final long a1) {
    unloadSoundFile(8);
//    loadedDrgnFiles_800bcf78.oru(0x80L); //TODO GH#3
    _800bd782.addu(0x1L);
//    final long fileIndex = 5815 + index * 5;
//    loadDrgnBinFile(0, fileIndex, 0, getMethodAddress(Scus94491BpeSegment.class, "musicPackageLoadedCallback", long.class, long.class, long.class), fileIndex * 0x100 | a1, 4);
  }

  @Method(0x8001f450L)
  public static long scriptLoadMusicPackage(final RunningScript a0) {
    unloadSoundFile(8);
//    loadedDrgnFiles_800bcf78.oru(0x80L); //TODO GH#3
    _800bd782.addu(0x1L);
//    final long fileIndex = 5815 + a0.params_20.get(0).deref().get() * 5;
//    loadDrgnBinFile(0, fileIndex, 0, getMethodAddress(Scus94491BpeSegment.class, "musicPackageLoadedCallback", long.class, long.class, long.class), fileIndex << 8 | a0.params_20.get(1).deref().get(), 0x4L);
    return 0;
  }

  @Method(0x8001f560L)
  public static long FUN_8001f560(final RunningScript a0) {
    //TODO GH#3
    unloadSoundFile(8);
    _800bd782.addu(0x1L);
    if(true) return 0;

    unloadSoundFile(8);
    loadedDrgnFiles_800bcf78.oru(0x80L);
    final int fileIndex = 732 + a0.params_20.get(0).deref().get() * 5;
    loadDrgnBinFile(0, fileIndex, 0, getMethodAddress(Scus94491BpeSegment.class, "musicPackageLoadedCallback", long.class, long.class, long.class), fileIndex << 8 | a0.params_20.get(1).deref().get(), 0x4L);
    return 0;
  }

  @Method(0x8001f674L)
  public static long FUN_8001f674(final RunningScript a0) {
    //TODO GH#3
    unloadSoundFile(8);
    _800bd782.addu(0x1L);
    if(true) return 0;

    unloadSoundFile(8);
    loadedDrgnFiles_800bcf78.oru(0x80L);
    final int fileIndex = 2353 + a0.params_20.get(0).deref().get() * 6;
    loadDrgnBinFile(0, fileIndex, 0, getMethodAddress(Scus94491BpeSegment.class, "musicPackageLoadedCallback", long.class, long.class, long.class), fileIndex << 8 | a0.params_20.get(1).deref().get(), 0x4L);
    return 0;
  }

  @Method(0x8001f708L)
  public static void FUN_8001f708(final long chapterIndex, final long a1) {
    unloadSoundFile(8);
//    loadedDrgnFiles_800bcf78.oru(0x80L); TODO GH#3
    _800bd782.addu(0x1L);
//    final long fileIndex = 5850 + chapterIndex * 5;
//    loadDrgnBinFile(0, fileIndex, 0, getMethodAddress(Scus94491BpeSegment.class, "musicPackageLoadedCallback", long.class, long.class, long.class), fileIndex << 8 | a1, 0x4L);
  }

  @Method(0x8001f810L)
  public static void FUN_8001f810() {
    free(soundMrgPtr_800bd76c.getPointer());

    long s0 = soundFileArr_800bcf80.get(11)._18.get() - 0x1L;
    int s1 = 1;

    //LAB_8001f860
    while((int)s0 >= 0) {
      final long a3;
      if(s0 != 0) {
        a3 = getMethodAddress(Scus94491BpeSegment.class, "FUN_8001f8e0", long.class, long.class, long.class);
      } else {
        //LAB_8001f87c
        a3 = getMethodAddress(Scus94491BpeSegment.class, "FUN_8001f968", long.class, long.class, long.class);
      }

      //LAB_8001f88c
      loadDrgnBinFile(0, (int)((_800bd0fc.get() >> 8) + s1), 0, a3, 0, 0x4L);
      s0--;
      s1++;
    }

    //LAB_8001f8b4
    _800bd782.addu(0x1L);
  }

  @Method(0x8001f8e0L)
  public static void FUN_8001f8e0(final long audioAddress, final long spuTransferSize, final long unused) {
    setSpuDmaCompleteCallback(0);

    SPU.directWrite(0x2_1f70 + (int)soundFileArr_800bcf80.get(11).spuRamOffset_14.get(), audioAddress, (int)spuTransferSize);

    soundFileArr_800bcf80.get(11).spuRamOffset_14.add(spuTransferSize);
    free(audioAddress);
  }

  @Method(0x8001f968L)
  public static void FUN_8001f968(final long audioAddress, final long spuTransferSize, final long unused) {
    setSpuDmaCompleteCallback(0);

    //TODO make sure directWrite works
    SPU.directWrite(0x2_1f70 + (int)soundFileArr_800bcf80.get(11).spuRamOffset_14.get(), audioAddress, (int)spuTransferSize);

    if(_800bd0fc.get(0x1L) == 0) {
      FUN_8004cf8c((int)sssqChannelIndex_800bd0f8.get());
    }

    free(audioAddress);
    loadedDrgnFiles_800bcf78.and(0xffff_ff7fL);
  }

  @Method(0x8001fa18L)
  public static void FUN_8001fa18() {
    FUN_8004cf8c((int)sssqChannelIndex_800bd0f8.get());
    sssqTempo_800bd104.setu(sssqGetTempo((int)sssqChannelIndex_800bd0f8.get()) * sssqTempoScale_800bd100.get());
    sssqSetTempo((int)sssqChannelIndex_800bd0f8.get(), (int)(sssqTempo_800bd104.get() << 8) >> 16);
    free(soundMrgPtr_800bd76c.getPointer());

    _800bd782.addu(0x1L);
    loadedDrgnFiles_800bcf78.and(0xffff_ff7fL);
  }

  @Method(0x8001fb44L)
  public static void FUN_8001fb44(final long address, final long fileSize, final long param) {
    long v0;
    long v1;
    final long a0;
    final long s0;
    final long s2;
    soundMrgPtr_800bd76c.setPointer(address);
    v0 = mallocTail(MEMORY.ref(4, address).offset(0x20L).get());
    s0 = soundFileArr_800bcf80.getAddress();
    MEMORY.ref(4, s0).offset(0x138L).setu(v0);
    memcpy(v0, address, (int)MEMORY.ref(4, address).offset(0x20L).get());

    s2 = MEMORY.ref(4, s0).offset(0x138L).get();
    v0 = s2 + MEMORY.ref(4, s2).offset(0x8L).get();
    v0 = MEMORY.ref(2, v0).offset(0x0L).get();
    MEMORY.ref(2, s0).offset(0x136L).setu(v0);
    if(param == 0) {
      a0 = getMethodAddress(Scus94491BpeSegment.class, "FUN_8001ddd8");
    } else {
      //LAB_8001fbc4
      a0 = getMethodAddress(Scus94491BpeSegment.class, "FUN_8001fc54");
    }

    //LAB_8001fbcc
    setSpuDmaCompleteCallback(a0);
    v0 = loadSshdAndSoundbank(address + MEMORY.ref(4, address).offset(0x20L).get(), MEMORY.ref(4, s2 + MEMORY.ref(4, s2).offset(0x18L).get(), SshdFile::new), 0x2_1f70L);
    v1 = soundFileArr_800bcf80.getAddress();
    MEMORY.ref(2, v1).offset(0x144L).setu(v0);
    MEMORY.ref(2, v1).offset(0x134L).setu(0x1L);
    v0 = FUN_8004c1f8((short)v0, MEMORY.ref(4, s2 + MEMORY.ref(4, s2).offset(0x10L).get(), SssqFile::new));
    v1 = _800bd0f0.getAddress();
    MEMORY.ref(2, v1).offset(0x8L).setu(v0);
    FUN_8001b1a8(0x28L);
    _800bd781.setu(0);
    _800bd0f0.setu(0x2L);
  }

  @Method(0x8001fcf4L)
  public static void FUN_8001fcf4(final int fileIndex) {
    loadedDrgnFiles_800bcf78.oru(0x4000L);
    loadDrgnBinFile(0, fileIndex, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001fd4c", long.class, long.class, long.class), 0x6L, 0x2L);
  }

  @Method(0x8001fd4cL)
  public static void FUN_8001fd4c(final long address, final long fileSize, final long param) {
    final long s0 = _800bd610.offset(param * 0x10L).getAddress();
    MEMORY.ref(4, s0).offset(0x4L).setu(address);
    MEMORY.ref(2, s0).offset(0x0L).setu(0x2L);
    MEMORY.ref(4, s0).offset(0x8L).setu(address + MEMORY.ref(4, address).offset(0x18L).get());
    FUN_8004c8dc((int)MEMORY.ref(2, s0).offset(0xcL).getSigned(), 0x28L);
    MEMORY.ref(2, s0).offset(0x0L).setu(0x2L);
    loadedDrgnFiles_800bcf78.and(0xffff_bfffL);
  }

  @Method(0x8001ff74L)
  public static void FUN_8001ff74() {
    loadSupportOverlay(1, getConsumerAddress(Scus94491BpeSegment.class, "FUN_8001de84", int.class), 0);
  }

  @Method(0x8001ffb0L)
  public static long getLoadedDrgnFiles() {
    return loadedDrgnFiles_800bcf78.get();
  }

  @Method(0x8001ffc0L)
  public static long FUN_8001ffc0(final RunningScript a0) {
    a0.params_20.get(0).deref().set((int)loadedDrgnFiles_800bcf78.get());
    return 0;
  }

  @Method(0x8001ffdcL)
  public static long FUN_8001ffdc(final RunningScript a0) {
    unloadSoundFile(a0.params_20.get(0).deref().get());
    return 0;
  }
}
