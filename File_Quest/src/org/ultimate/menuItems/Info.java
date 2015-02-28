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
 *                             anuraxsharma1512@gmail.com
 *
 */

package org.ultimate.menuItems;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.R;

import android.app.Dialog;
import android.content.Context;



public class Info{

	Context mContext; 
	Dialog dialog; 
	
	public Info(Context context,int width) {
		// TODO Auto-generated constructor stub
		mContext = context;
		dialog = new Dialog(mContext, Constants.DIALOG_STYLE);
		dialog.setContentView(R.layout.info_layout);
		dialog.setCancelable(true);
		dialog.getWindow().getAttributes().width = width;
		dialog.show();
	}
	
	public void onCreate() {
		// TODO Auto-generated method stub
		
		//params.alpha = 0.8f;
		//setContentView(R.layout.info_layout);
		
	}
}
