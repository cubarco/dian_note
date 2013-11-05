package com.dianwork.note;

import android.R.string;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	public static int MAIN_ACTIVITY = 10;// requestcode内容
	public static int LOGUP_ACTIVITY = 11;

	public static int TO_FINISH_PREVIOUS_ACTIVITY = 100;// resultcode内容

	Button signinButton, signupButton;
	EditText editUsername, editPwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		this.setTitle("Log in & Log up");
		signinButton = (Button) this.findViewById(R.id.signinbutton);
		signupButton = (Button) this.findViewById(R.id.signupbutton);
		editUsername = (EditText) this.findViewById(R.id.editusername);
		editPwd = (EditText) this.findViewById(R.id.editpassword);

		signupButton.setOnClickListener(new signUpBtnOnclickListener());
		signinButton.setOnClickListener(new signInBtnOnclickListener());

	}

	// ====================================================================================初始化控件动作响应事件
	public class signInBtnOnclickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			String userNameString=editUsername.getText().toString();
			intent.putExtra("Username", userNameString);
			startActivityForResult(intent, MAIN_ACTIVITY);
		}
	}

	public class signUpBtnOnclickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(LoginActivity.this, LogupActivity.class);
			startActivityForResult(intent, LOGUP_ACTIVITY);
		}
	}

	// ====================================================================================初始化控件动作响应事件

	// ====================================================================================Activity的响应事件
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if ((requestCode == MAIN_ACTIVITY||requestCode==LOGUP_ACTIVITY)
				&& resultCode == TO_FINISH_PREVIOUS_ACTIVITY) {
			finish();
		}

	}

	// ====================================================================================Activity的响应事件
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.login, menu);
	// return true;
	// }

}
