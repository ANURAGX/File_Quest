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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.zip.ZipFile;

import org.anurag.compress.RarManager;
import org.anurag.compress.ZipManager;

import android.content.Context;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;

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
	private FileUtils utils;
	
//	private boolean isInTar;
	private boolean isInZip;
	private boolean isInRar;
	private ZipManager zMgr;
	private RarManager rMgr;
//	private TarManager tMgr;
	
	/**
	 * 
	 * @param context
	 */
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
	 * 
	 * @param value
	 */
	public void setInZip(boolean value){
		if(!value){
			zMgr = null;
		}
		isInZip = value;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setInRar(boolean value){
		if(!value){
			rMgr = null;
		}
		isInRar = value;
	}
	
	/**
	 * 
	 * @return true if rar or zip archive is opened
	 */
	public boolean isArchiveOpened(){
		return (isInRar || isInZip);
	}
	
	/**
	 * 
	 * @param value
	 *//*
	public void setInTar(boolean value){
		if(!value){
			tMgr = null;
		}
		isInTar = value;
	}*/
	
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
	 * @return the list of files of selected folder,can return list inside of rar,zip 
	 * or tar archives....
	 * 
	 * sorting is not applied inside archives,it uses default folder first 
	 * sorting(in alphabetical order)...
	 * 
	 */
	public ArrayList<Item> getList(){
		if(isInZip && nStack.peek().contains("->")){
			zMgr.setPath(nStack.peek().substring(0 , nStack.peek().length() - 6));
			return zMgr.generateList();
		}else if(isInRar && nStack.peek().contains("->")){
			rMgr.setPath(nStack.peek().substring(0 , nStack.peek().length() - 6));
			return rMgr.generateList();
		}/*else if(isInTar && nStack.peek().contains("->")){
			tMgr.setPath(nStack.peek().substring(0 , nStack.peek().length() - 6));
			return tMgr.generateList();
		}*/
		else{
			setInZip(false);
			setInRar(false);
			//setInTar(false);
		}
		
		items.clear();
		
		file = new File(nStack.peek());
		File[] files = file.listFiles(new HiddenFileFilter());
		int len = files.length;
		for(int i = 0 ; i < len ; ++i){
			FileType filetype = new FileType(files[i], ctx);
			items.add(new Item(files[i], filetype.getIcon(), filetype.getType(), filetype.getSize()));
		}	
		
		
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
		if(isInZip){
			if(zMgr == null){
				
				try {
					zMgr = new ZipManager(new ZipFile(new File(path)), "", ctx);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					isInZip = false;
					return;
				}
				
				nStack.push("/ ->Zip");
				return;
			}	

			nStack.push(path + " ->Zip");
			return;
		}
		
		if(isInRar){
			if(rMgr == null){
				try {
					rMgr = new RarManager(new Archive(new File(path)), "", ctx);
				} catch (RarException | IOException e) {
					// TODO Auto-generated catch block
					isInRar = false;
					return;
				}
				nStack.push("/ ->Rar");
				return;
			}
			
			nStack.push(path + " ->Rar");
			return;
		}
	/*
		if(isInTar){
			if(tMgr == null){
				try {
					tMgr = new TarManager(new File(path), "", ctx);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					isInTar = false;
					return;
				}

				nStack.push("/ ->Tar");
				return;
			}
			nStack.push(path + " ->Tar");
			return;
		
		}		*/
		nStack.push(path);
	}
	
	/**
	 * pops out the top path from the stack.... 
	 */
	public void popTopPath(){
		nStack.pop();
	}	
}
