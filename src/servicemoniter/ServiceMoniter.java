/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicemoniter;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.TestCase.assertEquals;
/**
 * Test class
 *
 * @author User
 */
public class ServiceMoniter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Service mobitel = new Service("Mobitel", "LOCALHOST", 9999);
        mobitel.setOutageStartBoundary(1524046833);
        mobitel.setOutageEndBoundary(1524056833);
        Service dialog = new Service("Dialog", "LOCALHOST", 9980);
        Service etisalate = new Service("Etisalate", "LOCALHOST", 9998);
        Service hutch = new Service("Hutch", "LOCALHOST", 9080);

        Caller caller1 = new Caller("call center1");
        caller1.setGraceInterval(2000);
        Caller caller2 = new Caller("call center2");
        Caller caller3 = new Caller("call center3");
        caller1.setGraceInterval(500);

        Monitor moniter = new Monitor();
        moniter.register(caller1, mobitel);
        moniter.register(caller2, mobitel);

        moniter.register(caller1, dialog);
        moniter.register(caller2, dialog);
        moniter.register(caller3, dialog);
        moniter.unRegister(caller1, dialog);

        moniter.register(caller1, hutch);
        moniter.register(caller3, hutch);

        moniter.establishConnection(dialog);
        moniter.establishConnection(mobitel);
        moniter.establishConnection(hutch);
        
        assertEquals(moniter.ADJUSTED_FREQUENCY, 500);
        moniter.startMonitoring();

        try {
            mobitel.setServiceDown();
            TimeUnit.SECONDS.sleep(2);
            mobitel.setServiceUp();
            dialog.setServiceDown();
            TimeUnit.SECONDS.sleep(2);
            dialog.setServiceUp();
            hutch.setServiceDown();
            TimeUnit.SECONDS.sleep(2);
            hutch.setServiceUp();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServiceMoniter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
