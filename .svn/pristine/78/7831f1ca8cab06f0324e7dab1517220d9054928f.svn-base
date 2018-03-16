package com.ustcinfo.mobile.platform.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ustcinfo.mobile.platform.R;
import com.ustcinfo.mobile.platform.ability.retrofit.OkhttpUtils;
import com.ustcinfo.mobile.platform.core.appstore.AppStore;
import com.ustcinfo.mobile.platform.core.config.MConfig;
import com.ustcinfo.mobile.platform.core.constants.Constants;
import com.ustcinfo.mobile.platform.core.core.MLogin;
import com.ustcinfo.mobile.platform.core.interfaces.HttpRequestCallbak;
import com.ustcinfo.mobile.platform.core.interfaces.LoginCallBack;
import com.ustcinfo.mobile.platform.core.model.UserInfo;
import com.ustcinfo.mobile.platform.core.utils.Key64;
import com.ustcinfo.mobile.platform.core.utils.MHttpClient;
import com.ustcinfo.mobile.platform.core.utils.MSharedPreferenceUtils;
import com.ustcinfo.mobile.platform.core.utils.RSAUtils;
import com.ustcinfo.mobile.platform.core.utils.RequestParams;
import com.ustcinfo.mobile.platform.utils.SubCharSequence;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    private boolean isHide = true;

    @BindView(R.id.account_editor)
    EditText userNameView;

    @BindView(R.id.account_password)
    EditText userPassword;

    @BindView(R.id.password_eye)
    ImageView password_show;

    @BindView(R.id.password_editor)
    EditText verifyCodeView;

    @BindView(R.id.verify_btn)
    Button getVerifyBtn;

    @BindView(R.id.login_btn)
    Button loginBtn;

    @BindView(R.id.container)
    LinearLayout container;

    @BindView(R.id.scroller)
    ScrollView scroller;

    private CountDownTimer verifyCodePassedTimer;
    public static final String APPID = "appId";
    public static final String APPNAME = "appName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestBaseWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initView();
        initMoveKeyBoard(container, scroller, userNameView, verifyCodeView, userPassword);

    }

    @Override
    protected void onStop() {
        super.onStop();
        userNameView.setText("");
        userPassword.setText("");
        verifyCodeView.setText("");
        userNameView.requestFocus() ;
    }

    @OnClick(R.id.verify_btn)
    public void getVerify(View v) {
        if (!TextUtils.isEmpty(userNameView.getText().toString())) {
            getVerifyCode();
        } else {
            toast(getString(R.string.input_user_name_please));
        }
    }

    @OnClick(R.id.forget_password)
    public void resetPassword(View v) {
        Intent i = new Intent(this, ModifyPasswordActivity.class);
        i.putExtra("type", "reset");
        startActivity(i);
    }

    @OnClick(R.id.password_eye)
    public void showPassword(View view) {
        if (isHide) {
            password_show.setImageResource(R.mipmap.open);
            userPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isHide = false;
        } else {
            password_show.setImageResource(R.mipmap.close);
            userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isHide = true;
        }
        int index = userPassword.getText().toString().length();
        userPassword.setSelection(index);
    }

    @OnClick(R.id.login_btn)
    public void login(View v) {
        if (validate()) {
            String userName = userNameView.getText().toString();
            String verifyCode = verifyCodeView.getText().toString();
            String userPassWord = userPassword.getText().toString();
            new MLogin(this, true, new LoginCallBack() {
                @Override
                public void onLoginStart() {
                    showProgressBar();
                }

                @Override
                public void onLoginSuccess(JSONObject responseObj) {

                    try {
                        Log.e("MLogin", responseObj.toString());
                        com.alibaba.fastjson.JSONObject dataObj = com.alibaba.fastjson.JSONObject.parseObject(responseObj.getJSONObject("data").toString());

                        String userId = dataObj.getString("userId") != null ? Key64.decipher(dataObj.getString("userId")) : "";
                        if (!userId.equals(userName)) {
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
                        UserInfo.get().create(userId, areaCode, userName, userPassWord, organization, ticket, telephone).save();
                        MSharedPreferenceUtils.saveBooleanSettings(getApplicationContext(), Constants.KEY_PREFERENCE_AUTO_LOGIN, false);
                        MSharedPreferenceUtils.saveBooleanSettings(getApplicationContext(), Constants.KEY_PREFERENCE_CHANGE_ACCOUNT, false);
                        goHomePage();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        MSharedPreferenceUtils.saveStringSettings(mActivity, Constants.KEY_PREFERENCE_LOGIN_TIME, sdf.format(new Date()), true);
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
                    mActivity.toast(message);
                    closeProgressBar();
                }
            }).login(userName, userPassWord, verifyCode, false);
        }
    }

    private void goHomePage() {
        Intent i = new Intent();
        i.setClass(mActivity, HomePageActivity.class);
        if (getIntent().getStringExtra(APPNAME) != null && getIntent().getStringExtra(APPID) != null) {
            i.putExtra(HomePageActivity.APPNAME, getIntent().getStringExtra(APPNAME));
            i.putExtra(HomePageActivity.APPID, getIntent().getStringExtra(APPID));
        }

        mActivity.startActivity(i);
        mActivity.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.anhui_activity_login;
    }

    public void initView() {
        initValues();
        verifyCodeView.setTransformationMethod(new TransformationMethod() {

            @Override
            public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {

            }

            @Override
            public CharSequence getTransformation(CharSequence source, View view) {
                return new SubCharSequence(source);
            }
        });
        userPassword.setTransformationMethod(new TransformationMethod() {

            @Override
            public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {

            }

            @Override
            public CharSequence getTransformation(CharSequence source, View view) {
                return new SubCharSequence(source);
            }
        });
    }

    private void initValues() {
        //获取验证码自动倒计时
        verifyCodePassedTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                getVerifyBtn.setText(String.format(getString(R.string.get_verify_code_after_seconds), (int) (l / 1000)));
            }

            @Override
            public void onFinish() {
                getVerifyBtn.setEnabled(true);
                getVerifyBtn.setText(getString(R.string.retry_verify_code));
            }
        };
    }

    private boolean validate() {
        if (TextUtils.isEmpty(userNameView.getText().toString())) {
            toast(getString(R.string.input_user_name_please));
            return false;
        }
        if (TextUtils.isEmpty(userPassword.getText().toString())) {
            toast(getString(R.string.input_password_please));
            return false;
        }
        if (TextUtils.isEmpty(verifyCodeView.getText().toString())) {
            toast(getString(R.string.input_verify_code_please));
            return false;
        }

        return true;
    }

    //获取验证码
    private void getVerifyCode() {
        RequestParams p = new RequestParams();
        p.put("userId", userNameView.getText().toString());
        showProgressBar("正在获取验证码请稍后");
        MHttpClient.get().post(MConfig.get("getVerifyCode"), p, new HttpRequestCallbak() {
            @Override
            public void onSuccess(JSONObject responseObj) {
                closeProgressBar();
                verifyCodePassedTimer.start();
                getVerifyBtn.setEnabled(false);
            }

            @Override
            public void onFailed(String msg) {
                closeProgressBar();
                toast(msg);
                getVerifyBtn.setEnabled(true);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivity.dismissProgressDialog();
        verifyCodePassedTimer.cancel();
    }
}
