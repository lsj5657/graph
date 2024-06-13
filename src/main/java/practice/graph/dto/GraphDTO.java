package practice.graph.dto;


import lombok.Getter;
import lombok.Setter;
import practice.graph.entity.Graph;

@Getter
@Setter
public class GraphDTO {
    int vertexCount;
    int edgeCount;
    String edgeInfo;

    public GraphDTO(Graph graph) {
        this.vertexCount = graph.getVertexCount();
        this.edgeCount = graph.getEdgeCount();
        this.edgeInfo = graph.getEdgeInfo();
    }

    public GraphDTO() {
    }
}
