package mobi.checkapp.epoc

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices

import mobi.checkapp.epoc.adapter.OnUpdateFragment
import mobi.checkapp.epoc.chat.MainWindow
import mobi.checkapp.epoc.utils.CacheMainActivity
import mobi.checkapp.epoc.utils.Constants
import mobi.checkapp.epoc.utils.Utils


class MainActivity : AppCompatActivity(), OnUpdateFragment, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private var viewPager: ViewPager? = null
    //TODO:put 3 variables in static Constant class
    private var adapter: ExerciseListPagerAdapter? = null
    private var tabLayout: TabLayout? = null
    //variables
    private val fabExerciseActivity: FloatingActionButton? = null
    private var tabIcons: Array<Drawable>? = null
    private var tabNames: Array<String>? = null

    //General
    private val TAG = this.javaClass.getName()
    //Map manipulation
    private val mShowMap: Int = 0
    //Float Button
    private var toolbar: Toolbar? = null
    private var fab: FloatingActionButton? = null
    private var fabChildAnnotation: FloatingActionButton? = null
    private var fabChildActivity: FloatingActionButton? = null
    private val fabChildFilter: FloatingActionButton? = null
    private var fabChildNewPlace: FloatingActionButton? = null
    private var textChildAnnotation: TextView? = null
    private var textChildActivity: TextView? = null
    private val textChildFilter: TextView? = null
    private var textChildNewPlace: TextView? = null
    private var isFabOpen: Boolean? = false
    private var an_fab_open: Animation? = null
    private var an_fab_close: Animation? = null
    private var an_rotate_forward: Animation? = null
    private var an_rotate_backward: Animation? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    //get location
    private var mContext: Context? = null
    internal var isGPSEnabled = false    // flag for GPS status
    internal var isNetworkEnabled = false    // flag for network status
    internal var canGetLocation = false
    protected var locationManager: LocationManager? = null                      // Declaring a Location Manager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        mContext = applicationContext
        //Toolbar definitions
        toolbar = findViewById(R.id.toolbar_main_activity2) as Toolbar
        setSupportActionBar(toolbar)
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
        }
        getLocation()
        //Float Button
        startFloatButton()
        //Draw Navigation View
        startNavigationView()
        setupTabs()
    }

    private fun setupTabs() {
        //setup toolbar
        tabNames = arrayOf(getString(R.string.ExerciseListTabResume), getString(R.string.ExerciseListTabRecommend), getString(R.string.ExerciseListTabMyExercise), getString(R.string.ExerciseListTabTimeline))
        tabIcons = arrayOf(resources.getDrawable(R.drawable.ic_assessment_black_24dp), resources.getDrawable(R.drawable.ic_import_export_white_24dp), resources.getDrawable(R.drawable.ic_view_list_white_24dp), resources.getDrawable(R.drawable.ic_timeline_black_24dp))
        //tab setup
        tabIcons!![0].setColorFilter(resources.getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
        tabIcons!![1].setColorFilter(resources.getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
        tabIcons!![2].setColorFilter(resources.getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
        tabIcons!![3].setColorFilter(resources.getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
        tabLayout = findViewById(R.id.tab_list_exercise_layout2) as TabLayout
        tabLayout!!.addTab(tabLayout!!.newTab().setIcon(tabIcons!![0]))       //ExerciseListTabResume
        tabLayout!!.addTab(tabLayout!!.newTab().setIcon(tabIcons!![1]))       //ExerciseListTabRecommend
        tabLayout!!.addTab(tabLayout!!.newTab().setIcon(tabIcons!![2]))       //ExerciseListTabMyExercise
        tabLayout!!.addTab(tabLayout!!.newTab().setIcon(tabIcons!![3]))       //ExerciseListTabFavorites
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
        viewPager = findViewById(R.id.activity_list_exercise_viewpager) as ViewPager
        adapter = ExerciseListPagerAdapter(supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        supportActionBar!!.setTitle(tabNames!![viewPager!!.currentItem])
        tabIcons!![viewPager!!.currentItem].setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP)
        tabLayout!!.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
                supportActionBar!!.setTitle(tabNames!![tab.position])
                tabLayout!!.getTabAt(tab.position)
                tabIcons!![0].setColorFilter(resources.getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
                tabIcons!![1].setColorFilter(resources.getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
                tabIcons!![2].setColorFilter(resources.getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
                tabIcons!![3].setColorFilter(resources.getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
                tabIcons!![viewPager!!.currentItem].setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun startNavigationView() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun startFloatButton() {
        fab = findViewById(R.id.fabExerciseActivity2) as FloatingActionButton
        fabChildAnnotation = findViewById(R.id.fabChildActivity2) as FloatingActionButton
        fabChildActivity = findViewById(R.id.fabChildAnnotation2) as FloatingActionButton
        //fabChildFilter = (FloatingActionButton) findViewById(R.id.fabChildFilter2);
        fabChildNewPlace = findViewById(R.id.fabChildNewPlace2) as FloatingActionButton
        textChildAnnotation = findViewById(R.id.textChildAnnotation2) as TextView
        textChildActivity = findViewById(R.id.textChildActivity2) as TextView
        //textChildFilter = (TextView) findViewById(R.id.textChildFilter2);
        textChildNewPlace = findViewById(R.id.textChildNewPlace2) as TextView
        an_fab_open = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
        an_fab_close = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)
        an_rotate_forward = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_forward)
        an_rotate_backward = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_backward)
        fab!!.setOnClickListener(this)
        //fabChildNewPlace.setOnClickListener(this);
        //fabChildFilter.setOnClickListener(this);
        fabChildAnnotation!!.setOnClickListener(this)
        fabChildActivity!!.setOnClickListener(this)
        //textChildNewPlace.setOnClickListener(this);
        //textChildFilter.setOnClickListener(this);
        textChildAnnotation!!.setOnClickListener(this)
        textChildActivity!!.setOnClickListener(this)
    }

    private fun animateFAB() {
        if (isFabOpen!!) {
            fab!!.startAnimation(an_rotate_backward)
            textChildActivity!!.startAnimation(an_fab_close)
            textChildAnnotation!!.startAnimation(an_fab_close)
            fabChildActivity!!.startAnimation(an_fab_close)
            fabChildAnnotation!!.startAnimation(an_fab_close)
            textChildActivity!!.isClickable = false
            textChildAnnotation!!.isClickable = false
            fabChildActivity!!.isClickable = false
            fabChildAnnotation!!.isClickable = false
            isFabOpen = false
            Log.d(TAG, "close")
        } else {
            fab!!.startAnimation(an_rotate_forward)
            textChildActivity!!.startAnimation(an_fab_open)
            textChildAnnotation!!.startAnimation(an_fab_open)
            fabChildActivity!!.startAnimation(an_fab_open)
            fabChildAnnotation!!.startAnimation(an_fab_open)
            textChildActivity!!.isClickable = true
            textChildAnnotation!!.isClickable = true
            fabChildActivity!!.isClickable = true
            fabChildAnnotation!!.isClickable = true
            isFabOpen = true
            Log.d(TAG, "open")
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.map, menu);
        //return true;
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        val i: Intent
        /**
         * if (id == R.id.nav_profile) {
         * // Handle the camera action
         * } else
         */
        if (id == R.id.nav_favorite) {
            i = Intent(this, ExerciseFavorite::class.java)
            startActivityForResult(i, Constants.EXERCISEREQCODEACTIVITY)
        } else if (id == R.id.nav_map) {
            i = Intent(applicationContext, MapActivity::class.java)
            startActivity(i)
        } else if (id == R.id.nav_chat) {
            i = Intent(applicationContext, MainWindow::class.java)
            startActivity(i)
        }
        /**
         * else if (id == R.id.nav_about) {

         * } else if (id == R.id.nav_helpfeedback) {

         * }
         */
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun updateFragments() {
        val currentTab = viewPager!!.currentItem
        adapter!!.notifyDataSetChanged()
        //adapter = new ExerciseListPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager!!.adapter = adapter
        viewPager!!.currentItem = currentTab
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == Constants.EXERCISEREQCODEACTIVITY) {
            // Make sure the request was successful
            if (resultCode == android.app.Activity.RESULT_OK) {
                updateFragments()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun OnResume() {
        super.onResume()
        updateFragments()
    }

    override fun onClick(v: View) {
        val id = v.id
        val i: Intent
        when (id) {
            R.id.fabExerciseActivity2 -> animateFAB()
        /**
         * case R.id.textChildNewPlace2:
         * case R.id.fabChildNewPlace2:
         * i = new Intent(this, NewLocationActivity.class);
         * startActivityForResult(i, Constants.EXERCISEREQCODEACTIVITY);
         * animateFAB();
         * Log.d(TAG, "FAB button, New Place");
         * break;
         */
            R.id.textChildActivity2, R.id.fabChildActivity2 -> {
                //Utils.updateAddress(this,mMap);
                getLocation()
                i = Intent(this, ExerciseEditActivity::class.java)
                startActivityForResult(i, Constants.EXERCISEREQCODEACTIVITY)
                animateFAB()
                Log.d(TAG, "FAB button, New Activity")
            }
            R.id.textChildAnnotation2, R.id.fabChildAnnotation2 -> {
                //Utils.updateAddress(this,mMap);
                getLocation()
                i = Intent(this, AssignTimelineAnnotationActivity::class.java)
                startActivityForResult(i, Constants.EXERCISEREQCODEACTIVITY)
                animateFAB()
                Log.d(TAG, "FAB button, New Annotation")
            }
            else -> animateFAB()
        }
    }

    override fun onConnected(connectionHint: Bundle?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
        }
        val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient)
        if (mLastLocation != null) {
            CacheMainActivity.DirectionLastKnowLocation = mLastLocation
        }
    }

    fun getLocation() {
        var location: Location? = null // location
        var latitude = 0.0 // latitude
        var longitude = 0.0 // longitude
        try {
            locationManager = mContext!!
                    .getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // getting GPS status
            isGPSEnabled = locationManager!!
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)

            // getting network status
            isNetworkEnabled = locationManager!!
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true
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
                        return
                    }
                    locationManager!!.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                    Log.d("Network", "Network")
                    if (locationManager != null) {
                        location = locationManager!!
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null) {
                            latitude = location.latitude
                            longitude = location.longitude
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager!!.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                        Log.d("GPS Enabled", "GPS Enabled")
                        if (locationManager != null) {
                            location = locationManager!!
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (location != null) {
                                latitude = location.latitude
                                longitude = location.longitude
                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
        }
        if (location != null && mGoogleApiClient != null) {
            Utils.updateAddress(mContext, location, mGoogleApiClient)
        }
    }

    override fun onLocationChanged(location: Location) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

    override fun onConnectionSuspended(i: Int) {

    }

    override fun onStart() {
        mGoogleApiClient!!.connect()
        super.onStart()
    }

    override fun onStop() {
        mGoogleApiClient!!.disconnect()
        super.onStop()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    companion object {
        private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters     // The minimum distance to change Updates in meters
        private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()  // 1 minute      // The minimum time between updates in milliseconds
    }
}
