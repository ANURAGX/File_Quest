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

import android.widget.TextView;

/**
 * this class handles the queued tasks like copying,moving,etc....
 * 
 * @author anurag
 *
 */
public class QueuedTaskManager {
	
	private static HashMap<String , QueuedTask> task_list;
	private static HashMap<String , TextView> action_views;
	
	public QueuedTaskManager() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param task to be queued in the list....
	 */
	public static void add_task(QueuedTask task ,String id){
		if(task_list == null){
			task_list = new HashMap<>();
			action_views = new HashMap<>();
		}	
		task_list.put(id, task);		
	}
	
	/**
	 * 
	 * @param id of the task to be removed....
	 */
	public static void remove_task(String id){
		if(task_list == null)
			return;
		
		try{
			task_list.remove(id);
		}catch(Exception e){
			
		}
	}
	
	private static void build_action_view(String id){
		
	}
}
