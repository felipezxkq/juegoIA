
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class Lienzo extends Canvas implements Constantes{
    
    public Graphics graficoBuffer;
    public Image imagenBuffer;
   
    public Escenario escenario;
    
    public Lienzo() throws IOException{
        escenario=new Escenario(this);
        this.setBackground(Color.orange);
        this.setSize(ANCHURA_ESCENARIO,LARGO_ESCENARIO);
       
        addKeyListener(new java.awt.event.KeyAdapter(){            
            @Override
            public void keyPressed(KeyEvent e){
                
                // se obtiene las coordenadas de donde estaba el jugador antes de moverse
                int x = escenario.jugador.x;
                int y = escenario.jugador.y;
                
                // si el jugador se movio repintamos
                if(escenario.jugador.moverJugador(e)){                    
                    repaint();                   
                }
            }        
        });
                
        addMouseListener(new MouseAdapter(){         
            
            @Override
            public void mousePressed(MouseEvent evt) {                
                seleccionarJugador(evt);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                escenario.jugador.jugador.celdaSeleccionada = false;
                repaint();
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent evt) {
                moverJugador(evt);
                repaint();
            }
        });       
                
    }
    
    @Override
    public void update(Graphics g){
        
        if(graficoBuffer==null){
            imagenBuffer = createImage(this.getWidth(), this.getHeight());
            graficoBuffer = imagenBuffer.getGraphics();          
        }
        graficoBuffer.setColor(getBackground());
        graficoBuffer.fillRect(0,0,this.getWidth(),this.getHeight());
        
        escenario.update(graficoBuffer);
        g.drawImage(imagenBuffer, 0, 0, null);
    }
    
    //metodo paint para pintar el escnario
    @Override
    public void paint(Graphics g) {
       update(g); 
    }
    
    
    private void activarCelda(MouseEvent evt){
        for(int i=0; i<NUMERO_CELDAS_ANCHO; i++){
            for(int j=0; j<NUMERO_CELDAS_LARGO; j++){
                escenario.celdas[i][j].comprobarSiCeldaSeleccionada(evt.getX(), evt.getY());
                //escenario.jugador.cambiarPosicion(j, j);
            }
        }        
    }
    
    public void seleccionarJugador(MouseEvent evt){
        escenario.jugador.jugador.comprobarSiCeldaSeleccionada(evt.getX(), evt.getY());
    }
    
    public void moverJugador(MouseEvent evt){
        if(escenario.jugador.jugador.celdaSeleccionada){
            escenario.jugador.jugador.x = evt.getX();
        escenario.jugador.jugador.y = evt.getY();
        }
    }
    
}
