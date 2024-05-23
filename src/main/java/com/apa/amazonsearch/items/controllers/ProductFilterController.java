package com.apa.amazonsearch.items.controllers;

import com.apa.amazonsearch.items.models.ItemDetail;
import com.apa.amazonsearch.items.models.PagedItemDetailCollection;
import com.apa.amazonsearch.items.representation_models.ProductFilterRequest;
import com.apa.amazonsearch.items.representation_models.ProductResponse;
import com.apa.amazonsearch.items.representation_models.ProductResultFilter;
import com.apa.amazonsearch.items.services.ProductFilterService;
import com.apa.users.models.User;
import com.apa.users.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/amazon-products")
@RequiredArgsConstructor
@Slf4j
public class ProductFilterController {
    private final ProductFilterService productFilterService;
    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.GET)
    @CrossOrigin
    public ResponseEntity<PagedItemDetailCollection> findAmazonProducts(@ModelAttribute ProductFilterRequest productFilterRequest) throws IOException {
        log.info("Start find products for " + productFilterRequest.getAmazonOriginalPage());

        User user = getUser();

        productFilterRequest.setUserId(user.getId());

        PagedItemDetailCollection products = productFilterService.findProductsFromAmazon(productFilterRequest);

        return new ResponseEntity(products, HttpStatus.OK);
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity<PagedItemDetailCollection> findProductResults(@RequestBody ProductResultFilter productResultFilter) throws IOException {

        productResultFilter.setUserId(getUser().getId());

        List<ItemDetail> products = productFilterService.findFilteredProducts(productResultFilter);

        List<ProductResponse> productResponses = products.stream().map(product -> new ProductResponse(product)).collect(Collectors.toList());

        productResponses.forEach(productResponse -> {
            WebMvcLinkBuilder unFavoriteLinkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductFilterController.class).unfavoriteItem(productResponse.getId()));
            productResponse.add(unFavoriteLinkBuilder.withRel("unfavorite_item"));

            WebMvcLinkBuilder favoriteLinkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductFilterController.class).favoriteItem(productResponse.getId()));
            productResponse.add(favoriteLinkBuilder.withRel("favorite_item"));
        });

        return new ResponseEntity(productResponses, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/item-id/{itemId}/favorite", method = RequestMethod.PUT)
    @CrossOrigin
    public ResponseEntity<ItemDetail> favoriteItem(@PathVariable String itemId) {
        ItemDetail itemDetail = productFilterService.favoriteItem(itemId);

        return new ResponseEntity(itemDetail, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/item-id/{itemId}/unfavorite", method = RequestMethod.PUT)
    @CrossOrigin
    public ResponseEntity<ItemDetail> unfavoriteItem(@PathVariable String itemId) {
        ItemDetail itemDetail = productFilterService.unfavoriteItem(itemId);

        return new ResponseEntity(itemDetail, HttpStatus.OK);
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/delete-all", method = RequestMethod.DELETE)
    @CrossOrigin
    public void deleteAllSearchResult() {
        productFilterService.deleteAllResults(getUser().getId());
    }
}
