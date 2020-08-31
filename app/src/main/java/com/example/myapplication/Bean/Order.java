package com.example.myapplication.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order implements Parcelable, Serializable {
  /**
   * 具体这一段不知道干什么的
   * 看着像是将对象继承Parcelable，Serializable类并返回，实现可传递对象
   */
  public static final Creator<Order> CREATOR = new Creator<Order>() {
      public Order createFromParcel(Parcel param1Parcel) { return new Order(param1Parcel); }
      
      public Order[] newArray(int param1Int) { return new Order[param1Int]; }
    };
  
  private Object dcheck;             //
  
  private long ddate;                //日期
  
  private Object ddateEnd;           //结束日期
  
  private Object ddateStart;         //开始日期
  
  private String ddh;                //
  
  private String dmdbh;              //门店编号
  
  private String dmoney;             //订单金额
  
  private Object dposno;             //pos机编号
  
  private String dshopname;          //商铺名称
  
  private String dstate;             //订单状态
  
  private String dworkerno;          //收银员编号
  
  private String dwxid;              //微信id
  
  private String dzfbid;             //支付宝id
  
  private String dzftype;            //支付类型

  private Double rate;               //费率
  
  public Order() {}
  
  public Order(Parcel paramParcel) {
    this.dwxid = paramParcel.readString();
    this.dzfbid = paramParcel.readString();
    this.dmdbh = paramParcel.readString();
    this.ddh = paramParcel.readString();
    this.ddate = paramParcel.readLong();
    if (paramParcel.readByte() == 0) {
      this.dmoney = null;
    } else {
      this.dmoney = paramParcel.readString();
    } 
    this.dzftype = paramParcel.readString();
    this.dstate = paramParcel.readString();
    this.dworkerno = paramParcel.readString();
    this.dshopname = paramParcel.readString();
    this.rate = paramParcel.readDouble();
  }
  
  public int describeContents() { return 0; }
  
  public Object getDcheck() { return this.dcheck; }
  
  public String getDdate() { return stampToDate(this.ddate); }
  
  public Object getDdateEnd() { return this.ddateEnd; }
  
  public Object getDdateStart() { return this.ddateStart; }
  
  public String getDdh() { return this.ddh; }
  
  public String getDmdbh() { return this.dmdbh; }
  
  public String getDmoney() { return String.valueOf(this.dmoney); }
  
  public Object getDposno() { return this.dposno; }
  
  public String getDshopname() { return this.dshopname; }
  
  public String getDstate() { return this.dstate; }
  
  public String getDworkerno() { return this.dworkerno; }
  
  public String getDwxid() { return this.dwxid; }
  
  public String getDzfbid() { return this.dzfbid; }
  
  public String getDzftype() { return this.dzftype; }
  
  public void setDcheck(Object paramObject) { this.dcheck = paramObject; }
  
  public void setDdate(long paramLong) { this.ddate = paramLong; }
  
  public void setDdateEnd(Object paramObject) { this.ddateEnd = paramObject; }
  
  public void setDdateStart(Object paramObject) { this.ddateStart = paramObject; }
  
  public void setDdh(String paramString) { this.ddh = paramString; }
  
  public void setDmdbh(String paramString) { this.dmdbh = paramString; }
  
  public void setDmoney(String paramString) { this.dmoney = paramString; }
  
  public void setDposno(Object paramObject) { this.dposno = paramObject; }
  
  public void setDshopname(String paramString) { this.dshopname = paramString; }
  
  public void setDstate(String paramString) { this.dstate = paramString; }
  
  public void setDworkerno(String paramString) { this.dworkerno = paramString; }
  
  public void setDwxid(String paramString) { this.dwxid = paramString; }
  
  public void setDzfbid(String paramString) { this.dzfbid = paramString; }
  
  public void setDzftype(String paramString) { this.dzftype = paramString; }
  
  public String stampToDate(long paramLong) { return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(paramLong)); }

  public Double getRate() {
    return rate;
  }

  public void setRate(Double rate) {
    this.rate = rate;
  }

  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Order{dwxid='");
    stringBuilder.append(this.dwxid);
    stringBuilder.append('\'');
    stringBuilder.append(", dzfbid='");
    stringBuilder.append(this.dzfbid);
    stringBuilder.append('\'');
    stringBuilder.append(", dmdbh='");
    stringBuilder.append(this.dmdbh);
    stringBuilder.append('\'');
    stringBuilder.append(", ddh='");
    stringBuilder.append(this.ddh);
    stringBuilder.append('\'');
    stringBuilder.append(", ddate=");
    stringBuilder.append(this.ddate);
    stringBuilder.append(", dmoney='");
    stringBuilder.append(this.dmoney);
    stringBuilder.append('\'');
    stringBuilder.append(", dzftype='");
    stringBuilder.append(this.dzftype);
    stringBuilder.append('\'');
    stringBuilder.append(", dstate='");
    stringBuilder.append(this.dstate);
    stringBuilder.append('\'');
    stringBuilder.append(", dcheck=");
    stringBuilder.append(this.dcheck);
    stringBuilder.append(", dworkerno='");
    stringBuilder.append(this.dworkerno);
    stringBuilder.append('\'');
    stringBuilder.append(", dshopname='");
    stringBuilder.append(this.dshopname);
    stringBuilder.append('\'');
    stringBuilder.append(", ddateStart=");
    stringBuilder.append(this.ddateStart);
    stringBuilder.append(", ddateEnd=");
    stringBuilder.append(this.ddateEnd);
    stringBuilder.append(", dposno=");
    stringBuilder.append(this.dposno);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeString(this.dwxid);
    paramParcel.writeString(this.dzfbid);
    paramParcel.writeString(this.dmdbh);
    paramParcel.writeString(this.ddh);
    paramParcel.writeLong(this.ddate);
    if (this.dmoney == null) {
      paramParcel.writeByte((byte)0);
    } else {
      paramParcel.writeByte((byte)1);
      paramParcel.writeString(this.dmoney);
    } 
    paramParcel.writeString(this.dzftype);
    paramParcel.writeString(this.dstate);
    paramParcel.writeString(this.dworkerno);
    paramParcel.writeString(this.dshopname);
  }
}


/* Location:              C:\Users\SYQ\Desktop\支付查询_classes_dex2jar.jar!\com\smrj\myapplication\bean\Order.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.2
 */