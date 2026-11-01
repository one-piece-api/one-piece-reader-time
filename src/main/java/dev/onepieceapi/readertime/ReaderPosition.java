package dev.onepieceapi.readertime;

import java.util.Objects;

/**
 * Where a reader is: up to and including chapter N, or episode N (workflow 3.8,
 * {@code asOfChapter} / {@code asOfEpisode}).
 *
 * @param axis the axis the reader follows
 * @param number the last chapter or episode read
 */
public record ReaderPosition(Axis axis, int number) {

	public ReaderPosition {
		Objects.requireNonNull(axis, "axis");
		if (number < 0) {
			throw new IllegalArgumentException("A chapter or episode number is never negative: " + number);
		}
	}

	public static ReaderPosition atChapter(int number) {
		return new ReaderPosition(Axis.CHAPTER, number);
	}

	public static ReaderPosition atEpisode(int number) {
		return new ReaderPosition(Axis.EPISODE, number);
	}

	/**
	 * Whether the reader has reached this point: it has a part on the reader's axis, at
	 * or before the position. A point without a part on the axis is never reached there.
	 */
	public boolean hasReached(Point point) {
		return Point.orNone(point).on(this.axis).stream().anyMatch(part -> part <= this.number);
	}

}
