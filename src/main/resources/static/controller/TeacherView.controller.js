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
        "sap/ui/core/UIComponent",
    ],
    function(coreLibrary, Fragment, Controller, DateFormat, JSONModel, unifiedLibrary, mobileLibrary, MessageToast, UI5Date, HttpHandler, Constants, Common, UIComponent) {
        "use strict";

        var CalendarDayType = unifiedLibrary.CalendarDayType;
        var ValueState = coreLibrary.ValueState;

        return Controller.extend("myuniprogram.controller.TeacherView", {
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
                    buttonVisible: true,
                    teacherVisible: false
                });

                this.getView().setModel(oModel);

                this.getAllTasks();
            },

            getAllTasks: async function () {
                try {
                    this.byId('SPC1').setBusy(true);

                    var userId = sap.ui.getCore().getModel("loggedInUser").getData().id;
                    const response = await HttpHandler.executeGetRequest(`http://localhost:5000/teacher/user/${userId}`);

                    const teacherId = response.id;

                    const teacher = await HttpHandler.executeGetRequest(`http://localhost:5000/teacher/${teacherId}`);
                    this.getView().getModel().setData({
                        userName: teacher.firstName + " " + teacher.lastName
                    }, true)

                    const modelArray = await HttpHandler.executeGetRequest(`http://localhost:5000/teacher/${teacherId}/timetable/events`);

                    let allTasks = [];

                    for (let i = 0; i < modelArray.length; i++) {
                        let oTask = this._generateModelAppointment(
                            modelArray[i].mainCourseEvent.course.name,
                            modelArray[i].location,
                            modelArray[i].mainCourseEvent.course.universityMajor.faculty.name + ", " +  modelArray[i].mainCourseEvent.course.universityMajor.name,
                            new Date (modelArray[i].start),
                            new Date (modelArray[i].end),
                            modelArray[i].id,
                            modelArray[i].info,
                            modelArray[i].timetable.group
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

            _generateModelAppointment: function (course, location, uniMajor, start, end, id, info, group) {
                return {
                    course: course,
                    location: location,
                    uniMajor: uniMajor,
                    start: start,
                    end: end,
                    id: id,
                    info: info,
                    group: group
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

            handleAppointmentDrop: function (oEvent) {
                var oAppointment = oEvent.getParameter("appointment"),
                    oStartDate = oEvent.getParameter("startDate"),
                    oEndDate = oEvent.getParameter("endDate"),
                    bCopy = oEvent.getParameter("copy"),
                    sAppointmentTitle = oAppointment.getTitle(),
                    oModel = this.getView().getModel(),
                    oNewAppointment;

                if (bCopy) {
                    oNewAppointment = {
                        title: sAppointmentTitle,
                        icon: oAppointment.getIcon(),
                        text: oAppointment.getText(),
                        type: oAppointment.getType(),
                        startDate: oStartDate,
                        endDate: oEndDate
                    };
                    oModel.getData().appointments.push(oNewAppointment);
                    oModel.updateBindings();
                } else {
                    oAppointment.setStartDate(oStartDate);
                    oAppointment.setEndDate(oEndDate);
                }

                MessageToast.show("Appointment with title \n'"
                    + sAppointmentTitle
                    + "'\n has been " + (bCopy ? "create" : "moved")
                );
            },

            handleAppointmentResize: function (oEvent) {
                var oAppointment = oEvent.getParameter("appointment"),
                    oStartDate = oEvent.getParameter("startDate"),
                    oEndDate = oEvent.getParameter("endDate"),
                    sAppointmentTitle = oAppointment.getTitle();

                oAppointment.setStartDate(oStartDate);
                oAppointment.setEndDate(oEndDate);

                MessageToast.show("Appointment with title \n'"
                    + sAppointmentTitle
                    + "'\n has been resized"
                );
            },

            handleAppointmentCreateDnD: function(oEvent) {
                var oStartDate = oEvent.getParameter("startDate"),
                    oEndDate = oEvent.getParameter("endDate"),
                    sAppointmentTitle = "New Appointment",
                    oModel = this.getView().getModel(),
                    oNewAppointment = {
                        title: sAppointmentTitle,
                        startDate: oStartDate,
                        endDate: oEndDate
                    };

                oModel.getData().appointments.push(oNewAppointment);
                oModel.updateBindings();

                MessageToast.show("Appointment with title \n'"
                    + sAppointmentTitle
                    + "'\n has been created"
                );
            },

            handleAppointmentSelect: function (oEvent) {
                var oAppointment = oEvent.getParameter("appointment"),
                    oView = this.getView();

                if (oAppointment === undefined) {
                    return;
                }

                if (!oAppointment.getSelected() && this._pDetailsPopover) {
                    this._pDetailsPopover.then(function(oResponsivePopover){
                        oResponsivePopover.close();
                    });
                    return;
                }

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

            handleEditButton: function () {
                var oDetailsPopover = this.byId("detailsPopover");
                oDetailsPopover.close();
                this.sPath = oDetailsPopover.getBindingContext().getPath();
                this._arrangeDialogFragment("Редактиране");
            },

            handlePopoverDeleteButton: function () {
                var oModel = this.getView().getModel(),
                    oAppointments = oModel.getData().appointments,
                    oDetailsPopover = this.byId("detailsPopover"),
                    oAppointment = oDetailsPopover._getBindingContext().getObject();

                oDetailsPopover.close();

                oAppointments.splice(oAppointments.indexOf(oAppointment), 1);
                oModel.updateBindings();
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

            _setValuesToDialogContent: function (oNewAppointmentDialog) {
                var oTitleControl = this.byId("location"),
                    oTextControl = this.byId("moreInfo"),
                    oStartDateControl = this.byId("startDate"),
                    oEndDateControl = this.byId("endDate"),
                    oEmptyError = {errorState:false, errorMessage: ""},
                    oContext,
                    oContextObject,
                    sTitle,
                    sText,
                    oStartDate,
                    oEndDate;


                if (this.sPath) {
                    oContext = this.byId("detailsPopover").getBindingContext();

                    oContextObject = oContext.getObject();
                    sTitle = oContextObject.location;
                    sText = oContextObject.info;
                    oStartDate = oContextObject.start;
                    oEndDate = oContextObject.end;
                } else {
                    sTitle = "";
                    sText = "";
                    if (this._oChosenDayData) {
                        oStartDate = this._oChosenDayData.start;
                        oEndDate = this._oChosenDayData.end;

                        delete this._oChosenDayData;
                    } else {
                        oStartDate = UI5Date.getInstance(oSPCStartDate);
                        oStartDate.setHours(this._getDefaultAppointmentStartHour());
                        oEndDate = UI5Date.getInstance(oSPCStartDate);
                        oEndDate.setHours(this._getDefaultAppointmentEndHour());
                    }
                }

                oTitleControl.setValue(sTitle);
                oTextControl.setValue(sText);
                oStartDateControl.setDateValue(oStartDate);
                oEndDateControl.setDateValue(oEndDate);
                this._setDateValueState(oStartDateControl, oEmptyError);
                this._setDateValueState(oEndDateControl, oEmptyError);
                this.updateButtonEnabledState(oStartDateControl, oEndDateControl, oNewAppointmentDialog.getBeginButton());
            },

            handleDialogSaveButton: async function () {
                let sLocation = this.byId("location").getValue(),
                    sInfo = this.byId("moreInfo").getValue(),
                    oStartDate = this.byId("startDate"),
                    oEndDate = this.byId("endDate"),
                    customEventId = this.getView().getModel().getProperty(this.sPath + "/id");

                var dateEnd = new Date(oEndDate.getValue()).toISOString()
                var dateStart = new Date(oStartDate.getValue()).toISOString()

                if (oStartDate.getValueState() !== ValueState.Error
                    && oEndDate.getValueState() !== ValueState.Error) {

                    if (this.sPath) {
                        let courseEventDto = {};
                        courseEventDto.location = sLocation;
                        courseEventDto.info = sInfo;
                        courseEventDto.start = dateStart;
                        courseEventDto.end = dateEnd;
                        await HttpHandler.executePutRequest(`http://localhost:5000/course/event/update/${customEventId}`, courseEventDto);
                            this.getView().getModel().setProperty(this.sPath + "/end", new Date(dateEnd));
                            this.getView().getModel().setProperty(this.sPath + "/start", new Date(dateStart));
                            this.getView().getModel().setProperty(this.sPath + "/location", sLocation);
                            this.getView().getModel().setProperty(this.sPath  + "/info", sInfo);
                    }
                }
                this.byId("modifyDialog").close();
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

            handleCheckBoxSelect: function (oEvent) {
                var bSelected = oEvent.getSource().getSelected(),
                    sStartDatePickerID = bSelected ? "DTPStartDate" : "DPStartDate",
                    sEndDatePickerID = bSelected ? "DTPEndDate" : "DPEndDate",
                    oOldStartDate = this.byId(sStartDatePickerID).getDateValue(),
                    oNewStartDate = UI5Date.getInstance(oOldStartDate),
                    oOldEndDate = this.byId(sEndDatePickerID).getDateValue(),
                    oNewEndDate = UI5Date.getInstance(oOldEndDate);

                if (!bSelected) {
                    oNewStartDate.setHours(this._getDefaultAppointmentStartHour());
                    oNewEndDate.setHours(this._getDefaultAppointmentEndHour());
                } else {
                    this._setHoursToZero(oNewStartDate);
                    this._setHoursToZero(oNewEndDate);
                }

                sStartDatePickerID = !bSelected ? "DTPStartDate" : "DPStartDate";
                sEndDatePickerID = !bSelected ? "DTPEndDate" : "DPEndDate";
                this.byId(sStartDatePickerID).setDateValue(oNewStartDate);
                this.byId(sEndDatePickerID).setDateValue(oNewEndDate);
            },

            _setHoursToZero: function (oDate) {
                oDate.setHours(0, 0, 0, 0);
            },

            handleAppointmentCreate: function () {
                this._createInitialDialogValues(this.getView().byId("SPC1").getStartDate());
            },

            handleStartDateChange: function (oEvent) {
                var oStartDate = oEvent.getParameter("date");
                MessageToast.show("'startDateChange' event fired.\n\nNew start date is "  + oStartDate.toString());
            },

            updateButtonEnabledState: function (oDateTimePickerStart, oDateTimePickerEnd, oButton) {
                var bEnabled = oDateTimePickerStart.getValueState() !== ValueState.Error
                    && oDateTimePickerStart.getValue() !== ""
                    && oDateTimePickerEnd.getValue() !== ""
                    && oDateTimePickerEnd.getValueState() !== ValueState.Error;

                oButton.setEnabled(bEnabled);
            },

            handleDateTimePickerChange: function(oEvent) {
                var oDateTimePickerStart = this.byId("startDate"),
                    oDateTimePickerEnd = this.byId("endDate"),
                    oStartDate = oDateTimePickerStart.getDateValue(),
                    oEndDate = oDateTimePickerEnd.getDateValue(),
                    oErrorState = {errorState: false, errorMessage: ""};

                if (!oStartDate){
                    oErrorState.errorState = true;
                    oErrorState.errorMessage = "Please pick a date";
                    this._setDateValueState(oDateTimePickerStart, oErrorState);
                } else if (!oEndDate){
                    oErrorState.errorState = true;
                    oErrorState.errorMessage = "Please pick a date";
                    this._setDateValueState(oDateTimePickerEnd, oErrorState);
                } else if (!oEvent.getParameter("valid")){
                    oErrorState.errorState = true;
                    oErrorState.errorMessage = "Invalid date";
                    if (oEvent.getSource() === oDateTimePickerStart){
                        this._setDateValueState(oDateTimePickerStart, oErrorState);
                    } else {
                        this._setDateValueState(oDateTimePickerEnd, oErrorState);
                    }
                } else if (oStartDate && oEndDate && (oEndDate.getTime() <= oStartDate.getTime())){
                    oErrorState.errorState = true;
                    oErrorState.errorMessage = "Start date should be before End date";
                    this._setDateValueState(oDateTimePickerStart, oErrorState);
                    this._setDateValueState(oDateTimePickerEnd, oErrorState);
                } else {
                    this._setDateValueState(oDateTimePickerStart, oErrorState);
                    this._setDateValueState(oDateTimePickerEnd, oErrorState);
                }

                this.updateButtonEnabledState(oDateTimePickerStart, oDateTimePickerEnd, this.byId("modifyDialog").getBeginButton());
            },

            handleDatePickerChange: function () {
                var oDatePickerStart = this.byId("DPStartDate"),
                    oDatePickerEnd = this.byId("DPEndDate"),
                    oStartDate = oDatePickerStart.getDateValue(),
                    oEndDate = oDatePickerEnd.getDateValue(),
                    bEndDateBiggerThanStartDate = oEndDate.getTime() < oStartDate.getTime(),
                    oErrorState = {errorState: false, errorMessage: ""};

                if (oStartDate && oEndDate && bEndDateBiggerThanStartDate){
                    oErrorState.errorState = true;
                    oErrorState.errorMessage = "Start date should be before End date";
                }
                this._setDateValueState(oDatePickerStart, oErrorState);
                this._setDateValueState(oDatePickerEnd, oErrorState);
                this.updateButtonEnabledState(oDatePickerStart, oDatePickerEnd, this.byId("modifyDialog").getBeginButton());
            },

            _setDateValueState: function(oPicker, oErrorState) {
                if (oErrorState.errorState) {
                    oPicker.setValueState(ValueState.Error);
                    oPicker.setValueStateText(oErrorState.errorMessage);
                } else {
                    oPicker.setValueState(ValueState.None);
                }
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
