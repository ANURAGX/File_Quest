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

package org.anurag.adapters;


import java.util.concurrent.ConcurrentHashMap;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.FileGalleryAdapter;
import org.anurag.file.quest.FileQuestHD;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.OpenFileDialog;
import org.anurag.file.quest.R;
import org.anurag.file.quest.Utils;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class FileGallery extends Fragment implements OnClickListener{
	
	private static ListView ls;
	private static LinearLayout file_gallery;
	private static boolean is_gallery_opened;
	private static String current_Tile;
	public FileGallery() {
		// TODO Auto-generated constructor stub
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
		ls = (ListView) v.findViewById(R.id.customListView);
		file_gallery = (LinearLayout) v.findViewById(R.id.file_gallery_layout);
		Utils.setContext(null , getActivity());
		Utils.load();
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
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Item itm = (Item) ls.getAdapter().getItem(arg2);
				new OpenFileDialog(getActivity(), Uri.parse(itm.getPath()),
						Constants.size.x*8/9);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Utils.pause();
		ConcurrentHashMap<String , Item> lists = new ConcurrentHashMap<String, Item>();
		ConcurrentHashMap<String , String> keys = new ConcurrentHashMap<String, String>();
		
		switch (v.getId()){
		
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
		file_gallery.setVisibility(View.GONE);
		ls.setVisibility(View.VISIBLE);
		ls.setAdapter(new FileGalleryAdapter(getActivity(), lists, keys));
		is_gallery_opened = true;
		FileQuestHD.notify_Title_Indicator(0, current_Tile);
	}
	
	/**
	 * 
	 * @return true then file gallery is opened....
	 */
	public static boolean isGalleryOpened(){
		return is_gallery_opened;
	}
	
	public static void collapseGallery(){
		ls.setVisibility(View.GONE);
		file_gallery.setVisibility(View.VISIBLE);
		is_gallery_opened = false;
		current_Tile = "FILE GALLERY";
		FileQuestHD.notify_Title_Indicator(0, current_Tile);
	}
}
