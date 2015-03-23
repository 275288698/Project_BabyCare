package com.babycare;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.babycare.base.CustomTitleActivity;
import com.babycare.tool.T;

/** 

 * @Description: TODO() 
 * @author 易勤
 * @date 2015-3-19 上午11:15:16 
 * @version V1.0 
 */
public class YQ_AboutUsActivity extends CustomTitleActivity {
	private View layout_version,layout_protocal,layout_visitorus,layout_comment;
	private TextView tv_version,tv_name;
	private ImageView iv_head;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_aboutus);
		super.onCreate(savedInstanceState);
		setTitle("关于我们");
		setTitleBarRightBtnText("");
		setTitleBarLeftBtnText("返回");
	}

	@Override
	protected void initViews() {
		layout_version = findViewById(R.id.arrow1);
		layout_protocal = findViewById(R.id.arrow2);
		layout_visitorus = findViewById(R.id.arrow3);
		layout_comment = findViewById(R.id.arrow4);
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
		case R.id.layout_version:
			T.showShort(YQ_AboutUsActivity.this, "已经是最新版本了");
			break;
		case R.id.layout_protocal:
			T.showShort(YQ_AboutUsActivity.this, "协议详情 请登录官网");
			break;
		case R.id.layout_visitorus:
			T.showShort(YQ_AboutUsActivity.this, "www.???.com");
			break;
		case R.id.layout_comment:
			T.showShort(YQ_AboutUsActivity.this, "已经评过分了");
			break;


	
		default:
			break;
		}
	
	}
	
	

}