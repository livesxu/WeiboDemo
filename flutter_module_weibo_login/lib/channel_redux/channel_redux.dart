import 'package:fluttermoduleweibologin/login/weibo_account.dart';
import 'package:redux/redux.dart';
import 'channel_action.dart';
import 'channel_state.dart';

final Reducer<ChannelState> channelReducer = combineReducers([
  new TypedReducer<ChannelState, WeiboLoginSuccessAction>(weiboLoginReducer),
]);

final weiboLoginReducer = (ChannelState state, WeiboLoginSuccessAction data) {

  WeiboAccount.analysisInfo(data.info);

  state.weiboMethodChannel.invokeMethod("access_token",{"access_token":WeiboAccount.share.access_token});

  return state;
};