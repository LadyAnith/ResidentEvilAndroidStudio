package com.example.residentevil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;

/**
 * Clase encargada del fondo del juego que hereda de RecursoInterfazUsuario
 */
public class Fondo extends RecursoInterfazUsuario {

    Display display;
    Rect screen;

    /**
     * Constructor
     * @param context como parámetro se le pasa el contexto
     */
    public Fondo(Context context) {
        super(context, 0, 0, R.drawable.fondo2);
        init();

    }

    /**
     * Método que inicializa los atributos de la clase Fondo
     */
    private void init() {
        int ancho;
        int alto;
        Point size = new Point();
        display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        display.getSize(size);
        //Ajusta el alto y ancho de la pantalla a la imagen
        ancho = size.x;
        alto = size.y;
        screen = new Rect(0, 0, ancho, alto);
    }

    /**
     * Método encargado de pintar el fondo del juego
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(imagenEscalada, null, screen, null);
    }

    //Getter & Setter
    @Override
    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public Rect getScreen() {
        return screen;
    }

    public void setScreen(Rect screen) {
        this.screen = screen;
    }
}
