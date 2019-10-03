
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

    //constructor, inicializa los atributos
    public Adversario(int x,int y, Escenario escenario) throws IOException {
        this.x=x;
        this.y=y;
        this.escenario = escenario;
        this.adversario = new Celda(this.escenario, x, y, ADVERSARIO);
    }
    
    public void moverAdversario(){
        
        // primero decide si moverse o no
        int random = (int) (Math.random() * 2) + 1;
        
        if(random==1){ // si sale 1, se mueve
            random = (int) (Math.random() * 4) + 1;
            switch(random){
                case 1:
                    moverAdversarioArriba();
                    break;
                case 2:
                    moverAdversarioAbajo();
                    break;
                case 3:
                    moverAdversarioIzquierda();
                    break;
                case 4:
                    moverAdversarioDerecha();
                    break;
            }
            
        }else{
            
        }      
        if(adversario.x>0){
            
        }
    }
    
     public boolean moverAdversarioArriba(){
        if(this.adversario.y>LARGO_BORDE_VENTANA/2 && intersecta(this.adversario.x, this.adversario.y-PIXEL_CELDA)!=OBSTACULO){
              
            this.y = this.y - PIXEL_CELDA;
            this.adversario.y = this.adversario.y - PIXEL_CELDA;
            if(intersectaJugador())
                this.escenario.jugador.vida--;
            //this.borrar = true;            
            return true;
        }
        else{
            return false;
        }
    }
    public boolean moverAdversarioAbajo(){
        if(this.adversario.y<LARGO_ESCENARIO - 5*LARGO_BORDE_VENTANA/2 && intersecta(this.adversario.x, this.adversario.y+PIXEL_CELDA)!=OBSTACULO){
            this.y = this.y + PIXEL_CELDA;
            this.adversario.y = this.adversario.y + PIXEL_CELDA;
            if(intersectaJugador())
                this.escenario.jugador.vida--;
            //this.borrar = true;
                        
        return true;
        }
        else{
            
            return false;
        }
    }
    public boolean moverAdversarioDerecha(){
        if(this.adversario.x<ANCHURA_ESCENARIO - 6*ANCHO_BORDE_VENTANA && intersecta(this.adversario.x+PIXEL_CELDA, this.adversario.y)!=OBSTACULO){
            this.x = this.x + PIXEL_CELDA;
            this.adversario.x = this.adversario.x + PIXEL_CELDA;
            if(intersectaJugador())
                this.escenario.jugador.vida--;
           //this.borrar = true;          
          
        return true;
        }
        else{
            return false;
        }      
    }
    public boolean moverAdversarioIzquierda(){
        if(this.adversario.x>ANCHO_BORDE_VENTANA && intersecta(this.adversario.x-PIXEL_CELDA, this.adversario.y)!=OBSTACULO){
            this.x = this.x - PIXEL_CELDA;
            this.adversario.x = this.adversario.x - PIXEL_CELDA;
            if(intersectaJugador())
                this.escenario.jugador.vida--;
            //this.borrar = true;            
           
        return true;
        }
        else{
            return false;
        }
    }  
    
    /* método que dice si en el punto x, y hay un obstaculo o recompensa comestible 
    (si pasa esto último entonces se come la recompensa y suma al puntaje) */
    public int intersecta(int x, int y){       
        
        try{
        int tipo = this.escenario.celdas[(x - ANCHO_BORDE_VENTANA/2)/PIXEL_CELDA][(y - LARGO_BORDE_VENTANA/2)/PIXEL_CELDA].tipo;
        
        if(tipo==OBSTACULO){
            return OBSTACULO;
        }
        else{
            return 0;
        }            
        }catch(Exception e){
            System.out.println(e);
        }     
        return 0;        
        //         
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
    
}
