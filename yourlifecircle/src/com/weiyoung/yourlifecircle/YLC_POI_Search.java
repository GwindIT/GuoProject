package com.weiyoung.yourlifecircle;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionInfo;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.weiyoung.yourlifecircle.YLC_APPLICATION.MyMKGeneralListener;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

public class YLC_POI_Search extends Activity {
	private MapView myMapView = null;
	private MKSearch mySearch = null;// 搜索模块，也可以去掉地图独立存在
	/*
	 * 搜索关键字输入窗口
	 */
	private AutoCompleteTextView keyWorldView = null;
	private ArrayAdapter<String> sugAdapter = null;
	private int load_Index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		YLC_APPLICATION app = (YLC_APPLICATION) this.getApplication();
		if (app.bMapManager == null) {
			app.bMapManager = new BMapManager(this);
			app.bMapManager.init(YLC_APPLICATION.STR_KEY,
					new YLC_APPLICATION.MyMKGeneralListener());
		}
		setContentView(R.layout.activity_ylc__poi__search);
		myMapView = (MapView) findViewById(R.id.bmapView_poi);
		myMapView.getController().enableClick(true);
		myMapView.getController().setZoom(12);
		// 初始化搜索模块，注册监听事件
		mySearch = new MKSearch();
		mySearch.init(app.bMapManager, new MKSearchListener() {

			// 在此处理详情页结果
			@Override
			public void onGetPoiDetailSearchResult(int type, int error) {
				if (error != 0) {
					Toast.makeText(YLC_POI_Search.this, "抱歉，未找到结果",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(YLC_POI_Search.this, "成功，查看详情页面",
							Toast.LENGTH_SHORT).show();
				}
			}

			/**
			 * 在此处理poi搜索结果
			 */
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				// 错误号可参考MKEvent中的定义
				if (error != 0 || res == null) {
					Toast.makeText(YLC_POI_Search.this, "抱歉，未找到结果",
							Toast.LENGTH_LONG).show();
					return;
				}
				// 将地图移动到第一个POI中心点
				if (res.getCurrentNumPois() > 0) {
					// 将poi结果显示到地图上
					YLC_PoiOverlay poiOverlay = new YLC_PoiOverlay(
							YLC_POI_Search.this, myMapView, mySearch);
					poiOverlay.setData(res.getAllPoi());
					myMapView.getOverlays().clear();
					myMapView.getOverlays().add(poiOverlay);
					myMapView.refresh();
					// 当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
					for (MKPoiInfo info : res.getAllPoi()) {
						if (info.pt != null) {
							myMapView.getController().animateTo(info.pt);
							break;
						}
					}
				} else if (res.getCityListNum() > 0) {
					// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
					String strInfo = "在";
					for (int i = 0; i < res.getCityListNum(); i++) {
						strInfo += res.getCityListInfo(i).city;
						strInfo += ",";
					}
					strInfo += "找到结果";
					Toast.makeText(YLC_POI_Search.this, strInfo,
							Toast.LENGTH_LONG).show();
				}
			}

			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
			}

			public void onGetAddrResult(MKAddrInfo res, int error) {
			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}

			/**
			 * 更新建议列表
			 */
			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
				if (res == null || res.getAllSuggestions() == null) {
					return;
				}
				sugAdapter.clear();
				for (MKSuggestionInfo info : res.getAllSuggestions()) {
					if (info.key != null)
						sugAdapter.add(info.key);
				}
				sugAdapter.notifyDataSetChanged();

			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
				// TODO Auto-generated method stub

			}
		});
		keyWorldView = (AutoCompleteTextView) findViewById(R.id.searchkey);
		sugAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line);
		keyWorldView.setAdapter(sugAdapter);

		/**
		 * 当输入关键字变化时，动态更新建议列表
		 */
		keyWorldView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				 if ( cs.length() <=0 ){
					 return ;
				 }
				 String city =  ((EditText)findViewById(R.id.city)).getText().toString();
				 /**
				  * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				  */
                 mySearch.suggestionSearch(cs.toString(), city);				
			}
		});
	}
	 @Override
	    protected void onPause() {
		 myMapView.onPause();
	        super.onPause();
	    }
	    
	    @Override
	    protected void onResume() {
	    	myMapView.onResume();
	        super.onResume();
	    }
	    
	    @Override
	    protected void onDestroy(){
	    	myMapView.destroy();
	    	mySearch.destory();
	    	super.onDestroy();
	    }
	    
	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	    	super.onSaveInstanceState(outState);
	    	myMapView.onSaveInstanceState(outState);
	    	
	    }
	    
	    @Override
	    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    	super.onRestoreInstanceState(savedInstanceState);
	    	myMapView.onRestoreInstanceState(savedInstanceState);
	    }
	    
	    private void initMapView() {
	    	myMapView.setLongClickable(true);
	    	myMapView.getController().setZoom(14);
	    	myMapView.getController().enableClick(true);
	    	myMapView.setBuiltInZoomControls(true);
	    }
	    /**
	     * 影响搜索按钮点击事件
	     * @param v
	     */
	    public void searchButtonProcess(View v) {
	          EditText editCity = (EditText)findViewById(R.id.city);
	          EditText editSearchKey = (EditText)findViewById(R.id.searchkey);
	          mySearch.poiSearchInCity(editCity.getText().toString(), 
	                  editSearchKey.getText().toString());
	    }
	   public void goToNextPage(View v) {
	        //搜索下一组poi
	        int flag = mySearch.goToPoiPage(++load_Index);
	        if (flag != 0) {
	            Toast.makeText(YLC_POI_Search.this, "先搜索开始，然后再搜索下一组数据", Toast.LENGTH_SHORT).show();
	        }
	    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ylc__poi__search, menu);
		return true;
	}
}
