package com.example.residentevil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
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
    private Enemigo enemigo;
    private Disparo disparo;


    /*Array de Touch */
    private ArrayList<Click> toques = new ArrayList();
    boolean hayToque = false;

    private int estado_jill = 1;
    private final float VELOCIDAD_HORIZONTAL = GameActivity.anchoPantalla/6/BucleJuego.MAX_FPS;
    private final float VELOCIDAD_VERTICAL = GameActivity.altoPantalla/10/BucleJuego.MAX_FPS;

    /* Disparos */
    private ArrayList<Disparo> lista_disparos=new ArrayList<Disparo>();

    private int frames_para_nuevo_disparo=0;
    //entre disparo y disparo deben pasar al menos MAX_FRAMES_ENTRE_DISPARO
    private final int MAX_FRAMES_ENTRE_DISPARO=BucleJuego.MAX_FPS/4; //4 disparos por segundo
    private boolean nuevo_disparo=false;

    /* sonidos */
    MediaPlayer mediaPlayer;

    /* Enemigos */
    public static Bitmap zombie, nemesis;
    public final int TOTAL_ENEMIGOS=500; //Enemigos para acabar el juego
    private int enemigos_minuto=50; //número de enemigos por minuto
    private int frames_para_nuevo_enemigo=0; //frames que restan hasta generar nuevo enemigo
    private int enemigos_muertos=0; //Contador de enemigos muertos
    private int enemigos_creados=0;

    private ArrayList<Enemigo> lista_enemigos=new ArrayList();
    private int nivel = 1;

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
            //zombie.dibujarPersonaje(canvas);
            actualizar();

            for(Disparo d:lista_disparos){
                d.dibujar(canvas,myPaint);
            }

            //dibuja los enemigos
            for(Enemigo e: lista_enemigos){
                e.dibujarEnemigo(canvas);
            }
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
        //zombie = new Enemigo(getContext(), GameActivity.anchoPantalla / 10 * 9, GameActivity.altoPantalla / 10 * 8, R.drawable.nemesis, 1);

        // creamos el game loop
        bucle = new BucleJuego(getHolder(), this);

        // Hacer la Vista focusable para que pueda capturar eventos
        setFocusable(true);

        setOnTouchListener(this);

        //Música del juego
        iniciarMusicaJuego();

        cargaEnemigos();

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

    public void actualizar() {
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

        //Jill dispara
        if (controlesJuego.getBotones().get("shoot").isPulsado()) {
                /* Disparo */
                nuevo_disparo = true;
                if (frames_para_nuevo_disparo == 0) {
                    if (nuevo_disparo) {
                        creaDisparo();
                        nuevo_disparo = false;
                    }
//nuevo ciclo de disparos
                    frames_para_nuevo_disparo = MAX_FRAMES_ENTRE_DISPARO;
                }
                frames_para_nuevo_disparo--;
                jill.caminar();


            //Los disparos se mueven
            for (Iterator<Disparo> it_disparos = lista_disparos.iterator(); it_disparos.hasNext(); ) {
                Disparo d = it_disparos.next();
                d.actualizaCoordenadas();
                if (d.fueraDePantalla()) {
                    it_disparos.remove();
                }
            }
        }

        /*Enemigos*/
        if(frames_para_nuevo_enemigo==0){
            crearNuevoEnemigo();
        //nuevo ciclo de enemigos
            frames_para_nuevo_enemigo=bucle.MAX_FPS*60/enemigos_minuto;
        }
        frames_para_nuevo_enemigo--;

        //Los enemigos persiguen al jugador
        for(Enemigo e: lista_enemigos){
            e.actualizaCoordenadas(jill);
        }
    }

    public void creaDisparo(){
        lista_disparos.add(new Disparo(getContext(),jill.getCoordenadaX(),jill.getCoordenadaY(), R.drawable.bala,jill));
    }

    private void iniciarMusicaJuego(){
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.comisaria);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
        mediaPlayer.start();
    }
    public void fin(){
        mediaPlayer.release();
    }

    public void cargaEnemigos(){
        frames_para_nuevo_enemigo=bucle.MAX_FPS*60/enemigos_minuto;
        zombie = BitmapFactory.decodeResource(getResources(), R.drawable.zombie);
        nemesis = BitmapFactory.decodeResource(getResources(), R.drawable.nemesis);
    }

    public void crearNuevoEnemigo(){
        if(TOTAL_ENEMIGOS-enemigos_creados>0) {
            lista_enemigos.add(new Enemigo(getContext(), GameActivity.anchoPantalla / 10 * 9, GameActivity.altoPantalla / 10 * 8, R.drawable.zombie, nivel));
            enemigos_creados++;
        }
    }


}


