package com.jamarlesf.plazoletarestaurants.domain.model;

import com.jamarlesf.plazoletarestaurants.domain.exception.DomainException;
import lombok.Getter;

@Getter
public class PaginationCriteria {
    
    private final int pageNumber;
    private final int pageSize;
    
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 500;

    public PaginationCriteria(Integer pageNumber, Integer pageSize) {
        this.pageNumber = (pageNumber != null) ? pageNumber : DEFAULT_PAGE_NUMBER;
        this.pageSize = (pageSize != null) ? pageSize : DEFAULT_PAGE_SIZE;
        
        validate();
    }

    private void validate() {
        if (this.pageNumber < 0) {
            throw new DomainException("El numero de pagina no puede ser negativo");
        }
        if (this.pageSize <= 0) {
            throw new DomainException("El tamaño de pagina debe ser mayor a cero");
        }
        if (this.pageSize > MAX_PAGE_SIZE) {
            throw new DomainException("El tamaño de pagina no puede ser mayor a " + MAX_PAGE_SIZE);
        }
    }

}
