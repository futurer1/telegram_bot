package com.mikhail.telegram.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.type.SqlTypes;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.hibernate.annotations.JdbcTypeCode;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Convert(attributeName = "jsonb", converter = JsonBinaryType.class)
@Builder
@Table(name = "raw_data")
@Entity
public class RawData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Update event;
}
