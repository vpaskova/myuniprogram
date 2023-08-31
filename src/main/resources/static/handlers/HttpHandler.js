sap.ui.define('myuniprogram/handlers/HttpHandler',['myuniprogram/utils/Constants'], function (Constants) {
    'use strict'

    async function executeGetRequest(sUrl) {
        return executeRequestJsonResponse("GET", sUrl)
    }

    async function executeGetRequestTextResponse (sUrl) {
        return executeRequestTextResponse(Constants.REQUEST_TYPE.GET, sUrl)
    }

    async function executePostRequest (sUrl, oBody) {
        return executeRequestWithJsonBody(Constants.REQUEST_TYPE.POST, sUrl, oBody)
    }

    async function executePostRequestTextResponse (sUrl, oBody) {
        return executeRequestWithJsonBodyTextResponse(Constants.REQUEST_TYPE.POST, sUrl, oBody)
    }

    async function executePatchRequestTextResponse (sUrl, oBody) {
        return executeRequestWithJsonBodyTextResponse(Constants.REQUEST_TYPE.PATCH, sUrl, oBody)
    }

    async function executePutRequest (sUrl, oBody) {
        return executeRequestTextResponse(Constants.REQUEST_TYPE.PUT, sUrl, JSON.stringify(oBody))
    }

    async function executePutRequestWithHeader (sUrl, sHeader) {
        return executeRequestTextResponseWithHeader(Constants.REQUEST_TYPE.PUT, sUrl, sHeader)
    }

    async function executePutRequestWithJsonBody (sUrl, oBody) {
        return executeRequestWithJsonBody(Constants.REQUEST_TYPE.PUT, sUrl, oBody)
    }

    async function executeDeleteRequest(sUrl) {
        return executeRequestJsonResponse(Constants.REQUEST_TYPE.DELETE, sUrl)
    }

    async function executeDeleteRequestTextResponse (sUrl) {
        return executeRequestTextResponse(Constants.REQUEST_TYPE.DELETE, sUrl)
    }

    async function executeRequestWithJsonBody (sMethod, sUrl, oBody) {
        return executeRequestJsonResponse(sMethod, sUrl, JSON.stringify(oBody))
    }

    async function executeRequestWithJsonBodyTextResponse(sMethod,sUrl, oBody) {
        return executeRequestTextResponse(sMethod, sUrl, JSON.stringify(oBody))
    }

    async function executeRequestJsonResponse(sMethod, sUrl, sBody) {
        try {
            const response = await executeRequest(sMethod, sUrl, sBody)
            return response.json()
        } catch (e) {
            console.log(e)
            throw e
        }
    }

    async function executeRequestTextResponse(sMethod, sUrl, sBody) {
        try {
            const response = await executeRequest(sMethod, sUrl, sBody)
            return response.text()
        } catch (e) {
            console.log(e)
            throw e
        }
    }

    async function executeRequestTextResponseWithHeader(sMethod, sUrl, sHeader) {
        try {
            const response = await executeRequestWithSessionHeader(sMethod, sUrl, sHeader)
            return response.text()
        } catch (e) {
            console.log(e)
            throw e
        }
    }

    async function handleStatus(response) {
        if (!response.ok) {
            throw new Error(`Received statusCode : ${response.status}`)
        }
    }

    function executeRequestWithSessionHeader(sMethod, sUrl, sHeader) {
        return fetch(sUrl, {
            //credentials: 'include',
            method: sMethod,
            headers: {
                'Content-Type': 'application/json',
                'session-token': `${sHeader}`
            },
        })
    }
    function executeRequest(sMethod, sUrl, sBody) {
        return fetch(sUrl, {
            // credentials: 'include',
            method: sMethod,
            body: sBody,
            headers: {
                'Content-Type': 'application/json',
            },
        })
    }

    return {
        executeGetRequest: executeGetRequest,
        executePostRequest: executePostRequest,
        executePutRequest: executePutRequest,
        executeDeleteRequest:  executeDeleteRequest,
        executeDeleteRequestTextResponse: executeDeleteRequestTextResponse,
        executeGetRequestTextResponse: executeGetRequestTextResponse,
        executePostRequestTextResponse: executePostRequestTextResponse,
        executePatchRequestTextResponse: executePatchRequestTextResponse,
        executePutRequestWithJsonBody: executePutRequestWithJsonBody,
        executePutRequestWithHeader: executePutRequestWithHeader
    }
})