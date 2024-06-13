package practice.graph.dto;


import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import practice.graph.entity.Graph;
import practice.graph.validator.ValidEdgeInfo;


@ToString
@Getter
@Setter
@ValidEdgeInfo
public class GraphDTO {

    @Positive
    int vertexCount;
    @PositiveOrZero
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
