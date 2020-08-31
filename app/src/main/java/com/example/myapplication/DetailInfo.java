package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Bean.Order;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import cz.msebera.android.httpclient.Header;

public class DetailInfo extends AppCompatActivity {
    private Button button;//声明查询按钮

    private TextView ddate;//声明日期文本框

    private TextView ddh;//声明店铺文本框

    private TextView dmdbh;//声明店铺编号文本框

    private TextView dmoney;//声明金额文本框

    private TextView dshopname;//声明店铺名称文本框

    private TextView dstate;//声明交易状态文本框

    private TextView workerno;//声明员工编号文本框

    final String wxUrl = "http://www.shangmengsoft.com/PAY/WXPAY/example/orderqueryjava.php";//声明查询微信数据URL

    final String zfbUrl = "http://www.shangmengsoft.com/PAY/F2F-PAY/f2fpay/queryjava.php";//声明查询支付宝数据URL；
    //查询方法
    private void asyncHttpClient(String paramString, Order paramOrder) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();//声明一个请求框架
        asyncHttpClient.setEnableRedirects(true);//设置请求可以被转发
        RequestParams requestParams = new RequestParams();//声明参数对象
        requestParams.put("out_trade_no", this.ddh.getText().toString());//放置参数
        if (paramOrder.getDzftype().equals("ZFBZF")) {
            requestParams.put("AuthToken", paramOrder.getDzfbid());
        } else if (paramOrder.getDzftype().equals("WXZF")) {
            requestParams.put("sub_mch_id", paramOrder.getDwxid());
        }
        asyncHttpClient.post(paramString, requestParams, (ResponseHandlerInterface)new AsyncHttpResponseHandler() {//发起请求
            //失败方法
            public void onFailure(int param1Int, Header[] param1ArrayOfHeader, byte[] param1ArrayOfbyte, Throwable param1Throwable) {
                System.out.println("_____________________________________");
                System.out.println(param1ArrayOfbyte.toString());
            }
            //成功方法
            public void onSuccess(int param1Int, Header[] param1ArrayOfHeader, byte[] param1ArrayOfbyte) {
                Intent intent = new Intent((Context)DetailInfo.this, BottomActivity.class);//设置Intent准备跳转
                DetailInfo.this.startActivity(intent);//开始跳转
                DetailInfo.this.finish();//当前页面销毁
            }
        });
    }
    //初始化各种控件
    private void initView() {
        this.ddh = (TextView)findViewById(R.id.ddh);
        this.dmdbh = (TextView)findViewById(R.id.dmdbh);
        this.dshopname = (TextView)findViewById(R.id.dshopname);
        this.ddate = (TextView)findViewById(R.id.ddate);
        this.dmoney = (TextView)findViewById(R.id.dmoney);
        this.dstate = (TextView)findViewById(R.id.dstate);
        this.workerno = (TextView)findViewById(R.id.workerno);
        this.button = (Button)findViewById(R.id.ok_status);
    }

    //Activity声明周期，创建方法
    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.detail_info);
        final Order order = (Order)getIntent().getSerializableExtra("order");//从Intent中获取order对象
        initView();//调用实例化控件方法
        this.ddh.setText(order.getDdh());//给各个文本框设置值
        this.dmdbh.setText(order.getDmdbh());
        this.dshopname.setText(order.getDshopname());
        this.ddate.setText(order.getDdate());
        this.dmoney.setText(order.getDmoney());
        this.dstate.setText(order.getDstate());
        this.workerno.setText(order.getDworkerno());
        if (this.dstate.getText().toString().equals("提交支付"))//如果销售状态为提交支付
            this.button.setVisibility(View.VISIBLE);//将按钮设置为显示状态
        this.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {//为提交按钮设置单击监听事件
                if (order.getDzftype().equals("ZFBZF")) {//如果支付类型为支付宝
                    DetailInfo.this.asyncHttpClient("http://www.shangmengsoft.com/PAY/F2F-PAY/f2fpay/queryjava.php", order);//访问此方法，并将order参数传入
                    return;
                }
                if (order.getDzftype().equals("WXZF"))//如果支付类型为微信
                    DetailInfo.this.asyncHttpClient("http://www.shangmengsoft.com/PAY/WXPAY/example/orderqueryjava.php", order);//访问此方法，并将order参数传入
            }
        });
    }
}
