/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoia;

import java.util.TimerTask;

/**
 *
 * @author Felipe
 */
public class Tiempo extends TimerTask {
    int tiempo;
    
    public Tiempo(){
        tiempo = 0;
    }

    @Override
    public void run(){
        this.tiempo++;        
    }
    
    @Override
    public String toString() {
        return ""+tiempo;
    }
}


