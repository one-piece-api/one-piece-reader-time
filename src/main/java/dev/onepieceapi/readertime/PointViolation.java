package dev.onepieceapi.readertime;

/**
 * A point rule broken. The services map it to their own error, naming the field.
 *
 * @param part the point at fault
 * @param axis the axis at fault; null when the rule is about the whole point
 * @param rule the rule broken
 */
public record PointViolation(PointPart part, Axis axis, PointRule rule) {

}
