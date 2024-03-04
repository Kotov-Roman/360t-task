package org.example;

import org.example.iterprocess.RemotePlayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * Entry point for creating a server that accepts a connection from a client.
 * Allowing the client to begin interprocess communication.
 */
public class Server {
    public static int PORT = 8085;

    public static void main(String[] args) throws IOException {
        String port = System.getProperty("server.port");
        if (port != null) {
            PORT = Integer.parseInt(port);
        }

        if (!IsPortAvailable(PORT)) {
            throw new RuntimeException(PORT + " port is not available, try to switch port");
        }

        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket socket = null;
        DataInputStream inputStream = null;
        DataOutputStream outputStream = null;
        try {
            // blocks until client connects
            socket = serverSocket.accept();
            System.out.println("A new client is connected : " + socket);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            //create player, start and wait for end of communication
            CountDownLatch latch = new CountDownLatch(1);
            RemotePlayer remotePlayer = new RemotePlayer("remote player", false, inputStream,
                    outputStream, latch);
            new Thread(remotePlayer).start();
            latch.await();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
            serverSocket.close();
            System.out.println("server shut down");
        }
    }

    private static boolean IsPortAvailable(int port) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", port);
            // If the code makes it this far without an exception it means
            // something is using the port and has responded.
            System.out.println("Port " + port + " is not available");
            return false;
        } catch (IOException e) {
            System.out.println("Port " + port + " is available");
            return true;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to close socket", e);
                }
            }
        }
    }
}
