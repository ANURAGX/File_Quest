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


import java.util.ArrayList;
import java.util.Enumeration;

import android.content.Context;

import com.adarshr.raroscope.RAREntry;
import com.adarshr.raroscope.RARFile;



/**
 * 
 * @author Anurag
 *
 */
public class RarManager {
	
	ArrayList<RarObj> list;
	Context ctx;
	String path;
	Enumeration<RAREntry> entryList;
	public RarManager(RARFile rarfile, String pathToShow , Context context) {
		// TODO Auto-generated constructor stub
		list = new ArrayList<RarObj>();
		path = pathToShow;
		ctx = context;
		entryList = rarfile.entries();
	}
	
	public ArrayList<RarObj> generateList(){
		while(entryList.hasMoreElements()){
			RAREntry entry = entryList.nextElement();
			if(entry.isDirectory())
				continue;
			int len = list.size();
			boolean added = false;
			String name = entry.getName();
			while(name.contains("\\"))
				name = name.substring(0, name.lastIndexOf("\\"));
			if(path.equalsIgnoreCase("/")){
				for(int i = 0;i<len;++i){
					if(list.get(i).getFileName().equalsIgnoreCase(name)){
						added = true;
						break;
					}
				}
				if(!added)
					list.add(new RarObj(entry, name, "",ctx));
				
			}
		}
		return list;
	}

}
