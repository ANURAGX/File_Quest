/**
 * Copyright(c) 2014 DRAWNZER.ORG PROJECTS -> ANURAG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *      
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *                             
 *                             anurag.dev1512@gmail.com
 *
 */


package org.anurag.file.quest;

import java.io.File;
import java.util.ArrayList;

import org.ultimate.menuItems.GetHomeDirectory;
import org.ultimate.menuItems.Info;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Settings extends Activity {

	ExpandableListView folderLs;
	ExpandableListView uiLs;
	ArrayList<String> parentAnimList;
	SettingsAdapter uiAdapter;
	ArrayList<String> parentFolderList;
	SettingsAdapter folderAdapter;
	ListView ls;
	ArrayList<String> abtString;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_ui);
		
		ls = (ListView)findViewById(R.id.abtls);
		uiLs = (ExpandableListView)findViewById(R.id.intUI);
		folderLs = (ExpandableListView)findViewById(R.id.folderls);
		parentAnimList = new ArrayList<String>();
		parentFolderList = new ArrayList<String>();
		abtString = new ArrayList<String>();
		abtString.add(getString(R.string.abtme));
		
		
		parentAnimList.add(getString(R.string.animation));
		parentAnimList.add(getString(R.string.appearance));
		uiAdapter = new SettingsAdapter(Settings.this, parentAnimList,0);
		uiLs.setAdapter(uiAdapter);
		
		parentFolderList.add(getString(R.string.startup));
		parentFolderList.add(getString(R.string.locker));
		parentFolderList.add(getString(R.string.folderopt));
		parentFolderList.add(getString(R.string.sethomdir));
		parentFolderList.add(getString(R.string.cleargesturedata));
		folderAdapter = new SettingsAdapter(Settings.this, parentFolderList,1);
		folderLs.setAdapter(folderAdapter);
		
		folderLs.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(arg2==3)
					new GetHomeDirectory(Settings.this, FileQuest.size.x*8/9 , FileQuest.preferences);
				else if(arg2==4){
					try{
						new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/File Quest/.gesture").delete();
					}catch(Exception e){
						
					}
					Toast.makeText(Settings.this, getString(R.string.gesturedatacleared),Toast.LENGTH_SHORT).show();
				}
				return false;
			}
		});
		
		
		
		ls.setAdapter(new abtAdapter());
		ls.setOnItemLongClickListener(null);
		ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				// TODO Auto-generated method stub
				new Info(Settings.this, FileQuest.size.x*8/9);
			}
		});
	}
	
	
	class abtAdapter extends BaseAdapter{		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return abtString.size();
		}
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
		class abt{
			ImageView img;
			TextView txt;
		}		
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			abt a = new abt();
			if(arg1==null){
				LayoutInflater inf = (LayoutInflater) Settings.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
				arg1 = inf.inflate(R.layout.row_list_expandable, arg2 , false);
				a.img = (ImageView)arg1.findViewById(R.id.iconImage2);
				a.txt = (TextView)arg1.findViewById(R.id.directoryName2);
				arg1.setTag(a);
			}else
				a = (abt) arg1.getTag();
			a.img.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_info));
			a.txt.setText(abtString.get(arg0));
			return arg1;
		}		
	}
}
