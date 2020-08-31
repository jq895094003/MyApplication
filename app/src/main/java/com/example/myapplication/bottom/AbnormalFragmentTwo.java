package com.example.myapplication.bottom;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Bean.Fuser;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import cz.msebera.android.httpclient.Header;

public class AbnormalFragmentTwo extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    String aaaaa = getNowDay();

    private Button abDownDay;

    private EditText abEndDate;

    private Button abFil;

    ArrayList<Order> abList = new ArrayList<Order>();

    private ListView abListView;

    private TextView abNowDay;

    private EditText abStartDate;

    private Button abUpDay;

    private Spinner abZftype;

    Fuser fuser;

    ProgressDialog progressDialog;

    private TextView shopSpinner;

    private TextView workerSpinner;

    private String getNowDay() { return (new SimpleDateFormat("yyyy-MM-dd")).format(new Date()); }
    //与AbnormalFragment相同
    private void initView() {
        this.fuser = (Fuser)getIntent().getSerializableExtra("Fuser");
        this.shopSpinner = (TextView)findViewById(R.id.abnormal_shop_filtrate);
        this.shopSpinner.setText(this.fuser.getFuserShopName());
        this.workerSpinner = (TextView)findViewById(R.id.ab_worker_fil_two);
        this.workerSpinner.setText(this.fuser.getFuserNo());
        this.abStartDate = (EditText)findViewById(R.id.ab_begindate_filtrate);
        this.abStartDate.setText(this.aaaaa);
        this.abEndDate = (EditText)findViewById(R.id.abnormal_stopdate_filtrate);
        this.abEndDate.setText(this.aaaaa);
        this.abUpDay = (Button)findViewById(R.id.abnormal_upDay);
        this.abDownDay = (Button)findViewById(R.id.abnormal_downDay);
        this.abNowDay = (TextView)findViewById(R.id.abnormal_nowDay);
        this.abNowDay.setText(this.aaaaa);
        this.abFil = (Button)findViewById(R.id.abnormal_fil_button);
        this.abListView = (ListView)findViewById(R.id.abnormal_fragment_list);
        this.abListView.setOnItemClickListener(this);
        this.abZftype = (Spinner)findViewById(R.id.ab_zftype_fil);
        this.abStartDate.setOnClickListener(this);
        this.abEndDate.setOnClickListener(this);
        this.abUpDay.setOnClickListener(this);
        this.abDownDay.setOnClickListener(this);
        this.abFil.setOnClickListener(this);
    }

    private void xiaSet(ArrayList<Order> paramArrayList) {
        this.abListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
                Intent intent = new Intent((Context)AbnormalFragmentTwo.this, DetailInfo.class);
                intent.putExtra("order", (Serializable)AbnormalFragmentTwo.this.abList.get(param1Int));
                AbnormalFragmentTwo.this.startActivity(intent);
            }
        });
        String[] arrayOfString1 = new String[this.abList.size()];
        String[] arrayOfString2 = new String[1 + this.abList.size()];
        byte b = 0;
        arrayOfString2[0] = "全部";
        String[] arrayOfString3 = new String[1 + this.abList.size()];
        arrayOfString3[0] = "全部";
        int i;
        int k;
        for (i = 0; i < this.abList.size(); i = k) {
            arrayOfString1[i] = ((Order)this.abList.get(i)).getDshopname();
            System.out.println(arrayOfString1[i]);
            if (((Order)this.abList.get(i)).getDzftype().equals("ZFBZF")) {
                arrayOfString3[i + 1] = "支付宝";
            } else {
                arrayOfString3[i + 1] = "微信";
            }
            k = i + 1;
            arrayOfString2[k] = ((Order)this.abList.get(i)).getDworkerno();
        }
        HashSet hashSet = new HashSet(Arrays.asList((Object[])arrayOfString3));
        ArrayAdapter arrayAdapter = new ArrayAdapter((Context)this, android.R.layout.simple_spinner_item, hashSet.toArray((Object[])new String[hashSet.size()]));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        this.abZftype.setAdapter((SpinnerAdapter)arrayAdapter);
        int j = arrayAdapter.getCount();
        while (b < j) {
            if ("全部".equals(((String)arrayAdapter.getItem(b)).toString()))
                this.abZftype.setSelection(b, true);
            b++;
        }
    }

    public void aaa(ArrayList<Order> paramArrayList) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int j;
        for (int i = 0; i < paramArrayList.size(); i = j) {
            j = i + 1;
            for (int k = j; k < paramArrayList.size(); k++) {
                if (simpleDateFormat.parse(((Order)paramArrayList.get(i)).getDdate()).before(simpleDateFormat.parse(((Order)paramArrayList.get(k)).getDdate()))) {
                    Order order = paramArrayList.get(i);
                    paramArrayList.set(i, paramArrayList.get(k));
                    paramArrayList.set(k, order);
                }
            }
        }
    }

    public void async() {
        this.abList.clear();
        if (this.abStartDate.getText().toString() != this.abEndDate.getText().toString()) {
            TextView textView = this.abNowDay;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.abStartDate.getText().toString());
            stringBuilder.append("-");
            stringBuilder.append(this.abEndDate.getText().toString());
            textView.setText(stringBuilder.toString());
        } else {
            this.abNowDay.setText(this.abEndDate.getText().toString());
        }
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setEnableRedirects(true);
        String str1 = (new AsyncHttpUrl()).getSelectAbnormalUrl();
        String str2 = getSharedPreferences("data", 0).getString("Token", "");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", str2);
        String str3 = this.fuser.getFshopNo();
        System.out.println(str3);
        requestParams.put("dmdbh", this.fuser.getFshopNo());
        requestParams.put("ddateStart", this.abStartDate.getText().toString());
        requestParams.put("ddateEnd", this.abEndDate.getText().toString());
        requestParams.put("dworkerno", this.workerSpinner.getText().toString());
        asyncHttpClient.post(str1, requestParams, (ResponseHandlerInterface)new JsonHttpResponseHandler() {
            public void onFailure(int param1Int, Header[] param1ArrayOfHeader, Throwable param1Throwable, JSONObject param1JSONObject) { AbnormalFragmentTwo.this.progressDialog.dismiss(); }

            public void onSuccess(int param1Int, Header[] param1ArrayOfHeader, JSONObject param1JSONObject) {
                String str = new String(String.valueOf(param1JSONObject));
                System.out.println(str);
                try {
                    JSONArray jSONArray = param1JSONObject.getJSONArray("data");
                    for (byte b = 0;; b++) {
                        if (b < jSONArray.length()) {
                            JSONObject jSONObject = jSONArray.getJSONObject(b);
                            Order order = new Order();
                            order.setDwxid(jSONObject.optString("dwxid"));
                            order.setDzfbid(jSONObject.optString("dzfbid"));
                            order.setDmdbh(jSONObject.optString("dmdbh"));
                            order.setDdh(jSONObject.optString("ddh"));
                            order.setDdate(jSONObject.getLong("ddate"));
                            order.setDmoney((new DecimalFormat("0.00")).format(jSONObject.get("dmoney")));
                            order.setDzftype(jSONObject.optString("dzftype"));
                            order.setDstate(jSONObject.optString("dstate"));
                            order.setDworkerno(jSONObject.optString("dworkerno"));
                            order.setDshopname(jSONObject.optString("dshopname"));
                            if (!order.getDstate().equals("支付成功"))
                                AbnormalFragmentTwo.this.abList.add(order);
                        } else {
                            AbnormalListViewAdapter abnormalListViewAdapter = new AbnormalListViewAdapter(AbnormalFragmentTwo.this.abList);
                            try {
                                AbnormalFragmentTwo.this.aaa(AbnormalFragmentTwo.this.abList);
                            } catch (ParseException parseException) {
                                parseException.printStackTrace();
                            }
                            AbnormalFragmentTwo.this.abListView.setAdapter((ListAdapter)abnormalListViewAdapter);
                            AbnormalFragmentTwo.this.progressDialog.dismiss();
                            AbnormalFragmentTwo abnormalFragmentTwo1 = AbnormalFragmentTwo.this;
                            abnormalFragmentTwo1.xiaSet(abnormalFragmentTwo1.abList);
                        }
                    }
                } catch (JSONException jSONException) {
                    jSONException.printStackTrace();
                }
                AbnormalFragmentTwo abnormalFragmentTwo = AbnormalFragmentTwo.this;
                abnormalFragmentTwo.xiaSet(abnormalFragmentTwo.abList);
            }
        });
    }

    public void onClick(View paramView) {
        DatePickerDialog datePickerDialog2;
        Calendar calendar2;
        switch (paramView.getId()) {
            default:
                return;
            case 2131296278:
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse(this.abNowDay.getText().toString());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, -1);
                    String str = simpleDateFormat.format(calendar.getTime());
                    this.abStartDate.setText(str);
                    this.abEndDate.setText(str);
                    this.abNowDay.setText(str);
                    this.progressDialog = ProgressDialog.show((Context)this, "请稍等……", "正在获取数据……", true);
                    async();
                    return;
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                    return;
                }
            case 2131296277:
                calendar2 = Calendar.getInstance();
                datePickerDialog2 = new DatePickerDialog((Context)this, new DatePickerDialog.OnDateSetListener() {
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
                        EditText editText = AbnormalFragmentTwo.this.abEndDate;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(param1Int1);
                        stringBuilder.append("-");
                        stringBuilder.append(str1);
                        stringBuilder.append("-");
                        stringBuilder.append(str2);
                        editText.setText(stringBuilder.toString());
                    }
                },  calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DATE));
                datePickerDialog2.show();
                this.abEndDate.clearFocus();
                return;
            case 2131296269:
                this.progressDialog = ProgressDialog.show((Context)this, "请稍等……", "正在获取数据……", true);
                async();
                return;
            case 2131296268:
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse(this.abNowDay.getText().toString());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, 1);
                    String str = simpleDateFormat.format(calendar.getTime());
                    this.abStartDate.setText(str);
                    this.abEndDate.setText(str);
                    this.abNowDay.setText(str);
                    this.progressDialog = ProgressDialog.show((Context)this, "请稍等……", "正在获取数据……", true);
                    async();
                    return;
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                    return;
                }
            case 2131296262:
                break;
        }
        Calendar calendar1 = Calendar.getInstance();
        DatePickerDialog datePickerDialog1 = new DatePickerDialog((Context)this, new DatePickerDialog.OnDateSetListener() {
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
                EditText editText = AbnormalFragmentTwo.this.abStartDate;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(param1Int1);
                stringBuilder.append("-");
                stringBuilder.append(str1);
                stringBuilder.append("-");
                stringBuilder.append(str2);
                editText.setText(stringBuilder.toString());
            }
        },  calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DATE));
        datePickerDialog1.show();
        this.abStartDate.clearFocus();
    }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.abnormal_orders_fragment_two);
        this.progressDialog = ProgressDialog.show((Context)this, "请稍后……", "正在获取数据……", true);
        initView();
        async();
    }

    @Override
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        Order order = this.abList.get(paramInt);
        Intent intent = new Intent((Context)this, DetailInfo.class);
        intent.putExtra("order", (Serializable)order);
        startActivity(intent);
    }
}
