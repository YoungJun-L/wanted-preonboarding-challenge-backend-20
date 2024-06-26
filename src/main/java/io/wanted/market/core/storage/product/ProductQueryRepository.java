package io.wanted.market.core.storage.product;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.wanted.market.core.domain.product.ProductStatus;
import io.wanted.market.core.domain.support.cursor.SortType;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static io.wanted.market.core.storage.product.QProductEntity.productEntity;


@Repository
public class ProductQueryRepository {
    private final JPAQueryFactory queryFactory;

    public ProductQueryRepository(final EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ProductEntity> findAllOnSaleOrderByUpdatedAt(Long cursor, Long limit, SortType sortType) {
        return queryFactory
                .selectFrom(productEntity)
                .where(
                        updatedAtLtOrGt(cursor, sortType),
                        productEntity.status.eq(ProductStatus.SALE)
                )
                .orderBy(updatedAtOrderSpecifier(sortType))
                .limit(limit)
                .fetch();
    }

    private BooleanExpression updatedAtLtOrGt(Long cursor, SortType sortType) {
        if (cursor == 1) {
            return null;
        }

        LocalDateTime parsedCursor = Instant.ofEpochSecond(cursor).atZone(ZoneId.systemDefault()).toLocalDateTime();
        if (sortType == SortType.ASC) {
            return productEntity.updatedAt.gt(parsedCursor);
        }
        return productEntity.updatedAt.lt(parsedCursor);
    }

    private OrderSpecifier<LocalDateTime> updatedAtOrderSpecifier(SortType sortType) {
        if (sortType.equals(SortType.ASC)) {
            return productEntity.updatedAt.asc();
        }
        return productEntity.updatedAt.desc();
    }

    public List<ProductEntity> findAllOnSaleOrderByPrice(Long cursor, Long limit, SortType sortType) {
        return queryFactory
                .selectFrom(productEntity)
                .where(
                        priceLtOrGt(cursor, sortType),
                        productEntity.status.eq(ProductStatus.SALE)
                )
                .orderBy(priceOrderSpecifier(sortType))
                .limit(limit)
                .fetch();
    }

    private BooleanExpression priceLtOrGt(Long cursor, SortType sortType) {
        if (cursor == 1) {
            return null;
        }

        if (sortType == SortType.ASC) {
            return productEntity.price.gt(cursor);
        }
        return productEntity.price.lt(cursor);
    }

    private OrderSpecifier<BigDecimal> priceOrderSpecifier(SortType sortType) {
        if (sortType.equals(SortType.ASC)) {
            return productEntity.price.asc();
        }
        return productEntity.price.desc();
    }
}
