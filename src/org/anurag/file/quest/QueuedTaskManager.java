/**
 * Copyright(c) 2015 DRAWNZER.ORG PROJECTS -> ANURAG
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

package org.anurag.file.quest;

import java.util.HashMap;

import android.view.SubMenu;

/**
 * this class handles the queued tasks like copying,moving,etc....
 * 
 * @author anurag
 *
 */
public class QueuedTaskManager {
	
	private HashMap<String , QueuedTask> task_list;
	public int COPY_ID = 1;
	public int CUT_ID = 2;
	
	public QueuedTaskManager() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param task to be queued in the list....
	 */
	public void add_task(QueuedTask task ,String id){
		if(task_list == null){
			task_list = new HashMap<>();
		}	
		task_list.put(id, task);		
	}
	
	/**
	 * 
	 * @param id of the task to be removed....
	 */
	public void remove_task(String id){
		if(task_list == null){
			return;
		}	
		
		try{
			task_list.remove(id);
		}catch(Exception e){
			
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String getId(){
		if(task_list == null){
			return "";
		}
		return (""+task_list.size() + 1);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasTask(){
		if(task_list == null)
			return false;
		return task_list.size() > 0;
	}

	/**
	 * 
	 * @param subMenu
	 */
	public void prepareMenu(SubMenu subMenu) {
		// TODO Auto-generated method stub
		
	}		
}
