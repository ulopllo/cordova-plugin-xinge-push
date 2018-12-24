package org.zyj.xinge.main;


import android.util.Log;

import com.nordnetab.chcp.main.js.PluginResultHelper;
import com.tencent.android.otherPush.StubAppUtils;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.zyj.xinge.main.js.JSAction;
import org.json.JSONException;
import org.json.JSONObject;

public class XinGePushPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        boolean cmdProcessed = true;
        if (JSAction.INIT.equals(action)) {
            initPush(callbackContext);
        } else if (JSAction.REGISTER_PUSH.equals(action)) {
            registerPush(callbackContext);
        } else {
            cmdProcessed = false;
        }

        return cmdProcessed;
    }

    private void initPush(CallbackContext callbackContext) {
        String miAppId = preferences.getString("MI_PUSH_ID", null);
        String miAppKey = preferences.getString("MI_PUSH_KEY", null);
        String mzAppId = preferences.getString("MZ_PUSH_ID", null);
        String mzAppKey = preferences.getString("MZ_PUSH_KEY", null);
        if (miAppId == null || miAppKey == null || mzAppId == null || mzAppKey == null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("errMsg", "push key is null,please complete it!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PluginResult result = new PluginResult(PluginResult.Status.OK, jsonObject);
            callbackContext.sendPluginResult(result);
            return;
        }
        StubAppUtils.attachBaseContext(cordova.getContext().getApplicationContext());
        XGPushConfig.enableOtherPush(cordova.getContext().getApplicationContext(), true);
        XGPushConfig.setHuaweiDebug(true);
        XGPushConfig.setMiPushAppId(cordova.getContext().getApplicationContext(), miAppId);
        XGPushConfig.setMiPushAppKey(cordova.getContext().getApplicationContext(), miAppKey);
        XGPushConfig.setMzPushAppId(cordova.getContext(), mzAppId);
        XGPushConfig.setMzPushAppKey(cordova.getContext(), mzAppKey);
        callbackContext.success();
    }

    private void registerPush(CallbackContext callbackContext) {
        XGPushManager.registerPush(cordova.getContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                Log.d("TPush", "注册成功，设备token为：" + data);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("token", data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PluginResult result = new PluginResult(PluginResult.Status.OK, jsonObject);
                callbackContext.sendPluginResult(result);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("errCode", errCode);
                    jsonObject.put("errMsg", msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PluginResult result = new PluginResult(PluginResult.Status.OK, jsonObject);
                callbackContext.sendPluginResult(result);
            }
        });
    }
}
