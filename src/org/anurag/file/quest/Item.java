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
	
	public Item(File fi) {
		// TODO Auto-generated constructor stub
		file = fi;
		parent = file.getParent();
		path = file.getPath();
		name = file.getName();
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
}
