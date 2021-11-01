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
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.types.BigStruct;
import legend.game.types.DR_MOVE;
import legend.game.types.ExtendedTmd;
import legend.game.types.JoyStruct;
import legend.game.types.MrgEntry;
import legend.game.types.MrgFile;
import legend.game.types.RotateTranslateStruct;
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
import static legend.game.SMap.FUN_800d9e64;
import static legend.game.SMap.FUN_800da114;
import static legend.game.SMap.FUN_800daa3c;
import static legend.game.SMap.FUN_800de004;
import static legend.game.SMap.FUN_800e2220;
import static legend.game.SMap.FUN_800e4018;
import static legend.game.SMap.FUN_800e4708;
import static legend.game.SMap.FUN_800e4b20;
import static legend.game.SMap.FUN_800e4e5c;
import static legend.game.SMap.FUN_800e4f8c;
import static legend.game.SMap.FUN_800e519c;
import static legend.game.SMap.FUN_800e5534;
import static legend.game.SMap.FUN_800e6730;
import static legend.game.SMap.FUN_800e828c;
import static legend.game.SMap.FUN_800e8e50;
import static legend.game.SMap.FUN_800ea4c8;
import static legend.game.SMap._800f7e54;
import static legend.game.SMap.renderDobj2;
import static legend.game.Scus94491BpeSegment.BASCUS_94491drgn00_80010734;
import static legend.game.Scus94491BpeSegment.FUN_80012b1c;
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.FUN_8001ad18;
import static legend.game.Scus94491BpeSegment.FUN_8001e010;
import static legend.game.Scus94491BpeSegment.FUN_8001e29c;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.addToLinkedListHead;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.functionVectorA_000000a0;
import static legend.game.Scus94491BpeSegment.functionVectorB_000000b0;
import static legend.game.Scus94491BpeSegment.functionVectorC_000000c0;
import static legend.game.Scus94491BpeSegment.insertElementIntoLinkedList;
import static legend.game.Scus94491BpeSegment.linkedListAddress_1f8003d8;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rectArray28_80010770;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment_8003.CdMix;
import static legend.game.Scus94491BpeSegment_8003.DrawSync;
import static legend.game.Scus94491BpeSegment_8003.DsSearchFile;
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
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.updateTmdPacketIlen;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040b90;
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
import static legend.game.Scus94491BpeSegment_8004.callbackIndex_8004ddc4;
import static legend.game.Scus94491BpeSegment_8004.loadingSmapOvl_8004dd08;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.setCdVolume;
import static legend.game.Scus94491BpeSegment_8005._800503b0;
import static legend.game.Scus94491BpeSegment_8005._800503d4;
import static legend.game.Scus94491BpeSegment_8005._800503f8;
import static legend.game.Scus94491BpeSegment_8005._80050424;
import static legend.game.Scus94491BpeSegment_8005._80052ae0;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
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
import static legend.game.Scus94491BpeSegment_8005._8005a1d8;
import static legend.game.Scus94491BpeSegment_8005.index_80052c38;
import static legend.game.Scus94491BpeSegment_8005.maxJoypadIndex_80059628;
import static legend.game.Scus94491BpeSegment_8005.memcardEventIndex_80052e4c;
import static legend.game.Scus94491BpeSegment_800b.HwCARD_EvSpERROR_EventId_800bf264;
import static legend.game.Scus94491BpeSegment_800b.HwCARD_EvSpIOE_EventId_800bf260;
import static legend.game.Scus94491BpeSegment_800b.HwCARD_EvSpNEW_EventId_800bf26c;
import static legend.game.Scus94491BpeSegment_800b.HwCARD_EvSpTIMOUT_EventId_800bf268;
import static legend.game.Scus94491BpeSegment_800b.SwCARD_EvSpERROR_EventId_800bf254;
import static legend.game.Scus94491BpeSegment_800b.SwCARD_EvSpIOE_EventId_800bf250;
import static legend.game.Scus94491BpeSegment_800b.SwCARD_EvSpNEW_EventId_800bf25c;
import static legend.game.Scus94491BpeSegment_800b.SwCARD_EvSpTIMOUT_EventId_800bf258;
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
import static legend.game.Scus94491BpeSegment_800b._800bdc28;
import static legend.game.Scus94491BpeSegment_800b._800bdc38;
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
import static legend.game.Scus94491BpeSegment_800b._800be667;
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
import static legend.game.Scus94491BpeSegment_800b._800bf144;
import static legend.game.Scus94491BpeSegment_800b._800bf148;
import static legend.game.Scus94491BpeSegment_800b._800bf14c;
import static legend.game.Scus94491BpeSegment_800b._800bf150;
import static legend.game.Scus94491BpeSegment_800b._800bf154;
import static legend.game.Scus94491BpeSegment_800b._800bf158;
import static legend.game.Scus94491BpeSegment_800b._800bf160;
import static legend.game.Scus94491BpeSegment_800b._800bf164;
import static legend.game.Scus94491BpeSegment_800b._800bf170;
import static legend.game.Scus94491BpeSegment_800b._800bf174;
import static legend.game.Scus94491BpeSegment_800b._800bf178;
import static legend.game.Scus94491BpeSegment_800b._800bf17c;
import static legend.game.Scus94491BpeSegment_800b._800bf184;
import static legend.game.Scus94491BpeSegment_800b._800bf1b4;
import static legend.game.Scus94491BpeSegment_800b._800bf1b8;
import static legend.game.Scus94491BpeSegment_800b._800bf1bc;
import static legend.game.Scus94491BpeSegment_800b._800bf1c0;
import static legend.game.Scus94491BpeSegment_800b._800bf1c4;
import static legend.game.Scus94491BpeSegment_800b._800bf200;
import static legend.game.Scus94491BpeSegment_800b._800bf240;
import static legend.game.Scus94491BpeSegment_800b.cardDoneRead_800bf270;
import static legend.game.Scus94491BpeSegment_800b.cardError2000_800bf28c;
import static legend.game.Scus94491BpeSegment_800b.cardError8000_800bf284;
import static legend.game.Scus94491BpeSegment_800b.cardErrorBusyLow_800bf288;
import static legend.game.Scus94491BpeSegment_800b.cardErrorBusy_800bf278;
import static legend.game.Scus94491BpeSegment_800b.cardErrorEject_800bf27c;
import static legend.game.Scus94491BpeSegment_800b.cardErrorWrite_800bf274;
import static legend.game.Scus94491BpeSegment_800b.cardFinishedOkay_800bf280;
import static legend.game.Scus94491BpeSegment_800b.cardPort_800bf180;
import static legend.game.Scus94491BpeSegment_800b.linkedListEntry_800bdc50;
import static legend.game.Scus94491BpeSegment_800b.loadedDrgnFiles_800bcf78;
import static legend.game.Scus94491BpeSegment_800b.mono_800bb0a8;
import static legend.game.Scus94491BpeSegment_800b.vibrationEnabled_800bb0a9;
import static legend.game.Scus94491BpeSegment_800c._800c6688;
import static legend.game.Scus94491BpeSegment_800e.main;
import static legend.game.Scus94491BpeSegment_800e.ramSize_800e6f04;
import static legend.game.Scus94491BpeSegment_800e.stackSize_800e6f08;

