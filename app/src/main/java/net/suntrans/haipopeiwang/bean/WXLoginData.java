package net.suntrans.haipopeiwang.bean;

/**
 * Created by Looney on 2018/8/17.
 * Des:
 */
public class WXLoginData {

    /**
     * access_token : 12_ht8q7MjZnGqzv25poZBUKLDS_XZ6vnhGE-hqCb1DdIAm2OHAvHzUV_1iTnnumESB4B1l5hmCfNgP_Co1_XWZyGRDl5E698ulnXbpQ8ukPps
     * expires_in : 7200
     * refresh_token : 12_9RfAywksCmOkMsMnRdoiCCNgvl47nfRqDi0C032Qs5g1QZfQCY_TmjrtpurCT3jesMWNfA921HsKThN9Sgk0y3MMXSoIsdj__G1FRRa0L4c
     * openid : opEvI1a27EYW8DJkiKjWC39m-v1A
     * scope : snsapi_userinfo
     * unionid : or0ahwAqHWXl_fdtlIOf3xMC22WE
     */

    public String access_token;
    public long expires_in;
    public String refresh_token;
    public String openid;
    public String scope;
    public String unionid;

    @Override
    public String toString() {
        return "WXLoginData{" +
                "access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", refresh_token='" + refresh_token + '\'' +
                ", openid='" + openid + '\'' +
                ", scope='" + scope + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
