package fh_swf.mechatronik.classes;

import fh_swf.mechatronik.model.MainModel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class WifiReceiver extends Thread {

    private DatagramSocket receiver;
    private byte[] recData;
    private MainModel data = MainModel.getInstance();
    private ScheduledExecutorService scheduler;


    WifiReceiver(DatagramSocket udpSocket) throws SocketException {

        receiver = udpSocket;
        recData = new byte[256];
        scheduler = Executors.newScheduledThreadPool(1);
        udpSocket.setSoTimeout(100000);
    }

    @Override
    public void run()
    {
        receiveData();
    }


    private void receiveData()
    {

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                DatagramPacket rec = new DatagramPacket(recData, recData.length);
                try {
                    receiver.receive(rec);
                    System.out.println("ERHALTEN");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String sentence = new String(rec.getData(), 0, rec.getLength());
                data.setReceivedMessage(sentence);
            }
        },0,1, TimeUnit.SECONDS);
    }

    void cancel()
    {
        receiver.close();
        scheduler.shutdown();
    }


}
