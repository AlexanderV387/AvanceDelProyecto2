import javax.swing.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class VentanaPrincipal {
    public JPanel panelPrincipal;
    public JTabbedPane tabbedPane;

    public JLabel lblCliente;
    public JTextField txtCliente;
    public JButton btnAgregarPedido;
    public JButton btnAtenderPedido;
    public JButton btnSimularRuta;
    public JButton btnVerRutas;
    public JTextArea txtAreaPedidos;

    public JLabel lblProblema;
    public JTextField txtProblema;
    public JButton btnAgregarProblema;
    public JButton btnAtenderProblema;
    public JTextArea txtAreaSoporte;

    private Queue<String> colaPedidos = new LinkedList<>();
    private Stack<String> pilaSoporte = new Stack<>();
    private Grafo grafo = new Grafo();

    public VentanaPrincipal() {
        // Precarga de rutas de ejemplo
        grafo.agregarArista("Quito", "Coca", 50);
        grafo.agregarArista("Quito", "Guayaquil", 300);
        grafo.agregarArista("Guayaquil", "Coca", 80);
        grafo.agregarArista("Coca", "Tena", 70);
        grafo.agregarArista("Tena", "Quito", 120);

        btnAgregarPedido.addActionListener(e -> {
            String cliente = txtCliente.getText().trim();
            if (cliente.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Ingrese nombre de cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            colaPedidos.offer(cliente);
            txtCliente.setText("");
            actualizarAreaPedidos();
        });

        btnAtenderPedido.addActionListener(e -> {
            if (colaPedidos.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "No hay pedidos pendientes.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String atendido = colaPedidos.poll();
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Atendiendo pedido de: " + atendido, "Pedido Atendido", JOptionPane.INFORMATION_MESSAGE);
            actualizarAreaPedidos();
        });

        btnSimularRuta.addActionListener(e -> {
            String origen = JOptionPane.showInputDialog(panelPrincipal, "Ingrese ciudad de origen:");
            if (origen == null || origen.trim().isEmpty()) return;
            String destino = JOptionPane.showInputDialog(panelPrincipal, "Ingrese ciudad de destino:");
            if (destino == null || destino.trim().isEmpty()) return;

            Map<String, Integer> distancias = grafo.dijkstra(origen.trim());
            Integer distanciaMin = distancias.get(destino.trim());
            if (distanciaMin == null || distanciaMin == Integer.MAX_VALUE) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "No existe ruta desde " + origen + " hasta " + destino,
                        "Simular Ruta", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Distancia mínima desde \"" + origen + "\" hasta \"" + destino + "\": " + distanciaMin,
                        "Simular Ruta", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnVerRutas.addActionListener(e -> {
            String todas = grafo.listarRutas();
            JTextArea area = new JTextArea(todas);
            area.setEditable(false);
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new java.awt.Dimension(400, 200));
            JOptionPane.showMessageDialog(panelPrincipal, scroll,
                    "Rutas Disponibles", JOptionPane.INFORMATION_MESSAGE);
        });

        btnAgregarProblema.addActionListener(e -> {
            String problema = txtProblema.getText().trim();
            if (problema.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Ingrese descripción del problema.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            pilaSoporte.push(problema);
            txtProblema.setText("");
            actualizarAreaSoporte();
        });

        btnAtenderProblema.addActionListener(e -> {
            if (pilaSoporte.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "No hay problemas pendientes.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String atendido = pilaSoporte.pop();
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Atendiendo problema: " + atendido, "Soporte Atendido", JOptionPane.INFORMATION_MESSAGE);
            actualizarAreaSoporte();
        });
    }

    private void actualizarAreaPedidos() {
        StringBuilder sb = new StringBuilder();
        for (String cliente : colaPedidos) {
            sb.append(cliente).append("\n");
        }
        txtAreaPedidos.setText(sb.toString());
    }

    private void actualizarAreaSoporte() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pilaSoporte.size(); i++) {
            sb.append(pilaSoporte.get(i)).append("\n");
        }
        txtAreaSoporte.setText(sb.toString());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LogiMusic - TrackNote");
        frame.setContentPane(new VentanaPrincipal().panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}