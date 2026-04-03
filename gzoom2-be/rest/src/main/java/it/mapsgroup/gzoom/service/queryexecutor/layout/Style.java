package it.mapsgroup.gzoom.service.queryexecutor.layout;

import org.apache.poi.ss.util.CellRangeAddress;

interface Style {
    int getColNum();
    void setColNum(int colNum);
    String getDataFormat();
    void setDataFormat(String dataFormat);
    String getBorderStyle();
    void setBorderStyle(String borderStyle);
    String getBgColor();
    Integer getWidth();
    void setWidth(Integer width);
    void setBgColor(String bgColor);
    TextStyle getTextStyle();
    void setTextStyle(TextStyle textStyle);
    String getColName();
    void setColName(String colName);
    public boolean isMerged();
    public CellRangeAddress getCellRangeAddress();
    public void setCellRangeAddress(CellRangeAddress cellRangeAddress);
}
