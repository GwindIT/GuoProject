package com.idsmanager.eagleeye.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.FileTileProvider;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Tile;
import com.baidu.mapapi.map.TileOverlay;
import com.baidu.mapapi.map.TileOverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.idsmanager.eagleeye.R;
import com.idsmanager.eagleeye.domain.Thing;
import com.idsmanager.eagleeye.domain.Things;
import com.idsmanager.eagleeye.net.IDsManagerPostRequest;
import com.idsmanager.eagleeye.net.NetService;
import com.idsmanager.eagleeye.net.RequestQueueHelper;
import com.idsmanager.eagleeye.net.response.AllMakersResponse;
import com.idsmanager.eagleeye.utils.DoubleClickExitHelper;
import com.idsmanager.eagleeye.utils.StatLog;
import com.idsmanager.eagleeye.utils.ToastUtil;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaiduMap.OnMapLongClickListener, View.OnClickListener, BaiduMap.OnMarkerClickListener, OnGetPoiSearchResultListener, OnGetSuggestionResultListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static boolean first;
    private static final int MAX_LEVEL = 21;
    private static final int MIN_LEVEL = 3;
    private static final int REQUEST_NEWS = 1;
    private static final int SEARCH = 1;

    private MapView mMapView = null;
    private BaiduMap mMap;
    private TileOverlay tileOverlay;

    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;

    private AutoCompleteTextView keyWorldsView = null;
    private ArrayAdapter<String> sugAdapter = null;
    protected ImageButton locateMe;
    protected ImageButton btSearch;
    protected List<String> suggest;
    public static BDLocation location;

    private LatLng myLatLng = null;
    protected FileTileProvider tileProvider;
    protected FileTileProvider demoTileProvider;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    DoubleClickExitHelper doubleClick = new DoubleClickExitHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocationClient = new LocationClient(getApplicationContext());//声明
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        first = true;
        initView();
        locate();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        locateMe = (ImageButton) findViewById(R.id.bt_back);
        btSearch = (ImageButton) findViewById(R.id.ib_search);
        locateMe.setOnClickListener(this);
        btSearch.setOnClickListener(this);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mMap = mMapView.getMap();
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setBuildingsEnabled(true);

        MapStatusUpdate zoomUpdate = MapStatusUpdateFactory.zoomTo(21);// 默认是12
        mMap.setMapStatus(zoomUpdate);

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        keyWorldsView = (AutoCompleteTextView) findViewById(R.id.search_key);
        sugAdapter = new ArrayAdapter<>(this, R.layout.activity_auto_complete_new_style);
        keyWorldsView.setAdapter(sugAdapter);
        keyWorldsView.setThreshold(1);

        initMarker();

        keyWorldsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String place = (String) parent.getItemAtPosition(position);
                String latitude = place.substring(place.indexOf("latitude:") + 9, place.indexOf(","));
                String longitude = place.substring(place.indexOf("longitude:") + 10);

                String locate = place.substring(0, place.indexOf("---"));
                keyWorldsView.setText(locate);

                myLatLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                MapStatus mapStatus = new MapStatus.Builder().target(myLatLng).build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
                //改变地图状态
                mMap.setMapStatus(mMapStatusUpdate);

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.weizhi);
                MarkerOptions option = new MarkerOptions();
                option.title("place").position(myLatLng).icon(icon).draggable(false);
                mMap.addOverlay(option);
            }
        });

        keyWorldsView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() <= 0) {
                    return;
                }
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(cs.toString()).city("北京"));
            }
        });
    }

    private void initMarker() {
        RequestQueue requestQueue = RequestQueueHelper.getInstance(getApplicationContext());
        IDsManagerPostRequest<AllMakersResponse> request = new IDsManagerPostRequest<>(NetService.FIND_ALL_MARK_URL, AllMakersResponse.class, NetService.add(),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        getSuccess(((AllMakersResponse) response).detail);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getFailed(error.getMessage());
            }
        });

        RetryPolicy retryPolicy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        request.setTag(TAG);
        requestQueue.add(request);
    }

    private void getSuccess(Things things) {
        List<Thing> all = things.markPoints;
        if (all == null) {
            ToastUtil.showToast("该地图未经过任何标记");
            return;
        }
        for (Thing thing : all) {
            BitmapDescriptor icon = null;
            String type = thing.pointType;
            String name = thing.pointName;
            if ("testFengzhibo".equals(name)) {
                break;
            }

            LatLng latLng = new LatLng(thing.latitude, thing.longitude);
            if ("花".equals(type)) {
                icon = BitmapDescriptorFactory.fromResource(R.drawable.flower);
            } else if ("树".equals(type)) {
                icon = BitmapDescriptorFactory.fromResource(R.drawable.tree);
            } else if ("草".equals(type)) {
                icon = BitmapDescriptorFactory.fromResource(R.drawable.grass);
            }
            MarkerOptions option = new MarkerOptions();
            option.title(name).position(latLng).icon(icon).draggable(false);
            mMap.addOverlay(option);
        }
    }

    private void getFailed(String msg) {
        ToastUtil.showToast(msg);
    }

    private void locate() {
        initLocation();
        show();
    }

    /**
     * 设置定位的属性
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    /**
     * 设置显示定位位置的属性
     */
    private void show() {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.location);
        MyLocationConfiguration config = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL,
                true, icon);
        mMap.setMyLocationConfigeration(config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.REQUEST_NEWS && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String type = extras.getString(AddNewsActivity.News_Type);
            String name = extras.getString(AddNewsActivity.NEWS_NAME);
            LatLng latLng = (LatLng) extras.get(AddNewsActivity.News_LatLng);
            addMarkerOption(name, latLng, type);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        MapStatus mapStatus = mMap.getMapStatus();
        float zoom = mapStatus.zoom;
        switch (item.getItemId()) {
            case R.id.nav_add:
                addTileOverlay();
                mMap.showMapPoi(true);
                MapStatusUpdate zoomUpdate = MapStatusUpdateFactory.zoomTo(21);// 默认是12
                mMap.setMapStatus(zoomUpdate);
                item.setChecked(true);
                initMarker();
                break;
            case R.id.nav_hide:
                startActivity(new Intent(this, MainActivity.class));
                MainActivity.this.overridePendingTransition(0, 0);
                if (tileOverlay != null) {
                    tileOverlay.removeTileOverlay();
                }
                mMap.showMapPoi(true);
                MapStatusUpdate zoomUpdate2 = MapStatusUpdateFactory.zoomTo(zoom);// 默认是12
                mMap.setMapStatus(zoomUpdate2);
                item.setChecked(true);
                break;
            case R.id.nav_demo:
                mMap.showMapPoi(true);
                addDemoTileOverlay();
                MapStatusUpdate zoomUpdate3 = MapStatusUpdateFactory.zoomTo(21);// 默认是12
                mMap.setMapStatus(zoomUpdate3);
                item.setChecked(true);
                break;
            case R.id.nav_set_ip:
                LayoutInflater inflate = getLayoutInflater();
                View layout = inflate.inflate(R.layout.dialog_my, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(layout);
                final Dialog dialog = builder.show();
                final EditText editText = (EditText) layout.findViewById(R.id.et_add_ip);
                Button btCancel = (Button) layout.findViewById(R.id.bt_cancel);
                btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button btConfirm = (Button) layout.findViewById(R.id.bt_confirm);
                btConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ip = editText.getText().toString().trim();
                        if (!TextUtils.isEmpty(ip)) {
                            NetService.HTTP_URL = ip;
                            ToastUtil.showToast("修改成功");
                            dialog.dismiss();
                        } else {
                            ToastUtil.showToast("请输入IP");
                        }
                    }
                });
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 添加本地瓦片图
     */
    private void addTileOverlay() {

        if (tileOverlay != null && mMap != null) {
            tileOverlay.removeTileOverlay();
        }
        tileProvider = new FileTileProvider() {
            @Override
            public Tile getTile(int x, int y, int z) {
                String fileDir = "LocalTileImage/" + z + "/" + z + "_" + x + "_" + y + ".jpg";
                String latLng = "x:" + x + ",y" + y + ",z" + z;
                Log.i("图片坐标:", latLng);
                Bitmap bm = getFromAssets(fileDir);
                if (bm == null) {
                    bm = getFromAssets("LocalTileImage/" + 20 + "/" + 1 + ".jpg");
                    if (bm == null) {
                        return null;
                    }
                }
                Tile offlineTile = new Tile(bm.getWidth(), bm.getHeight(), toRawData(bm));
                bm.recycle();
                return offlineTile;
            }

            @Override
            public int getMaxDisLevel() {
                return MAX_LEVEL;
            }

            @Override
            public int getMinDisLevel() {
                return MIN_LEVEL;
            }
        };
        TileOverlayOptions options = new TileOverlayOptions();
        LatLng northeast = new LatLng(80, 180);
        LatLng southwest = new LatLng(-80, -180);
        options.tileProvider(tileProvider).setPositionFromBounds(new LatLngBounds.Builder().include(northeast).include(southwest).build());
        tileOverlay = mMap.addTileLayer(options);
    }

    private void addDemoTileOverlay() {
        if (tileOverlay != null && mMap != null) {
            tileOverlay.removeTileOverlay();
        }
        demoTileProvider = new FileTileProvider() {
            @Override
            public Tile getTile(int x, int y, int z) {
                String fileDir = "demo/" + z + "/" + z + "_" + x + "_" + y + ".png";
                String latLng = "x:" + x + ",y" + y + ",z" + z;
                StatLog.printLog(TAG, latLng);
                //x:404762,y150585,z21
                Bitmap bm = getFromAssets(fileDir);
                if (bm == null) {
                    return null;
                }
                Tile offlineTile = new Tile(bm.getWidth(), bm.getHeight(), toRawData(bm));
                bm.recycle();
                return offlineTile;
            }

            @Override
            public int getMaxDisLevel() {
                return MAX_LEVEL;
            }

            @Override
            public int getMinDisLevel() {
                return MIN_LEVEL;
            }
        };

        TileOverlayOptions options = new TileOverlayOptions();
        LatLng northeast = new LatLng(80, 180);
        LatLng southwest = new LatLng(-80, -180);
        options.tileProvider(demoTileProvider).setPositionFromBounds(new LatLngBounds.Builder().include(northeast).include(southwest).build());
        tileOverlay = mMap.addTileLayer(options);
    }

    /**
     * get assets
     */
    private Bitmap getFromAssets(String fileName) {
        AssetManager am = this.getAssets();
        InputStream is = null;
        Bitmap bm;
        try {
            is = am.open(fileName);
            bm = BitmapFactory.decodeStream(is);
            return bm;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * return bytes[]
     *
     * @param bitmap
     * @return
     */
    byte[] toRawData(Bitmap bitmap) {
        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getWidth()
                * bitmap.getHeight() * 4);
        bitmap.copyPixelsToBuffer(buffer);
        byte[] data = buffer.array();
        buffer.clear();
        return data;
    }

    /**
     * 长按的监听
     *
     * @param latLng
     */
    @Override
    public void onMapLongClick(final LatLng latLng) {
        Intent intent = new Intent(this, AddNewsActivity.class);
        intent.putExtra(AddNewsActivity.LOCATION_KEY, latLng);
        Log.i("点击坐标:", latLng.latitude + "," + latLng.longitude);
        startActivityForResult(intent, REQUEST_NEWS);

    }

    private void addMarkerOption(String name, LatLng latLng, String type) {
        BitmapDescriptor icon = null;
        if ("花".equals(type)) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.flower);
        } else if ("树".equals(type)) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.tree);
        } else if ("草".equals(type)) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.grass);
        }
        MarkerOptions option = new MarkerOptions();
        option.title(name).position(latLng).icon(icon).draggable(false);
        mMap.addOverlay(option);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if ("place".equals(marker.getTitle())) {
            return true;
        }
        ShowNewsActivity.open(MainActivity.this, marker.getPosition(), marker.getTitle(), 0);
        return true;
    }

    @Override
    public void onClick(View v) {
        MapStatus nowMapStatus = mMap.getMapStatus();
        switch (v.getId()) {
            case R.id.bt_back:
                if (location != null) {
                    float zoom = nowMapStatus.zoom;
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    MapStatus mapStatus = new MapStatus.Builder().target(latLng).zoom(zoom).build();
                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
                    //改变地图状态
                    mMap.setMapStatus(mMapStatusUpdate);
                } else {
                    ToastUtil.showToast(getString(R.string.wait_locate));
                }
                break;
            case R.id.ib_search:
                double latitude = 0;
                double longitude = 0;
                try {
                    latitude = myLatLng.latitude;
                    longitude = myLatLng.longitude;
                } catch (Exception e) {
                    ToastUtil.showToast(getString(R.string.put_in));
                    break;
                }
                if (latitude == 0 || longitude == 0) {
                    ToastUtil.showToast(getString(R.string.put_in));
                    break;
                }
                LatLng latLng = new LatLng(latitude, longitude);
                MapStatus mapStatus = new MapStatus.Builder().target(latLng).build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
                mMap.setMapStatus(mMapStatusUpdate);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.weizhi);
                MarkerOptions option = new MarkerOptions();
                option.title("place").position(myLatLng).icon(icon).draggable(false);
                mMap.addOverlay(option);
                break;
            default:
                break;
        }
    }

    /**
     * Poi搜索的方法
     */
    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(MainActivity.this, "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mMap.clear();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(MainActivity.this, strInfo, Toast.LENGTH_LONG).show();
        }
    }

    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        suggest = new ArrayList<>();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key + "---" + info.pt);
            }
        }
        sugAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.activity_auto_complete_new_style, suggest);
        keyWorldsView.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();
    }

    /**
     * 定位位置的监听类
     */
    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //第一次进入时显示为当前位置
            if (first) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus mapStatus = new MapStatus.Builder().target(latLng).zoom(16).build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
                //改变地图状态
                mMap.setMapStatus(mMapStatusUpdate);
                first = false;
            }
            MainActivity.location = location;
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            MyLocationData data = new MyLocationData.Builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .direction(location.getDirection())
                    .build();
            mMap.setMyLocationEnabled(true);
            mMap.setMyLocationData(data);// 显示定位数据
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.start();
    }

    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mMapView.onPause();
        mLocationClient.stop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return doubleClick.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
