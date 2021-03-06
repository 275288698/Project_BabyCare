package com.babycare;

import generalplus.com.GPComAir5Lib.User;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.babycare.app.MyAplication;
import com.babycare.base.CustomTitleActivity;
import com.babycare.utils.AsyncCallBack;
import com.babycare.utils.BasicTool;
import com.babycare.utils.ConnectUtil;
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
public class YQ_LoginActivity extends CustomTitleActivity {
	private Button register, resend, confirm;
	private EditText userName,password;
	private SharedPreferences mSharedPreferences;
	private String name;
	private String psw;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_login);
		super.onCreate(savedInstanceState);
		setTitle("勇娃");
		setTitleBarRightBtnText("");
		setTitleBarLeftBtnText("");
		MyAplication aplication = (MyAplication) getApplication();
		aplication.activities.add(this);
		mSharedPreferences = getSharedPreferences("logginStatus", Context.MODE_PRIVATE);
		name = mSharedPreferences.getString("mobile", "");
		psw = mSharedPreferences.getString("password", "");
		if (BasicTool.isNotEmpty(name)&&BasicTool.isNotEmpty(psw)) {
			if (BasicTool.isNetWork(YQ_LoginActivity.this)) {
				HttpPost(name, psw);
			}else {
				T.showShort(YQ_LoginActivity.this, "请检查网络是否打开");
			}
		}
	}

	@Override
	protected void initViews() {
		register = (Button) findViewById(R.id.btn_register);
		resend = (Button) findViewById(R.id.btn_resend);
		confirm = (Button) findViewById(R.id.btn_confirm);
		userName = (EditText) findViewById(R.id.userName);
		password = (EditText) findViewById(R.id.password);
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

	public void onBtnClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			name = userName.getText().toString();
			psw = password.getText().toString();
			if (BasicTool.isNotEmpty(name)&&BasicTool.isNotEmpty(psw)) {
				HttpPost(name, psw);
			}else {
//				showAlerDialog();
			}
			
			
//			startActivity(new Intent(YQ_LoginActivity.this,ContentMainActivity.class));
			break;
		case R.id.tv_register:
			startActivity(new Intent(YQ_LoginActivity.this,YQ_RegisterActivity.class));
			break;
		case R.id.go_directly:

//			Intent intent = new Intent(YQ_LoginActivity.this,generalplus.com.BabudouAssistant.MainActivity.class);
			Intent intent = new Intent(YQ_LoginActivity.this,ContentMainActivity.class);
			intent.putExtra("isLogging", false);
			startActivity(intent);
			this.finish();
//			startActivity(new Intent(YQ_LoginActivity.this,ContentMainActivity.class));
			break;
		case R.id.tv_forgetPW:
			startActivity(new Intent(YQ_LoginActivity.this,ForgetPwdActivity.class));
			break;
		default:
			break;
		}
	}
	private void HttpPost(String userName,String psw) {
		RequestParams params = new RequestParams();
		
		params.put("mobile", userName);
		params.put("password", psw);
		
		String relativeUrl ="/auths/login";
		
		ConnectUtil.postRequest(YQ_LoginActivity.this, relativeUrl, params, new NormalTask(YQ_LoginActivity.this));
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
//					Intent intent = new Intent(YQ_LoginActivity.this,generalplus.com.BabudouAssistant.MainActivity.class);
					Intent intent = new Intent(YQ_LoginActivity.this,ContentMainActivity.class);
					intent.putExtra("isLogging", true);
					startActivity(intent);
					mSharedPreferences.edit().putString("mobile", name).putString("password", psw).commit();
					finish();
					break;
				default:
					T.showShort(YQ_LoginActivity.this, "账号或密码错误");
					break;
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			

		}
	}
	
public void showAlerDialog() {
	
		
		View view = LayoutInflater.from(YQ_LoginActivity.this).inflate(
				R.layout.dialog_login, null);
		TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
		TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
		
		final Dialog mDeleteDialog = new Dialog(YQ_LoginActivity.this, R.style.dialog);  
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
				showSelectDeviceDialog();
			}
		});
	}
	

public void showSelectDeviceDialog() {
	
	
	View view = LayoutInflater.from(YQ_LoginActivity.this).inflate(
			R.layout.dialog_login_device, null);
	TextView tv_cellphone = (TextView) view.findViewById(R.id.tv_cellphone);
	TextView tv_stroyDevice = (TextView) view.findViewById(R.id.tv_stroyDevice);
	
	final Dialog mDeleteDialog = new Dialog(YQ_LoginActivity.this, R.style.dialog);  
	mDeleteDialog.setContentView(view);  
	mDeleteDialog.show();  
	mDeleteDialog.getWindow().setGravity(Gravity.CENTER); 		
	
	tv_cellphone.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mDeleteDialog.dismiss();
			startActivity(new Intent(YQ_LoginActivity.this,YQ_RegisterActivity.class));
		}
	});
	tv_stroyDevice.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mDeleteDialog.dismiss();
			startActivity(new Intent(YQ_LoginActivity.this,YQ_BindActivity.class));
		}
	});
}
}
