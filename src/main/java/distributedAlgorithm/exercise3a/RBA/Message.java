package distributedAlgorithm.exercise3a.RBA;

import java.io.Serializable;

public class Message implements Serializable {

    Stage stage;
    int round;
    int v;
    String id;

    public Message(Stage stage, int round, int v, String id) {
        this.stage = stage;
        this.round = round;
        this.v = v;
        this.id =id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "stage=" + stage +
                ", round=" + round +
                ", v=" + v +
                ", id=" + id +
                '}';
    }
}
