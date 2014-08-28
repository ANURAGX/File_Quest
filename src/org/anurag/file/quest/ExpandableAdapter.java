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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableAdapter extends ArrayAdapter<String>{

	Context ctx;
	LayoutInflater INFLATER;
	int[] res = {R.drawable.ic_launcher_music,
			     R.drawable.ic_launcher_apk,
			     R.drawable.ic_launcher_ppt,
			     R.drawable.ic_launcher_images,
			     R.drawable.ic_launcher_video,
			     R.drawable.ic_launcher_archive_operation,
			     R.drawable.ic_launcher_unknown};
	String[] ls;
	public ExpandableAdapter(Context context, int resource,int textViewResourceId, String[] objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		ctx = context;
		ls = objects;
		INFLATER = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	class holder {
		ImageView img;
		TextView text;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		holder hold = new holder();
		if(convertView==null){
			convertView = INFLATER.inflate(R.layout.expandable_row_list, parent , false);
			hold.img = (ImageView)convertView.findViewById(R.id.expImage);
			hold.text = (TextView)convertView.findViewById(R.id.expandable_toggle_button);
			convertView.setTag(hold); 
		}else
			hold = (holder) convertView.getTag();
		hold.img.setImageDrawable(ctx.getResources().getDrawable(res[position]));
		hold.text.setText(ls[position]);
		return convertView;
	}
	
	

}
