sap.ui.define([
    "myuniprogram/utils/Common",
], function (Common) {
    'use strict'
    return {
        initApp: function (elementId) {
            Common.executeInit(elementId)
        }
    }
})