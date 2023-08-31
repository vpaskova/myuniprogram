package com.vp.myuniprogram.services;

import com.vp.myuniprogram.dtos.MainCourseEventDTO;
import com.vp.myuniprogram.entities.CourseEvent;
import com.vp.myuniprogram.entities.MainCourseEvent;
import com.vp.myuniprogram.enums.Day;
import com.vp.myuniprogram.enums.Semester;
import com.vp.myuniprogram.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MainCourseEventService {

    final CourseEventRepository courseEventRepository;
    final CourseRepository courseRepository;
    final MainCourseEventRepository mainCourseEventRepository;
    final TeacherRepository teacherRepository;
    final TimetableRepository timetableRepository;

    public List<MainCourseEvent> getAllMainCourseEventsByTimetable(Integer id) {
        return  mainCourseEventRepository.findMainCourseEventByTimetableId(id);
    }

    public ResponseEntity<String> addMainCourseEvent(MainCourseEventDTO mainCourseEventDTO) {
        try{
            MainCourseEvent mainCourseEvent = MainCourseEvent.builder()
                    .course(courseRepository.findCourseById(mainCourseEventDTO.getCourseId()))
                    .start(mainCourseEventDTO.getStart())
                    .end(mainCourseEventDTO.getEnd())
                    .location(mainCourseEventDTO.getLocation())
                    .timetable(timetableRepository.findTimetableById(mainCourseEventDTO.getTimetableId()))
                    .teacher(teacherRepository.findTeacherById(mainCourseEventDTO.getTeacherId()))
                    .day(mainCourseEventDTO.getDay()).build();

            MainCourseEvent mainCourseEvent1 = mainCourseEventRepository.save(mainCourseEvent);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h[h]:mm:ss a");
            String semester = mainCourseEvent1.getTimetable().getSemester();
            Map<LocalDateTime, LocalDateTime> startDates = getDatesWithTimeForWeekdayInPeriod(DayOfWeek.valueOf(String.valueOf(Day.valueOfDayName(mainCourseEventDTO.getDay()))), LocalTime.parse(mainCourseEventDTO.getStart(), formatter), LocalTime.parse(mainCourseEventDTO.getEnd(), formatter), semester);

            for (Map.Entry<LocalDateTime, LocalDateTime> entry : startDates.entrySet()) {
                CourseEvent courseEvent = CourseEvent.builder()
                        .mainCourseEvent(mainCourseEventRepository.findMainCourseEventById(mainCourseEvent1.getId()))
                        .start(entry.getKey().toString())
                        .end(entry.getValue().toString())
                        .location(mainCourseEventDTO.getLocation())
                        .timetable(timetableRepository.findTimetableById(mainCourseEventDTO.getTimetableId())).build();

                courseEventRepository.save(courseEvent);
            }

            return ResponseEntity.ok("Събитията са добавени успешно!");
        }catch(DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity deleteMainCourseEventById(Integer id) {
        MainCourseEvent mainCourseEventToDelete = mainCourseEventRepository.findMainCourseEventById(id);
        if(mainCourseEventToDelete == null){
            return new ResponseEntity<>("Неправилно id!",HttpStatus.BAD_REQUEST);
        }
        List<CourseEvent> courseEventsToDeleteList = courseEventRepository.findAllCourseEventsByMainCourseEventId(id);
        if(courseEventsToDeleteList != null)
            courseEventRepository.deleteAll(courseEventsToDeleteList);

        mainCourseEventRepository.delete(mainCourseEventToDelete);

        return new ResponseEntity<>("Успешно изтрити събития!",HttpStatus.OK);
    }
    public ResponseEntity<String> updateMainCourseEvent(Integer id, MainCourseEventDTO mainCourseEventDTO) {
        try{
            MainCourseEvent existingMainCourseEvent = mainCourseEventRepository.findMainCourseEventById(id);
            List<CourseEvent> courseEventsToEditList = courseEventRepository.findAllCourseEventsByMainCourseEventId(id);

            if(existingMainCourseEvent == null)
                return new ResponseEntity<>("MainEventCourse не е намерен!",HttpStatus.BAD_REQUEST);

            if(mainCourseEventDTO.getStart() != null && mainCourseEventDTO.getEnd() != null && mainCourseEventDTO.getDay() != null && mainCourseEventDTO.getLocation() != null  && mainCourseEventDTO.getTeacherId() != null) {
                existingMainCourseEvent.setStart(mainCourseEventDTO.getStart());
                existingMainCourseEvent.setEnd(mainCourseEventDTO.getEnd());
                existingMainCourseEvent.setDay(mainCourseEventDTO.getDay());
                existingMainCourseEvent.setLocation(mainCourseEventDTO.getLocation());
                existingMainCourseEvent.setTeacher(teacherRepository.findTeacherById(mainCourseEventDTO.getTeacherId()));

                courseEventsToEditList.forEach(c -> c.setLocation(mainCourseEventDTO.getLocation()));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h[h]:mm:ss a");
                String semester = existingMainCourseEvent.getTimetable().getSemester();
                Map<LocalDateTime, LocalDateTime> startDates = getDatesWithTimeForWeekdayInPeriod(DayOfWeek.valueOf(String.valueOf(Day.valueOfDayName(mainCourseEventDTO.getDay()))), LocalTime.parse(mainCourseEventDTO.getStart(), formatter), LocalTime.parse(mainCourseEventDTO.getEnd(), formatter), semester);

                int size = Math.min(startDates.size(), courseEventsToEditList.size());

                IntStream.range(0, size).parallel().forEachOrdered(index -> {
                    LocalDateTime dateTimeEntryKey = (LocalDateTime) startDates.keySet().toArray()[index];
                    LocalDateTime dateTimeEntryValue = (LocalDateTime) startDates.values().toArray()[index];
                    CourseEvent courseEvent = courseEventsToEditList.get(index);

                    courseEvent.setStart(dateTimeEntryKey.toString());
                    courseEvent.setEnd(dateTimeEntryValue.toString());
                });
            }

            mainCourseEventRepository.save(existingMainCourseEvent);
            courseEventRepository.saveAll(courseEventsToEditList);

            return ResponseEntity.ok("Успешно редактирано!");
        }catch(DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private Map<LocalDateTime, LocalDateTime> getDatesWithTimeForWeekdayInPeriod(DayOfWeek weekday, LocalTime starTime, LocalTime endTime, String semester) {
        Map<LocalDateTime, LocalDateTime> dates = new HashMap<>();
        Integer startYear = null;
        Integer startMonth = null;
        Integer endMonth = null;
        Integer startDayOfMonth = null;
        Integer endDayOfMonth = null;
        if(Objects.equals(Semester.WINTER.getValue(), semester)) {
            startYear = Year.now().getValue();
            startMonth = Month.SEPTEMBER.getValue();
            endMonth = Month.DECEMBER.getValue();
            startDayOfMonth = 18;
            endDayOfMonth = 23;
        } else if(Objects.equals(Semester.SUMMER.getValue(), semester)) {
            startYear = isCurrentMonthInRange(Month.JANUARY, Month.JUNE) ? Year.now().getValue() : Year.now().getValue() + 1;
            startMonth = Month.FEBRUARY.getValue();
            endMonth = Month.MAY.getValue();
            startDayOfMonth = 5;
            endDayOfMonth = 11;
        }

        LocalDate currentDate = LocalDate.of(startYear, startMonth, startDayOfMonth);
        while (!currentDate.isAfter(LocalDate.of(startYear, endMonth, endDayOfMonth))) {
            if (currentDate.getDayOfWeek() == weekday) {
                LocalDateTime startDateTime = LocalDateTime.of(currentDate, starTime);
                LocalDateTime endDateTime = LocalDateTime.of(currentDate, endTime);
                dates.put(startDateTime, endDateTime);
            }
            currentDate = currentDate.plus(1, ChronoUnit.DAYS);
        }

        return dates;
    }

    private boolean isCurrentMonthInRange(Month startMonth, Month endMonth) {
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();

        if (startMonth.getValue() <= endMonth.getValue()) {
            return currentMonth.getValue() >= startMonth.getValue() &&
                    currentMonth.getValue() <= endMonth.getValue();
        } else {
            return currentMonth.getValue() >= startMonth.getValue() ||
                    currentMonth.getValue() <= endMonth.getValue();
        }
    }
}
