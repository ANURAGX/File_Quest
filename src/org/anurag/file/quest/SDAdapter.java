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

import java.util.ArrayList;
import java.util.HashMap;

import org.anurag.adapters.SdCardPanel;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class SDAdapter extends BaseAdapter{
	private HashMap<String, Drawable> apkList;
	private HashMap<String, Bitmap> imgList;
	private HashMap<String, Bitmap> musicList;
	Bitmap image;
	Holder h;
	public static boolean MULTI_SELECT;
	public static boolean[] thumbselection;
	public static long C;
	Item item;
	Context ctx;
	ArrayList<Item> list; 
	LayoutInflater inflater;
	static ArrayList<Item> MULTI_FILES;
	public SDAdapter(Context context,ArrayList<Item> object) {
		// TODO Auto-generated constructor stub
		ctx = context;
		MULTI_FILES = new ArrayList<Item>();
		list = object;
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imgList = new HashMap<String , Bitmap>();
		apkList = new HashMap<String , Drawable>();
		musicList = new HashMap<String , Bitmap>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	class Holder{
		ImageView icon;
		TextView fName;
		TextView fType;
		TextView fSize;
	//	CheckBox box;
		ImageView lockimg;
		ImageView favimg;
	}
	
	@Override
	public View getView(int pos, View convertView2, ViewGroup arg2) {
		// TODO Auto-generated method stub
		item = list.get(pos);
		h = new Holder();
		View convertView = null;
		h = new Holder();
		convertView = inflater.inflate(R.layout.row_list_1, arg2 , false);
		h.icon = (ImageView)convertView.findViewById(R.id.fileIcon);
		h.fName = (TextView)convertView.findViewById(R.id.fileName);
		h.fType = (TextView)convertView.findViewById(R.id.fileType);
		h.fSize = (TextView)convertView.findViewById(R.id.fileSize);
		h.lockimg = (ImageView)convertView.findViewById(R.id.lockimg);
		h.favimg = (ImageView)convertView.findViewById(R.id.favimg);
		convertView.setTag(h);
		
		if(item.isLocked())
			h.lockimg.setImageDrawable(ctx.getResources().getDrawable(R.drawable.lock_icon_hd));
		else
			h.lockimg.setImageDrawable(ctx.getResources().getDrawable(R.drawable.unlocked_icon_hd));
		
		h.lockimg.setId(pos);
		h.lockimg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImageView img = (ImageView)v;
				SharedPreferences prefs = ctx.getSharedPreferences("SETTINGS", 0);
				if(!list.get(img.getId()).isLocked()){
					//checking for master password is set or not
					String passwd = prefs.getString("MASTER_PASSWORD", null);
					if(passwd==null){
						Constants.lock = img;
 						new MasterPassword(ctx, Constants.size.x*8/9, null,prefs,Constants.MODES.DEFAULT);
					}
					else{
						list.get(img.getId()).setLockStatus(true);
						img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.lock_icon_hd));
						Constants.db.insertNodeToLock(list.get(img.getId()).getFile().getAbsolutePath());
						Toast.makeText(ctx, R.string.itemlocked, Toast.LENGTH_SHORT).show();
					}					
				}else{
					//unlocking file,before that asking the password...
					Constants.lock = img;
					new MasterPassword(ctx, Constants.size.x*8/9,  null,prefs,Constants.MODES.DEFAULT);
				}
			}
		});
		
		if(item.isFavItem())
			h.favimg.setImageDrawable(ctx.getResources().getDrawable(R.drawable.fav_icon_hd));
		else
			h.favimg.setImageDrawable(ctx.getResources().getDrawable(R.drawable.non_fav_icon_hd));
		
		h.favimg.setId(pos);
		h.favimg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImageView im = (ImageView)v;
				if(list.get(im.getId()).isFavItem()){
					im.setImageDrawable(ctx.getResources().getDrawable(R.drawable.non_fav_icon_hd));
					list.get(im.getId()).setFavStatus(false);
					Constants.db.deleteFavItem(list.get(im.getId()).getPath());
					Toast.makeText(ctx, R.string.favremoved, Toast.LENGTH_SHORT).show();
					//rebuilding the favorite items list after an item was removed....
					Utils.buildFavItems(list.get(im.getId()) , false);
					Utils.fav_Update_Needed = true;
				}else{
					im.setImageDrawable(ctx.getResources().getDrawable(R.drawable.fav_icon_hd));
					list.get(im.getId()).setFavStatus(true);
					Constants.db.insertNodeToFav(list.get(im.getId()).getPath());
					Toast.makeText(ctx, R.string.favadded, Toast.LENGTH_SHORT).show();
					//rebuilding the favorite items list after an item was added....
					Utils.buildFavItems(list.get(im.getId()) , true);
					Utils.fav_Update_Needed = true;
				}
			}
		});
		
		h.fName.setText(item.getName());
		h.fType.setText(item.getType());
		h.fSize.setText(item.getSize());
		h.icon.setImageDrawable(item.getIcon());
		if(item.getType().equals("Image")){
			image = imgList.get(item.getPath());
			if(image != null)
				h.icon.setImageBitmap(image);
			else
				new LoadImage(h.icon, item).execute();
		}else if(item.getType().equals("App")){
			Drawable draw = apkList.get(item.getPath());
			if(draw == null)
				new LoadApkIcon(h.icon, item).execute();
			else
				h.icon.setImageDrawable(draw);
			
		}else if(item.getType().equals("Music")){
			Bitmap music = musicList.get(item.getPath());
			if(music !=null)
				h.icon.setImageBitmap(music);
			else
				new LoadAlbumArt(h.icon , item).execute();
			
		}
		
		//true when multi select is on....
		if(Constants.LONG_CLICK){
			if(SdCardPanel.ITEMS[pos] == 1)
				convertView.setBackgroundColor(ctx.getResources().getColor(R.color.white_grey));
		}
		
		return convertView;
	}	
	
	/**
	 * class to load images thumbnail...
	 * @author Anurag
	 *
	 */	
	private class LoadImage extends AsyncTask<Void , Void ,Void>{
		ImageView iView;
		Bitmap map;
		Item itm;
		public LoadImage(ImageView view , Item it) {
			// TODO Auto-generated constructor stub
			this.iView = view;
			this.itm = it;
		}		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(map != null)
				iView.setImageBitmap(map);
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try{
				map = imgList.get(itm.getPath());
				if(map == null){
					long len_kb = itm.getFile().length() / 1024;
					
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.outWidth = 50;
					options.outHeight = 50;
						
					if (len_kb > 1000 && len_kb < 5000) {
						options.inSampleSize = 32;
						options.inPurgeable = true;
						map = (BitmapFactory.decodeFile(itm.getPath(), options));
											
					} else if (len_kb >= 5000) {
						options.inSampleSize = 32;
						options.inPurgeable = true;
						map = (BitmapFactory.decodeFile(itm.getPath(), options));
										
					} else if (len_kb <= 1000) {
						options.inPurgeable = true;
						map = (Bitmap.createScaledBitmap(BitmapFactory.decodeFile(itm.getPath()),50,50,false));
					}
					imgList.put(itm.getPath(), map);
				}
			}catch(OutOfMemoryError e){
				map = null;
				imgList.clear();
				imgList = null;
				imgList = new HashMap<String , Bitmap>();
			}catch(Exception e){
				map = null;
			}
			return null;
		}		
	}
	
	/**
	 * Class to load the apk icons...
	 * @author Anurag
	 *
	 */
	private class LoadApkIcon extends AsyncTask<Void, Void, Void>{

		ImageView iView;
		Item itm;
		Drawable dra;
		public LoadApkIcon(ImageView view , Item it) {
			// TODO Auto-generated constructor stub
			this.iView = view;
			this.itm = it;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(dra != null)
				iView.setImageDrawable(dra);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try{
				PackageInfo inf = ctx.getPackageManager().getPackageArchiveInfo(itm.getPath(),0);
				inf.applicationInfo.publicSourceDir = itm.getPath();
				dra = inf.applicationInfo.loadIcon(ctx.getPackageManager());
				apkList.put(itm.getPath(), dra);
				
			}catch(OutOfMemoryError e){
				dra = null;
				apkList.clear();
				apkList = null;
				apkList = new HashMap<String , Drawable>();
			}catch(Exception e){
				dra = null;
			}
			
			return null;
		}		
	}
	
	/**
	 * Class to load the album art of the music files....
	 * @author Anurag....
	 *
	 */
	private class LoadAlbumArt extends AsyncTask<Void, Void, Void>{

		ImageView iView;
		Bitmap map;
		Item itm;
		public LoadAlbumArt(ImageView view , Item it) {
			// TODO Auto-generated constructor stub
			this.iView = view;
			this.itm = it;
		}		
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			if(map !=null)
				iView.setImageBitmap(map);
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try{
				MediaMetadataRetriever ret = new MediaMetadataRetriever();
				ret.setDataSource(ctx, Uri.parse(itm.getPath()));
				map = BitmapFactory.decodeByteArray(ret.getEmbeddedPicture(), 0, ret.getEmbeddedPicture().length);
				if(map!=null)
					musicList.put(itm.getPath(), map);
			}catch(OutOfMemoryError e){
				map = null;
				musicList.clear();
				musicList = null;
				musicList = new HashMap<String , Bitmap>();
			}
			catch(Exception e){
				map = null;
			}
			return null;
		}		
	}
}
