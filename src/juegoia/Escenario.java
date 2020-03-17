import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import javax.swing.JComponent;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import juegoia.Tiempo;

public class Escenario extends JComponent implements Constantes {    
    public Celda[][] celdas;
    public Adversario[]adversarios;
    int adversarios_length = 0;
    int k = 0; // inciar numero de adversarios
    public Jugador jugador;
    public Lienzo lienzo;
    public Timer lanzadorTareas;    
    public int cantidadRecompensas = 0;
    public Image fondo;
    public Tiempo tiempo;
    
    int numeroCeldas = NUMERO_CELDAS_ANCHO*NUMERO_CELDAS_ANCHO;
    
    public Escenario(Lienzo lienzo) throws IOException {        
       this.lienzo = lienzo;        
       celdas=new Celda[NUMERO_CELDAS_ANCHO][NUMERO_CELDAS_LARGO];
       adversarios=new Adversario[NUMERO_CELDAS_ANCHO*NUMERO_CELDAS_ANCHO/10]; 
       lanzadorTareas=new Timer();
       tiempo = new Tiempo();
       this.lanzadorTareas.scheduleAtFixedRate(tiempo, 0, 1000);
       
       if(ANCHURA_ESCENARIO > 1500){
           fondo = ImageIO.read(new File("src/juegoia/imagenes/fondo2.png"));
       }else{
           fondo = ImageIO.read(new File("src/juegoia/imagenes/fondo.png"));
       }
       
       
       //inicializar el array de celdas
       for(int i=0; i < NUMERO_CELDAS_ANCHO; i++)
          for ( int j=0 ; j <  NUMERO_CELDAS_LARGO ; j++) {              
              int random = (int) (Math.random() * 100) + 1;
              
              if(random<10 && i!=0 && j!=0) // 10% de las celdas son ocupadas por obstaculos
              {             
                  celdas[i][j]=new Celda(this, i*PIXEL_CELDA + ANCHO_BORDE_VENTANA/2, j*PIXEL_CELDA + LARGO_BORDE_VENTANA/2, OBSTACULO);
              }
              else if(random<13 && i!=0 && j!=0){ // 5% de las celdas son ocupadas por adversarios
                  celdas[i][j]=new Celda(this, i*PIXEL_CELDA+ANCHO_BORDE_VENTANA/2,j*PIXEL_CELDA+LARGO_BORDE_VENTANA/2);         
                  adversarios[k] = new Adversario(i*PIXEL_CELDA+ANCHO_BORDE_VENTANA/2,j*PIXEL_CELDA+LARGO_BORDE_VENTANA/2, this);
                  int movAdversario = (int) (Math.random() * 3) + 1;    
                  //lanzadorTareas.scheduleAtFixedRate(adversarios[k], 0, 1000*movAdversario);
                  k++;
                  adversarios_length++;
              }
              else if(random<17 && i!=0 && j!=0) // 5% de las celdas son ocupadas por recompensas
              {             
                  celdas[i][j]=new Celda(this, i*PIXEL_CELDA+ANCHO_BORDE_VENTANA/2,j*PIXEL_CELDA+LARGO_BORDE_VENTANA/2, RECOMPENSA);
                  cantidadRecompensas++;
              }
              else
              {
                  celdas[i][j]=new Celda(this, i*PIXEL_CELDA+ANCHO_BORDE_VENTANA/2,j*PIXEL_CELDA+LARGO_BORDE_VENTANA/2);
              }
          }
       
       this.jugador = new Jugador(1+ANCHO_BORDE_VENTANA/2, LARGO_BORDE_VENTANA/2, this); // es importante que el jugador sea creado al final            
       comenzarBuscaquedaAnchuraAdversario();
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
        g.setFont(new Font("ComicSans", Font.BOLD, 18));
        g.drawString("Tiempo: " + tiempo, ANCHURA_ESCENARIO - 4*PIXEL_CELDA, LARGO_BORDE_VENTANA / 3); // puntaje

        
    }
    
    public void mostrarDerrota(){
        JOptionPane.showMessageDialog(lienzo, "Perdiste!");
    }
    
    public void comenzarBuscaquedaAnchura(){
        this.jugador.inteligencia.anadirDestinos();
        this.lanzadorTareas.scheduleAtFixedRate(jugador.inteligencia, 0, 300); 
    }
    
    public void comenzarBuscaquedaAnchuraAdversario(){
        for(int i=0; i<adversarios_length; i++){
            adversarios[i].inteligencia_adversario = new BusquedaAnchuraAdversario(this, adversarios[i]);
            this.lanzadorTareas.scheduleAtFixedRate(adversarios[i].inteligencia_adversario, 0, 2050);
        }
    }
  
    
    @Override
    public void paintComponent(Graphics g) {      
        update(g);              
    }
}
