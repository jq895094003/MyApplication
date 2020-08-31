package com.example.myapplication.bottom;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.example.myapplication.util.AsyncHttpUrl;
import com.example.myapplication.util.ListViewAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
import cz.msebera.android.httpclient.params.CoreConnectionPNames;

public class DetailFragment extends Fragment {
    private ArrayAdapter<String> arrayAdapter;//声明数组适配器

    private Button button;//声明

    private Button downDay;

    private EditText end_fil;

    int indes = 1;

    String initString;

    ArrayList<Order> list = new ArrayList<Order>();

    private PullToRefreshListView listView;

    ListViewAdapter listViewAdapter;

    ArrayList<Order> mItems = new ArrayList<Order>();

    private PullToRefreshBase.OnRefreshListener2<ListView> mListViewOnRefreshListener2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        public void onPullDownToRefresh(PullToRefreshBase<ListView> param1PullToRefreshBase) {
            listView.postDelayed(new Runnable() {
                public void run() {
                    list.clear();
                    indes = 1;
                    mItems.clear();
                    InitNewJK();
                    initAsync(String.valueOf(DetailFragment.this.indes), "20");
                    listView.onRefreshComplete();
                }
            }, 500);
        }

        public void onPullUpToRefresh(PullToRefreshBase<ListView> param1PullToRefreshBase) {
            listView.postDelayed(new Runnable() {
                public void run() {
                    indes = 1 + indes;
                    async(String.valueOf(DetailFragment.this.indes), "20");
                    listView.onRefreshComplete();
                }
            }, 500);
        }
    };

    ArrayList<Order> newList = new ArrayList<Order>();

    private TextView nowDay;

    ProgressDialog progressDialog;

    private TextView shopname_fil;

    private EditText start_fil;

    private EditText pay_money;

    private Spinner typefil;

    private Button upDay;

    private Spinner workerno_fil;

    private ProgressBar progressBar;

    private void InitNewJK() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String str1 = (new AsyncHttpUrl()).getSelectDataFromShopName();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String str2 = sharedPreferences.getString("Token", "");
        String str3 = sharedPreferences.getString("AAA", "");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", str2);
        requestParams.put("dmdbh", str3);
        requestParams.put("ddateStart", this.start_fil.getText().toString());
        requestParams.put("ddateEnd", this.end_fil.getText().toString());
        asyncHttpClient.post(str1, requestParams, (ResponseHandlerInterface) new JsonHttpResponseHandler() {
            public void onFailure(int param1Int, Header[] param1ArrayOfHeader, Throwable param1Throwable, JSONObject param1JSONObject) {
                super.onFailure(param1Int, param1ArrayOfHeader, param1Throwable, param1JSONObject);
                param1Throwable.printStackTrace();
            }

            public void onSuccess(int param1Int, Header[] param1ArrayOfHeader, JSONObject param1JSONObject) {
                super.onSuccess(param1Int, param1ArrayOfHeader, param1JSONObject);
                boolean bool = param1JSONObject.optString("code").equals("200");
                byte b = 0;
                if (bool)
                    try {
                        JSONArray jSONArray = param1JSONObject.getJSONArray("data");
                        new DecimalFormat("#0.00");
                        ArrayList<String> arrayList = new ArrayList();
                        arrayList.add("全部");
                        while (b < jSONArray.length()) {
                            arrayList.add(jSONArray.getJSONObject(b).optString("dworkerno"));
                            b++;
                        }
                        ArrayList arrayList1 = DetailFragment.this.hashSet(arrayList);
                        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList1);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        DetailFragment.this.workerno_fil.setAdapter((SpinnerAdapter) DetailFragment.this.arrayAdapter);
                        TextView textView = DetailFragment.this.nowDay;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(DetailFragment.this.start_fil.getText().toString());
                        stringBuilder.append("-");
                        stringBuilder.append(DetailFragment.this.end_fil.getText().toString());
                        textView.setText(stringBuilder.toString());
                        return;
                    } catch (JSONException jSONException) {
                        jSONException.printStackTrace();
                        return;
                    }
                Toast.makeText((Context) DetailFragment.this.getActivity(), param1JSONObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getNowDay() {
        return (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
    }

    private String getUpDay() {
        return null;
    }

    private ArrayList<String> hashSet(ArrayList<String> paramArrayList) {
        HashSet<String> hashSet = new HashSet();
        ArrayList<String> arrayList = new ArrayList();
        for (String str : paramArrayList) {
            if (hashSet.add(str))
                arrayList.add(str);
        }
        return arrayList;
    }

    private ArrayList<Order> mockList(ArrayList<Order> paramArrayList, int paramInt1, int paramInt2) {
        for (int i = paramInt1; i < paramInt1 + paramInt2 && i < paramArrayList.size(); i++) {
            Order order = this.list.get(i);
            this.mItems.add(order);
        }
        return this.mItems;
    }

    private void newJK() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String str1 = (new AsyncHttpUrl()).getSelectDataFromShopName();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String str2 = sharedPreferences.getString("Token", "");
        String str3 = sharedPreferences.getString("AAA", "");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", str2);
        requestParams.put("dmdbh", str3);
        requestParams.put("ddateStart", this.start_fil.getText().toString());
        requestParams.put("ddateEnd", this.end_fil.getText().toString());
        asyncHttpClient.post(str1, requestParams, (ResponseHandlerInterface) new JsonHttpResponseHandler() {
            public void onFailure(int param1Int, Header[] param1ArrayOfHeader, Throwable param1Throwable, JSONObject param1JSONObject) {
                super.onFailure(param1Int, param1ArrayOfHeader, param1Throwable, param1JSONObject);
                param1Throwable.printStackTrace();
            }

            public void onSuccess(int param1Int, Header[] param1ArrayOfHeader, JSONObject param1JSONObject) {
                super.onSuccess(param1Int, param1ArrayOfHeader, param1JSONObject);
                boolean bool = param1JSONObject.optString("code").equals("200");
                byte b = 0;
                if (bool)
                    try {
                        JSONArray jSONArray = param1JSONObject.getJSONArray("data");
                        new DecimalFormat("#0.00");
                        ArrayList<String> arrayList = new ArrayList();
                        arrayList.add("全部");
                        while (b < jSONArray.length()) {
                            arrayList.add(jSONArray.getJSONObject(b).optString("dworkerno"));
                            b++;
                        }
                        ArrayList arrayList1 = DetailFragment.this.hashSet(arrayList);
                        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList1);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        DetailFragment.this.arrayAdapter.notifyDataSetChanged();
                        TextView textView = DetailFragment.this.nowDay;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(DetailFragment.this.start_fil.getText().toString());
                        stringBuilder.append("-");
                        stringBuilder.append(DetailFragment.this.end_fil.getText().toString());
                        textView.setText(stringBuilder.toString());
                        return;
                    } catch (JSONException jSONException) {
                        jSONException.printStackTrace();
                        return;
                    }
                Toast.makeText((Context)getActivity(), param1JSONObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void aaa(ArrayList<Order> paramArrayList) throws ParseException {
        byte b;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int i = 0;
        while (true) {
            int j = paramArrayList.size();
            b = 0;
            if (i < j) {
                int k = i + 1;
                for (int m = k; m < paramArrayList.size(); m++) {
                    Date date = simpleDateFormat.parse(((Order) paramArrayList.get(i)).getDdate());
                    if (simpleDateFormat.parse(((Order) paramArrayList.get(m)).getDdate()).before(date)) {
                        Order order = paramArrayList.get(i);
                        paramArrayList.set(i, paramArrayList.get(m));
                        paramArrayList.set(m, order);
                    }
                }
                i = k;
                continue;
            }
            break;
        }

    }

    public void async(String paramString1, String paramString2) {

        System.out.println("paramString1 = " + paramString1);
        System.out.println("paramString2 = " + paramString2);

        progressDialog = ProgressDialog.show(getActivity(),"请稍后","正在查询数据",false);
        progressDialog.setCancelable(true);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.getHttpClient().getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
        asyncHttpClient.getHttpClient().getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
        String str1 = (new AsyncHttpUrl()).getSelectAbnormalUrl();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String str2 = sharedPreferences.getString("Token", "");
        String str3 = sharedPreferences.getString("AAA", "");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", str2);
        requestParams.put("dmdbh", str3);
        Log.i("dmdbh................",str3);
        requestParams.put("ddateStart", this.start_fil.getText().toString());
        requestParams.put("ddateEnd", this.end_fil.getText().toString());
        requestParams.put("state", "支付成功");
        requestParams.put("page", paramString1);
        requestParams.put("dmoney", pay_money.getText().toString());
        requestParams.put("size", paramString2);
        if (!this.workerno_fil.getSelectedItem().equals("全部"))
            requestParams.put("dworkerno", this.workerno_fil.getSelectedItem());
        if (this.typefil.getSelectedItem().equals("支付宝")) {
            requestParams.put("dzftype", "ZFBZF");
        } else if (this.typefil.getSelectedItem().equals("微信")) {
            requestParams.put("dzftype", "WXZF");
        }
        asyncHttpClient.post(str1, requestParams, (ResponseHandlerInterface) new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int param1Int, Header[] param1ArrayOfHeader, Throwable param1Throwable, JSONObject param1JSONObject) {
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(int param1Int, Header[] param1ArrayOfHeader, JSONObject param1JSONObject) {
                boolean bool = param1JSONObject.optString("code").equals("0");
                byte b = 0;
                if (bool) {
                    new String(String.valueOf(param1JSONObject));
                    try {
                        JSONArray jSONArray = param1JSONObject.getJSONArray("data");
                        Gson gson = new Gson();
                        for (int i = 0; i < jSONArray.length(); i++) {
                            JSONObject jSONObject = jSONArray.getJSONObject(i);
                            Order order = (Order) gson.fromJson(String.valueOf(jSONObject), Order.class);
                            DetailFragment.this.list.add(order);
                        }
//                        while (b < jSONArray.length()) {
//                            JSONObject jSONObject = jSONArray.getJSONObject(b);
//                            Order order = (Order) (new Gson()).fromJson(String.valueOf(jSONObject), Order.class);
//                            DetailFragment.this.list.add(order);
//                            b++;
//                        }
                        try {
                            DetailFragment.this.aaa(DetailFragment.this.list);
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                        DetailFragment.this.listViewAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                        return;
                    } catch (JSONException jSONException) {
                        jSONException.printStackTrace();
                        return;
                    }
                }
                Toast.makeText((Context) DetailFragment.this.getActivity(), param1JSONObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<Order> getList(ArrayList<Order> paramArrayList, int paramInt) {
        for (int i = paramInt + 1; i < paramInt + 5; i++) ;
        return null;
    }

    public void initAsync(String paramString1, String paramString2) {
        this.newList.clear();
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String str1 = (new AsyncHttpUrl()).getSelectAbnormalUrl();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", 0);
        String str2 = sharedPreferences.getString("Token", "");
        String str3 = sharedPreferences.getString("AAA", "");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", str2);
        requestParams.put("dmdbh", str3);
        requestParams.put("ddateStart", this.start_fil.getText().toString());
        requestParams.put("ddateEnd", this.end_fil.getText().toString());
        requestParams.put("dmoney", this.pay_money.getText().toString());

        System.out.println("this.pay_money.getText().toString() = " + this.pay_money.getText().toString());
        
        requestParams.put("state", "支付成功");
        requestParams.put("page", paramString1);
        requestParams.put("size", paramString2);
        if (!this.workerno_fil.getSelectedItem().equals("全部"))
            requestParams.put("dworkerno", this.workerno_fil.getSelectedItem());
        if (this.typefil.getSelectedItem().equals("支付宝")) {
            requestParams.put("dzftype", "ZFBZF");
        } else if (this.typefil.getSelectedItem().equals("微信")) {
            requestParams.put("dzftype", "WXZF");
        }
        asyncHttpClient.post(str1, requestParams, (ResponseHandlerInterface) new JsonHttpResponseHandler() {
            public void onFailure(int param1Int, Header[] param1ArrayOfHeader, Throwable param1Throwable, JSONObject param1JSONObject) {
                param1Throwable.printStackTrace();
            }

            public void onSuccess(int param1Int, Header[] param1ArrayOfHeader, JSONObject param1JSONObject) {
                boolean bool = param1JSONObject.optString("code").equals("0");
                byte b = 0;
                if (bool) {
                    new String(String.valueOf(param1JSONObject));
                    try {
                        JSONArray jSONArray = param1JSONObject.getJSONArray("data");
                        Gson gson = new Gson();
                        for (int i = 0; i < jSONArray.length(); i++) {
                            JSONObject jSONObject = jSONArray.getJSONObject(i);
                            Order order = (Order) gson.fromJson(String.valueOf(jSONObject), Order.class);
                            DetailFragment.this.list.add(order);
                        }


//                        while (b < jSONArray.length()) {
//                            JSONObject jSONObject = jSONArray.getJSONObject(b);
//                            Order order = (Order) (new Gson()).fromJson(String.valueOf(jSONObject), Order.class);
//                            System.out.println("order = " + order);
//                            DetailFragment.this.list.add(order);
//                            b++;
//                        }
                        try {
                            DetailFragment.this.aaa(DetailFragment.this.list);
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                        DetailFragment.this.listViewAdapter = new ListViewAdapter(DetailFragment.this.list);
                        DetailFragment.this.listView.setAdapter((ListAdapter) DetailFragment.this.listViewAdapter);
                        return;
                    } catch (JSONException jSONException) {
                        jSONException.printStackTrace();
                        return;
                    }
                }
                Toast.makeText((Context) DetailFragment.this.getActivity(), param1JSONObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onActivityCreated(@Nullable Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        this.nowDay = (TextView) getActivity().findViewById(R.id.nowDay);
        this.nowDay.setText(getNowDay());
        this.workerno_fil = (Spinner) getActivity().findViewById(R.id.worker_fil);
        this.shopname_fil = (TextView) getActivity().findViewById(R.id.shop_filtrate);
        String str = getActivity().getSharedPreferences("data", 0).getString("BBB", "");
        this.shopname_fil.setText(str);
        this.typefil = (Spinner) getActivity().findViewById(R.id.zftype_fil);
        this.listView = (PullToRefreshListView) getActivity().findViewById(R.id.detail_fragment_list);
        this.listView.setMode(PullToRefreshBase.Mode.BOTH);
        this.listView.setOnRefreshListener(this.mListViewOnRefreshListener2);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
                Order order = DetailFragment.this.list.get(param1Int - 1);
                Intent intent = new Intent((Context) DetailFragment.this.getActivity(), DetailInfo.class);
                intent.putExtra("order", (Serializable) order);
                DetailFragment.this.startActivity(intent);
            }
        });
        this.start_fil = (EditText) getActivity().findViewById(R.id.begindate_filtrate);
        this.start_fil.setText(getNowDay());
        this.start_fil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog((Context) DetailFragment.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
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
                        EditText editText = DetailFragment.this.start_fil;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(param2Int1);
                        stringBuilder.append("-");
                        stringBuilder.append(str1);
                        stringBuilder.append("-");
                        stringBuilder.append(str2);
                        editText.setText(stringBuilder.toString());
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                datePickerDialog.show();
                DetailFragment.this.start_fil.clearFocus();
            }
        });
        this.end_fil = (EditText) getActivity().findViewById(R.id.stopdate_filtrate);
        this.pay_money = (EditText) getActivity().findViewById(R.id.pay_money);
        this.end_fil.setText(getNowDay());
        this.end_fil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog((Context) DetailFragment.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
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
                        EditText editText = DetailFragment.this.end_fil;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(param2Int1);
                        stringBuilder.append("-");
                        stringBuilder.append(str1);
                        stringBuilder.append("-");
                        stringBuilder.append(str2);
                        editText.setText(stringBuilder.toString());
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                datePickerDialog.show();
                DetailFragment.this.end_fil.clearFocus();
            }
        });
        initAsync("1", "20");
        InitNewJK();
        this.upDay = (Button) getActivity().findViewById(R.id.upDay);
        this.upDay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                try {
                    DetailFragment.this.list.clear();
                    DetailFragment.this.indes = 1;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse(DetailFragment.this.end_fil.getText().toString());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, -1);
                    String str = simpleDateFormat.format(calendar.getTime());
                    DetailFragment.this.start_fil.setText(str);
                    DetailFragment.this.end_fil.setText(str);
                    DetailFragment.this.nowDay.setText(str);
                    DetailFragment.this.async(String.valueOf(DetailFragment.this.indes), "20");
                    DetailFragment.this.newJK();
                    return;
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                    return;
                }
            }
        });
        this.downDay = (Button) getActivity().findViewById(R.id.downDay);
        this.downDay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                try {
                    DetailFragment.this.list.clear();
                    DetailFragment.this.indes = 1;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse(DetailFragment.this.end_fil.getText().toString());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, 1);
                    String str = simpleDateFormat.format(calendar.getTime());
                    DetailFragment.this.start_fil.setText(str);
                    DetailFragment.this.end_fil.setText(str);
                    DetailFragment.this.nowDay.setText(str);
                    DetailFragment.this.async(String.valueOf(DetailFragment.this.indes), "20");
                    DetailFragment.this.newJK();
                    return;
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                    return;
                }
            }
        });
        this.button = (Button) getActivity().findViewById(R.id.fil_button);
        this.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                DetailFragment.this.list.clear();
                DetailFragment detailFragment = DetailFragment.this;
                detailFragment.indes = 1;
                detailFragment.async(String.valueOf(detailFragment.indes), "20");
                DetailFragment.this.newJK();
            }
        });
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        return paramLayoutInflater.inflate(R.layout.detailfragment, paramViewGroup, false);
    }
}
