import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SoporteManager {
    private Stack<String> problemas = new Stack<>();

    public void agregarProblema(String descripcion) {
        problemas.push(descripcion);
    }

    public String atenderProblema() {
        return problemas.isEmpty() ? null : problemas.pop();
    }

    public List<String> obtenerLista() {
        return new ArrayList<>(problemas);
    }

    public boolean estaVacio() {
        return problemas.isEmpty();
    }
}