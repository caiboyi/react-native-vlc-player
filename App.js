/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {NativeModules, NativeEventEmitter,DeviceEventEmitter,Dimensions} from 'react-native';
import { play } from 'react-native-vlc-player'

var VideoView = require('./VlcPlayerView');


import {
  Platform,
  StyleSheet,
  Text,
  View
} from 'react-native';

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' +
    'Cmd+D or shake for dev menu',
  android: 'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

type Props = {};
export default class App extends Component<Props> {
  constructor(props) {
    super(props);
    const windowwidth = Dimensions.get('window').width;
    const windowheight = Dimensions.get('window').height;
    this.state = {
      width: windowwidth,
      height: 200
    }
  }

	video = null;
	subscription = null;
  
	_press_play =()=>{
			  if (this.subscription){
		  this.subscription.remove();
	  }
		NativeModules.ToastExample.show('click play video');
		this.video.play();
		this.subscription = DeviceEventEmitter.addListener('VideoControll', function  (msg) {
			console.log(msg);
			NativeModules.ToastExample.show('play action:'+msg['action']);
        }); 
		
  }
  
	_press_pause =()=>{
			  if (this.subscription != undefined){
		  this.subscription.remove();
	  }
		NativeModules.ToastExample.show('click pause video');
		this.video.pause();
		this.subscription = DeviceEventEmitter.addListener('VideoControll', function  (msg) {
			console.log(msg);
			NativeModules.ToastExample.show('pause action:'+msg['action']);
        });
  }
	_press_capture =()=>{
			  if (this.subscription != undefined){
		  this.subscription.remove();
	  }
		NativeModules.ToastExample.show('click capture video');
		this.video.capture();
		this.subscription = DeviceEventEmitter.addListener('VideoControll', function  (msg) {
			console.log(msg);
			NativeModules.ToastExample.show('capture action:'+msg['action']);
        });
  }
  _press_full_screen =()=>{
    const windowwidth = Dimensions.get('window').width;
    const windowheight = Dimensions.get('window').height + 25;  
    

    this.setState({
      width: windowheight,
      height: windowwidth
    })

	  if (this.subscription != undefined){
		  this.subscription.remove();
	  }
	NativeModules.ToastExample.show('click full screen video');
	this.video.full_screen();
	this.subscription = DeviceEventEmitter.addListener('VideoControll', function  (msg) {
		console.log("windowwidth : "+windowwidth + " windowheight : "+windowheight);
		NativeModules.ToastExample.show('full_screen action:'+msg['action']);
    });
	DeviceEventEmitter.addListener('VideoError', function  (msg) {
		console.log(msg);
		NativeModules.ToastExample.show('full_screen action load_ing:'+msg['load_ing']+", load_success:"+msg['load_ing']);
    });
  }

  _press_exit_full_screen =()=>{
    this.state = {
      width: 360,
      height: 200
    }
    if (this.subscription != undefined){
      this.subscription.remove();
    }
  NativeModules.ToastExample.show('click exit full screen video');
  this.video.exit_full_screen();
  this.subscription = DeviceEventEmitter.addListener('VideoControll', function  (msg) {
    console.log(msg);
    NativeModules.ToastExample.show('exit_full_screen action:'+msg['action']);
    });
  DeviceEventEmitter.addListener('VideoError', function  (msg) {
    console.log(msg);
    NativeModules.ToastExample.show('exit_full_screen action load_ing:'+msg['load_ing']+", load_success:"+msg['load_ing']);
    });
  }

  _press_record_start =()=>{
    if (this.subscription != undefined){
      this.subscription.remove();
    }
  NativeModules.ToastExample.show('click set record_start video');
  this.video.record_start();
  this.subscription = DeviceEventEmitter.addListener('VideoControll', function  (msg) {
    console.log(msg);
    NativeModules.ToastExample.show('set  record_start action:'+msg['action']);
    });
  DeviceEventEmitter.addListener('VideoError', function  (msg) {
    console.log(msg);
    NativeModules.ToastExample.show('set record_start action load_ing:'+msg['load_ing']+", load_success:"+msg['load_ing']);
    });
  }

  _press_record_end =()=>{
    if (this.subscription != undefined){
      this.subscription.remove();
    }
  NativeModules.ToastExample.show('click record_end video');
  this.video.record_end();
  this.subscription = DeviceEventEmitter.addListener('VideoControll', function  (msg) {
    console.log(msg);
    NativeModules.ToastExample.show('set record_end action:'+msg['action']);
    });
  DeviceEventEmitter.addListener('VideoError', function  (msg) {
    console.log(msg);
    NativeModules.ToastExample.show('set record_end  load_ing:'+msg['load_ing']+", load_success:"+msg['load_ing']);
    });
  }


  
  render() {  
    return (
  <View style={styles.container}>

    
        
    <VideoView 
      ref={(video)=>{this.video = video}}
      style={ {width: this.state.width, height: this.state.height}}
      url={'rtsp://admin:admin_123@10.17.5.149:443/Streaming/Channels/101/1/4'}>
    </VideoView>  
        
		<Text style={styles.instructions} onPress={this._press_play}>
          播放
        </Text>
		<Text style={styles.instructions} onPress={this._press_pause}>
          暂停123
        </Text>
		<Text style={styles.instructions}  onPress={this._press_capture}>
          截图
        </Text>
		<Text style={styles.instructions}>
          錄屏
        </Text>
		<Text style={styles.instructions} onPress={this._press_full_screen}>
          全屏
        </Text>

    <Text style={styles.instructions} onPress={this._press_exit_full_screen}>
          退出全屏
    </Text>
     <Text style={styles.instructions} onPress={this._press_record_start}>
          开始录频
    </Text> 
    <Text style={styles.instructions} onPress={this._press_record_end}>
          停止录频
    </Text>   

     <Text onPress={()=> this.getStatusActionBarHeight()}>测试</Text>

      </View>
    );
  }


   async test() {
      try {
        var {
            height,
        } = await NativeModules.ScreenModule.testAc("hello");

        console.log("height : "+height);  
      } catch (e) {
        console.error(e);
      }
    }


    /**
    *获取状态栏和导航栏高度
    */
    getStatusActionBarHeight(){
      NativeModules.ScreenModule.getActionBarHeight(true).then(e=>{
          console.log("height : "+e.height);
        }).catch(error=>{
          console.log(error);
        });

    }


}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  }
});
