
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;


public class Celda extends JComponent implements Constantes {
    //atributos
    public int x;
    public int y;
    public int tipo, direccion;
    public boolean celdaSeleccionada;
    public boolean comestible;
    public BufferedImage imagen, basuraMar;
    public boolean borrar = false;
    public Escenario escenario;
    
    //constructor, para celdas de mar
    public Celda(Escenario e, int x,int y) throws IOException {
        this.x=x;
        this.y=y;
        this.escenario = e;
        tipo = 0; // celda vacia
        imagen = ImageIO.read(new File("src/juegoia/imagenes/celdaMar64x64.jpg"));
    }
    
    // constructor para crear celdas obstaculo o recompensa
    public Celda(Escenario e, int x,int y, int tipo) throws IOException {
        this.x=x;
        this.y=y;
        this.escenario = e;
        this.tipo = tipo;
        celdaSeleccionada = false;
        
        switch (tipo) {
            case RECOMPENSA:
                basuraMar = ImageIO.read(new File("src/juegoia/imagenes/biberonkawaii.jpg"));
                comestible = true; // significa que la recompensa es visible y se puede obtener
                break;
            case ADVERSARIO:
                imagen = ImageIO.read(new File("src/juegoia/imagenes/calavera.jpg"));
                break;
            case JUGADOR:
                this.direccion = DERECHA;
                imagen = ImageIO.read(new File("src/juegoia/imagenes/canguro_derecha.jpg"));             
                break;
            case OBSTACULO:
                imagen = ImageIO.read(new File("src/juegoia/imagenes/obstaculo64x64.jpg"));
                break;
            default:
                break;
        }        
    }
    
    @Override
    public void update(Graphics g){
        
        g.setColor(Color.BLACK);
        g.drawRect(x,y,PIXEL_CELDA,PIXEL_CELDA);
        
        if(tipo == CELDA_VACIA){
            //g.setColor(Color.ORANGE);
            g.drawImage(imagen,x,y, null);
            //g.setColor(Color.ORANGE);
            //g.fillRect(x, y, PIXEL_CELDA, PIXEL_CELDA);
        }
        else if(tipo==OBSTACULO){
            g.drawImage(imagen, x, y, null);
            //g.setColor(Color.BLACK);
            //g.fillRect(x, y, PIXEL_CELDA, PIXEL_CELDA);
        }
        else if(tipo==ADVERSARIO){
            g.drawImage(imagen, x, y, null);
            //g.setColor(Color.RED);
            //g.fillRect(x, y, PIXEL_CELDA, PIXEL_CELDA);
        }
        else if(tipo==RECOMPENSA){ 
            if(comestible){ // si la recompensa no ha sido tragada por el jugador entonces será visible (y comestible)
                g.drawImage(basuraMar,x,y, null);
                
                /*                
                g.setColor(Color.magenta);
                g.fillRect(x,y,PIXEL_CELDA,PIXEL_CELDA);
                g.setFont(new Font("ComicSans", Font.BOLD, 13));
                g.setColor(Color.ORANGE);
                g.drawString("R", x+ PIXEL_CELDA*1/8, y + PIXEL_CELDA*3/7);
*/
            }
            else{
                try {
                    imagen = ImageIO.read(new File("src/juegoia/imagenes/celdaMar64x64.jpg"));
                } catch (IOException ex) {
                    Logger.getLogger(Celda.class.getName()).log(Level.SEVERE, null, ex);
                }
                g.drawImage(imagen,x,y, null);
            }
        }
        else if(tipo==JUGADOR){            
                try {
                if(this.direccion == ARRIBA){
                    imagen = ImageIO.read(new File("src/juegoia/imagenes/canguro_arriba.jpg"));             
                }
                else if(this.direccion == ABAJO){
                    imagen = ImageIO.read(new File("src/juegoia/imagenes/canguro_abajo.jpg"));
                }
                else if(this.direccion == IZQUIERDA){
                    imagen = ImageIO.read(new File("src/juegoia/imagenes/canguro_izquierda.jpg"));
                }
                else{
                    imagen = ImageIO.read(new File("src/juegoia/imagenes/canguro_derecha.jpg"));
                }
                } catch (IOException ex) {
                    Logger.getLogger(Celda.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            g.drawImage(imagen, x, y, null);
            if (borrar) {
            g.setColor(Color.ORANGE);
            g.fillRect(ANCHO_BORDE_VENTANA - 9, LARGO_BORDE_VENTANA / 3 - 20, PIXEL_CELDA, PIXEL_CELDA);
            g.setColor(Color.ORANGE);
            g.fillRect(ANCHO_BORDE_VENTANA + PIXEL_CELDA - 9, LARGO_BORDE_VENTANA / 3 - 20, PIXEL_CELDA, PIXEL_CELDA);
            this.borrar = false;
        }
        g.setColor(Color.BLUE);
        g.setFont(new Font("ComicSans", Font.BOLD, 18));
        g.drawString("Energía: " + this.escenario.jugador.energy, ANCHO_BORDE_VENTANA, LARGO_BORDE_VENTANA / 3); // puntaje
        g.drawString("Vidas: " + this.escenario.jugador.vida, ANCHO_BORDE_VENTANA + 5*PIXEL_CELDA, LARGO_BORDE_VENTANA / 3); // vidas
        g.drawString("Puntaje: " + this.escenario.jugador.puntaje, ANCHO_BORDE_VENTANA + 7/2*PIXEL_CELDA, LARGO_BORDE_VENTANA / 3); // vidas

        }
        
        if(celdaSeleccionada){
            g.setColor(Color.BLACK);
            g.drawRect(x,y,63, 63);
        }       
    }
    
    //metodo de JComponent para pintar en un contexto grafico
    @Override
    public void paintComponent(Graphics g) {         
        update(g);     
    } 
    
    public void comprobarSiCeldaSeleccionada(int clickX,int clickY){
        Rectangle rectanguloCelda = new Rectangle(x, y, PIXEL_CELDA, PIXEL_CELDA);
        if(rectanguloCelda.contains(new Point(clickX, clickY)))
            celdaSeleccionada = !celdaSeleccionada;                        
    }
    
    // método usado para ver si el jugador choca con recompensas u obstáculos
    public boolean intersecta(int x, int y){
         Rectangle rectanguloCelda = new Rectangle(x, y, PIXEL_CELDA, PIXEL_CELDA);
        if(rectanguloCelda.contains(new Point(x, y)))
            return true;
        else
            return false;
    }
    
}
