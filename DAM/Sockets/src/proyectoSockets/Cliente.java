package proyectoSockets;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Cliente {

    public static void main(String[] args) {
        String ip = "localhost";
        int puerto = 12345;

        try {
            Socket socket = new Socket(ip, puerto);
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            while (true) {
                System.out.print("Ingrese un mensaje para enviar al servidor: ");
                String mensaje = input.readLine();
                out.writeBytes(mensaje + "\n");
                String respuesta = in.readLine();
                System.out.println("Mensaje recibido: " + respuesta);

                if (mensaje.equals("#0#")) {
                    System.out.println("Cerrando la conexión...");
                    break;
                }
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
