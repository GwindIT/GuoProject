package com.weiyoung.yourlifecircle;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.weiyoung.service.YLC_SAVEUSERSERVICE;
import com.weiyoung.utils.YLC_HttpClient;
import com.weiyoung.utils.YLC_SaveUserInfo;
import com.weiyoung.variable.LoginInfo;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class YLC_Main extends Activity implements OnTouchListener {
	private Tencent mtencent;
	private static final String AppID = "101027899";
	private static final String Scope = "all";
	private TextView nickname_textview;
	private ImageView imageView;
	private ImageView BDMapService_imageview;
	private ImageView BDMAPLocation_imageview;
	private ImageView BDSet_imageview;
	// 地图处理部分
	private MapView mapView = null;
	private MapController mapController = null;
	private MKMapViewListener mkMapViewListener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
        .detectDiskReads()  
        .detectDiskWrites()  
        .detectNetwork()   // or .detectAll() for all detectable problems  
        .penaltyLog()  
        .build());  
    	StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
        .detectLeakedSqlLiteObjects()  
        .detectLeakedClosableObjects()  
        .penaltyLog()  
        .penaltyDeath()  
        .build());
		YLC_APPLICATION ylc_Application = (YLC_APPLICATION) this
				.getApplication();
		if (ylc_Application.bMapManager == null) {
			ylc_Application.bMapManager = new BMapManager(this);
			ylc_Application.bMapManager.init(YLC_APPLICATION.STR_KEY,
					new YLC_APPLICATION.MyMKGeneralListener());
			ylc_Application.bMapManager.start();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ylc__main);
		initMap();
		mtencent = Tencent.createInstance(AppID, this.getApplicationContext());
		findView();
	}

	public void findView() {
		nickname_textview=(TextView) findViewById(R.id.nickname_textview);
		imageView = (ImageView) findViewById(R.id.imageView);
		BDMapService_imageview = (ImageView) findViewById(R.id.BDMapService_imageview);
		BDMAPLocation_imageview = (ImageView) findViewById(R.id.BDMAPLocation_imageview);
		BDSet_imageview = (ImageView) findViewById(R.id.BDSet_imageview);
		BDMapService_imageview.setOnTouchListener(this);
		BDMAPLocation_imageview.setOnTouchListener(this);
		BDSet_imageview.setOnTouchListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.destroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		mapView.onRestoreInstanceState(savedInstanceState);
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	public void initMap() {
		mapView = (MapView) findViewById(R.id.bmapView);
		mapController = mapView.getController();
		mapController.enableClick(true);
		mapController.setZoom(12);
		/**
		 * 将地图移动至指定点
		 * 使用百度经纬度坐标，可以通过http://api.map.baidu.com/lbsapi/getpoint/index
		 * .html查询地理坐标 如果需要在百度地图上显示使用其他坐标系统的位置，请发邮件至mapapi@baidu.com申请坐标转换接口
		 */
		GeoPoint p;
		double cLat = 39.945;
		double cLon = 116.404;
		Intent intent = getIntent();
		if (intent.hasExtra("x") && intent.hasExtra("y")) {
			// 当用intent参数时，设置中心点为指定点
			Bundle b = intent.getExtras();
			p = new GeoPoint(b.getInt("y"), b.getInt("x"));
		} else {
			// 设置中心点为天安门
			p = new GeoPoint((int) (cLat * 1E6), (int) (cLon * 1E6));
		}

		mapController.setCenter(p);

		/**
		 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
		mkMapViewListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
				/**
				 * 在此处理地图移动完成回调 缩放，平移等操作完成后，此回调被触发
				 */
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * 在此处理底图poi点击事件 显示底图poi名称并移动至该点 设置过：
				 * mMapController.enableClick(true); 时，此回调才能被触发
				 * 
				 */
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(YLC_Main.this, title, Toast.LENGTH_SHORT)
							.show();
					mapController.animateTo(mapPoiInfo.geoPt);
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				/**
				 * 当调用过 mMapView.getCurrentMap()后，此回调会被触发 可在此保存截图至存储设备
				 */
			}

			@Override
			public void onMapAnimationFinish() {
				/**
				 * 地图完成带动画的操作（如: animationTo()）后，此回调被触发
				 */
			}

			/**
			 * 在此处理地图载完成事件
			 */
			@Override
			public void onMapLoadFinish() {
				Toast.makeText(YLC_Main.this, "地图加载完成", Toast.LENGTH_SHORT)
						.show();

			}
		};
		mapView.regMapViewListener(YLC_APPLICATION.getInstance().bMapManager,
				mkMapViewListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ylc__main, menu);
		return true;
	}

	public void userLogin(View view) {
		mtencent.login(YLC_Main.this, Scope, new BaseUiListener());// 登录
	}

	private class BaseUiListener implements IUiListener {
		@Override
		public void onCancel() {// 取消登录
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(Object json) {
			// TODO Auto-generated method stub
			JSONObject jsonObject = (JSONObject) json;
			YLC_HttpClient ylc_HttpClient = new YLC_HttpClient();
			Uri uri = ylc_HttpClient.getQQuserInfo(jsonObject);
			LoginInfo.LOGIN_STATE = true; // 登录成功设置登录状态为true;
			nickname_textview.setText(LoginInfo.nickname);
			try {
				Bitmap bitmap = BitmapFactory
						.decodeStream(getImageViewInputStream(uri.toString()));
				imageView.setImageBitmap(bitmap);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//启动service，向服务器传递数据
			Thread saveUserThread=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					YLC_SaveUserInfo saveUserInfo=new YLC_SaveUserInfo(YLC_Main.this);
					saveUserInfo.saveUser();
				}
			});
			saveUserThread.start();
		}

		@Override
		public void onError(UiError ui) {
			// TODO Auto-generated method stub
			Toast.makeText(YLC_Main.this, "登录出错，请确认你的网络连接" + ui,
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 2 * 从网络中获取图片，以流的形式返回 3 * @return 4
	 */
	public static InputStream getImageViewInputStream(String URL_PATH)
			throws IOException {
		InputStream inputStream = null;
		URL url = new URL(URL_PATH); // 服务器地址
		if (url != null) {
			// 打开连接
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setConnectTimeout(3000);// 设置网络连接超时的时间为3秒
			httpURLConnection.setRequestMethod("GET"); // 设置请求方法为GET
			httpURLConnection.setDoInput(true); // 打开输入流
			int responseCode = httpURLConnection.getResponseCode(); // 获取服务器响应值
			if (responseCode == HttpURLConnection.HTTP_OK) { // 正常连接
				inputStream = httpURLConnection.getInputStream(); // 获取输入流
			}
		}
		return inputStream;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		Color color = new Color();
		switch (v.getId()) {
		case R.id.BDMapService_imageview:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				BDMapService_imageview
						.setBackgroundColor(color.rgb(14, 49, 55));
				BDMapService_imageview.setImageDrawable(getResources()
						.getDrawable(R.drawable.ylc_service_focus));
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				BDMapService_imageview.setBackgroundColor(color.TRANSPARENT);
				BDMapService_imageview.setImageDrawable(getResources()
						.getDrawable(R.drawable.ylc_service));
				/*
				 * Intent intent=new Intent(YLC_Main.this,
				 * LocationOverlayDemoTest.class);
				 * YLC_Main.this.startActivity(intent);
				 */
				showYLCService();
			}
			break;

		case R.id.BDMAPLocation_imageview:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				BDMAPLocation_imageview.setBackgroundColor(color
						.rgb(14, 49, 55));
				BDMAPLocation_imageview.setImageDrawable(getResources()
						.getDrawable(R.drawable.ylc_location_focus));
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				BDMAPLocation_imageview.setBackgroundColor(color.TRANSPARENT);
				BDMAPLocation_imageview.setImageDrawable(getResources()
						.getDrawable(R.drawable.ylc_location));
				Intent intent = new Intent(YLC_Main.this,
						YLC_SelfLocation.class);
				YLC_Main.this.startActivity(intent);
			}
			break;
		case R.id.BDSet_imageview:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				BDSet_imageview.setBackgroundColor(color.rgb(14, 49, 55));
				BDSet_imageview.setImageDrawable(getResources().getDrawable(
						R.drawable.ylc_set_hover));
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				BDSet_imageview.setBackgroundColor(color.TRANSPARENT);
				BDSet_imageview.setImageDrawable(getResources().getDrawable(
						R.drawable.ylc_set));
				Intent intent = new Intent(YLC_Main.this, YLC_Set.class);
				YLC_Main.this.startActivity(intent);
			}
			break;
		}
		return false;
	}

	/*
	 * 菜单按钮的各个选项
	 */
	public void showYLCService() {
		String[] items = getResources().getStringArray(
				R.array.ylc_string_service);
		AlertDialog dialogService = new AlertDialog.Builder(this)
				.setTitle("功能列表").setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = null;
						switch (which) {
						case 0:
							intent = new Intent(YLC_Main.this,
									YLC_POI_Search.class);
							YLC_Main.this.startActivity(intent);
							break;

						case 1:
							intent = new Intent(YLC_Main.this,
									YLC_RoutePlan.class);
							YLC_Main.this.startActivity(intent);
							break;
						case 2:
							intent = new Intent(YLC_Main.this,
									YLC_GeoCoder.class);
							YLC_Main.this.startActivity(intent);
							break;
						case 3:
							intent = new Intent(YLC_Main.this,
									YLC_BusLine.class);
							YLC_Main.this.startActivity(intent);
							break;
						case 4:
							intent = new Intent(YLC_Main.this,
									YLC_YourLifeCircle.class);
							YLC_Main.this.startActivity(intent);
							break;
						}
					}
				}).create();
		WindowManager wm = getWindowManager();
		Display d = wm.getDefaultDisplay();
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.x = -350;
		layoutParams.y = 120;
		layoutParams.gravity = Gravity.BOTTOM;
		layoutParams.width = (int) (d.getWidth() * 0.48);
		layoutParams.height = (int) (d.getHeight() * 0.50);
		layoutParams.alpha = 0.6f;// 设置对话框的透明度
		dialogService.setCanceledOnTouchOutside(true);
		dialogService.show();
		Window window = dialogService.getWindow();

		window.setAttributes(layoutParams);
		// window.setGravity(Gravity.BOTTOM);//设置弹出对话框位置

	}
	public void showUserInfo(View view){
		if(LoginInfo.userinfo_state){
			List<String> listItemTitle=null;
			List<String> listItemContent=null;
			if(LoginInfo.user_type==1){
				listItemTitle=getQQuserInfoTitle();
				listItemContent=getQQuserInfoContent();
				AlertDialog qquserDialog=new AlertDialog.Builder(this)
											.setTitle("QQ用户")
											.setAdapter(new UserInfoAdapter(listItemTitle,listItemContent), null)
											.create();
				qquserDialog.show();
											
			}else if(LoginInfo.user_type==2){
				listItemTitle=getQQWBuserInfoTitle();
				listItemContent=getQQWBuserInfoContent();
				AlertDialog qqwbuserDialog=new AlertDialog.Builder(this)
				.setTitle("QQ微博用户")
				.setAdapter(new UserInfoAdapter(listItemTitle,listItemContent), null)
				.create();
				qqwbuserDialog.show();
			}
		}else{
			Toast.makeText(YLC_Main.this, "数据正在更新，请稍后...", Toast.LENGTH_LONG).show();
		}
	}
	//从sharedpreference获取数据
	public List<String> getQQuserInfoTitle(){
		List<String> listItemTitle=new ArrayList<String>();
		listItemTitle.add("QQ昵称：");
		listItemTitle.add("性别：");
		listItemTitle.add("是否是黄钻:");
		listItemTitle.add("是否是VIP：");
		listItemTitle.add("黄钻等级：");
		listItemTitle.add("VIP等级：");
		listItemTitle.add("是否是年黄钻:");
		return listItemTitle;
	}
	public List<String> getQQuserInfoContent(){
		List<String> listItemContent=new ArrayList<String>();
		SharedPreferences share=this.getSharedPreferences("qquser", Context.MODE_PRIVATE);
		listItemContent.add(share.getString("nickname", ""));
		listItemContent.add(share.getString("gender", ""));
		if(share.getInt("is_yellow_vip", 10)==0){
			listItemContent.add("否");
		}else{
			listItemContent.add("是");
		}
		if(share.getInt("vip", 10)==0){
			listItemContent.add("否");
		}else{
			listItemContent.add("是");
		}
		if(share.getInt("yellow_vip_level", 10)==0){
			listItemContent.add("否");
		}else{
			listItemContent.add("是");
		}
		if(share.getInt("level", 10)==0){
			listItemContent.add("否");
		}else{
			listItemContent.add("是");
		}
		if(share.getInt("is_yellow_year_vip", 10)==0){
			listItemContent.add("否");
		}else{
			listItemContent.add("是");
		}
		return listItemContent;
	}
	public List<String> getQQWBuserInfoTitle(){
		List<String> listItemTitle=new ArrayList<String>();
		listItemTitle.add("QQ微博昵称：");
		listItemTitle.add("QQ昵称：");
		listItemTitle.add("微博账户：");
		listItemTitle.add("性别：");
		listItemTitle.add("是否是黄钻:");
		listItemTitle.add("是否是VIP：");
		listItemTitle.add("黄钻等级：");
		listItemTitle.add("VIP等级：");
		listItemTitle.add("是否是年黄钻:");
		listItemTitle.add("出生日期：");
		listItemTitle.add("所在地：");
		return listItemTitle;
	}
	public List<String> getQQWBuserInfoContent(){
		List<String> listItemContent=new ArrayList<String>();
		SharedPreferences share=this.getSharedPreferences("qqwbuser", Context.MODE_PRIVATE);
		listItemContent.add(share.getString("nick", ""));
		listItemContent.add(share.getString("nickname", ""));
		listItemContent.add(share.getString("wbcount", ""));
		listItemContent.add(share.getString("gender", ""));
		if(share.getInt("is_yellow_vip", 10)==0){
			listItemContent.add("否");
		}else{
			listItemContent.add("是");
		}
		if(share.getInt("vip", 10)==0){
			listItemContent.add("否");
		}else{
			listItemContent.add("是");
		}
		if(share.getInt("yellow_vip_level", 10)==0){
			listItemContent.add("否");
		}else{
			listItemContent.add("是");
		}
		if(share.getInt("level", 10)==0){
			listItemContent.add("否");
		}else{
			listItemContent.add("是");
		}
		if(share.getInt("is_yellow_year_vip", 10)==0){
			listItemContent.add("否");
		}else{
			listItemContent.add("是");
		}
		listItemContent.add(share.getString("birthday", ""));
		listItemContent.add(share.getString("location", ""));
		return listItemContent;
	}
	class UserInfoAdapter extends BaseAdapter{
		LayoutInflater layoutInflater;
		List<String> listItemTitle;
		List<String> listItemContent;
		public UserInfoAdapter(List<String> listItemTitle,List<String> listItemContent){
			this.listItemTitle=listItemTitle;
			this.listItemContent=listItemContent;
			layoutInflater=(LayoutInflater) YLC_Main.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listItemTitle.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View contentview, ViewGroup parent) {
			// TODO Auto-generated method stub
			UserInfoViewHolder viewholder;
			if(contentview==null){
				viewholder=new UserInfoViewHolder();
				contentview=layoutInflater.inflate(R.layout.activity_ylc_userinfo, parent, false);
				viewholder.tv1=(TextView) contentview.findViewById(R.id.textView_item);
				viewholder.tv2=(TextView) contentview.findViewById(R.id.textView_content);
				contentview.setTag(viewholder);
			}else{
				viewholder=(UserInfoViewHolder) contentview.getTag();
			}
			viewholder.tv1.setText(listItemTitle.get(position));
			viewholder.tv2.setText(listItemContent.get(position));
			return contentview;
		}
		
	}
	static class UserInfoViewHolder{
		TextView tv1;
		TextView tv2;
	}
}
