package com.enginerds.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTransaction is a Querydsl query type for Transaction
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTransaction extends EntityPathBase<Transaction> {

    private static final long serialVersionUID = -1375959760L;

    public static final QTransaction transaction = new QTransaction("transaction");

    public final NumberPath<Float> amount = createNumber("amount", Float.class);

    public final DateTimePath<java.util.Date> date = createDateTime("date", java.util.Date.class);

    public final StringPath payeeAccount = createString("payeeAccount");

    public final StringPath payeeName = createString("payeeName");

    public final StringPath payerAccount = createString("payerAccount");

    public final StringPath payerName = createString("payerName");

    public final StringPath RefId = createString("RefId");

    public final StringPath status = createString("status");

    public QTransaction(String variable) {
        super(Transaction.class, forVariable(variable));
    }

    public QTransaction(Path<? extends Transaction> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTransaction(PathMetadata metadata) {
        super(Transaction.class, metadata);
    }

}

