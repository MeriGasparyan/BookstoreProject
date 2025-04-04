package org.example.bookstoreproject.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Genre;
import org.example.bookstoreproject.persistance.repository.GenreRepository;
import org.example.bookstoreproject.service.dto.GenreDTO;
import org.example.bookstoreproject.service.mapper.GenreMapper;
import org.example.bookstoreproject.service.utility.ArrayStringProcessor;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class GenreService implements ServiceInterface, Service {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public void process(List<Map<String, String>> data) {
        System.out.println("Series processing started!" + genreRepository);
        for (Map<String, String> row : data) {
            System.out.println(row.get("genres"));
            String[] genresArr = ArrayStringProcessor.getArrElements(row.get("genres"));
            if (genresArr == null)
                continue;
            for (String genre : genresArr) {
                GenreDTO genreDTO = new GenreDTO(genre);
                System.out.println(genreDTO.getName());
                Genre genreEntity = genreMapper.mapDtoToEntity(genreDTO);
                genreRepository.save(genreEntity);
            }

        }
    }

    @Override
    public String value() {
        return "";
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
