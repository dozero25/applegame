package com.project.dozeo_appleGame.repository.account;

import com.project.dozeo_appleGame.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QUser qUser = QUser.user;

    public UserRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public int findNextUserIndex() {
        Integer maxIndex = jpaQueryFactory
                .select(qUser.id.max())
                .from(qUser)
                .fetchOne()
                .intValue();

        if(maxIndex == null || maxIndex == 0){
            return 0;
        }
        return maxIndex + 1;
    }
}
