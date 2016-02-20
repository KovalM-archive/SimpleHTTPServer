package server;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;

public class ServerWorkPanel extends JPanel{
    private JTextArea textArea;

    public ServerWorkPanel(){
        JFrame jfMainWin = new JFrame("Table student server");
        jfMainWin.setLayout(new BorderLayout());

        jfMainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfMainWin.setSize(1000, 500);
        jfMainWin.setLocationRelativeTo(null);

        textArea = new JTextArea();

        this.setLayout(new BorderLayout());
        this.add(textArea, BorderLayout.CENTER);
        this.setSize(jfMainWin.getWidth(), jfMainWin.getHeight());
        jfMainWin.add(new JScrollPane(this), BorderLayout.CENTER);
        jfMainWin.setVisible(true);
    }

    public JTextArea getTextArea() {
        return textArea;
    }

}
