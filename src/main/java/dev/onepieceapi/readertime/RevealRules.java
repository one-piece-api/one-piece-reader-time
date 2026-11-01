package dev.onepieceapi.readertime;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * The rules of the reveals of one field that need no knowledge of the entity (workflow
 * 3.7.1). The points follow {@link PointRules#ofReveal}; which fields are named together
 * is the entity's own rule.
 */
@UtilityClass
public class RevealRules {

	public <V> List<RevealViolation> check(RevealedField<V> field) {
		List<RevealViolation> violations = new ArrayList<>();
		if (field.initial() != null && field.reveals().isEmpty()) {
			violations.add(new RevealViolation(null, null, RevealRule.INITIAL_WITHOUT_REVEAL));
		}
		List<FieldReveal<V>> reveals = field.reveals();
		for (int index = 0; index < reveals.size(); index++) {
			violations.addAll(sharedPoints(index, reveals));
		}
		return violations;
	}

	private <V> List<RevealViolation> sharedPoints(int index, List<FieldReveal<V>> reveals) {
		return reveals.get(index)
			.point()
			.axes()
			.stream()
			.filter((axis) -> isSharedWithAnEarlier(index, reveals, axis))
			.map((axis) -> new RevealViolation(index, axis, RevealRule.SHARED_POINT))
			.toList();
	}

	private <V> boolean isSharedWithAnEarlier(int index, List<FieldReveal<V>> reveals, Axis axis) {
		Point point = reveals.get(index).point();
		return reveals.subList(0, index).stream().map(FieldReveal::point).anyMatch(point.sameOn(axis));
	}

}
