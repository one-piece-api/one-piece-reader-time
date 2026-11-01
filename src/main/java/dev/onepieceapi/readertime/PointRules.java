package dev.onepieceapi.readertime;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

import static dev.onepieceapi.readertime.PointPart.DISCOVERED;
import static dev.onepieceapi.readertime.PointPart.ENDED;
import static dev.onepieceapi.readertime.PointPart.REVEAL;
import static dev.onepieceapi.readertime.PointRule.AXIS_NOT_MET;
import static dev.onepieceapi.readertime.PointRule.BEFORE_FIRST_APPEARANCE;
import static dev.onepieceapi.readertime.PointRule.ENDED_BEFORE_DISCOVERED;
import static dev.onepieceapi.readertime.PointRule.ENDED_NOT_DISCOVERED;
import static dev.onepieceapi.readertime.PointRule.MISSING;
import static dev.onepieceapi.readertime.PointRule.NOT_AFTER_FIRST_APPEARANCE;

/**
 * The rules every point of an item follows, axis by axis (workflow 3.7.4). Pure checks:
 * they return the violations, the services decide how to report them.
 */
@UtilityClass
public class PointRules {

	/**
	 * A window of an item with a first appearance: discovered (required) and ended
	 * (optional) only on its axes, discovered not before it, ended not before discovered.
	 */
	public List<PointViolation> ofWindow(RevealWindow window, Point firstAppearance) {
		List<PointViolation> violations = new ArrayList<>(required(window.discovered(), DISCOVERED));
		for (Axis axis : window.discovered().axes()) {
			if (!firstAppearance.has(axis)) {
				violations.add(violation(DISCOVERED, axis, AXIS_NOT_MET));
			}
			else if (window.discovered().isBefore(firstAppearance, axis)) {
				violations.add(violation(DISCOVERED, axis, BEFORE_FIRST_APPEARANCE));
			}
		}
		for (Axis axis : window.ended().axes()) {
			if (!firstAppearance.has(axis)) {
				violations.add(violation(ENDED, axis, AXIS_NOT_MET));
			}
		}
		violations.addAll(endedRules(window));
		return violations;
	}

	/**
	 * A window of an item without a first appearance (Saga, Arc): a discovered point and
	 * the rules on {@code ended} only.
	 */
	public List<PointViolation> ofWindow(RevealWindow window) {
		List<PointViolation> violations = new ArrayList<>(required(window.discovered(), DISCOVERED));
		violations.addAll(endedRules(window));
		return violations;
	}

	/** A field reveal: strictly after the first appearance, only on its axes. */
	public List<PointViolation> ofReveal(Point reveal, Point firstAppearance) {
		List<PointViolation> violations = new ArrayList<>(required(reveal, REVEAL));
		for (Axis axis : reveal.axes()) {
			if (!firstAppearance.has(axis)) {
				violations.add(violation(REVEAL, axis, AXIS_NOT_MET));
			}
			else if (!firstAppearance.isBefore(reveal, axis)) {
				violations.add(violation(REVEAL, axis, NOT_AFTER_FIRST_APPEARANCE));
			}
		}
		return violations;
	}

	private List<PointViolation> required(Point point, PointPart part) {
		return point.isMissing() ? List.of(violation(part, null, MISSING)) : List.of();
	}

	private List<PointViolation> endedRules(RevealWindow window) {
		List<PointViolation> violations = new ArrayList<>();
		for (Axis axis : window.ended().axes()) {
			if (!window.discovered().has(axis)) {
				violations.add(violation(ENDED, axis, ENDED_NOT_DISCOVERED));
			}
			else if (window.ended().isBefore(window.discovered(), axis)) {
				violations.add(violation(ENDED, axis, ENDED_BEFORE_DISCOVERED));
			}
		}
		return violations;
	}

	private PointViolation violation(PointPart part, Axis axis, PointRule rule) {
		return new PointViolation(part, axis, rule);
	}

}
