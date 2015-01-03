package fycsb.gky.tb_autosign.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by codefu on 15-1-3.
 */
public class SignState implements Serializable {

    private static final long serialVersionUID = -1965964877726530129L;

    private List<ForumState> info;
    @SerializedName("show_dialog")
    private String           showDialog;
    @SerializedName("sign_notice")
    private String           signNotice;
    @SerializedName("is_timeout")
    private String           isTimeOut;
    @SerializedName("timeout_notice")
    private String           timeout_notice;
    private Error            error;
    private String           errorCode;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<ForumState> getInfo() {
        return info;
    }

    public void setInfo(List<ForumState> info) {
        this.info = info;
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

    public String getIsTimeOut() {
        return isTimeOut;
    }

    public void setIsTimeOut(String isTimeOut) {
        this.isTimeOut = isTimeOut;
    }

    public String getTimeout_notice() {
        return timeout_notice;
    }

    public void setTimeout_notice(String timeout_notice) {
        this.timeout_notice = timeout_notice;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
