package com.example.travel01;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

public class MapLocation extends AppCompatActivity implements View.OnClickListener {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private Context context;

    //定位相关
    private double mLatitude;
    private double mLongtitude;

    //方向传感器
    private MapListener mapListener;
    private float mCurrentX;
    //自定义图标
    private BitmapDescriptor mIconLocation;
    private LocationClient mLocationClient;
    public BDAbstractLocationListener myListener;
    private LatLng mLastLocationData;
    private boolean isFirstin = true;

    // 路线规划相关
    private RoutePlanSearch mSearch = null;

    Button drive_bt;
    Button bus_bt;
    Button walk_bt;
    Button bike_bt;
    Button return_bt;

    EditText editSt;
    EditText editEn;
    EditText editSt_city;
    EditText editEn_city;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.map_lay);
        SDKInitializer.setCoordType(CoordType.BD09LL);
        this.context = this;
        mMapView = findViewById(R.id.bmapView);
        //获取地图控件引用
        mBaiduMap = mMapView.getMap();
        return_bt=findViewById(R.id.return_bt);
        initMyLocation();
        initPoutePlan();
        button();


        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.biaoji);
                OverlayOptions options = new MarkerOptions().position(latLng).icon(descriptor);
                mBaiduMap.addOverlay(options);



            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        });

        return_bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                if (android.os.Build.VERSION.SDK_INT >= 27) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                }
            }
        });



