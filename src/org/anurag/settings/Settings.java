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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.anurag.file.quest.FileQuest;
import org.anurag.file.quest.R;
import org.ultimate.menuItems.Info;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity {

	SettingsInterFaceAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	ListView abtLs;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_ui);

		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.intUI);

		// preparing list data
		prepareListData();

		listAdapter = new SettingsInterFaceAdapter(this, listDataHeader, listDataChild);

		// setting list adapter
		expListView.setAdapter(listAdapter);

		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// Toast.makeText(getApplicationContext(),
				// "Group Clicked " + listDataHeader.get(groupPosition),
				// Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				Toast.makeText(getApplicationContext(),
						listDataHeader.get(groupPosition) + " Expanded",
						Toast.LENGTH_SHORT).show();
			}
		});

		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				Toast.makeText(getApplicationContext(),
						listDataHeader.get(groupPosition) + " Collapsed",
						Toast.LENGTH_SHORT).show();

			}
		});

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(
						getApplicationContext(),
						listDataHeader.get(groupPosition)
								+ " : "
								+ listDataChild.get(
										listDataHeader.get(groupPosition)).get(
										childPosition), Toast.LENGTH_SHORT)
						.show();
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
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add(getString(R.string.animation));
		listDataHeader.add(getString(R.string.appearance));
		
		// Adding child data
		List<String> animChild = new ArrayList<String>();
		animChild.add(getString(R.string.panelanim));
		animChild.add(getString(R.string.listanim));

		List<String> appearChild = new ArrayList<String>();
		appearChild.add(getString(R.string.adjusttrans));
		appearChild.add(getString(R.string.setfoldericn));
		

		
		listDataChild.put(listDataHeader.get(0), animChild); // Header, Child data
		listDataChild.put(listDataHeader.get(1), appearChild);
		
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
