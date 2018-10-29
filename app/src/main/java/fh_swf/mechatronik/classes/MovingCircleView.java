package fh_swf.mechatronik.classes;

import android.content.Context;
import android.graphics.*;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;
import fh_swf.mechatronik.model.MainModel;
import fh_swf.mechatronik.model.OptionsModel;

/**
 * View-Klasse welche einen Punkt auf eine Oberfläche zeichnert, welcher sich mit der Neigung des Geräts bewegt.
 * Created by zero_ on 13.06.2018.
 */
public class MovingCircleView extends View {

    private static final float CIRCLE_RADIUS = 0.025f;  // Konstante für den Radius des Punktes.
    // Todo variablen namen anpassen.
    private Bitmap background;                          // Hintergrund
    private Paint movingPoint;                          // Farbe des Punktes
    private float x;                                    // X-Koordinate
    private float y;                                    // Y-Koordinate
    private float scaleX;                               // Skalierung des X-Werts für verschiedene Bildschirmgrößen
    private float scaleY;                               // Skalierung des Y-Werts für verschiedene Bildschirmgrößen
    private RectF rimRect;                              // Rechteck, auf dem gezeichnet wird.
    private Paint backgroundPaint;                      // Hintergrundfarbe.
    private Paint rimPaint;                             // Hilfsvariable für Farbeinstellungen
    private MainModel mainData = MainModel.getInstance();  // Objekt der Hauptdatenklasse
    private OptionsModel optionsData = OptionsModel.getInstance();  //Objekt der Profildaten.

    /**
     * Konstruktor mit Kontext
     * @param context
     * Applikationskontext
     */

    public MovingCircleView(Context context) {
        super(context);
        initDrawingTools();
    }

    /**
     * Konstruktor mit Kontext und Attributsatz
     * @param context
     * Applikationskontext
     * @param attrs
     * Satz von Attributen
     */

    public MovingCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDrawingTools();
    }

    /**
     * Konstruktor mit Kontext, Attributsatz und Styletyp
     * @param context
     * Applikationskontext
     * @param attrs
     * Satz von Attributen
     * @param defStyle
     * Angabe des Styles
     */

    public MovingCircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initDrawingTools();
    }


    /**
     * Methode die bei Veränderung der Größe den Hintergrund wiederherstellt.
     * @param w
     * neue Breite
     * @param h
     * neue Höhe
     * @param oldw
     * alte Breite
     * @param oldh
     * alte Höhe
     */

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        regenerateBackground();

    }

    /**
     * Initialisierungen und Anpassungen für die Nutzung der View als Zeichenoberfläche.
     */

    private void initDrawingTools()
    {
        movingPoint = new Paint();
        rimRect = new RectF(0.0f, 0.0f, 1.0f, 1.0f);

        scaleX = ((rimRect.right - rimRect.left) / (100 *2));
        scaleY = ((rimRect.bottom - rimRect.top) / (100 *2));

        backgroundPaint = new Paint();
        backgroundPaint.setFilterBitmap(true);
        rimPaint = new Paint();
        rimPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        rimPaint.setColor(Color.GRAY);

        // Todo is blocked aus dem Programm entfernen.
    }

    /**
     * Methode welche den Punkt nach den vorgegebenen Koordinaten (Neigung) zeichnet.
     * @param canvas
     * Das Feld auf dem gezeichnet wird.
     */

    private void drawPoint(Canvas canvas)
    {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        movingPoint.setColor(Color.RED);
        canvas.drawCircle(this.x,this.y, CIRCLE_RADIUS, movingPoint);
        canvas.restore();

    }

    /**
     * Methode, welche den Hintergrund darstellt.
     * @param canvas
     * Das Feld auf dem die Hintergrunddarstellung stattfindet.
     */
    private void drawBackground(Canvas canvas)
    {
        if (background != null)
            canvas.drawBitmap(background, 0, 0, backgroundPaint);

    }

    /**
     * Methode, welche die X/Y Variablen für die Anzeige des Punkts mit Hilfe der Sensorwerte aktualisiert.
     * @param x
     * der angepasste X-Wert des Sensors
     * @param y
     * der angepasste Y-Wert des Sensors
     */

   public void updatePoint(float x, float y)
    {

        x = (x/9.81f) * 100*3.33f;
        y = (y/9.81f) * 100*3.33f;

        if(optionsData.getZero() == 15)
        {
            y -= 50;
        }
        else if(optionsData.getZero() == 30)
        {
            y -= 100;
        }
        if (x > 100)
        {
            x = 100;
        }
        if (x < -100)
        {
            x = -100;
        }
        if (y > 100)
        {
            y = 100;
        }
        if (y < -100)
        {
            y = -100;
        }

        this.x = scaleX * x + rimRect.centerX();
        this.y = scaleY * y + rimRect.centerY();

        this.invalidate();

    }

    /**
     * Methode welche die Canvas in der Größe skaliert.
     * @param canvas
     * Zeichenunterlage
     */

    @Override
    protected void onDraw(Canvas canvas)
    {

        drawBackground(canvas);

        float scale = (float) getWidth();
        float scale2 = (float) getHeight();
        //canvas.saveLayer(rimRect, rimPaint);
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(scale2,scale);
        drawPoint(canvas);
//        canvas.save(Canvas.MATRIX_SAVE_FLAG);
//        System.out.println("DRAWING!");
//        canvas.restore();
        canvas.restore();
    }

    /**
     * Prüfung der Bildschirmabmessungen um die Canvas richtig zu skalieren.
     * @param widthMeasureSpec
     * Prüfwert für die Breite.
     * @param heightMeasureSpec
     * Prüfwert für die Höhe.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int chosenWidth = chooseDimension(widthMode, widthSize);
        int chosenHeight = chooseDimension(heightMode, heightSize);

        int chosenDimension = Math.min(chosenWidth, chosenHeight);

        setMeasuredDimension(chosenDimension, chosenDimension);
    }

    /**
     * Auswahl der korrekten Canvas-Größe
     *
     * @param mode
     *          modus
     * @param size
     *          groeße
     * @return
     *          präferierte groeße
     */
    private int chooseDimension(int mode, int size)
    {
        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY)
        {
            return size;
        }
        else
        { // (mode == MeasureSpec.UNSPECIFIED)
            return getPreferredSize();
        }
    }

    /**
     * Hilfsmethode, falls keine präferierte Größe angegeben wird
     *
     * @return default preferred size.
     */
    private int getPreferredSize()
    {
        return 300;
    }

    /**
     * Wiederherstellung des Hintergrunds, falls benötigt.
     */
    private void regenerateBackground()
    {
        // free the old bitmap
        if (background != null)
        {
            background.recycle();
        }

        background = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas backgroundCanvas = new Canvas(background);
        float scale = (float) getWidth();
        float scale2 = (float) getHeight();
        backgroundCanvas.scale(scale2, scale);
        backgroundCanvas.drawRect(rimRect, rimPaint);

    }

}