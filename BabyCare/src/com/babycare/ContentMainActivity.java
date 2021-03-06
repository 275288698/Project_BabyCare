package com.babycare;

import generalplus.com.BabudouAssistant.DBAdapter;
import generalplus.com.BabudouAssistant.globe;
import generalplus.com.GPComAir5Lib.CaimiyuPerson;
import generalplus.com.GPComAir5Lib.ComAir5Wrapper;
import generalplus.com.GPComAir5Lib.Person;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.babycare.ElectronicFence.MyLocationListenner;
import com.babycare.adapter.ImageAdapter;
import com.babycare.app.MyAplication;
import com.babycare.utils.BasicTool;
import com.babycare.utils.JSONUtils;
import com.babycare.utils.ViewHolder;
import com.babycare.view.DrawerView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.nplatform.comapi.basestruct.GeoPoint;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class ContentMainActivity extends FragmentActivity {
	private View layout_topvView,layout_bottomView;
	
protected SlidingMenu side_drawer;
	

	private ProgressBar top_progress;

	private ImageView top_refresh;

	private ImageView top_head;

	private ImageView top_more;

	public final static int CHANNELREQUEST = 1;

	public final static int CHANNELRESULT = 10;
	
	private boolean isOpen ;

	private MapView iv_map;

	@Override
		protected void onCreate(Bundle arg0) {
			super.onCreate(arg0);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.content_main);
			layout_topvView =findViewById(R.id.layout_top);
			layout_bottomView=findViewById(R.id.layout_bottom);
			iv_map = (MapView)findViewById(R.id.iv_map);
			onCreate();
			initView();
			initSlidingMenu();
		}
	

	private void initView() {
	
		
		
		top_head = (ImageView) findViewById(R.id.top_head);
		top_more = (ImageView) findViewById(R.id.top_more);
		top_more.setBackgroundResource(R.drawable.titlebar_on_mainright);
		top_refresh = (ImageView) findViewById(R.id.top_refresh);
//		top_progress = (ProgressBar) findViewById(R.id.top_progress);
		
		top_head.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(side_drawer.isMenuShowing()){
					side_drawer.showContent();
				}else{
					side_drawer.showMenu();
				}
			}
		});
		top_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(side_drawer.isSecondaryMenuShowing()){
					side_drawer.showContent();
					top_more.setBackgroundResource(R.drawable.titlebar_on_mainright);
				}else{
					top_more.setBackgroundResource(R.drawable.titlebar_off_mainright);
					side_drawer.showSecondaryMenu();
					
				}
			}
		});
	}
	
	protected void initSlidingMenu() {
		side_drawer = new DrawerView(this).initSlidingMenu();
		
		side_drawer.setOnCloseListener(new OnCloseListener() {
			
			@Override
			public void onClose() {
				top_more.setBackgroundResource(R.drawable.titlebar_on_mainright);
				
				
//				if (top_more.isChecked()) {
//					top_more.setChecked(false);
//				}
				
			}
		});
	}
	
	
	
	
	//==============================================
	//==============================================
	//==============================================
	//==============================================
	//==============================================
	//==============================================
	//==============================================
	//==============================================
	
	private ComAir5Wrapper m_ComAir5 = new ComAir5Wrapper();
	private MediaPlayer mPlayer = null;
	private MyAdapter adapter;
	private ArrayAdapter<CaimiyuPerson> caimiyuadapter;
	
	private List<Person> persons;
	private ArrayList<CaimiyuPerson> caimiyupersons;

	public AudioManager audioManager;
	
//	private TextView buttongushi,buttonerge,buttonyingyu,buttonrenzhi;
	
//	private Button rotary;
	private RelativeLayout prev;
	private RelativeLayout playpause;
	private RelativeLayout next;
	private RelativeLayout plus;
	private RelativeLayout minus;
//	private Button lock;
	private ImageView poweroff,playImage;

	
	
	private ListView listView;
	private View titlebar;
	
 	private ProgressDialog MyDialog;
	private Thread newThread; 
	private int [] one = new int[300];
	private int [] two = new int[300];
	int i32Cmd,i32Count,i=0,j=0,k=0,h=0;
	long lastClick;
	private boolean flag=true;
	
	 private boolean scrollFlag = false;//标记是否滑动
	 private int lastVisibleItemPosition;// 标记上次滑动位置
	
