package com.enginerds.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSanctionList is a Querydsl query type for SanctionList
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSanctionList extends EntityPathBase<SanctionList> {

    private static final long serialVersionUID = 1386738499L;

    public static final QSanctionList sanctionList = new QSanctionList("sanctionList");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public QSanctionList(String variable) {
        super(SanctionList.class, forVariable(variable));
    }

    public QSanctionList(Path<? extends SanctionList> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSanctionList(PathMetadata metadata) {
        super(SanctionList.class, metadata);
    }

}

