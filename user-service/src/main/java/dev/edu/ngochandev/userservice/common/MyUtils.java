package dev.edu.ngochandev.userservice.common;

import dev.edu.ngochandev.sharedkernel.exception.DateFormatException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MyUtils {
    public static LocalDateTime parseFlexibleDate(String date) {
        try {
            return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        } catch (DateTimeParseException e) {
            throw new DateFormatException("error.date.invalid");
        }
    }
    public static Pageable createPageable(Integer page, Integer size, String sort) {
        // sort
        String[] order = sort.split(":");
        String sortField = order[0];
        Sort.Direction direction = order[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        // pageable
        int pageNo = page > 0 ? page - 1 : 0;
        int pageSize = size;

        return PageRequest.of(pageNo, pageSize, Sort.by(direction, sortField));
    }
}
