sap.ui.define([
    "sap/ui/core/UIComponent",
    "sap/m/Shell",
    "sap/m/Popover",
    "sap/m/Button",
    "sap/m/Text",
    "sap/ui/core/TextAlign",
    "myuniprogram/handlers/HttpHandler",
    "sap/ui/core/Fragment"
], function (UIComponent, Shell, Popover, Button, Text, TextAlign, HttpHandler, Fragment) {
    'use strict'
    return {
        getModelProperty: function (oScope, sProperty) {
            return oScope.getView().getModel().getProperty(sProperty)
        },

        setModelProperty: function (oScope, sProperty, oValue) {
            oScope.getView().getModel().setProperty(sProperty, oValue)
        },

        getRouter: function (oContext) {
            return UIComponent.getRouterFor(oContext)
        },

        executeInit: function (elementId) {
            new Shell({
                appWidthLimited: false,
                app: new  sap.ui.core.ComponentContainer({
                    name: 'myuniprogram',
                    height: '100%'
                }),
            }).placeAt(elementId)
        },

        onAvatarPress: function(oEvent, oView, oRouter, that) {
            var oAvatar = oView.byId("userAvatar");
            var userName = oView.getModel().getData().userName;
            var oPopover = new Popover({
                showHeader: false,
                placement: "Bottom",
                contentWidth: "200px",
                offsetY: 10,
                content: [
                    new Text({
                        text: userName,
                        textAlign: TextAlign.Center,
                        width: "100%"
                    }),
                    new Button({
                        text: "Смени парола",
                        width: "100%",
                        icon: "sap-icon://key",
                        press: function() {
                            if (!that.oEditPasswordDialog) {
                                that.oEditPasswordDialog = Fragment.load({
                                    id: oView.getId(),
                                    name: "myuniprogram.fragment.EditPassword",
                                    controller: that
                                }).then(function(oEditPasswordDialog){
                                    oView.addDependent(oEditPasswordDialog);
                                    return oEditPasswordDialog;
                                });
                            }

                            that.oEditPasswordDialog.then(function(oNewEditPasswordDialog) {
                               oNewEditPasswordDialog.open();
                            });
                        }
                    }),
                    new Button({
                        text: "Изход",
                        width: "100%",
                        icon: "sap-icon://visits",
                        press: async function() {
                            var userSessionToken = sap.ui.getCore().getModel("loggedInUser").getData().sessionToken;
                            await HttpHandler.executePutRequestWithHeader(`http://localhost:5000/auth/logout`, userSessionToken);
                            oRouter.navTo("loginRoute");
                        }
                    })
                ]
            });

            oPopover.openBy(oAvatar);
        },

        onSaveNewPassword: async function (oEvent, that) {
            var userId = sap.ui.getCore().getModel("loggedInUser").getData().id;
            var oldPassword = that.byId("oldPassword").getValue();
            var newPassword = that.byId("newPassword").getValue();
            let oldPasswordAndNewPasswordDto = {};
            oldPasswordAndNewPasswordDto.oldPassword = oldPassword;
            oldPasswordAndNewPasswordDto.newPassword = newPassword;
            var oResponse = await HttpHandler.executePutRequest(`http://localhost:5000/user/${userId}/change/password`, oldPasswordAndNewPasswordDto);
            console.log("oResponse " + oResponse);
            that.byId("editPasswordDialog").close();
        },

        onCancelNewPassword: function (oEvent, that) {
            that.byId("editPasswordDialog").close();
        },

    }
})