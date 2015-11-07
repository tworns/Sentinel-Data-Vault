package userInterface;

import java.awt.*;
import java.awt.event.*;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.*; 

import java.security.NoSuchAlgorithmException;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import controllers.DatabaseManager;
import controllers.VaultController;

import dataManagement.User;

public class LoginView {

	public JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton btnSignUp;
	private JButton btnForgotPassword;
	public String username = "";
	private JLabel lblSentinelDataVault;
	private int failedattempt = 0;

	/**
	 * Launch the application.
	 *    
	 * APIs
	 *    
	 * EmailValidator Class   
	 * 	https://commons.apache.org/proper/commons-validator/apidocs/org/apache/commons/validator/routines/EmailValidator.html
	 *    
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginView window = new LoginView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Login");
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblEmail = new JLabel("E-mail:");
		lblEmail.setBounds(81, 95, 56, 18);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(65, 132, 72, 18);
		
		textField = new JTextField();
		textField.setBounds(139, 95, 176, 24);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(139, 129, 176, 24);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.setBounds(327, 95, 72, 58);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				username = textField.getText();
				String password = String.valueOf(passwordField.getPassword()); // getText() is deprecated; changed to getPassword()
				
				/*
				 * SHA implementation to validate password
				 */
				
				VaultController v = new VaultController();
				int result = 0;
				try {
					result = v.loginCheck(username, password);
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (result == 1){
					//MainView window = new MainView(username);
					//window.frmSentinelDataVault.setVisible(true);
					failedattempt = 0;
					frame.dispose();
					
				}
				else{
					failedattempt++;
				}
				
				if(failedattempt > 1 && failedattempt <5){
					try {
						VaultController.Send("sentineldatavault", "SENTINELDATA", username, 
								"Security Warning", "Dear user,\nYou have multiple failed login attempts for your account\n"
										+ "If it is not you, please change your password immediatelly");
					} catch (AddressException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (MessagingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if(failedattempt == 5){
					DatabaseManager d = new DatabaseManager();
					User u = d.retrieveUserFromDatabase(username);
					d.deleteAllEntryFromDatabase(u);
					JOptionPane.showMessageDialog(null,"Your account data has been deleted due to multiple failed login attempts");
				}
			}
		});
		frame.getContentPane().setLayout(null);
		
		btnSignUp = new JButton("Sign up");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SignupView signup = new SignupView();
				signup.setVisible(true);
			}
		});
		btnSignUp.setBounds(91, 174, 124, 27);
		frame.getContentPane().add(btnSignUp);
		frame.getContentPane().add(btnNewButton);
		frame.getContentPane().add(lblPassword);
		frame.getContentPane().add(lblEmail);
		frame.getContentPane().add(textField);
		frame.getContentPane().add(passwordField);
		
		btnForgotPassword = new JButton("Forgot Password");
		btnForgotPassword.setBounds(229, 174, 140, 27);
		frame.getContentPane().add(btnForgotPassword);
		
		lblSentinelDataVault = new JLabel("Sentinel Data Vault");
		lblSentinelDataVault.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblSentinelDataVault.setBounds(139, 33, 189, 27);
		frame.getContentPane().add(lblSentinelDataVault);
		frame.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblEmail, textField, lblPassword, passwordField, btnNewButton, btnSignUp, btnForgotPassword}));
	}
}
