package mobi.checkapp.epoc.nav;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import mobi.checkapp.epoc.R;
import mobi.checkapp.epoc.component.YearComponent;
import mobi.checkapp.epoc.db.DbFunctions;
import mobi.checkapp.epoc.entities.Activity;
import mobi.checkapp.epoc.entities.Day;
import mobi.checkapp.epoc.entities.Month;
import mobi.checkapp.epoc.entities.TimelineAssign;
import mobi.checkapp.epoc.entities.Year;
import mobi.checkapp.epoc.utils.CacheMainActivity;

public class NewTimelineActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public LinearLayout lnYears;
    private DbFunctions dbFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_timeline_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lnYears = (LinearLayout) findViewById(R.id.lnYears);




        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_timeline_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

    }

    @Override
    protected void onResume() {
        super.onResume();

        //TODO leer de base de datos y llenar CachemainActivity.listTimeline
        dbFunctions = new DbFunctions(this);
        List<TimelineAssign> tmpExerciseList = dbFunctions.queryTimelineAssignList(
                null,
                null,
                null,
                null
        );

        CacheMainActivity.listTimeline = null;

        Year newyear;
        int valYear = 0;
        int valmonth = 0;
        int valday = 0;
        ArrayList<Month> listMonths;
        ArrayList<Day> lisDays;
        ArrayList<Activity> listActivities;

        for(TimelineAssign tla : tmpExerciseList){

            if(CacheMainActivity.listTimeline != null){

                if(tla.getYear() == valYear){

                    if(tla.getMonth() == valmonth){

                        if(tla.getDay() == valday){

                            Activity activity = new Activity();
                            activity.cuerpo_actividad = tla.getDescription();
                            activity.idActivity = String.valueOf(tla.getIdAssignedTimeline());
                            activity.idExcersice = String.valueOf(tla.getIdExerciseFK());
                            activity.titulo_actividad = tla.getDescription();
                            activity.nombre_grabacion =tla.getAudioFile();
                            activity.tiempo_actividad = String.valueOf(tla.getTimeDuration());
                            activity.ubicacion = String.valueOf(tla.getLatitude())+","+String.valueOf(tla.getLongitude());
                            activity.day = String.valueOf(valday);
                            activity.month = String.valueOf(valmonth);
                            activity.year = String.valueOf(valYear);

                            int i = 0;
                            int j = 0;
                            int z = 0;

                            for ( Year dy : CacheMainActivity.listTimeline){
                                if(dy.year == valYear){

                                    for(Month m : dy.listMonths){
                                        if(m.month == valmonth){

                                            for(Day d : m.listDays){

                                                if(d.day == valday){

                                                    CacheMainActivity.listTimeline.get(i).listMonths.get(j).listDays.get(z).listActivities.add(activity);

                                                }
                                                z++;
                                            }

                                        }
                                        j++;
                                    }

                                }
                                i++;
                            }

                        }else{
                            valday = tla.getDay();
                            Day day = new Day();
                            day.day = valday;
                            listActivities = new ArrayList<>();
                            Activity activity = new Activity();
                            activity.cuerpo_actividad = tla.getDescription();
                            activity.idActivity = String.valueOf(tla.getIdAssignedTimeline());
                            activity.idExcersice = String.valueOf(tla.getIdExerciseFK());
                            activity.titulo_actividad = tla.getDescription();
                            activity.nombre_grabacion =tla.getAudioFile();
                            activity.tiempo_actividad = String.valueOf(tla.getTimeDuration());
                            activity.ubicacion = String.valueOf(tla.getLatitude())+","+String.valueOf(tla.getLongitude());
                            activity.day = String.valueOf(valday);
                            activity.month = String.valueOf(valmonth);
                            activity.year = String.valueOf(valYear);

                            listActivities.add(activity);

                            day.listActivities = listActivities;

                            int i = 0;
                            int j = 0;

                            for ( Year dy : CacheMainActivity.listTimeline){
                                if(dy.year == valYear){

                                    for(Month m : dy.listMonths){
                                        if(m.month == valmonth){
                                            CacheMainActivity.listTimeline.get(i).listMonths.get(j).listDays.add(day);
                                        }
                                        j++;
                                    }

                                }
                                i++;
                            }
                        }


                    }else{

                        valmonth = tla.getMonth();
                        Month month = new Month();
                        month.month = valmonth;
                        lisDays = new ArrayList<>();
                        valday = tla.getDay();
                        Day day = new Day();
                        day.day = valday;
                        listActivities = new ArrayList<>();
                        Activity activity = new Activity();
                        activity.cuerpo_actividad = tla.getDescription();
                        activity.idActivity = String.valueOf(tla.getIdAssignedTimeline());
                        activity.idExcersice = String.valueOf(tla.getIdExerciseFK());
                        activity.titulo_actividad = tla.getDescription();
                        activity.nombre_grabacion =tla.getAudioFile();
                        activity.tiempo_actividad = String.valueOf(tla.getTimeDuration());
                        activity.ubicacion = String.valueOf(tla.getLatitude())+","+String.valueOf(tla.getLongitude());
                        activity.day = String.valueOf(valday);
                        activity.month = String.valueOf(valmonth);
                        activity.year = String.valueOf(valYear);

                        listActivities.add(activity);

                        day.listActivities = listActivities;

                        lisDays.add(day);

                        month.listDays = lisDays;
                        int i = 0;

                        for ( Year dy : CacheMainActivity.listTimeline){
                            if(dy.year == valYear){
                                CacheMainActivity.listTimeline.get(i).listMonths.add(month);

                            }
                            i++;
                        }

                    }

                }else{

                    valYear = tla.getYear();
                    newyear = new Year();
                    newyear.year = valYear;
                    listMonths = new ArrayList<>();
                    valmonth = tla.getMonth();
                    Month month = new Month();
                    month.month = valmonth;
                    lisDays = new ArrayList<>();
                    valday = tla.getDay();
                    Day day = new Day();
                    day.day = valday;
                    listActivities = new ArrayList<>();
                    Activity activity = new Activity();
                    activity.cuerpo_actividad = tla.getDescription();
                    activity.idActivity = String.valueOf(tla.getIdAssignedTimeline());
                    activity.idExcersice = String.valueOf(tla.getIdExerciseFK());
                    activity.titulo_actividad = tla.getDescription();
                    activity.nombre_grabacion =tla.getAudioFile();
                    activity.tiempo_actividad = String.valueOf(tla.getTimeDuration());
                    activity.ubicacion = String.valueOf(tla.getLatitude())+","+String.valueOf(tla.getLongitude());
                    activity.day = String.valueOf(valday);
                    activity.month = String.valueOf(valmonth);
                    activity.year = String.valueOf(valYear);

                    listActivities.add(activity);

                    day.listActivities = listActivities;

                    lisDays.add(day);

                    month.listDays = lisDays;

                    listMonths.add(month);

                    newyear.listMonths = listMonths;

                    CacheMainActivity.listTimeline.add(newyear);

                }

            }else{

                CacheMainActivity.listTimeline = new ArrayList<>();
                valYear = tla.getYear();
                newyear = new Year();
                newyear.year = valYear;
                listMonths = new ArrayList<>();
                valmonth = tla.getMonth();
                Month month = new Month();
                month.month = valmonth;
                lisDays = new ArrayList<>();
                valday = tla.getDay();
                Day day = new Day();
                day.day = valday;
                listActivities = new ArrayList<>();
                Activity activity = new Activity();
                activity.cuerpo_actividad = tla.getDescription();
                activity.idActivity = String.valueOf(tla.getIdAssignedTimeline());
                activity.idExcersice = String.valueOf(tla.getIdExerciseFK());
                activity.titulo_actividad = tla.getDescription();
                activity.nombre_grabacion =tla.getAudioFile();
                activity.tiempo_actividad = String.valueOf(tla.getTimeDuration());
                activity.ubicacion = String.valueOf(tla.getLatitude())+","+String.valueOf(tla.getLongitude());
                activity.day = String.valueOf(valday);
                activity.month = String.valueOf(valmonth);
                activity.year = String.valueOf(valYear);

                listActivities.add(activity);

                day.listActivities = listActivities;

                lisDays.add(day);

                month.listDays = lisDays;

                listMonths.add(month);

                newyear.listMonths = listMonths;

                CacheMainActivity.listTimeline.add(newyear);

            }

        }

        lnYears.removeAllViews();

        try {
            for (Year year : CacheMainActivity.listTimeline) {
                YearComponent component = new YearComponent(NewTimelineActivity.this, year);
                lnYears.addView(component);
            }
        }catch (Exception e){

        }

    }
}
