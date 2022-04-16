package com.example.residentevil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;

public class Disparo extends RecursoInterfazUsuario{

    private float velocidad;
    private MediaPlayer mediaPlayer; //para reproducir el sonido de disparo
    private final float MAX_SEGUNDOS_EN_CRUZAR_PANTALLA= 0.5f;
    private Personaje personajeQueDispara;

    public Disparo(Context context, Personaje personajeQueDispara, int recurso) {

        super(context, personajeQueDispara.getCoordenadaX(), personajeQueDispara.getCoordenadaY(), recurso);
        init(personajeQueDispara);
    }

    public void init(Personaje personajeQueDispara){
        velocidad=GameActivity.anchoPantalla /MAX_SEGUNDOS_EN_CRUZAR_PANTALLA/BucleJuego.MAX_FPS;
        this.personajeQueDispara = personajeQueDispara;
        this.direccion = personajeQueDispara.getDireccion();
        sonarDisparo();
    }

    public void dibujar(Canvas canvas) {
        int charTop = getCoordenadaY() ;
        int charRight = getCoordenadaX() + (imagenEscalada.getHeight() / 8);
        int charBottom = getCoordenadaY() + (imagenEscalada.getWidth() / 6);
        int charLeft = getCoordenadaX() ;

        super.colision = new Colision(charTop,charRight,charBottom,charLeft);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        super.dibujar(canvas, paint);

    }

        //se actualiza la coordenada y nada más
    public void actualizaCoordenadas(){
        super.coordenadaX += ((direccion == DIRECCION_DERECHA ? 1 : -1) * velocidad);
    }

    public boolean fueraDePantalla() {
        return super.coordenadaX < 0 || super.coordenadaX > GameActivity.anchoPantalla - getImagen().getWidth();
    }

    public void sonarDisparo(){
        mediaPlayer=MediaPlayer.create(getContext(), R.raw.shoot);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }
}
