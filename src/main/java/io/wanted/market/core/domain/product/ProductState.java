package io.wanted.market.core.domain.product;

public enum ProductState {
    AVAILABLE, OUT_OF_STOCK;

    public ProductState changedByStock(Long stockQuantity) {
        if (stockQuantity == 0) {
            return ProductState.OUT_OF_STOCK;
        }
        return this;
    }
}
