package mobi.checkapp.epoc.utils;

import android.graphics.Color;

/**
 * Created by allancalderon on 16/05/16.
 */
public class Constants {
    public static String URL_POLLUTION = "http://www.checkapp.mobi/webserver/pollution/index.php";
    public static String GOOGLE_KEY = "AIzaSyBJpZ1G9MbZGtJky-HQLHo_nUAaiIipFs8";
    public static final String LISTEXERCISEBUNDLE = "LISTEXERCISEBUNDLE";
    public static final String EXERCISEBUNDLE = "EXERCISEBUNDLE";
    public static String LISTEXERCISEDATA = "LISTEXERCISEDATA";
    public static String LISTTIMELINEASSIGNDATA = "LISTTIMELINEASSIGNDATA";
    public static String EXERCISEDATA = "EXERCISEDATA";
    public static String TIMELINEASSIGNDATA = "TIMELINEASSIGNDATA";
    public static int EXERCISEREQCODE = 1;
    public static int EXERCISEREQCODEACTIVITY = 2;
    public static int EXERCISEACTIVITYREQCODE = 3;
    public static int EXERCISEACTIVITYREQCODEDELETE = 4;
    public static int EXERCISEACTIVITYREQCODEEDIT = 5;
    public static int EXERCISEACTIVITYREQCODEADD = 6;

    public static final int EXERCISELISTTABRESUME = 0;
    public static final int EXERCISELISTTABRECOMMEND = 1;
    public static final int EXERCISELISTTABMYEXERCISE = 2;
    public static final int EXERCISELISTTABTIMELINE = 3;
    //public static final int EXERCISELISTTABFAVORITES = 4;

    // activitiesDb definitions
    public static final int DATABASE_ACTIVITIES_VERSION = 3;
    public static final String DATABASE_ACTIVITIES_NAME = "Activities.db";

    public static String SOURCERECOMMENDATEDEJERCISEXML =  "XMLREC";
    public static String SOURCERECOMMENDATEDEJERCISEUSER =  "USERREC";
    public static final int[] colorsPollutantsLevel = new int[] {
            Color.GRAY,                    //id 0
            Color.GREEN,                   //id 1
            Color.YELLOW,                  //id 2
            Color.rgb(255,165,0),          //id 3, orange
            Color.RED,                     //id 4
            Color.rgb(153,0,153)           //id 5, dark magenta
    };

}
