/**
 * Copyright(c) 2013 ANURAG 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * anurag.dev1512@gmail.com
 *
 */
package org.anurag.file.quest;

import java.util.ArrayList;

import java.util.List;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class LongClickDialog extends ListActivity{

	private Intent intent;
	private ListView lv;
	private TextView tv;
	private boolean status = false;
	private String statusStr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		statusStr = intent.getData().toString();
		if(statusStr.equalsIgnoreCase("Select Action"))
			status = true;
		WindowManager.LayoutParams params = this.getWindow().getAttributes();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			WindowManager w = getWindowManager();
			Point size = new Point();
			w.getDefaultDisplay().getSize(size);
			//params.alpha = 0.8f;
			/*if(status)
				params.height = size.y*3/5;
			else
				params.height = size.y*2/3;*/
			params.width = size.x*4/5;
		}
		setContentView(R.layout.long_click_dialog);
		ArrayList<String> l = new ArrayList<String>();
		l.add("");l.add("");l.add("");l.add("");l.add("");l.add("");l.add("");l.add("");l.add("");
		ArrayList<String> list = new ArrayList<String>();
		list.add("");list.add("");list.add("");list.add("");list.add("");list.add("");list.add("");
		tv = (TextView)findViewById(R.id.longClickDialogTitle);
		tv.setText(statusStr);
		if(status)
			setListAdapter(new DialogAdapter(getApplicationContext(), R.layout.row_list_2, list));
		else	
			setListAdapter(new DialogAdapter(getApplicationContext(), R.layout.row_list_2 , l));
		lv = getListView();
		lv.setSelector(R.drawable.action_item_btn);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long id) {
				if(status){////////////////////////////////////Check Code here
					intent = new Intent();
					intent.setData(Uri.parse(""+position));
					setResult(position , intent); // intent is passed as it holds position of application info from application list
					finish();
				}else{///////////////////////////check Code here
					setResult(position);
					finish();
				}
			}
		});
	}
	
	
	private static class FileHolder{
		ImageView Icon;
		TextView Name;
	}
	private class DialogAdapter extends ArrayAdapter<String>{
		public DialogAdapter(Context context, int textViewResourceId ,ArrayList<String> objects) {
			super(context, textViewResourceId, objects);
			
    	}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final FileHolder ho; 
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_list_2, parent , false);
				ho = new FileHolder();
				ho.Icon = (ImageView)convertView.findViewById(R.id.iconImage2);
				ho.Name = (TextView)convertView.findViewById(R.id.directoryName2);
				convertView.setTag(ho);
			}else
				ho = (FileHolder)convertView.getTag();
			
			if(status){
				if(position == 0){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_open);
					ho.Name.setText("Launch App");
				}else if(position == 1){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_uninstall);
					ho.Name.setText("Uninstall");
				}else if(position == 2){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_backupall);
					ho.Name.setText("Take Backup");
				}else if(position == 3){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_delete);
					ho.Name.setText("Delete Earlier Backups");
				}else if(position == 4){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_zip_it);
					ho.Name.setText("Create Flashable Zip");
				}else if(position == 5){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_bluetooth);
					ho.Name.setText("Send App");
				}else if(position == 6){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_stats);
					ho.Name.setText("App Properties");
				}
			}
			else{
				if(position == 0){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_open);
					ho.Name.setText("Open");
				}else if(position == 1){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_copy);
					ho.Name.setText("Copy");
				}else if(position == 2){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_cut);
					ho.Name.setText("Cut");
				}else if(position == 4){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_zip_it);
					ho.Name.setText("Create Zip");
				}else if(position == 5){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_delete);
					ho.Name.setText("Delete");
				}else if(position == 6){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_rename);
					ho.Name.setText("Rename");
				}else if(position == 7){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_bluetooth);
					ho.Name.setText("Send");
				}else if(position == 8){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_stats);
					ho.Name.setText("Properties");
				}else if(position == 3){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_paste);
					ho.Name.setText("Paste");
				}
			}
			return convertView;
		}
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			setResult(-1);
	        finish();
		}
	return false;
	}
	
}