//	private static ArrayList<ProvinceBean> parentData = new ArrayList<ProvinceBean>();
//	private String url_add = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx/getRegionProvince";
	private String PROVINCE_URL = "http://www.ubabytv.com.cn/llyapp/getRegionProvince_byname.php";
	private String[] cityName;
//	String catalog;  //收藏中的每个目录
	private ProgressDialog prodialog;
//	JSONParser jsonParser = new JSONParser();
//	JSONObject json22;
	static InputStream is = null;
	static JSONObject  jObj = null;

//	static String json ;
	DBAdapter db1;
	private boolean isPlaying = false;
	private boolean isLogging = false;
	private TextView textView ;
	private Gallery listener_4_gallery = null;
	private ImageAdapter imgAdapter = null; 
	private View bg_cd;
	private int [] bg = {R.drawable.bg_babysong,R.drawable.bg_babystory,R.drawable.bg_babyteach,R.drawable.bg_poetry,R.drawable.bg_why};
	private View parentView;
	private void init() {
		LayoutInflater inflater = LayoutInflater.from(ContentMainActivity.this);
		parentView = inflater.inflate(R.layout.layout_headlistview, null);
		
		listener_4_gallery = (Gallery) parentView.findViewById(R.id.listener_4_gallery);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		imgAdapter = new ImageAdapter(this, dm.heightPixels,
				dm.widthPixels);
		listener_4_gallery.setAdapter(imgAdapter); 
		listener_4_gallery.setGravity(Gravity.CENTER_VERTICAL); 
		// gallery.setSelection(0); //
		listener_4_gallery.setOnItemClickListener(clickListener);
		listener_4_gallery.setOnItemSelectedListener(selectedListener); 
		listener_4_gallery.setUnselectedAlpha(0.5f); 
		listener_4_gallery.setSpacing(dm.widthPixels / 6); 
		bg_cd = parentView.findViewById(R.id.bg_cd);
	}

	AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

		}
	};

	AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			
			bg_cd.setBackgroundResource(bg[position]);
			switch (position) {
			case 0:
				change(0);
				break;
			case 1:
				change(1);
				break;
			case 2:
				change(2);
				break;
			case 3:
				change(3);
				break;
			case 4:
				change(4);
				break;
			default:
				break;
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	};
	
    public void onCreate() {
//        setContentView(R.layout.main_activity);
    	MyAplication aplication = (MyAplication) getApplication();
		aplication.activities.add(this);
		initMaps();
		db1 = new DBAdapter(this);
        isLogging =getIntent().getBooleanExtra("isLogging", false);
        textView= (TextView) findViewById(R.id.title_back_btn);
        if (isLogging) {
        	textView.setText("登出");

            findViewById(R.id.backBtn).setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				showAlerDialog();
    			}
    		});
		}else {
			textView.setText("登录");

	        findViewById(R.id.backBtn).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(new Intent(ContentMainActivity.this,YQ_LoginActivity.class));
					ContentMainActivity.this.finish();
				}
			});
		}
        
        MyAplication appKiller = (MyAplication) getApplication();
		appKiller.activities.add(this);
        
        listView = (ListView) findViewById(R.id.listView);
        titlebar = findViewById(R.id.titlebar_bg);
        titlebar.getBackground().setAlpha(0);
        prev=(RelativeLayout)findViewById(R.id.preBtn);
//        rotary=(Button)findViewById(R.id.rBtn);
        playpause=(RelativeLayout)findViewById(R.id.pauseBtn);
        next=(RelativeLayout)findViewById(R.id.next1Btn);
        plus=(RelativeLayout)findViewById(R.id.volaBtn);
        minus=(RelativeLayout)findViewById(R.id.volpBtn);
//        lock=(Button)findViewById(R.id.lockBtn);
        poweroff=(ImageView)findViewById(R.id.poBtn);
        playImage=(ImageView)findViewById(R.id.i3);
        
//        buttongushi=(TextView)findViewById(R.id.gushiBtn);
//    	buttongushi.setBackgroundColor(Color.parseColor("#b2b2b2"));
//        buttonerge=(TextView)findViewById(R.id.ergeBtn);
//        buttonyingyu=(TextView)findViewById(R.id.yingyuBtn);
//        buttonrenzhi=(TextView)findViewById(R.id.renzhiBtn);

        
		prev.setOnClickListener(click);
//		rotary.setOnClickListener(click);
		playpause.setOnClickListener(click);
		next.setOnClickListener(click);
		plus.setOnClickListener(click);
		minus.setOnClickListener(click);
