package dev.edu.ngochandev.sharedkernel.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PageResponseDto <T>{
    private int currentPage;
    private int totalPages;
    private long totalItems;
    @Builder.Default
    private List<T> items = List.of();
}
