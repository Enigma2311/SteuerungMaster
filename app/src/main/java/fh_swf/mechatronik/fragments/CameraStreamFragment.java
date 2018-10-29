package fh_swf.mechatronik.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.Toast;
import fh_swf.mechatronik.model.OptionsModel;
import fh_swf.mechatronik.R;
import fh_swf.mechatronik.classes.SpsParser;
import fh_swf.mechatronik.classes.TcpConnection;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link CameraStreamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraStreamFragment extends Fragment implements TextureView.SurfaceTextureListener {

    private TextureView textureView;
    private DecoderThread decoder;

    public CameraStreamFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment CameraStreamFragment.
     */


    static CameraStreamFragment newInstance() {

        CameraStreamFragment fragment = new CameraStreamFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View view = inflater.inflate(R.layout.fragment_camera_stream, container, false);
      textureView = view.findViewById(R.id.textureView);
      textureView.setSurfaceTextureListener(this);
      return view;
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        stop();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        decoder = new DecoderThread();
        decoder.start();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    //******************************************************************************
    // onSurfaceTextureSizeChanged
    //******************************************************************************
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height)
    {
    }

    //******************************************************************************
    // onSurfaceTextureDestroyed
    //******************************************************************************
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture)
    {
        if (decoder != null)
        {
            decoder.setSurface(null);
        }
        return true;
    }

    //******************************************************************************
    // onSurfaceTextureUpdated
    //******************************************************************************
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture)
    {
    }

    //******************************************************************************
    // onSurfaceTextureAvailable
    //******************************************************************************
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height)
    {
        if (decoder != null)
        {
            decoder.setSurface(new Surface(surfaceTexture));
        }
    }

    void stop()
    {

        if(decoder != null)
        {
            Toast.makeText(getActivity(), "Video wird beendet..", Toast.LENGTH_LONG).show();
            decoder.interrupt();
            try {
                decoder.join(TcpConnection.IO_TIMEOUT*2);
            }
            catch (Exception ex){}
            decoder = null;
            textureView = null;
        }
    }

    private class DecoderThread extends Thread
    {

        private final static int TCPIP_BUFFER_SIZE = 16384;
        private final static int NAL_SIZE_INC = 4096;
        private final static int MAX_READ_ERRORS = 300;

        private MediaCodec decoder = null;
        private MediaFormat format;
        private boolean decoding = false;
        private Surface surface;
        private byte[] buffer = null;
        private ByteBuffer[] inputBuffers = null;
        private long presentationTime;
        private long presentationTimeInc = 66666;
        private TcpConnection reader = null;
        private OptionsModel optionsData = OptionsModel.getInstance();

        //******************************************************************************
        // setSurface
        //******************************************************************************
        public void setSurface(Surface surface)
        {
            this.surface = surface;

            if (decoder != null)
            {
                if (surface != null)
                {
                    boolean newDecoding = decoding;
                    if (decoding)
                    {
                        setDecodingState(false);
                    }
                    if (format != null)
                    {
                        try
                        {
                            decoder.configure(format, surface, null, 0);
                        }
                        catch (Exception ex) {}
                        if (!newDecoding)
                        {
                            newDecoding = true;
                        }
                    }
                    if (newDecoding)
                    {
                        setDecodingState(newDecoding);
                    }
                }
                else if (decoding)
                {
                    setDecodingState(false);
                }
            }
        }

        //******************************************************************************
        // getMediaFormat
        //******************************************************************************
        public MediaFormat getMediaFormat()
        {
            return format;
        }

        //******************************************************************************
        // setDecodingState
        //******************************************************************************
        private synchronized void setDecodingState(boolean newDecoding)
        {
            try
            {
                if (newDecoding != decoding && decoder != null)
                {
                    if (newDecoding)
                    {
                        decoder.start();
                    }
                    else
                    {
                        decoder.stop();
                    }
                    decoding = newDecoding;
                }
            } catch (Exception ex) {}
        }

        //******************************************************************************
        // run
        //******************************************************************************
        @Override
        public void run()
        {
            byte[] nal = new byte[NAL_SIZE_INC];
            int nalLen = 0;
            int numZeroes = 0;
            int numReadErrors = 0;

            try
            {

                //Decoder erstellen über die MediaCodec-Klasse
                decoder = MediaCodec.createDecoderByType("video/avc");

                buffer = new byte[TCPIP_BUFFER_SIZE];

                reader = new TcpConnection(optionsData.getIpAddress(),5001);

                if (!reader.isConnected())
                {
                    throw new Exception();
                }

                while (!isInterrupted())
                {
                    //Den ankommenden Datenstrom in den Buffer lesen.
                    int len = reader.read(buffer);
                    if (isInterrupted()) break;

                    //Die Daten im Buffer weiterverarbeiten
                    if (len > 0)
                    {
                        numReadErrors = 0;
                        for (int i = 0; i < len && !isInterrupted(); i++)
                        {
                            //Bufferdaten ins NAL-Array kopieren.
                            if (nalLen == nal.length)
                            {
                                nal = Arrays.copyOf(nal, nal.length + NAL_SIZE_INC);
                            }
                            nal[nalLen++] = buffer[i];

                            //Header erkennen
                            if (buffer[i] == 0)
                            {
                                numZeroes++;
                            }
                            else
                            {
                                if (buffer[i] == 1 && numZeroes == 3)
                                {
                                    if (nalLen > 4)
                                    {
                                        //Daten in processNal verarbeiten
                                        int nalType = processNal(nal, nalLen - 4);
                                        if (isInterrupted()) break;
                                        if (nalType == -1)
                                        {
                                            nal[0] = nal[1] = nal[2] = 0;
                                            nal[3] = 1;
                                        }
                                    }
                                    nalLen = 4;
                                }
                                numZeroes = 0;
                            }
                        }
                    }
                    else
                    {
                        numReadErrors++;
                        if (numReadErrors >= MAX_READ_ERRORS)
                        {
                            setMessage("Verbindung verloren!");
                            break;
                        }
                    }

                    //outputBuffer an die Surface senden und somit Bild darstellen.
                    if (format != null && decoding)
                    {
                        if (isInterrupted()) break;
                        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                        int index;
                        do
                        {
                            index = decoder.dequeueOutputBuffer(info, 0);
                            if (isInterrupted()) break;
                            if (index >= 0)
                            {
                                decoder.releaseOutputBuffer(index, true);
                            }
                        } while (index >= 0);
                    }
                }
            }
            catch (Exception ex)
            {
                if (reader == null || !reader.isConnected())
                {
                    setMessage("Verbindungsfehler!");
                    ex.printStackTrace();
                }
                else
                {
                    setMessage("Verbindung verloren!");
                    ex.printStackTrace();
                }

            }

            //reader schließen
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (Exception ex) {}
                reader = null;
            }

            //decoder stopen und Thread beenden
            if (decoder != null)
            {
                try
                {
                    setDecodingState(false);
                    decoder.release();
                }
                catch (Exception ex) {}
                decoder = null;
            }
            //alle einstellungen schließen
            optionsData = null;
            surface = null;
            format = null;
        }

        /**
         * Methode zur Verarbeitung der NAL-Einheiten.
         * @param nal
         * Byte-Array welches die NAL-Einheiten enthält
         * @param nalLen
         * Die Länge der Daten
         * @return
         * NAL-Type für weitere Verarbeitung
         */
        private int processNal(byte[] nal, int nalLen)
        {
            //Überprüfung des NAL-Typs
            int nalType = (nalLen > 4 && nal[0] == 0 && nal[1] == 0 && nal[2] == 0 && nal[3] == 1) ? (nal[4] & 0x1F) : -1;

            //Einstellungen der Optionen für die Bildanzeige (Auflösung und Bildwiederholungsrate)
            if (nalType == 7 && !decoding)
            {
                int width = 640;
                int height = 480;
                format = MediaFormat.createVideoFormat("video/avc", width, height);

                    format.setInteger(MediaFormat.KEY_FRAME_RATE, 20);
                    presentationTimeInc = 1000000 / 20;

                presentationTime = System.nanoTime() / 1000;
                decoder.configure(format, surface, null, 0);
                setDecodingState(true);
                //inputBuffers = decoder.getInputBuffers();
            }

            // Bild in den Buffer laden
            if (nalType > 0 && decoding)
            {
                int index = decoder.dequeueInputBuffer(0);
                if (index >= 0)
                {
                    //ByteBuffer inputBuffer = inputBuffers[index];
                    ByteBuffer inputBuffer = decoder.getInputBuffer(index);
                    inputBuffer.put(nal, 0, nalLen);
                    decoder.queueInputBuffer(index, 0, nalLen, presentationTime, 0);
                    presentationTime += presentationTimeInc;
                }
            }
            return nalType;
        }
        //Todo Schauen warum fehler.
        /**
         * Methode zur Anzeige von Fehlermeldungen
         * @param message
         * Nachricht die angezeigt werden soll.
         */
        private void setMessage(final String message)
        {
//            if(getActivity()!=null) {
//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
//                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
        }

    }
}





