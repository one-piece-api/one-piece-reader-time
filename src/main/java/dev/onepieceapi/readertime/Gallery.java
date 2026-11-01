package dev.onepieceapi.readertime;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;

/** The main portrait of a gallery (workflow 3.5.1): derived, never stored. */
@UtilityClass
public class Gallery {

	/**
	 * At a position: the latest discovered {@code ASPETTO} without an end there; else the
	 * latest discovered {@code ASPETTO}. "Latest" is by discovered point on the reader's
	 * axis; on a tie, the later in the list.
	 */
	public <T extends GalleryImage> Optional<T> mainPortraitAt(List<T> images, ReaderPosition position) {
		List<T> discovered = images.stream()
			.filter(image -> image.kind() == ImageKind.ASPETTO)
			.filter(image -> image.window().stateAt(position) != WindowState.HIDDEN)
			.toList();
		List<T> current = discovered.stream()
			.filter(image -> image.window().stateAt(position) == WindowState.CURRENT)
			.toList();
		Optional<T> latestCurrent = latest(current, position.axis());
		return latestCurrent.isPresent() ? latestCurrent : latest(discovered, position.axis());
	}

	/**
	 * Without a filter: the last {@code ASPETTO} without an end, in list order; else the
	 * last {@code ASPETTO}.
	 */
	public <T extends GalleryImage> Optional<T> mainPortrait(List<T> images) {
		List<T> appearances = images.stream().filter(image -> image.kind() == ImageKind.ASPETTO).toList();
		List<T> current = appearances.stream().filter(image -> image.window().ended().isMissing()).toList();
		if (!current.isEmpty()) {
			return Optional.of(current.getLast());
		}
		return appearances.isEmpty() ? Optional.empty() : Optional.of(appearances.getLast());
	}

	private <T extends GalleryImage> Optional<T> latest(List<T> images, Axis axis) {
		T latest = null;
		for (T image : images) {
			if (latest == null || discoveredOn(image, axis) >= discoveredOn(latest, axis)) {
				latest = image;
			}
		}
		return Optional.ofNullable(latest);
	}

	private int discoveredOn(GalleryImage image, Axis axis) {
		return image.window().discovered().on(axis).getAsInt();
	}

}
