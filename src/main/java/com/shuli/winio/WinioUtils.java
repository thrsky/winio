package com.shuli.winio;

import com.sun.jna.NativeLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create by thrsky on 10/15/2017
 * WinioUtil
 */
public class WinioUtils {

    private static final Logger logger = LoggerFactory.getLogger(WinioUtils.class);
    private static final WinIo winIo = WinIo.INSTANCE;
    private static String WINIO_32_DLL_PATH = WinioUtils.class.getResource("/").getPath();
    private static String WINIO_64_DLL_PATH = WinioUtils.class.getResource("/").getPath();

    static {
        NativeLibrary.addSearchPath("WinIo32", WINIO_32_DLL_PATH);
        NativeLibrary.addSearchPath("WinIo64", WINIO_64_DLL_PATH);
        if (!winIo.InitializeWinIo()) {
            logger.error("Cannot Initialize the WinIO");
            throw new IllegalStateException("Cannot Initialize the WinIO");
        }
    }

    /**
     * 禁用构造函数
     */
    private WinioUtils(){
       // 禁用构造函数
    }

    private static void KeyDown(int key) throws Exception {
        User32Util.KBCWait4IBE();
        winIo.SetPortVal(WinIo.CONTROL_PORT, 0xd2, 1);
        User32Util.KBCWait4IBE();
        winIo.SetPortVal(WinIo.DATA_PORT, key, 1);
    }

    private static void KeyUp(int key) throws Exception {
        User32Util.KBCWait4IBE();
        winIo.SetPortVal(WinIo.CONTROL_PORT, 0xd2, 1);
        User32Util.KBCWait4IBE();
        winIo.SetPortVal(WinIo.DATA_PORT, (key | 0x80), 1);
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
        NativeLibrary.addSearchPath("WinIo64", WinioUtils.class.getResource("/").getPath());
        NativeLibrary.addSearchPath("WinIo32", WinioUtils.class.getResource("/").getPath());
        if (!WinIo.INSTANCE.InitializeWinIo()) {
            logger.error("Cannot Initialize the WinIO");
            System.exit(1);
        }
        for (int i = 0; i < msg.length(); i++) {
            try {
                KeyPress(msg.charAt(i));
            } catch (Exception e) {
                logger.error("enter msg error"+ e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
