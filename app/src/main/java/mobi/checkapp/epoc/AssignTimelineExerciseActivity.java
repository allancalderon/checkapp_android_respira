package mobi.checkapp.epoc;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Calendar;

import mobi.checkapp.epoc.db.DbFunctions;
import mobi.checkapp.epoc.db.activitiesDbHelper;
import mobi.checkapp.epoc.entities.Exercises;
import mobi.checkapp.epoc.entities.TimelineAssign;
import mobi.checkapp.epoc.utils.CacheMainActivity;
import mobi.checkapp.epoc.utils.Constants;

public class AssignTimelineExerciseActivity extends Activity {
    private final String TAG = this.getClass().getName();
    private String[] minuteValues;
    private NumberPicker pickerAssignExerciseMinute;
    private NumberPicker pickerAssignExerciseHour;
    private Button btnAssignExerciseSave, btnAssignExerciseCancel;
    private TextView textAssignExerciseLocation;
    private DbFunctions dbFunctions = null;
    private Exercises exercises = null;
    private TimelineAssign timelineAssign = null;
    private activitiesDbHelper mDbHelper;
    private SQLiteDatabase db;
    private Intent returnIntent;
    private Bundle extra;
    private TextView editTextAssignExerciseDescription;
    private long resultDb;
    private CheckBox checkBoxAssignExerciseTime, checkBoxAssignExerciseDate;
    private TextView textAssignExerciseDate, textAssignExerciseTime;
    private int year, month, day, hour, min, timezone;
    private Calendar calendar;
    private boolean isUpdate;
    private int IDTYPEASSIGMENT = 1;            //1 for exercise and 2 for annotation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_exercise);
        //setup
        dbFunctions = new DbFunctions(this);
        pickerAssignExerciseMinute = (NumberPicker) findViewById(R.id.pickerAssignExerciseMinute);
        pickerAssignExerciseHour = (NumberPicker) findViewById(R.id.pickerAssignExerciseHour);
        btnAssignExerciseSave = (Button) findViewById(R.id.btnAssignExerciseSave);
        btnAssignExerciseCancel = (Button) findViewById(R.id.btnAssignExerciseCancel);
        textAssignExerciseLocation = (TextView) findViewById(R.id.textAssignExerciseLocation);
        editTextAssignExerciseDescription = (TextView) findViewById(R.id.editTextAssignExerciseDescription);
        textAssignExerciseDate = (TextView) findViewById(R.id.textAssignExerciseDate);
        textAssignExerciseTime = (TextView) findViewById(R.id.textAssignExerciseTime);
        checkBoxAssignExerciseDate = (CheckBox) findViewById(R.id.checkBoxAssignExerciseDate);
        checkBoxAssignExerciseTime = (CheckBox) findViewById(R.id.checkBoxAssignExerciseTime);
        //minutes
        minuteValues = new String[12];
        for (int i = 0; i < minuteValues.length; i++) {
            String number = Integer.toString(i*5);
            minuteValues[i] = number.length() < 2 ? "0" + number : number;
        }
        pickerAssignExerciseHour.setMinValue(0);
        pickerAssignExerciseHour.setMaxValue(24); //Specify the maximum value/number of NumberPicker
        pickerAssignExerciseMinute.setMinValue(0);
        pickerAssignExerciseMinute.setMaxValue(minuteValues.length-1); //Specify the maximum value/number of NumberPicker
        pickerAssignExerciseMinute.setDisplayedValues(minuteValues);
        //Wheel
        pickerAssignExerciseHour.setWrapSelectorWheel(true);
        pickerAssignExerciseMinute.setWrapSelectorWheel(true);
        //db setup
        mDbHelper = new activitiesDbHelper(this);
        db = mDbHelper.getWritableDatabase();     // Gets the data repository in write mode
        //get information
        returnIntent = new Intent();
        extra = this.getIntent().getBundleExtra(Constants.LISTEXERCISEBUNDLE);
        if(extra != null) {
            exercises = (Exercises) extra.getSerializable(Constants.LISTEXERCISEDATA);
            timelineAssign = (TimelineAssign) extra.getSerializable(Constants.LISTTIMELINEASSIGNDATA);
            //setup variables
        }
        if(timelineAssign != null){
            calendar = Calendar.getInstance();
            year = timelineAssign.getYear();
            month = timelineAssign.getMonth();
            day = timelineAssign.getDay();
            hour = timelineAssign.getHour();
            min = timelineAssign.getMinute();
            timezone = timelineAssign.getGmtOffset();
            double timeDuration = timelineAssign.getTimeDuration();
            pickerAssignExerciseHour.setValue((int) timeDuration/60);
            pickerAssignExerciseMinute.setValue((int) (timeDuration%60)/5);
            editTextAssignExerciseDescription.setText(timelineAssign.getDescription());
            isUpdate = true;
        }else{
            timelineAssign = new TimelineAssign();
            //setup date, hour and location
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            min = calendar.get(Calendar.MINUTE);
            timezone = calendar.get(Calendar.ZONE_OFFSET);
            isUpdate = false;
        }
        textAssignExerciseDate.setText(String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year) );
        textAssignExerciseTime.setText(hour+"h "+min+"min");
        textAssignExerciseLocation.setText(CacheMainActivity.Direction);
        //set actions
        btnAssignExerciseSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(exercises != null) {
                    timelineAssign.setIdExerciseFK(exercises.getIdExercises());
                }
                timelineAssign.setDescription(String.valueOf(editTextAssignExerciseDescription.getText()));
                timelineAssign.setTimeDuration(pickerAssignExerciseHour.getValue()*60+Integer.valueOf(pickerAssignExerciseHour.getValue()));
                timelineAssign.setIdTypeAssignment(IDTYPEASSIGMENT);      //1 for exercise and 2 for annotation
                //Get date
                timelineAssign.setDay(day);
                timelineAssign.setMonth(month);
                timelineAssign.setYear(year);
                //get time
                timelineAssign.setHour(hour);
                timelineAssign.setMinute(min);
                timelineAssign.setGmtOffset(timezone);
                if(CacheMainActivity.DirectionLatLng != null) {
                    timelineAssign.setLatitude(CacheMainActivity.DirectionLatLng.latitude);
                    timelineAssign.setLongitude(CacheMainActivity.DirectionLatLng.longitude);
                }
                if (isUpdate) {
                    resultDb = dbFunctions.updateTimelineAssign(timelineAssign);
                }else {
                    resultDb = dbFunctions.addTimelineAssign(timelineAssign);
                    timelineAssign.setIdAssignedTimeline(resultDb);
                }
                returnIntent.putExtra(Constants.TIMELINEASSIGNDATA, timelineAssign);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        //set actions
        btnAssignExerciseCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED, returnIntent);
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
