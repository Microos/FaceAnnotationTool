package hk.microos.tools;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class TableHelper {
	private JTable table;
	public DefaultTableModel tm;
	
	public TableHelper(JTable table){
		this.table = table;
		this.tm = (DefaultTableModel)table.getModel();
		basicSettings();
	}
	private void basicSettings(){
		//disable editing
		this.table.setDefaultEditor(Object.class, null);
		//disable dragging column
		this.table.getTableHeader().setReorderingAllowed(false);
		//disable multi-row selction
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//this enable scroll panel scroll
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
	public JTable getTable(){
		return table;
	}
	public void setColSize(int [] colSize){
		if(table.getColumnCount() != colSize.length)
			System.err.println("Unmatched colSize array and colNum!");
		
		for(int i=0;i<colSize.length;i++){
			TableColumn tc = table.getColumnModel().getColumn(i);
			tc.setPreferredWidth(colSize[i]);
		}
		
	}
	

}
