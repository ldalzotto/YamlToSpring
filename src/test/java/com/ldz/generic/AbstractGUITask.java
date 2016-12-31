package com.ldz.generic;

import javafx.application.Platform;

import java.util.concurrent.CountDownLatch;

/**
 * Created by ldalzotto on 29/12/2016.
 */
public abstract class AbstractGUITask {

    private CountDownLatch latch = new CountDownLatch(1);

    public AbstractGUITask(){
        Platform.runLater(new Runnable() {
            public void run() {
                try {
                    GUITask();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract void GUITask();



}
