package org.example.iterprocess;

import org.example.core.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

/**
 * The class implements sending and receiving messages for interprocess communication
 * through DataInputStream and DataOutputStream.
 * CountDownLatch allows to notify another thread that communication has completed.
 */
public class RemotePlayer extends Player {
    private final Logger logger = Logger.getLogger(RemotePlayer.class.getName());

    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    private final CountDownLatch latch;
    private boolean isCommunicationFinished;

    public RemotePlayer(String playerName,
                        boolean isInitiator,
                        DataInputStream inputStream,
                        DataOutputStream outputStream, CountDownLatch latch) {
        super(playerName, isInitiator);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.latch = latch;
    }

    @Override
    public void run() {
        super.run();
        isCommunicationFinished = true;
        latch.countDown();
    }

    @Override
    public String read() {
        String message;
        try {
            message = inputStream.readUTF();
            incrementRead();
        } catch (IOException e) {
            logger.info("Got error during reading");
            throw new RuntimeException(e);
        }
        logger.info(String.format("%s has received message: %s", getPlayerName(), message));
        return message;
    }

    @Override
    public void send(String message) {
        String msgWithCounter = message + ", " + getMessagesSentCounter();
        logger.info(String.format("%s is sending message: %s", getPlayerName(), msgWithCounter));

        try {
            outputStream.writeUTF(msgWithCounter);
            incrementSend();
        } catch (IOException e) {
            logger.info("Got error during sending");
            throw new RuntimeException(e);
        }
    }

    public boolean isCommunicationFinished() {
        return isCommunicationFinished;
    }
}
