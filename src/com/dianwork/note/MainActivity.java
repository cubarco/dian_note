package com.dianwork.note;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static int EDIE_ACTIVITY = 10; // requestcode
	public static int UPDATE_LISTVIEW = 100;// resultcode
	public Note[] Show = new Note[50];
	public ArrayList<HashMap<String, Object>> listItem;
	SimpleAdapter mSimpleAdapter;
	ListView listView;
	Button plusButton;
	Bitmap bmBitmap;
	String usernameString;
	ProgressDialog progressDialog;
	public static String mPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.setTitle("myNote记事本");
		setResult(LoginActivity.TO_FINISH_PREVIOUS_ACTIVITY);

		plusButton = (Button) this.findViewById(R.id.plusbutton);
		listView = (ListView) this.findViewById(R.id.listView1);
		Intent intent = getIntent();
		usernameString = intent.getStringExtra("Username");
		// File mFile = this.getDir("notecache", MODE_PRIVATE);
		// if (!mFile.exists()) {
		// mFile.mkdir();
		// }
		// mPath = mFile.getAbsolutePath();
		// Log.e("FilePath", mPath);

		// TODO 加载记事本源文件
		listItem = new ArrayList<HashMap<String, Object>>();
		// ListToView();
		mSimpleAdapter = new SimpleAdapter(this, listItem, R.layout.itemadded,
				new String[] { "ItemImage", "ItemTitle", "itemDate" },
				new int[] { R.id.imgview, R.id.titleview, R.id.dateview });
		listView.setAdapter(mSimpleAdapter);
		listView.setOnItemClickListener(new listitemOnclickListener());
		listView.setOnItemLongClickListener(new listitemLongclickListener());
		plusButton.setOnClickListener(new plusBtnOnclickListener());

		progressDialog = ProgressDialog.show(MainActivity.this, "请稍等",
				"正在载入记事本文件...", true);
		MyThread MyThread1 = new MyThread();
		MyThread1.start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == EDIE_ACTIVITY && resultCode == UPDATE_LISTVIEW) {
			mSimpleAdapter.notifyDataSetChanged();
		}
	}

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>初始化控件的动作响应事件
	public class listitemLongclickListener implements OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				final int arg2, long arg3) {
			// TODO 删除文件
			new AlertDialog.Builder(MainActivity.this)
					.setTitle("删除")
					.setMessage("确定要删除这条记事本吗？")
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									itemRemove(arg2, "");
									// TODO 删除数据，代码在itemremove里面写
								};

							}).setNegativeButton("否", null).setCancelable(true)
					.show();
			return false;
		}
	}

	public class listitemOnclickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 读取item对应的记事本
			Intent intent = new Intent(MainActivity.this, Note_edit.class);
			intent.putExtra("title", listItem.get(arg2).get("ItemTitle")
					.toString()); // 取出Title
			startActivity(intent);
		}
	}

	public class plusBtnOnclickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO 暂时不需要新建文本
			SimpleDateFormat sdf = new SimpleDateFormat("",
					Locale.SIMPLIFIED_CHINESE);
			sdf.applyPattern("yyyy年MM月dd日_HH时mm分ss秒");// final 是thread请求的
			final String timeStr = sdf.format(new Date());// 显示的Date
			sdf.applyPattern("yyyyMMddHHmmss");
			String timeSimple = sdf.format(new Date());// 在Title上显示的时间
			final String filenameString = "新的记事" + timeSimple;

			itemAdd(filenameString, timeStr);

			Intent intent = new Intent(MainActivity.this, Note_edit.class);
			intent.putExtra("title",
					listItem.get(listItem.size() - 1).get("ItemTitle")
							.toString());// 传递Title
			startActivity(intent);
		}
	}

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^初始化控件的动作响应事件
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>handler和thread
	private class MyThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				// TODO 载入文件
				for (int i = 0; i < 5; i++) {
					itemAdd("hehe" + i, "hehe");
				}
				handler.sendEmptyMessage(0);
				// mSimpleAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				Log.i("错误显示", e.getMessage());
			}
		}

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mSimpleAdapter.notifyDataSetChanged();
				progressDialog.dismiss();
				break;
			}
		}
	};

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^handler
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>图片压缩
	private void imageZoom() {
		double maxSize = 10.00;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		double mid = b.length / 1024;
		if (mid > maxSize) {
			double i = mid / maxSize;
			// bmBitmap = zoomImage(bmBitmap, bmBitmap.getWidth() /
			// Math.sqrt(i),
			// bmBitmap.getHeight() / Math.sqrt(i));
			bmBitmap = zoomImage(bmBitmap, 400, 400);// 200为长宽
		}
	}

	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^图片压缩
	protected void itemAdd(String title, String date) {
		// TODO bm的路径待修改
		File pngpathFile = getDir("notecache", MODE_PRIVATE);
		String pngpath = pngpathFile.getPath();
		bmBitmap = BitmapFactory
				.decodeFile(pngpath+"/noteicon-64.png");
		imageZoom();
		//用imagezoom在
		Uri uri2 = Uri.parse(MediaStore.Images.Media.insertImage(
				getContentResolver(), bmBitmap, null, null));
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemImage", uri2);
		map.put("ItemTitle", title);
		map.put("itemDate", date);
		listItem.add(0, map);
		// mSimpleAdapter.notifyDataSetChanged();

	}

	protected void itemRemove(int xuhao, String title) {
		// TODO 删除记事本源文件
		listItem.remove(xuhao);
		mSimpleAdapter.notifyDataSetChanged();
	}

	// public void ListToView() {
	// // 一开始在Listview上显示
	// // msg.note
	// String filePath = mPath + "/msg.note";
	// File Cun = new File(filePath);
	// if (!Cun.exists()) {
	// try {
	// Cun.createNewFile();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// // TODO 待删除
	// if (Cun.exists()) {
	// Log.i("CreatNewFile", "exists");
	// } else {
	// Log.i("CreatNewFile", "Dont exists");
	// }
	//
	// File L = new File(filePath);
	// String get;
	//
	// int i = 0;
	// StringBuffer str = new StringBuffer("");
	// // try {
	// // FileReader fr = new FileReader(L);
	// // while ((i = fr.read()) != -1)
	// // ;
	// // } catch (IOException e) {
	// // e.printStackTrace();
	// // }
	//
	// try {
	// FileInputStream is = new FileInputStream(L);
	// Scanner scanner = new Scanner(is);
	// while (scanner.hasNext()) {
	// str.append(scanner.nextLine());
	// }
	//
	// scanner.close();
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// get = str.toString();
	// Log.i("Get ouput", get);
	// if (get == "")
	// return;// 显示空
	// String[] list = get.split("*");
	// for (i = 0; i < list.length; i++) {
	// String[] lei = list[i].split(" ");
	// Show[i].Title = lei[0];
	// Show[i].Date = lei[1];
	// Show[i].Path = lei[2];
	// }
	// for (int j = 0; j < list.length; j++) {
	// HashMap<String, Object> map = new HashMap<String, Object>();
	// // TODO 加载源文件
	//
	// map.put("ItemTitle", Show[j].Title);
	// map.put("itemDate", Show[j].Date);
	// listItem.add(map);
	// }
	//
	// }

	// private void CreateNote(View v, String titleString, String dateString)
	// {// 创建一个Note
	// Note n = new Note();
	// // 此处判断与获取
	// n.Title = titleString;//
	// n.Date = dateString;// 系统时间
	// n.Msg = "";// 界面获取
	// n.Path = mPath + "/" + n.Title + ".note";
	// // 开始保存
	// File Cun = new File(n.Path);
	// try {
	// Cun.createNewFile();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// StringBuffer sb = new StringBuffer();
	// sb.append(n.Msg);
	// try {
	// FileWriter fw = new FileWriter(n.Path);
	// fw.write(sb.toString());
	// fw.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// Cun = new File(mPath + "/msg.note");
	// try {
	// FileOutputStream out = new FileOutputStream(Cun, true);
	// out.write((n.Title + ' ' + n.Date + ' ' + n.Path + '*').getBytes());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// // 提示保存成功
	// }

}
