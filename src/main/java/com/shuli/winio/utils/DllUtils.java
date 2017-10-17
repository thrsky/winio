package com.shuli.winio.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.StringTokenizer;

public class DllUtils {

    private static final Logger logger = LoggerFactory.getLogger(DllUtils.class);

    static void importDll(String dllName){
        try {
            String libPath = System.getProperty("java.library.path");
            if (libPath == null || libPath.length()==0){
                throw new RuntimeException("java.library.path is null");
            }

            String path = null;
            StringTokenizer st = new StringTokenizer(libPath,System.getProperty("path.separator"));
            if (st.hasMoreElements()){
                path = st.nextToken();
            }else {
                throw new RuntimeException("can not split library path:"+libPath);
            }
            Class thisClass = DllUtils.class;
            InputStream inputStream = thisClass.getResource(dllName).openStream();
            File dllFile = new File(new File(path),dllName);
            if(!dllFile.exists()){
                FileOutputStream outputStream = new FileOutputStream(dllFile);
                byte[] array = new byte[8192];
                for(int i=inputStream.read(array);i!=-1;i=inputStream.read(array)){
                    outputStream.write(array,0,i);
                }
                outputStream.close();
            }
            System.loadLibrary(dllName.replace("dll",""));
        }catch (Exception e){
            logger.error("load winio dll error"+e.getMessage());
        }
    }
}
