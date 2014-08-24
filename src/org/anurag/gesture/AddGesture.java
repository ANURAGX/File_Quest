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

import org.anurag.file.quest.R;

import android.app.Dialog;
import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddGesture {
	
	GestureOverlayView gesture;
	Gesture pattern;
	GestureLibrary library;
	public AddGesture(final Context context , int width,int height , final String filePath) {
		// TODO Auto-generated constructor stub
		final Dialog dialog = new Dialog(context, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.create_gesture);
		
		gesture = (GestureOverlayView)dialog.findViewById(R.id.gestures_overlay);
		dialog.setCancelable(true);
		dialog.getWindow().getAttributes().width = width;
		dialog.getWindow().getAttributes().height = height;		
		dialog.getWindow().getAttributes().alpha = 0.85f;
		dialog.show();
		
		final Button save = (Button)dialog.findViewById(R.id.done);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				File file = new File(Environment.getExternalStorageDirectory().getPath()+"/File Quest");
				if(!file.exists())
					file.mkdirs();
				file = new File(file,".gesture");
				if(!file.exists())
					try {
						file.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				library = GestureLibraries.fromFile(file.getAbsolutePath());
				library.load();
				library.addGesture(filePath, pattern);
				library.save();
				Toast.makeText(context, context.getString(R.string.gesturesaved), Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				}
		}); 
		
		Button discard = (Button)dialog.findViewById(R.id.discard);
		discard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();				
			}
		});
		
		
		
		gesture.addOnGestureListener(new GestureOverlayView.OnGestureListener() {
			@Override
			public void onGestureStarted(GestureOverlayView arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				save.setEnabled(false);
				pattern = null;
			}
			
			@Override
			public void onGestureEnded(GestureOverlayView arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				pattern = arg0.getGesture();
				if(pattern.getLength()<120.f){
					arg0.clear(false);
				}
				save.setEnabled(true);
			}
			
			@Override
			public void onGestureCancelled(GestureOverlayView arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGesture(GestureOverlayView arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
