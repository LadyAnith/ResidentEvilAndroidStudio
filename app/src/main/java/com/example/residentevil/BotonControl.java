package com.example.residentevil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class BotonControl extends RecursoInterfazUsuario {
    public boolean pulsado; //indica si el control está pulsado o no

    public BotonControl(Context c, int x, int y, int recurso) {
        super(c, x, y, recurso);
    }

    public boolean isPulsado() {
        return pulsado;
    }

    public void dibujar(Canvas c, Paint p) {
        c.drawBitmap(imagen, coordenadaX, coordenadaY, p);
    }

    public void setPulsado(boolean pulsado) {
        this.pulsado = pulsado;
    }

}

