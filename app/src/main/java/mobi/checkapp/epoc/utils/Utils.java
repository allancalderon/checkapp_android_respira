package mobi.checkapp.epoc.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import mobi.checkapp.epoc.R;
import mobi.checkapp.epoc.db.DbFunctions;
import mobi.checkapp.epoc.db.activitiesContract;
import mobi.checkapp.epoc.entities.Exercises;
import mobi.checkapp.epoc.entities.GroupExercise;
import mobi.checkapp.epoc.entities.Pollutant;
import mobi.checkapp.epoc.entities.PollutantMeasure;

/**
 * Created by luisbanon on 31/05/16.
 */
public class Utils {

    public static String getMonth(Context context, int month){
        switch (month) {
            case 1:
                return context.getResources().getString(R.string.month_january);
            case 2:
                return context.getResources().getString(R.string.month_february);
            case 3:
                return context.getResources().getString(R.string.month_march);
            case 4:
                return context.getResources().getString(R.string.month_april);
            case 5:
                return context.getResources().getString(R.string.month_may);
            case 6:
                return context.getResources().getString(R.string.month_june);
            case 7:
                return context.getResources().getString(R.string.month_july);
            case 8:
                return context.getResources().getString(R.string.month_august);
            case 9:
                return context.getResources().getString(R.string.month_september);
            case 10:
                return context.getResources().getString(R.string.month_octuber);
            case 11:
                return context.getResources().getString(R.string.month_november);
            case 12:
                return context.getResources().getString(R.string.month_december);
            default:
                return "";
        }
    }

    public static int getPollutantValue(PollutantMeasure pe) {
        int valorfinal = 0;

        double value = pe.getValue();

        double val1 = pe.getPollutant().getLimitGood();
        double val2 = pe.getPollutant().getLimitSatisfactory();
        double val3 = pe.getPollutant().getLimitModerately();
        double val4 = pe.getPollutant().getLimitPoor();
        double val5 = pe.getPollutant().getLimitVeryPoor();
        if ((val1 * val2 * val3 * val4 * val5) != -1) {
            if (value <= val1) {
                return 1;
            } else if (value <= val2) {
                return 2;
            } else if (value <= val3) {
                return 3;
            } else if (value <= val4) {
                return 4;
            } else if (value <= val5) {
                return 5;
            }
        }
        return valorfinal;
    }

