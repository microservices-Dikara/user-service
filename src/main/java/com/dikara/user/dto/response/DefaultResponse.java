package com.dikara.user.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@Getter
@Setter
public class DefaultResponse {


    private String createdBy;


    private LocalDateTime createdDate ;


    private String updatedBy;


    private LocalDateTime updatedDate;
}
