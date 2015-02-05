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
 *                             anuraxsharma1512@gmail.com
 *
 */

package org.anurag.fragments;

import java.util.ArrayList;

import org.anurag.adapters.AppAdapter;
import org.anurag.adapters.SimpleAppAdapter;
import org.anurag.file.quest.AppBackup;
import org.anurag.file.quest.AppManager;
import org.anurag.file.quest.Constants;
import org.anurag.file.quest.R;
import org.anurag.file.quest.Utils;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.extra.libs.JazzyHelper;

public class AppStore extends Fragment{
	
	private static ListView ls;
	private ArrayList<ApplicationInfo> apps;
	private static LoadApps load;
	private AppManager manager;
	public static int counter;
	public static int[] ITEMS;
	private static BaseAdapter adapter;
	private static JazzyHelper list_anim_helper;
	
	public AppStore() {
		// TODO Auto-generated constructor stub
		counter = 0;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.list_view_hd, container , false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		ls = (ListView) view.findViewById(R.id.list_view_hd);
		ls.setSelector(R.drawable.list_selector_hd);
		list_anim_helper = new JazzyHelper(getActivity(), null);
		setAnim(ls);
		if(load == null){
			load = new LoadApps();
			load.execute();
		}			
		
		ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				if(Constants.LONG_CLICK[3]){
					
					if(ITEMS[position] != 1){
						ITEMS[position] = 1;
						arg1.setBackgroundColor(getResources().getColor(R.color.white_grey));
						++counter;
						getActivity().sendBroadcast(new Intent("update_action_bar_long_click"));
					}else if(ITEMS[position] == 1){
						ITEMS[position] = 0;
						arg1.setBackgroundColor(Color.WHITE);
						if(--counter == 0)
							getActivity().sendBroadcast(new Intent("inflate_normal_menu"));
						else
							getActivity().sendBroadcast(new Intent("update_action_bar_long_click"));
					}
					
					return;					
				}
				
				
				ArrayList<ApplicationInfo> infos = new ArrayList<ApplicationInfo>();
				infos.add(apps.get(position));
				new AppBackup(getActivity(), Constants.size.x*8/9, infos);
			}
		});
		
		ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				boolean sendBroadcast = false;
				if(ITEMS == null){
					ITEMS = new int[apps.size()];
					sendBroadcast = true;
				}
				
				if(ITEMS[arg2] != 1){
					arg1.setBackgroundColor(getResources().getColor(R.color.white_grey));
					ITEMS[arg2] = 1;
					++counter;
					getActivity().sendBroadcast(new Intent("update_action_bar_long_click"));
				}else if(ITEMS[arg2] == 1){
					ITEMS[arg2] = 0;
					arg1.setBackgroundColor(Color.WHITE);
					if(--counter == 0)
						getActivity().sendBroadcast(new Intent("inflate_normal_menu"));
					else
						getActivity().sendBroadcast(new Intent("update_action_bar_long_click"));
				}
				
				if(sendBroadcast)
					getActivity().sendBroadcast(new Intent("inflate_long_click_menu"));
				
				return true;
			}
		});
		
		//loading file gallery thread here after all less time consuming
		//thread are done....
		Utils.load();
	}
	
	/**
	 * this function sets transition effect for list view.... 
	 * @param list2
	 */
	private void setAnim(ListView list2) {
		// TODO Auto-generated method stub
		//JazzyHelper help = new JazzyHelper(getActivity(), null);
		list_anim_helper.setTransitionEffect(Constants.LIST_ANIM);
		list2.setOnScrollListener(list_anim_helper);
	}
	
	/**
	 * sets the list view selector as per selected theme
	 * dynamically by user....
	 */
	/*public static void setListSelector(){
		ls.setSelector(Constants.SELECTOR_STYLE);
	}*/
	
	/**
	 * 
	 * @author anurag
	 *
	 */
	private class LoadApps extends AsyncTask<Void, Void , Void>{

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			adapter = getListAdapter();
			ls.setAdapter(adapter);
			load = new LoadApps();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(manager == null)
				manager = new AppManager(getActivity());
			apps = manager.get_downloaded_apps();
			return null;
		}
		
	}
	
	/**
	 * refreshes the list view....
	 */
	public static void refresh_list(){
		ls.setAdapter(null);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		load.execute();
	}
	
	/**
	 * this function clears the selected items via long click from list view....
	 */
	public static void clear_selected_items(){
		
		int last = ls.getLastVisiblePosition();
		int first = ls.getFirstVisiblePosition();
		for(int i = first; i <= last ; ++i){
			try{
				ls.getChildAt(i - first).setBackgroundResource(R.drawable.list_selector_hd);
			}catch(RuntimeException e){}
		}
		
		AppStore.ITEMS = null;
		counter = 0;
	}
	
	/**
	 * 
	 * @return the adapter as per the settings....
	 * will return detailed list adapter,simple list adapter,simple grid adapter or detailed
	 * grid adapter....
	 */
	private BaseAdapter getListAdapter(){
		switch(Constants.LIST_TYPE){
		case 1:
			return new SimpleAppAdapter(getActivity(), apps);
		case 2:
			return new AppAdapter(getActivity(), apps);
			
		}
		return null; 
	}
	
	/**
	 * reloads the adapter when list type is changed....
	 */
	public static void resetAdapter(){
		load.execute();
	}	
	
	/**
	 * this function is called when list view animation has to be changed....
	 */
	public static void change_list_anim(){
		list_anim_helper.setTransitionEffect(Constants.LIST_ANIM);
		ls.setOnScrollListener(list_anim_helper);
	}
}
