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
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.anurag.file.quest.Item;

import android.content.Context;
/**
 * 
 * @author Anurag
 */
public class ZipManager {
	
	private ArrayList<Item> list;
	private ZipEntry entry;
	private Enumeration<? extends ZipEntry> zList;
	private String zipPath;
	int len = 0;
	private String entryname;
	private Context ctx;
	
	public ZipManager(ZipFile file ,String pathToShow , Context context){
		// TODO Auto-generated constructor stub
		zList = file.entries();
		list = new ArrayList<Item>();
		zipPath = pathToShow;
		ctx = context;
	}

	/**
	 * 
	 * @param path
	 */
	public void setPath(String path){
		this.zipPath = path;
	}
	
	public ArrayList<Item> generateList(){
		while(zList.hasMoreElements()){
			entry = zList.nextElement();
			if(entry.isDirectory())
				continue;
			
			boolean added = false;
			entryname = entry.getName();
			if(zipPath.equalsIgnoreCase("/")){
				/**
				 * LIST FILES FROM THE PARENT PATH OF ZIP FILE....
				 */
				len = list.size();
				
				String name = entryname;
				if(name.startsWith("/"))
					name = name.substring(1, name.length());
				name = eliminateSlash(name);
				for(int i=0;i<len;++i){
					if(list.get(i).z_getName().equalsIgnoreCase(name)){
						added = true;
						break;
					}
				}
				if(!added){
					list.add(new Item("", name, entry.getName(), entry.getSize(), ctx , entry) );
				}				
			}else{
				/**
				 * LISTING HAS TO BE DONE FROM THE PROVIDED PATH....
				 */
				
				/*
				 * THE EXCEPTION WON'T EFFECT THE LISTING OF FILES FROM THE ARCHIVE....
				 */
				try{
					len = list.size();
					String name = entryname.substring(zipPath.length()+1, entryname.length());
					name = eliminateSlash(name);
					if(len>0){					
						for(int i=0;i<len;++i){
							if(list.get(i).z_getName().equalsIgnoreCase(name)){
								added = true;
								break;
							}
						}						
						if(!added && entryname.startsWith(zipPath)){
							list.add(new Item(zipPath, name, entry.getName(), entry.getSize(), ctx , entry));
						}
					}else{
						if(entryname.startsWith(zipPath)){
							list.add(new Item(zipPath, name, entry.getName(), entry.getSize(), ctx,entry));
						}
					}
				}catch(Exception e){
					
				}				
			}
		}
		sort();
		return list;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	private String eliminateSlash(String name){
		if(name.contains("/")){
			name = name.substring(0, name.lastIndexOf("/"));
			name = eliminateSlash(name);
		}		
		return name;
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
				boolean aisfolder =!a.is_z_File();
				boolean bisfolder = !b.is_z_File();
				if(aisfolder==bisfolder)
					return a.z_getName().compareToIgnoreCase(b.z_getName());
				else if(bisfolder)
					return 1;
				return -1;
			}
		};		
		Collections.sort(list, comp);
	}
}
