import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUI implements ActionListener {


    private JFrame mainFrame;
    private JPanel searchPanel;
    private JButton searchButton;
    private JTextField searchField;

    private JPanel similarPanel;
    private JLabel similar1;
    private JLabel similar2;

    SimFinder sf;

    public GUI() throws IOException {
        sf = new SimFinder();
        prepareGUI();
    }

    private void prepareGUI() {

        mainFrame = new JFrame("Project 1");
        mainFrame.setSize(400,150);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        searchPanel = new JPanel();

        searchField = new JTextField("Enter Buisness", 20);

        searchButton = new JButton("Search");
        searchButton.addActionListener(this);

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        mainFrame.add(searchPanel, BorderLayout.NORTH);
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {

        GUI gui = new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String text = searchField.getText();
            String bizId = SimFinder.lookUpBizId(text);

            if(bizId.equals("BIZDNE")){
                similar1 = new JLabel("Business does not exist");
                similarPanel = new JPanel();
                similar1.setOpaque(true);

                similar2 = new JLabel("Business 2");
                similar2.setOpaque(true);

                similarPanel.add(similar1);
                mainFrame.add(similarPanel, BorderLayout.CENTER);

                mainFrame.setVisible(true);
            } else {
                String[] results = SimFinder.getResults(bizId);
                similar1 = new JLabel(results[0] + "  |  ");
                similarPanel = new JPanel();
                similar1.setOpaque(true);
                similar2 = new JLabel(results[1]);
                similar2.setOpaque(true);
                similarPanel.add(similar1);
                similarPanel.add(similar2);
                mainFrame.add(similarPanel, BorderLayout.CENTER);
                mainFrame.setVisible(true);
            }

            
        }
    }
}
