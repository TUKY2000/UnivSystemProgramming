package com.tukY.Lab4.v2;

import com.tukY.Lab4.Philosopher;
import com.tukY.Lab4.Fork;

import java.util.concurrent.TimeUnit;

public class PhilosopherReq extends Philosopher {
    private final Waiter waiter;

    public PhilosopherReq(String name, Fork leftFork, Fork rightFork, Waiter waiter) {
        super(name, leftFork, rightFork);
        this.waiter = waiter;
    }

    private boolean request(Fork fork){
        return waiter.request(this, fork);
    }

    @Override
    protected boolean TakeForks() {
        return TakeFork(forkL) && TakeFork(forkR);
    }

    @Override
    protected boolean TakeFork(Fork fork) {
        synchronized (waiter){
            if (request(fork))
                return fork.get();
            return false;
        }
    }

    @Override
    protected void waitForFork(Fork fork) {
        System.out.println(getName() + "\t did not get forks");
    }

    @Override
    public void run() {
        long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(30);

        while (System.nanoTime() < stop) {
            speak();
            while (!TakeForks()) {
                waitForFork(null);
                speak();
            }
            eat();
        }
    }
}
