package mobi.checkapp.epoc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mobi.checkapp.epoc.db.DbFunctions;
import mobi.checkapp.epoc.entities.Exercises;
import mobi.checkapp.epoc.utils.Constants;

public class ExerciseDeleteActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    //activitiesDB definitions
    private DbFunctions dbFunctions;
    private long resultDb;
    private Exercises exercises = null;
    //extra
    private Bundle extra;
    Intent returnIntent;
    //define variables names
    private Button btnDeleteExerciseOk;
    private Button btnDeleteExerciseCancel;
    private TextView textDeleteExerciseLabelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_exercise);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_list_exercise_activity);
        //setSupportActionBar(toolbar);
        dbFunctions = new DbFunctions(this);
        //variables setup
        btnDeleteExerciseOk = (Button) findViewById(R.id.btnDeleteExerciseOk);
        btnDeleteExerciseCancel = (Button) findViewById(R.id.btnDeleteExerciseCancel);
        textDeleteExerciseLabelName = (TextView) findViewById(R.id.textDeleteExerciseLabelName);
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
            textDeleteExerciseLabelName.setText("¿Confirma que desea eliminar el ejercício '"+ exercises.getTitle() +"'?");
        }
        //set actions
        btnDeleteExerciseOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set intent
                //returnIntent = new Intent(getApplicationContext(), ExerciseEditActivity.class);
                returnIntent = new Intent();
                int result = 0;
                if(exercises != null) {
                    //TODO: change deleteExercise item. Instead of deleteExercise, hide
                    result = dbFunctions.deleteExercise(exercises);
                    if(result > 0) {
                        returnIntent.putExtra(Constants.EXERCISEDATA, exercises);
                        setResult(Activity.RESULT_OK, returnIntent);
                    }
                }
                if (result == 0 || exercises == null){
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                }
                finish();
            }
        });
        //set actions
        btnDeleteExerciseCancel.setOnClickListener(new View.OnClickListener() {
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
