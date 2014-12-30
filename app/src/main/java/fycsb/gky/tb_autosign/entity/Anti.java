package fycsb.gky.tb_autosign.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by codefu on 2014/12/27.
 */
public class Anti implements Serializable {
    private static final long serialVersionUID = 1971440814804386808L;
    private String tbs;
    @SerializedName("need_vcode")
    private String needVcode;
    @SerializedName("vcode_md5")
    private String vcodeMd5;
    @SerializedName("vcode_pic_url")
    private String vcodePicUrl;

    public String getNeedVcode() {
        return needVcode;
    }

    public void setNeedVcode(String needVcode) {
        this.needVcode = needVcode;
    }

    public String getVcodeMd5() {
        return vcodeMd5;
    }

    public void setVcodeMd5(String vcodeMd5) {
        this.vcodeMd5 = vcodeMd5;
    }

    public String getVcodePicUrl() {
        return vcodePicUrl;
    }

    public void setVcodePicUrl(String vcodePicUrl) {
        this.vcodePicUrl = vcodePicUrl;
    }

    public String getTbs() {
        return tbs;
    }

    public void setTbs(String tbs) {
        this.tbs = tbs;
    }
}
