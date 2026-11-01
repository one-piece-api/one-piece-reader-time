package dev.onepieceapi.readertime;

/** A rule the periods of a membership break (workflow 3.4.7). */
public enum PeriodRule {

	/** Discovered before a period listed before it. */
	OUT_OF_ORDER,

	/** Shares a point with an earlier period. */
	OVERLAPPING,

	/** Has no end although a later period follows. */
	OPEN_BEFORE_LAST

}
