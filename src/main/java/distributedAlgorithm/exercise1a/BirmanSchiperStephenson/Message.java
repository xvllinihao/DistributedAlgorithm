package distributedAlgorithm.exercise1a.BirmanSchiperStephenson;

import java.io.Serializable;
import java.util.Objects;

public class Message implements Serializable {

    int processorId;
    String msg;
    Vector vector;

    public Message(int processorId, String msg, Vector vector) {
        this.processorId = processorId;
        this.msg = msg;
        this.vector = vector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return processorId == message.processorId &&
                Objects.equals(msg, message.msg) &&
                Objects.equals(vector, message.vector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processorId, msg, vector);
    }

    @Override
    public String toString() {
        return "Message{" +
                "processorId=" + processorId +
                ", msg='" + msg + '\'' +
                ", vector=" + vector +
                '}';
    }
}
