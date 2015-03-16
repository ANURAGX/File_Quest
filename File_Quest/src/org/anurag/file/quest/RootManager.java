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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Stack;

import android.content.Context;

/**
 * 
 * @author Anurag
 *
 */
public class RootManager {

	private Stack<String> nStack;
	private ArrayList<Item> items;
	private Context ctx;
	private File file;
	private FileUtils utils;
	
	/**
	 * 
	 * @param context
	 */
	public RootManager(Context context) {
		// TODO Auto-generated constructor stub
		ctx = context;
		nStack = new Stack<String>();
		nStack.push("/");
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
			return !f.isHidden();
		}
		
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Item> getList(){
		items.clear();		
		file = new File(nStack.peek());
		File[] files = listFiles(file);
		int len = files.length;
		for(int i = 0 ; i < len ; ++i){
			FileType filetype = new FileType(files[i], ctx);
			items.add(new Item(files[i], filetype.getIcon() , filetype.getType(), filetype.getSize()));
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
		//nStack.pop();
		return getList();
	}
	
	/**
	 * FUNCTION LISTS THE ARRAY OF FILE ....
	 * IF ITS ROOT DIRECTORY NEEDED PERMISSION TO READ THEN SEEKS FOR ROOT ACCESS
	 * @param f
	 * @return
	 */
	public File[] listFiles(File f){
		if(f.canRead()){
			return f.listFiles(new HiddenFileFilter());
		}else{
			
			ArrayList<File> tList = new ArrayList<File>();
			BufferedReader reader = null; // errReader = null;
			try {
				reader = LinuxShell
						.execute("IFS='\n';CURDIR='"
								+ LinuxShell.getCmdPath(f.getAbsolutePath())
								+ "';for i in `ls $CURDIR`; do if [ -d $CURDIR/$i ]; then echo \"d $CURDIR/$i\";else echo \"f $CURDIR/$i\"; fi; done");
				if(reader==null)
					return new File[0];				
				File f2;
				String line;
				while ((line = reader.readLine()) != null) {
					f2 = new File(line.substring(2));
					tList.add(f2);
				}
			}catch(Exception e){
				nStack.pop();
				return f.listFiles(new HiddenFileFilter());
			}
			
			int l = tList.size();
			File[] r = new File[l];
			for(int i = 0 ; i<l;++i)
				r[i]=tList.get(i);
			return r;
		}
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
