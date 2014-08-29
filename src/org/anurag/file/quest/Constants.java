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

import android.os.Environment;

/**
 * THIS FILE CONTAINS THE CONSTANTS THAT ARE USED ALL OVER THE PROGRAM...
 * @author Anurag
 *
 */
public class Constants {
	
	public static int FOLDER_TYPE;
	public static int[] FOLDERS = {R.drawable.ic_launcher_orange_folder ,
		   R.drawable.ic_launcher_green_folder,
		   R.drawable.ic_launcher_yellow_folder , 
		   R.drawable.ic_launcher_violet_folder,
		   R.drawable.ic_launcher_red_folder ,
		   R.drawable.ic_launcher_brown_folder,
		   R.drawable.ic_launcher_blue_folder};	
	
	public static boolean LOAD_THUMBNAILS=false;
	public static long GB = 1024*1024*1024;
	public static long MB = 1024*1024;
	public static int BUFFER = 1024*1024;
	public static String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
}
