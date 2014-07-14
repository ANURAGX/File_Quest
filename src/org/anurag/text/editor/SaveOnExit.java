package org.anurag.text.editor;

import org.anurag.file.quest.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SaveOnExit{

	Context mContext;
	Dialog dialog;
	public SaveOnExit(Context con,int w) {
		// TODO Auto-generated constructor stub
		mContext = con;		
		dialog = new Dialog(mContext, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.delete_files);
		dialog.getWindow().getAttributes().width = w;
		onCreate();
	}
	
	void onCreate() {
		ImageView v = (ImageView)dialog.findViewById(R.id.popupImage);
		v.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_save));
		TextView popupTitle = (TextView)dialog.findViewById(R.id.popupTitle);
		popupTitle.setText(R.string.saveOnExit);
		TextView popupMessage = (TextView)dialog.findViewById(R.id.textMessage);
		popupMessage.setText(R.string.saveMesage);
		Button btn1 = (Button)dialog.findViewById(R.id.popupOk);
		Button btn2 = (Button)dialog.findViewById(R.id.popupCancel);
		
		btn1.setText(R.string.yes);
		btn2.setText(R.string.no);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mContext.sendBroadcast(new Intent("FQ_EDIT"));
				dialog.dismiss();
			}
		});
		
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				mContext.sendBroadcast(new Intent("FQ_EDIT_EXIT"));
				dialog.dismiss();
			}
		});
	}
}
