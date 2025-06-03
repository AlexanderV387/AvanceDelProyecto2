import javax.swing.*;
import java.util.*;

public class VentanaPrincipal {
    public JPanel panelPrincipal;
    public JTabbedPane tabbedPane;

    // Componentes para "Gestión de pedidos"
    public JLabel lblCliente;
    public JTextField txtCliente;
    public JLabel lblCedula;
    public JTextField txtCedula;
    public JLabel lblDistancia;
    public JTextField txtDistancia;
    public JButton btnAgregarPedido;
    public JButton btnAtenderPedido;
    public JButton btnSimularRuta;
    public JButton btnVerRutas;
    public JTextArea txtAreaPedidos;

    // Componentes para "Servicio técnico"
    public JLabel lblProblema;
    public JTextField txtProblema;
    public JButton btnAgregarProblema;
    public JButton btnAtenderProblema;
    public JTextArea txtAreaSoporte;
    public JComboBox<Pedido> cmbProblemasClientes;

    // Estructuras internas
    private Queue<Pedido> colaPedidos = new LinkedList<>();
    private Stack<String> pilaSoporte = new Stack<>();
    private Grafo grafo = new Grafo();

    public VentanaPrincipal() {
        // Precarga de rutas
        grafo.agregarArista("Quito", "Coca", 50);
        grafo.agregarArista("Quito", "Guayaquil", 300);
        grafo.agregarArista("Guayaquil", "Coca", 80);
        grafo.agregarArista("Coca", "Tena", 70);
        grafo.agregarArista("Tena", "Quito", 120);

        // Inicializar combo box con modelo vacío
        cmbProblemasClientes.setModel(new DefaultComboBoxModel<>());

        // Acción para "Agregar pedido"
        btnAgregarPedido.addActionListener(e -> {
            String nombre = txtCliente.getText().trim();
            String cedula = txtCedula.getText().trim();
            String distancia = txtDistancia.getText().trim();

            if (nombre.isEmpty() || cedula.isEmpty() || distancia.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Complete todos los campos (Cliente, Cédula, Distancia).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Pedido nuevoPedido = new Pedido(nombre, cedula, distancia);
            colaPedidos.offer(nuevoPedido);

            // Limpiar campos
            txtCliente.setText("");
            txtCedula.setText("");
            txtDistancia.setText("");

            // Actualizar la lista de pedidos y el combo box
            actualizarAreaPedidos();
            actualizarComboClientes();
        });

        // Acción para "Atender pedido"
        btnAtenderPedido.addActionListener(e -> {
            if (colaPedidos.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "No hay pedidos pendientes.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Pedido atendido = colaPedidos.poll();
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Atendiendo pedido de: " + atendido.getNombre(), "Pedido Atendido", JOptionPane.INFORMATION_MESSAGE);

            // Actualizar lista de pedidos y combo box
            actualizarAreaPedidos();
            actualizarComboClientes();
        });

        // Acción para "Simular ruta"
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

        // Acción para "Rutas" (solo muestra origen → destino)
        btnVerRutas.addActionListener(e -> {
            String rutas = grafo.listarSoloRutas();
            JTextArea area = new JTextArea(rutas);
            area.setEditable(false);
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new java.awt.Dimension(400, 200));
            JOptionPane.showMessageDialog(panelPrincipal, scroll,
                    "Rutas Disponibles", JOptionPane.INFORMATION_MESSAGE);
        });

        // Acción para Agregar problema
        btnAgregarProblema.addActionListener(e -> {
            Pedido seleccionado = (Pedido) cmbProblemasClientes.getSelectedItem();
            String problema = txtProblema.getText().trim();

            if (seleccionado == null) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Seleccione un cliente del combo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (problema.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Ingrese descripción del problema.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Formar la cadena con cliente completo mas problema
            String registroSoporte = seleccionado.toString() + " | Problema: " + problema;
            pilaSoporte.push(registroSoporte);

            // Limpiar campo problema y actualizar lista de soporte
            txtProblema.setText("");
            actualizarAreaSoporte();
        });

        // Acción para Atender problema
        btnAtenderProblema.addActionListener(e -> {
            if (pilaSoporte.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "No hay problemas pendientes.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String atendido = pilaSoporte.pop();
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Atendiendo → " + atendido, "Soporte Atendido", JOptionPane.INFORMATION_MESSAGE);
            actualizarAreaSoporte();
        });
    }

    private void actualizarAreaPedidos() {
        StringBuilder sb = new StringBuilder();
        for (Pedido p : colaPedidos) {
            sb.append(p.toString()).append("\n");
        }
        txtAreaPedidos.setText(sb.toString());
    }

    private void actualizarComboClientes() {
        DefaultComboBoxModel<Pedido> model = new DefaultComboBoxModel<>();
        for (Pedido p : colaPedidos) {
            model.addElement(p);
        }
        cmbProblemasClientes.setModel(model);
    }

    private void actualizarAreaSoporte() {
        StringBuilder sb = new StringBuilder();
        for (String s : pilaSoporte) {
            sb.append(s).append("\n");
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