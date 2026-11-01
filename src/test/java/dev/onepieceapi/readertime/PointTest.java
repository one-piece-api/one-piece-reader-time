package dev.onepieceapi.readertime;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class PointTest {

	@Test
	void eachPartIsIndependent() {
		Point chapterOnly = Point.ofChapter(8);

		assertThat(chapterOnly.has(Axis.CHAPTER)).isTrue();
		assertThat(chapterOnly.has(Axis.EPISODE)).isFalse();
		assertThat(chapterOnly.axes()).containsExactly(Axis.CHAPTER);
		assertThat(Point.NONE.isMissing()).isTrue();
		assertThat(Point.orNone(null)).isEqualTo(Point.NONE);
	}

	@Test
	void chapterZeroExistsButNoNumberIsNegative() {
		assertThat(Point.ofChapter(0).on(Axis.CHAPTER)).hasValue(0);
		assertThatIllegalArgumentException().isThrownBy(() -> Point.ofEpisode(-1));
		assertThatIllegalArgumentException().isThrownBy(() -> ReaderPosition.atChapter(-1));
	}

	@Test
	void aPositionReachesPointsUpToAndIncludingIt() {
		ReaderPosition atChapter8 = ReaderPosition.atChapter(8);

		assertThat(atChapter8.hasReached(Point.of(8, 50))).isTrue();
		assertThat(atChapter8.hasReached(Point.ofChapter(9))).isFalse();
		assertThat(atChapter8.hasReached(Point.ofEpisode(1))).isFalse();
		assertThat(atChapter8.hasReached(null)).isFalse();
	}

}
