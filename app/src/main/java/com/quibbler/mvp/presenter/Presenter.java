package com.quibbler.mvp.presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.quibbler.mvp.model.IModel;
import com.quibbler.mvp.model.Model;
import com.quibbler.mvp.view.IView;

import java.util.Timer;
import java.util.TimerTask;


public class Presenter {
    private static volatile Presenter sPresenter;

    private IView mViewCallback;
    private IModel mModel;
    //通过Handler切回到主线程更新View层
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

    private Timer mTimer = new Timer();
    @MainThread
    public void subScribeMessage() {
        //应该使用线程池
        new Thread(new Runnable() {
            @Override
            public void run() {
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getMessageInternal();
                    }
                }, 0, 1000);
            }
        }).start();
    }

    public void unSubscribeMessage(){
        mTimer.cancel();
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
        subScribeMessage();
    }

    @MainThread
    public void unSubscribe() {
        mViewCallback = null;
        unSubscribeMessage();
    }

}
