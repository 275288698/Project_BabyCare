package com.babycare;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.babycare.base.CustomTitleActivity;
import com.babycare.utils.AsyncCallBack;
import com.babycare.utils.BasicTool;
import com.babycare.utils.ConnectUtil;
import com.babycare.utils.T;
import com.babycare.utils.UserConfig;
import com.loopj.android.http.RequestParams;

public class ForgetPwdActivity extends CustomTitleActivity {
	private EditText cellphoneNum, captcherNum, newPassword;
	private static int sSecond = 0;
	private final static int PERIOD = 60;
	private Handler hander = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.forget_pwd);
		super.onCreate(savedInstanceState);
		setTitle("忘记密码");
		setTitleBarRightBtnText("");
		setTitleBarLeftBtnText("返回");
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		cellphoneNum = (EditText) findViewById(R.id.cellphoneNum);
		captcherNum = (EditText) findViewById(R.id.captcherNum);
		newPassword = (EditText) findViewById(R.id.newPassword);
	}

	@Override
	protected void initDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void installListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void uninstallListeners() {
		// TODO Auto-generated method stub

	}

	public void onBtnClick(final View v) {
		switch (v.getId()) {
		case R.id.btn_resend:
			String num = cellphoneNum.getText().toString();
			if (BasicTool.isCellphone(num)) {
				if (sSecond == 0) {
					HttpPost_Captcha(num);
					v.setEnabled(false);
					 Timer timer = new Timer();
					
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							sSecond++;
							if (sSecond > PERIOD) {
								this.cancel();
								final Button b = (Button) v;
							
								hander.post(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										if (b!=null) {
											b.setText("获取验证码");
											b.setEnabled(true);
											b.setBackgroundResource(R.drawable.bg_green_big_btn);
										}
										
									
									}
								});
								sSecond = 0;
							} else {
								final Button b = (Button) v;
								hander.post(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										if (b!=null) {
											b.setText(PERIOD - sSecond + "秒");
											b.setBackgroundResource(R.drawable.bg_green_verification_btn);
										}
									}
								});
							}

						}
					}, 0, 1000);
				} else {
					sSecond = 0;
				}
			} else {
				T.showShort(ForgetPwdActivity.this, "手机号格式不正确！");
			}

			break;
		case R.id.btn_register:
			String userName = cellphoneNum.getText().toString();
			String psw = newPassword.getText().toString();
			String captcha = captcherNum.getText().toString();
			if (BasicTool.isCellphone(userName) && BasicTool.isNotEmpty(psw)
					&& BasicTool.isNotEmpty(captcha)) {
				HttpPost(userName, psw, captcha);
			} else {
				T.showShort(ForgetPwdActivity.this, "请检查是否填写正确！");
			}
			break;

		default:
			break;
		}
	}

	private void HttpPost(String userName, String psw, String captcha) {
		RequestParams params = new RequestParams();

		params.put("mobile", userName);
		params.put("password", psw);
		params.put("captcha", captcha);

		String relativeUrl = "/auths/reset_password";

		ConnectUtil.postRequest(ForgetPwdActivity.this, relativeUrl, params,
				new NormalTask(ForgetPwdActivity.this));
	}

	private class NormalTask extends AsyncCallBack {

		public NormalTask(Context context) {
			super(context);
		}

		@Override
		// 这块显示联网的提示信息内容
		public String getLoadingMsg() {
			return getString(R.string.submit_tip);
		}

		@Override
		protected void resultCallBack(JSONObject result) {

			try {
				switch (result.getInt("code")) {
				case 10000:
					T.showShort(ForgetPwdActivity.this, "密码修改成功！");
					startActivity(new Intent(ForgetPwdActivity.this,
							YQ_LoginActivity.class));
					break;
				case 10601:
					T.showShort(ForgetPwdActivity.this, "手机号未注册");
					break;
				case 10602:
					T.showShort(ForgetPwdActivity.this, "验证码错误");
					break;
				case 10603:
					T.showShort(ForgetPwdActivity.this, "密码格式错误");
					break;

				default:
					break;
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	private void HttpPost_Captcha(String mobile) {
		RequestParams params = new RequestParams();

		params.put("mobile", mobile);

		String relativeUrl = "/auths/send_captcha";

		ConnectUtil.getRequest(ForgetPwdActivity.this, relativeUrl, params,
				new CaptchaTask(ForgetPwdActivity.this));
	}

	private class CaptchaTask extends AsyncCallBack {

		public CaptchaTask(Context context) {
			super(context);
		}

		@Override
		// 这块显示联网的提示信息内容
		public String getLoadingMsg() {
			return getString(R.string.submit_tip);
		}

		@Override
		protected void resultCallBack(JSONObject result) {

			try {
				switch (result.getInt("code")) {
				case 10000:
					T.showShort(ForgetPwdActivity.this, "验证码发送成功！");
					break;
				case 10101:
					T.showShort(ForgetPwdActivity.this, "手机格式错误");
					break;

				default:
					break;
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		
	}
}
