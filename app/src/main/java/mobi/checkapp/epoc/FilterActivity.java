package mobi.checkapp.epoc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import mobi.checkapp.epoc.utils.CacheMainActivity;

public class FilterActivity extends Activity {
    private final String TAG = this.getClass().getName();
    public CheckBox checkBoxFilterSensors;
    public CheckBox checkBoxFilterMyPlaces;
    public CheckBox checkBoxFilterTemperature;
    public CheckBox checkBoxFilterParks;
    public CheckBox checkBoxFilterGyms;
    public CheckBox checkBoxFilterRunningPlaces;
    public CheckBox checkBoxFilterBycicles;
    public CheckBox checkBoxFilterBars;
    public CheckBox checkBoxFilterHealth;
    public CheckBox checkBoxFilterHospitalEsp;
    public CheckBox checkBoxFilterClinics;
    public CheckBox checkBoxFilterHospital;
    public CheckBox checkBoxFilterOtherHealth;
    public CheckBox checkBoxFilterAnotherPlaces;
    public Button btnFilterSave;
    public Button btnFilterCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filter);
        /*
        checkBoxFilterSensors;
        checkBoxFilterMyPlaces;
        checkBoxFilterTemperature;
        checkBoxFilterParks;
        checkBoxFilterGyms;
        checkBoxFilterRunningPlaces;
        checkBoxFilterBycicles;
        checkBoxFilterBars;
        checkBoxFilterHealth;
        checkBoxFilterHospitalEsp;
        checkBoxFilterClinics;
        checkBoxFilterHospital;
        checkBoxFilterOtherHealth;
        checkBoxFilterAnotherPlaces;
        */

        checkBoxFilterSensors = (CheckBox)findViewById(R.id.checkBoxFilterSensors);
        checkBoxFilterMyPlaces = (CheckBox)findViewById(R.id.checkBoxFilterMyPlaces);
        checkBoxFilterTemperature = (CheckBox)findViewById(R.id.checkBoxFilterTemperature);
        checkBoxFilterParks = (CheckBox)findViewById(R.id.checkBoxFilterParks);
        checkBoxFilterGyms = (CheckBox)findViewById(R.id.checkBoxFilterGyms);
        checkBoxFilterRunningPlaces = (CheckBox)findViewById(R.id.checkBoxFilterRunningPlaces);
        checkBoxFilterBycicles = (CheckBox)findViewById(R.id.checkBoxFilterBycicles);
        checkBoxFilterBars = (CheckBox)findViewById(R.id.checkBoxFilterBars);
        checkBoxFilterHealth = (CheckBox)findViewById(R.id.checkBoxFilterHealth);
        checkBoxFilterHospitalEsp = (CheckBox)findViewById(R.id.checkBoxFilterHospitalEsp);
        checkBoxFilterClinics = (CheckBox)findViewById(R.id.checkBoxFilterClinics);
        checkBoxFilterHospital = (CheckBox)findViewById(R.id.checkBoxFilterHospital);
        checkBoxFilterOtherHealth = (CheckBox)findViewById(R.id.checkBoxFilterOtherHealth);
        checkBoxFilterAnotherPlaces = (CheckBox)findViewById(R.id.checkBoxFilterAnotherPlaces);
        btnFilterCancel = (Button)findViewById(R.id.btnFilterCancel);
        btnFilterSave = (Button)findViewById(R.id.btnFilterSave);


        String[]parts = CacheMainActivity.CheckboxState.split("-");
        int i = 0;

        for(String s: parts){
            //Toast.makeText(FilterActivity.this, s, Toast.LENGTH_SHORT).show();

            if(!Boolean.valueOf(s)){
                //Toast.makeText(FilterActivity.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                switch (i){
                    case 0:
                        checkBoxFilterSensors.setChecked(false);
                        break;
                    case 1:
                        checkBoxFilterMyPlaces.setChecked(false);
                        break;
                    case 2:
                        checkBoxFilterTemperature.setChecked(false);
                        break;
                    case 3:
                        checkBoxFilterParks.setChecked(false);
                        break;
                    case 4:
                        checkBoxFilterGyms.setChecked(false);
                        break;
                    case 5:
                        checkBoxFilterRunningPlaces.setChecked(false);
                        break;
                    case 6:
                        checkBoxFilterBycicles.setChecked(false);
                        break;
                    case 7:
                        checkBoxFilterBars.setChecked(false);
                        break;
                    case 8:
                        checkBoxFilterHealth.setChecked(false);
                        break;
                    case 9:
                        checkBoxFilterHospitalEsp.setChecked(false);
                        break;
                    case 10:
                        checkBoxFilterClinics.setChecked(false);
                        break;
                    case 11:
                        checkBoxFilterHospital.setChecked(false);
                        break;
                    case 12:
                        checkBoxFilterOtherHealth.setChecked(false);
                        break;
                    case 13:
                        checkBoxFilterAnotherPlaces.setChecked(false);
                        break;

                    default:break;
                }

            }else{
                switch (i){
                    case 0:
                        checkBoxFilterSensors.setChecked(true);
                        break;
                    case 1:
                        checkBoxFilterMyPlaces.setChecked(true);
                        break;
                    case 2:
                        checkBoxFilterTemperature.setChecked(true);
                        break;
                    case 3:
                        checkBoxFilterParks.setChecked(true);
                        break;
                    case 4:
                        checkBoxFilterGyms.setChecked(true);
                        break;
                    case 5:
                        checkBoxFilterRunningPlaces.setChecked(true);
                        break;
                    case 6:
                        checkBoxFilterBycicles.setChecked(true);
                        break;
                    case 7:
                        checkBoxFilterBars.setChecked(true);
                        break;
                    case 8:
                        checkBoxFilterHealth.setChecked(true);
                        break;
                    case 9:
                        checkBoxFilterHospitalEsp.setChecked(true);
                        break;
                    case 10:
                        checkBoxFilterClinics.setChecked(true);
                        break;
                    case 11:
                        checkBoxFilterHospital.setChecked(true);
                        break;
                    case 12:
                        checkBoxFilterOtherHealth.setChecked(true);
                        break;
                    case 13:
                        checkBoxFilterAnotherPlaces.setChecked(true);
                        break;

                    default:break;
                }
            }

            i++;
        }


        btnFilterCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        btnFilterSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CacheMainActivity.CheckboxState =
                        String.valueOf(checkBoxFilterSensors.isChecked()) +"-"+
                        String.valueOf(checkBoxFilterMyPlaces.isChecked()) + "-"+
                        String.valueOf(checkBoxFilterTemperature.isChecked()) +"-"+
                        String.valueOf(checkBoxFilterParks.isChecked()) +"-"+
                        String.valueOf(checkBoxFilterGyms.isChecked()) +"-"+
                        String.valueOf(checkBoxFilterRunningPlaces.isChecked()) +"-"+
                        String.valueOf(checkBoxFilterBycicles.isChecked()) +"-"+
                        String.valueOf(checkBoxFilterBars.isChecked()) +"-"+
                        String.valueOf(checkBoxFilterHealth.isChecked()) +"-"+
                        String.valueOf(checkBoxFilterHospitalEsp.isChecked())+"-"+
                        String.valueOf(checkBoxFilterClinics.isChecked())+"-"+
                        String.valueOf(checkBoxFilterHospital.isChecked())+"-"+
                        String.valueOf(checkBoxFilterOtherHealth.isChecked())+"-"+
                        String.valueOf(checkBoxFilterAnotherPlaces.isChecked());

                //TODO incluir en bd
                //true-true-true-true-true-true-true-true-true-true-true-true-true-true
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
