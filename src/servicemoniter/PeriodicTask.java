package servicemoniter;

import java.util.List;
import java.util.TimerTask;

/**
 * Responsible to do the periodic check of the service state
 *
 * @author User
 */
public class PeriodicTask extends TimerTask {

    public static final int MIN_FREQUENCY = 1000;
    public static final int INIT_TIME = 1000;
    private List<Subject> services;
    private Monitor moniter;

    PeriodicTask(List<Subject> services, Monitor moniter) {
        this.moniter = moniter;
        this.services = services;
    }

    @Override
    public void run() {
        System.out.println("periodick check start on " + System.currentTimeMillis());
        for (Object service : services) {
            Service service1 = (Service) service;
            if (service1.isInOutage()) {//If the service is on outage no need to notify
                continue;
            }

            if (service1.isState() != service1.isOldState()) {
                service1.setOldState(service1.isState());
                moniter.notifyServiceChange(service1);
            }
        }
    }

}
