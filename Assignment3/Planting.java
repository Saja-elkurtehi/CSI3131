/*
 * prog.c
 * Assignment 3 - CSI 3131 Summer 2024
 * Name: Saja Elkurehi
 * Student ID: 300288667
 */

import java.util.concurrent.Semaphore;

public class Planting
{

private
    static final int MAX = 5;
private
    static Semaphore emptyHoles = new Semaphore(0);
private
    static Semaphore filledHoles = new Semaphore(0);
private
    static Semaphore maxHoles = new Semaphore(MAX);
private
    static Semaphore shovel = new Semaphore(1);

public
    static void main(String[] args)
    {
        Thread studentThread = new Thread(new Student());
        Thread professorThread = new Thread(new Professor());
        Thread taThread = new Thread(new TA());

        studentThread.start();
        professorThread.start();
        taThread.start();

        try
        {
            studentThread.join();
            professorThread.join();
            taThread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    static class Student implements Runnable
    {
        @Override public void run()
        {
            try
            {
                while (true)
                {
                    maxHoles.acquire();
                    shovel.acquire();
                    System.out.println("Student: Dug a hole");
                    shovel.release();
                    emptyHoles.release();
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    static class Professor implements Runnable
    {
        @Override public void run()
        {
            try
            {
                while (true)
                {
                    emptyHoles.acquire();
                    System.out.println("Professor: Planted a seed");
                    filledHoles.release();
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    static class TA implements Runnable
    {
        @Override public void run()
        {
            try
            {
                while (true)
                {
                    filledHoles.acquire();
                    shovel.acquire();
                    System.out.println("TA: Filled a hole");
                    shovel.release();
                    maxHoles.release();
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
