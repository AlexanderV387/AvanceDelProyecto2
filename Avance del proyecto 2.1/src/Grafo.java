import java.util.*;

public class Grafo {
    private Map<String, List<Arista>> adj = new HashMap<>();

    public void agregarVertice(String ciudad) {
        adj.putIfAbsent(ciudad, new ArrayList<>());
    }

    public void agregarArista(String origen, String destino, int peso) {
        agregarVertice(origen);
        agregarVertice(destino);
        adj.get(origen).add(new Arista(destino, peso));
    }

    public Map<String, Integer> dijkstra(String inicio) {
        Map<String, Integer> dist = new HashMap<>();
        for (String vertice : adj.keySet()) {
            dist.put(vertice, Integer.MAX_VALUE);
        }
        if (!dist.containsKey(inicio)) return dist;
        dist.put(inicio, 0);
        PriorityQueue<VerticeDist> pq = new PriorityQueue<>(Comparator.comparingInt(v -> v.distancia));
        pq.offer(new VerticeDist(inicio, 0));
        while (!pq.isEmpty()) {
            VerticeDist actual = pq.poll();
            if (actual.distancia > dist.get(actual.nombre)) continue;
            for (Arista a : adj.get(actual.nombre)) {
                int nuevaDist = actual.distancia + a.peso;
                if (nuevaDist < dist.get(a.destino)) {
                    dist.put(a.destino, nuevaDist);
                    pq.offer(new VerticeDist(a.destino, nuevaDist));
                }
            }
        }
        return dist;
    }

    public String listarRutas() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<Arista>> entry : adj.entrySet()) {
            String origen = entry.getKey();
            for (Arista a : entry.getValue()) {
                sb.append(origen)
                        .append(" → ")
                        .append(a.destino)
                        .append("  (Distancia: ")
                        .append(a.peso)
                        .append(")\n");
            }
        }
        return sb.toString();
    }

    public static class Arista {
        String destino;
        int peso;

        public Arista(String destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    private static class VerticeDist {
        String nombre;
        int distancia;

        public VerticeDist(String nombre, int distancia) {
            this.nombre = nombre;
            this.distancia = distancia;
        }
    }
}