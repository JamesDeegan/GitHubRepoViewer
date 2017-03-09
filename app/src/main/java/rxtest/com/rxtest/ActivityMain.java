package rxtest.com.rxtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rxtest.com.rxtest.viewer.ProfileActivity;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.activity_main_fetch_btn)
    void onClick() {
        EditText et = (EditText) findViewById(R.id.activity_main_username_et);
        Intent intent = new Intent(ActivityMain.this, ProfileActivity.class);
        intent.putExtra("username", et.getText().toString());
        startActivity(intent);
    }
}
