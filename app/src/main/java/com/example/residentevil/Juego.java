package com.example.residentevil;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import java.util.ArrayList;
import java.util.Iterator;

public class Juego extends SurfaceView implements SurfaceHolder.Callback, SurfaceView.OnTouchListener {
    private static final String TAG = Juego.class.getSimpleName();
    private SurfaceHolder holder;
    private BucleJuego bucle;
    //Contador de frames
    private int contadorFrames = 0;

    //Indica la altura a la que van a ir los elementos en la pantalla
    public static int y_juego = GameActivity.altoPantalla / 10 * 7;

    private Controles controlesJuego;

    //Fondo del juego
    private Fondo fondo;
    //Protagonista
    private Personaje jill;

    //Array de Clicks
    private ArrayList<Click> toques = new ArrayList();
    boolean hayToque = false;

    //Velocidad
    private final float VELOCIDAD_HORIZONTAL = GameActivity.anchoPantalla/6/BucleJuego.MAX_FPS;
    private final float VELOCIDAD_VERTICAL = GameActivity.altoPantalla/10/BucleJuego.MAX_FPS;

    //Disparos
    private ArrayList<Disparo> lista_disparos=new ArrayList();
    private int frames_para_nuevo_disparo=0;
    //entre disparo y disparo deben pasar al menos MAX_FRAMES_ENTRE_DISPARO. Realizará 4 disparos por segundo
    private final int MAX_FRAMES_ENTRE_DISPARO=BucleJuego.MAX_FPS/4;
    private boolean nuevo_disparo=false;

    //Sonido de fondo
    private MediaPlayer mediaPlayer;

    //Enemigos
    //Bitmap para los enemigos
    public static Bitmap zombie, nemesis;
    public final int TOTAL_ENEMIGOS=200; //Total de enemigos
    public final int TOTAL_ENEMIGOS_MATADOS_FINALIZAR_JUEGO= 10; //Enemigos que hay que matar para acabar el juego
    public final int MAX_ENEMIGOS=7; //Cantidad de enemigos maximos que van a aparecer en pantalla
    private int enemigos_minuto=20; //Número de enemigos por minuto
    private int frames_para_nuevo_enemigo=0; //Frames que restan hasta generar nuevo enemigo
    private int enemigos_muertos=0; //Contador de enemigos que hemos matado
    private int enemigos_creados=0;
    //Listado con un arraylist de enemigos
    private ArrayList<Enemigo> lista_enemigos=new ArrayList();
    //Nivel de dificultad
    private int nivel = 1;

    //Boleanos que indicarán si el jugardor ha sido derrotado o ha obtenido la victoria
    private boolean victoria = false;
    private boolean derrota = false;


    /**
     * Constructor del juego
     * @param context
     */
    public Juego(AppCompatActivity context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
    }


