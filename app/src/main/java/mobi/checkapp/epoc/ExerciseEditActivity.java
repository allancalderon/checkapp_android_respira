package mobi.checkapp.epoc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import mobi.checkapp.epoc.db.DbFunctions;
import mobi.checkapp.epoc.entities.Exercises;
import mobi.checkapp.epoc.entities.GroupExercise;
import mobi.checkapp.epoc.utils.CacheMainActivity;
import mobi.checkapp.epoc.utils.Constants;
import mobi.checkapp.epoc.utils.Utils;

public class ExerciseEditActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    //activitiesDB definitions
    private DbFunctions dbFunctions;
    private long resultDb;
    private Exercises exercises = null;
    //extra
    private Bundle extra;
    Intent returnIntent;
    //define variables names
    private Button btnEditExerciseSave;
    private Button btnEditExerciseCancel;
    private TextView editTextEditExerciseName;
    private TextView editTextEditExerciseDescription;
    private LinearLayout linearLayoutEditExerciseMoreInfo;
    private LinearLayout linearLayoutEditExerciseMoreOptions;
    private int iconId;
    private int colorId;
    private Drawable iconDrawable;
    private ImageView imageEditExerciseExpand;
    private NumberPicker pickerEditExerciseMinute;
    private NumberPicker pickerEditExerciseHour;

    private String[] minuteValues;
    int generalGroup = 1;  //Start group as general <group value="1"><name value="General"/></group>
    private int iconIdAssign;
    private int colorIdAssign;
    private LinearLayout linearLayoutEditExerciseExerciseOptions;
    //spinners
    private Spinner spinnerEditExerciseGroup, spinnerEditExerciseSubGroup, spinnerEditExerciseType;
    private List<GroupExercise> listGroupExercise, listSubGroupExercise, listTypeExercise;
    private ArrayAdapter<GroupExercise> adapterSpinnerGroup, adapterSpinnerSubGroup, adapterSpinnerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exercise);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_list_exercise_activity);
        //setSupportActionBar(toolbar);
        dbFunctions = new DbFunctions(this);
        //variables setup
        btnEditExerciseSave = (Button) findViewById(R.id.btnEditExerciseSave);
        btnEditExerciseCancel = (Button) findViewById(R.id.btnEditExerciseCancel);
        editTextEditExerciseName = (TextView) findViewById(R.id.editTextEditExerciseName);
        editTextEditExerciseDescription = (TextView) findViewById(R.id.editTextEditExerciseDescription);
        linearLayoutEditExerciseMoreInfo = (LinearLayout) findViewById(R.id.linearLayoutEditExerciseMoreInfo);
        linearLayoutEditExerciseMoreOptions = (LinearLayout) findViewById(R.id.linearLayoutEditExerciseMoreOptions);
        imageEditExerciseExpand = (ImageView) findViewById(R.id.imageEditExerciseExpand);
        pickerEditExerciseMinute = (NumberPicker) findViewById(R.id.pickerEditExerciseMinute);
        pickerEditExerciseHour = (NumberPicker) findViewById(R.id.pickerEditExerciseHour);
        linearLayoutEditExerciseExerciseOptions = (LinearLayout) findViewById(R.id.linearLayoutEditExerciseExerciseOptions);

        //get information
        returnIntent = new Intent();
        extra = this.getIntent().getBundleExtra(Constants.LISTEXERCISEBUNDLE);
        if(extra != null) {
            exercises = (Exercises) extra.getSerializable(Constants.LISTEXERCISEDATA);
        }
        loadSpinners();
        //fill information
        if(exercises == null) {
            exercises = new Exercises();
            exercises.setSourceType(Constants.SOURCERECOMMENDATEDEJERCISEUSER);
        }
        else{
            //put variables data
            editTextEditExerciseName.setText(exercises.getTitle());
            editTextEditExerciseDescription.setText(exercises.getDescription());
            int total;
            //set selection of Group
            total = spinnerEditExerciseGroup.getAdapter().getCount();
            for(int i = 0; i < total; i++) {
                GroupExercise tmp = (GroupExercise) spinnerEditExerciseGroup.getItemAtPosition(i);
                if(tmp.getId() == exercises.getIdGroupFK()) {
                    spinnerEditExerciseGroup.setSelection(i);
                    break;
                }
            }
            //set selection of SubGroup
            total = spinnerEditExerciseSubGroup.getAdapter().getCount();
            for(int i = 0; i < total; i++) {
                GroupExercise tmp = (GroupExercise) spinnerEditExerciseSubGroup.getItemAtPosition(i);
                if(tmp.getId() == exercises.getIdSubGroupFK()) {
                    spinnerEditExerciseSubGroup.setSelection(i);
                    break;
                }
            }
            //set selection of type of exercise
            total = spinnerEditExerciseType.getAdapter().getCount();
            for(int i = 0; i < total; i++) {
                GroupExercise tmp = (GroupExercise) spinnerEditExerciseType.getItemAtPosition(i);
                if(tmp.getId() == exercises.getIdTypeExerciseFK()) {
                    spinnerEditExerciseType.setSelection(i);
                    break;
                }
            }
        }
        //set actions
        btnEditExerciseSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set intent
                //returnIntent = new Intent(getApplicationContext(), ExerciseEditActivity.class);
                returnIntent = new Intent();
                //set variables
                exercises.setTitle(String.valueOf(editTextEditExerciseName.getText()));
                exercises.setDescription(String.valueOf(editTextEditExerciseDescription.getText()));
                //Group and SubGroup of Exercise. Also type of exercise
                GroupExercise posGroupExercise = (GroupExercise) spinnerEditExerciseGroup.getSelectedItem();
                exercises.setIdGroupFK(posGroupExercise != null?posGroupExercise.getId():generalGroup);
                GroupExercise posSubGroupExercise = (GroupExercise) spinnerEditExerciseSubGroup.getSelectedItem();
                exercises.setIdSubGroupFK(posSubGroupExercise != null?posSubGroupExercise.getId():generalGroup);
                GroupExercise posTypeExercise = (GroupExercise) spinnerEditExerciseType.getSelectedItem();
                exercises.setIdTypeExerciseFK(posTypeExercise != null?posTypeExercise.getId():generalGroup);
                if(extra == null) {
                    resultDb = dbFunctions.addExercise(exercises);
                    exercises.setIdExercises(resultDb);
                }else{
                    resultDb = dbFunctions.updateExercise(exercises);
                    if(resultDb == 0) // no rows affected by updateExercise() method
                        setResult(Activity.RESULT_CANCELED, returnIntent);
                }
                if(exercises != null) {
                    returnIntent.putExtra(Constants.EXERCISEDATA, exercises);
                    setResult(Activity.RESULT_OK, returnIntent);
                } else {
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                }
                finish();
            }
        });
        //set actions
        btnEditExerciseCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
        //set actions
        linearLayoutEditExerciseMoreOptions.setVisibility(View.VISIBLE);
        iconId = R.drawable.ic_check_circle_black_36dp;
        colorId = android.R.color.darker_gray;
        iconDrawable = getResources().getDrawable(iconId);
        iconDrawable.setColorFilter(getResources().getColor(colorId), PorterDuff.Mode.SRC_ATOP);
        imageEditExerciseExpand.setImageDrawable(iconDrawable);
        linearLayoutEditExerciseMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconId = R.drawable.ic_circle_outline_black_36dp;
                colorId = android.R.color.darker_gray;
                if(linearLayoutEditExerciseMoreOptions.getVisibility() == View.GONE) {
                    linearLayoutEditExerciseMoreOptions.setVisibility(View.VISIBLE);
                    iconId = R.drawable.ic_check_circle_black_36dp;
                } else{
                    linearLayoutEditExerciseMoreOptions.setVisibility(View.GONE);
                    iconId = R.drawable.ic_circle_outline_black_36dp;
                }
                iconDrawable = getResources().getDrawable(iconId);
                iconDrawable.setColorFilter(getResources().getColor(colorId), PorterDuff.Mode.SRC_ATOP);
                imageEditExerciseExpand.setImageDrawable(iconDrawable);

            }
        });
        //minutes
        minuteValues = new String[12];
        for (int i = 0; i < minuteValues.length; i++) {
            String number = Integer.toString(i*5);
            minuteValues[i] = number.length() < 2 ? "0" + number : number;
        }
        pickerEditExerciseHour.setMinValue(0);
        pickerEditExerciseHour.setMaxValue(24); //Specify the maximum value/number of NumberPicker
        pickerEditExerciseMinute.setMinValue(0);
        pickerEditExerciseMinute.setMaxValue(minuteValues.length-1); //Specify the maximum value/number of NumberPicker
        pickerEditExerciseMinute.setDisplayedValues(minuteValues);
        //Wheel
        pickerEditExerciseHour.setWrapSelectorWheel(true);
        pickerEditExerciseMinute.setWrapSelectorWheel(true);

    }

    private void loadSpinners() {
        //setup spinners
        spinnerEditExerciseGroup = (Spinner) findViewById(R.id.spinnerEditExerciseGroup);
        spinnerEditExerciseSubGroup = (Spinner) findViewById(R.id.spinnerEditExerciseSubGroup);
        spinnerEditExerciseType = (Spinner) findViewById(R.id.spinnerEditExerciseType);
        listGroupExercise = new ArrayList<>();
        listTypeExercise = new ArrayList<>();
        Set<Integer> HeaderKeys = CacheMainActivity.groupExercisesList.keySet();
        if(CacheMainActivity.recommendedExercisesList == null) {
            Utils.loadExercisesInfoFromXml(this, getResources(), TAG);
        }
        if (CacheMainActivity.groupExercisesList == null) {
            Utils.loadExercisesConfigFromXml(getResources(), TAG);
        }
        int exerciseListGeneralId=0;
        for (Integer key : HeaderKeys) {listGroupExercise.add(CacheMainActivity.groupExercisesList.get(key));}
        //sort
        Collections.sort(listGroupExercise, new Comparator<GroupExercise>(){
            public int compare(GroupExercise emp1, GroupExercise emp2) {
                return (emp1.getId() - emp2.getId());
            }
        });
        if(listGroupExercise != null && listGroupExercise.size()>0) {
            listSubGroupExercise = listGroupExercise.get(exerciseListGeneralId).getSubGroup();
        }else {
            listSubGroupExercise = new ArrayList<>();
            listGroupExercise = new ArrayList<>();
        }
        adapterSpinnerGroup =
                new ArrayAdapter<GroupExercise>(this, android.R.layout.simple_list_item_1, listGroupExercise);
        spinnerEditExerciseGroup.setAdapter(adapterSpinnerGroup);
        if(listGroupExercise.size()>0)
            spinnerEditExerciseGroup.setSelection(exerciseListGeneralId);
        //subGroup
        adapterSpinnerSubGroup =
                new ArrayAdapter<GroupExercise>(this, android.R.layout.simple_list_item_1, listSubGroupExercise);
        spinnerEditExerciseSubGroup.setAdapter(adapterSpinnerSubGroup);
        //Exercise type
        HeaderKeys = CacheMainActivity.typeExercisesList.keySet();
        for (Integer key : HeaderKeys) {
            listTypeExercise.add(new GroupExercise(key,CacheMainActivity.typeExercisesList.get(key)));
        }
        //sort
        Collections.sort(listTypeExercise, new Comparator<GroupExercise>(){
            public int compare(GroupExercise emp1, GroupExercise emp2) {
                return (emp1.getId() - emp2.getId());
            }
        });
        if(listTypeExercise.size()>0)
            spinnerEditExerciseType.setSelection(exerciseListGeneralId);
        adapterSpinnerType =
                new ArrayAdapter<GroupExercise>(this, android.R.layout.simple_list_item_1, listTypeExercise);
        spinnerEditExerciseType.setAdapter(adapterSpinnerType);
        //listeners
        spinnerEditExerciseGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
                GroupExercise selectedGroup = (GroupExercise) parent.getItemAtPosition(pos);
                listSubGroupExercise = selectedGroup.getSubGroup();
                adapterSpinnerSubGroup.clear();
                adapterSpinnerSubGroup.addAll(listSubGroupExercise);
                adapterSpinnerSubGroup.notifyDataSetChanged();
            }
            public void onNothingSelected(AdapterView<?>arg0){
            }});
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
