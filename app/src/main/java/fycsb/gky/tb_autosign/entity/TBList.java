package fycsb.gky.tb_autosign.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by codefu on 2014/12/27.
 */
public class TBList implements Serializable {
    private static final long serialVersionUID = -5649306527668993984L;

    private Error          error;

    @SerializedName("forum_info")
    private List<ForumInfo> forumInfo;
    @SerializedName("show_dialog")
    private String         showDialog;
    @SerializedName("sign_notice")
    private String         signNotice;
    private String         title;
    @SerializedName("text_pre")
    private String         textPre;
    @SerializedName("text_color")
    private String         textColor;
    @SerializedName("text_mid")
    private String         textMid;
    @SerializedName("text_suf")
    private String         textSuf;
    @SerializedName("num_notice")
    private String         numNotice;
    private String         level;
    @SerializedName("sign_max_num")
    private String         signMaxNum;
    private String         valid;
    @SerializedName("msign_step_num")
    private String         msignStepNum;
    @SerializedName("server_time")
    private String         serverTime;
    private String         time;
    private String         ctime;
    private String         logid;
    @SerializedName("error_code")
    private String         errorCode;


    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public List<ForumInfo> getForumInfo() {
        return forumInfo;
    }

    public void setForumInfo(List<ForumInfo> forumInfo) {
        this.forumInfo = forumInfo;
    }

    public String getShowDialog() {
        return showDialog;
    }

    public void setShowDialog(String showDialog) {
        this.showDialog = showDialog;
    }

    public String getSignNotice() {
        return signNotice;
    }

    public void setSignNotice(String signNotice) {
        this.signNotice = signNotice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextPre() {
        return textPre;
    }

    public void setTextPre(String textPre) {
        this.textPre = textPre;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getTextMid() {
        return textMid;
    }

    public void setTextMid(String textMid) {
        this.textMid = textMid;
    }

    public String getTextSuf() {
        return textSuf;
    }

    public void setTextSuf(String textSuf) {
        this.textSuf = textSuf;
    }

    public String getNumNotice() {
        return numNotice;
    }

    public void setNumNotice(String numNotice) {
        this.numNotice = numNotice;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSignMaxNum() {
        return signMaxNum;
    }

    public void setSignMaxNum(String signMaxNum) {
        this.signMaxNum = signMaxNum;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getMsignStepNum() {
        return msignStepNum;
    }

    public void setMsignStepNum(String msignStepNum) {
        this.msignStepNum = msignStepNum;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getLogid() {
        return logid;
    }

    public void setLogid(String logid) {
        this.logid = logid;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
