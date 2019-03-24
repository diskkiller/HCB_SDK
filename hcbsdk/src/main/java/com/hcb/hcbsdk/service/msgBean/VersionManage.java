package com.hcb.hcbsdk.service.msgBean;

public class VersionManage {

    //设备号
    private String snCode;
    //版本号
    private String versionNo;
    //版本名称
    private String name;
    //下载地址
    private String url;
    //省份id
    private Long provinceId;
    //市id
    private Long cityId;
    //县id
    private Long countyId;
    //备注内容
    private String cntn;
    //类型（备用）
    private Integer type;
    //状态（备用）
    private Integer status;
    //操作用户
    private String actUser;

    public String getSnCode() {
        return snCode;
    }

    public void setSnCode(String snCode) {
        this.snCode = snCode;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getCountyId() {
        return countyId;
    }

    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    public String getCntn() {
        return cntn;
    }

    public void setCntn(String cntn) {
        this.cntn = cntn;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getActUser() {
        return actUser;
    }

    public void setActUser(String actUser) {
        this.actUser = actUser;
    }
}
