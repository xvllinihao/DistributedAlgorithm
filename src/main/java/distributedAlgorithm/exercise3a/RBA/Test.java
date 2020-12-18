package distributedAlgorithm.exercise3a.RBA;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Test {

    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(10990);
    }
}
