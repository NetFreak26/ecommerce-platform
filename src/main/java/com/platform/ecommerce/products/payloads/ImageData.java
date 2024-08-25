package com.platform.ecommerce.products.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

@Data
@AllArgsConstructor
public class ImageData {
    private final Resource resource;
    private final MediaType mediaType;
    private final String fileName;
}
