package proyectoSockets;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

public class Server {
    public static void main(String[] args) {
        int portNumber = 3000;
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Servidor socket multihilo iniciado en el puerto " + portNumber + "...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                if (mensaje.equals("#0#")) {
                    out.writeBytes("#0#\n");
                    System.out.println("Fin de la conexión con el cliente " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
                    break;
                } else if (mensaje.startsWith("#1#")) {
                    String texto = mensaje.substring(3);
                    String encode = Base64.getEncoder().encodeToString(texto.getBytes());
                    out.writeBytes("#1#" + encode + "\n");
                } else if (mensaje.startsWith("#2#")) {
                    String textoencode = mensaje.substring(3);
                    String decode = new String(Base64.getDecoder().decode(textoencode));
                    out.writeBytes("#2#" + decode + "\n");
                } else {
                    out.writeBytes("#99#\n");
                }

            }
            in.close();
            out.close();
            clientSocket.close();
        } catch (Exception e) {
            System.out.println("Cliente desconectado: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
        }
    }
}
