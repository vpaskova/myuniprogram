sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/m/MessageToast",
    "myuniprogram/handlers/HttpHandler",
    "sap/ui/model/json/JSONModel",
    "sap/ui/core/mvc/XMLView",
    "sap/ui/core/UIComponent"
], function(Controller, MessageToast, HttpHandler, JSONModel, XMLView, UIComponent) {
    "use strict";

    return Controller.extend("myuniprogram.controller.LoginView", {

        onLoginPress: async function() {
            var userEmail = this.getView().byId("user-email").getValue();
            var userPassword = this.getView().byId("user-password").getValue();

            let emailAndPasswordDto = {};
            emailAndPasswordDto.email = userEmail;
            emailAndPasswordDto.password = userPassword;

            var oResponse = await HttpHandler.executePutRequestWithJsonBody(`http://localhost:5000/auth/login`, emailAndPasswordDto);

            if (oResponse.status === undefined) {
                MessageToast.show("Успешен вход!");

                var loggedInUser = {
                    id: oResponse.id,
                    sessionToken: oResponse.sessionToken,
                    password: oResponse.password,
                    email: oResponse.email,
                    role: oResponse.role
                };

                var oModel = new JSONModel(loggedInUser);
                sap.ui.getCore().setModel(oModel, "loggedInUser");

                var oRouter = UIComponent.getRouterFor(this);
                if(oResponse.role === "STUDENT") {
                    oRouter.navTo("studentRoute");
                } else if (oResponse.role === "TEACHER") {
                    oRouter.navTo("teacherRoute");
                } else if (oResponse.role === "ADMIN") {
                    oRouter.navTo("adminRoute");
                }
            } else {
                MessageToast.show("Невалидни email или парола!");
            }
        },

    });
});
