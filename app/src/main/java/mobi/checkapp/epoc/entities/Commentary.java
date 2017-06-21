package mobi.checkapp.epoc.entities;

/**
 * Created by allancalderon on 16/05/16.
 */
public class Commentary {
    private int idComment;
    private int idUserFK;
    private String name;
    private String description;
    private String DateTime;
    private float ratio; //values between 1 and 5

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public int getIdUserFK() {
        return idUserFK;
    }

    public void setIdUserFK(int idUserFK) {
        this.idUserFK = idUserFK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
