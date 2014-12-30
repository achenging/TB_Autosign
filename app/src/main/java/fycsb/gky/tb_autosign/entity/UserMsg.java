package fycsb.gky.tb_autosign.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by codefu on 2014/12/27.
 */
public class UserMsg implements Serializable {
    private static final long serialVersionUID = -7441044278495064565L;
    private User user;
    private Anti   anti;
    @SerializedName("server_time")
    private String serverTime;
    private String ctime;
    private String logid;
    @SerializedName("error_code")
    private String errorCode;

    public Anti getAnti() {
        return anti;
    }

    public void setAnti(Anti anti) {
        this.anti = anti;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
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
