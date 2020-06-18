import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttermoduleweibologin/channel_redux/channel_redux.dart';
import 'package:fluttermoduleweibologin/channel_redux/channel_state.dart';
import 'package:fluttermoduleweibologin/login/weibo_account.dart';
import 'package:redux/redux.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:fluttermoduleweibologin/login/weibo_login.dart';

void main() async {

//  If you're running an application and need to access the binary messenger before `runApp()` has been called (for example, during plugin initialization),
//  then you need to explicitly call the `WidgetsFlutterBinding.ensureInitialized()` first.
  WidgetsFlutterBinding.ensureInitialized();

  await WeiboAccount.readInfo(null);

  Store store = Store<ChannelState>(channelReducer,initialState: ChannelState(
      weiboMethodChannel: MethodChannel("weibo_method"),
      weiboEventChannel: EventChannel("weibo_event")
  ));

  runApp(MyApp(store: store,));
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.

  Store store;

  MyApp({this.store});

  @override
  Widget build(BuildContext context) {
    return StoreProvider<ChannelState>(
      store: store,
      child: MaterialApp(
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        home: WeiboLogin(),
      ),
    );
  }
}
