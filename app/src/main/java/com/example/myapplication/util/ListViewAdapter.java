package com.example.myapplication.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.Bean.Order;
import com.example.myapplication.R;

import java.text.DecimalFormat;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private List<Order> listOrder;

    public ListViewAdapter(List<Order> paramList) {
        this.listOrder = paramList;
    }

    public int getCount() {
        List<Order> list = this.listOrder;
        return (list == null) ? 0 : list.size();
    }

    public Object getItem(int paramInt) {
        return this.listOrder.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        if (paramView == null)
            paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.detail_fragment_list_item, paramViewGroup, false);
        ((TextView) paramView.findViewById(R.id.pay_status)).setText(((Order) this.listOrder.get(paramInt)).getDstate());
        TextView textView1 = (TextView) paramView.findViewById(R.id.pay_type);
        if (((Order) this.listOrder.get(paramInt)).getDzftype().equals("WXZF")) {
            textView1.setText("微信支付");
        } else if (((Order) this.listOrder.get(paramInt)).getDzftype().equals("ZFBZF")) {
            textView1.setText("支付宝支付");
        }
        ((TextView) paramView.findViewById(R.id.pay_time)).setText(((Order) this.listOrder.get(paramInt)).getDdate());
        TextView textView2 = (TextView) paramView.findViewById(R.id.pay_money);
        if (((Order) this.listOrder.get(paramInt)).getDzftype().equals("WXZF")) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("+");
            stringBuilder.append(((Order) this.listOrder.get(paramInt)).getDmoney());
            textView2.setText(stringBuilder.toString());
        } else if (((Order) this.listOrder.get(paramInt)).getDzftype().equals("ZFBZF")) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("+");
            stringBuilder.append(((Order) this.listOrder.get(paramInt)).getDmoney());
            textView2.setText(stringBuilder.toString());
        }
        ((TextView) paramView.findViewById(R.id.woker_no)).setText(((Order) this.listOrder.get(paramInt)).getDworkerno());
        TextView orderRate = paramView.findViewById(R.id.pay_rate);
        DecimalFormat df = new DecimalFormat("#0.0");
        if (listOrder.get(paramInt).getRate() == null){
            orderRate.setText("0.0");
        }else {
            orderRate.setText(df.format(listOrder.get(paramInt).getRate()));
        }
        return paramView;
    }
}


/* Location:              C:\Users\SYQ\Desktop\支付查询_classes_dex2jar.jar!\com\smrj\myapplicatio\\util\ListViewAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.2
 */