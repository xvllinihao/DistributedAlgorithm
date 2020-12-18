package distributedAlgorithm.exercuse2a.Peterson;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProcessorRMI extends Remote {
    void send(String downstreamId, String infoType) throws RemoteException;
    
    void receive(String upstreamId) throws RemoteException;

    boolean getState() throws RemoteException;

    int getId() throws RemoteException;

    int getDownstreamId() throws RemoteException;

    int getUpstreamId() throws RemoteException;

    void infoChange(String infoType, int info) throws RemoteException;

    void stateChange() throws RemoteException;
}
