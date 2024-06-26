package io.wanted.market.core.domain.support.cursor;

public record PagingResult(Long nextCursor, boolean hasNext) {
    public static PagingResult from(Long nextCursor) {
        return new PagingResult(nextCursor, nextCursor != null);
    }
}
