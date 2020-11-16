package com.tukY.Lab4;

public class Philosopher extends Thread {

    protected final Fork forkL;
    protected final Fork forkR;
    protected int eaten;

    public Philosopher(String name, Fork leftFork, Fork rightFork) {
        super("Philosopher " + name);
        this.forkL = leftFork;
        this.forkR = rightFork;
        this.eaten = 0;
    }

    protected boolean TakeForks(){
        Fork f1, f2;
        if (forkL.getId() < forkR.getId()){
            f1 = forkL;
            f2 = forkR;
        } else{
            f1 = forkR;
            f2 = forkL;
        }

        return TakeFork(f1) && TakeFork(f2);
    }
    protected boolean TakeFork(Fork fork){
        return fork.get();
    }

    protected boolean PutFork(Fork fork){
        return fork.put();
    }
    protected void PutForks() {
        forkL.put();
        forkR.put();
    }


    public Fork getLeftFork() {
        return forkL;
    }

    public Fork getRightFork() {
        return forkR;
    }

    protected void eat() {
        System.out.println(getName() + "\t is eating");

        try {
            Thread.sleep(2300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(getName() + "\t finished eating");
        eaten++;
        PutForks();
    }

    public int EatenCount() {
        return eaten;
    }

    protected void speak() {
        System.out.println(getName() + "\t is speaking");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void waitForFork(Fork fork){
        System.out.println(getName() + "\t is waiting for fork");

        while (!TakeFork(fork)) { Thread.onSpinWait(); }
    }

    @Override
    public void run() {
        speak();

        if (!TakeFork(forkL))   waitForFork(forkL);
        if (!TakeFork(forkR))   waitForFork(forkR);

        eat();
        speak();
    }
}
