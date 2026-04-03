package it.memelabs.smartnebula.commons;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import org.slf4j.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Locale;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 *         https://www.mkyong.com/java/how-to-resize-an-image-in-java/
 *         http://stackoverflow.com/questions/15558202/how-to-resize-image-in-java
 */
public class ImageUtils {
    private static final Logger LOG = getLogger(ImageUtils.class);


    /**
     * TODO use new algoritm
     *
     * @param file
     * @param heightWidth
     * @return
     * @throws IOException
     */
    public static BufferedImage scale(File file, int heightWidth) throws IOException {
        Long start = System.currentTimeMillis();
        BufferedImage img = ImageIO.read(file);
        BufferedImage scale = scale(img, heightWidth);
        if (LOG.isDebugEnabled()) {
            LOG.info("Time to scale image: {}ms", System.currentTimeMillis() - start);
        }
        return scale;
    }


    public static BufferedImage scale(BufferedImage img, int heightWidth) throws IOException {
        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        BufferedImage scratchImage = null;
        Graphics2D g2 = null;

        int w = img.getWidth();
        int h = img.getHeight();

        if (w <= heightWidth || h <= heightWidth)
            return img;

        int prevW = w;
        int prevH = h;

        do {
            if (w > heightWidth) {
                w /= 2;
                w = (w < heightWidth) ? heightWidth : w;
            }

            if (h > heightWidth) {
                h /= 2;
                h = (h < heightWidth) ? heightWidth : h;
            }

            if (scratchImage == null) {
                scratchImage = new BufferedImage(w, h, type);
                g2 = scratchImage.createGraphics();
            }

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);

            prevW = w;
            prevH = h;
            ret = scratchImage;
        } while (w != heightWidth || h != heightWidth);

        if (g2 != null) {
            g2.dispose();
        }

        if (heightWidth != ret.getWidth() || heightWidth != ret.getHeight()) {
            scratchImage = new BufferedImage(heightWidth, heightWidth, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
        }

        return ret;

    }


