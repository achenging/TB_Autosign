package fycsb.gky.tb_autosign.entity;

import java.io.Serializable;

/**
 * Created by codefu on 2014/12/27.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 2052625984472849844L;

    private String id;
    private String name;
    private String BDUSS;
    private String passwd;
    private String portrait;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public String getBDUSS() {
        return BDUSS;
    }

    public void setBDUSS(String BDUSS) {
        this.BDUSS = BDUSS;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
