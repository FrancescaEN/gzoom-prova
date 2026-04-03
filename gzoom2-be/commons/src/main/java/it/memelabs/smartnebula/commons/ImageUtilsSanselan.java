package it.memelabs.smartnebula.commons;

import org.apache.commons.io.FileUtils;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.constants.TiffTagConstants;
import org.apache.sanselan.formats.tiff.write.TiffOutputDirectory;
import org.apache.sanselan.formats.tiff.write.TiffOutputField;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;
import org.slf4j.Logger;

import java.io.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 */
public class ImageUtilsSanselan {
    private static final Logger LOG = getLogger(ImageUtilsSanselan.class);

    public static Integer getOrientation(File imageFile) {
        Integer orientation = null;
        try {
            IImageMetadata metadata = Sanselan.getMetadata(imageFile);
            if (metadata != null && metadata instanceof JpegImageMetadata) {
                JpegImageMetadata jpegImageMetadata = ((JpegImageMetadata) metadata);
                TiffField tag = jpegImageMetadata.findEXIFValue(TiffTagConstants.TIFF_TAG_ORIENTATION);
                if (tag != null) {
                    orientation = tag.getIntValue();
                }
            }

        } catch (IOException | ImageReadException e) {
            LOG.warn("Cannot read exif metadata, image could not be a jpg");
        }
        return orientation;
    }

    /**
     * TODO exif dats is not saved see unit test
     *
     * @param orientation
     * @param src
     * @throws IOException
     * @throws ImageWriteException
     * @throws ImageReadException
     */
    public static void writeOrientation(Integer orientation, File src) throws IOException, ImageWriteException, ImageReadException {
        TiffOutputSet outputSet = new TiffOutputSet();
        TiffOutputDirectory dir = outputSet.getOrCreateExifDirectory();
        outputSet.removeField(TiffTagConstants.TIFF_TAG_ORIENTATION);
        TiffOutputField field = TiffOutputField.create(TiffTagConstants.TIFF_TAG_ORIENTATION,
                outputSet.byteOrder, orientation);
        dir.add(field);
        File tempFile = File.createTempFile("temp-" + System.currentTimeMillis(), ".jpeg");
        OutputStream os = new BufferedOutputStream(new FileOutputStream(tempFile));
        new ExifRewriter().updateExifMetadataLossless(src, os, outputSet);
        os.close();
        FileUtils.copyFile(tempFile, src);
    }
}
