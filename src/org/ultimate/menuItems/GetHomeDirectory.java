/**
 * Copyright(c) 2013 DRAWNZER.ORG PROJECTS -> ANURAG
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

package org.ultimate.menuItems;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("NewApi")
public class GetHomeDirectory {

	private Stack<File> stack;
	private File file ;
	private File[] list;
	private Button go;
	private TextView t;
	SharedPreferences.Editor ed;
	ImageView image;
	Context mContext;
	Dialog dialog;
	
	/**
	 * 
	 * @param context
	 * @param width
	 * @param edit ,if null means pass the selected path to extract files of an archive...
	 */
	public GetHomeDirectory(Context context,int width,SharedPreferences edit) {
		// TODO Auto-generated constructor stub
		mContext = context;
		dialog = new Dialog(mContext, Constants.DIALOG_STYLE);
		dialog.setContentView(R.layout.open_file_dialog);
		dialog.getWindow().getAttributes().width = width;
		if(edit !=null)
			ed = edit.edit();
		onCreate();
	}
	
	
	protected void onCreate() {
		// TODO Auto-generated method stub
		final ListView lv;
		Button ju = (Button)dialog.findViewById(R.id.justOnce);
		Button on = (Button)dialog.findViewById(R.id.always);
		file = new File(Environment.getExternalStorageDirectory().getPath());
		stack = new Stack<File>();
		stack.push(file);
		t = (TextView)dialog.findViewById(R.id.open);
		t.setText(mContext.getString(R.string.choosehome));
		lv = (ListView)dialog.findViewById(R.id.open_list);
		image= (ImageView)dialog.findViewById(R.id.getImage);
		
		image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.task));
		list = file.listFiles();
		lv.setAdapter(new Adapter(mContext, R.layout.row_list_2, list));
		go = (Button)dialog.findViewById(R.id.goBack);
		go.setVisibility(View.GONE);
		
		lv.setSelector(R.drawable.button_click);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,long id) {
				// TODO Auto-generated method stub
				file = list[pos];
				if(file.isDirectory() && file.canRead()){
					go.setVisibility(View.VISIBLE);
					stack.push(file);
					list = file.listFiles();
					t.setText(file.getPath());
					lv.setAdapter(new Adapter(mContext, R.layout.row_list_2, list));
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
		});
		dialog.show();
		
		ju.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(ed!=null){
					ed.putString("HOME_DIRECTORY", file.getAbsolutePath());
					ed.commit();
					Toast.makeText(mContext, mContext.getString(R.string.homeset),Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}else{
					Intent it = new Intent("FQ_EXTRACT_PATH");
					it.putExtra("extract_path", file.getAbsolutePath());
					mContext.sendBroadcast(it);
					dialog.dismiss();
				}				
			}
		});
		
		on.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}
	
	private class ItemHolder{
		ImageView Icon;
		TextView Name;
	}
	
	public class Adapter extends ArrayAdapter<File>{
		public Adapter(Context context, int textViewResourceId,	File[] objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
			list = objects;
			Arrays.sort(list,alphaFolderFirst);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			ItemHolder item = new ItemHolder();
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
				item.Name.setText(list[position].getName());
			return convertView;
		}
	}

	

	
	
	/**
	 * SORTS THE FILE[] ALPHABETICALLY WITH HAVING FOLDERS FIRST
	 */
	private final static Comparator<File> alphaFolderFirst = new Comparator<File>() {
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
	}; 
}


