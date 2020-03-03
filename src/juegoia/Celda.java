
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
    public BufferedImage imagen, biberon;
    public boolean borrar = false;
    public Escenario escenario;
    
    //constructor, para celdas vacías
    public Celda(Escenario e, int x,int y) throws IOException {
        this.x=x;
        this.y=y;
        this.escenario = e;
        tipo = 0; // celda vacia
        imagen = ImageIO.read(new File("src/juegoia/imagenes/floor.png"));
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
                biberon = ImageIO.read(new File("src/juegoia/imagenes/biberonkawaii.png"));
                comestible = true; // significa que la recompensa es visible y se puede obtener
                break;
            case ADVERSARIO:
                imagen = ImageIO.read(new File("src/juegoia/imagenes/calavera.jpg"));
                break;
            case JUGADOR:
                this.direccion = DERECHA;
                imagen = ImageIO.read(new File("src/juegoia/imagenes/canguro_derecha.png"));             
                break;
            case OBSTACULO:
                int random = (int) (Math.random() * 5);
                switch(random){
                    case 0:
                        imagen = ImageIO.read(new File("src/juegoia/imagenes/potato chips.png"));
                        break;
                    case 1:
                        imagen = ImageIO.read(new File("src/juegoia/imagenes/bread.png"));
                        break;
                    case 2:
                        imagen = ImageIO.read(new File("src/juegoia/imagenes/banana.png"));
                        break;
                    case 3:
                        imagen = ImageIO.read(new File("src/juegoia/imagenes/cereal.png"));
                        break;
                    case 4:
                        imagen = ImageIO.read(new File("src/juegoia/imagenes/coffee.png"));
                        break;
                }              
                break;
            default:
                break;
        }        
    }
    
    @Override
    public void update(Graphics g){       
        if(tipo == CELDA_VACIA){
            //g.setColor(Color.ORANGE);
            //g.drawImage(imagen,x,y, null);
            //g.setColor(Color.ORANGE);
            //g.fillRect(x, y, PIXEL_CELDA, PIXEL_CELDA);
        }
        else if(tipo==OBSTACULO){
            g.drawImage(imagen, x, y, null);
            //g.setColor(Color.BLACK);
            //g.fillRect(x, y, PIXEL_CELDA, PIXEL_CELDA);
        }
        else if(tipo==ADVERSARIO){
            try {
                switch (this.direccion) {
                    case ARRIBA:
                        imagen = ImageIO.read(new File("src/juegoia/imagenes/cazador_arriba.png"));
                        break;
                    case ABAJO:
                        imagen = ImageIO.read(new File("src/juegoia/imagenes/cazador_abajo.png"));
                        break;
                    case IZQUIERDA:
                        imagen = ImageIO.read(new File("src/juegoia/imagenes/cazador_izquierda.png"));
                        break;
                    default:
                        imagen = ImageIO.read(new File("src/juegoia/imagenes/cazador_derecha.png"));
                        break;
                }
                } catch (IOException ex) {
                    Logger.getLogger(Celda.class.getName()).log(Level.SEVERE, null, ex);
                }
            g.drawImage(imagen, x, y, null);
            //g.setColor(Color.RED);
            //g.fillRect(x, y, PIXEL_CELDA, PIXEL_CELDA);
        }
        else if(tipo==RECOMPENSA){ 
            if(comestible){ // si la recompensa no ha sido tragada por el jugador entonces será visible (y comestible)
                g.drawImage(biberon,x,y, null);
            }
            else{
                try {
                    imagen = ImageIO.read(new File("src/juegoia/imagenes/floor.png"));
                } catch (IOException ex) {
                    Logger.getLogger(Celda.class.getName()).log(Level.SEVERE, null, ex);
                }
                g.drawImage(imagen,x,y, null);
            }
        }
        else if(tipo==JUGADOR){            
            try {
                switch (this.direccion) {
                    case ARRIBA:
                        imagen = ImageIO.read(new File("src/juegoia/imagenes/canguro_arriba.png"));
                        break;
                    case ABAJO:
                        imagen = ImageIO.read(new File("src/juegoia/imagenes/canguro_abajo.png"));
                        break;
                    case IZQUIERDA:
                        imagen = ImageIO.read(new File("src/juegoia/imagenes/canguro_izquierda.png"));
                        break;
                    default:
                        imagen = ImageIO.read(new File("src/juegoia/imagenes/canguro_derecha.png"));
                        break;
                }
                } catch (IOException ex) {
                    Logger.getLogger(Celda.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            g.drawImage(imagen, this.escenario.jugador.x, this.escenario.jugador.y, null);
            
        g.setColor(Color.BLACK);
        g.setFont(new Font("ComicSans", Font.BOLD, 18));
        g.drawString("Vidas: " + this.escenario.jugador.vida, ANCHO_BORDE_VENTANA, LARGO_BORDE_VENTANA / 3); // vidas
        g.drawString("Puntaje: " + this.escenario.jugador.puntaje, ANCHO_BORDE_VENTANA + 2*PIXEL_CELDA, LARGO_BORDE_VENTANA / 3); // puntaje
        g.drawString("PRESIONE ENTER PARA INICIAR BUSQUEDA", -10*ANCHO_BORDE_VENTANA + ANCHURA_ESCENARIO/2, LARGO_BORDE_VENTANA / 3); // puntaje

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
