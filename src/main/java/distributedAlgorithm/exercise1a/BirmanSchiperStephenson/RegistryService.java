package distributedAlgorithm.exercise1a.BirmanSchiperStephenson;

import distributedAlgorithm.test.HelloRegistryFacade;
import distributedAlgorithm.test.HelloRegistryFacadeImpl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RegistryService {
    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.createRegistry(1099);
//            UnicastRemoteObject.exportObject(registry, 1099);
            registry.rebind("0", new ProcessorImpl());
//            for(long i = 0; i < processorNum; i++) {
//                registry.rebind(i+"", new ProcessorImpl());
//            }
//            HelloRegistryFacade hello = new HelloRegistryFacadeImpl();
//            registry.rebind("HelloRegistry", hello);
            System.out.println("======= Start RMI Server Success! =======");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
