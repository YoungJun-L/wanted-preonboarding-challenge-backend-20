package io.wanted.market.core.api.support.response;

import io.wanted.market.core.domain.support.cursor.PagingResult;

import java.util.List;

public record SliceResult<T>(T content, PagingResult paging) {
    public static <T> SliceResult<List<T>> of(List<T> content, Long nextCursor) {
        return new SliceResult<>(content, PagingResult.from(nextCursor));
    }
}
