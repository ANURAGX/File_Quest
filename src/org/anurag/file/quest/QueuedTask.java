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
 *                             anurag.dev1512@gmail.com
 *
 */
package org.anurag.file.quest;

import android.util.SparseArray;

/**
 * 
 * @author anurag
 *
 */
public class QueuedTask {
	
	
	public int DELETE = 0;
	public int COPY = 1;
	public int CUT = 2;
	public int PASTE = 3;
	public int COMPRESS = 4;
	public int ADD_GESTURE = 5;
	
	public static int TASK_COUNTER;
	public static SparseArray<Item> queue_list;
	
	/**
	 * 
	 * @param item
	 * @param task_ID
	 */
	public QueuedTask(Item item , int task_ID) {
		// TODO Auto-generated constructor stub
		if(queue_list == null){
			queue_list = new SparseArray<Item>();
			TASK_COUNTER = -1;
		}	
		queue_list.put(++TASK_COUNTER, item);
	}
}
