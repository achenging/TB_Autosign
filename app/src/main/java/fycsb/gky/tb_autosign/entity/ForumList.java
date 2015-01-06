package fycsb.gky.tb_autosign.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by codefu on 15-1-4.
 */
public class ForumList implements Serializable{
    private static final long serialVersionUID = -8856495097328475076L;
    private String id;
    private String name;
    @SerializedName("favo_type")
    private String favoType;
    @SerializedName("level_id")
    private String levelId;
    @SerializedName("level_name")
    private String levelName;
    @SerializedName("cur_score")
    private String curScore;
    @SerializedName("level_score")
    private String levelScore;
    private String avator;
    private String slogan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFavoType() {
        return favoType;
    }

    public void setFavoType(String favoType) {
        this.favoType = favoType;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getCurScore() {
        return curScore;
    }

    public void setCurScore(String curScore) {
        this.curScore = curScore;
    }

    public String getLevelScore() {
        return levelScore;
    }

    public void setLevelScore(String levelScore) {
        this.levelScore = levelScore;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }
}
