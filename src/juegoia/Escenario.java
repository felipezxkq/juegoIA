import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import javax.swing.JComponent;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Escenario extends JComponent implements Constantes {
    
    public Celda[][] celdas;
    public Adversario[]adversarios;
    int k = 0; // inciar numero de adversarios
    public Jugador jugador;
    public Lienzo lienzo;
    public Timer lanzadorTareas;    
    public int cantidadRecompensas = 0;
    public Image fondo;
    
    int numeroCeldas = NUMERO_CELDAS_ANCHO*NUMERO_CELDAS_ANCHO;
    
    public Escenario(Lienzo lienzo) throws IOException {
        
       this.lienzo = lienzo;        
       celdas=new Celda[NUMERO_CELDAS_ANCHO][NUMERO_CELDAS_LARGO];
       adversarios=new Adversario[NUMERO_CELDAS_ANCHO*NUMERO_CELDAS_ANCHO/10]; 
       lanzadorTareas=new Timer();
       
       fondo = ImageIO.read(new File("src/juegoia/imagenes/fondo.png"));
       
       //inicializar el array de celdas
       for(int i=0; i < NUMERO_CELDAS_ANCHO; i++)
          for ( int j=0 ; j <  NUMERO_CELDAS_LARGO ; j++) {              
              int random = (int) (Math.random() * 100) + 1;
              
              if(random<10 && i!=0 && j!=0) // 10% de las celdas son ocupadas por obstaculos
              {             
                  celdas[i][j]=new Celda(this, i*PIXEL_CELDA + ANCHO_BORDE_VENTANA/2, j*PIXEL_CELDA + LARGO_BORDE_VENTANA/2, OBSTACULO);
              }
              else if(random<15 && i!=0 && j!=0){ // 5% de las celdas son ocupadas por adversarios
                  celdas[i][j]=new Celda(this, i*PIXEL_CELDA+ANCHO_BORDE_VENTANA/2,j*PIXEL_CELDA+LARGO_BORDE_VENTANA/2);         
                  adversarios[k] = new Adversario(i*PIXEL_CELDA+ANCHO_BORDE_VENTANA/2,j*PIXEL_CELDA+LARGO_BORDE_VENTANA/2, this);
                  int movAdversario = (int) (Math.random() * 3) + 1;    
                  lanzadorTareas.scheduleAtFixedRate(adversarios[k], 0, 500*movAdversario);
                  k++;
              }
              else if(random<20 && i!=0 && j!=0) // 5% de las celdas son ocupadas por recompensas
              {             
                  celdas[i][j]=new Celda(this, i*PIXEL_CELDA+ANCHO_BORDE_VENTANA/2,j*PIXEL_CELDA+LARGO_BORDE_VENTANA/2, RECOMPENSA);
                  cantidadRecompensas++;
              }
              else
              {
                  celdas[i][j]=new Celda(this, i*PIXEL_CELDA+ANCHO_BORDE_VENTANA/2,j*PIXEL_CELDA+LARGO_BORDE_VENTANA/2);
              }
          }
       
       this.jugador = new Jugador(1+ANCHO_BORDE_VENTANA/2, LARGO_BORDE_VENTANA/2, this);
       //this.jugador.inteligencia.buscar(1+ANCHO_BORDE_VENTANA/2, LARGO_BORDE_VENTANA/2, PIXEL_CELDA*3+1+ANCHO_BORDE_VENTANA/2, 3*PIXEL_CELDA+LARGO_BORDE_VENTANA/2);
       //this.jugador.inteligencia.calcularRuta();
       //lanzadorTareas.scheduleAtFixedRate(jugador.inteligencia, 0, 1000);
             
    }
    
    @Override
    public void update(Graphics g){
        
        g.drawImage(fondo,ANCHO_BORDE_VENTANA/2,LARGO_BORDE_VENTANA/2, null);
        for(int i=0; i < NUMERO_CELDAS_ANCHO ; i++) 
            for ( int j=0 ; j < NUMERO_CELDAS_LARGO; j++) 
              celdas[i][j].update(g);
        
        for(int i=0; i<k; i++){
            adversarios[i].adversario.update(g);
        }
        jugador.jugador.update(g);
        
    }
    
    public void mostrarDerrota(){
        JOptionPane.showMessageDialog(lienzo, "Perdiste!");
    }
    
    @Override
    public void paintComponent(Graphics g) {      
        update(g);              
    }
}
