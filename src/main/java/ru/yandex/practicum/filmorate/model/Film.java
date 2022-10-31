package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Film extends StorageData{

    @NotBlank
    private String name;

    @Length(min = 1, max = 200)
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Min(1)
    private int duration;

    @JsonIgnore
    private Set<Long> userIds = new HashSet<>();

    @JsonIgnore
    private long rate = 0;

    public void addLike(Long userId) {
        userIds.add(userId);
        rate = userIds.size();
    }

    public void removeLike(Long userId) {
        userIds.remove(userId);
        rate = userIds.size();
    }
}
