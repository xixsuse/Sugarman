package com.sugarman.myb.ui.activities.mainScreeen;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sugarman.myb.R;

/**
 * Created by nikita on 03.11.2017.
 */

public class WalkDataViewPagerAdapter extends PagerAdapter {
  private Context mContext;
  private String mTodaySteps;
  private int steps;
  private LayoutInflater mLayoutInflater;
  private final int MAX_VALUE = 3;

  public WalkDataViewPagerAdapter(Context context) {
    this.mContext = context;
    mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  public void setWalkData(String todaySteps) {
    mTodaySteps = todaySteps;
    steps = Integer.parseInt(todaySteps);
    notifyDataSetChanged();
  }

  @Override public int getCount() {
    return Integer.MAX_VALUE; // INFINITE SCROLLING
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    View itemView = mLayoutInflater.inflate(R.layout.item_walk_data, container, false);
    final TextView tvData1 = (TextView) itemView.findViewById(R.id.tvData1);
    final TextView tvData2 = (TextView) itemView.findViewById(R.id.tvData2);
    int virtualPosition = position % MAX_VALUE;                               //INFINITE SCROLLING
    switch (virtualPosition) {
      case 0:
        tvData1.setText(mTodaySteps);
        tvData2.setText(tvData2.getContext().getResources().getString(R.string.steps_caps));
        break;
      case 1:
        tvData1.setText(String.format("%.2f", steps * 0.000762f));
        tvData1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
        tvData2.setText(tvData2.getContext().getResources().getString(R.string.distance));
        break;
      case 2:
        tvData1.setText(Integer.toString((int) (steps * 0.0435f)));
        tvData1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        tvData2.setText(tvData2.getContext().getResources().getString(R.string.calories));
        break;
    }
    container.addView(itemView);
    return itemView;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((ConstraintLayout) object);
  }

  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }
}
