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

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
	private Context ctx;
	
	private PopupWindow popupWindow;
	private boolean isPopupShowing;
	
	public QueuedTaskManager(Context context) {
		// TODO Auto-generated constructor stub
		this.ctx = context;
		popupWindow = new PopupWindow(ctx);
		this.isPopupShowing = false;
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
			return (""+0);
		}
		return (""+task_list.size());
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
	 * @param view
	 */
	public void showQueuedPopupWindow(final View view ,final int gravity){
		ListView ls = new ListView(ctx);
		prepareList(ls);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setWidth(Constants.size.x*7/10);
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setContentView(ls);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.sdl_background));
		popupWindow.showAtLocation(view, gravity , Constants.size.x , 0);
		ls.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ImageButton btn = (ImageButton) arg1.findViewById(R.id.clear);
				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						int id = arg0.getId();
						remove_task(""+id);
						showQueuedPopupWindow(view, gravity);
					}
				});
				Toast.makeText(ctx, "DONE", Toast.LENGTH_SHORT).show();
			}
		});		
		isPopupShowing = true;
	}
	
	/**
	 * 
	 * @return true is popup windows is showing
	 */
	public boolean isPopupWindowShowing(){
		return this.isPopupShowing;
	}
	
	/**
	 * dismisses the popup window
	 */
	public void closePopupWindow(){
		isPopupShowing = false;
		popupWindow.dismiss();
	}
	
	/**
	 * 
	 * @param subMenu
	 */
	public void prepareList(ListView ls) {
		// TODO Auto-generated method stub
		if(task_list == null){
			return;
		}
		ls.setAdapter(new QueueTaskAdapter()); 
		ls.setSelector(R.drawable.list_selector_hd);
	}		

	class holder{
		ImageView icon;
		TextView Name;
		TextView file;
		TextView folder;
	}
		
	class QueueTaskAdapter extends BaseAdapter{
		
		private LayoutInflater inf;
		public QueueTaskAdapter() {
			// TODO Auto-generated constructor stub
			inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(task_list == null){
				return 0;
			}				
			return task_list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			if(task_list != null){
				task_list.get(""+arg0);
			}
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View Convert, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View view = inf.inflate(R.layout.queue_row_hd, arg2 , false);
			holder h = new holder();
			h.icon = (ImageView) view.findViewById(R.id.clear);
			h.Name = (TextView) view.findViewById(R.id.taskName);
			h.file = (TextView) view.findViewById(R.id.file);
			h.folder = (TextView) view.findViewById(R.id.folder);
			view.setTag(h);
			
			QueuedTask task = task_list.get(""+arg0);
			
			if(task != null){
				h.file.setText(task.file_count());
				h.folder.setText("   " + task.folder_count());
				String main = "<b>" + task.getTaskType() + "</b>  <font color=#222222>" + task.getItemList() + "</font>";
				h.Name.setText(Html.fromHtml(main));				
			}
			
			return view;
		}		
	}
}
