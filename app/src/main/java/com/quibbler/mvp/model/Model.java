package com.quibbler.mvp.model;

import androidx.annotation.WorkerThread;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Model implements IModel {

    private Calendar mCalendar;
    private SimpleDateFormat dateFormat;

    public Model() {
        mCalendar = Calendar.getInstance();
        dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
    }

    @WorkerThread
    @Override
    public String getMessage() {
        return getMessageFromServer();
    }

    @WorkerThread
    private String getMessageFromServer() {
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        return "Hello World!\n\n\n" + dateFormat.format(mCalendar.getTime());
    }
}
