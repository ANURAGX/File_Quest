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
 *                             anuraxsharma1512@gmail.com
 *
 */

package org.anurag.compress;

import java.util.ArrayList;
import org.anurag.file.quest.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author ANURAG
 */
public class SZipAdapter extends BaseAdapter{

	Context ctx;
	LayoutInflater inflater;
	ArrayList<SZipObj> list;
	
	public SZipAdapter(Context cont , ArrayList<SZipObj> obj) {
		// TODO Auto-generated constructor stub
		list = obj;
		ctx = cont;
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		return arg0;
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
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		SZipObj t = list.get(arg0);
		Holder h = new Holder(); 
		if(convertView==null){
			convertView = inflater.inflate(R.layout.row_list_app, arg2 , false);
			h.icon = (ImageView)convertView.findViewById(R.id.fileIcon);
			h.fName = (TextView)convertView.findViewById(R.id.fileName);
			h.fType = (TextView)convertView.findViewById(R.id.fileType);
			h.fSize = (TextView)convertView.findViewById(R.id.fileSize);
			h.box = (CheckBox)convertView.findViewById(R.id.checkbox);
			h.box.setVisibility(View.GONE);
			if(!t.isFile())
				h.fSize.setVisibility(View.GONE);
			convertView.setTag(h);
		}else
			h = (Holder) convertView.getTag();
		
			h.fName.setText(t.getName());
		//	h.icon.setImageDrawable(t.getIcon());
		//	h.fSize.setText(t.getSize());
		//	h.fType.setText(t.getType());
		
		return convertView;
	}

}
