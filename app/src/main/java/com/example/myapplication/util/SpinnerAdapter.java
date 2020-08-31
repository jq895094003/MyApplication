package com.example.myapplication.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.R;


public class SpinnerAdapter extends ArrayAdapter<String> {
  private Context mContext;
  
  private String[] mStringArray;

  public SpinnerAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
    super(context, resource, objects);
    context = mContext;
    objects = mStringArray;
  }


  public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    if (paramView == null)
      paramView = LayoutInflater.from(this.mContext).inflate(R.layout.spinner_item, paramViewGroup, false);
    TextView textView = (TextView)paramView.findViewById(R.id.spinner_item);
    textView.setText(this.mStringArray[paramInt]);
    textView.setTextSize(22.0F);
    textView.setTextColor(-16777216);
    return paramView;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    if (paramView == null)
      paramView = LayoutInflater.from(this.mContext).inflate(R.layout.spinner_item, paramViewGroup, false);
    TextView textView = (TextView)paramView.findViewById(R.id.spinner_item);
    textView.setText(this.mStringArray[paramInt]);
    textView.setTextSize(22.0F);
    textView.setTextColor(-16777216);
    return paramView;
  }
}


/* Location:              C:\Users\SYQ\Desktop\支付查询_classes_dex2jar.jar!\com\smrj\myapplicatio\\util\SpinnerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.2
 */