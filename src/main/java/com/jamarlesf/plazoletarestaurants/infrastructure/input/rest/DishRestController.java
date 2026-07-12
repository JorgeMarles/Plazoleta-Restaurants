package com.jamarlesf.plazoletarestaurants.infrastructure.input.rest;

import com.jamarlesf.plazoletarestaurants.application.dto.request.DishRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.request.UpdateDishRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.request.UpdateDishStatusRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.DishResponseDto;
import com.jamarlesf.plazoletarestaurants.application.handler.IDishHandler;
import com.jamarlesf.plazoletarestaurants.infrastructure.security.utils.SecurityContextUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dishes/")
@RequiredArgsConstructor
@Tag(name = "Dishes", description = "Dish management endpoints")
public class DishRestController {

    private final IDishHandler dishHandler;

    @Operation(
            summary = "Create a new dish",
            description = "Creates a new dish with the provided information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Dish created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid dish data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Error al crear el plato\"}"
                            )
                    )
            )
    })
    @PostMapping()
    @PreAuthorize("hasAuthority('PROPIETARIO')")
    public ResponseEntity<Void> createDish(@RequestBody DishRequestDto dishRequestDto) {
        dishHandler.saveDish(dishRequestDto, SecurityContextUtils.getAuthenticatedUserId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all dishes",
            description = "Retrieves a list of all registered dishes"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dishes retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping()
    public ResponseEntity<List<DishResponseDto>> findAllDishes() {
        return ResponseEntity.ok(dishHandler.getDishes());
    }

    @Operation(
            summary = "Update a dish",
            description = "Updates the price and description of a dish"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dish updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Dish not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"message\": \"El plato con id X no existe\"}"
                            )
                    )
            )
    })
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('PROPIETARIO')")
    public ResponseEntity<Void> updateDish(@PathVariable Long id, @RequestBody UpdateDishRequestDto updateDishRequestDto) {
        updateDishRequestDto.setId(id);
        dishHandler.updateDish(updateDishRequestDto, SecurityContextUtils.getAuthenticatedUserId());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Update dish status (Enable/Disable)",
            description = "Enables or disables a dish status (active/inactive)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dish status updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Dish not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                     value = "{\"message\": \"El plato con id X no existe\"}"
                            )
                    )
            )
    })
    @PatchMapping("{id}/status")
    @PreAuthorize("hasAuthority('PROPIETARIO')")
    public ResponseEntity<Void> updateDishStatus(
            @PathVariable Long id,
            @RequestBody UpdateDishStatusRequestDto updateDishStatusRequestDto
    ) {
        dishHandler.updateDishStatus(id, updateDishStatusRequestDto, SecurityContextUtils.getAuthenticatedUserId());
        return ResponseEntity.ok().build();
    }
}
