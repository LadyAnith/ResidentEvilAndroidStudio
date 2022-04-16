package com.example.residentevil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;

public class Enemigo extends RecursoInterfazUsuario {

    private float hp;
    private int pose = 1;
    private boolean sentidoOrdinal = true;

    public final int NEMESIS = 0;
    public final int ZOMBIE = 1;

    public final float HP_ZOMBIE=2;
    public final float HP_NEMESIS=7;
    public final float VELOCIDAD_NEMESIS=5;
    public final float VELOCIDAD_ZOMBIE=2;
    public float velocidad;

    public int tipo_enemigo; //imagen del control

    private int nivel;
    MediaPlayer media;

    public Enemigo(Context context, int x, int y, int nivel) {

        super(context, x, y);
        this.nivel = nivel;
        init();
    }

    public void init(){
        //velocidad inicial=20 segundos en cruzar * factor de inteligencia y nivel
        float VELOCIDAD_ENEMIGO = GameActivity.altoPantalla/20f/BucleJuego.MAX_FPS;

        //probabilidad de enemigo tonto 80%, enemigo listo 20%
        if(Math.random()>0.20) {
            tipo_enemigo = ZOMBIE;
            hp = HP_ZOMBIE;
            if(nivel%5 == 0)
                hp ++;
            velocidad = (VELOCIDAD_ZOMBIE+ nivel)*VELOCIDAD_ENEMIGO;
        }
        else {
            tipo_enemigo = NEMESIS;
            hp = HP_NEMESIS;
            if(nivel%10 == 0)
                hp ++;

            velocidad = (VELOCIDAD_NEMESIS+ nivel)*VELOCIDAD_ENEMIGO;
        }

        imagen = getBitmap();
        imagenEscalada = escalarImagenPantalla(imagen);

//para el enemigo tonto se calcula la dirección aleatoria
        if(Math.random()>0.5)
            setDireccion(DIRECCION_DERECHA);
        else
            setDireccion(DIRECCION_IZQUIERDA);

        calculaCoordenadas();

        if(tipo_enemigo== NEMESIS) {
            sonidoNemesis();
        }
    }

    public void dibujarEnemigo(Canvas canvas) {
        int iCol = pose;
        int iRow = direccion;

        int leftOrigen = imagenEscalada.getWidth() / 3 * iCol;
        int topOrigen = imagenEscalada.getHeight() / 4 * iRow;
        int rightOrigen = imagenEscalada.getWidth() / 3 * (iCol + 1);
        int bottomOrigen = imagenEscalada.getHeight() / 4 * (iRow + 1);

        int charTop = getCoordenadaY() ;
        int charRight = getCoordenadaX() + (imagenEscalada.getHeight() / 8);
        int charBottom = getCoordenadaY() + (imagenEscalada.getWidth() / 6);
        int charLeft = getCoordenadaX() ;

        super.colision = new Colision(charTop,charRight,charBottom,charLeft);

        canvas.drawBitmap(imagenEscalada,
                new Rect(leftOrigen, topOrigen, rightOrigen, bottomOrigen),
                new Rect(charLeft, charTop, charRight,charBottom),
                null);
        caminar();

    }

    public void dibujar(Canvas c, Paint p){
        c.drawBitmap(getBitmap() ,super.coordenadaX , super.coordenadaY,p);
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

    public void calculaCoordenadas(){
        double x; //aleatorio
        /* Posicionamiento del enemigo */
//entre 0 y 0.125 sale por la izquierda (x=0, y=aleatorio(1/5) pantalla)
//entre 0.125 y 0.25 sale por la derecha (x=AnchoPantalla-anchobitmap, y=aleatorio(1/5))
//>0.25 sale por el centro (y=0, x=aleatorio entre 0 y AnchoPantalla-AnchoBitmap)
        x=Math.random();
        if(x<=0.25){
//25% de probabilidad de que el enemigo salga por los lados
            if(x<0.125) //sale por la izquierda
                super.coordenadaX = 0;
            else
                super.coordenadaX = GameActivity.anchoPantalla - super.getImagen().getHeight();
        }else{
            super.coordenadaX =(int)(Math.random()* (GameActivity.anchoPantalla/2 -super.getImagen().getWidth()));
        }
        //Se establece que los personajes aparezcan a la altura Y base
        super.coordenadaY = Juego.y_juego;

    }

    //Actualiza la coordenada del enemigo con respecto a la coordenada de la nave
    public void actualizaCoordenadas(Personaje jill){
        if(tipo_enemigo== NEMESIS) {
            if (jill.getCoordenadaX() > super.coordenadaX){
                super.coordenadaX += velocidad;
                setDireccion(DIRECCION_DERECHA);
            }
            else if (jill.getCoordenadaX() < super.coordenadaX){
                super.coordenadaX -= velocidad;
                setDireccion(DIRECCION_IZQUIERDA);
            }
            else if (jill.getCoordenadaY() < super.coordenadaY){
                super.coordenadaY -= velocidad;
                setDireccion(DIRECCION_ARRIBA);
            }
            else if (jill.getCoordenadaY() > super.coordenadaY){
                super.coordenadaX += velocidad;
                setDireccion(DIRECCION_ABAJO);
            }

            /*
            if(Math.abs(super.coordenadaX - jill.getCoordenadaX()) < velocidad)
                super.coordenadaX = jill.getCoordenadaX(); //si está muy cerca se pone a su altura
             */
        }
        else{
//el enemigo tonto hace caso omiso a la posición de la nave,
//simplemente pulula por la pantalla
            super.coordenadaX += (DIRECCION_DERECHA == getDireccion() ? 1 : -1) * velocidad;

//Cambios de direcciones al llegar a los bordes de la pantalla
            if( super.coordenadaX <= 0 && DIRECCION_IZQUIERDA == getDireccion())
                setDireccion(DIRECCION_DERECHA);
            if(super.coordenadaX > (GameActivity.anchoPantalla - super.getImagen().getHeight()) && DIRECCION_DERECHA == getDireccion())
                setDireccion(DIRECCION_IZQUIERDA);
        }
    }

    public Bitmap getBitmap(){
        if(tipo_enemigo==ZOMBIE)
            return Juego.zombie;
        else
            return Juego.nemesis;
    }

    public void damageEnemy(){
        hp -= 1;
    }

    public boolean isDead(){
        return hp <= 0;
    }

    public int getTipo_enemigo() {
        return tipo_enemigo;
    }

    public void sonidoNemesis(){
        media= MediaPlayer.create(getContext(), R.raw.nemesisstart);
        media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        media.start();
    }
}