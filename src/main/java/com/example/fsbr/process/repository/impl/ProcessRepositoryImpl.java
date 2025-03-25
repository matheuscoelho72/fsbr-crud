package com.example.fsbr.process.repository.impl;

import com.example.fsbr.process.filter.ProcessFilter;
import com.example.fsbr.process.model.Process;
import com.example.fsbr.process.repository.ProcessRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
@Repository
public class ProcessRepositoryImpl implements ProcessRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Process> findByFilter(ProcessFilter filter, Pageable pageable) {
        StringBuilder jpql = new StringBuilder(" SELECT p FROM Process p ");
        jpql.append(mountWhere(filter));

        try {
            var order = pageable.getSort().iterator().next();
            jpql.append(" ORDER BY ").append(order.getProperty()).append(" ").append(order.getDirection());
        } catch (NoSuchElementException e) {
            jpql.append(" ORDER BY p.id DESC ");
        }

        try {
            TypedQuery<Process> typedQuery = entityManager.createQuery(jpql.toString(), Process.class);
            setParameters(typedQuery, filter);

            if (pageable.isPaged()) {
                typedQuery.setFirstResult((int) pageable.getOffset());
                typedQuery.setMaxResults(pageable.getPageSize());
            }

            long total = countByFilter(filter);
            return new PageImpl<>(typedQuery.getResultList(), pageable, total);

        } catch (Exception e) {
            return Page.empty();
        }
    }

    private String mountWhere(ProcessFilter filter) {
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");

        if (Objects.nonNull(filter.getId())) {
            where.append(" AND p.id = :id ");
        }
        if (Objects.nonNull(filter.getNpu())) {
            where.append(" AND p.npu = :npu ");
        }
        if (Objects.nonNull(filter.getCreateDate())) {
            where.append(" AND p.createDate = :createDate ");
        }
        if (Objects.nonNull(filter.getVisualizationDate())) {
            where.append(" AND p.visualizationDate IS :visualizationDate ");
        }
        if (Objects.nonNull(filter.getCity())) {
            where.append(" AND p.city IS :city ");
        }
        if (Objects.nonNull(filter.getUf())) {
            where.append(" AND p.uf = :uf ");
        }

        return where.toString();
    }

    private void setParameters(Query query, ProcessFilter filter) {
        if (Objects.nonNull(filter.getId())) {
            query.setParameter("id", filter.getId());
        }
        if (Objects.nonNull(filter.getNpu())) {
            query.setParameter("npu", filter.getNpu());
        }
        if (Objects.nonNull(filter.getCreateDate())) {
            query.setParameter("createDate", filter.getCreateDate());
        }
        if (Objects.nonNull(filter.getVisualizationDate())) {
            query.setParameter("visualizationDate", filter.getVisualizationDate());
        }
        if (Objects.nonNull(filter.getCity())) {
            query.setParameter("city", filter.getCity());
        }
        if (Objects.nonNull(filter.getUf())) {
            query.setParameter("uf", filter.getUf());
        }
    }

    private Long countByFilter(ProcessFilter filter) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(p) FROM Process p ");
        jpql.append(mountWhere(filter));

        try {
            Query query = entityManager.createQuery(jpql.toString());
            setParameters(query, filter);
            return Long.parseLong(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0L;
        }
    }

}
