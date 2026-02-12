package com.orderflow.orderflow_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="simple_images")
public class SimpleImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long simpleImageId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "url")
    private String url;

    @NotBlank
    @Size(max = 100)
    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "album_image_id")
    private AlbumImage albumImage;
}
