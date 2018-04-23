
package servicemoniter;

/**
 * Interface for monitor
 * @author User
 */
public interface Observer {
    public void register(Caller caller,Subject service);
    public void unRegister(Caller caller,Subject service);
    public Object getUpdate(Service service);
    
}
