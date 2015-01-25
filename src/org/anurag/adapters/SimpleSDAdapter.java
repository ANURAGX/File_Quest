/**
 * Copyright(c) 2015 DRAWNZER.ORG PROJECTS -> ANURAG
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

import java.util.ArrayList;
import java.util.HashMap;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.MasterPassword;
import org.anurag.file.quest.R;
import org.anurag.file.quest.Utils;
import org.anurag.fragments.SdCardPanel;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SimpleSDAdapter extends BaseAdapter{

	private LayoutInflater inf;
	private ArrayList<Item> list;
	private Item item;
	private Context ctx;
	
	private static HashMap<String, Drawable> apkList;
	private static HashMap<String, Bitmap> imgList;
	private static HashMap<String, Bitmap> musicList;
	private static HashMap<String, Bitmap> vidList;
	private Bitmap image;
	public SimpleSDAdapter(Context ct , ArrayList<Item> objs) {
		// TODO Auto-generated constructor stub
		list = objs;
		ctx = ct;
		inf = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		apkList = new HashMap<>();
		imgList = new HashMap<>();
		musicList = new HashMap<>();
		vidList = new HashMap<>();
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

	class hold{
		ImageView icn;
		TextView name;
		ImageView lockimg;
		ImageView favimg;
	}
	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		hold h = new hold();
		item = list.get(arg0);
		View view = inf.inflate(R.layout.simple_list_hd, arg2 , false);
		h.icn = (ImageView) view.findViewById(R.id.fileIcon);
		h.name = (TextView) view.findViewById(R.id.fileName);
		h.lockimg = (ImageView) view.findViewById(R.id.lockimg);
		h.favimg = (ImageView) view.findViewById(R.id.favimg);
		view.setTag(h);
		
		if(item.isLocked())
			h.lockimg.setImageDrawable(Constants.LOCK_IMG);
		else
			h.lockimg.setImageDrawable(Constants.UNLOCK_IMG);
		
		
		h.lockimg.setId(arg0);
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
						img.setImageDrawable(Constants.LOCK_IMG);
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
			h.favimg.setImageDrawable(Constants.FAV_IMG);
		else
			h.favimg.setImageDrawable(Constants.NONFAV_IMG);
		

		h.favimg.setId(arg0);
		h.favimg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImageView im = (ImageView)v;
				if(list.get(im.getId()).isFavItem()){
					im.setImageDrawable(Constants.NONFAV_IMG);
					list.get(im.getId()).setFavStatus(false);
					Constants.db.deleteFavItem(list.get(im.getId()).getPath());
					Toast.makeText(ctx, R.string.favremoved, Toast.LENGTH_SHORT).show();
					//rebuilding the favorite items list after an item was removed....
					Utils.buildFavItems(list.get(im.getId()) , false);
					Utils.fav_Update_Needed = true;
				}else{
					im.setImageDrawable(Constants.FAV_IMG);
					list.get(im.getId()).setFavStatus(true);
					Constants.db.insertNodeToFav(list.get(im.getId()).getPath());
					Toast.makeText(ctx, R.string.favadded, Toast.LENGTH_SHORT).show();
					//rebuilding the favorite items list after an item was added....
					Utils.buildFavItems(list.get(im.getId()) , true);
					Utils.fav_Update_Needed = true;
				}
			}
		});
		
		h.name.setText(item.getName());
		h.icn.setImageDrawable(item.getIcon());
		if(item.getType().equals("Image")){
			image = imgList.get(item.getPath());
			if(image != null)
				h.icn.setImageBitmap(image);
			else
				new LoadImage(h.icn, item).execute();
		}else if(item.getType().equals("App")){
			Drawable draw = apkList.get(item.getPath());
			if(draw == null)
				new LoadApkIcon(h.icn, item).execute();
			else
				h.icn.setImageDrawable(draw);
			
		}else if(item.getType().equals("Music")){
			Bitmap music = musicList.get(item.getPath());
			if(music !=null)
				h.icn.setImageBitmap(music);
			else
				new LoadAlbumArt(h.icn , item).execute();
			
		}else if(item.getType().equals("Video")){
			Bitmap vi = vidList.get(item.getPath());
			if(vi != null)
				h.icn.setImageBitmap(vi);
			else
				new VidThumb(h.icn, item).execute();
		}
			
		//true when multi select is on....
		if(Constants.LONG_CLICK){
			if(SdCardPanel.ITEMS[arg0] == 1)
				view.setBackgroundColor(ctx.getResources().getColor(R.color.white_grey));
		}
		return view;
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
	/**
	 * 
	 * @author anurag
	 *
	 */
	private class VidThumb extends AsyncTask<Void, Void, Void>{

		ImageView iview;
		Item itm;
		Bitmap thmb;
		public VidThumb(ImageView img ,Item items) {
			// TODO Auto-generated constructor stub
			itm = items;
			iview = img;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(thmb != null)
				iview.setImageBitmap(thmb);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try{
				thmb = ThumbnailUtils.createVideoThumbnail(itm.getPath(), Thumbnails.MICRO_KIND);
				vidList.put(item.getPath(), thmb);
			}catch(OutOfMemoryError e){
				vidList.clear();
				vidList = null;
				thmb = null;
				vidList = new HashMap<>();
			}
			return null;
		}		
	}
}
