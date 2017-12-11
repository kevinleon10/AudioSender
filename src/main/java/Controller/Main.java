package Controller;
import sockets.Receiver;
import sockets.Sender;

import java.io.IOException;

/**
 * Created by lskev on 11-Dec-17.
 */
public class Main {
    public static void main(String args[]){
        /*try {
            (new Thread(new Receiver(1030))).start();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        try {
            (new Thread(new Sender(1030, "192.168.0.110"))).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
