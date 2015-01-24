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

package org.anurag.gesture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

public class G_Open {
	
	GestureOverlayView gesture;
	GestureLibrary library;
	
	public G_Open(final Context context,int width,int height) {
		// TODO Auto-generated constructor stub
		final Dialog dialog = new Dialog(context, Constants.DIALOG_STYLE);
		dialog.setContentView(R.layout.create_gesture);
		
		gesture = (GestureOverlayView)dialog.findViewById(R.id.gestures_overlay);
		dialog.setCancelable(true);
		dialog.getWindow().getAttributes().width = width;
		dialog.getWindow().getAttributes().height = height;		
		dialog.getWindow().getAttributes().alpha = 0.85f;
		dialog.show();
		
		File file = new File(Environment.getExternalStorageDirectory().getPath()+"/File Quest");
		if(!file.exists())
			file.mkdirs();
		file = new File(file, ".gesture");
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		library = GestureLibraries.fromFile(file);
		library.load();
		Button save = (Button)dialog.findViewById(R.id.done);
		save.setVisibility(View.GONE);
		Button discard = (Button)dialog.findViewById(R.id.discard);
		discard.setVisibility(View.GONE);
		gesture.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
			@Override
			public void onGesturePerformed(GestureOverlayView arg0, Gesture arg1) {
				// TODO Auto-generated method stub
				ArrayList<Prediction> list = library.recognize(arg1);
				if(list.size()>0){
					if(list.get(0).score > 0.9f){
						String name = list.get(0).name;
						Intent intent = new Intent("FQ_G_OPEN");
						intent.putExtra("gesture_path", name);
						context.sendBroadcast(intent);
						dialog.dismiss();
					}					
				}
			}
		});
	}

}
