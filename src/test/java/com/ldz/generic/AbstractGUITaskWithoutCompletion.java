package com.ldz.generic;

import javafx.application.Platform;

import java.util.concurrent.CountDownLatch;

/**
 * Created by ldalzotto on 29/12/2016.
 */
public abstract class AbstractGUITaskWithoutCompletion {

    public AbstractGUITaskWithoutCompletion(long timeToWaitInMs){
        Platform.runLater(new Runnable() {
            public void run() {
                try {
                    GUITask();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            Thread.sleep(timeToWaitInMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract void GUITask();
}
