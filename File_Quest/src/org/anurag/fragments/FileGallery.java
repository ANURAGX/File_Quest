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
import java.util.concurrent.ConcurrentHashMap;

import org.anurag.adapters.FileGalleryAdapter;
import org.anurag.adapters.FileGallerySimpleAdapter;
import org.anurag.dialogs.OpenFileDialog;
import org.anurag.file.quest.Constants;
import org.anurag.file.quest.FileQuestHD;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.R;
import org.anurag.file.quest.Utils;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.extra.libs.JazzyHelper;

/**
 * file gallery class that represents the metro like UI.
 * @author anurag
 *
 */
public class FileGallery extends Fragment implements OnClickListener{
	
	//list view 
	private static ListView ls;
	
	//metro like ui layout
	private static LinearLayout file_gallery;
	
	//true then a category from file gallery is selected
	private static boolean is_gallery_opened;
	
	//current title of file gallery
	private static String current_Tile;
	
	//async task instance to load the list of files 
	private static Loader loader;
	
	//adapter for list view
	private static BaseAdapter adpt;
	
	//list of files selected
	public static ConcurrentHashMap<String , Item> lists;
	
	//keys for list of files
	public static ConcurrentHashMap<String , String> keys;
	
	//no. of selected files via long press
	public static int counter;
	
	//ITEMS[i]=1 if lists[i] is selected
	public static int[] ITEMS;
	
	//enpty list view
	private static LinearLayout empty;
	
	
	private int id;
	
	//animation helper for list view
	private static JazzyHelper list_anim_helper;
	
	//currently selected item from list vier
	private static Item item;
	
	//true then selected list of files contains one or more locked files
	private static boolean isListHasLockedItem;
	
	//folder count from selected list
	public static int folder_count;
	
