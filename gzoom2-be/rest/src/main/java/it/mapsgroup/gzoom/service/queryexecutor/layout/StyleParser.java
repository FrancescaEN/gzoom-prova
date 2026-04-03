package it.mapsgroup.gzoom.service.queryexecutor.layout;
import org.slf4j.Logger;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class StyleParser {
    private static final Logger LOG = getLogger(StyleParser.class);
    public static String ROW_FORMAT_PARAM_NAME = "PARAMS";
    private static final String BREAK_ROW_STYLE_PREFIX_NAME = "BreakRow";
    private static final String BREAK_ROW_STYLE_COLUMN_NAME = "ColumnName";

    public StyleParser() {}

    public List<ColumnStyle> parseColumnStyle(String style, ResultSetMetaData rsmd) throws SQLException,NumberFormatException {
        if (LOG.isDebugEnabled()) {
            LOG.info("Start parsing column style");
        }
        List<ColumnStyle> ret = ret = new ArrayList<>();
        int columnCount = rsmd.getColumnCount();
        for (int i = 1; i <= columnCount; i++ ) {
            String name = rsmd.getColumnName(i);
            ColumnStyle cs = new ColumnStyle(i);
            cs.setColName(name);
            ret.add(cs);
        }
        if(style != null && style.length() > 0){
            String[] colsStyle = getColsStyle(style);
            if(colsStyle.length > 0){
                for(String colStyleName : colsStyle){
                    String styleName = getColStyleName(colStyleName);
                    if(styleName.length()> 0){
                        String[] colStyleValues =  getColStyleValues(colStyleName);
                        if(colStyleValues != null && colStyleValues.length > 0){
                            int n = 0;
                            for(String styleValue : colStyleValues){
                                if(styleValue.length() > 0){
                                    ColumnStyle columnStyle = ret.get(n);
                                    setStyle(styleName,"", styleValue,columnStyle);
                                    columnStyle.setHasStyle(true);
                                }
                                n++;
                            }
                        }
                    }
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.info("End parsing column style");
        }
        return ret;
    }


    public void parseRowStyle(int rowCount, String style, List<ColumnStyle> columnStyleList) throws NumberFormatException {
        if (LOG.isDebugEnabled()) {
            LOG.info("Start parsing row style");
        }
        String[] colsStyle = getColsStyle(style);
        if(colsStyle.length > 0){
            for(String colStyleName : colsStyle){
                String styleName = getColStyleName(colStyleName);
                if(styleName.length()> 0){
                    String[] colStyleValues = getColStyleValues(colStyleName);
                    if(colStyleValues != null && colStyleValues.length > 0){
                        int n = 0;
                        for(String styleValue : colStyleValues){
                            if(styleValue.length() > 0){
                                ColumnStyle cs;
                                if(columnStyleList != null && !columnStyleList.isEmpty()){
                                    cs = columnStyleList.get(n);
                                }
                                else{
                                    cs = new ColumnStyle(n);
                                    columnStyleList.add(cs);
                                }
                                CellStyle cellstyle = cs.getCellStyleListByRowNum(rowCount);
                                if(cellstyle == null){
                                    cellstyle = new CellStyle(cs.getColName(),cs.getColNum(),rowCount);
                                    cs.getCellStyleList().add(cellstyle);
                                }
                                setStyle(styleName,"", styleValue,cellstyle);
                            }
                            n++;
                        }
                    }
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.info("End parsing row style");
        }
    }

    public ColumnStyle parseBreakRowColumnStyle(String style){
        if (LOG.isDebugEnabled()) {
            LOG.info("Start parsing BreakRow column style");
        }
        ColumnStyle columnStyle = null;
        if(style != null && style.length() > 0){
            columnStyle = new ColumnStyle(0);
            columnStyle.setMerged(true);
            String[] colsStyle = getColsStyle(style);
            if(colsStyle.length > 0){
                for(String colStyleName : colsStyle){
                    String styleName = getColStyleName(colStyleName);
                    if(styleName.length()> 0){
                        String[] colStyleValues = getColStyleValues(colStyleName);
                        if(colStyleValues != null && colStyleValues.length > 0){
                            for(String styleValue : colStyleValues){
                                columnStyle.setHasStyle(true);
                                setStyle(styleName,BREAK_ROW_STYLE_PREFIX_NAME, styleValue,columnStyle);
                            }
                        }
                    }
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.info("End parsing BreakRow column style");
        }
        return columnStyle;
    }


    private void setStyle(String styleName,String styleNamePrefix, String styleValue,Style style){
        if(styleValue.length() > 0){
            if(styleName.equalsIgnoreCase(styleNamePrefix + BREAK_ROW_STYLE_COLUMN_NAME)){
                style.setColName(styleValue);
            }
            if(styleName.equalsIgnoreCase(styleNamePrefix + StyleEnum.BgColor.name())){
                style.setBgColor(styleValue);
            }
            if(styleName.equalsIgnoreCase(styleNamePrefix + StyleEnum.Width.name())){
                try {
                    Integer value = Integer.parseInt(styleValue);
                    style.setWidth(value);
                }
                catch (Exception e)
                {
                    throw new NumberFormatException(e.getMessage());
                }
            }
            if(styleName.equalsIgnoreCase(styleNamePrefix + StyleEnum.FontColor.name())){
                style.getTextStyle().setColor(styleValue);
            }
            if(styleName.equalsIgnoreCase(styleNamePrefix + StyleEnum.FontSize.name())){
                try {
                    Integer value = Integer.parseInt(styleValue);
                    style.getTextStyle().setSize(value);
                }
                catch (Exception e)
                {
                    throw new NumberFormatException(e.getMessage());
                }
            }
            if(styleName.equalsIgnoreCase(styleNamePrefix + StyleEnum.TextBold.name())){
                Boolean value = Boolean.parseBoolean(styleValue);
                style.getTextStyle().setBold(value);
            }
            if(styleName.equalsIgnoreCase(styleNamePrefix + StyleEnum.TextHorizontalAlignment.name())){
                style.getTextStyle().setHorizontalAlign(styleValue);
            }
            if(styleName.equalsIgnoreCase(styleNamePrefix + StyleEnum.TextVerticalAlignment.name())){
                style.getTextStyle().setVerticalAlign(styleValue);
            }
            if(styleName.equalsIgnoreCase(styleNamePrefix + StyleEnum.TextItalic.name())){
                Boolean value = Boolean.parseBoolean(styleValue);
                style.getTextStyle().setItalic(value);
            }
            if(styleName.equalsIgnoreCase(styleNamePrefix + StyleEnum.TextUnderline.name())){
                Boolean value = Boolean.parseBoolean(styleValue);
                style.getTextStyle().setUnderLine(value);
            }
            if(styleName.equalsIgnoreCase(styleNamePrefix + StyleEnum.CellsBorder.name())){
                style.setBorderStyle(styleValue);
            }
            if(styleName.equalsIgnoreCase(styleNamePrefix + StyleEnum.WrapText.name())){
                Boolean value = Boolean.parseBoolean(styleValue);
                style.getTextStyle().setWrap(value);
            }
            if(styleName.equalsIgnoreCase(styleNamePrefix + StyleEnum.DataFormat.name())){
                style.setDataFormat(styleValue);
            }
        }
    }


    private String[] getColsStyle(String style){
        return style.split("\\|");
    }

    private String[] getColStyleValues(String colStyleName){
        if(colStyleName.contains("=")){
            return colStyleName.substring(colStyleName.indexOf("=")+1).replaceAll("\t","").trim().split(";");
        }
        else{
            return null;
        }
    }

    private String getColStyleName(String colStyleName){
        if(colStyleName.contains("=")){
            return colStyleName.substring(0,colStyleName.indexOf("=")).replaceAll("\t","").trim();
        }
        else{
            return "";
        }
    }
}
