package distributedAlgorithm.exercise1a.BirmanSchiperStephenson;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(1099);
        String[] processorNames = registry.list();
        ArrayList<Processor> processors = new ArrayList<>();
        for(String name : processorNames) {
            processors.add((Processor) registry.lookup(name));
        }
    }
}


