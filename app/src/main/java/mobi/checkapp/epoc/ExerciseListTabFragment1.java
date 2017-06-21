package mobi.checkapp.epoc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import mobi.checkapp.epoc.adapter.ELExerciseAdapter;
import mobi.checkapp.epoc.adapter.OnElItemClickListener;
import mobi.checkapp.epoc.db.DbFunctions;
import mobi.checkapp.epoc.db.activitiesContract;
import mobi.checkapp.epoc.entities.Exercises;
import mobi.checkapp.epoc.entities.GroupExercise;
import mobi.checkapp.epoc.utils.CacheMainActivity;
import mobi.checkapp.epoc.utils.Constants;
import mobi.checkapp.epoc.utils.Utils;

/**
 * Created by allancalderon on 30/05/16.
 */
public class ExerciseListTabFragment1 extends Fragment implements ExpandableListView.OnChildClickListener{
    private final String TAG = this.getClass().getName();
    //variables
    private List<Exercises> listExercises;

    //activitiesDB definitions
    private Context context;
    private DbFunctions dbFunctions;

    private String[] projectionDb = {
            activitiesContract.ExerciseEntry._ID,
            activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDGROUPFK,
            activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDSUBGROUPFK,
            activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDTYPEEXERCISEFK,
            activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_TITLE,
            activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDSOURCE,
            activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_SOURCETYPE,
            activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_URLVIDEO,
            activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_DESCRIPTION,
            activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO1,
            activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO2,
            activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO3,
            activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO4,
            activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_FAVORITE,
            activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_HIDDEN,
            activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_RATIO
    };
    private String sortOrderDb = activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_TITLE + " ASC";
    private Cursor cursor;

