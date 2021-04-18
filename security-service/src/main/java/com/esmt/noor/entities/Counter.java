package com.esmt.noor.entities;

public class Counter {
    public static int getCounter() {
        return counter;
    }

    public  static void increment(){
        counter ++;
    }

    private static int counter=0;



}
