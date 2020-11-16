package com.tukY.Lab4;

import java.util.concurrent.atomic.AtomicBoolean;

public class Fork {
    private final int id;
    private final AtomicBoolean isUsing;

    public Fork() {
        this(0);
    }

    public Fork(int id) {
        this.id = id;
        this.isUsing = new AtomicBoolean(false);
    }

    public boolean get(){
        return isUsing.compareAndSet(false, true);
    }

    public boolean put(){
        return isUsing.compareAndSet(true, false);
    }

    public boolean isUsing() {
        return isUsing.get();
    }

    public int getId() {
        return id;
    }
}
