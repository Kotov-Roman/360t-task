package org.example.core;

import java.util.logging.Logger;

/**
 * The class provides common logic for players of different types,
 * leaving the need to write communication details in its descendants
 */
public abstract class Player implements Runnable, Messenger {
    public static final String INITIAL_MESSAGE = "Some initial message";
    private static final int READ_SEND_LIMIT = 10;
    private final Logger logger = Logger.getLogger(Player.class.getName());

    private final String playerName;
    private final boolean isInitiator;

    private int messagesReadCounter;
    private int messagesSentCounter;

    public Player(String playerName, boolean isInitiator) {
        this.playerName = playerName;
        this.isInitiator = isInitiator;
    }

    @Override
    public void run() {
        if (isInitiator) {
            send(INITIAL_MESSAGE);
        }
        while (messagesReadCounter < READ_SEND_LIMIT || messagesSentCounter < READ_SEND_LIMIT) {
            String msg = "";
            //double-check for loop because one player starts from reading, another starts from sending
            if (messagesReadCounter < READ_SEND_LIMIT) {
                msg = read();
            }
            if (messagesSentCounter < READ_SEND_LIMIT) {
                send(msg);
            }
        }
        logger.info(playerName + " has sent and read all messages");
    }

    public void incrementRead() {
        messagesReadCounter++;
    }

    public void incrementSend() {
        messagesSentCounter++;
    }

    public int getMessagesReadCounter() {
        return messagesReadCounter;
    }

    public int getMessagesSentCounter() {
        return messagesSentCounter;
    }

    public String getPlayerName() {
        return playerName;
    }

}
