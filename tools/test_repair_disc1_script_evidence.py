import unittest
from repair_disc1_script_evidence import classify


class ScriptAssociationTest(unittest.TestCase):
    def test_separates_camera_modes_empty_slots_and_real_motion(self):
        self.assertEqual(classify('call SMap::scriptSetModeParamForNextCallToScriptSetCameraOffsetOrHideSubmapForegroundObject, 0x2, 0x1, 0x0 ; args', 4), 'camera-mode-only')
        self.assertEqual(classify('call SMap::scriptSetModeParamForNextCallToScriptSetCameraOffsetOrHideSubmapForegroundObject, 0x3, 0x1, 0x0 ; args', 4), 'foreground-hide')
        self.assertEqual(classify('call SMap::scriptSetEnvForegroundPosition, 0x400, 0x400, 0x6 ; args', 4), 'untextured-slot-only')
        self.assertEqual(classify('call SMap::scriptSetEnvForegroundPosition, 0x0, 0xffffff88, 0x1 ; args', 4), 'foreground-move')
        self.assertEqual(classify('call SMap::scriptSetEnvForegroundPosition, 0x0, 0x0, stor[26] ; args', 4), 'show-at-origin')
        self.assertEqual(classify('call SMap::scriptSetEnvForegroundPosition, 0x400, 0x400, stor[26] ; args', 4), 'foreground-hide')


if __name__ == '__main__':
    unittest.main()
