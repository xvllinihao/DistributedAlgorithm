package distributedAlgorithm.exercise3a.RBA;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;


public class LocalServer {
    public static void main(String[] args) throws RemoteException, NotBoundException, AlreadyBoundException {
        int sum = 7;
        int f = 2;
        Registry registry = LocateRegistry.createRegistry(10990);
        for (int i = 0; i < sum; i++) {
            boolean byzantine = i < f;
            int v = Boolean.compare(true, new Random().nextBoolean());
            ProcessorRMI p = new Processor(i+"", sum, f, v, byzantine);
            String[] processorNames = registry.list();
            new Thread((Runnable) p).start();
            registry.bind(i+"", p);
        }
    }
}
