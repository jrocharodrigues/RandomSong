	cast.receiver.logger.setLevelValue(0);
	var receiver = cast.receiver.CastReceiverManager.getInstance();
	console.log('Starting Receiver Manager');
	var ytChannelHandler = receiver.getCastMessageBus(cfg.msgNamespace, cast.receiver.CastMessageBus.MessageType.JSON);
	var channel;
	//ytChannelHandler.addChannelFactory(receiver.createChannelFactory(cfg.msgNamespace));
	ytChannelHandler.addEventListener(
		cast.receiver.CastMessageBus.EventType.MESSAGE,
		onMessage.bind(this)
	);

	receiver.start();

	window.addEventListener('load', function() {
		var tag = document.createElement('script');
      		tag.src = "https://www.youtube.com/iframe_api";
      		var firstScriptTag = document.getElementsByTagName('script')[0];
      		firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
      	});

      	var player;
	ytMessages={
		"setChannel": function(event) {
			channel=event.target;
		},
		"loadVideo": function(event) {
			player.loadVideoById(event.data.videoId);
		},
		"stopCasting": function() {
			endcast();
		},
		"playVideo": function() {
			player.playVideo();
		},
		"pauseVideo": function() {
			player.pauseVideo();
		},
		"stopVideo": function() {
			player.stopVideo();
		},
		"getStatus": function() {
			ytChannelHandler.broadcast({'event':'statusCheck','message':player.getPlayerState()});
		}
	};

  	function onYouTubeIframeAPIReady() {
    	player = new YT.Player('player', {
		//height: 562,
		//width: 1000,
		playerVars: { 'autoplay': 0, 'controls': 0 },
      		events: {
			'onReady': onPlayerReady,
			'onStateChange': onPlayerStateChange
      		}
    	});
	}

	function onPlayerReady() {
		ytChannelHandler.broadcast({'event':'iframeApiReady','message':'ready'});
	}

	function onPlayerStateChange(event) {
		ytChannelHandler.broadcast({'event':'stateChange','message':event.data});
		if (event.data==YT.PlayerState.ENDED) {
			endcast();
		}
	}

	function onMessage(event) {
		console.log('Message [' + event.senderId + ']: ' + event.data);

		ytMessages[event.data.type](event);
		//player.loadVideoById(event.data);
    }

	function endcast() {
		setTimeout(window.close, 2000);		
	}