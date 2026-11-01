package dev.onepieceapi.readertime;

import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * Which paragraphs of a section a reader sees (workflow 3.7.3). Unlike a list entry, an
 * ended paragraph is hidden from its end: its text is superseded by a later one.
 */
@UtilityClass
public class Paragraphs {

	/** The paragraphs discovered and not ended at the position, in their order. */
	public <T extends Windowed> List<T> visibleAt(List<T> paragraphs, ReaderPosition position) {
		return paragraphs.stream()
			.filter(paragraph -> paragraph.window().stateAt(position) == WindowState.CURRENT)
			.toList();
	}

	/** Without a filter: the paragraphs without an end, in their order. */
	public <T extends Windowed> List<T> current(List<T> paragraphs) {
		return paragraphs.stream().filter(paragraph -> paragraph.window().ended().isMissing()).toList();
	}

}
