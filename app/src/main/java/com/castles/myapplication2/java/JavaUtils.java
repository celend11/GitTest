package com.castles.myapplication2.java;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import com.castles.myapplication2.java.model.ResultInfo;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import CTOS.CtSystem;

public class JavaUtils {
    private static final String TAG = "lliangfu";
    public static void postAAA(int delay, Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.start();

    }
    public void postBBB(){
        System.out.println("WWWWWWWWWWWWWWWw");
    }
    /**
     * 保存亮度设置状态
     *
     * @param resolver
     * @param brightness
     */
    public static void saveBrightness(ContentResolver resolver, int brightness) {
        Uri uri = android.provider.Settings.System
                .getUriFor("screen_brightness");

        android.provider.Settings.System.putInt(resolver, "screen_brightness",
                brightness);
        // resolver.registerContentObserver(uri, true, myContentObserver);
        resolver.notifyChange(uri, null);
    }
    public void testDate(){
        try {
            Process process = Runtime.getRuntime().exec("su");
            String datetime="20131023.112800"; //测试的设置的时间【时间格式 yyyyMMdd.HHmmss】
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone GMT\n");
            os.writeBytes("/system/bin/date -s "+datetime+"\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void setDate(int year, int month, int day) throws IOException, InterruptedException {

//        requestPermission();

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        long when = c.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE) {
            SystemClock.setCurrentTimeMillis(when);
        }

        long now = Calendar.getInstance().getTimeInMillis();
        //Log.d(TAG, "set tm="+when + ", now tm="+now);

        if(now - when > 1000)
            throw new IOException("failed to set Date.");
    }

    public static void StartOrStopWifi(Context context,boolean isStart){
        WifiManager wifiman = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiman.setWifiEnabled(isStart);//开启WiFi
    }

    /**
     * Gets the state of GPS location.
     *
     * @param context
     * @return true if enabled.
     */
    private static boolean getGpsState(Context context) {
        ContentResolver resolver = context.getContentResolver();
        boolean open = Settings.Secure.isLocationProviderEnabled(resolver,
                LocationManager.GPS_PROVIDER);
        System.out.println("getGpsState:" + open);
        return open;
    }

    /**
     * Toggles the state of GPS.
     *
     * @param context
     */
    public void toggleGps(Context context) {
        ContentResolver resolver = context.getContentResolver();
        boolean enabled = getGpsState(context);
        Settings.Secure.setLocationProviderEnabled(resolver,
                LocationManager.GPS_PROVIDER, !enabled);
    }

    @SuppressWarnings("deprecation")
    public boolean changeGpsStatus(boolean isEnable,Context context) {
        return setGpsLocationModeInternal(isEnable ?
                android.provider.Settings.Secure.LOCATION_MODE_HIGH_ACCURACY :
                android.provider.Settings.Secure.LOCATION_MODE_OFF,context);
    }

    @SuppressWarnings("deprecation")
    private boolean setGpsLocationModeInternal(int mode,Context context) {
        int currentMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF);

        Intent intent = new Intent("com.android.settings.location.MODE_CHANGING");
        intent.putExtra("CURRENT_MODE", currentMode);
        intent.putExtra("NEW_MODE", mode);

        context.sendBroadcast(intent, android.Manifest.permission.WRITE_SECURE_SETTINGS);
        Settings.Secure.putInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE, mode);

