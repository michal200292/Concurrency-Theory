package org.task;


import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        Test testing = new Test(20, 100, false);
        testing.test();
    }
}