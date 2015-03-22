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

import java.io.File;
import java.util.ArrayList;

import org.anurag.adapters.SDAdapter;
import org.anurag.adapters.SimpleSDAdapter;
import org.anurag.dialogs.OpenFileDialog;
import org.anurag.file.quest.Constants;
import org.anurag.file.quest.FileQuestHD;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.MasterPassword;
import org.anurag.file.quest.R;
import org.anurag.file.quest.SDManager;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.extra.libs.JazzyHelper;

/**
 * this fragment represents the sdcard panel in pager
 * @author anurag
 *
 */
public class SdCardPanel extends Fragment implements OnItemClickListener , OnItemLongClickListener{
	
	//grid view
	private static GridView grid;
	
	//list view
	private static ListView list;
	
	//empty liust view
	private LinearLayout empty;
	
	//list of files in list view
	private static ArrayList<Item> adapter_list;
	
	//async task to load list of files in background
	static LoadList load;
	
	//helper class to sort,load icons of files
	public static SDManager manager;
	
	//no. of selected files in long press
	public static int counter;
	
	//ITEMS[i]=1 if lists[i] is selected
	public static int[] ITEMS;
	
	//adapter for list view
	private static BaseAdapter adapter;
	
	//selected item	
	private static Item item;
	
	//animation helper for list view
	private static JazzyHelper list_anim_helper;
	
	//true then selected list of files contains one or more locked item
	private static boolean isListHasLockedItem;
	
	//no. of selected folders in selected files
	public static int folder_count;
	
