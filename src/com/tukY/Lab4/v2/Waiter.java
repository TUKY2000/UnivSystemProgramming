package com.tukY.Lab4.v2;

import com.tukY.Lab4.Fork;
import com.tukY.Lab4.Philosopher;

import java.util.HashMap;
import java.util.Map;

public class Waiter{

    private final Map<Philosopher, Fork> reserved;

    public Waiter() {
        reserved = new HashMap<>();
    }

    public boolean request(Philosopher philosopher, Fork fork){
        if (reserved.containsKey(philosopher) && reserved.get(philosopher) == fork){
            reserved.remove(philosopher);
            return true;
        }
        if (reserved.containsValue(fork))
            return false;

        if (ForksNotFree(philosopher))
            return false;

        Fork another = philosopher.getLeftFork() == fork ?
                philosopher.getRightFork() : philosopher.getLeftFork();

        reserved.put(philosopher, another);
        return true;

    }

    private boolean ForksNotFree(Philosopher philosopher){
        return philosopher.getLeftFork().isUsing()
                || philosopher.getRightFork().isUsing();
    }

}
