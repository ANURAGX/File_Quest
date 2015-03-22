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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.zip.GZIPInputStream;

import org.anurag.file.quest.Item;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import android.content.Context;


/**
 * 
 * @author ANURAG
 *
 */
public class TarManager {
	
	private ArrayList<Item> list;
	private Context ctx;
	private String path;
	private TarArchiveInputStream tar;
	private File f;
	
	/**
	 * 
	 * @param file
	 * @param pathToShow
	 * @param ct
	 * @throws IOException
	 */
	public TarManager(File file,String pathToShow , Context ct) throws IOException {
		// TODO Auto-generated constructor stub
		f = file;
		path = pathToShow;
		ctx = ct;
	}
	
	
	public void setPath(String p){
		this.path = p;
	}
	
	/**
	 * 
	 * THE DIRECTORY ENTRIES CANNOT BE SKIPPED LIKE WE SKIPPED IN
	 * RAR OR ZIP MANAGER....
	 * 
	 * @return
	 * @throws IOException
	 */
	public ArrayList<Item> generateList(){
		
		list = new ArrayList<Item>();
		TarArchiveEntry entry;
		
		try {
			if(f.getName().endsWith(".tar.gz"))
				tar = new TarArchiveInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(f))));

			else if(f.getName().endsWith(".tar.bz2")||f.getName().endsWith(".TAR.BZ2"))
				tar = new TarArchiveInputStream(new BZip2CompressorInputStream(new BufferedInputStream(new FileInputStream(f))));
			else
				tar = new TarArchiveInputStream(new BufferedInputStream(new FileInputStream(f)));
			
			
			while((entry=tar.getNextTarEntry())!=null){
				
				boolean added = false;
				int len = list.size();
				String name = entry.getName();
				if(name.startsWith("/"))
					name = name.substring(1, name.length());
				if(entry.isDirectory())
					name = name.substring(0, name.length()-1);
				
				if(path.equalsIgnoreCase("/")){
					while(name.contains("/"))
						name = name.substring(0,name.lastIndexOf("/"));
					
					for(int i=0;i<len;++i){
						if(list.get(i).getName().equalsIgnoreCase(name)){
								added = true;
							    break;
						}
					}
					if(!added&&!name.equalsIgnoreCase(""))
						list.add(new Item(entry, name, "", ctx));
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
								list.add(new Item(entry, name, path, ctx));
						}else if(entry.getName().startsWith(path))
							list.add(new Item(entry, name, path, ctx));
					}catch(Exception e){
						
					}
				}
			}	
			tar.close();
			sort();
			return list;
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		
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
