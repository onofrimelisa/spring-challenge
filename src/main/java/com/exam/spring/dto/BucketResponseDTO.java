package com.exam.spring.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BucketResponseDTO {
    private Integer id;
    private BucketInfoDTO bucketInfo;
}
