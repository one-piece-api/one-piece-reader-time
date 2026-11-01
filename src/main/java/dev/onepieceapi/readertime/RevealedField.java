package dev.onepieceapi.readertime;

import java.util.List;
import java.util.Optional;

/**
 * The reader's knowledge of one field, or of fields revealed together (workflow 3.7.1):
 * the current value, what the reader believes at the first appearance, and the reveals in
 * list order. Each axis is read on its own: no order between axes.
 *
 * @param current the main field; the value of every "reveals the current value"
 * @param initial the initial knowledge; null for unknown
 * @param reveals the reveals naming this field
 * @param <V> the field value; a record when fields are revealed together
 */
public record RevealedField<V>(V current, V initial, List<FieldReveal<V>> reveals) {

	public RevealedField {
		reveals = (reveals != null) ? List.copyOf(reveals) : List.of();
	}

	/** A field no reveal names: the reader always knows its current value. */
	public static <V> RevealedField<V> notRevealed(V current) {
		return new RevealedField<>(current, null, List.of());
	}

	/**
	 * The value at the position: the latest reveal whose part on the axis is reached (the
	 * later in the list on a tie); if none, the initial value; if none, unknown.
	 */
	public Optional<V> valueAt(ReaderPosition position) {
		if (this.reveals.isEmpty()) {
			return Optional.ofNullable(this.current);
		}
		Optional<FieldReveal<V>> latest = Optional.empty();
		for (FieldReveal<V> reveal : this.reveals) {
			if (position.hasReached(reveal.point()) && isNotBefore(reveal, latest, position.axis())) {
				latest = Optional.of(reveal);
			}
		}
		return latest.map(FieldReveal::value).map(this::resolve).or(() -> Optional.ofNullable(this.initial));
	}

	/** The value without a filter: the current one. */
	public Optional<V> value() {
		return Optional.ofNullable(this.current);
	}

	private boolean isNotBefore(FieldReveal<V> reveal, Optional<FieldReveal<V>> latest, Axis axis) {
		return latest.map(FieldReveal::point).filter((point) -> reveal.point().isBefore(point, axis)).isEmpty();
	}

	private V resolve(RevealedValue<V> value) {
		return switch (value) {
			case RevealedValue.Learned<V> learned -> learned.value();
			case RevealedValue.CurrentValue<V> currentValue -> this.current;
		};
	}

}
