package com.michael.leetcode.thread;

public class FooBar {

    private int n;

    private volatile boolean fooExc;

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {

        for (int i = 0; i < n; i++) {

            synchronized (this) {
                notifyAll();
                printFoo.run();
                fooExc = true;
                wait();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {

        for (int i = 0; i < n; i++) {

            synchronized (this) {

                while(!fooExc) {
                    wait();
                }
                printBar.run();
                fooExc = false;
                notifyAll();

            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        FooBar fooBar = new FooBar(1);


        Thread Bar = new Thread(() -> {
            try {
                fooBar.bar(() -> {System.out.println("Bar");});
            } catch (InterruptedException e) {
            }
        });


        Bar.start();

        Thread Foo = new Thread(() -> {
            try {
                fooBar.foo(() -> {System.out.print("Foo");});
            } catch (InterruptedException e) {
            }
        });
        Foo.start();
    }

}
