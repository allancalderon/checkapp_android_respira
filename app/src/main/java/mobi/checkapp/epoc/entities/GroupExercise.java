package mobi.checkapp.epoc.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allancalderon on 31/05/16.
 */
public class GroupExercise {

    private int id;
    private String name;
    private List<GroupExercise> subGroup;

    public GroupExercise(){
        this.subGroup = new ArrayList<>();
    }

    public GroupExercise(int id, String name){
        this.id = id;
        this.name = name;
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

    public List<GroupExercise> getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(List<GroupExercise> subGroup) {
        this.subGroup = subGroup;
    }

    @Override
    public String toString() {
        return getName();            // What to display in the Spinner list.
    }
}
