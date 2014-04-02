/**
 * Copyright(c) 2013 ANURAG 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * anurag.dev1512@gmail.com
 *
 */
public

package org.ultimate.menuItems;


import org.anurag.file.quest.R;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SystemFlash extends Activity{
	private TextView popupTitle;
	private TextView popupMessage;
	private Button btn1;
	private Button btn2;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams params = this.getWindow().getAttributes();
		WindowManager w = getWindowManager();
		Point size = new Point();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			w.getDefaultDisplay().getSize(size);
			//params.alpha = 0.8f;
			params.height = size.y*2/3;
			params.width = size.x*4/5;
		}
		setContentView(R.layout.popup_dialog);
		btn1 = (Button)findViewById(R.id.popupOk);
		btn2 = (Button)findViewById(R.id.popupCancel);
		popupTitle = (TextView)findViewById(R.id.popupTitle);
		//bar = (ProgressBar)findViewById(R.id.popupProgress);
		popupMessage = (TextView)findViewById(R.id.textMessage);
		ImageView V = (ImageView)findViewById(R.id.popupImage);
		V.setBackgroundResource(R.drawable.ic_launcher_both);
		
		//bar.setVisibility(View.GONE);
		popupTitle.setText("  Error");
		popupMessage.setText("You Are Creating Zip Of A System App,Since The System Apps Are " +
				"Customized For A Particular Device And Its Useless To Flash Its Zip On " +
				"Other Device As It May Harm The Host Device");
		btn2.setVisibility(View.GONE);
		btn1.setText("Ok");
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	
}
