import java.io.IOException;
import javax.swing.JFrame;

public class Main {
    
    public static void main (String[]args) throws IOException {
        
        VentanaPrincipal vp=new VentanaPrincipal();
        vp.setVisible(true);
        vp.setExtendedState(vp.getExtendedState()|JFrame.MAXIMIZED_BOTH);       
        
        vp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    }    

}

// FUENTES: JFrame maximizado: https://docs.oracle.com/javase/7/docs/api/javax/swing/JFrame.html
