
import java.awt.Dimension;
import java.awt.Toolkit;


public interface Constantes {
    
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    //size of the cells
    public final int PIXEL_CELDA=64;
    public final int PIXEL_MOV_JUGADOR = PIXEL_CELDA/2 - 5;
    
    public final int ANCHURA_ESCENARIO=screenSize.width;
    public final int LARGO_ESCENARIO=screenSize.height;
    
    //size of the stage
    public final int ANCHO_BORDE_VENTANA=20;
    public final int LARGO_BORDE_VENTANA=45;
    //number of cells - width
    public final int NUMERO_CELDAS_ANCHO=(screenSize.width-ANCHO_BORDE_VENTANA)/PIXEL_CELDA;
    //number of cells - height
    public final int NUMERO_CELDAS_LARGO=(screenSize.height-LARGO_BORDE_VENTANA)/PIXEL_CELDA;
    
    public final int JUGADOR_RADIO = 30;  
    
    // constantes de los actores
    public final int CELDA_VACIA = 0;
    public final int OBSTACULO = 1;
    public final int RECOMPENSA = 2;
    public final int ADVERSARIO = 3;
    public final int JUGADOR = 4;
    
    // direcciones
    public final int ARRIBA = 1, ABAJO=2, IZQUIERDA=3, DERECHA=4;
    
    // variables de menu
    public int FUENTE_SIZE = 12;
    public int CELDA_SIZE = 32;
    public int N=31, M=21;
    public String RUTA_DIRECTORIO=System.getProperty( "user.dir" );

    
}
