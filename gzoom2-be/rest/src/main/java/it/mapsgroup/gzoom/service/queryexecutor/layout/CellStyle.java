package it.mapsgroup.gzoom.service.queryexecutor.layout;

public class CellStyle extends ColumnStyle implements Style
{
    private long rowNum;
    private TextStyle textStyle;

    public CellStyle(String colName, int colNum, long rowNum) {
        super(colNum);
        super.setColName(colName);
        this.rowNum = rowNum;
        this.textStyle = new TextStyle();
    }

    @Override
    public TextStyle getTextStyle() {
        return textStyle;
    }

    @Override
    public void setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
    }

    public long getRowNum() {
        return rowNum;
    }

    public void setRowNum(long rowNum) {
        this.rowNum = rowNum;
    }
}
