package com.nkt.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GreetingDTO {
    @NotBlank(message = "Message cannot be blank")
    @Size(max = 255, message = "Message cannot exceed 255 characters")
    private String message;

    // This constructor is useful for testing or other service-layer instantiation
    public GreetingDTO(String message) {
        this.message = message;
    }

}