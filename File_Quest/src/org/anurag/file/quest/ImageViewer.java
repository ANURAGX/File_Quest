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

package org.anurag.file.quest;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.anurag.dialogs.BluetoothChooser;
import org.anurag.dialogs.DeleteFiles;
import org.anurag.file.quest.SystemBarTintManager.SystemBarConfig;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author anurag
 *
 */
public class ImageViewer extends ActionBarActivity{

	private ImageView image;
	private Intent intent;
	private Toolbar bar;
	//PhotoViewAttacher attacher = new PhotoViewAttacher(image);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_viewer);		
		bar = (Toolbar) findViewById(R.id.toolbar_top);
		image = (ImageView) findViewById(R.id.viewer_Image);
		intent = getIntent();
		
		setSupportActionBar(bar);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Constants.COLOR_STYLE));
		getSupportActionBar().setIcon(R.drawable.image_icon_hd);
		getSupportActionBar().setTitle("  " + getSupportActionBar().getTitle());
		
		if(intent != null){
			final String path = intent.getData().getPath();
			File file = new File(path);
			if(file.exists()){
				getSupportActionBar().setTitle("  " + file.getName());
				//getting screen resolution...
				final DisplayMetrics metrices = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metrices);
				
				/**
				 * loading image in background thread....
				 */
				new AsyncTask<Void , Void , Void>() {
					Bitmap map = null;
					
					@Override
					protected void onPostExecute(Void result) {
						// TODO Auto-generated method stub
						super.onPostExecute(result);
						if(map != null){
							image.setImageBitmap(map);
							new PhotoViewAttacher(image);
							image.setVisibility(View.VISIBLE);
						}else{
							TextView view = (TextView) findViewById(R.id.img_msg);
							view.setText(R.string.failed_to_load_image);
						}
					}
					
					@Override
					protected Void doInBackground(Void... params) {
						// TODO Auto-generated method stub
						try {
							int width = metrices.widthPixels;
							int height = metrices.heightPixels;
							
							//if size of image is large then scaling to ti 480*720
							/*if(new File(path).length() > 1024){
								width = 480;
								width = 720;
							}*/
								
							map = createScaledBitmapFromStream(new FileInputStream(new File(path)),
									width,height);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}
				}.execute();				
			}
		}
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.image_viewer_menu, menu);
		return true;
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId()){
		case R.id.share_image:
			if(intent != null)
				new BluetoothChooser(ImageViewer.this, intent.getData().getPath() , 
						Constants.size.x*8/9, null);
			break;
			
		case R.id.delete_image:
			if(intent != null){
				ArrayList<Item> ls = new ArrayList<Item>();
				ls.add(new Item(new File(intent.getData().getPath()),
						null, null, null));
				
				new DeleteFiles(ImageViewer.this,ls , null);
				
				//ImageViewer.this.finish();				
			}
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onResume(){
		super.onResume();
		init_system_ui();
		
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				if(intent.getAction().equals("DELETE_IMAGE")){
					ImageViewer.this.finish();
				}
			}
		}, new IntentFilter("DELETE_IMAGE"));
	}
	

	/**
	 * restyles the system UI like status bar or navigation bar if present....
	 */
	private void init_system_ui() {
		// TODO Auto-generated method stub
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
			return;
		SystemBarTintManager tint = new SystemBarTintManager(ImageViewer.this);
		tint.setStatusBarTintEnabled(true);
		tint.setStatusBarTintColor(Constants.COLOR_STYLE);
		SystemBarConfig conf = tint.getConfig();
		boolean hasNavBar = conf.hasNavigtionBar();
		if(hasNavBar){
			tint.setNavigationBarTintEnabled(true);
			tint.setNavigationBarTintColor(Constants.COLOR_STYLE);
		}
		LinearLayout main = (LinearLayout) findViewById(R.id.main);
		main.setPadding(0, getStatusBarHeight(), 0, hasNavBar ? getNavigationBarHeight() :0);
		main.setBackgroundColor(Constants.COLOR_STYLE);
	}
	
	/**
	 * 
	 * @return height of status bar....
	 */
	private int getStatusBarHeight(){
		int res = 0;
		int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if(resId > 0)
			res = getResources().getDimensionPixelSize(resId);
		return res;
	}
	
	/**
	 * 
	 * @return the height of navigation bar....
	 */
	private int getNavigationBarHeight(){
		int res = 0;
		int resId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
		if(resId > 0)
			res = getResources().getDimensionPixelSize(resId);
		return res;
	}
	
	/**
	 * 
	 * @param s
	 * @param minimumDesiredBitmapWidth
	 * @param minimumDesiredBitmapHeight
	 * @return
	 */
	protected Bitmap createScaledBitmapFromStream( InputStream s, int minimumDesiredBitmapWidth, int minimumDesiredBitmapHeight ) {
	    final BufferedInputStream is = new BufferedInputStream(s, 32*1024);

	    try {
	        final Options decodeBitmapOptions = new Options();
	        // For further memory savings, you may want to consider using this option
	        // decodeBitmapOptions.inPreferredConfig = Config.RGB_565; // Uses 2-bytes instead of default 4 per pixel

	        if( minimumDesiredBitmapWidth >0 && minimumDesiredBitmapHeight >0 ) {
	            final Options decodeBoundsOptions = new Options();
	            decodeBoundsOptions.inJustDecodeBounds = true;
	            is.mark(32*1024); // 32k is probably overkill, but 8k is insufficient for some jpgs
	            BitmapFactory.decodeStream(is,null,decodeBoundsOptions);
	            is.reset();

	            final int originalWidth = decodeBoundsOptions.outWidth;
	            final int originalHeight = decodeBoundsOptions.outHeight;

	            // inSampleSize prefers multiples of 2, but we prefer to prioritize memory savings
	            decodeBitmapOptions.inSampleSize= Math.max(1,Math.min(originalWidth / minimumDesiredBitmapWidth, originalHeight / minimumDesiredBitmapHeight));

	        }

	        return BitmapFactory.decodeStream(is,null,decodeBitmapOptions);

	    } catch( IOException e ) {
	        throw new RuntimeException(e); // this shouldn't happen
	    }finally {
	       try {
	           is.close();
	       } catch( IOException ignored ) {}
	    }
	}
	
}
