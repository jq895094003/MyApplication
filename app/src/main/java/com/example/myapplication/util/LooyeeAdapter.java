package com.example.myapplication.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.Bean.StatisticBean;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 此适配器不再使用
 */
public class LooyeeAdapter extends BaseAdapter {
  ArrayList<StatisticBean> statisticBeans;
  
  public LooyeeAdapter(ArrayList<StatisticBean> paramList) { this.statisticBeans = paramList; }
  
  public int getCount() {
    List<StatisticBean> list = this.statisticBeans;
    return (list == null) ? 0 : list.size();
  }
  
  public Object getItem(int paramInt) { return this.statisticBeans.get(paramInt); }
  
  public long getItemId(int paramInt) { return paramInt; }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    if (paramView == null)
      paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.statistical_list,
              paramViewGroup, false);
    TextView shopNo = paramView.findViewById(R.id.statistical_shopNo);
    shopNo.setText(statisticBeans.get(paramInt).getYgBH());
    TextView wxsk = paramView.findViewById(R.id.statistical_wxsk);
    wxsk.setText(statisticBeans.get(paramInt).getWxSK());
    TextView wxtk = paramView.findViewById(R.id.statistical_wxtk);
    wxtk.setText(statisticBeans.get(paramInt).getWxTK());
    TextView zfbsk = paramView.findViewById(R.id.statistical_zfbsk);
    zfbsk.setText(statisticBeans.get(paramInt).getZfbSK());
    TextView zfbtk = paramView.findViewById(R.id.statistical_zfbtk);
    zfbtk.setText(statisticBeans.get(paramInt).getZfbTK());
    TextView zfsb = paramView.findViewById(R.id.statistical_zfsb);
    zfsb.setText(statisticBeans.get(paramInt).getZfSB());
    return paramView;
  }
}


/* Location:              C:\Users\SYQ\Desktop\支付查询_classes_dex2jar.jar!\com\smrj\myapplicatio\\util\LooyeeAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.2
 */