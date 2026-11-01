package dev.onepieceapi.readertime;

import java.util.List;

import org.junit.jupiter.api.Test;

import static dev.onepieceapi.readertime.ReferenceCases.GOMU_GOMU_IS_NIKA;
import static dev.onepieceapi.readertime.ReferenceCases.MARINEFORD_ACE_DIES;
import static org.assertj.core.api.Assertions.assertThat;

class RevealedFieldTest {

	private static final FruitIdentity GOMU_GOMU = new FruitIdentity("Gomu Gomu no Mi", "PARAMECIA", null);

	private static final FruitIdentity NIKA = new FruitIdentity("Hito Hito no Mi, Model: Nika", "ZOAN", "MYTHICAL");

	private static final RevealedField<FruitIdentity> LUFFYS_FRUIT = new RevealedField<>(NIKA, GOMU_GOMU,
			List.of(FieldReveal.ofCurrentValue(GOMU_GOMU_IS_NIKA)));

	@Test
	void fieldsRevealedTogetherChangeTogether() {
		assertThat(LUFFYS_FRUIT.valueAt(ReaderPosition.atChapter(1043))).contains(GOMU_GOMU);
		assertThat(LUFFYS_FRUIT.valueAt(ReaderPosition.atChapter(1044))).contains(NIKA);
		assertThat(LUFFYS_FRUIT.valueAt(ReaderPosition.atEpisode(1071))).contains(NIKA);
		assertThat(LUFFYS_FRUIT.value()).contains(NIKA);
	}

	@Test
	void aceIsAliveUntilMarineford() {
		RevealedField<String> status = new RevealedField<>("DECEASED", "ALIVE",
				List.of(FieldReveal.ofCurrentValue(MARINEFORD_ACE_DIES)));

		assertThat(status.valueAt(ReaderPosition.atEpisode(482))).contains("ALIVE");
		assertThat(status.valueAt(ReaderPosition.atEpisode(483))).contains("DECEASED");
	}

	@Test
	void eachAxisFollowsTheOrderOfItsMedium() {
		FieldReveal<String> falseName = FieldReveal.learned(Point.of(10, 8), "false");
		RevealedField<String> name = new RevealedField<>("true", "initial",
				List.of(falseName, FieldReveal.ofCurrentValue(Point.of(20, 5))));

		assertThat(name.valueAt(ReaderPosition.atChapter(15))).contains("false");
		assertThat(name.valueAt(ReaderPosition.atChapter(20))).contains("true");
		assertThat(name.valueAt(ReaderPosition.atEpisode(5))).contains("true");
		assertThat(name.valueAt(ReaderPosition.atEpisode(8))).contains("false");
	}

	@Test
	void withoutAnInitialValueTheFieldIsUnknownUntilRevealed() {
		RevealedField<String> romaji = new RevealedField<>("Mera Mera no Mi", null,
				List.of(FieldReveal.ofCurrentValue(Point.ofChapter(159))));

		assertThat(romaji.valueAt(ReaderPosition.atChapter(158))).isEmpty();
		assertThat(romaji.valueAt(ReaderPosition.atEpisode(1000))).isEmpty();
	}

	@Test
	void aFieldNoRevealNamesIsAlwaysKnown() {
		assertThat(RevealedField.notRevealed("Nami").valueAt(ReaderPosition.atChapter(1))).contains("Nami");
	}

	@Test
	void twoRevealsOfTheSameFieldNeverShareAPointOnAnAxis() {
		FieldReveal<String> falseName = FieldReveal.learned(Point.of(10, 8), "false");
		FieldReveal<String> sameEpisode = FieldReveal.ofCurrentValue(Point.of(20, 8));
		RevealedField<String> name = new RevealedField<>("true", "initial", List.of(falseName, sameEpisode));

		assertThat(RevealRules.check(name))
			.containsExactly(new RevealViolation(1, Axis.EPISODE, RevealRule.SHARED_POINT));
		assertThat(RevealRules.check(LUFFYS_FRUIT)).isEmpty();
	}

	@Test
	void anInitialValueNeedsAReveal() {
		RevealedField<String> name = new RevealedField<>("true", "initial", List.of());

		assertThat(RevealRules.check(name))
			.containsExactly(new RevealViolation(null, null, RevealRule.INITIAL_WITHOUT_REVEAL));
	}

	private record FruitIdentity(String name, String type, String subcategory) {

	}

}
