package util;

import compilador.Token;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ParsinTableModel extends AbstractTableModel {
    private List<Token> tokens;
    private String[] columns = new String[]{
            "Código","Palavra"};

    /** Creates a new instance of DevmediaTableModel */
    public ParsinTableModel(List<Token> tokens) {
        this.tokens = tokens;
    }

    public ParsinTableModel(){
        this.tokens = new ArrayList<Token>();
    }

    public int getRowCount() {
        return tokens.size();
    }

    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int columnIndex){
        return columns[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }


    public void setValueAt(Token aValue, int rowIndex) {
        Token token = tokens.get(rowIndex);

        token.setType(aValue.getType());
        token.setText(aValue.getText());
        token.setLine(aValue.getLine());

        fireTableCellUpdated(rowIndex, 0);
        fireTableCellUpdated(rowIndex, 1);
        fireTableCellUpdated(rowIndex, 2);

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Token token = tokens.get(rowIndex);

        switch (columnIndex) {
            case 0:
                token.setType(Integer.parseInt( aValue.toString()));
            case 1:
                token.setText(aValue.toString());

            default:
                System.err.println("Índice da coluna inválido");
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Token selectedToken = tokens.get(rowIndex);
        String valueObject = null;
        switch(columnIndex){
            case 0: valueObject = String.valueOf(selectedToken.getType()); break;
            case 1: valueObject = selectedToken.getText(); break;
            case 2: valueObject = String.valueOf(selectedToken.getLine()); break;
            default: System.err.println("Índice inválido para propriedade do bean Usuario.class");
        }

        return valueObject;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Token getToken(int lineIndex) {
        return tokens.get(lineIndex);
    }

    public void addToken(Token u) {
        tokens.add(u);

        int lastIndex = getRowCount() - 1;

        fireTableRowsInserted(lastIndex, lastIndex);
    }


    public void removeUser(int lineIndex) {
        tokens.remove(lineIndex);

        fireTableRowsDeleted(lineIndex, lineIndex);
    }


    public void addUserList(List<Token> newUsers) {

        int oldSize = getRowCount();
        tokens.addAll(newUsers);
        fireTableRowsInserted(oldSize, getRowCount() - 1);
    }

    public void wipe() {
        tokens.clear();
        fireTableDataChanged();
    }

    public boolean isEmpty() {
        return tokens.isEmpty();
    }

}