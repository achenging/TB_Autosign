package fycsb.gky.tb_autosign.entity;

import java.io.Serializable;

/**
 * Created by codefu on 2014/12/27.
 */
public class Error implements Serializable{
    private static final long serialVersionUID = 6304198366481122399L;
    private String errno;
    private String errmsg;
    private String usermsg;

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getUsermsg() {
        return usermsg;
    }

    public void setUsermsg(String usermsg) {
        this.usermsg = usermsg;
    }
}
