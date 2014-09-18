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

package org.anurag.settings;

import org.anurag.file.quest.FileQuest;
import org.anurag.file.quest.R;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AdjusTranDialog {
	
	Context context;
	String[] list;
	int pos;
	public AdjusTranDialog(final Context ctx , int width ,final  SharedPreferences.Editor edit) {
		// TODO Auto-generated constructor stub
		final Dialog dialog = new Dialog(ctx, R.style.custom_dialog_theme);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.launch_file);
		dialog.getWindow().getAttributes().width = width;
		context = ctx;
		pos = -1;
		list = new String[5];
		list[0] = ctx.getString(R.string.opaque60);
		list[1] = ctx.getString(R.string.opaque70);
		list[2] = ctx.getString(R.string.opaque80);
		list[3] = ctx.getString(R.string.opaque90);
		list[4] = ctx.getString(R.string.opaque100);
		
		//dialog image....
		ImageView img = (ImageView)dialog.findViewById(R.id.launchImage);
		img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_appreance));
		
		//dialog title....
		TextView title = (TextView)dialog.findViewById(R.id.open);
		title.setText(ctx.getString(R.string.adjusttrans));
		
		//Buttons....
		Button btn = (Button)dialog.findViewById(R.id.justOnce);
		btn.setVisibility(View.GONE);
		btn = (Button)dialog.findViewById(R.id.always);		
		btn.setText(ctx.getString(R.string.ok));
		//List view for items...
		ListView ls = (ListView)dialog.findViewById(R.id.launch_list);
		ls.setSelector(R.drawable.blue_botton_2);
		ls.setAdapter(new adapter());
		ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				pos = arg2;
			}
		});
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pos == -1)//no item selected....
					Toast.makeText(ctx, ctx.getString(R.string.makeaselection), Toast.LENGTH_SHORT).show();
				else{
					FileQuest.TRANSPRA_LEVEL = Float.parseFloat(pos+"");
					edit.putFloat("TRANSPRA_LEVEL", FileQuest.TRANSPRA_LEVEL);
					edit.commit();
					Toast.makeText(ctx, ctx.getString(R.string.settingsapplied), Toast.LENGTH_SHORT).show();
					Settings.settingsChanged = true;
					dialog.dismiss();
				}
			}
		});
		dialog.show();		
	}
	
	class adapter extends BaseAdapter{

		public adapter() {
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		class grp{
			ImageView img;
			TextView txt;
		}
		
		@Override
		public View getView(int arg0, View convert, ViewGroup arg2) {
			// TODO Auto-generated method stub
			grp g = new grp();
			float po = 0;
			if(convert == null){
				LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convert = inf.inflate(R.layout.row_list_2, arg2,false);
				g.img = (ImageView)convert.findViewById(R.id.iconImage2);
				g.txt = (TextView)convert.findViewById(R.id.directoryName2);
				convert.setTag(g);
			}else
				g = (grp) convert.getTag();
			g.txt.setText(list[arg0]);
			
			if(arg0 == 0)
				po = 0.6f;
			else if(arg0 == 1)
				po = 0.7f;
			else if(arg0 == 2)
				po = 0.8f;
			else if(arg0 == 3)
				po = 0.9f;
			else if(arg0 == 4)
				po = 1.0f;
			
			if(FileQuest.TRANSPRA_LEVEL == po)
				g.img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				g.img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_appreance));
			return convert;
		}
		
	}

}
