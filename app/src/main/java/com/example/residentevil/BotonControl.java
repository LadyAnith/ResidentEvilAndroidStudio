package com.example.residentevil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class BotonControl extends RecursoInterfazUsuario {

    //Con este booleano, se comprueba si el botón ha sido pulsado
    public boolean pulsado;

    /**
     * Método Constructor
     * @param c
     * @param x
     * @param y
     * @param recurso
     */
    public BotonControl(Context c, int x, int y, int recurso) {
        super(c, x, y, recurso);
    }

    //Getter & Setter
    public boolean isPulsado() {
        return pulsado;
    }

    public void setPulsado(boolean pulsado) {
        this.pulsado = pulsado;
    }

    /**
     * Método encargado de dibujar el control
     * @param c se le pasa un Objeto Canvas como parámetro
     * @param p se le pasa un objeto de la clase Paint como parámetro
     */
    public void dibujar(Canvas c, Paint p) {
        c.drawBitmap(imagen, coordenadaX, coordenadaY, p);
    }

}

