package com.example.huangming.arrowpopupwindow;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int s = 3;
        switch (view.getId()) {
            case R.id.button:
                s = 20;
                break;
            case R.id.button2:
                //TODO implement
                break;
            case R.id.button3:
                //TODO implement
                break;
            case R.id.button4:
                //TODO implement
                break;
            case R.id.button5:
                //TODO implement
                break;
            case R.id.button6:
                //TODO implement
                break;
        }
        ArrowPopupWindow win = new ArrowPopupWindow(MainActivity.this);
        String title;
        for (int i = 0; i < s; i++) {
            if (i == 0) {
                title = "带箭头的弹出框带箭头的弹出框带箭头的弹出框带箭头的弹出框带箭头的弹出框带箭头的弹出框";
            }else{
                title = "带箭头的弹出框";
            }
            win.addPopuItem(new ArrowPopupWindow.PopuItem(title,
                ContextCompat.getDrawable(MainActivity.this, R.mipmap.ic_launcher), i));
        // win.addPopuItem(new ArrowPopupWindow.PopuItem(title,
        //         null, i));
        }
        win.setOnPopuItemClickListener(new ArrowPopupWindow.OnPopuItemClickListener() {
            @Override
            public void onItemClick(ArrowPopupWindow.PopuItem item) {
                //根据被选中的item作相应处理
                Toast.makeText(MainActivity.this, "点击了item" + item.value, Toast.LENGTH_SHORT)
                    .show();
            }

        });
        win.show(view);
    }
}
