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
 *                             anurag.dev1512@gmail.com
 *
 */

package org.anurag.file.quest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;

public class SaveOnExit{

	public SaveOnExit(final Context con) {
		// TODO Auto-generated constructor stub
		
		new MaterialDialog.Builder(con)
		.title(R.string.saveOnExit)
		.content(R.string.saveMesage)
		.autoDismiss(false)
		.positiveText(R.string.yes)
		.negativeText(R.string.dismiss)
		.callback(new ButtonCallback() {

			@Override
			public void onPositive(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onPositive(dialog);
				con.sendBroadcast(new Intent("FQ_EDIT"));
				dialog.dismiss();
			}

			@Override
			public void onNegative(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onNegative(dialog);
				dialog.dismiss();
			}
			
		})
		.dismissListener(new OnDismissListener() {			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				con.sendBroadcast(new Intent("FQ_EDIT_EXIT"));
				dialog.dismiss();
			}
		})
		.show();
	}	
}
