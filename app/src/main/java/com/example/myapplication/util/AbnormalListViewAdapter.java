package com.example.myapplication.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.Bean.Order;
import com.example.myapplication.R;

import java.util.List;

public class AbnormalListViewAdapter extends BaseAdapter {
  private List<Order> listOrder;
  
  public AbnormalListViewAdapter(List<Order> paramList) { this.listOrder = paramList; }
  
  public int getCount() {
    List<Order> list = this.listOrder;
    return (list == null) ? 0 : list.size();
  }
  
  public Object getItem(int paramInt) { return this.listOrder.get(paramInt); }
  
  public long getItemId(int paramInt) { return paramInt; }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    if (paramView == null)
      paramView = LayoutInflater.from(paramViewGroup.getContext())
              .inflate(R.layout.abnormal_orders_fragment_list_item, paramViewGroup, false);
    ((TextView)paramView.findViewById(R.id.abnormal_pay_status)).setText((listOrder.get(paramInt)).getDstate());
    TextView textView1 = (TextView)paramView.findViewById(R.id.abnormal_pay_type);
    if ((listOrder.get(paramInt)).getDzftype().equals("WXZF")) {
      textView1.setText("微信支付");
    } else if ((listOrder.get(paramInt)).getDzftype().equals("ZFBZF")) {
      textView1.setText("支付宝支付");
    } else if ((listOrder.get(paramInt)).getDzftype().equals("WXTH")) {
      textView1.setText("微信退款");
    } else if (listOrder.get(paramInt).getDzftype().equals("ZFBTH")){
      textView1.setText("支付宝退款");
    }
    ((TextView)paramView.findViewById(R.id.abnormal_pay_time)).setText((listOrder.get(paramInt)).getDdate());
    TextView textView2 = (TextView)paramView.findViewById(R.id.abnormal_pay_money);
    if ((listOrder.get(paramInt)).getDzftype().equals("WXZF")) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("+");
      stringBuilder.append((listOrder.get(paramInt)).getDmoney());
      textView2.setText(stringBuilder.toString());
    } else if ((listOrder.get(paramInt)).getDzftype().equals("ZFBZF")) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("+");
      stringBuilder.append((listOrder.get(paramInt)).getDmoney());
      textView2.setText(stringBuilder.toString());
    } else if ((listOrder.get(paramInt)).getDzftype().equals("WXTH")) {
      textView2.setText((listOrder.get(paramInt)).getDmoney());
    } 
    ((TextView)paramView.findViewById(R.id.ab_worker_no)).setText((listOrder.get(paramInt)).getDworkerno());
    return paramView;
  }
}


/* Location:              C:\Users\SYQ\Desktop\支付查询_classes_dex2jar.jar!\com\smrj\myapplicatio\\util\AbnormalListViewAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.2
 */