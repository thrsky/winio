package com.shuli.winio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create by thrsky on 10/15/2017
 * WinioUtil
 */
public class WinioUtils {

    private static final Logger logger = LoggerFactory.getLogger(WinioUtils.class);
    private static final WinIo winIo = WinIo.INSTANCE;

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
        if (!WinIo.INSTANCE.InitializeWinIo()) {
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

}