public final class Scus94491BpeSegment_8002 {
  private Scus94491BpeSegment_8002() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8002.class);

  private static final Object[] EMPTY_OBJ_ARRAY = {};

  @Method(0x80020008L)
  public static void FUN_80020008() {
    FUN_8001ad18();
    FUN_8001e29c(0x1L);
    FUN_8001e29c(0x3L);
    FUN_8001e29c(0x4L);
    FUN_8001e29c(0x5L);
    FUN_8001e29c(0x6L);
    FUN_8001e29c(0x7L);
    FUN_800201c8(0x6L);
  }

  @Method(0x800201c8L)
  public static void FUN_800201c8(final long a0) {
    if(_800bd610.offset(a0 * 16).get() != 0) {
      FUN_8004d034(_800bd61c.offset(a0 * 16).get(), 0x1L);
      FUN_8004c390(_800bd61c.offset(a0 * 16).get());
      removeFromLinkedList(_800bd614.offset(a0 * 16).get());
      _800bd610.offset(a0 * 16).setu(0);
    }

    //LAB_80020220
  }

  @Method(0x80020360L)
  public static void FUN_80020360(long a0, long a1) {
    long a2;
    long a3;
    long v0;
    long v1;

    a3 = a1 + 0x500L;

    //LAB_8002036c
    do {
      v1 = a1;
      v0 = a0;
      a2 = a0 + 0x20L;

      //LAB_80020378
      do {
        MEMORY.ref(4, v1).offset(0x0L).setu(MEMORY.ref(4, v0).offset(0x0L));
        MEMORY.ref(4, v1).offset(0x4L).setu(MEMORY.ref(4, v0).offset(0x4L));
        MEMORY.ref(4, v1).offset(0x8L).setu(MEMORY.ref(4, v0).offset(0x8L));
        MEMORY.ref(4, v1).offset(0xcL).setu(MEMORY.ref(4, v0).offset(0xcL));
        v0 += 0x10L;
        v1 += 0x10L;
      } while(v0 != a2);

      MEMORY.ref(4, v1).offset(0x0L).setu(MEMORY.ref(4, v0).offset(0x0L));
      MEMORY.ref(4, v1).offset(0x4L).setu(MEMORY.ref(4, v0).offset(0x4L));

      if(MEMORY.ref(1, a1).get() == 0x4L) {
        if(MEMORY.ref(4, a1).offset(0x1cL).get() != 0) {
          MEMORY.ref(1, a1).setu(0x3L);
        }
      }

      //LAB_800203d8
      a1 += 0x28L;
      a0 += 0x28L;
    } while(a1 < a3);
  }

  @Method(0x80020460L)
  public static void FUN_80020460() {
    // empty
  }

  @Method(0x80020468L)
  public static void FUN_80020468(GsDOBJ2 dobj2, long a1) {
    assert false;
  }

  /** Very similar to {@link Scus94491BpeSegment_800e#FUN_800e6b3c(BigStruct, ExtendedTmd, long)} */
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
          NotYetLoaded.FUN_800c8844(bigStruct.ObjTable_0c.top.deref().get(s1++), bigStruct.ub_9d.get());
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
    bigStruct.coord2ParamArrPtr_08.set(MEMORY.ref(4, address + bigStruct.count_c8.get() * 0x60L, UnboundedArrayRef.of(0x10, GsCOORD2PARAM::new)));
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

    if(a0.ub_9c.get() != 0x2L) {
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
      UnboundedArrayRef<RotateTranslateStruct> sp10 = a0.rotateTranslateArrPtr_94.deref();

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
      UnboundedArrayRef<RotateTranslateStruct> rotateTranslate = a0.rotateTranslateArrPtr_94.deref();

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
      NotYetLoaded.FUN_800c925c();
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
      NotYetLoaded.FUN_800c8af4();
    }

    //LAB_800212c8
  }

  @Method(0x800212d8L)
  public static void FUN_800212d8(final BigStruct a0) {
    final long count = a0.tmdNobj_ca.get();

    if(count == 0) {
      return;
    }

    UnboundedArrayRef<RotateTranslateStruct> rotateTranslate = a0.rotateTranslateArrPtr_94.deref();

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
      if(struct.id_0c.get() == -0x1L) {
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
      struct = table.top.deref().get((int)(table.nobj.get()));
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

      a3 = (s0_0 + 0x100L) & 0xffffL;
      rect.set((short)s6, (short)s7, (short)s5, (short)s3);
    } else {
      //LAB_80022358

      rect.set((short)s6, (short)s7, (short)s5, (short)s0_0);
      SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), rect, 960L, (s3 + 256L) & 0xffffL);
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
    _800bdc38.setu(0x4L);
  }

  @Method(0x80022590L)
  public static void FUN_80022590() {
    switch((int)_800bdc38.get()) {
      case 0x1 -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          _800bdc28.setu(0);
          _800bdc38.setu(0x2L);
          FUN_8001e010(0);
          _800bc0b9.setu(0x1L);
        }
      }

      case 0x6 -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          _800bdc28.setu(0);
          _800bdc38.setu(0x7L);
          FUN_8001e010(0);
          _800bc0b9.setu(0x1L);
        }
      }

      case 0xb -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          _800bdc28.setu(0);
          _800bdc38.setu(0xcL);
          FUN_8001e010(0);
          _800bc0b9.setu(0x1L);
        }
      }

      case 0x10 -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          _800bdc28.setu(0);
          _800bdc38.setu(0x11L);
          FUN_8001e010(0);
          _800bc0b9.setu(0x1L);
        }
      }

      case 0x15 -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          _800bdc28.setu(0);
          _800bdc38.setu(0x16L);
          FUN_8001e010(0);
          _800bc0b9.setu(0x1L);
        }
      }

      case 0x1f -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          _800bdc28.setu(0);
          _800bdc38.setu(0x20);
          FUN_8001e010(0);
          _800bc0b9.setu(0x1L);
        }
      }

      case 0x1a -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          _800bdc28.setu(0);
          _800bdc38.setu(0x1bL);
        }
      }

      case 0x2, 0x7, 0xc, 0x11, 0x16, 0x1b, 0x20 -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          _800bdc38.addu(0x1L);
          FUN_80012b1c(0x2L, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_80022520", long.class), 0);
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
        _800bdc38.setu(0);
      }

      case 0x1e, 0x14 -> {
        FUN_80012bb4();
        _800bc0b9.setu(0);
        _800bdc38.setu(0);
      }
    }
  }

  @Method(0x8002379cL)
  public static void FUN_8002379c() {
    // empty
  }

  @Method(0x800237a4L)
  public static long FUN_800237a4(final long a0) {
    if(a0 == 0) {
      FUN_8002df60(0);
      return 0;
    }

    final Ref<Long> sp18 = new Ref<>(0L);
    final Ref<Long> sp1c = new Ref<>(0L);
    final Ref<Long> sp20 = new Ref<>(0L);

    //LAB_800237c8
    if(FUN_8002efb8(0x1L, sp18, sp1c) == 0) {
      return 0;
    }

    if(sp1c.get() != 0 && sp1c.get() != 0x3L) {
      return 0x2L;
    }

    //LAB_800237fc
    final long address = addToLinkedListTail(0x280L);
    linkedListEntry_800bdc50.setu(address);
    FUN_8002ed48(0, BASCUS_94491drgn00_80010734.getAddress(), address, sp20, 0, 0xfL);

    removeFromLinkedList(linkedListEntry_800bdc50.get());
    if(sp20.get() == 0) {
      //LAB_80023854
      return 0x2L;
    }

    //LAB_80023860
    return 0x1L;
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
    loadDrgnBinFile(0, 0x1a0dL, 0, getMethodAddress(Scus94491BpeSegment_8002.class, "basicUiTexturesLoaded", Value.class, long.class, long.class), 0, 0x4L);
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
      _800be358.offset(i * 0x4cL).setu(0);
      _800bdf38.offset(i * 0x84L).setu(0);
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

  @Method(0x80025a04L)
  public static void FUN_80025a04(final long a0) {
    assert false;
  }

  @Method(0x80025f4cL)
  public static void FUN_80025f4c(final long a0) {
    assert false;
  }

  @Method(0x800264b0L)
  public static void FUN_800264b0(final long a0) {
    assert false;
  }

  @Method(0x800282acL)
  public static void FUN_800282ac(final long a0) {
    assert false;
  }

  @Method(0x80029920L)
  public static void FUN_80029920(final long a0, final long a1) {
    if(a1 == 0) {
      _800bdea0.offset(a0 * 12).setu(0);
    } else {
      //LAB_80029948
      _800bdea0.offset(a0 * 12).oru(0x1L);
    }

    //LAB_80029970
    final long v0 = _800be358.offset(a0 * 0x4c).getAddress();
    final long v1 = _800bdea0.offset(a0 * 0xc).getAddress();
    MEMORY.ref(2, v1).offset(0x4L).setu(_800be358.offset(a0 * 0x4c).offset(0x14L));
    MEMORY.ref(2, v1).offset(0x8L).setu(0);
    MEMORY.ref(2, v1).offset(0x6L).setu(MEMORY.ref(2, v0).offset(0x16L).get() + MEMORY.ref(2, v0).offset(0x1aL).get() * 6);
  }

  @Method(0x800299d4L)
  public static void FUN_800299d4(final long a0) {
    assert false;
  }

  @Method(0x8002a058L)
  public static void FUN_8002a058() {
    //LAB_8002a080
    for(int i = 0; i < 8; i++) {
      if(_800be358.offset(i * 0x4cL).get() != 0) {
        FUN_80025a04(i);
      }

      //LAB_8002a098
      if(_800bdf38.offset(i * 0x84L).get() != 0) {
        FUN_800264b0(i);
      }
    }

    FUN_80024994();
  }

  @Method(0x8002a0e4L)
  public static void FUN_8002a0e4() {
    long s1 = _800be358.getAddress();
    long s2 = _800bdf38.getAddress();

    //LAB_8002a10c
    for(int s0 = 0; s0 < 0x8; s0++) {
      if(MEMORY.ref(4, s1).get() != 0 && MEMORY.ref(4, s1).offset(0x8L).getSigned() < 0) {
        FUN_80025f4c(s0);
      }

      //LAB_8002a134
      if(MEMORY.ref(4, s2).get() != 0) {
        FUN_800282ac(s0);
        FUN_800299d4(s0);
      }

      //LAB_8002a154
      s2 += 0x84L;
      s1 += 0x4cL;
    }
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
      _800be666.offset(1, s0).setu(0);
      _800be667.offset(1, s0).setu(0);
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
      if(FUN_800e4b20() != 0) {
        FUN_800e5534(-0x1L, 0);
      }
    }

    //LAB_8002abdc
    //LAB_8002abe0
    final long a0 = FUN_800e6730(index_80052c38.get());
    if((a0 & 0x10L) != 0) {
      FUN_800e5534(a0 >>> 22, (a0 >> 16) & 0x3fL);
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
          long v0 = 0x800L - FUN_80040b90((short)MathHelper.get(sp18, i * 4, 2), (short)MathHelper.get(sp18, i * 4 + 2, 2));
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
      long v0 = 0x800L - FUN_80040b90(joyStruct.sArr48.get(i * 2).get(), joyStruct.sArr48.get(i * 2 + 1).get());
      joyStruct.sArr50.get(i).set((int)v0);
      joyStruct.iArr38.get(i).set(rsin(v0 & 0xffffL));
      joyStruct.iArr40.get(i).set(rcos(joyStruct.sArr50.get(i).get()));
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
      joyStruct2.s02.set(joyStruct2.s02.get() & 0xafff);
    }

    //LAB_8002c1b4
    if((joyStruct2.s02.get() & 0xa000L) == 0xa000L) {
      joyStruct2.s02.set(joyStruct2.s02.get() & 0x5fff);
    }

    //LAB_8002c1cc
    final int v1 = joyStruct2.s02.get() & ~joyStruct2.s00.get();
    joyStruct2.s04.set(v1);
    if(v1 != 0) {
      joyStruct2.s06.set(v1);
      joyStruct2.s08.set(joyStruct2.s0c);
      return;
    }

    //LAB_8002c1f4
    if(joyStruct2.s02.get() == 0 || joyStruct2.s08.sub(a1).get() << 16 > 0) {
      //LAB_8002c220
      joyStruct2.s06.set(0);
      return;
    }

    //LAB_8002c228
    joyStruct2.s06.set(joyStruct2.s02);
    joyStruct2.s08.set(joyStruct2.s0e);
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
      setCdVolume(_800bf0ce.get(), _800bf0ce.get());
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

    _800bf170.setu(0);
    _800bf174.setu(0);
    _800bf178.setu(0);
    _800bf17c.setu(0);
    _800bf184.setu(0xffff_ffffL);

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
    long v1 = MEMORY.ref(4, a0).get();

    if(v1 != 0xaL) {
      if(v1 >= 0xbL) {
        //LAB_8002dc88
        if(v1 == 0xbL) {
          //LAB_8002dd0c
          if(FUN_8002fe98() == 0) {
            return 0;
          }

          final long ret = FUN_8002fce8();
          _800bf144.setu(ret);
          _80052e30.setu(_800bf1b8.offset(cardPort_800bf180.get() >> 0x4 << 0x2));

          if(ret != 0x1L) {
            if(ret < 0x2L) {
              if(ret == 0) {
                //LAB_8002de0c
                if((_800bf17c.get() & 0x1L << cardPort_800bf180.get()) == 0) {
                  _800bf144.setu(0x4L);
                }

                //LAB_8002de28
                _800bf1b8.offset(cardPort_800bf180.get() >> 0x4 << 0x2).setu(0);

                //LAB_8002de60
                _800bf174.setu(FUN_8002f244(_800bf144.get()));
                return 0x1L;
              }

              //LAB_8002ded8
              _800bf174.setu(FUN_8002f244(_800bf144.get()));
              _800bf1b8.offset(cardPort_800bf180.get() >> 0x4 << 0x2).setu(0);
              return 0x1L;
            }

            //LAB_8002dd7c
            if(ret != 0x2L) {
              if(ret != 0x4L) {
                //LAB_8002ded8
                _800bf174.setu(FUN_8002f244(_800bf144.get()));
                _800bf1b8.offset(cardPort_800bf180.get() >> 0x4 << 0x2).setu(0);
                return 0x1L;
              }

              if(_800bf1b8.offset(cardPort_800bf180.get() >> 0x4 << 0x2).get() == 0) {
                if(_80052e2c.get() < 0x80L) {
                  testEvents();

                  FUN_8002f6f0((int)cardPort_800bf180.get());

                  MEMORY.ref(4, a0).setu(0x15L);
                  return 0;
                }
              }

              //LAB_8002ddcc
              _800bf1b8.offset(cardPort_800bf180.get() >> 0x4 << 0x2).setu(0x1L);
              _800bf174.setu(FUN_8002f244(_800bf144.get()));
              return 0x1L;
            }

            //LAB_8002de38
            _800bf140.addu(0x1L);
            if(_800bf140.get() < 0x3L) {
              MEMORY.ref(4, a0).setu(0xaL);
              return 0;
            }

            _800bf1b8.offset(cardPort_800bf180.get() >> 0x4 << 0x2).setu(0x1L);

            //LAB_8002de60
            _800bf174.setu(FUN_8002f244(0x2L));
            return 0x1L;
          }

          //LAB_8002de7c
          _800bf140.addu(0x1L);
          if(_800bf140.get() < 0x11L) {
            //LAB_8002dea0
            MEMORY.ref(4, a0).setu(0xaL);
            return 0;
          }

          //LAB_8002dea8
          _800bf174.setu(FUN_8002f244(0x1L));
          _800bf1b8.offset(cardPort_800bf180.get() >> 0x4 << 0x2).setu(0);
          return 0x1L;
        }

        if(v1 == 0x15L) {
          //LAB_8002df14
          if(FUN_8002fed4() != 0) {
            FUN_8002fdc0();
            MEMORY.ref(4, a0).setu(0);
          }

          return 0;
        }

        //LAB_8002df34
        LOGGER.error("error");
        throw new RuntimeException("error");
      }

      if(v1 != 0) {
        //LAB_8002df34
        LOGGER.error("error");
        throw new RuntimeException("error");
      }

      //LAB_8002dca0
      _800bf144.setu(0);
      _800bf140.setu(0);
      MEMORY.ref(4, a0).setu(0xaL);
      v1 = cardPort_800bf180.get() >> 0x4 << 0x2;
      final long v0 = _800bf1c0.offset(v1).get();
      _800bf1c0.offset(v1).setu(0);
      _80052e2c.setu(v0);
    }

    //LAB_8002dce0
    testEvents();
    _card_info((int)cardPort_800bf180.get());
    MEMORY.ref(4, a0).addu(0x1L);
    return 0;
  }

  @Method(0x8002df60L)
  public static boolean FUN_8002df60(final long cardPort) {
    if(_800bf170.getSigned() > 0) {
      //LAB_8002dfa8
      LOGGER.error("Access Denied. : event multiple open");
      throw new RuntimeException("Access Denied. : event multiple open");
    }

    _800bf170.setu(0x2L);
    _800bf174.setu(0);
    _800bf178.setu(0);
    cardPort_800bf180.setu(cardPort);
    FUN_8002f760(getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_8002dfc8", long.class));

    //LAB_8002dfb8
    return true;
  }

  @Method(0x8002dfc8L)
  public static long FUN_8002dfc8(final long a0) {
    switch((int)MEMORY.ref(4, a0).get()) {
      case 0:
        _800bf148.setu(0);
        _800bf14c.setu(0);
        _800bf150.setu(0);
        _800bf154.setu(0);
        _800bf158.setu(0);

        MEMORY.ref(4, a0).addu(0x1L);

      case 1:
        FUN_8002f760(getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_8002dc44", long.class));
        MEMORY.ref(4, a0).setu(0xaL);
        return 0;

      case 0xa:
        if(_800bf174.get() == 0x1L) {
          return 0x1L;
        }

        if(_800bf174.get() >= 0x2L) {
          //LAB_8002e084
          if(_800bf174.get() != 0x3L) {
            return 0x1L;
          }

          _800bf158.setu(0x1L);
          _800bf17c.oru(0x1L << cardPort_800bf180.get());
          testEvents();

          FUN_8002f6f0((int)cardPort_800bf180.get());
          MEMORY.ref(4, a0).setu(0x15L);
          return 0;
        }

        if(_800bf174.get() != 0) {
          return 0x1L;
        }

        //LAB_8002e0c4
        MEMORY.ref(4, a0).setu(0x1eL);
        return 0;

      case 0x15:
        if(FUN_8002fed4() == 0) {
          return 0;
        }

        FUN_8002fdc0();

        MEMORY.ref(4, a0).setu(0x1eL);

      case 0x1e:
        testEvents();

        _card_load((int)cardPort_800bf180.get());

        MEMORY.ref(4, a0).addu(0x1L);
        return 0;

      case 0x1f:
        if(FUN_8002fe98() == 0) {
          return 0;
        }

        _800bf154.setu(FUN_8002fce8());

        if(_800bf154.get() == 0x1L) {
          //LAB_8002e1c4
          _800bf14c.addu(0x1L);

          if(_800bf14c.get() < 0x11L) {
            MEMORY.ref(4, a0).setu(0x1eL);
            return 0;
          }
        } else {
          //LAB_8002e158
          if(_800bf154.get() < 0x2L) {
            if(_800bf154.get() == 0) {
              //LAB_8002e170
              //LAB_8002e188
              if(_800bf158.get() == 0) {
                _800bf174.setu(0);
              } else {
                _800bf174.setu(0x3L);
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
        }

        //LAB_8002e1e8
        _800bf174.setu(FUN_8002f244(_800bf154.get()));
        return 0x1L;

      case 0x32:
        if(FUN_8002fe98() == 0) {
          return 0;
        }

        _800bf154.setu(FUN_8002fce8());

        if(_800bf154.get() == 0) {
          _800bf174.setu(0x4L);
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
  public static long FUN_8002ed48(long a0, long a1, long a2, final Ref<Long> a3, long a4, long a5) {
    assert false;
    return 0;
  }

  @Method(0x8002efb8L)
  public static long FUN_8002efb8(final long a0, @Nullable final Ref<Long> a1, @Nullable final Ref<Long> a2) {
    if(_800bf170.get() == 0) {
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
      if(a2 != null) {
        a2.set(_800bf164.get());
      }

      //LAB_8002f030
      if(a1 != null) {
        a1.set(_800bf160.get());
      }

      //LAB_8002f04c
      _800bf178.setu(0);
      return 0x1L;
    }

    //LAB_8002f060
    if(_800bf178.get() == 0) {
      if(a2 != null) {
        a2.set(_800bf174.get());
      }

      //LAB_8002f07c
      if(a1 != null) {
        a1.set(_800bf170.get());
      }

      return 0;
    }

    //LAB_8002f08c
    if(a2 != null) {
      a2.set(_800bf164.get());
    }

    //LAB_8002f0a8
    if(a1 != null) {
      a1.set(_800bf160.get());
    }

    //LAB_8002f0c4
    _800bf178.setu(0);

    //LAB_8002f0cc
    return 0x1L;
  }

  @Method(0x8002f244L)
  public static long FUN_8002f244(final long a0) {
    if(a0 != 0x1L) {
      if(a0 < 0x2L) {
        if(a0 == 0) {
          return 0;
        }

        return a0 | 0x8000L;
      }

      //LAB_8002f26c
      if(a0 == 0x2L) {
        return 0x1L;
      }

      if(a0 != 0x4L) {
        return a0 | 0x8000L;
      }

      return 0x3L;
    }

    //LAB_8002f28c
    //LAB_8002f290
    return 0x2L;
  }

  @Method(0x8002f298L)
  public static void memcardVsyncInterruptHandler() {
    if(FUN_8002f848() == 0) {
      FUN_8002f7dc();

      if(FUN_8002f848() != 0) {
        _800bf160.setu(_800bf170);
        _800bf164.setu(_800bf174);
        _800bf170.setu(0);
        _800bf174.setu(0);
        _800bf178.setu(0x1L);

        if(!_800bf1b4.isNull()) {
          _800bf1b4.deref().run(_800bf160.get(), _800bf164.get());
        }
      }
    }

    //LAB_8002f30c
    _800bf1c0.addu(0x1L);
    _800bf1c4.addu(0x1L);
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
    _card_write(port, 0x3f, 0L);
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
  public static void FUN_8002f760(final long callback) {
    final long a2 = memcardEventIndex_80052e4c.getSigned() + 0x1L;

    if(a2 >= 0x4L) {
      LOGGER.error("libmcrd: event overflow");
      throw new RuntimeException("libmcrd: event overflow");
    }

    //LAB_8002f790
    memcardEventIndex_80052e4c.setu(a2);
    _800bf240.offset(a2 * 4).setu(callback);

    //LAB_8002f7bc
    for(int i = 0; i < 4; i++) {
      _800bf200.offset(a2 * 16L).offset(i * 4L).setu(0);
    }

    //LAB_8002f7cc
  }

  @Method(0x8002f7dcL)
  public static void FUN_8002f7dc() {
    final long index = memcardEventIndex_80052e4c.getSigned();
    if(index >= 0) {
      if((long)_800bf240.offset(index * 4).deref(4).call(_800bf200.offset(index * 16).getAddress()) != 0) {
        memcardEventIndex_80052e4c.subu(0x1L);
      }
    }
  }

  @Method(0x8002f848L)
  public static long FUN_8002f848() {
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

    EnableEvent((int)SwCARD_EvSpIOE_EventId_800bf250.get());
    EnableEvent((int)SwCARD_EvSpERROR_EventId_800bf254.get());
    EnableEvent((int)SwCARD_EvSpTIMOUT_EventId_800bf258.get());
    EnableEvent((int)SwCARD_EvSpNEW_EventId_800bf25c.get());
    EnableEvent((int)HwCARD_EvSpIOE_EventId_800bf260.get());
    EnableEvent((int)HwCARD_EvSpERROR_EventId_800bf264.get());
    EnableEvent((int)HwCARD_EvSpTIMOUT_EventId_800bf268.get());
    EnableEvent((int)HwCARD_EvSpNEW_EventId_800bf26c.get());

    testEvents();

    if(enteredCriticalSection) {
      ExitCriticalSection();
    }
  }

  @Method(0x8002fbe0L)
  public static void testEvents() {
    TestEvent((int)SwCARD_EvSpIOE_EventId_800bf250.get());
    TestEvent((int)SwCARD_EvSpERROR_EventId_800bf254.get());
    TestEvent((int)SwCARD_EvSpTIMOUT_EventId_800bf258.get());
    TestEvent((int)SwCARD_EvSpNEW_EventId_800bf25c.get());
    TestEvent((int)HwCARD_EvSpIOE_EventId_800bf260.get());
    TestEvent((int)HwCARD_EvSpERROR_EventId_800bf264.get());
    TestEvent((int)HwCARD_EvSpTIMOUT_EventId_800bf268.get());
    TestEvent((int)HwCARD_EvSpNEW_EventId_800bf26c.get());

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
  public static long FUN_8002fce8() {
    long s0;

    do {
      s0 = (cardDoneRead_800bf270.get() ? 1 : 0) + (cardErrorWrite_800bf274.get() ? 2 : 0) + (cardErrorBusy_800bf278.get() ? 4 : 0) + (cardErrorEject_800bf27c.get() ? 8 : 0);
      DebugHelper.sleep(1);
    } while(s0 == 0);

    TestEvent((int)HwCARD_EvSpIOE_EventId_800bf260.get());
    TestEvent((int)HwCARD_EvSpERROR_EventId_800bf264.get());
    TestEvent((int)HwCARD_EvSpTIMOUT_EventId_800bf268.get());
    TestEvent((int)HwCARD_EvSpNEW_EventId_800bf26c.get());

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

    TestEvent((int)SwCARD_EvSpIOE_EventId_800bf250.get());
    TestEvent((int)SwCARD_EvSpERROR_EventId_800bf254.get());
    TestEvent((int)SwCARD_EvSpTIMOUT_EventId_800bf258.get());
    TestEvent((int)SwCARD_EvSpNEW_EventId_800bf25c.get());

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
  public static int OpenEvent(final long desc, final int spec, final int mode, final long func) {
    return (int)functionVectorB_000000b0.run(0x8L, new Object[] {desc, spec, mode, func});
  }

  @Method(0x8002ff30L)
  public static int TestEvent(final int event) {
    return (int)functionVectorB_000000b0.run(0xbL, new Object[] {event});
  }

  @Method(0x8002ff40L)
  public static boolean EnableEvent(final int event) {
    return (boolean)functionVectorB_000000b0.run(0xcL, new Object[] {event});
  }
}
