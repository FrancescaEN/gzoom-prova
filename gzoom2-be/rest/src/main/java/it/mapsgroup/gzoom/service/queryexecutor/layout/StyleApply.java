package it.mapsgroup.gzoom.service.queryexecutor.layout;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.slf4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;

import static org.slf4j.LoggerFactory.getLogger;

public class StyleApply {
    private static final Logger LOG = getLogger(StyleApply.class);
    private static final int rowHeightMultiplierCoefficient = 20;
    private static final int colWidthMultiplierCoefficient = 50;
    private final HashMap<String,XSSFCellStyle> cellStylePool;
//    private long cellStyleCounter = 0;


    public StyleApply(){
        this.cellStylePool = new HashMap<>();
    }

    public void setCellStyle(Sheet sheet, Workbook workbook, ColumnStyle columnStyleFormat, Cell cell, int rowCount, boolean hasRowFormatParam) {
        if((columnStyleFormat != null && columnStyleFormat.isHasStyle()) || hasRowFormatParam){
            /*
                I cellStyle devono essere riutilizzati evitare il problema del limite dei 64.000
                https://stackoverflow.com/questions/70116469/apache-poi-what-is-impact-of-the-maximum-number-of-cell-styles-was-exceeded
             */
            XSSFCellStyle cellStyle = getFromCellStylePool(columnStyleFormat,cell,rowCount);
            if(cellStyle == null){
                cellStyle = (XSSFCellStyle) workbook.createCellStyle();
                Font font = workbook.createFont();
                cellStyle.setFont(font);
//                cellStyleCounter++;
//                LOG.info("StyleApply : " + String.valueOf(cellStyleCounter));
            }
            //applico formattazione colonne
            setCellStyleByColumn(sheet,workbook,columnStyleFormat,cell,cellStyle);
            //applico formattazione righe (sovrascrive eventuale formattazione colonne)
            setCellStyleByRow(sheet,workbook,columnStyleFormat,cell,rowCount,cellStyle);
            cell.setCellStyle(cellStyle);
            addToCellStylePool(columnStyleFormat,cellStyle,cell,rowCount);
        }
    }


    private XSSFCellStyle getFromCellStylePool(ColumnStyle columnStyleFormat,Cell cell, int rowCount){
        String key = null;
        if(columnStyleFormat.getCellStyleList() != null && !columnStyleFormat.getCellStyleList().isEmpty()){
            if(cell.getRow().getRowNum() == rowCount){
                CellStyle cellStyleFormat = columnStyleFormat.getCellStyleListByRowNum(rowCount);
                if(cellStyleFormat != null){
                    key = cellStyleFormat.toString();
                }
            }
        }
        else{
            key = columnStyleFormat.toString();
        }
        XSSFCellStyle cellStyle = cellStylePool.get(key);
        return cellStyle;
    }

    private void addToCellStylePool(ColumnStyle columnStyleFormat,XSSFCellStyle cellStyle,Cell cell, int rowCount){
        if(columnStyleFormat.getCellStyleList() != null && !columnStyleFormat.getCellStyleList().isEmpty()){
            if(cell.getRow().getRowNum() == rowCount){
                CellStyle cellStyleFormat = columnStyleFormat.getCellStyleListByRowNum(rowCount);
                if(cellStyleFormat != null){
                    this.cellStylePool.put(cellStyleFormat.toString(),cellStyle);
                }
            }
        }
        else{
            this.cellStylePool.put(columnStyleFormat.toString(),cellStyle);
        }
    }


        private void setCellStyleByColumn(Sheet sheet, Workbook workbook, ColumnStyle columnStyleFormat, Cell cell,XSSFCellStyle cellStyle){
            if(columnStyleFormat.isHasStyle()){
                setObjStyle(sheet,workbook,cell,cellStyle,columnStyleFormat);
                //cellStyleCounter++;
                //LOG.info("ccs by StyleApply : " + cellStyleCounter);
            }
        }


