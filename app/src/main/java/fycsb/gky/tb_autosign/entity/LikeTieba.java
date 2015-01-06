package fycsb.gky.tb_autosign.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by codefu on 15-1-4.
 */
public class LikeTieba implements Serializable{
    private static final long serialVersionUID = 1343265225746842422L;
    @SerializedName("forum_list")
    private List<ForumList> forumList;
    @SerializedName("server_time")
    private String serverTime;
    private long time;
    private int ctime;
    private long logid;
    @SerializedName("error_code")
    private String errorCode;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<ForumList> getForumList() {
        return forumList;
    }

    public void setForumList(List<ForumList> forumList) {
        this.forumList = forumList;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getCtime() {
        return ctime;
    }

    public void setCtime(int ctime) {
        this.ctime = ctime;
    }

    public long getLogid() {
        return logid;
    }

    public void setLogid(long logid) {
        this.logid = logid;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}