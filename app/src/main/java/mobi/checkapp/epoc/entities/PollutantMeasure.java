package mobi.checkapp.epoc.entities;

import java.util.Calendar;

/**
 * Created by allancalderon on 30/05/16.
 */
public class PollutantMeasure {
    private int year;
    private int month;
    private int day;
    private int hour;
    private Pollutant pollutant;
    private double value;
    private Calendar lastUpdate;

    public PollutantMeasure(Pollutant pollutant, double value, int year, int month, int day, int hour){
        this.pollutant = pollutant;
        this.value = value;
        this.setYear(year);
        this.setMonth(month);
        this.setDay(day);
        this.setHour(hour);
    }

    public Pollutant getPollutant() {
        return pollutant;
    }

    public void setPollutant(Pollutant pollutant) {
        this.pollutant = pollutant;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public Calendar getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Calendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
