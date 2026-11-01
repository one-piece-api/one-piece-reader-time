package dev.onepieceapi.readertime;

import java.util.List;

import org.junit.jupiter.api.Test;

import static dev.onepieceapi.readertime.ReferenceCases.DRAGON_IS_LUFFYS_FATHER;
import static dev.onepieceapi.readertime.ReferenceCases.NAMI_FIRST_APPEARANCE;
import static org.assertj.core.api.Assertions.assertThat;

class VisibilityTest {

	@Test
	void eachReaderMeetsNamiOnTheirOwnAxis() {
		assertThat(Visibility.itemVisibleAt(NAMI_FIRST_APPEARANCE, ReaderPosition.atChapter(7))).isFalse();
		assertThat(Visibility.itemVisibleAt(NAMI_FIRST_APPEARANCE, ReaderPosition.atChapter(8))).isTrue();
		assertThat(Visibility.itemVisibleAt(NAMI_FIRST_APPEARANCE, ReaderPosition.atEpisode(1))).isTrue();
	}

	@Test
	void anItemWithoutAPartOnTheAxisIsHiddenThere() {
		assertThat(Visibility.itemVisibleAt(Point.ofChapter(1100), ReaderPosition.atEpisode(2000))).isFalse();
	}

	@Test
	void anEntryWaitsForItsTargetToBeMet() {
		RevealWindow fatherDragon = RevealWindow.from(DRAGON_IS_LUFFYS_FATHER);
		Point dragonMetLate = Point.ofChapter(500);
		ReaderPosition beforeMeetingHim = ReaderPosition.atChapter(450);
		ReaderPosition afterMeetingHim = ReaderPosition.atChapter(500);

		assertThat(Visibility.entryVisibleAt(fatherDragon, dragonMetLate, beforeMeetingHim)).isFalse();
		assertThat(Visibility.entryVisibleAt(fatherDragon, dragonMetLate, afterMeetingHim)).isTrue();
	}

	@Test
	void formerEntriesStayVisible() {
		TestEntry former = new TestEntry(new RevealWindow(Point.ofChapter(10), Point.ofChapter(20)));
		TestEntry later = new TestEntry(RevealWindow.from(Point.ofChapter(30)));

		assertThat(Visibility.discoveredAt(List.of(former, later), ReaderPosition.atChapter(25)))
			.containsExactly(former);
	}

	private record TestEntry(RevealWindow window) implements Windowed {

	}

}
