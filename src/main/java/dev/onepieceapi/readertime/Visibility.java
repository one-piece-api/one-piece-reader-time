package dev.onepieceapi.readertime;

import lombok.experimental.UtilityClass;

import java.util.List;

/** What a reader at a position sees of items and list entries (workflow 3.8). */
@UtilityClass
public class Visibility {

	/** An item is visible from its first appearance on the reader's axis. */
	public boolean itemVisibleAt(Point firstAppearance, ReaderPosition position) {
		return position.hasReached(firstAppearance);
	}

	/**
	 * A list entry is visible, current or former, once its window is discovered and its
	 * target is visible too (workflow 3.7.4: nothing checks that order on save).
	 */
	public boolean entryVisibleAt(RevealWindow window, Point targetFirstAppearance, ReaderPosition position) {
		return window.isDiscoveredAt(position) && itemVisibleAt(targetFirstAppearance, position);
	}

	/** The entries discovered at the position, current or former, in their order. */
	public <T extends Windowed> List<T> discoveredAt(List<T> entries, ReaderPosition position) {
		return entries.stream().filter((entry) -> entry.window().isDiscoveredAt(position)).toList();
	}

}
