package dev.onepieceapi.readertime;

import java.util.List;

import org.junit.jupiter.api.Test;

import static dev.onepieceapi.readertime.ReferenceCases.DRESSROSA_SABO_ALIVE;
import static dev.onepieceapi.readertime.ReferenceCases.SABO_FIRST_APPEARANCE;
import static org.assertj.core.api.Assertions.assertThat;

/** Sabo's biography (workflow 3.7.3): a belief ended at Dressrosa, the facts kept. */
class ParagraphsTest {

	private static final TestParagraph CHILDHOOD = new TestParagraph("childhood",
			RevealWindow.from(SABO_FIRST_APPEARANCE));

	private static final TestParagraph CELESTIAL_DRAGON_ATTACK = new TestParagraph("shot",
			RevealWindow.from(SABO_FIRST_APPEARANCE));

	private static final TestParagraph BELIEVED_DEAD = new TestParagraph("believed dead",
			new RevealWindow(SABO_FIRST_APPEARANCE, DRESSROSA_SABO_ALIVE));

	private static final TestParagraph REVOLUTIONARY = new TestParagraph("revolutionary",
			RevealWindow.from(DRESSROSA_SABO_ALIVE));

	private static final TestParagraph SURVIVED = new TestParagraph("survived",
			RevealWindow.from(DRESSROSA_SABO_ALIVE));

	private static final List<TestParagraph> BIOGRAPHY = List.of(CHILDHOOD, CELESTIAL_DRAGON_ATTACK, BELIEVED_DEAD,
			REVOLUTIONARY, SURVIVED);

	@Test
	void beforeDressrosaTheReaderBelievesHimDead() {
		assertThat(Paragraphs.visibleAt(BIOGRAPHY, ReaderPosition.atChapter(730))).containsExactly(CHILDHOOD,
				CELESTIAL_DRAGON_ATTACK, BELIEVED_DEAD);
	}

	@Test
	void fromDressrosaTheBeliefIsGoneAndTheFactsStay() {
		assertThat(Paragraphs.visibleAt(BIOGRAPHY, ReaderPosition.atChapter(731))).containsExactly(CHILDHOOD,
				CELESTIAL_DRAGON_ATTACK, REVOLUTIONARY, SURVIVED);
	}

	@Test
	void beforeTheFirstAppearanceNothingIsShown() {
		assertThat(Paragraphs.visibleAt(BIOGRAPHY, ReaderPosition.atChapter(582))).isEmpty();
	}

	@Test
	void withoutAFilterOnlyParagraphsWithoutAnEndAreShown() {
		List<TestParagraph> withoutAnEnd = List.of(CHILDHOOD, CELESTIAL_DRAGON_ATTACK, REVOLUTIONARY, SURVIVED);

		assertThat(Paragraphs.current(BIOGRAPHY)).isEqualTo(withoutAnEnd);
	}

	@Test
	void theAnimeReaderSeesNothingWithoutEpisodes() {
		assertThat(Paragraphs.visibleAt(BIOGRAPHY, ReaderPosition.atEpisode(1000))).isEmpty();
	}

	private record TestParagraph(String text, RevealWindow window) implements Windowed {

	}

}
