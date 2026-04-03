package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.commons.collect.Tuple2;
import it.mapsgroup.gzoom.ofbiz.service.VersionServiceOfBiz;
import it.mapsgroup.gzoom.mybatis.dao.PartyNoteDao;
import it.mapsgroup.gzoom.mybatis.dao.PartyContentDao;
import it.mapsgroup.gzoom.mybatis.dto.PartyNoteEx;
import it.mapsgroup.gzoom.mybatis.dto.PartyContentEx;
import it.mapsgroup.gzoom.rest.ValidationException;
import it.mapsgroup.gzoom.util.Manifests;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import it.mapsgroup.gzoom.model.Result;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.List;
import java.util.Date;
import java.util.function.Supplier;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Node service.
 *
 */
@Service
public class NodeService {
    private static final Logger LOG = getLogger(NodeService.class);

    public static final String DEFAULT_IMPLEMENTATION_TITLE = "GZoom REST";
    // TODO rimuovere lmm
    public static final String DEFAULT_PATH_LOGO = "/lmm/images/gzoom_logo.jpg";
    public static final String DEFAULT_PATH_LOGO_LOGIN = "/lmm/images/gzoom_logo_login.jpg";
    public static final String DEFAULT_PATH_ICON = "/lmm/images/gzoom.ico";

    private final PartyNoteDao partyNoteDao;
    private final PartyContentDao partyContentDao;
    private final VersionServiceOfBiz versionService;

    @Autowired
    public NodeService(PartyNoteDao partyNoteDao, PartyContentDao partyContentDao, VersionServiceOfBiz versionService) {
        this.partyNoteDao = partyNoteDao;
        this.partyContentDao = partyContentDao;
        this.versionService = versionService;
    }

    public PartyNoteEx getNodeConfiguration(String partyId, String noteName) {
        PartyNoteEx ret = partyNoteDao.getPartyNote(partyId, noteName);
        return ret;
    }

    public String versionLegacy() {
        String  result = versionService.version();
        return result;
    }

    public String versionREST() {
        try {
            final Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                Supplier<Manifest> supplier = () -> {
                    Manifest manifest = new Manifest();
                    try {
                        manifest = new Manifest(resources.nextElement().openStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return manifest;
                };
                Tuple2<String,Date> t = Manifests.getProductInfo(supplier, DEFAULT_IMPLEMENTATION_TITLE);
                if(t.first() != null){
                    return t.first();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*Tuple2<String,Date> t = Manifests.getProductInfo(supplier, implementationTitle);


        Enumeration<URL> resources = null;
        try {
            resources = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                try {
                    Manifest manifest = new Manifest(resources.nextElement().openStream());
                    Tuple2<String,Date> t2 = Manifests.getProductInfo(manifest);

                    if(t2.first() != null){
                        return t2.first();
                    }
                    /*Attributes attribs = manifest.getMainAttributes();
                    String title = attribs.getValue("Implementation-Title");
                    LOG.info("enum Implementation-Title " + title);
                    if ("GZoom REST".equals(title)) {
                        String version = initProductVersion(attribs);
                        LOG.info(version);
                        date = initProductDate(attribs);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        URLClassLoader cl = (URLClassLoader) getClass().getClassLoader();
        try {
            URL url = cl.findResource("META-INF/MANIFEST.MF");
            Manifest manifest = new Manifest(url.openStream());
            // do stuff with it
            Attributes attribs = manifest.getMainAttributes();
            String title = attribs.getValue("Implementation-Title");
            LOG.info("url Implementation-Title " + title);
            if ("GZoom REST".equals(title)) {
                String version = initProductVersion(attribs);
                LOG.info(version);
                // TODO date = initProductDate(attribs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
            /*Supplier<Manifests> supplier = () -> {
                try  {
                // Manifests manifest = new Manifests();
                InputStream bw = new ClassPathResource("/META-INF/MANIFEST.MF").getInputStream();
                return bw;
                } catch (IOException e) {
                    LOG.error("error loading file", e);
                    throw new ValidationException("error loading file");
                }
                // Manifests.getProductInfo(Thread.currentClassLoader().getInputStream("/META-INF/MANIFEST.MF"));
                // return manifest;
            };

            Tuple2<String,Date> t = Manifests.getProductInfo(supplier);
            return result;
            */
        return "";
    }

    /**
     *
     * @param partyId
     * @param partyContentTypeId
     * @param request
     * @param response
     * @return
     */
    public String stream(String partyId, String partyContentTypeId, HttpServletRequest request, HttpServletResponse response) {
        try  {
            String imagePath = getDefaultImagePath(partyContentTypeId);
            LOG.info("stream default imagePath: " + imagePath);
            InputStream bw = new ClassPathResource(imagePath).getInputStream();
            String fileName = imagePath;

            PartyContentEx partyContentEx = partyContentDao.getPartyContent(partyId, partyContentTypeId);

            if (partyContentEx != null) {
                LOG.info("stream partyContent path: " + partyContentEx.getDataResource().getObjectInfo());
                File partyContentFile = new File(partyContentEx.getDataResource().getObjectInfo());
                if (partyContentFile.exists() && partyContentFile.canRead()) {
                    File file = partyContentFile;
                    response.setContentType(partyContentEx.getDataResource().getMimeTypeId());
                    fileName = partyContentEx.getContentName();
                    bw = new BufferedInputStream(new FileInputStream(file));
                    response.setContentLength((int) file.length());
                } else {
                    LOG.info("No valid partyContent path found, use default " + imagePath);
                }
            }
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"");
            IOUtils.copy(bw, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            LOG.error("error loading file", e);
            throw new ValidationException("error loading file");
        }
        return "";
    }

    private String getDefaultImagePath(String partyContentTypeId) {
        if ("LOGO".equals(partyContentTypeId)) {
            return DEFAULT_PATH_LOGO;
        } else if ("LOGO_LOGIN".equals(partyContentTypeId)) {
            return DEFAULT_PATH_LOGO_LOGIN;
        }
        return DEFAULT_PATH_ICON;
    }

}
