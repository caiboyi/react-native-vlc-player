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


    /**获取高度*/
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
  url : 'rtsp://admin:admin_123@10.17.5.149:443/Streaming/Channels/101/1/4',
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