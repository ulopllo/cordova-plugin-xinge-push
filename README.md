# cordova-plugin-xinge-push
cordova plugin 集成信鸽推送

## Supported platforms

- Android 4.4 or above.

### Installation

```sh
cordova plugin add https://github.com/zhoumo99133/cordova-plugin-xinge-push.git
```

### Quick start guide


1. 创建一个cordova项目，并添加Android平台:

  ```sh
  cordova create TestProject com.example.testproject TestProject
  cd ./TestProject
  cordova platform add android
  ```
  或者使用已有项目

2. 项目添加xml2js依赖

  ```sh
  npm install xml2js
  ```

3. 添加推送平台配置项,修改根目录当config.xml,添加各个平台的id和key，如下：
  ```xml
    <push-config>
        <xg id="111" key="21123"/> //信鸽
        <mz id="312" key="33"/> //魅族
        <xm id="312" key="33"/> //小米
        <hw id="312"/> //华为
    </push-config>
  ```

4. 添加插件:

  ```sh
  cordova plugin add https://github.com/zhoumo99133/cordova-plugin-xinge-push.git
  ```

