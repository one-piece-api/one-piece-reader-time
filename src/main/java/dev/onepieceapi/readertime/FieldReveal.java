package dev.onepieceapi.readertime;

/**
 * One reveal of one field: where the reader learns it and what.
 *
 * @param point where the reader learns it
 * @param value the learned value or the current one
 * @param <V> the field value
 */
public record FieldReveal<V>(Point point, RevealedValue<V> value) {

	public FieldReveal {
		point = Point.orNone(point);
	}

	public static <V> FieldReveal<V> learned(Point point, V value) {
		return new FieldReveal<>(point, RevealedValue.learned(value));
	}

	public static <V> FieldReveal<V> ofCurrentValue(Point point) {
		return new FieldReveal<>(point, RevealedValue.currentValue());
	}

}
