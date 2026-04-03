package it.mapsgroup.gzoom.mybatis.dto;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ContentExDataResource extends Content {
    private DataResource dataResource;

    public DataResource getDataResource() {
        return dataResource;
    }

    public void setDataResource(DataResource dataResource) {
        this.dataResource = dataResource;
    }

    public String getImageBase64() {
        String encodedFile = "";
        File file = new File(dataResource.getObjectInfo());
        if(file.exists()) {
            try {
                byte[] fileByte = FileUtils.readFileToByteArray(file);
                encodedFile = Base64.getEncoder().encodeToString(fileByte);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return encodedFile;
    }

}
