package com.example.residentevil;

public class Colision {

    //Atributos que indicarán la posición del spriter
    private int charTop;
    private int charRight;
    private int charBottom;
    private int charLeft;

    /**
     * Método Contructor
     * @param charTop
     * @param charRight
     * @param charBottom
     * @param charLeft
     */
    public Colision(int charTop, int charRight, int charBottom, int charLeft) {
        this.charTop = charTop;
        this.charRight = charRight;
        this.charBottom = charBottom;
        this.charLeft = charLeft;
    }


    /**
     * Método que comprueba si se ha colisionado con otro objeto
     * @param c
     * @return
     */
    public boolean isColision(Colision c){
        return isColisionCoordenada(c.getCharLeft(), c.getCharTop())
                || isColisionCoordenada(c.getCharRight(), c.getCharTop())
                || isColisionCoordenada(c.getCharRight(), c.getCharBottom())
                || isColisionCoordenada(c.getCharLeft(), c.getCharBottom());
    }

    /**
     * Método que calcula si las coordenadas coinciden con las coordenadas pasadas como prámetro
     * @param x
     * @param y
     * @return retorna un booleano
     */
    private boolean isColisionCoordenada(int x, int y){
        return this.charTop <= y && this.charBottom >= y
                && this.charLeft <= x && this.charRight >= x;
    }


   //Getter & Setter
    public int getCharTop() {
        return charTop;
    }

    public void setCharTop(int charTop) {
        this.charTop = charTop;
    }

    public int getCharRight() {
        return charRight;
    }

    public void setCharRight(int charRight) {
        this.charRight = charRight;
    }

    public int getCharBottom() {
        return charBottom;
    }

    public void setCharBottom(int charBottom) {
        this.charBottom = charBottom;
    }

    public int getCharLeft() {
        return charLeft;
    }

    public void setCharLeft(int charLeft) {
        this.charLeft = charLeft;
    }
}
