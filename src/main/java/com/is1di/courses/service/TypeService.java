package com.is1di.courses.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TypeService {
    private final MongoOperations mongoOperations;

    public <T> Page<T> getPage(int p, int l, String className, Class<T> clazz) {
        Pageable page = PageRequest.of(Math.max(1, p) - 1, Math.max(l, 10));
        Query q = Query.query(Criteria.where("_class").is(className)).with(page);
        return PageableExecutionUtils.getPage(mongoOperations.find(q, clazz),
                page,
                () -> mongoOperations.count(Query.of(q).limit(-1).skip(-1), clazz)
        );
    }

    public <T> Page<T> getPage(int p, int l, Query qu, String className, Class<T> clazz) {
        Pageable page = PageRequest.of(Math.max(1, p) - 1, Math.max(l, 10));
        Query q = qu.addCriteria(Criteria.where("_class").is(className)).with(page);
        return PageableExecutionUtils.getPage(mongoOperations.find(q, clazz),
                page,
                () -> mongoOperations.count(Query.of(q).limit(-1).skip(-1), clazz)
        );
    }

    public <T> Optional<T> findById(ObjectId id, String className, Class<T> clazz) {
        return Optional.ofNullable(mongoOperations.findOne(Query.query(Criteria.where("_id").is(id).and("_class").is(className)), clazz));
    }

    public <T> boolean existsById(ObjectId id, String className, Class<T> clazz) {
        return mongoOperations.exists(Query.query(Criteria.where("_id").is(id).and("_class").is(className)), clazz);
    }

    public <T> Optional<T> findOne(Query q, String className, Class<T> clazz) {
        return Optional.ofNullable(mongoOperations.findOne(q.addCriteria(Criteria.where("_class").is(className)), clazz));
    }

    public <T> boolean exists(Query q, String className, Class<T> clazz) {
        return mongoOperations.exists(q.addCriteria(Criteria.where("_class").is(className)), clazz);
    }

    public <T> Page<T> search(String s, int p, int l, String className, Class<T> clazz) {
        return getPage(p, l, Query.query(Criteria.where("fullName").regex(".*" + s + ".*", "i")), className, clazz);
    }

    public <T> Page<T> search(String s, int p, int l, Query q, String className, Class<T> clazz) {
        return getPage(p, l, q.addCriteria(Criteria.where("fullName").regex(".*" + s + ".*", "i")), className, clazz);
    }
}
