package org.hand.mas.utl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by gonglixuan on 15/3/20.
 */
public class ConstantUtl {
    public static final String SYS_PREFRENCES_CONFIG_FILE_DIR_NAME = "config";
    public final static String configFile = "mobile_exp_backend_config.xml";
    public final static String SYS_PREFRENCES_PUSH_TOKEN = "push_token";

    public static byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1){
                baos.write(b,0,n);
            }
            fis.close();
            baos.close();
            buffer = baos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        return buffer;
    }

}
