package project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Parcel {
    String parcelId;
    String recipient;
    String status;

    Parcel(String parcelId, String recipient, String status) {
        this.parcelId = parcelId;
        this.recipient = recipient;
        this.status = status;
    }

    @Override
    public String toString() {
        return parcelId + " - " + recipient + " (" + status + ")";
    }
}

public class courierApp {

    static java.util.List<Parcel> assignedParcels = new ArrayList<>();

    public static void main(String[] args) {
        // Dummy parcels
        assignedParcels.add(new Parcel("P001", "Ali", "Pending"));
        assignedParcels.add(new Parcel("P002", "Siti", "Pending"));

        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}

// LOGIN SCREEN
class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Courier Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1));
        JTextField username = new JTextField();
        JButton loginBtn = new JButton("Login");

        panel.add(new JLabel("Courier Username:"));
        panel.add(username);
        panel.add(loginBtn);

        loginBtn.addActionListener(e -> {
            dispose();
            new CourierDashboard();
        });

        add(panel);
        setVisible(true);
    }
}

// MAIN DASHBOARD
class CourierDashboard extends JFrame {
    public CourierDashboard() {
        setTitle("Courier Dashboard");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton viewBtn = new JButton("View Assigned Parcels");
        JButton updateBtn = new JButton("Update Parcel Status");

        viewBtn.addActionListener(e -> new ViewParcelWindow());
        updateBtn.addActionListener(e -> new UpdateParcelWindow());

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.add(viewBtn);
        panel.add(updateBtn);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}

// VIEW-ONLY PARCEL WINDOW
class ViewParcelWindow extends JFrame {
    public ViewParcelWindow() {
        setTitle("Assigned Parcels");
        setSize(350, 250);
        setLocationRelativeTo(null);

        DefaultListModel<String> model = new DefaultListModel<>();
        for (Parcel p : courierApp.assignedParcels) {
            model.addElement(p.parcelId + ": " + p.recipient + " - " + p.status);
        }

        JList<String> list = new JList<>(model);
        add(new JScrollPane(list));

        setVisible(true);
    }
}

// UPDATE STATUS WINDOW
//UPDATE STATUS WINDOW (Only shows Pending parcels)
class UpdateParcelWindow extends JFrame {
 JList<Parcel> parcelList;
 DefaultListModel<Parcel> pendingParcelModel;

 public UpdateParcelWindow() {
     setTitle("Update Parcel Status");
     setSize(400, 300);
     setLocationRelativeTo(null);

     pendingParcelModel = new DefaultListModel<>();
     for (Parcel p : courierApp.assignedParcels) {
         if (p.status.equalsIgnoreCase("Pending")) {
             pendingParcelModel.addElement(p);
         }
     }

     parcelList = new JList<>(pendingParcelModel);
     JScrollPane scroll = new JScrollPane(parcelList);
     JButton updateBtn = new JButton("Mark as Delivered");

     updateBtn.addActionListener(e -> {
         Parcel selected = parcelList.getSelectedValue();
         if (selected != null) {
             selected.status = "Delivered";
             pendingParcelModel.removeElement(selected); // remove from current list
             JOptionPane.showMessageDialog(this, "Parcel " + selected.parcelId + " marked as Delivered!");
         } else {
             JOptionPane.showMessageDialog(this, "Please select a parcel to update.");
         }
     });

     add(scroll, BorderLayout.CENTER);
     add(updateBtn, BorderLayout.SOUTH);
     setVisible(true);
 }
}

