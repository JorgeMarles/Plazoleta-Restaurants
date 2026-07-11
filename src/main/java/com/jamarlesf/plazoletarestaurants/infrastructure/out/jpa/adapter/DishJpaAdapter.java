package com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.adapter;

import com.jamarlesf.plazoletarestaurants.domain.model.Dish;
import com.jamarlesf.plazoletarestaurants.domain.spi.IDishPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.model.PageModel;
import com.jamarlesf.plazoletarestaurants.domain.model.PaginationCriteria;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.entity.DishEntity;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    @Override
    public void save(Dish dish) {
        DishEntity dishEntity = dishEntityMapper.toEntity(dish);
        dishRepository.save(dishEntity);
    }

    @Override
    public List<Dish> findAll() {
        return dishEntityMapper.toDishList(dishRepository.findAll());
    }

    @Override
    public Optional<Dish> findById(Long id) {
        return dishRepository.findById(id).map(dishEntityMapper::toDish);
    }

    @Override
    public PageModel<Dish> findByRestaurantId(Long restaurantId, Long categoryId, PaginationCriteria paginationCriteria) {
        Pageable pageable = PageRequest.of(
                paginationCriteria.getPageNumber(),
                paginationCriteria.getPageSize(),
                Sort.by("category.name").ascending()
        );

        Page<DishEntity> page;
        if (categoryId != null) {
            page = dishRepository.findByRestaurantIdAndCategoryId(restaurantId, categoryId, pageable);
        } else {
            page = dishRepository.findByRestaurantId(restaurantId, pageable);
        }

        return new PageModel<>(
                dishEntityMapper.toDishList(page.getContent()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
