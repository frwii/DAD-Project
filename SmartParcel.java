package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SmartParcel {

    // --- Data Models ---
    static class Parcel {
        String parcelId;
        String senderName;
        String receiverName;
        String pickupPoint;
        String dropPoint;
        String status;

        Parcel(String parcelId, String senderName, String receiverName,
               String pickupPoint, String dropPoint, String status) {
            this.parcelId = parcelId;
            this.senderName = senderName;
            this.receiverName = receiverName;
            this.pickupPoint = pickupPoint;
            this.dropPoint = dropPoint;
            this.status = status;
        }

        public String toString() {
            return parcelId + " - " + receiverName + " (" + status + ")";
        }

        public String getParcelId() { return parcelId; }
        public String getSenderName() { return senderName; }
        public String getReceiverName() { return receiverName; }
        public String getPickupPoint() { return pickupPoint; }
        public String getDropPoint() { return dropPoint; }
        public String getStatus() { return status; }
    }

    // --- Static Storage ---
    static java.util.List<Parcel> assignedParcels = new ArrayList<>();
    static Map<String, String> userDatabase = new HashMap<>();
    static int parcelCounter = 1;

    // --- App Launcher ---
    public static void main(String[] args) {
        assignedParcels.add(new Parcel("P001", "Ali", "Siti", "HQ", "Shah Alam", "Pending"));
        assignedParcels.add(new Parcel("P002", "Ahmad", "John", "Johor", "Penang", "Pending"));
        assignedParcels.add(new Parcel("P003", "Zara", "Lim", "Kuantan", "Ipoh", "Delivered"));

        userDatabase.put("courier1", "pass123");
        userDatabase.put("courier2", "pass456");

        SwingUtilities.invokeLater(() -> new MainMenu());
    }

    // --- Main Menu ---
    static class MainMenu extends JFrame {
        public MainMenu() {
            setTitle("SmartParcel - Main Menu");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new FlowLayout());

            JLabel welcomeLabel = new JLabel("Welcome to SmartParcel");
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));

            JButton createBtn = new JButton("Create Delivery Request");
            JButton courierBtn = new JButton("Courier Login");
            JButton statusBtn = new JButton("View Parcel Delivery Status");

            styleButton(createBtn, new Color(60, 179, 113));
            styleButton(courierBtn, new Color(70, 130, 180));
            styleButton(statusBtn, new Color(100, 149, 237));

            getContentPane().setBackground(new Color(224, 255, 255));
            add(welcomeLabel);
            add(createBtn);
            add(courierBtn);
            add(statusBtn);

            createBtn.addActionListener(e -> new ParcelApp());
            courierBtn.addActionListener(e -> new LoginFrame());
            statusBtn.addActionListener(e -> new ParcelStatusViewer(this));

            setVisible(true);
        }

        private void styleButton(JButton btn, Color bg) {
            btn.setPreferredSize(new Dimension(250, 40));
            btn.setBackground(bg);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
        }
    }

    // --- Delivery Request Form ---
    static class ParcelApp extends JFrame {
        private JTextField senderNameField, senderPhoneField, senderAddressField;
        private JTextField receiverNameField, receiverPhoneField, receiverAddressField;
        private JTextField pickupPointField, dropPointField;

        public ParcelApp() {
            setTitle("SmartParcel - Create Delivery Request");
            setSize(600, 500); // Fixed size
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
            panel.setLayout(new GridBagLayout());
            panel.setBackground(Color.WHITE);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            int y = 0;
            addLabelAndField(panel, gbc, "Sender Name:", senderNameField = new JTextField(20), y++);
            addLabelAndField(panel, gbc, "Sender Phone:", senderPhoneField = new JTextField(20), y++);
            addLabelAndField(panel, gbc, "Sender Address:", senderAddressField = new JTextField(20), y++);
            addLabelAndField(panel, gbc, "Receiver Name:", receiverNameField = new JTextField(20), y++);
            addLabelAndField(panel, gbc, "Receiver Phone:", receiverPhoneField = new JTextField(20), y++);
            addLabelAndField(panel, gbc, "Receiver Address:", receiverAddressField = new JTextField(20), y++);
            addLabelAndField(panel, gbc, "Pickup Point:", pickupPointField = new JTextField(20), y++);
            addLabelAndField(panel, gbc, "Drop-off Point:", dropPointField = new JTextField(20), y++);

            JButton submitBtn = new JButton("Create Delivery Request");
            submitBtn.setBackground(new Color(34, 139, 34));
            submitBtn.setForeground(Color.WHITE);
            submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
            submitBtn.setPreferredSize(new Dimension(250, 40));
            submitBtn.addActionListener(this::handleSubmit);

            gbc.gridx = 1;
            gbc.gridy = y;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(submitBtn, gbc);

            add(panel);
            setVisible(true);
        }

        private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int y) {
            gbc.gridx = 0;
            gbc.gridy = y;
            gbc.gridwidth = 1;
            panel.add(new JLabel(label), gbc);

            gbc.gridx = 1;
            gbc.gridy = y;
            gbc.gridwidth = 2;
            panel.add(field, gbc);
        }

        private void handleSubmit(ActionEvent e) {
            if (isEmpty(senderNameField) || isEmpty(senderPhoneField) || isEmpty(senderAddressField)
                    || isEmpty(receiverNameField) || isEmpty(receiverPhoneField) || isEmpty(receiverAddressField)
                    || isEmpty(pickupPointField) || isEmpty(dropPointField)) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Missing Info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Delivery request created successfully!");
            dispose();
        }

        private boolean isEmpty(JTextField field) {
            return field.getText().trim().isEmpty();
        }
    }

    // --- Login Frame ---
    static class LoginFrame extends JFrame {
        public LoginFrame() {
            setTitle("Courier Login");
            setSize(400, 250);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
            panel.setBackground(Color.WHITE);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField usernameField = new JTextField(20);
            JPasswordField passwordField = new JPasswordField(20);
            JButton loginBtn = new JButton("Login");
            loginBtn.setBackground(new Color(70, 130, 180));
            loginBtn.setForeground(Color.WHITE);
            loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
            loginBtn.setPreferredSize(new Dimension(100, 35));

            int y = 0;
            gbc.gridx = 0; gbc.gridy = y;
            panel.add(new JLabel("Username:"), gbc);
            gbc.gridx = 1;
            panel.add(usernameField, gbc);

            gbc.gridx = 0; gbc.gridy = ++y;
            panel.add(new JLabel("Password:"), gbc);
            gbc.gridx = 1;
            panel.add(passwordField, gbc);

            gbc.gridx = 1; gbc.gridy = ++y;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(loginBtn, gbc);

            add(panel);

            loginBtn.addActionListener(e -> {
                String user = usernameField.getText().trim();
                String pass = new String(passwordField.getPassword());

                if (SmartParcel.userDatabase.containsKey(user) &&
                        SmartParcel.userDatabase.get(user).equals(pass)) {
                    dispose();
                    new CourierDashboard(user);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials. Try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            });

            setVisible(true);
        }
    }

    // --- Courier Dashboard ---
    static class CourierDashboard extends JFrame {
        public CourierDashboard(String courierName) {
            setTitle("Courier Dashboard - " + courierName);
            setSize(400, 300);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
            panel.setBackground(Color.WHITE);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(15, 10, 15, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel welcomeLabel = new JLabel("Welcome, " + courierName + "!");
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
            welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JButton viewBtn = new JButton("View Assigned Parcels");
            JButton updateBtn = new JButton("Update Parcel Status");
            JButton logoutBtn = new JButton("Logout");

            styleButton(viewBtn);
            styleButton(updateBtn);
            styleButton(logoutBtn);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(welcomeLabel, gbc);

            gbc.gridy = 1;
            panel.add(viewBtn, gbc);

            gbc.gridy = 2;
            panel.add(updateBtn, gbc);

            gbc.gridy = 3;
            panel.add(logoutBtn, gbc);

            viewBtn.addActionListener(e -> new ViewParcelWindow());
            updateBtn.addActionListener(e -> new UpdateParcelWindow());
            logoutBtn.addActionListener(e -> {
                dispose();
                new LoginFrame();
            });

            add(panel);
            setVisible(true);
        }

        private void styleButton(JButton button) {
            button.setPreferredSize(new Dimension(220, 40));
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setBackground(new Color(65, 105, 225));
            button.setForeground(Color.WHITE);
        }
    }

    // --- View Parcels ---
    static class ViewParcelWindow extends JFrame {
        ViewParcelWindow() {
            setTitle("Assigned Parcels");
            setSize(400, 300);

            DefaultListModel<String> model = new DefaultListModel<>();
            for (Parcel p : assignedParcels) {
                model.addElement(p.toString());
            }

            JList<String> list = new JList<>(model);
            JButton back = new JButton("Back");
            back.addActionListener(e -> dispose());

            add(new JScrollPane(list), BorderLayout.CENTER);
            add(back, BorderLayout.SOUTH);
            setVisible(true);
        }
    }

    // --- Update Parcel Status ---
    static class UpdateParcelWindow extends JFrame {
        UpdateParcelWindow() {
            setTitle("Update Parcel Status");
            setSize(450, 300);

            DefaultListModel<Parcel> model = new DefaultListModel<>();
            for (Parcel p : assignedParcels) {
                if ("Pending".equalsIgnoreCase(p.status)) {
                    model.addElement(p);
                }
            }

            JList<Parcel> list = new JList<>(model);
            JButton update = new JButton("Mark as Delivered");
            JButton back = new JButton("Back");

            update.addActionListener(e -> {
                Parcel selected = list.getSelectedValue();
                if (selected != null) {
                    selected.status = "Delivered";
                    model.removeElement(selected);
                    JOptionPane.showMessageDialog(this, "Marked as delivered!");
                }
            });

            back.addActionListener(e -> dispose());

            JPanel btns = new JPanel();
            btns.add(update); btns.add(back);

            add(new JScrollPane(list), BorderLayout.CENTER);
            add(btns, BorderLayout.SOUTH);
            setVisible(true);
        }
    }

    // --- Parcel Status Viewer ---
    static class ParcelStatusViewer extends JFrame {
        ParcelStatusViewer(JFrame mainMenu) {
            setTitle("Parcel Status Viewer");
            setSize(500, 300);

            JTextField idField = new JTextField(20);
            JTextArea resultArea = new JTextArea();
            resultArea.setEditable(false);
            JButton search = new JButton("Search");
            JButton back = new JButton("Back");

            JPanel input = new JPanel();
            input.add(new JLabel("Parcel ID:"));
            input.add(idField);
            input.add(search);

            search.addActionListener(e -> {
                String id = idField.getText().trim();
                Parcel found = null;
                for (Parcel p : assignedParcels) {
                    if (p.getParcelId().equalsIgnoreCase(id)) {
                        found = p;
                        break;
                    }
                }
                if (found != null) {
                    resultArea.setText("Parcel ID: " + found.getParcelId() + "\nSender: " + found.getSenderName() +
                            "\nReceiver: " + found.getReceiverName() + "\nPickup: " + found.getPickupPoint() +
                            "\nDrop-off: " + found.getDropPoint() + "\nStatus: " + found.getStatus());
                } else {
                    resultArea.setText("Parcel not found.");
                }
            });

            back.addActionListener(e -> {
                mainMenu.setVisible(true);
                dispose();
            });

            add(input, BorderLayout.NORTH);
            add(new JScrollPane(resultArea), BorderLayout.CENTER);
            add(back, BorderLayout.SOUTH);
            setVisible(true);
        }
    }
}
