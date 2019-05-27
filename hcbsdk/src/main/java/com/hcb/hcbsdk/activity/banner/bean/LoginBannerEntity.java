package com.hcb.hcbsdk.activity.banner.bean;

/**
 * Create by ShuHeMing on 18/6/8
 */
public class LoginBannerEntity {
    private long id;
    private String url;
    private int type;
    private String redirectUrl;
    private int imageid;

    public LoginBannerEntity(int imageid) {
        this.imageid = imageid;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
