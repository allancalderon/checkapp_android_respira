package mobi.checkapp.epoc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mobi.checkapp.epoc.entities.Exercises;
import mobi.checkapp.epoc.entities.TimelineAssign;
import mobi.checkapp.epoc.utils.CacheMainActivity;
import mobi.checkapp.epoc.utils.Constants;

/**
 * Created by allancalderon on 19/07/16.
 */
public class DbFunctions {

    private activitiesDbHelper mDbHelper;
    private SQLiteDatabase db;     // Gets the data repository in write mode

    public DbFunctions(Context context){
        //db setup
        mDbHelper = new activitiesDbHelper(context);
        db = mDbHelper.getWritableDatabase();     // Gets the data repository in write mode
    }

    public long addExercise(Exercises exercises) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDGROUPFK, exercises.getIdGroupFK());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDSUBGROUPFK, exercises.getIdSubGroupFK());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDTYPEEXERCISEFK, exercises.getIdTypeExerciseFK());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDSOURCE, exercises.getIdSource());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_SOURCETYPE, exercises.getSourceType());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_TITLE, exercises.getTitle());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_URLVIDEO, exercises.getUrlVideo());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_DESCRIPTION, exercises.getDescription());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO1, exercises.getOtherInfo1());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO2, exercises.getOtherInfo2());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO3, exercises.getOtherInfo3());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO4, exercises.getOtherInfo4());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_HIDDEN, exercises.isHidden()==true?1:0);
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_FAVORITE, exercises.isFavorite()==true?1:0);
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_RATIO, exercises.getRatio());
        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                activitiesContract.ExerciseEntry.TABLE_NAME_EXERCISE,
                activitiesContract.COLUMN_NAME_NULLABLE,
                values);
        exercises.setIdExercises(newRowId);

        CacheMainActivity.myExercises.add(exercises);

        return newRowId;
    }

    public long updateExercise(Exercises exercises) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDGROUPFK, exercises.getIdGroupFK());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDSUBGROUPFK, exercises.getIdSubGroupFK());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDTYPEEXERCISEFK, exercises.getIdTypeExerciseFK());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDSOURCE, exercises.getIdSource());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_SOURCETYPE, exercises.getSourceType());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_TITLE, exercises.getTitle());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_URLVIDEO, exercises.getUrlVideo());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_DESCRIPTION, exercises.getDescription());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO1, exercises.getOtherInfo1());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO2, exercises.getOtherInfo2());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO3, exercises.getOtherInfo3());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO4, exercises.getOtherInfo4());
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_HIDDEN, exercises.isHidden()==true?1:0);
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_FAVORITE, exercises.isFavorite()==true?1:0);
        values.put(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_RATIO, exercises.getRatio());
        // Insert the new row, returning the primary key value of the new row
        long newRowId;

        String strFilter = "_id=" + exercises.getIdExercises();

        newRowId = db.update(
                activitiesContract.ExerciseEntry.TABLE_NAME_EXERCISE,
                values,
                strFilter,
                null
        );

        //update cache information
        if(newRowId == 1 && exercises.getSourceType().equals(Constants.SOURCERECOMMENDATEDEJERCISEXML))
            CacheMainActivity.recommendedExercisesList = null;
        return newRowId;
    }

    public Cursor queryExercise(String[] projectionDb, String sortOrderDb, String type, String[] args) {
        String whereClause = null;
        String[] whereArgs = null;

        //activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_FAVORITE
        if (type != null && args != null) {
            whereClause = type + "=?";
            whereArgs = args;
        }

       return db.query(
                activitiesContract.ExerciseEntry.TABLE_NAME_EXERCISE,  // The table to query
                projectionDb,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrderDb                                 // The sort order
        );
    }

    public List<Exercises> queryExerciseList(String[] projectionDb, String sortOrderDb, String whereClause, String[] whereArgs) {

        if(projectionDb == null){
            String[] tmpProjectionDb = {
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
            projectionDb = tmpProjectionDb;
        }

        if(sortOrderDb == null)
            sortOrderDb = activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_TITLE + " ASC";

        Cursor cursor =  db.query(
                activitiesContract.ExerciseEntry.TABLE_NAME_EXERCISE,  // The table to query
                projectionDb,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrderDb                                 // The sort order
        );

        Exercises tmpExercise;
        List<Exercises> listExercises = new ArrayList();
        if (cursor.moveToFirst()) {
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
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_FAVORITE))>0);
                tmpExercise.setHidden(cursor.getInt(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_HIDDEN))>0);
                tmpExercise.setRatio(cursor.getInt(cursor
                        .getColumnIndex(activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_RATIO)));
                listExercises.add(tmpExercise);
                cursor.moveToNext();
            }
        }
        return listExercises;
    }

    public int deleteExercise(Exercises exercises) {
        String strFilter = "_id=" + exercises.getIdExercises();
        //TODO: verify if is recommended exercises or if there are activities related
        return db.delete(
                activitiesContract.ExerciseEntry.TABLE_NAME_EXERCISE,  // The table to query
                strFilter,
                null
        );
    }

    public long addTimelineAssign(TimelineAssign timelineAssign) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_IDEXERCISEFK, timelineAssign.getIdExerciseFK());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_IDTYPEASSIGMENT, timelineAssign.getIdTypeAssignment());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_DAY, timelineAssign.getDay());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_MONTH, timelineAssign.getMonth());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_YEAR, timelineAssign.getYear());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_HOUR, timelineAssign.getHour());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_MINUTE, timelineAssign.getMinute());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_GMTOFFSET, timelineAssign.getGmtOffset());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_LAT, timelineAssign.getLatitude());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_LON, timelineAssign.getLongitude());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_DESCRIPTION, timelineAssign.getDescription());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_AUDIOFILE, timelineAssign.getAudioFile());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_TIMEDURATION, timelineAssign.getTimeDuration());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_RATIO, timelineAssign.getRatio());
        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                activitiesContract.AssignedTimelineEntry.TABLE_NAME_ASSIGNTIMELINE,
                activitiesContract.COLUMN_NAME_NULLABLE,
                values);
        timelineAssign.setIdAssignedTimeline(newRowId);
        return newRowId;
    }

    public long updateTimelineAssign(TimelineAssign timelineAssign) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_IDEXERCISEFK, timelineAssign.getIdExerciseFK());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_IDTYPEASSIGMENT, timelineAssign.getIdTypeAssignment());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_DAY, timelineAssign.getDay());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_MONTH, timelineAssign.getMonth());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_YEAR, timelineAssign.getYear());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_HOUR, timelineAssign.getHour());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_MINUTE, timelineAssign.getMinute());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_GMTOFFSET, timelineAssign.getGmtOffset());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_LAT, timelineAssign.getLatitude());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_LON, timelineAssign.getLongitude());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_DESCRIPTION, timelineAssign.getDescription());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_AUDIOFILE, timelineAssign.getAudioFile());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_TIMEDURATION, timelineAssign.getTimeDuration());
        values.put(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_RATIO, timelineAssign.getRatio());
        // Insert the new row, returning the primary key value of the new row

        String strFilter = "_id=" + timelineAssign.getIdAssignedTimeline();

        long newRowId = db.update(
                activitiesContract.AssignedTimelineEntry.TABLE_NAME_ASSIGNTIMELINE,
                values,
                strFilter,
                null
        );
        return newRowId;
    }

    public List<TimelineAssign> queryTimelineAssignList(String[] projectionDb,
                                                   String sortOrderDb,
                                                   String whereClause,
                                                   String[] whereArgs) {
        if(projectionDb == null){
            String[] tmpProjectionDb = {
                    activitiesContract.AssignedTimelineEntry._ID,
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_IDEXERCISEFK,
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_IDTYPEASSIGMENT,
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_DAY,
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_MONTH,
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_YEAR,
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_HOUR,
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_MINUTE,
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_GMTOFFSET,
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_LAT,
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_LON,
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_DESCRIPTION,
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_AUDIOFILE,
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_TIMEDURATION,
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_RATIO
            };
            projectionDb = tmpProjectionDb;
        }

        if(sortOrderDb == null) {
            sortOrderDb = activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_YEAR + " ASC" + ", " +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_MONTH + " ASC" + ", " +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_DAY + " ASC" + ", " +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_HOUR + " ASC" + ", " +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_MINUTE + " ASC";
        }

        Cursor cursor =  db.query(
                activitiesContract.AssignedTimelineEntry.TABLE_NAME_ASSIGNTIMELINE,  // The table to query
                projectionDb,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrderDb                                 // The sort order
        );

        TimelineAssign tmpTimelineAssign;
        List<TimelineAssign> listTimelineAssign= new ArrayList();
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                tmpTimelineAssign = new TimelineAssign();
                tmpTimelineAssign.setIdAssignedTimeline(Long.valueOf(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.AssignedTimelineEntry._ID))));
                tmpTimelineAssign.setIdExerciseFK(Long.valueOf(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_IDEXERCISEFK))));
                tmpTimelineAssign.setIdTypeAssignment(Integer.valueOf(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_IDTYPEASSIGMENT))));
                tmpTimelineAssign.setDay(Integer.valueOf(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_DAY))));
                tmpTimelineAssign.setMonth(Integer.valueOf(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_MONTH))));
                tmpTimelineAssign.setYear(Integer.valueOf(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_YEAR))));
                tmpTimelineAssign.setHour(Integer.valueOf(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_HOUR))));
                tmpTimelineAssign.setMinute(Integer.valueOf(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_MINUTE))));
                tmpTimelineAssign.setGmtOffset(Integer.valueOf(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_GMTOFFSET))));
                tmpTimelineAssign.setLatitude(Double.valueOf(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_LAT))));
                tmpTimelineAssign.setLongitude(Double.valueOf(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_LON))));
                tmpTimelineAssign.setDescription(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_DESCRIPTION)));
                tmpTimelineAssign.setAudioFile(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_AUDIOFILE)));
                tmpTimelineAssign.setTimeDuration(Double.valueOf(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_TIMEDURATION))));
                tmpTimelineAssign.setRatio(Double.valueOf(cursor.getString(cursor
                        .getColumnIndex(activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_RATIO))));
                listTimelineAssign.add(tmpTimelineAssign);
                cursor.moveToNext();
            }
        }
        return listTimelineAssign;
    }

}
