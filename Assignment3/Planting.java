// Name: Saja Elkurtehi
// Student id: 300288667
//
// Assignment 3
//
import java.util.concurrent.Semaphore;

public class Planting {
    public static void main(String args[]) {
        // Create Student, TA, Professor threads
        TA ta = new TA();
        Professor prof = new Professor(ta);
        Student stdnt = new Student(ta);

        // Start the threads
        prof.start();
        ta.start();
        stdnt.start();

        // Wait for prof to call it quits
        try {
            prof.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Terminate the TA and Student Threads
        ta.interrupt();
        stdnt.interrupt();
    }
}

class Student extends Thread {
    TA ta;

    public Student(TA taThread) {
        ta = taThread;
    }

    public void run() {
        while (!isInterrupted()) {
            try {
                ta.maxHoles.acquire(); // Wait if student is more than MAX holes ahead
                ta.shovel.acquire(); // Acquire the shovel
                System.out.println("Student: Got the shovel");
                try {
                    sleep((int) (100 * Math.random())); // Time to dig hole
                } catch (InterruptedException e) {
                    break;
                }
                ta.incrHoleDug(); // Hole dug - increment the number
                System.out.println("Student: Hole " + ta.getHoleDug() + " Dug");
                ta.shovel.release(); // Release the shovel
                System.out.println("Student: Letting go of the shovel");
                ta.emptyHoles.release(); // Signal that a new hole is ready for seeding
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println("Student is done");
    }
}

class TA extends Thread {
    // Some variables to count number of holes dug and filled - the TA keeps track of things
    private int holeFilledNum = 0; // number of the hole filled
    private int holePlantedNum = 0; // number of the hole planted
    private int holeDugNum = 0; // number of hole dug
    private final int MAX = 5; // can only get 5 holes ahead

    // Semaphores
    Semaphore emptyHoles = new Semaphore(0); // Holes ready for seeds
    Semaphore unfilledHoles = new Semaphore(0); // Holes with seeds ready to be filled
    Semaphore shovel = new Semaphore(1); // Only one shovel available
    Semaphore maxHoles = new Semaphore(MAX); // Control the max holes student can dig ahead

    public int getMAX() {
        return (MAX);
    }

    public void incrHoleDug() {
        holeDugNum++;
    }

    public int getHoleDug() {
        return (holeDugNum);
    }

    public void incrHolePlanted() {
        holePlantedNum++;
    }

    public int getHolePlanted() {
        return (holePlantedNum);
    }

    public TA() {
        // Initialise things here
    }

    public void run() {
        while (!isInterrupted()) {
            try {
                unfilledHoles.acquire(); // Wait for a hole with a seed to be ready for filling
                shovel.acquire(); // Acquire the shovel
                System.out.println("TA: Got the shovel");
                try {
                    sleep((int) (100 * Math.random())); // Time to fill hole
                } catch (InterruptedException e) {
                    break;
                }
                holeFilledNum++; // Hole filled - increment the number
                System.out.println("TA: The hole " + holeFilledNum + " has been filled");
                shovel.release(); // Release the shovel
                System.out.println("TA: Letting go of the shovel");
                maxHoles.release(); // Allow the student to dig another hole
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println("TA is done");
    }
}

class Professor extends Thread {
    TA ta;

    public Professor(TA taThread) {
        ta = taThread;
    }

    public void run() {
        while (ta.getHolePlanted() <= 20) {
            try {
                ta.emptyHoles.acquire(); // Wait for a dug hole to be ready
                try {
                    sleep((int) (50 * Math.random())); // Time to plant
                } catch (InterruptedException e) {
                    break;
                }
                ta.incrHolePlanted(); // The seed is planted - increment the number
                System.out.println("Professor: All be advised that I have completed planting hole " + ta.getHolePlanted());
                ta.unfilledHoles.release(); // Signal that a seed has been planted
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println("Professor: We have worked enough for today");
    }
}
