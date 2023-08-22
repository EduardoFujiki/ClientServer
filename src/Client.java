import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Change this to the server's address
        int serverPort = 12345; // Change this to the server's port

        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server.");
            System.out.println("Enter text to send to the server. Type 'exit' to quit.");

            int count = 600;
            String userInput;
            for(int i = 0; i<count; i++){
                userInput = "Numero:" + i;
                count++;
                System.out.println(userInput);
                if (userInput.equals("Numero:600")) {
                    break;
                }
                writer.write(userInput);
                writer.newLine();
                writer.flush();
                try {
                    Thread.sleep(1000); // Espera 1 segundo entre cada mensagem
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
