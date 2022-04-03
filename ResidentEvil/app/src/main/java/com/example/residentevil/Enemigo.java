package com.example.residentevil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Enemigo extends RecursoInterfazUsuario {
    public static final int DIRECCION_ABAJO = 0;
    public static final int DIRECCION_IZQUIERDA = 1;
    public static final int DIRECCION_DERECHA = 2;
    public static final int DIRECCION_ARRIBA = 3;

    private int direccion = DIRECCION_IZQUIERDA;
    private int pose = 1;
    private boolean sentidoOrdinal = true;

    public final int NEMESIS = 0;
    public final int ZOMBIE = 1;

    public final float VELOCIDAD_NEMESIS=5;
    public final float VELOCIDAD_ZOMBIE=2;
    public float velocidad;

    public int tipo_enemigo; //imagen del control

    public float direccion_horizontal=1; //inicialmente derecha
    private int nivel;

    public Enemigo(Context context, int x, int y, int recurso, int nivel) {

        super(context, x, y, recurso);
        this.nivel = nivel;
        init();
    }

    public void init(){
        //velocidad inicial=20 segundos en cruzar * factor de inteligencia y nivel
        float VELOCIDAD_ENEMIGO = GameActivity.altoPantalla/20f/BucleJuego.MAX_FPS;

        //probabilidad de enemigo tonto 80%, enemigo listo 20%
        if(Math.random()>0.20) {
            tipo_enemigo = ZOMBIE;
            velocidad = (VELOCIDAD_ZOMBIE+ nivel)*VELOCIDAD_ENEMIGO;
        }
        else {
            tipo_enemigo = NEMESIS;
            velocidad = (VELOCIDAD_NEMESIS+ nivel)*VELOCIDAD_ENEMIGO;
        }
//para el enemigo tonto se calcula la dirección aleatoria
        if(Math.random()>0.5)
            direccion_horizontal=1; //derecha
        else
            direccion_horizontal=-1; //izquierda

        calculaCoordenadas();
    }

    public void dibujarEnemigo(Canvas canvas) {
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
        caminar();
    }

    public void dibujar(Canvas c, Paint p){
        if(tipo_enemigo == ZOMBIE)
            c.drawBitmap(Juego.zombie ,super.coordenadaX , super.coordenadaY,p);
        else
            c.drawBitmap(Juego.nemesis ,super.coordenadaX , super.coordenadaY,p);
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
                super.coordenadaX = GameActivity.anchoPantalla - super.getImagen().getWidth();
            super.coordenadaY = (int) (Math.random()*GameActivity.altoPantalla /2);
        }else{
            super.coordenadaX =(int)(Math.random()* (GameActivity.anchoPantalla/2 -super.getImagen().getWidth()));
            super.coordenadaY=0;
        }
    }

    //Actualiza la coordenada del enemigo con respecto a la coordenada de la nave
    public void actualizaCoordenadas(Personaje jill){
        if(tipo_enemigo== NEMESIS) {
            if (jill.getCoordenadaX() > super.coordenadaX)
                super.coordenadaX += velocidad;
            else if (jill.getCoordenadaX() < super.coordenadaX)
                super.coordenadaX -= velocidad;
            if(Math.abs(super.coordenadaX - jill.getCoordenadaX()) < velocidad)
                super.coordenadaX = jill.getCoordenadaX(); //si está muy cerca se pone a su altura
        }
        else{
//el enemigo tonto hace caso omiso a la posición de la nave,
//simplemente pulula por la pantalla
            super.coordenadaX += direccion_horizontal * velocidad;

//Cambios de direcciones al llegar a los bordes de la pantalla
            if( super.coordenadaX <= 0 && direccion_horizontal == -1)
                direccion_horizontal=1;
            if(super.coordenadaX > (GameActivity.anchoPantalla - super.getImagen().getHeight()) && direccion_horizontal == 1){
            direccion_horizontal=-1;

        }
    }
}

    public Bitmap bitmap(){
        if(tipo_enemigo==ZOMBIE)
            return Juego.zombie;
        else
            return Juego.nemesis;
    }
}