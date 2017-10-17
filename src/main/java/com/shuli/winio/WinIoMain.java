package com.shuli.winio;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class WinIoMain {

    private static final Logger logger = LoggerFactory.getLogger(WinioUtils.class);
    private static final WinIo winIo = WinIo.INSTANCE;

    private WinIoMain(){}

    private static void KeyDown(int key) throws Exception {
        User32Util.KBCWait4IBE();
        winIo.SetPortVal(com.shuli.winio.WinIo.CONTROL_PORT, 0xd2, 1);
        User32Util.KBCWait4IBE();
        winIo.SetPortVal(com.shuli.winio.WinIo.DATA_PORT, key, 1);
    }

    private static void KeyUp(int key) throws Exception {
        User32Util.KBCWait4IBE();
        winIo.SetPortVal(com.shuli.winio.WinIo.CONTROL_PORT, 0xd2, 1);
        User32Util.KBCWait4IBE();
        winIo.SetPortVal(com.shuli.winio.WinIo.DATA_PORT, (key | 0x80), 1);
    }

    private static void KeyPress(char key) throws Exception {
        KeyPress(VKMapping.toVK("" + key));
    }

    private static void KeyPress(int vk) throws Exception {
        int scan = User32.INSTANCE.MapVirtualKey(vk, 0);
        KeyDown(scan);
        Thread.sleep(50);
        KeyUp(scan);
    }

    /**
     * enter the msg
     * @param msg
     */
    public static void enter(String msg){
        if (!com.shuli.winio.WinIo.INSTANCE.InitializeWinIo()) {
            logger.error("Cannot Initialize the WinIO");
            System.exit(1);
        }
        for (int i = 0; i < msg.length(); i++) {
            try {
                KeyPress(msg.charAt(i));
            } catch (Exception e) {
                logger.error("enter msg error"+ e.getMessage());
            }
        }
    }

    interface WinIo extends Library{
        static String arch = System.getProperty("os.arch");
        static String winio = "x86".equals(arch) ? "WinIo32" : "WinIo64";
        static WinIo INSTANCE = (WinIo) Native.loadLibrary(winio, WinIo.class, W32APIOptions.DEFAULT_OPTIONS);

        int CONTROL_PORT = 0x64;
        int DATA_PORT = 0x60;

        boolean GetPortVal(int portAddr, Pointer pPortVal, int size);

        boolean SetPortVal(int portAddr, int portVal, int size);
    }

    interface User32 extends StdCallLibrary, com.sun.jna.platform.win32.User32{
        User32 INSTANCE = (User32) Native.loadLibrary(
                "user32", User32.class, W32APIOptions.DEFAULT_OPTIONS
        );
        int MapVirtualKey(int uCode, int uMapType);
    }

    static class User32Util {

        static WinDef.LPARAM buildLPARAM(int vk, int flag){
            StringBuffer buffer = new StringBuffer();
            switch (flag){
                case User32.WM_KEYDOWN:
                    buffer.append("00");
                    break;
                case User32.WM_KEYUP:
                    buffer.append("c0");
                    break;
                default:
                    throw new RuntimeException("invalid flag");
            }
            buffer.append(Integer.toHexString(User32.INSTANCE.MapVirtualKey(vk,0)));
            buffer.append("0001");
            return new WinDef.LPARAM(Long.parseLong(buffer.toString(),16));
        }

        public static void KBCWait4IBE() throws Exception {
            int val;
            do {
                Pointer p = new Memory(8);
                if (!WinIo.INSTANCE.GetPortVal(WinIo.CONTROL_PORT, p, 1)) {
                    throw new RuntimeException("Cannot get the Port");
                }

                val = p.getInt(0);

            } while ((0x2 & val) > 0);
        }
    }

    static class VKMapping {

        private static final Map<String, Integer> map = new HashMap<String, Integer>();

        static {
            map.put("0", KeyEvent.VK_0);
            map.put("1", KeyEvent.VK_1);
            map.put("2", KeyEvent.VK_2);
            map.put("3", KeyEvent.VK_3);
            map.put("4", KeyEvent.VK_4);
            map.put("5", KeyEvent.VK_5);
            map.put("6", KeyEvent.VK_6);
            map.put("7", KeyEvent.VK_7);
            map.put("8", KeyEvent.VK_8);
            map.put("9", KeyEvent.VK_9);
            map.put("a", KeyEvent.VK_A);
            map.put("b", KeyEvent.VK_B);
            map.put("c", KeyEvent.VK_C);
            map.put("d", KeyEvent.VK_D);
            map.put("e", KeyEvent.VK_E);
            map.put("f", KeyEvent.VK_F);
            map.put("g", KeyEvent.VK_G);
            map.put("h", KeyEvent.VK_H);
            map.put("i", KeyEvent.VK_I);
            map.put("j", KeyEvent.VK_J);
            map.put("k", KeyEvent.VK_K);
            map.put("l", KeyEvent.VK_L);
            map.put("m", KeyEvent.VK_M);
            map.put("n", KeyEvent.VK_N);
            map.put("o", KeyEvent.VK_O);
            map.put("p", KeyEvent.VK_P);
            map.put("q", KeyEvent.VK_Q);
            map.put("r", KeyEvent.VK_R);
            map.put("s", KeyEvent.VK_S);
            map.put("t", KeyEvent.VK_T);
            map.put("u", KeyEvent.VK_U);
            map.put("v", KeyEvent.VK_V);
            map.put("w", KeyEvent.VK_W);
            map.put("x", KeyEvent.VK_X);
            map.put("y", KeyEvent.VK_Y);
            map.put("z", KeyEvent.VK_Z);
            map.put("Tab", KeyEvent.VK_TAB);
            map.put("Space", KeyEvent.VK_SPACE);
            map.put("Shift", KeyEvent.VK_SHIFT);
            map.put("Cntl", KeyEvent.VK_CONTROL);
            map.put("Alt", KeyEvent.VK_ALT);
            map.put("F1", KeyEvent.VK_F1);
            map.put("F2", KeyEvent.VK_F2);
            map.put("F3", KeyEvent.VK_F3);
            map.put("F4", KeyEvent.VK_F4);
            map.put("F5", KeyEvent.VK_F5);
            map.put("F6", KeyEvent.VK_F6);
            map.put("F7", KeyEvent.VK_F7);
            map.put("F8", KeyEvent.VK_F8);
            map.put("F9", KeyEvent.VK_F9);
            map.put("F10", KeyEvent.VK_F10);
            map.put("F11", KeyEvent.VK_F11);
            map.put("F12", KeyEvent.VK_F12);

        }

        public static int toVK(String key) {
            return map.get(key);
        }
    }

}
