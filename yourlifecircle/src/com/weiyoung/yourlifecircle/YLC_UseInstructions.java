package com.weiyoung.yourlifecircle;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class YLC_UseInstructions extends Activity {
	private List<String> listP,listE;
	private ListView listView_intruction;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ylc_use_instructions);
		initData();
	}

	public void initData() {
		// TODO Auto-generated method stub
		String[]arg1=getResources().getStringArray(R.array.ylc_userintructions_problem);
		String[]arg2=getResources().getStringArray(R.array.ylc_userintructions_explain);
		listP=new ArrayList<String>();
		listE=new ArrayList<String>();
		for(int i=0;i<arg1.length;i++){
			listP.add(arg1[i]);
			listE.add(arg2[i]);
		}
		listView_intruction=(ListView) findViewById(R.id.listView_intruction);
		listView_intruction.setAdapter(new MyIntructionAdapter(this));
	}
	class MyIntructionAdapter extends BaseAdapter{
		LayoutInflater layoutInflater;
		public MyIntructionAdapter(Context context) {
			// TODO Auto-generated constructor stub
			layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listP.size();
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
			ViewHolderOfIntruction vh;
			if(convertView==null){
				vh=new ViewHolderOfIntruction();
				convertView=layoutInflater.inflate(R.layout.ylc_intructions_item, parent, false);
				vh.tv1=(TextView) convertView.findViewById(R.id.textView_intruction_p);
				vh.tv2=(TextView) convertView.findViewById(R.id.textView_intruction_e);
				convertView.setTag(vh);
			}else{
				vh=(ViewHolderOfIntruction) convertView.getTag();
			}
			vh.tv1.setText(listP.get(position));
			vh.tv2.setText(listE.get(position));
			return convertView;
		}
		
	}
	class ViewHolderOfIntruction{
		TextView tv1;
		TextView tv2;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ylc__use_instructions, menu);
		return true;
	}

}
