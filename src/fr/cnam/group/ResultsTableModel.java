package fr.cnam.group;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class ResultsTableModel extends AbstractTableModel {
    private ArrayList<String> columnsNames = new ArrayList<String>();
    private ArrayList<String> columnsTypes = new ArrayList<String>();

    private ArrayList< ArrayList<Object> > values = new ArrayList<ArrayList<Object>>();

    public ResultsTableModel( ResultSet resultSet ) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnCounts = rsmd.getColumnCount();
        for( int i=1; i<= columnCounts; i++ ) {
            columnsNames.add( rsmd.getColumnName( i ) );
            System.out.println(rsmd.getColumnName(i));
            columnsTypes.add( rsmd.getColumnClassName( i ) );
        }
        while( resultSet.next() ) {
            ArrayList<Object> line = new ArrayList<Object>();
            for( int i=1; i<= columnCounts; i++ ) {
                line.add( resultSet.getObject( i ) );
            }
            values.add( line );
        }
    }

    @Override public Class<?> getColumnClass( int column ) {
        String type = this.columnsTypes.get( column );
        try {
            return Class.forName( type );
        } catch( Exception e ) {
            return String.class;
        }
    }

    @Override public String getColumnName(int i) {
        return columnsNames.get( i );
    }

    @Override public int getColumnCount() {
        return columnsNames.size();
    }

    @Override public int getRowCount() {
        return values.size();
    }

    @Override public Object getValueAt( int line, int column ) {
        return values.get( line ).get( column );
    }
}
