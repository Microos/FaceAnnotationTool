package hk.microos.frames;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import hk.microos.data.MyImage;
import hk.microos.tools.ClickHelper;
import hk.microos.tools.ImageTool;
import hk.microos.tools.TableHelper;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MainFrame extends JFrame {

	private JPanel contentPane;

	private JScrollPane scrollPanel;
	private MyImagePanel imagePanel;
	private JPanel toolPanel;
	private JButton button;
	private ClickHelper cm;
	static public int defaultScrollH = -1;
	static public int defaultScrollW = -1;
	private JTable imgNameTable;
	private JTable coordTable;
	public TableHelper imgListTH;
	public TableHelper coordListTH;

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
		setBounds(100, 100, 1195, 601);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		scrollPanel = new JScrollPane();
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		toolPanel = new JPanel();

		JScrollPane leftScrollPanel = new JScrollPane();

		JScrollPane rightScrollPanel = new JScrollPane();

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(toolPanel, GroupLayout.DEFAULT_SIZE, 1173, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(3)
								.addComponent(leftScrollPanel, GroupLayout.PREFERRED_SIZE, 230,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(scrollPanel, GroupLayout.PREFERRED_SIZE, 643, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(rightScrollPanel,
										GroupLayout.PREFERRED_SIZE, 286, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addComponent(toolPanel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(leftScrollPanel, GroupLayout.PREFERRED_SIZE, 516,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(scrollPanel, GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(14).addComponent(rightScrollPanel,
								GroupLayout.PREFERRED_SIZE, 505, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap()));

		button = new JButton("TEST");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testTable();
			}
		});
		toolPanel.add(button);

		imagePanel = new MyImagePanel(scrollPanel);
		imagePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					cm.leftClick(e.getX(), e.getY());
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					cm.rightClick(e.getX(), e.getY());
				}
			}
		});
		cm = new ClickHelper(imagePanel);
		imagePanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				cm.mouseAt(x, y);
				if (e.isShiftDown()) {
					cm.mouseShiftMoveAt(x, y);
				}
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

		imgNameTable = new JTable(
				new DefaultTableModel(new String[] { "id","Image name", "#Marks","Path prefix" }, 100));
		leftScrollPanel.setViewportView(imgNameTable);
		imgListTH = new TableHelper(imgNameTable);
		imgListTH.setColSize(new int[]{50,200,61,200});
		
		
		
		
		coordTable = new JTable(new DefaultTableModel(new Object[] { "id", "major", "minor", "angle", "x", "y" }, 10));
		rightScrollPanel.setViewportView(coordTable);
		coordListTH = new TableHelper(coordTable);
		int s = 50;
		coordListTH.setColSize(new int[]{34,s,s,s,s,s});
		
	}

	void testTable() {
		System.out.println(imgListTH.getTable().hashCode() == imgNameTable.hashCode());
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
