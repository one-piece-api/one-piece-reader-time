package dev.onepieceapi.readertime;

/**
 * A broken reveal rule.
 *
 * @param index the reveal in list order; null for the initial value
 * @param axis the axis it breaks on; null when it is not about an axis
 * @param rule the rule broken
 */
public record RevealViolation(Integer index, Axis axis, RevealRule rule) {

}
