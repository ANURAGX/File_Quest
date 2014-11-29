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

import android.app.Dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * 
 * @author Anurag....
 *
 */
public class CopyToCloud implements OnClickListener {

	private Item item;
	private Context context;
	private Dialog dialog;
	public CopyToCloud(Context ctx , Item itm) {
		// TODO Auto-generated constructor stub
		this.item = itm;
		this.context = ctx;
		dialog = new Dialog(ctx, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.copy_to_cloud);
		dialog.getWindow().getAttributes().width = FileQuest.size.x*8/9;
		
		
		dialog.show();
		LinearLayout drp = (LinearLayout)dialog.findViewById(R.id.cld_drp);
		LinearLayout sky = (LinearLayout)dialog.findViewById(R.id.cld_skydrv);
		LinearLayout cpy = (LinearLayout)dialog.findViewById(R.id.cld_copy);
		LinearLayout ggl = (LinearLayout)dialog.findViewById(R.id.cld_gogledrv);
		LinearLayout mdf = (LinearLayout)dialog.findViewById(R.id.cld_mdfire);
		LinearLayout sgr = (LinearLayout)dialog.findViewById(R.id.cld_sugar);
		
		drp.setOnClickListener(this);
		sky.setOnClickListener(this);
		cpy.setOnClickListener(this);
		ggl.setOnClickListener(this);
		mdf.setOnClickListener(this);
		sgr.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.cld_copy:
				
				break;
				
			case R.id.cld_drp:
				
				break;
				
			case R.id.cld_gogledrv:
				
				break;
				
			case R.id.cld_mdfire:
				
				break;
				
			case R.id.cld_skydrv:
				
				break;
				
			case R.id.cld_sugar:
				
				break;
		}
		dialog.dismiss();
		
	}
	
}
