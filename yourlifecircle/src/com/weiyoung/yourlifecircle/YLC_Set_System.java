package com.weiyoung.yourlifecircle;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class YLC_Set_System extends Activity {
	private ListView listView_system_set;
	private Drawable list_checked_drawable;
	private Drawable list_unchecked_drawable;
	private int selected=0;                	//这个值，应该从本地获取
	private List<String> listItem;
	MySystemSetAdapter mySystemSetAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ylc_set_system);
		initData();
		listView_system_set.setAdapter(mySystemSetAdapter);
		listView_system_set.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				selected=position;
				mySystemSetAdapter.notifyDataSetChanged();
				
			}
		});
	}
	class MySystemSetAdapter extends BaseAdapter{
		LayoutInflater layoutInflater;
		public MySystemSetAdapter(Context context) {
			// TODO Auto-generated constructor stub
			layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listItem.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolderOfSystemSet vhofs;
			if(convertView==null){
				vhofs=new ViewHolderOfSystemSet();
				convertView=layoutInflater.inflate(R.layout.activity_ylc_set_system_item, parent, false);
				vhofs.textView1=(TextView) convertView.findViewById(R.id.textView_system_set);
				vhofs.imageView1=(ImageView) convertView.findViewById(R.id.imageView_system_set);
				convertView.setTag(vhofs);
			}else{
				vhofs=(ViewHolderOfSystemSet) convertView.getTag();
			}
			vhofs.textView1.setText(listItem.get(position));
			vhofs.imageView1.setImageDrawable(list_checked_drawable);
			if(selected==position){
				vhofs.imageView1.setImageDrawable(list_checked_drawable);
			}else{
				vhofs.imageView1.setImageDrawable(list_unchecked_drawable);
			}
			return convertView;
		}
		
	}
	class ViewHolderOfSystemSet{
		TextView textView1;
		ImageView imageView1;
	}
	public void initData() {
		listView_system_set=(ListView) findViewById(R.id.listView_system_set);
		list_checked_drawable=getResources().getDrawable(R.drawable.ylc_checked);
		list_unchecked_drawable=getResources().getDrawable(R.drawable.ylc_unchecked);
		listItem=new ArrayList<String>();
		String[]str=getResources().getStringArray(R.array.ylc_system_set);
		for (String str1 : str) {
			listItem.add(str1);
		}
		Log.i("TAG", ""+listItem.size());
		mySystemSetAdapter=new MySystemSetAdapter(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ylc__set__system, menu);
		return true;
	}

}
