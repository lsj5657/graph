package practice.graph.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter @Setter
public class Edge {
    private int from;
    private int to;
    private int weight;


    public Edge() {
    }

    public Edge(int from, int to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}
