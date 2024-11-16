package com.wora.citronix.common.application.service;

import org.springframework.data.domain.Page;

public interface CrudService<I, R, S> {
    Page<S> findAll(int pageNum, int pageSize);

    S findById(I id);

    S create(R dto);

    S update(I id, R dto);

    void delete(I id);
}
