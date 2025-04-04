package org.example.bookstoreproject.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.entry.Series;
import org.example.bookstoreproject.persistance.repository.AuthorRepository;
import org.example.bookstoreproject.persistance.repository.SeriesRepository;
import org.example.bookstoreproject.service.dto.AuthorDTO;
import org.example.bookstoreproject.service.dto.SeriesDTO;
import org.example.bookstoreproject.service.mapper.AuthorMapper;
import org.example.bookstoreproject.service.mapper.SeriesMapper;
import org.example.bookstoreproject.service.utility.ArrayStringProcessor;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SeriesService implements ServiceInterface, Service{

    private final SeriesRepository seriesRepository;
    private final SeriesMapper seriesMapper;

    public void process(List<Map<String, String>> data) {
        System.out.println("Series processing started!"+ seriesRepository);
        for (Map<String, String> row : data) {
            if(!row.get("series").isEmpty()){
                SeriesDTO seriesDto = new SeriesDTO(row.get("series").trim());
                System.out.println(seriesDto.getTitle());
                Series series = seriesMapper.mapDtoToEntity(seriesDto);
                seriesRepository.save(series);
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




//Use this logic for genre
//public void process(List<Map<String, String>> data) {
//    System.out.println("Series processing started!"+ seriesRepository);
//    for (Map<String, String> row : data) {
//        System.out.println(row.get("series"));
//        String[] seriesArr = ArrayStringProcessor.getArrElements(row.get("series"));
//        if(seriesArr == null)
//            continue;
//        for (String series : seriesArr) {
//            SeriesDTO seriesDTO = new SeriesDTO(series);
//            System.out.println(seriesDTO.getTitle());
//            Series seriesEntity = seriesMapper.mapDtoToEntity(seriesDTO);
//            seriesRepository.save(seriesEntity);
//        }
//
//    }