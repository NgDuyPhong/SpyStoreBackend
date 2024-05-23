package com.apa.extensions;

import com.apa.extensions.interactors.ProductSearcher;
import com.apa.users.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/extensions")
@RequiredArgsConstructor
@Slf4j
public class ExtensionController {
    private final ProductDetailRepository productDetailRepository;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/products/users/{username}", method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity<String> addProducts(
            @PathVariable("username") String username,
            @RequestBody List<ProductData> products
    ) {
        log.info("Add products from aws");
        List<ProductDetail> productDetails = products.stream().map(productData -> {
                    try {
                        return productData.toItemDetail();
                    } catch (Exception exception) {
                        log.info("Exception " + exception.getMessage());
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());

        productDetails.forEach(productDetail -> productDetail.setUsername(username));

        productDetailRepository.saveAll(productDetails);

        return new ResponseEntity(productDetails, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    @CrossOrigin
    public ResponseEntity<List<ProductDetail>> searchProducts(
            @ModelAttribute SearchRequest searchRequest
    ) {
        List<ProductDetail> productDetails = productDetailRepository.findByUsername(getUser().getUsername());

        List<ProductDetail> results = new ProductSearcher(productDetails).search(searchRequest);

        return new ResponseEntity(results, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/products", method = RequestMethod.DELETE)
    @CrossOrigin
    public ResponseEntity<String> deleteProducts() {
        productDetailRepository.deleteByUsername(getUser().getUsername());

        return new ResponseEntity("Success", HttpStatus.OK);
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
