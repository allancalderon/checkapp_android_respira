package mobi.checkapp.epoc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import mobi.checkapp.epoc.adapter.OnUpdateFragment;
import mobi.checkapp.epoc.utils.CacheMainActivity;
import mobi.checkapp.epoc.utils.Constants;
import mobi.checkapp.epoc.utils.Utils;


public class MainActivity extends AppCompatActivity
        implements OnUpdateFragment, NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private ViewPager viewPager = null;
    //TODO:put 3 variables in static Constant class
    private ExerciseListPagerAdapter adapter = null;
    private TabLayout tabLayout;
    //variables
    private FloatingActionButton fabExerciseActivity;
    private Drawable[] tabIcons;
    private String[] tabNames;

    //General
    private final String TAG = this.getClass().getName();
    //Map manipulation
    private int mShowMap;
    //Float Button
    private Toolbar toolbar;
    private FloatingActionButton fab, fabChildAnnotation, fabChildActivity, fabChildFilter, fabChildNewPlace;
    private TextView textChildAnnotation, textChildActivity, textChildFilter, textChildNewPlace;
    private Boolean isFabOpen = false;
    private Animation an_fab_open, an_fab_close, an_rotate_forward, an_rotate_backward;
    private GoogleApiClient mGoogleApiClient;

    //get location
    private Context mContext;
    boolean isGPSEnabled = false;    // flag for GPS status
    boolean isNetworkEnabled = false;    // flag for network status
    boolean canGetLocation = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters     // The minimum distance to change Updates in meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;  // 1 minute      // The minimum time between updates in milliseconds
    protected LocationManager locationManager;                      // Declaring a Location Manager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mContext = getApplicationContext();
        //Toolbar definitions
        toolbar = (Toolbar) findViewById(R.id.toolbar_main_activity2);
        setSupportActionBar(toolbar);
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        getLocation();
        //Float Button
        startFloatButton();
        //Draw Navigation View
        startNavigationView();
        setupTabs();
    }

    private void setupTabs() {
        //setup toolbar
        tabNames = new String[]{
                getString(R.string.ExerciseListTabResume),
                getString(R.string.ExerciseListTabRecommend),
                getString(R.string.ExerciseListTabMyExercise),
                getString(R.string.ExerciseListTabTimeline)
        };
        tabIcons = new Drawable[]{
                getResources().getDrawable(R.drawable.ic_assessment_black_24dp),
                getResources().getDrawable(R.drawable.ic_import_export_white_24dp),
                getResources().getDrawable(R.drawable.ic_view_list_white_24dp),
                getResources().getDrawable(R.drawable.ic_timeline_black_24dp),
        };
        //tab setup
        tabIcons[0].setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        tabIcons[1].setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        tabIcons[2].setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        tabIcons[3].setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        tabLayout = (TabLayout) findViewById(R.id.tab_list_exercise_layout2);
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[0]));       //ExerciseListTabResume
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[1]));       //ExerciseListTabRecommend
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[2]));       //ExerciseListTabMyExercise
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[3]));       //ExerciseListTabFavorites
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.activity_list_exercise_viewpager);
        adapter = new ExerciseListPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        getSupportActionBar().setTitle(tabNames[viewPager.getCurrentItem()]);
        tabIcons[viewPager.getCurrentItem()].setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                getSupportActionBar().setTitle(tabNames[tab.getPosition()]);
                tabLayout.getTabAt(tab.getPosition());
                tabIcons[0].setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
                tabIcons[1].setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
                tabIcons[2].setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
                tabIcons[3].setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
                tabIcons[viewPager.getCurrentItem()].setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void startNavigationView() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void startFloatButton() {
        fab = (FloatingActionButton) findViewById(R.id.fabExerciseActivity2);
        fabChildAnnotation = (FloatingActionButton) findViewById(R.id.fabChildActivity2);
        fabChildActivity = (FloatingActionButton) findViewById(R.id.fabChildAnnotation2);
        //fabChildFilter = (FloatingActionButton) findViewById(R.id.fabChildFilter2);
        fabChildNewPlace = (FloatingActionButton) findViewById(R.id.fabChildNewPlace2);
        textChildAnnotation = (TextView) findViewById(R.id.textChildAnnotation2);
        textChildActivity = (TextView) findViewById(R.id.textChildActivity2);
        //textChildFilter = (TextView) findViewById(R.id.textChildFilter2);
        textChildNewPlace = (TextView) findViewById(R.id.textChildNewPlace2);
        an_fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        an_fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        an_rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        an_rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        fab.setOnClickListener(this);
        //fabChildNewPlace.setOnClickListener(this);
        //fabChildFilter.setOnClickListener(this);
        fabChildAnnotation.setOnClickListener(this);
        fabChildActivity.setOnClickListener(this);
        //textChildNewPlace.setOnClickListener(this);
        //textChildFilter.setOnClickListener(this);
        textChildAnnotation.setOnClickListener(this);
        textChildActivity.setOnClickListener(this);
    }

    private void animateFAB() {
        if (isFabOpen) {
            fab.startAnimation(an_rotate_backward);
            textChildActivity.startAnimation(an_fab_close);
            textChildAnnotation.startAnimation(an_fab_close);
            fabChildActivity.startAnimation(an_fab_close);
            fabChildAnnotation.startAnimation(an_fab_close);
            textChildActivity.setClickable(false);
            textChildAnnotation.setClickable(false);
            fabChildActivity.setClickable(false);
            fabChildAnnotation.setClickable(false);
            isFabOpen = false;
            Log.d(TAG, "close");
        } else {
            fab.startAnimation(an_rotate_forward);
            textChildActivity.startAnimation(an_fab_open);
            textChildAnnotation.startAnimation(an_fab_open);
            fabChildActivity.startAnimation(an_fab_open);
            fabChildAnnotation.startAnimation(an_fab_open);
            textChildActivity.setClickable(true);
            textChildAnnotation.setClickable(true);
            fabChildActivity.setClickable(true);
            fabChildAnnotation.setClickable(true);
            isFabOpen = true;
            Log.d(TAG, "open");
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.map, menu);
        //return true;
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i;
        /**
         if (id == R.id.nav_profile) {
         // Handle the camera action
         } else
         */
        if (id == R.id.nav_favorite) {
            i = new Intent(this, ExerciseFavorite.class);
            startActivityForResult(i, Constants.EXERCISEREQCODEACTIVITY);
        } else if (id == R.id.nav_map) {
            i = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(i);
        }
        /**
        } else if (id == R.id.nav_chat) {
            i = new Intent(getApplicationContext(), MainWindow.class);
            startActivity(i);
        }
         else if (id == R.id.nav_about) {

         } else if (id == R.id.nav_helpfeedback) {

         }
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void updateFragments() {
        int currentTab = viewPager.getCurrentItem();
        adapter.notifyDataSetChanged();
        //adapter = new ExerciseListPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentTab);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.EXERCISEREQCODEACTIVITY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                updateFragments();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void OnResume() {
        super.onResume();
        updateFragments();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent i;
        switch (id) {
            case R.id.fabExerciseActivity2:
                animateFAB();
                break;
            /**
             case R.id.textChildNewPlace2:
             case R.id.fabChildNewPlace2:
             i = new Intent(this, NewLocationActivity.class);
             startActivityForResult(i, Constants.EXERCISEREQCODEACTIVITY);
             animateFAB();
             Log.d(TAG, "FAB button, New Place");
             break;
             */
            case R.id.textChildActivity2:
            case R.id.fabChildActivity2:
                //Utils.updateAddress(this,mMap);
                getLocation();
                i = new Intent(this, ExerciseEditActivity.class);
                startActivityForResult(i, Constants.EXERCISEREQCODEACTIVITY);
                animateFAB();
                Log.d(TAG, "FAB button, New Activity");
                break;
            case R.id.textChildAnnotation2:
            case R.id.fabChildAnnotation2:
                //Utils.updateAddress(this,mMap);
                getLocation();
                i = new Intent(this, AssignTimelineAnnotationActivity.class);
                startActivityForResult(i, Constants.EXERCISEREQCODEACTIVITY);
                animateFAB();
                Log.d(TAG, "FAB button, New Annotation");
                break;
            default:
                animateFAB();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
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
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            CacheMainActivity.DirectionLastKnowLocation = mLastLocation;
        }
    }

    public void getLocation() {
        Location location = null; // location
        double latitude = 0; // latitude
        double longitude = 0; // longitude
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
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
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if(location !=null && mGoogleApiClient != null){
            Utils.updateAddress(mContext, location, mGoogleApiClient);
        }
    }
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onConnectionSuspended(int i) {

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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
