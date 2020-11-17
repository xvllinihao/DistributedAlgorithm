package distributedAlgorithm.exercise1a.BirmanSchiperStephenson;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ProcessorImpl extends UnicastRemoteObject implements Processor, Runnable {

    private final int processorId;
    private Registry registry;
    private final ArrayList<Processor> otherProcessors = new ArrayList<>();
    private Vector vector;
    private final ConcurrentLinkedDeque<Message> messageBuffer = new ConcurrentLinkedDeque<>();

    public static void main(String[] args) {

    }

    public ProcessorImpl(int processorNumber, int processorId) throws RemoteException {
        this.registry = LocateRegistry.getRegistry(10990);
        this.vector = new Vector(processorNumber);
        this.processorId = processorId;
    }

    @Override
    public void send() throws RemoteException, InterruptedException {
        vector.addOne(this.processorId);
        Message message = new Message(
                processorId,
                System.nanoTime() / 1000 + "",
                this.vector);
        for(Processor p : otherProcessors) {
            if(!p.equals(this)) {
                p.receive(message);
                Thread.sleep(new Random().nextInt(3000));
            }
        }
    }

    @Override
    public void receive(Message message) throws RemoteException {
        int[] tmpV = this.vector.v.clone();
        // V + ej
        tmpV[message.processorId] += 1;
        Vector tmpVector = new Vector(tmpV);
        // Vm
        Vector Vm = message.vector;
        // V + ej >= Vm
        if(tmpVector.bigOrEqualThan(Vm)) {
            confirmReceive(message);
            this.vector = tmpVector;
            goThoughBuffer();
        }
        else {
            System.out.println(this.processorId + " add to buffer " + message);
            messageBuffer.add(message);
        }
    }

    public void goThoughBuffer() {
        boolean flag;
        do {
            flag = false;
            for(Message message : messageBuffer) {
                int[] tmpV = this.vector.v.clone();
                // V + ej
                tmpV[message.processorId] += 1;
                Vector tmpVector = new Vector(tmpV);
                // Vm
                Vector Vm = message.vector;
                // V + ej >= Vm
                if(tmpVector.bigOrEqualThan(Vm)) {
                    confirmReceive(message);
                    System.out.println(this.processorId + " out of buffer");
                    this.vector = tmpVector;
                    messageBuffer.remove(message);
                    flag = true;
                }
            }
        }
        while (flag);
    }

    public void confirmReceive(Message message) {
        System.out.println(this.processorId + " " + message.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ProcessorImpl) {
            return ((ProcessorImpl) obj).processorId == (this.processorId);
        }
        return false;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(new Random().nextInt(1000)); // Wait other processors ready
            registry = LocateRegistry.getRegistry(10990);
            String[] processorNames = registry.list();
            for (String name : processorNames) {
                if(!name.equals(this.processorId + "")) {
                    Processor processor = (Processor) registry.lookup(name);
                    otherProcessors.add(processor);
                }
            }
            while (true) {
                send();
//                Thread.sleep(1000);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }
}
