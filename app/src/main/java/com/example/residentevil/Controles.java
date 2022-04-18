package com.example.residentevil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Controles {

    private Context context;
    //Aquí guardaré todos los botones del juego
    private Map<String, BotonControl> botones = new HashMap();

    /**
     * Método constructor
     * @param context
     */
    public Controles(Context context) {
        init(context);
    }

    /**
     * Método encargado de inicializar los controles del juego
     * @param context
     */
    private void init(Context context) {
        this.context = context;
        //Objeto de la clase BotonControl
        BotonControl button;
        //Variables que darán el margen a los controles
        int x, y, margen = 20;

        //Imagen y posición del control izquierdo
        x = margen;
        y = GameActivity.altoPantalla / 10 * 4;
        button = addBoton("left", x, y, R.drawable.flecha_izda);

        //Imagen y posición del control derecho
        x = margen + button.getImagen().getWidth();
        y = button.getCoordenadaY();
        button = addBoton("right", x, y, R.drawable.flecha_dcha);


        //Imagen y posición del control del salto
        x = GameActivity.anchoPantalla - button.getImagen().getWidth();
        y = GameActivity.altoPantalla / 10 * 3;
        button = addBoton("up", x, y, R.drawable.flecha_up);


        //Imagen y posición del control del disparo
        x = button.getCoordenadaX();
        y = button.getCoordenadaY() + button.getImagen().getHeight();
        button = addBoton("shoot", x, y, R.drawable.disparo);

    }

    /**
     * Método encargado de añadir los botones del juego a un HashMap de BotonControl
     * @param key String, es el nombre de la key que va a tener cada control
     * @param x parámetro de tipo int, es la posición X del botón
     * @param y prámetro de tipo int,es la posicion Y del botón
     * @param resource como parámetro le pasaremos el recurso del botón
     * @return
     */
    private BotonControl addBoton(String key, int x, int y, int resource) {
        BotonControl boton = new BotonControl(context, x, y, resource);
        botones.put(key, boton);
        return boton;
    }

    /**
     * Método para renderizar los botones
     * @param canvas
     * @param paint
     */
    public void renderizarBotones(Canvas canvas, Paint paint) {
        //Transparencia del botón
        paint.setAlpha(200);
        for (BotonControl boton : getBotones().values()) {
            boton.dibujar(canvas, paint);

        }
    }


    /**
     * Método encargado de comprobar si se ha pulsado el control
     * @param x parámetro de tipo int que indica la coordenada x donde está situado el botón
     * @param y parámetro de tipo int que indica la coordenada y donde está situado el botón
     * @param button objeto de la clase BotonControl
     */
    public void comprobarSiEsPulsado(int x, int y, BotonControl button){
        //Coordenadas del botón
        button.getCoordenadaY();
        button.getCoordenadaX();

        //Con este if, compruebo si se ha pulsado dentro del área de la imagen del botón
        if(x>button.getCoordenadaX() && x<button.getCoordenadaX()+ button.getImagen().getWidth() &&
                y>button.getCoordenadaY() && y<button.getCoordenadaY()+ button.getImagen().getHeight()){
            //Si pasa el if, cambia a estado pulsado como true
            button.setPulsado(true);
        }
    }

    /**
     * Método encargado de comprobar si se ha soltado el control
     * @param lista parámetro que manda un ArrayList de Click
     * @param button objeto de la clase BotonControl
     */
    public void comprueba_soltado(ArrayList<Click> lista,BotonControl button){

        boolean aux=false;
        //Recorro el listado de clicks
        for(Click c:lista){
            //Si las coordenadas del click están dentro del área del botón, es que el botón está siendo clickado, cambia el estado del aux a true
            if(c.x>button.getCoordenadaX() && c.x<button.getCoordenadaX()+ button.getImagen().getWidth() && c.y>button.getCoordenadaY() && c.y<button.getCoordenadaY()+ button.getImagen().getHeight()) {
                aux = true;
            }
        }
        //Si el aux es falso, es que no está siendo pulsado el área del botón, por lo que el estado del botón pasa a false
        if(!aux){
            button.setPulsado(false);
        }
    }

    //Getter de botones
    public Map<String, BotonControl> getBotones() {
        return botones;
    }
}
