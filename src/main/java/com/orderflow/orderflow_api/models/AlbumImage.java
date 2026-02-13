package com.orderflow.orderflow_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="album_images")
public class AlbumImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long albumImageid;

    @NotBlank
    @Size(max = 100)
    @Column(name = "title")
    private String title;

    @NotBlank
    @Size(max = 100)
    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToMany(mappedBy = "albumImage", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SimpleImage> simpleImages = new HashSet<>();

}
