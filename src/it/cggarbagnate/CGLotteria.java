package it.cggarbagnate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class CGLotteria {
	private static String data_estrazione = "12 aprile 2016";
	private static Integer delay = Integer.valueOf(5);
	private static List<String> biglietti = new ArrayList<String>();
	private static JFileChooser fc = new JFileChooser(new File("."));

	public static void main(String[] args) {
		initApp();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CGLotteria obj = new CGLotteria();
				obj.display();
			}
		});
	}

	private static void initApp() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(System.getProperty("user.dir") + "/CGLotteria.properties"));
			if (prop.getProperty("data_estrazione") != null) {
				data_estrazione = prop.getProperty("data_estrazione");
			}
			if ((prop.getProperty("delay") != null) && (Integer.getInteger(prop.getProperty("delay")) != null)) {
				delay = Integer.getInteger(prop.getProperty("delay"));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void display() {
		JFrame jfrm = new JFrame("Comitato Genitori Primo Circolo Garbagnate Milanese");
		jfrm.setSize(1000, 500);
		jfrm.setBackground(Color.WHITE);
		jfrm.setForeground(Color.WHITE);
		jfrm.setFont(new Font("Arial", 0, 20));
		jfrm.setIconImage(new ImageIcon(ClassLoader.getSystemResource("images/logocg.gif")).getImage());
		jfrm.setResizable(true);
		jfrm.setDefaultCloseOperation(3);
		jfrm.setLayout(new BorderLayout(10, 10));
		Border outline = BorderFactory.createEmptyBorder(4, 0, 4, 0);

		JPanel northPanel = new JPanel(new BorderLayout());
		JPanel westPanel = new JPanel(new BorderLayout());
		JPanel centerPanel = new JPanel(new BorderLayout());
		westPanel.setBackground(Color.WHITE);
		centerPanel.setBackground(Color.WHITE);
		final JPanel southPanel = new JPanel(new BorderLayout());

		JLabel jlabData = new JLabel("Estrazione Lotteria di Primavera - " + data_estrazione);
		jlabData.setForeground(Color.BLACK);
		jlabData.setFont(new Font("Arial", 1, 28));
		jlabData.setHorizontalAlignment(0);
		jlabData.setVerticalAlignment(0);
		jlabData.setBorder(outline);
		northPanel.add(jlabData, "North");
		northPanel.add(new JSeparator(0), "Center");
		jfrm.add(northPanel, "North");

		final JLabel jlabNumero = new JLabel(" Biglietti non caricati ");
		jlabNumero.setForeground(Color.RED);
		jlabNumero.setFont(new Font("Arial", 1, 60));
		jlabNumero.setHorizontalAlignment(0);
		jlabNumero.setVerticalAlignment(0);
		jlabNumero.setBorder(outline);

		final JButton jbutEstrai = new JButton("ESTRAI",
				new ImageIcon(ClassLoader.getSystemResource("images/ball.gif")));
		jbutEstrai.setEnabled(false);
		jbutEstrai.setForeground(Color.BLUE);
		jbutEstrai.setBackground(Color.LIGHT_GRAY);
		jbutEstrai.setFont(new Font("Arial", 1, 28));
		jbutEstrai.setCursor(Cursor.getPredefinedCursor(12));
		jbutEstrai.setSize(new Dimension(200, 100));
		jbutEstrai.setHorizontalAlignment(2);
		jbutEstrai.setVerticalAlignment(0);
		jbutEstrai.setMultiClickThreshhold(3000);

		jbutEstrai.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					Thread.sleep(CGLotteria.delay.intValue() * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (CGLotteria.biglietti.size() == 0) {
					jlabNumero.setText(" Biglietti non caricati ");
				} else {
					Collections.shuffle(CGLotteria.biglietti);
					String estratto = (String) CGLotteria.biglietti.get(0);
					CGLotteria.biglietti.remove(0);
					jlabNumero.setFont(new Font("Arial", 1, 90));
					jlabNumero.setText(estratto);
				}
			}
		});
		westPanel.add(jbutEstrai, "Center");
		jfrm.add(westPanel, "West");
		centerPanel.add(new JSeparator(1), "West");
		centerPanel.add(jlabNumero, "Center");
		jfrm.add(centerPanel, "Center");

		final JButton jbutCarica = new JButton("Carica lista biglietti ...",
				new ImageIcon(ClassLoader.getSystemResource("images/folderopen.png")));
		jbutCarica.setForeground(Color.BLACK);
		jbutCarica.setBackground(Color.WHITE);
		jbutCarica.setFont(new Font("Arial", 1, 24));
		jbutCarica.setCursor(Cursor.getPredefinedCursor(12));
		jbutCarica.setSize(new Dimension(100, 200));
		jbutCarica.setHorizontalAlignment(0);
		jbutCarica.setVerticalAlignment(0);
		jbutCarica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int returnVal = CGLotteria.fc.showOpenDialog(southPanel);
				if (returnVal == 0) {
					File file = CGLotteria.fc.getSelectedFile();
					try {
						Scanner s = new Scanner(file);
						CGLotteria.biglietti.clear();
						while (s.hasNext()) {
							CGLotteria.biglietti.add(s.next());
						}
						s.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (CGLotteria.biglietti.size() > 0) {
						jbutEstrai.setEnabled(true);
						jlabNumero.setFont(new Font("Arial", 1, 48));
						jlabNumero.setText("<html> Pronti per l'estrazione.<br>Biglietti caricati => "
								+ CGLotteria.biglietti.size() + "</html>");
						jbutCarica.setVisible(false);
					}
				}
			}
		});
		southPanel.add(new JSeparator(0), "North");
		southPanel.add(jbutCarica, "Center");
		jfrm.add(southPanel, "South");

		jfrm.setVisible(true);
	}
}
