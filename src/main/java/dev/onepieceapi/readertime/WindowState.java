package dev.onepieceapi.readertime;

/** The state of a {@link RevealWindow} for a reader (workflow 3.7.2). */
public enum WindowState {

	/** Not discovered yet. */
	HIDDEN,

	/** Discovered and not ended. */
	CURRENT,

	/** Ended: still shown, as history. */
	FORMER

}
