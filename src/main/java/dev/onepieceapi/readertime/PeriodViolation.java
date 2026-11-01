package dev.onepieceapi.readertime;

/**
 * A period rule broken.
 *
 * @param index the position of the period at fault in the list, from 0
 * @param axis the axis at fault; null when the rule is about the whole period
 * @param rule the rule broken
 */
public record PeriodViolation(int index, Axis axis, PeriodRule rule) {

}