	//file count from selected list
	public static int file_count;
	
	
	public FileGallery() {
		// TODO Auto-generated constructor stub
		counter = 0;
		file_count = 0;
		folder_count = 0;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.custom_list_view, container , false);
	}

	@Override
	public void onViewCreated(View v, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(v, savedInstanceState);
		empty = (LinearLayout) v.findViewById(R.id.empty);
		ls = (ListView) v.findViewById(R.id.customListView);
		ls.setSelector(R.drawable.list_selector_hd);
		file_gallery = (LinearLayout) v.findViewById(R.id.file_gallery_layout);
		Utils.setContext(null , getActivity());
		
		//loading file list at last....
		//this allows other threads of other panel to load first as 
		//they need little time and this thread can take long time and blocking
		//other threads....
		
		//Utils.load();
		
		Utils.setView(v);
		is_gallery_opened = false;

		LinearLayout fav = (LinearLayout) v.findViewById(R.id.fav);
		LinearLayout music = (LinearLayout) v.findViewById(R.id.music);
		LinearLayout app = (LinearLayout) v.findViewById(R.id.apps);
		LinearLayout docs = (LinearLayout) v.findViewById(R.id.docs);
		LinearLayout photo = (LinearLayout) v.findViewById(R.id.photos);
		LinearLayout vids = (LinearLayout) v.findViewById(R.id.videos);
		LinearLayout zips = (LinearLayout) v.findViewById(R.id.zips);
		LinearLayout misc = (LinearLayout) v.findViewById(R.id.misc);
		
		music.setOnClickListener(this);
		fav.setOnClickListener(this);
		app.setOnClickListener(this);
		docs.setOnClickListener(this);
		photo.setOnClickListener(this);
		vids.setOnClickListener(this);
		zips.setOnClickListener(this);
		misc.setOnClickListener(this);
		
		ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				item = (Item) ls.getAdapter().getItem(position);
				
				if(Constants.LONG_CLICK[0]){
					
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
				open_locked_item();				
			}
		});
		
		ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				boolean sendBroadcast = false;
				item = (Item) ls.getAdapter().getItem(arg2);
				
				if(ITEMS == null){
					ITEMS = new int[ls.getAdapter().getCount()];
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
		});
		
		list_anim_helper = new JazzyHelper(getActivity(), null);
		setAnim(ls);
	}

	/**
	 * this function is invoked when password is verified for locked item....
	 */
	public static void open_locked_item(){
		if(item.exists()){
			if(item.isDirectory()){
				//selecting a folder....
				
				//folder can be read
				if(item.canRead()){
					SdCardPanel.manager.pushPath(item.getPath());
					FileQuestHD.notify_Title_Indicator(2, item.getName());
					SdCardPanel.load.execute();
				}else{//can't read folder
					RootPanel.manager.pushPath(item.getPath());
					FileQuestHD.notify_Title_Indicator(1, item.getName());
					RootPanel.load.execute();
				}
			}else{
				//selecting a file....
				new OpenFileDialog(Constants.ctx, Uri.parse(item.getPath()));
			}
		}else
			Toast.makeText(Constants.ctx, R.string.not_exists, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Utils.pause();
		lists = new ConcurrentHashMap<String, Item>();
		keys = new ConcurrentHashMap<String, String>();
		id = v.getId();		
		
		if(loader == null)
			loader = new Loader();
		loader.execute();
	}
	
	private class Loader extends AsyncTask<Void , Void , Void>{
		public Loader() {
			// TODO Auto-generated constructor stub
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			file_gallery.setVisibility(View.GONE);
			if(lists.size() != 0){
				ls.setVisibility(View.VISIBLE);
				adpt = getListAdapter();
				ls.setAdapter(adpt);
			}else{
				empty.setVisibility(View.VISIBLE); 
			}
			is_gallery_opened = true;
			FileQuestHD.notify_Title_Indicator(0, current_Tile);
			loader = new Loader();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			switch (id){
			
			case R.id.fav:
				lists = Utils.fav;
				keys = Utils.favKey;
				current_Tile = "Favorite";
				break;
				
			case R.id.music:
				lists = Utils.music;
				keys = Utils.musicKey;
				current_Tile = "Music";
				break;
				
			case R.id.apps:
				lists = Utils.apps;
				keys = Utils.appKey;
				current_Tile = "Apps";
				break;
				
			case R.id.docs:
				lists = Utils.doc;
				keys = Utils.docKey;
				current_Tile = "Docs";
				break;
				
			case R.id.photos:
				lists = Utils.img;
				keys = Utils.imgKey;
				current_Tile = "Images";
				break;
				
			case R.id.videos:
				lists = Utils.vids;
				keys = Utils.videoKey;
				current_Tile = "Videos";
				break;
				
			case R.id.zips:
				lists = Utils.zip;
				keys = Utils.zipKey;
				current_Tile = "Archives";
				break;
				
			case R.id.misc:
				lists = Utils.mis;
				keys = Utils.misKey;
				current_Tile = "Unknown";
				break;
			}

			return null;
		}
		
	}
	
	/**
	 * 
	 * @return true then file gallery is opened....
	 */
	public static boolean isGalleryOpened(){
		return is_gallery_opened;
	}
	
	/**
	 * sets the list view selector as per selected theme
	 * dynamically by user....
	 */
	/*public static void setListSelector(){
		ls.setSelector(Constants.SELECTOR_STYLE);
	}
	*/
	
	public static void collapseGallery(){
		ls.setVisibility(View.GONE);
		empty.setVisibility(View.GONE);
		file_gallery.setVisibility(View.VISIBLE);
		is_gallery_opened = false;
		current_Tile = "FILE GALLERY";
		FileQuestHD.notify_Title_Indicator(0, current_Tile);
	}
	
	/**
	 * refreshes the list view....
	 */
	public static void refresh_list(){
		if(is_gallery_opened)
			return;
		ls.setAdapter(null);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ls.setAdapter(adpt);
	}
	
	/**
	 * this function sets transition effect for list view.... 
	 * @param list2
	 */
	private void setAnim(ListView list2) {
		// TODO Auto-generated method stub
		list_anim_helper.setTransitionEffect(Constants.LIST_ANIM);
		list2.setOnScrollListener(list_anim_helper);
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
		
		FileGallery.ITEMS = null;
		counter = 0;
		if(lists.size() == 0){
			ls.setVisibility(View.GONE);
			empty.setVisibility(View.VISIBLE);
		}
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
			return new FileGallerySimpleAdapter(getActivity(), lists, keys);
		case 2:
			return new FileGalleryAdapter(getActivity(), lists, keys);
			
		}
		return null; 
	}

	/**
	 * reloads the adapter when list type is changed....
	 */
	public static void resetAdapter(){
		if(!is_gallery_opened || lists.size() == 0)
			return;
		loader.execute();
	}
	
	/**
	 * this function is called when list view animation has to be changed....
	 */
	public static void change_list_anim(){
		list_anim_helper.setTransitionEffect(Constants.LIST_ANIM);
		ls.setOnScrollListener(list_anim_helper);
	}
	
	/**
	 * 
	 * @return the list of selected items by long press..... 
	 */
	public static ArrayList<Item> get_selected_items(){
		FileGallery.isListHasLockedItem = false;
		ArrayList<Item> lss = new ArrayList<>();
		int len = lists.size();
		for(int i = 0 ; i < len ; ++i)
			if(FileGallery.ITEMS[i] == 1){
				Item im = lists.get(keys.get(""+i));
					if(im.isLocked()){
						FileGallery.isListHasLockedItem = true;
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
	 * @return currently selected item
	 */
	public static Item getItem(){
		return item;
	}
}
