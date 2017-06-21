package mobi.checkapp.epoc.utils;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import mobi.checkapp.epoc.entities.Exercises;
import mobi.checkapp.epoc.entities.GroupExercise;
import mobi.checkapp.epoc.entities.MyItem;
import mobi.checkapp.epoc.entities.Pollutant;
import mobi.checkapp.epoc.entities.PollutantMeasure;
import mobi.checkapp.epoc.entities.Year;

/**
 * Created by allancalderon on 30/05/16.
 */
public class CacheMainActivity {
    //Preload exercises
    public static List<Exercises> recommendedExercisesList = null;//Recommended exercises
    public static HashMap<Integer, GroupExercise> groupExercisesList = null;
    public static HashMap<Integer, String> typeExercisesList = null;  //Type of exercise (0)General, (1)Caminar, (2) Correr, (3) Pedalar, (4) Bien estar

    public static ArrayList<Exercises> favorites = new ArrayList<>();
    public static ArrayList<Exercises> myExercises = new ArrayList<>();

    public static ArrayList<MyItem> myItems = new ArrayList<>();
    public static Location myLocation;
    public static LinkedHashMap<String, List<Exercises>> hashListItems;

    public static String CheckboxState;

    public static ArrayList<Year> listTimeline;

    public static String Direction = "No se pudo obtener";
    public static LatLng DirectionLatLng=null;
    public static Location DirectionLastKnowLocation=null;

    //pollutants
    public static HashMap<String, Date> lastUpdate = null;
    public static HashMap<String, List<PollutantMeasure>> sensorMeasureList = null;
    public static HashMap<Integer, Pollutant> pollutantsHashMap=null;
    public static HashMap<Integer, Double> pollutantsMaxMeasure=null;  // first parameter is the id od the sensor. The second argument is the contamination level
    public static int contTempSensors=0;
    public static float temperature;

    public static String CurrentCity = "ESPMAD";


}
