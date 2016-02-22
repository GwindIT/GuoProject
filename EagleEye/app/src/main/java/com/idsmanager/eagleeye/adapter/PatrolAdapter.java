package com.idsmanager.eagleeye.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.idsmanager.eagleeye.R;
import com.idsmanager.eagleeye.domain.PatrolNews;

import java.util.List;

/**
 * Created by wind on 2016/2/19.
 */
public class PatrolAdapter extends BaseAdapter {

    protected List<PatrolNews> list;
    protected Context context;
    PatrolHolder holder;


    public PatrolAdapter(Context context, List<PatrolNews> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_show_problem, null);
        }
        PatrolNews patrolNews = list.get(position);
        holder = PatrolHolder.getHolder(convertView);
        holder.tvTime.setText(patrolNews.createTime);
        holder.tvType.setText(patrolNews.patrolType);
        holder.tvDetail.setText(patrolNews.detial);
        return convertView;
    }

    static class PatrolHolder {
        TextView tvTime, tvType, tvDetail;

        public PatrolHolder(View convertView) {
            tvTime = (TextView) convertView.findViewById(R.id.tv_item_date);
            tvType = (TextView) convertView.findViewById(R.id.tv_item_type);
            tvDetail = (TextView) convertView.findViewById(R.id.tv_item_detail);
        }

        public static PatrolHolder getHolder(View convertView) {
            PatrolHolder viewHolder = (PatrolHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new PatrolHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }
    }
}
