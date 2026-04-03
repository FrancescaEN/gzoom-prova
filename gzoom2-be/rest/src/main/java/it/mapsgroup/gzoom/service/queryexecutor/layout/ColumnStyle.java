package it.mapsgroup.gzoom.service.queryexecutor.layout;
import org.apache.poi.ss.util.CellRangeAddress;
import java.util.ArrayList;
import java.util.List;

public class ColumnStyle implements Style{
    private String colName;
    private int colNum;
    private String borderStyle;
    private String bgColor;
    private Integer width;
    private TextStyle textStyle;
    private String dataFormat;
    private List<CellStyle> cellStyleList;
    private boolean isMerged;
    private CellRangeAddress cellRangeAddress;
    private boolean hasStyle;


    public boolean isHasStyle() {
        return hasStyle;
    }

    public void setHasStyle(boolean hasStyle) {
        this.hasStyle = hasStyle;
    }



    public CellRangeAddress getCellRangeAddress() {
        return cellRangeAddress;
    }

    public void setCellRangeAddress(CellRangeAddress cellRangeAddress) {
        this.cellRangeAddress = cellRangeAddress;
    }

    public boolean isMerged() {
        return isMerged;
    }

    public void setMerged(boolean merged) {
        isMerged = merged;
    }


    public List<CellStyle> getCellStyleList() {
        return cellStyleList;
    }

    public CellStyle getCellStyleListByRowNum(long rowNum) {
        if(this.cellStyleList != null && !this.cellStyleList.isEmpty()){
            for(CellStyle cs : cellStyleList){
                if(cs.getRowNum() == rowNum){
                    return cs;
                }
            }
        }
        return null;
    }


    public void setCellStyleList(List<CellStyle> cellStyleList) {
        this.cellStyleList = cellStyleList;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String getBorderStyle() {
        return borderStyle;
    }

    public void setBorderStyle(String borderStyle) {
        this.borderStyle = borderStyle;
    }




    public ColumnStyle(int num) {
        this.colNum = num;
        this.textStyle = new TextStyle();
        this.cellStyleList = new ArrayList<>();
        this.isMerged = false;
        this.hasStyle = false;
    }

    public int getColNum() {
        return colNum;
    }

    public void setColNum(int colNum) {
        this.colNum = colNum;
    }
    public String getBgColor() {
        return bgColor;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }


    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public TextStyle getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }


    @Override
    public String toString() {
        String ret = "";
        ret = this.getColName() + "-" + this.getColNum()+ "-";
        if(this.getWidth() != null){
            ret = ret  + this.getWidth();
        }
        if(this.getBgColor() != null){
            ret = ret  + this.getBgColor();
        }
        if(this.getTextStyle()!= null && this.getTextStyle().getColor() != null){
            ret = ret  + this.getTextStyle().getColor();
        }
        if(this.getTextStyle()!= null && this.getTextStyle().getSize() != null){
            ret = ret  + this.getTextStyle().getSize();
        }
        if(this.getTextStyle()!= null && this.getTextStyle().getBold() != null){
            ret = ret  + this.getTextStyle().getBold();
        }
        if(this.getTextStyle()!= null && this.getTextStyle().getHorizontalAlign() != null){
            ret = ret  + this.getTextStyle().getHorizontalAlign();
        }
        if(this.getTextStyle()!= null && this.getTextStyle().getVerticalAlign() != null){
            ret = ret  + this.getTextStyle().getVerticalAlign();
        }
        if(this.getTextStyle()!= null && this.getTextStyle().getItalic() != null){
            ret = ret  + this.getTextStyle().getItalic();
        }
        if(this.getTextStyle()!= null && this.getTextStyle().getUnderLine() != null){
            ret = ret  + this.getTextStyle().getUnderLine();
        }
        if(this.getBorderStyle()!= null){
            ret = ret  + this.getBorderStyle();
        }
        if(this.getTextStyle()!= null && this.getTextStyle().getWrap() != null){
            ret = ret  + this.getTextStyle().getWrap();
        }
        if(this.getDataFormat()!= null){
            ret = ret  + this.getDataFormat();
        }
        return ret;
    }
}
