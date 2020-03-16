/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 *
 * @author Felipe
 */
public class BusquedaAnchuraAdversario extends TimerTask implements Constantes {
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
    public Adversario adversario;
    public ArrayList<Estado> destinos;
    public boolean parar;
    
    public BusquedaAnchuraAdversario(Escenario escenario, Adversario adversario){
        this.escenario = escenario;
        colaEstados = new ArrayList<>();
        historial = new ArrayList<>();
        pasos = new ArrayList<>();
        index_pasos = 0;
        exito = false;
        
        this.adversario = adversario;
        destinos = new ArrayList<>();
        parar = false;
        //encontrarUbicacionRecompensas();
    }
    
    public boolean buscar(Estado inicial, Estado objetivo){
        index_pasos = 0;        
        colaEstados.add(inicial);
        historial.add(inicial);
        this.objetivo = objetivo;
        System.out.println("Objetivo: "+objetivo);
        System.out.println("Estoy: "+ inicial);
        exito = false;
        
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
        if(exito){            
            this.calcularRuta();
            System.out.println("Ruta calculada, yendo hacia: " + objetivo);
            return true;
        } 
        else{
            System.out.println("La ruta no pudo calcularse");
            return false;
        }
    }

    private void moverArriba(Estado e) {
        if(e.y > LARGO_BORDE_VENTANA / 2 && noIntersecta(e.x, e.y - PIXEL_CELDA)){
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
        if(e.y < LARGO_ESCENARIO - 5 * LARGO_BORDE_VENTANA / 2 && noIntersecta(e.x, e.y + PIXEL_CELDA)){
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
        if(e.x < ANCHURA_ESCENARIO - 3 * 2*ANCHO_BORDE_VENTANA && noIntersecta(e.x + PIXEL_CELDA, e.y)){
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
        if(e.x > ANCHO_BORDE_VENTANA / 2 && noIntersecta(e.x - PIXEL_CELDA, e.y) ){
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
        if(!parar){            
            colaEstados.clear();
            historial.clear();
            pasos.clear();
            Estado subinicial, subobjetivo;
            boolean resultado;
            
            do{                
                subinicial = new Estado(adversario.x, adversario.y, 'N', null);
                destinos.add(0, new Estado(this.escenario.jugador.x-1, this.escenario.jugador.y, 'N', null));
                subobjetivo = destinos.get(0);
                resultado = this.buscar(subinicial, subobjetivo);   
                
                
                if(subinicial.equals(subobjetivo)){
                    destinos.remove(subobjetivo);
                }
                
                if(destinos.isEmpty()){
                    System.out.println("Se acabó a donde ir");
                    this.cancel();
                }
            }while(!resultado && !destinos.isEmpty());
            
            if( pasos.size() > 1){
                System.out.println("CANTIDAD PASOS: "+pasos.size());
                switch(pasos.get(1)) {
                case 'D': this.adversario.moverAdversarioAbajo();break;
                case 'U': this.adversario.moverAdversarioArriba(); break;
                case 'R': this.adversario.moverAdversarioDerecha();break;
                case 'L': this.adversario.moverAdversarioIzquierda();break;
                }
                escenario.lienzo.repaint();
            }
        }
    }
    
    public void calcularRuta(){
        Estado predecesor = objetivo;
        System.out.println("Estado predecesor: " + predecesor.toString());
        
        do{
            pasos.add(0, predecesor.operacion);
            predecesor = predecesor.predecesor;
        }while( predecesor != null);
        index_pasos = pasos.size() - 1;
    }
    
    // Se fija que en el 'recorrido virtual' de anchura no intersecte adverarios u obstáculos
    public boolean noIntersecta(int x, int y) {
        try {
            int tipo = this.escenario.celdas[(x +1 - ANCHO_BORDE_VENTANA/2) / PIXEL_CELDA][(y - LARGO_BORDE_VENTANA / 2) / PIXEL_CELDA].tipo;
            if (tipo == OBSTACULO) {
                return false;
            } 
            
            if(intersectaAdversario(x, y)){
                return false;
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }     
        return true;
    }
    
    public boolean intersectaAdversario(int x, int y) {  
        try {
            Integer distanciaX, distanciaY;
            for (int i=0; i<this.escenario.adversarios_length; i++) {
                if(this.adversario != this.escenario.adversarios[i]){
                    distanciaX = Math.abs(this.escenario.adversarios[i].x - x);
                    distanciaY = Math.abs(this.escenario.adversarios[i].y - y);

                    if ( (distanciaX < 5 && distanciaY < 5) ) {
                        return true;
                    }  
                }                             
            }
            return false;
        } catch (Exception e) {
            System.out.println("Problema al calcular ruta para adversarios sin intersectar adversarios");
            System.out.println(e);
        } 
        return false;      
    }
    
    // Ve donde están las recompensas en el escenario y las agrega a los destinos 
    public void anadirDestinos(){
        for(int i=0; i<NUMERO_CELDAS_ANCHO; i++){
            for(int j=0; j<NUMERO_CELDAS_LARGO; j++){
                if(this.escenario.celdas[i][j].tipo == RECOMPENSA && this.escenario.celdas[i][j].comestible){
                    int x_recompensa = this.escenario.celdas[i][j].x+1;
                    int y_recompensa = this.escenario.celdas[i][j].y;
                    System.out.println("Objetivo: (x, y) = " + x_recompensa + ", " + y_recompensa);
                                           
                    this.destinos.add(new Estado(x_recompensa, y_recompensa, 'N', null));                   
                    
                }
            }
            
        }
    }
    
    
    
    
}
