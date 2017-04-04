package hk.microos.frames;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import hk.microos.data.Ellipse;
import hk.microos.data.MyImage;
import hk.microos.data.Point_;
import hk.microos.tools.ClickManager;
import hk.microos.tools.ImageTool;
import hk.microos.tools.UniverseTool;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JToolBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTable imageListTable;
	private JTable elpsListTable;
	private JScrollPane scrollPanel;
	private MyImagePanel imagePanel;
	private JPanel toolPanel;
	private JButton button;
	private ClickManager cm;
	static public int defaultScrollH = -1;
	static public int defaultScrollW = -1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();

					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 979, 601);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		scrollPanel = new JScrollPane();
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		imageListTable = new JTable();

		elpsListTable = new JTable();

		toolPanel = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
								.addComponent(imageListTable, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(scrollPanel, GroupLayout.PREFERRED_SIZE, 643, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(elpsListTable,
										GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE))
						.addComponent(toolPanel, GroupLayout.DEFAULT_SIZE, 957, Short.MAX_VALUE))
				.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addComponent(toolPanel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(scrollPanel, GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE).addGap(6))
						.addComponent(elpsListTable, GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
						.addComponent(imageListTable, GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE))));

		button = new JButton("TEST");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testSetBackgroundImage();
			}
		});
		toolPanel.add(button);

		imagePanel = new MyImagePanel(scrollPanel);
		imagePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1){
					cm.leftClick(e.getX(), e.getY());
				}
			}
		});
		cm = new ClickManager(imagePanel);
		imagePanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				cm.mouseAt(e.getX(), e.getY());

			}
		});
		imagePanel.setForeground(Color.WHITE);
		imagePanel.setBackground(Color.ORANGE);

		imagePanel.setSize(1, 1);
		scrollPanel.setViewportView(imagePanel);
		imagePanel.setLayout(new BorderLayout(0, 0));
		scrollPanel.revalidate();
		imagePanel.revalidate();
		contentPane.setLayout(gl_contentPane);

	}

	void setImgPanelSize(int w, int h) {
		w = Math.max(w, defaultScrollW);
		h = Math.max(h, defaultScrollH);
		Dimension d = new Dimension(w, h);
		imagePanel.setSize(d);
		imagePanel.setPreferredSize(d);
		// imagePanel.setMaximumSize(d);
		// imagePanel.setMinimumSize(d);

	}

	// void autoAdjustScrollPanelSize(int imw, int imh) {
	// int toolw = toolPanel.getWidth();
	// int toolh = toolPanel.getHeight();
	// int scrw = scrollPanel.getWidth();
	// int scrh = scrollPanel.getHeight();
	//
	// // deal with H
	// System.out.println("defaultH " + defaultScrollH);
	// if (imh <= defaultScrollH) {
	// setAllSize(scrollPanel, scrw, imh);
	// this.setSize(new Dimension(this.getWidth(), imh+toolh+50));
	// System.out.println("scrollPanel: " + scrw + " " + imh);
	// System.out.println("windowSize :" + this.getWidth() + " " +
	// this.getHeight());
	// }
	// if (imh > defaultScrollH){
	// setAllSize(scrollPanel, scrw, defaultScrollH);
	// this.setSize(new Dimension(this.getWidth(), defaultScrollH+toolh+50));
	// System.out.println("scrollPanel: " + scrw + " " + defaultScrollH);
	// System.out.println("windowSize :" + this.getWidth() + " " +
	// this.getHeight());
	// }
	//
	// }

	void testSetBackgroundImage() {
		if (defaultScrollW == -1)
			defaultScrollW = scrollPanel.getWidth();
		if (defaultScrollH == -1)
			defaultScrollH = scrollPanel.getHeight();
		MyImage mim = ImageTool.getTestImage();
		int imw = mim.w;
		int imh = mim.h;
		
		imagePanel.setCurrentImage(mim);
		setImgPanelSize(imw, imh);
		System.out.println("imsize: " + imw + " " + imh);
		System.out.println("pansize:" + imagePanel.getWidth() + " " + imagePanel.getHeight());
		
		
		imagePanel.repaint();
	}
}
