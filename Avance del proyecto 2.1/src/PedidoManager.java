import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PedidoManager {
    private Queue<String> pedidos = new LinkedList<>();

    public void agregarPedido(String cliente) {
        pedidos.offer(cliente);
    }

    public String atenderPedido() {
        return pedidos.isEmpty() ? null : pedidos.poll();
    }

    public List<String> obtenerLista() {
        return new ArrayList<>(pedidos);
    }

    public boolean estaVacio() {
        return pedidos.isEmpty();
    }
}