        private void setCellStyleByRow(Sheet sheet, Workbook workbook, ColumnStyle cs, Cell cell, int rowCount,XSSFCellStyle cellStyle){
        if(cs.getCellStyleList() != null && !cs.getCellStyleList().isEmpty()){
            if(cell.getRow().getRowNum() == rowCount){
                CellStyle cellStyleFormat = cs.getCellStyleListByRowNum(rowCount);
                if(cellStyleFormat != null){
                    setObjStyle(sheet,workbook,cell,cellStyle,cellStyleFormat);
                }
            }
        }
    }


    private void setObjStyle(Sheet sheet, Workbook workbook, Cell cell, XSSFCellStyle cellStyle, Style styleFormat){
        if(styleFormat.getBgColor() != null)
        {
            setBgColor(cellStyle,styleFormat);
        }
        if(styleFormat.getTextStyle().getColor() != null)
        {
            setFontColor(cellStyle.getFont(),styleFormat);
        }
        if(styleFormat.getTextStyle().getSize() != null)
        {
            setFontSize(cellStyle.getFont(),styleFormat);
        }
        if(styleFormat.getTextStyle().getBold() != null)
        {
            cellStyle.getFont().setBold(styleFormat.getTextStyle().getBold());
        }
        if(styleFormat.getTextStyle().getHorizontalAlign() != null)
        {
            setHorizontalAlign(cellStyle,styleFormat);
        }
        if(styleFormat.getTextStyle().getVerticalAlign() != null)
        {
            setVerticalAlign(cellStyle,styleFormat);
        }
        if(styleFormat.getTextStyle().getItalic() != null)
        {
            cellStyle.getFont().setItalic(styleFormat.getTextStyle().getItalic());
        }
        if(styleFormat.getTextStyle().getUnderLine() != null && styleFormat.getTextStyle().getUnderLine())
        {
            cellStyle.getFont().setUnderline(HSSFFont.U_SINGLE);
        }
        if(styleFormat.getBorderStyle() != null)
        {
            setBorderStyle(sheet,cellStyle,styleFormat);
        }
        if(styleFormat.getTextStyle().getWrap() != null)
        {
            cellStyle.setWrapText(styleFormat.getTextStyle().getWrap());
        }
        if(styleFormat.getDataFormat() != null)
        {
            setDataFormat(sheet,workbook,cellStyle,styleFormat,cell,cellStyle.getFont());
        }
        if(styleFormat.getWidth() != null)
        {
            setWidth(cell,styleFormat);
        }
    }

