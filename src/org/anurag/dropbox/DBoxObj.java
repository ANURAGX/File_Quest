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

package org.anurag.dropbox;

import android.content.Context;
import android.graphics.drawable.Drawable;


/**
 * 
 * @author Anurag
 * 
 */
public class DBoxObj {
	
	String name;
	String type;
	String path;
	Drawable icon;
	Context ctx;
	String size;
	boolean isFile;
	public DBoxObj(com.dropbox.client2.DropboxAPI.Entry entry , Context cont) {
		// TODO Auto-generated constructor stub
		this.ctx = cont;
		this.name = entry.fileName();
		this.type = entry.mimeType;
		this.path = entry.path;
		this.size = entry.size;
		this.isFile = !entry.isDir;
		this.icon = getIcon();
	}
	
	public Drawable getIcon() {
		// TODO Auto-generated method stub
		return this.icon;
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
	public String getSize(){
		return this.size;
	}
	
	public String getType(){
		return this.type;
	}
}
