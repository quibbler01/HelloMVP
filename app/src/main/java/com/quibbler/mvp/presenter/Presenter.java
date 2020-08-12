package com.quibbler.mvp.presenter;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.quibbler.mvp.model.IModel;
import com.quibbler.mvp.model.Model;
import com.quibbler.mvp.view.IView;


public class Presenter {
    private static volatile Presenter sPresenter;

    private IView mViewCallback;
    private IModel mModel;
    //通过Handler更新View层
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private Presenter() {
        mModel = new Model();
    }

    public static Presenter getInstance() {
        if (sPresenter == null) {
            synchronized (Presenter.class) {
                if (sPresenter == null) {
                    sPresenter = new Presenter();
                }
            }
        }
        return sPresenter;
    }

    @MainThread
    public void getMessage() {
        //应该使用线程池
        new Thread(new Runnable() {
            @Override
            public void run() {
                getMessageInternal();
            }
        }).start();
    }

    @WorkerThread
    private void getMessageInternal() {
        final String message = mModel.getMessage();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (null != mViewCallback) {
                    mViewCallback.showTextMessage(message);
                }
            }
        });
    }

    @MainThread
    public void subscribe(@NonNull IView viewCallback) {
        mViewCallback = viewCallback;
    }

    @MainThread
    public void unSubscribe() {
        mViewCallback = null;
    }

}
