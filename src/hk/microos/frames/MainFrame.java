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

import hk.microos.data.Ellipse;
import hk.microos.data.Flags;
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
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.HashMap;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -2269022070473175677L;

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
	private String recordedImgPath = "/home/rick/Space/work/FDDB/data/Annotations";

	private String recordedAnnotPath = "/home/rick/Space/work/FDDB/data/Annotations";
	private String recordedSavePath = "/home/rick/Space/work/FDDB/data/Annotations";
	private JButton btnReadImageList;

	private int leftTableSelectedRow = -1;
	private HashMap<String, MyImage> pathImgPair = null;
	private HashMap<String, ArrayList<Ellipse>> pathStaticElpsesPair = null;
	private JButton btnReadAnnotations;
	private String annotContentPrefix = null;
	private String annotContentSuffix = null;
	private JButton btnOutputAnnotation;
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
//					 frame.recordedImgPath =
//					 "/Users/microos/Downloads/originalPics/imgPath.txt";

					// frame.recordedAnnotPath = System.getProperty("user.home")
					// + "/Desktop";
					// frame.recordedAnnotPath =
					// "/Users/microos/Downloads/FDDB-folds/FDDB-fold-05-ellipseList.txt";
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
				imgListTH.tm.setRowCount(0);
				System.out.println("src = 0");
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

		btnReadAnnotations = new JButton("Read Annotations");
		btnReadAnnotations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadAnnotList();
			}
		});
		toolPanel.add(btnReadAnnotations);
		
		btnOutputAnnotation = new JButton("Output Annotation");
		btnOutputAnnotation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputAnnotations();
			}
		});
		toolPanel.add(btnOutputAnnotation);

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
				leftTableOnClick(e);
			}
		});

		coordTable = new JTable(new DefaultTableModel(new Object[] { "id", "major", "minor", "angle", "x", "y" }, 0));
		rightScrollPanel.setViewportView(coordTable);
		coordListTH = new TableHelper(coordTable);
		int s = 50;
		coordListTH.setColSize(new int[] { 34, s, s, s, s, s });

	}

	void leftTableOnClick(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		leftTableSelectedRow = imgListTH.getSelectedRowIndex();
		if (leftTableSelectedRow == -1)
			return;
		int id = (int) imgListTH.getValueAt(leftTableSelectedRow, 0);
		String path = imgListTH.getBehindRowDataAt(leftTableSelectedRow);
		MyImage mim = UniversalTool.getMyImageFromPathImgPair(path,pathImgPair);
		boolean changed = imagePanel.setCurrentImage(mim);
		// display coords on the right
		setRightPanelCoords(mim);
		if (changed) {
			// update image panel size according to the image size
			updateImagePanelSize(mim);
		}
	}

	void setRightPanelCoords(MyImage mim) {
		//call this will update right panel with image's staticEllipse/Ellipse
		//use null as the arg will use a selectedRow as target image
		if (mim == null) {
			System.out.println("setRightPanelCoords with null");
			if (leftTableSelectedRow == -1) {
				System.err.println("bad0 at setRightPanelCoords");
				return;
			}
			String p = imgListTH.getBehindRowDataAt(leftTableSelectedRow);
			mim = pathImgPair.get(p);
			if (mim == null) {
				System.err.println("bad1 at setRightPanelCoords");
				return;
			}
			
		}

		// 1 load static ellipse into the table
		// when pathElpsesPair set and
		ArrayList<Ellipse> staticElps;
		if (pathStaticElpsesPair != null) {
			staticElps = pathStaticElpsesPair.get(mim.getPath());
			mim.setElpsesStatic(staticElps);
		}

		// 2 load drawn ellipse into the table
		coordListTH.fillRightTable(mim.getStaticElpsesStrings(), mim.getElpsesStrings());
	}

	void updateImagePanelSize(MyImage mim) {
		if (defaultScrollW == -1)
			defaultScrollW = scrollPanel.getWidth();
		if (defaultScrollH == -1)
			defaultScrollH = scrollPanel.getHeight();
		int imw = mim.w();
		int imh = mim.h();

		imagePanel.setCurrentImage(mim);
		setImgPanelSize(imw, imh);
		imagePanel.repaint();
	}

	void loadImageList() {
		JFileChooser fc = new JFileChooser(recordedImgPath);
		// fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int res = fc.showOpenDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			recordedImgPath = f.getParent();
			if (!IOTool.isTextFile(f)) {
				// not a readable text file
				JOptionPane.showMessageDialog(this,
						String.format("\"%s\" \nis not a txt file.", f.getAbsolutePath()).toString(), "Not a txt file",
						JOptionPane.WARNING_MESSAGE);
			} else {
				// read
				ArrayList<String> imgList = IOTool.readText(f);
				if (imgList == null) {
					// read failed
					JOptionPane.showMessageDialog(this,
							String.format("\"%s\" \ncannot be properly read.", f.getAbsolutePath()).toString(),
							"Reading failed", JOptionPane.WARNING_MESSAGE);
				} else {

					pathImgPair = IOTool.filterImageList(imgList, this);
					if(pathImgPair.size() == 0){
						return;
					}
					fillImageNameTable();
					leftTableSelectedRow = 0;
					imgListTH.setSelectedRow(leftTableSelectedRow);
					freezeReadImageListBtn();

				}
			}

		}
	}

	void fillImageNameTable() {
		imgListTH.fillLeftRows(pathImgPair.keySet());
	}

	void testTable() {
		System.out.println(imgListTH.getTable().hashCode() == imgNameTable.hashCode());
	}

	void loadAnnotList() {
		if (pathImgPair == null) {
			// no images loaded, abort
			return;
		}
		if(annotContentPrefix == null){
			String s = JOptionPane.showInputDialog(this, "Please set a prefix string for your annotation image path.\n(leave empty or cancel if not necessary)", "Prefix", JOptionPane.QUESTION_MESSAGE);
			annotContentPrefix = s==null? "" : s;
			
			s = JOptionPane.showInputDialog(this, "Please set a suffix(usually is image format extention) for your annotation image path.\n(leave empty or cancel if not necessary)", "Suffix", JOptionPane.QUESTION_MESSAGE);
			annotContentSuffix = s==null? "" : s;
			
			
			
		}
		JFileChooser fc = new JFileChooser(recordedAnnotPath);
		// fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int res = fc.showOpenDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			recordedAnnotPath = f.getParent();
			if (!IOTool.isTextFile(f)) {
				// not a readable text file
				JOptionPane.showMessageDialog(this,
						String.format("\"%s\" \nis not a txt file.", f.getAbsolutePath()).toString(), "Not a txt file",
						JOptionPane.WARNING_MESSAGE);
				annotContentPrefix = annotContentSuffix = null;
			} else {
				// read
				boolean noError = true;
				String errMessage = "";
				try {
					pathStaticElpsesPair = IOTool.readAnnotationFile(f, annotContentPrefix, annotContentSuffix);
				} catch (Exception e) {
					pathStaticElpsesPair = null;
					noError = false;
					errMessage = e.getMessage();
				}

				if (noError == false) {
					annotContentPrefix = annotContentSuffix = null;
					// parse failed
					JOptionPane
							.showMessageDialog(this,
									String.format("Failed to parse the file: \"%s\"\n\n" + "Error:\n%s",
											f.getAbsolutePath(), errMessage),
									"Parsing failed", JOptionPane.ERROR_MESSAGE);
				} else {
					// // parse successes, reset
					//
					// // put these annotations to mImg.ellispe_static
					for (String p : pathImgPair.keySet()) {
						ArrayList<Ellipse> elpses = pathStaticElpsesPair.get(p);
						if (elpses != null) {// annotation list contains this
												// image's annotations
							MyImage mim = UniversalTool.getMyImageFromPathImgPair(p, pathImgPair);
							mim.setElpsesStatic(elpses);
							// update mark nums
						} else {
							// System.err.println(String.format("%s not found in
							// [pathElpsesPair], abort\n", p));
						}
					}
					marksLoadAnnotationUpdate();
					imagePanel.repaint();
					freezeReadAnnotationBtn();
					// // reset
					// pathImgPair = null;
					// leftTableSelectedRow = -1;
					// imagePanel.reset();

				}
			}
		}else{
			annotContentPrefix = annotContentSuffix = null;
		}
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
		int imw = mim.w();
		int imh = mim.h();

		imagePanel.setCurrentImage(mim);
		setImgPanelSize(imw, imh);

		imagePanel.repaint();
	}

	public void marksLoadAnnotationUpdate() {
		if (pathStaticElpsesPair == null)
			return;
		for (String p : pathStaticElpsesPair.keySet()) {
			int idx = imgListTH.getRowIndexOfValue(p);
			MyImage mim = pathImgPair.get(p);
			if (idx != -1 && mim != null) {
				imgListTH.setValueAt(idx, 2, mim.getMarkNumString());
			}
		}
		setRightPanelCoords(null);
	}

	public void marksUpdatedAtSelected(MyImage mim) {
		
		if (leftTableSelectedRow == -1) {
			System.err.println("marking on non-loaded image?");
		}
		// if(mim == null){
		// String p = imgListTH.getBehindRowDataAt(leftTableSelectedRow);
		// mim = pathImgPair.get(p);
		// if(mim == null) System.err.println("bad at marksUpdatedAtSelected");
		// return;
		// }
		imgListTH.setValueAt(leftTableSelectedRow, 2, mim.getMarkNumString());
		setRightPanelCoords(mim);
	}
	public void freezeReadImageListBtn(){
		this.btnReadImageList.setEnabled(false);
	}
	public void freezeReadAnnotationBtn(){
		this.btnReadAnnotations.setEnabled(false);
	}
	public HashMap<String, MyImage> getPathImgPair(){
		return pathImgPair;
	}
	void outputAnnotations(){
		if(pathImgPair == null){
			return; //even no image loaded? abort
		}
		if(Flags.numNewEllipse <= 0){
			JOptionPane.showMessageDialog(this, "You did not mark any annotations, nothing to output.","Nothing to output",JOptionPane.WARNING_MESSAGE);
			return;
		}
		//open a chooser
		//make it a .txt file
		// ask: output only the new Ellipse or both new and static
		
		JFileChooser fc = new JFileChooser(recordedSavePath);
		// fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int res = fc.showOpenDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			recordedImgPath = f.getParent();
			IOTool.outputEllipse(pathImgPair, f.getAbsolutePath(),pathStaticElpsesPair != null,this);
			
		}
	}
}
