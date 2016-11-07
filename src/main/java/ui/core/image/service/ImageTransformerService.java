package ui.core.image.service;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
public class ImageTransformerService {
    public Image transform(byte[] source) throws IOException {
        return ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(source)));
    }

    public byte[] transform(Image image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write((BufferedImage) image, "jpeg", outputStream);
        return outputStream.toByteArray();
    }

    public byte[] scale(byte[] source, int maxWidth, int maxHeight) throws IOException {
        return transform(scale(transform(source), maxWidth, maxHeight));
    }

    public Image scale(Image source, int maxWidth, int maxHeight) {
        try {
            int width = source.getWidth(null);
            int height = source.getHeight(null);
            float xRate = (float) maxWidth / width;
            float yRate = (float) maxHeight / height;
            return resize(source, Math.min(xRate, yRate));
        } catch (Exception ex) {
            throw ex;
        }
    }

    private Image resize(Image source, float rate) {
        return resize(source, Float.valueOf(source.getWidth(null) * rate).intValue(), Float.valueOf(source.getHeight(null) * rate).intValue());
    }

    private Image resize(Image source, int width, int height) {
        BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = target.createGraphics();
        graphics.drawImage(source, 0, 0, width, height, null);
        graphics.dispose();
        return target;
    }
}
