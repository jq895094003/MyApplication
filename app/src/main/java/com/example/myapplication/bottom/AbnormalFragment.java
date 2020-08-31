package com.example.myapplication.bottom;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import com.example.myapplication.Bean.Order;
import com.example.myapplication.DetailInfo;
import com.example.myapplication.R;
import com.example.myapplication.util.AbnormalListViewAdapter;
import com.example.myapplication.util.AsyncHttpUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import cz.msebera.android.httpclient.Header;

public class AbnormalFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Button abDownDay;//声明下一天按钮

    private EditText abEndDate;//声明结束日期输入框

    private Button abFil;//声明查询按钮

    ArrayList<Order> abList = new ArrayList<Order>();//声明一个Order对象数组

    private ListView abListView;//声明ListView控件

    private TextView abNowDay;//声明当前查询日期文本框

    private EditText abStartDate;//声明开始日期输入框

    private Button abUpDay;//声明上一天按钮

    private Spinner abZftype;//声明支付类型下拉选择框

    ProgressDialog progressDialog;//声明进度条控件

    private TextView shopSpinner;//声明商铺文本框

    private Spinner workerSpinner;//声明员工下拉选择框

    //得到今天得日期方法
    private String getNowDay() {
        return (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
    }

    //声明此页面使用得适配器
    private AbnormalListViewAdapter abnormalListViewAdapter;

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
    //实例化当前页面得控件
    private void initView() {
        this.shopSpinner = (TextView) getActivity().findViewById(R.id.abnormal_shop_filtrate);
        String str = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE).getString("BBB", "");//从内部存储中获取商铺编号
        this.shopSpinner.setText(str);
        this.workerSpinner = (Spinner) getActivity().findViewById(R.id.ab_worker_fil);
        this.abStartDate = (EditText) getActivity().findViewById(R.id.ab_begindate_filtrate);
        this.abEndDate = (EditText) getActivity().findViewById(R.id.abnormal_stopdate_filtrate);
        this.abStartDate.setText(getNowDay());
        this.abEndDate.setText(getNowDay());
        this.abUpDay = (Button) getActivity().findViewById(R.id.abnormal_upDay);
        this.abDownDay = (Button) getActivity().findViewById(R.id.abnormal_downDay);
        this.abNowDay = (TextView) getActivity().findViewById(R.id.abnormal_nowDay);
        this.abNowDay.setText(getNowDay());
        this.abFil = (Button) getActivity().findViewById(R.id.abnormal_fil_button);
        this.abListView = (ListView) getActivity().findViewById(R.id.abnormal_fragment_list);
        this.abListView.setOnItemClickListener(this);//为ListView设置子项单击监听
        this.abZftype = (Spinner) getActivity().findViewById(R.id.ab_zftype_fil);
        this.abStartDate.setOnClickListener(this);//为开始事件输入框设置单击监听
        this.abEndDate.setOnClickListener(this);//为结束时间输入框设置单击监听
        this.abUpDay.setOnClickListener(this);//为上一天按钮设置单击监听
        this.abDownDay.setOnClickListener(this);//为下一天按钮设置单击监听
        this.abFil.setOnClickListener(this);//为查询按钮设置单击监听
    }
    //员工查询接口
    private void newJK() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();//声明请求框架
        String str1 = (new AsyncHttpUrl()).getSelectDataFromShopName();//获取URL
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);//实例化内部存储对象
        String str2 = sharedPreferences.getString("Token", "");//从内部存储对象中获取Token值
        String str3 = sharedPreferences.getString("AAA", "");//从内部存储对象中获取商铺编号值
        RequestParams requestParams = new RequestParams();//声明参数对象
        requestParams.put("token", str2);//传入参数
        requestParams.put("dmdbh", str3);//传入参数
        requestParams.put("ddateStart", this.abStartDate.getText().toString());//传入参数
        requestParams.put("ddateEnd", this.abEndDate.getText().toString());//传入参数
        requestParams.put("state", "支付失败");//传入参数
        asyncHttpClient.post(str1, requestParams, (ResponseHandlerInterface) new JsonHttpResponseHandler() {//发起请求
            //失败方法
            public void onFailure(int param1Int, Header[] param1ArrayOfHeader, Throwable param1Throwable, JSONObject param1JSONObject) {
                super.onFailure(param1Int, param1ArrayOfHeader, param1Throwable, param1JSONObject);
                System.out.println("异常订单的newJK方法url有问题");//打印
            }
           //成功方法
            public void onSuccess(int param1Int, Header[] param1ArrayOfHeader, JSONObject param1JSONObject) {
                super.onSuccess(param1Int, param1ArrayOfHeader, param1JSONObject);
                if (param1JSONObject.optString("code").equals("200")) {//如果返回值中code参数为200
                    try {
                        byte b2;
                        JSONArray jSONArray = param1JSONObject.getJSONArray("data");//将返回值中data参数转换成JSONArray
                        new DecimalFormat("#0.00");//声明DecimalFormat对象
                        ArrayList<String> arrayList = new ArrayList();//声明文字数组
                        arrayList.add("全部");//添加元素
                        byte b1 = 0;
                        while (true) {//对JSONArray进行循环
                            int i = jSONArray.length();
                            if (b1 < i) {
                                arrayList.add(jSONArray.getJSONObject(b1).optString("dworkerno"));//将JSONArray中得子项中得dworkerno参数取出放入list
                                b1++;
                                continue;
                            }
                            break;
                        }
                        ArrayList arrayList1 = hashSet(arrayList);//对arrayList去重
                        ArrayAdapter arrayAdapter = new ArrayAdapter((Context) AbnormalFragment.this.getActivity(), android.R.layout.simple_list_item_1, arrayList1);//声明并实例化适配器
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);//设置下拉选择框下拉样式
                        workerSpinner.setAdapter((SpinnerAdapter) arrayAdapter);//为员工下拉选择框设置适配器
                    } catch (JSONException jSONException) {
                        jSONException.printStackTrace();
                        return;
                    }
                    async();//调用数据查询接口
                } else {
                    Toast.makeText((Context) AbnormalFragment.this.getActivity(), param1JSONObject.optString("msg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //去重方法
    public void aaa(ArrayList<Order> paramArrayList) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < paramArrayList.size(); i++) {
            int j = i + 1;
            for (int k = j; k < paramArrayList.size(); k++) {
                if (simpleDateFormat.parse(((Order) paramArrayList.get(i)).getDdate()).before(simpleDateFormat.parse(((Order) paramArrayList.get(k)).getDdate()))) {
                    Order order = paramArrayList.get(i);
                    paramArrayList.set(i, paramArrayList.get(k));
                    paramArrayList.set(k, order);
                }
            }
        }
    }
    //数据查询方法
    public void async() {
        abList.clear();//将数据组清空
        progressDialog = ProgressDialog.show(getActivity(), "请稍后", "正在获取数据", false);//弹出进度条框
        progressDialog.setCancelable(true);//设置进度条框可以取消
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();//声明请求框架
        asyncHttpClient.setEnableRedirects(true);//设置请求可以被转发
        String str1 = (new AsyncHttpUrl()).getSelectAbnormalUrl();//获取URL
        String str2 = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE).getString("Token", "");//从内部存储中获取Token值
        RequestParams requestParams = new RequestParams();//声明参数对象
        requestParams.put("token", str2);//放入参数
        requestParams.put("dmdbh", shopSpinner.getText().toString());//放入参数
        requestParams.put("ddateStart", abStartDate.getText().toString());//放入参数
        requestParams.put("ddateEnd", abEndDate.getText().toString());//放入参数
        requestParams.put("stringstate", "");//放入参数
        if (!workerSpinner.getSelectedItem().equals("全部"))//放入参数
            requestParams.put("dworkerno", workerSpinner.getSelectedItem());//放入参数
        if (abZftype.getSelectedItem().equals("支付宝失败")) {//如果支付类型为支付宝失败
            requestParams.put("dzftype", "ZFBZF");//放入参数
        } else if (abZftype.getSelectedItem().equals("微信失败")) {//如果支付类型为微信失败
            requestParams.put("dzftype", "WXZF");//放入参数
        }else if (abZftype.getSelectedItem().equals("支付宝退款")){//如果支付类型为支付宝退款
            requestParams.put("dzftype","ZFBTH");//放入参数
        }else if (abZftype.getSelectedItem().equals("微信退款")){//如果支付类型为微信退款
            requestParams.put("dzftype","WXTH");//放入参数
        }
        asyncHttpClient.post(str1, requestParams, (ResponseHandlerInterface) new JsonHttpResponseHandler() {//发起请求
            //失败方法
            @Override
            public void onFailure(int param1Int, Header[] param1ArrayOfHeader, Throwable param1Throwable, JSONObject param1JSONObject) {
                progressDialog.dismiss();//关闭进度条框
                System.out.println("异常订单的async方法url有问题");
            }
            //成功方法
            @Override
            public void onSuccess(int param1Int, Header[] param1ArrayOfHeader, JSONObject param1JSONObject) {
                if (param1JSONObject.optString("code").equals("0")) {
                    JSONArray jSONArray = param1JSONObject.optJSONArray("data");//将返回值中得data参数转换为JSONArray
                    for (int i = 0; i < jSONArray.length(); i++) {//将JSONArray进行循环
                        JSONObject jSONObject = jSONArray.optJSONObject(i);
                        Order order = new Order();//声明并实例化一个Order对象
                        order.setDwxid(jSONObject.optString("dwxid"));//从json中获取对应值放入Order对象
                        order.setDzfbid(jSONObject.optString("dzfbid"));//从json中获取对应值放入Order对象
                        order.setDmdbh(jSONObject.optString("dmdbh"));//从json中获取对应值放入Order对象
                        order.setDdh(jSONObject.optString("ddh"));//从json中获取对应值放入Order对象
                        order.setDdate(jSONObject.optLong("ddate"));//从json中获取对应值放入Order对象
                        order.setDmoney((new DecimalFormat("0.00")).format(jSONObject.opt("dmoney")));//从json中获取对应值放入Order对象
                        order.setDzftype(jSONObject.optString("dzftype"));//从json中获取对应值放入Order对象
                        order.setDstate(jSONObject.optString("dstate"));//从json中获取对应值放入Order对象
                        order.setDworkerno(jSONObject.optString("dworkerno"));//从json中获取对应值放入Order对象
                        order.setDshopname(jSONObject.optString("dshopname"));//从json中获取对应值放入Order对象
                        if (!"支付成功".equals(order.getDstate())) {//如果order得dstate属性为支付成功
                            abList.add(order);//将order对象放入abList
                        }
                    }
//                    try {
//                        aaa(abList);
//                    } catch (ParseException parseException) {
//                        parseException.printStackTrace();
//                    }
//                    for (int i = 0; i < abList.size(); i++) {
//                        System.out.println(abList.toString());
//                    }
                    abnormalListViewAdapter.notifyDataSetChanged();//更新ListView显示内容
                    progressDialog.dismiss();//关闭进度条框
                }
                progressDialog.dismiss();//关闭进度条框
            }
        });
    }

    //Fragment声明周日，页面创建方法
    public void onActivityCreated(@Nullable Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        initView();//实例化控件
        abnormalListViewAdapter = new AbnormalListViewAdapter(abList);//实例化适配器
        abListView.setAdapter(abnormalListViewAdapter);//给ListView设置适配器
        newJK();//调用查询员工方法
    }

    //重写单击事件
    public void onClick(View paramView) {
        DatePickerDialog datePickerDialog2;
        Calendar calendar2;
        switch (paramView.getId()) {//获取控件ID
            default:
                return;
            case R.id.abnormal_upDay://如果为上一天
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//声明格式化日期对象
                    Date date = simpleDateFormat.parse(this.abEndDate.getText().toString());//将声明得日期格式化
                    Calendar calendar = Calendar.getInstance();//声明Calender对象
                    calendar.setTime(date);//给Calender对象设置值
                    calendar.add(Calendar.DATE, -1);//将Calender日期中得天数-1
                    String str = simpleDateFormat.format(calendar.getTime());//获取Calender中得七日
                    this.abStartDate.setText(str);//给开始日期输入框设置值
                    this.abEndDate.setText(str);//给结束如期输入框设置值
                    this.abNowDay.setText(str);//给当前查询文本框设置值
                    newJK();//调用查询员工接口
                    return;
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                    return;
                }
            case R.id.abnormal_stopdate_filtrate://如果单击得为查询按钮
                calendar2 = Calendar.getInstance();//实例化Calender对象
                datePickerDialog2 = new DatePickerDialog((Context) getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker param1DatePicker, int param1Int1, int param1Int2, int param1Int3) {//弹出日期选择框
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
                        abEndDate.setText(stringBuilder.toString());
                    }
                }, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DATE));
                datePickerDialog2.show();
                this.abEndDate.clearFocus();
                return;
            case R.id.abnormal_fil_button://如果单击得是查询按钮
                newJK();//调用员工查询
                return;
            case R.id.abnormal_downDay://如果单击得是下一天，与上一天一样，只是天数+1
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse(this.abEndDate.getText().toString());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, 1);
                    String str = simpleDateFormat.format(calendar.getTime());
                    this.abStartDate.setText(str);
                    this.abEndDate.setText(str);
                    this.abNowDay.setText(str);
                    newJK();
                    return;
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                    return;
                }
            case R.id.ab_begindate_filtrate://如果单击得是开始事件输入框
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
                        abStartDate.setText(stringBuilder.toString());
                    }
                }, calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DATE));
                datePickerDialog1.show();
                abStartDate.clearFocus();
                break;
        }

    }

    //Fragment声明周期，实例化页面布局方法
    @Nullable
    public View onCreateView(@NonNull LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        return paramLayoutInflater.inflate(R.layout.abnormal_orders_fragment, paramViewGroup, false);
    }
    //ListView子项单击监听
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        Order order = abList.get(paramInt);//根据单击得行数来获取对应得Order对象
        Intent intent = new Intent((Context) getActivity(), DetailInfo.class);//声明Intent对象准备跳转页面
        intent.putExtra("order", (Serializable) order);//向Intent中放入Order对象
        startActivity(intent);//开始跳转
    }
}


/* Location:              C:\Users\SYQ\Desktop\支付查询_classes_dex2jar.jar!\com\smrj\myapplication\bottomActivity\AbnormalFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.2
 */