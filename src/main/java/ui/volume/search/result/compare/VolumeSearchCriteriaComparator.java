package ui.volume.search.result.compare;

import java.util.Comparator;

import org.springframework.util.Assert;

import remote.googlebooks.domain.request.VolumesSearchQuery;

public class VolumeSearchCriteriaComparator implements Comparator<VolumesSearchQuery> {
    @Override
    public int compare(VolumesSearchQuery o1, VolumesSearchQuery o2) {
        // As all elements has java.util.Optional<T> type, therefore no null check needs to be done
        Assert.notNull(o1);
        Assert.notNull(o1.getTitle());
        Assert.notNull(o1.getAuthor());
        Assert.notNull(o1.getIsbn());
        Assert.notNull(o2);
        if(o1.getTitle().equals(o2.getTitle()) && o1.getAuthor().equals(o2.getAuthor()) && o1.getIsbn().equals(o2.getIsbn())) {
            return 0;
        } else {
            return 1;
        }
    }
}
