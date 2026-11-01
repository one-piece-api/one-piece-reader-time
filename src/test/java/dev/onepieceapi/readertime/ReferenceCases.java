package dev.onepieceapi.readertime;

/**
 * The reference cases of the reveals plan (README section 1), as numbers. Checked on the
 * web on 2026-10-31; numbers marked "to verify" were not confirmed by a reliable source
 * and are used only where the rule does not depend on the exact value. An episode left
 * out is a legitimate case too: not seen on the anime axis (workflow 3.7).
 */
final class ReferenceCases {

	/**
	 * Ace first appears in chapter 154. The anime episode is left out: sources disagree
	 * between 91 and 94 (checked 2026-11-01).
	 */
	static final Point ACE_FIRST_APPEARANCE = Point.ofChapter(154);

	/** Ace dies at Marineford: chapter 574, episode 483. */
	static final Point MARINEFORD_ACE_DIES = Point.of(574, 483);

	/**
	 * Sabo eats the Mera Mera no Mi at Dressrosa: chapter 744 (to verify), anime episode
	 * to verify.
	 */
	static final Point DRESSROSA_SABO_EATS_MERA = Point.ofChapter(744);

	/** Sabo first appears in the flashback of chapter 583 (to verify). */
	static final Point SABO_FIRST_APPEARANCE = Point.ofChapter(583);

	/** Sabo is shown alive at Dressrosa: chapter 731 (to verify). */
	static final Point DRESSROSA_SABO_ALIVE = Point.ofChapter(731);

	/** Usopp duels Luffy and leaves the crew at Water 7: chapter 332, episode 236. */
	static final Point WATER_7_USOPP_LEAVES = Point.of(332, 236);

	/**
	 * Usopp rejoins after Enies Lobby: chapter 438, episode 323 (Wikipedia, One Piece
	 * season 9, checked 2026-11-01).
	 */
	static final Point USOPP_REJOINS = Point.of(438, 323);

	/** Usopp first appears in chapter 23, episode 9. */
	static final Point USOPP_FIRST_APPEARANCE = Point.of(23, 9);

	/** Garp reveals that Dragon is Luffy's father: chapter 432, episode 314. */
	static final Point DRAGON_IS_LUFFYS_FATHER = Point.of(432, 314);

	/** Luffy first appears in chapter 1, episode 1. */
	static final Point LUFFY_FIRST_APPEARANCE = Point.of(1, 1);

	/** Rocks is first named by Sengoku: chapter 957, episode 958. */
	static final Point ROCKS_FIRST_NAMED = Point.of(957, 958);

	/**
	 * Nami first appears in chapter 8 and in episode 1: each medium has its own point.
	 */
	static final Point NAMI_FIRST_APPEARANCE = Point.of(8, 1);

	/**
	 * The Gomu Gomu no Mi is revealed as the Hito Hito no Mi, Model: Nika: chapter 1044,
	 * episode 1071.
	 */
	static final Point GOMU_GOMU_IS_NIKA = Point.of(1044, 1071);

	private ReferenceCases() {
	}

}
