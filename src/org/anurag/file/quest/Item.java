/**
 * Copyright(c) 2012-2014 DRAWNZER.ORG PROJECTS -> ANURAG
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

import android.graphics.drawable.Drawable;

public class Item {
	
	File file;
	String name;
	String path;
	String parent;
	Drawable icon;
	String size;
	String type;
	
	public Item(File fi , Drawable img , String typ , String si) {
		// TODO Auto-generated constructor stub
		this.file = fi;
		this.parent = file.getParent();
		this.path = file.getPath();
		this.name = file.getName();
		this.icon = img;
		this.type = typ;
		this.size = si;
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
	public String getParent(){
		return this.parent;
	}

	/**
	 * 
	 * @return
	 */
	public String getSize(){
		return this.size;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getType(){
		return this.type;
	}
	
	/**
	 * 
	 * @return
	 */
	public Drawable getIcon(){
		return this.icon;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean canRead(){
		return file.canRead();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean canWrite(){
		return file.canWrite();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean canExecute(){
		return file.canExecute();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isDirectory(){
		return file.isDirectory();
	}
}
