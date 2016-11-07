package ui.core.image.service;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import ui.core.image.exception.NoRegisteredImageReaderException;

@Component
public class ImageRetrivalService {
    public Image retrieve(File file) {
        Assert.notNull(file);
        try {
            return Optional
                .ofNullable(ImageIO.read(file))
                .orElseThrow(() -> new NoRegisteredImageReaderException(String.format("Theres no registered ImageReader can be found for '%s'", file.getAbsolutePath())));
        } catch (IOException ex) {
            throw new IllegalStateException("Image cannot be retrieved due to the following error:" + ex);
        }
    }

    public Image retrieve(String url) {
        Assert.notNull(url);
        try {
            return Optional
                .ofNullable(ImageIO.read(parseUrl(url)))
                .orElseThrow(() -> new NoRegisteredImageReaderException(String.format("Theres no registered ImageReader can be found for '%s'", url)));
        } catch (IOException ex) {
            throw new IllegalStateException("Image cannot be retrieved due to the following error:" + ex);
        }
    }

    private URL parseUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException ex) {
            // This should not occurred
            throw new IllegalStateException("Image URL cannot be generated due to the following error:" + ex);
        }
    }
}
