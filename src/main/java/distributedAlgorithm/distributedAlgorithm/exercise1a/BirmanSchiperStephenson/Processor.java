package distributedAlgorithm.exercise1a.BirmanSchiperStephenson;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Processor extends Remote {

    void send() throws RemoteException, InterruptedException;

    void receive(Message message) throws RemoteException, InterruptedException;

}
