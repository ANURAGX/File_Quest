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

import java.io.IOException;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;


/**
 * THIS CLASS IS BASICALLY TO SHOW THE PREVIEW TO THE MP3,OR ANY ANDROID SUPPORTED AUDIO
 * FILE...
 * 
 * THIS IS NOT A FULL FLEDGED MUSIC PLAYER ACTIVITY....
 * @author Anurag
 *
 */
public class FileQuestPlayer extends Activity{

	boolean playing;
	MediaPlayer player;
	SeekBar seekbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		playing = true;
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		Intent intent = getIntent();
		Dialog dialog = new Dialog(FileQuestPlayer.this, R.style.custom_dialog_theme);
		dialog.setCancelable(true); 
		dialog.setContentView(R.layout.file_quest_player);
		seekbar = (SeekBar)dialog.findViewById(R.id.seek);
		try {
			player = new MediaPlayer();
			player.setDataSource(FileQuestPlayer.this, intent.getData());
			player.prepare();
			player.start();
			seekbar.setMax(player.getDuration());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			player = null;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			player = null;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			player = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			player = null;
		}
		
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if(fromUser)
					player.seekTo(progress);
			}
		});
		
		dialog.getWindow().getAttributes().width = size.x*8/9;
		dialog.show();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				playing = false;
				player.release();
				FileQuestPlayer.this.finish();
			}
		});
		
		final Handler handle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				switch(msg.what){
					case 0 :
							seekbar.incrementProgressBy(1);
							break;
				}
			}
		};
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(playing){
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handle.sendEmptyMessage(0);
				}
			}
		});
		if(player!=null)
			thread.start();	
	}	
}
