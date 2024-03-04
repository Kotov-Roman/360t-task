package org.example;

import org.example.iterprocess.RemotePlayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;

/**
 * Entry point for creating a client that connects to the server via a socket for interprocess communication.
 * Allows player to start its task in another thread
 */
public class Client {

    public static int PORT = 8085;

    public static void main(String[] args) throws UnknownHostException {
        String port = System.getProperty("server.port");
        if (port != null) {
            PORT = Integer.parseInt(port);
        }

        InetAddress ip = InetAddress.getByName("localhost");

        try (Socket s = new Socket(ip, PORT);
             DataInputStream inputStream = new DataInputStream(s.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(s.getOutputStream())) {

            CountDownLatch latch = new CountDownLatch(1);
            RemotePlayer remotePlayer = new RemotePlayer("remote Initiator", true, inputStream,
                    outputStream, latch);
            new Thread(remotePlayer).start();
            latch.await();

            if (!remotePlayer.isCommunicationFinished()) {
                throw new IllegalStateException("Something went wrong during messaging from client side");
            }
            System.out.println("messaging is finished on client side");
            System.out.println("client shut down");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
