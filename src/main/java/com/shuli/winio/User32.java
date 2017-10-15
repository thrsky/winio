package com.shuli.winio;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface User32 extends StdCallLibrary, com.sun.jna.platform.win32.User32{
    User32 INSTANCE = (User32) Native.loadLibrary(
            "user32", User32.class, W32APIOptions.DEFAULT_OPTIONS
    );
    int MapVirtualKey(int uCode, int uMapType);
}
