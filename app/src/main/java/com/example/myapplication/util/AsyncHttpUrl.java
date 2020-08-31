package com.example.myapplication.util;

public class AsyncHttpUrl {

    private String deviceSelectUrl;

    private String loginUrl;

    private String registerUrl;

    private String selectAbnormalUrl;

    private String selectAllShopName;

    private String selectDataFromShopName;

    private String shopStatisticalUrl;

    private String upLoadUrl;

    private String updateUserDevice;

    public String getDeviceSelectUrl() { return "http://122.14.217.184:8081/ZFCX/app/selectByString.action"; }

    public String getLoginUrl() { return "http://122.14.217.184:8081/ZFCX/app/login.action"; }

    public String getRegisterUrl() { return "http://122.14.217.184:8081/ZFCX/app/register.action"; }

    public String getSelectAbnormalUrl() { return "http://122.14.217.184:8081/ZFCX/admin/paySelect/shop/order/selectAllOrderBySearch.action"; }

    public String getSelectAllShopName() { return "http://122.14.217.184:8081/ZFCX/app/getInfo.action"; }

    public String getSelectDataFromShopName() { return "http://122.14.217.184:8081/ZFCX/admin/paySelect/shop/order/countNumber.action"; }

    public String getShopStatisticalUrl() { return "http://122.14.217.184:8081/ZFCX/admin/paySelect/shop/order/selectDshopOrder.action"; }

    public String getUpLoadUrl() { return "http://123.58.7.107:8081/APPMANAGE/app/uploadapp.action"; }

    public String getUpdateUserDevice() {
        return "http://122.14.217.184:8081/ZFCX/app/updateNUMBER.action";
    }
}
//122.14.217.184:8081/ZFCX