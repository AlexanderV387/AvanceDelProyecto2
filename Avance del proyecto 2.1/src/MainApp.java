import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("LogiMusic - TrackNote");
        frame.setContentPane(new VentanaPrincipal().panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}