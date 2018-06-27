### React-Native-VLC-Player

> VLC Player for react-native

*Only Android support now.*

![](https://media.giphy.com/media/l4hLFPgXI7ipAAMGk/giphy.gif)

#### Integrate

##### Android

 如果在MainApplication初始化须加在

 @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),new VLCPlayerPackage()
      );
    }


 如果是在Activity中加react-native页面和初始化
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        tvToolBar = (TextView) findViewById(R.id.tvToolBar);
        List<ReactPackage> list = Arrays.<ReactPackage>asList(
                new MainReactPackage(),
                new AnExampleReactPackage(),
                new VLCPlayerPackage(),
                new ScreenPackage(tvToolBar)
        );

        ReactRootView mReactRootView = new ReactRootView(this);
        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setBundleAssetName("index.bundle")
                .setJSMainModulePath("index")
                .addPackages(list)
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
        //这里的ReactNativeView对应index.android.js中AppRegistry.registerComponent('ReactNativeView', () => Root)的ReactNativeView
        mReactRootView.startReactApplication(mReactInstanceManager, "AwesomeProject", null);
        layout.addView(mReactRootView);
    }

##### Install via npm
`npm i react-native-vlc-player --save`

##### Add dependency to `android/settings.gradle`
```
include ':vlc'
project(':vlc').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-vlc-player/android/vlc')
```

##### Add `android/app/build.gradle`
```
...
dependencies {
    ...
    compile project(':vlc')
}
```
##### Add 'android/build.gradle'

...
allprojects {
    repositories {
        ...
        flatDir {
            // 由于Library module中引用了 youkuplayer 库的 aar，在多 module 的情况下，
            // 其他的module编译会报错，所以需要在所有工程的repositories
            // 下把Library module中的libs目录添加到依赖关系中
            dirs project(':vlc').file('libs')
        }
    }
}

...

##### Register module in `MainApplication.java`
```Java
import com.ghondar.vlcplayer.*;  // <--- import

@Override
 protected List<ReactPackage> getPackages() {
   return Arrays.<ReactPackage>asList(
      new VLCPlayerPackage(),  // <------- here
      new MainReactPackage()
   );
 }
```

#### Usage

```Javascript
import React, { AppRegistry, StyleSheet, Component, View, Text, TouchableHighlight } from 'react-native'

import { play } from 'react-native-vlc-player'

class Example extends Component {
  constructor(props, context) {
    super(props, context)
  }

  render() {

    return (
      <View style={styles.container}>

        <TouchableHighlight
          onPress={() => { play('file:///storage/emulated/0/example.avi') }}>
            <Text >Play Video!</Text>
        </TouchableHighlight>

      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  }
});

AppRegistry.registerComponent('example', () => Example);
```

###### App.js

var VideoView = require('./VlcPlayerView');

export default class App extends Component<Props> {
render() {  
    return (
  <View style={styles.container}>
   
    <VideoView 
      ref={(video)=>{this.video = video}}
      style={ {width: this.state.width, height: this.state.height}}
      url={'rtsp://xxx:xxx@1xxxxxx/Streaming/Channels/101/1/4'}
      username = {'xxxx'}
      password = {'xxxxx'}
      ip = {'xxxxxx'}
      camera_parent_node = {1}
      cameraid = {'4'}
      show_actionbar={true}
      >
    </VideoView>  
      </View>
    );
  }


  }

###### 视频接口文件 VlcPlayerView.js

//VlcPlayerView

import PropTypes from 'prop-types'
import React,{Component} from 'react';
import {
    requireNativeComponent,
    View,
    UIManager,
    findNodeHandle,
} from 'react-native';

var RCT_VIDEO_REF = 'VideoView';

class VideoView extends Component{
  
  constructor(props){
    super(props);
  }
  
  /**
    * 播放
  */
  play=()=>{
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.play,
      null
    );
  }
  
  /**停止播放*/
  stop=()=>{
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.stop,
      null
    );
  }
  
  /**全屏*/
  full_screen=()=>{
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.full_screen,
      null
    );
  }
  
  /**退出全屏*/
   exit_full_screen=()=>{
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.exit_full_screen,
      null
    );
  }

    /**抓拍*/
  capture=()=>{
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.capture,
      null
    );
  }

    /**高清*/
  hight_stream=()=> {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.hight_stream,
      null
    );
  }
  /**标清*/
  stande_stream=()=> {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.stande_stream,
      null
    );
  }
  /**流畅*/
  fluency_stream=()=> {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.fluency_stream,
      null
    );
  }

    /**向左*/
    left=()=> {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.left,
      null
    );
  }
  /**向右*/
  right=()=> {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.right,
      null
    );
  }

  /**向上*/
  up=()=> {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.up,
      null
    );
  }

  /**向下*/
  down=()=> {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.down,
      null
    );
  }

  /**内焦距*/
  in_focal_length=()=> {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.in_focal_length,
      null
    );
  }

  /**外焦距*/
  out_focal_length=()=> {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.out_focal_length,
      null
    );
  }

    /**近焦点*/
  near_focus=()=> {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.near_focus,
      null
    );
  }


    /**远焦点*/
  far_focus=()=> {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.far_focus,
      null
    );
  }
  /**开始录频*/
    record_start=()=> {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.record_start,
      null
    );
  }

  /**停止录频*/
  record_end=()=> {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.record_end,
      null
    );
  }


    /**获取状态栏和标题栏高度*/
  get_actionbar_height=()=> {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.VideoView.Commands.actionbar_height,
      null
    );

  }

  render(){

        return <RCTVideoView
            {...this.props}
            ref = {RCT_VIDEO_REF}
        />;
    };
}

VideoView.name = "VideoView";

VideoView.defaultProps = {
  url : 'rtsp://xxxx:xxxx@xxxxxx:443/Streaming/Channels/101/1/4',
  username : 'admin',
  password : 'admin_123',
  ip : '10.17.5.149:443',
  camera_parent_node : 1,
  cameraid : '4',
  show_actionbar : false
};

VideoView.propTypes = {
  style: PropTypes.style,
  url : PropTypes.string.isRequired,
  username : PropTypes.string.isRequired,
  password : PropTypes.string.isRequired,
  ip : PropTypes.string.isRequired,
  camera_parent_node : PropTypes.number.isRequired,
  cameraid : PropTypes.string.isRequired,
  show_actionbar : PropTypes.bool,
  ...View.propTypes,
};

var RCTVideoView = requireNativeComponent('VideoView',VideoView,{
    nativeOnly: {onChange: true}
});
module.exports = VideoView;


// var oface = {
  // name : 'VlcPlayerView',
  // propTypes : {
    // url : PropTypes.string,
  // },
// }


// module.exports = requireNativeComponent('VlcPlayerView', oface);


调用获取状态栏和标题高度返回值要注册回调监听
this.subscription = DeviceEventEmitter.addListener('VideoControll', function  (param) {

        var actionBarHeight = param['getheight'];
        console.log("getheight : "+actionBarHeight);
      });


#### LICENSE
MIT
