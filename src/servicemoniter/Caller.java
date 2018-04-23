/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicemoniter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Responsible to do all the responsibilities of caller
 *
 * @author User
 */
public class Caller {

    private int graceInterval;
    private List<Subject> services;
    private String name;

    public Caller(String name) {
        services = new ArrayList<>();
        this.name = name;
    }

    public int getGraceInterval() {
        return graceInterval;
    }

    public void setGraceInterval(int graceInterval) {
        this.graceInterval = graceInterval;
    }

    public void setService(Service service) {
        services.add(service);
    }

    public List<Subject> getServices() {
        return services;
    }

    public void NotifyServiceChange(Service service) {
        Logger.getLogger(" Service[" + service.getName() + "]goes down for client :"+ getName());
        System.out.println("Service[" + service.getName() + "]goes down for client :"+ getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
