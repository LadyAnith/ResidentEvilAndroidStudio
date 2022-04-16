package com.example.residentevil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Personaje extends RecursoInterfazUsuario {

    private int pose = 1;
    private boolean sentidoOrdinal = true;

    public Personaje(Context context, int x, int y, int recurso) {
        super(context, x, y, recurso);
        this.direccion = DIRECCION_DERECHA;
    }

    public void dibujarPersonaje(Canvas canvas) {
        int iCol = pose;
        int iRow = direccion;

        int leftOrigen = imagenEscalada.getWidth() / 3 * iCol;
        int topOrigen = imagenEscalada.getHeight() / 4 * iRow;
        int rightOrigen = imagenEscalada.getWidth() / 3 * (iCol + 1);
        int bottomOrigen = imagenEscalada.getHeight() / 4 * (iRow + 1);

        //Ajusto la imagen al tamaño correspondiente
        int charTop = getCoordenadaY() ;
        int charRight = getCoordenadaX() + (imagenEscalada.getHeight() / 8);
        int charBottom = getCoordenadaY() + (imagenEscalada.getWidth() / 6);
        int charLeft = getCoordenadaX() ;

        super.colision = new Colision(charTop,charRight,charBottom,charLeft);

        canvas.drawBitmap(imagenEscalada,
                new Rect(leftOrigen, topOrigen, rightOrigen, bottomOrigen),
                new Rect(charLeft, charTop, charRight,charBottom),
                null);
    }

    public void caminar(){
        if (sentidoOrdinal)
            pose ++;
        if (!sentidoOrdinal)
            pose --;

        if (pose>=2){
            sentidoOrdinal = false;
            pose = 2;
        }
        if (pose==0){
            sentidoOrdinal = true;
            pose = 0;
        }
    }
}
