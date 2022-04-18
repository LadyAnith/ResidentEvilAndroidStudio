package com.example.residentevil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;

/**
 * Clase personaje que hereda de la clase RecursoInterfazUsuario
 */
public class Personaje extends RecursoInterfazUsuario {

    private int pose = 1;
    private boolean sentidoOrdinal = true;
    private float hp_Jill;
    private MediaPlayer media;

    public Personaje(Context context, int x, int y, int recurso) {
        super(context, x, y, recurso);
        //Indica la posición a la que va a mirar el personaje
        this.direccion = DIRECCION_DERECHA;
        this.hp_Jill = 50;
    }

    /**
     * Método encarggado de dibujar al personaje
     * @param canvas
     */
    public void dibujarPersonaje(Canvas canvas) {
        int iCol = pose;
        int iRow = direccion;

        //Posiciones de las imágenes dentro del spriter
        int leftOrigen = imagenEscalada.getWidth() / 3 * iCol;
        int topOrigen = imagenEscalada.getHeight() / 4 * iRow;
        int rightOrigen = imagenEscalada.getWidth() / 3 * (iCol + 1);
        int bottomOrigen = imagenEscalada.getHeight() / 4 * (iRow + 1);

        //Ajusto la imagen al tamaño correspondiente de la pantalla
        int charTop = getCoordenadaY() ;
        int charRight = getCoordenadaX() + (imagenEscalada.getHeight() / 8);
        int charBottom = getCoordenadaY() + (imagenEscalada.getWidth() / 6);
        int charLeft = getCoordenadaX() ;

        //Nuevo objeto de la clase Colision
        super.colision = new Colision(charTop,charRight,charBottom,charLeft);

        //Pinta la imagen del spriter en el canvas en una posición en concreto
        canvas.drawBitmap(imagenEscalada,
                new Rect(leftOrigen, topOrigen, rightOrigen, bottomOrigen),
                new Rect(charLeft, charTop, charRight,charBottom),
                null);
    }

    /**
     * Método encargado de hacer que camine el personaje en la dirección adecuada con la postura correcta
     */
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

    /**
     * Método encargado de reproducir el sonido cuando Jill muere
     */
    public void sonidoJillHerida(){
        media= MediaPlayer.create(getContext(), R.raw.screaming);
        media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        media.start();
    }

    public float getHp_Jill() {
        return hp_Jill;
    }

    public void setHp_Jill(float hp_Jill) {
        this.hp_Jill = hp_Jill;
    }

    /**
     * Método encargado de bajar la vida a Jill
     */
    public void damageJill(){
        hp_Jill -= 1;
    }

}
