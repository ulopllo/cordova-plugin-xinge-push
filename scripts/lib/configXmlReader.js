var fs = require('fs');
var path = require('path');
var xmlHelper = require('./xmlHelper.js');
var cordovaContext;
var projectRoot;

module.exports = {
    readOptions: readOptions
};


function readOptions(ctx) {
    var configFilePath = path.join(ctx.opts.projectRoot, 'config.xml');
    var configXmlContent = xmlHelper.readXmlAsJson(configFilePath, true);

    // return configXmlContent;
    return parseConfig(configXmlContent);
}


function parseConfig(configXmlContent) {
    if (!configXmlContent['push-config']) {
        return {};
    }

    return configXmlContent['push-config'];
}