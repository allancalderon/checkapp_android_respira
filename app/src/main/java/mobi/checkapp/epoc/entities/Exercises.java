package mobi.checkapp.epoc.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by allancalderon on 16/05/16.
 */
public class Exercises implements Serializable {
    private long idExercises=-1;
    private int idGroupFK=-1;
    private int idSubGroupFK=-1;
    private int idTypeExerciseFK =-1;
    private String idSource;       //0 for prerecorded exercise and 1 for exercise add by the user
    private String sourceType;
    private String title="";
    private String urlVideo="";
    private String description="";
    private String otherInfo1="";
    private String otherInfo2="";
    private String otherInfo3="";
    private String otherInfo4="";
    private boolean favorite=false;
    private boolean hidden=false;
    private List<Commentary> listCommentary = null;
    private float ratio=0; //values between 1 and 5

    public Exercises(int idExercise, int group, String title, String urlVideo, String description){
        this.idExercises = idExercise;
        this.idGroupFK = group;
        this.title = title;
        this.urlVideo = urlVideo;
        this.description = description;
    }

    public Exercises(){
    }

    public long getIdExercises() {
        return idExercises;
    }

    public void setIdExercises(long idExercises) {
        this.idExercises = idExercises;
    }

    public int getIdGroupFK() {
        return idGroupFK;
    }

    public void setIdGroupFK(int idGroupFK) {
        this.idGroupFK = idGroupFK;
    }

    public int getIdSubGroupFK() {
        return idSubGroupFK;
    }

    public void setIdSubGroupFK(int idSubGroupFK) {
        this.idSubGroupFK = idSubGroupFK;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }



    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public int getIdTypeExerciseFK() {
        return idTypeExerciseFK;
    }

    public void setIdTypeExerciseFK(int idTypeExerciseFK) {
        this.idTypeExerciseFK = idTypeExerciseFK;
    }

    public List<Commentary> getListCommentary() {
        return listCommentary;
    }

    public void setListCommentary(List<Commentary> listCommentary) {
        this.listCommentary = listCommentary;
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

    public String getOtherInfo4() {
        return otherInfo4;
    }

    public void setOtherInfo4(String otherInfo4) {
        this.otherInfo4 = otherInfo4;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    public String getIdSource() {
        return idSource;
    }

    public void setIdSource(String idSource) {
        this.idSource = idSource;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceType() {
        return this.sourceType;
    }
}
