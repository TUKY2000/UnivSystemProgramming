package com.tukY.Lab4.v1;

import com.tukY.Lab4.Fork;
import com.tukY.Lab4.Philosopher;
import com.tukY.Lab4.v2.PhilosopherReq;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.tukY.Lab4.utils.*;

public class Lab4 {

    /**
     *
     * @param args number of philosophers
     */
    public static void main(String[] args) {

        final int count = Integer.parseInt(args[0]);

        List<Fork> Forks = IntStream.range(0, count)
                .mapToObj(Fork::new)
                .collect(Collectors.toList());


        List<Philosopher> Philosophers = Stream.concat(Stream
                        .of(new Philosopher("0", Forks.get(count-1), Forks.get(0)))
                ,IntStream.range(1, count)
                        .mapToObj(i -> new Philosopher(String.valueOf(i), Forks.get(i-1), Forks.get(i))))
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
    }
}
