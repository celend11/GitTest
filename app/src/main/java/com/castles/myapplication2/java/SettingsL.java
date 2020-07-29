package com.castles.myapplication2.java;

import android.content.Context;
import android.provider.Settings.Global;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

public class SettingsL {
    private static final String TAG = "Settings";

    private static final String FILE_DEBUG_STATE = "/data/Debug_State";
    private static final String FILE_DEFAULT_APP = "/data/AP_def_package";

    public static boolean getDebugStatus() {
        try {
            return Boolean.valueOf(readFile(FILE_DEBUG_STATE));
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public static String getDefaultApp() {
            try {
                return readFile(FILE_DEFAULT_APP);
            } catch (Exception e) {
                Log.e(TAG, "", e);
                return null;
            }
    }

    public static boolean setDefaultApp(String packageName) {
        try {
            if (packageName == null) {
                File file = new File(FILE_DEFAULT_APP);
                return file.delete();
            } else {
                writeFile(FILE_DEFAULT_APP, packageName);
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public static boolean changeDebugStatus(Context ctx, boolean enable) {
        try {
            writeFile(FILE_DEBUG_STATE, Boolean.toString(enable));
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return false;
        }

        if ("".equals(systemPropertiesGet("ro.oem.device", ""))) {
            if (enable) {
                systemPropertiesSet("persist.sys.usb.config", "adb, acm");
                Global.putInt(ctx.getContentResolver(), Global.ADB_ENABLED, 1);
            } else {
                systemPropertiesSet("persist.sys.usb.config", "acm");
                Global.putInt(ctx.getContentResolver(), Global.ADB_ENABLED, 0);
            }
        } else {
            if (enable) {
                systemPropertiesSet("persist.sys.usb.config", "diag,serial_smd,rmnet_qti_bam,adb");
                Global.putInt(ctx.getContentResolver(), Global.ADB_ENABLED, 1);
            } else {
                systemPropertiesSet("persist.sys.usb.config", "acm_tty");
                Global.putInt(ctx.getContentResolver(), Global.ADB_ENABLED, 0);
            }
        }
        return true;
    }

    public static boolean isTerminalTampered() {
        return systemPropertiesGetBoolean("ro.device.tamper", false);
    }

    private static String systemPropertiesGet(String key, String def) {
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Class[] paramTypes = {String.class, String.class};
            Method systemPropertiesGet= systemPropertiesClass.getMethod("get", paramTypes);
            Object[] params = {key, def};
            return (String)systemPropertiesGet.invoke(systemPropertiesClass, params);
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return def;
        }
    }
    private static boolean systemPropertiesGetBoolean(String key, boolean def) {
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Class[] paramTypes = {String.class, boolean.class};
            Method systemPropertiesGetBoolean = systemPropertiesClass.getDeclaredMethod("getBoolean", paramTypes);
            Object[] params = {key, def};
            return (boolean)systemPropertiesGetBoolean.invoke(systemPropertiesClass, params);
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return def;
        }
    }
    private static void systemPropertiesSet(String key, String value) {
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Class[] paramTypes = {String.class, String.class};
            Method systemPropertiesSet= systemPropertiesClass.getMethod("set", paramTypes);
            Object[] params = {key, value};
            systemPropertiesSet.invoke(systemPropertiesClass, params);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    private static String readFile(String path) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line = br.readLine();
        if (line != null) {
            contentBuilder.append(line);
        }
        while ((line = br.readLine()) != null) {
            contentBuilder.append('\n');
            contentBuilder.append(line);
        }
        br.close();
        return contentBuilder.toString();
    }

    public static void writeFile(String path, String data) throws IOException {
        File file = new File(path);
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        out.write(data.getBytes());
        out.close();
    }


}
