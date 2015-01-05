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
 *                             
 *                             anurag.dev1512@gmail.com
 *
 */
package org.anurag.file.quest;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class WhatsNew {

	public WhatsNew(Context ctx,int width,int height) {
		// TODO Auto-generated constructor stub
		final Dialog dialog = new Dialog(ctx, R.style.Dialog_Orange);
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
