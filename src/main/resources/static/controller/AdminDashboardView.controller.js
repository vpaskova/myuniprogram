sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/ui/model/json/JSONModel",
    "myuniprogram/handlers/HttpHandler",
    "myuniprogram/utils/Constants",
    "sap/ui/model/Sorter",
    "sap/ui/model/Filter",
    "sap/ui/model/FilterOperator",
    "sap/ui/model/FilterType",
    "sap/m/Dialog",
    "sap/m/library",
    "sap/m/Button",
    "sap/m/Text",
    "sap/ui/core/Fragment",
    "myuniprogram/utils/Common",
    "sap/ui/core/UIComponent"
], function (Controller, JSONModel, HttpHandler, Constants, Sorter, Filter, FilterOperator, FilterType, Dialog, mobileLibrary, Button, Text, Fragment, Common, UIComponent) {
    "use strict";

    return Controller.extend("myuniprogram.controller.AdminDashboardView", {

        onInit: function() {

            var oModel = new JSONModel();
            oModel.setData({
                selectedKey: "allTimetables",
                order : 0,
                navigation: [
                    {
                        key: "allTimetables",
                        title: "Програма",
                        icon: "sap-icon://accelerated"
                    },
                    {
                        key: "allStudents",
                        title: "Студенти",
                        icon: "sap-icon://study-leave"
                    },
                    {
                        key: "allTeachers",
                        title: "Преподаватели",
                        icon: "sap-icon://learning-assistant"
                    },
                    {
                        key: "allAdmins",
                        title: "Администратори",
                        icon: "sap-icon://key-user-settings"
                    }
                ],
                semester: [
                    {
                        id: "летен",
                        name: "летен",
                    },
                    {
                        id: "зимен",
                        name: "зимен",
                    }
                ],
                year: [
                    {
                        id: "1",
                        name: "1",
                    },
                    {
                        id: "2",
                        name: "2",
                    },
                    {
                        id: "3",
                        name: "3",
                    },
                    {
                        id: "4",
                        name: "4",
                    },
                    {
                        id: "магистър",
                        name: "магистър",
                    }
                ],
                day: [
                    {
                        id: "понеделник",
                        name: "понеделник",
                    },
                    {
                        id: "вторник",
                        name: "вторник",
                    },
                    {
                        id: "сряда",
                        name: "сряда",
                    },
                    {
                        id: "четвъртък",
                        name: "четвъртък",
                    },
                    {
                        id: "петък",
                        name: "петък",
                    }
                ]
            });

            this.getView().setModel(oModel);

            this.getUser();
            this.getAllFaculty();
            this.getAllStudents();
            this.getAllTeachers();
            this.getAllAdmins();
            this.getAllTimetables();
        },

        onCollapseExpandPress: function () {
            var oSideNavigation = this.byId("sideNavigation");
            var bExpanded = oSideNavigation.getExpanded();

            oSideNavigation.setExpanded(!bExpanded);
        },

        onNavigationItemSelect: function (oEvent) {
            const navKey = oEvent.getParameter('item').getKey();
            this.byId('pageContainer').to(this.getView().createId(navKey));
        },

        getUser: async function () {
            var userId = sap.ui.getCore().getModel("loggedInUser").getData().id;
            const response = await HttpHandler.executeGetRequest(`http://localhost:5000/admin/user/${userId}`);

            const adminId = response.id;

            const admin = await HttpHandler.executeGetRequest(`http://localhost:5000/admin/${adminId}`);
            this.getView().getModel().setData({
                userName: admin.firstName + " " + admin.lastName
            }, true)
        },

        getAllFaculty: async function () {
            try {
                const modelArray = await HttpHandler.executeGetRequest(Constants.API.ALL_FACULTY);
                let allFaculty = [];

                for (let i = 0; i < modelArray.length; i++) {
                    let oFaculty = this._generateModelFaculty(
                        modelArray[i].name,
                        modelArray[i].id,
                        modelArray[i].info,
                    );

                    allFaculty.push(oFaculty);
                }

                this.getView().getModel().setData({
                    faculties: allFaculty
                }, true)
            } catch (e) {
                console.log("Error " + e)
            }

        },

        _generateModelFaculty: function (name, id, info) {
            return {
                name: name,
                id: id,
                info: info
            }
        },

        getAllStudents: async function () {
            try {
                const modelArray = await HttpHandler.executeGetRequest(Constants.API.STUDENTS);
                let allStudents = [];

                for (let i = 0; i < modelArray.length; i++) {
                    let oStudents = this._generateModelStudents(
                        modelArray[i].id,
                        modelArray[i].user.id,
                        modelArray[i].user.email,
                        modelArray[i].firstName,
                        modelArray[i].lastName,
                        modelArray[i].universityMajor.name,
                        modelArray[i].faculty.name,
                        modelArray[i].year,
                        modelArray[i].semester,
                        modelArray[i].facultyNumber,
                        modelArray[i].group,
                    );

                    allStudents.push(oStudents);
                }

                this.getView().getModel().setData({
                    students: allStudents
                }, true)
            } catch (e) {
                console.log("Error " + e)
            }

        },

        _generateModelStudents: function (id, userId, email, firstName, lastName, universityMajor, faculty, year, semester, facultyNumber, group) {
            return {
                id: id,
                userId: userId,
                email: email,
                firstName: firstName,
                lastName: lastName,
                universityMajor: universityMajor,
                faculty: faculty,
                year: year,
                semester: semester,
                facultyNumber: facultyNumber,
                group: group
            }
        },

        getAllTeachers: async function () {
            try {
                const modelArray = await HttpHandler.executeGetRequest(Constants.API.TEACHERS);
                let allTeachers = [];

                for (let i = 0; i < modelArray.length; i++) {
                    let oTeachers = this._generateModelTeachers(
                        modelArray[i].id,
                        modelArray[i].user.id,
                        modelArray[i].user.email,
                        modelArray[i].firstName,
                        modelArray[i].lastName,
                    );

                    allTeachers.push(oTeachers);
                }

                this.getView().getModel().setData({
                    teachers: allTeachers
                }, true)
            } catch (e) {
                console.log("Error " + e)
            }

        },

        _generateModelTeachers: function (id, userId, email, firstName, lastName) {
            return {
                id: id,
                userId: userId,
                email: email,
                firstName: firstName,
                lastName: lastName,
            }
        },

        getAllAdmins: async function () {
            try {
                const modelArray = await HttpHandler.executeGetRequest(Constants.API.ADMINS);
                let allAdmins = [];

                for (let i = 0; i < modelArray.length; i++) {
                    let oTeachers = this._generateModelTeachers(
                        modelArray[i].id,
                        modelArray[i].user.id,
                        modelArray[i].user.email,
                        modelArray[i].firstName,
                        modelArray[i].lastName,
                    );

                    allAdmins.push(oTeachers);
                }

                this.getView().getModel().setData({
                    admins: allAdmins
                }, true)
            } catch (e) {
                console.log("Error " + e)
            }

        },


        getAllCoursesByMajor: async function (majorId) {
            try {
                var majorId = majorId;
                const modelArray = await HttpHandler.executeGetRequest(`http://localhost:5000/course/major/${majorId}`);
                let allCourseByMajor = [];

                for (let i = 0; i < modelArray.length; i++) {
                    let oCourseByMajor = this._generateModelCourseByMajor(
                        modelArray[i].id,
                        modelArray[i].name,
                    );

                    allCourseByMajor.push(oCourseByMajor);
                }

                this.getView().getModel().setData({
                    courseByMajor: allCourseByMajor
                }, true)
            } catch (e) {
                console.log("Error " + e)
            }

        },

        getAllMainEventsByTimetableId: async function (timetableId) {
            try {
                var timetableId = timetableId;
                const modelArray = await HttpHandler.executeGetRequest(`http://localhost:5000/main/course/event/timetable/${timetableId}`);
                let allMainCourseEventsByTimetable = [];

                for (let i = 0; i < modelArray.length; i++) {
                    let oMainCourseEventsByTimetable = this._generateMainCourseEventByTimetable(
                        modelArray[i].id,
                        modelArray[i].course.name,
                        modelArray[i].start,
                        modelArray[i].end,
                        modelArray[i].location,
                        modelArray[i].day,
                        modelArray[i].teacher.firstName + " " + modelArray[i].teacher.lastName,
                        modelArray[i].teacher.id,
                    );

                    allMainCourseEventsByTimetable.push(oMainCourseEventsByTimetable);
                }

                this.getView().getModel().setData({
                    mainCourseEvents: allMainCourseEventsByTimetable
                }, true)
            } catch (e) {
                console.log("Error " + e)
            }

        },

        _generateModelCourseByMajor: function (id, name) {
            return {
                id: id,
                name: name
            }
        },

        _generateMainCourseEventByTimetable: function (id, course, start, end, location, day, teacher, teacherId) {
            return {
                id: id,
                course: course,
                start: start,
                end: end,
                location: location,
                day: day,
                teacher: teacher,
                teacherId: teacherId
            }
        },

        getAllTimetables: async function () {
            try {
                const modelArray = await HttpHandler.executeGetRequest(Constants.API.TIMETABLES);
                let allTimetables = [];

                for (let i = 0; i < modelArray.length; i++) {
                    let oTimetables = this._generateModelTimetables(
                        modelArray[i].id,
                        modelArray[i].faculty.name,
                        modelArray[i].universityMajor.name,
                        modelArray[i].universityMajor.id,
                        modelArray[i].year,
                        modelArray[i].semester,
                        modelArray[i].group,
                    );

                    allTimetables.push(oTimetables);
                }

                this.getView().getModel().setData({
                    timetables: allTimetables
                }, true)
            } catch (e) {
                console.log("Error " + e)
            }

        },

        _generateModelTimetables: function (id, faculty, universityMajor, universityMajorId, year, semester, group) {
            return {
                id: id,
                faculty: faculty,
                universityMajor: universityMajor,
                universityMajorId: universityMajorId,
                year: year,
                semester: semester,
                group: group
            }
        },

        handleSelectedItemStudent: async function () {
            this.handleSelectedItem('facultyStudent', 'majorStudent');
        },

        handleSelectedItemTimetable: async function () {
            this.handleSelectedItem('selectFacultyTimetable', 'selectMajorTimetable');
        },

        handleSelectedItem: async function (combobox1Id, combobox2Id) {
            this.getView().getModel().setData({
                majors: null
            }, true)

            this.getView().byId(combobox2Id).clearSelection();

            var facultyId = this.getView().byId(combobox1Id).getSelectedKey();
            try {
                const modelArray = await HttpHandler.executeGetRequest(`http://localhost:5000/unimajor/all/faculty/${facultyId}`);
                let majors = [];

                for (let i = 0; i < modelArray.length; i++) {
                    let oFaculty = this._generateModelFaculty(
                        modelArray[i].name,
                        modelArray[i].id,
                        modelArray[i].info,
                    );

                    majors.push(oFaculty);
                }

                this.getView().getModel().setData({
                    majors: majors
                }, true)
            } catch (e) {
                console.log("Error " + e)
            }
        },

        handleAdminSaveButton: async function () {
            let firstName = this.byId("firstnameAdmin").getValue(),
                lastName = this.byId("lastnameAdmin").getValue(),
                email = this.byId("emailAdmin").getValue(),
                password = this.byId("passwordAdmin").getValue();

            let adminDto = {};
            adminDto.firstName = firstName;
            adminDto.lastName = lastName;
            adminDto.email = email;
            adminDto.password = password;

            await HttpHandler.executePostRequestTextResponse(`http://localhost:5000/admin/register`, adminDto);

            this.byId("addAdminDialog").close();

            this.getAllAdmins();
        },

        handleStudentSaveButton: async function () {
            let firstName = this.byId("firstnameStudent").getValue(),
                lastName = this.byId("lastnameStudent").getValue(),
                email = this.byId("emailStudent").getValue(),
                password = this.byId("passwordStudent").getValue(),
                facultyNum = this.byId("facultyNumStudent").getValue(),
                faculty = this.byId("facultyStudent").getSelectedKey(),
                universityMajor = this.byId("majorStudent").getSelectedKey(),
                group = this.byId("groupStudent").getValue(),
                year = this.byId("yearStudent").getSelectedKey(),
                semester = this.byId("semesterStudent").getSelectedKey();

            let studentDto = {};
            studentDto.firstName = firstName;
            studentDto.lastName = lastName;
            studentDto.email = email;
            studentDto.password = password;
            studentDto.facultyNumber = facultyNum;
            studentDto.universityMajor = universityMajor;
            studentDto.faculty = faculty;
            studentDto.group = group;
            studentDto.year = year;
            studentDto.semester = semester;

            await HttpHandler.executePostRequestTextResponse(`http://localhost:5000/student/register`, studentDto);

            this.getAllStudents();
            this.byId("addStudentDialog").close();
        },

        handleStudentEditSaveButton: async function () {
            let studentId = this.getView().getModel().getProperty(this.sPath + "/id");
            let year = this.byId("yearStudentEdit").getSelectedKey(),
                semester = this.byId("semesterStudentEdit").getSelectedKey();
            let studentDto = {};
            studentDto.year = year;
            studentDto.semester = semester;

            await HttpHandler.executePutRequest(`http://localhost:5000/student/update/${studentId}`, studentDto);

            this.byId("editStudentDialog").close();
        },

        handleMainCourseEventEditSaveButton: async function () {
            let teacher = this.byId("allTeachersMainCourseEventEdit").getSelectedKey(),
                location = this.byId("locationMainCourseEventEdit").getValue(),
                day = this.byId("daysMainCourseEventEdit").getSelectedKey(),
                startTime = this.byId("startTimeMainCourseEventEdit"),
                endTime = this.byId("endTimeMainCourseEventEdit");

            let mainCourseEventId = this.getView().getModel().getProperty(this.sPath + "/id");
            var dateEnd = new Date(endTime.getDateValue()).toLocaleTimeString();
            var dateStart = new Date(startTime.getDateValue()).toLocaleTimeString();

            let mainCourseEventDto = {};
            mainCourseEventDto.start = dateStart;
            mainCourseEventDto.end = dateEnd;
            mainCourseEventDto.location = location;
            mainCourseEventDto.day = day;
            mainCourseEventDto.teacherId = teacher;

            await HttpHandler.executePutRequest(`http://localhost:5000/main/course/event/update/${mainCourseEventId}`, mainCourseEventDto);

            this.byId("editMainCourseEventDialog").close();
        },

        handleTeacherSaveButton: async function () {
            let firstName = this.byId("firstnameTeacher").getValue(),
                lastName = this.byId("lastnameTeacher").getValue(),
                email = this.byId("emailTeacher").getValue(),
                password = this.byId("passwordTeacher").getValue();

            let teacherDto = {};
            teacherDto.firstName = firstName;
            teacherDto.lastName = lastName;
            teacherDto.email = email;
            teacherDto.password = password;

            await HttpHandler.executePostRequestTextResponse(`http://localhost:5000/teacher/register`, teacherDto);

            this.byId("addTeacherDialog").close();

            this.getAllTeachers();
        },

        handleTimetableSaveButton: async function () {
            let faculty = this.byId("selectFacultyTimetable").getSelectedKey(),
                universityMajor = this.byId("selectMajorTimetable").getSelectedKey(),
                year = this.byId("selectYearTimetable").getSelectedKey(),
                semester = this.byId("selectSemesterTimetable").getSelectedKey(),
                group = this.byId("groupTimetable").getValue();

            let timetableDto = {};
            timetableDto.faculty = faculty;
            timetableDto.universityMajor = universityMajor;
            timetableDto.year = year;
            timetableDto.semester = semester;
            timetableDto.group = group;

            await HttpHandler.executePostRequestTextResponse(`http://localhost:5000/timetable/add`, timetableDto);

            this.byId("addTimetableDialog").close();
            this.getAllTimetables();

        },
        handleListMainCourseEventPress: async function(oEvent) {
            var oView = this.getView();

            if (!this.oListMainCourseEventDialog) {
                this.oListMainCourseEventDialog = Fragment.load({
                    id: oView.getId(),
                    name: "myuniprogram.fragment.ListMainCourseEvent",
                    controller: this
                }).then(function(oListMainCourseEventDialog){
                    oView.addDependent(oListMainCourseEventDialog);
                    return oListMainCourseEventDialog;
                });
            }

            this.oListMainCourseEventDialog.then(function(oNewListMainCourseEventDialog) {
                this._arrangeDialogListMainEvent(oEvent, oNewListMainCourseEventDialog);
            }.bind(this));
        },

        handleAddMainCourseEventPress: async function(oEvent) {
            var oView = this.getView();

            if (!this.oAddMainCourseEventDialog) {
                this.oAddMainCourseEventDialog = Fragment.load({
                    id: oView.getId(),
                    name: "myuniprogram.fragment.AddMainCourseEvent",
                    controller: this
                }).then(function(oAddMainCourseEventDialog){
                    oView.addDependent(oAddMainCourseEventDialog);
                    return oAddMainCourseEventDialog;
                });
            }

            this.oAddMainCourseEventDialog.then(function(oNewAddMainCourseEventDialog) {
                this._arrangeDialogMainEvent(oEvent, oNewAddMainCourseEventDialog);
            }.bind(this));
        },

        _arrangeDialogMainEvent: function (oEvent, oNewDialog) {
            this._setValuesToMainEventDialog(oEvent);
            oNewDialog.open();
        },

        _arrangeDialogMainCourseEventEdit: function (oEvent, oNewDialog) {
            this._setValuesToMainEventDialogEdit(oEvent);
            oNewDialog.open();
        },

        _arrangeDialogListMainEvent: function (oEvent, oNewDialog) {
            this._setValuesToListMainEventDialog(oEvent);
            oNewDialog.open();
        },

        handleStudentEditPress: async function(oEvent) {
            var oView = this.getView();

            if (!this.oStudentEditDialog) {
                this.oStudentEditDialog = Fragment.load({
                    id: oView.getId(),
                    name: "myuniprogram.fragment.EditStudent",
                    controller: this
                }).then(function(oStudentEditDialog){
                    oView.addDependent(oStudentEditDialog);
                    return oStudentEditDialog;
                });
            }

            this.oStudentEditDialog.then(function(oNewStudentEditDialog) {
                this._arrangeDialog(oEvent, oNewStudentEditDialog);
            }.bind(this));
        },

        _arrangeDialog: function (oEvent, oNewDialog) {
            this._setValuesToDialogContent(oEvent);
            oNewDialog.open();
        },

        _setValuesToDialogContent: function (oEvent) {
            this.sPath = oEvent.getSource().getBindingContext().getPath();
            this.byId("yearStudentEdit").setSelectedKey(this.getView().getModel().getProperty("year", oEvent.getSource().getBindingContext()));
            this.byId("semesterStudentEdit").setSelectedKey(this.getView().getModel().getProperty("semester", oEvent.getSource().getBindingContext()));
        },

        _setValuesToMainEventDialog: function (oEvent) {
            this.sPath = oEvent.getSource().getBindingContext().getPath();
            let majorId =  this.getView().getModel().getProperty(this.sPath + "/universityMajorId");
            this.getView().getModel().setData({
                courseByMajor: null
            }, true);
            this.getAllCoursesByMajor(majorId);
        },

        _setValuesToMainEventDialogEdit: function (oEvent) {
            this.sPath = oEvent.getSource().getBindingContext().getPath();
            this.byId("allTeachersMainCourseEventEdit").setSelectedKey(this.getView().getModel().getProperty("teacherId", oEvent.getSource().getBindingContext()));
            this.byId("locationMainCourseEventEdit").setValue(this.getView().getModel().getProperty("location", oEvent.getSource().getBindingContext()));
            this.byId("daysMainCourseEventEdit").setSelectedKey(this.getView().getModel().getProperty("day", oEvent.getSource().getBindingContext()));
            this.byId("startTimeMainCourseEventEdit").setValue(this.getView().getModel().getProperty("start", oEvent.getSource().getBindingContext()));
            this.byId("endTimeMainCourseEventEdit").setValue(this.getView().getModel().getProperty("end", oEvent.getSource().getBindingContext()));
        },

        _setValuesToListMainEventDialog: function (oEvent) {
            this.sPath = oEvent.getSource().getBindingContext().getPath();
            let timetableId =  this.getView().getModel().getProperty(this.sPath + "/id");
            this.getView().getModel().setData({
                mainCourseEvents: null
            }, true)
            this.getAllMainEventsByTimetableId(timetableId);
        },

        handleUserDeletePress: async function(oEvent) {
            var DialogType = mobileLibrary.DialogType;
            var ButtonType = mobileLibrary.ButtonType;
            if (!this.oApproveDialog) {
                this.oApproveDialog = new Dialog({
                    type: DialogType.Message,
                    title: "Потвърждение",
                    content: new Text({ text: "Сигурни ли сте, че искате да изтриете този запис?" }),
                    beginButton: new Button({
                        type: ButtonType.Emphasized,
                        text: "Изтрий",
                        press: async function () {
                            var userId = this.getView().getModel().getProperty("userId", oEvent.getSource().getBindingContext());
                            await HttpHandler.executeDeleteRequestTextResponse(`http://localhost:5000/user/delete/${userId}`);
                            this.oApproveDialog.close();
                        }.bind(this)
                    }),
                    endButton: new Button({
                        text: "Откажи",
                        press: function () {
                            this.oApproveDialog.close();
                        }.bind(this)
                    })
                });
            }

            this.oApproveDialog.open();
        },

        handleMainCourseEventDeletePress: async function(oEvent) {
            var DialogType = mobileLibrary.DialogType;
            var ButtonType = mobileLibrary.ButtonType;
            if (!this.oMainCourseEventApproveDialog) {
                this.oMainCourseEventApproveDialog = new Dialog({
                    type: DialogType.Message,
                    title: "Потвърждение",
                    content: new Text({ text: "Сигурни ли сте, че искате да изтриете този запис?" }),
                    beginButton: new Button({
                        type: ButtonType.Emphasized,
                        text: "Изтрий",
                        press: async function () {
                            var mainCourseEventId = this.getView().getModel().getProperty("id", oEvent.getSource().getBindingContext());
                            await HttpHandler.executeDeleteRequestTextResponse(`http://localhost:5000/main/course/event/delete/${mainCourseEventId}`);
                            this.oMainCourseEventApproveDialog.close();
                        }.bind(this)
                    }),
                    endButton: new Button({
                        text: "Откажи",
                        press: function () {
                            this.oMainCourseEventApproveDialog.close();
                        }.bind(this)
                    })
                });
            }

            this.oMainCourseEventApproveDialog.open();
        },

        handleMainCourseEventEditPress: async function(oEvent) {
            var oView = this.getView();

            if (!this.oMainCourseEventEditDialog) {
                this.oMainCourseEventEditDialog = Fragment.load({
                    id: oView.getId(),
                    name: "myuniprogram.fragment.EditMainCourseEvent",
                    controller: this
                }).then(function(oMainCourseEventEditDialog){
                    oView.addDependent(oMainCourseEventEditDialog);
                    return oMainCourseEventEditDialog;
                });
            }

            this.oMainCourseEventEditDialog.then(function(oNewMainCourseEventEditDialog) {
                this._arrangeDialogMainCourseEventEdit(oEvent, oNewMainCourseEventEditDialog);
            }.bind(this));
        },

        handleMainCourseEventSaveButton: async function () {
            let courseByMajor = this.byId("courseByMajorMainCourseEvent").getSelectedKey(),
                teacher = this.byId("allTeachersMainCourseEvent").getSelectedKey(),
                location = this.byId("locationMainCourseEvent").getValue(),
                day = this.byId("daysMainCourseEvent").getSelectedKey(),
                startTime = this.byId("startTimeMainCourseEvent"),
                endTime = this.byId("endTimeMainCourseEvent");

            let timetableId =  this.getView().getModel().getProperty(this.sPath + "/id");
            var dateEnd = new Date(endTime.getDateValue()).toLocaleTimeString();
            var dateStart = new Date(startTime.getDateValue()).toLocaleTimeString();

            let mainCourseEventDto = {};
            mainCourseEventDto.start = dateStart;
            mainCourseEventDto.end = dateEnd;
            mainCourseEventDto.location = location;
            mainCourseEventDto.day = day;
            mainCourseEventDto.teacherId = teacher;
            mainCourseEventDto.courseId = courseByMajor;
            mainCourseEventDto.timetableId = timetableId;

            await HttpHandler.executePostRequestTextResponse(`http://localhost:5000/main/course/event/add`, mainCourseEventDto);

            this.byId("addMainCourseEventDialog").close();
        },

        loadAddTeacherDialog: function () {
            var oView = this.getView();

            if (!this.oAddTeacherDialog) {
                this.oAddTeacherDialog = Fragment.load({
                    id: oView.getId(),
                    name: "myuniprogram.fragment.AddTeacher",
                    controller: this
                }).then(function(oAddTeacherDialog){
                    oView.addDependent(oAddTeacherDialog);
                    return oAddTeacherDialog;
                });
            }

            this.oAddTeacherDialog.then(function(oNewAddTeacherDialog) {
                oNewAddTeacherDialog.open();
            });
        },

        loadAddAdminDialog: function () {
            var oView = this.getView();

            if (!this.oAddAdminDialog) {
                this.oAddAdminDialog = Fragment.load({
                    id: oView.getId(),
                    name: "myuniprogram.fragment.AddAdmin",
                    controller: this
                }).then(function(oAddAdminDialog){
                    oView.addDependent(oAddAdminDialog);
                    return oAddAdminDialog;
                });
            }

            this.oAddAdminDialog.then(function(oNewAddAdminDialog) {
                oNewAddAdminDialog.open();
            });
        },

        loadAddStudentDialog: function () {
            var oView = this.getView();

            if (!this.oAddStudentDialog) {
                this.oAddStudentDialog = Fragment.load({
                    id: oView.getId(),
                    name: "myuniprogram.fragment.AddStudent",
                    controller: this
                }).then(function(oAddStudentDialog){
                    oView.addDependent(oAddStudentDialog);
                    return oAddStudentDialog;
                });
            }

            this.oAddStudentDialog.then(function(oNewAddStudentDialog) {
                oNewAddStudentDialog.open();
            });
        },

        loadAddTimetableDialog: function () {
            var oView = this.getView();

            if (!this.oAddTimetableDialog) {
                this.oAddTimetableDialog = Fragment.load({
                    id: oView.getId(),
                    name: "myuniprogram.fragment.AddTimetable",
                    controller: this
                }).then(function(oAddTimetableDialog){
                    oView.addDependent(oAddTimetableDialog);
                    return oAddTimetableDialog;
                });
            }

            this.oAddTimetableDialog.then(function(oNewAddTimetableDialog) {
                oNewAddTimetableDialog.open();
            });
        },

        handleAddTeacherDialogCancelButton: function () {
            this.byId("addTeacherDialog").close();
        },

        handleAddAdminDialogCancelButton: function () {
            this.byId("addAdminDialog").close();
        },

        handleAddStudentDialogCancelButton: function () {
            this.byId("addStudentDialog").close();
        },

        handleAddTimetableDialogCancelButton: function () {
            this.byId("addTimetableDialog").close();
        },

        handleListMainCourseEventCancelButton: function () {
            this.byId("listMainCourseEventDialog").close();
        },

        handleMainCourseEventDialogCancelButton: function () {
            this.byId("addMainCourseEventDialog").close();
        },

        handleStudentEditCancelButton: function () {
            this.byId("editStudentDialog").close();
        },

        handleMainCourseEventEditCancelButton: function () {
            this.byId("editMainCourseEventDialog").close();
        },

        onSearch: function (property, searchField, list) {
            var oView = this.getView(),
                sValue = oView.byId(searchField).getValue(),
                oFilter = new Filter(property, FilterOperator.Contains, sValue);

            oView.byId(list).getBinding("items").filter(oFilter, FilterType.Application);
        },

        onSort: function () {
            var oView = this.getView(),
                aStates = [undefined, "asc", "desc"],
                aStateTextIds = ["sortNone", "sortAscending", "sortDescending"],
                sMessage,
                iOrder = oView.getModel("appView").getProperty("/order");

            iOrder = (iOrder + 1) % aStates.length;
            var sOrder = aStates[iOrder];

            oView.getModel("appView").setProperty("/order", iOrder);
            oView.byId("studentList").getBinding("items").sort(sOrder && new Sorter("LastName", sOrder === "desc"));

            sMessage = this._getText("sortMessage", [this._getText(aStateTextIds[iOrder])]);
            MessageToast.show(sMessage);
        },

        onAvatarPress: function(oEvent) {
            var oView = this.getView();
            var oRouter = UIComponent.getRouterFor(this);
            Common.onAvatarPress(oEvent, oView, oRouter, this);
        },

        onSaveNewPassword: async function (oEvent) {
            Common.onSaveNewPassword(oEvent, this);
        },

        onCancelNewPassword: function (oEvent) {
            Common.onCancelNewPassword(oEvent, this)
        },

    });
});