    /**
     * @param file
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    public static BufferedImage scale(File file, int width, int height) throws IOException {
        Long start = System.currentTimeMillis();
        BufferedImage img = ImageIO.read(file);
        if (LOG.isDebugEnabled()) {
            LOG.info("Time to load image: {}ms", System.currentTimeMillis() - start);
        }
        return scale(img, width, height);
    }

    /**
     *
     * @param img
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    public static BufferedImage scale(BufferedImage img, int width, int height) throws IOException {
        Long start = System.currentTimeMillis();
        double scaleX = 0d;
        double scaleY = 0d;
        if (width < img.getWidth()) {
            scaleY = ((double) img.getWidth()) / width;
        }
        if (height < img.getHeight()) {
            scaleX = ((double) img.getHeight()) / height;
        }
        if (scaleX > 0 || scaleY > 0) {
            double scale = Math.max(scaleX, scaleY);
            int imgWidth = (int) Math.round(img.getWidth() / scale);
            int imgHeight = (int) Math.round(img.getHeight() / scale);
            BufferedImage ret = resizeImageWithHint(img, img.getType(), imgWidth, imgHeight);
            if (LOG.isDebugEnabled()) {
                LOG.info("Time to scale image: {}ms", System.currentTimeMillis() - start);
            }
            return ret;
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.info("Time to scale image: {}ms (imaged not scaled)", System.currentTimeMillis() - start);
            }
            return img;
        }
    }


    /**
     * High quality resize algorithm
     *
     * @param originalImage
     * @param type
     * @return
     */
    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type, int imgWidth, int imgHeight) {
        BufferedImage resizedImage = new BufferedImage(imgWidth, imgHeight, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, imgWidth, imgHeight, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        return resizedImage;
    }

    /**
     * Writes an image to an output stream as a JPEG file. The JPEG quality can
     * be specified in percent.
     *
     * @param image          image to be written
     * @param stream         target stream
     * @param qualityPercent JPEG quality in percent
     * @throws IOException              if an I/O error occured
     * @throws IllegalArgumentException if qualityPercent not between 0 and 100
     * @see <a href= http://www.java2s.com/Code/Java/2D-Graphics-GUI/WritesanimagetoanoutputstreamasaJPEGfileTheJPEGqualitycanbespecifiedinpercent.htm}>
     * Writes an image to an output stream as a JPEG file</a>
     */
    public static void saveImageAsJPEG(BufferedImage image,
                                       OutputStream stream,
                                       int qualityPercent) throws IOException {
        if ((qualityPercent < 0) || (qualityPercent > 100)) {
            throw new IllegalArgumentException("Quality out of bounds!");
        }
        float quality = qualityPercent / 100f;
        ImageWriter writer = null;
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
        if (iter.hasNext()) {
            writer = iter.next();
        }
        ImageOutputStream ios = ImageIO.createImageOutputStream(stream);
        writer.setOutput(ios);
        ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
        iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwparam.setCompressionQuality(quality);
        writer.write(null, new IIOImage(image, null, null), iwparam);
        ios.flush();
        writer.dispose();
        ios.close();
    }

    /**
     * http://stackoverflow.com/a/21981275/5804720
     *
     * @param imageFile
     * @return
     * @throws IOException
     * @throws ImageProcessingException
     * @throws MetadataException
     */
    public static BufferedImage rotate(File imageFile) throws IOException, ImageProcessingException, MetadataException {
        long start = System.currentTimeMillis();
        BufferedImage originalImage = ImageIO.read(imageFile);
        if (LOG.isDebugEnabled()) {
            LOG.info("Time to load image: {}ms", System.currentTimeMillis() - start);
        }
        start = System.currentTimeMillis();
        Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
        ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
        //image is not jpeg
        if (jpegDirectory == null) {
            return originalImage;
        } else {
            int orientation = 1;
            try {
                orientation = exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            } catch (Exception ex) {
                LOG.warn("cannot read exif directory, image could not be a jpg");
            }


            int width = jpegDirectory.getImageWidth();
            int height = jpegDirectory.getImageHeight();

            AffineTransform affineTransform = new AffineTransform();

            switch (orientation) {
                case 1:
                    break;
                case 2: // Flip X
                    affineTransform.scale(-1.0, 1.0);
                    affineTransform.translate(-width, 0);
                    break;
                case 3: // PI rotation
                    affineTransform.translate(width, height);
                    affineTransform.rotate(Math.PI);
                    break;
                case 4: // Flip Y
                    affineTransform.scale(1.0, -1.0);
                    affineTransform.translate(0, -height);
                    break;
                case 5: // - PI/2 and Flip X
                    affineTransform.rotate(-Math.PI / 2);
                    affineTransform.scale(-1.0, 1.0);
                    break;
                case 6: // -PI/2 and -width
                    affineTransform.translate(height, 0);
                    affineTransform.rotate(Math.PI / 2);
                    break;
                case 7: // PI/2 and Flip
                    affineTransform.scale(-1.0, 1.0);
                    affineTransform.translate(-height, 0);
                    affineTransform.translate(0, width);
                    affineTransform.rotate(3 * Math.PI / 2);
                    break;
                case 8: // PI / 2
                    affineTransform.translate(0, width);
                    affineTransform.rotate(3 * Math.PI / 2);
                    break;
                default:
                    break;
            }
            if (orientation > 1) {
                AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
                BufferedImage destinationImage = new BufferedImage(originalImage.getHeight(), originalImage.getWidth(), originalImage.getType());
                destinationImage = affineTransformOp.filter(originalImage, destinationImage);
                if (LOG.isDebugEnabled()) {
                    LOG.info("Time to rotate image: {}ms", System.currentTimeMillis() - start);
                }
                return destinationImage;
            } else {
                // no rotation needed
                return originalImage;
            }
        }
    }
}
