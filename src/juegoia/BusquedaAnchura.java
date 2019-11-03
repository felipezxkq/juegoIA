/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.TimerTask;

/**
 *
 * @author Felipe
 */
public class BusquedaAnchura extends TimerTask implements Constantes {
    public Escenario escenario;
    public ArrayList<Estado> colaEstados;
    public ArrayList<Estado> historial;
    public ArrayList<Character> pasos;
    public int index_pasos;
    public Estado inicial;
    public Estado objetivo;
    public Estado temp;
    public boolean exito;
    
    // Atributos para búsqueda multiobjetivo
    
    
    public BusquedaAnchura(Escenario escenario){
        this.escenario = escenario;
        colaEstados = new ArrayList<>();
        historial = new ArrayList<>();
        pasos = new ArrayList<>();
        index_pasos = 0;
        exito = false;
    }
    
    public void buscar(int x1, int y1, int x2, int y2){
        inicial = new Estado(x1, y1, 'N', null);
        objetivo = new Estado(x2, y2, 'P', null);
        colaEstados.add(inicial);
        historial.add(inicial);
        
        if(inicial.equals(objetivo)){
            System.out.println("Inicial igual a objetivo");
            exito = true;
        }
        
        while( !colaEstados.isEmpty() && !exito){
            temp = colaEstados.get(0);
            colaEstados.remove(0);
            
            moverArriba(temp);
            moverAbajo(temp);
            moverDerecha(temp);
            moverIzquierda(temp);         
        }
        if(exito) System.out.println("Ruta calculada"); 
        else System.out.println("La ruta no pudo calcularse");
        
    }

    private void moverArriba(Estado e) {
        if(e.y > LARGO_BORDE_VENTANA / 2 && intersecta(e.x, e.y - PIXEL_CELDA)!=OBSTACULO ){
            Estado arriba = new Estado(e.x, e.y - PIXEL_CELDA, 'U', e);
            if(!historial.contains(arriba)){
                colaEstados.add(arriba);
                historial.add(arriba);
                
                if(arriba.equals(objetivo)){
                    objetivo = arriba;
                    exito = true;
                }
            }
        }
    }

    private void moverAbajo(Estado e) {
        if(e.y < LARGO_ESCENARIO - 5 * LARGO_BORDE_VENTANA / 2 && intersecta(e.x, e.y + PIXEL_CELDA)!=OBSTACULO ){
            Estado abajo = new Estado(e.x, e.y + PIXEL_CELDA, 'D', e);
            if(!historial.contains(abajo)){
                colaEstados.add(abajo);
                historial.add(abajo);
                
                if(abajo.equals(objetivo)){
                    objetivo = abajo;
                    exito = true;
                }
            }
        }
    }

    private void moverDerecha(Estado e) {
        if(e.x < ANCHURA_ESCENARIO - 3 * 2*ANCHO_BORDE_VENTANA && intersecta(e.x + PIXEL_CELDA, e.y)!=OBSTACULO){
            Estado derecha = new Estado(e.x + PIXEL_CELDA, e.y, 'R', e);
            if(!historial.contains(derecha)){
                colaEstados.add(derecha);
                historial.add(derecha);
                
                if(derecha.equals(objetivo)){
                    objetivo = derecha;
                    exito = true;
                }
            }
        }
    }

    private void moverIzquierda(Estado e) {
        if(e.y > LARGO_BORDE_VENTANA / 2 && intersecta(e.x - PIXEL_CELDA, e.y)!=OBSTACULO){
            Estado izquierda = new Estado(e.x - PIXEL_CELDA, e.y, 'L', e);
            if(!historial.contains(izquierda)){
                colaEstados.add(izquierda);
                historial.add(izquierda);
                
                if(izquierda.equals(objetivo)){
                    objetivo = izquierda;
                    exito = true;
                }
            }
        }
    }
    
    @Override
    public synchronized void run() {
        System.out.println("PASOS: " + index_pasos);
        if ( index_pasos >= 0 ) {
            switch(pasos.get(index_pasos)) {
                case 'D': escenario.jugador.moverJugadorAbajo();break;
                case 'U': escenario.jugador.moverJugadorArriba(); break;
                case 'R': escenario.jugador.moverJugadorDerecha();break;
                case 'L': escenario.jugador.moverJugadorIzquierda();break;
            }
            escenario.lienzo.repaint();
            index_pasos--;
        }else {
            this.cancel();
        }
    }
    
    public void calcularRuta(){
        Estado predecesor = objetivo;
        System.out.println("Estado predecesor: " + predecesor.toString());
        
        do{
            pasos.add(predecesor.operacion);
            predecesor = predecesor.predecesor;
        }while( predecesor != null);
        index_pasos = pasos.size() - 1;
    }
    
    public int intersecta(int x, int y) {
        try {
            int tipo = this.escenario.celdas[(x - ANCHO_BORDE_VENTANA / 2) / PIXEL_CELDA][(y - LARGO_BORDE_VENTANA / 2) / PIXEL_CELDA].tipo;

            if (tipo == OBSTACULO) {
                return OBSTACULO;
            } else if (tipo == RECOMPENSA && this.escenario.celdas[(x - ANCHO_BORDE_VENTANA / 2) / PIXEL_CELDA][(y - LARGO_BORDE_VENTANA / 2) / PIXEL_CELDA].comestible) {

                // hacemos desaparecer la recompensa
                //this.escenario.celdas[(x - ANCHO_BORDE_VENTANA / 2) / PIXEL_CELDA][(y - LARGO_BORDE_VENTANA / 2) / PIXEL_CELDA].comestible = false;
                return RECOMPENSA;
            } else {
                return 0;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;      
    }
    
    
    
    
}
