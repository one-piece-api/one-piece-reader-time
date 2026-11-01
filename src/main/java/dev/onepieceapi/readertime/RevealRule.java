package dev.onepieceapi.readertime;

/** A broken rule of the reveals of one field (workflow 3.7.1). */
public enum RevealRule {

	/** Two reveals of the same field share a point on an axis. */
	SHARED_POINT,

	/** An initial value is given but no reveal names the field. */
	INITIAL_WITHOUT_REVEAL

}
