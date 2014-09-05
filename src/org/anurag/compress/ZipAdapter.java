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

import java.util.ArrayList;
import org.anurag.file.quest.Constants;
import org.anurag.file.quest.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ZipAdapter extends BaseAdapter{

	ArrayList<ZipObj> list;
	Context ctx;
	LayoutInflater inflater;
	ZipObj zFile;
	public ZipAdapter(ArrayList<ZipObj> object , Context context) {
		// TODO Auto-generated constructor stub
		list = object;
		ctx = context;
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	/**
	 * 
	 * @author Anurag
	 *
	 */
	class Holder{
		ImageView icon;
		TextView fName;
		TextView fType;
		TextView fSize;
		CheckBox box;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Holder h = new Holder(); 
		zFile = list.get(pos);
		if(convertView == null){
			convertView = inflater.inflate(R.layout.row_list_app, arg2 , false);
			h.icon = (ImageView)convertView.findViewById(R.id.fileIcon);
			h.fName = (TextView)convertView.findViewById(R.id.fileName);
			h.fType = (TextView)convertView.findViewById(R.id.fileType);
			h.fSize = (TextView)convertView.findViewById(R.id.fileSize);
			h.box = (CheckBox)convertView.findViewById(R.id.checkbox);
			h.box.setVisibility(View.GONE);
			convertView.setTag(h);
		}else
			h = (Holder) convertView.getTag();
		
		if(!zFile.isFile()){
			h.fSize.setVisibility(View.GONE);
			h.icon.setImageDrawable(ctx.getResources().getDrawable(Constants.FOLDERS[Constants.FOLDER_TYPE]));
		}else
			h.icon.setImageDrawable(zFile.getIcon());
		h.fName.setText(zFile.getName());
		h.fSize.setText(zFile.getSize());
		h.fType.setText(zFile.getFileType());
		
		return convertView;
	}

}
