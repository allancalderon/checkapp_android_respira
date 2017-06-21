package mobi.checkapp.epoc.entities;

import java.io.Serializable;

/**
 * Created by allancalderon on 24/05/16.
 */
public class TimelineAssign implements Serializable {
    private long idAssignedTimeline;
    private long idExerciseFK;
    private int idTypeAssignment;      //1 for exercise and 2 for annotation
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;
    private int gmtOffset;
    private double latitude;
    private double longitude;
    private String description;
    private String audioFile;
    private double timeDuration; //in minutes, only for exercise
    private double ratio;

    public long getIdAssignedTimeline() {
        return idAssignedTimeline;
    }

    public void setIdAssignedTimeline(long idAssignedTimeline) {
        this.idAssignedTimeline = idAssignedTimeline;
    }

    public long getIdExerciseFK() {
        return idExerciseFK;
    }

    public void setIdExerciseFK(long idExerciseFK) {
        this.idExerciseFK = idExerciseFK;
    }

    public int getIdTypeAssignment() {
        return idTypeAssignment;
    }

    public void setIdTypeAssignment(int idTypeAssignment) {
        this.idTypeAssignment = idTypeAssignment;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getGmtOffset() {
        return gmtOffset;
    }

    public void setGmtOffset(int gmtOffset) {
        this.gmtOffset = gmtOffset;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public double getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(double timeDuration) {
        this.timeDuration = timeDuration;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}
