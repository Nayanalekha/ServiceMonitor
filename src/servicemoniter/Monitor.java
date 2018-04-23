package servicemoniter;

import java.util.ArrayList;
import java.util.List;
import java.net.Socket;
import java.io.IOException;
import java.util.Timer;
import java.util.logging.Logger;

/**
 * Responsible to do the all the responsibilities of monitor here
 *
 * @author User
 */
public class Monitor implements Observer {

    private List<Subject> services;
    private List<Caller> callerList;
    private final Object MUTEX = new Object();
    public int ADJUSTED_FREQUENCY = PeriodicTask.MIN_FREQUENCY;

    public Monitor() {
        this.callerList = new ArrayList<>();
        this.services = new ArrayList<>();

    }

    /**
     * Responsible to register a caller with specific service
     *
     * @param caller
     * @param service
     */
    @Override
    public void register(Caller caller, Subject service) {
        if (service == null) {
            Logger.getLogger("Invalid Service ...");
        }
        if (caller == null) {
            Logger.getLogger("Invalid Caller..");
        }

        if (!callerList.contains(caller)) {
            callerList.add(caller);
            if (caller.getGraceInterval()!= 0 && caller.getGraceInterval() < ADJUSTED_FREQUENCY) {
                ADJUSTED_FREQUENCY = caller.getGraceInterval();
            }
        }
        if (!services.contains(service)) {
            services.add(service);
            System.out.println("Service [" + service.getName() + "] registered succeessfully....");

        }
        if (caller.getServices().contains(service)) {
            Logger.getLogger("Service [" + service.getName() + "] already registered....");
            System.out.println("Service [" + service.getName() + "] already registered....");
        } else {
            synchronized (MUTEX) {
                caller.getServices().add(service);
            }
        }
    }

    /**
     * Responsible to unregister the service from caller
     *
     * @param caller
     * @param service
     */
    @Override
    public void unRegister(Caller caller, Subject service) {
        if (service == null) {
            Logger.getLogger("Invalid Service..");

            throw new NullPointerException("Invalid Service...");
        }
        if (caller == null) {
            Logger.getLogger("Invalid Caller..");

            throw new NullPointerException("Invalid Caller...");
        }
        if (!callerList.contains(caller)) {
            Logger.getLogger("Invalid Caller..");

            System.out.println("Invalid caller...");
            return;
        }
        if (caller.getServices().contains(service)) {
            synchronized (MUTEX) {
                caller.getServices().remove(service);
            }
            Logger.getLogger(" Service [" + service.getName() + "] unregistered successfully....");
            System.out.println(" Service [" + service.getName() + "] unregistered successfully....");


        }

    }

    /**
     * Responsible to establish a connection with specified service The monitor
     * will establish a TCP connection to the host on the specified port. If a
     * connection is established, the service is up, if the connection is
     * refused, the service is not up.
     *
     * @param service
     * @return
     */
    public boolean establishConnection(Service service) {
        Socket socket = null;
        try {
            socket = new Socket(service.getHost(), service.getPort());
            if (socket.isConnected()) {
                service.setState(true);
            } else {
                service.setState(false);
            }
        } catch (Exception e) {
            service.setState(false);

        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
            }
        }
        return service.isState();
    }

    @Override
    public String getUpdate(Service service) {
        return service.getMessage();
    }

    /**
     * Responsible to notify the service change(up/down) to the registered
     * clients The callers will notify only if exceeds the callers grace period
     * of the notify
     *
     * @param service
     */
    public void notifyServiceChange(Service service) {
        callerList.stream().map((caller) -> (Caller) caller).filter((caller1) -> (caller1.getServices().contains(service)
                && (service.getLastUpdate() + caller1.getGraceInterval()) <= System.currentTimeMillis())).forEachOrdered((caller1) -> {
            caller1.NotifyServiceChange(service);
        });
        service.setLastUpdate(System.currentTimeMillis());
    }

    /**
     * Responsible to start the monitoring services
     */
    public void startMonitoring() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new PeriodicTask(services, this), PeriodicTask.INIT_TIME,
                ADJUSTED_FREQUENCY);
        System.out.println("System periodick monitoring started on frequency : " + ADJUSTED_FREQUENCY );
    }
    


}
