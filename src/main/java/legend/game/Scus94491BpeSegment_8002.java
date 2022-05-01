package legend.game;

import legend.core.DebugHelper;
import legend.core.InterruptType;
import legend.core.MathHelper;
import legend.core.cdrom.CdlFILE;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.GsOBJTABLE2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable;
import legend.core.gte.VECTOR;
import legend.core.kernel.Kernel;
import legend.core.kernel.PriorityChainEntry;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiConsumerRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.DIRENTRY;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.types.BigStruct;
import legend.game.types.DR_MOVE;
import legend.game.types.Drgn0_6666Entry;
import legend.game.types.Drgn0_6666Struct;
import legend.game.types.LodString;
import legend.game.types.Renderable58;
import legend.game.types.ExtendedTmd;
import legend.game.types.JoyStruct;
import legend.game.types.MemcardStruct28;
import legend.game.types.MrgEntry;
import legend.game.types.MrgFile;
import legend.game.types.RenderableMetrics14;
import legend.game.types.RotateTranslateStruct;
import legend.game.types.RunningScript;
import legend.game.types.SpuStruct28;
import legend.game.types.Struct4c;
import legend.game.types.Struct84;
import legend.game.types.TmdAnimationFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.GATE;
import static legend.core.Hardware.MEMORY;
import static legend.core.InterruptController.I_MASK;
import static legend.core.InterruptController.I_STAT;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.core.Timers.TMR_DOTCLOCK_VAL;
import static legend.core.input.MemoryCard.JOY_MCD_CTRL;
import static legend.core.input.MemoryCard.JOY_MCD_DATA;
import static legend.core.input.MemoryCard.JOY_MCD_STAT;
import static legend.core.kernel.Bios.EnterCriticalSection;
import static legend.core.kernel.Bios.ExitCriticalSection;
import static legend.core.kernel.Kernel.EvMdINTR;
import static legend.core.kernel.Kernel.EvSpERROR;
import static legend.core.kernel.Kernel.EvSpIOE;
import static legend.core.kernel.Kernel.EvSpNEW;
import static legend.core.kernel.Kernel.EvSpTIMOUT;
import static legend.core.kernel.Kernel.HwCARD;
import static legend.core.kernel.Kernel.SwCARD;
import static legend.game.SInit.FUN_800fbec8;
import static legend.game.SItem.FUN_800fcad4;
import static legend.game.SItem.FUN_8010a948;
import static legend.game.SItem.FUN_8010d614;
import static legend.game.SItem.FUN_8010f198;
import static legend.game.SItem._80111d20;
import static legend.game.SItem._80111ff0;
import static legend.game.SMap.FUN_800d9e64;
import static legend.game.SMap.FUN_800da114;
import static legend.game.SMap.FUN_800daa3c;
import static legend.game.SMap.FUN_800de004;
import static legend.game.SMap.FUN_800e2220;
import static legend.game.SMap.FUN_800e2428;
import static legend.game.SMap.FUN_800e4018;
import static legend.game.SMap.FUN_800e4708;
import static legend.game.SMap.FUN_800e4e5c;
import static legend.game.SMap.FUN_800e4f8c;
import static legend.game.SMap.FUN_800e519c;
import static legend.game.SMap.FUN_800e5534;
import static legend.game.SMap.FUN_800e6730;
import static legend.game.SMap.FUN_800e828c;
import static legend.game.SMap.FUN_800e8e50;
import static legend.game.SMap.FUN_800ea4c8;
import static legend.game.SMap._800f7e54;
import static legend.game.SMap.handleEncounters;
import static legend.game.SMap.renderDobj2;
import static legend.game.Scus94491BpeSegment.BASCUS_94491drgn00_80010734;
import static legend.game.Scus94491BpeSegment.FUN_80012b1c;
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.FUN_8001ad18;
import static legend.game.Scus94491BpeSegment.FUN_8001e010;
import static legend.game.Scus94491BpeSegment._80010868;
import static legend.game.Scus94491BpeSegment._800108b0;
import static legend.game.Scus94491BpeSegment._80011174;
import static legend.game.Scus94491BpeSegment.addToLinkedListHead;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.functionVectorA_000000a0;
import static legend.game.Scus94491BpeSegment.functionVectorB_000000b0;
import static legend.game.Scus94491BpeSegment.functionVectorC_000000c0;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.insertElementIntoLinkedList;
import static legend.game.Scus94491BpeSegment.linkedListAddress_1f8003d8;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rectArray28_80010770;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment.unloadSoundFile;
import static legend.game.Scus94491BpeSegment_8003.CdMix;
import static legend.game.Scus94491BpeSegment_8003.DrawSync;
import static legend.game.Scus94491BpeSegment_8003.DsSearchFile;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b570;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b590;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b5b0;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003fd80;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrix;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrixL;
import static legend.game.Scus94491BpeSegment_8003.SetDrawMove;
import static legend.game.Scus94491BpeSegment_8003.SetVsyncInterruptCallback;
import static legend.game.Scus94491BpeSegment_8003.TransMatrix;
import static legend.game.Scus94491BpeSegment_8003.TransposeMatrix;
import static legend.game.Scus94491BpeSegment_8003.VSync;
import static legend.game.Scus94491BpeSegment_8003.adjustTmdPointers;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.updateTmdPacketIlen;
import static legend.game.Scus94491BpeSegment_8004.FUN_80042b60;
import static legend.game.Scus94491BpeSegment_8004.FUN_80042ba0;
import static legend.game.Scus94491BpeSegment_8004.FUN_80042d10;
import static legend.game.Scus94491BpeSegment_8004.FUN_80042e70;
import static legend.game.Scus94491BpeSegment_8004.FUN_80042f40;
import static legend.game.Scus94491BpeSegment_8004.FUN_80043040;
import static legend.game.Scus94491BpeSegment_8004.FUN_80043120;
import static legend.game.Scus94491BpeSegment_8004.FUN_80043230;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c390;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d034;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixX;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixY;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixZ;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040010;
import static legend.game.Scus94491BpeSegment_8004._8004f2ac;
import static legend.game.Scus94491BpeSegment_8004.callbackIndex_8004ddc4;
import static legend.game.Scus94491BpeSegment_8004.loadingSmapOvl_8004dd08;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_8004.setCdVolume;
import static legend.game.Scus94491BpeSegment_8005._800503b0;
import static legend.game.Scus94491BpeSegment_8005._800503d4;
import static legend.game.Scus94491BpeSegment_8005._800503f8;
import static legend.game.Scus94491BpeSegment_8005._80050424;
import static legend.game.Scus94491BpeSegment_8005._80052ae0;
import static legend.game.Scus94491BpeSegment_8005._80052b40;
import static legend.game.Scus94491BpeSegment_8005._80052b68;
import static legend.game.Scus94491BpeSegment_8005._80052b88;
import static legend.game.Scus94491BpeSegment_8005._80052ba8;
import static legend.game.Scus94491BpeSegment_8005._80052bc8;
import static legend.game.Scus94491BpeSegment_8005._80052bf4;
import static legend.game.Scus94491BpeSegment_8005._80052c34;
import static legend.game.Scus94491BpeSegment_8005._80052c3c;
import static legend.game.Scus94491BpeSegment_8005._80052c40;
import static legend.game.Scus94491BpeSegment_8005._80052c44;
import static legend.game.Scus94491BpeSegment_8005._80052c4c;
import static legend.game.Scus94491BpeSegment_8005._80052c5c;
import static legend.game.Scus94491BpeSegment_8005._80052c64;
import static legend.game.Scus94491BpeSegment_8005._80052dbc;
import static legend.game.Scus94491BpeSegment_8005._80052dc0;
import static legend.game.Scus94491BpeSegment_8005._80052e1c;
import static legend.game.Scus94491BpeSegment_8005._80052e2c;
import static legend.game.Scus94491BpeSegment_8005._80052e30;
import static legend.game.Scus94491BpeSegment_8005._80052e34;
import static legend.game.Scus94491BpeSegment_8005._80052e3c;
import static legend.game.Scus94491BpeSegment_8005._8005a1d8;
import static legend.game.Scus94491BpeSegment_8005.index_80052c38;
import static legend.game.Scus94491BpeSegment_8005.maxJoypadIndex_80059628;
import static legend.game.Scus94491BpeSegment_8005.memcardEventIndex_80052e4c;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8007.joypadInput_8007a39c;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.joypadRepeat_8007a3a0;
import static legend.game.Scus94491BpeSegment_800b.HwCARD_EvSpERROR_EventId_800bf264;
import static legend.game.Scus94491BpeSegment_800b.HwCARD_EvSpIOE_EventId_800bf260;
import static legend.game.Scus94491BpeSegment_800b.HwCARD_EvSpNEW_EventId_800bf26c;
import static legend.game.Scus94491BpeSegment_800b.HwCARD_EvSpTIMOUT_EventId_800bf268;
import static legend.game.Scus94491BpeSegment_800b.SwCARD_EvSpERROR_EventId_800bf254;
import static legend.game.Scus94491BpeSegment_800b.SwCARD_EvSpIOE_EventId_800bf250;
import static legend.game.Scus94491BpeSegment_800b.SwCARD_EvSpNEW_EventId_800bf25c;
import static legend.game.Scus94491BpeSegment_800b.SwCARD_EvSpTIMOUT_EventId_800bf258;
import static legend.game.Scus94491BpeSegment_800b._800babc8;
import static legend.game.Scus94491BpeSegment_800b._800bad64;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bb110;
import static legend.game.Scus94491BpeSegment_800b._800bb112;
import static legend.game.Scus94491BpeSegment_800b._800bc0b9;
import static legend.game.Scus94491BpeSegment_800b._800bd610;
import static legend.game.Scus94491BpeSegment_800b._800bd614;
import static legend.game.Scus94491BpeSegment_800b._800bd61c;
import static legend.game.Scus94491BpeSegment_800b._800bd7a0;
import static legend.game.Scus94491BpeSegment_800b._800bd7a4;
import static legend.game.Scus94491BpeSegment_800b._800bd7a8;
import static legend.game.Scus94491BpeSegment_800b._800bd7ac;
import static legend.game.Scus94491BpeSegment_800b._800bd7b0;
import static legend.game.Scus94491BpeSegment_800b._800bd7b4;
import static legend.game.Scus94491BpeSegment_800b._800bd7b8;
import static legend.game.Scus94491BpeSegment_800b._800bd80c;
import static legend.game.Scus94491BpeSegment_800b._800bdb88;
import static legend.game.Scus94491BpeSegment_800b._800bdb9c;
import static legend.game.Scus94491BpeSegment_800b._800bdba0;
import static legend.game.Scus94491BpeSegment_800b._800bdbf0;
import static legend.game.Scus94491BpeSegment_800b._800bdc58;
import static legend.game.Scus94491BpeSegment_800b.currentText_800bdca0;
import static legend.game.Scus94491BpeSegment_800b._800bdea0;
import static legend.game.Scus94491BpeSegment_800b._800bdf00;
import static legend.game.Scus94491BpeSegment_800b._800bdf04;
import static legend.game.Scus94491BpeSegment_800b._800bdf08;
import static legend.game.Scus94491BpeSegment_800b._800bdf18;
import static legend.game.Scus94491BpeSegment_800b._800bdf38;
import static legend.game.Scus94491BpeSegment_800b._800be358;
import static legend.game.Scus94491BpeSegment_800b._800be5b8;
import static legend.game.Scus94491BpeSegment_800b._800be5bc;
import static legend.game.Scus94491BpeSegment_800b._800be5c0;
import static legend.game.Scus94491BpeSegment_800b._800be5c4;
import static legend.game.Scus94491BpeSegment_800b._800be5c8;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b._800be5d8;
import static legend.game.Scus94491BpeSegment_800b._800be5d9;
import static legend.game.Scus94491BpeSegment_800b._800be5da;
import static legend.game.Scus94491BpeSegment_800b._800be5db;
import static legend.game.Scus94491BpeSegment_800b._800be5dc;
import static legend.game.Scus94491BpeSegment_800b._800be5dd;
import static legend.game.Scus94491BpeSegment_800b._800be5de;
import static legend.game.Scus94491BpeSegment_800b._800be5df;
import static legend.game.Scus94491BpeSegment_800b._800be5e0;
import static legend.game.Scus94491BpeSegment_800b._800be5e1;
import static legend.game.Scus94491BpeSegment_800b._800be5e2;
import static legend.game.Scus94491BpeSegment_800b._800be5e3;
import static legend.game.Scus94491BpeSegment_800b._800be5e4;
import static legend.game.Scus94491BpeSegment_800b._800be5e5;
import static legend.game.Scus94491BpeSegment_800b._800be5e6;
import static legend.game.Scus94491BpeSegment_800b._800be5e7;
import static legend.game.Scus94491BpeSegment_800b._800be5e8;
import static legend.game.Scus94491BpeSegment_800b._800be5e9;
import static legend.game.Scus94491BpeSegment_800b._800be5ea;
import static legend.game.Scus94491BpeSegment_800b._800be5eb;
import static legend.game.Scus94491BpeSegment_800b._800be5ec;
import static legend.game.Scus94491BpeSegment_800b._800be5ed;
import static legend.game.Scus94491BpeSegment_800b._800be5ee;
import static legend.game.Scus94491BpeSegment_800b._800be5ef;
import static legend.game.Scus94491BpeSegment_800b._800be5f0;
import static legend.game.Scus94491BpeSegment_800b._800be5f1;
import static legend.game.Scus94491BpeSegment_800b._800be5f2;
import static legend.game.Scus94491BpeSegment_800b._800be5f3;
import static legend.game.Scus94491BpeSegment_800b._800be5f8;
import static legend.game.Scus94491BpeSegment_800b._800be5fc;
import static legend.game.Scus94491BpeSegment_800b._800be5fe;
import static legend.game.Scus94491BpeSegment_800b._800be600;
import static legend.game.Scus94491BpeSegment_800b._800be602;
import static legend.game.Scus94491BpeSegment_800b._800be604;
import static legend.game.Scus94491BpeSegment_800b._800be606;
import static legend.game.Scus94491BpeSegment_800b._800be607;
import static legend.game.Scus94491BpeSegment_800b._800be628;
import static legend.game.Scus94491BpeSegment_800b._800be62d;
import static legend.game.Scus94491BpeSegment_800b._800be62e;
import static legend.game.Scus94491BpeSegment_800b._800be636;
import static legend.game.Scus94491BpeSegment_800b._800be63e;
import static legend.game.Scus94491BpeSegment_800b._800be640;
import static legend.game.Scus94491BpeSegment_800b._800be642;
import static legend.game.Scus94491BpeSegment_800b._800be644;
import static legend.game.Scus94491BpeSegment_800b._800be646;
import static legend.game.Scus94491BpeSegment_800b._800be648;
import static legend.game.Scus94491BpeSegment_800b._800be64a;
import static legend.game.Scus94491BpeSegment_800b._800be64c;
import static legend.game.Scus94491BpeSegment_800b._800be64e;
import static legend.game.Scus94491BpeSegment_800b._800be650;
import static legend.game.Scus94491BpeSegment_800b._800be652;
import static legend.game.Scus94491BpeSegment_800b._800be654;
import static legend.game.Scus94491BpeSegment_800b._800be656;
import static legend.game.Scus94491BpeSegment_800b._800be658;
import static legend.game.Scus94491BpeSegment_800b._800be65a;
import static legend.game.Scus94491BpeSegment_800b._800be65c;
import static legend.game.Scus94491BpeSegment_800b._800be65e;
import static legend.game.Scus94491BpeSegment_800b._800be660;
import static legend.game.Scus94491BpeSegment_800b._800be661;
import static legend.game.Scus94491BpeSegment_800b._800be662;
import static legend.game.Scus94491BpeSegment_800b._800be663;
import static legend.game.Scus94491BpeSegment_800b._800be664;
import static legend.game.Scus94491BpeSegment_800b._800be665;
import static legend.game.Scus94491BpeSegment_800b._800be666;
import static legend.game.Scus94491BpeSegment_800b._800be668;
import static legend.game.Scus94491BpeSegment_800b._800be669;
import static legend.game.Scus94491BpeSegment_800b._800be66a;
import static legend.game.Scus94491BpeSegment_800b._800be66b;
import static legend.game.Scus94491BpeSegment_800b._800be66c;
import static legend.game.Scus94491BpeSegment_800b._800be66e;
import static legend.game.Scus94491BpeSegment_800b._800be66f;
import static legend.game.Scus94491BpeSegment_800b._800be670;
import static legend.game.Scus94491BpeSegment_800b._800be671;
import static legend.game.Scus94491BpeSegment_800b._800be672;
import static legend.game.Scus94491BpeSegment_800b._800be673;
import static legend.game.Scus94491BpeSegment_800b._800be674;
import static legend.game.Scus94491BpeSegment_800b._800be675;
import static legend.game.Scus94491BpeSegment_800b._800be676;
import static legend.game.Scus94491BpeSegment_800b._800be677;
import static legend.game.Scus94491BpeSegment_800b._800be678;
import static legend.game.Scus94491BpeSegment_800b._800be679;
import static legend.game.Scus94491BpeSegment_800b._800be67a;
import static legend.game.Scus94491BpeSegment_800b._800be67b;
import static legend.game.Scus94491BpeSegment_800b._800be67c;
import static legend.game.Scus94491BpeSegment_800b._800be67e;
import static legend.game.Scus94491BpeSegment_800b._800be680;
import static legend.game.Scus94491BpeSegment_800b._800be682;
import static legend.game.Scus94491BpeSegment_800b._800be684;
import static legend.game.Scus94491BpeSegment_800b._800be686;
import static legend.game.Scus94491BpeSegment_800b._800be688;
import static legend.game.Scus94491BpeSegment_800b._800be68a;
import static legend.game.Scus94491BpeSegment_800b._800be68c;
import static legend.game.Scus94491BpeSegment_800b._800be68e;
import static legend.game.Scus94491BpeSegment_800b._800be690;
import static legend.game.Scus94491BpeSegment_800b._800be691;
import static legend.game.Scus94491BpeSegment_800b._800be692;
import static legend.game.Scus94491BpeSegment_800b._800be693;
import static legend.game.Scus94491BpeSegment_800b._800be694;
import static legend.game.Scus94491BpeSegment_800b._800be696;
import static legend.game.Scus94491BpeSegment_800b._800be697;
import static legend.game.Scus94491BpeSegment_800b._800bed60;
import static legend.game.Scus94491BpeSegment_800b._800bee80;
import static legend.game.Scus94491BpeSegment_800b._800bee88;
import static legend.game.Scus94491BpeSegment_800b._800bee8c;
import static legend.game.Scus94491BpeSegment_800b._800bee90;
import static legend.game.Scus94491BpeSegment_800b._800bee94;
import static legend.game.Scus94491BpeSegment_800b._800bee98;
import static legend.game.Scus94491BpeSegment_800b._800bee9c;
import static legend.game.Scus94491BpeSegment_800b._800beea4;
import static legend.game.Scus94491BpeSegment_800b._800beeac;
import static legend.game.Scus94491BpeSegment_800b._800beeb4;
import static legend.game.Scus94491BpeSegment_800b._800beebc;
import static legend.game.Scus94491BpeSegment_800b._800beec4;
import static legend.game.Scus94491BpeSegment_800b._800bef44;
import static legend.game.Scus94491BpeSegment_800b._800befc4;
import static legend.game.Scus94491BpeSegment_800b._800bf044;
import static legend.game.Scus94491BpeSegment_800b._800bf064;
import static legend.game.Scus94491BpeSegment_800b._800bf068;
import static legend.game.Scus94491BpeSegment_800b._800bf0a8;
import static legend.game.Scus94491BpeSegment_800b._800bf0ac;
import static legend.game.Scus94491BpeSegment_800b._800bf0c0;
import static legend.game.Scus94491BpeSegment_800b._800bf0c4;
import static legend.game.Scus94491BpeSegment_800b._800bf0c8;
import static legend.game.Scus94491BpeSegment_800b._800bf0cc;
import static legend.game.Scus94491BpeSegment_800b._800bf0cd;
import static legend.game.Scus94491BpeSegment_800b._800bf0ce;
import static legend.game.Scus94491BpeSegment_800b._800bf0d8;
import static legend.game.Scus94491BpeSegment_800b._800bf140;
import static legend.game.Scus94491BpeSegment_800b._800bf148;
import static legend.game.Scus94491BpeSegment_800b._800bf14c;
import static legend.game.Scus94491BpeSegment_800b._800bf150;
import static legend.game.Scus94491BpeSegment_800b._800bf154;
import static legend.game.Scus94491BpeSegment_800b._800bf158;
import static legend.game.Scus94491BpeSegment_800b._800bf178;
import static legend.game.Scus94491BpeSegment_800b._800bf17c;
import static legend.game.Scus94491BpeSegment_800b._800bf1b4;
import static legend.game.Scus94491BpeSegment_800b._800bf1b8;
import static legend.game.Scus94491BpeSegment_800b._800bf1bc;
import static legend.game.Scus94491BpeSegment_800b._800bf1c0;
import static legend.game.Scus94491BpeSegment_800b._800bf1c4;
import static legend.game.Scus94491BpeSegment_800b._800bf1c8;
import static legend.game.Scus94491BpeSegment_800b._800bf1d8;
import static legend.game.Scus94491BpeSegment_800b._800bf200;
import static legend.game.Scus94491BpeSegment_800b.activeMemcardEvent_800bf170;
import static legend.game.Scus94491BpeSegment_800b.cardDoneRead_800bf270;
import static legend.game.Scus94491BpeSegment_800b.cardError2000_800bf28c;
import static legend.game.Scus94491BpeSegment_800b.cardError8000_800bf284;
import static legend.game.Scus94491BpeSegment_800b.cardErrorBusyLow_800bf288;
import static legend.game.Scus94491BpeSegment_800b.cardErrorBusy_800bf278;
import static legend.game.Scus94491BpeSegment_800b.cardErrorEject_800bf27c;
import static legend.game.Scus94491BpeSegment_800b.cardErrorWrite_800bf274;
import static legend.game.Scus94491BpeSegment_800b.cardFinishedOkay_800bf280;
import static legend.game.Scus94491BpeSegment_800b.cardPort_800bf180;
import static legend.game.Scus94491BpeSegment_800b.deviceCallback_800bf1d0;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba4;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba8;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc5c;
import static legend.game.Scus94491BpeSegment_800b.selectedMenuOptionRenderablePtr_800bdbe0;
import static legend.game.Scus94491BpeSegment_800b.selectedMenuOptionRenderablePtr_800bdbe4;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdbe8;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdbec;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc20;
import static legend.game.Scus94491BpeSegment_800b.inventoryMenuState_800bdc28;
import static legend.game.Scus94491BpeSegment_800b.loadedDrgnFiles_800bcf78;
import static legend.game.Scus94491BpeSegment_800b.memcardDest_800bf190;
import static legend.game.Scus94491BpeSegment_800b.memcardFileHandle_800bf184;
import static legend.game.Scus94491BpeSegment_800b.memcardLength_800bf18c;
import static legend.game.Scus94491BpeSegment_800b.memcardPos_800bf188;
import static legend.game.Scus94491BpeSegment_800b.memcardState_800bf174;
import static legend.game.Scus94491BpeSegment_800b.memcardStatus_800bf144;
import static legend.game.Scus94491BpeSegment_800b.memcardStruct28ArrPtr_800bdc50;
import static legend.game.Scus94491BpeSegment_800b.memcardVsyncCallbacks_800bf240;
import static legend.game.Scus94491BpeSegment_800b.mono_800bb0a8;
import static legend.game.Scus94491BpeSegment_800b.previousMemcardEvent_800bf160;
import static legend.game.Scus94491BpeSegment_800b.previousMemcardState_800bf164;
import static legend.game.Scus94491BpeSegment_800b.vibrationEnabled_800bb0a9;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c._800c6688;
import static legend.game.Scus94491BpeSegment_800e.main;
import static legend.game.Scus94491BpeSegment_800e.ramSize_800e6f04;
import static legend.game.Scus94491BpeSegment_800e.stackSize_800e6f08;
import static legend.game.WMap.FUN_800c8844;
import static legend.game.WMap.FUN_800c925c;
import static legend.game.WMap.renderWmapDobj2;

public final class Scus94491BpeSegment_8002 {
  private Scus94491BpeSegment_8002() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8002.class);

  private static final Object[] EMPTY_OBJ_ARRAY = {};

  @Method(0x80020008L)
  public static void sssqResetStuff() {
    FUN_8001ad18();
    unloadSoundFile(1);
    unloadSoundFile(3);
    unloadSoundFile(4);
    unloadSoundFile(5);
    unloadSoundFile(6);
    unloadSoundFile(7);
    FUN_800201c8(0x6L);
  }

  @Method(0x800201c8L)
  public static void FUN_800201c8(final long a0) {
    if(_800bd610.offset(a0 * 16).get() != 0) {
      FUN_8004d034((int)_800bd61c.offset(a0 * 16).get(), 0x1L);
      FUN_8004c390((int)_800bd61c.offset(a0 * 16).get());
      removeFromLinkedList(_800bd614.offset(a0 * 16).get());
      _800bd610.offset(a0 * 16).setu(0);
    }

    //LAB_80020220
  }

  @Method(0x80020360L)
  public static void FUN_80020360(final ArrayRef<SpuStruct28> a0, final ArrayRef<SpuStruct28> a1) {
    //LAB_8002036c
    for(int i = 0; i < 32; i++) {
      final SpuStruct28 a0_0 = a0.get(i);
      final SpuStruct28 a1_0 = a1.get(i);

      //LAB_80020378
      memcpy(a1_0.getAddress(), a0_0.getAddress(), 0x28);

      if(a1_0._00.get() == 4) {
        if(a1_0._1c.get() != 0) {
          a1_0._00.set(3);
        }
      }

      //LAB_800203d8
    }
  }

  @Method(0x80020460L)
  public static void FUN_80020460() {
    // empty
  }

  @Method(0x80020468L)
  public static void FUN_80020468(GsDOBJ2 dobj2, long a1) {
    assert false;
  }

  /** Very similar to {@link Scus94491BpeSegment_800e#FUN_800e6b3c(BigStruct, ExtendedTmd, TmdAnimationFile)} */
  @Method(0x80020718L)
  public static void FUN_80020718(final BigStruct bigStruct, final ExtendedTmd extendedTmd, final TmdAnimationFile tmdAnimFile) {
    final int transferX = bigStruct.coord2_14.coord.transfer.getX();
    final int transferY = bigStruct.coord2_14.coord.transfer.getY();
    final int transferZ = bigStruct.coord2_14.coord.transfer.getZ();

    //LAB_80020760
    for(int i = 0; i < 7; i++) {
      bigStruct.aub_ec.get(i).set(0);
    }

    final Tmd tmd = extendedTmd.tmdPtr_00.deref().tmd;
    bigStruct.tmd_8c.set(tmd);
    bigStruct.tmdNobj_ca.set((int)tmd.header.nobj.get());

    if(mainCallbackIndex_8004dd20.get() == 0x5L) {
      FUN_800de004(bigStruct, extendedTmd);
    }

    //LAB_8002079c
    bigStruct.ui_108.set((extendedTmd.tmdPtr_00.deref().id.get() & 0xffff_0000L) >>> 11); //TODO reading the upper 16 bits of the TMD ID?

    final long v0 = extendedTmd.ptr_08.get();
    if(v0 == 0) {
      //LAB_80020818
      bigStruct.ptr_a8.set(extendedTmd.ptr_08.getAddress());

      //LAB_80020828
      for(int i = 0; i < 7; i++) {
        bigStruct.aui_d0.get(i).set(0);
      }
    } else {
      bigStruct.ptr_a8.set(extendedTmd.getAddress() + v0 / 4 * 4);

      //LAB_800207d4
      for(int i = 0; i < 7; i++) {
        //TODO make aui_d0 array of pointers to unsigned ints (also pointers but to what?)
        //TODO also ui_a8 is a pointer to a relative pointer?
        bigStruct.aui_d0.get(i).set(bigStruct.ptr_a8.get() + MEMORY.ref(4, bigStruct.ptr_a8.get()).offset(i * 0x4L).get() / 4 * 4);
        FUN_8002246c(bigStruct, i);
      }
    }

    //LAB_80020838
    adjustTmdPointers(bigStruct.tmd_8c.deref());
    FUN_80021b08(bigStruct.ObjTable_0c, bigStruct.dobj2ArrPtr_00.deref(), bigStruct.coord2ArrPtr_04.deref(), bigStruct.coord2ParamArrPtr_08.deref(), bigStruct.count_c8.get());
    bigStruct.coord2_14.param.set(bigStruct.coord2Param_64);
    GsInitCoordinate2(null, bigStruct.coord2_14);
    FUN_80021ca0(bigStruct.ObjTable_0c, bigStruct.tmd_8c.deref(), bigStruct.coord2_14, bigStruct.count_c8.get(), (short)(bigStruct.tmdNobj_ca.get() + 0x1L));

    bigStruct.us_a0.set(0);
    bigStruct.ub_a2.set(0);
    bigStruct.ub_a3.set(0);
    bigStruct.ui_f4.set(0);
    bigStruct.ui_f8.set(0);

    FUN_80021584(bigStruct, tmdAnimFile);

    bigStruct.coord2_14.coord.transfer.setX(transferX);
    bigStruct.coord2_14.coord.transfer.setY(transferY);
    bigStruct.coord2_14.coord.transfer.setZ(transferZ);

    int s1 = 0;
    if(mainCallbackIndex_8004dd20.get() == 0x5L || mainCallbackIndex_8004dd20.get() == 0x8L) {
      //LAB_80020940
      if(mainCallbackIndex_8004dd20.get() == 0x5L) {
        //LAB_80020958
        for(int i = 0; i < bigStruct.ObjTable_0c.nobj.get(); i++) {
          FUN_800d9e64(bigStruct.ObjTable_0c.top.deref().get(s1++), bigStruct.ub_9d.get());
        }
      }

      //LAB_80020978
      if(mainCallbackIndex_8004dd20.get() == 0x8L) {
        //LAB_80020990
        for(int i = 0; i < bigStruct.ObjTable_0c.nobj.get(); i++) {
          FUN_800c8844(bigStruct.ObjTable_0c.top.deref().get(s1++), bigStruct.ub_9d.get());
        }

        //LAB_800209ac
      }
    } else {
      //LAB_8002091c
      for(int i = 0; i < bigStruct.ObjTable_0c.nobj.get(); i++) {
        FUN_80020468(bigStruct.ObjTable_0c.top.deref().get(s1++), bigStruct.ub_9d.get());
      }
    }

    //LAB_800209b0
    bigStruct.ub_cc.set(0);
    bigStruct.ub_cd.set(0xfe);
    bigStruct.scaleVector_fc.setX(0x1000);
    bigStruct.scaleVector_fc.setY(0x1000);
    bigStruct.scaleVector_fc.setZ(0x1000);
    bigStruct.vector_10c.setX(0x1000);
    bigStruct.vector_10c.setY(0x1000);
    bigStruct.vector_10c.setZ(0x1000);
    bigStruct.vector_118.setX(0);
    bigStruct.vector_118.setY(0);
    bigStruct.vector_118.setZ(0);
  }

  @Method(0x80020a00L)
  public static void FUN_80020a00(final BigStruct bigStruct, final ExtendedTmd extendedTmd, final TmdAnimationFile tmdAnimFile) {
    bigStruct.count_c8.set((short)extendedTmd.tmdPtr_00.deref().tmd.header.nobj.get());

    final long address;
    if(mainCallbackIndex_8004dd20.get() != 0x6L || _800bd7a0.get() == 0) {
      //LAB_80020b00
      //LAB_80020b04
      address = addToLinkedListTail(bigStruct.count_c8.get() * 0x88L);
      bigStruct.dobj2ArrPtr_00.set(MEMORY.ref(4, address, UnboundedArrayRef.of(0x10, GsDOBJ2::new)));
    } else {
      removeFromLinkedList(_800bd7a0.get());

      address = addToLinkedListHead(bigStruct.count_c8.get() * 0x88L);
      bigStruct.dobj2ArrPtr_00.set(MEMORY.ref(4, address, UnboundedArrayRef.of(0x10, GsDOBJ2::new)));

      _800bd7a8.subu(0x1L);
      _800bd7a4.subu(bigStruct.count_c8.get() * 0x88L + _800bd7a8.get() * 0x100L);

      _800bd7a0.setu(addToLinkedListHead(_800bd7a4.get()));
    }

    //LAB_80020b40
    bigStruct.coord2ArrPtr_04.set(MEMORY.ref(4, address + bigStruct.count_c8.get() * 0x10L, UnboundedArrayRef.of(0x50, GsCOORDINATE2::new)));
    bigStruct.coord2ParamArrPtr_08.set(MEMORY.ref(4, address + bigStruct.count_c8.get() * 0x60L, UnboundedArrayRef.of(0x28, GsCOORD2PARAM::new)));
    FUN_80020718(bigStruct, extendedTmd, tmdAnimFile);
  }

  @Method(0x80020b98L)
  public static void FUN_80020b98(final BigStruct a0) {
    if(mainCallbackIndex_8004dd20.get() == 0x5L) {
      FUN_800da114(a0);
      return;
    }

    //LAB_80020be8
    //LAB_80020bf0
    for(int i = 0; i < 0x7L; i++) {
      if(a0.aub_ec.get(i).get() != 0) {
        FUN_80022018(a0, i);
      }

      //LAB_80020c08
    }

    if(a0.ub_9c.get() == 0x2L) {
      return;
    }

    if(a0.us_9e.get() == 0) {
      a0.ub_9c.set(0);
    }

    //LAB_80020c3c
    if(a0.ub_9c.get() == 0) {
      if(a0.ub_a2.get() == 0) {
        a0.us_9e.set(a0.us_9a);
      } else {
        //LAB_80020c68
        a0.us_9e.set((short)a0.us_9a.get() / 2);
      }

      //LAB_80020c7c
      a0.ub_9c.incr();
      a0.rotateTranslateArrPtr_94.set(a0.ptr_ui_90.deref());
    }

    //LAB_80020c90
    if((a0.us_9e.get() & 0x1L) == 0 && a0.ub_a2.get() == 0) {
      final UnboundedArrayRef<RotateTranslateStruct> sp10 = a0.rotateTranslateArrPtr_94.deref();

      if(a0.ub_a3.get() == 0) {
        final UnboundedArrayRef<RotateTranslateStruct> rotateTranslate = a0.rotateTranslateArrPtr_94.deref();

        //LAB_80020ce0
        for(int i = 0; i < a0.tmdNobj_ca.get() - 0x1L; i++) {
          final GsCOORDINATE2 coord2 = a0.dobj2ArrPtr_00.deref().get(i).coord2_04.deref();
          final GsCOORD2PARAM params = coord2.param.deref();
          RotMatrix_80040010(params.rotate, coord2.coord);

          params.trans.add(rotateTranslate.get(i).translate_06);
          params.trans.div(2);

          TransMatrix(coord2.coord, params.trans);
        }

        //LAB_80020d6c
        a0.rotateTranslateArrPtr_94.set(rotateTranslate.slice(a0.tmdNobj_ca.get()));
      } else {
        //LAB_80020d74
        final UnboundedArrayRef<RotateTranslateStruct> rotateTranslate = a0.rotateTranslateArrPtr_94.deref();

        //LAB_80020d8c
        for(int i = 0; i < a0.tmdNobj_ca.get() - 0x1L; i++) {
          final GsCOORDINATE2 coord2 = a0.dobj2ArrPtr_00.deref().get(i).coord2_04.deref();
          final GsCOORD2PARAM params = coord2.param.deref();

          params.rotate.set(rotateTranslate.get(i).rotate_00);
          RotMatrix_80040010(params.rotate, coord2.coord);

          params.trans.set(rotateTranslate.get(i).translate_06);
          TransMatrix(coord2.coord, params.trans);
        }

        //LAB_80020dfc
        a0.rotateTranslateArrPtr_94.set(rotateTranslate.slice(a0.tmdNobj_ca.get()));
      }

      //LAB_80020e00
      a0.rotateTranslateArrPtr_94.set(sp10);
    } else {
      //LAB_80020e0c
      final UnboundedArrayRef<RotateTranslateStruct> rotateTranslate = a0.rotateTranslateArrPtr_94.deref();

      //LAB_80020e24
      for(int i = 0; i < a0.tmdNobj_ca.get() - 0x1L; i++) {
        final GsCOORDINATE2 coord2 = a0.dobj2ArrPtr_00.deref().get(i).coord2_04.deref();
        final GsCOORD2PARAM params = coord2.param.deref();

        params.rotate.set(rotateTranslate.get(i).rotate_00);
        RotMatrix_80040010(params.rotate, coord2.coord);

        params.trans.set(rotateTranslate.get(i).translate_06);
        TransMatrix(coord2.coord, params.trans);
      }

      //LAB_80020e94
      a0.rotateTranslateArrPtr_94.set(rotateTranslate);
    }

    //LAB_80020e98
    a0.us_9e.decr();

    //LAB_80020ea8
  }

  @Method(0x80020ed8L)
  public static void FUN_80020ed8() {
    if(_800bdb88.get() == 0x5L) {
      if(loadingSmapOvl_8004dd08.get() == 0) {
        if(_800bd7b4.get() == 0x1L) {
          FUN_800e4708();
        }

        //LAB_80020f20
        FUN_8002aae8();
        FUN_800e4018();
      }
    }

    //LAB_80020f30
    //LAB_80020f34
    final long a0 = _800bdb88.get();
    _800bd7b4.setu(0);
    if(a0 != mainCallbackIndex_8004dd20.get()) {
      _800bd80c.setu(a0);
      _800bdb88.setu(mainCallbackIndex_8004dd20);

      if(mainCallbackIndex_8004dd20.get() == 0x5L) {
        _800bd7b0.setu(0x2L);
        _800bd7b8.setu(0);

        if(a0 == 0x2L) {
          _800bd7b0.setu(0x9L);
        }

        //LAB_80020f84
        if(a0 == 0x6L) {
          _800bd7b0.setu(-0x4L);
          _800bd7b8.setu(0x1L);
        }

        //LAB_80020fa4
        if(a0 == 0x8L) {
          _800bd7b0.setu(0x3L);
        }
      }
    }

    //LAB_80020fb4
    //LAB_80020fb8
    if(_800bdb88.get() == 0x2L) {
      _800bd7ac.setu(0x1L);
    }

    //LAB_80020fd0
  }

  @Method(0x80020fe0L)
  public static void FUN_80020fe0(final BigStruct a0) {
    if(!a0.dobj2ArrPtr_00.isNull()) {
      removeFromLinkedList(a0.dobj2ArrPtr_00.getPointer());
    }

    //LAB_80021008
    if(mainCallbackIndex_8004dd20.get() == 0x5L && !a0.smallerStructPtr_a4.isNull()) {
      removeFromLinkedList(a0.smallerStructPtr_a4.getPointer());
    }

    //LAB_80021034
    a0.dobj2ArrPtr_00.clear();
  }

  @Method(0x80021048L)
  public static void FUN_80021048(final long a0, final long a1) {
    // empty
  }

  @Method(0x80021050L)
  public static void FUN_80021050(final long a0, final long a1) {
    // empty
  }

  @Method(0x80021058L)
  public static void FUN_80021058(final long a0, final long a1) {
    // empty
  }

  @Method(0x80021060L)
  public static void FUN_80021060(final long a0, final long a1) {
    // empty
  }

  @Method(0x800211d8L)
  public static void FUN_800211d8(final BigStruct a0) {
    if(mainCallbackIndex_8004dd20.get() == 0x5L) {
      //LAB_80021230
      FUN_800daa3c(a0);
    } else if(mainCallbackIndex_8004dd20.get() == 0x6L) {
      //LAB_80021220
      NotYetLoaded.FUN_800ec974();
    } else if(mainCallbackIndex_8004dd20.get() == 0x8L) {
      //LAB_8002120c
      //LAB_80021240
      FUN_800c925c(a0);
    }

    //LAB_80021248
  }

  @Method(0x80021258L)
  public static void FUN_80021258(final GsDOBJ2 dobj2) {
    if(mainCallbackIndex_8004dd20.get() == 0x5L) {
      //LAB_800212b0
      renderDobj2(dobj2);
      return;
    }

    if(mainCallbackIndex_8004dd20.get() == 0x6L) {
      //LAB_800212a0
      NotYetLoaded.FUN_800ec0b0();
      return;
    }

    //LAB_8002128c
    if(mainCallbackIndex_8004dd20.get() == 0x8L) {
      //LAB_800212c0
      renderWmapDobj2(dobj2);
    }

    //LAB_800212c8
  }

  @Method(0x800212d8L)
  public static void FUN_800212d8(final BigStruct a0) {
    final long count = a0.tmdNobj_ca.get();

    if(count == 0) {
      return;
    }

    final UnboundedArrayRef<RotateTranslateStruct> rotateTranslate = a0.rotateTranslateArrPtr_94.deref();

    //LAB_80021320
    for(int i = 0; i < count; i++) {
      final GsDOBJ2 obj2 = a0.dobj2ArrPtr_00.deref().get(i);

      final GsCOORDINATE2 coord2 = obj2.coord2_04.deref();
      final GsCOORD2PARAM coord2param = coord2.param.deref();
      final MATRIX matrix = coord2.coord;

      coord2param.rotate.set(rotateTranslate.get(i).rotate_00);
      RotMatrix_80040010(coord2param.rotate, matrix);

      coord2param.trans.set(rotateTranslate.get(i).translate_06);
      TransMatrix(matrix, coord2param.trans);
    }

    //LAB_80021390
    a0.rotateTranslateArrPtr_94.set(rotateTranslate);
  }

  @Method(0x800214bcL)
  public static void FUN_800214bc(final BigStruct a0) {
    RotMatrix_8003faf0(a0.coord2Param_64.rotate, a0.coord2_14.coord);
    ScaleMatrix(a0.coord2_14.coord, a0.scaleVector_fc);
    a0.coord2_14.flg.set(0);
  }

  @Method(0x80021584L)
  public static void FUN_80021584(final BigStruct bigStruct, final TmdAnimationFile tmdAnimFile) {
    bigStruct.ptr_ui_90.set(tmdAnimFile.rotateTranslateArr_10);
    bigStruct.rotateTranslateArrPtr_94.set(tmdAnimFile.rotateTranslateArr_10);
    bigStruct.us_98.set(tmdAnimFile.count_0c);
    bigStruct.us_9a.set(tmdAnimFile._0e);
    bigStruct.ub_9c.set(0);

    FUN_800212d8(bigStruct);

    if(bigStruct.ub_a2.get() == 0) {
      bigStruct.us_9e.set(bigStruct.us_9a);
    } else {
      //LAB_800215e8
      bigStruct.us_9e.set(bigStruct.us_9a.get() / 2);
    }

    //LAB_80021608
    bigStruct.ub_9c.set(1);
    bigStruct.rotateTranslateArrPtr_94.set(bigStruct.ptr_ui_90.deref());
  }

  @Method(0x800217a4L)
  public static void FUN_800217a4(final BigStruct bigStruct) {
    if(bigStruct.coord2Param_64.rotate.pad.get() == -0x1L) {
      final MATRIX mat = new MATRIX();
      RotMatrix_8003fd80(bigStruct.coord2Param_64.rotate, mat);
      TransposeMatrix(mat, bigStruct.coord2_14.coord);
      bigStruct.coord2Param_64.rotate.x.set((short)0);
      bigStruct.coord2Param_64.rotate.y.set((short)0);
      bigStruct.coord2Param_64.rotate.z.set((short)0);
      bigStruct.coord2Param_64.rotate.pad.set((short)0);
    } else {
      bigStruct.coord2Param_64.rotate.y.set(FUN_800ea4c8(bigStruct.coord2Param_64.rotate.y.get()));
      RotMatrix_8003faf0(bigStruct.coord2Param_64.rotate, bigStruct.coord2_14.coord);
    }

    ScaleMatrix(bigStruct.coord2_14.coord, bigStruct.scaleVector_fc);
    bigStruct.coord2_14.flg.set(0);
  }

  @Method(0x800218f0L)
  public static void FUN_800218f0() {
    if(_800bd7ac.get() == 1) {
      _800bd7b0.setu(9);
      _800bd7ac.setu(0);
    }
  }

  @Method(0x80021918L)
  public static void FUN_80021918(final GsOBJTABLE2 table, final Tmd tmd, final GsCOORDINATE2 coord2, final long maxSize, final long a4) {
    final long s2 = (short)a4 >> 8 & 0xffL;
    final long dobj2Id = a4 & 0xffL;

    GsDOBJ2 dobj2 = getDObj2ById(table, dobj2Id);

    final Memory.TemporaryReservation temp;
    final MATRIX coord;
    final VECTOR scale;
    final SVECTOR rotation;
    final VECTOR translation;

    if(dobj2 == null) {
      temp = MEMORY.temp(0x10);
      dobj2 = new GsDOBJ2(temp.get()); //sp0x10;
      coord = new MATRIX(); //sp0x20;
      scale = new VECTOR();
      rotation = new SVECTOR(); //sp0x40;
      translation = new VECTOR();
    } else {
      temp = null;

      //LAB_80021984
      dobj2.coord2_04.deref().flg.set(0);

      scale = dobj2.coord2_04.deref().param.deref().scale;
      rotation = dobj2.coord2_04.deref().param.deref().rotate;
      translation = dobj2.coord2_04.deref().param.deref().trans;
      coord = dobj2.coord2_04.deref().coord;

      if(dobj2.coord2_04.deref().super_.isNull()) {
        dobj2.coord2_04.deref().super_.set(coord2);
      }
    }

    //LAB_800219ac
    //LAB_80021a98
    if(s2 == 0x2L && tmd != null) {
      updateTmdPacketIlen(getTmdObjTableOffset(tmd, dobj2Id), dobj2, 0);
    }

    //LAB_800219d8
    //LAB_80021ac0
    if(s2 == 0x8L && table != null) {
      FUN_80021bac(table, dobj2Id, maxSize);
    }

    if(s2 == 0x1L) {
      //LAB_800219ec
      rotation.x.set((short)11);
      rotation.y.set((short)11);
      rotation.z.set((short)11);

      dobj2.coord2_04.deref().coord.set(0, (short)0x1000);
      dobj2.coord2_04.deref().coord.set(1, (short)0);
      dobj2.coord2_04.deref().coord.set(2, (short)0);
      dobj2.coord2_04.deref().coord.set(3, (short)0);
      dobj2.coord2_04.deref().coord.set(4, (short)0x1000);
      dobj2.coord2_04.deref().coord.set(5, (short)0);
      dobj2.coord2_04.deref().coord.set(6, (short)0);
      dobj2.coord2_04.deref().coord.set(7, (short)0);
      dobj2.coord2_04.deref().coord.set(8, (short)0x1000);

      RotMatrixX(rotation.x.get(), coord);
      RotMatrixY(rotation.y.get(), coord);
      RotMatrixZ(rotation.z.get(), coord);

      scale.x.set(0x1000);
      scale.y.set(0x1000);
      scale.z.set(0x1000);

      RotMatrix_8003faf0(rotation, coord);
      ScaleMatrixL(coord, scale);

      translation.x.set(1);
      translation.y.set(1);
      translation.z.set(1);

      TransMatrix(coord, translation);
    }

    if(temp != null) {
      temp.release();
    }

    //LAB_80021ad8
  }

  @Method(0x80021b08L)
  public static void FUN_80021b08(final GsOBJTABLE2 table, final UnboundedArrayRef<GsDOBJ2> dobj2s, final UnboundedArrayRef<GsCOORDINATE2> coord2s, final UnboundedArrayRef<GsCOORD2PARAM> params, final long size) {
    table.top.set(dobj2s);
    table.nobj.set(0);

    //LAB_80021b2c
    for(int i = 0; i < size; i++) {
      dobj2s.get(i).attribute_00.set(0x8000_0000L);
      dobj2s.get(i).coord2_04.set(coord2s.get(i));
      dobj2s.get(i).tmd_08.clear();
      dobj2s.get(i).id_0c.set(0xffff_ffffL);

      coord2s.get(i).param.set(params.get(i));
    }

    //LAB_80021b5c
  }

  @Method(0x80021b64L)
  public static GsDOBJ2 getDObj2ById(final GsOBJTABLE2 table, final long id) {
    //LAB_80021b80
    for(int i = 0; i < table.nobj.get(); i++) {
      final GsDOBJ2 obj2 = table.top.deref().get(i);

      if(obj2.id_0c.get() == id) {
        return obj2;
      }

      //LAB_80021b98
    }

    //LAB_80021ba4
    return null;
  }

  @Method(0x80021bacL)
  public static void FUN_80021bac(final GsOBJTABLE2 table, final long id, final long maxSize) {
    final long size = table.nobj.get();

    //LAB_80021bd4
    GsDOBJ2 struct = table.top.deref().get(0);
    int i;
    for(i = 0; i < size; i++) {
      if((int)struct.id_0c.get() == -0x1L) {
        break;
      }

      struct = table.top.deref().get(i);
    }

    //LAB_80021c08
    if(i >= maxSize) {
      return;
    }

    //LAB_80021bf4
    if(i >= size) {
      struct = table.top.deref().get((int)table.nobj.get());
      table.nobj.incr();
    }

    //LAB_80021c2c
    struct.id_0c.set(id);
    struct.attribute_00.set(0);
    GsInitCoordinate2(null, struct.coord2_04.deref());
    struct.tmd_08.clear();

    //LAB_80021c48
  }

  @Method(0x80021c58L)
  public static UnboundedArrayRef<TmdObjTable> getTmdObjTableOffset(final Tmd tmd, final long objId) {
    long count = tmd.header.nobj.get();
    int id = 1;
    //LAB_80021c74
    while(count > 0) {
      if(id == objId) {
        //LAB_80021c8c
        return tmd.objTable.slice(id - 1);
      }

      count--;
      id++;
    }

    //LAB_80021c8c
    //LAB_80021c98
    return null;
  }

  @Method(0x80021ca0L)
  public static void FUN_80021ca0(final GsOBJTABLE2 table, final Tmd tmd, final GsCOORDINATE2 coord2, final long a3, final long a4) {
    long s1 = 0x801_0000L;

    //LAB_80021d08
    for(int s0 = 1; s0 < a4; s0++) {
      FUN_80021918(table, tmd, coord2, (short)a3, s1 / 0x1_0000L);
      s1 += 0x1_0000L;
    }

    //LAB_80021d3c
    long s2 = 0x101_0000L;
    s1 = 0x201_0000L;

    //LAB_80021d64
    for(int s0 = 1; s0 < a4; s0++) {
      FUN_80021918(table, tmd, coord2, (short)a3, s1 / 0x1_0000L);
      FUN_80021918(table, tmd, coord2, (short)a3, s2 / 0x1_0000L);
      s2 += 0x1_0000L;
      s1 += 0x1_0000L;
    }

    //LAB_80021db4
  }

  @Method(0x80021edcL)
  public static void SetRotMatrix(final MATRIX m) {
    CPU.CTC2((m.get(1) & 0xffffL) << 16 | m.get(0) & 0xffffL, 0); //
    CPU.CTC2((m.get(3) & 0xffffL) << 16 | m.get(2) & 0xffffL, 1); //
    CPU.CTC2((m.get(5) & 0xffffL) << 16 | m.get(4) & 0xffffL, 2); // Rotation matrix
    CPU.CTC2((m.get(7) & 0xffffL) << 16 | m.get(6) & 0xffffL, 3); //
    CPU.CTC2(m.get(8) & 0xffffL, 4); //
  }

  @Method(0x80021f0cL)
  public static void SetLightMatrix(final MATRIX m) {
    CPU.CTC2((m.get(1) & 0xffffL) << 16 | m.get(0) & 0xffffL,  8); //
    CPU.CTC2((m.get(3) & 0xffffL) << 16 | m.get(2) & 0xffffL,  9); //
    CPU.CTC2((m.get(5) & 0xffffL) << 16 | m.get(4) & 0xffffL, 10); // Light source matrix
    CPU.CTC2((m.get(7) & 0xffffL) << 16 | m.get(6) & 0xffffL, 11); //
    CPU.CTC2(m.get(8) & 0xffffL, 12); //
  }

  @Method(0x80021f3cL)
  public static void SetColorMatrix(final MATRIX m) {
    CPU.CTC2((m.get(1) & 0xffffL) << 16 | m.get(0) & 0xffffL, 16); //
    CPU.CTC2((m.get(3) & 0xffffL) << 16 | m.get(2) & 0xffffL, 17); //
    CPU.CTC2((m.get(5) & 0xffffL) << 16 | m.get(4) & 0xffffL, 18); // Light color matrix
    CPU.CTC2((m.get(7) & 0xffffL) << 16 | m.get(6) & 0xffffL, 19); //
    CPU.CTC2(m.get(8) & 0xffffL, 20); //
  }

  @Method(0x80021f6cL)
  public static void SetTransMatrix(final MATRIX m) {
    CPU.CTC2(m.transfer.getX(), 5); // Translation X
    CPU.CTC2(m.transfer.getY(), 6); // Translation Y
    CPU.CTC2(m.transfer.getZ(), 7); // Translation Z
  }

  @Method(0x80021f8cL)
  public static void SetBackColor(final long r, final long g, final long b) {
    CPU.CTC2(r * 0x10L, 13); // Background colour R
    CPU.CTC2(g * 0x10L, 14); // Background colour G
    CPU.CTC2(b * 0x10L, 15); // Background colour B
  }

  @Method(0x80021facL)
  public static void SetGeomOffset(final int x, final int y) {
    // cop2r56, OFX - Screen offset X
    CPU.CTC2(x << 0x10, 24);
    // cop2r57, OFY - Screen offset Y
    CPU.CTC2(y << 0x10, 25);
  }

  @Method(0x80021fc4L)
  public static long SquareRoot0(final long n) {
    return (long)Math.sqrt(n);
  }

  /**
   * This method animates the fog in the first cutscene with Rose/Feyrbrand
   */
  @Method(0x80022018L)
  public static void FUN_80022018(final BigStruct a0, final int index) {
    final RECT rect = new RECT();

    if(a0.aui_d0.get(index).get() == 0) {
      a0.aub_ec.get(index).set(0);
      return;
    }

    //LAB_80022068
    long v1 = a0.ub_9d.get();
    long t2;
    long v0;
    if((v1 & 0x80L) == 0) {
      v1 <<= 1;
      t2 = _800503b0.offset(v1).get();
      v0 = _800503d4.getAddress();
    } else {
      //LAB_80022098
      if(v1 == 0x80L) {
        return;
      }

      v1 &= 0x7fL;
      v1 <<= 1;
      t2 = _800503f8.offset(v1).get();
      v0 = _80050424.getAddress();
    }

    //LAB_800220c0
    long t1 = MEMORY.ref(2, v0).offset(v1).get();
    long s1;
    if(a0.usArr_ba.get(index).get() != 0x5678L) {
      a0.usArr_ba.get(index).decr();
      if(a0.usArr_ba.get(index).get() != 0) {
        return;
      }

      s1 = a0.aui_d0.get(index).get();
      a0.usArr_ba.get(index).set((int)MEMORY.ref(2, s1).get(0x7fffL));
      s1 += 0x2L;
      long a2 = MEMORY.ref(2, s1).get() + t2;
      s1 += 0x2L;
      long a0_1 = MEMORY.ref(2, s1).get() + t1;
      s1 += 0x2L;
      rect.w.set((short)(MEMORY.ref(2, s1).get() / 0x4L));
      s1 += 0x2L;
      rect.h.set((short)MEMORY.ref(2, s1).get());
      s1 += 0x2L;

      //LAB_80022154
      for(int i = 0; i < a0.usArr_ac.get(index).get(); i++) {
        s1 += 0x4L;
      }

      //LAB_80022164
      rect.x.set((short)(MEMORY.ref(2, s1).get() + t2));
      s1 += 0x2L;
      rect.y.set((short)(MEMORY.ref(2, s1).get() + t1));
      SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), rect, a2 & 0xffffL, a0_1 & 0xffffL);
      insertElementIntoLinkedList(tags_1f8003d0.deref().get(1).getAddress(), linkedListAddress_1f8003d8.get());
      linkedListAddress_1f8003d8.addu(0x18L);

      s1 += 0x2L;
      a0.usArr_ac.get(index).incr();

      v1 = MEMORY.ref(2, s1).get();
      if(v1 == 0xfffeL) {
        a0.aub_ec.get(index).set(0);
        a0.usArr_ac.get(index).set(0);
      }

      //LAB_800221f8
      if(v1 == 0xffffL) {
        a0.usArr_ac.get(index).set(0);
      }

      return;
    }

    //LAB_80022208
    s1 = a0.aui_d0.get(index).get();
    long a1_0 = a0.usArr_ac.get(index).get();
    s1 += 0x2L;
    long a0_0 = MEMORY.ref(2, s1).get();
    s1 += 0x2L;
    v0 = MEMORY.ref(2, s1).get();
    s1 += 0x2L;
    v1 = MEMORY.ref(2, s1).get();
    s1 += 0x2L;
    long s3 = MEMORY.ref(2, s1).get();
    s1 += 0x2L;
    long s0_0 = MEMORY.ref(2, s1).offset(0x2L).get();
    long s7 = v0 + t1;
    long s5 = v1 >>> 2;
    v1 = MEMORY.ref(2, s1).get();
    long s6 = a0_0 + t2;

    if((a1_0 & 0xfL) != 0) {
      a0.usArr_ac.get(index).set((int)(a1_0 - 0x1L));

      if(a0.usArr_ac.get(index).get() == 0) {
        a0.usArr_ac.get(index).set((int)s0_0);
        s0_0 = 0x10L;
      } else {
        //LAB_80022278
        s0_0 = 0;
      }
    }

    //LAB_8002227c
    s0_0 = (int)(s0_0 << 16);
    if(s0_0 == 0) {
      return;
    }

    rect.set((short)960, (short)256, (short)s5, (short)s3);
    SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), rect, s6 & 0xffffL, s7 & 0xffffL);
    insertElementIntoLinkedList(tags_1f8003d0.deref().get(1).getAddress(), linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0x18L);

    s0_0 = (int)s0_0 >> 20;
    s3 -= s0_0;

    long a3;
    if((int)(v1 << 16) == 0) {
      rect.set((short)s6, (short)(s7 + s3), (short)s5, (short)s0_0);
      SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), rect, 960L, 256L);
      insertElementIntoLinkedList(tags_1f8003d0.deref().get(1).getAddress(), linkedListAddress_1f8003d8.get());
      linkedListAddress_1f8003d8.addu(0x18L);

      a3 = s0_0 + 0x100L & 0xffffL;
      rect.set((short)s6, (short)s7, (short)s5, (short)s3);
    } else {
      //LAB_80022358

      rect.set((short)s6, (short)s7, (short)s5, (short)s0_0);
      SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), rect, 960L, s3 + 256L & 0xffffL);
      insertElementIntoLinkedList(tags_1f8003d0.deref().get(1).getAddress(), linkedListAddress_1f8003d8.get());
      linkedListAddress_1f8003d8.addu(0x18L);

      a3 = 0x100L;
      rect.set((short)s6, (short)(s0_0 + s7), (short)s5, (short)s3);
    }

    //LAB_8002241c
    SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), rect, 960L, a3);
    insertElementIntoLinkedList(tags_1f8003d0.deref().get(1).getAddress(), linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0x18L);

    //LAB_80022440
  }

  @Method(0x8002246cL)
  public static void FUN_8002246c(final BigStruct a0, final int a1) {
    if(a0.aui_d0.get(a1).get() == 0) {
      a0.aub_ec.get(a1).set(0);
      return;
    }

    //LAB_80022490
    a0.usArr_ac.get(a1).set(0);
    a0.usArr_ba.get(a1).set((int)MEMORY.ref(2, a0.aui_d0.get(a1).get()).get(0x3fffL));

    if(MEMORY.ref(2, a0.aui_d0.get(a1).get()).get(0x8000L) != 0) {
      a0.aub_ec.get(a1).set(1);
    } else {
      //LAB_800224d0
      a0.aub_ec.get(a1).set(0);
    }

    //LAB_800224d8
    if(MEMORY.ref(2, a0.aui_d0.get(a1).get()).get(0x4000L) != 0) {
      a0.usArr_ba.get(a1).set(0x5678);
      a0.usArr_ac.get(a1).set((int)MEMORY.ref(2, a0.aui_d0.get(a1).get()).offset(0xcL).get());
      a0.aub_ec.get(a1).set(1);
    }

    //LAB_80022510
  }

  @Method(0x80022518L)
  public static void FUN_80022518() {
    // Empty
  }

  @Method(0x80022520L)
  public static void FUN_80022520(final long unused) {
    whichMenu_800bdc38.setu(0x4L);
  }

  @Method(0x80022540L)
  public static void FUN_80022540(final long unused) {
    whichMenu_800bdc38.setu(0xeL);
  }

  @Method(0x80022590L)
  public static void FUN_80022590() {
    switch((int)whichMenu_800bdc38.get()) {
      case 0x1 -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          inventoryMenuState_800bdc28.setu(0);
          whichMenu_800bdc38.setu(0x2L);
          FUN_8001e010(0);
          _800bc0b9.setu(0x1L);
        }
      }

      case 0x2 -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38.addu(0x1L);
          FUN_80012b1c(0x2L, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_80022520", long.class), 0);
        }
      }

      case 0x6 -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          inventoryMenuState_800bdc28.setu(0);
          whichMenu_800bdc38.setu(0x7L);
          FUN_8001e010(0);
          _800bc0b9.setu(0x1L);
        }
      }

      case 0x7 -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38.addu(0x1L);
          FUN_80012b1c(0x2L, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_80022530", long.class), 0);
        }
      }

      case 0xb -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          inventoryMenuState_800bdc28.setu(0);
          whichMenu_800bdc38.setu(0xcL);
          FUN_8001e010(0);
          _800bc0b9.setu(0x1L);
        }
      }

      case 0xc -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38.addu(0x1L);
          FUN_80012b1c(0x2L, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_80022540", long.class), 0);
        }
      }

      case 0x10 -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          inventoryMenuState_800bdc28.setu(0);
          whichMenu_800bdc38.setu(0x11L);
          FUN_8001e010(0);
          _800bc0b9.setu(0x1L);
        }
      }

      case 0x11 -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38.addu(0x1L);
          FUN_80012b1c(0x2L, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_80022550", long.class), 0);
        }
      }

      case 0x15 -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          inventoryMenuState_800bdc28.setu(0);
          whichMenu_800bdc38.setu(0x16L);
          FUN_8001e010(0);
          _800bc0b9.setu(0x1L);
        }
      }

      case 0x16 -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38.addu(0x1L);
          FUN_80012b1c(0x2L, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_80022560", long.class), 0);
        }
      }

      case 0x1f -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          inventoryMenuState_800bdc28.setu(0);
          whichMenu_800bdc38.setu(0x20);
          FUN_8001e010(0);
          _800bc0b9.setu(0x1L);
        }
      }

      case 0x1a -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          inventoryMenuState_800bdc28.setu(0);
          whichMenu_800bdc38.setu(0x1bL);
        }
      }

      case 0x1b -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38.addu(0x1L);
          FUN_80012b1c(0x2L, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_80022570", long.class), 0);
        }
      }

      case 0x20 -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38.addu(0x1L);
          FUN_80012b1c(0x2L, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_80022580", long.class), 0);
        }
      }

      case 0xe, 0x13, 0x18, 0x24, 0x4 -> FUN_800fcad4();
      case 0x1d -> FUN_8010d614();
      case 0x9 -> FUN_8010a948();
      case 0x22 -> FUN_8010f198();

      case 0xa, 0xf, 0x19, 0x23, 0x5 -> {
        FUN_80012bb4();
        FUN_8001e010(-0x1L);
        _800bc0b9.setu(0);
        whichMenu_800bdc38.setu(0);
      }

      case 0x1e, 0x14 -> {
        FUN_80012bb4();
        _800bc0b9.setu(0);
        whichMenu_800bdc38.setu(0);
      }
    }
  }

  @Method(0x800228d0L)
  public static long FUN_800228d0(final long a0) {
    if(a0 >= 0xc0L) {
      return _8004f2ac.offset((a0 - 0xc0L) * 0xcL).offset(0x7L).getSigned();
    }

    //LAB_80022908
    return _80111ff0.offset(a0 * 0x1cL).offset(0xeL).getSigned();
  }

  @Method(0x80022928L)
  public static long FUN_80022928(final byte[] a0, final long a1) {
    //LAB_80022940
    for(int a2 = 0; a2 < 8; a2++) {
      a0[a2] = (byte)0xff;
    }

    if(a1 == -0x1L) {
      //LAB_80022a08
      return 0;
    }

    if(a1 == 0 && (_800bad64.get() >>> 7) != 0) {
      a0[0] = 9;
      a0[1] = 4;
      return 0x2L;
    }

    //LAB_80022994
    //LAB_80022998
    long a0_0 = _80111d20.offset(a1 * 0x4L).get();

    //LAB_800229d0
    int a3 = 0;
    for(int i = 0; i < _800be5f8.offset(a1 * 0xa0L).offset(1, 0xfL).get() + 0x1L; i++) {
      final long v1 = MEMORY.ref(1, a0_0).offset(0x2L).get();

      if(v1 != 0xffL) {
        a0[a3] = (byte)v1;
        a3++;
      }

      //LAB_800229e8
      a0_0 += 0x8L;
    }

    //LAB_80022a00
    return a3;
  }

  @Method(0x80022a10L)
  public static long FUN_80022a10(final long a0) {
    if((int)a0 == -0x1L) {
      return 0;
    }

    //LAB_80022a24
    if(a0 == 0 && (_800bad64.get() >>> 7) != 0) {
      return 0x2L;
    }

    //LAB_80022a4c
    //LAB_80022a50
    final long v1 = _80111d20.offset(a0 * 0x4L).get();

    //LAB_80022a64
    long a2 = 0;
    for(int i = 0; i < 6; i++) {
      if(MEMORY.ref(1, v1).offset(i * 0x8L).offset(0x2L).get() != 0xffL) {
        a2 = a2 + 0x1L;
      }

      //LAB_80022a7c
    }

    return a2;
  }

  @Method(0x80022a94L)
  public static void FUN_80022a94(final Value address) {
    final TimHeader header = parseTimHeader(address.offset(0x4L));

    if(header.imageRect.w.get() != 0 || header.imageRect.h.get() != 0) {
      LoadImage(header.imageRect, header.imageAddress.get());
    }

    //LAB_80022acc
    if((header.flags.get() & 0x8L) != 0) {
      LoadImage(header.clutRect, header.clutAddress.get());
    }

    //LAB_80022aec
  }

  @Method(0x80022afcL)
  public static long FUN_80022afc(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x80022b50L)
  public static long FUN_80022b50(final long a0, final long a1) {
    assert false;
    return 0;
  }

  @Method(0x80022c08L)
  public static long FUN_80022c08(final long a0, final long a1) {
    assert false;
    return 0;
  }

  @Method(0x80022d88L)
  public static long FUN_80022d88(final long a0, final long a1, final long a2) {
    assert false;
    return 0;
  }

  @Method(0x80023148L)
  public static void FUN_80023148() {
    _800babc8.offset(2, 0x1e4L).setu(0);

    //LAB_80023164
    while(_800babc8.offset(1, _800babc8.offset(2, 0x1e4L)).offset(0x1e8L).get() != 0xffL) {
      if(_800babc8.offset(2, 0x1e4L).getSigned() >= 0xffL) {
        break;
      }

      _800babc8.offset(2, 0x1e4L).addu(0x1L);
    }

    //LAB_80023198
    //LAB_800231c0
    for(long v1 = _800babc8.offset(2, 0x1e4L).getSigned(); v1 < 0x101L; v1++) {
      _800babc8.offset(1, v1).offset(0x1e8L).setu(0xffL);
    }

    //LAB_800231d8
    _800babc8.offset(2, 0x1e6L).setu(0);

    //LAB_800231f0
    while(_800babc8.offset(1, _800babc8.offset(2, 0x1e6L)).offset(0x2e9L).get() != 0xffL) {
      if(_800babc8.offset(2, 0x1e6L).getSigned() >= 0x20L) {
        break;
      }

      _800babc8.offset(2, 0x1e6L).addu(0x1L);
    }

    //LAB_80023224
    //LAB_80023248
    for(long v1 = _800babc8.offset(2, 0x1e6L).getSigned(); v1 < 0x21L; v1++) {
      _800babc8.offset(1, v1).offset(0x2e9L).setu(0xffL);
    }

    //LAB_8002325c
  }

  @Method(0x800232dcL)
  public static long FUN_800232dc(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x800233d8L)
  public static long FUN_800233d8(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x80023484L)
  public static long FUN_80023484(final long a0) {
    assert false;
    return 0;
  }

  /**
   * @param part 0: second, 1: minute, 2: hour
   */
  @Method(0x80023674L)
  public static long getTimestampPart(long timestamp, final long part) {
    if(timestamp >= 216000000L) { // Clamp to 1000 hours
      timestamp = 215999999L;
    }

    // Hours
    if(part == 0) {
      return timestamp / 216000L % 1000L;
    }

    // Minutes
    if(part == 1) {
      return timestamp / 3600L % 60L;
    }

    // Seconds
    if(part == 2) {
      return timestamp / 60L % 60L;
    }

    return 0;
  }

  @Method(0x8002379cL)
  public static void FUN_8002379c() {
    // empty
  }

  @Method(0x800237a4L)
  public static long hasSavedGames(final long initializeMemcard) {
    if(initializeMemcard == 0) {
      FUN_8002df60(0);
      return 0;
    }

    final Ref<Long> sp18 = new Ref<>(0L);
    final Ref<Long> sp1c = new Ref<>(0L);
    final Ref<Long> fileCount = new Ref<>(0L);

    //LAB_800237c8
    if(FUN_8002efb8(0x1L, sp18, sp1c) == 0) {
      return 0;
    }

    if(sp1c.get() != 0 && sp1c.get() != 0x3L) {
      return 0x2L;
    }

    //LAB_800237fc
    memcardStruct28ArrPtr_800bdc50.setPointer(addToLinkedListTail(0x280L));
    FUN_8002ed48(0, BASCUS_94491drgn00_80010734.getAddress(), memcardStruct28ArrPtr_800bdc50.deref(), fileCount, 0, 0xfL);
    removeFromLinkedList(memcardStruct28ArrPtr_800bdc50.getPointer());

    if(fileCount.get() == 0) {
      //LAB_80023854
      return 0x2L;
    }

    //LAB_80023860
    return 0x1L;
  }

  @Method(0x80023870L)
  public static void playSound(final long soundIndex) {
    Scus94491BpeSegment.playSound(0, (int)soundIndex, 0, 0, (short)0, (short)0);
  }

  /**
   * Gets the highest priority button on the joypad that is currently pressed. "Priority" is likely arbitrary.
   */
  @Method(0x800238a4L)
  public static long getJoypadInputByPriority() {
    final long repeat = joypadRepeat_8007a3a0.get();

    if((repeat & 0x4L) != 0) {
      return 0x4L;
    }

    //LAB_800238c4
    if((repeat & 0x8L) != 0) {
      return 0x8L;
    }

    //LAB_800238d4
    if((repeat & 0x1L) != 0) {
      return 0x1L;
    }

    //LAB_800238e4
    if((repeat & 0x2L) != 0) {
      return 0x2L;
    }

    //LAB_800238f4
    if((repeat & 0x1000L) != 0) {
      return 0x1000L;
    }

    //LAB_80023904
    if((repeat & 0x4000L) != 0) {
      return 0x4000L;
    }

    //LAB_80023914
    if((repeat & 0x8000L) != 0) {
      return 0x8000L;
    }

    //LAB_80023924
    if((repeat & 0x2000L) != 0) {
      return 0x2000L;
    }

    //LAB_80023934
    final long press = joypadPress_8007a398.get();

    if((press & 0x10L) != 0) {
      return 0x10L;
    }

    //LAB_80023950
    if((press & 0x40L) != 0) {
      return 0x40L;
    }

    //LAB_80023960
    if((press & 0x80L) != 0) {
      return 0x80L;
    }

    //LAB_80023970
    return press & 0x20L;
  }

  @Method(0x800239e0L)
  public static void FUN_800239e0(final long a0, final long a1, final long a2) {
    assert false;
  }

  @Method(0x80023a2cL)
  public static void FUN_80023a2c(final long a0, final long a1, final long a2) {
    assert false;
  }

  @Method(0x80023b54L)
  public static Renderable58 allocateRenderable(final Drgn0_6666Struct a0, @Nullable Renderable58 a1) {
    if(a1 == null) {
      a1 = MEMORY.ref(4, addToLinkedListTail(0x58L), Renderable58::new);
    }

    //LAB_80023b7c
    a1.flags_00.set(0);
    a1.glyph_04.set(0);
    a1._08.set(a0._0a.get());
    a1._0c.set(0);
    a1.startGlyph_10.set(0);
    a1.endGlyph_14.set(a0.entryCount_06.get() - 0x1L);
    a1._18.set(0);
    a1._1c.set(0);
    a1.drgn0_6666_20.set(a0);
    a1.drgn0_6666_data_24.set(MEMORY.ref(4, a0.entries_08.get(a0.entryCount_06.get()).getAddress()).cast(UnboundedArrayRef.of(0x4, UnsignedIntRef::new)));
    a1._28.set(0);
    a1.tpage_2c.set(0);
    a1._34.set(0x1000L);
    a1._38.set(0x1000L);
    a1._3c.set(0x24);
    a1.x_40.set(0);
    a1.y_44.set(0);
    a1._48.set(0);
    a1.child_50.clear();

    if(!renderablePtr_800bdc5c.isNull()) {
      a1.parent_54.set(renderablePtr_800bdc5c.deref());
      renderablePtr_800bdc5c.deref().child_50.set(a1);
    } else {
      //LAB_80023c08
      a1.parent_54.clear();
      renderablePtr_800bdc5c.set(a1);
    }

    //LAB_80023c0c
    renderablePtr_800bdc5c.set(a1);
    return a1;
  }

  @Method(0x80023c28L)
  public static void uploadRenderables() {
    Renderable58 renderable = renderablePtr_800bdc5c.derefNullable();

    _800bdc58.addu(0x1L);

    //LAB_80023c8c
    while(renderable != null) {
      boolean forceUnload = false;
      final UnboundedArrayRef<Drgn0_6666Entry> entries = renderable.drgn0_6666_20.deref().entries_08;

      if((renderable.flags_00.get() & 0x4L) == 0) {
        renderable._08.decr();

        if(renderable._08.get() < 0) {
          if((renderable.flags_00.get() & 0x20L) != 0) {
            renderable.glyph_04.decr();

            if(renderable.glyph_04.get() < renderable.startGlyph_10.get()) {
              if((renderable.flags_00.get() & 0x10L) != 0) {
                forceUnload = true;
                renderable.flags_00.or(0x40L);
              }

              //LAB_80023d0c
              if(renderable._18.get() != 0) {
                renderable.startGlyph_10.set(renderable._18);

                if(renderable._1c.get() != 0) {
                  renderable.endGlyph_14.set(renderable._1c);
                } else {
                  //LAB_80023d34
                  renderable.endGlyph_14.set(renderable._18);
                  renderable.flags_00.or(0x4L);
                }

                //LAB_80023d48
                renderable._18.set(0);
                renderable.flags_00.and(0xffff_ffdfL);
              }

              //LAB_80023d5c
              //LAB_80023e00
              renderable.glyph_04.set(renderable.endGlyph_14);
              renderable._0c.incr();
            }
          } else {
            //LAB_80023d6c
            renderable.glyph_04.incr();

            if(renderable.endGlyph_14.get() < renderable.glyph_04.get()) {
              if((renderable.flags_00.get() & 0x10L) != 0) {
                forceUnload = true;
                renderable.flags_00.or(0x40L);
              }

              //LAB_80023da4
              if(renderable._18.get() != 0) {
                renderable.startGlyph_10.set(renderable._18);

                if(renderable._1c.get() != 0) {
                  renderable.endGlyph_14.set(renderable._1c);
                } else {
                  //LAB_80023dcc
                  renderable.endGlyph_14.set(renderable._18);
                  renderable.flags_00.or(0x4L);
                }

                //LAB_80023de0
                renderable._18.set(0);
                renderable.flags_00.and(0xffff_ffdfL);
              }

              //LAB_80023df4
              //LAB_80023e00
              renderable.glyph_04.set(renderable.startGlyph_10);
              renderable._0c.incr();
            }
          }

          //LAB_80023e08
          renderable._08.set(entries.get((int)renderable.glyph_04.get())._02.get() - 1);
        }
      }

      //LAB_80023e28
      if((renderable.flags_00.get() & 0x40L) == 0) {
        final long centreX = displayWidth_1f8003e0.get() / 2 + 0x8L;

        final ArrayRef<RenderableMetrics14> metricses = renderable.drgn0_6666_20.deref().getMetrics((int)renderable.drgn0_6666_data_24.deref().get(entries.get((int)renderable.glyph_04.get())._00.get()).get());

        //LAB_80023e94
        for(int i = metricses.length() - 1; i >= 0; i--) {
          final RenderableMetrics14 metrics = metricses.get(i);

          final long packet = linkedListAddress_1f8003d8.get();
          linkedListAddress_1f8003d8.addu(0x28L);
          MEMORY.ref(1, packet).offset(0x3L).setu(0x9L);
          MEMORY.ref(4, packet).offset(0x4L).setu(0x2c80_8080L);
          MEMORY.ref(1, packet).offset(0x4L).setu(0x80L); // R
          MEMORY.ref(1, packet).offset(0x5L).setu(0x80L); // G
          MEMORY.ref(1, packet).offset(0x6L).setu(0x80L); // B
          MEMORY.ref(1, packet).offset(0x7L).oru((metrics.clut_04.get() & 0x8000L) >>> 14); // Command

          final long x1;
          final long x2;
          if(renderable._34.get() == 0x1000L) {
            if(metrics._10.get() < 0) {
              x2 = renderable.x_40.get() + metrics.x_02.get() - centreX;
              x1 = x2 + metrics.width_08.get();
            } else {
              //LAB_80023f20
              x1 = renderable.x_40.get() + metrics.x_02.get() - centreX;
              x2 = x1 + metrics.width_08.get();
            }
          } else {
            //LAB_80023f40
            final long a0_0 = renderable._34.get() != 0 ? renderable._34.get() : metrics._10.get();

            //LAB_80023f4c
            //LAB_80023f68
            final long a1 = abs((int)(metrics.width_08.get() * a0_0 / 0x1000));
            if(metrics._10.get() < 0) {
              x2 = renderable.x_40.get() + metrics.width_08.get() / 2 + metrics.x_02.get() - centreX - a1 / 2;
              x1 = x2 + a1;
            } else {
              //LAB_80023fb4
              x1 = renderable.x_40.get() + metrics.width_08.get() / 2 + metrics.x_02.get() - centreX - a1 / 2;
              x2 = x1 + a1;
            }
          }

          //LAB_80023fe4
          final long y1;
          final long y2;
          if(renderable._38.get() == 0x1000L) {
            if(metrics._12.get() < 0) {
              y2 = renderable.y_44.get() + metrics.y_03.get() - 120;
              y1 = y2 + metrics.height_0a.get();
            } else {
              //LAB_80024024
              y1 = renderable.y_44.get() + metrics.y_03.get() - 120;
              y2 = y1 + metrics.height_0a.get();
            }
          } else {
            //LAB_80024044
            final long a0_0 = renderable._38.get() != 0 ? renderable._38.get() : metrics._12.get();

            //LAB_80024050
            //LAB_8002406c
            final long a1 = abs((int)(metrics.height_0a.get() * a0_0 / 0x1000));
            if(metrics._12.get() < 0) {
              y2 = renderable.y_44.get() + metrics.height_0a.get() / 2 + metrics.y_03.get() - a1 / 2 - 120;
              y1 = y2 + a1;
            } else {
              //LAB_800240b8
              y1 = renderable.y_44.get() + metrics.height_0a.get() / 2 + metrics.y_03.get() - a1 / 2 - 120;
              y2 = y1 + a1;
            }
          }

          //LAB_800240e8
          MEMORY.ref(2, packet).offset(0x08L).setu(x1); // V0 X
          MEMORY.ref(2, packet).offset(0x0aL).setu(y1); // V0 Y
          MEMORY.ref(1, packet).offset(0x0cL).setu(metrics.u_00.get()); // V0 U
          MEMORY.ref(1, packet).offset(0x0dL).setu(metrics.v_01.get()); // V0 V
          MEMORY.ref(2, packet).offset(0x10L).setu(x2); // V1 X
          MEMORY.ref(2, packet).offset(0x12L).setu(y1); // V1 Y
          MEMORY.ref(2, packet).offset(0x18L).setu(x1); // V2 X
          MEMORY.ref(2, packet).offset(0x1aL).setu(y2); // V2 Y
          MEMORY.ref(2, packet).offset(0x20L).setu(x2); // V3 X
          MEMORY.ref(2, packet).offset(0x22L).setu(y2); // V4 Y

          //LAB_80024144
          //LAB_800241b4
          long v1 = metrics.u_00.get() + metrics.width_08.get();
          final long u = v1 < 0xffL ? v1 : v1 - 0x1L;

          //LAB_80024188
          //LAB_800241b8
          //LAB_800241e0
          v1 = metrics.v_01.get() + metrics.height_0a.get();
          final long v = v1 < 0xffL ? v1 : v1 - 0x1L;

          //LAB_80024148
          MEMORY.ref(1, packet).offset(0x14L).setu(u); // V1 U
          MEMORY.ref(1, packet).offset(0x15L).setu(metrics.v_01.get()); // V1 V

          //LAB_8002418c
          MEMORY.ref(1, packet).offset(0x1cL).setu(metrics.u_00.get()); // V2 U
          MEMORY.ref(1, packet).offset(0x1dL).setu(v); // V2 V

          //LAB_800241e4
          MEMORY.ref(1, packet).offset(0x24L).setu(u); // V3 U
          MEMORY.ref(1, packet).offset(0x25L).setu(v); // V3 V

          if(renderable.clut_30.get() != 0) {
            MEMORY.ref(2, packet).offset(0xeL).setu(renderable.clut_30.get()); // CLUT
          } else {
            //LAB_80024204
            MEMORY.ref(2, packet).offset(0xeL).setu(metrics.clut_04.get() & 0x7fffL); // CLUT
          }

          //LAB_80024214
          if(renderable.tpage_2c.get() != 0) {
            MEMORY.ref(2, packet).offset(0x16L).setu(metrics.tpage_06.get() & 0x60L | renderable.tpage_2c.get()); // TPAGE
          } else {
            //LAB_8002423c
            MEMORY.ref(2, packet).offset(0x16L).setu(metrics.tpage_06.get() & 0x7fL); // TPAGE
          }

          //LAB_8002424c
          insertElementIntoLinkedList(tags_1f8003d0.deref().get(renderable._3c.get()).getAddress(), packet);
        }
      }

      //LAB_80024280
      if((renderable.flags_00.get() & 0x8L) != 0 || forceUnload) {
        //LAB_800242a8
        unloadRenderable(renderable);
      }

      //LAB_800242b0
      renderable = renderable.parent_54.derefNullable();
    }

    //LAB_800242b8
  }

  @Method(0x800242e8L)
  public static void unloadRenderable(final Renderable58 a0) {
    final Renderable58 v0 = a0.child_50.derefNullable();
    final Renderable58 v1 = a0.parent_54.derefNullable();

    if(v0 == null) {
      if(v1 == null) {
        renderablePtr_800bdc5c.clear();
      } else {
        //LAB_80024320
        renderablePtr_800bdc5c.set(v1);
        v1.child_50.clear();
      }
      //LAB_80024334
    } else if(v1 == null) {
      v0.parent_54.clear();
    } else {
      //LAB_80024350
      v0.parent_54.set(v1);
      v1.child_50.setNullable(a0.child_50.derefNullable());
    }

    //LAB_80024364
    removeFromLinkedList(a0.getAddress());
  }

  @Method(0x8002437cL)
  public static void FUN_8002437c(final long a0) {
    Renderable58 s0 = renderablePtr_800bdc5c.derefNullable();

    if(s0 != null) {
      //LAB_800243b4
      while(!s0.parent_54.isNull()) {
        final Renderable58 a0_0 = s0;
        s0 = s0.parent_54.deref();

        if(a0_0._28.get() <= a0) {
          unloadRenderable(a0_0);
        }

        //LAB_800243d0
      }

      //LAB_800243e0
      if(s0._28.get() <= a0) {
        unloadRenderable(s0);
      }

      //LAB_800243fc
      if(a0 != 0) {
        saveListUpArrow_800bdb94.clear();
        saveListDownArrow_800bdb98.clear();
        _800bdb9c.setu(0);
        _800bdba0.setu(0);
        renderablePtr_800bdba4.clear();
        renderablePtr_800bdba8.clear();

        selectedMenuOptionRenderablePtr_800bdbe0.clear();
        selectedMenuOptionRenderablePtr_800bdbe4.clear();
        renderablePtr_800bdbe8.clear();
        renderablePtr_800bdbec.clear();
        _800bdbf0.setu(0);

        renderablePtr_800bdc20.clear();
      }
    }

    //LAB_80024460
  }

  @Method(0x80024654L)
  public static void FUN_80024654() {
    FUN_800fbec8(_80052ae0.getAddress());
  }

  @Method(0x8002498cL)
  public static void noop_8002498c() {
    // empty
  }

  @Method(0x80024994L)
  public static void FUN_80024994() {
    // empty
  }

  /**
   * Loads DRGN0 MRG @ 77382 (basic UI textures)
   *
   * <ol start="0">
   *   <li>Game font</li>
   *   <li>Japanese font</li>
   *   <li>Japanese text</li>
   *   <li>Dialog box border</li>
   *   <li>Red downward bobbing arrow</li>
   * </ol>
   */
  @Method(0x800249b4L)
  public static void basicUiTexturesLoaded(final Value address, final long fileSize, final long unused) {
    final RECT[] rects = new RECT[28]; // image size, clut size, image size, clut size...

    for(int i = 0; i < 28; i++) {
      rects[i] = new RECT().set(rectArray28_80010770.get(i)); // Detach from the heap
    }

    rects[2].x.set((short)0);
    rects[2].y.set((short)0);
    rects[2].w.set((short)0x40);
    rects[2].h.set((short)0x10);
    rects[3].x.set((short)0);
    rects[3].y.set((short)0);
    rects[3].w.set((short)0);
    rects[3].h.set((short)0);

    final int[] indexOffsets = {0, 20, 22, 24, 26};

    final MrgFile mrg = address.deref(4).cast(MrgFile::new);

    //LAB_80024e88
    for(int i = 0; i < mrg.count.get(); i++) {
      final MrgEntry entry = mrg.entries.get(i);

      if(entry.size.get() != 0) {
        final TimHeader tim = parseTimHeader(MEMORY.ref(4, mrg.getFile(i)).offset(0x4L));
        final int rectIndex = indexOffsets[i];

        // Iteration 0 will count as >= 2
        if(MathHelper.unsign(i - 1, 4) >= 0x2L) {
          LoadImage(rects[rectIndex], tim.getImageAddress());
        }

        //LAB_80024efc
        if(i == 0x3L) {
          //LAB_80024f2c
          LoadImage(rects[i * 2 + 1], tim.getClutAddress());
        } else if(i < 0x4L) {
          //LAB_80024fac
          for(int s0 = 0; s0 < 4; s0++) {
            final RECT rect = new RECT().set(rects[rectIndex + 1]);
            rect.x.set((short)(rect.x.get() + s0 * 16));
            LoadImage(rect, tim.getClutAddress() + s0 * 0x80L);
          }
          //LAB_80024f1c
        } else if(i == 0x4L) {
          //LAB_80024f68
          LoadImage(rects[rectIndex + 1], tim.getClutAddress());
        }

        //LAB_80025000
        DrawSync(0);
      }

      //LAB_80025008
    }

    //LAB_80025018
    removeFromLinkedList(mrg.getAddress());
  }

  @Method(0x8002504cL)
  public static void loadBasicUiTexturesAndSomethingElse() {
    loadDrgnBinFile(0, 6669, 0, getMethodAddress(Scus94491BpeSegment_8002.class, "basicUiTexturesLoaded", Value.class, long.class, long.class), 0, 0x4L);
    noop_8002498c();

    _800bdf00.setu(0xdL);
    _800bdf04.setu(0);
    _800bdf08.setu(0);
    _800be5c4.setu(0);
    FUN_8002a6fc();

    //LAB_800250c0
    for(int i = 0; i < 8; i++) {
      FUN_80029920(i, 0);
    }

    //LAB_800250ec
    for(int i = 0; i < 8; i++) {
      _800be358.get(i)._00.set(0);
      _800bdf38.get(i)._00.set(0);
    }

    //LAB_80025118
    for(int i = 0; i < 8; i++) {
      _800bdf18.offset(i * 0x4L).setu(0);
    }

    _800be5b8.setu(0);
    _800be5bc.setu(0);
    _800be5c0.setu(0);
    _800be5c8.setu(0);
  }

  @Method(0x80025218L)
  public static long FUN_80025218(final RunningScript a0) {
    if(a0.params_20.get(2).deref().get() == 0) {
      return 0;
    }

    final long s2 = a0.params_20.get(0).deref().get();
    final long s3 = _80052ba8.offset(((a0.params_20.get(2).deref().get() & 0xf00L) >>> 8) * 0x2L).get();
    FUN_800257e0((int)s2);

    final Struct4c struct4c = _800be358.get((int)s2);
    struct4c._04.set((short)_80052b88.offset(((a0.params_20.get(2).deref().get() & 0xf0L) >>> 4) * 0x2L).get());
    struct4c._06.set((short)_80052b68.offset((a0.params_20.get(2).deref().get() & 0xfL) * 0x2L).get());
    struct4c._14.set(0);
    struct4c._16.set(0);
    struct4c._18.set((short)(a0.params_20.get(3).deref().get() + 0x1L));
    struct4c._1a.set((short)(a0.params_20.get(4).deref().get() + 0x1L));
    FUN_800258a8(s2);

    final Struct84 struct84 = _800bdf38.get((int)s2);
    struct84._04.set((int)s3);
    struct84.ptr_24.set(a0.params_20.get(5).getPointer());

    if((short)s3 == 0x1L && (a0.params_20.get(1).deref().get() & 0x1000L) > 0) {
      struct84._08.or(0x20L);
    }

    //LAB_80025370
    //LAB_80025374
    if((short)s3 == 0x3L) {
      struct84._6c.set(-0x1);
    }

    //LAB_800253a4
    if((short)s3 == 0x4L) {
      struct84._08.or(0x200L);
    }

    //LAB_800253d4
    struct84._08.or(0x1000L);
    struct84.ptr_58.set(addToLinkedListHead(struct84._1c.get() * (struct84._1e.get() + 0x1L) * 0x8L));
    FUN_8002a2b4(s2);
    FUN_80028938(s2, a0.params_20.get(1).deref().get());

    if(struct4c._04.get() == 0x2L) {
      //TODO these might be wrong
      struct4c._24.get(5).set((short)struct4c._14.get());
      struct4c._24.get(6).set((short)struct4c._16.get());
      struct4c._14.set((int)struct4c._24.get(1).get());
      struct4c._16.set((int)struct4c._24.get(2).get());
      struct4c._08.or(0x2L);
    }

    //LAB_80025494
    //LAB_80025498
    return 0;
  }

  @Method(0x800254bcL)
  public static long FUN_800254bc(final RunningScript a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long s0;
    long s1;
    long s2;
    long s4;
    long s5;

    v1 = a0.params_20.get(1).deref().get();
    s2 = a0.params_20.get(0).deref().get();
    if(v1 != 0) {
      v0 = 0x8005_0000L;
      a2 = a0.params_20.get(1).deref().get();
      v0 = v0 + 0x2b88L;
      a3 = a2 & 0xf0L;
      a3 = a3 >>> 3;
      a3 = a3 + v0;
      v0 = 0x8005_0000L;
      v0 = v0 + 0x2b68L;
      a1 = a2 & 0xfL;
      a1 = a1 << 1;
      a1 = a1 + v0;
      v0 = 0x8005_0000L;
      v0 = v0 + 0x2ba8L;
      v1 = a2 & 0xf00L;
      v1 = v1 >>> 7;
      v1 = v1 + v0;
      a2 = a2 & 0x1000L;
      s0 = MEMORY.ref(2, a3).offset(0x0L).get();
      s1 = MEMORY.ref(2, a1).offset(0x0L).get();
      s4 = MEMORY.ref(2, v1).offset(0x0L).get();
      s5 = 0 < a2 ? 1 : 0;
      FUN_800257e0((int)s2);
      v0 = 0x800c_0000L;
      v0 = v0 - 0x1ca8L;
      v1 = s2 << 2;
      v1 = v1 + s2;
      v1 = v1 << 2;
      v1 = v1 - s2;
      v1 = v1 << 2;
      v1 = v1 + v0;
      MEMORY.ref(2, v1).offset(0x4L).setu(s0);
      MEMORY.ref(2, v1).offset(0x6L).setu(s1);

      v0 = a0.params_20.get(2).deref().get() & 0xffffL;
      MEMORY.ref(2, v1).offset(0x14L).setu(v0);

      v0 = a0.params_20.get(3).deref().get() & 0xffffL;
      MEMORY.ref(2, v1).offset(0x16L).setu(v0);

      v0 = a0.params_20.get(4).deref().get() & 0xffffL;
      v0 = v0 + 0x1L;
      MEMORY.ref(2, v1).offset(0x18L).setu(v0);

      v0 = a0.params_20.get(5).deref().get() & 0xffffL;
      v0 = v0 + 0x1L;
      MEMORY.ref(2, v1).offset(0x1aL).setu(v0);
      FUN_800258a8(s2);
      v1 = 0x800c_0000L;
      v1 = v1 - 0x20c8L;
      v0 = s2 << 5;
      v0 = v0 + s2;
      v0 = v0 << 2;
      v1 = v0 + v1;
      MEMORY.ref(2, v1).offset(0x4L).setu(s4);

      v0 = a0.params_20.get(6).getPointer();
      MEMORY.ref(4, v1).offset(0x24L).setu(v0);
      if((short)s4 == 0x1L && s5 == 0x1L) {
        MEMORY.ref(4, v1).offset(0x8L).oru(0x20L);
      }

      //LAB_8002562c
      //LAB_80025630
      if((short)s4 == 0x3L) {
        v0 = 0x800c_0000L;
        v0 = v0 - 0x20c8L;
        v1 = s2 << 5;
        v1 = v1 + s2;
        v1 = v1 << 2;
        v1 = v1 + v0;
        v0 = -0x1L;
        MEMORY.ref(4, v1).offset(0x6cL).setu(v0);
      }

      //LAB_80025660
      if((short)s4 == 0x4L) {
        v0 = 0x800c_0000L;
        v0 = v0 - 0x20c8L;
        v1 = s2 << 5;
        v1 = v1 + s2;
        v1 = v1 << 2;
        v1 = v1 + v0;
        v0 = MEMORY.ref(4, v1).offset(0x8L).get();

        v0 = v0 | 0x200L;
        MEMORY.ref(4, v1).offset(0x8L).setu(v0);
      }

      //LAB_80025690
      v0 = 0x800c_0000L;
      v0 = v0 - 0x20c8L;
      s0 = s2 << 5;
      s0 = s0 + s2;
      s0 = s0 << 2;
      s0 = s0 + v0;
      v0 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
      MEMORY.ref(4, s0).offset(0x8L).oru(0x1000L);
      MEMORY.ref(4, s0).offset(0x58L).setu(addToLinkedListHead(v0 * (MEMORY.ref(2, s0).offset(0x1eL).getSigned() + 0x1L) * 0x8L));
      FUN_8002a2b4(s2);
      a1 = MEMORY.ref(2, s0).offset(0x14L).getSigned();
      a2 = MEMORY.ref(2, s0).offset(0x16L).getSigned();
      FUN_80027d74(s2, a1, a2);
    }

    //LAB_800256f0
    return 0;
  }

  @Method(0x800257e0L)
  public static void FUN_800257e0(final int a0) {
    if(_800bdf38.get(a0)._00.get() != 0) {
      removeFromLinkedList(_800bdf38.get(a0).ptr_58.get());
    }

    //LAB_80025824
    final Struct4c struct = _800be358.get(a0);

    struct._00.set(0x1L);
    struct._06.set((short)0);
    struct._08.set(0);
    struct._0c.set(0xeL);
    struct._10.set(0);
    struct._1c.set(0);
    struct._1e.set(0);
    struct._20.set((short)0x1000);
    struct._22.set((short)0x1000);

    //LAB_80025880
    for(int i = 0; i < 10; i++) {
      struct._24.get(i).set(0);
    }
  }

  @Method(0x800258a8L)
  public static void FUN_800258a8(final long a0) {
    final long a0_0 = _800bdf38.get((int)a0).getAddress(); //TODO
    MEMORY.ref(4, a0_0).offset(0x0cL).setu(0xdL);
    MEMORY.ref(2, a0_0).offset(0x20L).setu(0x1000L);
    MEMORY.ref(2, a0_0).offset(0x22L).setu(0x1000L);
    MEMORY.ref(4, a0_0).offset(0x00L).setu(0x1L);
    MEMORY.ref(2, a0_0).offset(0x3eL).setu(0x1L);
    MEMORY.ref(2, a0_0).offset(0x2aL).setu(0x2L);
    MEMORY.ref(4, a0_0).offset(0x08L).setu(0);
    MEMORY.ref(4, a0_0).offset(0x10L).setu(0);
    MEMORY.ref(2, a0_0).offset(0x28L).setu(0);
    MEMORY.ref(2, a0_0).offset(0x2cL).setu(0);
    MEMORY.ref(4, a0_0).offset(0x30L).setu(0);
    MEMORY.ref(2, a0_0).offset(0x34L).setu(0);
    MEMORY.ref(2, a0_0).offset(0x36L).setu(0);
    MEMORY.ref(2, a0_0).offset(0x38L).setu(0);
    MEMORY.ref(2, a0_0).offset(0x3aL).setu(0);
    MEMORY.ref(2, a0_0).offset(0x3cL).setu(0);
    MEMORY.ref(2, a0_0).offset(0x40L).setu(0);
    MEMORY.ref(2, a0_0).offset(0x42L).setu(0);
    MEMORY.ref(2, a0_0).offset(0x44L).setu(0);

    final long v0 = _800be358.get((int)a0).getAddress(); //TODO
    MEMORY.ref(2, a0_0).offset(0x14L).setu(MEMORY.ref(2, v0).offset(0x14L).get());
    MEMORY.ref(2, a0_0).offset(0x16L).setu(MEMORY.ref(2, v0).offset(0x16L).get());
    MEMORY.ref(2, a0_0).offset(0x1cL).setu(MEMORY.ref(2, v0).offset(0x18L).get() - 0x1L);
    MEMORY.ref(2, a0_0).offset(0x1eL).setu(MEMORY.ref(2, v0).offset(0x1aL).get() - 0x1L);
    MEMORY.ref(2, a0_0).offset(0x18L).setu(MEMORY.ref(2, a0_0).offset(0x14L).get() - MEMORY.ref(2, a0_0).offset(0x1cL).getSigned() * 9 / 2);
    MEMORY.ref(2, a0_0).offset(0x1aL).setu(MEMORY.ref(2, a0_0).offset(0x16L).get() - MEMORY.ref(2, a0_0).offset(0x1eL).getSigned() * 6);

    //LAB_800259b4
    for(int i = 0; i < 8; i++) {
      MEMORY.ref(2, a0_0).offset(0x46L).offset(i * 0x2L).setu(0);
    }

    //LAB_800259e4
    for(int i = 0; i < 10; i++) {
      MEMORY.ref(4, a0_0).offset(0x5cL).offset(i * 0x4L).setu(0);
    }
  }

  @Method(0x80025a04L)
  public static void FUN_80025a04(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long lo;

    v1 = 0x800c_0000L;
    v1 = v1 - 0x1ca8L;
    v0 = a0 << 2;
    v0 = v0 + a0;
    v0 = v0 << 2;
    v0 = v0 - a0;
    v0 = v0 << 2;
    v0 = v0 + v1;
    v0 = MEMORY.ref(4, v0).offset(0x0L).get();
    switch((int)v0) {
      case 1 -> {
        v0 = 0x800c_0000L;
        v0 = v0 - 0x1ca8L;
        v1 = a0 << 2;
        v1 = v1 + a0;
        v1 = v1 << 2;
        v1 = v1 - a0;
        v1 = v1 << 2;
        a2 = v1 + v0;
        a1 = MEMORY.ref(2, a2).offset(0x4L).getSigned();
        v0 = 0x1L;
        if(a1 != v0) {
          v0 = 0x2L;
          if((int)a1 < 0x2L) {
            if(a1 == 0) {
              //LAB_80025ab8
              v0 = 0x4L;
              MEMORY.ref(4, a2).offset(0x0L).setu(v0);
              v0 = MEMORY.ref(4, a2).offset(0x8L).get();
              v1 = 0x8000_0000L;
              v0 = v0 ^ v1;
              MEMORY.ref(4, a2).offset(0x8L).setu(v0);
              break;
            }
          } else {
            //LAB_80025aa8
            if(a1 == v0) {
              //LAB_80025ad4
              v0 = 0x8008_0000L;
              v1 = MEMORY.ref(4, v0).offset(-0x5c48L).get();
              v0 = 0x3cL;
              lo = (int)v0 / (int)v1;
              a0 = lo;
              v0 = MEMORY.ref(4, a2).offset(0x8L).get();
              MEMORY.ref(4, a2).offset(0x10L).setu(0);
              v0 = v0 | 0x1L;
              MEMORY.ref(4, a2).offset(0x8L).setu(v0);
              if((int)a0 < 0) {
                a0 = a0 + 0x3L;
              }

              //LAB_80025b00
              v1 = MEMORY.ref(4, a2).offset(0x8L).get();
              v0 = (int)a0 >> 2;
              MEMORY.ref(4, a2).offset(0x24L).setu(v0);
              v1 = v1 & 0x2L;
              if(v1 == 0) {
                MEMORY.ref(4, a2).offset(0x0L).setu(a1);
                break;
              }
              MEMORY.ref(4, a2).offset(0x0L).setu(a1);
              a0 = MEMORY.ref(4, a2).offset(0x28L).get();
              v1 = MEMORY.ref(4, a2).offset(0x38L).get();

              a0 = a0 - v1;
              lo = (int)a0 / (int)v0;
              a0 = lo;
              a1 = MEMORY.ref(4, a2).offset(0x3cL).get();
              v0 = MEMORY.ref(4, a2).offset(0x2cL).get();
              v1 = MEMORY.ref(4, a2).offset(0x24L).get();
              v0 = v0 - a1;
              lo = (int)v0 / (int)v1;
              v0 = lo;
              MEMORY.ref(4, a2).offset(0x30L).setu(a0);
              MEMORY.ref(4, a2).offset(0x34L).setu(v0);
              break;
            }
          }
        }

        //LAB_80025b54
        v1 = 0x800c_0000L;
        v1 = v1 - 0x1ca8L;

        //LAB_80025b5c
        v0 = a0 << 2;
        v0 = v0 + a0;
        v0 = v0 << 2;
        v0 = v0 - a0;
        v0 = v0 << 2;
        a0 = v0 + v1;
        v1 = MEMORY.ref(2, a0).offset(0x18L).getSigned();
        v0 = 0x5L;
        MEMORY.ref(4, a0).offset(0x0L).setu(v0);
        v0 = v1 << 3;
        v0 = v0 + v1;
        v1 = v0 >>> 31;
        v0 = v0 + v1;
        v1 = MEMORY.ref(2, a0).offset(0x1aL).getSigned();
        v0 = (int)v0 >> 1;
        MEMORY.ref(2, a0).offset(0x1cL).setu(v0);
        v0 = v1 << 1;
        v0 = v0 + v1;
        v1 = MEMORY.ref(4, a0).offset(0x8L).get();
        v0 = v0 << 1;
        v1 = v1 & 0x4L;
        MEMORY.ref(2, a0).offset(0x1eL).setu(v0);
        if(v1 != 0) {
          v0 = 0x6L;
          MEMORY.ref(4, a0).offset(0x0L).setu(v0);
        }

        //LAB_80025bc0
        v0 = MEMORY.ref(4, a0).offset(0x8L).get();
        v1 = 0x8000_0000L;
        v0 = v0 | v1;
        MEMORY.ref(4, a0).offset(0x8L).setu(v0);
      }
      case 2 -> {
        v1 = 0x800c_0000L;
        v1 = v1 - 0x1ca8L;
        v0 = a0 << 2;
        v0 = v0 + a0;
        v0 = v0 << 2;
        v0 = v0 - a0;
        v0 = v0 << 2;
        a2 = v0 + v1;
        v0 = MEMORY.ref(4, a2).offset(0x8L).get();
        v1 = 0x8000_0000L;
        v0 = v0 | v1;
        v1 = MEMORY.ref(2, a2).offset(0x4L).getSigned();
        MEMORY.ref(4, a2).offset(0x8L).setu(v0);
        v0 = 0x2L;
        if(v1 != v0) {
          v1 = 0x5L;
        } else {
          v1 = 0x5L;
          v0 = MEMORY.ref(4, a2).offset(0x10L).get();
          v1 = MEMORY.ref(4, a2).offset(0x24L).get();
          v0 = v0 << 12;
          lo = (int)v0 / (int)v1;
          v0 = lo;
          a0 = MEMORY.ref(4, a2).offset(0x10L).get();
          v1 = MEMORY.ref(4, a2).offset(0x24L).get();
          a0 = a0 << 12;
          lo = (int)a0 / (int)v1;
          a0 = lo;
          v1 = MEMORY.ref(2, a2).offset(0x18L).getSigned();
          MEMORY.ref(2, a2).offset(0x20L).setu(v0);
          v0 = v1 << 3;
          v0 = v0 + v1;
          v1 = v0 >>> 31;
          v0 = v0 + v1;
          v1 = MEMORY.ref(2, a2).offset(0x20L).getSigned();
          v0 = (int)v0 >> 1;
          lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
          a1 = lo;
          MEMORY.ref(2, a2).offset(0x22L).setu(a0);
          if((int)a1 < 0) {
            a1 = a1 + 0xfffL;
          }

          //LAB_80025c70
          v1 = MEMORY.ref(2, a2).offset(0x22L).getSigned();

          v0 = v1 << 1;
          v0 = v0 + v1;
          v1 = MEMORY.ref(2, a2).offset(0x1aL).getSigned();
          v0 = v0 << 1;
          lo = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
          v0 = (int)a1 >> 12;
          v1 = lo;
          MEMORY.ref(2, a2).offset(0x1cL).setu(v0);
          if((int)v1 < 0) {
            v1 = v1 + 0xfffL;
          }

          //LAB_80025ca0
          v0 = (int)v1 >> 12;
          MEMORY.ref(2, a2).offset(0x1eL).setu(v0);
          v0 = MEMORY.ref(4, a2).offset(0x10L).get();
          v1 = MEMORY.ref(4, a2).offset(0x8L).get();
          v0 = v0 + 0x1L;
          v1 = v1 & 0x2L;
          MEMORY.ref(4, a2).offset(0x10L).setu(v0);
          if(v1 != 0) {
            v0 = MEMORY.ref(4, a2).offset(0x28L).get();
            a0 = MEMORY.ref(4, a2).offset(0x30L).get();
            v1 = MEMORY.ref(4, a2).offset(0x2cL).get();
            a1 = MEMORY.ref(4, a2).offset(0x34L).get();
            v0 = v0 - a0;
            MEMORY.ref(4, a2).offset(0x28L).setu(v0);
            v0 = MEMORY.ref(2, a2).offset(0x28L).get();
            v1 = v1 - a1;
            MEMORY.ref(4, a2).offset(0x2cL).setu(v1);
            v1 = MEMORY.ref(2, a2).offset(0x2cL).get();
            MEMORY.ref(2, a2).offset(0x14L).setu(v0);
            MEMORY.ref(2, a2).offset(0x16L).setu(v1);
          }

          //LAB_80025cf0
          v0 = MEMORY.ref(4, a2).offset(0x10L).get();
          v1 = MEMORY.ref(4, a2).offset(0x24L).get();

          if((int)v0 < (int)v1) {
            break;
          }
          v0 = 0x5L;
          MEMORY.ref(4, a2).offset(0x0L).setu(v0);
          v0 = MEMORY.ref(4, a2).offset(0x8L).get();
          v1 = MEMORY.ref(2, a2).offset(0x18L).getSigned();
          v0 = v0 ^ 0x1L;
          MEMORY.ref(4, a2).offset(0x8L).setu(v0);
          v0 = v1 << 3;
          v0 = v0 + v1;
          v1 = v0 >>> 31;
          v0 = v0 + v1;
          v1 = MEMORY.ref(2, a2).offset(0x1aL).getSigned();
          v0 = (int)v0 >> 1;
          MEMORY.ref(2, a2).offset(0x1cL).setu(v0);
          v0 = v1 << 1;
          v0 = v0 + v1;
          v1 = MEMORY.ref(4, a2).offset(0x8L).get();
          v0 = v0 << 1;
          v1 = v1 & 0x4L;
          MEMORY.ref(2, a2).offset(0x1eL).setu(v0);
          if(v1 != 0) {
            v0 = 0x6L;
            MEMORY.ref(4, a2).offset(0x0L).setu(v0);
          }

          //LAB_80025d5c
          v0 = MEMORY.ref(4, a2).offset(0x8L).get();

          v0 = v0 & 0x2L;
          if(v0 == 0) {
            break;
          }

          v0 = MEMORY.ref(2, a2).offset(0x38L).get();
          v1 = MEMORY.ref(2, a2).offset(0x3cL).get();
          MEMORY.ref(2, a2).offset(0x14L).setu(v0);
          MEMORY.ref(2, a2).offset(0x16L).setu(v1);
          break;
        }

        //LAB_80025d84
        v0 = MEMORY.ref(4, a2).offset(0x8L).get();
        v0 = v0 & 0x4L;
        if(v0 == 0) {
          MEMORY.ref(4, a2).offset(0x0L).setu(v1);
          break;
        }
        MEMORY.ref(4, a2).offset(0x0L).setu(v1);
        v0 = 0x6L;
        MEMORY.ref(4, a2).offset(0x0L).setu(v0);
      }
      case 3 -> {
        v1 = 0x800c_0000L;
        v1 = v1 - 0x1ca8L;
        v0 = a0 << 2;
        v0 = v0 + a0;
        v0 = v0 << 2;
        v0 = v0 - a0;
        v0 = v0 << 2;
        a1 = v0 + v1;
        v1 = MEMORY.ref(2, a1).offset(0x4L).getSigned();
        v0 = 0x2L;
        if(v1 == v0) {
          v0 = MEMORY.ref(4, a1).offset(0x10L).get();
          v1 = MEMORY.ref(4, a1).offset(0x24L).get();
          v0 = v0 << 12;
          lo = (int)v0 / (int)v1;
          v0 = lo;
          a0 = MEMORY.ref(4, a1).offset(0x10L).get();
          v1 = MEMORY.ref(4, a1).offset(0x24L).get();
          a0 = a0 << 12;
          lo = (int)a0 / (int)v1;
          a0 = lo;
          v1 = MEMORY.ref(2, a1).offset(0x18L).getSigned();
          MEMORY.ref(2, a1).offset(0x20L).setu(v0);
          v0 = v1 << 3;
          v0 = v0 + v1;
          v1 = v0 >>> 31;
          v0 = v0 + v1;
          v1 = MEMORY.ref(2, a1).offset(0x20L).getSigned();
          v0 = (int)v0 >> 1;
          lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
          a2 = lo;
          MEMORY.ref(2, a1).offset(0x22L).setu(a0);
          if((int)a2 < 0) {
            a2 = a2 + 0xfffL;
          }

          //LAB_80025e30
          v1 = MEMORY.ref(2, a1).offset(0x22L).getSigned();

          v0 = v1 << 1;
          v0 = v0 + v1;
          v1 = MEMORY.ref(2, a1).offset(0x1aL).getSigned();
          v0 = v0 << 1;
          lo = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
          v0 = (int)a2 >> 12;
          v1 = lo;
          MEMORY.ref(2, a1).offset(0x1cL).setu(v0);
          if((int)v1 < 0) {
            v1 = v1 + 0xfffL;
          }

          //LAB_80025e60
          v0 = MEMORY.ref(4, a1).offset(0x10L).get();
          v1 = (int)v1 >> 12;
          MEMORY.ref(2, a1).offset(0x1eL).setu(v1);
          v0 = v0 - 0x1L;
          if((int)v0 > 0) {
            MEMORY.ref(4, a1).offset(0x10L).setu(v0);
            break;
          }
          MEMORY.ref(4, a1).offset(0x10L).setu(v0);
          v0 = MEMORY.ref(4, a1).offset(0x8L).get();
          MEMORY.ref(2, a1).offset(0x1eL).setu(0);
          MEMORY.ref(2, a1).offset(0x1cL).setu(0);
          MEMORY.ref(4, a1).offset(0x0L).setu(0);
          v0 = v0 ^ 0x1L;
          MEMORY.ref(4, a1).offset(0x8L).setu(v0);
          break;
        }

        //LAB_80025e94
        MEMORY.ref(4, a1).offset(0x0L).setu(0);
      }
      case 4, 5 -> {
        v0 = 0x800c_0000L;
        v0 = v0 - 0x20c8L;
        v1 = a0 << 5;
        v1 = v1 + a0;
        v1 = v1 << 2;
        v1 = v1 + v0;
        v0 = MEMORY.ref(4, v1).offset(0x0L).get();
        if(v0 != 0) {
          v1 = 0x800c_0000L;
          break;
        }
        v1 = 0x800c_0000L;
        v1 = v1 - 0x1ca8L;
        v0 = a0 << 2;
        v0 = v0 + a0;
        v0 = v0 << 2;
        v0 = v0 - a0;
        v0 = v0 << 2;
        a1 = v0 + v1;
        v1 = MEMORY.ref(2, a1).offset(0x4L).getSigned();
        v0 = 0x2L;
        if(v1 == v0) {
          v0 = 0x8008_0000L;
          v1 = MEMORY.ref(4, v0).offset(-0x5c48L).get();
          v0 = 0x3cL;
          lo = (int)v0 / (int)v1;
          v1 = lo;
          v0 = MEMORY.ref(4, a1).offset(0x8L).get();

          v0 = v0 | 0x1L;
          MEMORY.ref(4, a1).offset(0x8L).setu(v0);
          if((int)v1 < 0) {
            v1 = v1 + 0x3L;
          }

          //LAB_80025f18
          v0 = (int)v1 >> 2;
          MEMORY.ref(4, a1).offset(0x24L).setu(v0);
          MEMORY.ref(4, a1).offset(0x10L).setu(v0);
          v0 = 0x3L;
          MEMORY.ref(4, a1).offset(0x0L).setu(v0);
        } else {
          //LAB_80025f30
          MEMORY.ref(4, a1).offset(0x0L).setu(0);
        }

        //LAB_80025f34
        a1 = 0;
        FUN_80029920((int)a0, a1);
      }
    }

    //LAB_80025f3c
  }

  @Method(0x80025f4cL)
  public static void FUN_80025f4c(long a0) {
    long v0;

    //LAB_80025f7c
    final byte[] sp0x10 = MEMORY.getBytes(_80010868.getAddress(), 0x48);
    final long s1 = _800be358.get((int)a0).getAddress();
    if(MEMORY.ref(2, s1).offset(0x4L).getSigned() != 0) {
      if(MEMORY.ref(4, s1).offset(0x0L).get() != 0x1L) {
        final long s0 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x24L);
        FUN_8003b5b0(s0);
        gpuLinkedListSetCommandTransparency(s0, true);
        v0 = MEMORY.ref(2, s1).offset(0x14L).get() - MEMORY.ref(2, s1).offset(0x1cL).get() - centreScreenX_1f8003dc.get();
        MEMORY.ref(2, s0).offset(0x18L).setu(v0);
        MEMORY.ref(2, s0).offset(0x8L).setu(v0);
        v0 = MEMORY.ref(2, s1).offset(0x14L).get() + MEMORY.ref(2, s1).offset(0x1cL).get() - centreScreenX_1f8003dc.get();
        MEMORY.ref(2, s0).offset(0x20L).setu(v0);
        MEMORY.ref(2, s0).offset(0x10L).setu(v0);
        v0 = MEMORY.ref(2, s1).offset(0x16L).get() - MEMORY.ref(2, s1).offset(0x1eL).get() - centreScreenY_1f8003de.get();
        MEMORY.ref(2, s0).offset(0x12L).setu(v0);
        MEMORY.ref(2, s0).offset(0xaL).setu(v0);
        final long v1 = MEMORY.ref(2, s1).offset(0x16L).get() + MEMORY.ref(2, s1).offset(0x1eL).get() - centreScreenY_1f8003de.get();
        MEMORY.ref(2, s0).offset(0x22L).setu(v1);
        MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
        v0 = _800be5c4.getSigned() * 0xcL;
        MEMORY.ref(1, s0).offset(0x14L).setu(sp0x10[(int)v0]);
        MEMORY.ref(1, s0).offset(0xcL).setu(sp0x10[(int)v0]);
        MEMORY.ref(1, s0).offset(0x15L).setu(sp0x10[(int)v0 + 4]);
        MEMORY.ref(1, s0).offset(0xdL).setu(sp0x10[(int)v0 + 4]);
        MEMORY.ref(1, s0).offset(0x1cL).setu(0);
        MEMORY.ref(1, s0).offset(0x16L).setu(sp0x10[(int)v0 + 8]);
        MEMORY.ref(1, s0).offset(0xeL).setu(sp0x10[(int)v0 + 8]);
        MEMORY.ref(1, s0).offset(0x4L).setu(0);
        MEMORY.ref(1, s0).offset(0x1dL).setu(0);
        MEMORY.ref(1, s0).offset(0x5L).setu(0);
        MEMORY.ref(1, s0).offset(0x1eL).setu(0);
        MEMORY.ref(1, s0).offset(0x6L).setu(0);

        if(MEMORY.ref(2, s1).offset(0x6L).getSigned() != 0) {
          FUN_800261c0(a0, s0);
        }

        //LAB_80026144
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + MEMORY.ref(4, s1).offset(0xcL).get() * 0x4L, s0);
        final long a1 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x8L);
        MEMORY.ref(1, a1).offset(0x3L).setu(0x1L);
        MEMORY.ref(4, a1).offset(0x4L).setu(0xe100_0200L | (_800bb112.get() | 0xdL) & 0x9ffL);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + MEMORY.ref(4, s1).offset(0xcL).get() * 0x4L, a1);
      }
    }

    //LAB_800261a0
  }

  @Method(0x800261c0L)
  public static void FUN_800261c0(long a0, long a1) {
    long v0;
    long v1;
    long s0;
    long s1;
    long s2;
    long s3;

    long a2 = a0;

    //LAB_80026248
    //LAB_8002624c
    final byte[] sp0x20 = MEMORY.getBytes(_800108b0.getAddress(), 0x60);

    //LAB_8002627c
    final short[] sp0x10 = new short[4];
    final short[] sp0x18 = new short[4];
    v0 = MEMORY.ref(2, a1).offset(0x08L).get() + 0x4L;
    sp0x10[2] = (short)v0;
    sp0x10[0] = (short)v0;
    v0 = MEMORY.ref(2, a1).offset(0x10L).get() - 0x4L;
    sp0x10[3] = (short)v0;
    sp0x10[1] = (short)v0;
    v1 = MEMORY.ref(2, a1).offset(0x0aL).get() + 0x5L;
    sp0x18[1] = (short)v1;
    sp0x18[0] = (short)v1;
    v0 = MEMORY.ref(2, a1).offset(0x1aL).get() - 0x5L;
    sp0x18[3] = (short)v0;
    sp0x18[2] = (short)v0;

    s2 = _800be358.get((int)a2).getAddress();
    s1 = 0;

    //LAB_800262e4
    for(s3 = 0; s3 < 8; s3++) {
      s0 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.setu(s0 + 0x28L);
      FUN_8003b590(s0);
      gpuLinkedListSetCommandTransparency(s0, false);
      MEMORY.ref(1, s0).offset(0x6L).setu(0x80L);
      MEMORY.ref(1, s0).offset(0x5L).setu(0x80L);
      MEMORY.ref(1, s0).offset(0x4L).setu(0x80L);
      a0 = MathHelper.get(sp0x20, (int)s1 + 0x8, 2);
      a1 = MathHelper.get(sp0x20, (int)s1 + 0xa, 2);
      if((MEMORY.ref(4, s2).offset(0x8L).get() & 0x1L) != 0) {
        //LAB_80026358
        //LAB_80026378
        a0 = MEMORY.ref(2, s2).offset(0x20L).getSigned() * (short)a0 / 0x1000L;
        a1 = MEMORY.ref(2, s2).offset(0x22L).getSigned() * (short)a1 / 0x1000L;
      }

      //LAB_8002637c
      v0 = sp0x10[(int)MathHelper.get(sp0x20, (int)s1, 2)] - a0;
      MEMORY.ref(2, s0).offset(0x18L).setu(v0);
      MEMORY.ref(2, s0).offset(0x8L).setu(v0);
      v0 = sp0x10[(int)MathHelper.get(sp0x20, (int)s1 + 0x2, 2)] + a0;
      MEMORY.ref(2, s0).offset(0x20L).setu(v0);
      MEMORY.ref(2, s0).offset(0x10L).setu(v0);
      v0 = sp0x18[(int)MathHelper.get(sp0x20, (int)s1, 2)] - a1;
      MEMORY.ref(2, s0).offset(0x12L).setu(v0);
      MEMORY.ref(2, s0).offset(0xaL).setu(v0);
      v0 = sp0x18[(int)MathHelper.get(sp0x20, (int)s1 + 0x2, 2)] + a1;
      MEMORY.ref(2, s0).offset(0x22L).setu(v0);
      MEMORY.ref(2, s0).offset(0x1aL).setu(v0);
      v0 = sp0x20[(int)s1 + 0x4];
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);
      v0 = sp0x20[(int)s1 + 0x4] + 0x10L;
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);
      v0 = sp0x20[(int)s1 + 0x6];
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
      MEMORY.ref(2, s0).offset(0xeL).setu(0x7934L);
      v1 = sp0x20[(int)s1 + 0x6] + 0x10L;
      MEMORY.ref(1, s0).offset(0x25L).setu(v1);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v1);
      MEMORY.ref(2, s0).offset(0x16L).setu(GetTPage(0, 0, 896L, 256L));
      insertElementIntoLinkedList(tags_1f8003d0.getPointer() + MEMORY.ref(4, s2).offset(0xcL).get() * 0x4L, s0);
      s1 = s1 + 0xcL;
    }
  }

  /** I think this method handles textboxes */
  @Method(0x800264b0L)
  public static void FUN_800264b0(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long t0;
    long s0;
    long s1;
    long s2;
    long s3;

    final Struct4c struct4c = _800be358.get((int)a0);
    final Struct84 struct84 = _800bdf38.get((int)a0);

    s2 = a0;
    s0 = _800bdf38.get((int)s2).getAddress();
    v1 = struct84._00.get();
    if(v1 == 0xcL) {
      //LAB_80026af0
      if(struct4c._00.get() == 0) {
        removeFromLinkedList(struct84.ptr_58.get());
        struct84._00.set(0);
      }
    } else {
      if((int)v1 >= 0xdL) {
        //LAB_8002659c
        s3 = 0x12L;
        if(v1 == s3) {
          //LAB_80026d94
          a1 = MEMORY.ref(2, s0).offset(0x60L).getSigned();
          FUN_80029140(s2, a1);

          if((joypadPress_8007a398.get() & 0x20L) != 0) {
            Scus94491BpeSegment.playSound(0, 2, 0, 0, (short)0, (short)0);
            removeFromLinkedList(struct84.ptr_58.get());
            struct84._00.set(0);
            struct84._6c.set(struct84._68.get());
          } else {
            //LAB_80026df0
            if((joypadInput_8007a39c.get() & 0x4000L) == 0) {
              //LAB_80026ee8
              if((joypadInput_8007a39c.get() & 0x1000L) != 0) {
                if((struct84._08.get() & 0x100L) == 0 || struct84._68.get() != 0) {
                  //LAB_80026f38
                  Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);

                  s3 = 0x3L;
                  if((int)struct84._60.get() > 0) {
                    struct84._60.sub(0x1L);
                    struct84._00.set(0x13L);
                    struct84._64.set(0x4L);
                    struct84._68.sub(0x1);
                  } else {
                    //LAB_80026f88
                    if((struct84._08.get() & 0x2L) != 0) {
                      v1 = struct84._3a.get();

                      // TODO not sure about this block of code
                      if(v1 == 0x1L) {
                        s3 = 0x1L;
                      } else {
                        if(v1 == 0) {
                          //LAB_80026fbc
                          s3 = 0x2L;
                        }

                        //LAB_80026fc0
                        struct84._3a.set((short)0);
                        struct84._08.xor(0x2L);
                      }

                      //LAB_80026fe8
                      struct84._3a.sub((short)1);
                    }

                    //LAB_80027014
                    struct84._68.sub(1);

                    if(struct84._68.get() < 0) {
                      struct84._68.set(0);
                    } else {
                      //LAB_80027044
                      struct84._2c.set((short)12);
                      FUN_800280d4(s2);

                      a2 = struct84.ptr_24.get();

                      //LAB_80027068
                      s1 = 0;
                      do {
                        v0 = MEMORY.ref(2, a2).offset((struct84._30.get() - 1) * 0x2L).get() >>> 8;

                        if(v0 == 0xa1L) {
                          s1++;
                        }

                        //LAB_80027090
                        if(s1 == struct84._1e.get() + s3) {
                          break;
                        }

                        struct84._30.sub(0x1L);
                      } while((int)struct84._30.get() > 0);

                      //LAB_800270b0
                      struct84._34.set((short)0);
                      struct84._36.set((short)0);
                      struct84._08.or(0x80L);

                      //LAB_800270dc
                      do {
                        FUN_800274f0(s2);
                      } while(struct84._36.get() == 0 && struct84._00.get() != 0x5L);

                      //LAB_80027104
                      struct84._00.set(0x15L);
                      struct84._08.xor(0x80L);
                    }
                  }
                }
              }
            }
            struct84._00.set(0x13L);
            struct84._64.set(0x4L);
            struct84._60.add(0x1L);
            struct84._68.add(1);
            if((struct84._08.get() & 0x100L) == 0 || struct84._36.get() + 0x1L != struct84._68.get()) {
              //LAB_80026e68
              //LAB_80026e6c
              if((int)struct84._60.get() < struct84._1e.get()) {
                //LAB_80026ed0
                Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);

                //LAB_80026ee8
                if((joypadInput_8007a39c.get() & 0x1000L) != 0) {
                  if((struct84._08.get() & 0x100L) == 0 || struct84._68.get() != 0) {
                    //LAB_80026f38
                    Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);

                    s3 = 0x3L;
                    if((int)struct84._60.get() > 0) {
                      struct84._60.sub(0x1L);
                      struct84._00.set(0x13L);
                      struct84._64.set(0x4L);
                      struct84._68.sub(0x1);
                    } else {
                      //LAB_80026f88
                      if((struct84._08.get() & 0x2L) != 0) {
                        v1 = struct84._3a.get();

                        // TODO not sure about this block of code
                        if(v1 == 0x1L) {
                          s3 = 0x1L;
                        } else {
                          if(v1 == 0) {
                            //LAB_80026fbc
                            s3 = 0x2L;
                          }

                          //LAB_80026fc0
                          struct84._3a.set((short)0);
                          struct84._08.xor(0x2L);
                        }

                        //LAB_80026fe8
                        struct84._3a.sub((short)1);
                      }

                      //LAB_80027014
                      struct84._68.sub(1);

                      if(struct84._68.get() < 0) {
                        struct84._68.set(0);
                      } else {
                        //LAB_80027044
                        struct84._2c.set((short)12);
                        FUN_800280d4(s2);

                        a2 = struct84.ptr_24.get();

                        //LAB_80027068
                        s1 = 0;
                        do {
                          v0 = MEMORY.ref(2, a2).offset((struct84._30.get() - 1) * 0x2L).get() >>> 8;

                          if(v0 == 0xa1L) {
                            s1++;
                          }

                          //LAB_80027090
                          if(s1 == struct84._1e.get() + s3) {
                            break;
                          }

                          struct84._30.sub(0x1L);
                        } while((int)struct84._30.get() > 0);

                        //LAB_800270b0
                        struct84._34.set((short)0);
                        struct84._36.set((short)0);
                        struct84._08.or(0x80L);

                        //LAB_800270dc
                        do {
                          FUN_800274f0(s2);
                        } while(struct84._36.get() == 0 && struct84._00.get() != 0x5L);

                        //LAB_80027104
                        struct84._00.set(0x15L);
                        struct84._08.xor(0x80L);
                      }
                    }
                  }
                }
              } else {
                struct84._60.set(_800bdf38.get((int)s2)._1e.get() - 0x1L);
                struct84._00.set(0x14L);
                struct84._2c.set((short)0);

                if(struct84._3a.get() == 0x1L) {
                  struct84._00.set(0x12L);
                  struct84._68.sub(0x1);
                }
              }
            } else {
              struct84._00.set(s3);
              struct84._68.sub(0x1);
              struct84._60.sub(0x1L);
            }
          }
        } else if((int)v1 < 0x13L) {
          if(v1 == 0xfL) {
            //LAB_80026cb0
            if((struct84._08.get() & 0x20L) != 0) {
              struct84._00.set(0x10L);
            } else {
              //LAB_80026cd0
              if((joypadPress_8007a398.get() & 0x20L) != 0) {
                removeFromLinkedList(struct84.ptr_58.get());
                struct84._00.set(0);
                FUN_80029920((int)s2, 0);
              }
            }
          } else {
            s3 = 0xdL;
            if((int)v1 < 0x10L) {
              if(v1 == s3) {
                //LAB_80026b34
                struct84._08.or(0x8L);
                FUN_80029920((int)s2, 0x1L);

                //LAB_80026b4c
                do {
                  FUN_800274f0(s2);
                  v1 = struct84._00.get();
                  if(v1 == 0x5L) {
                    //LAB_80026b28
                    struct84._00.set(0xbL);
                    break;
                  }
                } while(v1 != 0xfL);

                //LAB_80026b6c
                if((struct84._08.get() & 0x20L) != 0) {
                  FUN_80029920((int)s2, 0);
                }

                //LAB_80026ba0
                if(struct84._3e.get() != 0) {
                  FUN_80029920((int)s2, 0);
                  struct84._5c.set(struct84._00.get());
                  struct84._00.set(0xeL);
                }

                //LAB_80026bc8
                if((struct84._08.get() & 0x800L) != 0) {
                  FUN_80029920((int)s2, 0);
                  struct84._00.set(0x17L);
                  struct84._64.set(0xaL);
                  struct84._78.set(0x16L);
                  struct84._68.set(struct84._72.get());
                  Scus94491BpeSegment.playSound(0, 4, 0, 0, (short)0, (short)0);
                }
              } else if(v1 == 0xeL) {
                //LAB_80026c18
                if((struct84._08.get() & 0x40L) == 0) {
                  MEMORY.ref(2, s0).offset(0x40L).subu(0x1L);
                  if(MEMORY.ref(2, s0).offset(0x40L).getSigned() <= 0) {
                    v1 = struct84._5c.get();
                    MEMORY.ref(2, s0).offset(0x40L).setu(MEMORY.ref(2, s0).offset(0x3eL).get());
                    if(v1 == 0xbL) {
                      //LAB_80026c70
                      FUN_8002a2b4(s2);
                      struct84._34.set((short)0);
                      struct84._36.set((short)0);
                      struct84._3a.set((short)0);
                      struct84._00.set(s3);
                      struct84._08.xor(0x1L);
                    } else if(v1 == 0xfL) {
                      //LAB_80026c98
                      //LAB_80026c9c
                      removeFromLinkedList(struct84.ptr_58.get());
                      struct84._00.set(0);
                    }
                  }
                }
              }
            } else {
              //LAB_800265d8
              if(v1 == 0x10L) {
                //LAB_80026cdc
                //LAB_80026ce8
                if((struct84._08.get() & 0x40L) != 0) {
                  removeFromLinkedList(struct84.ptr_58.get());
                  struct84._00.set(0);
                  FUN_80029920((int)s2, 0);
                }
              } else {
                if(v1 == 0x11L) {
                  //LAB_80026d20
                  MEMORY.ref(2, s0).offset(0x1eL).addu(0x1L);

                  //LAB_80026d30
                  do {
                    FUN_800274f0(s2);
                    v1 = struct84._00.get();
                    if(v1 == 0x5L) {
                      //LAB_80026d14
                      struct84._00.set(0x12L);
                      break;
                    }
                    if(v1 == 0xfL) {
                      struct84._00.set(0x12L);
                      struct84._3a.set((short)0);
                      struct84._08.or(0x102L);
                      break;
                    }
                  } while(true);

                  //LAB_80026d64
                  v1 = 0x800c_0000L;
                  v1 = v1 - 0x20c8L;
                  v0 = s2 << 5;
                  v0 = v0 + s2;
                  v0 = v0 << 2;
                  v0 = v0 + v1;
                  v1 = MEMORY.ref(2, v0).offset(0x1eL).get();
                  a0 = -0x1L;
                  MEMORY.ref(4, v0).offset(0x6cL).setu(a0);
                  v1 = v1 + a0;
                  MEMORY.ref(2, v0).offset(0x1eL).setu(v1);
                }
              }
            }
          }
        } else {
          //LAB_800265f4
          v0 = 0x15L;
          if(v1 == v0) {
            //LAB_8002727c
            v0 = MEMORY.ref(2, s0).offset(0x2cL).get();

            v0 = v0 - 0x4L;
            MEMORY.ref(2, s0).offset(0x2cL).setu(v0);
            v0 = v0 << 16;
            if((int)v0 <= 0) {
              v0 = MEMORY.ref(4, s0).offset(0x8L).get();
              MEMORY.ref(2, s0).offset(0x36L).setu(0);
              MEMORY.ref(2, s0).offset(0x2cL).setu(0);
              MEMORY.ref(4, s0).offset(0x0L).setu(s3);
              v0 = v0 | 0x4L;
              MEMORY.ref(4, s0).offset(0x8L).setu(v0);
            }

            //LAB_800272b0
            v1 = MEMORY.ref(4, s0).offset(0x8L).get();

            v0 = v1 & 0x4L;
            if(v0 != 0) {
              s1 = 0;
              a1 = s0;
              t0 = 0xa0L;
              a3 = 0xa1L;
              a2 = MEMORY.ref(4, a1).offset(0x24L).get();
              v0 = v1 ^ 0x4L;
              MEMORY.ref(4, a1).offset(0x8L).setu(v0);

              //LAB_800272dc
              do {
                a0 = MEMORY.ref(4, a1).offset(0x30L).get();

                v0 = a0 << 1;
                v0 = v0 + a2;
                v0 = MEMORY.ref(2, v0).offset(0x2L).get();

                v0 = v0 >>> 8;
                if(v0 == t0) {
                  //LAB_80027274
                  a0--;
                  break;
                }

                if(v0 == a3) {
                  s1 = s1 + 0x1L;
                }

                //LAB_8002730c
                v1 = MEMORY.ref(2, a1).offset(0x1eL).getSigned();
                v0 = a0 + 0x1L;
                MEMORY.ref(4, a1).offset(0x30L).setu(v0);
              } while(s1 != v1);

              v0 = a0 + 0x2L;

              //LAB_80027320
              MEMORY.ref(4, a1).offset(0x30L).setu(v0);
              v1 = 0x800c_0000L;
              v1 = v1 - 0x20c8L;
              v0 = s2 << 5;
              v0 = v0 + s2;
              v0 = v0 << 2;
              v0 = v0 + v1;
              a0 = MEMORY.ref(2, v0).offset(0x1eL).get();
              v1 = 0x12L;
              MEMORY.ref(2, v0).offset(0x34L).setu(0);
              MEMORY.ref(4, v0).offset(0x0L).setu(v1);
              MEMORY.ref(2, v0).offset(0x36L).setu(a0);
            }
          } else {
            if((int)v1 >= 0x16L) {
              //LAB_80026620
              v0 = 0x17L;
              if(v1 == v0) {
                //LAB_800274a4
                v0 = MEMORY.ref(4, s0).offset(0x64L).get();

                v0 = v0 - 0x1L;
                MEMORY.ref(4, s0).offset(0x64L).setu(v0);
                if(v0 == 0) {
                  v1 = MEMORY.ref(4, s0).offset(0x78L).get();
                  v0 = 0x4L;
                  MEMORY.ref(4, s0).offset(0x64L).setu(v0);
                  MEMORY.ref(4, s0).offset(0x0L).setu(v1);
                }
              } else {
                a0 = s2;
                if((int)v1 < (int)v0) {
                  //LAB_80027354
                  v0 = MEMORY.ref(4, s0).offset(0x70L).get();
                  a1 = MEMORY.ref(2, s0).offset(0x68L).getSigned();
                  s1 = (int)v0 >> 16;
                  s3 = v0 & 0xffffL;
                  FUN_80029140(a0, a1);
                  v0 = 0x8008_0000L;
                  v0 = MEMORY.ref(4, v0).offset(-0x5c68L).get();

                  v0 = v0 & 0x20L;
                  if(v0 != 0) {
                    Scus94491BpeSegment.playSound(0, 2, 0, 0, (short)0, (short)0);
                    a0 = MEMORY.ref(4, s0).offset(0x58L).get();
                    removeFromLinkedList(a0);
                    v0 = MEMORY.ref(4, s0).offset(0x68L).get();
                    MEMORY.ref(4, s0).offset(0x0L).setu(0);
                    v0 = v0 - s1;
                    MEMORY.ref(4, s0).offset(0x6cL).setu(v0);
                  } else {
                    //LAB_800273bc
                    v0 = 0x8008_0000L;
                    v0 = MEMORY.ref(4, v0).offset(-0x5c64L).get();

                    v0 = v0 & 0x1000L;
                    if(v0 != 0) {
                      v1 = 0x13L;
                      v0 = MEMORY.ref(4, s0).offset(0x68L).get();
                      MEMORY.ref(4, s0).offset(0x0L).setu(v1);
                      v1 = 0x4L;
                      MEMORY.ref(4, s0).offset(0x64L).setu(v1);
                      v0 = v0 - 0x1L;
                      MEMORY.ref(4, s0).offset(0x68L).setu(v0);

                      if((int)v0 < (int)s1) {
                        v0 = 0x16L;
                        MEMORY.ref(4, s0).offset(0x68L).setu(s1);
                        MEMORY.ref(4, s0).offset(0x0L).setu(v0);
                      } else {
                        //LAB_80027404
                        Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);
                      }
                    }

                    //LAB_80027420
                    v0 = 0x8008_0000L;
                    v0 = MEMORY.ref(4, v0).offset(-0x5c64L).get();

                    v0 = v0 & 0x4000L;
                    if(v0 != 0) {
                      v1 = 0x800c_0000L;
                      v1 = v1 - 0x20c8L;
                      v0 = s2 << 5;
                      v0 = v0 + s2;
                      v0 = v0 << 2;
                      a0 = v0 + v1;
                      v0 = MEMORY.ref(4, a0).offset(0x68L).get();
                      v1 = 0x13L;
                      MEMORY.ref(4, a0).offset(0x0L).setu(v1);
                      v1 = 0x4L;
                      MEMORY.ref(4, a0).offset(0x64L).setu(v1);
                      v0 = v0 + 0x1L;
                      MEMORY.ref(4, a0).offset(0x68L).setu(v0);
                      if((int)s3 >= (int)v0) {
                        v0 = 0x16L;

                        //LAB_80027480
                        a0 = 0;
                        a1 = 0x1L;
                        a2 = a0;
                        a3 = a0;

                        //LAB_80027490
                        Scus94491BpeSegment.playSound((int)a0, (int)a1, a2, a3, (short)0, (short)0);
                      } else {
                        v0 = 0x16L;
                        MEMORY.ref(4, a0).offset(0x68L).setu(s3);
                        MEMORY.ref(4, a0).offset(0x0L).setu(v0);
                      }
                    }
                  }
                }
              }
            } else {
              v0 = 0x13L;
              if(v1 == v0) {
                //LAB_8002711c
                a1 = MEMORY.ref(2, s0).offset(0x68L).getSigned();
                FUN_80029140(s2, a1);
                v0 = MEMORY.ref(4, s0).offset(0x64L).get();

                v0 = v0 - 0x1L;
                MEMORY.ref(4, s0).offset(0x64L).setu(v0);
                if(v0 == 0) {
                  v0 = MEMORY.ref(4, s0).offset(0x8L).get();

                  v0 = v0 & 0x800L;
                  MEMORY.ref(4, s0).offset(0x0L).setu(s3);
                  if(v0 != 0) {
                    v0 = 0x16L;
                    MEMORY.ref(4, s0).offset(0x0L).setu(v0);
                  }
                }
              } else {
                v0 = 0x14L;
                if(v1 == v0) {
                  //LAB_8002715c
                  v0 = MEMORY.ref(2, s0).offset(0x2cL).get();

                  v0 = v0 + 0x4L;
                  MEMORY.ref(2, s0).offset(0x2cL).setu(v0);
                  v0 = v0 << 16;
                  v0 = (int)v0 >> 16;
                  if((int)v0 >= 0xcL) {
                    FUN_80027eb4(s2);
                    v0 = MEMORY.ref(2, s0).offset(0x1eL).get();
                    v1 = MEMORY.ref(4, s0).offset(0x8L).get();
                    MEMORY.ref(2, s0).offset(0x36L).setu(v0);
                    v0 = MEMORY.ref(2, s0).offset(0x2cL).get();
                    v1 = v1 | 0x4L;
                    MEMORY.ref(4, s0).offset(0x8L).setu(v1);
                    v0 = v0 - 0xcL;
                    MEMORY.ref(2, s0).offset(0x2cL).setu(v0);
                  }

                  //LAB_800271a8
                  v1 = MEMORY.ref(4, s0).offset(0x8L).get();

                  v0 = v1 & 0x4L;
                  if(v0 != 0) {
                    v0 = v1 ^ 0x4L;
                    MEMORY.ref(4, s0).offset(0x8L).setu(v0);
                    v0 = v0 & 0x2L;
                    if(v0 == 0) {
                      v1 = 0x800c_0000L;

                      //LAB_8002720c
                      v1 = v1 - 0x20c8L;
                      v0 = s2 << 5;
                      v0 = v0 + s2;
                      v0 = v0 << 2;
                      s0 = v0 + v1;

                      //LAB_80027220
                      do {
                        FUN_800274f0(s2);

                        v1 = MEMORY.ref(4, s0).offset(0x0L).get();
                        if(v1 == 0xfL) {
                          v0 = MEMORY.ref(4, s0).offset(0x8L).get();
                          MEMORY.ref(2, s0).offset(0x3aL).setu(0);
                          v0 = v0 | 0x2L;
                          MEMORY.ref(4, s0).offset(0x8L).setu(v0);
                          break;
                        }
                      } while(v1 != 0x5L);
                    } else {
                      v0 = MEMORY.ref(2, s0).offset(0x3aL).get();
                      v1 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
                      v0 = v0 + 0x1L;
                      MEMORY.ref(2, s0).offset(0x3aL).setu(v0);
                      v0 = v0 << 16;
                      v0 = (int)v0 >> 16;
                      v1 = v1 + 0x1L;
                      if((int)v0 >= (int)v1) {
                        a0 = MEMORY.ref(4, s0).offset(0x58L).get();
                        removeFromLinkedList(a0);
                        MEMORY.ref(4, s0).offset(0x0L).setu(0);
                      }
                    }

                    //LAB_80027250
                    v0 = 0x800c_0000L;

                    //LAB_80027254
                    v0 = v0 - 0x20c8L;
                    v1 = s2 << 5;
                    v1 = v1 + s2;
                    v1 = v1 << 2;
                    v1 = v1 + v0;
                    v0 = 0x12L;
                    MEMORY.ref(4, v1).offset(0x0L).setu(v0);
                  }
                }
              }
            }
          }
        }
      } else {
        v0 = 0x6L;
        if(v1 == v0) {
          //LAB_800268dc
          v0 = 0x8008_0000L;
          v0 = MEMORY.ref(4, v0).offset(-0x5c68L).get();

          v0 = v0 & 0x20L;
          if(v0 != 0) {
            v0 = 0x4L;
            MEMORY.ref(4, s0).offset(0x0L).setu(v0);
          }
        } else {
          if((int)v1 >= 0x7L) {
            //LAB_80026554
            v0 = 0x9L;
            if(v1 == v0) {
              //LAB_800269f0
              FUN_80028828((int)s2);
            } else {
              if((int)v1 < 0xaL) {
                v0 = 0x7L;
                if(v1 == v0) {
                  //LAB_800268fc
                  v0 = MEMORY.ref(2, s0).offset(0x40L).get();
                  v1 = MEMORY.ref(2, s0).offset(0x3eL).getSigned();
                  v0 = v0 + 0x1L;
                  MEMORY.ref(2, s0).offset(0x40L).setu(v0);
                  v0 = v0 << 16;
                  v0 = (int)v0 >> 16;
                  if((int)v0 >= (int)v1) {
                    v0 = 0x4L;
                    MEMORY.ref(2, s0).offset(0x40L).setu(0);
                    MEMORY.ref(4, s0).offset(0x0L).setu(v0);
                  }

                  //LAB_80026928
                  if((MEMORY.ref(4, s0).offset(0x8L).get() & 0x20L) == 0) {
                    if((joypadInput_8007a39c.get() & 0x20L) != 0) {
                      s1 = 0;
                      s3 = 0;

                      //LAB_80026954
                      do {
                        FUN_800274f0(s2);

                        v1 = MEMORY.ref(4, s0).offset(0x0L).get();
                        v0 = v1 - 0x5L;
                        if(v0 < 0x2L || v1 == 0xfL || v1 == 0xbL || v1 == 0xdL) {
                          //LAB_8002698c
                          s3 = 0x1L;
                          break;
                        }

                        //LAB_80026994
                        s1++;
                      } while((int)s1 < 0x4L);

                      //LAB_800269a0
                      if(s3 == 0) {
                        v1 = _800bdf38.get((int)s2).getAddress();
                        MEMORY.ref(2, v1).offset(0x40L).setu(0);
                        MEMORY.ref(4, v1).offset(0x0L).setu(0x4L);
                      }
                    }
                  }
                } else {
                  v0 = 0x8L;
                  if(v1 == v0) {
                    //LAB_800269cc
                    v0 = MEMORY.ref(2, s0).offset(0x44L).getSigned();
                    v1 = MEMORY.ref(2, s0).offset(0x44L).get();
                    if((int)v0 > 0) {
                      v0 = v1 - 0x1L;

                      //LAB_800269e8
                      MEMORY.ref(2, s0).offset(0x44L).setu(v0);
                    } else {
                      v0 = 0x4L;

                      //LAB_800269e0
                      MEMORY.ref(4, s0).offset(0x0L).setu(v0);
                    }
                  }
                }
                //LAB_80026580
              } else if(v1 == 0xaL) {
                //LAB_80026a00
                FUN_800288a4(s2);

                if((MEMORY.ref(4, s0).offset(0x8L).get() & 0x4L) != 0) {
                  MEMORY.ref(4, s0).offset(0x8L).xoru(0x4L);
                  if((MEMORY.ref(4, s0).offset(0x8L).get() & 0x2L) == 0) {
                    //LAB_80026a5c
                    do {
                      FUN_800274f0(s2);
                      v1 = MEMORY.ref(4, s0).offset(0x0L).get();
                      if(v1 == 0x5L) {
                        break;
                      }
                      if(v1 == 0xfL) {
                        MEMORY.ref(2, s0).offset(0x3aL).setu(0);
                        MEMORY.ref(4, s0).offset(0x8L).oru(0x2L);
                        break;
                      }
                    } while(true);

                    //LAB_80026a8c
                    MEMORY.ref(4, s0).offset(0x0L).setu(0xaL);
                  } else {
                    v0 = MEMORY.ref(2, s0).offset(0x3aL).get();
                    v1 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
                    v0 = v0 + 0x1L;
                    MEMORY.ref(2, s0).offset(0x3aL).setu(v0);
                    v0 = v0 << 16;
                    v0 = (int)v0 >> 16;
                    v1 = v1 + 0x1L;
                    if((int)v0 >= (int)v1) {
                      a0 = MEMORY.ref(4, s0).offset(0x58L).get();
                      removeFromLinkedList(a0);
                      MEMORY.ref(4, s0).offset(0x0L).setu(0);
                    }
                  }
                }
              } else {
                v0 = 0xbL;
                if(v1 == v0) {
                  v0 = 0x8008_0000L;

                  //LAB_80026a98
                  v0 = MEMORY.ref(4, v0).offset(-0x5c68L).get();

                  v0 = v0 & 0x20L;
                  if(v0 != 0) {
                    FUN_80029920((int)s2, 0);
                    FUN_8002a2b4(s2);
                    v0 = MEMORY.ref(4, s0).offset(0x8L).get();
                    v1 = 0x4L;
                    MEMORY.ref(2, s0).offset(0x34L).setu(0);
                    MEMORY.ref(2, s0).offset(0x36L).setu(0);
                    MEMORY.ref(2, s0).offset(0x3aL).setu(0);
                    MEMORY.ref(4, s0).offset(0x0L).setu(v1);
                    v0 = v0 ^ 0x1L;
                    MEMORY.ref(4, s0).offset(0x8L).setu(v0);
                    v0 = v0 & 0x8L;
                    if(v0 != 0) {
                      v0 = 0xdL;
                      MEMORY.ref(4, s0).offset(0x0L).setu(v0);
                    }
                  }
                }
              }
            }
          } else if(v1 != 0x3L) {
            if((int)v1 >= 0x4L) {
              //LAB_80026538
              if(v1 == 0x4L) {
                //LAB_800267c4
                FUN_800274f0(s2);
              } else {
                if(v1 == 0x5L) {
                  //LAB_800267d4
                  a2 = MEMORY.ref(4, s0).offset(0x8L).get();

                  if((a2 & 0x1L) != 0) {
                    if((a2 & 0x200L) == 0) {
                      a0 = 0x1L;
                    } else {
                      a0 = 0x2L;
                    }

                    //LAB_800267f4
                    if(MEMORY.ref(2, s0).offset(0x3aL).getSigned() >= MEMORY.ref(2, s0).offset(0x1eL).getSigned() - a0) {
                      MEMORY.ref(4, s0).offset(0x8L).setu(a2 ^ 0x1L);
                      MEMORY.ref(2, s0).offset(0x3aL).setu(0);
                      FUN_80029920((int)s2, 0x1L);
                    } else {
                      //LAB_80026828
                      struct84._00.set(0x9L);
                      struct84._3a.incr();
                      FUN_80028828((int)s2);
                    }
                  } else {
                    //LAB_8002684c
                    v0 = a2 & 0x20L;
                    if(v0 != 0) {
                      v1 = 0x9L;
                      v0 = MEMORY.ref(4, s0).offset(0x8L).get();
                      MEMORY.ref(4, s0).offset(0x0L).setu(v1);
                      v0 = v0 | 0x1L;
                      MEMORY.ref(4, s0).offset(0x8L).setu(v0);
                    } else {
                      //LAB_8002686c
                      v0 = 0x8008_0000L;
                      v0 = MEMORY.ref(4, v0).offset(-0x5c68L).get();

                      v0 = v0 & 0x20L;
                      if(v0 != 0) {
                        FUN_80029920((int)s2, 0);
                        v1 = MEMORY.ref(2, s0).offset(0x4L).getSigned();
                        if(v1 == 0x1L || v1 == 0x4L) {
                          //LAB_800268b4
                          v0 = _800bdf38.get((int)s2).getAddress();
                          v1 = MEMORY.ref(4, v0).offset(0x8L).get();
                          a0 = 0x9L;
                          MEMORY.ref(4, v0).offset(0x0L).setu(a0);
                          v1 = v1 | 0x1L;
                          MEMORY.ref(4, v0).offset(0x8L).setu(v1);
                        }

                        if(v1 == 0x2L) {
                          //LAB_800268d0
                          v0 = 0xaL;
                          v1 = _800bdf38.get((int)s2).getAddress();
                          MEMORY.ref(4, v1).offset(0x0L).setu(v0);
                        }
                      }
                    }
                  }
                }
              }
            } else {
              v0 = 0x1L;
              if(v1 == v0) {
                //LAB_8002663c
                v0 = 0x800c_0000L;
                v0 = v0 - 0x1ca8L;
                v1 = s2 << 2;
                v1 = v1 + s2;
                v1 = v1 << 2;
                v1 = v1 - s2;
                v1 = v1 << 2;
                v1 = v1 + v0;
                v0 = MEMORY.ref(4, v1).offset(0x8L).get();

                v0 = v0 & 0x1L;
                if(v0 == 0) {
                  v1 = MEMORY.ref(2, s0).offset(0x4L).getSigned();
                  switch((int)v1) {
                    case 0:
                      v0 = 0x800c_0000L;
                      v0 = v0 - 0x20c8L;
                      v1 = s2 << 5;
                      v1 = v1 + s2;
                      v1 = v1 << 2;
                      v1 = v1 + v0;
                      v0 = 0xcL;
                      MEMORY.ref(4, v1).offset(0x0L).setu(v0);
                      break;

                    case 2:
                      v1 = 0x800c_0000L;
                      v1 = v1 - 0x20c8L;
                      v0 = s2 << 5;
                      v0 = v0 + s2;
                      v0 = v0 << 2;
                      v0 = v0 + v1;
                      a1 = MEMORY.ref(2, v0).offset(0x1eL).get();
                      a0 = MEMORY.ref(4, v0).offset(0x8L).get();
                      v1 = 0x1L;
                      MEMORY.ref(2, v0).offset(0x2aL).setu(v1);
                      v1 = 0xaL;
                      MEMORY.ref(2, v0).offset(0x34L).setu(0);
                      MEMORY.ref(4, v0).offset(0x0L).setu(v1);
                      a0 = a0 | 0x1L;
                      MEMORY.ref(2, v0).offset(0x36L).setu(a1);
                      MEMORY.ref(4, v0).offset(0x8L).setu(a0);
                      break;

                    case 3:
                      a0 = 0;
                      a1 = 0x4L;
                      a2 = a0;
                      a3 = a0;
                      v1 = 0x800c_0000L;
                      v1 = v1 - 0x20c8L;
                      v0 = s2 << 5;
                      v0 = v0 + s2;
                      v0 = v0 << 2;
                      v0 = v0 + v1;
                      v1 = 0x1L;
                      MEMORY.ref(2, v0).offset(0x2aL).setu(v1);
                      v1 = 0x17L;
                      MEMORY.ref(4, v0).offset(0x0L).setu(v1);
                      v1 = 0xaL;
                      MEMORY.ref(4, v0).offset(0x64L).setu(v1);
                      v1 = MEMORY.ref(4, v0).offset(0x8L).get();
                      t0 = 0x11L;
                      MEMORY.ref(2, v0).offset(0x34L).setu(0);
                      MEMORY.ref(2, v0).offset(0x36L).setu(0);
                      MEMORY.ref(4, v0).offset(0x78L).setu(t0);
                      v1 = v1 | 0x1L;
                      MEMORY.ref(4, v0).offset(0x8L).setu(v1);
                      Scus94491BpeSegment.playSound((int)a0, (int)a1, a2, a3, (short)0, (short)0);
                      break;

                    case 4:
                      v1 = 0x800c_0000L;
                      v1 = v1 - 0x20c8L;
                      v0 = s2 << 5;
                      v0 = v0 + s2;
                      v0 = v0 << 2;
                      s0 = v0 + v1;

                      //LAB_80026780
                      do {
                        FUN_800274f0(s2);
                        v1 = MEMORY.ref(4, s0).offset(0x8L).get();
                        v0 = v1 & 0x400L;
                      } while(v0 == 0);

                      v0 = v1 ^ 0x400L;
                      MEMORY.ref(4, s0).offset(0x8L).setu(v0);
                      // Fall through

                    default:
                      //LAB_800267a0
                      v0 = 0x800c_0000L;
                      v0 = v0 - 0x20c8L;
                      v1 = s2 << 5;
                      v1 = v1 + s2;
                      v1 = v1 << 2;
                      v1 = v1 + v0;
                      v0 = 0x4L;
                      MEMORY.ref(4, v1).offset(0x0L).setu(v0);
                      break;
                  }
                }
              } else if(v1 == 0x2L) {
                MEMORY.ref(4, s0).offset(0x0L).setu(0x4L);
              }
            }
          }
        }
      }
    }

    //LAB_800274c8
    a0 = s2;
    FUN_8002a4c4(a0);
  }

  @Method(0x800274f0L)
  public static void FUN_800274f0(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long t0;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;
    long fp;
    long lo;
    long sp10;

    s3 = a0;
    v0 = 0x800c_0000L;
    s4 = v0 - 0x20c8L;
    v0 = s3 << 5;
    v0 = v0 + s3;
    s2 = v0 << 2;
    s0 = s2 + s4;
    v0 = MEMORY.ref(4, s0).offset(0x8L).get();
    s5 = MEMORY.ref(4, s0).offset(0x24L).get();
    v0 = v0 & 0x10L;
    if(v0 == 0) {
      s1 = 0x1L;
    } else {
      s1 = 0x1L;
      a2 = MEMORY.ref(2, s0).offset(0x80L).get();
      a1 = MEMORY.ref(2, s0).offset(0x34L).getSigned();
      v0 = MEMORY.ref(4, s0).offset(0x80L).get();
      a3 = MEMORY.ref(1, s0).offset(0x28L).get();
      s1 = a2;
      v0 = v0 + 0x1L;
      MEMORY.ref(4, s0).offset(0x80L).setu(v0);
      v0 = s1 << 16;
      v0 = (int)v0 >> 15;
      v0 = v0 + s2;
      v0 = v0 + s4;
      a2 = MEMORY.ref(2, s0).offset(0x36L).getSigned();
      v0 = MEMORY.ref(2, v0).offset(0x46L).getSigned();
      sp10 = v0;
      FUN_8002a180(a0, a1, a2, a3, sp10);
      v1 = MEMORY.ref(2, s0).offset(0x3cL).get();
      v0 = MEMORY.ref(2, s0).offset(0x34L).get();
      v1 = v1 + 0x1L;
      v0 = v0 + 0x1L;
      MEMORY.ref(2, s0).offset(0x34L).setu(v0);
      v0 = v0 << 16;
      MEMORY.ref(2, s0).offset(0x3cL).setu(v1);
      v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
      v0 = (int)v0 >> 16;
      if((int)v0 < (int)v1) {
        //LAB_80027768
        v0 = s1 << 16;
        v0 = (int)v0 >> 15;
        v0 = v0 + 0x2L;
        v0 = v0 + s2;
        v0 = v0 + s4;
        v1 = MEMORY.ref(2, v0).offset(0x46L).getSigned();
        v0 = -0x1L;
        if(v1 == v0) {
          v0 = MEMORY.ref(4, s0).offset(0x8L).get();
          v0 = v0 ^ 0x10L;
          MEMORY.ref(4, s0).offset(0x8L).setu(v0);
        }
      } else {
        v0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
        v1 = MEMORY.ref(2, s0).offset(0x36L).getSigned();
        v0 = v0 - 0x1L;
        if((int)v1 >= (int)v0) {
          v0 = s1 << 16;
          v0 = (int)v0 >> 15;
          v0 = v0 + 0x2L;
          v0 = v0 + s2;
          v0 = v0 + s4;
          v1 = MEMORY.ref(2, v0).offset(0x46L).getSigned();
          v0 = -0x1L;
          if(v1 != v0) {
            a0 = s3;
            a1 = 0x1L;
            v1 = MEMORY.ref(2, s0).offset(0x36L).get();
            v0 = 0x5L;
            MEMORY.ref(4, s0).offset(0x0L).setu(v0);
            MEMORY.ref(2, s0).offset(0x34L).setu(0);
            v1 = v1 + a1;
            MEMORY.ref(2, s0).offset(0x36L).setu(v1);
            FUN_80029920((int)a0, a1);
            return;
          }

          //LAB_80027618
          a0 = MEMORY.ref(4, s0).offset(0x30L).get();

          v0 = a0 << 1;
          v0 = v0 + s5;
          v0 = MEMORY.ref(2, v0).offset(0x0L).get();
          v1 = v0 >>> 8;

          if(v1 == 0xa0L) {
            //LAB_800276f4
            MEMORY.ref(4, s0).offset(0x0L).setu(0xfL);

            //LAB_80027704
            FUN_80029920((int)s3, 0x1L);

            //LAB_80027740
            _800bdf38.get((int)s3)._08.xor(0x10L);
            return;
          }

          if(v1 == 0xa1L) {
            v0 = a0 + 0x1L;
            MEMORY.ref(4, s0).offset(0x30L).setu(v0);
          }

          //LAB_8002764c
          a0 = s3;
          a1 = 0x1L;
          v1 = 0x800c_0000L;
          v1 = v1 - 0x20c8L;
          v0 = s3 << 5;
          v0 = v0 + s3;
          v0 = v0 << 2;
          v0 = v0 + v1;
          a2 = MEMORY.ref(2, v0).offset(0x36L).get();
          v1 = 0x5L;
          MEMORY.ref(4, v0).offset(0x0L).setu(v1);
          MEMORY.ref(2, v0).offset(0x34L).setu(0);
          a2 = a2 + a1;
          MEMORY.ref(2, v0).offset(0x36L).setu(a2);

          //LAB_80027704
          FUN_80029920((int)a0, a1);
        } else {
          //LAB_80027688
          v0 = MEMORY.ref(2, s0).offset(0x36L).get();
          MEMORY.ref(2, s0).offset(0x34L).setu(0);
          v0 = v0 + 0x1L;
          MEMORY.ref(2, s0).offset(0x36L).setu(v0);
          v0 = s1 << 16;
          v0 = (int)v0 >> 15;
          v0 = v0 + 0x2L;
          v0 = v0 + s2;
          v0 = v0 + s4;
          v1 = MEMORY.ref(2, v0).offset(0x46L).getSigned();
          v0 = -0x1L;
          if(v1 == v0) {
            a0 = MEMORY.ref(4, s0).offset(0x30L).get();

            v0 = a0 << 1;
            v0 = v0 + s5;
            v0 = MEMORY.ref(2, v0).offset(0x0L).get();

            v1 = v0 >>> 8;
            v0 = 0xa0L;
            if(v1 == v0) {
              //LAB_800276f4
              MEMORY.ref(4, s0).offset(0x0L).setu(0xfL);

              //LAB_80027704
              FUN_80029920((int)s3, 0x1L);
            } else {
              if(v1 == 0xa1L) {
                //LAB_80027714
                MEMORY.ref(4, s0).offset(0x30L).setu(a0 + 0x1L);
              }

              //LAB_80027724
              _800bdf38.get((int)s3)._00.set(0x7L);
            }

            //LAB_80027740
            _800bdf38.get((int)s3)._08.xor(0x10L);
            return;
          }
        }
      }

      //LAB_8002779c
      _800bdf38.get((int)s3)._00.set(0x7L);
      return;
    }

    //LAB_800277bc
    s6 = s4;
    s4 = 0xaL;
    s7 = s1;
    fp = s1;

    //LAB_800277cc
    do {
      v0 = MEMORY.ref(4, s0).offset(0x30L).get();

      v0 = v0 << 1;
      v0 = v0 + s5;
      a0 = MEMORY.ref(2, v0).offset(0x0L).get();

      v0 = a0 << 16;
      t0 = (int)v0 >> 16;
      v0 = v0 >>> 24;

      switch((int)v0) {
        case 0xa0:
          v0 = 0xfL;
          MEMORY.ref(4, s0).offset(0x0L).setu(v0);
          a0 = s3;
          a1 = 0x1L;
          FUN_80029920((int)a0, a1);
          s1 = 0;
          break;

        case 0xa1:
          v0 = MEMORY.ref(4, s0).offset(0x8L).get();
          v1 = MEMORY.ref(2, s0).offset(0x36L).get();
          MEMORY.ref(2, s0).offset(0x34L).setu(0);
          v0 = v0 | 0x400L;
          v1 = v1 + 0x1L;
          MEMORY.ref(2, s0).offset(0x36L).setu(v1);
          v1 = v1 << 16;
          MEMORY.ref(4, s0).offset(0x8L).setu(v0);
          v0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
          v1 = (int)v1 >> 16;
          if((int)v1 < (int)v0 || (MEMORY.ref(4, s0).offset(0x8L).get() & 0x80L) != 0) {
            //LAB_80027880
            v1 = MEMORY.ref(4, s0).offset(0x8L).get();
            v0 = 0x5L;
            v1 = v1 & 0x1L;
            MEMORY.ref(4, s0).offset(0x0L).setu(v0);
            if(v1 == 0) {
              a0 = s3;
              a1 = 0x1L;
              FUN_80029920((int)a0, a1);
            }
            s1 = 0;
          }

          break;

        case 0xa2:
          v0 = 0x6L;

          //LAB_80027d28
          MEMORY.ref(4, s0).offset(0x0L).setu(v0);

          //LAB_80027d2c
          s1 = 0;
          break;

        case 0xa3:
          a0 = s3;
          a1 = 0x1L;
          FUN_80029920((int)a0, a1);
          a0 = MEMORY.ref(4, s0).offset(0x30L).get();
          v0 = 0xbL;
          MEMORY.ref(4, s0).offset(0x0L).setu(v0);
          v0 = a0 << 1;
          v0 = v0 + s5;
          v0 = MEMORY.ref(2, v0).offset(0x2L).get();
          v1 = 0xa1L;
          v0 = v0 >>> 8;
          if(v0 != v1) {
            s1 = 0;
          } else {
            s1 = 0;
            v0 = a0 + 0x1L;
            MEMORY.ref(4, s0).offset(0x30L).setu(v0);
          }

          break;

        case 0xa5:
          v0 = a0 & 0xffL;
          MEMORY.ref(2, s0).offset(0x3eL).setu(v0);
          MEMORY.ref(2, s0).offset(0x40L).setu(0);
          break;

        case 0xa6:
          v0 = 0x8008_0000L;
          v1 = MEMORY.ref(4, v0).offset(-0x5c48L).get();
          v0 = 0x3cL;
          lo = (int)v0 / (int)v1;
          v0 = lo;
          v1 = a0 & 0xffL;
          lo = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
          s1 = 0;
          v0 = 0x8L;
          MEMORY.ref(4, s0).offset(0x0L).setu(v0);
          v1 = lo;
          MEMORY.ref(2, s0).offset(0x44L).setu(v1);
          break;

        case 0xa7:
          v0 = a0 & 0xfL;
          a2 = v0;
          if((int)v0 >= 0xcL) {
            a2 = 0;
          }

          //LAB_80027950
          MEMORY.ref(2, s0).offset(0x28L).setu(a2);
          break;

        case 0xa8:
          a2 = 0;
          v0 = MEMORY.ref(4, s0).offset(0x8L).get();
          v1 = -0x1L;
          v0 = v0 | 0x10L;
          MEMORY.ref(4, s0).offset(0x8L).setu(v0);

          //LAB_80027970
          do {
            v0 = a2 << 16;
            v0 = (int)v0 >> 15;
            v0 = v0 + s2;
            v0 = v0 + s6;
            MEMORY.ref(2, v0).offset(0x46L).setu(v1);
            v0 = a2 + 0x1L;
            a2 = v0;
            v0 = v0 << 16;
            v0 = (int)v0 >> 16;
          } while((int)v0 < 0x8L);

          v1 = 0x800c_0000L;
          v1 = v1 - 0x20f0L;
          v0 = a0 & 0xffL;
          v0 = v0 << 2;
          v0 = v0 + v1;
          a1 = MEMORY.ref(4, v0).offset(0x0L).get();
          a3 = 1_000_000_000L;

          final long[] sp0x18 = new long[(int)s4];

          if(s7 != 0) {
            //LAB_800279dc
            for(a2 = 0; a2 < s4; a2++) {
              v0 = a1 / a3;
              a1 = a1 % a3;
              a3 = a3 / 10;
              sp0x18[(int)a2] = _80052b40.get((int)v0).deref().get();
            }
          }

          //LAB_80027a34
          if(fp == 0) {
            s1 = 0;
          } else {
            v0 = 0x8005_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x2b40L).get();
            a0 = s4 - 0x1L;
            v1 = MEMORY.ref(2, v0).offset(0x0L).get();

            //LAB_80027a54
            for(s1 = 0; s1 < a0; s1++) {
              if(sp0x18[(int)s1] != v1) {
                break;
              }
            }
          }

          //LAB_80027a84
          //LAB_80027a90
          for(a2 = 0; a2 < 8 && s1 < s4; a2++, s1++) {
            MEMORY.ref(2, s6).offset(0x46L).offset(a2 * 0x2L).offset(s2).setu(sp0x18[(int)s1]);
          }

          //LAB_80027ae4
          MEMORY.ref(4, s0).offset(0x80L).setu(0);

          //LAB_80027d2c
          s1 = 0;
          break;

        case 0xad:
          v1 = a0 & 0xffL;
          v0 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
          a0 = MEMORY.ref(2, s0).offset(0x1cL).get();
          if((int)v1 >= (int)v0) {
            v0 = a0 - 0x1L;
            MEMORY.ref(2, s0).offset(0x34L).setu(v0);
          } else {
            //LAB_80027b0c
            MEMORY.ref(2, s0).offset(0x34L).setu(v1);
          }

          break;

        case 0xae:
          v1 = a0 & 0xffL;
          v0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
          a0 = MEMORY.ref(2, s0).offset(0x1eL).get();
          v0 = v0 - 0x1L;
          if((int)v1 >= (int)v0) {
            v0 = a0 - 0x1L;
            MEMORY.ref(2, s0).offset(0x36L).setu(v0);
          } else {
            //LAB_80027b38
            MEMORY.ref(2, s0).offset(0x36L).setu(v1);
          }

          break;

        case 0xb0:
          v0 = 0x8008_0000L;
          v1 = MEMORY.ref(4, v0).offset(-0x5c48L).get();
          v0 = 0x3cL;
          lo = (int)v0 / (int)v1;
          v0 = lo;
          v1 = a0 & 0xffL;
          lo = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
          a0 = MEMORY.ref(4, s0).offset(0x30L).get();
          v1 = 0xdL;
          MEMORY.ref(4, s0).offset(0x0L).setu(v1);
          v0 = lo;
          MEMORY.ref(2, s0).offset(0x3eL).setu(v0);
          MEMORY.ref(2, s0).offset(0x40L).setu(v0);
          v0 = a0 << 1;
          v0 = v0 + s5;
          v0 = MEMORY.ref(2, v0).offset(0x2L).get();
          v1 = 0xa1L;
          v0 = v0 >>> 8;
          if(v0 != v1) {
            s1 = 0;
          } else {
            s1 = 0;
            v0 = a0 + 0x1L;
            MEMORY.ref(4, s0).offset(0x30L).setu(v0);
          }

          break;

        case 0xb1:
          v0 = a0 & 0xffL;
          MEMORY.ref(4, s0).offset(0x7cL).setu(v0);
          break;

        case 0xb2:
          v1 = a0 & 0x1L;
          v0 = 0x1L;
          if(v1 == v0) {
            v0 = MEMORY.ref(4, s0).offset(0x8L).get();
            v0 = v0 | 0x1000L;
            MEMORY.ref(4, s0).offset(0x8L).setu(v0);
          } else {
            //LAB_80027bd0
            v0 = MEMORY.ref(4, s0).offset(0x8L).get();
            v0 = v0 ^ 0x1000L;
            MEMORY.ref(4, s0).offset(0x8L).setu(v0);
          }

          break;

        default:
          //LAB_80027be4
          a1 = MEMORY.ref(2, s0).offset(0x34L).getSigned();
          a2 = MEMORY.ref(2, s0).offset(0x36L).getSigned();
          a3 = MEMORY.ref(1, s0).offset(0x28L).get();
          a0 = s3;
          sp10 = t0;
          FUN_8002a180(a0, a1, a2, a3, sp10);
          v1 = MEMORY.ref(2, s0).offset(0x3cL).get();
          v0 = MEMORY.ref(2, s0).offset(0x34L).get();
          v1 = v1 + 0x1L;
          v0 = v0 + 0x1L;
          MEMORY.ref(2, s0).offset(0x34L).setu(v0);
          v0 = v0 << 16;
          MEMORY.ref(2, s0).offset(0x3cL).setu(v1);
          v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
          v0 = (int)v0 >> 16;
          if((int)v0 < (int)v1) {
            v0 = 0x7L;

            //LAB_80027d28
            MEMORY.ref(4, s0).offset(0x0L).setu(v0);

            //LAB_80027d2c
            s1 = 0;
            break;
          }

          v0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
          v1 = MEMORY.ref(2, s0).offset(0x36L).getSigned();
          v0 = v0 - 0x1L;
          if((int)v1 >= (int)v0) {
            a0 = MEMORY.ref(4, s0).offset(0x30L).get();

            v0 = a0 << 1;
            v0 = v0 + s5;
            v0 = MEMORY.ref(2, v0).offset(0x2L).get();

            v1 = v0 >>> 8;
            if(v1 == 0xa0L) {
              //LAB_80027c7c
              MEMORY.ref(4, s0).offset(0x0L).setu(0xfL);
              FUN_80029920((int)s3, 0x1L);
            } else {
              if(v1 == 0xa1L) {
                //LAB_80027c98
                MEMORY.ref(4, s0).offset(0x30L).setu(a0 + 0x1L);
              }

              //LAB_80027c9c
              MEMORY.ref(4, s0).offset(0x00L).setu(0x5L);
              MEMORY.ref(4, s0).offset(0x08L).oru(0x400L);
              MEMORY.ref(2, s0).offset(0x34L).setu(0);
              MEMORY.ref(2, s0).offset(0x36L).addu(0x1L);

              if((MEMORY.ref(4, s0).offset(0x08L).get() & 0x1L) == 0) {
                FUN_80029920((int)s3, 0x1L);
              }
            }
          } else {
            //LAB_80027ce0
            MEMORY.ref(4, s0).offset(0x08L).oru(0x400L);
            MEMORY.ref(2, s0).offset(0x34L).setu(0);
            MEMORY.ref(2, s0).offset(0x36L).addu(0x1L);

            if(MEMORY.ref(2, s5).offset((MEMORY.ref(4, s0).offset(0x30L).get() + 1) * 0x2L).get() >>> 8 == 0xa1L) {
              MEMORY.ref(4, s0).offset(0x30L).addu(0x1L);
            }

            //LAB_80027d28
            MEMORY.ref(4, s0).offset(0x00L).setu(0x7L);
          }

          //LAB_80027d2c
          s1 = 0;
          break;
      }

      //LAB_80027d30
      MEMORY.ref(4, s0).offset(0x30L).addu(0x1L);
    } while(s1 != 0);

    //LAB_80027d44
  }

  @Method(0x80027d74L)
  public static void FUN_80027d74(long a0, long a1, long a2) {
    long v0;
    long v1;
    long a3;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;

    t5 = a0;
    t2 = a1;
    t3 = a2;
    t1 = 0x136L;
    v0 = 0x8005_0000L;
    v1 = MEMORY.ref(4, v0).offset(-0x22e0L).get();
    v0 = 0x5L;
    if(v1 != v0) {
      v1 = 0x800c_0000L;
    } else {
      v1 = 0x800c_0000L;
      t1 = 0x15eL;
    }

    //LAB_80027d9c
    v1 = v1 - 0x20c8L;
    v0 = t5 << 5;
    v0 = v0 + t5;
    v0 = v0 << 2;
    t0 = v0 + v1;
    v1 = MEMORY.ref(2, t0).offset(0x1cL).getSigned();
    a0 = MEMORY.ref(2, t0).offset(0x1eL).getSigned();
    v0 = v1 << 3;
    v0 = v0 + v1;
    v1 = v0 >>> 31;
    v0 = v0 + v1;
    a3 = (int)v0 >> 1;
    v1 = a1 - a3;
    a1 = a1 + a3;
    v0 = a0 << 1;
    v0 = v0 + a0;
    a0 = v0 << 1;
    t4 = a2 - a0;
    v1 = v1 << 16;
    v1 = (int)v1 >> 16;
    if((int)v1 >= 0xaL) {
      a2 = a2 + a0;
    } else {
      a2 = a2 + a0;
      t2 = a3 + 0xaL;
    }

    //LAB_80027dfc
    v0 = a1 << 16;
    v0 = (int)v0 >> 16;
    if((int)t1 >= (int)v0) {
      v0 = t4 << 16;
    } else {
      v0 = t4 << 16;
      t2 = t1 - a3;
    }

    //LAB_80027e14
    v0 = (int)v0 >> 16;
    if((int)v0 >= 0x12L) {
      v1 = a2 << 16;
    } else {
      v1 = a2 << 16;
      t3 = a0 + 0x12L;
    }

    //LAB_80027e28
    v1 = (int)v1 >> 16;
    v0 = 0xdeL;
    if((int)v0 >= (int)v1) {
      v0 = 0xdeL;
    } else {
      v0 = 0xdeL;
      t3 = v0 - a0;
    }

    //LAB_80027e40
    v1 = 0x800c_0000L;
    v1 = v1 - 0x1ca8L;
    v0 = t5 << 2;
    v0 = v0 + t5;
    v0 = v0 << 2;
    v0 = v0 - t5;
    v0 = v0 << 2;
    v0 = v0 + v1;
    MEMORY.ref(2, v0).offset(0x14L).setu(t2);
    MEMORY.ref(2, t0).offset(0x14L).setu(t2);
    MEMORY.ref(2, v0).offset(0x16L).setu(t3);
    v1 = MEMORY.ref(2, t0).offset(0x1cL).getSigned();
    a0 = MEMORY.ref(2, t0).offset(0x1eL).getSigned();
    MEMORY.ref(2, t0).offset(0x16L).setu(t3);
    v0 = v1 << 3;
    v0 = v0 + v1;
    v1 = v0 >>> 31;
    v0 = v0 + v1;
    v1 = MEMORY.ref(2, t0).offset(0x14L).get();
    v0 = (int)v0 >> 1;
    v1 = v1 - v0;
    v0 = a0 << 1;
    v0 = v0 + a0;
    MEMORY.ref(2, t0).offset(0x18L).setu(v1);
    v1 = t3;
    v0 = v0 << 1;
    v1 = v1 - v0;
    MEMORY.ref(2, t0).offset(0x1aL).setu(v1);
  }

  @Method(0x80027eb4L)
  public static void FUN_80027eb4(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long lo;

    t3 = a0;
    v0 = 0x800c_0000L;
    t0 = v0 - 0x20c8L;
    a1 = t3 << 5;
    v0 = a1 + t3;
    v0 = v0 << 2;
    a0 = v0 + t0;
    v0 = MEMORY.ref(4, a0).offset(0x8L).get();
    v1 = MEMORY.ref(2, a0).offset(0x1eL).getSigned();
    a2 = MEMORY.ref(4, a0).offset(0x58L).get();
    v0 = v0 & 0x200L;
    a3 = v0 > 0 ? 1 : 0;
    if((int)a3 < (int)v1) {
      t4 = t0;
      t5 = a1;
      t2 = a0;

      //LAB_80027efc
      do {
        t1 = t5;
        v0 = MEMORY.ref(2, t2).offset(0x1cL).getSigned();

        if((int)v0 <= 0) {
          a1 = 0;
        } else {
          a1 = 0;
          t0 = a3 + 0x1L;
          a0 = t1 + t3;

          //LAB_80027f18
          do {
            a0 = a0 << 2;
            a0 = a0 + t4;
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            lo = (long)(int)a3 * (int)v0 & 0xffff_ffffL;
            v1 = lo;
            lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
            v1 = v1 + a1;
            v1 = v1 << 3;
            v0 = lo;
            v0 = v0 + a1;
            v0 = v0 << 3;
            v0 = v0 + a2;
            v0 = MEMORY.ref(2, v0).offset(0x0L).get();
            v1 = v1 + a2;
            MEMORY.ref(2, v1).offset(0x0L).setu(v0);
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            lo = (long)(int)a3 * (int)v0 & 0xffff_ffffL;
            v1 = lo;
            lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
            v1 = v1 + a1;
            v1 = v1 << 3;
            v0 = lo;
            v0 = v0 + a1;
            v0 = v0 << 3;
            v0 = v0 + a2;
            v0 = MEMORY.ref(2, v0).offset(0x2L).get();
            v1 = v1 + a2;
            v0 = v0 - 0x1L;
            MEMORY.ref(2, v1).offset(0x2L).setu(v0);
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            lo = (long)(int)a3 * (int)v0 & 0xffff_ffffL;
            v1 = lo;
            lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
            v1 = v1 + a1;
            v1 = v1 << 3;
            v0 = lo;
            v0 = v0 + a1;
            v0 = v0 << 3;
            v0 = v0 + a2;
            v0 = MEMORY.ref(1, v0).offset(0x4L).get();
            v1 = v1 + a2;
            MEMORY.ref(1, v1).offset(0x4L).setu(v0);
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            lo = (long)(int)a3 * (int)v0 & 0xffff_ffffL;
            v1 = lo;
            lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
            v1 = v1 + a1;
            v1 = v1 << 3;
            v0 = lo;
            v0 = v0 + a1;
            v0 = v0 << 3;
            v0 = v0 + a2;
            v0 = MEMORY.ref(2, v0).offset(0x6L).get();
            v1 = v1 + a2;
            MEMORY.ref(2, v1).offset(0x6L).setu(v0);
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            a1 = a1 + 0x1L;
            a0 = t1 + t3;
          } while((int)a1 < (int)v0);
        }

        //LAB_80028038
        v0 = MEMORY.ref(2, t2).offset(0x1eL).getSigned();
        a3 = a3 + 0x1L;
      } while((int)a3 < (int)v0);

    }
    v0 = 0x800c_0000L;

    //LAB_8002804c
    v0 = v0 - 0x20c8L;
    v1 = t3 << 5;
    v1 = v1 + t3;
    v1 = v1 << 2;
    v1 = v1 + v0;
    a0 = MEMORY.ref(2, v1).offset(0x1cL).getSigned();
    v0 = MEMORY.ref(2, v1).offset(0x1eL).getSigned();
    lo = (long)(int)a0 * (int)v0 & 0xffff_ffffL;
    a1 = lo;
    v0 = v0 + 0x1L;
    lo = (long)(int)a0 * (int)v0 & 0xffff_ffffL;
    v0 = a1 << 3;
    a0 = v0 + a2;
    a3 = lo;
    if((int)a1 < (int)a3) {
      a2 = v1;

      //LAB_80028098
      do {
        MEMORY.ref(2, a0).offset(0x0L).setu(0);
        MEMORY.ref(2, a0).offset(0x2L).setu(0);
        MEMORY.ref(1, a0).offset(0x4L).setu(0);
        MEMORY.ref(2, a0).offset(0x6L).setu(0);
        v0 = MEMORY.ref(2, a2).offset(0x1eL).getSigned();
        v1 = MEMORY.ref(2, a2).offset(0x1cL).getSigned();
        v0 = v0 + 0x1L;
        lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
        a1 = a1 + 0x1L;
        t6 = lo;
        a0 = a0 + 0x8L;
      } while((int)a1 < (int)t6);
    }

    //LAB_800280cc
  }

  @Method(0x800280d4L)
  public static void FUN_800280d4(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long lo;

    t2 = a0;
    v0 = 0x800c_0000L;
    a0 = v0 - 0x20c8L;
    v1 = t2 << 5;
    v0 = v1 + t2;
    v0 = v0 << 2;
    v0 = v0 + a0;
    a3 = MEMORY.ref(2, v0).offset(0x1eL).getSigned();
    a2 = MEMORY.ref(4, v0).offset(0x58L).get();
    if((int)a3 > 0) {
      t3 = a0;
      t5 = v1;
      t4 = v0;

      //LAB_8002810c
      do {
        t1 = t5;
        v0 = MEMORY.ref(2, t4).offset(0x1cL).getSigned();

        if((int)v0 > 0) {
          a1 = 0;
          t0 = a3 - 0x1L;
          a0 = t1 + t2;

          //LAB_80028128
          do {
            a0 = a0 << 2;
            a0 = a0 + t3;
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            lo = (long)(int)a3 * (int)v0 & 0xffff_ffffL;
            v1 = lo;
            lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
            v1 = v1 + a1;
            v1 = v1 << 3;
            v0 = lo;
            v0 = v0 + a1;
            v0 = v0 << 3;
            v0 = v0 + a2;
            v0 = MEMORY.ref(2, v0).offset(0x0L).get();
            v1 = v1 + a2;
            MEMORY.ref(2, v1).offset(0x0L).setu(v0);
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            lo = (long)(int)a3 * (int)v0 & 0xffff_ffffL;
            v1 = lo;
            lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
            v1 = v1 + a1;
            v1 = v1 << 3;
            v0 = lo;
            v0 = v0 + a1;
            v0 = v0 << 3;
            v0 = v0 + a2;
            v0 = MEMORY.ref(2, v0).offset(0x2L).get();
            v1 = v1 + a2;
            v0 = v0 + 0x1L;
            MEMORY.ref(2, v1).offset(0x2L).setu(v0);
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            lo = (long)(int)a3 * (int)v0 & 0xffff_ffffL;
            v1 = lo;
            lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
            v1 = v1 + a1;
            v1 = v1 << 3;
            v0 = lo;
            v0 = v0 + a1;
            v0 = v0 << 3;
            v0 = v0 + a2;
            v0 = MEMORY.ref(1, v0).offset(0x4L).get();
            v1 = v1 + a2;
            MEMORY.ref(1, v1).offset(0x4L).setu(v0);
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            lo = (long)(int)a3 * (int)v0 & 0xffff_ffffL;
            v1 = lo;
            lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
            v1 = v1 + a1;
            v1 = v1 << 3;
            v0 = lo;
            v0 = v0 + a1;
            v0 = v0 << 3;
            v0 = v0 + a2;
            v0 = MEMORY.ref(2, v0).offset(0x6L).get();
            v1 = v1 + a2;
            MEMORY.ref(2, v1).offset(0x6L).setu(v0);
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            a1 = a1 + 0x1L;
            a0 = t1 + t2;
          } while((int)a1 < (int)v0);
        }

        //LAB_80028248
        a3 = a3 - 0x1L;
      } while((int)a3 > 0);
    }

    //LAB_80028254
    v0 = 0x800c_0000L;
    v0 = v0 - 0x20c8L;
    v1 = t2 << 5;
    v1 = v1 + t2;
    v1 = v1 << 2;
    v1 = v1 + v0;
    v0 = MEMORY.ref(2, v1).offset(0x1cL).getSigned();

    if((int)v0 > 0) {
      a1 = 0;
      a0 = a2;

      //LAB_80028280
      do {
        MEMORY.ref(2, a0).offset(0x0L).setu(0);
        MEMORY.ref(2, a0).offset(0x2L).setu(0);
        MEMORY.ref(1, a0).offset(0x4L).setu(0);
        MEMORY.ref(2, a0).offset(0x6L).setu(0);
        v0 = MEMORY.ref(2, v1).offset(0x1cL).getSigned();
        a1 = a1 + 0x1L;
        a0 = a0 + 0x8L;
      } while((int)a1 < (int)v0);
    }

    //LAB_800282a4
  }

  @Method(0x800282acL)
  public static void FUN_800282ac(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;
    long sp;
    long fp;
    long ra;
    long hi;
    long lo;
    long sp60;
    long sp70;
    long sp22;
    long sp44;
    long sp54;
    long sp10;
    long sp68;
    long sp40;
    long sp50;
    long sp64;
    long sp20;
    long sp74;
    long sp30;
    long sp18;
    long sp48;
    long sp58;
    long sp14;
    long sp28;
    long sp38;
    long sp5c;
    long sp6c;
    v1 = 0x800c_0000L;
    v1 = v1 - 0x20c8L;
    v0 = a0 << 5;
    v0 = v0 + a0;
    v0 = v0 << 2;
    a0 = v0 + v1;
    sp10 = 0;
    v0 = MEMORY.ref(2, a0).offset(0x1cL).get();
    sp38 = 0;
    v1 = v0 << 16;
    v0 = MEMORY.ref(4, a0).offset(0x8L).get();
    a3 = (int)v1 >> 16;
    sp14 = a3;
    a1 = MEMORY.ref(4, a0).offset(0x58L).get();
    v0 = v0 & 0x200L;
    if(v0 == 0) {
      s7 = a0;
    } else {
      s7 = a0;
      sp10 = a3;
      a3 = a3 << 1;
      sp14 = a3;
    }

    //LAB_80028328
    v0 = MEMORY.ref(2, s7).offset(0x1eL).getSigned();
    v1 = (int)v1 >> 16;
    v0 = v0 + 0x1L;
    lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
    fp = a1;
    a3 = lo;
    if((int)a3 <= 0) {
      s5 = 0;
    } else {
      s5 = 0;

      //LAB_80028348
      do {
        v0 = MEMORY.ref(2, fp).offset(0x0L).getSigned();

        if(v0 == 0) {
          sp38 = 0;
        }

        //LAB_8002835c
        v0 = MEMORY.ref(2, fp).offset(0x6L).getSigned();

        if(v0 == 0) {
          s3 = 0;
        } else {
          s3 = 0;
          s2 = s3;
          v0 = MEMORY.ref(4, s7).offset(0x8L).get();

          v0 = v0 & 0x1L;
          if(v0 == 0) {
            s1 = s3;
          } else {
            s1 = s3;
            a3 = sp10;

            if((int)s5 >= (int)a3) {
              a3 = sp14;

              if((int)s5 < (int)a3) {
                v1 = MEMORY.ref(2, s7).offset(0x2cL).get();
                v0 = -v1;
                s1 = v0;
                s2 = v0;
                s3 = v1;
              }
            }

            //LAB_800283c4
            a0 = MEMORY.ref(2, s7).offset(0x1cL).getSigned();
            v1 = MEMORY.ref(2, s7).offset(0x1eL).getSigned();

            lo = (long)(int)a0 * (int)v1 & 0xffff_ffffL;
            a3 = lo;
            if((int)s5 < (int)a3) {
              v0 = v1 + 0x1L;
            } else {
              v0 = v1 + 0x1L;
              lo = (long)(int)a0 * (int)v0 & 0xffff_ffffL;
              a3 = lo;
              if((int)s5 >= (int)a3) {
                v0 = 0xcL;
              } else {
                v0 = 0xcL;
                s1 = 0;
                s2 = s1;
                v1 = MEMORY.ref(2, s7).offset(0x2cL).get();
                s3 = v0 - v1;
              }
            }
          }

          //LAB_8002840c
          a0 = MEMORY.ref(2, fp).offset(0x6L).get();
          FUN_8002a63c(a0);
          a2 = 0x6480_0000L;
          a3 = 0x1f80_0000L;
          v1 = 0x800c_0000L;
          a0 = 0x800c_0000L;
          a1 = 0x800c_0000L;
          v0 = MEMORY.ref(4, a3).offset(0x3d8L).get();
          v1 = MEMORY.ref(2, v1).offset(-0x1a48L).get();
          a0 = MEMORY.ref(2, a0).offset(-0x1a38L).get();
          s4 = MEMORY.ref(2, a1).offset(-0x1a40L).get();
          s6 = v0;
          v0 = s6 + 0x14L;
          MEMORY.ref(4, a3).offset(0x3d8L).setu(v0);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(2, v0).offset(-0x1a44L).get();
          a2 = a2 | 0x8080L;
          sp18 = v1;
          sp20 = a0;
          sp22 = v0;
          v0 = 0x4L;
          MEMORY.ref(1, s6).offset(0x3L).setu(v0);
          MEMORY.ref(4, s6).offset(0x4L).setu(a2);
          a0 = MEMORY.ref(2, fp).offset(0x6L).get();
          v0 = FUN_8002a25c(a0);
          a3 = sp38;
          a0 = MEMORY.ref(2, fp).offset(0x0L).getSigned();
          s0 = a3 - v0;
          v1 = a0 << 3;
          v1 = v1 + a0;
          a0 = 0x1f80_0000L;
          v0 = MEMORY.ref(2, s7).offset(0x18L).get();
          a0 = MEMORY.ref(2, a0).offset(0x3dcL).get();
          v0 = v0 + v1;
          v0 = v0 - a0;
          v0 = v0 - s0;
          MEMORY.ref(2, s6).offset(0x8L).setu(v0);
          a0 = MEMORY.ref(2, fp).offset(0x6L).get();
          v0 = FUN_8002a1fc(a0);
          a3 = 0x1f80_0000L;
          v1 = MEMORY.ref(2, fp).offset(0x2L).getSigned();
          a3 = a3 + 0x3dcL;
          a0 = v1 << 1;
          a0 = a0 + v1;
          v1 = MEMORY.ref(2, s7).offset(0x1aL).get();
          a0 = a0 << 2;
          v1 = v1 + a0;
          a0 = MEMORY.ref(2, a3).offset(0x2L).get();
          a1 = MEMORY.ref(2, s7).offset(0x2cL).get();
          v1 = v1 - a0;
          v1 = v1 - a1;
          v1 = v1 - s1;
          MEMORY.ref(2, s6).offset(0xaL).setu(v1);
          v1 = MEMORY.ref(4, s7).offset(0x8L).get();
          s0 = s0 + v0;
          v1 = v1 & 0x200L;
          if(v1 == 0) {
            sp38 = s0;
          } else {
            sp38 = s0;
            if((int)s5 >= 0) {
              v0 = MEMORY.ref(2, s7).offset(0x1cL).getSigned();

              if((int)s5 < (int)v0) {
                v0 = MEMORY.ref(2, fp).offset(0x2L).getSigned();
                a0 = MEMORY.ref(2, a3).offset(0x2L).get();
                v1 = v0 << 1;
                v1 = v1 + v0;
                v0 = MEMORY.ref(2, s7).offset(0x1aL).get();
                v1 = v1 << 2;
                v0 = v0 + v1;
                v0 = v0 - a0;
                v0 = v0 - s1;
                MEMORY.ref(2, s6).offset(0xaL).setu(v0);
              }
            }
          }

          //LAB_80028544
          v0 = sp22;

          if(v0 < 0x6L) {
            a0 = 0;
          } else {
            a0 = 0xf0L;
          }

          //LAB_80028564
          v0 = s4 & 0xffffL;
          v0 = v0 << 4;
          sp40 = v0;
          a3 = sp40;

          MEMORY.ref(1, s6).offset(0xcL).setu(a3);
          v1 = sp20;

          v0 = v1 << 1;
          v0 = v0 + v1;
          v0 = v0 << 2;
          v0 = a0 + v0;
          v0 = v0 - s2;
          sp44 = v0;
          a3 = sp44;

          MEMORY.ref(1, s6).offset(0xdL).setu(a3);
          a3 = sp18;
          v0 = MEMORY.ref(1, fp).offset(0x4L).get();
          v1 = a3 + 0x1e0L;
          s4 = v1 << 6;
          v0 = v0 << 4;
          v0 = v0 + 0x340L;
          v0 = v0 & 0x3f0L;
          v0 = (int)v0 >> 4;
          v0 = s4 | v0;
          MEMORY.ref(2, s6).offset(0xeL).setu(v0);
          v0 = s3 << 16;
          a3 = MEMORY.ref(2, s6).offset(0x8L).get();
          v0 = (int)v0 >> 16;
          sp28 = a3;
          a3 = MEMORY.ref(2, s6).offset(0xaL).get();
          if((int)v0 >= 0xdL) {
            sp30 = a3;
          } else {
            sp30 = a3;
            a0 = s6;
            a1 = 0;
            a3 = 0x8L;
            s2 = 0xcL;
            s2 = s2 - s3;
            MEMORY.ref(2, s6).offset(0x10L).setu(a3);
            MEMORY.ref(2, s6).offset(0x12L).setu(s2);
            gpuLinkedListSetCommandTransparency(a0, false);
            a1 = s6;
            a3 = 0xe100_0000L;
            sp48 = a3;
            a3 = 0x1f80_0000L;
            a0 = MEMORY.ref(4, s7).offset(0xcL).get();
            v0 = MEMORY.ref(4, a3).offset(0x3d0L).get();
            a0 = a0 << 2;
            a0 = v0 + a0;
            insertElementIntoLinkedList(a0, a1);
            a3 = 0x1f80_0000L;
            s3 = 0x800c_0000L;
            s1 = 0x8005_0000L;
            s1 = s1 + 0x2bf4L;
            a1 = MEMORY.ref(4, a3).offset(0x3d8L).get();
            a3 = sp48;
            s3 = s3 - 0x4ef0L;
            a3 = a3 | 0x200L;
            v0 = a1 + 0x8L;
            sp48 = a3;
            a3 = 0x1f80_0000L;
            MEMORY.ref(4, a3).offset(0x3d8L).setu(v0);
            a3 = 0x1L;
            v0 = 0x8005_0000L;
            v0 = v0 + 0x2bc8L;
            MEMORY.ref(1, a1).offset(0x3L).setu(a3);
            s0 = sp22;
            a3 = sp48;
            s0 = s0 << 2;
            s1 = s0 + s1;
            s0 = s0 + v0;
            v1 = MEMORY.ref(4, s1).offset(0x0L).get();
            a0 = MEMORY.ref(4, s0).offset(0x0L).get();
            v1 = v1 & 0x100L;
            v1 = (int)v1 >> 8;
            v1 = v1 << 1;
            v1 = v1 + s3;
            a0 = a0 & 0x3c0L;
            v0 = MEMORY.ref(2, v1).offset(0x0L).get();
            a0 = (int)a0 >> 6;
            v0 = v0 | a0;
            v0 = v0 & 0x9ffL;
            v0 = v0 | a3;
            a3 = 0x1f80_0000L;
            MEMORY.ref(4, a1).offset(0x4L).setu(v0);
            a0 = MEMORY.ref(4, s7).offset(0xcL).get();
            v0 = MEMORY.ref(4, a3).offset(0x3d0L).get();
            a0 = a0 << 2;
            a0 = v0 + a0;
            insertElementIntoLinkedList(a0, a1);
            v1 = 0x6480_0000L;
            a3 = 0x1f80_0000L;
            v0 = MEMORY.ref(4, a3).offset(0x3d8L).get();
            v1 = v1 | 0x8080L;
            s6 = v0;
            v0 = s6 + 0x14L;
            MEMORY.ref(4, a3).offset(0x3d8L).setu(v0);
            v0 = 0x4L;
            MEMORY.ref(1, s6).offset(0x3L).setu(v0);
            MEMORY.ref(4, s6).offset(0x4L).setu(v1);
            a3 = sp28;
            a1 = 0;
            v0 = a3 + 0x1L;
            MEMORY.ref(2, s6).offset(0x8L).setu(v0);
            a3 = sp30;
            a0 = s6;
            v0 = a3 + 0x1L;
            MEMORY.ref(2, s6).offset(0xaL).setu(v0);
            a3 = sp40;
            v0 = s4 | 0x3dL;
            MEMORY.ref(1, s6).offset(0xcL).setu(a3);
            a3 = sp44;
            MEMORY.ref(2, s6).offset(0xeL).setu(v0);
            MEMORY.ref(2, s6).offset(0x12L).setu(s2);
            MEMORY.ref(1, s6).offset(0xdL).setu(a3);
            a3 = 0x8L;
            MEMORY.ref(2, s6).offset(0x10L).setu(a3);
            gpuLinkedListSetCommandTransparency(a0, false);
            a1 = s6;
            a3 = 0x1f80_0000L;
            a0 = MEMORY.ref(4, s7).offset(0xcL).get();
            v0 = MEMORY.ref(4, a3).offset(0x3d0L).get();
            a0 = a0 << 2;
            a0 = a0 + 0x4L;
            a0 = v0 + a0;
            insertElementIntoLinkedList(a0, a1);
            a3 = 0x1f80_0000L;
            a1 = MEMORY.ref(4, a3).offset(0x3d8L).get();

            v0 = a1 + 0x8L;
            MEMORY.ref(4, a3).offset(0x3d8L).setu(v0);
            a3 = 0x1L;
            MEMORY.ref(1, a1).offset(0x3L).setu(a3);
            v0 = MEMORY.ref(4, s1).offset(0x0L).get();
            v1 = MEMORY.ref(4, s0).offset(0x0L).get();
            a3 = sp48;
            v0 = v0 & 0x100L;
            v0 = (int)v0 >> 8;
            v0 = v0 << 1;
            v0 = v0 + s3;
            v1 = v1 & 0x3c0L;
            v0 = MEMORY.ref(2, v0).offset(0x0L).get();
            v1 = (int)v1 >> 6;
            v0 = v0 | v1;
            v0 = v0 & 0x9ffL;
            v0 = v0 | a3;
            a3 = 0x1f80_0000L;
            MEMORY.ref(4, a1).offset(0x4L).setu(v0);
            a0 = MEMORY.ref(4, s7).offset(0xcL).get();
            v0 = MEMORY.ref(4, a3).offset(0x3d0L).get();
            a0 = a0 << 2;
            a0 = a0 + 0x4L;
            a0 = v0 + a0;
            insertElementIntoLinkedList(a0, a1);
          }
        }

        //LAB_800287d4
        v0 = MEMORY.ref(2, s7).offset(0x1eL).getSigned();
        v1 = MEMORY.ref(2, s7).offset(0x1cL).getSigned();
        v0 = v0 + 0x1L;
        lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
        s5 = s5 + 0x1L;
        a3 = lo;
        fp = fp + 0x8L;
      } while((int)s5 < (int)a3);
    }

    //LAB_800287f8
  }

  @Method(0x80028828L)
  public static void FUN_80028828(final int a0) {
    long s0 = _800bdf38.get(a0).getAddress(); //TODO
    MEMORY.ref(2, s0).offset(0x2cL).addu(MEMORY.ref(2, s0).offset(0x2aL).get());
    if(MEMORY.ref(2, s0).offset(0x2cL).getSigned() >= 0xcL) {
      FUN_80027eb4(a0);
      MEMORY.ref(2, s0).offset(0x2cL).setu(0);
      MEMORY.ref(4, s0).offset(0x0L).setu(0x4L);
      MEMORY.ref(2, s0).offset(0x36L).subu(0x1L);
    }

    //LAB_80028894
  }

  @Method(0x800288a4L)
  public static void FUN_800288a4(long a0) {
    if((_800bb0fc.get() & 0x1L) == 0) {
      final long s0 = _800bdf38.get((int)a0).getAddress(); //TODO
      MEMORY.ref(2, s0).offset(0x2cL).addu(MEMORY.ref(2, s0).offset(0x2aL).get() & 0x7L);

      if(MEMORY.ref(2, s0).offset(0x2cL).getSigned() >= 0xcL) {
        FUN_80027eb4(a0);
        MEMORY.ref(2, s0).offset(0x36L).setu(MEMORY.ref(2, s0).offset(0x1eL).get());
        MEMORY.ref(4, s0).offset(0x8L).oru(0x4L);
        MEMORY.ref(2, s0).offset(0x2cL).subu(0xcL);
      }
    }

    //LAB_80028928
  }

  @Method(0x80028938L)
  public static void FUN_80028938(long a0, long a1) {
    long v0;
    long v1;
    long a2;
    long a3;
    long t0;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;
    long sp;
    long fp;
    long ra;
    long sp44;
    long sp54;
    long sp10;
    long sp24;
    long sp34;
    long sp40;
    long sp50;
    long sp20;
    long sp30;
    long sp18;
    long sp48;
    long sp58;
    long sp14;
    long sp38;
    long sp3c;
    long sp4c;
    long sp1c;

    sp58 = a0;
    FUN_800e2428(a1);
    a0 = 0x800c_0000L;
    a0 = a0 + 0x68e8L;
    a1 = 0x800c_0000L;
    a1 = a1 - 0x1ca8L;
    v0 = MEMORY.ref(4, a0).offset(0x70L).get();
    a3 = sp58;
    a2 = MEMORY.ref(4, a0).offset(0x74L).get();
    v1 = MEMORY.ref(4, a0).offset(0x7cL).get();
    a0 = MEMORY.ref(4, a0).offset(0x68L).get();
    s4 = v0;
    v0 = a3 << 2;
    v0 = v0 + a3;
    v0 = v0 << 2;
    v0 = v0 - a3;
    v0 = v0 << 2;
    s3 = v0 + a1;
    v1 = a2 - v1;
    v0 = v1 >>> 31;
    v1 = v1 + v0;
    v1 = (int)v1 >> 1;
    a2 = a2 - v1;
    MEMORY.ref(4, s3).offset(0x28L).setu(s4);
    sp10 = v1;
    sp18 = a2;
    v1 = MEMORY.ref(2, s3).offset(0x18L).getSigned();
    a0 = s4 - a0;
    MEMORY.ref(4, s3).offset(0x2cL).setu(a2);
    sp14 = a0;
    v0 = v1 << 3;
    v0 = v0 + v1;
    v1 = v0 >>> 31;
    v0 = v0 + v1;
    v1 = MEMORY.ref(2, s3).offset(0x1aL).getSigned();
    v0 = (int)v0 >> 1;
    sp1c = v0;
    v0 = v1 << 1;
    v0 = v0 + v1;
    s7 = v0 << 1;
    v0 = 0x8005_0000L;
    v0 = MEMORY.ref(4, v0).offset(-0x22e0L).get();
    t0 = 0x5L;
    if(v0 != t0) {
      fp = 0x140L;
    } else {
      fp = 0x168L;
    }

    //LAB_80028a20
    v0 = 0x800c_0000L;
    a3 = sp58;
    v0 = v0 - 0x20c8L;
    v1 = a3 << 5;
    v1 = v1 + a3;
    v1 = v1 << 2;
    s0 = v1 + v0;
    v0 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();

    if((int)v0 < 0x11L) {
      v0 = s4 << 16;
    } else {
      v0 = s4 << 16;
      t0 = sp18;

      if((int)t0 >= 0x79L) {
        //LAB_80028acc
        v0 = fp >>> 1;
        MEMORY.ref(2, s3).offset(0x14L).setu(v0);
        MEMORY.ref(2, s0).offset(0x14L).setu(v0);
        t0 = sp18;
        a3 = sp10;

        v0 = t0 - a3;
        v0 = v0 - s7;
        MEMORY.ref(2, s3).offset(0x16L).setu(v0);
        v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
        a0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
        MEMORY.ref(2, s0).offset(0x16L).setu(v0);
        v0 = v1 << 3;
        v0 = v0 + v1;
        v1 = v0 >>> 31;
        v0 = v0 + v1;
        v1 = MEMORY.ref(2, s0).offset(0x14L).get();
        v0 = (int)v0 >> 1;
        v1 = v1 - v0;
        v0 = a0 << 1;
        v0 = v0 + a0;
        MEMORY.ref(2, s0).offset(0x18L).setu(v1);
        v1 = MEMORY.ref(2, s0).offset(0x16L).get();
        v0 = v0 << 1;
        v1 = v1 - v0;
        v0 = 0x8L;

        //LAB_80028de4
        MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
        MEMORY.ref(4, s3).offset(0x48L).setu(v0);
        return;
      }

      v0 = fp >>> 1;
      MEMORY.ref(2, s3).offset(0x14L).setu(v0);
      MEMORY.ref(2, s0).offset(0x14L).setu(v0);
      a3 = sp10;

      v0 = t0 + a3;
      v0 = v0 + s7;
      MEMORY.ref(2, s3).offset(0x16L).setu(v0);
      v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
      a0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
      MEMORY.ref(2, s0).offset(0x16L).setu(v0);
      v0 = v1 << 3;
      v0 = v0 + v1;
      v1 = v0 >>> 31;
      v0 = v0 + v1;
      v1 = MEMORY.ref(2, s0).offset(0x14L).get();
      v0 = (int)v0 >> 1;
      v1 = v1 - v0;
      v0 = a0 << 1;
      v0 = v0 + a0;
      MEMORY.ref(2, s0).offset(0x18L).setu(v1);
      v1 = MEMORY.ref(2, s0).offset(0x16L).get();
      v0 = v0 << 1;
      v1 = v1 - v0;
      v0 = 0x7L;

      //LAB_80028de4
      MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
      MEMORY.ref(4, s3).offset(0x48L).setu(v0);
      return;
    }

    //LAB_80028b38
    s2 = (int)v0 >> 16;
    a1 = s2;
    t0 = sp18;
    a3 = sp10;
    a0 = sp58;
    t0 = t0 - a3;
    s1 = t0 - s7;
    a2 = s1 << 16;
    a2 = (int)a2 >> 16;
    sp20 = t0;
    v0 = FUN_80028f20(a0, a1, a2);
    a1 = s2;
    if(v0 != 0) {
      MEMORY.ref(2, s3).offset(0x14L).setu(s4);
      MEMORY.ref(2, s0).offset(0x14L).setu(s4);
      MEMORY.ref(2, s3).offset(0x16L).setu(s1);
      v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
      a0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
      MEMORY.ref(2, s0).offset(0x16L).setu(s1);
      v0 = v1 << 3;
      v0 = v0 + v1;
      v1 = v0 >>> 31;
      v0 = v0 + v1;
      v1 = MEMORY.ref(2, s0).offset(0x14L).get();
      v0 = (int)v0 >> 1;
      v1 = v1 - v0;
      v0 = a0 << 1;
      v0 = v0 + a0;
      MEMORY.ref(2, s0).offset(0x18L).setu(v1);
      v1 = s1;
      v0 = v0 << 1;
      v1 = v1 - v0;
      MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
      MEMORY.ref(4, s3).offset(0x48L).setu(0);
      return;
    }

    //LAB_80028bc4
    t0 = sp18;
    a3 = sp10;
    a0 = sp58;
    t0 = t0 + a3;
    s1 = t0 + s7;
    a2 = s1 << 16;
    a2 = (int)a2 >> 16;
    sp24 = t0;
    v0 = FUN_80028f20(a0, a1, a2);
    if(v0 != 0) {
      MEMORY.ref(2, s3).offset(0x14L).setu(s4);
      MEMORY.ref(2, s0).offset(0x14L).setu(s4);
      MEMORY.ref(2, s3).offset(0x16L).setu(s1);
      v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
      a0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
      MEMORY.ref(2, s0).offset(0x16L).setu(s1);
      v0 = v1 << 3;
      v0 = v0 + v1;
      v1 = v0 >>> 31;
      v0 = v0 + v1;
      v1 = MEMORY.ref(2, s0).offset(0x14L).get();
      v0 = (int)v0 >> 1;
      v1 = v1 - v0;
      v0 = a0 << 1;
      v0 = v0 + a0;
      MEMORY.ref(2, s0).offset(0x18L).setu(v1);
      v1 = s1;
      v0 = v0 << 1;
      v1 = v1 - v0;
      v0 = 0x1L;

      //LAB_80028de4
      MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
      MEMORY.ref(4, s3).offset(0x48L).setu(v0);
      return;
    }

    //LAB_80028c44
    v0 = fp >>> 1;
    if((int)v0 < (int)s4) {
      s5 = (int)s7 >> 1;
      //LAB_80028d58
      a0 = sp58;
      t0 = sp14;
      a3 = sp1c;
      v0 = s4 - t0;
      s2 = v0 - a3;
      v0 = s2 << 16;
      s6 = (int)v0 >> 16;
      t0 = sp20;
      a1 = s6;
      s1 = t0 - s5;
      a2 = s1 << 16;
      a2 = (int)a2 >> 16;
      v0 = FUN_80028f20(a0, a1, a2);
      a1 = s6;
      if(v0 != 0) {
        MEMORY.ref(2, s3).offset(0x14L).setu(s2);
        MEMORY.ref(2, s0).offset(0x14L).setu(s2);
        MEMORY.ref(2, s3).offset(0x16L).setu(s1);
        v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
        a0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
        MEMORY.ref(2, s0).offset(0x16L).setu(s1);
        v0 = v1 << 3;
        v0 = v0 + v1;
        v1 = v0 >>> 31;
        v0 = v0 + v1;
        v1 = MEMORY.ref(2, s0).offset(0x14L).get();
        v0 = (int)v0 >> 1;
        v1 = v1 - v0;
        v0 = a0 << 1;
        v0 = v0 + a0;
        MEMORY.ref(2, s0).offset(0x18L).setu(v1);
        v1 = s1;
        v0 = v0 << 1;
        v1 = v1 - v0;
        v0 = 0x4L;

        //LAB_80028de4
        MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
        MEMORY.ref(4, s3).offset(0x48L).setu(v0);
        return;
      }

      //LAB_80028df0
      a3 = sp24;
      a0 = sp58;
      s1 = a3 + s5;
      a2 = s1 << 16;
      a2 = (int)a2 >> 16;
      v0 = FUN_80028f20(a0, a1, a2);
      t0 = 0x5L;
      if(v0 != 0) {
        MEMORY.ref(2, s3).offset(0x14L).setu(s2);
        MEMORY.ref(2, s0).offset(0x14L).setu(s2);
        MEMORY.ref(2, s3).offset(0x16L).setu(s1);
        v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
        a0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
        MEMORY.ref(2, s0).offset(0x16L).setu(s1);
        v0 = v1 << 3;
        v0 = v0 + v1;
        v1 = v0 >>> 31;
        v0 = v0 + v1;
        v1 = MEMORY.ref(2, s0).offset(0x14L).get();
        v0 = (int)v0 >> 1;
        v1 = v1 - v0;
        v0 = a0 << 1;
        v0 = v0 + a0;
        MEMORY.ref(2, s0).offset(0x18L).setu(v1);
        v1 = s1;
        v0 = v0 << 1;
        v1 = v1 - v0;
        MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
        MEMORY.ref(4, s3).offset(0x48L).setu(t0);
        return;
      }
    } else {
      s5 = (int)s7 >> 1;
      a0 = sp58;
      t0 = sp14;
      a3 = sp1c;
      v0 = s4 + t0;
      s2 = v0 + a3;
      v0 = s2 << 16;
      s6 = (int)v0 >> 16;
      t0 = sp20;
      a1 = s6;
      s1 = t0 - s5;
      a2 = s1 << 16;
      a2 = (int)a2 >> 16;
      v0 = FUN_80028f20(a0, a1, a2);
      if(v0 == 0) {
        a1 = s6;
      } else {
        a1 = s6;
        MEMORY.ref(2, s3).offset(0x14L).setu(s2);
        MEMORY.ref(2, s0).offset(0x14L).setu(s2);
        MEMORY.ref(2, s3).offset(0x16L).setu(s1);
        v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
        a0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
        MEMORY.ref(2, s0).offset(0x16L).setu(s1);
        v0 = v1 << 3;
        v0 = v0 + v1;
        v1 = v0 >>> 31;
        v0 = v0 + v1;
        v1 = MEMORY.ref(2, s0).offset(0x14L).get();
        v0 = (int)v0 >> 1;
        v1 = v1 - v0;
        v0 = a0 << 1;
        v0 = v0 + a0;
        MEMORY.ref(2, s0).offset(0x18L).setu(v1);
        v1 = s1;
        v0 = v0 << 1;
        v1 = v1 - v0;
        v0 = 0x2L;

        //LAB_80028de4
        MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
        MEMORY.ref(4, s3).offset(0x48L).setu(v0);
        return;
      }

      //LAB_80028ce4
      a3 = sp24;
      a0 = sp58;
      s1 = a3 + s5;
      a2 = s1 << 16;
      a2 = (int)a2 >> 16;
      v0 = FUN_80028f20(a0, a1, a2);
      if(v0 != 0) {
        MEMORY.ref(2, s3).offset(0x14L).setu(s2);
        MEMORY.ref(2, s0).offset(0x14L).setu(s2);
        MEMORY.ref(2, s3).offset(0x16L).setu(s1);
        v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
        a0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
        MEMORY.ref(2, s0).offset(0x16L).setu(s1);
        v0 = v1 << 3;
        v0 = v0 + v1;
        v1 = v0 >>> 31;
        v0 = v0 + v1;
        v1 = MEMORY.ref(2, s0).offset(0x14L).get();
        v0 = (int)v0 >> 1;
        v1 = v1 - v0;
        v0 = a0 << 1;
        v0 = v0 + a0;
        MEMORY.ref(2, s0).offset(0x18L).setu(v1);
        v1 = s1;
        v0 = v0 << 1;
        v1 = v1 - v0;
        v0 = 0x3L;

        //LAB_80028de4
        MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
        MEMORY.ref(4, s3).offset(0x48L).setu(v0);
        return;
      }
    }

    //LAB_80028e68
    v0 = fp >>> 1;
    if((int)v0 >= (int)s4) {
      a3 = sp14;
      t0 = sp1c;
      v0 = s4 + a3;
      v0 = v0 + t0;
    } else {
      //LAB_80028e8c
      a3 = sp14;
      t0 = sp1c;
      v0 = s4 - a3;
      v0 = v0 - t0;
    }

    //LAB_80028e9c
    a1 = v0 << 16;
    a1 = (int)a1 >> 16;
    a3 = sp18;
    t0 = sp10;
    a0 = sp58;
    a2 = a3 + t0;
    a2 = a2 + s7;
    a2 = a2 << 16;
    a2 = (int)a2 >> 16;
    FUN_80027d74(a0, a1, a2);
    v1 = 0x800c_0000L;
    a3 = sp58;
    v1 = v1 - 0x1ca8L;
    v0 = a3 << 2;
    v0 = v0 + a3;
    v0 = v0 << 2;
    v0 = v0 - a3;
    v0 = v0 << 2;
    v0 = v0 + v1;
    v1 = 0x6L;
    MEMORY.ref(4, v0).offset(0x48L).setu(v1);

    //LAB_80028ef0
  }

  @Method(0x80028f20L)
  public static long FUN_80028f20(final long a0, final long a1, final long a2) {
    final long t1;
    if(mainCallbackIndex_8004dd20.get() == 0x5L) {
      t1 = 0x15eL;
    } else {
      t1 = 0x136L;
    }

    //LAB_80028f40
    //LAB_80028fa8
    //LAB_80028fc0
    //LAB_80028fd4
    if(
      a1 - _800be358.get((int)a0)._18.get() * 9 / 2 < 0xaL ||
      a1 + _800be358.get((int)a0)._18.get() * 9 / 2 > (int)t1 ||
      a2 - _800be358.get((int)a0)._1a.get() * 6 < 0x12L ||
      a2 + _800be358.get((int)a0)._1a.get() * 6 > 0xdeL
    ) {
      return 0;
    }

    //LAB_80028ff0
    return 0x1L;
  }

  @Method(0x80029140L)
  public static void FUN_80029140(long a0, long a1) {
    long v0;
    long v1;
    long a2;
    long a3;
    long t0;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;

    s4 = 0x1f80_0000L;
    s1 = MEMORY.ref(4, s4).offset(0x3d8L).get();
    s2 = a0;
    s3 = a1;
    a0 = s1;
    v0 = s1 + 0x18L;
    MEMORY.ref(4, s4).offset(0x3d8L).setu(v0);
    FUN_8003b570(a0);
    a0 = s1;
    v0 = 0x800c_0000L;
    v0 = v0 - 0x1ca8L;
    s0 = s2 << 2;
    s0 = s0 + s2;
    s0 = s0 << 2;
    s0 = s0 - s2;
    s0 = s0 << 2;
    s0 = s0 + v0;
    a2 = 0x1f80_0000L;
    s3 = s3 << 16;
    s3 = (int)s3 >> 16;
    v0 = MEMORY.ref(2, s0).offset(0x18L).getSigned();
    a3 = MEMORY.ref(2, a2).offset(0x3dcL).get();
    t0 = MEMORY.ref(2, s0).offset(0x1aL).getSigned();
    v0 = v0 - 0x1L;
    v1 = v0 << 3;
    v1 = v1 + v0;
    v0 = v1 >>> 31;
    v1 = v1 + v0;
    v1 = (int)v1 >> 0x1L;
    v0 = MEMORY.ref(2, s0).offset(0x14L).get();
    t0 = t0 - 0x1L;
    v0 = v0 - v1;
    v0 = v0 - a3;
    MEMORY.ref(2, s1).offset(0x8L).setu(v0);
    v0 = MEMORY.ref(2, s0).offset(0x14L).get();
    a3 = MEMORY.ref(2, a2).offset(0x3dcL).get();
    v0 = v0 - v1;
    v0 = v0 - a3;
    MEMORY.ref(2, s1).offset(0x10L).setu(v0);
    v0 = MEMORY.ref(2, s0).offset(0x14L).get();
    a3 = MEMORY.ref(2, a2).offset(0x3dcL).get();
    v0 = v0 + v1;
    v0 = v0 - a3;
    MEMORY.ref(2, s1).offset(0xcL).setu(v0);
    v0 = MEMORY.ref(2, s0).offset(0x14L).get();
    a3 = MEMORY.ref(2, a2).offset(0x3dcL).get();
    a2 = a2 + 0x3dcL;
    v0 = v0 + v1;
    v0 = v0 - a3;
    MEMORY.ref(2, s1).offset(0x14L).setu(v0);
    a3 = MEMORY.ref(2, s0).offset(0x16L).get();
    a2 = MEMORY.ref(2, a2).offset(0x2L).get();
    v0 = 0x80L;
    MEMORY.ref(1, s1).offset(0x4L).setu(v0);
    v0 = 0x32L;
    MEMORY.ref(1, s1).offset(0x5L).setu(v0);
    v0 = 0x64L;
    v1 = t0 << 0x1L;
    v1 = v1 + t0;
    v1 = v1 << 0x1L;
    MEMORY.ref(1, s1).offset(0x6L).setu(v0);
    v0 = s3 << 0x1L;
    v0 = v0 + s3;
    v0 = v0 << 2;
    a3 = a3 - v1;
    a3 = a3 - a2;
    v0 = v0 + a3;
    MEMORY.ref(2, s1).offset(0xaL).setu(v0);
    MEMORY.ref(2, s1).offset(0xeL).setu(v0);
    v0 = v0 + 0xcL;
    MEMORY.ref(2, s1).offset(0x12L).setu(v0);
    MEMORY.ref(2, s1).offset(0x16L).setu(v0);
    gpuLinkedListSetCommandTransparency(a0, true);
    a1 = s1;
    s1 = 0x1f80_0000L;
    a0 = MEMORY.ref(4, s0).offset(0xcL).get();
    v0 = MEMORY.ref(4, s1).offset(0x3d0L).get();
    a0 = a0 << 2;
    a0 = v0 + a0;
    insertElementIntoLinkedList(a0, a1);
    v1 = 0xe100_0000L;
    a1 = MEMORY.ref(4, s4).offset(0x3d8L).get();
    v1 = v1 | 0x200L;
    v0 = a1 + 0x8L;
    MEMORY.ref(4, s4).offset(0x3d8L).setu(v0);
    v0 = 0x1L;
    MEMORY.ref(1, a1).offset(0x3L).setu(v0);
    v0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, s1).offset(0x3d0L).get();
    v0 = MEMORY.ref(2, v0).offset(-0x4eeeL).get();
    a0 = a0 + 0x30L;
    v0 = v0 | 0xdL;
    v0 = v0 & 0x9ffL;
    v0 = v0 | v1;
    MEMORY.ref(4, a1).offset(0x4L).setu(v0);
    insertElementIntoLinkedList(a0, a1);
  }

  @Method(0x80029300L)
  public static void renderText(final LodString text, final long x, long y, final long a3, final long a4) {
    //LAB_80029358
    int length;
    for(length = 0; ; length++) {
      final long c = text.charAt(length);

      if(c == 0xa0ffL) {
        currentText_800bdca0.charAt(length, 0xffffL);
        break;
      }

      //LAB_80029374
      currentText_800bdca0.charAt(length, c);

      //LAB_80029384
    }

    final long s7 = MathHelper.clamp(a4, -0xcL, 0xcL);

    //LAB_800293bc
    //LAB_800293d8
    long lineIndex = 0;
    long glyphNudge = 0;

    for(int i = 0; i < length; i++) {
      final long c = currentText_800bdca0.charAt(i);

      if(c == 0xa1ffL) {
        lineIndex = 0;
        glyphNudge = 0;
        y += 0xcL;
      } else {
        //LAB_80029404
        if(c < 0x340L) {
          //LAB_8002945c
          _800be5b8.setu(c / 208);
          _800be5bc.setu(0);
          _800be5c0.setu(c & 0xfL);
          _800be5c8.setu(c % 208 / 16);
        } else {
          //LAB_8002946c
          final long a0_0 = (c - 832) / 16;

          //LAB_80029480
          _800be5b8.setu(a0_0 % 4);
          _800be5bc.setu(a0_0 / 4 + 1);
          _800be5c0.setu(c & 0xfL);
          _800be5c8.setu(0);
        }

        //LAB_800294b4
        final long fp = _800be5bc.get();

        final long packet1 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x14L);

        MEMORY.ref(1, packet1).offset(0x03L).setu(0x4L);
        MEMORY.ref(4, packet1).offset(0x04L).setu(0x6480_8080L); // Textured rect, variable size, opaque, texture-blending

        if(lineIndex == 0) {
          glyphNudge = 0;
        }

        //LAB_80029504
        //LAB_80029534
        if(c == 0x45L) {
          glyphNudge -= 0x1L;
        } else if(c == 0x2L) {
          //LAB_80029548
          glyphNudge -= 0x2L;
        } else if(c >= 0x5L && c < 0x7L) {
          //LAB_80029550
          glyphNudge -= 0x3L;
        }

        //LAB_80029554
        //LAB_80029558
        MEMORY.ref(2, packet1).offset(0x08L).setu(x + lineIndex * 8 - centreScreenX_1f8003dc.get() - glyphNudge); // x

        glyphNudge += switch((int)c) {
          case 0x5, 0x23, 0x24, 0x2a, 0x37, 0x38, 0x3a, 0x3b, 0x3c, 0x3d, 0x3f, 0x40, 0x43, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4d, 0x4e, 0x51, 0x52 -> 0x1L;
          case 0x2, 0x8, 0x3e, 0x4c -> 0x2L;
          case 0xb, 0xc, 0x42 -> 0x3L;
          case 0x1, 0x3, 0x4, 0x9, 0x16, 0x41, 0x44 -> 0x4L;
          case 0x6, 0x27 -> 0x5L;
          default -> 0;
        };

        //LAB_800295d8
        lineIndex++;

        MEMORY.ref(2, packet1).offset(0x0aL).setu(y - centreScreenY_1f8003de.get()); // y

        final long v1;
        if(fp < 0x6L) {
          v1 = _800be5c8.get() * 0xcL;
        } else {
          v1 = 0xf0L + _800be5c8.get() * 0xcL;
        }

        //LAB_80029618
        if((short)s7 >= 0) {
          MEMORY.ref(1, packet1).offset(0x0dL).setu(v1); // v
          MEMORY.ref(2, packet1).offset(0x12L).setu(0xcL - s7); // height
        } else {
          //LAB_80029648
          MEMORY.ref(1, packet1).offset(0x0dL).setu(v1 - s7); // v
          MEMORY.ref(2, packet1).offset(0x12L).setu(s7 + 0xcL); // height
        }

        //LAB_80029658
        MEMORY.ref(1, packet1).offset(0x0cL).setu(_800be5c0.get() * 0x10L); // u
        MEMORY.ref(2, packet1).offset(0x0eL).setu((_800be5b8.get() + 0x1e0L) * 0x40L | (((a3 & 0xfL) * 0x10L + 0x340L) & 0x3f0L) >> 4); // clut
        MEMORY.ref(2, packet1).offset(0x10L).setu(0x8L); // width
        gpuLinkedListSetCommandTransparency(packet1, false);
        insertElementIntoLinkedList(tags_1f8003d0.deref().get((int)_800bdf00.get()).getAddress(), packet1);

        final long packet2 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x8L);

        MEMORY.ref(1, packet2).offset(0x3L).setu(0x1L);
        MEMORY.ref(4, packet2).offset(0x4L).setu(0xe100_0200L | (_800bb110.offset(((_80052bf4.offset(fp * 0x4L).get() & 0x100L) >> 8) * 0x2L).get() | (_80052bc8.offset(fp * 0x4L).get() & 0x3c0L) >> 6) & 0x9ffL);
        insertElementIntoLinkedList(tags_1f8003d0.deref().get((int)_800bdf00.get()).getAddress(), packet2);
      }

      //LAB_80029760
    }

    //LAB_80029770
  }

  @Method(0x80029920L)
  public static void FUN_80029920(final int a0, final long a1) {
    if(a1 == 0) {
      _800bdea0.offset(a0 * 0xcL).setu(0);
    } else {
      //LAB_80029948
      _800bdea0.offset(a0 * 0xcL).oru(0x1L);
    }

    //LAB_80029970
    final Struct4c struct = _800be358.get(a0);
    final long v1 = _800bdea0.offset(a0 * 0xcL).getAddress();
    MEMORY.ref(2, v1).offset(0x4L).setu(struct._14.get());
    MEMORY.ref(2, v1).offset(0x8L).setu(0);
    MEMORY.ref(2, v1).offset(0x6L).setu(struct._16.get() + struct._1a.get() * 6L);
  }

  @Method(0x800299d4L)
  public static void FUN_800299d4(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long s0;
    long s1;
    long s2;

    v0 = 0x800c_0000L;
    v0 = v0 - 0x2160L;
    v1 = a0 << 1;
    v1 = v1 + a0;
    v1 = v1 << 2;
    s1 = v1 + v0;
    v0 = MEMORY.ref(4, s1).offset(0x0L).get();

    v0 = v0 & 0x1L;
    if(v0 != 0) {
      v0 = 0x800c_0000L;
      v0 = v0 - 0x20c8L;
      v1 = a0 << 5;
      v1 = v1 + a0;
      v1 = v1 << 2;
      s2 = v1 + v0;
      v0 = MEMORY.ref(4, s2).offset(0x8L).get();

      v0 = v0 & 0x1000L;
      if(v0 != 0) {
        v1 = 0x1f80_0000L;
        s0 = MEMORY.ref(4, v1).offset(0x3d8L).get();

        a0 = s0;
        v0 = s0 + 0x28L;
        MEMORY.ref(4, v1).offset(0x3d8L).setu(v0);
        FUN_8003b590(a0);
        a0 = s0;
        a1 = 0x1L;
        gpuLinkedListSetCommandTransparency(a0, true);
        v0 = 0x80L;
        a0 = 0x1f80_0000L;
        a2 = 0x380L;
        MEMORY.ref(1, s0).offset(0x4L).setu(v0);
        MEMORY.ref(1, s0).offset(0x5L).setu(v0);
        MEMORY.ref(1, s0).offset(0x6L).setu(v0);
        v0 = MEMORY.ref(2, a0).offset(0x3dcL).get();
        v1 = MEMORY.ref(2, s1).offset(0x4L).get();
        v0 = v0 + 0x8L;
        v1 = v1 - v0;
        MEMORY.ref(2, s0).offset(0x18L).setu(v1);
        MEMORY.ref(2, s0).offset(0x8L).setu(v1);
        v0 = MEMORY.ref(2, a0).offset(0x3dcL).get();
        a0 = a0 + 0x3dcL;
        v1 = MEMORY.ref(2, s1).offset(0x4L).get();
        v0 = v0 - 0x8L;
        v1 = v1 - v0;
        MEMORY.ref(2, s0).offset(0x20L).setu(v1);
        MEMORY.ref(2, s0).offset(0x10L).setu(v1);
        v1 = MEMORY.ref(2, a0).offset(0x2L).get();
        v0 = MEMORY.ref(2, s1).offset(0x6L).get();
        a3 = 0x100L;
        v0 = v0 - v1;
        v0 = v0 - 0x6L;
        MEMORY.ref(2, s0).offset(0x12L).setu(v0);
        MEMORY.ref(2, s0).offset(0xaL).setu(v0);
        v1 = MEMORY.ref(2, a0).offset(0x2L).get();
        v0 = MEMORY.ref(2, s1).offset(0x6L).get();
        a0 = 0;
        v0 = v0 - v1;
        v0 = v0 + 0x8L;
        MEMORY.ref(2, s0).offset(0x22L).setu(v0);
        MEMORY.ref(2, s0).offset(0x1aL).setu(v0);
        v0 = MEMORY.ref(2, s1).offset(0x8L).getSigned();
        a1 = a0;
        v0 = v0 << 4;
        v0 = v0 + 0x40L;
        MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
        MEMORY.ref(1, s0).offset(0xcL).setu(v0);
        v1 = MEMORY.ref(2, s1).offset(0x8L).getSigned();
        v0 = 0xeL;
        MEMORY.ref(1, s0).offset(0x25L).setu(v0);
        MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
        v0 = 0x793fL;
        MEMORY.ref(1, s0).offset(0x15L).setu(0);
        MEMORY.ref(1, s0).offset(0xdL).setu(0);
        MEMORY.ref(2, s0).offset(0xeL).setu(v0);
        v1 = v1 << 4;
        v1 = v1 + 0x50L;
        MEMORY.ref(1, s0).offset(0x24L).setu(v1);
        MEMORY.ref(1, s0).offset(0x14L).setu(v1);
        v0 = GetTPage(a0, a1, a2, a3);
        a1 = s0;
        MEMORY.ref(2, a1).offset(0x16L).setu(v0);
        v0 = 0x1f80_0000L;
        a0 = MEMORY.ref(4, s2).offset(0xcL).get();
        v0 = MEMORY.ref(4, v0).offset(0x3d0L).get();
        a0 = a0 << 2;
        a0 = v0 + a0;
        insertElementIntoLinkedList(a0, a1);
      }
    }

    //LAB_80029b50
  }

  @Method(0x80029b68L)
  public static long FUN_80029b68(final RunningScript a0) {
    //LAB_80029b7c
    for(int i = 0; i < 8; i++) {
      if(_800be358.get(i)._00.get() == 0 && _800bdf38.get(i)._00.get() == 0) {
        a0.params_20.get(0).deref().set(i);
        return 0;
      }

      //LAB_80029bac
      //LAB_80029bb0
    }

    a0.params_20.get(0).deref().set(0xffff_ffffL);
    return 0;
  }

  @Method(0x80029c98L)
  public static long FUN_80029c98(final RunningScript a0) {
    final int a2 = (int)a0.params_20.get(0).deref().get();
    a0.params_20.get(1).deref().set(_800be358.get(a2)._00.get() | _800bdf38.get(a2)._00.get());
    return 0;
  }

  @Method(0x80029d6cL)
  public static long FUN_80029d6c(RunningScript a0) {
    long s1 = a0.params_20.get(0).deref().get();
    final Struct84 struct84 = _800bdf38.get((int)s1);

    if(struct84._00.get() != 0) {
      removeFromLinkedList(struct84.ptr_58.get());
    }

    //LAB_80029db8
    struct84._00.set(0);
    _800be358.get((int)s1)._00.set(0);
    FUN_80029920((int)s1, 0);
    return 0;
  }

  @Method(0x80029e04L)
  public static void FUN_80029e04() {
    //LAB_80029e2c
    for(int i = 0; i < 8; i++) {
      final Struct4c s2 = _800be358.get(i);
      final Struct84 s0 = _800bdf38.get(i);

      if(s0._00.get() != 0) {
        removeFromLinkedList(s0.ptr_58.get());
      }

      //LAB_80029e48
      s0._00.set(0);
      s2._00.set(0);
      FUN_80029920(i, 0);
    }
  }

  @Method(0x8002a058L)
  public static void FUN_8002a058() {
    //LAB_8002a080
    for(int i = 0; i < 8; i++) {
      if(_800be358.get(i)._00.get() != 0) {
        FUN_80025a04(i);
      }

      //LAB_8002a098
      if(_800bdf38.get(i)._00.get() != 0) {
        FUN_800264b0(i);
      }
    }

    FUN_80024994();
  }

  @Method(0x8002a0e4L)
  public static void FUN_8002a0e4() {
    //LAB_8002a10c
    for(int i = 0; i < 8; i++) {
      final Struct4c struct4c = _800be358.get(i);

      if(struct4c._00.get() != 0 && (int)struct4c._08.get() < 0) {
        FUN_80025f4c(i);
      }

      //LAB_8002a134
      if(_800bdf38.get(i)._00.get() != 0) {
        FUN_800282ac(i);
        FUN_800299d4(i);
      }

      //LAB_8002a154
    }
  }

  @Method(0x8002a180L)
  public static void FUN_8002a180(long a0, long a1, long a2, long a3, long a4) {
    long v0;
    long v1;

    v1 = _800bdf38.get((int)a0).getAddress();
    a0 = MEMORY.ref(2, v1).offset(0x36L).getSigned() * MEMORY.ref(2, v1).offset(0x1cL).getSigned() + MEMORY.ref(2, v1).offset(0x34L).getSigned();
    v0 = MEMORY.ref(4, v1).offset(0x58L).get() + a0 * 0x8L;
    MEMORY.ref(2, v0).offset(0x0L).setu(a1);
    MEMORY.ref(2, v0).offset(0x2L).setu(a2);

    if((MEMORY.ref(4, v1).offset(0x8L).get() & 0x200L) != 0 && (short)a2 == 0) {
      a3 = 0x8L;
    }

    //LAB_8002a1e8
    //LAB_8002a1ec
    v0 = MEMORY.ref(4, v1).offset(0x58L).get() + a0 * 0x8L;
    MEMORY.ref(1, v0).offset(0x4L).setu(a3);
    MEMORY.ref(2, v0).offset(0x6L).setu(a4);
  }

  @Method(0x8002a1fcL)
  public static long FUN_8002a1fc(final long a0) {
    //LAB_8002a254
    return switch((int)(a0 & 0xffff)) {
      case 0x5, 0x23, 0x24, 0x2a, 0x37, 0x38, 0x3a, 0x3b, 0x3c, 0x3d, 0x3f, 0x40, 0x43, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4d, 0x4e, 0x51, 0x52 -> 0x1L;
      case 0x2, 0x8, 0x3e, 0x4c -> 0x2L;
      case 0xb, 0xc, 0x42 -> 0x3L;
      case 0x1, 0x3, 0x4, 0x9, 0x16, 0x41, 0x44 -> 0x4L;
      case 0x6, 0x27 -> 0x5L;
      default -> 0;
    };
  }

  @Method(0x8002a25cL)
  public static long FUN_8002a25c(long a0) {
    a0 = a0 & 0xffffL;

    //LAB_8002a288
    if(a0 == 0x45L) {
      return 0x1L;
    }

    if(a0 == 0x2L) {
      //LAB_8002a29c
      return 0x2L;
    }

    if((int)a0 < 0x5L) {
      return 0;
    }

    if((int)a0 < 0x7L) {
      //LAB_8002a2a4
      return 0x3L;
    }

    //LAB_8002a2a8
    //LAB_8002a2ac
    return 0;
  }

  @Method(0x8002a2b4L)
  public static void FUN_8002a2b4(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long lo;

    v1 = 0x800c_0000L;
    v1 = v1 - 0x20c8L;
    v0 = a0 << 5;
    v0 = v0 + a0;
    v0 = v0 << 2;
    v0 = v0 + v1;
    a2 = v0;
    v0 = MEMORY.ref(2, a2).offset(0x1eL).getSigned();
    v1 = MEMORY.ref(2, a2).offset(0x1cL).getSigned();
    v0 = v0 + 0x1L;
    lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
    a0 = MEMORY.ref(4, a2).offset(0x58L).get();
    a3 = lo;

    if((int)a3 > 0) {
      a1 = 0;

      //LAB_8002a2f0
      do {
        MEMORY.ref(2, a0).offset(0x0L).setu(0);
        MEMORY.ref(2, a0).offset(0x2L).setu(0);
        MEMORY.ref(1, a0).offset(0x4L).setu(0);
        MEMORY.ref(2, a0).offset(0x6L).setu(0);
        v0 = MEMORY.ref(2, a2).offset(0x1eL).getSigned();
        v1 = MEMORY.ref(2, a2).offset(0x1cL).getSigned();
        v0 = v0 + 0x1L;
        lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
        a1 = a1 + 0x1L;
        a3 = lo;
        a0 = a0 + 0x8L;
      } while((int)a1 < (int)a3);
    }

    //LAB_8002a324
  }

  @Method(0x8002a32cL)
  public static void FUN_8002a32c(final int a0, final long a1, final long a2, final long a3, final long a4, final long a5) {
    FUN_800257e0(a0);

    final Struct4c struct = _800be358.get(a0);
    struct._04.set((short)((a1 & 1) + 1));
    struct._06.set((short)1);
    struct._08.or(0x4L);

    struct._14.set((int)a2);
    struct._16.set((int)a3);
    struct._18.set((short)(a4 + 1));
    struct._1a.set((short)(a5 + 1));
  }

  @Method(0x8002a3ecL)
  public static void FUN_8002a3ec(final int a0, final long a1) {
    if((a1 & 0x1L) == 0) {
      //LAB_8002a40c
      _800bdf38.get(a0)._00.set(0);
      _800be358.get(a0)._00.set(0);
    } else {
      //LAB_8002a458
      _800be358.get(a0)._00.set(0x3L);
    }
  }

  @Method(0x8002a488L)
  public static long FUN_8002a488(final long a0) {
    return _800be358.get((int)a0)._00.get() == 6 ? 1 : 0;
  }

  @Method(0x8002a4c4L)
  public static void FUN_8002a4c4(final long a0) {
    final long a1 = _800bdea0.offset(a0 * 0xcL).getAddress(); //TODO struct

    if((MEMORY.ref(4, a1).offset(0x0L).get() & 0x1L) != 0) {
      if((_800bdf38.get((int)a0)._08.get() & 0x1000L) != 0) {
        if((_800bb0fc.get() & 0x1L) == 0) {
          MEMORY.ref(2, a1).offset(0x8L).addu(0x1L);
        }

        //LAB_8002a53c
        if(MEMORY.ref(2, a1).offset(0x8L).getSigned() >= 0x7L) {
          MEMORY.ref(2, a1).offset(0x8L).setu(0);
        }
      }
    }

    //LAB_8002a554
  }

  @Method(0x8002a63cL)
  public static void FUN_8002a63c(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long hi;

    a2 = a0;
    a0 = a2 & 0xffffL;
    if(a0 < 0x340L) {
      v1 = 0x800c_0000L;
      v0 = 0x4ec4_0000L;
      v0 = v0 | 0xec4fL;
      hi = (a0 & 0xffff_ffffL) * (v0 & 0xffff_ffffL) >>> 32;
      v0 = 0x800c_0000L;
      MEMORY.ref(4, v0).offset(-0x1a44L).setu(0);
      v0 = a2 & 0xfL;
      MEMORY.ref(4, v1).offset(-0x1a40L).setu(v0);
      a3 = hi;
      v1 = a3 >>> 6;
      v1 = v1 & 0xffffL;
      v0 = v1 << 1;
      v0 = v0 + v1;
      v0 = v0 << 2;
      v0 = v0 + v1;
      v0 = v0 << 4;
      a0 = a0 - v0;
      v0 = 0x800c_0000L;
      MEMORY.ref(4, v0).offset(-0x1a48L).setu(v1);
      if((int)a0 < 0) {
        a0 = a0 + 0xfL;
      }

      //LAB_8002a6a0
      v1 = (int)a0 >> 4;
      v0 = 0x800c_0000L;
      MEMORY.ref(4, v0).offset(-0x1a38L).setu(v1);
      return;
    }

    //LAB_8002a6b0
    v0 = a0 - 0x340L;
    a1 = (int)v0 >> 4;
    if((int)a1 >= 0) {
      v0 = a1;
    } else {
      v0 = a1 + 0x3L;
    }

    //LAB_8002a6c4
    v0 = (int)v0 >> 2;
    a0 = v0 + 0x1L;
    v1 = 0x800c_0000L;
    MEMORY.ref(4, v1).offset(-0x1a44L).setu(a0);
    v1 = 0x800c_0000L;
    v0 = v0 << 2;
    v0 = a1 - v0;
    MEMORY.ref(4, v1).offset(-0x1a48L).setu(v0);
    v1 = 0x800c_0000L;
    v0 = a2 & 0xfL;
    MEMORY.ref(4, v1).offset(-0x1a40L).setu(v0);
    v0 = 0x800c_0000L;
    MEMORY.ref(4, v0).offset(-0x1a38L).setu(0);
  }

  @Method(0x8002a6fcL)
  public static void FUN_8002a6fc() {
    long s0 = 0;

    //LAB_8002a730
    for(int s2 = 0; s2 < 9; s2++) {
      _800be5f8.offset(4, s0).setu(0);
      _800be5fc.offset(2, s0).setu(0);
      _800be5fe.offset(2, s0).setu(0);
      _800be600.offset(2, s0).setu(0);
      _800be602.offset(2, s0).setu(0);
      _800be604.offset(2, s0).setu(0);
      _800be606.offset(1, s0).setu(0);
      _800be607.offset(1, s0).setu(0);

      //LAB_8002a758
      for(int i = 0; i < 5; i++) {
        _800be628.offset(1, s0).offset(i).setu(0xffL);
      }

      _800be62d.offset(1, s0).setu(0);

      //LAB_8002a780;
      for(int i = 0; i < 8; i++) {
        _800be62e.offset(1, s0).offset(i).setu(0);
        _800be636.offset(1, s0).offset(i).setu(0);
      }

      _800be63e.offset(2, s0).setu(0);
      _800be640.offset(2, s0).setu(0);
      _800be642.offset(2, s0).setu(0);
      _800be644.offset(2, s0).setu(0);
      _800be646.offset(2, s0).setu(0);
      _800be648.offset(2, s0).setu(0);
      _800be64a.offset(2, s0).setu(0);
      _800be64c.offset(2, s0).setu(0);
      _800be64e.offset(2, s0).setu(0);
      _800be650.offset(2, s0).setu(0);
      _800be652.offset(2, s0).setu(0);
      _800be654.offset(2, s0).setu(0);
      _800be656.offset(2, s0).setu(0);
      _800be658.offset(2, s0).setu(0);
      _800be65a.offset(2, s0).setu(0);
      _800be65c.offset(2, s0).setu(0);
      _800be65e.offset(2, s0).setu(0);
      _800be660.offset(1, s0).setu(0);
      _800be661.offset(1, s0).setu(0);
      _800be662.offset(1, s0).setu(0);
      _800be663.offset(1, s0).setu(0);
      _800be664.offset(1, s0).setu(0);
      _800be665.offset(1, s0).setu(0);
      _800be666.offset(2, s0).setu(0);
      _800be668.offset(1, s0).setu(0);
      _800be669.offset(1, s0).setu(0);
      _800be66a.offset(1, s0).setu(0);
      _800be66b.offset(1, s0).setu(0);
      _800be66c.offset(1, s0).setu(0);
      FUN_8002a86c(s2);
      _800be694.offset(2, s0).setu(0);
      _800be696.offset(1, s0).setu(0);
      _800be697.offset(1, s0).setu(0);

      s0 += 0xa0L;
    }

    FUN_8002a8f8();
    _800be5d0.setu(0);
  }

  @Method(0x8002a86cL)
  public static void FUN_8002a86c(final long a0) {
    final long v0 = a0 * 0xa0;

    _800be66e.offset(1, v0).setu(0);
    _800be66f.offset(1, v0).setu(0);
    _800be670.offset(1, v0).setu(0);
    _800be671.offset(1, v0).setu(0);
    _800be672.offset(1, v0).setu(0);
    _800be673.offset(1, v0).setu(0);
    _800be674.offset(1, v0).setu(0);
    _800be675.offset(1, v0).setu(0);
    _800be676.offset(1, v0).setu(0);
    _800be677.offset(1, v0).setu(0);
    _800be678.offset(1, v0).setu(0);
    _800be679.offset(1, v0).setu(0);
    _800be67a.offset(1, v0).setu(0);
    _800be67b.offset(1, v0).setu(0);
    _800be67c.offset(1, v0).setu(0);
    _800be67e.offset(2, v0).setu(0);
    _800be680.offset(2, v0).setu(0);
    _800be682.offset(2, v0).setu(0);
    _800be684.offset(2, v0).setu(0);
    _800be686.offset(2, v0).setu(0);
    _800be688.offset(2, v0).setu(0);
    _800be68a.offset(2, v0).setu(0);
    _800be68c.offset(2, v0).setu(0);
    _800be68e.offset(2, v0).setu(0);
    _800be690.offset(1, v0).setu(0);
    _800be691.offset(1, v0).setu(0);
    _800be692.offset(1, v0).setu(0);
    _800be693.offset(1, v0).setu(0);
  }

  @Method(0x8002a8f8L)
  public static void FUN_8002a8f8() {
    _800be5d8.setu(0);
    _800be5d9.setu(0);
    _800be5da.setu(0);
    _800be5db.setu(0);
    _800be5dc.setu(0);
    _800be5dd.setu(0);
    _800be5de.setu(0);
    _800be5df.setu(0);
    _800be5e0.setu(0);
    _800be5e1.setu(0);
    _800be5e2.setu(0);
    _800be5e3.setu(0);
    _800be5e4.setu(0);
    _800be5e5.setu(0);
    _800be5e6.setu(0);
    _800be5e7.setu(0);
    _800be5e8.setu(0);
    _800be5e9.setu(0);
    _800be5ea.setu(0);
    _800be5eb.setu(0);
    _800be5ec.setu(0);
    _800be5ed.setu(0);
    _800be5ee.setu(0);
    _800be5ef.setu(0);
    _800be5f0.setu(0);
    _800be5f1.setu(0);
    _800be5f2.setu(0);
    _800be5f3.setu(0);
  }

  @Method(0x8002a9c0L)
  public static void FUN_8002a9c0() {
    submapCut_80052c30.setu(0x2a3L);
    _80052c34.setu(0x4L);
    index_80052c38.set(0);
    _80052c3c.setu(-0x1L);
    _80052c40.setu(0);
    _80052c44.setu(0x2L);
  }

  @Method(0x8002aae8L)
  public static long FUN_8002aae8() {
    long s0 = 0;
    switch((int)_80052c44.get()) {
      case 0x1:
      case 0x2:
        return 0;

      case 0x5:
        FUN_800e519c();
        FUN_800e8e50();
        FUN_800e828c();
        FUN_800e4f8c();
        FUN_800e2220();

        _80052c44.setu(0x2L);
        break;

      case 0x4:
        FUN_800e519c();

      case 0x3:
        FUN_800e8e50();
        FUN_800e828c();
        FUN_800e4f8c();
        FUN_800e2220();
        FUN_800e4e5c();

        //LAB_8002ab98
        _80052c44.setu(0x2L);
        break;

      case 0x0:
        s0 = 0x1L;
        FUN_800e519c();
        break;
    }

    //caseD_6
    if(_800f7e54.get(0x1L) == 0) {
      // If an encounter should start
      if(handleEncounters() != 0) {
        FUN_800e5534(-0x1L, 0);
      }
    }

    //LAB_8002abdc
    //LAB_8002abe0
    final long a0 = FUN_800e6730(index_80052c38.get());
    if((a0 & 0x10L) != 0) {
      FUN_800e5534(a0 >>> 22, a0 >> 16 & 0x3fL);
    }

    //LAB_8002ac10
    //LAB_8002ac14
    return s0;
  }

  @Method(0x8002ac24L)
  public static void FUN_8002ac24() {
    FUN_800fbec8(_80052c4c.getAddress());
  }

  @Method(0x8002ac48L)
  public static long loadDRGN2xBIN() {
    //LAB_8002ac6c
    for(long attempts = 0; attempts < 10; attempts++) {
      //LAB_8002ac74
      for(long i = 0; i < 4; ) {
        i++;

        if(DsSearchFile(new CdlFILE(), String.format("\\SECT\\DRGN2%d.BIN;1", i)) != null) {
          return i;
        }
      }
    }

    //LAB_8002acbc
    return -0x1L;
  }

  @Method(0x8002acd8L)
  public static void FUN_8002acd8(final int joypadIndex, final int a1, final int a2) {
    for(int a3 = 0; a3 < 2; a3++) {
      //LAB_8002acfc
      _800bed60.get(joypadIndex).joyStruct2Arr00.get(a3).s00.set(0);
      _800bed60.get(joypadIndex).joyStruct2Arr00.get(a3).s02.set(0);
      _800bed60.get(joypadIndex).joyStruct2Arr00.get(a3).s04.set(0);
      _800bed60.get(joypadIndex).joyStruct2Arr00.get(a3).s06.set(0);

      _800bed60.get(joypadIndex).joyStruct2Arr00.get(a3).s0c.set(a1);
      _800bed60.get(joypadIndex).joyStruct2Arr00.get(a3).s0e.set(a2);

      _800bf068.get(joypadIndex).get(a3).s00.set(0);
      _800bf068.get(joypadIndex).get(a3).s02.set(0);
      _800bf068.get(joypadIndex).get(a3).s04.set(0);
      _800bf068.get(joypadIndex).get(a3).s06.set(0);
      _800bf068.get(joypadIndex).get(a3).s0c.set(a1);
      _800bf068.get(joypadIndex).get(a3).s0e.set(a2);
    }

    for(int a3 = 0; a3 < 4; a3++) {
      //LAB_8002ad60
      _800bed60.get(joypadIndex).sArr48.get(a3).set((short)0);
    }

    _800bed60.get(joypadIndex).b80.set(0);
    _800bed60.get(joypadIndex).b81.set(0);
    _800bed60.get(joypadIndex).b82.set(0);
    _800bed60.get(joypadIndex).b83.set(0);
    _800bed60.get(joypadIndex).i88.set(0);
    _800bed60.get(joypadIndex).b8c.set(0);
    _800bed60.get(joypadIndex).bArr58.get(0).set(0);
    _800bed60.get(joypadIndex).bArr58.get(1).set(0);
    _800bed60.get(joypadIndex).bArr58.get(2).set(0);
    _800bed60.get(joypadIndex).bArr58.get(3).set(0);
    _800bed60.get(joypadIndex).s5c.set(0);

    for(int a3 = 0; a3 < 0x20; a3++) {
      //LAB_8002adcc
      _800beec4.offset(a3 * 4L).set(0);
      _800bef44.offset(a3 * 4L).set(0);
      _800befc4.offset(a3 * 4L).set(0);
      _800bf044.offset(a3).set(0);
    }

    _800bee90.setu(0);
    _800bee94.setu(0);
    _800bee98.setu(0);
    _800bf064.setu(0);
  }

  @Method(0x8002ae0cL)
  public static long FUN_8002ae0c(final int a0) {
    long s0 = 0;

    for(int joypadIndex = 0; joypadIndex < maxJoypadIndex_80059628.get() + 1 /* this was hardcoded to 2 joypads */; joypadIndex++) {
      long a1;
      long a2;

      do {
        //LAB_8002ae40:
        _800bed60.get(joypadIndex).b80.set(FUN_80042e70(joypadIndex * 0x10));
        _800bed60.get(joypadIndex).b81.set(FUN_80042f40(joypadIndex * 0x10, 0x1L, 0));
        _800bed60.get(joypadIndex).s5c.set(FUN_80042f40(joypadIndex * 0x10, 0x2L, 0));
        _800bed60.get(joypadIndex).b84.set(FUN_8002b0bc(_800bed60.get(joypadIndex), joypadIndex));

        s0 = FUN_8002b250(_800bed60.get(joypadIndex).b84.get() /* signed */, _800bed60.get(joypadIndex), a0, joypadIndex);

        FUN_8002bf00(_800bed60.get(joypadIndex));

        a2 = _800bed60.get(joypadIndex).bArr58.get(0).get() + _800bed60.get(joypadIndex).bArr58.get(1).get();
        a1 = _800bed60.get(joypadIndex).bArr58.get(2).get() + _800bed60.get(joypadIndex).bArr58.get(3).get();

        if(a1 != 0 || a2 == 0) {
          break;
        }

        if(vibrationEnabled_800bb0a9.get() == 0) {
          break;
        }

        _800bed60.get(joypadIndex).i88.set(VSync(-1));
        _800bed60.get(joypadIndex).b8c.set(1);
      } while(true);

      //LAB_8002aef8
      //LAB_8002af00
      if(a1 != 0 && a2 == 0) {
        _800bed60.get(joypadIndex).b8c.set(0);
      }

      //LAB_8002af0c
      if(_800bed60.get(joypadIndex).b8c.get() != 0) {
        if(Math.abs((int)(_800bed60.get(joypadIndex).i88.get() - VSync(-1))) > 3600) {
          _800bed60.get(joypadIndex).bArr58.get(0).set(0);
          _800bed60.get(joypadIndex).bArr58.get(1).set(0);
        }
      }

      //LAB_8002af44
      _800bed60.get(joypadIndex).bArr58.get(2).set(_800bed60.get(joypadIndex).bArr58.get(0));
      _800bed60.get(joypadIndex).bArr58.get(3).set(_800bed60.get(joypadIndex).bArr58.get(1));
    }

    if(_800bf0a8.get() == 0) {
      //LAB_8002b090
      FUN_8002b840();
      return s0;
    }

    if(_800bf0ac.get() > 0x20L) {
      _800bf0ac.setu(0x1f);
    }

    //LAB_8002af94
    long t0 = 0;
    long t1 = 0;
    long a3 = 0;
    s0 = 0;

    for(long i = 0; i < _800bf0ac.get(); i++) {
      //LAB_8002afc4
      long s2 = _800bf064.get() - 0x1L - i;

      if(s2 < 0) {
        s2 += 0x20;
      }

      //LAB_8002afd4
      a3 |= _800beec4.offset(s2 * 4).get();
      t0 |= _800bef44.offset(s2 * 4).get();
      t1 |= _800befc4.offset(s2 * 4).get();
      s0 |= _800bf044.offset(s2).get();
    }

    //LAB_8002b00c
    _800bee90.setu(a3);
    _800bee94.setu(t0);
    _800bee98.setu(t1);
    _800bf0a8.setu(0);
    _800bf0ac.setu(0);

    for(int i = 0; i < 2; i++) {
      //LAB_8002b040
      _800bee9c.offset(i * 4).setu(_800bed60.get(i).sArr50.get(1).get());
      _800beea4.offset(i * 4).setu(_800bed60.get(i).iArr38.get(1).get());
      _800beeac.offset(i * 4).setu(_800bed60.get(i).iArr40.get(1).get());
      _800beeb4.offset(i * 4).setu(_800bed60.get(i).sArr54.get(1).get());
      _800beebc.offset(i * 4).setu(_800bed60.get(i).b83.getUnsigned());
    }

    //LAB_8002b09c
    return s0;
  }

  @Method(0x8002b0bcL)
  public static int FUN_8002b0bc(final JoyStruct joyStruct, final int joypadIndex) {
    _800bee80.get(joypadIndex).set(0);

    if(joyStruct.b80.getUnsigned() == 0) {
      return 0;
    }

    int s0 = 0;

    switch(joyStruct.b81.get()) {
      case 1, 2, 3, 5, 6 -> s0 = -1;
      case 7 -> s0 = 1;
      case 4 -> _800bee80.get(joypadIndex).set(1);
    }

    //caseD_8
    if(vibrationEnabled_800bb0a9.get() == 0 || _800bee80.get(0).get() != 0) {
      //LAB_8002b17c
      joyStruct.bArr58.get(0).set(0);
      joyStruct.bArr58.get(1).set(0);
    }

    //LAB_8002b184
    if(joyStruct.b80.getUnsigned() == 0x1L) {
      joyStruct.b82.setUnsigned(0);
    }

    //LAB_8002b198
    if(joyStruct.b82.getUnsigned() != 0) {
      return s0;
    }

    if(joyStruct.b80.getUnsigned() == 0x6L) {
      if(_800bee8c.get() == 0) {
        if(FUN_80042d10(0, 1, 0)) {
          _800bee8c.setu(0x2L);
        }

        return s0;
      }
    }

    //LAB_8002b1ec
    if(joyStruct.b80.getUnsigned() == 0x2L) {
      joyStruct.b82.setUnsigned(0x1L);
    } else if(joyStruct.b80.getUnsigned() == 0x6L) {
      if(FUN_80043040(joypadIndex * 0x10, joypadIndex, 4) < 60) {
        if(FUN_80042ba0(joypadIndex * 0x10, _80052c5c) != 0) {
          //LAB_8002b22c
          joyStruct.b82.setUnsigned(0x1L);
        }
      }
    }

    //LAB_8002b234
    //LAB_8002b238
    return s0;
  }

  @Method(0x8002b250L)
  public static long FUN_8002b250(final long a0, final JoyStruct joyStruct, final int a2, final int a3) {
    if(joyStruct.b5e.get(0).get() != 0) {
      //LAB_8002b39c
      for(int i = 0; i < 2; i++) {
        //LAB_8002b3b0
        joyStruct.joyStruct2Arr00.get(i).s00.set(0);
        joyStruct.joyStruct2Arr00.get(i).s02.set(0);
        joyStruct.joyStruct2Arr00.get(i).s04.set(0);
        joyStruct.joyStruct2Arr00.get(i).s06.set(0);

        joyStruct.bArr58.get(i).set(0);

        _800bf068.get(a3).get(i).s00.set(0);
        _800bf068.get(a3).get(i).s02.set(0);
        _800bf068.get(a3).get(i).s04.set(0);
        _800bf068.get(a3).get(i).s06.set(0);
      }

      return 1;
    }

    if((int)a0 < 0) {
      //LAB_8002b340
      for(int i = 0; i < 2; i++) {
        //LAB_8002b354
        joyStruct.joyStruct2Arr00.get(i).s00.set(0);
        joyStruct.joyStruct2Arr00.get(i).s02.set(0);
        joyStruct.joyStruct2Arr00.get(i).s04.set(0);
        joyStruct.joyStruct2Arr00.get(i).s06.set(0);

        joyStruct.bArr58.get(i).set(0);

        _800bf068.get(a3).get(i).s00.set(0);
        _800bf068.get(a3).get(i).s02.set(0);
        _800bf068.get(a3).get(i).s04.set(0);
        _800bf068.get(a3).get(i).s06.set(0);
      }

      return 1;
    }

    joyStruct.joyStruct2Arr00.get(0).s00.set(joyStruct.joyStruct2Arr00.get(0).s02);
    joyStruct.joyStruct2Arr00.get(0).s02.set(joyStruct.b5e.get(2).get() << 8 | joyStruct.b5e.get(3).get());

    FUN_8002c23c(joyStruct.joyStruct2Arr00.get(0).s02);

    joyStruct.joyStruct2Arr00.get(1).s00.set(joyStruct.joyStruct2Arr00.get(1).s02);

    FUN_8002b980(joyStruct);

    joyStruct.b83.setUnsigned(0x1L);

    if(a0 == 0) {
      joyStruct.joyStruct2Arr00.get(1).s02.set(0xffff);

      for(int i = 0; i < 4; i++) {
        //LAB_8002b2d4
        joyStruct.sArr48.get(i).set((short)0);
      }

      for(int i = 0; i < 2; i++) {
        //LAB_8002b2f0
        joyStruct.iArr38.get(i).set(0);
        joyStruct.iArr40.get(i).set(0);
        joyStruct.sArr50.get(i).set(0);
        joyStruct.sArr54.get(i).set(0);
      }

      joyStruct.b83.setUnsigned(0);
    }

    //LAB_8002b318
    for(int i = 0; i < 2; i++) {
      //LAB_8002b31c
      FUN_8002c190(joyStruct.joyStruct2Arr00.get(i), a2);
    }

    //LAB_8002b3f4
    return 0;
  }

  @Method(0x8002b40cL)
  public static void FUN_8002b40c(final JoyStruct a0, final ArrayRef<JoyStruct.JoyStruct2> a1, final int joypadIndex) {
    final byte[] sp10 = new byte[8];
    final byte[] sp18 = new byte[8];
    final byte[] sp20 = new byte[4];

    if(a0.b5e.get(0).get() == 0) {
      if(a0.b84.get() >= 0) {
        a1.get(0).s00.set(a1.get(0).s02);
        a1.get(0).s02.set(a0.b5e.get(2).get() << 8 | a0.b5e.get(3).get());
        FUN_8002c23c(a1.get(0).s02);

        a1.get(1).s00.set(a1.get(1).s02);

        //LAB_8002b48c
        for(int i = 0; i < 4; i++) {
          final long a0_1 = a0.b62.get(i).getUnsigned() - 0x80L;

          if(abs((int)a0_1) < 0x30L) {
            MathHelper.set(sp18, i * 2, 2, 0);
          } else {
            MathHelper.set(sp18, i * 2, 2, a0_1);
          }

          //LAB_8002b4b4
        }

        //LAB_8002b4dc
        for(int i = 0; i < 2; i++) {
          long v0 = 0x800L - ratan2((short)MathHelper.get(sp18, i * 4, 2), (short)MathHelper.get(sp18, i * 4 + 2, 2));
          MathHelper.set(sp20, i * 2, 2, v0);
          if(MathHelper.get(sp18, i * 4, 2) != 0 || MathHelper.get(sp18, i * 4 + 2, 2) != 0) {
            //LAB_8002b51c
            final long a0_1 = MathHelper.get(sp20, i * 2, 2);
            final long v1 = a0_1 + 0x100L;
            if((int)v1 < 0) {
              v0 = a0_1 + 0x10ffL;
            } else {
              v0 = v1;
            }

            //LAB_8002b534
            v0 = (int)v0 >> 0xcL;
            v0 <<= 0xcL;
            v0 = v1 - v0;
            if((int)v0 < 0) {
              v0 += 0x1ffL;
            }

            //LAB_8002b54c
            v0 = (int)v0 >> 0x9L;
            v0 = _80052c64.offset(v0).get();
          } else {
            v0 = 0xffL;
          }

          //LAB_8002b560
          MathHelper.set(sp10, i * 4, 4, v0);
        }

        if(a0.b84.getUnsigned() == 0) {
          a1.get(1).s02.set(0xffff);
        } else {
          a1.get(1).s02.set((int)((MathHelper.get(sp10, 4, 2) << 12 | MathHelper.get(sp10, 0, 2) << 4 | 0xf) & 0xffff));
        }

        //LAB_8002b5b8
        //LAB_8002b5bc
        for(int i = 0; i < 2; i++) {
          FUN_8002c190(a1.get(i), 1);
        }

        return;
      }

      //LAB_8002b5e0
      //LAB_8002b5f4
      for(int i = 0; i < 2; i++) {
        a0.joyStruct2Arr00.get(i).s00.set(0);
        a0.joyStruct2Arr00.get(i).s02.set(0);
        a0.joyStruct2Arr00.get(i).s04.set(0);
        a0.joyStruct2Arr00.get(i).s06.set(0);

        a0.bArr58.get(i).set(0);

        _800bf068.get(joypadIndex).get(i).s00.set(0);
        _800bf068.get(joypadIndex).get(i).s02.set(0);
        _800bf068.get(joypadIndex).get(i).s04.set(0);
        _800bf068.get(joypadIndex).get(i).s06.set(0);
      }

      return;
    }

    //LAB_8002b63c
    //LAB_8002b650
    for(int i = 0; i < 2; i++) {
      a0.joyStruct2Arr00.get(i).s00.set(0);
      a0.joyStruct2Arr00.get(i).s02.set(0);
      a0.joyStruct2Arr00.get(i).s04.set(0);
      a0.joyStruct2Arr00.get(i).s06.set(0);

      a0.bArr58.get(i).set(0);

      _800bf068.get(joypadIndex).get(i).s00.set(0);
      _800bf068.get(joypadIndex).get(i).s02.set(0);
      _800bf068.get(joypadIndex).get(i).s04.set(0);
      _800bf068.get(joypadIndex).get(i).s06.set(0);
    }

    //LAB_8002b690
  }

  @Method(0x8002b6c0L)
  public static void FUN_8002b6c0() {
    final long[] stack = new long[6];

    long v0;
    long v1;
    for(int t2 = 0; t2 < 2; t2++) {
      //LAB_8002b6e0
      v0 = _800bf068.get(t2).get(1).s02.get() & 0xf90f;
      v1 = _800bf068.get(t2).get(0).s02.get();
      stack[t2 * 3] = v1 | v0;

      v0 = _800bf068.get(t2).get(1).s04.get() & 0xf90f;
      v1 = _800bf068.get(t2).get(0).s04.get();
      stack[t2 * 3 + 1] = v1 | v0;

      v0 = _800bf068.get(t2).get(1).s06.get() & 0xf90f;
      v1 = _800bf068.get(t2).get(0).s06.get();
      stack[t2 * 3 + 2] = v1 | v0;

      // Filter out up+down and left+right combos
      for(int a3 = 0; a3 < 3; a3++) {
        //LAB_8002b724
        if((stack[t2 * 3 + a3] & 0x5000L) == 0x5000L) {
          stack[t2 * 3 + a3] &= 0xafffL;
        }

        if((stack[t2 * 3 + a3] & 0xa000L) == 0xa000L) {
          stack[t2 * 3 + a3] &= 0x5fffL;
        }

        //LAB_8002b75c
      }
    }

    _800beec4.offset(_800bf064.get() * 4).setu(stack[0]);
    _800bef44.offset(_800bf064.get() * 4).setu(stack[1]);
    _800befc4.offset(_800bf064.get() * 4).setu(stack[2]);

    if(_800bed60.get(0).b5e.get(0).get() == 0) {
      _800bf044.offset(_800bf064).setu(0);
    } else {
      //LAB_8002b7f8
      _800bf044.offset(_800bf064).setu(0x1L);
    }

    //LAB_8002b808
    final long a0 = _800bf064.get();
    v1 = a0 + 0x1L;
    if((int)v1 < 0) {
      v0 = a0 + 0x20L;
    } else {
      v0 = v1;
    }

    //LAB_8002b828
    v0 >>= 0x5L;
    v0 <<= 0x5L;
    _800bf064.setu(v1 - v0);
  }

  @Method(0x8002b840L)
  public static void FUN_8002b840() {
    //TODO why is this two sets of 3 numbers when only the first set is used?
    final long[] values = new long[6];

    //LAB_8002b860
    for(int t2 = 0; t2 < 2; t2++) {
      values[t2 * 3    ] = (_800bed60.get(t2).joyStruct2Arr00.get(0).s02.get() | _800bed60.get(t2).s12.get()) & 0b1111_1001_0000_1111L;
      values[t2 * 3 + 1] = (_800bed60.get(t2).joyStruct2Arr00.get(0).s04.get() | _800bed60.get(t2).s14.get()) & 0b1111_1001_0000_1111L;
      values[t2 * 3 + 2] = (_800bed60.get(t2).joyStruct2Arr00.get(0).s06.get() | _800bed60.get(t2).s16.get()) & 0b1111_1001_0000_1111L;

      //LAB_8002b8a4
      for(int t0 = 0; t0 < 3; t0++) {
        if((values[t2 * 3 + t0] & 0b0101_0000_0000_0000L) != 0) {
          values[t2 * 3 + t0] &= 0b1010_1111_1111_1111L;
        }

        //LAB_8002b8d0
        if((values[t2 * 3 + t0] & 0b1010_0000_0000_0000L) != 0) {
          values[t2 * 3 + t0] &= 0b0101_1111_1111_1111L;
        }

        //LAB_8002b8dc
      }
    }

    _800bee90.setu(values[0]);
    _800bee94.setu(values[1]);
    _800bee98.setu(values[2]);

    //LAB_8002b930
    for(int i = 0; i < 2; i++) {
      _800bee9c.offset(i * 4).setu(_800bed60.get(i).sArr50.get(1).get());
      _800beea4.offset(i * 4).setu(_800bed60.get(i).iArr38.get(1).get());
      _800beeac.offset(i * 4).setu(_800bed60.get(i).iArr40.get(1).get());
      _800beeb4.offset(i * 4).setu(_800bed60.get(i).sArr54.get(1).get());
      _800beebc.offset(i * 4).setu(_800bed60.get(i).b83.getUnsigned());
    }
  }

  @Method(0x8002b980L)
  public static void FUN_8002b980(final JoyStruct joyStruct) {
    //LAB_8002b9b4
    for(int i = 0; i < 4; i++) {
      final long a = joyStruct.b62.get(i).getUnsigned() - 0x80L;
      if(abs((int)a) < 0x30L) {
        joyStruct.sArr48.get(i).set((short)0);
      } else {
        joyStruct.sArr48.get(i).set((short)a);
      }

      //LAB_8002b9dc
    }

    final long[] s4 = new long[2];

    //LAB_8002ba08
    for(int i = 0; i < 2; i++) {
      long v0 = 0x800L - ratan2(joyStruct.sArr48.get(i * 2).get(), joyStruct.sArr48.get(i * 2 + 1).get());
      joyStruct.sArr50.get(i).set((int)v0);
      joyStruct.iArr38.get(i).set(rsin(v0 & 0xffffL) & 0xffff);
      joyStruct.iArr40.get(i).set(rcos(joyStruct.sArr50.get(i).get()) & 0xffff);
      long v1 = joyStruct.sArr48.get(i * 2).get();
      v0 = joyStruct.sArr48.get(i * 2 + 1).get();
      joyStruct.sArr54.get(i).set((int)SquareRoot0(v1 * v1 + v0 * v0));

      if(joyStruct.sArr48.get(i * 2).get() != 0 || joyStruct.sArr48.get(i * 2 + 1).get() != 0) {
        //LAB_8002ba90
        final long a = joyStruct.sArr50.get(i).get();
        v1 = a + 0x100L;
        v0 = v1;
        if((int)v0 < 0) {
          v0 = a + 0x10ffL;
        }

        //LAB_8002baa8
        v0 = (int)v0 >> 12 << 12;
        v0 = v1 - v0;
        if((int)v0 < 0) {
          v0 += 0x1ffL;
        }

        //LAB_8002bac0
        v0 = _80052c64.offset(1, (int)v0 >> 9).get();
      } else {
        v0 = 0xffL;
      }

      //LAB_8002bacc
      s4[i] = v0;
    }

    joyStruct.s12.set((int)((s4[1] << 12 | s4[0] << 4 | 0xf) & 0xffff));
  }

  @Method(0x8002bb38L)
  public static void FUN_8002bb38(final int joypadIndex, long a1) {
    if(vibrationEnabled_800bb0a9.get() == 0) {
      return;
    }

    if(_800bee80.get(joypadIndex).get() != 0) {
      return;
    }

    if(a1 == 0) {
      //LAB_8002bbc0
      FUN_8002c150(joypadIndex);
      return;
    }

    _800bed60.get(joypadIndex).b34.set(0);

    long v1;
    int a2;
    int a3;
    if(a1 == 0x1L) {
      //LAB_8002bbd0
      v1 = mainCallbackIndex_8004dd20.get();
      if(v1 != 0x5L) {
        //LAB_8002bbec
        a3 = 0;
        a2 = 0x80;
      } else {
        a3 = 0;
        a2 = 0x78;
      }
      //LAB_8002bba8
    } else if(a1 == 0x2L) {
      //LAB_8002bbf4
      v1 = mainCallbackIndex_8004dd20.get();
      if(v1 == 0x5L) {
        //LAB_8002bc18
        a3 = 0;
        a2 = 0x96;
      } else if(v1 == 0x6L) {
        //LAB_8002bc24
        a3 = 0x1;
        a2 = (int)(-0x80 - (_800bee88.get() & 0xff));
      } else {
        a3 = 0x1;
        a2 = 0x80;
      }
    } else if(a1 == 0x3L) {
      //LAB_8002bc38
      v1 = mainCallbackIndex_8004dd20.get();
      if(v1 == 0x5L) {
        //LAB_8002bc5c
        a3 = 0;
        a2 = 0xc8;
      } else if(v1 == 0x6L) {
        //LAB_8002bc68
        a3 = 0x1;
        a2 = (int)~(_800bee88.get() & 0xff);
      } else {
        a3 = 0x1;
        a2 = 0xff;
      }
    } else {
      throw new RuntimeException("Impossible case?");
    }

    //LAB_8002bc78
    //LAB_8002bc80
    final JoyStruct joyStruct = _800bed60.get(joypadIndex);

    if(joyStruct.s5c.get() != 0) {
      joyStruct.bArr58.get(0).set(a3);
      joyStruct.bArr58.get(1).set(a2);
    } else {
      //LAB_8002bcac
      joyStruct.bArr58.get(0).set(0x40);
      joyStruct.bArr58.get(1).set(0x1);
    }

    //LAB_8002bcb8
  }

  @Method(0x8002bcc8L)
  public static void FUN_8002bcc8(final long a0, final long a1) {
    assert false;
  }

  @Method(0x8002bda4L)
  public static void FUN_8002bda4(final long a0, final long a1, final long a2) {
    assert false;
  }

  @Method(0x8002bf00L)
  private static void FUN_8002bf00(final JoyStruct joyStruct) {
    if(joyStruct.b34.get() == 0) {
      return;
    }

    if(vibrationEnabled_800bb0a9.get() == 0 || _800bee80.get(0).get() != 0) {
      //LAB_8002bf30
      joyStruct.bArr58.get(0).set(0);
      joyStruct.bArr58.get(1).set(0);
      joyStruct.b34.set(0);
      return;
    }

    //LAB_8002bf40
    //LAB_8002bf44
    for(int a2 = 0; a2 < 2; a2++) {
      joyStruct.iArr20.get(a2).add(joyStruct.iArr28.get(a2));
    }

    long a1 = joyStruct.iArr20.get(0).get() / 0xc000L;

    if(a1 != 0) {
      a1 = 0x1L;
    }

    //LAB_8002bf90
    joyStruct.bArr58.get(0).set((int)a1);
    joyStruct.bArr58.get(1).set((int)(joyStruct.iArr20.get(1).get() >> 8 & 0xffff)); // This is weird
    joyStruct.i30.decr();

    if(joyStruct.i30.get() <= 0) {
      joyStruct.b34.set(0);
      joyStruct.bArr58.get(0).set(joyStruct.b35);
      joyStruct.bArr58.get(1).set(joyStruct.b36);
    }

    //LAB_8002bfc0
    if(joyStruct.s5c.get() != 0) {
      return;
    }

    if((joyStruct.bArr58.get(0).get() | joyStruct.bArr58.get(1).get()) != 0) {
      joyStruct.bArr58.get(0).set(0x40);
      joyStruct.bArr58.get(1).set(0x01);
      return;
    }

    //LAB_80002bff8
    joyStruct.bArr58.get(0).set(0);
    joyStruct.bArr58.get(1).set(0);

    //LAB_8002c000
  }

  /**
   * This might be initializing controllers
   */
  @Method(0x8002c008L)
  public static void FUN_8002c008() {
    FUN_80043230(_800bed60.get(0).b5e, _800bed60.get(1).b5e);

    for(int joypadIndex = 0; joypadIndex < 2; joypadIndex++) {
      //LAB_8002c034
      FUN_8002acd8(joypadIndex, 0x14, 0x4);
      _800bee80.get(joypadIndex).set(0);
    }

    FUN_80042b60(0, _800bed60.get(0).bArr58, 0x2L);
    FUN_80042b60(0x10, _800bed60.get(1).bArr58, 0x2L);

    FUN_80043120();

    vibrationEnabled_800bb0a9.setu((byte)0x1L);
    _800bee88.setu(0);
    _800bee8c.setu(0);
    _800bf0a8.setu(0);
    _800bf0ac.setu(0);
  }

  @Method(0x8002c0c8L)
  public static void FUN_8002c0c8() {
    //LAB_8002c0f0
    for(int joypadIndex = 0; joypadIndex < 2; joypadIndex++) {
      FUN_8002b40c(_800bed60.get(joypadIndex), _800bf068.get(joypadIndex), joypadIndex);
    }

    FUN_8002b6c0();

    _800bf0a8.setu(0x1L);
    _800bf0ac.addu(0x1L);
  }

  @Method(0x8002c150L)
  public static void FUN_8002c150(final int joypadIndex) {
    _800bed60.get(joypadIndex).b34.set(0);
    _800bed60.get(joypadIndex).bArr58.get(0).set(0);
    _800bed60.get(joypadIndex).bArr58.get(1).set(0);
  }

  @Method(0x8002c190L)
  public static void FUN_8002c190(final JoyStruct.JoyStruct2 joyStruct2, final int a1) {
    joyStruct2.s02.not();

    if((joyStruct2.s02.get() & 0x5000L) == 0x5000L) {
      joyStruct2.s02.and(0xafff);
    }

    //LAB_8002c1b4
    if((joyStruct2.s02.get() & 0xa000L) == 0xa000L) {
      joyStruct2.s02.and(0x5fff);
    }

    //LAB_8002c1cc
    final int v1 = joyStruct2.s02.get() & ~joyStruct2.s00.get();
    joyStruct2.s04.set(v1);
    if(v1 != 0) {
      joyStruct2.s06.set(v1);
      joyStruct2.s08.set((short)joyStruct2.s0c.get());
      return;
    }

    //LAB_8002c1f4
    if(joyStruct2.s02.get() == 0 || joyStruct2.s08.sub((short)a1).get() > 0) {
      //LAB_8002c220
      joyStruct2.s06.set(0);
      return;
    }

    //LAB_8002c228
    joyStruct2.s06.set(joyStruct2.s02);
    joyStruct2.s08.set((short)joyStruct2.s0e.get());
  }

  @Method(0x8002c23cL)
  public static void FUN_8002c23c(final UnsignedShortRef a0) {
    a0.set(a0.get() & 0xff9f | (a0.get() & 0x40) / 2 | (a0.get() & 0x20) * 2);
  }

  @Method(0x8002c86cL)
  public static void FUN_8002c86c() {
    if(_800bf0cd.get() != 0) {
      _800bf0c0.addu(_800bf0c4);
      _800bf0ce.setu(_800bf0c0).shra(0x10L);
      _800bf0c8.subu(0x1L);

      if(_800bf0c8.getSigned() <= 0) {
        _800bf0cd.setu(0);
        _800bf0ce.setu(_800bf0cc);
      }

      //LAB_8002c8c4
      setCdVolume((int)_800bf0ce.get(), (int)_800bf0ce.get());
      setCdMix(0x3fL);
    }

    //LAB_8002c8dc
    if(_800bf0d8.get() == 0x1L) {
      callbackIndex_8004ddc4.setu(0x15L);
    }

    //LAB_8002c8f4
  }

  @Method(0x8002c904L)
  public static void setCdMix(final long volume) {
    if(mono_800bb0a8.get() == 0) {
      //LAB_8002c95c
      CdMix(volume, 0, volume, 0);
    } else {
      final long mixedVol = volume * 70 / 100;
      CdMix(mixedVol, mixedVol, mixedVol, mixedVol);
    }
  }

  @Method(0x8002ced8L)
  public static void start(final int argc, final long argv) {
    for(int i = 0; i < 0x6c4b0; i += 4) {
      _8005a1d8.offset(i).setu(0);
    }

    _80052dc0.setu(ramSize_800e6f04.get() - 0x8L - stackSize_800e6f08.get() - _800c6688.getAddress());
    _80052dbc.setu(_800c6688.getAddress());

    main();

    assert false : "Shouldn't get here";
  }

  @Method(0x8002d060L)
  public static void _bu_init() {
    functionVectorA_000000a0.run(0x70L, EMPTY_OBJ_ARRAY);
  }

  @Method(0x8002d070L)
  public static void SetMem(final int size) {
    functionVectorA_000000a0.run(0x9fL, new Object[] {size});
  }

  @Method(0x8002d12cL)
  public static long getTimerValue(final long timerIndex) {
    if(timerIndex >= 3) {
      return 0;
    }

    return TMR_DOTCLOCK_VAL.offset(timerIndex * 0x10L).get();
  }

  @Method(0x8002d210L)
  public static int abs(final int i) {
    return (int)functionVectorA_000000a0.run(0xeL, new Object[] {i});
  }

  @Method(0x8002d220L)
  public static int strcmp(final String s1, final String s2) {
    return (int)functionVectorA_000000a0.run(0x17L, new Object[] {s1, s2});
  }

  @Method(0x8002d230L)
  public static int strncmp(final String s1, final String s2, final int length) {
    return (int)functionVectorA_000000a0.run(0x18L, new Object[] {s1, s2, length});
  }

  @Method(0x8002d260L)
  public static int rand() {
    return (int)functionVectorA_000000a0.run(0x2fL, EMPTY_OBJ_ARRAY);
  }

  @Method(0x8002d270L)
  public static void srand(final long seed) {
    functionVectorA_000000a0.run(0x30L, new Object[] {seed});
  }

  @Method(0x8002d280L)
  public static void initMemcard(final boolean sharedWithController) {
    initMemcard1(sharedWithController);
    initMemcard2();
    _bu_init();
  }

  @Method(0x8002d2d0L)
  public static void initMemcard1(final boolean sharedWithController) {
    ChangeClearPAD(false);
    VSync(0);

    final boolean enteredCriticalSection = EnterCriticalSection();

    final boolean s0;
    if(get80052e1c() == 0) {
      s0 = false;
    } else {
      s0 = sharedWithController;
    }

    //LAB_8002d310
    InitCARD(s0);

    LOGGER.warn("Skipping bios patches");
    //TODO
//    copyPatches();
    early_card_irq_patch();
//    patch_card_specific_delay();
//    patch_card_info_step4();

    if(enteredCriticalSection) {
      ExitCriticalSection();
    }
  }

  @Method(0x8002d360L)
  public static void initMemcard2() {
    final boolean enteredCriticalSection = EnterCriticalSection();

    StartCARD();
    ChangeClearPAD(false);

    if(enteredCriticalSection) {
      ExitCriticalSection();
    }
  }

  @Method(0x8002d3f0L)
  public static boolean ChangeClearPAD(final boolean val) {
    return (boolean)functionVectorB_000000b0.run(0x5bL, new Object[] {val});
  }

  @Method(0x8002d40cL)
  public static long get80052e1c() {
    return _80052e1c.get();
  }

  @Method(0x8002d6c0L)
  public static long SysEnqIntRP(final int priority, final PriorityChainEntry struct) {
    return (long)functionVectorC_000000c0.run(0x2L, new Object[] {priority, struct});
  }

  @Method(0x8002d6d0L)
  @Nullable
  public static PriorityChainEntry SysDeqIntRP(final int priority, final PriorityChainEntry struct) {
    return (PriorityChainEntry)functionVectorC_000000c0.run(0x3L, new Object[] {priority, struct});
  }

  @Method(0x8002d800L)
  public static void InitCARD(final boolean pad_enable) {
    functionVectorB_000000b0.run(0x4aL, new Object[] {pad_enable});
  }

  @Method(0x8002d810L)
  public static int StartCARD() {
    return (int)functionVectorB_000000b0.run(0x4bL, EMPTY_OBJ_ARRAY);
  }

  /**
   * Replaces 0x641c (early_card_irq_vector)
   */
  @Method(0x8002d90cL)
  public static void early_card_irq_patch() {
    GATE.acquire();
    _0000641c.set(Scus94491BpeSegment_8002::early_card_irq_vector_patched);
    GATE.release();
  }

  @Method(0x8002dbdcL)
  public static long FUN_8002dbdc(final long port) {
    if((int)activeMemcardEvent_800bf170.get() > 0) {
      //LAB_8002dc24
      LOGGER.error("Access denied: multiple events open");
      throw new RuntimeException("Access denied: multiple events open");
    }

    activeMemcardEvent_800bf170.setu(0x1L);
    memcardState_800bf174.setu(0);
    _800bf178.setu(0);
    cardPort_800bf180.setu(port);
    setMemcardVsyncCallback(getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_8002dc44", long.class));

    //LAB_8002dc34
    return 0x1L;
  }

  @Method(0x8002e4f4L)
  public static long FUN_8002e4f4(final long a0) {
    final long v1 = MEMORY.ref(4, a0).get();

    if(v1 == 0) {
      //LAB_8002e548
      _80052e34.setu(0);
      MEMORY.ref(4, a0).setu(0xaL);
      setMemcardVsyncCallback(getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_8002dc44", long.class));
      return 0;
    }

    if(v1 == 0xaL) {
      //LAB_8002e568
      if(memcardState_800bf174.get() != 0) { // Not okay
        return 0x1L;
      }

      //LAB_8002e584
      while(lseek(memcardFileHandle_800bf184.get(), memcardPos_800bf188.get(), 0) != memcardPos_800bf188.get()) {
        DebugHelper.sleep(1);
      }

      testEvents();

      //LAB_8002e5b4
      while(read(memcardFileHandle_800bf184.get(), memcardDest_800bf190.get(), (int)memcardLength_800bf18c.get()) != 0) {
        DebugHelper.sleep(1);
      }

      MEMORY.ref(4, a0).setu(0x1eL);
      return 0;
    }

    //LAB_8002e530
    if(v1 == 0x1eL) {
      //LAB_8002e5d8
      if(FUN_8002fe98() == 0) {
        return 0;
      }

      final long memcardStatus = awaitMemcard();
      if(memcardStatus != 0) { // Problem
        _80052e34.addu(0x1L);

        if(_80052e34.getSigned() < 0x4L) {
          MEMORY.ref(4, a0).setu(0xaL);
          return 0;
        }

        //LAB_8002e628
        if(memcardStatus == 0x4L) { // Ejected
          testEvents();
          FUN_8002f6f0((int)cardPort_800bf180.get());
          MEMORY.ref(4, a0).setu(0x20L);
          return 0;
        }
      }

      //LAB_8002e654
      memcardState_800bf174.setu(getMemcardState(memcardStatus));
      return 0x1L;
    }

    if(v1 == 0x20L) {
      //LAB_8002e670
      if(FUN_8002fed4() != 0) {
        FUN_8002fdc0();
        MEMORY.ref(4, a0).setu(0);
      }

      //LAB_8002e68c
      //LAB_8002e690
      return 0;
    }

    return 0;
  }

  @Method(0x8002e908L)
  public static long readMemcardFile(final long port, final String saveName, long memcardDest, long memcardPos, long memcardLength) {
    if((int)activeMemcardEvent_800bf170.get() > 0) {
      LOGGER.error("Access denied: system busy");
      throw new RuntimeException("Access denied: system busy");
    }

    //LAB_8002e960
    if(memcardFileHandle_800bf184.get() >= 0) {
      LOGGER.error("Access denied: file already open");
      throw new RuntimeException("Access denied: file already open");
    }

    //LAB_8002e97c
    if((memcardLength & 0x7fL) != 0) {
      LOGGER.error("Access denied: invalid data size align");
      throw new RuntimeException("Access denied: invalid data size align");
    }

    //LAB_8002e990
    if((memcardPos & 0x7fL) != 0) {
      //LAB_8002e9e4
      //LAB_8002e9ec
      LOGGER.error("Access denied: invalid offset value align");
      throw new RuntimeException("Access denied: invalid offset value align");
    }

    final long s0 = activeMemcardEvent_800bf170.offset(1, 0x24L).getAddress();
    getDevice(port, s0);
    strcat(MEMORY.ref(0x20, s0).cast(CString::new), saveName);

//    _800bf170.setu(0x3L);
//    _800bf174.setu(0);
//    _800bf178.setu(0);
    cardPort_800bf180.setu(port);
    memcardPos_800bf188.setu(memcardPos);
    memcardLength_800bf18c.setu(memcardLength);
    memcardDest_800bf190.setu(memcardDest);

//    MEMCARD.directRead((int)memcardPos / 0x80, memcardDest, (int)memcardLength / 0x80);

    final int file = open(activeMemcardEvent_800bf170.offset(1, 0x24L).getString(), 0x8001);

    while(lseek(file, memcardPos, 0) != memcardPos) {
      DebugHelper.sleep(1);
    }

    while(read(file, memcardDest, (int)memcardLength) != 0) {
      DebugHelper.sleep(1);
    }

    close(file);

    activeMemcardEvent_800bf170.setu(0);
    memcardState_800bf174.setu(0); // Okay
    _800bf178.setu(1);

//    setMemcardVsyncCallback(getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_8002ea20", long.class));

    //LAB_8002e9f8
    return 0x1L;
  }

  @Method(0x8002ea20L)
  public static long FUN_8002ea20(final long a0) {
    final long v1 = MEMORY.ref(4, a0).get();

    if(v1 == 0) {
      //LAB_8002ea74
      _80052e3c.setu(0);
      setMemcardVsyncCallback(getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_8002dc44", long.class));
      MEMORY.ref(4, a0).setu(0xaL);
      return 0;
    }

    if(v1 == 0xaL) {
      //LAB_8002ea90
      if(memcardState_800bf174.get() != 0) { // Not okay
        return 0x1L;
      }

      memcardFileHandle_800bf184.set(open(activeMemcardEvent_800bf170.offset(1, 0x24L).getString(), 0x8001));
      if(memcardFileHandle_800bf184.get() < 0) {
        memcardState_800bf174.setu(0x5L); // Failed to open file
        return 0x1L;
      }

      //LAB_8002ead0
      MEMORY.ref(4, a0).setu(0x14L);
      setMemcardVsyncCallback(getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_8002e4f4", long.class));
      return 0;
    }

    //LAB_8002ea5c
    if(v1 == 0xbL) {
      //LAB_8002ead0
      MEMORY.ref(4, a0).setu(0x14L);
      setMemcardVsyncCallback(getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_8002e4f4", long.class));
      return 0;
    }

    if(v1 == 0x14L) {
      //LAB_8002eaec
      close(memcardFileHandle_800bf184.get());
      memcardFileHandle_800bf184.set(-1);
      return 0x1L;
    }

    return 0;
  }

  @Method(0x8002eb28L)
  public static long FUN_8002eb28(final long a0, final long a1, final long a2, final long a3, final long a4) {
    assert false;
    return 0;
  }

  @Method(0x8002f0d4L)
  public static long FUN_8002f0d4(final long a0, final long a1, final long a2) {
    assert false;
    return 0;
  }

  @Method(0x8002f1d0L)
  public static long FUN_8002f1d0(final long a0) {
    assert false;
    return 0;
  }

  static {
    GATE.acquire();
  }

  private static final Value _0000641c = MEMORY.ref(4, 0x0000641cL);

  private static final Value _000072f0 = MEMORY.ref(4, 0x000072f0L);

  private static final Value _000075c0 = MEMORY.ref(4, 0x000075c0L);
  private static final Value memcardIoAddress_000075c4 = MEMORY.ref(4, 0x000075c4L);
  /** @see Kernel#memcardStateBitset_00007568 */
  private static final Pointer<UnsignedByteRef> memcardStateBitset_000075c8 = MEMORY.ref(4, 0x000075c8L, Pointer.of(1, UnsignedByteRef::new));
  private static final Pointer<UnsignedIntRef> _000075cc = MEMORY.ref(4, 0x000075ccL, Pointer.of(4, UnsignedIntRef::new));

  static {
    GATE.release();
  }

  // Address: 0x641c
  public static boolean early_card_irq_vector_patched() {
    if(_000075c0.get() == 0 || I_STAT.get(0x80L) == 0 || I_MASK.get(0x80L) == 0) {
      return false;
    }

    while(JOY_MCD_STAT.get(0x80L) != 0) {
      DebugHelper.sleep(1);
    }

    final long memcardState = memcardStateBitset_000075c8.deref().get();
    if(memcardState == 0x4L) { // Write
      JOY_MCD_DATA.get(); // Intentional read to nowhere

      // There seems to be a bug in the kernel causing this to read from address 0 sometimes. I verified this is the case in no$.
      final long data;
      if(memcardIoAddress_000075c4.get() != 0) {
        data = memcardIoAddress_000075c4.deref(1).get();
        memcardIoAddress_000075c4.addu(0x1L);
      } else {
        data = 0;
      }

      JOY_MCD_DATA.setu(data);
      JOY_MCD_CTRL.oru(0x10L);

      I_STAT.setu(0xffff_ff7fL);

      _000075cc.deref().xor(data);
      _000072f0.addu(0x1L);

      if(_000072f0.get() >= 0x80L) {
        _000075c0.setu(0);
      }

      //LAB_000064ec;
    } else if(memcardState == 0x2L) { // Read
      final long data = JOY_MCD_DATA.get();
      memcardIoAddress_000075c4.deref(1).setu(data);
      memcardIoAddress_000075c4.addu(0x1L);
      JOY_MCD_DATA.setu(0);
      JOY_MCD_CTRL.oru(0x10L);

      I_STAT.setu(0xffff_ff7fL);

      _000075cc.deref().xor(data);
      _000072f0.addu(0x1L);

      if(_000072f0.get() >= 0x7fL) {
        _000075c0.setu(0);
      }

      //LAB_0000658c
    } else {
      //LAB_00006594
      return false;
    }

    //LAB_0000659c
//    final long epc = ProcessControlBlockPtr_a0000108.deref().threadControlBlockPtr.deref().cop0r14Epc.get();
//    MEMORY.ref(4, epc).call();

    CPU.RFE();
    return true;
  }

  @Method(0x8002db2cL)
  public static void FUN_8002db2c() {
    resetMemcardEventIndex();

    activeMemcardEvent_800bf170.setu(0);
    memcardState_800bf174.setu(0); // Okay
    _800bf178.setu(0);
    _800bf17c.setu(0);
    memcardFileHandle_800bf184.set(-1);

    _800bf1b4.clear();
    _800bf1b8.setu(0x1L);
    _800bf1bc.setu(0x1L);
    _800bf1c0.setu(0);
    _800bf1c4.setu(0);

    setupMemcardEvents();
    SetVsyncInterruptCallback(InterruptType.CONTROLLER, getMethodAddress(Scus94491BpeSegment_8002.class, "memcardVsyncInterruptHandler"));
  }

  @Method(0x8002dc44L)
  public static long FUN_8002dc44(final long a0) {
    System.out.println("The 2 thing ------------------------------------------------ " + MEMORY.ref(4, a0));

    long v1 = MEMORY.ref(4, a0).get();

    if(v1 == 0) {
      //LAB_8002dca0
      memcardStatus_800bf144.setu(0);
      _800bf140.setu(0);
      v1 = _800bf1c0.offset((cardPort_800bf180.get() >> 4) * 0x4L).getAddress();
      _80052e2c.setu(MEMORY.ref(4, v1).get());
      MEMORY.ref(4, v1).setu(0);

      //LAB_8002dce0
      testEvents();
      _card_info((int)cardPort_800bf180.get());
      MEMORY.ref(4, a0).setu(0xbL);
      return 0;
    }

    if(v1 == 0xaL) {
      //LAB_8002dce0
      testEvents();
      _card_info((int)cardPort_800bf180.get());
      MEMORY.ref(4, a0).addu(0x1L);
      return 0;
    }

    //LAB_8002dc88
    if(v1 == 0xbL) {
      //LAB_8002dd0c
      memcardStatus_800bf144.setu(0);
      _80052e30.setu(_800bf1b8.offset((cardPort_800bf180.get() >> 4) * 0x4L).get());

      //LAB_8002de0c
      if((_800bf17c.get() & 1L << cardPort_800bf180.get()) == 0) {
        memcardStatus_800bf144.setu(0x4L); // Ejected
      }

      //LAB_8002de28
      _800bf1b8.offset((cardPort_800bf180.get() >> 4) * 0x4L).setu(0);

      //LAB_8002de60
      memcardState_800bf174.setu(getMemcardState(memcardStatus_800bf144.get()));
      return 0x1L;
    }

    if(v1 == 0x15L) {
      //LAB_8002df14
      if(FUN_8002fed4() == 0) {
        return 0;
      }

      FUN_8002fdc0();
      MEMORY.ref(4, a0).setu(0);
      return 0;
    }

    //LAB_8002df34
    LOGGER.error("error");
    throw new RuntimeException("error");
  }

  @Method(0x8002df60L)
  public static boolean FUN_8002df60(final long cardPort) {
    if(activeMemcardEvent_800bf170.getSigned() > 0) {
      //LAB_8002dfa8
      LOGGER.error("Access Denied. : event multiple open");
      throw new RuntimeException("Access Denied. : event multiple open");
    }

    activeMemcardEvent_800bf170.setu(0x2L);
    memcardState_800bf174.setu(0); // Okay
    _800bf178.setu(0);
    cardPort_800bf180.setu(cardPort);
    setMemcardVsyncCallback(getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_8002dfc8", long.class));

    //LAB_8002dfb8
    return true;
  }

  @Method(0x8002dfc8L)
  public static long FUN_8002dfc8(final long a0) {
    System.out.println("The thing ------------------------------------------------ " + MEMORY.ref(4, a0));

    switch((int)MEMORY.ref(4, a0).get()) {
      case 0:
        _800bf148.setu(0);
        _800bf14c.setu(0);
        _800bf150.setu(0);
        _800bf154.setu(0);
        _800bf158.setu(0);

        MEMORY.ref(4, a0).addu(0x1L);

      case 1:
        setMemcardVsyncCallback(getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_8002dc44", long.class));
        MEMORY.ref(4, a0).setu(0xaL);
        return 0;

      case 0xa:
        if(memcardState_800bf174.get() == 0x1L) { // Busy
          return 0x1L;
        }

        if(memcardState_800bf174.get() >= 0x2L) { // Some error occurred
          //LAB_8002e084
          if(memcardState_800bf174.get() == 0x3L) { // Ejected
            _800bf158.setu(0x1L);
            _800bf17c.oru(1L << cardPort_800bf180.get());
            testEvents();

            FUN_8002f6f0((int)cardPort_800bf180.get());
            MEMORY.ref(4, a0).setu(0x15L);
            return 0;
          }

          return 0x1L;
        }

        if(memcardState_800bf174.get() != 0) { // Not okay
          return 0x1L;
        }

        //LAB_8002e0c4
        MEMORY.ref(4, a0).setu(0x1eL);
        return 0;

      case 0x15:
        // Shouldn't be needed anymore since memcards are instant
        if(false) {
          if(FUN_8002fed4() == 0) {
            return 0;
          }

          FUN_8002fdc0();
        }

        MEMORY.ref(4, a0).setu(0x1eL);

      case 0x1e:
        testEvents();

        _card_load((int)cardPort_800bf180.get());

        MEMORY.ref(4, a0).addu(0x1L);
        return 0;

      case 0x1f:
        // Shouldn't be needed anymore since memcards are instant
        if(false) {
          if(FUN_8002fe98() == 0) {
            return 0;
          }

          _800bf154.setu(awaitMemcard());
        } else {
          _800bf154.setu(0);
        }

        if(_800bf154.get() == 0x1L) {
          //LAB_8002e1c4
          _800bf14c.addu(0x1L);

          if(_800bf14c.get() < 0x11L) {
            MEMORY.ref(4, a0).setu(0x1eL);
            return 0;
          }
          //LAB_8002e158
        } else if(_800bf154.get() < 0x2L) {
          if(_800bf154.get() == 0) {
            //LAB_8002e170
            //LAB_8002e188
            if(_800bf158.get() == 0) {
              memcardState_800bf174.setu(0); // Okay
            } else {
              memcardState_800bf174.setu(0x3L); // Ejected
            }

            //LAB_8002e194
            return 0x1L;
          }
        } else if(_800bf154.get() == 0x2L) {
          //LAB_8002e1bc
          MEMORY.ref(4, a0).setu(0x1L);
          return 0;
        } else if(_800bf154.get() == 0x4L) {
          //LAB_8002e19c
          testEvents();

          _card_info((int)cardPort_800bf180.get());

          MEMORY.ref(4, a0).setu(0x32L);
          return 0;
        }

        //LAB_8002e1e8
        memcardState_800bf174.setu(getMemcardState(_800bf154.get()));
        return 0x1L;

      case 0x32:
        // Shouldn't be needed anymore since memcards are instant
        if(false) {
          if(FUN_8002fe98() == 0) {
            return 0;
          }

          _800bf154.setu(awaitMemcard());
        } else {
          _800bf154.setu(0);
        }

        if(_800bf154.get() == 0) {
          memcardState_800bf174.setu(0x4L);
          return 0x1L;
        }

        //LAB_8002e254
        MEMORY.ref(4, a0).setu(0x1L);
        break;
    }

    //LAB_8002e25c
    return 0;
  }

  @Method(0x8002ed48L)
  public static long FUN_8002ed48(final long port, final long filename, final ArrayRef<MemcardStruct28> s7, @Nullable final Ref<Long> fileCountRef, final long a4, final long a5) {
    if(activeMemcardEvent_800bf170.get() != 0) {
      LOGGER.error("Access denied: system busy");
      throw new RuntimeException("Access denied: system busy");
    }

    //LAB_8002edb0
    final Memory.TemporaryReservation tmpStr = MEMORY.temp(25);
    final CString device = tmpStr.get().offset(25, 0x0L).cast(CString::new);
    getDevice(port, device.getAddress());
    final String path = strcat(device, MEMORY.ref(1, filename).getString());
    tmpStr.release();

    _800bf17c.oru(1L << port);

    long fileCount = 0;

    //LAB_8002ee08
    for(int i = 0; i < a4 + a5; i++) {
      final Memory.TemporaryReservation sp0x30tmp = MEMORY.temp(0x34);
      final DIRENTRY dir = sp0x30tmp.get().cast(DIRENTRY::new);

      jump_8002eee8:
      {
        if(i == 0) {
          //LAB_8002ee10
          for(int s2 = 0; s2 < 4; s2++) {
            testEvents();
            final DIRENTRY s0_0 = FUN_8002f400(path, dir);
            if(s0_0 != null) {
              break jump_8002eee8;
            }

            if(getMemcardState(FUN_8002fdc0()) == 0) {
              //LAB_8002eee0
              if(s0_0 == null) {
                break;
              }

              break jump_8002eee8;
            }
          }

          _800bf1c8.setNullable(FUN_8002efa4(null));

          if((int)activeMemcardEvent_800bf170.get() > 0) {
            LOGGER.error("Access denied: multiple events open");
            throw new RuntimeException("Access denied: multiple events open");
          }

          //LAB_8002ee88
          activeMemcardEvent_800bf170.setu(0x2L);
          memcardState_800bf174.setu(0); // Okay
          _800bf178.setu(0);
          cardPort_800bf180.setu(port);
          setMemcardVsyncCallback(getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_8002dfc8", long.class));

          //LAB_8002eeac
          final Ref<Long> ref = new Ref<>();
          FUN_8002efb8(0, null, ref);
          FUN_8002efa4(_800bf1c8.derefNullable());

          sp0x30tmp.release();

          return ref.get();
        }

        //LAB_8002eed4
        final DIRENTRY s0_0 = nextfile(dir);

        //LAB_8002eee0
        if(s0_0 == null) {
          sp0x30tmp.release();
          break;
        }
      }

      //LAB_8002eee8
      if(i >= (int)a4) {
        if(s7 != null) {
          //LAB_8002eefc
          memcpy(s7.get((int)fileCount).getAddress(), dir.getAddress(), 0x28);
          fileCount++;
        }
      }

      sp0x30tmp.release();

      //LAB_8002ef48
    }

    //LAB_8002ef60
    if(fileCountRef != null) {
      fileCountRef.set(fileCount);
    }

    //LAB_8002ef74
    return 0;
  }

  @Method(0x8002efa4L)
  public static BiConsumerRef<Long, Long> FUN_8002efa4(@Nullable final BiConsumerRef<Long, Long> a0) {
    final BiConsumerRef<Long, Long> v0 = _800bf1b4.derefNullable();
    _800bf1b4.setNullable(a0);
    return v0;
  }

  @Method(0x8002efb8L)
  public static long FUN_8002efb8(final long a0, @Nullable final Ref<Long> memcardEvent, @Nullable final Ref<Long> memcardState) {
    if(activeMemcardEvent_800bf170.get() == 0) {
      if(_800bf178.get() == 0) {
        return -0x1L;
      }
    }

    //LAB_8002efe0
    if(a0 == 0) {
      //LAB_8002f004
      while(_800bf178.get() == 0) {
        DebugHelper.sleep(1);
      }

      //LAB_8002f014
      if(memcardState != null) {
        memcardState.set(previousMemcardState_800bf164.get());
      }

      //LAB_8002f030
      if(memcardEvent != null) {
        memcardEvent.set(previousMemcardEvent_800bf160.get());
      }

      //LAB_8002f04c
      _800bf178.setu(0);
      return 0x1L;
    }

    //LAB_8002f060
    if(_800bf178.get() == 0) {
      if(memcardState != null) {
        memcardState.set(memcardState_800bf174.get());
      }

      //LAB_8002f07c
      if(memcardEvent != null) {
        memcardEvent.set(activeMemcardEvent_800bf170.get());
      }

      return 0;
    }

    //LAB_8002f08c
    if(memcardState != null) {
      memcardState.set(previousMemcardState_800bf164.get());
    }

    //LAB_8002f0a8
    if(memcardEvent != null) {
      memcardEvent.set(previousMemcardEvent_800bf160.get());
    }

    //LAB_8002f0c4
    _800bf178.setu(0);

    //LAB_8002f0cc
    return 0x1L;
  }

  @Method(0x8002f244L)
  public static long getMemcardState(final long memcardStatus) {
    if(memcardStatus == 0) { // Done
      return 0;
    }

    if(memcardStatus == 0x1L) { // Write error
      return 0x2L;
    }

    //LAB_8002f26c
    if(memcardStatus == 0x2L) { // Busy
      return 0x1L;
    }

    if(memcardStatus == 0x4L) { // Ejected
      return 0x3L;
    }

    return memcardStatus | 0x8000L;

    //LAB_8002f28c
    //LAB_8002f290
  }

  @Method(0x8002f298L)
  public static void memcardVsyncInterruptHandler() {
    if(noMemcardEventPending() == 0) {
      executeMemcardVsyncCallback();

      if(noMemcardEventPending() != 0) {
        previousMemcardEvent_800bf160.setu(activeMemcardEvent_800bf170);
        previousMemcardState_800bf164.setu(memcardState_800bf174);
        activeMemcardEvent_800bf170.setu(0);
        memcardState_800bf174.setu(0);
        _800bf178.setu(0x1L);

        if(!_800bf1b4.isNull()) {
          _800bf1b4.deref().run(previousMemcardEvent_800bf160.get(), previousMemcardState_800bf164.get());
        }
      }
    }

    //LAB_8002f30c
    _800bf1c0.addu(0x1L);
    _800bf1c4.addu(0x1L);
  }

  @Method(0x8002f344L)
  public static void getDevice(final long port, final long out) {
    MEMORY.ref(4, out).offset(0x0L).setu(_80011174.offset(4, 0x0L));
    MEMORY.ref(1, out).offset(0x4L).setu(_80011174.offset(1, 0x4L));
    MEMORY.ref(1, out).offset(0x5L).setu(_80011174.offset(1, 0x5L));

    final long v0;
    if((int)port >= 0) {
      v0 = port;
    } else {
      v0 = port + 0xfL;
    }

    //LAB_8002f380
    final long joypadIndex = v0 >> 4;
    MEMORY.ref(1, out).offset(0x2L).setu('0' + joypadIndex);
    MEMORY.ref(1, out).offset(0x3L).setu('0' + port - joypadIndex * 0x10L);
  }

  @Method(0x8002f3a0L)
  public static int open(final String filename, final int access) {
    return (int)functionVectorB_000000b0.run(0x32L, new Object[] {filename, access});
  }

  @Method(0x8002f3b0L)
  public static int lseek(final int fd, final long pos, final int seektype) {
    return (int)functionVectorB_000000b0.run(0x33L, new Object[] {fd, pos, seektype});
  }

  @Method(0x8002f3d0L)
  public static int read(final int fd, final long dest, final int length) {
    return (int)functionVectorB_000000b0.run(0x34L, new Object[] {fd, dest, length});
  }

  @Method(0x8002f3e0L)
  public static int close(final int fd) {
    return (int)functionVectorB_000000b0.run(0x36L, new Object[] {fd});
  }

  @Method(0x8002f3f0L)
  public static DIRENTRY nextfile(final DIRENTRY direntry) {
    return (DIRENTRY)functionVectorB_000000b0.run(0x43L, new Object[] {direntry});
  }

  @Method(0x8002f400L)
  @Nullable
  public static DIRENTRY FUN_8002f400(final String a0, final DIRENTRY dir) {
    long v1;
    long s0;

    long a0_1 = _800bf1d8.getAddress();

    //LAB_8002f438
    for(int i = 0; a0.charAt(i) >= ':'; i++) {
      MEMORY.ref(1, a0_1).setu(a0.charAt(i));
      a0_1++;
    }

    //LAB_8002f458
    MEMORY.ref(1, a0_1).offset(0x0L).setu(0);

    GATE.acquire();

    v1 = MEMORY.ref(4, 0x154L).get() / 0x50L;
    s0 = MEMORY.ref(4, 0x150L).get();
    v1 = s0 + v1 * 0x50L;

    jump_8002f504:
    {
      // Find the correct device (e.g. tty, bu)
      //LAB_8002f498
      while(s0 < v1) {
        final long a0_0 = MEMORY.ref(4, s0).get();

        if(a0_0 != 0 && strcmp(MEMORY.ref(1, a0_0).getString(), _800bf1d8.getString()) == 0) {
          //LAB_8002f4e0
          deviceCallback_800bf1d0.setu(MEMORY.ref(4, s0).offset(0x34L));
          break jump_8002f504;
        }

        //LAB_8002f4bc
        s0 += 0x50L;
      }

      GATE.release();

      //LAB_8002f4cc
      //LAB_8002f4d0
      return null;
    }

    //LAB_8002f504
    v1 = MEMORY.ref(4, 0x154L).get() / 0x50L;
    s0 = MEMORY.ref(4, 0x150L).get();
    v1 = s0 + v1 * 0x50L;

    //LAB_8002f540
    while(s0 < v1) {
      final long a0_0 = MEMORY.ref(4, s0).get();

      if(a0_0 != 0 && strcmp(MEMORY.ref(1, a0_0).getString(), _800bf1d8.getString()) == 0) {
        //LAB_8002f4f4
        MEMORY.ref(4, s0).offset(0x34L).setu(getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_8002f59c", long.class, String.class, DIRENTRY.class));
        break;
      }

      //LAB_8002f564
      s0 += 0x50L;
    }

    GATE.release();

    //LAB_8002f574
    //LAB_8002f578
    //LAB_8002f580
    return firstfile(a0, dir);
  }

  @Method(0x8002f59cL)
  public static DIRENTRY FUN_8002f59c(final long a0, final String a1, DIRENTRY a2) {
    if(MEMORY.ref(4, a0).get() == 0) {
      MEMORY.ref(4, a0).setu(0x1L);
    }

    GATE.acquire();

    //LAB_8002f5dc
    long v1 = MEMORY.ref(4, 0x154L).get();
    long s0 = MEMORY.ref(4, 0x150L).get();
    v1 = s0 + v1 / 0x50L * 0x50L;

    //LAB_8002f620
    while(s0 < v1) {
      final long a0_0 = MEMORY.ref(4, s0).get();

      if(a0_0 != 0 && strcmp(MEMORY.ref(1, a0_0).getString(), _800bf1d8.getString()) == 0) {
        MEMORY.ref(4, s0).offset(0x34L).setu(deviceCallback_800bf1d0);
        break;
      }

      //LAB_8002f64c
      s0 += 0x50L;
    }

    GATE.release();

    //LAB_8002f65c
    //LAB_8002f660
    return (DIRENTRY)deviceCallback_800bf1d0.deref(4).call(a0, a1, a2);
  }

  @Method(0x8002f6a0L)
  public static DIRENTRY firstfile(final String name, final DIRENTRY dir) {
    return (DIRENTRY)functionVectorB_000000b0.run(0x42L, new Object[] {name, dir});
  }

  @Method(0x8002f6b0L)
  public static String strcat(final CString dest, final String src) {
    return (String)functionVectorA_000000a0.run(0x15L, new Object[] {dest, src});
  }

  @Method(0x8002f6d0L)
  public static boolean _card_info(final int port) {
    return (boolean)functionVectorA_000000a0.run(0xabL, new Object[] {port});
  }

  @Method(0x8002f6e0L)
  public static void _card_load(final int port) {
    functionVectorA_000000a0.run(0xacL, new Object[] {port});
  }

  @Method(0x8002f6f0L)
  public static void FUN_8002f6f0(final int port) {
    _new_card();
    //TODO why write from RAM 0? To clear data? _card_write(port, 0x3f, 0L);
  }

  @Method(0x8002f730L)
  public static boolean _card_write(final int port, final int sector, final long src) {
    return (boolean)functionVectorB_000000b0.run(0x4eL, new Object[] {port, sector, src});
  }

  @Method(0x8002f740L)
  public static void _new_card() {
    functionVectorB_000000b0.run(0x50L, EMPTY_OBJ_ARRAY);
  }

  @Method(0x8002f750L)
  public static void resetMemcardEventIndex() {
    memcardEventIndex_80052e4c.setu(0xffff_ffffL);
  }

  @Method(0x8002f760L)
  public static void setMemcardVsyncCallback(final long callback) {
    final long a2 = memcardEventIndex_80052e4c.getSigned() + 0x1L;

    if(a2 >= 0x4L) {
      LOGGER.error("libmcrd: event overflow");
      throw new RuntimeException("libmcrd: event overflow");
    }

    //LAB_8002f790
    memcardEventIndex_80052e4c.setu(a2);
    memcardVsyncCallbacks_800bf240.offset(a2 * 0x4L).setu(callback);

    //LAB_8002f7bc
    for(int i = 0; i < 4; i++) {
      _800bf200.offset(a2 * 0x10L).offset(i * 0x4L).setu(0);
    }

    //LAB_8002f7cc
  }

  @Method(0x8002f7dcL)
  public static void executeMemcardVsyncCallback() {
    final long index = memcardEventIndex_80052e4c.getSigned();
    if(index >= 0) {
      if((long)memcardVsyncCallbacks_800bf240.offset(index * 4).deref(4).call(_800bf200.offset(index * 16).getAddress()) != 0) {
        memcardEventIndex_80052e4c.subu(0x1L);
      }
    }
  }

  @Method(0x8002f848L)
  public static long noMemcardEventPending() {
    return memcardEventIndex_80052e4c.get() >>> 0x1fL;
  }

  @Method(0x8002f860L)
  public static int handleCardDoneRead() {
    cardDoneRead_800bf270.set(true);
    return 0;
  }

  @Method(0x8002f874L)
  public static int handleCardErrorWrite() {
    cardErrorWrite_800bf274.set(true);
    return 0;
  }

  @Method(0x8002f888L)
  public static int handleCardErrorBusy() {
    cardErrorBusy_800bf278.set(true);
    return 0;
  }

  @Method(0x8002f89cL)
  public static int handleCardErrorEject() {
    cardErrorEject_800bf27c.set(true);
    return 0;
  }

  @Method(0x8002f8b0L)
  public static int handleCardFinishedOkay() {
    cardFinishedOkay_800bf280.set(true);
    return 0;
  }

  @Method(0x8002f8c4L)
  public static int handleCardError8000() {
    cardError8000_800bf284.set(true);
    return 0;
  }

  @Method(0x8002f8d8L)
  public static int handleCardErrorBusyLow() {
    cardErrorBusyLow_800bf288.set(true);
    return 0;
  }

  @Method(0x8002f8ecL)
  public static int handleCardError2000() {
    cardError2000_800bf28c.set(true);
    return 0;
  }

  @Method(0x8002f934L)
  public static void setupMemcardEvents() {
    final boolean enteredCriticalSection = EnterCriticalSection();

    SwCARD_EvSpIOE_EventId_800bf250.setu(OpenEvent(SwCARD, EvSpIOE, EvMdINTR, getMethodAddress(Scus94491BpeSegment_8002.class, "handleCardDoneRead")));
    SwCARD_EvSpERROR_EventId_800bf254.setu(OpenEvent(SwCARD, EvSpERROR, EvMdINTR, getMethodAddress(Scus94491BpeSegment_8002.class, "handleCardErrorWrite")));
    SwCARD_EvSpTIMOUT_EventId_800bf258.setu(OpenEvent(SwCARD, EvSpTIMOUT, EvMdINTR, getMethodAddress(Scus94491BpeSegment_8002.class, "handleCardErrorBusy")));
    SwCARD_EvSpNEW_EventId_800bf25c.setu(OpenEvent(SwCARD, EvSpNEW, EvMdINTR, getMethodAddress(Scus94491BpeSegment_8002.class, "handleCardErrorEject")));
    HwCARD_EvSpIOE_EventId_800bf260.setu(OpenEvent(HwCARD, EvSpIOE, EvMdINTR, getMethodAddress(Scus94491BpeSegment_8002.class, "handleCardFinishedOkay")));
    HwCARD_EvSpERROR_EventId_800bf264.setu(OpenEvent(HwCARD, EvSpERROR, EvMdINTR, getMethodAddress(Scus94491BpeSegment_8002.class, "handleCardError8000")));
    HwCARD_EvSpTIMOUT_EventId_800bf268.setu(OpenEvent(HwCARD, EvSpTIMOUT, EvMdINTR, getMethodAddress(Scus94491BpeSegment_8002.class, "handleCardErrorBusyLow")));
    HwCARD_EvSpNEW_EventId_800bf26c.setu(OpenEvent(HwCARD, EvSpNEW, EvMdINTR, getMethodAddress(Scus94491BpeSegment_8002.class, "handleCardError2000")));

    EnableEvent(SwCARD_EvSpIOE_EventId_800bf250.get());
    EnableEvent(SwCARD_EvSpERROR_EventId_800bf254.get());
    EnableEvent(SwCARD_EvSpTIMOUT_EventId_800bf258.get());
    EnableEvent(SwCARD_EvSpNEW_EventId_800bf25c.get());
    EnableEvent(HwCARD_EvSpIOE_EventId_800bf260.get());
    EnableEvent(HwCARD_EvSpERROR_EventId_800bf264.get());
    EnableEvent(HwCARD_EvSpTIMOUT_EventId_800bf268.get());
    EnableEvent(HwCARD_EvSpNEW_EventId_800bf26c.get());

    testEvents();

    if(enteredCriticalSection) {
      ExitCriticalSection();
    }
  }

  @Method(0x8002fbe0L)
  public static void testEvents() {
    TestEvent(SwCARD_EvSpIOE_EventId_800bf250.get());
    TestEvent(SwCARD_EvSpERROR_EventId_800bf254.get());
    TestEvent(SwCARD_EvSpTIMOUT_EventId_800bf258.get());
    TestEvent(SwCARD_EvSpNEW_EventId_800bf25c.get());
    TestEvent(HwCARD_EvSpIOE_EventId_800bf260.get());
    TestEvent(HwCARD_EvSpERROR_EventId_800bf264.get());
    TestEvent(HwCARD_EvSpTIMOUT_EventId_800bf268.get());
    TestEvent(HwCARD_EvSpNEW_EventId_800bf26c.get());

    cardDoneRead_800bf270.set(false);
    cardErrorWrite_800bf274.set(false);
    cardErrorBusy_800bf278.set(false);
    cardErrorEject_800bf27c.set(false);
    cardFinishedOkay_800bf280.set(false);
    cardError8000_800bf284.set(false);
    cardErrorBusyLow_800bf288.set(false);
    cardError2000_800bf28c.set(false);
  }

  @Method(0x8002fce8L)
  public static long awaitMemcard() {
    long s0;

    do {
      s0 = (cardDoneRead_800bf270.get() ? 1 : 0) + (cardErrorWrite_800bf274.get() ? 2 : 0) + (cardErrorBusy_800bf278.get() ? 4 : 0) + (cardErrorEject_800bf27c.get() ? 8 : 0);
      DebugHelper.sleep(1);
    } while(s0 == 0);

    TestEvent(HwCARD_EvSpIOE_EventId_800bf260.get());
    TestEvent(HwCARD_EvSpERROR_EventId_800bf264.get());
    TestEvent(HwCARD_EvSpTIMOUT_EventId_800bf268.get());
    TestEvent(HwCARD_EvSpNEW_EventId_800bf26c.get());

    cardDoneRead_800bf270.set(false);
    cardErrorWrite_800bf274.set(false);
    cardErrorBusy_800bf278.set(false);
    cardErrorEject_800bf27c.set(false);

    return s0 >> 0x1L;
  }

  @Method(0x8002fdc0L)
  public static long FUN_8002fdc0() {
    long s0;

    do {
      s0 = (cardFinishedOkay_800bf280.get() ? 1 : 0) + (cardError8000_800bf284.get() ? 2 : 0) + (cardErrorBusyLow_800bf288.get() ? 4 : 0) + (cardError2000_800bf28c.get() ? 8 : 0);
      DebugHelper.sleep(1);
    } while(s0 == 0);

    TestEvent(SwCARD_EvSpIOE_EventId_800bf250.get());
    TestEvent(SwCARD_EvSpERROR_EventId_800bf254.get());
    TestEvent(SwCARD_EvSpTIMOUT_EventId_800bf258.get());
    TestEvent(SwCARD_EvSpNEW_EventId_800bf25c.get());

    cardFinishedOkay_800bf280.set(false);
    cardError8000_800bf284.set(false);
    cardErrorBusyLow_800bf288.set(false);
    cardError2000_800bf28c.set(false);

    return s0 >> 0x1L;
  }

  @Method(0x8002fe98L)
  public static long FUN_8002fe98() {
    return (cardDoneRead_800bf270.get() ? 1 : 0) + (cardErrorWrite_800bf274.get() ? 2 : 0) + (cardErrorBusy_800bf278.get() ? 4 : 0) + (cardErrorEject_800bf27c.get() ? 8 : 0);
  }

  @Method(0x8002fed4L)
  public static long FUN_8002fed4() {
    return (cardFinishedOkay_800bf280.get() ? 1 : 0) + (cardError8000_800bf284.get() ? 2 : 0) + (cardErrorBusyLow_800bf288.get() ? 4 : 0) + (cardError2000_800bf28c.get() ? 8 : 0);
  }

  @Method(0x8002ff10L)
  public static long OpenEvent(final long desc, final int spec, final int mode, final long func) {
    return (long)functionVectorB_000000b0.run(0x8L, new Object[] {desc, spec, mode, func});
  }

  @Method(0x8002ff30L)
  public static int TestEvent(final long event) {
    return (int)functionVectorB_000000b0.run(0xbL, new Object[] {event});
  }

  @Method(0x8002ff40L)
  public static boolean EnableEvent(final long event) {
    return (boolean)functionVectorB_000000b0.run(0xcL, new Object[] {event});
  }
}
