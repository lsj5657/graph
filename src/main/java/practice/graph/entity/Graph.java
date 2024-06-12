package practice.graph.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Graph {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "graph_id")
    private Long id;

    private int vertexCount;
    private int edgeCount;

    @OneToMany(mappedBy = "graph")
    private List<Edge> edges = new ArrayList<>();

}