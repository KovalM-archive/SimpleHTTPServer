import server.ClientWaiter;

import javax.swing.SwingUtilities;

public class Runner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ClientWaiter();
        });
    }
}
