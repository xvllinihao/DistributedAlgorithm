package distributedAlgorithm.exercise1a.BirmanSchiperStephenson;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServerImpl extends UnicastRemoteObject implements Server {

    ArrayList<Processor> processors;

    protected ServerImpl() throws RemoteException {
        super();
    }

    @Override
    public void register(Processor processor) throws RemoteException {
        processors.add(processor);
    }

    @Override
    public void unregister(Processor processor) throws RemoteException {
        processors.remove(processor);
    }

    @Override
    public void sendMessage(Processor processor) throws RemoteException {
        for(Processor p : processors) {
            if(!p.equals(processor)) {
                p.notifyChanges();
            }
        }
    }
}
