package InterfaceGrafica;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Label extends JFrame {

//	Font grande = new Font("Arial", Font.BOLD, 50);
//	JLabel legenda = new JLabel("texto");
	
		ImageIcon imagem = new ImageIcon(getClass().getResource("carro.png"));
		JLabel label = new JLabel(imagem);
	
	
	public Label() {
//		legenda.setFont(grande);
//		legenda.setText("ok");
//		legenda.setHorizontalAlignment(SwingConstants.CENTER);
//		add(legenda);
		add(label);
		setSize(20, 80);
//		label.setAlignmentY(BOTTOM_ALIGNMENT);
//		label.setAlignmentY(RIGHT_ALIGNMENT);
		label.setAlignmentX(RIGHT_ALIGNMENT);
//		label.setAlignmentY(BOTTOM_ALIGNMENT);
//		label.setVerticalAlignment(SwingConstants);
//		label.setBounds(80, 40, 20, 30);
		
//		setTitle("");
//		setSize(40, 80);
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(this);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Label();
	}

}
