package com.example.sensorsdataprocessor.model.mongo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@Document("notifications")
@CompoundIndex(def = """
        {
            'email': 1,
            'jobId': 1
        }
        """, unique = true)
public class Notification {

    @Id
    private String id;

    @Length(min = 2)
    @NotBlank
    private String email;

    @NotBlank
    private String jobId;

    private double severity;

}
