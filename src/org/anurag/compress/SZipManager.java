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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import android.content.Context;

/**
 * 
 * @author ANURAG
 *
 */
public class SZipManager {
	
	Context ctx;
	String path;
	SevenZFile zFile;
	ArrayList<SZipObj> list;
	
	/**
	 * 
	 * @param file
	 * @param pathToShow
	 * @param ct
	 * @throws IOException
	 */
	public SZipManager(File file , String pathToShow , Context ct) throws IOException {
		// TODO Auto-generated constructor stub
		path = pathToShow;
		ctx = ct;
		zFile = new SevenZFile(file);
	}
	
	/**
	 * GENERATES THE LIST OF FILES INSIDE OF 7Z ARCHIVE...
	 * @return
	 * @throws IOException
	 */
	public ArrayList<SZipObj> generateList() throws IOException{
		list = new ArrayList<SZipObj>();
		if(zFile!=null){
			SevenZArchiveEntry entry;
			while((entry=zFile.getNextEntry())!=null){
				if(entry.isDirectory())
					continue;
				int len = list.size();
				boolean added = false;
				String name = entry.getName();
				if(path.equalsIgnoreCase("/")){//listing entries in root of archive....
					while(name.contains("/"))
						name = name.substring(0, name.lastIndexOf("/"));
					for(int i=0;i<len;++i)
						if(list.get(i).getName().equalsIgnoreCase(name)){
							added = true;
							break;
						}
					if(!added)
						list.add(new SZipObj(entry, name, "", ctx));
				}else{
					
				}
			}
		}
		return list;
	}

}