//        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                Log.d("点击地图", "onMarkerClick: "+marker.getPosition().toString());
//                return false;
//            }
//        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
       mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        //停止方向传感器
        mapListener.stop();

    }

    protected void onStart() {
        super.onStart();
        //开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
            mLocationClient.start();
        //开启方向传感器
        mapListener.start();
    }

    @Override
    public void onClick(View v) {
        SDKInitializer.initialize(getApplicationContext());
        switch (v.getId()) {
            case R.id.but_Loc: {
                centerToMyLocation(mLatitude, mLongtitude);
                break;
            }

//            case R.id.return_bt: {
//               finish();
//                if (android.os.Build.VERSION.SDK_INT >= 27) {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
//                }
//                break;
//            }
//            case R.id.drive_bt: {
//                SDKInitializer.initialize(getApplicationContext());
//                // 设置起、终点信息
//                if((!editSt.getText().toString().equals("") )&& (!editEn.getText().toString().equals("")) ){
//                    PlanNode stNode = PlanNode.withCityNameAndPlaceName("温州", editSt.getText().toString());
//                    PlanNode enNode = PlanNode.withCityNameAndPlaceName("温州", editEn.getText().toString());
//                    mSearch.drivingSearch((new DrivingRoutePlanOption())
//                            .from(stNode)
//                            .to(enNode));
//                }
//
//                break;
//            }
//
//            case R.id.bus_bt: {
//                SDKInitializer.initialize(getApplicationContext());
//                // 设置起、终点信息
//                if((!editSt.getText().toString().equals("") )&& (!editEn.getText().toString().equals("")) ){
//                    PlanNode stNode = PlanNode.withCityNameAndPlaceName("温州", editSt.getText().toString());
//                    PlanNode enNode = PlanNode.withCityNameAndPlaceName("温州", editEn.getText().toString());
//                    mSearch.transitSearch((new TransitRoutePlanOption())
//                            .from(stNode)
//                            .to(enNode));
//                }
//
//                break;
//            }


            case R.id.but_RoutrPlan: {
                SDKInitializer.initialize(getApplicationContext());
                // 设置起、终点信息
                if((!editSt.getText().toString().equals("") )&& (!editEn.getText().toString().equals("")) ){
                    PlanNode stNode = PlanNode.withCityNameAndPlaceName(editSt_city.getText().toString(), editSt.getText().toString());
                    PlanNode enNode = PlanNode.withCityNameAndPlaceName(editEn_city.getText().toString(), editEn.getText().toString());
                    mSearch.walkingSearch((new WalkingRoutePlanOption())
                            .from(stNode)
                            .to(enNode));
                }

                break;
            }

//
//            case R.id.bike_bt: {
//                SDKInitializer.initialize(getApplicationContext());
//                // 设置起、终点信息
//                if((!editSt.getText().toString().equals("") )&& (!editEn.getText().toString().equals("")) ){
//                    PlanNode stNode = PlanNode.withCityNameAndPlaceName("温州", editSt.getText().toString());
//                    PlanNode enNode = PlanNode.withCityNameAndPlaceName("温州", editEn.getText().toString());
//                    mSearch.bikingSearch((new BikingRoutePlanOption())
//                            .from(stNode)
//                            .to(enNode));
//                }
//
//                break;
//            }





                default:
        }
    }


    //按钮响应
    private void button() {
        //按钮
        Button mbut_Loc =  findViewById(R.id.but_Loc);
        Button mbut_RoutrPlan = findViewById(R.id.but_RoutrPlan);

//        drive_bt=findViewById(R.id.drive_bt);
//        bus_bt=findViewById(R.id.bus_bt);
//        walk_bt=findViewById(R.id.walk_bt);
//        bike_bt=findViewById(R.id.bike_bt);

        //输入框
        editSt=findViewById(R.id.begin_et);
        editEn=findViewById(R.id.end_et);
        editSt_city=findViewById(R.id.begincity_et);
        editEn_city=findViewById(R.id.endcity_et);

        //按钮处理
        mbut_Loc.setOnClickListener(this);
        mbut_RoutrPlan.setOnClickListener(this);
//        drive_bt.setOnClickListener(this);
//        bus_bt.setOnClickListener(this);
//        walk_bt.setOnClickListener(this);
//        bike_bt.setOnClickListener(this);

//        mbut_RoutrPlan.setOnClickListener(this);
//        return_bt.setOnClickListener(this);
    }

    //定位
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentX).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            //设置自定义图标
            MyLocationConfiguration config = new
                    MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, mIconLocation);
            mBaiduMap.setMyLocationConfiguration(config);
            //更新经纬度
            mLatitude = location.getLatitude();
            mLongtitude = location.getLongitude();
            //设置起点
            mLastLocationData = new LatLng(mLatitude, mLongtitude);
            if (isFirstin) {
                centerToMyLocation(location.getLatitude(), location.getLongitude());

                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    // GPS定位结果
                    Toast.makeText(context, "定位:"+location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // 网络定位结果
                    Toast.makeText(context, "定位:"+location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    // 离线定位结果
                    Toast.makeText(context, "定位:"+location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(context, "定位:服务器错误", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(context, "定位:网络错误", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(context, "定位:手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
                }
                isFirstin = false;
            }
        }
    }


    //初始化定位
    private void initMyLocation() {
        //缩放地图
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
        //开启定位
        mBaiduMap.setMyLocationEnabled(true);
        //声明LocationClient类
        mLocationClient = new LocationClient(this);
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);//设置是否需要地址信息
        option.setScanSpan(1000);
        //设置locationClientOption
        mLocationClient.setLocOption(option);
        myListener = new MyLocationListener();
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        //初始化图标
        mIconLocation = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps);
        initOrientation();
        //开始定位
        mLocationClient.start();
    }

    //回到定位中心
    private void centerToMyLocation(double latitude, double longtitude) {
        mBaiduMap.clear();
        mLastLocationData = new LatLng(latitude, longtitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(mLastLocationData);
        mBaiduMap.animateMapStatus(msu);
    }

    //传感器
    private void initOrientation() {
        //传感器
        mapListener = new MapListener(context);
        mapListener.setOnOrientationListener(new MapListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });
    }

    //路线规划初始化
    private void initPoutePlan() {
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(listener);
    }

    // 路线规划模块
    public OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(MapLocation.this, "路线规划:未找到结果,检查输入", Toast.LENGTH_SHORT).show();
                //禁止定位
                isFirstin = false;
            }
            assert result != null;
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                result.getSuggestAddrInfo();
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                mBaiduMap.clear();
                Toast.makeText(MapLocation.this, "路线规划:搜索完成", Toast.LENGTH_SHORT).show();
                WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
            //禁止定位
            isFirstin = false;
        }
        @Override
        public void onGetTransitRouteResult(TransitRouteResult var1) {
        }
        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult var1) {
        }
        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult result) {
        }
        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult var1) {
        }
        @Override
        public void onGetBikingRouteResult(BikingRouteResult var1) {
        }
    };
    //开始规划
    private void StarRoute() {
        SDKInitializer.initialize(getApplicationContext());
        // 设置起、终点信息
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("温州", "温州大学");
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("温州", "温州南站");
        mSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(stNode)
                .to(enNode));
    }


}
