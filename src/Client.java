import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Endereço do servidor
        int serverPort = 12345; // Porta do servidor

        try (DatagramSocket socket = new DatagramSocket()) {
            System.out.println("Conectado ao servidor.");

            int count = 600;
            String userInput;
            InetAddress serverInetAddress = InetAddress.getByName(serverAddress);

            for (int i = 0; i < count; i++) {
                userInput = "Numero:" + i;
                count++;
                System.out.println(userInput);
                if (userInput.equals("Numero:600")) {
                    break;
                }

                byte[] sendData = userInput.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverInetAddress, serverPort);
                socket.send(sendPacket);

                try {
                    Thread.sleep(1000); //wait 1 sec for each message
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
