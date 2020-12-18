package test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        for(int i = 0; i < 10; i++) {
            new Thread(new Work(countDownLatch, i+"")).start();
        }
        countDownLatch.await();
        System.out.println("end");
    }
}

class Work implements Runnable {

    private CountDownLatch countDownLatch;
    private String name;

    public Work(CountDownLatch countDownLatch, String name) {
        this.countDownLatch = countDownLatch;
        this.name = name;
    }

    public Work() {

    }

    @Override
    public void run() {
        try {
            this.doWork();
            this.countDownLatch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doWork() {
        System.out.println(this.name + " working");
        try {
            Thread.sleep(new Random().nextInt(5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.name + " finish");
    }
}
