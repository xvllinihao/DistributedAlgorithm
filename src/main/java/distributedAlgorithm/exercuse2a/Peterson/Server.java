package distributedAlgorithm.exercuse2a.Peterson;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        int numberOfProcessor = 10;
        int[] intialId = new int[]{2, 6, 5, 7, 4, 9, 12, 1, 3, 8};
        try {
            Registry registry = LocateRegistry.createRegistry(10990);
            for (int i = 0; i < numberOfProcessor; i++) {
                ProcessorRMI processor = new Processor(intialId[i],intialId[i == 0? numberOfProcessor-1: i-1],
                        intialId[i == numberOfProcessor-1? 0: i+1],true);
                String processorName = intialId[i] + "";
                registry.rebind(processorName, processor);
                new Thread((Runnable) processor).start();
            }
            System.out.println("=========== Start RMI Server Success! ===========");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