    /**
     * Método encargado de renderizar las imágenes en el canvas
     * @param canvas
     */
    public void renderizar(Canvas canvas) {
        contadorFrames++;
        if (canvas != null) {
            Paint myPaint = new Paint();
            myPaint.setStyle(Paint.Style.STROKE);

            //Renderizar el fondo
            fondo.draw(canvas);

            //Renderizar los botones
            controlesJuego.renderizarBotones(canvas, myPaint);

            //Si el jugador no ha sido derrotado renderiza el Personaje
            if(!derrota)
            jill.dibujarPersonaje(canvas);

            actualizar();

            //Bucle que dibuja los disparos
            for(Disparo d:lista_disparos){
                d.dibujar(canvas);
            }

            //Dibuja los enemigos
            for(Enemigo e: lista_enemigos){
                e.dibujarEnemigo(canvas);
            }

            //Si el jugardor gana, pinta un mensaje de Victoria
            if(victoria){
                myPaint.setColor(Color.YELLOW);
                myPaint.setTextSize(GameActivity.anchoPantalla/10);
                canvas.drawText("YOU WIN!!", 100, GameActivity.altoPantalla/2-300, myPaint);
                myPaint.setTextSize(GameActivity.anchoPantalla/20);
                canvas.drawText("Has Derrotado a Nemesis!!", 50,
                        GameActivity.altoPantalla/2-200, myPaint);

            }
            //Si el jugador pierder, pinta un mensaje de derrota
            if(derrota) {
                myPaint.setColor(Color.YELLOW);
                myPaint.setTextSize(GameActivity.anchoPantalla/10);
                canvas.drawText("YOU ARE DEAD!!", 100, GameActivity.altoPantalla/2-300, myPaint);
                myPaint.setTextSize(GameActivity.anchoPantalla/20);
                canvas.drawText("Los zombies han conquistado Racoon City!!!!", 50,
                        GameActivity.altoPantalla/2-200, myPaint);
            }
        }
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

        //Aquí se cargan los elementos visuales
        fondo = new Fondo(getContext());
        controlesJuego = new Controles(getContext());
        jill = new Personaje(getContext(), GameActivity.anchoPantalla / 10 * 3, y_juego, R.drawable.jill);

        //Aquí se crea el bucle del juego
        bucle = new BucleJuego(getHolder(), this);

        //Hace la Vista focusable para que se puedan capturar eventos
        setFocusable(true);

        setOnTouchListener(this);

        //Método que inicia la música del juego
        iniciarMusicaJuego();

        //Método que carga a los enemigos
        cargaEnemigos();

        //Comienza el bucle
        bucle.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        // cerrar el thread y esperar que acabe
        boolean retry = true;
        while (retry) {
            try {
                //Método que libera recursos
                fin();
                bucle.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

    }

    /**
     * Método encargado de detectar cuando se pulsa con el dedo la pantalla del móvil
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int index;
        int x, y;

        // Obtiene el pointer asociado con la acción
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

                //Se comprueba si ha sido pulsado
                for (BotonControl valor : controlesJuego.getBotones().values()) {
                    controlesJuego.comprobarSiEsPulsado(x, y, valor);
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                synchronized (this) {
                    toques.remove(index);
                }

                //Se comprueba si se ha soltado el botón
                for (BotonControl valor : controlesJuego.getBotones().values()) {
                    controlesJuego.comprueba_soltado(toques, valor);
                }
                break;

            case MotionEvent.ACTION_UP:
                synchronized (this) {
                    toques.clear();
                }

                hayToque = false;
                //Se comprueba si se ha soltado el botón
                for (BotonControl valor : controlesJuego.getBotones().values()) {
                    controlesJuego.comprueba_soltado(toques, valor);
                }
                break;
        }
        return true;
    }

    /**
     * Método encargado de actualizar el juego
     */
    public void actualizar() {
        contadorFrames++;
        if(!derrota) {
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
                //if (jill.getCoordenadaY() < GameActivity.altoPantalla - jill.getImagen().getHeight())
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
            }
            if (frames_para_nuevo_disparo > 0) {
                frames_para_nuevo_disparo--;
            }

        }

        //Los disparos se mueven
        for (Iterator<Disparo> it_disparos = lista_disparos.iterator(); it_disparos.hasNext(); ) {
            boolean golpeado = false;
            Disparo d = it_disparos.next();
            d.actualizaCoordenadas();
            //Si la bala se sale fuera de la pantalla, se elimina
            if (d.fueraDePantalla()) {
                it_disparos.remove();
                golpeado = true;
            }

            if (golpeado) continue;
            //Bucle encargado de comprobar si las balas colisionan con un enemigo
            for(Iterator<Enemigo> it_enemigos = lista_enemigos.iterator();it_enemigos.hasNext() && !golpeado; ){
                Enemigo e = it_enemigos.next();
                if (d.getColision().isColision(e.getColision())){
                    golpeado = true;
                    it_disparos.remove();
                    //Daña a un enemigo al colisionar
                    e.damageEnemy();
                    //Si el enemigo muere, desaparece
                    if (e.isDead()) {
                        it_enemigos.remove();
                        enemigos_muertos++;
                        nivel ++;
                    }
                }
            }

        }

        //Enemigos
        if(frames_para_nuevo_enemigo==0){
            crearNuevoEnemigo();
        //Nuevo ciclo de enemigos
            frames_para_nuevo_enemigo=bucle.MAX_FPS*60/enemigos_minuto;
        }
        frames_para_nuevo_enemigo--;

        //Actualiza el método comprueblaFinDelJuego
        if(!derrota && !victoria)
            compruebaFinDelJuego();
    }

    /**
     * Método encargado de comprobar si se ha finalizado el juego
     */
    private void compruebaFinDelJuego() {

        //Los enemigos Nemesis persiguen al jugador
        for(Enemigo e: lista_enemigos){
            e.actualizaCoordenadas(jill);
            //Se comprueba si hay colisiones, si las hay, baja la vida a Jill
            if (e.getColision().isColision(jill.getColision())){
                Log.d("info","GOLPE A JILL");
                jill.damageJill();
                //Si la vida de Jill llega a 0
                if(jill.getHp_Jill() == 0){
                    jill.sonidoJillHerida();
                    derrota = true;
                }
            }
        }
        //Si los enemigos muertos son iguales al número de enemigos matados para finalziar el juego, conseguimos una victoria
        if(!derrota)
            if(enemigos_muertos==TOTAL_ENEMIGOS_MATADOS_FINALIZAR_JUEGO)
                victoria=true;

    }

    /**
     * Método encargado de crear una lista de disparos
     */
    public void creaDisparo(){
        lista_disparos.add(new Disparo(getContext(),jill, R.drawable.bala));
    }

    /**
     * Método encargado de añadir música al juego
     */
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

    /**
     * Método encargado de liberar recursos del juego
     */
    public void fin(){
        bucle.fin();
        mediaPlayer.release();
        nemesis.recycle();
        zombie.recycle();
    }

    /**
     * Método encargado de cargar el listado de los enemigos
     */
    public void cargaEnemigos(){
        frames_para_nuevo_enemigo=bucle.MAX_FPS*60/enemigos_minuto;
        zombie = BitmapFactory.decodeResource(getResources(), R.drawable.zombie);
        nemesis = BitmapFactory.decodeResource(getResources(), R.drawable.nemesis);
    }

    /**
     * Método encargado de crear un nuevo enemigo
     */
    public void crearNuevoEnemigo(){
        if(TOTAL_ENEMIGOS-enemigos_creados>0 && lista_enemigos.size() < MAX_ENEMIGOS) {
            lista_enemigos.add(new Enemigo(getContext(), GameActivity.anchoPantalla / 2 * 10, y_juego, nivel));
            enemigos_creados++;
        }
    }


}


