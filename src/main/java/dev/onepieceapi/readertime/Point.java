package dev.onepieceapi.readertime;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A point of the reader axis (workflow 3.7): a chapter and/or an episode, by number, each
 * part optional and independent. A missing part means "not seen on that axis".
 *
 * @param chapter the chapter number; null for none
 * @param episode the episode number; null for none
 */
public record Point(Integer chapter, Integer episode) {

	/** No part at all: nothing chosen yet. */
	public static final Point NONE = new Point(null, null);

	public Point {
		requireNotNegative(chapter);
		requireNotNegative(episode);
	}

	public static Point of(int chapter, int episode) {
		return new Point(chapter, episode);
	}

	public static Point ofChapter(int chapter) {
		return new Point(chapter, null);
	}

	public static Point ofEpisode(int episode) {
		return new Point(null, episode);
	}

	/** The point given, or {@link #NONE} for null. */
	public static Point orNone(Point point) {
		return (point != null) ? point : NONE;
	}

	/** The part on this axis; empty when the point has none there. */
	public OptionalInt on(Axis axis) {
		Integer part = (axis == Axis.CHAPTER) ? this.chapter : this.episode;
		return (part != null) ? OptionalInt.of(part) : OptionalInt.empty();
	}

	public boolean has(Axis axis) {
		return on(axis).isPresent();
	}

	/** Whether both points have a part on the axis and this one comes first there. */
	public boolean isBefore(Point other, Axis axis) {
		return has(axis) && other.has(axis) && on(axis).getAsInt() < other.on(axis).getAsInt();
	}

	/** A test for points with the same part as this one on the axis. */
	public Predicate<Point> sameOn(Axis axis) {
		return (other) -> has(axis) && on(axis).equals(other.on(axis));
	}

	/** Whether no part is chosen, on any axis. */
	public boolean isMissing() {
		return this.chapter == null && this.episode == null;
	}

	/** The axes this point has a part on. */
	public Set<Axis> axes() {
		Set<Axis> axes = EnumSet.noneOf(Axis.class);
		Arrays.stream(Axis.values()).filter(this::has).forEach(axes::add);
		return axes;
	}

	private static void requireNotNegative(Integer number) {
		if (number != null && number < 0) {
			throw new IllegalArgumentException("A chapter or episode number is never negative: " + number);
		}
	}

}
