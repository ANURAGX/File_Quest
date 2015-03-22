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

package org.anurag.compress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.anurag.file.quest.Item;

import android.content.Context;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;


/**
 * 
 * @author Anurag
 *
 */
public class RarManager {
	
	private ArrayList<Item> list;
	private Context ctx;
	private String path;
	private List<FileHeader> ls;
	private Archive rar;
	
	/**
	 * 
	 * @param rarfile
	 * @param pathToShow
	 * @param context
	 */
	public RarManager(Archive rarfile, String pathToShow , Context context) {
		// TODO Auto-generated constructor stub
		list = new ArrayList<Item>();
		path = pathToShow;
		ctx = context;
		rar = rarfile;
	}
	
	/**
	 * 
	 * @param showPath
	 */
	public void setPath(String showPath){
		path = showPath;
	}
	
	public ArrayList<Item> generateList(){
		ls = rar.getFileHeaders();
				
		for(FileHeader fh : ls){
			if(fh.isDirectory())
				continue;
			boolean added = false;
			String name;
			if(fh.isUnicode())
				name = fh.getFileNameW();
			else 
				name = fh.getFileNameString();
			int len = list.size();
			if(path.equalsIgnoreCase("/")){
				while(name.contains("\\"))
					name = name.substring(0, name.lastIndexOf("\\"));
				for(int i=0;i<len;++i)
					if(list.get(0).getName().equalsIgnoreCase(name)){
						added = true;
						break;
					}
				
				if(!added)
					list.add(new Item(fh, name, "", ctx));
			}else{
				try{
					String headername = name;
					name = name.substring(path.length()+1, name.length());
					if(len>0){
						while(name.contains("\\"))
							name = name.substring(0, name.lastIndexOf("\\"));
						for(int i=0;i<len;++i){
							if(list.get(i).getName().equalsIgnoreCase(name)){
								added = true;
								break;
							}
						}
						if(!added && headername.startsWith(path))
							list.add(new Item(fh, name, path, ctx));
					}else{
						if(headername.startsWith(path)){
							while(name.contains("\\"))
								name = name.substring(0, name.lastIndexOf("\\"));
							list.add(new Item(fh, name, path, ctx));
						}	
					}
				}catch(StringIndexOutOfBoundsException e){
					
				}
			}
		}
		sort();
		return list;
	}

	
	/**
	 * SORTING THE FILES AS PER ALPHABETICAL ORDER...
	 * FIRST FOLDER AND THEN FILES.....
	 */
	private void sort(){
		Comparator<Item> comp = new Comparator<Item>() {
			@Override
			public int compare(Item a, Item b) {
				// TODO Auto-generated method stub
				boolean aisfolder = a.isDirectory();
				boolean bisfolder = b.isDirectory();
				if(aisfolder==bisfolder)
					return a.getName().compareToIgnoreCase(b.getName());
				else if(bisfolder)
					return 1;
				return -1;
			}
		};		
		Collections.sort(list, comp);
	}
}
