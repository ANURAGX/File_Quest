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

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

import org.ultimate.root.LinuxShell;

import android.os.Handler;

public class SFileManager {
	public static int SORT_TYPE;
	public static Stack<String> nStack;
	private static ArrayList<File> nFiles;
	public static boolean SHOW_HIDDEN_FOLDER = false;
	private static File file;
	//private static Handler mHandler;
	public SFileManager(){
		nStack = new Stack<String>();
		nFiles = new ArrayList<File>();
		nStack.push("/");
	}	
	
	public SFileManager(Handler handler){
		//this.mHandler = handler;
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
	
	public final static Comparator<File> alpha = new Comparator<File>() {
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
		if(file.exists()){
			File[] files = listFiles(file);
			if(SORT_TYPE == 1)
				Arrays.sort(files,alpha);
			else if(SORT_TYPE == 2)
				Arrays.sort(files,alphaFolderFirst);
			else if(SORT_TYPE == 3)
				Arrays.sort(files,alphaFileFirst);
			for(int i = 0 ;i<files.length ; ++i){
				File f = files[i];
				if(SHOW_HIDDEN_FOLDER)
					nFiles.add(f);
				else if(!f.getName().startsWith("."))
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
		if(file.exists()){
			File[] files = listFiles(file);
			Arrays.sort(files,alphaFolderFirst);
			for(int i = 0 ;i<files.length ; ++i)
				if(!files[i].getName().startsWith("."))
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
		if(file.exists()){
			File[] files = listFiles(file);
			Arrays.sort(files,alphaFolderFirst);
			for(int i = 0 ;i<files.length ; ++i)
				if(SHOW_HIDDEN_FOLDER)
					if( files[i].getName().startsWith(".") )
						nFiles.add(files[i]);
			for(int i = 0 ;i<files.length ; ++i)
				if( !files[i].getName().startsWith(".")) 
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
		if(file.exists()){
			File[] files = listFiles(file);
			Arrays.sort(files,alphaFolderFirst);
			for(int i = 0 ;i<files.length ; ++i)
				if( !files[i].getName().startsWith(".") )
					nFiles.add(files[i]);
			for(int i = 0 ;i<files.length ; ++i)
				if(SHOW_HIDDEN_FOLDER)
					if( files[i].getName().startsWith("."))
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
	 * FUNCTION LISTS THE ARRAY OF FILE ....
	 * IF ITS ROOT DIRECTORY NEEDED PERMISSION TO READ THEN SEEKS FOR ROOT ACCESS
	 * @param f
	 * @return
	 */
	public static File[] listFiles(File f){
		if(f.canRead()){
			return f.listFiles();				
		}else{
			ArrayList<File> tList = new ArrayList<File>();
			BufferedReader reader = null; // errReader = null;
			try {
				reader = LinuxShell
						.execute("IFS='\n';CURDIR='"
								+ LinuxShell.getCmdPath(f.getAbsolutePath())
								+ "';for i in `ls $CURDIR`; do if [ -d $CURDIR/$i ]; then echo \"d $CURDIR/$i\";else echo \"f $CURDIR/$i\"; fi; done");
				File f2;
				String line;
				while ((line = reader.readLine()) != null) {
					f2 = new File(line.substring(2));
					tList.add(f2);
				}
			}catch(Exception e){
				nStack.pop();
				return f.listFiles();
			}	
			int l = tList.size();
			File[] r = new File[l];
			for(int i = 0 ; i<l;++i)
				r[i]=tList.get(i);
			return r;
		}
	}
		
}
