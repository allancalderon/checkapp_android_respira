package mobi.checkapp.epoc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import mobi.checkapp.epoc.utils.Constants;

/**
 * Created by allancalderon on 24/05/16.
 */
public class activitiesDbHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String FLOAT_TYPE = " FLOAT";
    private static final String DOUBLE_TYPE = " DOUBLE";
    private static final String COMMA_SEP = ",";

    //table exercise create
    private static final String SQL_CREATE_ENTRIES_EXERCISE =
            "CREATE TABLE " + activitiesContract.ExerciseEntry.TABLE_NAME_EXERCISE + " (" +
                    activitiesContract.ExerciseEntry._ID + " INTEGER PRIMARY KEY," +
                    activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_ID + INTEGER_TYPE + COMMA_SEP +
                    activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDGROUPFK + INTEGER_TYPE + COMMA_SEP +
                    activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDSUBGROUPFK + INTEGER_TYPE + COMMA_SEP +
                    activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDTYPEEXERCISEFK + INTEGER_TYPE + COMMA_SEP +
                    activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_IDSOURCE + TEXT_TYPE + COMMA_SEP +
                    activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_SOURCETYPE + TEXT_TYPE + COMMA_SEP +
                    activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_TITLE + TEXT_TYPE + COMMA_SEP +
                    activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_URLVIDEO + TEXT_TYPE + COMMA_SEP +
                    activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO1 + TEXT_TYPE + COMMA_SEP +
                    activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO2 + TEXT_TYPE + COMMA_SEP +
                    activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO3 + TEXT_TYPE + COMMA_SEP +
                    activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_OTHERINFO4 + TEXT_TYPE + COMMA_SEP +
                    activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_FAVORITE + INTEGER_TYPE + COMMA_SEP +
                    activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_HIDDEN + INTEGER_TYPE + COMMA_SEP +
                    activitiesContract.ExerciseEntry.COLUMN_NAME_EXERCISE_RATIO + FLOAT_TYPE +
            " )";

    //table EXERCISE drop
    private static final String SQL_DELETE_ENTRIES_EXERCISE =
            "DROP TABLE IF EXISTS " + activitiesContract.ExerciseEntry.TABLE_NAME_EXERCISE;

    //table ASSIGN TIMELINE create
    private static final String SQL_CREATE_ENTRIES_ASSIGNTIMELINE =
            "CREATE TABLE " + activitiesContract.AssignedTimelineEntry.TABLE_NAME_ASSIGNTIMELINE + " (" +
                    activitiesContract.AssignedTimelineEntry._ID + " INTEGER PRIMARY KEY," +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_ID + INTEGER_TYPE + COMMA_SEP +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_IDEXERCISEFK + INTEGER_TYPE + COMMA_SEP +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_IDTYPEASSIGMENT + INTEGER_TYPE + COMMA_SEP +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_DAY + INTEGER_TYPE + COMMA_SEP +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_MONTH + INTEGER_TYPE + COMMA_SEP +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_YEAR + INTEGER_TYPE + COMMA_SEP +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_HOUR + INTEGER_TYPE + COMMA_SEP +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_MINUTE + INTEGER_TYPE + COMMA_SEP +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_GMTOFFSET + INTEGER_TYPE + COMMA_SEP +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_LAT + DOUBLE_TYPE + COMMA_SEP +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_LON + DOUBLE_TYPE + COMMA_SEP +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_AUDIOFILE + TEXT_TYPE + COMMA_SEP +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_TIMEDURATION + DOUBLE_TYPE + COMMA_SEP +
                    activitiesContract.AssignedTimelineEntry.COLUMN_NAME_ASSIGNTIMELINE_RATIO + DOUBLE_TYPE +
            " )";

    //table EXERCISE drop
    private static final String SQL_DELETE_ENTRIES_ASSIGNTIMELINE =
            "DROP TABLE IF EXISTS " + activitiesContract.AssignedTimelineEntry.TABLE_NAME_ASSIGNTIMELINE;

    //default methods
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_EXERCISE);
        db.execSQL(SQL_CREATE_ENTRIES_ASSIGNTIMELINE);
    }
    public activitiesDbHelper(Context context) {
        super(context, Constants.DATABASE_ACTIVITIES_NAME, null, Constants.DATABASE_ACTIVITIES_VERSION);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES_EXERCISE);
        db.execSQL(SQL_DELETE_ENTRIES_ASSIGNTIMELINE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
