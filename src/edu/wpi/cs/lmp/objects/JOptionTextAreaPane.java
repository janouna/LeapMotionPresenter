package edu.wpi.cs.lmp.objects;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class JOptionTextAreaPane extends JOptionPane {
	public static String showInputDialog(final String message)
	{
		String data = null;
		class GetData extends JDialog implements ActionListener
		{
			JTextArea ta = new JTextArea(5,10);
			JButton btnOK = new JButton("   OK   ");
			JButton btnCancel = new JButton("Cancel");
			String str = null;
			public GetData()
			{
				setModal(true);
				getContentPane().setLayout(new BorderLayout());
				setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				setLocation(400,300);
				getContentPane().add(new JLabel(message),BorderLayout.NORTH);
				getContentPane().add(ta,BorderLayout.CENTER);
				JPanel jp = new JPanel();
				btnOK.addActionListener(this);
				btnCancel.addActionListener(this);
				jp.add(btnOK);
				jp.add(btnCancel);
				getContentPane().add(jp,BorderLayout.SOUTH);
				pack();
				setVisible(true);
			}
			public void actionPerformed(ActionEvent ae)
			{
				if(ae.getSource() == btnOK) str = ta.getText();
				dispose();
			}
			public String getData(){return str;}
		}
		data = new GetData().getData();
		return data;
	}

}
