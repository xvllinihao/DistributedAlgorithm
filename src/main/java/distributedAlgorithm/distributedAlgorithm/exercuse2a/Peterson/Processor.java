package distributedAlgorithm.exercuse2a.Peterson;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Processor extends UnicastRemoteObject implements ProcessorRMI, Runnable{
    private int id;
    private int tid;
    private int ntid;
    private int nntid;
    private int upstreamId;
    private int downstreamId;
    private boolean state;
    private Registry registry;

    public Processor(int id,int upstreamId, int downstreamId, boolean state) throws RemoteException {
        this.id = id;
        this.state = state;
        this.tid = id;
        this.downstreamId = downstreamId;
        this.upstreamId = upstreamId;
        this.registry = LocateRegistry.getRegistry(10990);
    }

    @Override
    public void send(String downstreamId, String infoType) throws RemoteException {
        try {
            int info;
            ProcessorRMI downstream = (ProcessorRMI) registry.lookup(downstreamId);
            switch (infoType){
                case "ntid":
                    info = tid;
                    break;
                case "nntid":
                    info = Math.max(tid, ntid);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + infoType);
            }
            downstream.infoChange(infoType,info);
            System.out.println("send "+infoType+" to"+downstreamId);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(String upstreamId) throws RemoteException {
        try {
            ProcessorRMI upstream = (ProcessorRMI) registry.lookup(upstreamId);
            upstream.send(id+"","ntid");
            System.out.println("receive ntid from "+upstreamId);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }

    public void infoChange(String infoType, int info){
        switch (infoType){
            case "tid":
                this.tid = info;
                break;
            case "ntid":
                this.ntid = info;
                break;
            case "nntid":
                this.nntid = info;
                break;
        }
    }

    public void stateChange(){
        if (ntid >= tid && ntid >=nntid) {
            tid = ntid;
            state = true;
        }
        else {
            state = false;
        }
    }

    public boolean getState(){
        return state;
    }

    public int getDownstreamId(){
        return downstreamId;
    }

    public int getUpstreamId(){
        return upstreamId;
    }

    public int getId(){
        return id;
    }


    public void peterson() throws RemoteException, NotBoundException {
        send(downstreamId+"", "ntid");
        receive(upstreamId+"");
        send(downstreamId+"", "nntid");
        ProcessorRMI downstream = (ProcessorRMI) registry.lookup(downstreamId+"");
        downstream.stateChange();
    }

    public void updateNeighbourInfo() throws NotBoundException, RemoteException {
        ProcessorRMI downstream = (ProcessorRMI) registry.lookup(downstreamId + "");
        ProcessorRMI upstream = (ProcessorRMI) registry.lookup(upstreamId + "");
        while (!downstream.getState()) {
            downstream = (ProcessorRMI) registry.lookup(downstream.getDownstreamId()+"");
        }
        downstreamId = downstream.getId();
        while (!upstream.getState()) {
            upstream = (ProcessorRMI) registry.lookup(upstream.getUpstreamId()+ "");
        }
        upstreamId = upstream.getId();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            peterson();
            Thread.sleep(1000);
            updateNeighbourInfo();
            while (state) {
                peterson();
                updateNeighbourInfo();
                if (upstreamId == id) {
                    System.out.println(id+" is elected");
                    System.exit(0);
                }
            }

        } catch (InterruptedException | RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
