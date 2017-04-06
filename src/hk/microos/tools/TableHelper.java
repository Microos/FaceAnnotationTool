package hk.microos.tools;

import java.util.ArrayList;
import java.util.Set;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class TableHelper {
	private JTable table;
	private DefaultTableModel tm;
	private ArrayList<String> pathData;
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
	public void fillRows(Set<String> list, boolean imgList){
		for(int i=0;i<tm.getRowCount();i++){
			tm.removeRow(i);
		}
		tm.setRowCount(0);
		
		
		if(imgList){
			//fill image List
			//"id","Image name", "#Marks","Path prefix"
			int id = 1;
			pathData = new ArrayList<String>(list);
			for(String s: list){
				String[] pn = UniversalTool.getPrefixAndName(s);
				String prefix = pn[0];
				String name = pn[1];
				String mark = "-";
				tm.addRow(new Object[]{id,name,mark,prefix});
				id++;
			}
			
		}else{
		}
	}
	public int getSelectedRowIndex(){
		return table.getSelectedRow();
	}
	public Object getValueAt(int row, int col){
		return  this.tm.getValueAt(row, col);
	}
	public String getPathAt(int row){
		return pathData.get(row);
	}
	public void setValueAt(int row, int col, Object v){
		tm.setValueAt(v, row, col);
	}

}