//		lock.setOnClickListener(click);
		poweroff.setOnClickListener(click);


				
//		buttongushi.setOnClickListener(click);
//		buttonerge.setOnClickListener(click);
//		buttonyingyu.setOnClickListener(click);
//
//		buttonrenzhi.setOnClickListener(click);

		
		
		    
        if(ContentMainActivity.this.getRequestedOrientation()==1){
//          Toast.makeText(ContentMainActivity.this, "无法获取系统朝向", Toast.LENGTH_LONG).show(); 
         
        }else{
        	if(ContentMainActivity.this.getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
        		globe.x=1;
        		ContentMainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        	}else if(ContentMainActivity.this.getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT){
        		ContentMainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        		globe.x=2;
        	}
        	
        }
//        System.out.println("111");
        persons = ContentMainActivity.this.readXml(0);
//        System.out.println("222");
      //simple_expandable_list_item_1是前面留有空格；simple_list_item_1置顶
        adapter = new MyAdapter(ContentMainActivity.this,
				 persons);
        init();
        listView.addHeaderView(parentView);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bg_cd.getLayoutParams(); // 取控件viewPager当前的布局参数
        params.height = BasicTool.dip2px(ContentMainActivity.this, 220);// 当控件的高强制设成height像素  params.height = height;得出的结果莫测为30
        bg_cd.setLayoutParams(params); // 使设置好的布局参数应用到控件viewPager
        
        listView.setAdapter(adapter);
        audioManager=(AudioManager) getSystemService(Service.AUDIO_SERVICE);
        
        listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				  switch(scrollState){  
			        case OnScrollListener.SCROLL_STATE_IDLE://空闲状态 
			        	scrollFlag = false;
			        	
			        	break;
			        case OnScrollListener.SCROLL_STATE_FLING://滚动状态  
			        	scrollFlag = false;
			        	
//			        	titlebar.getBackground().setAlpha(0);
			            break;  
			        case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动  
			        	scrollFlag = true;
//			        	titlebar.getBackground().setAlpha(150);
			        	//初始化  
//			        	Animation alphaAnimation = new AlphaAnimation(0f, 0.6f);  
			        //	alphaAnimation.setInterpolator(new OvershootInterpolator());
//			        	alphaAnimation.setDuration(1000);     
//			        	alphaAnimation.setFillAfter(true);
//			        	titlebar.startAnimation(alphaAnimation); 
//			        	alphaAnimation.setAnimationListener(new MyAnim());
			        	
//			        	titlebar.getBackground().setAlpha(0);
			            break;  
			            
			        } 
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
			    
				int last = firstVisibleItem + visibleItemCount;
			    if (scrollFlag) {

			    if (last > lastVisibleItemPosition) {
			    //上滑
			    	titlebar.getBackground().setAlpha(150);
			    }

			    if (last < lastVisibleItemPosition) {
			    //下滑
			    	if(firstVisibleItem==0){  
				    	 isTop = true;
				    	 titlebar.getBackground().setAlpha(0);
		              } else {
		            	  isTop = false;
		            	  titlebar.getBackground().setAlpha(180);
					}
			    }

			    if (last == lastVisibleItemPosition) {

			    return;

			    }

			    lastVisibleItemPosition = firstVisibleItem+ visibleItemCount;

			    }
			}
		});
        
      
        
		listView.setOnItemClickListener(new OnItemClickListener() {
		        	@Override	       
	        public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
		        	if(arg2==0){ 
		        		return	;
		        	}else {
		        		arg2--;
					}		        		
		        		
	        	if (System.currentTimeMillis() - lastClick <= 3100)  
			        {  
			            return;  
			        }  
			        lastClick = System.currentTimeMillis();  
			        if(flag){
			        	flag=false;
			        	MyDialog = ProgressDialog.show( ContentMainActivity.this,"", "正在发送...");       	
			        	newThread = new Thread(new Runnable() {   
				        	@Override 
				        	public void run() {  
				        		SystemClock.sleep(3000);
				        		handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
				        	}
				        });
				        newThread.start(); //启动线程	
				        //根据id=arg2获取listview每个item对应的两个声波码，从对应的数组中获取到对应元素中数字
		//		        for(i=0;i<=260;i++)
		//		        {
		//		        	if(i==arg2)
		//		        	m_ComAir5.PlayComAirCmd(one[i], globe.voice);
		//		        }
		//		        for(j=0;j<=260;j++)
		//			    {
		//		        	if(j==arg2)
		//					m_ComAir5.PlayComAirCmd2(two[j], globe.voice);
		//			    }
				        for(i=0,j=0;i<=260;i++,j++)
				        {
				        	if(i==(arg2-1))
				        	m_ComAir5.PlayComAirCmd1(one[i],two[j], globe.voice);
		
				        }
				        flag=true;
			    }
	        }	
	    });	
		
	}
	
	public void showToast(String str) {  
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();  
          
    } 
 
	@Override
	protected void onDestroy() {
		MyAplication app = (MyAplication) getApplication();
		app.activities.remove(this);
		super.onDestroy();
		iv_map.onDestroy();
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			return true;
		} else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			return true;
		}else if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){

//			Intent i= new Intent(Intent.ACTION_MAIN);
//			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //android123提示如果是服务里调用，必须加入new task标识  
//			i.addCategory(Intent.CATEGORY_HOME);
//			startActivity(i);  
			exitBy2Click();  
			
//			globe.x=0;	
//			MyAplication appKiller = (MyAplication) getApplication();
//			List<Activity> list = appKiller.activities;
//			for (Activity activity : list) {
//					activity.finish();
//				}	
			
			return true;
		}else{
			
			return false;
		}
	}
	
	
	public OnClickListener click =new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){        
//			case R.id.gushiBtn:	
//
//				change1();				
//				break;
//			
//			case R.id.ergeBtn:	
//
//
//				change2();				
//				break;
//				
//			case R.id.renzhiBtn:	
//
//			
//				change3();			
//				break;	
//				
//			case R.id.yingyuBtn:
//
//
//				change4();				
//				break;	
//				
		
					
			case R.id.rBtn:				
				 if(ContentMainActivity.this.getRequestedOrientation()==-1){
			          Toast.makeText(ContentMainActivity.this, "系统朝向无法获得", Toast.LENGTH_LONG).show();        
			        }else{
			        	if(ContentMainActivity.this.getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
			        		globe.x=1;
			        		ContentMainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
			        	}else if(ContentMainActivity.this.getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT){
			        		globe.x=2;
			        		ContentMainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			        	}
			        }
				break;
			case R.id.preBtn:
				
				if (System.currentTimeMillis() - lastClick <= 1500)  
		        {   
		            return;  
		        }  
		        lastClick = System.currentTimeMillis();  	
				MyDialog = ProgressDialog.show( ContentMainActivity.this,"", "正在发送...");       	
	        	newThread = new Thread(new Runnable() {   
		        	@Override 
		        	public void run() {  
		        		SystemClock.sleep(1500);
		        		handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
		        	}
		        });
		        newThread.start(); //启动线程	
				m_ComAir5.PlayComAirCmd2(1, globe.voice);
				break;		
				
			case R.id.pauseBtn:
				
				if (System.currentTimeMillis() - lastClick <= 1500)  
		        {  
		            return;  
		        }  
				
				
		        lastClick = System.currentTimeMillis();  
				
				MyDialog = ProgressDialog.show( ContentMainActivity.this,"", "正在发送...");       	
	        	newThread = new Thread(new Runnable() {   
		        	@Override 
		        	public void run() {  
		        		SystemClock.sleep(1500);
		        		handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
		        	}
		        });
		        newThread.start(); //启动线程	
				m_ComAir5.PlayComAirCmd2(0, globe.voice);	
//				if(isPlaying){
//					playImage.setImageResource(R.drawable.circle_pause);
//				}else{
//					playImage.setImageResource(R.drawable.circle_play);
//				}
				isPlaying = !isPlaying;
				break;		
			case R.id.next1Btn:
				
				if (System.currentTimeMillis() - lastClick <= 1500)  
		        {  
		            return;  
		        }  
		        lastClick = System.currentTimeMillis();  
				
				MyDialog = ProgressDialog.show( ContentMainActivity.this,"", "正在发送...");       	
	        	newThread = new Thread(new Runnable() {   
		        	@Override 
		        	public void run() {  
		        		SystemClock.sleep(1500);
		        		handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
		        	}
		        });
		        newThread.start(); //启动线程	
				m_ComAir5.PlayComAirCmd2(2, globe.voice);
//				if(!bTxMode)
//					PlayCmdSound(2);
				break;
	
			case R.id.volaBtn:
				
				if (System.currentTimeMillis() - lastClick <= 1500)  
		        {  
		            return;  
		        }  
		        lastClick = System.currentTimeMillis();  
				
				MyDialog = ProgressDialog.show( ContentMainActivity.this,"", "正在发送...");       	
	        	newThread = new Thread(new Runnable() {   
		        	@Override 
		        	public void run() {  
		        		SystemClock.sleep(1500);
		        		handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
		        	}
		        });
		        newThread.start(); //启动线程	
				m_ComAir5.PlayComAirCmd2(3, globe.voice);	
				break;
				
            case R.id.volpBtn:
				
				if (System.currentTimeMillis() - lastClick <= 1500)  
		        {  
		            return;  
		        }  
		        lastClick = System.currentTimeMillis();  
				
				MyDialog = ProgressDialog.show( ContentMainActivity.this,"", "正在发送...");       	
	        	newThread = new Thread(new Runnable() {   
		        	@Override 
		        	public void run() {  
		        		SystemClock.sleep(1500);
		        		handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
		        	}
		        });
		        newThread.start(); //启动线程	
				m_ComAir5.PlayComAirCmd2(4, globe.voice);	
				break;
				
            case R.id.lockBtn:
				
				if (System.currentTimeMillis() - lastClick <= 1500)  
		        {  
		            return;  
		        }  
		        lastClick = System.currentTimeMillis();  
				
				MyDialog = ProgressDialog.show( ContentMainActivity.this,"", "正在发送...");       	
	        	newThread = new Thread(new Runnable() {   
		        	@Override 
		        	public void run() {  
		        		SystemClock.sleep(1500);
		        		handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
		        	}
		        });
		        newThread.start(); //启动线程	
				m_ComAir5.PlayComAirCmd2(8, globe.voice);	
				break;
				
            case R.id.poBtn:
				
				if (System.currentTimeMillis() - lastClick <= 1500)  
		        {  
		            return;  
		        }  
		        lastClick = System.currentTimeMillis();  
				
				MyDialog = ProgressDialog.show( ContentMainActivity.this,"", "正在发送...");       	
	        	newThread = new Thread(new Runnable() {   
		        	@Override 
		        	public void run() {  
		        		SystemClock.sleep(1500);
		        		handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
		        	}
		        });
		        newThread.start(); //启动线程	
				m_ComAir5.PlayComAirCmd2(5, globe.voice);	
				break;
			default:
				break;
			}
		}

		
	};

	
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// handler接收到消息后就会执行此方�?			
			MyDialog.dismiss();// 关闭ProgressDialog
		}
	};
	protected boolean isTop;

	private BaiduMap mBaiduMap;

	private LocationClient mLocClient;
	
	/*
	 *播放每个目录 */	
    public void PlayCmdSound(int i32Command)
	{
		if(mPlayer != null)
		{
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;			
		}
		mPlayer = MediaPlayer.create(this, getResources().getIdentifier("n" + i32Command, "raw", this.getPackageName()));
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mPlayer.setVolume(2.0f, 2.0f);
		
		mPlayer.start();
		
		mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
			}				
		});
	}
    
    /*
     * "assets/K11/tonghuagushi.xml"
     * 解析故事这个xml文件*/
    
    private List<Person> readXml(int index) {  
        InputStream file = this.getClass().getClassLoader()  
                .getResourceAsStream("assets/K11/ss.txt");  
        
        return JSONUtils.JSONParse(file, index);
//        SAXXmlContentHandler contentHandler = new SAXXmlContentHandler(); 
//        try {  
//            SAXParserFactory factory = SAXParserFactory.newInstance();  
//            SAXParser parser = factory.newSAXParser();  
//  
//            parser.parse(file, contentHandler);
//            file.close();
//            i=j=0;
//            persons=contentHandler.getBooks();
//            for(Person person : persons){  
////            	System.out.println("id:"+person.getId()+"\tname:"+person.getName()+"\tonenumber:"+person.getOnenumber()+"\ttwonumber:"+person.getTwonumber()+"\tmold:"+person.getMold());
//            	one[i]=person.getOnenumber();
////            	System.out.println("yi"+one[i]);
//            	i++;
//            	two[j]=person.getTwonumber();
////            	System.out.println("er"+two[i]);
//            	j++;
//            } 
//            
//            
//
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        }  
//        return contentHandler.getBooks();  		
	}
    
    
   private class MyAdapter extends BaseAdapter{

	   private List<Person> storys;
		private Context context;
		private LayoutInflater inflater;
		public MyAdapter(Context context, List<Person> storys){
			
			this.storys = storys;
			this.context = context;
			inflater = LayoutInflater.from(this.context);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return storys.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return  storys.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (convertView==null) {
				convertView = inflater.inflate(R.layout.item_story, null);
			}
			one[position] = storys.get(position).getOnenumber();
			two[position] = storys.get(position).getTwonumber();
//			ImageView contact_headUrl =ViewHolder.get(convertView, CPResourceUtil.getId(context, "contact_head"));
			TextView contact_name =ViewHolder.get(convertView, R.id.text_story);
			contact_name.setText(storys.get(position).getName());
//			TextView contact_message =ViewHolder.get(convertView, CPResourceUtil.getId(context, "contact_message"));
//			
			
			
			return convertView;
		}

	   
	   
   }
   public void showAlerDialog() {
		
		
		View view = LayoutInflater.from(ContentMainActivity.this).inflate(
				R.layout.dialog_login, null);
		TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
		TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
		TextView content = (TextView) view.findViewById(R.id.content);
		content.setText("确定退出故事机？");
		final Dialog mDeleteDialog = new Dialog(ContentMainActivity.this, R.style.dialog);  
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
				
				
				SharedPreferences mSharedPreferences = getSharedPreferences("logginStatus", Context.MODE_PRIVATE);
				mSharedPreferences.edit().putString("mobile", "").putString("password", "").commit();
				mDeleteDialog.dismiss();
				startActivity(new Intent(ContentMainActivity.this,YQ_LoginActivity.class));
				finish();
			}
		});
	}
   
   

   /** 
    * 双击退出函数 
    */  
   private static Boolean isExit = false;  
     
   private void exitBy2Click() {  
       Timer tExit = null;  
       if (isExit == false) {  
           isExit = true; // 准备退出  
           Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();  
           tExit = new Timer();  
           tExit.schedule(new TimerTask() {  
               @Override  
               public void run() {  
                   isExit = false; // 取消退出  
               }  
           }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务  
     
       } else {  
           finish();  
			globe.x=0;	
			MyAplication appKiller = (MyAplication) getApplication();
			List<Activity> list = appKiller.activities;
			for (Activity activity : list) {
					activity.finish();
			}
       }  
   }  
   private void change(int index) {
//		buttongushi.setEnabled(true);
//		buttonerge.setEnabled(true);
//		buttonyingyu.setEnabled(false);
//		buttonrenzhi.setEnabled(true);
//		buttongushi.setBackgroundColor(Color.parseColor("#00000000"));
//		buttonerge.setBackgroundColor(Color.parseColor("#00000000"));
//		buttonyingyu.setBackgroundColor(Color.parseColor("#b2b2b2"));
//		buttonrenzhi.setBackgroundColor(Color.parseColor("#00000000"));
		
		//simple_expandable_list_item_1是前面留有空格；simple_list_item_1置顶
		persons = ContentMainActivity.this.readXml(index);
		   adapter = new MyAdapter(ContentMainActivity.this,
					 persons);
		listView.setAdapter(adapter);
		audioManager=(AudioManager) getSystemService(Service.AUDIO_SERVICE);
		 listView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					  switch(scrollState){  
				        case OnScrollListener.SCROLL_STATE_IDLE://空闲状态 
				        	scrollFlag = false;
				        	
				        	break;
				        case OnScrollListener.SCROLL_STATE_FLING://滚动状态  
				        	scrollFlag = false;
				        	
//				        	titlebar.getBackground().setAlpha(0);
				            break;  
				        case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动  
				        	scrollFlag = true;
//				        	titlebar.getBackground().setAlpha(150);
				        	//初始化  
//				        	Animation alphaAnimation = new AlphaAnimation(0f, 0.6f);  
				        //	alphaAnimation.setInterpolator(new OvershootInterpolator());
//				        	alphaAnimation.setDuration(1000);     
//				        	alphaAnimation.setFillAfter(true);
//				        	titlebar.startAnimation(alphaAnimation); 
//				        	alphaAnimation.setAnimationListener(new MyAnim());
				        	
//				        	titlebar.getBackground().setAlpha(0);
				            break;  
				            
				        } 
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
				    
					int last = firstVisibleItem + visibleItemCount;
				    if (scrollFlag) {

				    if (last > lastVisibleItemPosition) {
				    //上滑
				    	titlebar.getBackground().setAlpha(150);
				    }

				    if (last < lastVisibleItemPosition) {
				    //下滑
				    	if(firstVisibleItem==0){  
					    	 isTop = true;
					    	 titlebar.getBackground().setAlpha(0);
			              } else {
			            	  isTop = false;
			            	  titlebar.getBackground().setAlpha(180);
						}
				    }

				    if (last == lastVisibleItemPosition) {

				    return;

				    }

				    lastVisibleItemPosition = firstVisibleItem + visibleItemCount;

				    }
				}
			});
		listView.setOnItemClickListener(new OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		    	if(arg2==0){ 
	        		return	;
	        	}else {
	        		arg2--;
				}		        		
	        		
		    	if (System.currentTimeMillis() - lastClick <= 3100)  
		        {  
		            return;  
		        }  
		        lastClick = System.currentTimeMillis();  
		        if(flag){
		        	flag=false;
		        	MyDialog = ProgressDialog.show(ContentMainActivity.this,"", "正在发送...");       	
		        	newThread = new Thread(new Runnable() {   
			        	@Override 
			        	public void run() {  
			        		SystemClock.sleep(3000);
			        		handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
			        	}
			        });
			        newThread.start(); //启动线程	
			        
			        for(i=0,j=0;i<=260;i++,j++)
			        {
			        	if(i==arg2)
			        	m_ComAir5.PlayComAirCmd1(one[i],two[j], globe.voice);
			        }
			        flag=true;
			    }
//			    	m_ComAir5.PlayComAirCmd((24+(arg2+557)/56), globe.voice);
//					m_ComAir5.PlayComAirCmd2((24+(arg2+557)%56), globe.voice);
		    }
		});
	}
	private class MyAnim implements AnimationListener{
		
		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			if (isTop) {
				titlebar.getBackground().setAlpha(0);
			}else {
				titlebar.getBackground().setAlpha(128);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
	
	
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case DrawerView.SEARCH_BTN:
			layout_bottomView.setVisibility(View.VISIBLE);
			layout_topvView.setVisibility(View.GONE);
//			initMaps();
			break;

		default:
			break;
		}
		
		
	}
	
	private boolean isFirstLoc;
	
	public MyLocationListenner myListener = new MyLocationListenner();
	   /**
			 * 定位SDK监听函数
			 */
			public class MyLocationListenner implements BDLocationListener {

				

				@Override
				public void onReceiveLocation(BDLocation location) {
					// map view 销毁后不在处理新接收的位置
					if (location == null || iv_map == null)
						return;
					MyLocationData locData = new MyLocationData.Builder()
							.accuracy(1000)
							// 此处设置开发者获取到的方向信息，顺时针0-360
							.direction(100).latitude(location.getLatitude())
							.longitude(location.getLongitude()).build();

					mBaiduMap.setMyLocationData(locData);
					if (isFirstLoc) {
						isFirstLoc = false;
						LatLng ll = new LatLng(location.getLatitude(),
								location.getLongitude());
						MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
						mBaiduMap.animateMapStatus(u);
					}
				}

				public void onReceivePoi(BDLocation poiLocation) {
				}
			}
			
			public void add(View view){
				onViewcl(view);
			}
			public void delete(View view){
				onViewcl(view);
			}
			public void expand(View view){
				onViewcl(view);
			}
			public void narrow(View view){
				onViewcl(view);
			}
			private void onViewcl(View view) {
				TextView textView = (TextView) view;
				Toast.makeText(getApplicationContext(), textView.getText().toString(), 1).show();
				
			}
	protected void initMaps() {
		// TODO Auto-generated method stub
		iv_map.showZoomControls(false);;// 取消放大缩小

		mBaiduMap = iv_map.getMap();

		mBaiduMap.setMaxAndMinZoomLevel(19, 7);

	
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));

		new MapStatus.Builder().zoom(14);

		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);

		mLocClient.setLocOption(option);
		mLocClient.start();
	}
	
	

	    @Override  
	    protected void onResume() {  
	        super.onResume();  
	        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
	        	iv_map.onResume();  
	        }  
	    @Override  
	    protected void onPause() {  
	        super.onPause();  
	        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
	        	iv_map.onPause();  
	        }  

	
}
