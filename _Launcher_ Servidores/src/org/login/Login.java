package org.login;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Window.Type;
import java.awt.Font;
import java.awt.Cursor;

import javax.swing.JOptionPane;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.manager.utils.SQL;

public class Login {

	private JFrame frmLauncher;
	private JPasswordField pwdPassword;
	private JTextField txtNicknameOuEmail;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frmLauncher.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLauncher = new JFrame();
		frmLauncher.setAlwaysOnTop(false);
		frmLauncher.setFont(new Font("VCR OSD Mono", Font.PLAIN, 12));
		frmLauncher.setTitle("Launcher");
		frmLauncher.setType(Type.POPUP);
		frmLauncher.getContentPane().setBackground(Color.GRAY);
		frmLauncher.setBounds(680, 280, 500, 500);
		frmLauncher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLauncher.getContentPane().setLayout(null);
		frmLauncher.setUndecorated(false);
		frmLauncher.setResizable(false);
		
		JLabel background = new JLabel(new ImageIcon(getClass().getResource("/org/manager/design/background.png")));
		background.setBounds(0, 0, 500, 471);
		
		JLabel registro = new JLabel(new ImageIcon(getClass().getResource("/org/manager/design/registro.png")));
		registro.setBounds(99, 366, 170, 25);
		
		JLabel esqueceu = new JLabel(new ImageIcon(getClass().getResource("/org/manager/design/esqueceu.png")));
		esqueceu.setBounds(99, 405, 180, 27);
		
		txtNicknameOuEmail = new JTextField();
		txtNicknameOuEmail.setText("Nickname ou Email");
		txtNicknameOuEmail.setBounds(119, 221, 287, 32);
		txtNicknameOuEmail.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				if(txtNicknameOuEmail.getText().equals("")){
					txtNicknameOuEmail.setText("Nickname ou Email");
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if(txtNicknameOuEmail.getText().equals("Nickname ou Email")){
					txtNicknameOuEmail.setText("");
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(txtNicknameOuEmail.getText().equals("Nickname ou Email")){
					txtNicknameOuEmail.setText("");
				}
			}
		});
		frmLauncher.getContentPane().add(txtNicknameOuEmail);
		txtNicknameOuEmail.setColumns(10);
		
		pwdPassword = new JPasswordField();
		pwdPassword.setBounds(117, 264, 289, 32);
		
		JLabel sign = new JLabel(new ImageIcon(getClass().getResource("/org/manager/design/signbutton1.png")));
		sign.setBounds(172, 303, 144, 52);
		sign.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
				sign.setIcon(new ImageIcon(getClass().getResource("/org/manager/design/signbutton1.png")));
				txtNicknameOuEmail.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				sign.setIcon(new ImageIcon(getClass().getResource("/org/manager/design/signbutton.png")));
				txtNicknameOuEmail.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!txtNicknameOuEmail.getText().equals("") && !txtNicknameOuEmail.getText().equals("Nickname ou Email")){
					if(pwdPassword.getText().toString() != null){
						if(txtNicknameOuEmail.getText().contains("@")){
							try{
								ResultSet rs = SQL.getQueryResult("SELECT * FROM LAUNCHER WHERE EMAIL = '"+txtNicknameOuEmail.getText()+"' AND SENHA = '"+pwdPassword.getText().toString()+"'");
								if(rs.next()){
									SQL.execute("UPDATE LAUNCHER SET LAUNCHER = 1 WHERE EMAIL = '"+txtNicknameOuEmail.getText()+"'");
									JOptionPane.showMessageDialog(null, "Login realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
									
									frmLauncher.setVisible(false);
								}else{
									JOptionPane.showMessageDialog(null, "Email/Senha incorretos.", "Ops", JOptionPane.ERROR_MESSAGE);
								}
								rs.getStatement().getConnection().close();
							}catch(SQLException ee){
								ee.printStackTrace();
							}
						}else{
							try{
								ResultSet rs = SQL.getQueryResult("SELECT * FROM LAUNCHER WHERE NICK_NAME = '"+txtNicknameOuEmail.getText()+"' AND SENHA = '"+pwdPassword.getText().toString()+"'");
								if(rs.next()){
									SQL.execute("UPDATE LAUNCHER SET LAUNCHER = 1 WHERE NICK_NAME = '"+txtNicknameOuEmail.getText()+"'");
									JOptionPane.showMessageDialog(null, "Login realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
									
									frmLauncher.setVisible(false);
								}else{
									JOptionPane.showMessageDialog(null, "Nickname/Senha incorretos.", "Ops", JOptionPane.ERROR_MESSAGE);
								}
								rs.getStatement().getConnection().close();
							}catch(SQLException ee){
								ee.printStackTrace();
							}
						}
					}else{
						JOptionPane.showMessageDialog(null, "Você não digitou a sua senha.", "Ops", JOptionPane.ERROR_MESSAGE);
					}
				}
				
				if(txtNicknameOuEmail.getText().equals("")){
					JOptionPane.showMessageDialog(null, "Você não digitou seu email e/ou senha.", "Ops", JOptionPane.ERROR_MESSAGE);
				}
				
				if(txtNicknameOuEmail.getText().equals("Nickname ou Email")){
					JOptionPane.showMessageDialog(null, "Você não digitou seu email e/ou senha.", "Ops", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		frmLauncher.getContentPane().add(pwdPassword);
		frmLauncher.getContentPane().add(sign);
		frmLauncher.getContentPane().add(esqueceu);
		frmLauncher.getContentPane().add(registro);
		frmLauncher.getContentPane().add(background);
	}
}
