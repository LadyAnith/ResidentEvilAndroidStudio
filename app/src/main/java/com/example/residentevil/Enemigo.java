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
    //Tipos de enemigos que hay
    public final int NEMESIS = 0;
    public final int ZOMBIE = 1;

    //Vida de los enemigos
    public final float HP_ZOMBIE=2;
    public final float HP_NEMESIS=5;
    //velocidad de los enemigos
    public final float VELOCIDAD_NEMESIS=5;
    public final float VELOCIDAD_ZOMBIE=2;
    public float velocidad;

    //Tipo de enemigos
    public int tipo_enemigo;
    //Nivel de dificultad
    private int nivel;
    private MediaPlayer media;

    /**
     * Método constructor
     * @param context
     * @param x
     * @param y
     * @param nivel
     */
    public Enemigo(Context context, int x, int y, int nivel) {

        super(context, x, y);
        this.nivel = nivel;
        init();
    }

    /**
     * Método que inicializa un enemigo
     */
    public void init(){
        //Velocidad inicial= 20 segundos en cruzar * factor de inteligencia y nivel
        float VELOCIDAD_ENEMIGO = GameActivity.altoPantalla/20f/BucleJuego.MAX_FPS;

        //Probabilidad de que salga un zombie 80%, probabilidad de que salga Nemesis 20%
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

        //La dirección en la que aparezcan los zombies será aleatoria
        if(Math.random()>0.5)
            setDireccion(DIRECCION_DERECHA);
        else
            setDireccion(DIRECCION_IZQUIERDA);

        calculaCoordenadas();
        //Si el enemigo que aparece es Nemesis, emitirá su sonido
        if(tipo_enemigo== NEMESIS) {
            sonidoNemesis();
        }
    }

    /**
     * Método encargado de dibujar a un enemigo con una posición y un tamaño concreto
     * @param canvas
     */
    public void dibujarEnemigo(Canvas canvas) {
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
        caminar();

    }

    /**
     * Método encargado de dibujar en el canvas en unas determinadas coordenadas al enemigo
     * @param c se le pasa un Objeto Canvas como parámetro
     * @param p se le pasa un objeto de la clase Paint como parámetro
     */
    public void dibujar(Canvas c, Paint p){
        c.drawBitmap(getBitmap() ,super.coordenadaX , super.coordenadaY,p);
    }

    /**
     * Método encargado de hacer que camine el personaje en la dirección adecuada con la postura correspondiente
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
     * Método encargado de calcular aleatoriamente las coordenadas por donde va a aparecer un enemigo
     */
    public void calculaCoordenadas(){
        double x; //aleatorio
        /* Posicionamiento del enemigo */
        x=Math.random();
        if(x<=1){
//25% de probabilidad de que el enemigo salga por los lados
            if(x<0.25) //sale por la izquierda
                super.coordenadaX = 0;
            else
                super.coordenadaX = GameActivity.anchoPantalla - super.getImagen().getHeight();
        }
        //Se establece que los personajes aparezcan a la altura Y base
        super.coordenadaY = Juego.y_juego;

    }


    /**
     * Método encargado de actualizar las coordenadas de Nemesis con respecto a las coordenadas de Jill
     * @param jill
     */
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
        }
        else{
            super.coordenadaX += (DIRECCION_DERECHA == getDireccion() ? 1 : -1) * velocidad;

            //Si es un zombie, cambirá de dirección al llegar al borde de la pantalla
            if( super.coordenadaX <= 0 && DIRECCION_IZQUIERDA == getDireccion())
                setDireccion(DIRECCION_DERECHA);
            if(super.coordenadaX > (GameActivity.anchoPantalla - super.getImagen().getHeight()) && DIRECCION_DERECHA == getDireccion())
                setDireccion(DIRECCION_IZQUIERDA);
        }
    }

    /**
     * Método que crea el Bitmap del enemigo dependiendo del tipo de enemigo que sea
     * @return
     */
    public Bitmap getBitmap(){
        if(tipo_enemigo==ZOMBIE)
            return Juego.zombie;
        else
            return Juego.nemesis;
    }

    /**
     * Método encargado de bajar la vida a un enemigo
     */
    public void damageEnemy(){
        hp -= 1;
    }

    /**
     * Método que comprueba si el enemigo se ha muerto al llegar su vida a 0
     * @return
     */
    public boolean isDead(){
        return hp <= 0;
    }

    /**
     * Getter tipo de enemigo
     * @return
     */
    public int getTipo_enemigo() {
        return tipo_enemigo;
    }

    /**
     * Método que se encarga de añadir sonido al enemigo Nemesis
     */
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