    private ExpandableListView expListView;
    HashMap<String, List<Exercises>> exDataChild;
    List<String> exDataHeader;
    ELExerciseAdapter elExerciseAdapter;
    View rootView;
    //ExerciseListActivity mUpdateFragments;
    MainActivity mUpdateFragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_list_exercise_fragment, container, false);
        context = getActivity().getApplicationContext();
        //mUpdateFragments = (ExerciseListActivity) getActivity();
        mUpdateFragments = (MainActivity) getActivity();
        //read entries from db
        dbFunctions = new DbFunctions(getContext());
        expListView = (ExpandableListView) rootView.findViewById(R.id.elListExercise);
        setupExpandableList();
        return rootView;
    }

    public void updateExpandableList(){
        if(exDataHeader!=null)
            exDataHeader.clear();
        if(exDataChild!=null)
            exDataChild.clear();
        setupExpandableList();
        if(elExerciseAdapter!=null)
            elExerciseAdapter.notifyDataSetChanged();
    }

    private void setupExpandableList() {
        HashMap<Integer, List<Exercises>> tmpDataChild = new HashMap<>();
        exDataChild = new HashMap<>();
        List<Exercises> lt;
        listExercises = new ArrayList<>();
        List<Exercises> tmpLtExercises;
        //load from cache
        preloadInformation();
        //print expandable list view
        for(Exercises e : CacheMainActivity.recommendedExercisesList) {
            int groupId = e.getIdGroupFK();
            tmpLtExercises = new ArrayList<>();
            if(tmpDataChild.containsKey(groupId)){
                tmpLtExercises = tmpDataChild.get(groupId);
                tmpDataChild.remove(groupId);
            }
            tmpLtExercises.add(e);
            tmpDataChild.put(groupId, tmpLtExercises);
        }
        //header
        exDataHeader = new ArrayList<>();
        Set<Integer> HeaderKeys = tmpDataChild.keySet();
        for (Integer key : HeaderKeys)
        {
            lt = tmpDataChild.get(key);
            if(lt != null) {
                if (lt.size() > 0) {
                    GroupExercise tmpGroupExercise = CacheMainActivity.groupExercisesList.get(key);
                    String name="";
                    if(tmpGroupExercise!=null)
                        name = tmpGroupExercise.getName();
                    exDataHeader.add(name);
                    exDataChild.put(name, lt);
                }
            }
        }
        elExerciseAdapter = new ELExerciseAdapter(getActivity(), exDataHeader, exDataChild);
        elExerciseAdapter.mListener = new OnElItemClickListener() {
            Intent i = null;
            Exercises exerciseChild = null;
            @Override
            public void onElItemClicked(int groupPosition, int childPosition, int layoutId, View view) {
                exerciseChild = (Exercises) elExerciseAdapter.getChild(groupPosition, childPosition);
                i = null;
                switch (layoutId){
                    case R.id.linearLayoutListExerciseText:
                        i = new Intent(context, ExerciseActivity.class);
                        break;
                    case R.id.btnListExerciseBodyAdd:
                        i = new Intent(context, AssignTimelineExerciseActivity.class);
                        break;
                    case R.id.btnListExerciseBodyMore:
                        PopupMenu popupMenu = new PopupMenu(getContext(), view);
                        popupMenu.setOnMenuItemClickListener(this);
                        popupMenu.inflate(R.menu.menu_activities_rec_details);
                        popupMenu.show();
                        break;
                    case R.id.btnListExerciseFavorite:
                        //TODO: read from database utils.Constants.TEMP variables and remove temp class
                        if(exerciseChild.isFavorite()) {
                            exerciseChild.setFavorite(false);
                        }else {
                            exerciseChild.setFavorite(true);
                        }
                        //TODO:verificar el motivo de no
                        long resultDb = dbFunctions.updateExercise(exerciseChild);
                        if(elExerciseAdapter!=null)
                            elExerciseAdapter.notifyDataSetChanged();
                        //updateExpandableList();
                        //mUpdateFragments.updateFragments();
                        break;
                }
                if(exerciseChild != null && i != null) {
                    Bundle exerciseInfo = new Bundle();
                    exerciseInfo.putSerializable(Constants.LISTEXERCISEDATA, exerciseChild);
                    i.putExtra(Constants.LISTEXERCISEBUNDLE, exerciseInfo);
                    startActivityForResult(i,Constants.EXERCISEREQCODE);
                }
            }
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_activities_details_share:
                        i = new Intent(context, ExerciseShareActivity.class);
                        break;
                    case R.id.menu_activities_details_hide:
                        i = new Intent(context, ExerciseDeleteActivity.class);
                        break;
                }

                if(exerciseChild != null && i != null) {
                    Bundle exerciseInfo = new Bundle();
                    exerciseInfo.putSerializable(Constants.LISTEXERCISEDATA, exerciseChild);
                    i.putExtra(Constants.LISTEXERCISEBUNDLE, exerciseInfo);
                    startActivityForResult(i,Constants.EXERCISEREQCODE);
                    return true;
                }
                return false;
            }
        };
        expListView.setAdapter(elExerciseAdapter);
        for(int i=0; i < elExerciseAdapter.getGroupCount(); i++)
            expListView.expandGroup(i);
    }

    private void preloadInformation() {
        if(CacheMainActivity.recommendedExercisesList == null) {
            Utils.loadExercisesInfoFromXml(getContext(), getResources(), TAG);
        }
        if (CacheMainActivity.groupExercisesList == null)
            Utils.loadExercisesConfigFromXml(getResources(), TAG);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Exercises exerciseChild = (Exercises) elExerciseAdapter.getChild(groupPosition, childPosition);
        Intent i = new Intent(context, ExerciseActivity.class);
        if(exerciseChild != null && i != null) {
            Bundle exerciseInfo = new Bundle();
            exerciseInfo.putSerializable(Constants.LISTEXERCISEDATA, exerciseChild);
            i.putExtra(Constants.LISTEXERCISEBUNDLE, exerciseInfo);
            startActivityForResult(i,Constants.EXERCISEREQCODE);
        }else{
            // Prompt user to enter credentials
            Toast.makeText(context,
                    "Error recovering information. Please, try again!", Toast.LENGTH_LONG)
                    .show();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == Constants.EXERCISEREQCODE) {
            // Make sure the request was successful
            if (resultCode == getActivity().RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Exercises exercise;
                Bundle exerciseInfo = data.getExtras();
                if(exerciseInfo != null) {
                    exercise = (Exercises) exerciseInfo.getSerializable(Constants.EXERCISEDATA);
                    if (exercise != null) {
                        //TODO: when the list is updated, need to save the current view state (before update) and restore (after update)
                        updateExpandableList();
                        //mUpdateFragments.updateFragments();
                    }
                }
            }
        }
    }

}
