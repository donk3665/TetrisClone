package SceneControllers;

public class SceneTransferData {
    String filename;
    String folderName;

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
    public String getFolderName() {
        return folderName;
    }
    public void setFilename(String filename){
        this.filename = filename;
    }
    public String getFilename() {
        return filename;
    }

}
