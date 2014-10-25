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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * THIS CLASS IS BASICALLY TO SHOW THE PREVIEW TO THE MP3,OR ANY ANDROID SUPPORTED AUDIO
 * FILE...
 * 
 * THIS IS NOT A FULL FLEDGED MUSIC PLAYER ACTIVITY....
 * @author Anurag
 *
 */
@SuppressLint("DefaultLocale")
public class FileQuestPlayer extends Activity{

	private TextView total_time;
	private TextView current_time;
	private boolean playing;
	private MediaPlayer player;
	private SeekBar seekbar;
	private MediaMetadataRetriever retreive;
	
	@SuppressLint("HandlerLeak")
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
		
		
		total_time = (TextView)dialog.findViewById(R.id.time_total);
		current_time = (TextView)dialog.findViewById(R.id.time_current);
		
		TextView albumname = (TextView)dialog.findViewById(R.id.albumName);
		TextView artist = (TextView)dialog.findViewById(R.id.artistName);
		ImageView album = (ImageView)dialog.findViewById(R.id.albumart);
		try {
			player = new MediaPlayer();
			player.setDataSource(FileQuestPlayer.this, intent.getData());
			player.prepare();
			
			//if other players are running asking them to close....
			Intent i = new Intent("com.android.music.musicservicecommand");
		    i.putExtra("command", "pause");
		    sendBroadcast(i);
			
			player.start();
			current_time.setText(generateTime(0000));
			total_time.setText(generateTime(player.getDuration()));
			player.setLooping(true);
			seekbar.setMax(player.getDuration());
			retreive = new MediaMetadataRetriever();
			retreive.setDataSource(FileQuestPlayer.this, intent.getData());
			
			/**
			 * extracting mediametadata like album name,artist,album art...
			 */
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			player = null;
			Toast.makeText(FileQuestPlayer.this, R.string.failedtoplay , Toast.LENGTH_SHORT).show();
			FileQuestPlayer.this.finish();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			player = null;
			Toast.makeText(FileQuestPlayer.this, R.string.failedtoplay , Toast.LENGTH_SHORT).show();
			FileQuestPlayer.this.finish();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			player = null;
			Toast.makeText(FileQuestPlayer.this, R.string.failedtoplay , Toast.LENGTH_SHORT).show();
			FileQuestPlayer.this.finish();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			player = null;
			Toast.makeText(FileQuestPlayer.this, R.string.failedtoplay , Toast.LENGTH_SHORT).show();
			FileQuestPlayer.this.finish();
		}
		
		if(player!=null){
			
			//setting the album art....
			try{
				byte art[] = retreive.getEmbeddedPicture();
				Bitmap img = BitmapFactory.decodeByteArray(art, 0, art.length);
				album.setImageBitmap(img);
			}catch(Exception e){
				album.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_albumart));
			}
			
			//setting the album name...
			try{
				String name = retreive.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
				if(name.length() != 0)
					albumname.setText(retreive.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
				else
					albumname.setText(R.string.notavailable);
			}catch(Exception e){
				albumname.setText(R.string.notavailable);
			}
			
			//setting the artist name...
			try{
				String name = retreive.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
				if(name.length() != 0)
					artist.setText(retreive.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
				else
					albumname.setText(R.string.notavailable);
			}catch(Exception e){
				albumname.setText(R.string.notavailable);
			}
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
				if(fromUser){
					player.seekTo(progress);
					current_time.setText(generateTime(progress));
				}	
			}
		});
		
		dialog.getWindow().getAttributes().width = size.x*8/9;
		if(player!=null)
			dialog.show();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				try{
					player.release();
					playing = false;
				}catch(Exception e){
					
				}
				FileQuestPlayer.this.finish();
			}
		});
		
		if(player != null){
			final Handler handle = new Handler(){
				@Override
				public void handleMessage(Message msg){
					long time = player.getCurrentPosition();
					current_time.setText(generateTime(time));
					seekbar.setProgress((int)time);
				}
			};
			handle.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(playing){
						handle.sendEmptyMessage(0);
						handle.postDelayed(this, 1000);
					}
				}
			}, 1000);
		}
			//play.execute();
		else{
			FileQuestPlayer.this.finish();
		}		
	}	

	private String generateTime(long time) {
		int totalSeconds = (int) (time / 1000);
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;
		return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
	}
	
}
