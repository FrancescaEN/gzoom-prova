export class ToolbarButton {
    /*------------------*/
    FONT?: boolean = true;
    SIZE?: boolean = true;
    /*------------------*/
    BOLD?: boolean = true;
    ITALIC?: boolean = true;
    UNDERLINE?: boolean = true;
    STRIKE?: boolean = true;
    /*------------------*/
    COLOR?: boolean = true;
    BACKGROUND?: boolean = true;
    /*------------------*/
    SCRIPT_SUB?: boolean = true;
    SCRIPT_SUPER?: boolean = true;
    /*------------------*/
    HEADER_ONE?: boolean = true;
    HEADER_TWO?: boolean = true;
    BLOCKQUOTE?: boolean = true;
    CODE_BLOCK?: boolean = true;
    /*------------------*/
    LIST_ORDERED?: boolean = true;
    LIST_BULLET?: boolean = true;
    INDENT_NEG?: boolean = true;
    INDENT_POS?: boolean = true;
    /*------------------*/
    DIRECTION?: boolean = true;
    ALIGN?: boolean = true;
    /*------------------*/
    LINK?: boolean = true;
    IMAGE?: boolean = true;
    VIDEO?: boolean = true;
    FORMULA?: boolean = true;
    /*------------------*/
    CLEAN?: boolean = true;

}

export enum ToolbarButtonType {
    FONT = "FONT",
    SIZE = "SIZE",
    /*------------------*/
    BOLD = "BOLD",
    ITALIC = "ITALIC",
    UNDERLINE = "UNDERLINE",
    STRIKE = "STRIKE",
    /*------------------*/
    COLOR = "COLOR",
    BACKGROUND = "BACKGROUND",
    /*------------------*/
    SCRIPT_SUB = "SCRIPT_SUB",
    SCRIPT_SUPER = "SCRIPT_SUPER",
    /*------------------*/
    HEADER_ONE = "HEADER_ONE",
    HEADER_TWO = "HEADER_TWO",
    BLOCKQUOTE = "BLOCKQUOTE",
    CODE_BLOCK = "CODE_BLOCK",
    /*------------------*/
    LIST_ORDERED = "LIST_ORDERED",
    LIST_BULLET = "LIST_BULLET",
    INDENT_NEG = "INDENT_NEG",
    INDENT_POS = "INDENT_POS",
    /*------------------*/
    DIRECTION = "DIRECTION",
    ALIGN = "ALIGN",
    /*------------------*/
    LINK = "LINK",
    IMAGE = "IMAGE",
    VIDEO = "VIDEO",
    FORMULA = "FORMULA",
    /*------------------*/
    CLEAN = "CLEAN",


}