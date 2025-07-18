package smartparcel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List; // NEW import for List

public class SmartParcel {

    // --- App Launcher ---
    public static void main(String[] args) {
        // --- REMOVED --- No more hardcoded data initialization.
        // The main method now simply starts the application.
        SwingUtilities.invokeLater(() -> new MainMenu());
    }

    // --- Main Menu ---
    static class MainMenu extends JFrame {
        public MainMenu() {
            setTitle("SmartParcel - Main Menu");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
            setLocationRelativeTo(null);

            JLabel welcomeLabel = new JLabel("Welcome to SmartParcel");
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));

            JButton createBtn = new JButton("Create Delivery Request");
            JButton courierBtn = new JButton("Courier Login");
            JButton statusBtn = new JButton("View Parcel Delivery Status");

            styleButton(createBtn, new Color(60, 179, 113));
            styleButton(courierBtn, new Color(70, 130, 180));
            styleButton(statusBtn, new Color(100, 149, 237));

            getContentPane().setBackground(new Color(240, 248, 255));
            add(welcomeLabel);
            add(createBtn);
            add(courierBtn);
            add(statusBtn);

            createBtn.addActionListener(e -> new ParcelApp());
            courierBtn.addActionListener(e -> new LoginFrame());
            // --- CHANGED --- We pass 'this' frame reference to the viewer
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

        public ParcelApp() { /* Constructor is mostly unchanged */ 
            setTitle("SmartParcel - Create Delivery Request");
            setSize(600, 500); 
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
            gbc.gridx = 1; gbc.gridy = y; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
            panel.add(submitBtn, gbc);
            add(panel);
            setVisible(true);
        }

