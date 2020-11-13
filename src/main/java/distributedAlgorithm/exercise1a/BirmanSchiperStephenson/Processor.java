package distributedAlgorithm.exercise1a.BirmanSchiperStephenson;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public interface Processor extends Remote {

    void ping() throws RemoteException;

    void notifyChanges() throws RemoteException;

    void notifyAddNewProcessor() throws RemoteException;

    Long getProcessorId() throws RemoteException;

    Registry getRegistry() throws RemoteException;

}
