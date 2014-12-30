package fycsb.gky.tb_autosign.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by codefu on 2014/12/27.
 */
public class ForumInfo implements Serializable{
    private static final long serialVersionUID = -5894121336456133430L;
    @SerializedName("forum_id")
    private String forumId;
    @SerializedName("forum_name")
    private String forumName;
    @SerializedName("user_level")
    private String userLevel;
    @SerializedName("user_exp")
    private String userExp;
    @SerializedName("need_exp")
    private String needExp;
    @SerializedName("is_sign_in")
    private String isSignIn;
    @SerializedName("cont_sign_num")
    private String contSignNum;
    private String avatar;

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

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserExp() {
        return userExp;
    }

    public void setUserExp(String userExp) {
        this.userExp = userExp;
    }

    public String getIsSignIn() {
        return isSignIn;
    }

    public void setIsSignIn(String isSignIn) {
        this.isSignIn = isSignIn;
    }

    public String getContSignNum() {
        return contSignNum;
    }

    public void setContSignNum(String contSignNum) {
        this.contSignNum = contSignNum;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNeedExp() {
        return needExp;
    }

    public void setNeedExp(String needExp) {
        this.needExp = needExp;
    }
}