        private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int y) { /* Unchanged */ 
             gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
             panel.add(new JLabel(label), gbc);
             gbc.gridx = 1; gbc.gridy = y; gbc.gridwidth = 2;
             panel.add(field, gbc);
        }

        // --- CHANGED --- handleSubmit now calls the API
        private void handleSubmit(ActionEvent e) {
            if (isEmpty(senderNameField) || isEmpty(senderPhoneField) || isEmpty(senderAddressField)
                    || isEmpty(receiverNameField) || isEmpty(receiverPhoneField) || isEmpty(receiverAddressField)
                    || isEmpty(pickupPointField) || isEmpty(dropPointField)) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Missing Info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Map<String, String> formData = new HashMap<>();
            formData.put("sender_name", senderNameField.getText());
            formData.put("sender_phone", senderPhoneField.getText());
            formData.put("sender_address", senderAddressField.getText());
            formData.put("receiver_name", receiverNameField.getText());
            formData.put("receiver_phone", receiverPhoneField.getText());
            formData.put("receiver_address", receiverAddressField.getText());
            formData.put("pickup_point", pickupPointField.getText());
            formData.put("drop_off_point", dropPointField.getText());

            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() {
                    return ApiClient.createParcel(formData);
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(ParcelApp.this, "Delivery request created successfully!");
                            dispose(); // Close the window on success
                        } else {
                            JOptionPane.showMessageDialog(ParcelApp.this, "Failed to create request.", "API Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }.execute();
        }

        private boolean isEmpty(JTextField field) {
            return field.getText().trim().isEmpty();
        }
    }

    // --- Login Frame ---
    static class LoginFrame extends JFrame {
        public LoginFrame() { /* Constructor is mostly unchanged */
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
            gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Username:"), gbc);
            gbc.gridx = 1; panel.add(usernameField, gbc);
            gbc.gridx = 0; gbc.gridy = ++y; panel.add(new JLabel("Password:"), gbc);
            gbc.gridx = 1; panel.add(passwordField, gbc);
            gbc.gridx = 1; gbc.gridy = ++y; gbc.anchor = GridBagConstraints.CENTER;
            panel.add(loginBtn, gbc);
            add(panel);
            
            // --- CHANGED --- Login button now calls the API
            loginBtn.addActionListener(e -> {
                String user = usernameField.getText().trim();
                String pass = new String(passwordField.getPassword());
                
                loginBtn.setText("Logging in...");
                loginBtn.setEnabled(false);

                new SwingWorker<User, Void>() {
                    @Override
                    protected User doInBackground() {
                        return ApiClient.login(user, pass);
                    }

                    @Override
                    protected void done() {
                        try {
                            User loggedInUser = get();
                            if (loggedInUser != null) {
                                dispose();
                                new CourierDashboard(loggedInUser); // Pass the whole User object
                            } else {
                                JOptionPane.showMessageDialog(LoginFrame.this, "Invalid credentials. Try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            loginBtn.setText("Login");
                            loginBtn.setEnabled(true);
                        }
                    }
                }.execute();
            });

            setVisible(true);
        }
    }

    // --- Courier Dashboard ---
    static class CourierDashboard extends JFrame {
        private User loggedInUser; // --- NEW --- Store the logged-in user

        // --- CHANGED --- Constructor now takes a User object
        public CourierDashboard(User courier) {
            this.loggedInUser = courier;
            setTitle("Courier Dashboard - " + loggedInUser.getUsername());
            setSize(400, 300);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
            panel.setBackground(Color.WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(15, 10, 15, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUser.getUsername() + "!");
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
            welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JButton viewBtn = new JButton("View All Parcels");
            JButton updateBtn = new JButton("Update Parcel Status");
            JButton logoutBtn = new JButton("Logout");
            styleButton(viewBtn); styleButton(updateBtn); styleButton(logoutBtn);
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; panel.add(welcomeLabel, gbc);
            gbc.gridy = 1; panel.add(viewBtn, gbc);
            gbc.gridy = 2; panel.add(updateBtn, gbc);
            gbc.gridy = 3; panel.add(logoutBtn, gbc);
            
            // --- CHANGED --- Actions now pass the user object
            viewBtn.addActionListener(e -> new ViewParcelWindow(loggedInUser));
            updateBtn.addActionListener(e -> new UpdateParcelWindow(loggedInUser));
            logoutBtn.addActionListener(e -> {
                dispose();
                new MainMenu().setVisible(true); // Go back to main menu
            });
            
            add(panel);
            setVisible(true);
        }

        private void styleButton(JButton button) { /* Unchanged */
            button.setPreferredSize(new Dimension(220, 40));
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setBackground(new Color(65, 105, 225));
            button.setForeground(Color.WHITE);
        }
    }

    // --- View Parcels ---
    // --- CHANGED --- This window now fetches live data
    static class ViewParcelWindow extends JFrame {
        ViewParcelWindow(User courier) { // Takes User object
            setTitle("All Parcels");
            setSize(500, 400);
            setLocationRelativeTo(null);

            DefaultListModel<Parcel> model = new DefaultListModel<>();
            JList<Parcel> list = new JList<>(model);
            list.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JButton back = new JButton("Back");
            back.addActionListener(e -> dispose());
            
            add(new JScrollPane(list), BorderLayout.CENTER);
            add(back, BorderLayout.SOUTH);

            // Fetch data from API
            new SwingWorker<List<Parcel>, Void>() {
                @Override
                protected List<Parcel> doInBackground() {
                    return ApiClient.getParcels(null); // null filter to get all
                }
                @Override
                protected void done() {
                    try {
                        List<Parcel> parcels = get();
                        for (Parcel p : parcels) {
                            model.addElement(p);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
            
            setVisible(true);
        }
    }

    // --- Update Parcel Status ---
    // --- CHANGED --- This window now fetches live data and updates status
    static class UpdateParcelWindow extends JFrame {
        UpdateParcelWindow(User courier) {
            setTitle("Update Parcel Status");
            setSize(500, 400);
            setLocationRelativeTo(null);
            
            DefaultListModel<Parcel> model = new DefaultListModel<>();
            JList<Parcel> list = new JList<>(model);
            list.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JButton updateBtn = new JButton("Mark as Delivered");
            JButton backBtn = new JButton("Back");

         // The corrected code
            updateBtn.addActionListener(e -> {
                Parcel selected = list.getSelectedValue();
                if (selected != null) {
                    new SwingWorker<Boolean, Void>() {
                        @Override
                        protected Boolean doInBackground() {
                           // --- FIX ---
                           // We now pass the third argument: courier.getId()
                           return ApiClient.updateParcelStatus(selected.getParcelId(), "Delivered", courier.getId());
                        }
                        @Override
                        protected void done() {
                           try {
                               if(get()) {
                                   JOptionPane.showMessageDialog(UpdateParcelWindow.this, "Marked as delivered!");
                                   model.removeElement(selected); // Update UI
                               } else {
                                   JOptionPane.showMessageDialog(UpdateParcelWindow.this, "Failed to update.", "Error", JOptionPane.ERROR_MESSAGE);
                               }
                           } catch (Exception ex) { ex.printStackTrace(); }
                        }
                    }.execute();
                }
            });

            backBtn.addActionListener(e -> dispose());

            JPanel btnsPanel = new JPanel();
            btnsPanel.add(updateBtn);
            btnsPanel.add(backBtn);

            add(new JScrollPane(list), BorderLayout.CENTER);
            add(btnsPanel, BorderLayout.SOUTH);
            
            // Fetch parcels with "Pending" or "In Transit" status
            new SwingWorker<List<Parcel>, Void>() {
                @Override
                protected List<Parcel> doInBackground() {
                     // In a real app, you might fetch only for a specific courier
                     // or only non-delivered items. Here we fetch all pending for simplicity.
                    return ApiClient.getParcels("Pending"); 
                }
                @Override
                protected void done() {
                    try {
                        model.clear();
                        get().forEach(model::addElement);
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }.execute();

            setVisible(true);
        }
    }

    // --- Parcel Status Viewer ---
    // --- CHANGED --- This now uses API and SwingWorker
    static class ParcelStatusViewer extends JFrame {
        ParcelStatusViewer(JFrame mainMenu) {
            setTitle("Parcel Status Viewer");
            setSize(500, 300);
            setLocationRelativeTo(null);

            JTextField idField = new JTextField(20);
            JTextArea resultArea = new JTextArea();
            resultArea.setEditable(false);
            resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            
            JButton searchBtn = new JButton("Search");
            JButton backBtn = new JButton("Back");

            JPanel inputPanel = new JPanel();
            inputPanel.add(new JLabel("Parcel ID:"));
            inputPanel.add(idField);
            inputPanel.add(searchBtn);

            searchBtn.addActionListener(e -> {
                String id = idField.getText().trim();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a Parcel ID.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                resultArea.setText("Searching...");
                new SwingWorker<Parcel, Void>() {
                    @Override
                    protected Parcel doInBackground() {
                        return ApiClient.getParcelStatus(id);
                    }
                    @Override
                    protected void done() {
                        try {
                            Parcel found = get();
                            if (found != null) {
                                resultArea.setText("Parcel ID: " + found.getParcelId() + "\nSender: " + found.getSenderName() +
                                        "\nReceiver: " + found.getReceiverName() + "\nPickup: " + found.getPickupPoint() +
                                        "\nDrop-off: " + found.getDropPoint() + "\n\nStatus: " + found.getStatus());
                            } else {
                                resultArea.setText("Parcel not found.");
                            }
                        } catch (Exception ex) {
                            resultArea.setText("An error occurred while searching.");
                            ex.printStackTrace();
                        }
                    }
                }.execute();
            });

            backBtn.addActionListener(e -> {
                mainMenu.setVisible(true);
                dispose();
            });
            
            JPanel bottomPanel = new JPanel();
            bottomPanel.add(backBtn);

            add(inputPanel, BorderLayout.NORTH);
            add(new JScrollPane(resultArea), BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);
            setVisible(true);
        }
    }
}
