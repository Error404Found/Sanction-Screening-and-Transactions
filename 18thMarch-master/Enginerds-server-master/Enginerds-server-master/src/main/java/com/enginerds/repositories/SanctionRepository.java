package com.enginerds.repositories;


import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.*;
import org.springframework.data.repository.CrudRepository;

import com.enginerds.domain.QSanctionList;
import com.enginerds.domain.SanctionList;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;

public interface SanctionRepository extends CrudRepository<SanctionList, Integer>, QueryDslPredicateExecutor<SanctionList>, QuerydslBinderCustomizer<QSanctionList> {

    @Override
    default public void customize(QuerydslBindings bindings, QSanctionList qSanctionList) {
        bindings.bind(String.class)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }
    public SanctionList save(SanctionList sanctionList);
}
