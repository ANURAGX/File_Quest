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

import org.anurag.dropbox.DBoxUsers;

import android.os.Environment;
import android.widget.ImageView;

/**
 * THIS FILE CONTAINS THE CONSTANTS THAT ARE USED ALL OVER THE APP...
 * @author Anurag
 *
 */
public class Constants {
	
	public static int FOLDER_ICON;
	public static int[] FOLDERS = {R.drawable.ic_launcher_orange_folder ,
		   R.drawable.ic_launcher_green_folder,
		   R.drawable.ic_launcher_cyan_folder , 
		   R.drawable.ic_launcher_violet_folder,
		   R.drawable.ic_launcher_grey_folder ,
		   R.drawable.ic_launcher_brown_folder,
		   R.drawable.ic_launcher_blue_folder
		   };	
	
	public static boolean LOAD_THUMBNAILS=false;
	public static long GB = 1024*1024*1024;
	public static long MB = 1024*1024;
	public static int BUFFER = 1024*1024;
	
	//environment path....
	public static String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	//external memory card path,if not available its null....
	public static String EXT_PATH;
	
	//available above JBMR1....,else null
	public static String EMULATED_PATH;
	
	//in some devices legacy path is available in emulated folder,if not
	//available its null....
	public static String LEGACY_PATH; 
	
	public static boolean LOCK_CHILD;
	public static ImageView lock;
	public static ImageView fav;
	public static ItemDB db;
	public static DBoxUsers dboxDB;
	
	
	
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
		ARCHIVE , PASTEINTO , G_OPEN , OPEN , DEFAULT , UNLOCK_ALL , HOME};
		
	//This is get reference for which operation the master password was verified....	
	public static MODES activeMode;
	
	public static ArrayList<String> RESOLVED_PATHS;
	
	//holding the id for the item in file gallery that was selected for locking
	//or unlocking,after verification of password this id will be used in FileQuest Activity....
	public static String lockID;
}
