package dev.onepieceapi.readertime;

import java.util.List;

import org.junit.jupiter.api.Test;

import static dev.onepieceapi.readertime.ReferenceCases.USOPP_REJOINS;
import static dev.onepieceapi.readertime.ReferenceCases.WATER_7_USOPP_LEAVES;
import static org.assertj.core.api.Assertions.assertThat;

class PeriodRulesTest {

	private static final RevealWindow USOPP_JOINS = new RevealWindow(Point.of(41, 17), WATER_7_USOPP_LEAVES);

	@Test
	void usoppLeavesAndComesBack() {
		assertThat(PeriodRules.check(List.of(USOPP_JOINS, RevealWindow.from(USOPP_REJOINS)))).isEmpty();
	}

	@Test
	void periodsSharingAPointOverlap() {
		RevealWindow tooEarly = RevealWindow.from(Point.ofChapter(300));

		assertThat(PeriodRules.check(List.of(USOPP_JOINS, tooEarly)))
			.containsExactly(new PeriodViolation(1, Axis.CHAPTER, PeriodRule.OVERLAPPING));
	}

	@Test
	void periodsFollowTheListOrder() {
		RevealWindow later = new RevealWindow(Point.ofChapter(438), Point.ofChapter(500));

		assertThat(PeriodRules.check(List.of(later, USOPP_JOINS)))
			.containsExactly(new PeriodViolation(1, Axis.CHAPTER, PeriodRule.OUT_OF_ORDER));
	}

	@Test
	void onlyTheLastPeriodMayBeOpen() {
		RevealWindow openInTheManga = RevealWindow.from(Point.ofChapter(41));
		RevealWindow inTheAnime = RevealWindow.from(Point.ofEpisode(312));

		assertThat(PeriodRules.check(List.of(openInTheManga, inTheAnime)))
			.containsExactly(new PeriodViolation(0, null, PeriodRule.OPEN_BEFORE_LAST));
	}

}
