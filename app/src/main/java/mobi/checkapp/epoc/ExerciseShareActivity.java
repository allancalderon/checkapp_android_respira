package mobi.checkapp.epoc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mobi.checkapp.epoc.entities.Exercises;
import mobi.checkapp.epoc.utils.Constants;

/**
 * Created by allancalderon on 26/07/16.
 */
public class ExerciseShareActivity  extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    //activitiesDB definitions
    private Exercises exercises = null;
    //extra
    private Bundle extra;
    Intent returnIntent;
    //define variables names
    private Button btnShareExerciseOk;
    private Button btnShareExerciseCancel;
    private EditText textShareExerciseLabelDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_exercise);
        //variables setup
        btnShareExerciseOk = (Button) findViewById(R.id.btnShareExerciseOk);
        btnShareExerciseCancel = (Button) findViewById(R.id.btnShareExerciseCancel);
        textShareExerciseLabelDescription = (EditText) findViewById(R.id.textShareExerciseLabelDescription);
        //get information
        returnIntent = new Intent();
        extra = this.getIntent().getBundleExtra(Constants.LISTEXERCISEBUNDLE);
        if(extra != null) {
            exercises = (Exercises) extra.getSerializable(Constants.LISTEXERCISEDATA);
        }
        //fill information
        if(exercises == null) {exercises = new Exercises();}
        else{
            //put variables data
            textShareExerciseLabelDescription.setText("[CheckApp] Hice el ejercício '" +
                    exercises.getTitle() +
                    "' para mejorar mi capacidad respiratória!");
        }
        //set actions
        btnShareExerciseOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set intent
                //returnIntent = new Intent(getApplicationContext(), ExerciseEditActivity.class);
                returnIntent = new Intent();
                int result = 0;
                if(exercises != null) {
                    //TODO: put share content
                    Intent sharingIntent = new Intent();
                    sharingIntent.setAction(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "CheckApp");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, textShareExerciseLabelDescription.getText().toString());
                    startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.exerciseShare_title)));
                    //Utils.share(textShareExerciseLabelDescription.getText().toString(),getApplicationContext());
                    setResult(Activity.RESULT_OK, returnIntent);
                }
                if (result == 0 || exercises == null){
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                }
                finish();
            }
        });
        //set actions
        btnShareExerciseCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Log.d(TAG, "Back button clicked");
            super.onBackPressed();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }


}