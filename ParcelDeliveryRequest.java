package src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ParcelDeliveryRequest {

	private JFrame frame;
	private JTextField senderNameField, senderPhoneField, senderAddressField;
	private JTextField receiverNameField, receiverPhoneField, receiverAddressField;
	private JTextField pickupPointField, dropPointField;

	// Parcel ID counter
	private static int parcelCounter = 1;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				ParcelDeliveryRequest window = new ParcelDeliveryRequest();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public ParcelDeliveryRequest() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame("SmartParcel - Create Delivery Request");
		frame.setBounds(100, 100, 600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;

		int y = 0;

		addLabelField(panel, gbc, "Sender Name:", senderNameField = new JTextField(20), y++);
		addLabelField(panel, gbc, "Sender Phone:", senderPhoneField = new JTextField(20), y++);
		addLabelField(panel, gbc, "Sender Address:", senderAddressField = new JTextField(20), y++);

		addLabelField(panel, gbc, "Receiver Name:", receiverNameField = new JTextField(20), y++);
		addLabelField(panel, gbc, "Receiver Phone:", receiverPhoneField = new JTextField(20), y++);
		addLabelField(panel, gbc, "Receiver Address:", receiverAddressField = new JTextField(20), y++);

		addLabelField(panel, gbc, "Pickup Point:", pickupPointField = new JTextField(20), y++);
		addLabelField(panel, gbc, "Drop-off Point:", dropPointField = new JTextField(20), y++);

		JButton btnSubmit = new JButton("Create Delivery Request");
		btnSubmit.setFont(new Font("Arial", Font.BOLD, 14));
		btnSubmit.setBackground(new Color(34, 139, 34));
		btnSubmit.setForeground(Color.WHITE);
		btnSubmit.setFocusPainted(false);
		btnSubmit.addActionListener(e -> handleSubmit());

		gbc.gridx = 1;
		gbc.gridy = y;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(btnSubmit, gbc);

		frame.getContentPane().add(panel);
	}

	private void addLabelField(JPanel panel, GridBagConstraints gbc, String labelText, JTextField field, int y) {
		gbc.gridx = 0;
		gbc.gridy = y;
		gbc.gridwidth = 1;
		panel.add(new JLabel(labelText), gbc);

		gbc.gridx = 1;
		gbc.gridy = y;
		gbc.gridwidth = 2;
		panel.add(field, gbc);
	}

	private void handleSubmit() {
		// Empty field check
		if (isEmpty(senderNameField) || isEmpty(senderPhoneField) || isEmpty(senderAddressField) ||
			isEmpty(receiverNameField) || isEmpty(receiverPhoneField) || isEmpty(receiverAddressField) ||
			isEmpty(pickupPointField) || isEmpty(dropPointField)) {

			JOptionPane.showMessageDialog(frame,
					"Please fill in all the fields.",
					"Missing Information",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Phone number validation
		if (!isValidPhone(senderPhoneField.getText()) || !isValidPhone(receiverPhoneField.getText())) {
			JOptionPane.showMessageDialog(frame,
					"Phone numbers must contain only digits and be 10 to 15 characters long.",
					"Invalid Phone Number",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Name validation
		if (!isValidName(senderNameField.getText()) || !isValidName(receiverNameField.getText())) {
			JOptionPane.showMessageDialog(frame,
					"Names must contain only letters and spaces (no numbers or special characters).",
					"Invalid Name Format",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		showParcelSummary();
	}

	private boolean isEmpty(JTextField field) {
		return field.getText().trim().isEmpty();
	}

	private boolean isValidPhone(String phone) {
		return phone.matches("\\d{10,15}");
	}

	private boolean isValidName(String name) {
		return name.matches("[a-zA-Z\\s]+");
	}

	private void showParcelSummary() {
		String parcelId = "P" + parcelCounter;

		String senderInfo = "Sender Info:\n" +
				"Name: " + senderNameField.getText() + "\n" +
				"Phone: " + senderPhoneField.getText() + "\n" +
				"Address: " + senderAddressField.getText() + "\n";

		String receiverInfo = "Receiver Info:\n" +
				"Name: " + receiverNameField.getText() + "\n" +
				"Phone: " + receiverPhoneField.getText() + "\n" +
				"Address: " + receiverAddressField.getText() + "\n";

		String deliveryInfo = "Pickup Point : " + pickupPointField.getText() + "\n" +
				"Drop-off Point: " + dropPointField.getText();

		String fullMessage = "--- Parcel Delivery Request ---\n\n" +
				"Parcel ID: " + parcelId + "\n\n" +
				senderInfo + "\n" + receiverInfo + "\n" + deliveryInfo;

		JOptionPane.showMessageDialog(frame, fullMessage, "Parcel Summary", JOptionPane.INFORMATION_MESSAGE);

		parcelCounter++; // Increment ID for next request
		resetForm();     // Clear form
	}

	private void resetForm() {
		senderNameField.setText("");
		senderPhoneField.setText("");
		senderAddressField.setText("");

		receiverNameField.setText("");
		receiverPhoneField.setText("");
		receiverAddressField.setText("");

		pickupPointField.setText("");
		dropPointField.setText("");

		senderNameField.requestFocus(); // Focus back to first field
	}
}