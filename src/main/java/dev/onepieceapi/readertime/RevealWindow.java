package dev.onepieceapi.readertime;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * When the reader learns that something starts and stops being true (workflow 3.7.2): a
 * {@code discovered} point and an optional {@code ended} one. It holds no world duration.
 *
 * @param discovered where the reader learns it; {@link Point#NONE} while not chosen
 * @param ended where the reader learns it is over; {@link Point#NONE} for none
 */
public record RevealWindow(Point discovered, Point ended) {

	public RevealWindow {
		discovered = Point.orNone(discovered);
		ended = Point.orNone(ended);
	}

	/** A window with no end. */
	public static RevealWindow from(Point discovered) {
		return new RevealWindow(discovered, Point.NONE);
	}

	/** The state for a reader at this position. */
	public WindowState stateAt(ReaderPosition position) {
		if (!position.hasReached(this.discovered)) {
			return WindowState.HIDDEN;
		}
		return position.hasReached(this.ended) ? WindowState.FORMER : WindowState.CURRENT;
	}

	/** Whether a reader at this position knows the window, current or former. */
	public boolean isDiscoveredAt(ReaderPosition position) {
		return stateAt(position) != WindowState.HIDDEN;
	}

	/** The state without a filter: former once an end is known on any axis. */
	public WindowState state() {
		return this.ended.isMissing() ? WindowState.CURRENT : WindowState.FORMER;
	}

	/**
	 * The axes on which this window and the other share a point: both are discovered
	 * there, and their spans, from {@code discovered} (included) to {@code ended}
	 * (excluded, open when missing), intersect. A window discovered and ended at the same
	 * point spans nothing, so it never overlaps.
	 */
	public Set<Axis> overlapsOn(RevealWindow other) {
		Set<Axis> axes = EnumSet.noneOf(Axis.class);
		Arrays.stream(Axis.values()).filter((axis) -> overlapsOn(other, axis)).forEach(axes::add);
		return axes;
	}

	private boolean overlapsOn(RevealWindow other, Axis axis) {
		if (!spansOn(axis) || !other.spansOn(axis)) {
			return false;
		}
		return startOn(axis) < other.endOn(axis) && other.startOn(axis) < endOn(axis);
	}

	private boolean spansOn(Axis axis) {
		return this.discovered.has(axis) && startOn(axis) < endOn(axis);
	}

	private long startOn(Axis axis) {
		return this.discovered.on(axis).getAsInt();
	}

	private long endOn(Axis axis) {
		return this.ended.on(axis).isPresent() ? this.ended.on(axis).getAsInt() : Long.MAX_VALUE;
	}

}
