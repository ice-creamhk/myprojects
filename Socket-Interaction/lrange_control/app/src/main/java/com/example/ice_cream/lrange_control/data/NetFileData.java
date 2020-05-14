package com.example.ice_cream.lrange_control.data;

public class NetFileData {
    private long fileSize = 0;// 文件长度应该long型数据，否则大于2GB的文件大小无法表达
    private String fileName = "$error";// 文件名称，不含目录信息,默认值用于表示文件出错
    private String filePath = ".\\";// 该文件对象所处的目录，默认值为当前相对目录
    private String fileSizeStr = "0";// 文件的大小，用字符串表示，能智能地选择B、KB、MB、GB来表达
    private boolean isDirectory = false;// true为文件夹，false为文件
    private String fileModifiedDate = "1970-01-01 00:00:00";// 文件最近修改日期，默认值为1970年基准时间
    private int fileType=0;

    public NetFileData(String fileName, String fileSizeStr, boolean isDirectory, String fileModifiedDate) {
        this.fileName = fileName;
        this.fileSizeStr = fileSizeStr;
        this.isDirectory = isDirectory;
        this.fileModifiedDate = fileModifiedDate;
    }
    public  NetFileData(String fileName,int fileType){
            this.fileName=fileName;
            this.isDirectory=true;
            this.fileType=fileType;
    }
    public NetFileData(String fileInfo, String filePath) {
        String[] list=fileInfo.split(">");
        this.fileName=list[0];
        this.fileModifiedDate=list[1];
        this.fileSize=Long.parseLong(list[2]);
        this.fileSizeStr=parseSize(this.fileSize);
        this.fileType=Integer.valueOf(list[3]);
        if(list[3].equals("1")||list[3].equals("2")) {
            this.isDirectory=true;
            if(list[3].equals("2")){
                this.fileType=2;
            }
        }
        else
            this.isDirectory=false;
        this.filePath=filePath;

    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    private  String parseSize(long fileSize){
        String sizestr="";
        double K=1024.0f;
        double M=K*1024.0f;
        double G=M*1024.0f;

        if(fileSize>G){
            return  String.format("%.2fGB",fileSize/G);
        }
        if(fileSize>M){
            return  String.format("%.2fMB",fileSize/M);
        }
        if(fileSize>K){
            return  String.format("%.2fKB",fileSize/K);
        }
        if(fileSize>0){
            return  String.format("%dB",fileSize);
        }
        return  sizestr;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSizeStr() {
        return fileSizeStr;
    }

    public void setFileSizeStr(String fileSizeStr) {
        this.fileSizeStr = fileSizeStr;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public String getFileModifiedDate() {
        return fileModifiedDate;
    }

    public void setFileModifiedDate(String fileModifiedDate) {
        this.fileModifiedDate = fileModifiedDate;
    }
}
