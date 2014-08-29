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
import java.util.Comparator;
import java.util.Stack;

/**
 * 
 * @author Anurag
 *
 */
public class SDManager {

	
	public static int SORT_TYPE;
	public static Stack<String> nStack;
	ArrayList<Item> items;
	public SDManager() {
		// TODO Auto-generated constructor stub
		nStack = new Stack<String>();
		if(new File("/storage").exists()){
			nStack.push("/storage");
		}else
			nStack.push(Constants.PATH);		
		nStack.push(Constants.PATH);
		items = new ArrayList<Item>();
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
		File file = new File(nStack.peek());
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
	public static Comparator<Item> alphaFolderFirst = new Comparator<Item>() {
		@Override
		public int compare(Item a, Item b) {
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
	public static Comparator<Item> alphaFileFirst = new Comparator<Item>() {
		@Override
		public int compare(Item a, Item b) {
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
	
	public static Comparator<Item> alpha = new Comparator<Item>() {
		@Override
		public int compare(Item a, Item b) {
				return a.getName().compareToIgnoreCase(b.getName());
		}
	}; 
}
