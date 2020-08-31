package com.example.myapplication.bottom;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.Bean.StatisticBean;
import com.example.myapplication.R;
import com.example.myapplication.util.AsyncHttpUrl;
import com.example.myapplication.util.StatisticalListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class StatisticsFragment extends Fragment {


    protected Button button;//声明查询按钮

    private EditText endDate;//声明结束日期输入框

    private ListView listView;//声明数据展示ListView

    LocalBroadcastManager localBroadcastManager;//声明广播

    private ProgressDialog progressDialog;//声明进度条框

    private SharedPreferences sharedPreferences;//声明内部存储空间

    protected final ArrayList<String> shopName = new ArrayList<String>();//声明商铺名称List
    private TextView snoTv;//声明商铺编号


    private Spinner spinner;//声明下拉列表框

    private EditText startDate;//声明开始日期输入框

    private Button tommorow;//声明后一天按钮


    private TextView wxDDS;//声明微信笔数文本框

    private TextView wxSY;//声明微信收益文本框

    private TextView wxTHP;//声明微信退款金额文本框

    private Button yesterday;//声明前一天按钮

    private TextView zfbDDS;//声明支付宝笔数文本框

    private TextView zfbSY;//声明支付宝金额文本框

    private TextView zfbTHP;//声明支付宝退款金额

    private TextView zfbFV;//声明支付宝费率

    private TextView wxFV;//声明微信费率

    //获取当前日期
    private String getNowDay() {
        return (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
    }
    //去重方法
    private ArrayList<String> hashSet(ArrayList<String> paramArrayList) {
        HashSet<String> hashSet = new HashSet();
        ArrayList<String> arrayList = new ArrayList();
        for (String str : paramArrayList) {
            if (hashSet.add(str))
                arrayList.add(str);
        }
        return arrayList;
    }

    //实例化页面布局控件，并设置控件监听
    private void initView() {
        this.startDate = (EditText) getActivity().findViewById(R.id.begindate);
        this.startDate.setText(getNowDay());//给开始日期设置初始值
        this.endDate = (EditText) getActivity().findViewById(R.id.stopdate);
        this.endDate.setText(getNowDay());//给结束日期设置初始值
        this.button = (Button) getActivity().findViewById(R.id.query_statistical);
        this.wxSY = (TextView) getActivity().findViewById(R.id.wx_money);
        this.zfbSY = (TextView) getActivity().findViewById(R.id.zfb_money);
        this.wxFV = getActivity().findViewById(R.id.wx_rate);
        this.zfbFV = getActivity().findViewById(R.id.zfb_rate);
        this.wxDDS = (TextView) getActivity().findViewById(R.id.wx_ratio);
        this.zfbDDS = (TextView) getActivity().findViewById(R.id.zfb_ratio);
        this.zfbTHP = (TextView) getActivity().findViewById(R.id.zfb_th_money);
        this.wxTHP = (TextView) getActivity().findViewById(R.id.wx_th_money);
        this.tommorow = (Button) getActivity().findViewById(R.id.tommorow);
        this.yesterday = (Button) getActivity().findViewById(R.id.yesterday);
        this.listView = (ListView) getActivity().findViewById(R.id.statistic_list);
        this.spinner = (Spinner) getActivity().findViewById(R.id.sno_spinner);
//        this.spItem = (TextView) getActivity().findViewById(R.id.sno_tv);
        snoTv = (TextView) getActivity().findViewById(R.id.sno_tv);
        tommorow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {//给后一天按钮设置点击事件
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = simpleDateFormat.parse(StatisticsFragment.this.endDate.getText().toString());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, 1);
                    String str = simpleDateFormat.format(calendar.getTime());
                    startDate.setText(str);
                    endDate.setText(str);
                    shopStatistical(spinner.getSelectedItem().toString());
                    return;
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                    return;
                }
            }
        });
        this.yesterday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {//给前一天按钮设置点击事件
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String str = StatisticsFragment.this.endDate.getText().toString();
                try {
                    Date date = simpleDateFormat.parse(str);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, -1);
                    String str1 = simpleDateFormat.format(calendar.getTime());
                    startDate.setText(str1);
                    endDate.setText(str1);
                    shopStatistical(spinner.getSelectedItem().toString());
                    return;
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                    return;
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {//给查询按钮设置点击事件
                shopStatistical(spinner.getSelectedItem().toString());//调用查询数据方法
            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {//给开始时间输入框设置日期弹出框事件
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog((Context) StatisticsFragment.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker param2DatePicker, int param2Int1, int param2Int2, int param2Int3) {
                        String str2;
                        String str1;
                        int i = param2Int2 + 1;
                        if (i < 10) {
                            StringBuilder stringBuilder1 = new StringBuilder();
                            stringBuilder1.append("0");
                            stringBuilder1.append(i);
                            str1 = stringBuilder1.toString();
                        } else {
                            str1 = String.valueOf(i);
                        }
                        if (param2Int3 < 10) {
                            StringBuilder stringBuilder1 = new StringBuilder();
                            stringBuilder1.append("0");
                            stringBuilder1.append(param2Int3);
                            str2 = stringBuilder1.toString();
                        } else {
                            str2 = String.valueOf(param2Int3);
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(param2Int1);
                        stringBuilder.append("-");
                        stringBuilder.append(str1);
                        stringBuilder.append("-");
                        stringBuilder.append(str2);
                        startDate.setText(stringBuilder.toString());
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                datePickerDialog.show();
                startDate.clearFocus();//开始事件输入框清除焦点
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {//给结束时间文本框设置点击事件弹出日期弹出框
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog((Context) StatisticsFragment.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker param2DatePicker, int param2Int1, int param2Int2, int param2Int3) {
                        String str2;
                        String str1;
                        int i = param2Int2 + 1;
                        if (i < 10) {
                            StringBuilder stringBuilder1 = new StringBuilder();
                            stringBuilder1.append("0");
                            stringBuilder1.append(i);
                            str1 = stringBuilder1.toString();
                        } else {
                            str1 = String.valueOf(i);
                        }
                        if (param2Int3 < 10) {
                            StringBuilder stringBuilder1 = new StringBuilder();
                            stringBuilder1.append("0");
                            stringBuilder1.append(param2Int3);
                            str2 = stringBuilder1.toString();
                        } else {
                            str2 = String.valueOf(param2Int3);
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(param2Int1);
                        stringBuilder.append("-");
                        stringBuilder.append(str1);
                        stringBuilder.append("-");
                        stringBuilder.append(str2);
                        endDate.setText(stringBuilder.toString());
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                datePickerDialog.show();
                endDate.clearFocus();//开始日期输入框清除焦点
            }
        });
    }

    //查询数据方法，与ShopStatisticalFragment中得 shopStatistical()方法基本一致
    private void newJK(String paramString) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String str1 = (new AsyncHttpUrl()).getSelectDataFromShopName();
        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String str2 = sharedPreferences1.getString("Token", "");
        String str3 = sharedPreferences1.getString("AAA", "");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", str2);
        if (paramString.equals("本日无数据")) {
            requestParams.put("dmdbh", str3);
            requestParams.put("mdbh", paramString);
        } else {
            requestParams.put("dmdbh", str3);
            requestParams.put("mdbh", paramString);
        }
        requestParams.put("ddateStart", this.startDate.getText().toString());
        requestParams.put("ddateEnd", this.endDate.getText().toString());
        asyncHttpClient.post(str1, requestParams, (ResponseHandlerInterface) new JsonHttpResponseHandler() {
            public void onFailure(int param1Int, Header[] param1ArrayOfHeader, Throwable param1Throwable, JSONObject param1JSONObject) {
                super.onFailure(param1Int, param1ArrayOfHeader, param1Throwable, param1JSONObject);
                StatisticsFragment.this.progressDialog.dismiss();
                Toast.makeText((Context) StatisticsFragment.this.getActivity(), "网络连接异常，查询失败", Toast.LENGTH_SHORT).show();
            }

            public void onSuccess(int param1Int, Header[] param1ArrayOfHeader, JSONObject param1JSONObject) {
                Double totalWxsy = 0.0;
                Integer totalWxnum = 0;
                Double totalZfbsy = 0.0;
                Integer totalZfbnum = 0;
                Double totalWxtk = 0.0;
                Double totalZfbtk = 0.0;
                Double wxRate = 0.0;
                Double zfbRate = 0.0;
                DecimalFormat df = new DecimalFormat("#0.0");
                JSONArray jsonArray = param1JSONObject.optJSONArray("data");
                String[] loyeeNo = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    System.out.println(jsonArray.optJSONObject(i));
                    loyeeNo[i] = jsonArray.optJSONObject(i).optString("dworkerno");
                    if (jsonArray.optJSONObject(i).optString("dstate").equals("支付成功")){
                        if (jsonArray.optJSONObject(i).optString("dzftype").equals("WXZF")){
                            wxRate += jsonArray.optJSONObject(i).optDouble("rate");
                            totalWxsy += jsonArray.optJSONObject(i).optDouble("dmoney");
                            totalWxnum += jsonArray.optJSONObject(i).optInt("number");
                        }else if (jsonArray.optJSONObject(i).optString("dzftype").equals("ZFBZF")){
                            zfbRate += jsonArray.optJSONObject(i).optDouble("rate");
                            totalZfbsy += jsonArray.optJSONObject(i).optDouble("dmoney");
                            totalZfbnum += jsonArray.optJSONObject(i).optInt("number");
                        }
                    }else if (jsonArray.optJSONObject(i).optString("dstate").equals("退款成功")){
                        if (jsonArray.optJSONObject(i).optString("dzftype").equals("WXTH")){
                            wxRate -= jsonArray.optJSONObject(i).optDouble("rate");
                            totalWxtk += jsonArray.optJSONObject(i).optDouble("dmoney");
                        }else if (jsonArray.optJSONObject(i).optString("dzftype").equals("ZFBTH")){
                            wxRate -= jsonArray.optJSONObject(i).optDouble("rate");
                            totalZfbtk += jsonArray.optJSONObject(i).optDouble("dmoney");
                        }
                    }
                }
                wxSY.setText(df.format(totalWxsy));
                zfbSY.setText(df.format(totalZfbsy));
                wxDDS.setText(String.valueOf(totalWxnum));
                zfbDDS.setText(String.valueOf(totalZfbnum));
                wxTHP.setText(df.format(totalWxtk));
                zfbTHP.setText(df.format(totalZfbtk));
                if (!(wxRate > 0)){
                    wxFV.setText("0.0");
                }else {
                    wxFV.setText(df.format(wxRate));
                }
                if (!(zfbRate > 0)){
                    zfbFV.setText("0.0");
                }else {
                    zfbFV.setText(df.format(zfbRate));
                }
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < loyeeNo.length; i++) {
                    if (!list.contains(loyeeNo[i])) {
                        list.add(loyeeNo[i]);
                    }
                }
                String[] newStr = list.toArray(new String[1]);
                ArrayList<StatisticBean> statisticBeans = new ArrayList<StatisticBean>();
                for (int i = 0; i < newStr.length; i++) {
                    StatisticBean statisticBean = new StatisticBean();
                    Double wxsy = 0.0;
                    Double zfbsy = 0.0;
                    Double wxtk = 0.0;
                    Double zfbtk = 0.0;
                    Double zfsb = 0.0;
                    Double rate = 0.0;
                    for (int j = 0; j < jsonArray.length(); j++) {
                        if (jsonArray.optJSONObject(j).optString("dworkerno").equals(newStr[i])) {
                            if (jsonArray.optJSONObject(j).optString("dstate").equals("支付成功")) {
                                rate += jsonArray.optJSONObject(i).optDouble("rate");
                                if (jsonArray.optJSONObject(j).optString("dzftype").equals("WXZF")) {
                                    wxsy += jsonArray.optJSONObject(j).optDouble("dmoney");
                                } else if (jsonArray.optJSONObject(j).optString("dzftype").equals("ZFBZF")) {
                                    zfbRate += jsonArray.optJSONObject(i).optDouble("rate");
                                    zfbsy += jsonArray.optJSONObject(j).optDouble("dmoney");
                                }
                            } else if (jsonArray.optJSONObject(j).optString("dstate").equals("支付失败")) {
                                zfsb += jsonArray.optJSONObject(j).optDouble("dmoney");
                            } else if (jsonArray.optJSONObject(j).optString("dstate").equals("退款成功")) {
                                zfbRate -= jsonArray.optJSONObject(i).optDouble("rate");
                                if (jsonArray.optJSONObject(j).optString("dzftype").equals("WXZF")) {
                                    wxtk += jsonArray.optJSONObject(j).optDouble("dmoney");
                                } else if (jsonArray.optJSONObject(j).optString("dzftype").equals("ZFBZF")) {
                                    zfbtk += jsonArray.optJSONObject(j).optDouble("dmoney");
                                }
                            }
                        }
                    }
                    statisticBean.setYgBH(newStr[i]);
                    statisticBean.setWxSK(df.format(wxsy));
                    statisticBean.setZfbSK(df.format(zfbsy));
                    statisticBean.setWxTK(df.format(wxtk));
                    statisticBean.setZfbTK(df.format(zfbtk));
                    statisticBean.setZfSB(df.format(zfsb));
                    if (!(rate > 0)){
                        statisticBean.setRate("0");
                    }else {
                        statisticBean.setRate(df.format(rate));
                    }
                    statisticBeans.add(statisticBean);
                }
                StatisticalListAdapter shopStatisticalListAdapter = new StatisticalListAdapter(statisticBeans);
                listView.setAdapter(shopStatisticalListAdapter);
            }
        });
    }


    //门店编号下拉选择框请求
    private void shopStatistical(final String shopNumber) {
        this.progressDialog = ProgressDialog.show((Context) getActivity(), "请稍后……", "正在获取数据……", true);//开启进度条
        this.shopName.clear();//将shopName字符串数组清空
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();//声明请求框架
        String str = (new AsyncHttpUrl()).getShopStatisticalUrl();//获取URL
        RequestParams requestParams = new RequestParams();//声明参数对象
        this.sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);//实例化内部存储空间
        requestParams.put("dshopno", sharedPreferences.getString("AAA", ""));//从内部存储空间取出商铺编号并放入参数对象
        requestParams.put("ddateStartString", startDate.getText().toString());//获取开始日期输入框中得值并放入参数对象
        requestParams.put("ddateEndString", startDate.getText().toString());//获取结束日期输入框得值并放入参数对象
        asyncHttpClient.post(str, requestParams, (ResponseHandlerInterface) new JsonHttpResponseHandler() {//发起请求
            //请求失败方法
            public void onFailure(int param1Int, Header[] param1ArrayOfHeader, Throwable param1Throwable, JSONObject param1JSONObject) {
                super.onFailure(param1Int, param1ArrayOfHeader, param1Throwable, param1JSONObject);
                StatisticsFragment.this.progressDialog.dismiss();//关闭进度条
                Toast.makeText((Context) StatisticsFragment.this.getActivity(), "网络连接错误，商铺编号查询失败", Toast.LENGTH_SHORT).show();
            }
            //请求成功方法
            public void onSuccess(int param1Int, Header[] param1ArrayOfHeader, JSONObject param1JSONObject) {
                super.onSuccess(param1Int, param1ArrayOfHeader, param1JSONObject);
                boolean bool = param1JSONObject.optString("code").equals("200");//判断返回值中得code参数是否为200
                byte b = 0;
                if (bool) {//如果bool为true
                    try {
                        JSONArray jSONArray = param1JSONObject.getJSONArray("data");//将返回值中得data单数转换为JSONArray
                        ArrayList<String> arrayList1 = new ArrayList();//声明一个String数组
                        for (byte b1 = 0; b1 < jSONArray.length(); b1++)//对JSONArray进行循环
                            arrayList1.add(jSONArray.getJSONObject(b1).optString("dmdbh"));//将JSONArray中每条数据得门店编号值传入list
                        ArrayList<String> arrayList2 = hashSet(arrayList1);//对list进行去重
                        for (int i = 0; i < arrayList2.size(); i++) {
                            Log.e("StatisticalFragment",arrayList2.get(i));
                        }
                        if (arrayList2.size() == 0) {//如果arrayList2长度为0
                            snoTv.setVisibility(View.GONE);//设置商铺编号文本框为隐藏
                            arrayList2.add("本日无数据");//将此信息添加到arrayList2里
                        } else {
                            snoTv.setVisibility(View.VISIBLE);//将商铺编号文本框设置为显示
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter((Context) StatisticsFragment.this.getActivity(), R.layout.spinner_item, arrayList2);//声明并实例化适配器
                        StatisticsFragment.this.spinner.setAdapter((SpinnerAdapter) arrayAdapter);//给店铺编号下拉列表控件设置适配器
                        while (b < arrayList2.size()) {//对arrList2进行循环
                            if ((arrayList2.get(b)).equals(shopNumber))
                                spinner.setSelection(b, true);//设置默认选中
                            b++;
                        }
                    } catch (JSONException jSONException) {
                        jSONException.printStackTrace();
                    }
                    newJK(spinner.getSelectedItem().toString());//调用查询数据功能
                    progressDialog.dismiss();//关闭进度条
                    return;
                }
                progressDialog.dismiss();//关闭进度条
                Toast.makeText((Context) StatisticsFragment.this.getActivity(), param1JSONObject.optString("msg"), Toast.LENGTH_SHORT).show();//弹出提示
            }
        });
    }

    //Fragment生命周期，创建页面
    public void onActivityCreated(@Nullable Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        initView();//初始化页面控件
        this.sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);//初始化内部存储控件
        shopStatistical(sharedPreferences.getString("AAA", ""));//从内部存储控件取出门店编号
        this.localBroadcastManager = LocalBroadcastManager.getInstance((Context) getActivity());//实例化广播对象
        IntentFilter intentFilter = new IntentFilter();//声明注册对象
        intentFilter.addAction("android.intent.action.CART_BROADCAST");//注册广播
        this.localBroadcastManager.registerReceiver(this.broadcastReceiver, intentFilter);//发送广播
    }

    //广播接收器
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context param1Context, Intent param1Intent) {
            String str1 = param1Intent.getStringExtra("dmdbh");//取出广播中得门店编号
            String str2 = param1Intent.getStringExtra("startDate");//取出广播中得开始时间
            String str3 = param1Intent.getStringExtra("endDate");//取出广播中得结束时间
            StatisticsFragment.this.startDate.setText(str2);//给当前页面得开始时间赋值
            StatisticsFragment.this.endDate.setText(str3);//给当前页面得结束时间赋值
            shopStatistical(str1);//调用查询接口
        }
    };

    //fragment声明周期，页面初始化
    @Nullable
    public View onCreateView(@NonNull LayoutInflater paramLayoutInflater, @Nullable ViewGroup
            paramViewGroup, @Nullable Bundle paramBundle) {
        return paramLayoutInflater.inflate(R.layout.statistical, paramViewGroup, false);
    }
}