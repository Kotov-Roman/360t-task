package org.example.singleProcess;

import org.example.core.Player;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Еhe class implements sending and receiving messages between different threads
 * within the same process via BlockingQueue
 */
public class LocalPlayer extends Player {
    private final Logger logger = Logger.getLogger(LocalPlayer.class.getName());

    private final BlockingQueue<String> incomingMessages;
    private final BlockingQueue<String> outgoingMessages;

    public LocalPlayer(String playerName,
                       boolean isInitiator,
                       BlockingQueue<String> incomingMessages,
                       BlockingQueue<String> outgoingMessages) {
        super(playerName, isInitiator);
        this.incomingMessages = incomingMessages;
        this.outgoingMessages = outgoingMessages;
    }

    @Override
    public String read() {
        return readAndWaitForMessage();
    }

    @Override
    public void send(String message) {
        sendMessageWithCounter(message);
    }

    private void sendMessageWithCounter(String msg) {
        String msgWithCounter = msg + ", " + getMessagesSentCounter();
        try {
            outgoingMessages.put(msgWithCounter);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Thread was interrupted");
            throw new RuntimeException(e);
        }
        incrementSend();
    }

    private String readAndWaitForMessage() {
        String msg;
        try {
            msg = incomingMessages.take();
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Thread was interrupted");
            throw new RuntimeException(e);
        }
        logger.info(String.format("%s has received message: %s", getPlayerName(), msg));
        incrementRead();
        return msg;
    }
}
