package com.divide.ibitech.divide_ibitech;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by s216100801 on 2018/04/18.
 */

public class MpagerAdapter extends PagerAdapter {

    private int[] layouts;
    private android.view.LayoutInflater LayoutInflater;
    private Context context;

    public MpagerAdapter(int [] layouts, Context context )

    {
    this.context = context;
    LayoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.inflate(layouts[position],container,false);
container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

View view    = (View) object;
container.removeView(view);


    }
}
