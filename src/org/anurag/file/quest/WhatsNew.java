package org.anurag.file.quest;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class WhatsNew {

	public WhatsNew(Context ctx,int width,int height) {
		// TODO Auto-generated constructor stub
		final Dialog dialog = new Dialog(ctx, R.style.custom_dialog_theme);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.whats_new);
		Button btn = (Button)dialog.findViewById(R.id.whats_new_btn);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.getWindow().getAttributes().width = width;
		dialog.getWindow().getAttributes().height = height;
		dialog.show();
	}
}
