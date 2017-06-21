package mobi.checkapp.epoc.entities;

/**
 * Created by allancalderon on 30/05/16.
 */
public class Pollutant {
    private int id;
    private String name;
    private String formula;
    private String unit;
    private String description;
    private double limitGood;                //AQI 0-50
    private double limitSatisfactory;        //AQI 51-100
    private double limitModerately;          //AQI 101-150
    private double limitPoor;                //AQI 151-200
    private double limitVeryPoor;            //AQI 201-300

    public Pollutant(int id, String name, String formula, String unit, String description){
        this.id = id;
        this.name = name;
        this.formula = formula;
        this.unit = unit;
        this.description = description;
        this.setLimitGood(-1);
        this.setLimitSatisfactory(-1);
        this.setLimitModerately(-1);
        this.setLimitPoor(-1);
        this.setLimitVeryPoor(-1);
    }
    public Pollutant(int id, String name, String formula, String unit, String description,
                     double limitGood, double limitSatisfactory, double limitModerately, double limitPoor, double limitVeryPoor){
        this.id = id;
        this.name = name;
        this.formula = formula;
        this.unit = unit;
        this.setLimitGood(limitGood);
        this.description = description;
        this.setLimitSatisfactory(limitSatisfactory);
        this.setLimitModerately(limitModerately);
        this.setLimitPoor(limitPoor);
        this.setLimitVeryPoor(limitVeryPoor);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public double getLimitGood() {
        return limitGood;
    }

    public void setLimitGood(double limitGood) {
        this.limitGood = limitGood;
    }

    public double getLimitSatisfactory() {
        return limitSatisfactory;
    }

    public void setLimitSatisfactory(double limitSatisfactory) {
        this.limitSatisfactory = limitSatisfactory;
    }

    public double getLimitModerately() {
        return limitModerately;
    }

    public void setLimitModerately(double limitModerately) {
        this.limitModerately = limitModerately;
    }

    public double getLimitPoor() {
        return limitPoor;
    }

    public void setLimitPoor(double limitPoor) {
        this.limitPoor = limitPoor;
    }

    public double getLimitVeryPoor() {
        return limitVeryPoor;
    }

    public void setLimitVeryPoor(double limitVeryPoor) {
        this.limitVeryPoor = limitVeryPoor;
    }
}
