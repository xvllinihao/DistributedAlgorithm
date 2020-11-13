package distributedAlgorithm.exercise1a.BirmanSchiperStephenson;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

public class ProcessorImpl extends UnicastRemoteObject implements Processor {

    Long processorId;
    Registry registry;
    ArrayList<Processor> processors = new ArrayList<>();
    Vector vector;
    Queue<Message> messageQueue;

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Processor processor = new ProcessorImpl();
        processor.getRegistry().rebind("Processor" + processor.getProcessorId(), processor);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Registry registry = processor.getRegistry();

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public ProcessorImpl() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(1099);
        String[] processorNames = registry.list();
        ArrayList<Processor> processors = new ArrayList<>();
        for(String name : processorNames) {
            processors.add((Processor) registry.lookup(name));
        }
        processorId = generateProcessorId(processors);
    }

    @Override
    public void ping() throws RemoteException {
        for(Processor p : processors) {
            if(!p.equals(this)) {
                p.notifyChanges();
            }
        }
    }

    @Override
    public void notifyChanges() throws RemoteException {

    }

    @Override
    public void notifyAddNewProcessor() throws RemoteException {

    }

    public long generateProcessorId(ArrayList<Processor> existProcessors) throws RemoteException {
        ArrayList<Long> existProcessorIds = new ArrayList<>();
        for(Processor p : processors) {
            existProcessorIds.add(p.getProcessorId());
        }
        long processorId;
        do {
            processorId = new Random().nextLong();
        }
        while (existProcessorIds.contains(processorId));
        return processorId;
    }

    public Long getProcessorId() throws RemoteException {
        return processorId;
    }

    public void setProcessorId(Long processorId) {
        this.processorId = processorId;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public ArrayList<Processor> getProcessors() {
        return processors;
    }

    public void setProcessors(ArrayList<Processor> processors) {
        this.processors = processors;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ProcessorImpl) {
            return ((ProcessorImpl) obj).processorId.equals(this.processorId);
        }
        return false;
    }
}
