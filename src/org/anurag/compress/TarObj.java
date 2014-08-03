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

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * 
 * @author ANURAG
 *
 */
public class TarObj {
	
	TarArchiveEntry ent;
	String name;
	String path;
	boolean isFile;
	Context ctx;
	Drawable icon;
	public TarObj(TarArchiveEntry entry , String fname , String pname,Context ct) {
		// TODO Auto-generated constructor stub
		this.ent = entry;
		this.name = fname;
		this.path = pname;
		this.ctx = ct;
	}

	/**
	 * 
	 * @return
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPath(){
		return this.path;
	}
	
	/**
	 * 
	 * @return
	 */
	public TarArchiveEntry getEntry(){
		return this.ent;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEntryName(){
		return this.ent.getName();
	}
	
	public boolean isFile(){
		return false;
	}
	
}
