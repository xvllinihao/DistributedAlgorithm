package distributedAlgorithm.test;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloRegistryFacade extends Remote {
    String helloWorld(String name) throws RemoteException;
}
