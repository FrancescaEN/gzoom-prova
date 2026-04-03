package it.memelabs.smartnebula.commons;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Andrea Fossi.
 * @link org.apache.commons.io.IOUtils
 */
public class InternalIoUtils {
    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /*
     * If copied bytes size exceeded length a {@link SizeLimitExceededException} is generated
     * This method was inspired by {@link IOUtils#copyLarge(InputStream, OutputStream, long)}
      * @param input
      * @param output
      * @param length
      * @return
      * @throws IOException
      */
    public static long copyLarge(InputStream input, OutputStream output, final long length)
            throws IOException {
        return copyLarge(input, output, 0, length, new byte[DEFAULT_BUFFER_SIZE]);
    }

    /**
     * If copied bytes size exceeded length a {@link SizeLimitExceededException} is generated
     * This method was inspired by {@link IOUtils#copyLarge(InputStream, OutputStream, long, long, byte[])}
     *
     * @param input
     * @param output
     * @param inputOffset
     * @param length
     * @param buffer
     * @return
     * @throws IOException
     */
    public static long copyLarge(InputStream input, OutputStream output,
                                 final long inputOffset, final long length, byte[] buffer) throws IOException {
        if (inputOffset > 0) {
            IOUtils.skipFully(input, inputOffset);
        }
        if (length == 0) {
            return 0;
        }
        final int bufferLength = buffer.length;
        int bytesToRead = bufferLength;
        if (length > 0 && length < bufferLength) {
            bytesToRead = (int) length;
        }
        int read;
        long totalRead = 0;
        while (bytesToRead > 0 && EOF != (read = input.read(buffer, 0, bytesToRead))) {
            output.write(buffer, 0, read);
            totalRead += read;
            if (length > 0) { // only adjust length if not reading to the end
                // Note the cast must work because buffer.length is an integer
                if (totalRead > length) throw new SizeLimitExceededException();
            }
        }
        return totalRead;
    }

    public static WrappedInputStream wrap(InputStream is, long limit) {
        return new WrappedInputStream(is, limit);
    }

    public static class SizeLimitExceededException extends IOException {
        static final long serialVersionUID = 7818375828146090155L;
    }


    private static class WrappedInputStream extends InputStream {
        private final InputStream is;
        private long count = 0;
        private final long limit;

        private void checkLimit(long n) throws SizeLimitExceededException {
            count += n;
            if (count > limit) throw new SizeLimitExceededException();
        }

        public WrappedInputStream(InputStream is, long limit) {
            this.is = is;
            this.limit = limit;
        }

        @Override
        public int read() throws IOException {
            int read = is.read();
            checkLimit(1);
            return read;
        }

        @Override
        public int read(byte[] b) throws IOException {
            int read = is.read(b);
            checkLimit(b.length);
            return read;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int read = is.read(b, off, len);
            checkLimit(1);
            return read;
        }

        @Override
        public long skip(long n) throws IOException {
            checkLimit(n);
            return is.skip(n);
        }

        @Override
        public int available() throws IOException {
            return is.available();
        }

        @Override
        public void close() throws IOException {
            is.close();
        }

        @Override
        public void mark(int readlimit) {
            is.mark(readlimit);
        }

        @Override
        public void reset() throws IOException {
            is.reset();
        }

        @Override
        public boolean markSupported() {
            return is.markSupported();
        }
    }
}
