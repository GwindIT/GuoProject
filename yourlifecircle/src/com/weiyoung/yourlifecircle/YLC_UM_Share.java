package com.weiyoung.yourlifecircle;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class YLC_UM_Share extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ylc_um_share);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ylc__um__share, menu);
		return true;
	}

}
