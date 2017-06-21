package mobi.checkapp.epoc.db;

import android.provider.BaseColumns;

/**
 * Created by allancalderon on 24/05/16.
 */
public class activitiesContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public static String COLUMN_NAME_NULLABLE = null;

    public activitiesContract() {}

    /* Inner class that defines the table contents */
    public static abstract class AssignedTimelineEntry implements BaseColumns {
        public static final String TABLE_NAME_ASSIGNTIMELINE = "assignExercise";
        public static final String COLUMN_NAME_ASSIGNTIMELINE_ID = "idAssignedTimeline";
        public static final String COLUMN_NAME_ASSIGNTIMELINE_IDEXERCISEFK = "idExerciseFK";
        public static final String COLUMN_NAME_ASSIGNTIMELINE_IDTYPEASSIGMENT = "type";
        public static final String COLUMN_NAME_ASSIGNTIMELINE_DAY = "day";
        public static final String COLUMN_NAME_ASSIGNTIMELINE_MONTH = "month";
        public static final String COLUMN_NAME_ASSIGNTIMELINE_YEAR = "year";
        public static final String COLUMN_NAME_ASSIGNTIMELINE_HOUR = "hour";
        public static final String COLUMN_NAME_ASSIGNTIMELINE_MINUTE = "minute";
        public static final String COLUMN_NAME_ASSIGNTIMELINE_GMTOFFSET = "gmtOffset";
        public static final String COLUMN_NAME_ASSIGNTIMELINE_LAT = "latitude";
        public static final String COLUMN_NAME_ASSIGNTIMELINE_LON = "longitude";
        public static final String COLUMN_NAME_ASSIGNTIMELINE_DESCRIPTION = "description";
        public static final String COLUMN_NAME_ASSIGNTIMELINE_AUDIOFILE = "audioFile";
        public static final String COLUMN_NAME_ASSIGNTIMELINE_TIMEDURATION = "timeDuration"; //in minutes
        public static final String COLUMN_NAME_ASSIGNTIMELINE_RATIO = "ratio";
    }

    /* Inner class that defines the table contents */
    public static abstract class ExerciseEntry implements BaseColumns {
        public static final String TABLE_NAME_EXERCISE = "exercise";
        public static final String COLUMN_NAME_EXERCISE_ID = "idExercises";
        public static final String COLUMN_NAME_EXERCISE_IDGROUPFK = "idGroupFK";
        public static final String COLUMN_NAME_EXERCISE_IDSUBGROUPFK = "idSubGroupFK";
        public static final String COLUMN_NAME_EXERCISE_IDTYPEEXERCISEFK = "idTypeExerciseFK";
        public static final String COLUMN_NAME_EXERCISE_IDSOURCE = "idSource";
        public static final String COLUMN_NAME_EXERCISE_SOURCETYPE = "sourceType";
        public static final String COLUMN_NAME_EXERCISE_TITLE = "title";
        public static final String COLUMN_NAME_EXERCISE_URLVIDEO = "urlVideo";
        public static final String COLUMN_NAME_EXERCISE_DESCRIPTION = "description";
        public static final String COLUMN_NAME_EXERCISE_OTHERINFO1 = "otherInfo1";
        public static final String COLUMN_NAME_EXERCISE_OTHERINFO2 = "otherInfo2";
        public static final String COLUMN_NAME_EXERCISE_OTHERINFO3 = "otherInfo3";
        public static final String COLUMN_NAME_EXERCISE_OTHERINFO4 = "otherInfo4";
        public static final String COLUMN_NAME_EXERCISE_HIDDEN = "isHidden";
        public static final String COLUMN_NAME_EXERCISE_FAVORITE = "favorite";
        public static final String COLUMN_NAME_EXERCISE_RATIO = "ratio";
    }

    /* Inner class that defines the table contents */
    public static abstract class ConstantsEntry implements BaseColumns {
        public static final String TABLE_NAME_CONSTANT = "constants";
        public static final String COLUMN_NAME_CONSTANT_VAL1 = "idExercises";
        public static final String COLUMN_NAME_CONSTANT_VAL2 = "idExercises";
        public static final String COLUMN_NAME_CONSTANT_VAL3 = "idExercises";
        public static final String COLUMN_NAME_CONSTANT_VAL4 = "idExercises";
        public static final String COLUMN_NAME_CONSTANT_VAL5 = "idExercises";
    }

}
