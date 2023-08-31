sap.ui.define([
        "sap/ui/core/library",
        "sap/ui/core/Fragment",
        "sap/ui/core/mvc/Controller",
        "sap/ui/core/format/DateFormat",
        "sap/ui/model/json/JSONModel",
        "sap/ui/unified/library",
        "sap/m/library",
        "sap/m/MessageToast",
        "sap/ui/core/date/UI5Date",
        "myuniprogram/handlers/HttpHandler",
        "myuniprogram/utils/Constants",
        "myuniprogram/utils/Common",
        "sap/ui/core/UIComponent"
    ],
    function(coreLibrary, Fragment, Controller, DateFormat, JSONModel, unifiedLibrary, mobileLibrary, MessageToast, UI5Date, HttpHandler, Constants, Common, UIComponent) {
        "use strict";

        var CalendarDayType = unifiedLibrary.CalendarDayType;
        var ValueState = coreLibrary.ValueState;

        return Controller.extend("myuniprogram.controller.StudentView", {
            onInit: function() {
                var oModel = new JSONModel();
                oModel.setData({
                    supportedAppointmentItems: [
                        {
                            text: "Team Meeting",
                            type: CalendarDayType.Type01
                        },
                        {
                            text: "Personal",
                            type: CalendarDayType.Type05
                        },
                        {
                            text: "Discussions",
                            type: CalendarDayType.Type08
                        },
                        {
                            text: "Out of office",
                            type: CalendarDayType.Type09
                        },
                        {
                            text: "Private meeting",
                            type: CalendarDayType.Type03
                        }
                    ],
                    buttonVisible: false,
                    teacherVisible: true,
                });

                this.getView().setModel(oModel);

                oModel = new JSONModel();
                oModel.setData({allDay: false});
                this.getView().setModel(oModel, "allDay");

                this.getAllTasks();
            },

            getAllTasks: async function () {
                try {
                    this.byId('SPC1').setBusy(true);

                     var userId = sap.ui.getCore().getModel("loggedInUser").getData().id;
                     const response = await HttpHandler.executeGetRequest(`http://localhost:5000/student/user/${userId}`);

                     const studentId = response.id;

                    const student = await HttpHandler.executeGetRequest(`http://localhost:5000/student/${studentId}`);
                    this.getView().getModel().setData({
                        userName: student.firstName + " " + student.lastName
                    }, true)

                    const timetable = await HttpHandler.executeGetRequest(`http://localhost:5000/student/${studentId}/timetable`);
                    this.getView().getModel().setData({
                        timetableInfo: "Факултет: " + timetable.faculty.name + ", Специалност: " + timetable.universityMajor.name +
                            ", Курс: " + timetable.year + ", Семестър: " + timetable.semester + ", Група: " + timetable.group
                    }, true)

                    const modelArray = await HttpHandler.executeGetRequest(`http://localhost:5000/student/${studentId}/timetable/events`);

                    let allTasks = [];

                    for (let i = 0; i < modelArray.length; i++) {
                        let oTask = this._generateModelAppointment(
                            modelArray[i].mainCourseEvent.course.name,
                            modelArray[i].location,
                            modelArray[i].mainCourseEvent.teacher.firstName + " " + modelArray[i].mainCourseEvent.teacher.lastName,
                            new Date (modelArray[i].start),
                            new Date (modelArray[i].end),
                            modelArray[i].info === null ? " " :  modelArray[i].info
                        );

                        allTasks.push(oTask);
                    }

                    this.getView().getModel().setData({
                        appointments: allTasks
                    }, true)
                    this.byId('SPC1').setBusy(false);
                } catch (e) {
                    console.log("Error " + e)
                }

            },

            _generateModelAppointment: function (course, location, teacher, start, end, info) {
                return {
                    course: course,
                    location: location,
                    teacher: teacher,
                    start: start,
                    end: end,
                    info: info
                }
            },

            _typeFormatter: function(sType) {
                var sTypeText = "",
                    aTypes = this.getView().getModel().getData().supportedAppointmentItems;

                for (var  i = 0; i < aTypes.length; i++){
                    if (aTypes[i].type === sType){
                        sTypeText = aTypes[i].text;
                    }
                }

                if (sTypeText !== ""){
                    return sTypeText;
                } else {
                    return sType;
                }
            },

            handleAppointmentSelect: function (oEvent) {
                var oAppointment = oEvent.getParameter("appointment"),
                    oStartDate,
                    oEndDate,
                    oTrimmedStartDate,
                    oTrimmedEndDate,
                    bAllDate,
                    oModel,
                    oView = this.getView();

                if (oAppointment === undefined) {
                    return;
                }

                oStartDate = oAppointment.getStartDate();
                oEndDate = oAppointment.getEndDate();
                oTrimmedStartDate = UI5Date.getInstance(oStartDate);
                oTrimmedEndDate = UI5Date.getInstance(oEndDate);
                bAllDate = false;
                oModel = this.getView().getModel("allDay");

                if (!oAppointment.getSelected() && this._pDetailsPopover) {
                    this._pDetailsPopover.then(function(oResponsivePopover){
                        oResponsivePopover.close();
                    });
                    return;
                }

                this._setHoursToZero(oTrimmedStartDate);
                this._setHoursToZero(oTrimmedEndDate);

                if (oStartDate.getTime() === oTrimmedStartDate.getTime() && oEndDate.getTime() === oTrimmedEndDate.getTime()) {
                    bAllDate = true;
                }

                oModel.getData().allDay = bAllDate;
                oModel.updateBindings();

                if (!this._pDetailsPopover) {
                    this._pDetailsPopover = Fragment.load({
                        id: oView.getId(),
                        name: "myuniprogram.fragment.EventDetails",
                        controller: this
                    }).then(function(oResponsivePopover){
                        oView.addDependent(oResponsivePopover);
                        return oResponsivePopover;
                    });
                }
                this._pDetailsPopover.then(function (oResponsivePopover) {
                    oResponsivePopover.setBindingContext(oAppointment.getBindingContext());
                    oResponsivePopover.openBy(oAppointment);
                });
            },

            handleMoreLinkPress: function(oEvent) {
                var oDate = oEvent.getParameter("date"),
                    oSinglePlanningCalendar = this.getView().byId("SPC1");

                oSinglePlanningCalendar.setSelectedView(oSinglePlanningCalendar.getViews()[2]);

                this.getView().getModel().setData({ startDate: oDate }, true);
            },


            _arrangeDialogFragment: function (sTitle) {
                var oView = this.getView();

                if (!this._pNewAppointmentDialog) {
                    this._pNewAppointmentDialog = Fragment.load({
                        id: oView.getId(),
                        name: "myuniprogram.fragment.EditEvent",
                        controller: this
                    }).then(function(oNewAppointmentDialog){
                        oView.addDependent(oNewAppointmentDialog);
                        return oNewAppointmentDialog;
                    });
                }

                this._pNewAppointmentDialog.then(function(oNewAppointmentDialog) {
                    this._arrangeDialog(sTitle, oNewAppointmentDialog);
                }.bind(this));
            },

            _arrangeDialog: function (sTitle, oNewAppointmentDialog) {
                this._setValuesToDialogContent(oNewAppointmentDialog);
                oNewAppointmentDialog.setTitle(sTitle);
                oNewAppointmentDialog.open();
            },

            formatDate: function (oDate) {
                if (oDate) {
                    var iHours = oDate.getHours(),
                        iMinutes = oDate.getMinutes(),
                        iSeconds = oDate.getSeconds();

                    if (iHours !== 0 || iMinutes !== 0 || iSeconds !== 0) {
                        return DateFormat.getDateTimeInstance({ style: "medium" }).format(oDate);
                    } else  {
                        return DateFormat.getDateInstance({ style: "medium" }).format(oDate);
                    }
                }
            },

            handleDialogCancelButton:  function () {
                this.sPath = null;
                this.byId("modifyDialog").close();
            },

            _setHoursToZero: function (oDate) {
                oDate.setHours(0, 0, 0, 0);
            },

            handleStartDateChange: function (oEvent) {
                var oStartDate = oEvent.getParameter("date");
                MessageToast.show("'startDateChange' event fired.\n\nNew start date is "  + oStartDate.toString());
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