	//no. of selected files in selected files
	public static int file_count;
	
		
	public SdCardPanel() {
		// TODO Auto-generated constructor stub
		counter = 0;
		file_count = 0;
		folder_count = 0;
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
		list.setBackgroundColor(Color.WHITE);
		grid = (GridView) view.findViewById(R.id.grid_view_hd);
		empty = (LinearLayout) view.findViewById(R.id.empty);
		empty.setBackgroundColor(Color.WHITE);
		list.setSelector(R.drawable.list_selector_hd);
		list_anim_helper = new JazzyHelper(getActivity(), null);
		setAnim(list);
		if(load == null){
			load = new LoadList();
			load.execute();
		}	
		
		list.setOnItemClickListener(this);
		grid.setOnItemClickListener(this);
		list.setOnItemLongClickListener(this);
		grid.setOnItemLongClickListener(this);
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
	 * @author anurag
	 *
	 */
	class LoadList extends AsyncTask<Void , Void , Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			SdCardPanel.ITEMS = null;
			SdCardPanel.counter = 0;
			//SdCardPanel.isListHasLockedItem = false;			
		}		
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(adapter_list.size() != 0){
				empty.setVisibility(View.GONE);
				adapter = getListAdapter();
				list.setAdapter(adapter);
			}else{
				empty.setVisibility(View.VISIBLE);
			}
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
				manager = new SDManager(getActivity());
			adapter_list = manager.getList();
			return null;
		}		
	}	
	
	/**
	 * moves one level back....
	 */
	public static void navigate_to_back(){
		manager.popTopPath();
		FileQuestHD.notify_Title_Indicator(2, manager.getCurrentDirectoryName());
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
		if(manager.getCurrentDirectory().equalsIgnoreCase(Constants.PATH))
			return true;
		return false;
	}
	

	/**
	 * reloads the current folder
	 * called when the folder icon has to be changed....
	 */
	public static void notifyDataSetChanged(){
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
	 * this function clears the selected items via long click from list view....
	 */
	public static void clear_selected_items(){
		//list.setAdapter(adapter);
		int last = list.getLastVisiblePosition();
		int first = list.getFirstVisiblePosition();
		for(int i = first; i <= last ; ++i){
			try{
				list.getChildAt(i - first).setBackgroundResource(R.drawable.list_selector_hd);
			}catch(RuntimeException e){}
		}
		
		SdCardPanel.ITEMS = null;
		SdCardPanel.counter = 0;
		SdCardPanel.folder_count = 0 ; 
		SdCardPanel.file_count = 0;
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
			return new SimpleSDAdapter(getActivity(), adapter_list , manager.isArchiveOpened());
		case 2:
			return new SDAdapter(getActivity(), adapter_list , manager.isArchiveOpened());			
		}
		return null; 
	}
	
	/**
	 * reloads the adapter when list type is changed....
	 */
	public static void resetAdapter(){
		load.execute();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		boolean sendBroadcast = false;
		item = adapter_list.get(arg2);
		if(ITEMS == null){
			ITEMS = new int[adapter_list.size()];
			sendBroadcast = true;
		}
		
		
		
		if(ITEMS[arg2] != 1){
			arg1.setBackgroundColor(getResources().getColor(R.color.white_grey));
			ITEMS[arg2] = 1;
			++counter;
			
			//updating folder and file count while long press is active....
			if(item.isDirectory())
				folder_count++;
			else
				file_count++;
			
			getActivity().sendBroadcast(new Intent("update_action_bar_long_click"));
		}else if(ITEMS[arg2] == 1){
			ITEMS[arg2] = 0;
			arg1.setBackgroundColor(Color.WHITE);
			
			//updating folder and file count while long press is active....
			if(item.isDirectory())
				folder_count--;
			else
				file_count--;
			
			if(--counter == 0)
				getActivity().sendBroadcast(new Intent("inflate_normal_menu"));
			else
				getActivity().sendBroadcast(new Intent("update_action_bar_long_click"));
		}
		
		if(sendBroadcast)
			getActivity().sendBroadcast(new Intent("inflate_long_click_menu"));
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub

		item = adapter_list.get(position);
		
		if(Constants.LONG_CLICK[2]){
					
			if(ITEMS[position] != 1){
				ITEMS[position] = 1;
				arg1.setBackgroundColor(getResources().getColor(R.color.white_grey));
				++counter;
				
				//updating folder and file count while long press is active....
				if(item.isDirectory())
					folder_count++;
				else
					file_count++;
				
				getActivity().sendBroadcast(new Intent("update_action_bar_long_click"));
			}else if(ITEMS[position] == 1){
				ITEMS[position] = 0;
				arg1.setBackgroundColor(Color.WHITE);
				
				//updating folder and file count while long press is active....
				if(item.isDirectory())
					folder_count--;
				else
					file_count--;
				
				if(--counter == 0)
					getActivity().sendBroadcast(new Intent("inflate_normal_menu"));
				else
					getActivity().sendBroadcast(new Intent("update_action_bar_long_click"));
			}
			return;					
		}
		
		if(item.isLocked()){
			new MasterPassword(getActivity(), item, null, Constants.MODES.OPEN,null);
			return;
		}
		
		open_locked_item();
	}
	

	/**
	 * this function is invoked when password is verified for locked item....
	 */
	public static void open_locked_item(){
		if(item.exists()){
			if(item.isDirectory()){
				//selecting a folder....
				SdCardPanel.manager.pushPath(item.getPath());
				FileQuestHD.notify_Title_Indicator(2, manager.getCurrentDirectoryName());
				load.execute();
			}else{
				//selecting a file....
				new OpenFileDialog(Constants.ctx, Uri.parse(item.getPath()));
			}
		}else
			Toast.makeText(Constants.ctx, R.string.not_exists, Toast.LENGTH_SHORT).show();
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
		SdCardPanel.isListHasLockedItem = false;
		ArrayList<Item> lss = new ArrayList<>();
		int len = adapter_list.size();
		for(int i = 0 ; i < len ; ++i)
			if(SdCardPanel.ITEMS[i] == 1){
				Item im = adapter_list.get(i);
				if(im.isLocked()){
					SdCardPanel.isListHasLockedItem = true;
					//break;
				}	
				lss.add(adapter_list.get(i));
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
		manager.pushPath(path);
	}	
	
	/**
	 * 
	 * @param type
	 */
	public static void loadArchive(int type){
		switch(type){
		case 1:
			manager.setInZip(true);
			break;
		case 2:
			manager.setInRar(true);
			break;
			
		case 3:
			//for tar archives....
			break;
		}
		manager.pushPath(item.getPath());
		load.execute();
	}
}
