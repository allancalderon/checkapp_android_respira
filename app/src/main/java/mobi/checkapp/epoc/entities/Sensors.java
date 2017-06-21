package mobi.checkapp.epoc.entities;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by allancalderon on 16/05/16.
*/
public class Sensors {
    private int idsensor;
    private String name;
    private String addressName;
    private LatLng position;
    private String otherInfo1;
    private String otherInfo2;
    private String source;
    private List<Integer> type;
    private List<PollutantMeasure> listPollutantMeasure;
    private int maxLevel;        //Level 1: AQI 0-50, Level 2: AQI 51-150, Level 3: AQI 151-200, Level 4: AQI 201-300, Level 5: AQI > 301

    public int getIdsensor() {
        return idsensor;
    }

    public void setIdsensor(int idsensor) {
        this.idsensor = idsensor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getOtherInfo1() {
        return otherInfo1;
    }

    public void setOtherInfo1(String otherInfo1) {
        this.otherInfo1 = otherInfo1;
    }

    public String getOtherInfo2() {
        return otherInfo2;
    }

    public void setOtherInfo2(String otherInfo2) {
        this.otherInfo2 = otherInfo2;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Integer> getType() {
        return type;
    }

    public void setType(List<Integer> type) {
        this.type = type;
    }

    public List<PollutantMeasure> getListPollutantMeasure() {
        return listPollutantMeasure;
    }

    public void setListPollutantMeasure(List<PollutantMeasure> listPollutantMeasure) {
        this.listPollutantMeasure = listPollutantMeasure;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }
}