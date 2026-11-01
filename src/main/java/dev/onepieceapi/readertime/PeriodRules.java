package dev.onepieceapi.readertime;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * The periods of one membership (workflow 3.4.7): in list order and not overlapping, axis
 * by axis; only the last may have no end.
 */
@UtilityClass
public class PeriodRules {

	public List<PeriodViolation> check(List<RevealWindow> periods) {
		List<PeriodViolation> violations = new ArrayList<>();
		for (int index = 0; index < periods.size(); index++) {
			RevealWindow period = periods.get(index);
			if (index < periods.size() - 1 && period.ended().isMissing()) {
				violations.add(new PeriodViolation(index, null, PeriodRule.OPEN_BEFORE_LAST));
			}
			for (RevealWindow previous : periods.subList(0, index)) {
				violations.addAll(againstPrevious(index, period, previous));
			}
		}
		return violations.stream().distinct().toList();
	}

	private List<PeriodViolation> againstPrevious(int index, RevealWindow period, RevealWindow previous) {
		List<PeriodViolation> violations = new ArrayList<>();
		for (Axis axis : Axis.values()) {
			if (period.discovered().isBefore(previous.discovered(), axis)) {
				violations.add(new PeriodViolation(index, axis, PeriodRule.OUT_OF_ORDER));
			}
		}
		for (Axis axis : period.overlapsOn(previous)) {
			violations.add(new PeriodViolation(index, axis, PeriodRule.OVERLAPPING));
		}
		return violations;
	}

}
