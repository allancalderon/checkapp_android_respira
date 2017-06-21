package mobi.checkapp.epoc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mobi.checkapp.epoc.entities.MyItem;
import mobi.checkapp.epoc.utils.CacheMainActivity;

/**
 * Created by luisbanon on 27/05/16.
 */
public class NewLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap = null;
    private int mShowMap;
    private boolean isMapSetup;
    public LinearLayout lnCreate;
    public TextView txtAddress;
    public Button btnCreate;
    public LatLng myCurrentLocation;
    public EditText edtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_location_layout);
        mShowMap = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());
        isMapSetup = startMap();
        lnCreate = (LinearLayout)findViewById(R.id.lnCreate);
        txtAddress = (TextView)findViewById(R.id.txtAddress);
        btnCreate = (Button)findViewById(R.id.btnCreate);
        edtName = (EditText)findViewById(R.id.edtName);

    }
    private boolean startMap() {
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                    findFragmentById(R.id.map);
            mapFragment.getMapAsync(NewLocationActivity.this);
        }
        return (mMap != null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();
        mMap = googleMap;
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                int REQUEST_LOCATION = 2;
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                } else {
                    ActivityCompat.requestPermissions(
                            this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION);
                }
            } else {

                GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .enableAutoManage(this /* FragmentActivity */,
                                this /* OnConnectionFailedListener */)
                        .addApi(Drive.API)
                        .addScope(Drive.SCOPE_FILE)
                        .build();
                Location myLocation =
                        LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }
        }

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(NewLocationActivity.this, "Allan is cabrito", Toast.LENGTH_SHORT).show();

                MyItem myItem = new MyItem();
                myItem.setAddressName(txtAddress.getText().toString());
                ArrayList<Integer>lista = new ArrayList<Integer>();
                lista.add(6);
                myItem.setTypes((List)lista);
                myItem.setPosition(myCurrentLocation);
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                myItem.setDateTime(df.format(new Date()));
                myItem.setName(edtName.getText().toString());

                CacheMainActivity.myItems.add(myItem);
                finish();
            }
        });
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                myCurrentLocation = latLng;

                List<Address> addresses = null;

                Geocoder geocoder = new Geocoder(NewLocationActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(
                            latLng.latitude,
                            latLng.longitude,

                            1);
                } catch (Exception e) {
                    //gg
                }

                if (addresses == null || addresses.size()  == 0) {
                    //gg
                } else {
                    Address address = addresses.get(0);
                    ArrayList<String> addressFragments = new ArrayList<String>();
                    for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        addressFragments.add(address.getAddressLine(i));
                    }

                    String direction = "";
                    for(String s:addressFragments){
                        direction += s +"\n";
                    }

                    lnCreate.setVisibility(View.VISIBLE);
                    txtAddress.setText(direction);

                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.mymarker))
                            .position(latLng).draggable(true));
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                    mMap.animateCamera(yourLocation);

                    mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker arg0) {
                        }

                        @SuppressWarnings("unchecked")
                        @Override
                        public void onMarkerDragEnd(Marker arg0) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));


                            myCurrentLocation = new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude);

                            List<Address> addresses = null;

                            Geocoder geocoder = new Geocoder(NewLocationActivity.this, Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(
                                        arg0.getPosition().latitude,
                                        arg0.getPosition().longitude,
                                        1);
                            } catch (Exception e) {
                                //gg
                            }

                            if (addresses == null || addresses.size()  == 0) {
                                lnCreate.setVisibility(View.GONE);
                            } else {
                                Address address = addresses.get(0);
                                ArrayList<String> addressFragments = new ArrayList<String>();
                                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                    addressFragments.add(address.getAddressLine(i));
                                }

                                String direction = "";
                                for (String s : addressFragments) {
                                    direction += s + "\n";
                                }

                                lnCreate.setVisibility(View.VISIBLE);
                                txtAddress.setText(direction);
                            }
                        }

                        @Override
                        public void onMarkerDrag(Marker arg0) {

                        }
                    });

                }
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(NewLocationActivity.this, connectionResult.toString(), Toast.LENGTH_SHORT).show();
    }
}
