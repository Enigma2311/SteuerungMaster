package fh_swf.mechatronik.classes;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Klasse der TCP/IP-Verbindung für den Kamera-Stream.
 * Created by Fabian Schäfer on 28.08.2018.
 */
public class TcpConnection {

    private Socket socket;
    private InputStream inputStream = null;
    public final static int IO_TIMEOUT = 1000;



    public TcpConnection (String ip, int port)
    {
        try {
            socket = getConnection(ip, port);
            socket.setSoTimeout(IO_TIMEOUT);
            inputStream = socket.getInputStream();
        }
        catch (Exception ex){
        }
    }

    private Socket getConnection(String ip, int port)
    {
        Socket mSocket;
        try {
            mSocket = new Socket();
            InetSocketAddress socketAddress = new InetSocketAddress(ip,port);
            mSocket.connect(socketAddress,IO_TIMEOUT);
        }
        catch (Exception ex)
        {
            mSocket = null;
        }

        return mSocket;
    }

    public int read(byte[] buffer)
    {
        try
        {
            return (inputStream != null) ? inputStream.read(buffer) : 0;
        }
        catch (IOException ex)
        {
            return 0;
        }
    }

    public boolean isConnected()
    {
        return (socket != null) ? socket.isConnected() : false;
    }

    public void close ()
    {
        if(inputStream != null)
        {
            try {
                inputStream.close();
            }
            catch (Exception ex)
            {
            }
            inputStream = null;
        }
        if(socket != null)
        {
            try {
                socket.close();
            }
            catch (Exception ex)
            {
            }
            socket = null;
        }
    }

}
