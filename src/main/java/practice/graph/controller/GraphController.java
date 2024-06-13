package practice.graph.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import practice.graph.Service.GraphService;
import practice.graph.domain.Edge;
import practice.graph.dto.GraphDTO;
import practice.graph.entity.Graph;
import practice.graph.utils.UnionFind;

import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GraphController {
    private final GraphService graphService;

    @GetMapping("graphs")
    public String list(Model model){
        List<Graph> graphs = graphService.findAll();
        model.addAttribute("graphs",graphs);

        for (Graph graph : graphs) {
            log.info("graph id = {}", graph.getId());
        }

        return "graphs/graphList";
    }

    @GetMapping("graphs/new")
    public String createGraph(Model model){
        GraphDTO graphDTO = new GraphDTO();
        model.addAttribute("graph", graphDTO);
        return "graphs/createGraphForm";
    }

    @PostMapping("graphs/new")
    public String create(@ModelAttribute("graph") @Valid GraphDTO graphDTO, BindingResult bindingResult){

        System.out.println(graphDTO);
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.toString()));
            return "graphs/createGraphForm";
        }


        Graph graph = new Graph();
        graph.setVertexCount(graphDTO.getVertexCount());
        graph.setEdgeCount(graphDTO.getEdgeCount());
        graph.setEdgeInfo(graphDTO.getEdgeInfo());


        System.out.println(graph.getEdgeInfo());
        graphService.save(graph);


        return "redirect:/graphs";
    }

    @GetMapping("graphs/{id}/edit")
    public String editForm(@PathVariable Long id,Model model){
        log.info("get edit");
        Graph graph = graphService.findById(id);
        GraphDTO graphDTO = new GraphDTO(graph);
        model.addAttribute("id", id);
        model.addAttribute("graph", graphDTO);

        return "graphs/editGraphForm";
    }

    @PostMapping("graphs/{id}/edit")
    public String edit(@PathVariable Long id,@ModelAttribute("graph") GraphDTO graphDTO){
        log.info("post edit");
        System.out.println("graphDTO = " + graphDTO.getVertexCount());
        System.out.println("graphDTO = " + graphDTO.getEdgeCount());
        System.out.println("graphDTO = " + graphDTO.getEdgeInfo());
        graphService.update(id,graphDTO);

        return "redirect:/graphs/{id}";
    }

    @PostMapping("graphs/{id}/delete")
    public String delete(@PathVariable Long id){

        log.info("delete");
        graphService.delete(id);

        return "redirect:/graphs";
    }


    @GetMapping("graphs/{id}")
    public String graph(@PathVariable Long id,Model model){
        Graph graph = graphService.findById(id);
        int N=graph.getVertexCount();
        int M=graph.getEdgeCount();
        List<Edge> edges = parseEdges(graph.getEdgeInfo());
        for (Edge edge : edges) {
            System.out.println("edge = " + edge);
        }

        model.addAttribute("graphId",id);
        model.addAttribute("vertexCount", N);
        model.addAttribute("edgeCount", M);
        model.addAttribute("edges", edges);


        return "graphs/graphView";
    }

    @GetMapping("graphs/{id}/mst")
    public String mst(@PathVariable Long id,Model model){
        Graph graph = graphService.findById(id);
        int N=graph.getVertexCount();
        int M=graph.getEdgeCount();
        List<Edge> edges = parseEdges(graph.getEdgeInfo());

        List<Edge>treeEdges = new ArrayList<>();
        for (Edge edge : edges) {
            System.out.println("edge = " + edge);
        }

        Collections.sort(edges, Comparator.comparingInt(Edge::getWeight));
        UnionFind uf = new UnionFind(N);

        int mstWeight = 0;
        for (Edge edge : edges) {
            if (uf.find(edge.getFrom() - 1) != uf.find(edge.getTo() - 1)) {
                uf.union(edge.getFrom() - 1, edge.getTo() - 1);
                treeEdges.add(edge);
                mstWeight += edge.getWeight();
            }
        }

        model.addAttribute("graphId",id);
        model.addAttribute("vertexCount", N);
        model.addAttribute("edgeCount", treeEdges.size());
        model.addAttribute("edges", treeEdges);
        model.addAttribute("mstWeight", mstWeight);


        return "graphs/mstView";
    }

    @GetMapping("graphs/{id}/dijkstra")
    public String dijkstra(@PathVariable Long id, Model model, @RequestParam int startVertex){
        Graph graph = graphService.findById(id);
        int N=graph.getVertexCount();
        int M=graph.getEdgeCount();
        List<Edge> edges = parseEdges(graph.getEdgeInfo());

        for (Edge edge : edges) {
            System.out.println("edge = " + edge);
        }

        List<List<int[]>> adjacencyList = new ArrayList<>();
        for (int i = 0; i <= N; i++) {
            adjacencyList.add(new ArrayList<>());
        }
        for (Edge edge : edges) {
            adjacencyList.get(edge.getFrom()).add(new int[]{edge.getWeight(), edge.getTo()});
            adjacencyList.get(edge.getTo()).add(new int[]{edge.getWeight(), edge.getFrom()});
        }

        // Dijkstra algorithm variables
        int[] dist = new int[N+1];
        int[] predecessor = new int[N+1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(predecessor, -1);
        dist[startVertex] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(pair -> pair[0]));
        pq.add(new int[]{0, startVertex});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int currentDist = current[0];
            int currentVertex = current[1];


            if(dist[currentVertex]!=currentDist) continue;

            System.out.println("currentDist currentVertex" + currentDist + " " + currentVertex);

            for (int[] neighbor : adjacencyList.get(currentVertex)) {
                int weight = neighbor[0];
                int nextVertex = neighbor[1];
                System.out.println("weight nextVertex " + weight + " " + nextVertex);
                if (currentDist + weight < dist[nextVertex]) {
                    dist[nextVertex] = currentDist + weight;
                    predecessor[nextVertex] = currentVertex;
                    pq.add(new int[]{dist[nextVertex], nextVertex});
                }
            }
        }

        // Create treeEdges from the predecessor array
        List<Edge> treeEdges = new ArrayList<>();

        System.out.println("predecessor");
        for (int i = 1; i <= N; i++) {
            System.out.print(predecessor[i] + " ");
            if (predecessor[i] != -1) {
                treeEdges.add(new Edge(predecessor[i], i, dist[i] - dist[predecessor[i]]));
            }
        }

        for (int i=1; i<=N; i++){
            if(dist[i]==Integer.MAX_VALUE) dist[i]=-1;
        }

        model.addAttribute("graphId",id);
        model.addAttribute("vertexCount", N);
        model.addAttribute("edgeCount", treeEdges.size());
        model.addAttribute("edges", treeEdges);
        model.addAttribute("dist", dist);


        return "graphs/dijkstraView";
    }



    private static List<Edge> parseEdges(String edgeInfo) {
        List<Edge> edges = new ArrayList<>();
        String[] lines = edgeInfo.split("\n");

        for (String line : lines) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length >= 3) {
                int vertex1 = Integer.parseInt(parts[0]);
                int vertex2 = Integer.parseInt(parts[1]);
                int weight = Integer.parseInt(parts[2]);
                edges.add(new Edge(vertex1, vertex2, weight));
            }
        }

        return edges;
    }
}
