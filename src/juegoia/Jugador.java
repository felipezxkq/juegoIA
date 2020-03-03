

import java.awt.event.KeyEvent;
import java.io.IOException;
import static java.lang.Math.abs;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Alumno
 */
public class Jugador extends TimerTask implements Constantes {
    public int x, y, direccion;
    public int vida = 3;
    public int puntaje = 0;
    public boolean borrar = false;
    public int xAnterior;
    public int yAnterior;
    public Escenario escenario;
    public char tipo;
    public Celda jugador;
    public BusquedaAnchura inteligencia;

    public Jugador(int x, int y, Escenario escenario) throws IOException {
        this.x = x;
        this.y = y;
        this.direccion = DERECHA;

        this.xAnterior = x;
        this.yAnterior = y;
        this.escenario = escenario;
        this.jugador = new Celda(this.escenario, x, y, JUGADOR);
        inteligencia = new BusquedaAnchura(this.escenario, this);
    }
    
    // método para mover al jugador dependiendo de si hay o no obstáculos
    public boolean moverJugador(KeyEvent evento) {
        switch (evento.getKeyCode()) {
            case KeyEvent.VK_UP:
                return moverJugadorArriba();

            case KeyEvent.VK_DOWN:
                return moverJugadorAbajo();

            case KeyEvent.VK_LEFT:
                return moverJugadorIzquierda();

            case KeyEvent.VK_RIGHT:
                return moverJugadorDerecha();
            case KeyEvent.VK_ENTER:
                this.escenario.comenzarBuscaquedaAnchura();
        }
        return true;
    }

    public boolean moverJugadorArriba() {
        if (y > LARGO_BORDE_VENTANA / 2 && intersecta(x, y - PIXEL_CELDA) != OBSTACULO) {
            this.jugador.direccion = ARRIBA;
            y = y - PIXEL_CELDA; 

            if (intersectaAdversario()) {
                moverJugadorAbajo();
                moverJugadorAbajo();
                this.vida--;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean moverJugadorAbajo() {
        if (y < LARGO_ESCENARIO - 5 * LARGO_BORDE_VENTANA / 2 && intersecta(x, y + PIXEL_CELDA) != OBSTACULO) {
            y = y + PIXEL_CELDA;
            this.jugador.direccion = ABAJO;
            
            if (intersectaAdversario()) {
                moverJugadorArriba();
                moverJugadorArriba();
                this.vida--;
            }

            return true;
        } else {

            return false;
        }
    }

    public boolean moverJugadorDerecha() {
        if (x < ANCHURA_ESCENARIO - 3 * 2*ANCHO_BORDE_VENTANA && intersecta(x - 1 + PIXEL_CELDA, y) != OBSTACULO) {
            x = x + PIXEL_CELDA;
            this.jugador.direccion = DERECHA;
                    
            if (intersectaAdversario()) {
                moverJugadorIzquierda();
                moverJugadorIzquierda();
                this.vida--;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean moverJugadorIzquierda() {
        if (x > ANCHO_BORDE_VENTANA && intersecta(x - 1 - PIXEL_CELDA, y) != OBSTACULO) {
            x = x - PIXEL_CELDA;
            this.jugador.direccion = IZQUIERDA;
            
            if (intersectaAdversario()) { 
                moverJugadorDerecha();
                moverJugadorDerecha();
                this.vida--;              
            }

            return true;
        } else {
            return false;
        }
    }
    
    public void ver_si_ganaste(){
        if(puntaje==escenario.cantidadRecompensas){
                    this.escenario.lanzadorTareas.cancel();
                    JOptionPane.showMessageDialog(escenario.lienzo, "Ganaste!");
                    System.exit(0);
                }
    }
    
    public void ver_si_perdiste(){
        if(vida==0){
                    this.escenario.lanzadorTareas.cancel();
                    JOptionPane.showMessageDialog(escenario.lienzo, "Perdiste, tu puntaje fue: "+puntaje);
                    System.exit(0);
                }
    }

    public void cambiarPosicion(int x, int y) {
        this.x = x;
        this.y = y;
        this.borrar = true;
    }

    /* método que dice si en el punto x, y hay un obstaculo o recompensa comestible 
    (si pasa esto último entonces se come la recompensa y suma al puntaje) */
    public int intersecta(int x, int y) {
        try {
            int tipo = this.escenario.celdas[(x - ANCHO_BORDE_VENTANA / 2) / PIXEL_CELDA][(y - LARGO_BORDE_VENTANA / 2) / PIXEL_CELDA].tipo;

            if (tipo == OBSTACULO) {
                return OBSTACULO;
            } else if (tipo == RECOMPENSA && this.escenario.celdas[(x - ANCHO_BORDE_VENTANA / 2) / PIXEL_CELDA][(y - LARGO_BORDE_VENTANA / 2) / PIXEL_CELDA].comestible) {

                // hacemos desaparecer la recompensa
                this.escenario.celdas[(x - ANCHO_BORDE_VENTANA / 2) / PIXEL_CELDA][(y - LARGO_BORDE_VENTANA / 2) / PIXEL_CELDA].comestible = false;
                this.puntaje++; 
                return RECOMPENSA;
            } else {
                return 0;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;      
    }

    public boolean intersectaAdversario() {
        try {
            int distanciaX, distanciaY;
            for (int i = 0; i < this.escenario.adversarios.length; i++) {
                distanciaX = abs(this.escenario.adversarios[i].x - this.x);
                distanciaY = abs(this.escenario.adversarios[i].y - this.y);
                if (distanciaX < 5 && distanciaY < 5) {
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
    
    public void dormir(){
        try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
                    }
    }    
    
    @Override
    public void run(){
        escenario.lienzo.repaint();
        
    }

}
