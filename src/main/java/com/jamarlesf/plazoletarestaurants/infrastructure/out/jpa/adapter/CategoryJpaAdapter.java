package com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.adapter;

import com.jamarlesf.plazoletarestaurants.domain.model.Category;
import com.jamarlesf.plazoletarestaurants.domain.spi.ICategoryPersistencePort;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.mapper.ICategoryEntityMapper;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryPersistencePort {

    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryEntityMapper::toCategory);
    }
}
