package practice.graph.utils;

public class UnionFind {
    private int[] parent;

    public UnionFind(int n) {
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    public int find(int u) {
        if (parent[u] != u) {
            parent[u] = find(parent[u]);
        }
        return parent[u];
    }

    public void union(int u, int v) {
        u = find(u);
        v = find(v);

        if(u==v) return;

        if (u > v) {
            int temp = u;
            u = v;
            v = temp;
        }

        parent[v]=u;
    }

}
