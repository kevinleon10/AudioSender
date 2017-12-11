package sockets;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import javax.sound.sampled.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/**
 * Created by lskev on 6-Nov-17.
 */
public class Receiver implements Runnable{
    //variables necesarias para recibir mensajes
    private String serverMessage;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int port;

    /**
     * Establece los valores del servidor
     * @param port
     * @throws IOException
     */
    public Receiver(int port) throws IOException {
        this.port = port;
    }

    /**
     * Empieza a recibir
     * @throws IOException
     */
    public void startReceiver() throws IOException {
        while (true) {
            clientSocket = serverSocket.accept();
            Thread t = new ListenToClientThread();
            t.start();
        }
    }

    /**
     * Método para correr el hilo
     */
    public void run() {
        try {
            this.serverSocket = new ServerSocket(port);
            this.clientSocket = new Socket();
            this.startReceiver();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clase que se encarga de procesar los mensajes que llegan
     */
    class ListenToClientThread extends Thread {

        ListenToClientThread() {
        }

        /**
         * Metodo para correr el hilo y ver que hacer para cada caso
         */
        @Override
        public void run() {
            BufferedReader input = null;
            byte byteArray[] = new byte[1024];
            BufferedInputStream bufferedInputStream = null;
            try {
               bufferedInputStream  = new BufferedInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            DataInputStream dataInputStream = null;
            try {
                dataInputStream = new DataInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String file = null;
            try {
                file = dataInputStream.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file = file.substring(file.indexOf("\\")+1, file.length());
            BufferedOutputStream bufferedOutputStream = null;
            try {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            int in;
            try {
                while((in=bufferedInputStream.read(byteArray))!=-1){
                    bufferedOutputStream.write(byteArray, 0, in);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                dataInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BasicPlayer player = new BasicPlayer();
            String songName = "strings.wav";
            String pathToMp3 = System.getProperty("user.dir") +"/"+ songName;
            try {
                player.open(new URL("file:///" + pathToMp3));
                player.play();
            } catch (BasicPlayerException | MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(5*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }
}
