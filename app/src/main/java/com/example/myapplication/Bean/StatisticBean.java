package com.example.myapplication.Bean;

public class StatisticBean {
  private String wxSK;    //微信收款总额
  
  private String wxTK;    //微信退款总额
  
  private String ygBH;    //分店编号
  
  private String zfSB;    //支付宝支付失败总额
  
  private String zfbSK;   //支付宝收款总额
  
  private String zfbTK;   //支付宝退款总额

  private String rate;    //费率总额
  
  public String getWxSK() { return this.wxSK; }
  
  public String getWxTK() { return this.wxTK; }
  
  public String getYgBH() { return this.ygBH; }
  
  public String getZfSB() { return this.zfSB; }
  
  public String getZfbSK() { return this.zfbSK; }
  
  public String getZfbTK() { return this.zfbTK; }
  
  public void setWxSK(String paramString) { this.wxSK = paramString; }
  
  public void setWxTK(String paramString) { this.wxTK = paramString; }
  
  public void setYgBH(String paramString) { this.ygBH = paramString; }
  
  public void setZfSB(String paramString) { this.zfSB = paramString; }
  
  public void setZfbSK(String paramString) { this.zfbSK = paramString; }
  
  public void setZfbTK(String paramString) { this.zfbTK = paramString; }

  public String getRate() {
    return rate;
  }

  public void setRate(String rate) {
    this.rate = rate;
  }

  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("StatisticBean{ygBH='");
    stringBuilder.append(this.ygBH);
    stringBuilder.append('\'');
    stringBuilder.append(", wxSK='");
    stringBuilder.append(this.wxSK);
    stringBuilder.append('\'');
    stringBuilder.append(", wxTK='");
    stringBuilder.append(this.wxTK);
    stringBuilder.append('\'');
    stringBuilder.append(", zfbSK='");
    stringBuilder.append(this.zfbSK);
    stringBuilder.append('\'');
    stringBuilder.append(", zfbTK='");
    stringBuilder.append(this.zfbTK);
    stringBuilder.append('\'');
    stringBuilder.append(", zfSB='");
    stringBuilder.append(this.zfSB);
    stringBuilder.append('\'');
    stringBuilder.append(", rate='");
    stringBuilder.append(this.rate);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\SYQ\Desktop\支付查询_classes_dex2jar.jar!\com\smrj\myapplication\bean\StatisticBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.2
 */