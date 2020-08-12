package com.quibbler.mvp.view;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.quibbler.mvp.R;
import com.quibbler.mvp.presenter.Presenter;

public class MainActivity extends AppCompatActivity implements IView {

    private Presenter mPresenter;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = Presenter.getInstance();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
    }

    private void initView() {
        mTextView = findViewById(R.id.text);
        mPresenter.subscribe(this);
    }

    @UiThread
    @Override
    public void showTextMessage(String message) {
        mTextView.setText(message);
    }
}