        return true;
    }

    @SuppressWarnings("deprecation")
    public boolean setGpsLocationMode(int mode,Context context) {
        switch (mode) {
            case 0:
                // Device only
                return setGpsLocationModeInternal(android.provider.Settings.Secure.LOCATION_MODE_SENSORS_ONLY,context);
            case 1:
                // Battery saving
                return setGpsLocationModeInternal(android.provider.Settings.Secure.LOCATION_MODE_BATTERY_SAVING,context);
            case 2:
                // High accuracy
                return setGpsLocationModeInternal(android.provider.Settings.Secure.LOCATION_MODE_HIGH_ACCURACY, context);
        }
        return false;
    }

    public boolean changeMobileNetworkStatus(Context context,boolean enable) {
        try {
            TelephonyManager ts = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            Method setMobileDataEnabledMethod = ts.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            setMobileDataEnabledMethod.invoke(ts, enable);
            Log.d(TAG, "changeMobileNetworkStatus: " + enable);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public void updateLanguage(Locale locale) {
        Log.d("ANDROID_LAB", locale.toString());
        try {
            Object objIActMag, objActMagNative;
            Class clzIActMag = Class.forName("android.app.IActivityManager");
            Class clzActMagNative = Class.forName("android.app.ActivityManagerNative");
            Method mtdActMagNative$getDefault = clzActMagNative.getDeclaredMethod("getDefault");
// IActivityManager iActMag = ActivityManagerNative.getDefault();
            objIActMag = mtdActMagNative$getDefault.invoke(clzActMagNative);
// Configuration config = iActMag.getConfiguration();
            Method mtdIActMag$getConfiguration = clzIActMag.getDeclaredMethod("getConfiguration");
            Configuration config = (Configuration) mtdIActMag$getConfiguration.invoke(objIActMag);
            config.locale = locale;
// iActMag.updateConfiguration(config);
// 此处需要声明权限:android.permission.CHANGE_CONFIGURATION
// 会重新调用 onCreate();
            Class[] clzParams = { Configuration.class };
            Method mtdIActMag$updateConfiguration = clzIActMag.getDeclaredMethod(
                    "updateConfiguration", clzParams);
            mtdIActMag$updateConfiguration.invoke(objIActMag, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTimeZone(Context context,String timeZone){
//        ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE)).setTimeZone("Asia/Shanghai");
        AlarmManager mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.setTimeZone(timeZone);
//        final Calendar now = Calendar.getInstance();
//        TimeZone tz = TimeZone.getTimeZone(timeZone);
//        now.setTimeZone(tz);
    }

    public void ChangeDebugStatus(Context context){
        ContentResolver resolver = context.getContentResolver();
        boolean mAdbEnabled = Settings.Secure.getInt(resolver,
                Settings.Secure.ADB_ENABLED, 0 ) !=  0 ;
        Settings.Secure.putInt(resolver, Settings.Secure.ADB_ENABLED,  mAdbEnabled?0:1);  //0关闭 1开启

    }

        public boolean changeLockScreenStatus(Context mContext,boolean lock, String password) {
        try {
            Class<?> lockPatternUtilsCls = Class.forName("com.android.internal.widget.LockPatternUtils");
            Constructor<?> lockPatternUtilsConstructor = lockPatternUtilsCls.getConstructor(new Class[]{Context.class});
            lockPatternUtilsConstructor.setAccessible(true);
            Object lockPatternUtils = lockPatternUtilsConstructor.newInstance(mContext);
            Method clearLockMethod;

            if (!lock) {
                if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
                    clearLockMethod = lockPatternUtils.getClass().getMethod("clearLock", int.class);
                    clearLockMethod.setAccessible(true);
                    clearLockMethod.invoke(lockPatternUtils, 0);
                } else {
                    clearLockMethod = lockPatternUtils.getClass().getMethod("clearLock", String.class, int.class);
                    clearLockMethod.setAccessible(true);
                    clearLockMethod.invoke(lockPatternUtils, password, 0);
                }
            }
            Method setLockScreenDisabledMethod = lockPatternUtils.getClass().getMethod("setLockScreenDisabled", boolean.class, int.class);
            setLockScreenDisabledMethod.setAccessible(true);
            setLockScreenDisabledMethod.invoke(lockPatternUtils, !lock, 0);
            Log.e(TAG, "changeLockScreenStatus success");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "",e);
            return false;
        }
    }

    public boolean changeLockScreenPassword(Context mContext,String password, String old) {
        try {
            Class<?> lockPatternUtilsCls = Class.forName("com.android.internal.widget.LockPatternUtils");
            Constructor<?> lockPatternUtilsConstructor = lockPatternUtilsCls.getConstructor(new Class[]{Context.class});
            lockPatternUtilsConstructor.setAccessible(true);
            Object lockPatternUtils = lockPatternUtilsConstructor.newInstance(mContext);

            Method clearLockMethod = lockPatternUtils.getClass().getMethod("saveLockPassword", String.class, String.class, int.class, int.class);
            clearLockMethod.setAccessible(true);
            clearLockMethod.invoke(lockPatternUtils, password, old,  android.app.admin.DevicePolicyManager.PASSWORD_QUALITY_NUMERIC, 0);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    // Reference: Quectel_SC20_Android6/src/branch/master/vendor/castles/system/app/Launcher/src/com/ctos/CastlesStart/Launcher.java
    @SuppressWarnings("deprecation")
    public ResultInfo setLauncherApp(Context mContext, String packageName) {
        ComponentName launcher = ComponentName.unflattenFromString(packageName);
        if (launcher == null || launcher.getClassName() == null) {
            return new ResultInfo(-1, "invalid component name");
        }
        Log.d(TAG, "launcher getClassName: " + launcher.getClassName());

        PackageManager pm = mContext.getPackageManager();

        ArrayList<IntentFilter> intentList = new ArrayList<IntentFilter>();
        ArrayList<ComponentName> cnList = new ArrayList<ComponentName>();
        mContext.getPackageManager().getPreferredActivities(intentList, cnList, null);
        IntentFilter dhIF;
        for (int i = 0; i < cnList.size(); i++) {
            dhIF = intentList.get(i);
            if (dhIF.hasAction(Intent.ACTION_MAIN) && dhIF.hasCategory(Intent.CATEGORY_HOME)) {
                mContext.getPackageManager().clearPackagePreferredActivities(cnList.get(i).getPackageName());
            }
        }

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> list;

        list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        final int N = list.size();
        Log.d(TAG, "Launcher count = " + N);

        ComponentName[] set = new ComponentName[N];
        int defaultMatch = 0;
        for (int i = 0; i < N; i++) {
            ResolveInfo r = list.get(i);
            set[i] = new ComponentName(r.activityInfo.packageName, r.activityInfo.name);
            Log.d(TAG, "packageName = " + r.activityInfo.packageName);
            Log.d(TAG, "className = " + r.activityInfo.name);
            if (launcher.getClassName().equals(r.activityInfo.name)) {
                defaultMatch = r.match;
            }
        }
        pm.addPreferredActivity(filter, defaultMatch, set, launcher);

        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        return new ResultInfo(0, "");
    }

    /**
     * 获取写入sdcard的权限
     * @param activity
     */
    public static void getSDCardRWPermission(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.SET_TIME);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SET_TIME},
                    1);
        }
    }

    public boolean setSleepTime(Context mContext,int timeout) {
        Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, timeout);
        return true;
    }

    public boolean CloseAutoTime(Context context,String isClose){
        Settings.System.putString(context.getContentResolver(),Settings.System.AUTO_TIME,isClose);
        return true;
    }

    public static boolean setDefaultApp(String packageName) {
        try {
            if (packageName == null) {
                File file = new File("/data/AP_def_package");
                return file.delete();
            } else {
                SettingsL.writeFile("/data/AP_def_package", packageName);
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return false;
        }
    }
    public static boolean setDefaultApp2(String packageName){
        try{
            CtSystem ctSystem = new CtSystem();
            ctSystem.setDefaultApp(packageName);
        }catch (Exception ex){
            Log.d("XXX",ex.getMessage());
            return false;
        }
        Log.d("XXX","Set DefaultApp Success");
        return true;
    }
}