    private void setBgColor(XSSFCellStyle cellStyle,Style StyleFormat){
        try{byte[] rgbB = Hex.decodeHex(StyleFormat.getBgColor().toCharArray());
            XSSFColor color = new XSSFColor(rgbB, null);
            cellStyle.setFillForegroundColor(color);
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        catch (Exception e) {
            LOG.error("Error decoding color : " + StyleFormat.getBgColor());
            e.printStackTrace();
        }
    }



    private void setFontColor(Font font,Style StyleFormat){
        String color = StyleFormat.getTextStyle().getColor();
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFPalette palette = hwb.getCustomPalette();
        int red = Integer.valueOf(color.substring(0, 2), 16);
        int green = Integer.valueOf(color.substring(2, 4), 16);
        int blue =  Integer.valueOf(color.substring(4, 6), 16);
        HSSFColor similarColor = palette.findSimilarColor(red, green, blue);
        font.setColor(similarColor.getIndex());
    }

    private void setFontSize(Font font,Style StyleFormat){
        short value = Integer.valueOf(StyleFormat.getTextStyle().getSize() * 20).shortValue();
        font.setFontHeight(value);
    }


    private void setHorizontalAlign(XSSFCellStyle cellStyle,Style StyleFormat){
        if(StyleFormat.getTextStyle().getHorizontalAlign().equalsIgnoreCase(HorizontalAlign.L.name())){
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
        }
        if(StyleFormat.getTextStyle().getHorizontalAlign().equalsIgnoreCase(HorizontalAlign.R.name())){
            cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        }
        if(StyleFormat.getTextStyle().getHorizontalAlign().equalsIgnoreCase(HorizontalAlign.C.name())){
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
        }
    }

    private void setVerticalAlign(XSSFCellStyle cellStyle,Style StyleFormat){
        if(StyleFormat.getTextStyle().getVerticalAlign().equalsIgnoreCase(VerticalAlign.T.name())){
            cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        }
        if(StyleFormat.getTextStyle().getVerticalAlign().equalsIgnoreCase(VerticalAlign.B.name())){
            cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
        }
        if(StyleFormat.getTextStyle().getVerticalAlign().equalsIgnoreCase(VerticalAlign.C.name())){
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        }
    }

    private void setBorderStyle(Sheet sheet,XSSFCellStyle cellStyle,Style StyleFormat){
        if(StyleFormat.isMerged()){
            if(StyleFormat.getBorderStyle().equalsIgnoreCase(BorderStyle.THIN.name())){
                RegionUtil.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN, StyleFormat.getCellRangeAddress(), sheet);
                RegionUtil.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN, StyleFormat.getCellRangeAddress(), sheet);
                RegionUtil.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN, StyleFormat.getCellRangeAddress(), sheet);
                RegionUtil.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN, StyleFormat.getCellRangeAddress(), sheet);
            }
            if(StyleFormat.getBorderStyle().equalsIgnoreCase(BorderStyle.MEDIUM.name())){
                RegionUtil.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM, StyleFormat.getCellRangeAddress(), sheet);
                RegionUtil.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM, StyleFormat.getCellRangeAddress(), sheet);
                RegionUtil.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM, StyleFormat.getCellRangeAddress(), sheet);
                RegionUtil.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM, StyleFormat.getCellRangeAddress(), sheet);
            }
            if(StyleFormat.getBorderStyle().equalsIgnoreCase(BorderStyle.DOUBLE.name())){
                RegionUtil.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.DOUBLE, StyleFormat.getCellRangeAddress(), sheet);
                RegionUtil.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.DOUBLE, StyleFormat.getCellRangeAddress(), sheet);
                RegionUtil.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.DOUBLE, StyleFormat.getCellRangeAddress(), sheet);
                RegionUtil.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.DOUBLE, StyleFormat.getCellRangeAddress(), sheet);
            }
        }
        else{
            if(StyleFormat.getBorderStyle().equalsIgnoreCase(BorderStyle.THIN.name())){
                cellStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
                cellStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
                cellStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
                cellStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            }

            if(StyleFormat.getBorderStyle().equalsIgnoreCase(BorderStyle.MEDIUM.name())){
                cellStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);
                cellStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);
                cellStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);
                cellStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);
            }

            if(StyleFormat.getBorderStyle().equalsIgnoreCase(BorderStyle.DOUBLE.name())){
                cellStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.DOUBLE);
                cellStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.DOUBLE);
                cellStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.DOUBLE);
                cellStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.DOUBLE);
            }
        }
    }


    private void setDataFormat(Sheet sheet, Workbook workbook, XSSFCellStyle cellStyle, Style StyleFormat, Cell cell, Font font){
        org.apache.poi.ss.usermodel.DataFormat fmt = workbook.createDataFormat();

        if(StyleFormat.getDataFormat().equalsIgnoreCase(DataFormat.A.name())){
            cellStyle.setDataFormat(fmt.getFormat("General"));
        }
        if(StyleFormat.getDataFormat().equalsIgnoreCase(DataFormat.D.name())){
            cellStyle.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
        }
        if(StyleFormat.getDataFormat().equalsIgnoreCase(DataFormat.I.name())){
            cellStyle.setDataFormat(fmt.getFormat("###,###"));
        }
        if(StyleFormat.getDataFormat().equalsIgnoreCase(DataFormat.N.name())){
            try {
                BigDecimal bd = BigDecimal.valueOf(cell.getNumericCellValue());
                int numberOfDecimal = bd.scale();
                StringBuilder numberSigns = new StringBuilder();
                numberSigns.append("#".repeat(Math.max(0, numberOfDecimal)));
                if(numberOfDecimal > 1){
                    cellStyle.setDataFormat(fmt.getFormat("###,###." + numberSigns));
                }
                else{
                    cellStyle.setDataFormat(fmt.getFormat("###,###"));
                }
            }catch (Exception e) {
                LOG.error("invalid value to format as Decimal : " + cell.getStringCellValue());
            }
        }
        if(StyleFormat.getDataFormat().equalsIgnoreCase(DataFormat.N1.name())){
            cellStyle.setDataFormat(fmt.getFormat("###,###.#"));
        }
        if(StyleFormat.getDataFormat().equalsIgnoreCase(DataFormat.N2.name())){
            cellStyle.setDataFormat(fmt.getFormat("###,###.##"));
            try {
            }catch (Exception e) {
                LOG.error("invalid value to format as Decimal : " + cell.getStringCellValue());
            }
        }
        if(StyleFormat.getDataFormat().equalsIgnoreCase(DataFormat.T.name())){
            cellStyle.setDataFormat(fmt.getFormat("dd/mm/yyyy hh:mm:ss"));
        }
        if(StyleFormat.getDataFormat().equalsIgnoreCase(DataFormat.L.name())){
            org.apache.commons.validator.routines.UrlValidator urlValidator = new org.apache.commons.validator.routines.UrlValidator();
            if( urlValidator.isValid(cell.getStringCellValue())){
                CreationHelper createHelper = workbook.getCreationHelper();
                XSSFHyperlink link = (XSSFHyperlink)createHelper.createHyperlink(HyperlinkType.URL);
                link.setAddress(cell.getStringCellValue());
                cell.setHyperlink(link);
                font.setUnderline(XSSFFont.U_SINGLE);
                font.setColor(IndexedColors.BLUE.index);
            }
            else{
                LOG.error("invalid value to format as URL : " + cell.getStringCellValue());
            }
        }
        if(StyleFormat.getDataFormat().equalsIgnoreCase(DataFormat.P16.name())){
            setCellPicture(sheet, workbook, cell, 16);
        }
        if(StyleFormat.getDataFormat().equalsIgnoreCase(DataFormat.P32.name())){
            setCellPicture(sheet, workbook, cell, 32);
        }
    }

    private void setWidth(Cell cell,Style StyleFormat){
        /*
        (width + 1) * 256 è l'approssimazione più vicina all'unità di misura utilizzata da excel (con uno scarto di 0,29) vedere link sotto per maggiori dettagli :
        https://poi.apache.org/apidocs/dev/org/apache/poi/ss/usermodel/Sheet.html#setColumnWidth-int-int-
         */
        cell.getSheet().setColumnWidth(cell.getColumnIndex(),(StyleFormat.getWidth()+1) * 256);
    }


        private void setCellPicture(Sheet sheet,Workbook workbook, Cell cell, int pictureSize){
        try {
            File file = new File(cell.getStringCellValue());
            if (file.exists()){
                InputStream inputStream = new FileInputStream(file);
                //Get the contents of an InputStream as a byte[].
                byte[] bytes = IOUtils.toByteArray(inputStream);
                //Adds a picture to the workbook
                int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
                //close the input stream
                inputStream.close();
                //Returns an object that handles instantiating concrete classes
                CreationHelper helper = workbook.getCreationHelper();
                //Creates the top-level drawing patriarch.
                Drawing drawing = sheet.createDrawingPatriarch();
                //Create an anchor that is attached to the worksheet
                ClientAnchor anchor = helper.createClientAnchor();
                //create an anchor with upper left cell _and_ bottom right cell
                anchor.setCol1(cell.getColumnIndex()); //Column B
                anchor.setRow1(cell.getRow().getRowNum()); //Row 3
                anchor.setCol2(cell.getColumnIndex() + 1); //Column C
                anchor.setRow2(cell.getRow().getRowNum() + 1); //Row 4
                //Creates a picture
                drawing.createPicture(anchor, pictureIdx);
                cell.setBlank();
                cell.getRow().setHeight(Integer.valueOf(pictureSize * rowHeightMultiplierCoefficient).shortValue());
                cell.getSheet().setColumnWidth(cell.getColumnIndex(), pictureSize * colWidthMultiplierCoefficient);
                }
            else{
                if (LOG.isInfoEnabled()) {
                    LOG.info("File not found : " + file.getAbsolutePath());
                }
            }
            }
                catch (Exception e) {
                    LOG.error("invalid value to format as Decimal : " + cell.getStringCellValue());
            }
        }
}
