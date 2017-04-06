package hk.microos.frames;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import hk.microos.data.MyImage;
import hk.microos.tools.ClickHelper;
import hk.microos.tools.IOTool;
import hk.microos.tools.ImageTool;
import hk.microos.tools.TableHelper;
import hk.microos.tools.UniversalTool;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;

import java.awt.Dimension;
import java.awt.Color;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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
	private String recordedOpenPath = "/home/rick/Space/work/FDDB/data/ImagePath.txt";
	private String recordedSavePath = "";
	private JButton btnReadImageList;
	private int leftTableSelectedRow = -1;
	private HashMap<String, MyImage> pathImgPair;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					// frame.recordedOpenPath =
					// System.getProperty("user.home")+"/Desktop";
					frame.recordedSavePath = System.getProperty("user.home") + "/Desktop";
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
				testSetBackgroundImage();
			}
		});
		toolPanel.add(button);

		btnReadImageList = new JButton("Read Image List");
		btnReadImageList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadImageList();
			}
		});
		toolPanel.add(btnReadImageList);

		imagePanel = new MyImagePanel(this, scrollPanel);
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
				new DefaultTableModel(new String[] { "id", "Image name", "Marks", "Path prefix" }, 0));
		leftScrollPanel.setViewportView(imgNameTable);
		imgListTH = new TableHelper(imgNameTable);
		imgListTH.setColSize(new int[] { 50, 115, 50, 400 });
		ListSelectionModel lsm = imgNameTable.getSelectionModel();
		lsm.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting())
					return;
				leftTableSelectedRow = imgListTH.getSelectedRowIndex();
				int id = (int) imgListTH.getValueAt(leftTableSelectedRow, 0);
				String path = imgListTH.getPathAt(leftTableSelectedRow);
				MyImage mim = getMyImageFromPathImgPari(path, id);
				boolean changed = imagePanel.setCurrentImage(mim);
				if (changed) {
					if (defaultScrollW == -1)
						defaultScrollW = scrollPanel.getWidth();
					if (defaultScrollH == -1)
						defaultScrollH = scrollPanel.getHeight();
					int imw = mim.w;
					int imh = mim.h;

					imagePanel.setCurrentImage(mim);
					setImgPanelSize(imw, imh);
					imagePanel.repaint();
				}
			}
		});

		coordTable = new JTable(new DefaultTableModel(new Object[] { "id", "major", "minor", "angle", "x", "y" }, 0));
		rightScrollPanel.setViewportView(coordTable);
		coordListTH = new TableHelper(coordTable);
		int s = 50;
		coordListTH.setColSize(new int[] { 34, s, s, s, s, s });

	}

	void loadImageList() {
		JFileChooser fc = new JFileChooser(recordedOpenPath);
		// fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int res = fc.showOpenDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			recordedOpenPath = f.getParent();
			if (!IOTool.isTextFile(f)) {
				// not a readable text file
				JOptionPane.showMessageDialog(this, String
						.format("\"%s\" \ndoes not likely contain any readable text.", f.getAbsolutePath()).toString(),
						"Not a text file", JOptionPane.WARNING_MESSAGE);
			} else {
				// read
				ArrayList<String> imgList = IOTool.readText(f);
				if (imgList == null) {
					// read failed
					JOptionPane.showMessageDialog(this,
							String.format("\"%s\" \ncannot be properly read.", f.getAbsolutePath()).toString(),
							"Reading failed", JOptionPane.WARNING_MESSAGE);
				} else {
					// read successes
					// load Images to table
					pathImgPair = null;
					leftTableSelectedRow = -1;
					pathImgPair = IOTool.filterImageList(imgList, this);
					fillImageNameTable();
				}
			}

		}
	}

	void fillImageNameTable() {
		imgListTH.fillRows(pathImgPair.keySet(), true);
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

		imagePanel.repaint();
	}

	public void marksUpdated(MyImage mim) {
		if(leftTableSelectedRow == -1){
			System.err.println("marking on non-loaded image?");
		}
		imgListTH.setValueAt(leftTableSelectedRow, 2, mim.getMarkNumString());
	}

	MyImage getMyImageFromPathImgPari(String s, int id) {
		MyImage img = pathImgPair.get(s);
		if (img == null) {
			System.err.println(s + " not loaded, load now.");
			File f = new File(s);
			img = new MyImage(f, id);
			pathImgPair.put(s, img);
			return img;
		} else {
			return img;
		}

	}
}
