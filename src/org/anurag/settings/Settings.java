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


package org.anurag.settings;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.anurag.file.quest.FileQuest;
import org.anurag.file.quest.R;
import org.ultimate.menuItems.GetHomeDirectory;
import org.ultimate.menuItems.Info;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity {

	SettingsInterFaceAdapter listAdapter;
	ExpandableListView expListView1,expListView2;
	List<String> listDataHeader1,listDataHeader2;
	HashMap<String, List<String>> listDataChild1,listDataChild2;
	ListView abtLs;
	SharedPreferences settingsPrefs;
	SharedPreferences.Editor edit;
	SettingsFolderOptAdapter listAdapter2;
	
	public static ImageView applied;
	public static boolean settingsChanged;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingsChanged = false;
		setContentView(R.layout.settings_ui);
		settingsPrefs = getSharedPreferences("MY_APP_SETTINGS", 0);
		edit = settingsPrefs.edit();
		// get the listview
		expListView1 = (ExpandableListView) findViewById(R.id.intUI);
		expListView2 = (ExpandableListView) findViewById(R.id.folderls);
		// preparing list data
		prepareListData();
		listAdapter = new SettingsInterFaceAdapter(this, listDataHeader1, listDataChild1);

		// setting list adapter
		expListView1.setAdapter(listAdapter);

		prepareListData2();
		listAdapter2 = new SettingsFolderOptAdapter(this, listDataHeader2, listDataChild2);
		expListView2.setAdapter(listAdapter2);

		expListView1.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		

		// Listview on child click listener
		expListView1.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				if(groupPosition== 0 && childPosition == 0)
					new PagerAnimDialog(Settings.this, FileQuest.size.x*8/9, edit);
				else if(groupPosition == 0 && childPosition == 1)
					new ListAnimDialog(Settings.this, FileQuest.size.x*8/9, edit);
				return false;
			}
		});
		
		
		expListView2.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(arg2 == 3)
					new GetHomeDirectory(Settings.this, FileQuest.size.x*8/9, settingsPrefs);
				else if(arg2 == 4){
					try{
						new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/File Quest/.gesture").delete();
					}catch(Exception e){
						
					}
					Toast.makeText(Settings.this, getString(R.string.gesturedatacleared),Toast.LENGTH_SHORT).show();
				}
				return false;
			}
		});
		
		expListView2.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int grp,
					int child, long arg4) {
				// TODO Auto-generated method stub
				if(grp == 2 && child == 0){
					if(FileQuest.SHOW_HIDDEN_FOLDERS){
						FileQuest.SHOW_HIDDEN_FOLDERS = false;
						applied.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_disabled));
					}	
					else{
						FileQuest.SHOW_HIDDEN_FOLDERS = true;
						applied.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_apply));
					}	
					edit.putBoolean("SHOW_HIDDEN_FOLDERS",FileQuest.SHOW_HIDDEN_FOLDERS);
					edit.commit();
					Toast.makeText(Settings.this, R.string.settingsapplied, Toast.LENGTH_SHORT).show();
				}
				else if(grp == 0 && child == 0)
					new StartUpPanelDialog(Settings.this, FileQuest.size.x*8/9 , edit);
				
				return false;
			}
		});
		
		abtLs = (ListView)findViewById(R.id.abtls);
		abtLs.setAdapter(new abtAdapter());
		abtLs.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				new Info(Settings.this, FileQuest.size.x*8/9);
			}
		});
		abtLs.setOnItemLongClickListener(null);
	}

	/*
	 * Preparing the list data
	 */
	private void prepareListData() {
		listDataHeader1 = new ArrayList<String>();
		listDataChild1 = new HashMap<String, List<String>>();
		// Adding child data
		listDataHeader1.add(getString(R.string.animation));
		listDataHeader1.add(getString(R.string.appearance));
		// Adding child data
		List<String> animChild = new ArrayList<String>();
		animChild.add(getString(R.string.panelanim));
		animChild.add(getString(R.string.listanim));
		List<String> appearChild = new ArrayList<String>();
		appearChild.add(getString(R.string.adjusttrans));
		appearChild.add(getString(R.string.setfoldericn));		
		listDataChild1.put(listDataHeader1.get(0), animChild); // Header, Child data
		listDataChild1.put(listDataHeader1.get(1), appearChild);
		
	}
	
	/*
	 * Preparing the list data
	 */
	private void prepareListData2() {
		listDataHeader2 = new ArrayList<String>();
		listDataChild2 = new HashMap<String, List<String>>();
		// Adding child data
		listDataHeader2.add(getString(R.string.startup));
		listDataHeader2.add(getString(R.string.locker));
		listDataHeader2.add(getString(R.string.folderopt));
		listDataHeader2.add(getString(R.string.sethomdir));
		listDataHeader2.add(getString(R.string.cleargesturedata));
		// Adding child data
		List<String> start = new ArrayList<String>();
		start.add(getString(R.string.setstartpanel));
		start.add(getString(R.string.setstartupdir));
		
		List<String> lock = new ArrayList<String>();
		String[] arr = getResources().getStringArray(R.array.itemlockerlist);
		lock.add(arr[1]);
		lock.add(arr[2]);
		lock.add(arr[3]);
		List<String> opt = new ArrayList<String>();
		opt.add(getString(R.string.showhidden));
		opt.add(getString(R.string.sort));
		
		listDataChild2.put(listDataHeader2.get(0), start); // Header, Child data
		listDataChild2.put(listDataHeader2.get(1), lock);	
		listDataChild2.put(listDataHeader2.get(2), opt);
		listDataChild2.put(listDataHeader2.get(3), new ArrayList<String>());
		listDataChild2.put(listDataHeader2.get(4), new ArrayList<String>());
	}
	
	class abtAdapter extends BaseAdapter{		
				@Override
				public int getCount() {
					// TODO Auto-generated method stub
					return 1;
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
					a.txt.setText(getString(R.string.abtme));
					return arg1;
				}
						
		}
}
