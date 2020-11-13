package distributedAlgorithm.exercise1a.BirmanSchiperStephenson;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {

    void register(Processor processor) throws RemoteException;

    void unregister(Processor processor) throws RemoteException;

    void sendMessage(Processor processor) throws RemoteException;

}
