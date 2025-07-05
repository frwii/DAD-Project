package smartparcel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParcelStatusViewer extends JFrame {
    private JTextField parcelIdField;
    private JTextArea resultArea;
    private JButton backButton;

    private JFrame mainMenuFrame; // to hold the Main Menu reference

    public ParcelStatusViewer(JFrame mainMenuFrame) {
        this.mainMenuFrame = mainMenuFrame;

        setTitle("SmartParcel - Parcel Status Viewer");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel parcelIdLabel = new JLabel("Enter Parcel ID: ");
        parcelIdField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        inputPanel.add(parcelIdLabel);
        inputPanel.add(parcelIdField);
        inputPanel.add(searchButton);

        // Result Area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Back Button
        backButton = new JButton("Back to Main Menu");

        // Pretty UI tweaks
        inputPanel.setBackground(new Color(173, 216, 230));
        resultArea.setBackground(new Color(240, 248, 255));
        searchButton.setBackground(new Color(100, 149, 237));
        searchButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(220, 20, 60));
        backButton.setForeground(Color.WHITE);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        // Search button action
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String parcelId = parcelIdField.getText().trim();
                if (parcelId.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a Parcel ID.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    Parcel parcel = ApiClient.getParcelStatus(parcelId);
                    if (parcel != null) {
                        displayParcelInfo(parcel);
                    } else {
                        resultArea.setText("Parcel not found.\nPlease check your Parcel ID.");
                    }
                }
            }
        });

        // Back button action
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenuFrame.setVisible(true);  // Show Main Menu again
                dispose(); // Close this window
            }
        });
    }

    private void displayParcelInfo(Parcel parcel) {
        StringBuilder sb = new StringBuilder();
        sb.append("Parcel ID: ").append(parcel.getParcelId()).append("\n");
        sb.append("Sender: ").append(parcel.getSenderName()).append("\n");
        sb.append("Receiver: ").append(parcel.getReceiverName()).append("\n");
        sb.append("Pickup Point: ").append(parcel.getPickupPoint()).append("\n");
        sb.append("Drop Point: ").append(parcel.getDropPoint()).append("\n");
        sb.append("Current Status: ").append(parcel.getStatus()).append("\n");

        resultArea.setText(sb.toString());
    }
}
