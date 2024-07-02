package org.example.controller;

public class MainThread extends Thread {
    private final Runnable target;

    public MainThread(Runnable target) {
        this.target = target;
    }

    @Override
    public void run() {
        if (target != null) {
            target.run();
        }
    }
}
