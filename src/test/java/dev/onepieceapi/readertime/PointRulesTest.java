package dev.onepieceapi.readertime;

import org.junit.jupiter.api.Test;

import static dev.onepieceapi.readertime.PointPart.DISCOVERED;
import static dev.onepieceapi.readertime.PointPart.ENDED;
import static dev.onepieceapi.readertime.PointPart.REVEAL;
import static dev.onepieceapi.readertime.PointRule.AXIS_NOT_MET;
import static dev.onepieceapi.readertime.PointRule.BEFORE_FIRST_APPEARANCE;
import static dev.onepieceapi.readertime.PointRule.ENDED_BEFORE_DISCOVERED;
import static dev.onepieceapi.readertime.PointRule.ENDED_NOT_DISCOVERED;
import static dev.onepieceapi.readertime.PointRule.MISSING;
import static dev.onepieceapi.readertime.PointRule.NOT_AFTER_FIRST_APPEARANCE;
import static dev.onepieceapi.readertime.ReferenceCases.ACE_FIRST_APPEARANCE;
import static dev.onepieceapi.readertime.ReferenceCases.GOMU_GOMU_IS_NIKA;
import static dev.onepieceapi.readertime.ReferenceCases.LUFFY_FIRST_APPEARANCE;
import static dev.onepieceapi.readertime.ReferenceCases.MARINEFORD_ACE_DIES;
import static dev.onepieceapi.readertime.ReferenceCases.NAMI_FIRST_APPEARANCE;
import static org.assertj.core.api.Assertions.assertThat;

class PointRulesTest {

	@Test
	void aWindowWithinTheFirstAppearanceIsValid() {
		RevealWindow namiInTheCrew = RevealWindow.from(Point.of(69, 18));

		assertThat(PointRules.ofWindow(namiInTheCrew, NAMI_FIRST_APPEARANCE)).isEmpty();
	}

	@Test
	void aWindowMayStartAtTheFirstAppearance() {
		RevealWindow fromTheFirstAppearance = RevealWindow.from(NAMI_FIRST_APPEARANCE);

		assertThat(PointRules.ofWindow(fromTheFirstAppearance, NAMI_FIRST_APPEARANCE)).isEmpty();
	}

	@Test
	void aWindowWithoutDiscoveryIsMissing() {
		assertThat(PointRules.ofWindow(RevealWindow.from(Point.NONE), NAMI_FIRST_APPEARANCE))
			.containsExactly(new PointViolation(DISCOVERED, null, MISSING));
	}

	@Test
	void aPartOnAnAxisTheFirstAppearanceLacksIsRefused() {
		RevealWindow withAnEpisode = new RevealWindow(Point.of(200, 150), Point.ofEpisode(483));

		assertThat(PointRules.ofWindow(withAnEpisode, ACE_FIRST_APPEARANCE)).containsExactlyInAnyOrder(
				new PointViolation(DISCOVERED, Axis.EPISODE, AXIS_NOT_MET),
				new PointViolation(ENDED, Axis.EPISODE, AXIS_NOT_MET));
	}

	@Test
	void aDiscoveryBeforeTheFirstAppearanceIsRefusedOnItsAxis() {
		RevealWindow tooEarly = RevealWindow.from(Point.of(8, 0));

		assertThat(PointRules.ofWindow(tooEarly, Point.of(8, 1)))
			.containsExactly(new PointViolation(DISCOVERED, Axis.EPISODE, BEFORE_FIRST_APPEARANCE));
	}

	@Test
	void anEndBeforeTheDiscoveryOrOnAnAxisNotDiscoveredIsRefused() {
		RevealWindow wrong = new RevealWindow(Point.ofChapter(600), Point.of(574, 483));

		assertThat(PointRules.ofWindow(wrong, LUFFY_FIRST_APPEARANCE)).containsExactlyInAnyOrder(
				new PointViolation(ENDED, Axis.CHAPTER, ENDED_BEFORE_DISCOVERED),
				new PointViolation(ENDED, Axis.EPISODE, ENDED_NOT_DISCOVERED));
	}

	@Test
	void aWindowWithoutAnAnchorChecksOnlyItsEnd() {
		RevealWindow arcParagraph = new RevealWindow(Point.ofChapter(10), Point.ofChapter(5));

		assertThat(PointRules.ofWindow(arcParagraph))
			.containsExactly(new PointViolation(ENDED, Axis.CHAPTER, ENDED_BEFORE_DISCOVERED));
	}

	@Test
	void aRevealComesStrictlyAfterTheFirstAppearance() {
		assertThat(PointRules.ofReveal(GOMU_GOMU_IS_NIKA, LUFFY_FIRST_APPEARANCE)).isEmpty();
		assertThat(PointRules.ofReveal(Point.of(1, 2), LUFFY_FIRST_APPEARANCE))
			.containsExactly(new PointViolation(REVEAL, Axis.CHAPTER, NOT_AFTER_FIRST_APPEARANCE));
	}

	@Test
	void aRevealNeedsAPointOnTheAxesOfTheFirstAppearance() {
		assertThat(PointRules.ofReveal(Point.NONE, ACE_FIRST_APPEARANCE))
			.containsExactly(new PointViolation(REVEAL, null, MISSING));
		assertThat(PointRules.ofReveal(MARINEFORD_ACE_DIES, ACE_FIRST_APPEARANCE))
			.containsExactly(new PointViolation(REVEAL, Axis.EPISODE, AXIS_NOT_MET));
	}

}
