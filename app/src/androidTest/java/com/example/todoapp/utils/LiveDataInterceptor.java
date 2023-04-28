package com.example.todoapp.utils;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class LiveDataInterceptor<T> {
    private static final Long AWAIT_TIME_VALUE = 2L;
    private static final TimeUnit AWAIT_TIME_UNIT = TimeUnit.SECONDS;

    private T data;

    public T getOrAwaitValueFrom(LiveData<T> liveData) throws TimeoutException {

        CountDownLatch latch = new CountDownLatch(1);
        Handler handler = new Handler(Looper.getMainLooper());
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T t) {
                data = t;
                latch.countDown();
            }
        };

        handler.post(new Runnable() {
            @Override
            public void run() {
                liveData.observeForever(observer);
            }
        });

        try {
            if (!latch.await(AWAIT_TIME_VALUE, AWAIT_TIME_UNIT)) {
                throw new TimeoutException("LiveData value was never set.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    liveData.removeObserver(observer);
                }
            });
        }

        return data;
    }
}
