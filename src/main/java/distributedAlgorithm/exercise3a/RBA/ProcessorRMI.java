package distributedAlgorithm.exercise3a.RBA;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProcessorRMI extends Remote {
    void receiveNotification(Message message) throws RemoteException, NotBoundException;
    void receiveProposal(Message message) throws RemoteException, NotBoundException;
}
