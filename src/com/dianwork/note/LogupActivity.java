package com.dianwork.note;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LogupActivity extends Activity {
	Button confirmButton;
	EditText editUsername, editPwd, editConfirmPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logup);
		this.setTitle("Sign up");
		confirmButton = (Button) this.findViewById(R.id.confirmbutton);
		editUsername = (EditText) this.findViewById(R.id.editusername);
		editPwd = (EditText) this.findViewById(R.id.editpassword);
		editConfirmPwd = (EditText) this.findViewById(R.id.confirmpassword);

		confirmButton.setOnClickListener(new confirmBtnOnclickListener());

	}

	public class confirmBtnOnclickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(LogupActivity.this, MainActivity.class);
			String userNameString = editUsername.getText().toString();
			intent.putExtra("Username", userNameString);
			startActivityForResult(intent, LoginActivity.MAIN_ACTIVITY);

			setResult(LoginActivity.TO_FINISH_PREVIOUS_ACTIVITY);//关闭login页面
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == LoginActivity.MAIN_ACTIVITY
				&& resultCode == LoginActivity.TO_FINISH_PREVIOUS_ACTIVITY) {
			finish();
		}

	}
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.logup, menu);
	// return true;
	// }

}
