package com.example.residentevil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Enemigo extends RecursoInterfazUsuario {
    public static final int DIRECCION_ABAJO = 0;
    public static final int DIRECCION_IZQUIERDA = 1;
    public static final int DIRECCION_DERECHA = 2;
    public static final int DIRECCION_ARRIBA = 3;

    private int direccion = DIRECCION_IZQUIERDA;
    private int pose = 1;
    private boolean sentidoOrdinal = true;

    public Enemigo(Context context, int x, int y, int recurso) {
        super(context, x, y, recurso);
    }

    public void dibujarPersonaje(Canvas canvas) {
        int iCol = pose;
        int iRow = direccion;

        int leftOrigen = imagenEscalada.getWidth() / 3 * iCol;
        int topOrigen = imagenEscalada.getHeight() / 4 * iRow;
        int rightOrigen = imagenEscalada.getWidth() / 3 * (iCol + 1);
        int bottomOrigen = imagenEscalada.getHeight() / 4 * (iRow + 1);

        int charLeft = getCoordenadaX() - (imagenEscalada.getWidth() / 6);
        int charTop = getCoordenadaY() - (imagenEscalada.getHeight() / 8);
        int charRight = getCoordenadaX();
        int charBottom = getCoordenadaY();

        canvas.drawBitmap(imagenEscalada,
                new Rect(leftOrigen, topOrigen, rightOrigen, bottomOrigen),
                new Rect(charLeft, charTop, charRight,charBottom),
                null);
    }

    public int getDireccion() {
        return direccion;
    }

    public void setDireccion(int direccion) {
        this.direccion = direccion;
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