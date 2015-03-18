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

package org.anurag.dialogs;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;

/**
 * 
 * This class gets the folder location(like browsing a location in windows) for moving
 * the files
 * 
 * @author anurag
 *
 */
public class GetMoveLocation {

	private Stack<File> stack;
	private File file ;
	private File[] list;
	private Context mContext;
	private ArrayList<Item> arrList;
	
	public GetMoveLocation(Context context,final ConcurrentHashMap<String, Item> itemList,final ConcurrentHashMap<String, String> keys) {
		// TODO Auto-generated constructor stub
		mContext = context;
		arrList = new ArrayList<Item>();
	
		LayoutInflater inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inf.inflate(R.layout.list_view_hd, null , false);
		view.setPadding(10, 0, 0, 0);
		final ListView ls = (ListView)view.findViewById(R.id.list_view_hd);
		
		new MaterialDialog.Builder(mContext)
		.title(R.string.movealltoonelocation)
		.negativeText(R.string.dismiss)
		.positiveText(R.string.ok)
		.neutralText(R.string.goback)
		.callback(new ButtonCallback() {
						
			@Override
			public void onNeutral(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onNeutral(dialog);
				if(stack.size() > 1){
					stack.pop();
					file = stack.peek();
					list = file.listFiles();
					
					Arrays.sort(list, new Comparator<File>() {
						@Override
						public int compare(File a, File b) {
							boolean aIsFolder = a.isDirectory();
							boolean bIsFolder = b.isDirectory();
							if(bIsFolder == aIsFolder )
								return a.getName().compareToIgnoreCase(b.getName());
							else if(bIsFolder)
								return 1;
							return -1;
						}
					});
					ls.setAdapter(new Adapter());
				}
			}

			@Override
			public void onPositive(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onPositive(dialog);
				
				int len = itemList.size();
				for(int i = 0 ; i < len ; ++i){
					try{
						arrList.add(itemList.get(keys.get(""+i)));
					}catch(NullPointerException e){
						continue;
					}
				}				
				dialog.dismiss();
				new CopyDialog(mContext, arrList, file.getAbsolutePath(), true);
			}

			@Override
			public void onNegative(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onNegative(dialog);
				dialog.dismiss();
			}			
		})
		.autoDismiss(false)
		.customView(view, false)
		.show();
		
		file = new File(Environment.getExternalStorageDirectory().getPath());
		stack = new Stack<File>();
		stack.push(file);
		list = file.listFiles();
		
		Arrays.sort(list, new Comparator<File>() {
			@Override
			public int compare(File a, File b) {
				boolean aIsFolder = a.isDirectory();
				boolean bIsFolder = b.isDirectory();
				if(bIsFolder == aIsFolder )
					return a.getName().compareToIgnoreCase(b.getName());
				else if(bIsFolder)
					return 1;
				return -1;
			}
		});
		
		
		
		ls.setAdapter(new Adapter());
		
		ls.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				file = list[arg2];
				if(file.isDirectory() && file.canRead()){
					stack.push(file);
					list = file.listFiles();
					
					Arrays.sort(list, new Comparator<File>() {
						@Override
						public int compare(File a, File b) {
							boolean aIsFolder = a.isDirectory();
							boolean bIsFolder = b.isDirectory();
							if(bIsFolder == aIsFolder )
								return a.getName().compareToIgnoreCase(b.getName());
							else if(bIsFolder)
								return 1;
							return -1;
						}
					});
					ls.setAdapter(new Adapter());
				}
			}
		});		
	}
	
	
	protected void onCreate() {
		// TODO Auto-generated method stub
		final ListView lv;
		//Button ju = (Button)dialog.findViewById(R.id.justOnce);
		//Button on = (Button)dialog.findViewById(R.id.always);
		
		/*lv.setAdapter(new Adapter(mContext, R.layout.row_list_2, list));
		//go = (Button)dialog.findViewById(R.id.goBack);
		go.setVisibility(View.GONE);
		
		lv.setSelector(R.drawable.button_click);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,long id) {
				// TODO Auto-generated method stub
				file = list[pos];
				if(file.isDirectory() && file.canRead()){
					go.setVisibility(View.VISIBLE);
					
				}
			}
		});
		
		go.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				stack.pop();
				file = stack.peek();
				t.setText(file.getPath());
				lv.setAdapter(new Adapter(mContext , R.layout.row_list_2,file.listFiles()));
				if(stack.size()<2)
					go.setVisibility(View.GONE);
			}
		});*/
		
		/*ju.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				{
					dialog.dismiss();
					
					new MultipleCopyDialog(mContext, arrList, window_size, file.getAbsolutePath(), true);
				}				
			}
		});
		*/
	}
	
	private class ItemHolder{
		ImageView Icon;
		TextView Name;
	}
	
	private class Adapter extends BaseAdapter{
		
		public Adapter() {
			// TODO Auto-generated constructor stub
		}		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			ItemHolder item = new ItemHolder();
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_list_2, parent , false);
				item.Icon = (ImageView)convertView.findViewById(R.id.iconImage2);
				item.Name = (TextView)convertView.findViewById(R.id.directoryName2);
				convertView.setTag(item);
			}else
				item = (ItemHolder)convertView.getTag();
				if(list[position].isDirectory())
					item.Icon.setBackgroundResource(Constants.FOLDERS[Constants.FOLDER_ICON]);
				else
					item.Icon.setBackgroundResource(R.drawable.unknown_icon_hd);
				
				item.Name.setTextColor(Color.BLACK);
				item.Name.setText(list[position].getName());
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
	
}


