package smartparcel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("SmartParcel - Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel welcomeLabel = new JLabel("Welcome to SmartParcel");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JButton viewStatusButton = new JButton("View Parcel Delivery Status");
        viewStatusButton.setPreferredSize(new Dimension(250, 40));

        // Pretty UI colors
        getContentPane().setBackground(new Color(224, 255, 255));
        viewStatusButton.setBackground(new Color(100, 149, 237));
        viewStatusButton.setForeground(Color.WHITE);

        add(welcomeLabel);
        add(viewStatusButton);

        viewStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ParcelStatusViewer statusViewer = new ParcelStatusViewer(MainMenu.this);
                statusViewer.setVisible(true);
                setVisible(false); // Hide MainMenu while ParcelStatusViewer is open
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
        });
    }
}
