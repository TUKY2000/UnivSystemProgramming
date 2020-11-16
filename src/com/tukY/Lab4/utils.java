package com.tukY.Lab4;

import java.util.List;

public final class utils {
    public static void eatenOut(List<Philosopher> philosophers){
        System.out.print("eaten\t=>\t[ ");
        philosophers.stream()
                .map(Philosopher::EatenCount)
                .forEach(i -> System.out.print(i + " "));
        System.out.println("]");
    }

    public static void ForksDown(List<Fork> forks){
        System.out.print("forks\t=>\t[ ");
        forks.stream()
                .map(Fork::isUsing)
                .forEach(b -> System.out.print(b + " "));
        System.out.println("]");
    }
}
