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

package org.anurag.file.quest;

import java.util.ArrayList;
import java.util.HashMap;

import org.anurag.dropbox.DBoxUsers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.ImageView;

/**
 * THIS FILE CONTAINS THE CONSTANTS THAT ARE USED ALL OVER THE APP...
 * @author Anurag
 *
 */
public class Constants {
	
	public static int FOLDER_ICON;
	public static int[] FOLDERS = {R.drawable.grey_folder ,
		   R.drawable.red_folder,
		   R.drawable.orange_folder , 
		   R.drawable.green_folder,
		   R.drawable.blue_folder ,
		   R.drawable.violet_folder  };	
	
	//settings to verify whether to show following thumbs,,,,,
	//by default they are true....
	public static boolean SHOW_IMAGE_THUMB;
	public static boolean SHOW_APP_THUMB;
	public static boolean SHOW_MUSIC_THUMB;
	
	public static long GB = 1024*1024*1024;
	public static long MB = 1024*1024;
	public static int BUFFER = 1024*1024;
	
	//environment path....
	public static String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	//external memory card path,if not available its null....
	public static String EXT_PATH;
	
	//available above JBMR1....,else its empty....
	public static String EMULATED_PATH;
	
	//in some devices legacy path is available in emulated folder,if not
	//available its empty....
	public static String LEGACY_PATH; 
	
	//if true then external sdcard is plugged....
	public static boolean isExtAvailable;
	
	public static boolean LOCK_CHILD;
	public static ImageView lock;
	public static ItemDB db;
	public static DBoxUsers dboxDB;
	public static boolean disable_lock;
	public static Context ctx;
	
	/**
	 * variables for adapter classes used....
	 * they are common in 8 adapter classes....
	 */
	public static HashMap<String, Drawable> apkList = new HashMap<>();
	public static HashMap<String, Bitmap> imgList = new HashMap<>();
	public static HashMap<String, Bitmap> musicList = new HashMap<>();
	//public static HashMap<String, Bitmap> vidList = new HashMap<>();
	
	
	/**
	 * THIS ENUM IS FOR MASTER PASSWORD ....
	 * 
	 * DEFAULT for setting password for first time...
	 * RESET for changing password...
	 * DELETE for deleting files...
	 * COPY for copying files...
	 * RENAME for renaming files...
	 * SEND for sending files...
	 * ARCHIVE for archive files...
	 * PASTEINTO for pasting files...
	 * ADDGESTURE for gesture stuffs...
	 * OPEN for opening files...
	 * UNLOCK_ALL to delete all locked files entries from DB...
	 * HOME opening the home folder that is locked...  
	 */
	public static enum MODES { RESET , DELETE , COPY , RENAME , SEND , 
		ARCHIVE , PASTEINTO , G_OPEN , OPEN , DEFAULT , UNLOCK_ALL , HOME , DISABLE_NEXT_RESTART};
		
	//This is get reference for which operation the master password was verified....	
	public static MODES activeMode;
	
	public static ArrayList<String> RESOLVED_PATHS;
	
	//holding the id for the item in file gallery that was selected for locking
	//or unlocking,after verification of password this id will be used in FileQuest Activity....
	public static String lockID;
	
	public static int SORT_TYPE;
	public static boolean SHOW_HIDDEN_FOLDERS;
	
	public static Drawable FOLDER_IMAGE;
	public static Drawable MUSIC;
	public static Drawable APP;
	public static Drawable IMAGE;
	public static Drawable VIDEO;
	public static Drawable DOCS;
	public static Drawable PDF;
	public static Drawable ARCHIVE;
	public static Drawable UNKNOWN;
	public static Drawable WEB;
	public static Drawable SCRIPT;
	
	public static String GB_STR;
	public static String MB_STR;
	public static String KB_STR;
	public static String BYT_STR;
	
	public static Drawable LOCK_IMG;
	public static Drawable UNLOCK_IMG;
	public static Drawable FAV_IMG;
	public static Drawable NONFAV_IMG;
	public static Point size;
	
	public static int PANEL_NO;
	public static int COLOR_STYLE = 0;
	public static int DIALOG_STYLE = 0;
	public static boolean ACTION_AT_TOP;
	
	public static int LIST_ANIM;
	
	/**
	 * 
	 */
	public static int PAGER_ANIM;
	/**
	 * 1 simple list view
	 * 2 detailed list view
	 * 3 simple grid view
	 * 4 detailed grid view
	 */
	public static int LIST_TYPE;
	public static boolean LONG_CLICK[] = new boolean[4];
	
	/**
	 * 
	 * @param ctx
	 */
	public static void BUILD_ICONS(Context ctx){
		FOLDER_IMAGE = ctx.getResources().getDrawable(Constants.FOLDERS[Constants.FOLDER_ICON]);
		MUSIC = ctx.getResources().getDrawable(R.drawable.music_icon_hd);
		APP = ctx.getResources().getDrawable(R.drawable.app_icon_hd);
		IMAGE = ctx.getResources().getDrawable(R.drawable.image_icon_hd);
		VIDEO = ctx.getResources().getDrawable(R.drawable.video_icon_hd);
		DOCS = ctx.getResources().getDrawable(R.drawable.docs_icon_hd);
		PDF = ctx.getResources().getDrawable(R.drawable.pdf_icon_hd);
		ARCHIVE = ctx.getResources().getDrawable(R.drawable.archive_icon_hd);
		UNKNOWN = ctx.getResources().getDrawable(R.drawable.unknown_icon_hd);
		WEB = ctx.getResources().getDrawable(R.drawable.web_icon_hd);
		SCRIPT = ctx.getResources().getDrawable(R.drawable.script_icon_hd);
		
		GB_STR = "%.2f GB";
		MB_STR = "%.2f MB";
		KB_STR = "%.2f KB";
		BYT_STR = "%.2f Byte";
	}
	
	/**
	 * 
	 * @param ctx
	 */
	public static void BUILD_LIST_ICONS(Context ctx){
		LOCK_IMG = ctx.getResources().getDrawable(R.drawable.lock_icon_hd);
		UNLOCK_IMG = ctx.getResources().getDrawable(R.drawable.unlocked_icon_hd);
		FAV_IMG = ctx.getResources().getDrawable(R.drawable.fav_icon_hd);
		NONFAV_IMG = ctx.getResources().getDrawable(R.drawable.non_fav_icon_hd);
	}
	
}
