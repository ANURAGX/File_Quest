package org.anurag.file.quest;

import java.io.File;

import org.ultimate.menuItems.BluetoothChooser;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ImageViewer extends Activity{

	
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_viewer);		
		intent= getIntent();
		if(intent!=null){
			final File file = new File(intent.getData().getPath());
			if(file.exists()){
				Drawable draw = Drawable.createFromPath(file.getAbsolutePath());
				if(draw!=null){
					TextView name = (TextView)findViewById(R.id.viewer_Name);
					name.setText(file.getName());
					ImageView image = (ImageView)findViewById(R.id.viewer_Image);
					image.setImageDrawable(draw);
					PhotoViewAttacher attacher = new PhotoViewAttacher(image);
					try{
						
						LinearLayout delete = (LinearLayout)findViewById(R.id.viewer_delete);
						delete.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								final Dialog dialog = new Dialog(ImageViewer.this,R.style.custom_dialog_theme);
								dialog.setContentView(R.layout.delete_files);
								dialog.getWindow().getAttributes().width = TaskerActivity.size.x*5/6;
								
								ImageView ic = (ImageView)dialog.findViewById(R.id.popupImage);
								ic.setBackgroundResource(R.drawable.ic_launcher_delete);
								
								TextView t = (TextView)dialog.findViewById(R.id.popupTitle);
								t.setText("Confirm Deletion of file?");
								
								final TextView msg = (TextView)dialog.findViewById(R.id.textMessage);
								msg.setText("Are you sure to delete the file :- " + file.getName());
								
								final Button can = (Button)dialog.findViewById(R.id.popupCancel);
								can.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View arg0) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
								
								final Button yes = (Button)dialog.findViewById(R.id.popupOk);
								yes.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										msg.setText("Please wait while deleting file");
										yes.setVisibility(View.GONE);
										can.setVisibility(View.GONE);
										if(file.delete()){
											Toast.makeText(getApplicationContext(), "File deleted successfully",
													Toast.LENGTH_SHORT).show();
											ImageViewer.this.sendBroadcast(new Intent("FQ_DELETE"));
											dialog.dismiss();
											ImageViewer.this.finish();
										}else{
											dialog.dismiss();
											Toast.makeText(getApplicationContext(), "Failed to delete file",
													Toast.LENGTH_SHORT).show();
										}
									}
								});
								dialog.show();
							}
							
						});
						
						
					}catch(RuntimeException e){
						Toast.makeText(getApplicationContext(), R.string.xerror, Toast.LENGTH_LONG).show();
					}catch(Exception e){
						Toast.makeText(getApplicationContext(), R.string.xerror, Toast.LENGTH_LONG).show();
					}
					
					
					try{
						LinearLayout share = (LinearLayout)findViewById(R.id.viewer_share);
						share.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								new BluetoothChooser(ImageViewer.this, (file.getAbsolutePath()),
										TaskerActivity.size.x*7/8,null );
							}
						});
					}catch(RuntimeException e){
						Toast.makeText(getApplicationContext(), R.string.xerror, Toast.LENGTH_LONG).show();
					}catch(Exception e){
						Toast.makeText(getApplicationContext(), R.string.xerror, Toast.LENGTH_LONG).show();
					}
					
					
				}else{
					Toast.makeText(getApplicationContext(), "Unable to open image",
							Toast.LENGTH_SHORT).show();
					ImageViewer.this.finish();
				}
			}else{
				Toast.makeText(getApplicationContext(), ""+file.getPath(),
						Toast.LENGTH_SHORT).show();
				ImageViewer.this.finish();
			}
			
		}else{
			Toast.makeText(getApplicationContext(), "Invalid File Selected",
					Toast.LENGTH_SHORT).show();
			ImageViewer.this.finish();
		}
	}

	
	
}
