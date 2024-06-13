package practice.graph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import practice.graph.entity.Graph;

@Repository
public interface GraphRepository extends JpaRepository<Graph,Long> {
}
