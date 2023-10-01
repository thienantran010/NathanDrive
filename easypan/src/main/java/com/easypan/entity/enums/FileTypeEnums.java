package com.easypan.entity.enums;

import org.apache.commons.lang3.ArrayUtils;

public enum FileTypeEnums {
    //1: video 2: audio 3: picture 4: pdf 5: word 6: excel 7: txt 8: code 9: zip 10: others
    VIDEO(FileCategoryEnums.VIDEO, 1, new String[]{".mp4", ".avi", ".rmvb", ".mkv", ".mov"}, "VIDEO"),
    MUSIC(FileCategoryEnums.MUSIC, 2, new String[]{".mp3", ".wav", ".wma", ".mp2", ".flac", ".midi", ".ra", ".ape", ".acc", ".cda"}, "AUDIO"),
    IMAGE(FileCategoryEnums.IMAGE, 3, new String[]{".jpeg", ".jpg", ".JPG", ".avif", ".png", ".gif", ".bmp", ".dds", ".psd", ".pdt", ".webp", ".xmp", ".svg", ".tiff"}, "IMAGE"),
    PDF(FileCategoryEnums.DOC, 4, new String[]{".pdf"}, "PDF"),
    WORD(FileCategoryEnums.DOC, 5, new String[]{".docx"}, "WORD"),
    EXCEL(FileCategoryEnums.DOC, 6, new String[]{".xlsx"}, "EXCEL"),
    TXT(FileCategoryEnums.DOC, 7, new String[]{".txt"}, "TXTDOC"),
    PROGRAM(FileCategoryEnums.OTHERS, 8, new String[]{".h", ".c", ".hpp", ".hxx", ".cpp", ".cc", ".c++", ".cxx", ".m", ".o", ".s", ".dll", ".cs", ".java", ".class", ".js", ".ts",
    ".css", ".scss", ".vue", ".jsx", ".sql", ".md", ".json", ".html", ".xml"}, "CODE"),
    ZIP(FileCategoryEnums.OTHERS, 9, new String[]{"rar", ".zip", ".7z", ".cab", ".arj", ".lzh", ".tar", ".gz", ".ace", ".uue", ".bz", ".jar", ".iso", "mpq"}, "ZIP"),
    OTHERS(FileCategoryEnums.OTHERS, 10, new String[]{}, "OTHERS");

    private FileCategoryEnums category;
    private Integer type;
    private String[] suffixs;
    private String desc;

    FileTypeEnums(FileCategoryEnums category, Integer type, String[] suffixs, String desc){
        this.category = category;
        this.type = type;
        this.suffixs = suffixs;
        this.desc = desc;
    }

    public static FileTypeEnums getFileTypeBySuffix(String suffix) {
        for (FileTypeEnums item : FileTypeEnums.values()){
            if (ArrayUtils.contains(item.getSuffixs(), suffix)){
                return item;
            }
        }
        return FileTypeEnums.OTHERS;
    }

    public static FileTypeEnums getByType(Integer type) {
        for (FileTypeEnums item : FileTypeEnums.values()){
            if (item.getType().equals(type)){
                return item;
            }
        }
        return null;
    }

    public String[] getSuffixs() { return suffixs; }
    public FileCategoryEnums getCategory() { return category; }
    public Integer getType() { return type; }
    public String getDesc() { return desc; }
}
























