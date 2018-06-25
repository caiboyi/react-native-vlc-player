### React-Native-VLC-Player

> VLC Player for react-native

*Only Android support now.*

![](https://media.giphy.com/media/l4hLFPgXI7ipAAMGk/giphy.gif)

#### Integrate

##### Android

须在Application中初始化视频sdk 
 VideoMgr.initIVMSSDK(this);

 如果要添加原生控件须在Activity中加react-native页面和初始化
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
...
include ':libvlc'
project(':libvlc').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-vlc-player/android/libvlc')

include ':react-native-vlc-player'
project(':react-native-vlc-player').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-vlc-player/android/vlc')
```

##### Add `android/app/build.gradle`
```
...
dependencies {
    ...
    compile project(':react-native-vlc-player')
}
```
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

#### LICENSE
MIT
