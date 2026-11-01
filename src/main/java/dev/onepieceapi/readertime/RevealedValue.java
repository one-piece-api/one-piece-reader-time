package dev.onepieceapi.readertime;

/**
 * What a reveal says about one field (workflow 3.7.1): the value the reader learns there,
 * or that the reader learns the current value, never copied.
 *
 * @param <V> the field value; a record when fields are revealed together
 */
public sealed interface RevealedValue<V> {

	static <V> RevealedValue<V> learned(V value) {
		return new Learned<>(value);
	}

	static <V> RevealedValue<V> currentValue() {
		return new CurrentValue<>();
	}

	/** The value the reader learns, which is no longer the current one. */
	record Learned<V>(V value) implements RevealedValue<V> {

	}

	/** The reader learns the value of the main field. */
	record CurrentValue<V>() implements RevealedValue<V> {

	}

}
