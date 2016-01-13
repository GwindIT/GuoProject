package com.weiyoung.yourlifecircle;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class YLC_AboutUS extends Activity {
	private List<String> listAboutus;
	private ListView listView_aboutus;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ylc_about_us);
		initData();
	}

	public void initData() {
		// TODO Auto-generated method stub
		String[] arg1=getResources().getStringArray(R.array.ylc_aboutus);
		listAboutus=new ArrayList<String>();
		for (String str : arg1) {
			listAboutus.add(str);
		}
		listView_aboutus=(ListView) findViewById(R.id.listView_aboutus);
		listView_aboutus.setAdapter(new MyAboutusAdapter(this));
	}
	class MyAboutusAdapter extends BaseAdapter{
		LayoutInflater layoutInflater;
		public MyAboutusAdapter(Context context){
			layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listAboutus.size();
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
			ViewHolderOfaboutus vh;
			if(convertView==null){
				vh=new ViewHolderOfaboutus();
				convertView=layoutInflater.inflate(R.layout.ylc_aboutus_item, parent, false);
				vh.tv1=(TextView) convertView.findViewById(R.id.textView_aboutus);
				convertView.setTag(vh);
			}
			else{
				vh=(ViewHolderOfaboutus) convertView.getTag();
			}
			if(position==2|position==3){
				vh.tv1.setText(listAboutus.get(position));
				vh.tv1.setMovementMethod(LinkMovementMethod.getInstance());
			}else{
				vh.tv1.setText(listAboutus.get(position));
			}
			return convertView;
		}
	}
	class ViewHolderOfaboutus{
		TextView tv1;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ylc__about_u, menu);
		return true;
	}

}
