package distributedAlgorithm.exercise3a.RBA;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class Processor extends UnicastRemoteObject implements Runnable, ProcessorRMI {
    private String id;
    private int n;
    private int f;
    private int v;
    private int r;
    private boolean byzantine;
    private double byzantineDontSendProbability = 0.1;
    private boolean decided = false;
    private CountDownLatch notifyCountDownLatch;
    private CountDownLatch proposalCountDownLatch;
    private Stage stage = Stage.NOTIFICATION;
    private final ConcurrentLinkedQueue<Message> notifyMessages = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Message> proposalMessages = new ConcurrentLinkedQueue<>();
    private HashSet<ProcessorRMI> neighborProcessors = new HashSet<>();
    private int delayBound = 5000;

    private final Registry registry;

    public Processor(String id, int n, int f, int v, boolean byzantine) throws RemoteException {
        System.out.println(id + " new processor");
        this.id = id;
        this.n = n;
        this.f = f;
        this.v = v;
        this.byzantine = byzantine;

        this.notifyCountDownLatch = new CountDownLatch(this.n - this.f - 1);
        this.proposalCountDownLatch = new CountDownLatch(this.n - this.f - 1);

        this.registry = LocateRegistry.getRegistry(10990);
        if (!byzantine)
            System.out.println("processor " + id + " initial value = " + v);
    }


//    @Override
    public void notifyNewProcessor() throws RemoteException, NotBoundException {
        String[] processorNames = registry.list();
        for (String name : processorNames) {
            if(!name.equals(this.id)) {
                ProcessorRMI rp = (ProcessorRMI) registry.lookup(name);
                neighborProcessors.add(rp);
            }
        }
    }

    public void notifyBroadcast(Message message) throws InterruptedException {
        for (ProcessorRMI p : neighborProcessors) {
            new Thread(() -> {
                try {
                    Thread.sleep(new Random().nextInt(delayBound));
                    p.receiveNotification(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void proposalBroadcast(Message message) {
        for (ProcessorRMI p : neighborProcessors) {
            new Thread(() ->
            {
                try {
                    Thread.sleep(new Random().nextInt(delayBound));
                    p.receiveProposal(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ).start();
        }
    }

    @Override
    public void receiveNotification(Message message) {
        if((!byzantine && message.round == this.r)
                || (byzantine && new Random().nextDouble() > byzantineDontSendProbability)) {
            notifyMessages.add(message);
            notifyCountDownLatch.countDown();
        }
    }

    @Override
    public void receiveProposal(Message message) {
        if((!byzantine && message.round == this.r)
                || (byzantine && new Random().nextDouble() > byzantineDontSendProbability)) {
            proposalMessages.add(message);
            proposalCountDownLatch.countDown();
        }
    }

    public void decide(Message message) {
        System.out.println(id + " " + message);
    }



    public void changeStage() {
        switch (stage) {
            case NOTIFICATION:
                stage = Stage.PROPOSAL;
                break;
            case PROPOSAL:
                stage = Stage.DECISION;
                break;
            default:
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Processor processor = (Processor) o;
        return id.equals(processor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000 + new Random().nextInt(delayBound));
            notifyNewProcessor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            if (!byzantine) {
                while (n > neighborProcessors.size() + 1) {
                    System.out.println("error");
                }
                try {
                    System.out.println("processor " + id + " start in round " + r + " value = " + v);
                    if (decided) {
                        System.out.println("processor " + id + " end in round " + (r - 1));
                        notifyBroadcast(new Message(Stage.NOTIFICATION, r, v, id));
                        notifyCountDownLatch.await();
                        proposalBroadcast(new Message(Stage.PROPOSAL, r, v, id));
                        proposalCountDownLatch.await();
                        for (; ; ) ;
                    }

                    // notification phase
//                countDownLatch = new CountDownLatch(this.n - this.f - 1);
                    notifyBroadcast(new Message(stage, r, v, id));
                    notifyCountDownLatch.await();
                    System.out.println("processor "+ id+" round "+r+" received notifications: "+notifyMessages);
                    changeStage();
                    // proposal phase
//                countDownLatch = new CountDownLatch(this.n - this.f - 1);
                    ArrayList<Message> zeroMessages =
                            (ArrayList<Message>) notifyMessages.stream().filter(message -> message.v == 0).collect(Collectors.toList());
                    ArrayList<Message> oneMessages =
                            (ArrayList<Message>) notifyMessages.stream().filter(message -> message.v == 1).collect(Collectors.toList());
                    if (zeroMessages.size() > (n + f) / 2) {
                        Message message = new Message(stage, r, 0, id);
                        proposalBroadcast(message);
                    } else if (oneMessages.size() > (n + f) / 2) {
                        Message message = new Message(stage, r, 1, id);
                        proposalBroadcast(message);
                    } else {
                        Message message = new Message(
                                stage,
                                r,
                                changeBoolToInt(new Random().nextBoolean()),
                                id
                        );
                        proposalBroadcast(message);
                    }
                    proposalCountDownLatch.await();
                    System.out.println("processor "+ id +" round "+r+" received proposals: "+proposalMessages);
//                countDownLatch = new CountDownLatch(this.n - this.f - 1);

                    changeStage();

                    //decision phase
                    zeroMessages =
                            (ArrayList<Message>) proposalMessages.stream().filter(message -> message.v == 0).collect(Collectors.toList());
                    oneMessages =
                            (ArrayList<Message>) proposalMessages.stream().filter(message -> message.v == 1).collect(Collectors.toList());
                    if (zeroMessages.size() > f || oneMessages.size() > f) {
                        if (zeroMessages.size() > oneMessages.size()) {
                            v = 0;
                            if (zeroMessages.size() > 3 * f) {
                                decide(new Message(stage, r, v, id));
                                decided = true;
                            }
                        } else {
                            v = 1;
                            if (oneMessages.size() > 3 * f) {
                                decide(new Message(stage, r, v, id));
                                decided = true;
                            }
                        }
                    } else {
                        v = changeBoolToInt(new Random().nextBoolean());
                        System.out.println("processor "+id+" random value in round "+ r + " is "+ v);
                    }

                    r++;
                    notifyMessages.clear();
                    proposalMessages.clear();
                    this.notifyCountDownLatch = new CountDownLatch(this.n - this.f - 1);
                    this.proposalCountDownLatch = new CountDownLatch(this.n - this.f - 1);
                    stage = Stage.NOTIFICATION;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // byzantine
            else {
                try {
                    notifyBroadcast(new Message(Stage.NOTIFICATION, r, changeBoolToInt(new Random().nextBoolean()),id));
                    notifyCountDownLatch.await();
                    proposalBroadcast(new Message(Stage.PROPOSAL, r, changeBoolToInt(new Random().nextBoolean()),id));
                    proposalCountDownLatch.await();

                    r++;
                    notifyMessages.clear();
                    proposalMessages.clear();
                    this.notifyCountDownLatch = new CountDownLatch(this.n - this.f - 1);
                    this.proposalCountDownLatch = new CountDownLatch(this.n - this.f - 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int changeBoolToInt(Boolean bool) {
        return bool ? 1 : 0;
    }
}

enum Stage {
    NOTIFICATION,
    PROPOSAL,
    DECISION
}