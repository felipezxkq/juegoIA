
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.abs;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alumno
 */
public class Adversario extends TimerTask implements Constantes{
    //atributos
    public int x;
    public int y;
    public Escenario escenario;
    public Celda adversario;
    public BusquedaAnchuraAdversario inteligencia_adversario;

    //constructor, inicializa los atributos
    public Adversario(int x,int y, Escenario escenario) throws IOException {
        this.x=x;
        this.y=y;
        this.escenario = escenario;
        this.adversario = new Celda(this.escenario, x, y, ADVERSARIO);
    }
    
    public void moverAdversario(){
        // primero decide si moverse o no 
        if(adversario.x>0){
            
        }
    }
    
     public boolean moverAdversarioArriba(){
        if(this.adversario.y>LARGO_BORDE_VENTANA/2 && noIntersecta(this.adversario.x, this.adversario.y-PIXEL_CELDA)){              
            this.adversario.direccion = ARRIBA;
            this.y = this.y - PIXEL_CELDA;
            this.adversario.y = this.adversario.y - PIXEL_CELDA;
            if(intersectaJugador()){
                this.escenario.jugador.vida--;
                if(!this.escenario.jugador.moverJugadorArriba()){
                    this.escenario.jugador.moverJugadorEnOtraDireccion();
                }
            }
                
            //this.borrar = true;            
            return true;
        }
        else{
            return false;
        }
    }
    public boolean moverAdversarioAbajo(){
        if(this.adversario.y<LARGO_ESCENARIO - 5*LARGO_BORDE_VENTANA/2 && noIntersecta(this.adversario.x, this.adversario.y+PIXEL_CELDA)){
            this.adversario.direccion = ABAJO;
            this.y = this.y + PIXEL_CELDA;
            this.adversario.y = this.adversario.y + PIXEL_CELDA;
            if(intersectaJugador()){
                this.escenario.jugador.vida--;
                if(!this.escenario.jugador.moverJugadorAbajo()){
                    this.escenario.jugador.moverJugadorEnOtraDireccion();
                }
            }                
            //this.borrar = true;
                        
        return true;
        }
        else{
            return false;
        }
    }
    public boolean moverAdversarioDerecha(){
        if(this.adversario.x<ANCHURA_ESCENARIO - 6*ANCHO_BORDE_VENTANA && noIntersecta(this.adversario.x+PIXEL_CELDA, this.adversario.y)){
            this.adversario.direccion = DERECHA;
            this.x = this.x + PIXEL_CELDA;
            this.adversario.x = this.adversario.x + PIXEL_CELDA;
            if(intersectaJugador())
            {
                this.escenario.jugador.vida--;
                if(!this.escenario.jugador.moverJugadorDerecha()){
                    this.escenario.jugador.moverJugadorEnOtraDireccion();
                }                
            }
           //this.borrar = true;          
          
        return true;
        }
        else{
            return false;
        }      
    }
    public boolean moverAdversarioIzquierda(){
        if(this.adversario.x>ANCHO_BORDE_VENTANA && noIntersecta(this.adversario.x-PIXEL_CELDA, this.adversario.y)){
            this.adversario.direccion = IZQUIERDA;
            this.x = this.x - PIXEL_CELDA;
            this.adversario.x = this.adversario.x - PIXEL_CELDA;
            if(intersectaJugador()){
                this.escenario.jugador.vida--;
                if(!this.escenario.jugador.moverJugadorIzquierda()){                    
                    this.escenario.jugador.moverJugadorEnOtraDireccion();
                }
                
            }
             
            //this.borrar = true;            
           
        return true;
        }
        else{
            return false;
        }
    }  
    
    // Se fija en que el adversario no intersecte ni obstáculos ni recompensas
    public boolean noIntersecta(int x, int y) {
        try {
            int tipo = this.escenario.celdas[(x +1 - ANCHO_BORDE_VENTANA/2) / PIXEL_CELDA][(y - LARGO_BORDE_VENTANA / 2) / PIXEL_CELDA].tipo;
            if (tipo == OBSTACULO || tipo == RECOMPENSA) {
                return false;
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }     
        return true;
    }
    
    public boolean intersectaJugador() {
        try {
            int distanciaX, distanciaY;
            distanciaX = abs(this.escenario.jugador.x - this.x);
            distanciaY = abs(this.escenario.jugador.y - this.y);
            if (distanciaX < 5 && distanciaY < 5) {
                return true;
            }           
            return false;

        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
    
    @Override
    public void run(){
        moverAdversario(); 
        escenario.lienzo.repaint();
        
    }
    
    @Override
    public boolean equals(Object x){
        Adversario e = (Adversario)x;
        return this.x==e.x && this.y==e.y;
    }
    
}
