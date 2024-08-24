package com.platform.ecommerce.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPageResponse {
    private List<CategoryDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalElement;
    private Boolean lastPage;
}
