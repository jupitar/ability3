package com.ustcinfo.mobile.platform.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;

import com.amap.querry.AmapLocation;
import com.ustcinfo.mobile.platform.R;
import com.ustcinfo.mobile.platform.core.appstore.AppStore;
import com.ustcinfo.mobile.platform.core.config.MConfig;
import com.ustcinfo.mobile.platform.core.constants.Constants;
import com.ustcinfo.mobile.platform.core.core.MLogin;
import com.ustcinfo.mobile.platform.core.interfaces.LoginCallBack;
import com.ustcinfo.mobile.platform.core.model.UserInfo;
import com.ustcinfo.mobile.platform.core.utils.Key64;
import com.ustcinfo.mobile.platform.core.utils.MSharedPreferenceUtils;
import com.ustcinfo.mobile.platform.core.utils.RSAUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ustcinfo.mobile.platform.ui.activity.PatternLockActivity.FLAG_SP_LOCK_PASSWORD;

public class SplashActivity extends BaseActivity implements LoginCallBack {

    public static final int REQUEST_CODE_TO_PATTERN = 0x23; // 到达解锁的请求码

    private boolean autoLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestBaseWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        checkLoginBySSO();
        AmapLocation.get().lunch(this);
    }

    private void checkLoginBySSO() {
        if (!TextUtils.isEmpty(MSharedPreferenceUtils.queryStringBySettings(this, FLAG_SP_LOCK_PASSWORD))) {
            // 如果设置了手势密码,则进行解锁后登陆
            startActivityForResult(new Intent(this, PatternLockActivity.class), REQUEST_CODE_TO_PATTERN);
        } else {
            new MLogin(this, autoLogin, this).loginBySSO();
        }
    }

    private void goHomePage() {
        Intent i = new Intent();
        i.setClass(mActivity, HomePageActivity.class);
        mActivity.startActivity(i);
        mActivity.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TO_PATTERN && resultCode == RESULT_OK) {
            new MLogin(this, autoLogin, this).loginBySSO();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.anhui_activity_splash;
    }

    @Override
    public void onLoginStart() {
    }

    @Override
    public void onLoginSuccess(JSONObject responseObj) {

        try {
            Log.e("MLogin", responseObj.toString());
            com.alibaba.fastjson.JSONObject dataObj = com.alibaba.fastjson.JSONObject.parseObject(responseObj.getJSONObject("data").toString());

            String userId = dataObj.getString("userId") != null ? Key64.decipher(dataObj.getString("userId")) : "";
            if (!userId.equals(UserInfo.get().getUserIdCache())) {
                onLoginFailed(getString(com.ustcinfo.mobile.platform.core.R.string.login_exception));
                return;
            }

            String loginTime;
            try {
                loginTime = Key64.decipher(dataObj.getString("loginTime"));
                if (!MSharedPreferenceUtils.queryStringBySettings(getApplicationContext(), "loginTime").equals(loginTime)) {
                    onLoginFailed(getString(com.ustcinfo.mobile.platform.core.R.string.login_exception));
                    return;
                }

            } catch (Exception e) {
            }
            String areaCode = dataObj.getString("areaCode") != null ? dataObj.getString("areaCode") : "";
            String telephone = dataObj.getString("telephone") != null ? Key64.decipher(dataObj.getString("telephone")) : "";

            String userName = dataObj.getString("userName") != null ? Key64.decipher(dataObj.getString("userName")) : "";
            String organization = dataObj.getString("orgName") != null ? dataObj.getString("orgName") : "";
            String ticket = dataObj.getString("ticket") != null ? dataObj.getString("ticket") : "";
            if (dataObj.getString("accessToken") != null)
                MSharedPreferenceUtils.saveStringSettings(getApplicationContext(), "access_token", RSAUtils.decryptByPrivateKeyStr(dataObj.getJSONObject("accessToken").getString("access_token"), Constants.EOS_PRIVITE_KEY), true);
            //处理返回的应用列表
            AppStore.get().appsJson2List(responseObj);
            //将登陆数据进行缓存
            UserInfo.get().create(userId, areaCode, userName, UserInfo.get().getPasswordCache(), organization, ticket, telephone).save();
            MSharedPreferenceUtils.saveBooleanSettings(getApplicationContext(), Constants.KEY_PREFERENCE_AUTO_LOGIN, true);
            MSharedPreferenceUtils.saveBooleanSettings(getApplicationContext(), Constants.KEY_PREFERENCE_CHANGE_ACCOUNT, false);
            goHomePage();
        } catch (com.alibaba.fastjson.JSONException e) {
            e.printStackTrace();
            onLoginFailed(getString(com.ustcinfo.mobile.platform.core.R.string.data_parse_error_by_login));
        } catch (JSONException e) {
            e.printStackTrace();
            onLoginFailed(getString(com.ustcinfo.mobile.platform.core.R.string.data_parse_error_by_login));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onLoginFailed(String message) {
        toast(message);
        if ("没有此用户".equals(message)) {
            Intent i = new Intent();
            try {
                i.setClass(mActivity, Class.forName(MConfig.getActivity("login")));
                mActivity.startActivity(i);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        finish();
    }
}
