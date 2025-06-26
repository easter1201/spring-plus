package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom{ //QueryDSL 사용
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId){
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        Todo result = queryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin() //N+1 문제 방지
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<TodoSearchResponse> searchTodos(TodoSearchRequest request, Pageable pageable){
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;
        QManager manager = QManager.manager;
        QComment comment = QComment.comment;

        List<TodoSearchResponse> results = queryFactory
                .select(Projections.constructor(TodoSearchResponse.class,
                        todo.title,
                        manager.countDistinct().intValue().as("managerCount"),
                        comment.countDistinct().intValue().as("commentCount")))
                .from(todo)
                .leftJoin(todo.managers, manager).fetchJoin()
                .leftJoin(manager.user, user).fetchJoin()
                .leftJoin(todo.comments, comment).fetchJoin()
                .where(titleContains(request.getTitleKeyword()),
                        nickNameContains(request.getNickNameKeyword()),
                        createdAfter(request.getStartDate()),
                        createdBefore(request.getEndDate()))
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(todo.countDistinct())
                .from(todo)
                .leftJoin(todo.managers, manager).fetchJoin()
                .leftJoin(manager.user, user).fetchJoin()
                .where(titleContains(request.getTitleKeyword()),
                        nickNameContains(request.getNickNameKeyword()),
                        createdAfter(request.getStartDate()),
                        createdBefore(request.getEndDate()))
                .fetchOne();

        return new PageImpl<>(results, pageable, total != null ? total : 0);
    }

    private BooleanExpression titleContains(String titleKeyword){
        return titleKeyword != null ? QTodo.todo.title.containsIgnoreCase(titleKeyword) : null;
    }
    private BooleanExpression nickNameContains(String nickNameKeyword){
        return nickNameKeyword != null ? QUser.user.nickName.containsIgnoreCase(nickNameKeyword) : null;
    }
    private BooleanExpression createdAfter(LocalDateTime startDate){
        return startDate != null ? QTodo.todo.createdAt.goe(startDate) : null;
    }
    private BooleanExpression createdBefore(LocalDateTime endDate){
        return endDate != null ? QTodo.todo.createdAt.loe(endDate) : null;
    }
}
