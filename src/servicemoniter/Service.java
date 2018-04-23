package servicemoniter;

import java.util.logging.Logger;

/**
 * Responsible to represent the service 
 * All the responsibilities of service will do in here
 * @author User
 */
public class Service implements Subject {

    private String message;
    private String name;
    private boolean state;//kep the current status (online/offline)
    private boolean oldState;//kep the old status (online/offline)
    private String host;
    private int port;
    private long outageStartBoundary;
    private long outageEndBoundary;
    private long lastUpdate;

    public Service(String name, String host, int port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public boolean isState() {
        return state;
    }

    String getMessage() {
        return message;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setOutageStartBoundary(long outageStartBoundary) {
        this.outageStartBoundary = outageStartBoundary;
    }

    public void setOutageEndBoundary(long outageEndBoundary) {
        this.outageEndBoundary = outageEndBoundary;
    }

    public long getOutageStartBoundary() {
        return outageStartBoundary;
    }

    public long getOutageEndBoundary() {
        return outageEndBoundary;
    }

    public boolean isOldState() {
        return oldState;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setOldState(boolean oldState) {
        this.oldState = oldState;
    }

    /**
     * Responsible to check the service is in outage level
     *
     * @return
     */
    public boolean isInOutage() {
        return (getOutageStartBoundary() <= System.currentTimeMillis() && System.currentTimeMillis() <= getOutageEndBoundary());
    }
    
    /**
     * Responsible to set the service down
     * @return 
     */
    public boolean setServiceDown(){
        Logger.getLogger(" Service ["+ this.getName() +"] Down succesfully...");
        System.out.println(" Service ["+ this.getName() +"] Down succesfully...");
        setState(false);
        return true;
    }
    /**
     * Responsible to set the service Up
     * @return 
     */
    public boolean setServiceUp(){
        setState(true);
        Logger.getLogger(" Service ["+ this.getName() +"] Up successfully...");
        System.out.println(" Service ["+ this.getName() +"] Up successfully...");
        return true;
    }

}
