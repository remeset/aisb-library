package ui.core.image.exception;

@SuppressWarnings("serial")
public class NoRegisteredImageReaderException extends RuntimeException {
    public NoRegisteredImageReaderException(String message) {
        super(message);
    }
}
