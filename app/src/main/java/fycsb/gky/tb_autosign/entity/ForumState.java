package fycsb.gky.tb_autosign.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by codefu on 15-1-3.
 */
public class ForumState implements Serializable {
    private static final long serialVersionUID = 1164057815599759423L;
    @SerializedName("forum_id")
    private String forumId;
    @SerializedName("forum_name")
    private String forumName;
    private String signed;
    @SerializedName("is_on")
    private String isOn;
    @SerializedName("is_filter")
    private String isFilter;
    @SerializedName("sign_day_count")
    private String signDayCount;
    @SerializedName("cur_score")
    private String cur_score;
    private Error  eroor;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public String getSigned() {
        return signed;
    }

    public void setSigned(String signed) {
        this.signed = signed;
    }

    public String getIsOn() {
        return isOn;
    }

    public void setIsOn(String isOn) {
        this.isOn = isOn;
    }

    public String getIsFilter() {
        return isFilter;
    }

    public void setIsFilter(String isFilter) {
        this.isFilter = isFilter;
    }

    public String getSignDayCount() {
        return signDayCount;
    }

    public void setSignDayCount(String signDayCount) {
        this.signDayCount = signDayCount;
    }

    public String getCur_score() {
        return cur_score;
    }

    public void setCur_score(String cur_score) {
        this.cur_score = cur_score;
    }

    public Error getEroor() {
        return eroor;
    }

    public void setEroor(Error eroor) {
        this.eroor = eroor;
    }

    @Override
    public String toString() {
        return forumName + "吧";
    }
}
