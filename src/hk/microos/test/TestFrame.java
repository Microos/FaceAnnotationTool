package hk.microos.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import hk.microos.data.Ellipse;
import hk.microos.data.Point_;
import hk.microos.tools.UniversalTool;

@SuppressWarnings("serial")
public class TestFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestFrame frame = new TestFrame();
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
	public TestFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(new JComponent() {
            public void paintComponent(Graphics g) {
            	Graphics2D g2d = (Graphics2D) g;
            	for(int i=0;i<200;i++){
            		g2d.fill(UniversalTool.getPointOval(0, 10*i, 5));
            		g2d.fill(UniversalTool.getPointOval(10*i, 0, 5));
            	}
            	
            	Ellipse e = UniversalTool.getTestEllipse(10);
            	Ellipse2D.Double ed = e.getErectedEllipse2D();
            		
        			//save old transform
        			AffineTransform old = g2d.getTransform();
        			for (Point_ p : e.getKeyPoints()){
        				g2d.fill(UniversalTool.getPointOval(p, 5));
        			}
//        			for(int i=0;i<12;i++){
//        				g2d.setTransform(old);
//        				trf.rotate(Math.toRadians(30*i),e.x,e.y);
//        				g2d.setTransform(trf);
//        				g2d.draw(ed);
//        			}
        			g2d.rotate(e.angle,e.x,e.y);
        			//draw elps
        			g2d.setColor(Color.GREEN);
        			
        			g2d.draw(ed);
        			//draw pts
        			g2d.setColor(Color.RED);
        			
        			//revert transform
        			g2d.setTransform(old);
//                g2d.setStroke(new BasicStroke(10));
//                g2d.draw(new Line2D.Float(30, 20, 80, 90));
            }
        });
		contentPane.repaint();
	}

}
