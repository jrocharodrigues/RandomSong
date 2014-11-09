	var receiver = new cast.receiver.Receiver(cfg.appId, [cfg.msgNamespace],"",5);
	var ytChannelHandler = new cast.receiver.ChannelHandler(cfg.msgNamespace);
	var channel;
	ytChannelHandler.addChannelFactory(receiver.createChannelFactory(cfg.msgNamespace));
	ytChannelHandler.addEventListener(
		cast.receiver.Channel.EventType.MESSAGE,
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
			player.cueVideoById(event.message.videoId);
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
			channel.send({'event':'statusCheck','message':player.getPlayerState()});
		}
	};

  	function onYouTubeIframeAPIReady() {
    	player = new YT.Player('player', {
		height: 562,
		width: 1000,
		playerVars: { 'autoplay': 0, 'controls': 0 },
      		events: {
			'onReady': onPlayerReady,
			'onStateChange': onPlayerStateChange
      		}
    	});
	}

	function onPlayerReady() {
		channel.send({'event':'iframeApiReady','message':'ready'});
	}

	function onPlayerStateChange(event) {
		channel.send({'event':'stateChange','message':event.data});
		if (event.data==YT.PlayerState.ENDED) {
			endcast();
		}
	}

	function onMessage(event) {
		ytMessages[event.message.type](event);
    }

	function endcast() {
		setTimeout(window.close, 2000);		
	}