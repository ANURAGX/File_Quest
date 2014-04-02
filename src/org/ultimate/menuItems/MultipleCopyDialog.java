package org.ultimate.menuItems;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.anurag.file.quest.R;
import org.ultimate.root.LinuxShell;
import com.stericson.RootTools.RootTools;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MultipleCopyDialog {
	
	int copyResult;
	Context mContext;
	static int BUFFER = 256;
	Dialog dialog;
	ArrayList<File> list;
	long si = 0;
	String DEST;
	int len = 0;
	COPY copy;
	Button btn1,btn2;
	TextView copyTo;
	TextView copyFrom;
	TextView copying;
	TextView contentSize;
	TextView time;
	boolean command;
	ImageView iM;
	public MultipleCopyDialog(Context context,ArrayList<File> obj,int windowSize,String dest,boolean comm) {
		// TODO Auto-generated constructor stub
		mContext = context;
		DEST = dest;
		copyResult = 0;
		command = comm;
		si = 0;
		BUFFER = 256;
		list = obj;
		WebView web;
		dialog = new Dialog(mContext, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.copy_dialog);
		dialog.setCancelable(false);
		dialog.getWindow().getAttributes().width = windowSize;
		web = (WebView)dialog.findViewById(R.id.copy_Web_View);
		web.loadUrl("file:///android_asset/Progress_Bar_HTML/index.html");
		web.setEnabled(false);
		
		iM = (ImageView)dialog.findViewById(R.id.headerImage);
		iM.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_file_task));
		btn1 = (Button)dialog.findViewById(R.id.copyOk);
		btn2 = (Button)dialog.findViewById(R.id.copyCancel);
		btn1.setVisibility(View.GONE);
		copyTo = (TextView)dialog.findViewById(R.id.copyTo);
		copyFrom = (TextView)dialog.findViewById(R.id.copyFrom);
		copying  = (TextView)dialog.findViewById(R.id.currentFile);
		time = (TextView)dialog.findViewById(R.id.timeLeft);
		contentSize = (TextView)dialog.findViewById(R.id.copyFileSize);
		len = list.size();
		copyTo.setText("Copying To :-"+DEST);
		startCopying();
	}

	public void startCopying(){
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				copy.cancel(true);
				Toast.makeText(mContext, R.string.stopped, Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		});
		try{
			copy = new COPY();
			copy.execute();
		}catch(RuntimeException e){
			Toast.makeText(mContext, R.string.xerror, Toast.LENGTH_SHORT).show();
		}
		
		//dialog.show();
	}
	
	/**
	 * 
	 * @author anurag
	 *
	 */
	class COPY extends AsyncTask<Void, String, Void>{
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(copyResult == 0)
				Toast.makeText(mContext, R.string.success, Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(mContext, "Failed to copy file", Toast.LENGTH_SHORT).show();
			mContext.sendBroadcast(new Intent("FQ_DELETE"));
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected void onProgressUpdate(String... val) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(val);
			copyFrom.setText(val[0]);
			copying.setText(val[1]);
			contentSize.setText(val[2]);
			time.setText(val[3]);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			File file;
			int j = 0;
			int c = 0;
			for(int i= 0 ; i<len ; ++i){
				file = (File) list.get(i);
				if(file!=null){
					getFileSize(file);
					c++;
				}	
			}
			File Dest = new File(DEST);
			String formated = formatsize();
			for(int i = 0 ; i<len ;++i){
				file = (File) list.get(i);
				if(file!=null){
					String[] res = {"Copying : "+file.getName(),size(file),formated,""+(j/c)*100 + " Percent Copied"};
					publishProgress(res);
					copyResult = copyToDirectory(file.getPath(), DEST);
					j++;
					if(command){
						if(Dest.canWrite())
							deleteTargetForCut(file);
						else{
							RootTools.deleteFileOrDirectory(file.getPath(), false);
						}
					}	
				}
			}
			
			return null;
		}
		
	}
	
	
	
	
	
	/**
	 * 
	 * @param old
	 *            the file to be copied
	 * @param newDir
	 *            the directory to move the file to
	 * @return
	 */
	public static int copyToDirectory(String old, String newDir) {
		File old_file = new File(old);
		File temp_dir = new File(newDir);
		byte[] data = new byte[BUFFER];
		int read = 0;

		if (old_file.isFile() && temp_dir.isDirectory() && temp_dir.canWrite()) {
			String file_name = old
					.substring(old.lastIndexOf("/"), old.length());
			File cp_file = new File(newDir + file_name);

			if (cp_file.exists())
				return -2;

			try {

				BufferedOutputStream o_stream = new BufferedOutputStream(
						new FileOutputStream(cp_file));
				BufferedInputStream i_stream = new BufferedInputStream(
						new FileInputStream(old_file));

				while ((read = i_stream.read(data, 0, BUFFER)) != -1)
					o_stream.write(data, 0, read);

				o_stream.flush();
				i_stream.close();
				o_stream.close();

			} catch (FileNotFoundException e) {
				Log.e("FileNotFoundException", e.getMessage());
				return -1;

			} catch (IOException e) {
				Log.e("IOException", e.getMessage());
				return -1;

			}

		} else if (old_file.isDirectory() && temp_dir.isDirectory()
				&& temp_dir.canWrite()) {
			String files[] = old_file.list();
			String dir = newDir
					+ old.substring(old.lastIndexOf("/"), old.length());
			int len = files.length;

			if (!new File(dir).mkdir())
				return -1;

			for (int i = 0; i < len; i++)
				copyToDirectory(old + "/" + files[i], dir);

		} else if (old_file.isFile() && !temp_dir.canWrite()) {
			int root = moveCopyRoot(old, newDir);

			if (root == 0)
				return 0;
			else
				return -1;

		} else if (!temp_dir.canWrite())
			return -1;

		return 0;
	}
	
	
	// Move or Copy with Root Access using RootTools library
		private static int moveCopyRoot(String old, String newDir) {

			try {
				File f = new File(old);
				if (LinuxShell.isRoot()) {
					if (!readReadWriteFile()) {
						RootTools.remount(newDir + "/"+f.getName(), "rw");
						//LinuxShell.execute("mount -o remount,rw "+newDir    +" \n");
					}
					
				//	CommandCapture command = new CommandCapture(0, "cat "+old+" > "+newDir+ "/"+f.getName()    + "/n");
					RootTools.copyFile("'"+old+"'","'"+ newDir+"'", true, true);
					//LinuxShell.execute("cat "+old+" > "+newDir+ "/"+f.getName()    + "/n");
					//RootTools.getShell(true).add(command);	
					//Shell.SU.run("cat '" + old + "' >" +"'"+ newDir+"'");
					return 0;
				} else {
					return -1;
				}

			} catch (Exception e) {
				return -1;
			}
		}
	
	
	/**
	 * 
	 * @param file
	 */
	public void getFileSize(File file){
		if(file.isFile()){
			si = si + file.length();
		}else if(file.isDirectory() && file.listFiles().length !=0){
			File[] a = file.listFiles();
			for(int i = 0 ; i<a.length ; ++i){
				if(a[i].isFile()){
					si = si + a[i].length();
				}else
					getFileSize(a[i]);
			}
		}
	}
	
	/**
	 * THIS FUNCTION RETURN THE SIZE IF THE GIVEN FIZE IN PARAMETER
	 * @param f
	 * @return
	 */
	public String size(File f){
		long size = f.length();
		if(size>1024*1024*1024){
			size = size/(1024*1024*1024);
			return String.format("File size :%.2f GB", (double)size);
		}
		else if(size > 1024*1024){
			size = size/(1024*1024);
			return String.format("File size :%.2f MB", (double)size);
		}
		else if(size>1024){
			size = size/1024;
			return String.format("File size :%.2f KB", (double)size);
		}
		else{
			return String.format("File size :%.2f Bytes", (double)size);
		}	
	}
	
	
	/**
	 * THIS FUNCTION RETURN THE SIZE IF THE GIVEN FIZE IN PARAMETER
	 * @param f
	 * @return
	 */
	public String formatsize(){
		//long size = f.length();
		if(si>1024*1024*1024)
			return String.format("Content size :%.2f GB", (double)si/(1024*1024*1024));
		
		else if(si > 1024*1024)
			return String.format("Content size :%.2f MB", (double)si/(1024*1024));
		
		else if(si>1024)
			return String.format("Content size :%.2f KB", (double)si/(1024));
		
		else
			return String.format("Content size :%.2f Bytes", (double)si);
	}
	
	/**
	 * 
	 * @param file
	 */
	public void deleteTargetForCut(File file) {
		File target = file;
		if(target.exists() && target.isFile() && target.canWrite())
			target.delete();
		
		else if(target.exists() && target.isDirectory() && target.canRead()) {
			String[] file_list = target.list();
			
			if(file_list != null && file_list.length == 0) {
				target.delete();
			} else if(file_list != null && file_list.length > 0) {
				
				for(int i = 0; i < file_list.length; i++) {
					File temp_f = new File(target.getAbsolutePath() + "/" + file_list[i]);
					if(temp_f.isDirectory())
						deleteTargetForCut(temp_f);
					else if(temp_f.isFile())
						temp_f.delete();
				}
			}
			if(target.exists())
				if(target.delete()){}
		}
	}
	
	
	
	
	// Check if system is mounted
		private static boolean readReadWriteFile() {
			File mountFile = new File("/proc/mounts");
			StringBuilder procData = new StringBuilder();
			if (mountFile.exists()) {
				try {
					FileInputStream fis = new FileInputStream(mountFile.toString());
					DataInputStream dis = new DataInputStream(fis);
					BufferedReader br = new BufferedReader(new InputStreamReader(
							dis));
					String data;
					while ((data = br.readLine()) != null) {
						procData.append(data + "\n");
					}

					br.close();
				} catch (Exception e) {
					e.printStackTrace();
					return false;			
				}
				if (procData.toString() != null) {
					String[] tmp = procData.toString().split("\n");
					for (int x = 0; x < tmp.length; x++) {
						// Kept simple here on purpose different devices have
						// different blocks
						if (tmp[x].contains("/dev/block")
								&& tmp[x].contains("/system")) {
							if (tmp[x].contains("rw")) {
								// system is rw
								return true;
							} else if (tmp[x].contains("ro")) {
								// system is ro
								return false;
							} else {
								return false;
							}
						}
					}
				}
			}
			return false;
		}
	
	
	
	
	
	
	
	
	
	
}
