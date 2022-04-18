package com.example.residentevil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Clase abstracta que contiene atributos comunes y hereda de la clase View
 */
abstract class RecursoInterfazUsuario extends View {
    protected int coordenadaX, coordenadaY;
    protected Bitmap imagen;
    protected Bitmap imagenEscalada;
    protected int recurso;

    //Atributos estáticos con las posiciones de las imágenes del spriter
    public static final int DIRECCION_ABAJO = 0;
    public static final int DIRECCION_IZQUIERDA = 1;
    public static final int DIRECCION_DERECHA = 2;
    public static final int DIRECCION_ARRIBA = 3;

    protected int direccion = DIRECCION_IZQUIERDA;

    protected Colision colision;

    /**
     * Método constructor con recurso
     * @param context
     * @param x
     * @param y
     * @param recurso
     */
    public RecursoInterfazUsuario(Context context, int x, int y, int recurso) {
        super(context);
        init(x, y, recurso);
    }

    /**
     * Método constructor sin el recurso
     * @param context
     * @param x
     * @param y
     */
    public RecursoInterfazUsuario(Context context, int x, int y) {
        super(context);
        init(x, y, null);
    }


    /**
     *Método encargado de inicializar las coordenadas y la iamgen
     *
     * @param x de tipo int
     * @param y de tipo int
     * @param recurso de la clase Integer
     */
    public void init(int x, int y, Integer recurso) {
        this.coordenadaX = x;
        this.coordenadaY = y;
        this.colision = new Colision(0,0,0,0);

        //Si el recurso es diferente a null, guarda la imagen en el bitmap, y la escala con el método escalrImagenpantalla()
        if (recurso != null){
            this.recurso = recurso;
            this.imagen = BitmapFactory.decodeResource(getResources(), recurso);
            this.imagenEscalada = escalarImagenPantalla(imagen);
        }
    }

    //Getter & Setter
    public int getCoordenadaX() {
        return coordenadaX;
    }

    public void setCoordenadaX(int coordenadaX) {
        this.coordenadaX = coordenadaX;
    }

    public int getCoordenadaY() {
        return coordenadaY;
    }

    public void setCoordenadaY(int coordenadaY) {
        this.coordenadaY = coordenadaY;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public Bitmap getImagenEscalada() {
        return imagenEscalada;
    }

    public void setImagenEscalada(Bitmap imagenEscalada) {
        this.imagenEscalada = imagenEscalada;
    }

    public int getRecurso() {
        return recurso;
    }

    public void setRecurso(int recurso) {
        this.recurso = recurso;
    }

    public int getDireccion() {
        return direccion;
    }

    public void setDireccion(int direccion) {
        this.direccion = direccion;
    }

    public Colision getColision() {
        return colision;
    }

    public void setColision(Colision colision) {
        this.colision = colision;
    }

    /**
     * Método encargado de pintar la imagen en las determinadas coordenadas
     * @param c se le pasa un Objeto Canvas como parámetro
     * @param p se le pasa un objeto de la clase Paint como parámetro
     */
    public void dibujar(Canvas c, Paint p) {
        c.drawBitmap(imagen, coordenadaX, coordenadaY, p);
    }

    /**
     * Método encargado de ajustar la imagen de un Bitmap a la pantalla del dispositivo
     * @param imagenOrigen como parámetro se le pasa el bitmap que queremos escalar
     * @return devuelve un Bitmap escalado
     */
    public Bitmap escalarImagenPantalla(Bitmap imagenOrigen) {
        return Bitmap.createScaledBitmap(imagenOrigen, GameActivity.altoPantalla, GameActivity.anchoPantalla, true);
    }

}
