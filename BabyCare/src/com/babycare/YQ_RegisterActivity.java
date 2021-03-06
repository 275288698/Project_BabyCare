package com.babycare;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.babycare.base.CustomTitleActivity;
import com.babycare.httpconnect.HttpRestClient;
import com.babycare.httpconnect.JsonHttpResponseHandler;
import com.babycare.utils.AsyncCallBack;
import com.babycare.utils.BasicTool;
import com.babycare.utils.ConnectUtil;
import com.babycare.utils.L;
import com.babycare.utils.T;
import com.babycare.utils.UserConfig;
import com.loopj.android.http.RequestParams;

/**
 * 
 * @Description: TODO()
 * @author 易勤
 * @date 2015-3-18 下午12:31:38
 * @version V1.0
 */
public class YQ_RegisterActivity extends CustomTitleActivity {
	private Button register, resend, confirm;
	private View firstLayout, secondLayout;
	private EditText cellphone,regist_captcher,regist_psw,regist_equip;
	private String num;
	private TextView tv_phoneNumMark;
	private Handler hander = new Handler();
	private static int sSecond = 0;
	private final static int PERIOD = 60;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_register);
		super.onCreate(savedInstanceState);
		setTitle("注册");
		setTitleBarRightBtnText("");
	}

	@Override
	protected void initViews() {
		register = (Button) findViewById(R.id.btn_register);
		resend = (Button) findViewById(R.id.btn_resend);
		confirm = (Button) findViewById(R.id.btn_confirm);
		firstLayout = findViewById(R.id.layout_before);
		secondLayout = findViewById(R.id.layout_after);
		
		cellphone = (EditText) findViewById(R.id.cellphone);
		regist_captcher = (EditText) findViewById(R.id.regist_captcher);
		regist_psw = (EditText) findViewById(R.id.regist_psw);
		regist_equip = (EditText) findViewById(R.id.regist_equip);
		tv_phoneNumMark = (TextView) findViewById(R.id.tv_phoneNumMark);
		
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
		case R.id.btn_register://完成注册
			String captcha = regist_captcher.getText().toString();
			String psw = regist_psw.getText().toString();
			String equip = regist_equip.getText().toString();
			if (BasicTool.isNotEmpty(captcha)&&BasicTool.isNotEmpty(psw)) {
				if (psw.length()<6) {
					T.showShort(YQ_RegisterActivity.this, "密码不能少于六位！");
				}else {
					HttpPost_regist(num, captcha, psw, equip);
				}
			}
			
			
			break;
		case R.id.btn_resend://重新发送验证码
				if (sSecond==0) {
				HttpPost_Captcha(num);
				v.setEnabled(false);
				Timer timer = new Timer();
			        timer.schedule(new TimerTask() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							sSecond++;
							if (sSecond>PERIOD) {
								this.cancel();
								final Button b = (Button) v;
							
								hander.post(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										if (b!=null) {
											b.setText("重新发送");
											b.setEnabled(true);
											b.setBackgroundResource(R.drawable.bg_green_big_btn);
										}
									}
								});
								
								sSecond = 0 ;
							}else {
								
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
					}, 0,1000);
				}else {
					sSecond=0;
				}
			
			break;
		case R.id.btn_confirm://发送验证码
			num = cellphone.getText().toString();
		
			if (BasicTool.isCellphone(num)) {
				if (!BasicTool.isNetWork(YQ_RegisterActivity.this)) {
					T.showShort(YQ_RegisterActivity.this, "请检查手机网络是否链接！");
					return;
				}
				
				HttpRestClient.hasRegist(YQ_RegisterActivity.this, num, new JsonHttpResponseHandler(){
					private boolean hasRegist = false;
					@Override
					public void onSuccess(JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(response);
						try {
							switch (response.getInt("code")) {
							case 10000:
								break;
							default:
								hasRegist = true;
								break;
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						if (hasRegist) {
							T.showShort(YQ_RegisterActivity.this, num+"已经注册过了!");
						}else {
							showAlerDialog(num);
						}
					}
					
					
					
				});
				
			
			}else {
				T.showShort(YQ_RegisterActivity.this, "手机格式不正确！");
			}
			
			
			
			break;
		default:
			break;
		}
	}

	public void showAlerDialog(final String num) {
	
		
		View view = LayoutInflater.from(YQ_RegisterActivity.this).inflate(
				R.layout.dialog_register, null);
		TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
		TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
		TextView dialogContent = (TextView) view.findViewById(R.id.dialogContent);
		dialogContent.setText(dialogContent.getText().toString()+num);
		
		final Dialog mDeleteDialog = new Dialog(YQ_RegisterActivity.this, R.style.dialog);  
		mDeleteDialog.setContentView(view);  
		mDeleteDialog.show();  
		mDeleteDialog.getWindow().setGravity(Gravity.CENTER); 		
		
		tv_cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDeleteDialog.dismiss();

			}
		});
		tv_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDeleteDialog.dismiss();
				HttpPost_Captcha(num);
				
			}
		});

	}
	
	private void HttpPost_Captcha(String mobile) {
		RequestParams params = new RequestParams();
		
		params.put("mobile", mobile);
		String relativeUrl ="/auths/send_captcha";
		ConnectUtil.getRequest(YQ_RegisterActivity.this, relativeUrl, params, new CaptchaTask(YQ_RegisterActivity.this));
	}
	
	private class CaptchaTask extends AsyncCallBack{

		public CaptchaTask(Context context) {
			super(context);
		}

		@Override//这块显示联网的提示信息内容
		public String getLoadingMsg() {
			return getString(R.string.submit_tip);
		}

		@Override
		protected void resultCallBack(JSONObject result) {
		
			try {
				switch (result.getInt("code")) {
				case 10000:
					firstLayout.setVisibility(View.GONE);
					secondLayout.setVisibility(View.VISIBLE);
					tv_phoneNumMark.setText("验证码已发送到"+num+" :");
					T.showShort(YQ_RegisterActivity.this, "验证码发送成功！");
					break;
				case 10101:
					T.showShort(YQ_RegisterActivity.this, "手机格式错误");
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
	
	
	
	
	private void HttpPost_regist(String mobile,String captcha,String password,String device) {
		HttpRestClient.registe(YQ_RegisterActivity.this, mobile, captcha, password, device, new JsonHttpResponseHandler(){
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}
			@Override
			public void onSuccess(JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(response);
				try {
					switch (response.getInt("code")) {
					case 10000:
						UserConfig.sToken = response.getString("token");
						startActivity(new Intent(YQ_RegisterActivity.this,YQ_LoginActivity.class));
						break;
					case 10301:
						T.showShort(YQ_RegisterActivity.this, "手机格式错误!  看到句话，肯定是服务器问题！");
						break;
					case 10302:
						T.showShort(YQ_RegisterActivity.this, "手机验证码错误");
						break;
					case 10303:
						T.showShort(YQ_RegisterActivity.this, "密码格式不正确");
						break;
					default:
						break;
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				T.showLong(getApplicationContext(), "服务器出问题了~~"+content);
				L.e(content);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}

		});
//		RequestParams params = new RequestParams();
//		
//		params.put("mobile", mobile);
//		params.put("captcha", captcha);
//		params.put("password", password);
//		params.put("device", device);
//		
//		String relativeUrl ="/auths/register";
//		
//		ConnectUtil.postRequest(YQ_RegisterActivity.this, relativeUrl, params, new NormalTask(YQ_RegisterActivity.this));
	}
	private class NormalTask extends AsyncCallBack{

		public NormalTask(Context context) {
			super(context);
		}

		@Override//这块显示联网的提示信息内容
		public String getLoadingMsg() {
			return getString(R.string.submit_tip);
		}
	

		@Override
		protected void resultCallBack(JSONObject result) {
			
			try {
				switch (result.getInt("code")) {
				case 10000:
					UserConfig.sToken = result.getString("token");
					startActivity(new Intent(YQ_RegisterActivity.this,YQ_LoginActivity.class));
					break;
				case 10301:
					T.showShort(YQ_RegisterActivity.this, "手机格式错误!  看到句话，肯定是服务器问题！");
					break;
				case 10302:
					T.showShort(YQ_RegisterActivity.this, "手机验证码错误");
					break;
				case 10303:
					T.showShort(YQ_RegisterActivity.this, "密码格式不正确");
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

}
