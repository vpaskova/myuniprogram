package com.vp.myuniprogram.services;

import com.vp.myuniprogram.dtos.CourseEventDTO;
import com.vp.myuniprogram.entities.CourseEvent;
import com.vp.myuniprogram.repositories.CourseEventRepository;
import com.vp.myuniprogram.repositories.CourseRepository;
import com.vp.myuniprogram.repositories.MainCourseEventRepository;
import com.vp.myuniprogram.repositories.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseEventService {

    final CourseEventRepository courseEventRepository;
    final CourseRepository courseRepository;
    final MainCourseEventRepository mainCourseEventRepository;
    final TimetableRepository timetableRepository;

    public List<CourseEvent> getAllCourseEvents() {
        return  courseEventRepository.findAll();
    }

    public ResponseEntity<String> addCourseEvent(CourseEventDTO courseEventDTO) {
        try{
            CourseEvent courseEvent = CourseEvent.builder()
                    .mainCourseEvent(mainCourseEventRepository.findMainCourseEventById(courseEventDTO.getMainCourseEventId()))
                    .start(courseEventDTO.getStart())
                    .end(courseEventDTO.getEnd())
                    .location(courseEventDTO.getLocation())
                    .timetable(timetableRepository.findTimetableById(courseEventDTO.getTimetableId())).build();

            courseEventRepository.save(courseEvent);

            return ResponseEntity.ok("CourseEvent успешно добавен");
        }catch(DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> updateCourseEvent(Integer id, CourseEventDTO courseEventDTO) {
        try{
            CourseEvent courseEvent = courseEventRepository.findCourseEventById(id);
            if(courseEvent == null)
                return new ResponseEntity<>("CourseEvent не е намерен",HttpStatus.BAD_REQUEST);

            if(courseEventDTO.getMainCourseEventId() != null)
                courseEvent.setMainCourseEvent(mainCourseEventRepository.findMainCourseEventById(courseEventDTO.getMainCourseEventId()));

            if(courseEventDTO.getStart() != null)
                courseEvent.setStart(courseEventDTO.getStart());

            if(courseEventDTO.getEnd() != null)
                courseEvent.setEnd(courseEventDTO.getEnd());

            if(courseEventDTO.getLocation() != null)
                courseEvent.setLocation(courseEventDTO.getLocation());

            if(courseEventDTO.getInfo() != null)
                courseEvent.setInfo(courseEventDTO.getInfo());

            courseEventRepository.save(courseEvent);

            return ResponseEntity.ok("CourseEvent успешно редактиран!");

        }catch(DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
