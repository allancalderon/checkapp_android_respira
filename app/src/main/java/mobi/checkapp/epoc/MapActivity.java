package mobi.checkapp.epoc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import mobi.checkapp.epoc.component.MakerItemComponent;
import mobi.checkapp.epoc.controller.VolleyControler;
import mobi.checkapp.epoc.entities.MarkerItem;
import mobi.checkapp.epoc.entities.MyItem;
import mobi.checkapp.epoc.entities.Pollutant;
import mobi.checkapp.epoc.entities.PollutantMeasure;
import mobi.checkapp.epoc.utils.CacheMainActivity;
import mobi.checkapp.epoc.utils.Constants;
import mobi.checkapp.epoc.utils.Utils;

//import com.google.maps.android.clustering.ClusterManager;

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        ClusterManager.OnClusterItemInfoWindowClickListener<MyItem> {
    //General
    private final String TAG = this.getClass().getName();
    //Map manipulation
    private int mShowMap;
    private boolean isMapSetup;
    final private int RQS_GooglePlayServices = 1;

    private Context context=null;
    private GoogleMap mMap = null;
    private ClusterManager<MyItem> mClusterManager;
    private MyItem clickedClusterItem;
    private Cluster<MyItem> clickedCluster;
    private boolean isClustered = false;
    //Float Button
    private Toolbar toolbar;
    //Pollution Measurement
    private HashMap<Integer, Pollutant> pollutantsHashMap;

    private Date lastSensorUpdate;
    //volley
    private String urlJsonArry;
    private GoogleApiClient mGoogleApiClient = null;
    public boolean firstTime = false;
    private boolean isZoomSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        //Toolbar definitions
        toolbar = (Toolbar) findViewById(R.id.toolbar_main_activity);
        context = getApplicationContext();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Sitio y sensores");

        //Check for map availability
        mShowMap = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());
        isMapSetup = startMap();
        //Draw Navigation View
        //startNavigationView();
        Utils.setupPollutantsList();
        getPollutantsMeasure(1, "ESPMAD", 0);
        //Utils.initialize();

        //TODO inicializar checkbox
        CacheMainActivity.CheckboxState = "true-true-true-true-true-true-true-true-true-true-true-true-true-true";

    }

    /**
    private void startNavigationView() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    */

    public void getPollutantsMeasure(int type, String id, int complexity) {
        urlJsonArry = Constants.URL_POLLUTION +
                "?type=" + String.valueOf(type) +
                "&id=" + id +
                "&complexity=" + String.valueOf(complexity);
        long diffInMillies = 0;
        Date currentDate = null;
        //date compare in order to verify the new request
        if (CacheMainActivity.lastUpdate == null)
            CacheMainActivity.lastUpdate = new HashMap<>();
        currentDate = CacheMainActivity.lastUpdate.get(id.substring(0, 5));
        if (currentDate != null) {
            diffInMillies = currentDate.getTime() - new Date().getTime();

            diffInMillies = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
        }
        if (diffInMillies > 40 ||
                currentDate == null ||
                CacheMainActivity.lastUpdate == null ||
                CacheMainActivity.sensorMeasureList == null) {
            makeJsonObjectRequest(urlJsonArry, id);
        } else if (!CacheMainActivity.sensorMeasureList.isEmpty()) {
            updateBar();
        }
    }

    private void makeJsonObjectRequest(String getParams, final String id) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                getParams, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());
                String error = "";
                try {
                    // Parsing json array response
                    // loop through each json object
                    JSONArray sensors = response.getJSONArray("sensors");
                    error = response.getString("error");
                    if (!error.equals("true")) {
                        for (int contSensors = 0; contSensors < sensors.length(); contSensors++) {
                            List<PollutantMeasure> pollutantMeasureList = new ArrayList<>();
                            JSONObject arraySensors = (JSONObject) sensors.get(contSensors);
                            String idsensor = arraySensors.getString("idsensor");
                            JSONArray pollutants = arraySensors.getJSONArray("pollutants");
                            for (int contPollutants = 0; contPollutants < pollutants.length(); contPollutants++) {
                                JSONObject pollutantsObject = (JSONObject) pollutants.get(contPollutants);
                                String idpollutantglobal = pollutantsObject.getString("idpollutantglobal");
                                String year = pollutantsObject.getString("year");
                                String month = pollutantsObject.getString("month");
                                String day = pollutantsObject.getString("day");
                                String hour0 = pollutantsObject.getString("hour0");
                                Integer hour0val = pollutantsObject.getInt("hour0val");
                                if (hour0val != 100) {
                                    Calendar calendar = new GregorianCalendar();
                                    calendar.set(Calendar.YEAR, 2016);
                                    calendar.set(Calendar.MONTH, 3); // 11 = december
                                    calendar.set(Calendar.DAY_OF_MONTH, 24); // christmas eve
                                    Pollutant pollutantTemp = pollutantsHashMap.get(Integer.valueOf(idpollutantglobal));
                                    PollutantMeasure pollutMeasureTmp = new PollutantMeasure(
                                            pollutantTemp,
                                            Double.valueOf(hour0),
                                            Integer.valueOf(year),
                                            Integer.valueOf(month),
                                            Integer.valueOf(day),
                                            hour0val);
                                    pollutantMeasureList.add(pollutMeasureTmp);
                                }
                            }
                            if (CacheMainActivity.sensorMeasureList == null)
                                CacheMainActivity.sensorMeasureList = new HashMap<>();
                            if (CacheMainActivity.sensorMeasureList.get(idsensor) != null)
                                CacheMainActivity.sensorMeasureList.remove(idsensor);
                            CacheMainActivity.sensorMeasureList.put(idsensor, pollutantMeasureList);
                        }
                        CacheMainActivity.lastUpdate = new HashMap<>();
                        //lastUpdate.put(id.substring(0,5),Calendar.getInstance().getTime());
                        CacheMainActivity.lastUpdate.put(id.substring(0, 5), new Date());
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Error: " + error,
                                Toast.LENGTH_LONG).show();
                    }
                    updateBar();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // Adding request to request queue
        if (jsonObjReq != null)
            VolleyControler.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void updateBar() {
        int value = 0;
        for (List<PollutantMeasure> lista : CacheMainActivity.sensorMeasureList.values()) {
            int newvalue = 0;
            for (PollutantMeasure p : lista) {
                newvalue = Utils.getPollutantValue(p);
                if (newvalue > value)
                    value = newvalue;
            }
        }
        if (value == 1) {
            getSupportActionBar().setTitle(getResources().getString(R.string.contaminants_good));
            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 255, 0)));
        }
        if (value == 2) {
            getSupportActionBar().setTitle(getResources().getString(R.string.contaminants_satisfactory));
            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,0)));
        }
        if (value == 3) {
            getSupportActionBar().setTitle(getResources().getString(R.string.contaminants_moderately));
            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(255,128,0)));
        }
        if (value == 4) {
            getSupportActionBar().setTitle(getResources().getString(R.string.contaminants_poor));
            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(255,51,51)));
        }
        if (value == 5) {
            getSupportActionBar().setTitle(getResources().getString(R.string.contaminants_verypoor));
            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(153,51,255)));
        }
        //getSupportActionBar().setTitle(String.valueOf(CacheMainActivity.sensorMeasureList.size()));
    }

    private boolean startMap() {

        //TODO filtrar
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                    findFragmentById(R.id.map);
            //mapFragment.getMapAsync(MapActivity.this);
            try {
                mapFragment.getMapAsync(MapActivity.this);
             }catch (Exception e){
                 e.printStackTrace();
                 Toast.makeText(getApplicationContext(),
                 "Error: " + e.toString(),
                 Toast.LENGTH_LONG).show();
             }
        }
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return (mMap != null);
            }
            if(mMap!=null)
                Utils.updateAddress(this, mMap.getMyLocation(), mGoogleApiClient);
        }
        return (mMap != null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startMap();

        if (firstTime) {
            inicializarMapa();
        }

        firstTime = true;

    }

    @Override
    public void onBackPressed() {
        /**
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
         */
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i = null;
        switch(id){
            case android.R.id.home:
                Log.d(TAG, "Back button clicked");
                super.onBackPressed();
                finish();
                break;
            case R.id.action_filter:
                i = new Intent(getApplicationContext(), FilterActivity.class);
                startActivity(i);
                break;
            case R.id.action_legend:
                i = new Intent(getApplicationContext(), FilterActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //googleMap.clear();
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now
                int REQUEST_LOCATION = 2;
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Display UI and wait for user interaction
                } else {
                    ActivityCompat.requestPermissions(
                            this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION);
                }
            } else {
                // Create an instance of GoogleAPIClient.
                if (mGoogleApiClient == null) {
                    mGoogleApiClient = new GoogleApiClient.Builder(this)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .addApi(LocationServices.API)
                            .build();
                }
                if(mMap!=null)
                    Utils.updateAddress(this, mMap.getMyLocation(), mGoogleApiClient);
            }
        }
        inicializarMapa();
    }

    //added with edit
    @Override
    public void onClusterItemInfoWindowClick(MyItem myItem) {

        //Cluster item InfoWindow clicked, set title as action
        //Intent i = new Intent(this, OtherActivity.class);
        //i.setAction(myItem.getName());
        //startActivity(i);

        //You may want to do different things for each InfoWindow:
        //if (myItem.getIdTypeExerciseFK().equals("some title")){

        //do something specific to this InfoWindow....

        //}
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if(mMap!=null)
            Utils.updateAddress(this, mMap.getMyLocation(), mGoogleApiClient);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void inicializarMapa() {
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(context != null && mMap != null && mGoogleApiClient != null) {
                    Utils.updateAddress(context, mMap.getMyLocation(), mGoogleApiClient);
                }
                if(!isZoomSet) {
                    zoomPlace(new LatLng(location.getLatitude(), location.getLongitude()));
                    isZoomSet = true;
                }
            }
        });
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        //mMap.getUiSettings().setMapToolbarEnabled(false);
        mClusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setRenderer(new MyClusterRenderer(this, mMap,
                mClusterManager));
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                // Clear the map here because the markers will be recreated
                // when the manager is notified of a (zoom level) camera change
                float zoom = mMap.getCameraPosition().zoom;
                //Toast.makeText(getApplicationContext(), String.valueOf(zoom), Toast.LENGTH_LONG).show();
                if (zoom > 11) {
                    isClustered = false;
                    //mClusterManager.clearItems();
                } else {
                    isClustered = true;
                }
                mClusterManager.onCameraChange(cameraPosition);
            }
        });
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnInfoWindowClickListener(mClusterManager); //added
        mClusterManager.setOnClusterItemInfoWindowClickListener(this); //added

        mClusterManager
                .setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
                    @Override
                    public boolean onClusterItemClick(MyItem item) {
                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(item.getPosition(), mMap.getCameraPosition().zoom+1));
                        //default
                        clickedClusterItem = item;
                        return false;
                    }
                });
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), mMap.getCameraPosition().zoom+1));
                // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                /**
                 CameraPosition cameraPosition = new CameraPosition.Builder()
                 .target(cluster.getPosition())      // Sets the center of the map to Mountain View
                 .zoom(mMap.getCameraPosition().zoom+1)                   // Sets the zoom
                 .build();                   // Creates a CameraPosition from the builder
                 mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                 mMap.animateCamera(CameraUpdateFactory.zoomIn());
                 */
                clickedCluster = cluster; // remember for use later in the Adapter
                return false;
            }
        });


        mClusterManager.clearItems();

        String[]parts = CacheMainActivity.CheckboxState.split("-");
        int i = 0;

        for(String s: parts){
            //Toast.makeText(FilterActivity.this, s, Toast.LENGTH_SHORT).show();

            if(Boolean.valueOf(s)){
                //Toast.makeText(FilterActivity.this, String.valueOf(i), Toast.LENGTH_SHORT).show();

                switch (i){
                    case 0:
                        setupMarkersFromXml(i);
                        break;
                    case 1:
                        setupMarkersFromXml(i);
                        break;
                    case 2:
                        setupMarkersFromXml(i);
                        break;
                    case 3:
                        setupMarkersFromXml(i);
                        break;
                    case 4:
                        setupMarkersFromXml(i);
                        break;
                    case 5:
                        setupMarkersFromXml(i);
                        setupRoutes();
                        break;
                    case 6:
                        setupMarkersFromXml(i);
                        break;
                    case 7:
                        setupMarkersFromXml(i);
                        break;
                    case 8:
                        setupMarkersFromXml(i);
                        break;
                    case 9:
                        setupMarkersFromXml(i);
                        break;
                    case 10:
                        setupMarkersFromXml(i);
                        break;
                    case 11:
                        setupMarkersFromXml(i);
                        break;
                    case 12:
                        setupMarkersFromXml(i);
                        break;
                    case 13:
                        setupMarkersFromXml(i);
                        break;

                    default:break;
                }
            }

            i++;
        }

        CacheMainActivity.myLocation = mMap.getMyLocation();

        if(mClusterManager!=null) {
            mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(
                    new MyCustomAdapterForItems());
        }
        if (mMap != null) {
            CameraPosition currentCameraPosition = mMap.getCameraPosition();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentCameraPosition));
        }
    }

    public class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter {
        private final View myContentsView;
        MyCustomAdapterForItems() {
            myContentsView = getLayoutInflater().inflate(
                    R.layout.ln_info_window, null);
        }
        @Override
        public View getInfoWindow(Marker marker) {
            TextView tvTitle = ((TextView) myContentsView
                    .findViewById(R.id.window));
            tvTitle.setText(clickedClusterItem.getName());
            //TODO
            LinearLayout lnInformation = (LinearLayout)myContentsView.findViewById(R.id.lnInformation);

            if(clickedClusterItem.getTypes().size()>0 && clickedClusterItem.getTypes().get(0)==5){
                //aca se llena el layout con los pollutants
                String idpollution = clickedClusterItem.getIdPosition();
                List<PollutantMeasure> list = null;
                if(CacheMainActivity.sensorMeasureList != null) {
                    list = CacheMainActivity.sensorMeasureList.get(idpollution);
                    //  TODO temp, uv
                    ArrayList<MarkerItem> listaMarkerItems = new ArrayList<>();
                    for (PollutantMeasure p : list) {
                        int value = Utils.getPollutantValue(p);
                        if (value != 0) {
                            String message = "";
                            if (value == 1)
                                message = getResources().getString(R.string.contaminants_good);
                            if (value == 2)
                                message = getResources().getString(R.string.contaminants_satisfactory);
                            if (value == 3)
                                message = getResources().getString(R.string.contaminants_moderately);
                            if (value == 4)
                                message = getResources().getString(R.string.contaminants_poor);
                            if (value == 5)
                                message = getResources().getString(R.string.contaminants_verypoor);
                            MarkerItem item = new MarkerItem();
                            item.value = value;
                            item.nValue = message;
                            item.Nombre = p.getPollutant().getName();
                            listaMarkerItems.add(item);
                        }
                    }
                    Collections.sort(listaMarkerItems, new Comparator<MarkerItem>() {
                        @Override
                        public int compare(MarkerItem o1, MarkerItem o2) {
                            return o2.value - o1.value;
                        }
                    });
                    lnInformation.removeAllViews();

                    for (MarkerItem m : listaMarkerItems) {
                        MakerItemComponent component = new MakerItemComponent(MapActivity.this, m);
                        //TextView textView = new TextView(MapActivity.this);
                        //textView.setText(m.Nombre + " value = " + m.nValue);
                        lnInformation.addView(component);
                    }
                    lnInformation.setVisibility(View.VISIBLE);
                }
            }else{
                lnInformation.setVisibility(View.GONE);
            }

            return myContentsView;
        }
        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    public class MyClusterRenderer extends DefaultClusterRenderer<MyItem> {
        private IconGenerator mClusterIconGenerator;
        public MyClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item,
                                                   MarkerOptions markerOptions) {
            BitmapDescriptor markerDescriptor;
            Drawable icon;
            Canvas canvas;
            Bitmap bitmap;
            int iconId = R.drawable.ic_place_black_24dp;
            int colorId = android.R.color.holo_purple;
            if(item.getTypes() != null && item.getTypes().size() > 0) {
                if (item.getTypes().get(0).equals(1)) {
                    iconId = R.drawable.ic_add_location_black_24dp;
                    colorId = android.R.color.holo_purple;
                } else if (item.getTypes().get(0).equals(2)) {
                    iconId = R.drawable.ic_add_location_black_24dp;
                    colorId = android.R.color.holo_green_dark;
                } else if (item.getTypes().get(0).equals(3)) {
                    iconId = R.drawable.ic_add_location_black_24dp;
                    colorId = android.R.color.darker_gray;
                } else if (item.getTypes().get(0).equals(4)) {
                    iconId = R.drawable.ic_person_pin_circle_black_24dp;
                    colorId = android.R.color.darker_gray;
                }else if (item.getTypes().get(0).equals(5)) {
                    iconId = R.drawable.ic_golf_course_black_48dp;
                    colorId = android.R.color.darker_gray;
                }else if (item.getTypes().get(0).equals(6)) {
                    iconId = R.drawable.ic_add_location_black_24dp;
                    colorId = android.R.color.holo_blue_light;
                }
            }
            try{
                icon = getResources().getDrawable(iconId);
                icon.setColorFilter(getResources().getColor(colorId), PorterDuff.Mode.SRC_ATOP);
                bitmap = Bitmap.createBitmap(icon.getIntrinsicWidth(), icon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                icon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                icon.draw(canvas);
                markerDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                markerOptions.icon(markerDescriptor);
                }
            catch (Exception e){
                Log.e(TAG, "Error reading group markers", e);
            }
        }

        @Override
        protected void onClusterItemRendered(MyItem clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
        }
        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            if(isClustered)
                return cluster.getSize() > 4;
            return false;
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MyItem> cluster, MarkerOptions markerOptions){
            mClusterIconGenerator = new IconGenerator(getApplicationContext());
            Drawable clusterIconDrawable;
            int iconId = R.drawable.ic_announcement_black_24dp;
            int colorId = android.R.color.holo_orange_light;
            try{
                clusterIconDrawable = getResources().getDrawable(iconId);
                clusterIconDrawable.setColorFilter(getResources().getColor(colorId), PorterDuff.Mode.SRC_ATOP);
                mClusterIconGenerator.setBackground(clusterIconDrawable);
                mClusterIconGenerator.setContentPadding(10, 10, 10, 10);
                Bitmap clusterIconBitmap = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(clusterIconBitmap));
            }catch (Exception e){
                Log.e(TAG, "Error reading cluster render", e);
            }
        }
    }

    private void setupMarkersFromXml(){
        XmlResourceParser mMarkersXmlParser = getResources().getXml(R.xml.maps_markers);
        try {
            mMarkersXmlParser.next();
            String tag;
            MyItem myItem = null;
            List<Integer> types = null;
            int latLngCont=0;
            float lat=0;
            float lng=0;
            while (mMarkersXmlParser.getEventType()!= XmlPullParser.END_DOCUMENT) {
                if (mMarkersXmlParser.getEventType()==XmlPullParser.START_TAG) {
                    tag = mMarkersXmlParser.getName();
                    switch(tag){
                        case "mark":
                            myItem = new MyItem();
                            latLngCont = 0;
                            types = new ArrayList<>();
                            break;
                        case "lat":
                            lat = Float.valueOf(mMarkersXmlParser.getAttributeValue(0));
                            latLngCont++;
                            break;
                        case "lng":
                            lng = Float.valueOf(mMarkersXmlParser.getAttributeValue(0));
                            latLngCont++;
                            break;
                        case "name":
                            myItem.setName(mMarkersXmlParser.getAttributeValue(0));
                            break;
                        case "addressName":
                            myItem.setAddressName(mMarkersXmlParser.getAttributeValue(0));
                            break;
                        case "otherInfo1":
                            myItem.setOtherInfo1(mMarkersXmlParser.getAttributeValue(0));
                            break;
                        case "otherInfo2":
                            myItem.setOtherInfo2(mMarkersXmlParser.getAttributeValue(0));
                            break;
                        case "otherInfo3":
                            myItem.setOtherInfo3(mMarkersXmlParser.getAttributeValue(0));
                            break;
                        case "dateTime":
                            myItem.setDateTime(mMarkersXmlParser.getAttributeValue(0));
                            break;
                        case "source":
                            myItem.setSource(mMarkersXmlParser.getAttributeValue(0));
                            break;
                        case "idPosition":
                            myItem.setIdPosition(mMarkersXmlParser.getAttributeValue(0));
                            break;
                        case "idTypeAssignment":
                            types.add(Integer.valueOf(mMarkersXmlParser.getAttributeValue(0)));
                            break;
                    }
                }else if (mMarkersXmlParser.getEventType()==XmlPullParser.END_TAG) {
                    tag = mMarkersXmlParser.getName();
                    if(tag.equals("mark")){
                        if(latLngCont == 2){
                            myItem.setPosition(new LatLng(lat,lng));
                        }
                        myItem.setTypes(types);
                        mClusterManager.addItem(myItem);
                    }
                }
                mMarkersXmlParser.next();
            }

            for(MyItem i:CacheMainActivity.myItems)
                mClusterManager.addItem(i);

        }catch(Exception e){
            Log.e(TAG, "Error reading markers from res/xml", e);
        }
    }

    private void setupMarkersFromXml(int type){

        XmlResourceParser mMarkersXmlParser = null;
        switch (type){
            case 0:
                mMarkersXmlParser = getResources().getXml(R.xml.maps_points_markers);
                break;
            case 1:
                //checkBoxFilterMyPlaces.setChecked(false);
                break;
            case 2:
                //checkBoxFilterTemperature.setChecked(false);
                break;
            case 3:
                //checkBoxFilterParks.setChecked(false);
                break;
            case 4:
                //checkBoxFilterGyms.setChecked(false);
                break;
            case 5:
                mMarkersXmlParser = getResources().getXml(R.xml.maps_rare_points_markers);
                break;
            case 6:
                //checkBoxFilterBycicles.setChecked(false);
                break;
            case 7:
                //checkBoxFilterBars.setChecked(false);
                break;
            case 8:
                //checkBoxFilterHealth.setChecked(false);
                break;
            case 9:
                //checkBoxFilterHospitalEsp.setChecked(false);
                break;
            case 10:
                mMarkersXmlParser = getResources().getXml(R.xml.maps_clinics_markers);
                break;
            case 11:
                mMarkersXmlParser = getResources().getXml(R.xml.maps_hospital_markers);
                break;
            case 12:
                mMarkersXmlParser = getResources().getXml(R.xml.maps_centers_markers);
                break;
            case 13:
                //checkBoxFilterAnotherPlaces.setChecked(false);
                break;

            default:break;
        }
        //mMarkersXmlParser = getResources().getXml(R.xml.maps_markers);
        try {
            if(mMarkersXmlParser != null) {
                mMarkersXmlParser.next();
                String tag;
                MyItem myItem = null;
                List<Integer> types = null;
                int latLngCont = 0;
                float lat = 0;
                float lng = 0;
                while (mMarkersXmlParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (mMarkersXmlParser.getEventType() == XmlPullParser.START_TAG) {
                        tag = mMarkersXmlParser.getName();
                        switch (tag) {
                            case "mark":
                                myItem = new MyItem();
                                latLngCont = 0;
                                types = new ArrayList<>();
                                break;
                            case "lat":
                                lat = Float.valueOf(mMarkersXmlParser.getAttributeValue(0));
                                latLngCont++;
                                break;
                            case "lng":
                                lng = Float.valueOf(mMarkersXmlParser.getAttributeValue(0));
                                latLngCont++;
                                break;
                            case "name":
                                myItem.setName(mMarkersXmlParser.getAttributeValue(0));
                                break;
                            case "addressName":
                                myItem.setAddressName(mMarkersXmlParser.getAttributeValue(0));
                                break;
                            case "otherInfo1":
                                myItem.setOtherInfo1(mMarkersXmlParser.getAttributeValue(0));
                                break;
                            case "otherInfo2":
                                myItem.setOtherInfo2(mMarkersXmlParser.getAttributeValue(0));
                                break;
                            case "otherInfo3":
                                myItem.setOtherInfo3(mMarkersXmlParser.getAttributeValue(0));
                                break;
                            case "dateTime":
                                myItem.setDateTime(mMarkersXmlParser.getAttributeValue(0));
                                break;
                            case "source":
                                myItem.setSource(mMarkersXmlParser.getAttributeValue(0));
                                break;
                            case "idPosition":
                                myItem.setIdPosition(mMarkersXmlParser.getAttributeValue(0));
                                break;
                            case "idTypeAssignment":
                                types.add(Integer.valueOf(mMarkersXmlParser.getAttributeValue(0)));
                                break;
                        }
                    } else if (mMarkersXmlParser.getEventType() == XmlPullParser.END_TAG) {
                        tag = mMarkersXmlParser.getName();
                        if (tag.equals("mark")) {
                            if (latLngCont == 2) {
                                myItem.setPosition(new LatLng(lat, lng));
                            }
                            myItem.setTypes(types);
                            mClusterManager.addItem(myItem);
                        }
                    }
                    mMarkersXmlParser.next();
                }
            }
            for(MyItem i:CacheMainActivity.myItems)
                mClusterManager.addItem(i);

        }catch(Exception e){
            Log.e(TAG, "Error reading markers from res/xml", e);
        }
    }

    private void setupRoutes() {
        LatLng tmpLatLng;
        List<LatLng> routes = new ArrayList<LatLng>();
        PolylineOptions tmpPolylineOptions = new PolylineOptions();
        List<PolylineOptions> listPolylineOptions = new ArrayList<PolylineOptions>();
        //new route
        routes.add(new LatLng(40.5485413,-3.6262447));
        routes.add(new LatLng(40.5483579,-3.6260623));
        routes.add(new LatLng(40.5482152,-3.626411));
        routes.add(new LatLng(40.5482111,-3.6267382));
        routes.add(new LatLng(40.5480155,-3.6269367));
        routes.add(new LatLng(40.5481948,-3.6272961));
        routes.add(new LatLng(40.5477628,-3.6279988));
        routes.add(new LatLng(40.5476812,-3.6277735));
        routes.add(new LatLng(40.5475223,-3.6274463));
        routes.add(new LatLng(40.5472858,-3.6271995));
        routes.add(new LatLng(40.5477668,-3.6262554));
        routes.add(new LatLng(40.5475263,-3.6260784));
        routes.add(new LatLng(40.5473103,-3.626368));
        routes.add(new LatLng(40.5470983,-3.626985));
        routes.add(new LatLng(40.5468415,-3.626883));
        routes.add(new LatLng(40.5469842,-3.6264914));
        routes.add(new LatLng(40.5471513,-3.6261749));
        routes.add(new LatLng(40.5472899,-3.6262125));
        routes.add(new LatLng(40.5472736,-3.6259657));
        routes.add(new LatLng(40.5470616,-3.625719));
        routes.add(new LatLng(40.5476038,-3.6250913));
        routes.add(new LatLng(40.5470779,-3.6238253));
        routes.add(new LatLng(40.5475712,-3.6234176));
        routes.add(new LatLng(40.5479584,-3.6238199));
        routes.add(new LatLng(40.5479706,-3.6242008));
        routes.add(new LatLng(40.5480644,-3.6246943));
        routes.add(new LatLng(40.5483375,-3.6244637));
        routes.add(new LatLng(40.5487003,-3.6240882));
        routes.add(new LatLng(40.5486065,-3.6244261));
        routes.add(new LatLng(40.5484516,-3.6247373));
        routes.add(new LatLng(40.5488919,-3.6250538));
        routes.add(new LatLng(40.5492343,-3.6255687));
        routes.add(new LatLng(40.5488063,-3.6257136));
        routes.add(new LatLng(40.5485413,-3.6262447));
        tmpPolylineOptions.addAll(routes);
        tmpPolylineOptions
                .width(5)
                .color(Color.argb(255,0,139,139));
        listPolylineOptions.add(tmpPolylineOptions);
        //restart
        tmpPolylineOptions = null;
        tmpPolylineOptions = new PolylineOptions();
        routes = null;
        routes = new ArrayList<LatLng>();
        //new route
        routes.add(new LatLng(40.5456145,-3.6236376));
        routes.add(new LatLng(40.5458184,-3.6239326));
        routes.add(new LatLng(40.5468823,-3.6230421));
        routes.add(new LatLng(40.547188,-3.6237609));
        //add new PolyLine
        tmpPolylineOptions.addAll(routes);
        tmpPolylineOptions
                .width(5)
                .color(Color.argb(255,0,190,190));
        listPolylineOptions.add(tmpPolylineOptions);
        //restart
        tmpPolylineOptions = null;
        tmpPolylineOptions = new PolylineOptions();
        routes = null;
        routes = new ArrayList<LatLng>();
        //new route
        routes.add(new LatLng(40.5485413,-3.6262447));
        routes.add(new LatLng(40.548953,-3.6267757));
        routes.add(new LatLng(40.5494584,-3.6277199));
        //add new PolyLine
        tmpPolylineOptions.addAll(routes);
        tmpPolylineOptions
                .width(5)
                .color(Color.argb(255,0,190,190));
        listPolylineOptions.add(tmpPolylineOptions);
        //restart
        tmpPolylineOptions = null;
        tmpPolylineOptions = new PolylineOptions();
        routes = null;
        routes = new ArrayList<LatLng>();
        //new route
        routes.add(new LatLng(40.5494584,-3.6277199));
        routes.add(new LatLng(40.5497682,-3.6280632));
        routes.add(new LatLng(40.5517655,-3.6292648));
        routes.add(new LatLng(40.5522872,-3.6298978));
        routes.add(new LatLng(40.5522872,-3.6301661));
        routes.add(new LatLng(40.552385,-3.6304235));
        routes.add(new LatLng(40.5525317999999,-3.6304665));
        //add new PolyLine
        tmpPolylineOptions.addAll(routes);
        tmpPolylineOptions
                .width(5)
                .color(Color.RED);
        listPolylineOptions.add(tmpPolylineOptions);
        //restart
        tmpPolylineOptions = null;
        tmpPolylineOptions = new PolylineOptions();
        routes = null;
        routes = new ArrayList<LatLng>();
        //new route
        routes.add(new LatLng(40.5506568,-3.63258));
        routes.add(new LatLng(40.5486921,-3.6332345));
        routes.add(new LatLng(40.5486432,-3.6331272));
        routes.add(new LatLng(40.5485617,-3.6330843));
        routes.add(new LatLng(40.5485128,-3.6331594));
        routes.add(new LatLng(40.5484476,-3.6330199));
        routes.add(new LatLng(40.5468904,-3.6343074));
        routes.add(new LatLng(40.5463687,-3.6344361));
        routes.add(new LatLng(40.5457328,-3.6338246));
        routes.add(new LatLng(40.5454148,-3.6332881));
        routes.add(new LatLng(40.5449583,-3.632226));
        routes.add(new LatLng(40.5442245,-3.6293721));
        routes.add(new LatLng(40.5441756,-3.6289322));
        routes.add(new LatLng(40.5442734,-3.6286533));
        routes.add(new LatLng(40.5444935,-3.6284494));
        routes.add(new LatLng(40.5429282,-3.6262608));
        routes.add(new LatLng(40.5436212,-3.6257458));
        routes.add(new LatLng(40.5435315,-3.6255097));
        routes.add(new LatLng(40.5437272,-3.6253917));
        routes.add(new LatLng(40.5437435,-3.6251235));
        routes.add(new LatLng(40.5454311,-3.6237395));
        routes.add(new LatLng(40.5455289,-3.6237824));
        routes.add(new LatLng(40.5456349,-3.623718));
        routes.add(new LatLng(40.5456349,-3.6235249));
        routes.add(new LatLng(40.5480481,-3.6216474));
        routes.add(new LatLng(40.5481459,-3.6216795));
        routes.add(new LatLng(40.5481785,-3.6215401));
        routes.add(new LatLng(40.552059,-3.6186969));
        routes.add(new LatLng(40.5522302,-3.6187398));
        routes.add(new LatLng(40.5525644,-3.6184394));
        routes.add(new LatLng(40.552597,-3.6181498));
        routes.add(new LatLng(40.5575368,-3.6137187));
        routes.add(new LatLng(40.5576999,-3.6138153));
        routes.add(new LatLng(40.5578873,-3.6137187));
        routes.add(new LatLng(40.5579607,-3.6135364));
        routes.add(new LatLng(40.5579689,-3.6133003));
        routes.add(new LatLng(40.5596072,-3.6117339));
        routes.add(new LatLng(40.5597784,-3.6118734));
        routes.add(new LatLng(40.559974,-3.6117661));
        routes.add(new LatLng(40.563218,-3.6172378));
        routes.add(new LatLng(40.5631202,-3.6173451));
        routes.add(new LatLng(40.5630958,-3.6175489));
        routes.add(new LatLng(40.5632017,-3.6177313));
        routes.add(new LatLng(40.5616124,-3.6193085));
        routes.add(new LatLng(40.5616368,-3.6194801));
        routes.add(new LatLng(40.5615716,-3.6196196));
        routes.add(new LatLng(40.5647177,-3.6249197));
        routes.add(new LatLng(40.5646281,-3.6249626));
        routes.add(new LatLng(40.5646199,-3.6250591));
        routes.add(new LatLng(40.5640494,-3.6251128));
        routes.add(new LatLng(40.5636256,-3.6253273));
        routes.add(new LatLng(40.5629246,-3.6254239));
        routes.add(new LatLng(40.561001,-3.6254561));
        routes.add(new LatLng(40.5603571,-3.625617));
        routes.add(new LatLng(40.5597295,-3.627001));
        routes.add(new LatLng(40.5594687,-3.6279988));
        routes.add(new LatLng(40.5594198,-3.628428));
        routes.add(new LatLng(40.5586862,-3.6284173));
        routes.add(new LatLng(40.5585028,-3.6285889));
        //add new PolyLine
        tmpPolylineOptions.addAll(routes);
        tmpPolylineOptions
                .width(5)
                .color(Color.RED);
        listPolylineOptions.add(tmpPolylineOptions);
        //restart
        tmpPolylineOptions = null;
        tmpPolylineOptions = new PolylineOptions();
        routes = null;
        routes = new ArrayList<LatLng>();
        //new route
        routes.add(new LatLng(40.5545127,-3.6281919));
        routes.add(new LatLng(40.5535263,-3.6286747));
        routes.add(new LatLng(40.5533062,-3.6288249));
        routes.add(new LatLng(40.5532451,-3.628833));
        routes.add(new LatLng(40.5531798,-3.6289215));
        routes.add(new LatLng(40.5525746,-3.6300722));
        routes.add(new LatLng(40.5526011,-3.6301956));
        routes.add(new LatLng(40.5526011,-3.6303458));
        routes.add(new LatLng(40.552544,-3.6304021));
        routes.add(new LatLng(40.5525012,-3.630437));
        routes.add(new LatLng(40.552436,-3.6304343));
        routes.add(new LatLng(40.5523137,-3.6305469));
        routes.add(new LatLng(40.5513762,-3.6323279));
        routes.add(new LatLng(40.5519061,-3.6331889));
        routes.add(new LatLng(40.5521588,-3.6336932));
        routes.add(new LatLng(40.552273,-3.6342323));
        routes.add(new LatLng(40.5524095,-3.6352676));
        routes.add(new LatLng(40.5524788,-3.6352944));
        routes.add(new LatLng(40.5524869,-3.6353856));
        routes.add(new LatLng(40.5524421,-3.6354339));
        routes.add(new LatLng(40.5525644,-3.6363727));
        routes.add(new LatLng(40.5528375,-3.6368555));
        routes.add(new LatLng(40.5530983,-3.637231));
        routes.add(new LatLng(40.553188,-3.6374938));
        routes.add(new LatLng(40.5532573,-3.6383039));
        routes.add(new LatLng(40.5533551,-3.6386633));
        routes.add(new LatLng(40.5536282,-3.6394089));
        routes.add(new LatLng(40.5537179,-3.6394733));
        routes.add(new LatLng(40.5537464,-3.6395538));
        routes.add(new LatLng(40.5538116,-3.6395538));
        routes.add(new LatLng(40.5539176,-3.6392963));
        routes.add(new LatLng(40.5540725,-3.6384487));
        routes.add(new LatLng(40.5541458,-3.6374831));
        routes.add(new LatLng(40.5543822,-3.6355948));
        routes.add(new LatLng(40.5544474,-3.6350423));
        //add new PolyLine
        tmpPolylineOptions.addAll(routes);
        tmpPolylineOptions
                .width(5)
                .color(Color.MAGENTA);
        listPolylineOptions.add(tmpPolylineOptions);
        //restart
        tmpPolylineOptions = null;
        tmpPolylineOptions = new PolylineOptions();
        routes = null;
        routes = new ArrayList<LatLng>();
        //new route
        routes.add(new LatLng(40.5524095,-3.6352676));
        routes.add(new LatLng(40.5523728,-3.6353159));
        routes.add(new LatLng(40.5523728,-3.6353749));
        routes.add(new LatLng(40.5518592,-3.6353588));
        routes.add(new LatLng(40.5510155,-3.6353749));
        routes.add(new LatLng(40.5510318,-3.6347258));
        routes.add(new LatLng(40.5509055,-3.6340392));
        routes.add(new LatLng(40.5506568,-3.63258));
        routes.add(new LatLng(40.5513762,-3.6323279));
        //add new PolyLine
        tmpPolylineOptions.addAll(routes);
        tmpPolylineOptions
                .width(5)
                .color(Color.MAGENTA);
        listPolylineOptions.add(tmpPolylineOptions);
        //restart
        tmpPolylineOptions = null;
        tmpPolylineOptions = new PolylineOptions();
        routes = null;
        routes = new ArrayList<LatLng>();
        //new route
        routes.add(new LatLng(40.5525317999999,-3.6304665));
        routes.add(new LatLng(40.5532899,-3.6316037));
        routes.add(new LatLng(40.5537464,-3.6325049));
        routes.add(new LatLng(40.5544556,-3.6342323));
        routes.add(new LatLng(40.5545453,-3.6341786));
        routes.add(new LatLng(40.5537627,-3.6323225));
        routes.add(new LatLng(40.5543985,-3.6317325));
        routes.add(new LatLng(40.5545534,-3.631357));
        routes.add(new LatLng(40.5547572,-3.6303163));
        routes.add(new LatLng(40.5546838,-3.6302412));
        routes.add(new LatLng(40.5547083,-3.630091));
        routes.add(new LatLng(40.554798,-3.630091));
        routes.add(new LatLng(40.5548387,-3.6295652));
        routes.add(new LatLng(40.5547083,-3.6288035));
        routes.add(new LatLng(40.5546105,-3.6283851));
        routes.add(new LatLng(40.5546023,-3.6282241));
        routes.add(new LatLng(40.5554908,-3.6276984));
        routes.add(new LatLng(40.555874,-3.6275804));
        routes.add(new LatLng(40.5559881,-3.6274731));
        routes.add(new LatLng(40.5567136,-3.6272693));
        routes.add(new LatLng(40.5569663,-3.627398));
        routes.add(new LatLng(40.5576917,-3.628267));
        routes.add(new LatLng(40.5579689,-3.6283851));
        routes.add(new LatLng(40.5581726,-3.6283743));
        routes.add(new LatLng(40.5584335,-3.6284816));
        routes.add(new LatLng(40.5585028,-3.6285889));
        routes.add(new LatLng(40.5583601,-3.6295867));
        routes.add(new LatLng(40.558409,-3.6301446));
        routes.add(new LatLng(40.5587758,-3.631593));
        routes.add(new LatLng(40.5588736,-3.6316681));
        routes.add(new LatLng(40.5588981,-3.6318398));
        routes.add(new LatLng(40.5588003,-3.6319578));
        routes.add(new LatLng(40.5589388,-3.632859));
        routes.add(new LatLng(40.5589633,-3.6333632));
        routes.add(new LatLng(40.5588818,-3.6343718));
        routes.add(new LatLng(40.5587025,-3.6352515));
        routes.add(new LatLng(40.5584579,-3.6365068));
        routes.add(new LatLng(40.5583438,-3.6364746));
        routes.add(new LatLng(40.558246,-3.6365175));
        routes.add(new LatLng(40.5577814,-3.6365068));
        routes.add(new LatLng(40.5577732,-3.6366248));
        routes.add(new LatLng(40.5576836,-3.6366999));
        routes.add(new LatLng(40.5575776,-3.6365926));
        routes.add(new LatLng(40.5572678,-3.6365497));
        routes.add(new LatLng(40.5568766,-3.6364746));
        routes.add(new LatLng(40.5564201,-3.6362922));
        routes.add(new LatLng(40.5559881,-3.6360669));
        routes.add(new LatLng(40.5555887,-3.6357343));
        routes.add(new LatLng(40.5552218,-3.6353803));
        routes.add(new LatLng(40.5549691,-3.6350906));
        routes.add(new LatLng(40.5547735,-3.6348009));
        routes.add(new LatLng(40.5546431,-3.6347687));
        routes.add(new LatLng(40.5544963,-3.634758));
        //add new PolyLine
        tmpPolylineOptions.addAll(routes);
        tmpPolylineOptions
                .width(5)
                .color(Color.MAGENTA);
        listPolylineOptions.add(tmpPolylineOptions);
        //print routes
        for(int i = 0; i<listPolylineOptions.size();i++){
            mMap.addPolyline(listPolylineOptions.get(i));
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(MapActivity.this, connectionResult.toString(), Toast.LENGTH_SHORT).show();
    }

    public void zoomPlace(LatLng location){
        if(location != null && mMap != null) {
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, 13);
            mMap.animateCamera(yourLocation);
        }
    }

}
