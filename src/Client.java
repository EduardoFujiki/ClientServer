import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client implements Runnable {
    public static void createClient() {
        String serverAddress = "192.168.1.3"; // Endereço do servidor
        int serverPort = 34254; // Porta do servidor

        try (DatagramSocket socket = new DatagramSocket()) {
            System.out.println("Conectado ao servidor.");

            int count = 5000;
            String userInput;
            InetAddress serverInetAddress = InetAddress.getByName(serverAddress);

            String[] alfabeto = new String[26];
            // Preencher o vetor com as letras do alfabeto
            for (char letra = 'a'; letra <= 'z'; letra++) {
                alfabeto[letra - 'a'] = String.valueOf(letra);
            }
            int alfcounter = 0;
            for (int i = 0; i < count; i++) {

                if (alfcounter == 26) {
                    alfcounter = 0;
                } else {
                    userInput = alfabeto[alfcounter];
                    byte[] sendData = userInput.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverInetAddress, serverPort);
                    socket.send(sendPacket);
                    System.out.println(userInput);
                    alfcounter++;
                }

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

    public static void main(String[] args) {
        int clientsCounter = 1;

        for (int i = 0; i < clientsCounter; clientsCounter++) {
            Client client = new Client();
            new Thread(client).start();
        }
    }

    @Override
    public void run() {
        this.createClient();
    }
}
