package dev.patika.library_management.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookBorrowingUpdateRequest {

    private String borrowerName;
    private LocalDate borrowingDate;
    private LocalDate returnDate;

}
