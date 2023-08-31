sap.ui.define([], function () {
    'use strict'

    const REQUEST_TYPE = {
        GET: 'GET',
        POST: 'POST',
        PUT: 'PUT',
        PATCH: 'PATCH',
        DELETE: 'DELETE',
    }
    const API = {
        STUDENTS: 'http://localhost:5000/student/all',
        TEACHERS: 'http://localhost:5000/teacher/all',
        ADMINS: 'http://localhost:5000/admin/all',
        TIMETABLES: 'http://localhost:5000/timetable/all',
        ALL_EVENTS: 'http://localhost:5000/course/event/all',
        STUDENTS_EVENTS: 'http://localhost:5000/student/1/timetable/events',
        TEACHER_EVENTS: 'http://localhost:5000/teacher/3/timetable/events',
        UPDATE_EVENT: 'http://localhost:5000/course/event',
        ALL_FACULTY: 'http://localhost:5000/faculty/all',
        MAJOR_BY_FACULTY: 'http://localhost:5000/unimajor/all/faculty'
    }

    return {
        REQUEST_TYPE: REQUEST_TYPE,
        API: API,
    }
})