package distributedAlgorithm.exercise1a.BirmanSchiperStephenson;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RegistryService {
    public static void main(String[] args) {
        int numberOfProcessor = 3;
        try {
            Registry registry = LocateRegistry.createRegistry(10990);
            for (int i = 0; i < numberOfProcessor; i++) {
                String processorName = i + "";
                Processor processor = new ProcessorImpl(numberOfProcessor, i);
                registry.rebind(processorName, processor);
                new Thread((Runnable) processor).start();
            }
            System.out.println("======= Start RMI Server Success! =======");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
