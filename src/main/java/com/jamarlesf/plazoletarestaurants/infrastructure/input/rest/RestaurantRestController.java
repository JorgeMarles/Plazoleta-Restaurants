package com.jamarlesf.plazoletarestaurants.infrastructure.input.rest;

import com.jamarlesf.plazoletarestaurants.application.dto.request.RestaurantRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.RestaurantResponseDto;
import com.jamarlesf.plazoletarestaurants.application.handler.IRestaurantHandler;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants/")
@RequiredArgsConstructor
@Tag(name = "Restaurants", description = "Restaurant management endpoints")
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;

    @Operation(
            summary = "Create a new restaurant",
            description = "Creates a new restaurant with the provided information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Restaurant created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid restaurant data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"message\": \"El usuario especificado no es un propietario\"}"
                            )
                    )
            )
    })
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping()
    public ResponseEntity<Void> createRestaurant(@RequestBody RestaurantRequestDto restaurant) {
        restaurantHandler.saveRestaurant(restaurant);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all restaurants",
            description = "Retrieves a list of all registered restaurants"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Restaurants retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping()
    public ResponseEntity<List<RestaurantResponseDto>> findAllRestaurants() {
        return ResponseEntity.ok(restaurantHandler.getRestaurants());
    }
}
