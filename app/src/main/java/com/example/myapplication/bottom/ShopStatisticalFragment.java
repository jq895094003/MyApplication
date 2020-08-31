package com.example.myapplication.bottom;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Bean.StatisticBean;
import com.example.myapplication.R;
import com.example.myapplication.util.AsyncHttpUrl;
import com.example.myapplication.util.FragmentToFragment;
import com.example.myapplication.util.ShopStatisticalListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ShopStatisticalFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    FragmentToFragment fragmentToFragment;//声明FragmentToFragment接口

    ProgressDialog progressDialog;//声明进度条

    private Button queryBtn;//声明查询按钮

    SharedPreferences sharedPreferences;//声明内部存储控件

    private EditText shopBegin;//声明开始日期输入框

    private ListView shopListView;//声明数据列表

    ArrayList<StatisticBean> shopStatisticals = new ArrayList<StatisticBean>();//声明StatisticBean对象数组

    private EditText shopStop;//声明结束日期输入框

    private Button shopTommo;//声明下一天按钮

    private Button shopYester;//声明上一天按钮

    private TextView topName;//声明上方文本框

    private TextView wxbsTV;//声明微信笔数文本框

    private TextView wxskTV;//声明微信收款文本框

    private TextView wxtkTV;//声明微信退款文本框

    private TextView zfbTKTV;//声明支付宝退款文本框

    private TextView zfbbsTV;//声明支付宝笔数文本框

    private TextView zfbskTV;//声明支付宝收款文本框

    private TextView wxFV;//声明微信费率文本框

    private TextView zfbFV;//声明支付宝费率文本框

    //获取今天得日期
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

    //实例化页面控件并为对应控件设置单击事件监听
    private void initView() {
        this.shopListView = (ListView) getActivity().findViewById(R.id.shop_statistic_list);//实例化数据展示ListView控件
        this.shopListView.setOnItemClickListener(this);//为数据展示ListView设置子项点击方法
        this.topName = (TextView) getActivity().findViewById(R.id.shop_sno_spinner);//实例化门店编号下拉选择框
        this.wxskTV = (TextView) getActivity().findViewById(R.id.shop_wx_money);//实例化微信收款文本框
        this.zfbskTV = (TextView) getActivity().findViewById(R.id.shop_zfb_money);//实例化支付宝收款文本框
        this.wxtkTV = (TextView) getActivity().findViewById(R.id.shop_wx_th_money);//实例化微信退款文本框
        this.zfbTKTV = (TextView) getActivity().findViewById(R.id.shop_zfb_th_money);//实例化支付宝退款文本框
        this.wxbsTV = (TextView) getActivity().findViewById(R.id.shop_wx_ratio);//实例化微信笔数文本框
        this.zfbbsTV = (TextView) getActivity().findViewById(R.id.shop_zfb_ratio);//实例化支付宝笔数文本框
        this.shopBegin = (EditText) getActivity().findViewById(R.id.shop_begindate);//实例化开始事件输入框
        this.shopBegin.setText(getNowDay());//为开始时间输入框设置初始值
        this.shopStop = (EditText) getActivity().findViewById(R.id.shop_stopdate);//实例化结束时间文本框
        this.shopStop.setText(getNowDay());//为结束时间输入框设置初始值
        this.wxFV = getActivity().findViewById(R.id.shop_wx_rate);//实例化微信费率文本框
        this.zfbFV = getActivity().findViewById(R.id.shop_zfb_rate);//实例化支付宝费率文本框
        this.shopYester = (Button) getActivity().findViewById(R.id.shop_yesterday);//实例化上一天按钮
        this.shopTommo = (Button) getActivity().findViewById(R.id.shop_tommorow);//实例化下一天按钮
        this.queryBtn = (Button) getActivity().findViewById(R.id.shop_query_statistical);//实例化查询按钮
        this.shopYester.setOnClickListener(this);//为上一天按钮设置点击监听
        this.shopTommo.setOnClickListener(this);//为下一天按钮设置点击监听
        this.shopBegin.setOnClickListener(this);//为开始时间输入框设置点击监听
        this.shopStop.setOnClickListener(this);//为结束时间输入框设置点击监听
        this.queryBtn.setOnClickListener(this);//为查询按钮设置点击监听
    }

    /**
     * 数据请求方法
     */
    private void shopStatistical() {
        this.progressDialog = ProgressDialog.show((Context) getActivity(), "请稍后……", "正在获取数据……", true);//开启进度条框
        progressDialog.setCancelable(true);//设置点击其他区域关闭进度条
        this.shopStatisticals.clear();//将之前存储得数据清空
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();//声明请求框架
        String str = (new AsyncHttpUrl()).getShopStatisticalUrl();//获取URL
        RequestParams requestParams = new RequestParams();//声明参数实体类对象
        this.sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);//声明内部存储空间
        requestParams.put("dshopno", this.sharedPreferences.getString("AAA", ""));//从内部存储空间取得店铺编号并放入参数类
        System.out.println(sharedPreferences.getString("AAA",""));
        requestParams.put("ddateStartString", this.shopBegin.getText().toString());//取开始日期输入框值放入参数类
        requestParams.put("ddateEndString", this.shopStop.getText().toString());//取结束日期值放入参数类
        asyncHttpClient.post(str, requestParams, (ResponseHandlerInterface) new JsonHttpResponseHandler() {//发起请求
            //失败方法
            @Override
            public void onFailure(int param1Int, Header[] param1ArrayOfHeader, Throwable param1Throwable, JSONObject param1JSONObject) {
                super.onFailure(param1Int, param1ArrayOfHeader, param1Throwable, param1JSONObject);
                ShopStatisticalFragment.this.progressDialog.dismiss();//关闭进度条框
                Toast.makeText((Context) ShopStatisticalFragment.this.getActivity(), "网络连接异常，查询失败", Toast.LENGTH_SHORT).show();
            }
            //成功方法
            @Override
            public void onSuccess(int param1Int, Header[] param1ArrayOfHeader, JSONObject param1JSONObject) {
                Log.i("successResult", String.valueOf(param1JSONObject));
                progressDialog.dismiss();//关闭进度条框
                Double totalWxsy = 0.0;//声明局部微信总金额变量
                Integer totalWxnum = 0;//声明局部微信总客流数变量
                Double totalZfbsy = 0.0;//声明支付宝总金额变量
                Integer totalZfbnum = 0;//声明支付宝总客流数变量
                Double totalWxtk = 0.0;//声明微信退款总金额变量
                Double totalZfbtk = 0.0;//声明支付宝总退款总金额变量
                Double wxRate = 0.0;//声明微信费率变量
                Double zfbRate = 0.0;//声明支付宝总费率变量
                DecimalFormat df = new DecimalFormat("#0.0");//声明DecimalFormat对象，并设置格式化样式
                JSONArray jsonArray = param1JSONObject.optJSONArray("data");//将返回值中得data值转换为JSONArray
                String[] shopNies = new String[jsonArray.length()];//声明一个跟JSONArray长度相等得字符串数组，用于存储所有数据得门店编号
                String[] typies = new String[jsonArray.length()];//声明一个跟JSONArray长度相等得字符串数组，用于存储所有数据得支付类型
                for (int i = 0; i < jsonArray.length(); i++) {//对JSONArray数据进行循环
                    Log.i("result", String.valueOf(jsonArray.optJSONObject(i)));
                    shopNies[i] = jsonArray.optJSONObject(i).optString("dmdbh");//从JSONArray中取出门店编号值存入shopNies；
                    typies[i] = jsonArray.optJSONObject(i).optString("dstate");//从JSONArray中取出支付类型值存入typies；
                    if (jsonArray.optJSONObject(i).optString("dstate").equals("支付成功")){//如果此条数据得支付状态为支付成功
                        if (jsonArray.optJSONObject(i).optString("dzftype").equals("WXZF")){//如果此条数据得支付类型为微信支付
                            wxRate += jsonArray.optJSONObject(i).optDouble("rate");//从此条数据中取出费率字段，更改wxRate值
                            totalWxsy += jsonArray.optJSONObject(i).optDouble("dmoney");//微信总金额加上此条数据中得dmoney值
                            totalWxnum += jsonArray.optJSONObject(i).optInt("number");//微信总笔数加上此条数据中得number值
                        }else if (jsonArray.optJSONObject(i).optString("dzftype").equals("ZFBZF")){//如果支付类型为支付宝支付
                            zfbRate += jsonArray.optJSONObject(i).optDouble("rate");//从此条数据中取出费率字段，更改zfbRate值
                            totalZfbsy += jsonArray.optJSONObject(i).optDouble("dmoney");//支付宝总金额加上此条数据中得dmoney值
                            totalZfbnum += jsonArray.optJSONObject(i).optInt("number");//支付宝总笔数加上此条数据中得number值
                        }
                    }else if (jsonArray.optJSONObject(i).optString("dstate").equals("退款成功")){//如果此条数据得支付状态为退款成功
                        if (jsonArray.optJSONObject(i).optString("dzftype").equals("WXTH")){//如果此条数据为微信退款
                            wxRate -= jsonArray.optJSONObject(i).optDouble("rate");////从此条数据中取出费率字段，更改wxRate值
                            totalWxtk += jsonArray.optJSONObject(i).optDouble("dmoney");//微信退款总金额加上此条数据中得dmoney值
                        }else if (jsonArray.optJSONObject(i).optString("dzftype").equals("ZFBTH")){//如果此条数据得支付状态为支付宝退款
                            wxRate -= jsonArray.optJSONObject(i).optDouble("rate");//从此条数据中取出费率字段，更改zfbRate值
                            totalZfbtk += jsonArray.optJSONObject(i).optDouble("dmoney");//支付宝退款总金额加上此条数据中得dmoney值
                        }
                    }
                }
                wxskTV.setText(df.format(totalWxsy));//为微信收款文本框设置值
                zfbskTV.setText(df.format(totalZfbsy));//为支付宝收款文本框设置值
                wxbsTV.setText(String.valueOf(totalWxnum));//为微信笔数文本框设置值
                zfbbsTV.setText(String.valueOf(totalZfbnum));//为支付宝笔数文本框设置值
                wxtkTV.setText(df.format(totalWxtk));//为微信退款文本框设置值
                zfbTKTV.setText(df.format(totalZfbtk));//为支付宝退款文本框设置值
                if (!(wxRate > 0)){//对费率进行非空判断，如果为空则设置费0.0因捕获不到null值，用0值代替
                    wxFV.setText("0.0");
                }else {//如果不为空则用DecimalFormat工具格式化值并赋予微信费率文本框
                    wxFV.setText(df.format(wxRate));
                }
                if (!(zfbRate > 0)){//对支付宝费率进行非空判断，如果为空则设置为0.0，因捕获不到null值，用0代替
                    zfbFV.setText("0.0");
                }else {//如果不为空则使用DecimalFormat工具进行格式化并赋予支付宝费率文本框
                    zfbFV.setText(df.format(zfbRate));
                }
                List<String> list = new ArrayList<String>();//声明一个新得list字符数组
                for (int i = 0; i < shopNies.length; i++) {//循环shopNies使用contains方法进行去重
                    if (!list.contains(shopNies[i])) {
                        list.add(shopNies[i]);
                    }
                }
                String[] newStr = list.toArray(new String[1]);//将去重后得结果转为字符串数组
                for (int i = 0; i < newStr.length; i++) {//对新字符串数组进行循环
                    StatisticBean statisticBean = new StatisticBean();//声明一个StatisticBean对象
                    Double wxsy = 0.0;//声明微信收款金额
                    Double zfbsy = 0.0;//声明支付宝收款金额
                    Double wxtk = 0.0;//声明微信退款金额
                    Double zfbtk = 0.0;//声明支付宝退款金额
                    Double zfsb = 0.0;//声明支付宝失败金额
                    Double rate = 0.0;//声明总费率
                    for (int j = 0; j < jsonArray.length(); j++) {//重新对JSONArray进行循环
                        if (jsonArray.optJSONObject(j).optString("dmdbh").equals(newStr[i])) {//判断此条数据中得商铺编号是否与循环到得呢newStr字符串数组项相同
                            if (jsonArray.optJSONObject(j).optString("dstate").equals("支付成功")) {//判断此条数据中得支付状态值是否为支付成功
                                rate += jsonArray.optJSONObject(i).optDouble("rate");//更改总费率值
                                if (jsonArray.optJSONObject(j).optString("dzftype").equals("WXZF")) {//如果支付类型为微信支付
                                    wxsy += jsonArray.optJSONObject(j).optDouble("dmoney");//更改该微信收款金额值
                                } else if (jsonArray.optJSONObject(j).optString("dzftype").equals("ZFBZF")) {//如果支付类型为支付宝支付
                                    zfbsy += jsonArray.optJSONObject(j).optDouble("dmoney");//更改字符包收款金额值
                                    totalZfbsy += jsonArray.optJSONObject(j).optDouble("dmoney");//
                                    totalZfbnum += 1;//支付宝总数+1
                                }
                            } else if (jsonArray.optJSONObject(j).optString("dstate").equals("支付失败")) {//如果此条数据支付状态为支付失败
                                zfsb += jsonArray.optJSONObject(j).optDouble("dmoney");//更改支付失败金额值
                            } else if (jsonArray.optJSONObject(j).optString("dstate").equals("退款成功")) {//如果此条数据支付状态为退款成功
                                rate -= jsonArray.optJSONObject(i).optDouble("rate");//更改总费率值
                                if (jsonArray.optJSONObject(j).optString("dzftype").equals("WXZF")) {//如果此条数据支付状态为微信支付
                                    wxtk += jsonArray.optJSONObject(j).optDouble("dmoney");//变更微信退款总金额
                                    totalWxtk += jsonArray.optJSONObject(j).optDouble("dmoney");//变更微信总退款金额
                                } else if (jsonArray.optJSONObject(j).optString("dzftype").equals("ZFBZF")) {//如果此条数据支付类型为支付宝支付
                                    zfbtk += jsonArray.optJSONObject(j).optDouble("dmoney");//变更支付宝退款金额
                                }
                            }
                        }
                    }
                    statisticBean.setYgBH(newStr[i]);//将此项newStr中得值赋给StatisticBean得门店编号字段
                    statisticBean.setWxSK(df.format(wxsy));//将此项中得微信收益值赋给StatisticBean中得微信收益字段
                    statisticBean.setZfbSK(df.format(zfbsy));//将此项中得支付宝收益值赋给StatisticBean中得支付宝收益字段
                    statisticBean.setWxTK(df.format(wxtk));//将此项中得微信退款赋给StatisticBean中得微信退款字段
                    statisticBean.setZfbTK(df.format(zfbtk));//将此项中得支付宝退款值赋给StatisticBean中得支付吧哦退款字段
                    statisticBean.setZfSB(df.format(zfsb));//将此项中得支付失败值赋给StatisticBean中得支付失败字段
                    if (!(rate > 0)){//对此项数据费率字段进行判空，因捕获不到null值所以使用0代替
                        statisticBean.setRate("0");
                    }else {//如果不为空
                        statisticBean.setRate(df.format(rate));//将此项中得总费率值赋给StatisticBean中得费率字段
                    }
                    shopStatisticals.add(statisticBean);//将实例化得StatisticBean对象放入StatisticBean数组
                }
//                for (int i = 0; i < shopStatisticals.size(); i++) {
//                    System.out.println(shopStatisticals.get(i).toString());
//                }
                ShopStatisticalListAdapter shopStatisticalListAdapter = new ShopStatisticalListAdapter(shopStatisticals);//声明shopListView使用得适配器
                shopListView.setAdapter(shopStatisticalListAdapter);//为shopListView设置适配器
            }

        });
    }

    //Fragment声明周期，页面创建方法
    public void onActivityCreated(@Nullable Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        initView();//实例化页面控件
        shopStatistical();//调用数据查询方法
        this.sharedPreferences = getActivity().getSharedPreferences("data", 0);//实例化内部存储控件
        String str = this.sharedPreferences.getString("BBB", "");//从内部存储空间取出商铺名信息
        this.topName.setText(str);//为商铺名称文本框设置值
        fragmentToFragment = (FragmentToFragment) getActivity();//实例化FragmentToFragment接口
    }

    ;

    //点击事件实现方法
    @Override
    public void onClick(View paramView) {
        SimpleDateFormat simpleDateFormat2;
        SimpleDateFormat simpleDateFormat1;
        DatePickerDialog datePickerDialog2;
        Calendar calendar2;
        switch (paramView.getId()) {
            default:
                return;
            case R.id.shop_yesterday://如果点击得是上一天按钮，则进行获取当前查询日期前一天方法
                simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = simpleDateFormat2.parse(this.shopBegin.getText().toString());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, -1);
                    String str = simpleDateFormat2.format(calendar.getTime());
                    this.shopBegin.setText(str);
                    this.shopStop.setText(str);
                    shopStatistical();
                    return;
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                    return;
                }
            case R.id.shop_tommorow://如果点击得是下一天按钮，则进行获取当前查询日期后台一天方法
                simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = simpleDateFormat1.parse(this.shopBegin.getText().toString());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, 1);
                    String str = simpleDateFormat1.format(calendar.getTime());
                    this.shopBegin.setText(str);
                    this.shopStop.setText(str);
                    shopStatistical();
                    return;
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                    return;
                }
            case R.id.shop_begindate://如果单击得是开始时间输入框，则弹出日期选择框，并对该输入框赋值
                calendar2 = Calendar.getInstance();
                datePickerDialog2 = new DatePickerDialog((Context) getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker param1DatePicker, int param1Int1, int param1Int2, int param1Int3) {
                        String str2;
                        String str1;
                        int i = param1Int2 + 1;
                        if (i < 10) {
                            StringBuilder stringBuilder1 = new StringBuilder();
                            stringBuilder1.append("0");
                            stringBuilder1.append(i);
                            str1 = stringBuilder1.toString();
                        } else {
                            str1 = String.valueOf(i);
                        }
                        if (param1Int3 < 10) {
                            StringBuilder stringBuilder1 = new StringBuilder();
                            stringBuilder1.append("0");
                            stringBuilder1.append(param1Int3);
                            str2 = stringBuilder1.toString();
                        } else {
                            str2 = String.valueOf(param1Int3);
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(param1Int1);
                        stringBuilder.append("-");
                        stringBuilder.append(str1);
                        stringBuilder.append("-");
                        stringBuilder.append(str2);
                        shopBegin.setText(stringBuilder.toString());
                    }
                }, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DATE));
                datePickerDialog2.show();
                this.shopStop.clearFocus();//清除该输入框得焦点
                return;
            case R.id.shop_query_statistical://如果点击得是查询按钮
                shopStatistical();//调用数据查询方法
                return;
            case R.id.shop_stopdate://如果单击得是结束时间输入框，则弹出日期选择窗口，并为该输入框进行赋值
                Calendar calendar1 = Calendar.getInstance();
                DatePickerDialog datePickerDialog1 = new DatePickerDialog((Context) getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker param1DatePicker, int param1Int1, int param1Int2, int param1Int3) {
                        String str2;
                        String str1;
                        int i = param1Int2 + 1;
                        if (i < 10) {
                            StringBuilder stringBuilder1 = new StringBuilder();
                            stringBuilder1.append("0");
                            stringBuilder1.append(i);
                            str1 = stringBuilder1.toString();
                        } else {
                            str1 = String.valueOf(i);
                        }
                        if (param1Int3 < 10) {
                            StringBuilder stringBuilder1 = new StringBuilder();
                            stringBuilder1.append("0");
                            stringBuilder1.append(param1Int3);
                            str2 = stringBuilder1.toString();
                        } else {
                            str2 = String.valueOf(param1Int3);
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(param1Int1);
                        stringBuilder.append("-");
                        stringBuilder.append(str1);
                        stringBuilder.append("-");
                        stringBuilder.append(str2);
                        shopStop.setText(stringBuilder.toString());
                    }
                }, calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DATE));
                datePickerDialog1.show();
                this.shopStop.clearFocus();//清除该文本框得焦点
                break;
        }

    }

    //Fragment声明周期方法，实例化页面布局
    @Nullable
    public View onCreateView(@NonNull LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        return paramLayoutInflater.inflate(R.layout.shopstatisticalfragment, paramViewGroup, false);
    }

    //ListView子项单击监听
    @Override
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        StatisticBean statisticBean = this.shopStatisticals.get(paramInt);//根据单击得position标识从shopStatistical数组中取出对应StatisticBean对象
        HashMap<String, String> hashMap = new HashMap<String, String>();//声明一个Map对象
        hashMap.put("dmdbh", statisticBean.getYgBH());//存入门店编号参数
        hashMap.put("startDate", this.shopBegin.getText().toString());//存入开始时间参数
        hashMap.put("endDate", this.shopStop.getText().toString());//存入结束时间参数
        fragmentToFragment.shopStatisticalToStatistical(hashMap);//调用fragmentToFragment中得方法
    }
}
