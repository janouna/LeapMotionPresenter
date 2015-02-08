package edu.wpi.cs.lmp.objects;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TextInputFrame extends JFrame {
	JLabel lbl = new JLabel("Enter Text");
	public TextInputFrame()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600,300);
		setLocation(200,100);
		final JPanel main = new JPanel(new BorderLayout());
		main.add(lbl,BorderLayout.CENTER);
		final JButton btn = new JButton("OK");
		btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new JOptionTextAreaPane();
				lbl.setText("<html>Text entered = <br>"+
						JOptionTextAreaPane.showInputDialog("Enter some text").replaceAll("\\n","<br>")+
						"</html>");}});
		main.add(btn,BorderLayout.SOUTH);
		getContentPane().add(main);
	}
}
