package it.memelabs.smartnebula.commons;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author Andrea Fossi.
 */
public class ImageUtilsSanselanIT {
    @Test
    public void rotate() throws Exception {
        File file = new File("/Users/anfo/Documents/DCIM/100D7100/DSC_0010.JPG");
        BufferedImage image = ImageIO.read(file);
        Integer orientation = ImageUtilsSanselan.getOrientation(file);
        File dest = new File("/tmp/test.jpg");
        FileOutputStream os = new FileOutputStream(dest);
        image=ImageUtils.scale(image,2000,1200);
        ImageUtils.saveImageAsJPEG(image, os, 70);
        os.flush();
        os.close();
        ImageUtilsSanselan.writeOrientation(orientation, dest);
    }
}
