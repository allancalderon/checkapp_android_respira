package mobi.checkapp.epoc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import mobi.checkapp.epoc.controller.VolleyControler;
import mobi.checkapp.epoc.utils.Constants;

public class ContactListActivity extends AppCompatActivity {

    //general
    private final String TAG = this.getClass().getName();
    private FloatingActionButton fabShareActivity;
    //volley
    private String urlJsonArry;  // json array response url
    private String jsonResponse;    // temporary string to show the parsed response
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        fabShareActivity = (FloatingActionButton) findViewById(R.id.fabShareActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("List of contacts");
        fabShareActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent i = new Intent(getApplicationContext(), ContactActivity.class);
                startActivity(i);
            }
        });

        getPollution();
    }

    public void getPollution() {
        urlJsonArry = Constants.URL_POLLUTION+"?type=1&id=ESPMAD&complexity=0";
        t1 = (TextView) findViewById(R.id.t1);
        makeJsonObjectRequest(urlJsonArry);
    }

    private void makeJsonObjectRequest(String getParams) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                getParams, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());
                try {
                    // Parsing json array response
                    // loop through each json object
                    jsonResponse = "";
                    JSONArray sensors = response.getJSONArray("sensors");
                    String error = response.getString("error");
                    if(!error.equals("true")){
                        for(int contSensors = 0; contSensors < sensors.length() ;contSensors++){
                            JSONObject arraySensors = (JSONObject) sensors.get(contSensors);
                            String idsensor = arraySensors.getString("idsensor");
                            JSONArray pollutants = arraySensors.getJSONArray("pollutants");
                            for(int contPollutants = 0; contPollutants < pollutants.length() ;contPollutants++){
                                JSONObject pollutantsObject = (JSONObject) pollutants.get(contPollutants);
                                String idpollutantglobal = pollutantsObject.getString("idpollutantglobal");
                                String year = pollutantsObject.getString("year");
                                String month = pollutantsObject.getString("month");
                                String day = pollutantsObject.getString("day");
                                String hour0 = pollutantsObject.getString("hour0");
                                Integer hour0val = pollutantsObject.getInt("hour0val");
                                jsonResponse += "error: " + error + "\n";
                                jsonResponse += "idsensor: " + idsensor + "\n";
                                jsonResponse += "idpollutantglobal: " + idpollutantglobal + "\n";
                                jsonResponse += "year: " + year + "\n";
                                jsonResponse += "month: " + month + "\n";
                                jsonResponse += "day: " + day + "\n";
                                jsonResponse += "hour0: " + hour0 + "\n";
                                jsonResponse += "hour0val: " + hour0val + "\n\n";
                            }
                        }
                    }
                    t1.setText(jsonResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
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
        if(jsonObjReq != null)
            VolleyControler.getInstance().addToRequestQueue(jsonObjReq);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Log.d(TAG, "Back button clicked");
            super.onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
