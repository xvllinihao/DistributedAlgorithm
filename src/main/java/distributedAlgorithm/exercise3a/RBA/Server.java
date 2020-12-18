package distributedAlgorithm.exercise3a.RBA;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;


public class Server {
    public static void main(String[] args) throws RemoteException, NotBoundException, AlreadyBoundException {
        int sum = Integer.parseInt(args[1]);
        int f = Integer.parseInt(args[2]);
        int id = Integer.parseInt(args[3]);
        boolean byzantine = Boolean.parseBoolean(args[4]);
//        int sum = 10;
//        int f = 0;
//        int id = new Random().nextInt(1000);
        Registry registry;
        if (args[0].equals("true")) {
            registry = LocateRegistry.createRegistry(10990);
        }
        else {
            registry = LocateRegistry.getRegistry(10990);
        }
        int v = Boolean.compare(true, new Random().nextBoolean());
        ProcessorRMI p = new Processor(id + "", sum, f, v, byzantine);
        new Thread((Runnable) p).start();
        registry.bind(id + "", p);
//        for (int i = 0; i < sum; i++) {
//            int v = Boolean.compare(true, new Random().nextBoolean());
//            ProcessorRMI p = new Processor(i+"", sum, 4, v);
//            String[] processorNames = registry.list();
//            new Thread((Runnable) p).start();
//            registry.bind(i+"", p);
//        }
    }
}
