package simulator;

import javax.swing.BorderFactory;  
import javax.swing.JLabel;  
import javax.swing.JTable;  
import javax.swing.border.Border;  
import javax.swing.table.TableCellRenderer;  
import java.awt.Color;  
import java.awt.Component;  
  
  
public class CustomTableCellRenderer extends JLabel implements TableCellRenderer  {  
  
    public CustomTableCellRenderer(Passenger[][] plane) {  
        setOpaque(false); //MUST do this for background to show up.  
    }  
  
    public Component getTableCellRendererComponent(  
                            JTable table, Object value,  
                            boolean isSelected, boolean hasFocus,  
                            int row, int column, Passenger plane[][]) {  
  
        //if(column == 4){  
  
            if (plane[row][column] != null)  
            {  
                setBackground(Color.BLUE);  
                //setForeground(Color.BLACK);  
            }  
            else  
            {  
                setBackground(Color.RED);  
                //setForeground(Color.BLACK);  
            }  
        //}  
        return this;  
    }

	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1,
			boolean arg2, boolean arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		return null;
	}  
}