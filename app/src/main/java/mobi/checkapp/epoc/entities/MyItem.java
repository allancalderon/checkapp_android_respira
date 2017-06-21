package mobi.checkapp.epoc.entities;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.List;

/**
 * Created by allancalderon on 23/05/16.
 */
public class MyItem implements ClusterItem {
    private String idPosition;
    private String name;
    private String addressName;
    private LatLng position;
    private String otherInfo1;
    private String otherInfo2;
    private String otherInfo3;
    private String dateTime;
    private String source;
    private List<Integer> types = null;

    public MyItem() {

    }
    public MyItem(double lat, double lng) {
        this.setPosition(new LatLng(lat, lng));
    }

    public MyItem(double lat, double lng, String name, String addressName) {
        this.setPosition(new LatLng(lat, lng));
        this.setName(name);
        this.setAddressName(addressName);
    }

    public MyItem(double lat, double lng, String name, String addressName, List<Integer> types,
                  String otherInfo1, String otherInfo2, String otherInfo3, String dateTime, String source) {
        this.setPosition(new LatLng(lat, lng));
        this.setName(name);
        this.setAddressName(addressName);
        this.setOtherInfo1(otherInfo1);
        this.setOtherInfo2(otherInfo2);
        this.setOtherInfo3(otherInfo3);
        this.setDateTime(dateTime);
        this.setSource(source);
        this.setTypes(types);
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getIdPosition() {
        return idPosition;
    }

    public void setIdPosition(String idPosition) {
        this.idPosition = idPosition;
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

    public String getOtherInfo3() {
        return otherInfo3;
    }

    public void setOtherInfo3(String otherInfo3) {
        this.otherInfo3 = otherInfo3;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Integer> getTypes() {
        return types;
    }

    public void setTypes(List<Integer> types) {
        this.types = types;
    }
}
