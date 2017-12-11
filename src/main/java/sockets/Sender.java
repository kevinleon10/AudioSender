package sockets;

import java.io.*;
import java.net.Socket;

/**
 * Created by lskev on 6-Nov-17.
 */
public class Sender implements Runnable{
    //variables del emisor
    private Socket clientSocket;
    private DataOutputStream outServer = null;
    private boolean connected = false;
    private int port;
    private String host;

    /**
     * Establece los parámetros de a quién se le va a enviar
     * @param port
     * @param host
     * @throws IOException
     */
    public Sender(int port, String host) throws IOException {
        this.port = port;
        this.host = host;
    }

    /**
     * Intenta conectarse y enviar el mensaje
     * @throws IOException
     * @throws InterruptedException
     */
    public void startSender() throws IOException, InterruptedException {
        Boolean trying = false;
        while (!connected) {
            try {
                if (trying) {
                    System.out.print("Reintentando conectarse al servidor " + host + "\n");
                }
                //para enviar audio
                BufferedInputStream bufferedInputStream = null;
                BufferedOutputStream bufferedOutputStream = null;
                File file = null;
                byte byteArray[];
                int in;

                clientSocket = new Socket(host, port);

                //Para enviar audio
                file = new File("slr.wav");
                bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                bufferedOutputStream = new BufferedOutputStream(clientSocket.getOutputStream());

                outServer = new DataOutputStream(clientSocket.getOutputStream());
                outServer.writeUTF(file.getName());

                byteArray = new byte[8192];

                while((in=bufferedInputStream.read(byteArray))!=-1){
                    bufferedOutputStream.write(byteArray, 0, in);
                }
                bufferedInputStream.close();
                bufferedOutputStream.close();
                System.out.println("Se envio toda la cancion");
                connected = true;
            } catch (Exception e) {
                connected = false;
                Thread.sleep(150000);
            }
        }
        clientSocket.close();
    }

    /**
     * Método para correr hilo
     */
    public void run() {
        try {
            startSender();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