    public static void loadExercisesConfigFromXml(Resources res, String TAG) {
        XmlResourceParser mExercisesXmlParser = res.getXml(R.xml.exercises_definition);
        try {
            mExercisesXmlParser.next();
            String tag = "";
            Float version;
            GroupExercise groupExercises = null;
            GroupExercise subGroupExercises = null;
            List<GroupExercise> subGroupExercisesList = null;
            while (mExercisesXmlParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (mExercisesXmlParser.getEventType() == XmlPullParser.START_TAG) {
                    tag = mExercisesXmlParser.getName();
                    switch (tag) {
                        case "exercisetype":
                            CacheMainActivity.typeExercisesList = new HashMap<>();
                            break;
                        case "idTypeAssignment":
                            CacheMainActivity.typeExercisesList.put(Integer.valueOf(mExercisesXmlParser.getAttributeValue(0)),
                                    mExercisesXmlParser.getAttributeValue(1));
                            break;
                        case "exercisesdefinition":
                            CacheMainActivity.groupExercisesList = new HashMap<Integer, GroupExercise>();
                            version = Float.valueOf(mExercisesXmlParser.getAttributeValue(0));
                            break;
                        case "exercisesgroup":
                            break;
                        case "subgroup":
                            subGroupExercises = new GroupExercise();
                            subGroupExercises.setId(Integer.valueOf(mExercisesXmlParser.getAttributeValue(0)));
                            break;
                        case "group":
                            subGroupExercisesList = new ArrayList<>();
                            groupExercises = new GroupExercise();
                            groupExercises.setId(Integer.valueOf(mExercisesXmlParser.getAttributeValue(0)));
                            break;
                        case "name":
                            groupExercises.setName(mExercisesXmlParser.getAttributeValue(0));
                            break;
                    }
                } else if (mExercisesXmlParser.getEventType() == XmlPullParser.END_TAG) {
                    tag = mExercisesXmlParser.getName();
                    switch (tag) {
                        case "group":
                            groupExercises.setSubGroup(subGroupExercisesList);
                            CacheMainActivity.groupExercisesList.put(groupExercises.getId(), groupExercises);
                            break;
                        case "subgroup":
                            subGroupExercisesList.add(subGroupExercises);
                            break;
                    }
                } else if (mExercisesXmlParser.getEventType() == XmlPullParser.TEXT) {
                    switch (tag) {
                        case "subgroup":
                            subGroupExercises.setName(mExercisesXmlParser.getText());
                            break;
                    }
                }
                mExercisesXmlParser.next();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reading exercises configuration.", e);
        }
    }


    public static void loadExercisesInfoFromXml(Context context, Resources res, String TAG) {
        DbFunctions dbFunctions = new DbFunctions(context);
        String[] projectionDb = {
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
        String sortOrderDb = activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_TITLE + " ASC";
        Cursor cursor = dbFunctions.queryExercise(
                projectionDb,
                sortOrderDb,
                activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_SOURCETYPE,
                new String[]{Constants.SOURCERECOMMENDATEDEJERCISEXML});
        Exercises tmpExercise;
        if (cursor.moveToFirst()) {
            if (CacheMainActivity.recommendedExercisesList == null) {
                CacheMainActivity.recommendedExercisesList = new ArrayList();
            }
            while (cursor.isAfterLast() == false) {
                tmpExercise = new Exercises();
                tmpExercise.setIdExercises(Long.valueOf(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry._ID))));
                tmpExercise.setIdGroupFK(cursor.getInt(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDGROUPFK)));
                tmpExercise.setIdSubGroupFK(cursor.getInt(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDSUBGROUPFK)));
                tmpExercise.setIdTypeExerciseFK(cursor.getInt(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDTYPEEXERCISEFK)));
                tmpExercise.setIdSource(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDSOURCE)));
                tmpExercise.setSourceType(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_SOURCETYPE)));
                tmpExercise.setTitle(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_TITLE)));
                tmpExercise.setUrlVideo(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_URLVIDEO)));
                tmpExercise.setDescription(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_DESCRIPTION)));
                tmpExercise.setOtherInfo1(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO1)));
                tmpExercise.setOtherInfo2(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO2)));
                tmpExercise.setOtherInfo3(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO3)));
                tmpExercise.setOtherInfo4(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO4)));
                tmpExercise.setFavorite(cursor.getInt(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_FAVORITE)) > 0);
                tmpExercise.setHidden(cursor.getInt(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_HIDDEN)) > 0);
                tmpExercise.setRatio(cursor.getInt(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_RATIO)));
                CacheMainActivity.recommendedExercisesList.add(tmpExercise);
                cursor.moveToNext();
            }
        }
        if (CacheMainActivity.recommendedExercisesList == null) {
            XmlResourceParser mExercisesXmlParser = res.getXml(R.xml.exercises_recommended);
            try {
                mExercisesXmlParser.next();
                String tag = "";
                Exercises exercises = null;
                while (mExercisesXmlParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (mExercisesXmlParser.getEventType() == XmlPullParser.START_TAG) {
                        tag = mExercisesXmlParser.getName();
                        switch (tag) {
                            case "exerciseslist":
                                CacheMainActivity.recommendedExercisesList = new ArrayList<Exercises>();
                                break;
                            case "exercises":
                                exercises = new Exercises();
                                break;
                            case "idExercises":
                                exercises.setIdExercises(Long.valueOf(mExercisesXmlParser.getAttributeValue(0)));
                                break;
                            case "idGroupFK":
                                exercises.setIdGroupFK(Integer.valueOf(mExercisesXmlParser.getAttributeValue(0)));
                                break;
                            case "idSubGroupFK":
                                exercises.setIdSubGroupFK(Integer.valueOf(mExercisesXmlParser.getAttributeValue(0)));
                                break;
                            case "idTypeExerciseFK":
                                exercises.setIdTypeExerciseFK(Integer.valueOf(mExercisesXmlParser.getAttributeValue(0)));
                                break;
                            case "idSource":
                                exercises.setIdSource(mExercisesXmlParser.getAttributeValue(0));
                                break;
                            case "sourceType":
                                exercises.setSourceType(mExercisesXmlParser.getAttributeValue(0));
                                break;
                            case "title":
                                exercises.setTitle(mExercisesXmlParser.getAttributeValue(0));
                                break;
                            case "urlVideo":
                                exercises.setUrlVideo(mExercisesXmlParser.getAttributeValue(0));
                                break;
                            case "otherInfo1":
                                exercises.setOtherInfo1(mExercisesXmlParser.getAttributeValue(0));
                                break;
                            case "otherInfo2":
                                exercises.setOtherInfo2(mExercisesXmlParser.getAttributeValue(0));
                                break;
                            case "otherInfo3":
                                exercises.setOtherInfo3(mExercisesXmlParser.getAttributeValue(0));
                                break;
                            case "otherInfo4":
                                exercises.setOtherInfo4(mExercisesXmlParser.getAttributeValue(0));
                                break;
                            case "favorite":
                                exercises.setFavorite(Boolean.valueOf(mExercisesXmlParser.getAttributeValue(0)));
                                break;
                            case "hidden":
                                exercises.setHidden(Boolean.valueOf(mExercisesXmlParser.getAttributeValue(0)));
                                break;
                            case "ratio":
                                exercises.setRatio(Float.valueOf(mExercisesXmlParser.getAttributeValue(0)));
                                break;
                        }
                    } else if (mExercisesXmlParser.getEventType() == XmlPullParser.END_TAG) {
                        tag = mExercisesXmlParser.getName();
                        if (tag.equals("exercises")) {
                            long resultDb = dbFunctions.addExercise(exercises);
                            if (resultDb > 0) {
                                CacheMainActivity.recommendedExercisesList.add(exercises);
                            }
                        }
                    } else if (mExercisesXmlParser.getEventType() == XmlPullParser.TEXT) {
                        if (tag.equals("description"))
                            exercises.setDescription(mExercisesXmlParser.getText());
                    }
                    mExercisesXmlParser.next();
                }
                //TODO: load ratio from DB for markers
            } catch (Exception e) {
                Log.e(TAG, "Error reading markers from res/xml", e);
            }
        }
    }

    public static void share(String text_to_share, Context _context) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = text_to_share;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Epoc App");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        _context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static void updateAddress(Context context, Location location, GoogleApiClient mGoogleApiClient) {
        //Get current direction to send in new activity
        List<Address> addresses = null;
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            CacheMainActivity.DirectionLastKnowLocation = mLastLocation;
        }
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Address address;
        ArrayList<String> addressFragments;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (Exception e) {

        }
        if (addresses == null || addresses.size() == 0) {
            //get last location
            try {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient) != null) {
                    addresses = geocoder.getFromLocation(
                            LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLatitude(),
                            LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLongitude(),
                            1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (addresses != null && addresses.size() > 0) {
            address = addresses.get(0);
            addressFragments = new ArrayList<String>();
            for (int j = 0; j < address.getMaxAddressLineIndex(); j++) {
                addressFragments.add(address.getAddressLine(j));
            }
            String direction = "";
            for (String s : addressFragments) {
                direction += s + " ";
            }
            CacheMainActivity.Direction = direction;
            try {
                CacheMainActivity.DirectionLatLng = new LatLng(location.getLatitude(),
                        location.getLongitude());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void setupPollutantsList() {
        if(CacheMainActivity.pollutantsHashMap==null) {
            CacheMainActivity.pollutantsHashMap = new HashMap<>();
            CacheMainActivity.pollutantsHashMap.put(1, new Pollutant(1, "Dioxido de Azufre", "SO2", "µg/m3", "ID 38: Fluorescencia ultravioleta", 175, 350, 525, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(2, new Pollutant(2, "Monoxido de Carbono", "CO", "mg/m3", "ID 48: Absorcion infrarroja", 5, 10, 15, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(3, new Pollutant(3, "Monoxido de Nitrogeno", "NO", "µg/m3", "ID 8: Quimioluminiscencia", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(4, new Pollutant(4, "Dioxido de Nitrogeno", "NO2", "µg/m3", "ID 8: Quimioluminiscencia", 100, 200, 300, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(5, new Pollutant(5, "Particulas < 2.5 µm", "PM2.5", "µg/m3", "ID 47: Microbalanza", 12, 35.4, 55.4, 150.4, 250.4));
            CacheMainActivity.pollutantsHashMap.put(6, new Pollutant(6, "Particulas < 10 µm", "PM10", "µg/m3", "ID 47: Microbalanza", 54, 154, 254, 354, 424));
            CacheMainActivity.pollutantsHashMap.put(7, new Pollutant(7, "oxidos de Nitrogeno", "NOx", "µg/m3", "ID 8: Quimioluminiscencia", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(8, new Pollutant(8, "Ozono", "O3", "µg/m3", "ID 6: Absorcion ultravioleta", 90, 180, 240, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(9, new Pollutant(9, "Tolueno", "TOL", "µg/m3", "ID 59: Cromatografia de gases", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(10, new Pollutant(10, "Benceno", "BEN", "µg/m3", "ID 59: Cromatografia de gases", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(11, new Pollutant(11, "Etilbenceno", "EBE", "µg/m3", "ID 59: Cromatografia de gases", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(12, new Pollutant(12, "Metaxileno", "MXY", "µg/m3", "ID 59: Cromatografia de gases", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(13, new Pollutant(13, "Paraxileno", "PXY", "µg/m3", "ID 59: Cromatografia de gases", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(14, new Pollutant(14, "Ortoxileno", "OXY", "µg/m3", "ID 59: Cromatografia de gases", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(15, new Pollutant(15, "Hidrocarburos totales (hexano)", "TCH", "mg/m3", "ID 2: Ionizacion de llama", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(16, new Pollutant(16, "Hidrocarburos (metano)", "CH4", "mg/m3", "ID 2: Ionizacion de llama", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(17, new Pollutant(17, "Hidrocarburos no metanicos (hexano)", "NMHC", "mg/m3", "ID 2: Ionizacion de llama", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(18, new Pollutant(18, "Radiacion ultravioleta", "UV", "mW/m2", "ID 98: Sensores meteorologicos", 50, 125, 175, 250, -1));
            CacheMainActivity.pollutantsHashMap.put(19, new Pollutant(19, "Velocidad del viento", "VV", "m/s", "ID 98: Sensores meteorologicos", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(20, new Pollutant(20, "Direccion del viento", "DV", "Grados o cuadrante", "ID 98: Sensores meteorologicos", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(21, new Pollutant(21, "Temperatura", "TMP", "Grado Celsius", "ID 98: Sensores meteorologicos", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(22, new Pollutant(22, "Humedad relativa", "HR", "%", "ID 98: Sensores meteorologicos", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(23, new Pollutant(23, "Presion", "PRB", "mb", "ID 98: Sensores meteorologicos", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(24, new Pollutant(24, "Radiacion solar", "RS", "kW/m2", "ID 98: Sensores meteorologicos", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(25, new Pollutant(25, "Precipitacion", "LL", "l/m2", "ID 98: Sensores meteorologicos", -1, -1, -1, -1, -1));
            CacheMainActivity.pollutantsHashMap.put(26, new Pollutant(26, "Lluvia acida", "LLA", "pH", "ID 98: Sensores meteorologicos", -1, -1, -1, -1, -1));
        }
    }


}
