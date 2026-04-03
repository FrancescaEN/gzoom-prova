package it.mapsgroup.gzoom.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mapsgroup.gzoom.commons.error.TrySupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.google.common.io.Files.getFileExtension;
import static com.google.common.io.Files.getNameWithoutExtension;
import static java.lang.String.format;
import static java.nio.file.Files.newDirectoryStream;
import static java.nio.file.Files.newInputStream;

/**
 * Reads translations from JSON files.
 *
 * @author Fabio G. Strozzi
 */
public class JsonLocalizationReader {
    private static final Logger LOG = LoggerFactory.getLogger(JsonLocalizationReader.class);
    private static final String TRANS_FILENAME_REGEX = "^[a-z]{2}([_][a-zA-Z]{2})?$";

    /**
     * The regular expression matching filenames in this format: {@code ^[a-z]{2}([_][a-zA-Z]{2})?$}.
     */
    public static final Pattern TRANS_FILENAME_PATTERN = Pattern.compile(TRANS_FILENAME_REGEX);

    /**
     * Reads localization data from a list of classpath JSON resources.
     * <p>
     * Resources' names must adhere to the naming convention (see {@link #TRANS_FILENAME_PATTERN}).
     * </p>
     *
     * @param transRes The list of translation resources in the classpath
     * @return A map from locales to translations
     */
    public static Map<String, Map<String, Object>> readClasspathResources(List<String> transRes) {
        HashMap<String, Map<String, Object>> trans = new HashMap<>();
        transRes.forEach(r -> trans.put(localeOf(r), localizationOf(r, () -> new ClassPathResource(r).getInputStream())));
        return trans;
    }

    /**
     * Reads localization data from the JSON files in a directory.
     * <p>
     * Files whose name does not adhere to the naming convention (see {@link #TRANS_FILENAME_PATTERN}) will be filtered
     * out.
     * </p>
     *
     * @param confDirPath The directory containing the translation files
     * @return A map from locales to translations
     */
    public static Map<String, Map<String, Object>> readDirectory(Path confDirPath) {
        File localesDir = confDirPath.toFile();
        if (!localesDir.exists())
            return new HashMap<>();

        if (!localesDir.isDirectory() || !localesDir.canRead()) {
            LOG.error("Locales directory is not a directory or cannot be read [path={}]", confDirPath);
            throw new TranslationReaderException(format("Locales directory is not a directory or cannot be read [path=%s]", confDirPath));
        }

        return readLocalizations(confDirPath);
    }

    /**
     * Creates the localization map of a resource.
     *
     * @param jsonPath The path (or classpath) to a resource containing the translation map as a JSON object.
     * @param streamer The supplier of the {@link InputStream}
     * @return A map with the translation
     */
    @SuppressWarnings("unchecked")
	static Map<String, Object> localizationOf(String jsonPath, TrySupplier<InputStream, Exception> streamer) {
        Map<String, Object> map;
        try (InputStream is = streamer.get()) {
            //noinspection unchecked
            map = new ObjectMapper().readValue(new BufferedInputStream(is), Map.class);
        } catch (Exception e) {
            LOG.error("Cannot read translation resource [path={}]", jsonPath, e);
            throw new TranslationReaderException(format("Cannot read translation resource [path=%s]", jsonPath), e);
        }

        return map;
    }

    /**
     * Retrieves the locale string of the specified resource, assuming it's formatted according to {@link
     * #TRANS_FILENAME_PATTERN}.
     *
     * @param resource The translation resource.
     * @return The locale string of the specified resource.
     */
    static String localeOf(String resource) {
        String name = getNameWithoutExtension(resource);
        if (!TRANS_FILENAME_PATTERN.asPredicate().test(name))
            throw new TranslationReaderException(format("Resource does not match expected format [name=%s]", name));
        return name.toLowerCase();
    }

    static Map<String, Map<String, Object>> readLocalizations(Path path) {
        Map<String, Map<String, Object>> map = new HashMap<>();
        try (DirectoryStream<Path> jsonPaths = newDirectoryStream(path, JsonLocalizationReader::filterJsonLocale)) {
            jsonPaths.forEach(p -> map.put(localeOf(p.toString()), localizationOf(p.toString(), () -> newInputStream(p))));
        } catch (IOException e) {
            LOG.error("Unexpected exception caught while reading locale files [path={}]", path, e);
            throw new TranslationReaderException(format("Unexpected exception caught while reading locale files [path=%s]", path), e);
        }
        return map;
    }

    static boolean filterJsonLocale(Path transPath) {
        String filename = transPath.toString();
        String name = getNameWithoutExtension(filename);
        String ext = getFileExtension(filename);
        return TRANS_FILENAME_PATTERN.asPredicate().test(name) && ext.endsWith("json");
    }

    /**
     * Exception thrown when something is wrong during the translation reading.
     */
    public static class TranslationReaderException extends RuntimeException {

		private static final long serialVersionUID = -2408171447612433621L;

		public TranslationReaderException(String message, Throwable throwable) {
            super(message, throwable);
        }

        public TranslationReaderException(String message) {
            super(message);
        }
    }
}
