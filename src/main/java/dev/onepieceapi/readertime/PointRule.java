package dev.onepieceapi.readertime;

/** A rule a point breaks (workflow 3.7.4). */
public enum PointRule {

	/** No part on any axis. */
	MISSING,

	/** A part on an axis where the item has no first appearance. */
	AXIS_NOT_MET,

	/** {@code discovered} before the first appearance. */
	BEFORE_FIRST_APPEARANCE,

	/** A reveal at or before the first appearance: it must come strictly after. */
	NOT_AFTER_FIRST_APPEARANCE,

	/** {@code ended} before {@code discovered}. */
	ENDED_BEFORE_DISCOVERED,

	/** {@code ended} on an axis where {@code discovered} has no part. */
	ENDED_NOT_DISCOVERED

}
