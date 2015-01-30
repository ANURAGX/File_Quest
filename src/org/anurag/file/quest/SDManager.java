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
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Stack;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * 
 * @author Anurag
 *
 */
public class SDManager {
	
	private Stack<String> nStack;
	private ArrayList<Item> items;
	private Context ctx;
	private File file;
	private String type;
	private Resources res;
	private FileUtils utils;
	
	public SDManager(Context context) {
		// TODO Auto-generated constructor stub
		ctx = context;
		nStack = new Stack<String>();
		if(new File("/storage").exists()){
			nStack.push("/storage");
			//nStack.push("/storage");
			Constants.PATH = "/storage";
		}else{
			nStack.push(Constants.PATH);		
			//nStack.push(Constants.PATH);
		}	
		items = new ArrayList<Item>();
		res = ctx.getResources();
		
		utils = new FileUtils();
	}
	
	/**
	 * Function to return current path  
	 * @return
	 */
	public String getCurrentDirectory(){
		return nStack.peek();
	}
	
	/**
	 * Function To return Current File Name
	 * @return
	 */
	public String getCurrentDirectoryName(){
		File file = new File(nStack.peek());
		return file.getName();
	}
	
	/**
	 * THIS CLASS FILTERS OUT THOS FILE THAT CANNOT BE READ AND ARE HIDDEN
	 * @author anurag
	 *
	 */
	public static class HiddenFileFilter implements FileFilter{
		@Override
		public boolean accept(File f) {
			// TODO Auto-generated method stub
			return f.canRead() && !f.isHidden();
		}
		
	}
	 
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Item> getList(){
		items.clear();
		
		file = new File(nStack.peek());
		File[] files = file.listFiles(new HiddenFileFilter());
		int len = files.length;
		for(int i = 0 ; i < len ; ++i)
			items.add(new Item(files[i], buildIcon(files[i]), type, getSize(files[i])));
		
		
		switch(Constants.SORT_TYPE){
		case 1:
			//a-z sort
			utils.a_zSort(items);
			break;
			
		case 2:
			//z-a sort
			utils.z_aSort(items);
			break;
			
		case 3:
			//smaller size first sort....
			utils.smallSize_Sort(items);
			break;
			
		case 4:
			//larger size first sort....
			utils.bigSize_Sort(items);
			break;
			
		case 5:
			//new date file first....
			utils.newDate_Sort(items);
			break;
			
		case 6:
			//old date file first....
			utils.oldDate_Sort(items);
		}
		
		return items;
	}

	/**
	 * 
	 * @return icon for the file....
	 */
	private Drawable buildIcon(File f){
		if(f.isDirectory()){
			type = ctx.getString(R.string.directory);
			if(Constants.isExtAvailable)
				if(f.getAbsolutePath().equalsIgnoreCase(Constants.EXT_PATH))
					return res.getDrawable(R.drawable.sdcard);
			return Constants.FOLDER_IMAGE;
		}
		
		String name = f.getName().toLowerCase(Locale.ENGLISH);
		
		if(name.endsWith(".zip")){
			
			type=ctx.getString(R.string.zip);
			return Constants.ARCHIVE;
			
		}else if(name.endsWith(".7z")){
			
			type=ctx.getString(R.string.zip7);
			return Constants.ARCHIVE;
			
		}else if(name.endsWith(".rar")){
			
			type=ctx.getString(R.string.rar);
			return Constants.ARCHIVE;
			
		}else if(name.endsWith(".tar")||name.endsWith(".tar.gz")||name.endsWith(".tar.bz2")){
			
			type=ctx.getString(R.string.tar);
			return Constants.ARCHIVE;
			
		}
		else if(name.endsWith(".mp3")||name.endsWith(".ogg")||name.endsWith(".m4a")||name.endsWith(".wav")
				||name.endsWith(".amr")){
			
			type=ctx.getString(R.string.music);
			return Constants.MUSIC;
			
		}
		else if(name.endsWith(".apk")){
			
			type=ctx.getString(R.string.application);
			return Constants.APP;
			
		}else if(name.endsWith(".sh")||name.endsWith(".prop")||name.endsWith("init")
				||name.endsWith(".default")||name.endsWith(".rc")){
			
			type=ctx.getString(R.string.script);
			return Constants.SCRIPT;
			
		}else if(name.endsWith(".pdf")){
			
			type=ctx.getString(R.string.pdf);
			return Constants.PDF;
			
		}else if(name.endsWith(".htm")||name.endsWith(".html")||name.endsWith(".mhtml")){
			
			type=ctx.getString(R.string.web);
			return Constants.WEB;
			
		}else if(name.endsWith(".flv")||name.endsWith(".mp4")||name.endsWith(".3gp")||name.endsWith(".avi")
				||name.endsWith(".mkv")){
			
			type=ctx.getString(R.string.vids);
			return Constants.VIDEO;
			
		}	
		else if(name.endsWith(".bmp")||name.endsWith(".gif")||name.endsWith(".jpeg")||name.endsWith(".jpg")
				||name.endsWith(".png")){
			
			type=ctx.getString(R.string.image);
			return Constants.IMAGE;
			
		}else if(name.endsWith(".txt")||name.endsWith(".log")||name.endsWith(".ini")){
			
			type=ctx.getString(R.string.text);
			return Constants.DOCS;
			
		}
		else if(name.endsWith(".doc")||name.endsWith(".ppt")||name.endsWith(".docx")||name.endsWith(".DOC")
				||name.endsWith(".pptx")||name.endsWith(".csv")){
			
			type=ctx.getString(R.string.docs);
			return Constants.DOCS;
			
		}
		else{
			
			type=ctx.getString(R.string.unknown);
			return Constants.UNKNOWN;
			
		}		
	}
	
	/**
	 * THIS FUNCTION RETURN THE SIZE IF THE GIVEN FIZE IN PARAMETER
	 * @param f
	 * @return
	 */
	private String getSize(File f){
		if(f.isDirectory())
			return f.list().length+" "+ctx.getString(R.string.items);
		long size = f.length();
		if(size>Constants.GB)
			return String.format(Constants.GB_STR, (double)size/(Constants.GB));
		
		else if(size > Constants.MB)
			return String.format(Constants.MB_STR, (double)size/(Constants.MB));
		
		else if(size>1024)
			return String.format(Constants.KB_STR, (double)size/(1024));
		
		else
			return String.format(Constants.BYT_STR, (double)size);
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Item> getPreviousList(){
	//	nStack.pop();
		return getList();
	}
	
	/**
	 * pushes a path to top of stack....
	 * 
	 * @param path which was being viewed....
	 */
	public void pushPath(String path){
		nStack.push(path);
	}
	
	/**
	 * pops out the top path from the stack.... 
	 */
	public void popTopPath(){
		nStack.pop();
	}
	
}
