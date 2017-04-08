package hk.microos.tools;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


public class TableHelper {
	private JTable table;
	public DefaultTableModel tm;
	private ArrayList<String> rowStringList;
	private int staticRowNum = 0;

	public TableHelper(JTable table) {
		this.table = table;
		this.tm = (DefaultTableModel) table.getModel();
		basicSettings();
	}

	private void basicSettings() {
		// disable editing
		this.table.setDefaultEditor(Object.class, null);
		// disable dragging column
		this.table.getTableHeader().setReorderingAllowed(false);
		// disable multi-row selction
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// this enable scroll panel scroll
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
	}

	public JTable getTable() {
		return table;
	}

	public void setColSize(int[] colSize) {
		if (table.getColumnCount() != colSize.length)
			System.err.println("Unmatched colSize array and colNum!");

		for (int i = 0; i < colSize.length; i++) {
			TableColumn tc = table.getColumnModel().getColumn(i);
			tc.setPreferredWidth(colSize[i]);
		}
	}

	@SuppressWarnings("serial")
	public void fillRightTable(ArrayList<String> staticCoords, ArrayList<String> coords) {
		clearAll();
		// set red color as selected color
		table.setSelectionBackground(Color.RED);
		// fill ellipses list
		// id, mja, min, angle, x, y
		int id = 1;
		rowStringList = new ArrayList<>();

		if (staticCoords != null) {
			rowStringList.addAll(staticCoords);
			staticRowNum = staticCoords.size();
			
			table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {

					final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
							column);
					if (!isSelected) {
						c.setBackground(row < staticRowNum ? Color.YELLOW : Color.WHITE);
					}

					return c;
				}
			});
			
		}
		
		if (coords != null) {
			rowStringList.addAll(coords);
		}
		for (String c : rowStringList) {//add onto right table
			c = String.format("%d,%s", id, c);
			String[] splitStr = c.split(",");
			tm.addRow(splitStr);
			id++;
		}

	}

	public void fillLeftRows(Set<String> list) {
		// list is path string
		clearAll();

		// fill image List
		// "id","Image name", "#Marks","Path prefix"
		int id = 1;
		rowStringList = new ArrayList<String>(list);
		for (String s : list) {
			String[] pn = UniversalTool.getPrefixAndName(s);
			String prefix = pn[0];
			String name = pn[1];
			String mark = "-";
			tm.addRow(new Object[] { id, name, mark, prefix });
			id++;
		}
	}

	public int getSelectedRowIndex() {
		return table.getSelectedRow();
	}

	public Object getValueAt(int row, int col) {
		return this.tm.getValueAt(row, col);
	}

	public String getBehindRowDataAt(int row) {
		return rowStringList.get(row);
	}

	public void setValueAt(int row, int col, Object v) {
		if (row > this.tm.getRowCount())
			this.tm.setRowCount(row + 1);
		tm.setValueAt(v, row, col);
	}

	public void clearAll() {
		for (int i = 0; i < tm.getRowCount(); i++) {
			tm.removeRow(i);
		}
		tm.setRowCount(0);
	}

	public void setSelectedRow(int row){
		if (row >= tm.getRowCount())
			return;
		this.table.getSelectionModel().setSelectionInterval(row, row);
	}
	public void rightPanelSetSelectedLine(int finalIndex){
		this.setSelectedRow(finalIndex);
		
	}
	public int getRowIndexOfValue(String v) {
		if (rowStringList == null)
			return -1;
		return rowStringList.indexOf(v);
	}
	public void deselect(){
		this.table.getSelectionModel().clearSelection();
	}
}
