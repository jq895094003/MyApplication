package com.example.myapplication.Bean;

public class Fuser {
    private String fshopNo;               //商铺编号

    private String fuserHeadimg;          //

    private int fuserId;                  //用户id

    private String fuserIsconditions;     //是否审核

    private String fuserName;             //用户姓名

    private String fuserNo;               //用户编号

    private String fuserPassword;         //用户密码

    private String fuserRole;             //用户角色

    private Object fuserRoleName;         //角色名称

    private String fuserShopName;         //用户商铺名称

    private String fuserTel;              //用户手机号

    public String getFshopNo() { return this.fshopNo; }

    public String getFuserHeadimg() { return this.fuserHeadimg; }

    public int getFuserId() { return this.fuserId; }

    public String getFuserIsconditions() { return this.fuserIsconditions; }

    public String getFuserName() { return this.fuserName; }

    public String getFuserNo() { return this.fuserNo; }

    public String getFuserPassword() { return this.fuserPassword; }

    public String getFuserRole() { return this.fuserRole; }

    public Object getFuserRoleName() { return this.fuserRoleName; }

    public String getFuserShopName() { return this.fuserShopName; }

    public String getFuserTel() { return this.fuserTel; }

    public void setFshopNo(String paramString) { this.fshopNo = paramString; }

    public void setFuserHeadimg(String paramString) { this.fuserHeadimg = paramString; }

    public void setFuserId(int paramInt) { this.fuserId = paramInt; }

    public void setFuserIsconditions(String paramString) { this.fuserIsconditions = paramString; }

    public void setFuserName(String paramString) { this.fuserName = paramString; }

    public void setFuserNo(String paramString) { this.fuserNo = paramString; }

    public void setFuserPassword(String paramString) { this.fuserPassword = paramString; }

    public void setFuserRole(String paramString) { this.fuserRole = paramString; }

    public void setFuserRoleName(Object paramObject) { this.fuserRoleName = paramObject; }

    public void setFuserShopName(String paramString) { this.fuserShopName = paramString; }

    public void setFuserTel(String paramString) { this.fuserTel = paramString; }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fuser{fuserId=");
        stringBuilder.append(this.fuserId);
        stringBuilder.append(", fuserName='");
        stringBuilder.append(this.fuserName);
        stringBuilder.append('\'');
        stringBuilder.append(", fuserNo='");
        stringBuilder.append(this.fuserNo);
        stringBuilder.append('\'');
        stringBuilder.append(", fuserPassword='");
        stringBuilder.append(this.fuserPassword);
        stringBuilder.append('\'');
        stringBuilder.append(", fuserIsconditions='");
        stringBuilder.append(this.fuserIsconditions);
        stringBuilder.append('\'');
        stringBuilder.append(", fuserHeadimg='");
        stringBuilder.append(this.fuserHeadimg);
        stringBuilder.append('\'');
        stringBuilder.append(", fshopNo='");
        stringBuilder.append(this.fshopNo);
        stringBuilder.append('\'');
        stringBuilder.append(", fuserTel='");
        stringBuilder.append(this.fuserTel);
        stringBuilder.append('\'');
        stringBuilder.append(", fuserRole='");
        stringBuilder.append(this.fuserRole);
        stringBuilder.append('\'');
        stringBuilder.append(", fuserRoleName=");
        stringBuilder.append(this.fuserRoleName);
        stringBuilder.append(", fuserShopName='");
        stringBuilder.append(this.fuserShopName);
        stringBuilder.append('\'');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
