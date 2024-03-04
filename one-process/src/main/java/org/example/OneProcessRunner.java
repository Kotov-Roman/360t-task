package org.example;

import org.example.core.Player;
import org.example.singleProcess.LocalPlayer;
import java.util.concurrent.SynchronousQueue;

/**
 * Entry point for starting communication between different threads within the same process via SynchronousQueue
 */
public class OneProcessRunner {
    public static void main(String[] args) {
        SynchronousQueue<String> firstQueue = new SynchronousQueue<>();
        SynchronousQueue<String> secondQueue = new SynchronousQueue<>();

        Player initiator = new LocalPlayer("Initiator", true, firstQueue, secondQueue);
        Player secondPlayer = new LocalPlayer("Second player", false, secondQueue, firstQueue);

        new Thread(initiator).start();
        new Thread(secondPlayer).start();
    }
}