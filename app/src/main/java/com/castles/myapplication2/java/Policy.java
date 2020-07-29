package com.castles.myapplication2.java;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

/* References
 * https://android.googlesource.com/platform/development/+/refs/heads/master/samples/training/device-management-policy/
 */

public class Policy {
    private static final String TAG = "Policy";
    private static final int ADMIN_POLICY_REQUEST = 1;

    private static final byte[] TOKEN = {
        (byte)0x89,0x13,0x17,0x71, 'C', 'A', 'S', 'T',
         'L', 'E','S', ' ', 'T', 'e', 'c','h',
         'n', 'o','l','o','g', 'y', ' ', ' ',
        (byte)0x89,0x13,0x17,0x71, 'C', 'A', 'S', 'T',
    };

    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mPolicyAdmin;

    public Policy(Context context) {
        mPolicyAdmin = new ComponentName(context, PolicyAdmin.class);
        mDevicePolicyManager = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
    }

    public ComponentName getPolicyAdmin() {
        return mPolicyAdmin;
    }

    public boolean isApplicationHidden(String packageName) {
       return mDevicePolicyManager.isApplicationHidden(mPolicyAdmin, packageName);
    }

    public boolean setApplicationHidden(String packageName, boolean hidden) {
       return mDevicePolicyManager.setApplicationHidden(mPolicyAdmin, packageName, hidden);
    }

    public boolean isAdminActive() {
        boolean ret = mDevicePolicyManager.isAdminActive(mPolicyAdmin);
        Log.d(TAG, "isAdminActive: " + ret);
        return ret;
    }

    public void lockNow() {
        Log.d(TAG, "lockNow()");
        mDevicePolicyManager.lockNow();
        //mDevicePolicyManager.removeActiveAdmin(mPolicyAdmin);
    }

    public boolean resetPassword(String password) {
        Log.d(TAG, "resetPassword()");

        mDevicePolicyManager.setPasswordQuality(mPolicyAdmin, DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
        mDevicePolicyManager.setPasswordMinimumLength(mPolicyAdmin, 0);

        if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            return mDevicePolicyManager.resetPassword(password, DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
        } else {
            if (!mDevicePolicyManager.isResetPasswordTokenActive(mPolicyAdmin) && !mDevicePolicyManager.setResetPasswordToken(mPolicyAdmin, TOKEN)) {
                return false;
            }
            return mDevicePolicyManager.resetPasswordWithToken(mPolicyAdmin, password, TOKEN, DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
        }
    }

    public static class PolicyAdmin extends DeviceAdminReceiver {
        private static final String TAG = "PolicyAdmin";
        @Override
        public void onEnabled(Context context, Intent intent) {
            super.onEnabled(context, intent);
            Log.d(TAG, "onEnabled");
        }
    }

    public DevicePolicyManager getDevicePolicyManager() {
        return mDevicePolicyManager;
    }

    /**
     *申请设备管理员权限
     */
    public void requestLockAdmins() {
        //检查是否已经获取设备管理权限
        boolean active = mDevicePolicyManager.isAdminActive(mPolicyAdmin);
        if (!active) {
            //打开DevicePolicyManager管理器，授权页面
            Intent intent = new Intent();
            //授权页面Action -->  DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN
            intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            //设置DEVICE_ADMIN，告诉系统申请管理者权限的Component/DeviceAdminReceiver
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mPolicyAdmin);
            //设置 提示语--可不添加
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "DevicePolicyManager涉及的管理权限,一次性激活!");
//            startActivityForResult(intent, requestCode);
        } else {
//            Toast.makeText(this, "已经获取的DevicePolicyManager管理器的授权", Toast.LENGTH_LONG).show();
        }
    }
}
