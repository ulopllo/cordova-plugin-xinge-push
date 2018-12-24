var exec = require('cordova/exec'),
    channel = require('cordova/channel'),

    // Reference name for the plugin
    PLUGIN_NAME = 'XinGePush',

    // Plugin methods on the native side that can be called from JavaScript
    pluginNativeMethod = {
        INIT_PUSH: 'jsInitPush',
        REGISTER_PUSH: 'jsRegisterPush'
    };


channel.onCordovaReady.subscribe(function () {
    ensureCustomEventExists();
    exec(nativeCallback, null, PLUGIN_NAME, pluginNativeMethod.INIT_PUSH, []);
});

function processMessageFromNative(msg) {
    var errorContent = null,
        dataContent = null,
        actionId = null;

    try {
        var resultObj = JSON.parse(msg);
        if (resultObj.hasOwnProperty('error')) {
            errorContent = resultObj.error;
        }
        if (resultObj.hasOwnProperty('data')) {
            dataContent = resultObj.data;
        }
        if (resultObj.hasOwnProperty('action')) {
            actionId = resultObj.action;
        }
    } catch (err) {
    }

    return {
        action: actionId,
        error: errorContent,
        data: dataContent
    };
}

/*
 * Polyfill for adding CustomEvent which may not exist on older versions of Android.
 * See https://developer.mozilla.org/fr/docs/Web/API/CustomEvent for more details.
 */
function ensureCustomEventExists() {
    // Create only if it doesn't exist
    if (window.CustomEvent) {
        return;
    }

    var CustomEvent = function (event, params) {
        params = params || {
            bubbles: false,
            cancelable: false,
            detail: undefined
        };
        var evt = document.createEvent('CustomEvent');
        evt.initCustomEvent(event, params.bubbles, params.cancelable, params.detail);
        return evt;
    };

    CustomEvent.prototype = window.Event.prototype;
    window.CustomEvent = CustomEvent;
}

function callNativeMethod(methodName, options, callback) {
    var innerCallback = function (msg) {
        var resultObj = processMessageFromNative(msg);
        if (callback !== undefined && callback != null) {
            callback(resultObj.error, resultObj.data);
        }
    };

    var sendArgs = [];
    if (options !== null && options !== undefined) {
        sendArgs.push(options);
    }

    exec(innerCallback, null, PLUGIN_NAME, methodName, sendArgs);

}

var xinge = {

    /**
     * 注册推送
     *
     * @param {Callback(error, data)} callback - 当客户端注册完成后回调
     */
    register: function (callback) {
        callNativeMethod(pluginNativeMethod.REGISTER_PUSH, callback);
    }

};

module.exports = xinge;