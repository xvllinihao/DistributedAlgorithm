package distributedAlgorithm.exercise1a.BirmanSchiperStephenson;

public class Message {

    Long id;
    String message;
    Vector vector;

    public Message(Long id, String message, Vector vector) {
        this.id = id;
        this.message = message;
        this.vector = vector;
    }
}
