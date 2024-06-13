package practice.graph.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.graph.dto.GraphDTO;
import practice.graph.entity.Graph;
import practice.graph.repository.GraphRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GraphService {
    private final GraphRepository graphRepository;


    public Graph findById(Long id){
        Optional<Graph> graph = graphRepository.findById(id);
        return graph.get();
    }

    public List<Graph> findAll(){
        return graphRepository.findAll();
    }
    public void save(Graph graph){
        graphRepository.save(graph);
    }

    public void delete(Long id){
        graphRepository.deleteById(id);
    }

    public void update(Long id, GraphDTO updateParam){
        Graph graph = findById(id);
        graph.setVertexCount(updateParam.getVertexCount());
        graph.setEdgeCount(updateParam.getEdgeCount());
        graph.setEdgeInfo(updateParam.getEdgeInfo());
        graphRepository.save(graph);
    }

}
