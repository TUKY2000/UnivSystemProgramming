package com.tukY.Lab4.v2;

import com.tukY.Lab4.Fork;
import com.tukY.Lab4.Philosopher;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.tukY.Lab4.utils.*;

public class Lab4 {
    public static void main(String[] args) {
        final int count = Integer.parseInt(args[0]);

        List<Fork> Forks = Stream.generate(Fork::new)
                .limit(count)
                .collect(Collectors.toList());

        Waiter waiter = new Waiter();

        List<Philosopher> Philosophers = Stream.concat(Stream
                        .of(new PhilosopherReq("0", Forks.get(0), Forks.get(count-1), waiter))
                ,IntStream.range(0, count - 1)
                        .mapToObj(i -> new PhilosopherReq(String.valueOf(i + 1), Forks.get(i), Forks.get(i+1), waiter)))
                .peek(Thread::start)
                .collect(Collectors.toList());

        try {
            for (var philosopher : Philosophers) {
                philosopher.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        eatenOut(Philosophers);
        ForksDown(Forks);
    }
}
