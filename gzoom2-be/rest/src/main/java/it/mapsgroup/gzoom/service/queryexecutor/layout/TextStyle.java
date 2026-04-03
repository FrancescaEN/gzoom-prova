package it.mapsgroup.gzoom.service.queryexecutor.layout;

public class TextStyle {
    private String horizontalAlign;
    private String verticalAlign;
    private String color;
    private Integer size;
    private Boolean bold;
    private Boolean italic;
    private Boolean underLine;
    private Boolean wrap;


    public Boolean getWrap() {
        return wrap;
    }

    public void setWrap(Boolean wrap) {
        this.wrap = wrap;
    }

    public Boolean getUnderLine() {
        return underLine;
    }

    public void setUnderLine(Boolean underLine) {
        this.underLine = underLine;
    }

    public Boolean getItalic() {
        return italic;
    }

    public void setItalic(Boolean italic) {
        this.italic = italic;
    }

    public Boolean getBold() {
        return bold;
    }

    public void setBold(Boolean bold) {
        this.bold = bold;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHorizontalAlign() {
        return horizontalAlign;
    }

    public void setHorizontalAlign(String horizontalAlign) {
        this.horizontalAlign = horizontalAlign;
    }

    public String getVerticalAlign() {
        return verticalAlign;
    }

    public void setVerticalAlign(String verticalAlign) {
        this.verticalAlign = verticalAlign;
    }
}
