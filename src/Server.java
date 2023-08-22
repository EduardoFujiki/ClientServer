import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static Map<String, Integer> clientIds = new HashMap<>();
    private static int nextClientId = 1;

    public static void main(String[] args) {
        int portNumber = 12345; // Port number for the UDP server
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        try (DatagramSocket serverSocket = new DatagramSocket(portNumber)) {
            System.out.println("Server started");

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                serverSocket.receive(receivePacket);
                String clientAddress = receivePacket.getAddress().getHostAddress();
                int clientPort = receivePacket.getPort();
                String clientKey = clientAddress + ":" + clientPort;

                // Get the client's ID or assign a new one based on the client's IP and port
                int clientId = getClientId(clientKey);

                System.out.println("Client (ID: " + clientId + ") connected from: " + clientKey);

                // Create a new thread for each client with their unique ID
                ClientHandler clientHandler = new ClientHandler(serverSocket, receivePacket, clientId);
                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getClientId(String clientKey) {
        return clientIds.computeIfAbsent(clientKey, k -> nextClientId++);
    }
}

class ClientHandler implements Runnable {
    private DatagramSocket serverSocket;
    private DatagramPacket receivePacket;
    private int clientId;

    public ClientHandler(DatagramSocket serverSocket, DatagramPacket receivePacket, int clientId) {
        this.serverSocket = serverSocket;
        this.receivePacket = receivePacket;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try {
            // Criar um arquivo para cada cliente e armazenar mensagens nele
            String fileName = "client_" + clientId + ".txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                writer.write(message);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}