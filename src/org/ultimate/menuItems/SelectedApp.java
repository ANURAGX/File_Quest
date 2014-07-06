
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


package org.ultimate.menuItems;




import org.anurag.file.quest.R;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectedApp {
	int mode;
	Context mContext;
	Dialog dialog;
	String action ;
	String TYPE;
	public SelectedApp(Context context,int width,int MODE,String data,String ac) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mode = MODE;
		TYPE = ac;
		action = data;
		dialog = new Dialog(context, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.selected_app);
		dialog.getWindow().getAttributes().width = width;
		onCreate();
	}
	
	
	protected void onCreate() {
		// TODO Auto-generated method stub
		
		//params.width = p.x*5/6;
		
		
		//setContentView(R.layout.selected_app);
		ImageView iv = (ImageView)dialog.findViewById(R.id.selectedImage);
		Button q = (Button)dialog.findViewById(R.id.quit);
		Button s = (Button) dialog.findViewById(R.id.set);
		TextView tv = (TextView)dialog.findViewById(R.id.selectedTitle);
		TextView tv1 = (TextView)dialog.findViewById(R.id.selecteddes1);
		TextView tv2 = (TextView)dialog.findViewById(R.id.selecteddes2);
		
		
		final SharedPreferences pref = mContext.getSharedPreferences("DEFAULT_APPS", mode);
		final SharedPreferences.Editor edit = pref.edit(); 
		PackageManager pack = mContext.getPackageManager();
		PackageInfo info = null;
		try{
			info = pack.getPackageInfo(action, 0);
		}catch(Exception e){
			
		}
		iv.setImageDrawable(info.applicationInfo.loadIcon(pack));
		tv.setText(info.applicationInfo.loadLabel(pack));
		tv1.setText("PROCESS NAME : " + info.applicationInfo.packageName);
		tv2.setText("VERSION : " + info.versionName);
		
		q.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		s.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(TYPE.equals("MUSIC")){
					edit.putString("MUSIC", null);
					edit.commit();
					Toast.makeText(mContext,
							R.string.setasdefault, Toast.LENGTH_SHORT).show();
					dialog.dismiss();;
				}else if(TYPE.equals("IMAGE")){
					edit.putString("IMAGE", null);
					edit.commit();
					Toast.makeText(mContext,
							R.string.setasdefault, Toast.LENGTH_SHORT).show();
					dialog.dismiss();;
				}else if(TYPE.equals("VIDEO")){
					edit.putString("VIDEO", null);
					edit.commit();
					Toast.makeText(mContext,
							R.string.setasdefault, Toast.LENGTH_SHORT).show();
					dialog.dismiss();;
				}else if(TYPE.equals("ZIP")){
					edit.putString("ZIP", null);
					edit.commit();
					Toast.makeText(mContext,
							R.string.setasdefault, Toast.LENGTH_SHORT).show();
					dialog.dismiss();;

				}else if(TYPE.equals("PDF")){
					edit.putString("PDF", null);
					edit.commit();
					Toast.makeText(mContext,
							R.string.setasdefault, Toast.LENGTH_SHORT).show();
					dialog.dismiss();;

				}else if(TYPE.equals("TEXT")){
					edit.putString("TEXT", null);
					edit.commit();
					Toast.makeText(mContext,
							R.string.setasdefault, Toast.LENGTH_SHORT).show();
					dialog.dismiss();;

				}else if(TYPE.equals("RAR")){
					edit.putString("RAR", null);
					edit.commit();
					Toast.makeText(mContext,
							R.string.setasdefault, Toast.LENGTH_SHORT).show();
					dialog.dismiss();;

				}
			}
		});
		
		dialog.show();
	}

	
	
}
