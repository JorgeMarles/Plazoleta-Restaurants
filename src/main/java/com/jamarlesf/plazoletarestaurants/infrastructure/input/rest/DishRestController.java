package com.jamarlesf.plazoletarestaurants.infrastructure.input.rest;

import com.jamarlesf.plazoletarestaurants.application.dto.request.DishRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.DishResponseDto;
import com.jamarlesf.plazoletarestaurants.application.handler.IDishHandler;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<Void> createDish(@RequestBody DishRequestDto dishRequestDto) {
        dishHandler.saveDish(dishRequestDto);
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
    @GetMapping()
    public ResponseEntity<List<DishResponseDto>> findAllDishes() {
        return ResponseEntity.ok(dishHandler.getDishes());
    }
}
