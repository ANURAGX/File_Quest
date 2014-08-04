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

package org.anurag.compress;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import android.content.Context;


/**
 * 
 * @author ANURAG
 *
 */
public class TarManager {
	
	ArrayList<TarObj> list;
	Context ctx;
	String path;
	TarArchiveInputStream tar;
	List<TarArchiveEntry> ls;
	public TarManager(File file,String pathToShow , Context ct) throws FileNotFoundException {
		// TODO Auto-generated constructor stub
		tar = new TarArchiveInputStream(new BufferedInputStream(new FileInputStream(file)));
		path = pathToShow;
		ctx = ct;
	}
	
	public ArrayList<TarObj> generateList() throws IOException{
		list = new ArrayList<TarObj>();
		TarArchiveEntry entry;
		while((entry=tar.getNextTarEntry())!=null){
			if(entry.isDirectory())
				continue;
			boolean added = false;
			int len = list.size();
			String name = entry.getName();
			if(name.startsWith("/"))
				name = name.substring(1, name.length());
			
			if(path.equalsIgnoreCase("/")){
				while(name.contains("/"))
					name = name.substring(0,name.lastIndexOf("/"));
				for(int i=0;i<len;++i){
					if(list.get(i).getName().equalsIgnoreCase(name)){
							added = true;
						break;
					}
				}
				if(!added)
					list.add(new TarObj(entry, name, "", ctx));
			}else{
				try{
					name = name.substring(path.length()+1, name.length());
					while(name.contains("/"))
						name = name.substring(0, name.lastIndexOf("/"));
					if(len>0){
						for(int i=0;i<len;++i)
							if(list.get(i).getName().equalsIgnoreCase(name)){
								added = true;
								break;
							}
						if(!added && entry.getName().startsWith(path))
							list.add(new TarObj(entry, name, path, ctx));
					}else if(entry.getName().startsWith(path))
						list.add(new TarObj(entry, name, path, ctx));
				}catch(Exception e){
					
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
		Comparator<TarObj> comp = new Comparator<TarObj>() {
			@Override
			public int compare(TarObj a, TarObj b) {
				// TODO Auto-generated method stub
				boolean aisfolder =!a.isFile();
				boolean bisfolder = !b.isFile();
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
