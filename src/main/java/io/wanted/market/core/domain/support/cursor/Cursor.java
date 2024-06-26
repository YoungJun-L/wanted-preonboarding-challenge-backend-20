package io.wanted.market.core.domain.support.cursor;

public record Cursor(
        Long cursor,
        Long limit,
        Enum<?> sortKey,
        SortType sortType
) {
    public static Cursor of(Long cursor, Long limit, Enum<?> sortKey, SortType sortType) {
        return new Cursor(Math.max(1, cursor), Math.min(Math.max(limit, 10), 100), sortKey, sortType);
    }
}
