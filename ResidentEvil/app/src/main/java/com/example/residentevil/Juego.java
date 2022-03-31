package com.example.residentevil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Juego extends SurfaceView implements SurfaceHolder.Callback, SurfaceView.OnTouchListener {
    private static final String TAG = Juego.class.getSimpleName();
    private SurfaceHolder holder;
    private BucleJuego bucle;
    //Contador de frames
    private int contadorFrames = 0;

    Controles controlesJuego;

    //Actores
    private Fondo fondo;
    private Personaje jill;
    private Enemigo zombie;


    /*Array de Touch */
    private ArrayList<Click> toques = new ArrayList();
    boolean hayToque = false;

    private int estado_jill = 1;
    private final float VELOCIDAD_HORIZONTAL = GameActivity.anchoPantalla/10/BucleJuego.MAX_FPS;
    private final float VELOCIDAD_VERTICAL = GameActivity.altoPantalla/10/BucleJuego.MAX_FPS;


    //        fondo = new Fondo(this);

    public Juego(AppCompatActivity context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
    }

    //Renderizar es pintar
    public void renderizar(Canvas canvas) {
        contadorFrames++;
        if (canvas != null) {
            Paint myPaint = new Paint();
            myPaint.setStyle(Paint.Style.STROKE);

            //renderizar fondo
            fondo.draw(canvas);

            //renderizar botones
            controlesJuego.renderizarBotones(canvas, myPaint);

            jill.dibujarPersonaje(canvas);
            zombie.dibujarPersonaje(canvas);
            actualizar();

        }

        //Esto es de ejemplo deberia de ir en el metodo de actualizar
        //if (contadorFrames % 3 == 0) {
        //    jill.caminar();
        //    zombie.caminar();
       // }
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

        //Aquí se cargan las cosas visuales
        fondo = new Fondo(getContext());
        controlesJuego = new Controles(getContext());
        jill = new Personaje(getContext(), GameActivity.anchoPantalla / 10 * 3, GameActivity.altoPantalla / 10 * 8, R.drawable.jill);
        zombie = new Enemigo(getContext(), GameActivity.anchoPantalla / 10 * 9, GameActivity.altoPantalla / 10 * 8, R.drawable.nemesis);

        // creamos el game loop
        bucle = new BucleJuego(getHolder(), this);

        // Hacer la Vista focusable para que pueda capturar eventos
        setFocusable(true);

        setOnTouchListener(this);

        //comenzar el bucle
        bucle.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int index;
        int x, y;

        // Obtener el pointer asociado con la acción
        index = MotionEventCompat.getActionIndex(event);

        x = (int) MotionEventCompat.getX(event, index);
        y = (int) MotionEventCompat.getY(event, index);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                hayToque = true;

                synchronized (this) {
                    toques.add(index, new Click(index, x, y));
                }

                //se comprueba si se ha pulsado
                for (BotonControl valor : controlesJuego.getBotones().values()) {
                    controlesJuego.comprobarSiEsPulsado(x, y, valor);
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                synchronized (this) {
                    toques.remove(index);
                }

                //se comprueba si se ha soltado el botón
                for (BotonControl valor : controlesJuego.getBotones().values()) {
                    controlesJuego.comprueba_soltado(toques, valor);
                }
                break;

            case MotionEvent.ACTION_UP:
                synchronized (this) {
                    toques.clear();
                }

                hayToque = false;
                //se comprueba si se ha soltado el botón
                for (BotonControl valor : controlesJuego.getBotones().values()) {
                    controlesJuego.comprueba_soltado(toques, valor);
                }
                break;
        }
        return true;
    }

    public void actualizar(){
        contadorFrames++;
        //Jill se mueve a la izquierda
        if (controlesJuego.getBotones().get("left").isPulsado()) {
            if (jill.getCoordenadaX() > 0)
                jill.setCoordenadaX((int) (jill.getCoordenadaX() - VELOCIDAD_HORIZONTAL));
                jill.setDireccion(Personaje.DIRECCION_IZQUIERDA);
                jill.caminar();

        }

        //Jill se mueve a la derecha
        if (controlesJuego.getBotones().get("right").isPulsado()) {
            if (jill.getCoordenadaX() < GameActivity.anchoPantalla - jill.getImagen().getWidth())
                jill.setCoordenadaX((int) (jill.getCoordenadaX() + VELOCIDAD_HORIZONTAL));
                jill.setDireccion(Personaje.DIRECCION_DERECHA);
                jill.caminar();
        }


        //Jill salta
        if (controlesJuego.getBotones().get("up").isPulsado()) {
            if (jill.getCoordenadaY() < GameActivity.altoPantalla - jill.getImagen().getHeight())

            jill.caminar();
        }

        if (controlesJuego.getBotones().get("shoot").isPulsado()) {
            if (jill.getCoordenadaY() < GameActivity.altoPantalla - jill.getImagen().getHeight())

                jill.caminar();
        }
    }
}


