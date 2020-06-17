import 'package:flutter/services.dart';
import 'package:fluttermoduleweibologin/login/weibo_account.dart';

class ChannelState {

  MethodChannel weiboMethodChannel;
  EventChannel weiboEventChannel;

  ChannelState({this.weiboMethodChannel,EventChannel weiboEventChannel}) {

    if (weiboEventChannel != null && this.weiboEventChannel == null) {

      this.weiboEventChannel = weiboEventChannel;

      Map access_tokenMap = {"access_token":""};
      if (WeiboAccount.isLogin && WeiboAccount.share.access_token != null) {

        access_tokenMap = {"access_token":WeiboAccount.share.access_token};
      }

      this.weiboEventChannel.receiveBroadcastStream(access_tokenMap).listen((value){


      });
    }
  }
}