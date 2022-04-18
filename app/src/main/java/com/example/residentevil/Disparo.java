package com.example.residentevil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;

public class Disparo extends RecursoInterfazUsuario {
    //Velocidad
    private float velocidad;
    //Reproduciremos el sonido del disparo con un objeto MediaPlayer
    private MediaPlayer mediaPlayer;
    //Tiempo en el que el disparo cruza la pantalla
    private final float MAX_SEGUNDOS_EN_CRUZAR_PANTALLA = 0.5f;
    //Objeto Personaje que será el que emita el disparo
    private Personaje personajeQueDispara;

    /**
     * Método constructor
     *
     * @param context             Contexto
     * @param personajeQueDispara el Personaje que dispara
     * @param recurso             el recurso del disparo
     */
    public Disparo(Context context, Personaje personajeQueDispara, int recurso) {

        super(context, personajeQueDispara.getCoordenadaX(), personajeQueDispara.getCoordenadaY(), recurso);
        init(personajeQueDispara);
    }

    /**
     * Método que inicializa el Disparo
     *
     * @param personajeQueDispara
     */
    public void init(Personaje personajeQueDispara) {
        //Tiempo que tarda en cruzar la pantalla
        velocidad = GameActivity.anchoPantalla / MAX_SEGUNDOS_EN_CRUZAR_PANTALLA / BucleJuego.MAX_FPS;
        //Personaje que va a disparar
        this.personajeQueDispara = personajeQueDispara;
        //Dirección a la que va a disparar, derecha o izquierda dependiendo donde mire el Personaje
        this.direccion = personajeQueDispara.getDireccion();
        //Sonido del disparo
        sonarDisparo();
    }

    /**
     * Método encargado de pintar el disparo en una determinada coordenada
     *
     * @param canvas
     */
    public void dibujar(Canvas canvas) {
        int charTop = getCoordenadaY();
        int charRight = getCoordenadaX() + (imagenEscalada.getHeight() / 8);
        int charBottom = getCoordenadaY() + (imagenEscalada.getWidth() / 6);
        int charLeft = getCoordenadaX();

        //Nuevo objeto colisión que se encargará de comprobar las coordenadas que tiene el Disparo
        super.colision = new Colision(charTop, charRight, charBottom, charLeft);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        super.dibujar(canvas, paint);

    }

    /**
     * Método que actualiza las coordenadas del disparo
     */
    public void actualizaCoordenadas() {
        super.coordenadaX += ((direccion == DIRECCION_DERECHA ? 1 : -1) * velocidad);
    }

    /**
     * Método para comprobar si el disparo se sale de la pantalla
     * @return
     */
    public boolean fueraDePantalla() {
        return super.coordenadaX < 0 || super.coordenadaX > GameActivity.anchoPantalla - getImagen().getWidth();
    }

    /**
     * Método encargado de emitir el sonido del disparo
     */
    public void sonarDisparo() {
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.shoot);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }
}
