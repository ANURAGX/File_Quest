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

import java.io.File;
import java.util.regex.Pattern;

import android.os.Build;
import android.text.TextUtils;



/**
 * class to find external,internal,emulated,legacy paths....
 * @author Anurag....
 *
 */
public class StorageUtils {

	/**
	 * 
	 */
	public StorageUtils() {
		// TODO Auto-generated constructor stub
		getExternalMemoryPath();
		getEmulatedPath();
		getLegacyPath();
	}
	
	/**
	 * gets the mounted  external sd card id available....
	 */
	private void getExternalMemoryPath(){
		String paths = System.getenv("SECONDARY_STORAGE");
		Constants.EXT_PATH = paths; 
	}
	
	/**
	 * gets emulated paths from android version jbmr1 above....
	 */
	private void getEmulatedPath(){
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1){
			Constants.EMULATED_PATH = null;
			return;
		}	
		String emulatedpath = System.getenv("EMULATED_STORAGE_TARGET");
		String id = "";
		String path = Constants.PATH;
		Pattern DIRPATTERN = Pattern.compile("/");
		String[] folders = DIRPATTERN.split(path);
		String lastFolder = folders[folders.length - 1];
		boolean isDigit = false;
		try{
			Integer.valueOf(lastFolder);
			isDigit = true;
		}catch(NumberFormatException e){
			
		}
		id = isDigit ? lastFolder : "";
		if(TextUtils.isEmpty(id))
			Constants.EMULATED_PATH = emulatedpath;
		else{
			Constants.EMULATED_PATH = emulatedpath + File.pathSeparator + id;
		}
	}
	
	/**
	 * in some android devives like htc there's a path like legacy
	 * and sdcard0 is symlink to that path.... 
	 */
	private void getLegacyPath(){
		String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
		if(TextUtils.isEmpty(rawExternalStorage)){
			Constants.LEGACY_PATH = null;
			return;
		}	
		if(TextUtils.equals(rawExternalStorage, Constants.PATH))
			Constants.LEGACY_PATH = Constants.PATH;
		else
			Constants.LEGACY_PATH = rawExternalStorage;
	}	
}
