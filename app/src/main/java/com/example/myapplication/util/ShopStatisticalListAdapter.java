package com.example.myapplication.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.Bean.StatisticBean;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ShopStatisticalListAdapter extends BaseAdapter {

    ArrayList<StatisticBean> statisticBeans;

    public ShopStatisticalListAdapter(ArrayList<StatisticBean> statisticBeans) {
        this.statisticBeans = statisticBeans;
    }

    @Override
    public int getCount() {
        return statisticBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return statisticBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.shopstatistical_list, parent, false);
        TextView shopNo = convertView.findViewById(R.id.statistical_shopNo);
        shopNo.setText(statisticBeans.get(position).getYgBH());
        TextView wxsk = convertView.findViewById(R.id.statistical_wxsk);
        wxsk.setText(statisticBeans.get(position).getWxSK());
        TextView wxtk = convertView.findViewById(R.id.statistical_wxtk);
        wxtk.setText(statisticBeans.get(position).getWxTK());
        TextView zfbsk = convertView.findViewById(R.id.statistical_zfbsk);
        zfbsk.setText(statisticBeans.get(position).getZfbSK());
        TextView zfbtk = convertView.findViewById(R.id.statistical_zfbtk);
        zfbtk.setText(statisticBeans.get(position).getZfbTK());
        TextView zfsb = convertView.findViewById(R.id.statistical_zfsb);
        zfsb.setText(statisticBeans.get(position).getZfSB());
        TextView rate = convertView.findViewById(R.id.statistical_rate);
        rate.setText(statisticBeans.get(position).getRate());
        return convertView;
    }
}
