package dev.onepieceapi.readertime;

import org.junit.jupiter.api.Test;

import static dev.onepieceapi.readertime.ReferenceCases.DRAGON_IS_LUFFYS_FATHER;
import static dev.onepieceapi.readertime.ReferenceCases.DRESSROSA_SABO_EATS_MERA;
import static dev.onepieceapi.readertime.ReferenceCases.MARINEFORD_ACE_DIES;
import static dev.onepieceapi.readertime.ReferenceCases.ROCKS_FIRST_NAMED;
import static dev.onepieceapi.readertime.ReferenceCases.USOPP_FIRST_APPEARANCE;
import static dev.onepieceapi.readertime.ReferenceCases.WATER_7_USOPP_LEAVES;
import static org.assertj.core.api.Assertions.assertThat;

class RevealWindowTest {

	private static final RevealWindow USOPP_FIRST_PERIOD = new RevealWindow(Point.of(41, 17), WATER_7_USOPP_LEAVES);

	@Test
	void anEntryIsHiddenBeforeItsDiscoveryCurrentUntilItsEndAndFormerAfter() {
		assertThat(USOPP_FIRST_PERIOD.stateAt(ReaderPosition.atChapter(40))).isEqualTo(WindowState.HIDDEN);
		assertThat(USOPP_FIRST_PERIOD.stateAt(ReaderPosition.atChapter(41))).isEqualTo(WindowState.CURRENT);
		assertThat(USOPP_FIRST_PERIOD.stateAt(ReaderPosition.atChapter(331))).isEqualTo(WindowState.CURRENT);
		assertThat(USOPP_FIRST_PERIOD.stateAt(ReaderPosition.atChapter(332))).isEqualTo(WindowState.FORMER);
		assertThat(USOPP_FIRST_PERIOD.stateAt(ReaderPosition.atEpisode(236))).isEqualTo(WindowState.FORMER);
	}

	@Test
	void aWindowWithoutAnEndStaysCurrent() {
		RevealWindow dragon = RevealWindow.from(DRAGON_IS_LUFFYS_FATHER);

		assertThat(dragon.stateAt(ReaderPosition.atEpisode(313))).isEqualTo(WindowState.HIDDEN);
		assertThat(dragon.stateAt(ReaderPosition.atEpisode(1100))).isEqualTo(WindowState.CURRENT);
		assertThat(dragon.state()).isEqualTo(WindowState.CURRENT);
	}

	@Test
	void anEntryDiscoveredAlreadyEndedIsNeverCurrent() {
		RevealWindow rocks = new RevealWindow(ROCKS_FIRST_NAMED, ROCKS_FIRST_NAMED);

		assertThat(rocks.stateAt(ReaderPosition.atChapter(956))).isEqualTo(WindowState.HIDDEN);
		assertThat(rocks.stateAt(ReaderPosition.atChapter(957))).isEqualTo(WindowState.FORMER);
		assertThat(rocks.state()).isEqualTo(WindowState.FORMER);
	}

	@Test
	void aMissingPartHidesOnThatAxisOnly() {
		RevealWindow chapterOnly = RevealWindow.from(Point.ofChapter(100));

		assertThat(chapterOnly.stateAt(ReaderPosition.atChapter(100))).isEqualTo(WindowState.CURRENT);
		assertThat(chapterOnly.stateAt(ReaderPosition.atEpisode(1000))).isEqualTo(WindowState.HIDDEN);
	}

	@Test
	void anEndUnknownOnTheAxisNeverMakesTheEntryFormerThere() {
		RevealWindow endedInTheMangaOnly = new RevealWindow(USOPP_FIRST_APPEARANCE, Point.ofChapter(332));

		assertThat(endedInTheMangaOnly.stateAt(ReaderPosition.atEpisode(1000))).isEqualTo(WindowState.CURRENT);
		assertThat(endedInTheMangaOnly.state()).isEqualTo(WindowState.FORMER);
	}

	@Test
	void aUserEndingWhereTheNextStartsDoesNotOverlap() {
		RevealWindow ace = new RevealWindow(Point.ofChapter(154), MARINEFORD_ACE_DIES);
		RevealWindow saboFromDressrosa = RevealWindow.from(DRESSROSA_SABO_EATS_MERA);
		RevealWindow saboFromMarineford = RevealWindow.from(MARINEFORD_ACE_DIES);

		assertThat(ace.overlapsOn(saboFromDressrosa)).isEmpty();
		assertThat(ace.overlapsOn(saboFromMarineford)).isEmpty();
	}

	@Test
	void twoOpenUsersOverlapOnEveryAxisTheyShare() {
		RevealWindow first = RevealWindow.from(Point.of(100, 90));
		RevealWindow second = RevealWindow.from(Point.ofChapter(200));

		assertThat(first.overlapsOn(second)).containsExactly(Axis.CHAPTER);
		assertThat(second.overlapsOn(first)).containsExactly(Axis.CHAPTER);
	}

	@Test
	void aWindowDiscoveredAlreadyEndedNeverOverlaps() {
		RevealWindow current = RevealWindow.from(Point.ofChapter(100));
		RevealWindow pastUser = new RevealWindow(Point.ofChapter(900), Point.ofChapter(900));

		assertThat(current.overlapsOn(pastUser)).isEmpty();
	}

}
