package com.dianwork.note;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class Note_edit extends Activity {
	EditText editTextTitle;
	EditText editTextMsg;
	Button editButton;
	public Note Art;
	public String strTitle; // strTitle用于存储Title，在标题栏显示正在编辑的文本
//	public String sdcardDirString = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_edit);
		Art = new Note();
		
		Intent intent = getIntent();
		strTitle = intent.getStringExtra("title");// 获取点击的listview项目的title		
		editTextMsg = (EditText) this.findViewById(R.id.editText2);
		this.setTitle("正在编辑: " + strTitle); // 此处使用了strTitle
		editTextTitle = (EditText) this.findViewById(R.id.editText1);// 输出title
		editTextTitle.setText(strTitle);
		/*
		 * 记得用imageZoom在保存img时压缩img
		 */

//		File pan = new File(Art.Path);
//		ReadTo();
//		editTextMsg.setText(Art.Msg);
//		if (pan.exists()) {
//			ReadTo();
//			editTextMsg.setText(Art.Msg);
//		}
//		Log.i("com.dianwork.note", strTitle);

		editButton = (Button) this.findViewById(R.id.editbutton);
		editButton.setOnClickListener(new editBtnOnclickListener());
	}
	
	
public class  editBtnOnclickListener implements OnClickListener{
	@Override
	public void onClick(View v) {
		//TODO 保存数据
		//TODO 更新listitem
		finish();	
	}
}
//=========================================================================此处缺少新建以及读取的代码，图片保存
//	public void CreateNote(View v) {// 创建一个Note
//		Note n = new Note();
//		// 此处判断与获取
//		n.Title = editTextTitle.getText().toString();
//		SimpleDateFormat sdf = new SimpleDateFormat("",
//				Locale.SIMPLIFIED_CHINESE);
//		sdf.applyPattern("yyyy年MM月dd日 HH时mm分ss秒");
//		n.Date = sdf.format(new Date());// 系统时间
//		n.Msg = editTextMsg.getText().toString();// 界面获取		
//		n.Path = MainActivity.mPath+"/" + n.Title + ".note";
//		// 开始保存
//		File Cun = new File(n.Path);
//		
//		try {
//			Cun.createNewFile();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		if(Cun.exists()) {
//			Log.i("CreatNewFile", "exists"+"            "+n.Path); 
//		
//		}
//		
//		StringBuffer sb = new StringBuffer();
//		sb.append(n.Msg);
//		try {
//			FileWriter fw = new FileWriter(n.Path);
//			fw.write(sb.toString());
//			fw.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		Cun = new File(MainActivity.mPath+"/msg.note");
//		try {
//			FileWriter out = new FileWriter(Cun, true);
//			out.write((n.Title + ' ' + n.Date + ' ' + n.Path + '*'));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		// 提示保存成功
//	}
//
//	public void ReadTo() {
//		// 根据路径显示文档
//		File f = new File(Art.Path);
//		int i = 0;
//		StringBuffer str = new StringBuffer("");
//		try {
//			FileReader fr = new FileReader(f);
//			while ((i = fr.read()) != -1);
//			fr.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Art.Msg = str.toString();
//		// UI显示
//	}
}
