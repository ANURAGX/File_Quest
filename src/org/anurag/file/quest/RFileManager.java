/**
 * Copyright(c) 2013 ANURAG 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * anurag.dev1512@gmail.com
 *
 */
package org.anurag.file.quest;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;
import android.os.Environment;


public class RFileManager {
	private static String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	//private Message msg;
	public static int SORT_TYPE;
	public static Stack<String> nStack;
	private static ArrayList<File> nFiles;
	public static boolean SHOW_HIDDEN_FOLDER = false;
	private static File file;
	//public Handler mHandler;
	public RFileManager(){
		nStack = new Stack<String>();
		nFiles = new ArrayList<File>();
		if(new File("/storage").exists())
			PATH = "/storage";
		nStack.push(PATH);
		nStack.push(Environment.getExternalStorageDirectory().getAbsolutePath());
	}	
	
	
	/**
	 * Function to return current path  
	 * @return
	 */
	public static String getCurrentDirectory(){
		return nStack.peek();
	}
	
	/**
	 * Function To return Current File Name
	 * @return
	 */
	public static String getCurrentDirectoryName(){
		file = new File(nStack.peek());
		return file.getName();
	}
	
	/**
	 * THIS CLASS FILTERS OUT THOSE FILES THAT CANNOT BE READ
	 * @author anurag
	 *
	 */
	public static class ReadFileFilter implements FileFilter{
		@Override
		public boolean accept(File f) {
			// TODO Auto-generated method stub
			return f.canRead();
		}
		
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
	 * SORTS THE FILE[] ALPHABETICALLY WITH HAVING FOLDERS FIRST
	 */
	public static Comparator<File> alphaFolderFirst = new Comparator<File>() {
		@Override
		public int compare(File a, File b) {
			boolean aIsFolder = a.isDirectory();
			boolean bIsFolder = b.isDirectory();
			if(bIsFolder == aIsFolder )
				return a.getName().compareToIgnoreCase(b.getName());
			else if(bIsFolder)
				return 1;
			return -1;
		}
	}; 
	
	/**
	 * SORTS THE FILE[] ALPHABETICALLY WITH HAVING FILES FIRST
	 */
	public static Comparator<File> alphaFileFirst = new Comparator<File>() {
		@Override
		public int compare(File a, File b) {
			boolean aIsFolder = a.isDirectory();
			boolean bIsFolder = b.isDirectory();
			if(bIsFolder == aIsFolder )
				return a.getName().compareToIgnoreCase(b.getName());
			else if(bIsFolder)
				return -1;
			return 1;
		}
	}; 
	
	/**
	 * SORTS THE FILE[] ALPHABETICALLY IRRESPECTIVE OF FILE OR FOLDER
	 */
	
	public static Comparator<File> alpha = new Comparator<File>() {
		@Override
		public int compare(File a, File b) {
				return a.getName().compareToIgnoreCase(b.getName());
		}
	}; 
	
	/**
	 * Function to Generate Current Directory File List
	 * @return
	 */
	public static ArrayList<File> getCurrentFileList(){
		nFiles.clear();
		file = new File(nStack.peek());
		if(SORT_TYPE == 4)
			return getCurrentFileListWithHiddenItemFirst();
		else if(SORT_TYPE == 5)
			return getCurrentFileListWithHiddenItemLast();
		if(file.canRead() && file.exists()){
			File[] files = null;
			if(!SHOW_HIDDEN_FOLDER)
				files = file.listFiles(new HiddenFileFilter());
			else
				files = file.listFiles(new ReadFileFilter());
			if(SORT_TYPE == 1)
				Arrays.sort(files,alpha);
			else if(SORT_TYPE == 2)
				Arrays.sort(files,alphaFolderFirst);
			else if(SORT_TYPE == 3)
				Arrays.sort(files,alphaFileFirst);
			for(int i = 0 ;i<files.length ; ++i){
				File f = files[i];
				nFiles.add(f);
			}	
		}
		return  nFiles;
	}
	
	/**
	 * Function to Generate Current Directory File List Without Having Hidden Folders In List
	 * Sorted in alphabetical order
	 * @return
	 */
	public static ArrayList<File> getCurrentFileListWithoutHiddenFolders(){
		nFiles.clear();
		file = new File(nStack.peek());
		if(file.canRead() && file.exists()){
			File[] files = file.listFiles(new HiddenFileFilter());
			for(int i = 0 ;i<files.length ; ++i)
				nFiles.add(files[i]);
		}
		return  nFiles;
	}

	/**
	 * function sorting files in alphabetical order
	 * keeping hidden items first
	 * @return
	 */
	public static ArrayList<File> getCurrentFileListWithHiddenItemFirst(){
		nFiles.clear();
		file = new File(nStack.peek());
		if(file.canRead() && file.exists()){
			File[] files = file.listFiles();
			Arrays.sort(files,alphaFolderFirst);
			if(SHOW_HIDDEN_FOLDER)
				for(int i = 0 ;i<files.length ; ++i)
					if( files[i].getName().startsWith(".") && files[i].canRead())
						nFiles.add(files[i]);
			for(int i = 0 ;i<files.length ; ++i)
				if( !files[i].getName().startsWith(".") && files[i].canRead())
					nFiles.add(files[i]);
		}
		
		return nFiles;
	}
	
	
	/**
	 * function sorting files in alphabetical order
	 * keeping hidden items first
	 * @return
	 */
	public static ArrayList<File> getCurrentFileListWithHiddenItemLast(){
		nFiles.clear();
		file = new File(nStack.peek());
		if(file.canRead() && file.exists()){
			File[] files = file.listFiles();
			Arrays.sort(files,alphaFolderFirst);
			for(int i = 0 ;i<files.length ; ++i)
				if( !files[i].getName().startsWith(".") && files[i].canRead())
					nFiles.add(files[i]);
			if(SHOW_HIDDEN_FOLDER)
				for(int i = 0 ;i<files.length ; ++i)
					if( files[i].getName().startsWith(".") && files[i].canRead())
						nFiles.add(files[i]);
		}
		return nFiles;
	}
	
	public static ArrayList<File> giveMeFileList(){
		return getCurrentFileList();
	}
	
	/**
	 * Function To Generate Previous Directory List
	 * @return
	 */
	public static ArrayList<File> getPreviousFileList(){
		if( nStack.size() >= 2)
			nStack.pop();
		else if( nStack.size() == 0)
			nStack.push("/");
		if(SHOW_HIDDEN_FOLDER)
			return getCurrentFileList();
		else{
			SHOW_HIDDEN_FOLDER = false;
			return getCurrentFileList();
		}
	}
	
	/**
	 * Function To Delete The Given File And Returns Message To Handler
	 * If Deletion is successful returns 0 else returns -1
	 * @param path
	 * @return
	 */
	public static void deleteTarget(File file) {
		File target = file;
		if(target.exists() && target.isFile() && target.canWrite())
			target.delete();
		else if(target.exists() && target.isDirectory() && target.canRead()) {
			String[] file_list = target.list();
			if(file_list != null && file_list.length == 0) {
				target.delete();
			} else if(file_list != null && file_list.length > 0) {
				for(int i = 0; i < file_list.length; i++) {
					File temp_f = new File(target.getAbsolutePath() + "/" + file_list[i]);
					if(temp_f.isDirectory())
						deleteTarget(temp_f);
					else if(temp_f.isFile())
						temp_f.delete();
				}
			}
			if(target.exists())
				if(target.delete()){}
		}
	}
		
}
