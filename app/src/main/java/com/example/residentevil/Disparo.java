package com.example.residentevil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;

public class Disparo extends RecursoInterfazUsuario{

    private float velocidad;
    private MediaPlayer mediaPlayer; //para reproducir el sonido de disparo
    private final float MAX_SEGUNDOS_EN_CRUZAR_PANTALLA=3;
    private Personaje personajeQueDispara;

    public Disparo(Context context, int x, int y, int recurso, Personaje personajeQueDispara) {
        super(context, x, y, recurso);
        super.coordenadaX = x +(personajeQueDispara.getImagen().getWidth()/2);
        super.coordenadaY = y - (super.imagenEscalada.getHeight());
        velocidad=GameActivity.altoPantalla /MAX_SEGUNDOS_EN_CRUZAR_PANTALLA/BucleJuego.MAX_FPS;

        mediaPlayer=MediaPlayer.create(context, R.raw.shoot);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }



    //se actualiza la coordenada y nada más
    public void actualizaCoordenadas(){
        super.coordenadaY -=velocidad;
    }

    public boolean fueraDePantalla() {
        return super.coordenadaY < 0;
    }
}
