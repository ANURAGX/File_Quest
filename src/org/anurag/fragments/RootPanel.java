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

package org.anurag.fragments;

import java.io.File;
import java.util.ArrayList;

import org.anurag.adapters.RootAdapter;
import org.anurag.adapters.SimpleRootAdapter;
import org.anurag.file.quest.Constants;
import org.anurag.file.quest.FileQuestHD;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.MasterPassword;
import org.anurag.file.quest.OpenFileDialog;
import org.anurag.file.quest.R;
import org.anurag.file.quest.RootManager;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.extra.libs.JazzyHelper;


public class RootPanel extends Fragment{
	
	private static ListView list;
	private LinearLayout empty;
	private static ArrayList<Item> adapter_list;
	private static LoadList load;
	private static RootManager manager;
	public static int ITEMS[];
	public static int counter;
	private static BaseAdapter adapter;
	private static Item item;
	private static JazzyHelper list_anim_helper;
	private static boolean isListHasLockedItem;
	
	public RootPanel() {
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
		list = (ListView)view.findViewById(R.id.list_view_hd);	
		empty = (LinearLayout) view.findViewById(R.id.empty);
		list.setSelector(R.drawable.list_selector_hd);
		list_anim_helper = new JazzyHelper(getActivity(), null);
		setAnim(list);
		if(load == null){
			load = new LoadList();
			load.execute();
		}
		

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				if(Constants.LONG_CLICK[1]){
					
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
				
				item = adapter_list.get(position);
				
				if(item.isLocked()){
					new MasterPassword(getActivity(), Constants.size.x*8/9, item, null,Constants.MODES.OPEN);
					return;
				}
				
				if(item.isDirectory()){
					//selecting a folder....
					manager.pushPath(item.getPath());
					FileQuestHD.notify_Title_Indicator(1, item.getName());
					load.execute();
				}else{
					//selecting a file....
					new OpenFileDialog(getActivity(), Uri.parse(item.getPath())
							, Constants.size.x*8/9);
				}
			}
		});		
		
		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				boolean sendBroadcast = false;
				if(ITEMS == null){
					ITEMS = new int[adapter_list.size()];
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
		
	}
	
	/**
	 * this function is invoked when password is verified for locked item....
	 */
	public static void open_locked_item(){
		if(item.isDirectory()){
			//selecting a folder....
			manager.pushPath(item.getPath());
			FileQuestHD.notify_Title_Indicator(1, item.getName());
			load.execute();
		}else{
			//selecting a file....
			new OpenFileDialog(Constants.ctx, Uri.parse(item.getPath())
					, Constants.size.x*8/9);
		}
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
	 *
	 * this class loads the list of files and folders in background thread
	 * and inflates the results in list view....
	 * @author anurag
	 *
	 */
	private class LoadList extends AsyncTask<Void , Void , Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			RootPanel.ITEMS = null;
			RootPanel.counter = 0;
		}		
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(adapter_list.size() != 0){
				empty.setVisibility(View.GONE);
				adapter = getListAdapter();
				list.setAdapter(adapter);
			}else
				empty.setVisibility(View.VISIBLE);
			load = new LoadList();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(manager == null)
				manager = new RootManager(getActivity());
			adapter_list = manager.getList();
			return null;
		}		
	}	
	
	/**
	 * moves one level back....
	 */
	public static void navigate_to_back(){
		manager.popTopPath();
		FileQuestHD.notify_Title_Indicator(1, manager.getCurrentDirectoryName());
		load.execute();
	}
	
	/**
	 * sets the list view selector as per selected theme
	 * dynamically by user....
	 */
	/*public static void setListSelector(){
		list.setSelector(Constants.SELECTOR_STYLE);
	}*/
	
	/**
	 * 
	 * @return true if current directory is /
	 */
	public static boolean isAtTopLevel(){
		if(manager.getCurrentDirectory().equalsIgnoreCase("/"))
			return true;
		return false;
	}

	/**
	 * reloads the current folder
	 * called when the folder icon has to be changed....
	 */
	public static void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		load.execute();
	}
	
	/**
	 * refreshes the list view....
	 */
	public static void refresh_list(){
		list.setAdapter(null);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		load.execute();
	}
	/**
	 * this function clears the selected items via long click from lits view....
	 */
	public static void clear_selected_items(){
		list.setAdapter(adapter);
		RootPanel.ITEMS = null;
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
			return new SimpleRootAdapter(getActivity(), adapter_list);
		case 2:
			return new RootAdapter(getActivity(), adapter_list);
			
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
		list.setOnScrollListener(list_anim_helper);
	}
	
	/**
	 * 
	 * @return the list of selected items by long press..... 
	 */
	public static ArrayList<Item> get_selected_items(){
		RootPanel.isListHasLockedItem = false;
		ArrayList<Item> lss = new ArrayList<>();
		int len = adapter_list.size();
		for(int i = 0 ; i < len ; ++i)
			if(RootPanel.ITEMS[i] == 1){
				Item im = adapter_list.get(i);
					if(im.isLocked()){
						RootPanel.isListHasLockedItem = true;
					//break;
				}	
				lss.add(im);	
			}	
		return lss;
	}
	
	/**
	 * 
	 * @return true if items selected via long click contains
	 * a locked item....
	 */
	public static boolean does_list_has_locked_item(){
		return isListHasLockedItem;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static Item get_current_working_dir(){
		return new Item(new File(manager.getCurrentDirectory()),
				null, null, null);
	}
	
	/**
	 * 
	 * @param path is puhsed to top of stacked indicating top dir....
	 */
	public static void push_path(String path){
		manager.nStack.push(path);
	}
}
