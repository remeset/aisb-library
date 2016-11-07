package ui.volume.search.facade;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import remote.googlebooks.domain.request.VolumesSearchQuery;
import remote.googlebooks.domain.response.ImageLinks;
import remote.googlebooks.domain.response.Volume;
import remote.googlebooks.domain.response.Volumes;
import remote.googlebooks.facade.BooksFacade;
import ui.core.image.service.ImageRetrivalService;
import ui.core.image.service.ImageTransformerService;
import ui.volume.search.item.domain.RemoteVolumeListItemModel;
import ui.volume.search.item.domain.VolumesListModelImpl;

@Component
public class RemoteVolumeListItemModelFacade implements VolumeListItemModelFacade<RemoteVolumeListItemModel, Volume> {
    @Value("${ui.image.unavailableImageFileLocation}")
    private String unavailableImageFileLocation;

    @Value("${ui.image.volume.maxThumbnailWidht}")
    private int maxThumbnailWidht;

    @Value("${ui.image.volume.maxThumbnailHeight}")
    private int maxThumbnailHeight;

    @Autowired
    private BooksFacade booksFacade;

    @Autowired
    private ImageRetrivalService imageRetrivalService;

    @Autowired
    private ImageTransformerService imageTransformerService;

    @Async
    @Override
    public CompletableFuture<VolumesListModelImpl<RemoteVolumeListItemModel, Volume>> lookup(VolumesSearchQuery query) {
        Volumes volumes = booksFacade.findVolumes(query);
        return CompletableFuture.completedFuture(new VolumesListModelImpl.Builder<RemoteVolumeListItemModel, Volume>()
            .withTotalItems(volumes.getTotalItems())
            .withVolumes(Optional
                .ofNullable(volumes.getItems())
                .orElse(Collections.emptyList())
                .stream()
                .map(volume -> new RemoteVolumeListItemModel.Builder()
                    .withTitle(volume.getVolumeInfo().getTitle())
                    .withAuthors(volume.getVolumeInfo().getAuthors())
                    .withThumbnail(retrieveThumbnail(volume.getVolumeInfo().getImageLinks())) // At this point it's not possible to get 'NoRegisteredImageReaderException' as the image is a classpath resource (and it must be a '.png' file)
                    .withData(volume)
                    .build())
                .collect(Collectors.toList()))
            .build());
    }

    private byte[] retrieveThumbnail(ImageLinks links) {
        return createScaledThumbnailImage(Optional
            .ofNullable(links)
            .flatMap(url -> Optional.ofNullable(imageRetrivalService.retrieve(url.getThumbnail())))
            .orElse(imageRetrivalService.retrieve(retrieveUnavailableImageClassPathResource())));
    }

    private File retrieveUnavailableImageClassPathResource() {
        try {
            return new ClassPathResource(unavailableImageFileLocation).getFile();
        } catch (IOException ex) {
            throw new IllegalStateException(String.format("Unable to retrieve '%s' file due to the following error: ", unavailableImageFileLocation) , ex);
        }
    }

    private byte[] createScaledThumbnailImage(Image originalThumbnailImage) {
        try {
            return imageTransformerService.transform(imageTransformerService.scale(originalThumbnailImage, maxThumbnailWidht, maxThumbnailHeight));
        } catch (IOException ex) {
            throw new IllegalStateException("Image transformation cannot be done due to the following error: ", ex);
        }
    }

}
