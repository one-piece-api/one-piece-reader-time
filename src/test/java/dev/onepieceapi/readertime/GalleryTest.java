package dev.onepieceapi.readertime;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** Luffy's gallery (workflow 3.5.1): two appearances and a form. */
class GalleryTest {

	private static final TestImage BEFORE_THE_TIME_SKIP = new TestImage("before", ImageKind.ASPETTO,
			new RevealWindow(Point.of(1, 1), Point.of(598, 517)));

	private static final TestImage AFTER_THE_TIME_SKIP = new TestImage("after", ImageKind.ASPETTO,
			RevealWindow.from(Point.of(598, 517)));

	private static final TestImage GEAR_5 = new TestImage("gear 5", ImageKind.FORMA,
			RevealWindow.from(Point.of(1044, 1071)));

	private static final List<TestImage> GALLERY = List.of(BEFORE_THE_TIME_SKIP, AFTER_THE_TIME_SKIP, GEAR_5);

	@Test
	void theMainPortraitIsTheLatestCurrentAppearance() {
		assertThat(portraitAt(ReaderPosition.atChapter(597))).contains(BEFORE_THE_TIME_SKIP);
		assertThat(portraitAt(ReaderPosition.atChapter(598))).contains(AFTER_THE_TIME_SKIP);
	}

	@Test
	void aFormIsNeverTheMainPortrait() {
		assertThat(portraitAt(ReaderPosition.atEpisode(1100))).contains(AFTER_THE_TIME_SKIP);
	}

	@Test
	void whenEveryAppearanceHasEndedTheLatestOneIsShown() {
		TestImage ended = new TestImage("ended", ImageKind.ASPETTO,
				new RevealWindow(Point.ofChapter(1), Point.ofChapter(5)));

		assertThat(Gallery.mainPortraitAt(List.of(ended), ReaderPosition.atChapter(10))).contains(ended);
	}

	@Test
	void nothingIsShownBeforeTheFirstAppearance() {
		TestImage later = new TestImage("later", ImageKind.ASPETTO, RevealWindow.from(Point.ofChapter(8)));

		assertThat(Gallery.mainPortraitAt(List.of(later), ReaderPosition.atChapter(7))).isEmpty();
	}

	@Test
	void withoutAFilterTheLastAppearanceWithoutAnEndIsTheMainPortrait() {
		assertThat(Gallery.mainPortrait(GALLERY)).contains(AFTER_THE_TIME_SKIP);
		assertThat(Gallery.mainPortrait(List.of(GEAR_5))).isEmpty();
	}

	private static Optional<TestImage> portraitAt(ReaderPosition position) {
		return Gallery.mainPortraitAt(GALLERY, position);
	}

	private record TestImage(String name, ImageKind kind, RevealWindow window) implements GalleryImage {

	}

}
