import 'package:shared_preferences/shared_preferences.dart';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'weibo_login.dart';

mixin WeiboLoginAction {

  void judgeLogin (BuildContext context,VoidCallback voidAction) async {

    if (WeiboAccount.isLogin == false) {

      final backInfo = await Navigator.of(context).push(MaterialPageRoute(builder:(_) => WeiboLogin()));

      print("back_____" + backInfo.toString());
      if (backInfo != null && voidAction != null && backInfo['success'] == true) {

        voidAction();
      }
    } else if (voidAction != null) {

      voidAction();
    }
  }
}

class WeiboAccount {

  static bool isLogin = false;

  static WeiboAccountModel _share;
  static WeiboAccountModel get share {

    if (_share == null)  {

      _share = WeiboAccountModel();
    }
    return _share;
  }

  //读取数据
  static readInfo (SharedPreferences prefs) async {

    if (_share != null && _share.access_token != null) {

      return ;
    }

    if (prefs == null) {

      prefs = await SharedPreferences.getInstance();
    }

    String accountInfo = prefs.get('weibo_account');

    if (accountInfo == null || accountInfo.length == 0) { //如果不存在就返回空

      _share = WeiboAccountModel();
    } else {

      _share = WeiboAccountModel.fromJson(json.decode(accountInfo));
      isLogin = true;
    }
  }

  //储存数据
  static writeInfo() async {

    SharedPreferences prefs = await SharedPreferences.getInstance();

    prefs.setString('weibo_account', json.encode(_share));

  }

  //清理登录信息
  static clearInfo() async {

    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setString('weibo_account', '');

    _share = WeiboAccountModel();
    isLogin = false;
  }

  //解析存储数据
  static analysisInfo(Map info){

    _share = WeiboAccountModel.fromJson(info);
    isLogin = true;
    WeiboAccount.writeInfo();
  }
}

class WeiboAccountModel {

  WeiboAccountModel();

  String access_token;
  int expires_in;//access_token的生命周期，单位是秒数。
  String uid;//授权用户的UID

  WeiboAccountModel.fromJson(Map json)
      : access_token = json['access_token'],
        expires_in = json['expires_in'],
        uid = json['uid']
        ;

  Map toJson() =>
      {
        'access_token':access_token,
        'expires_in':expires_in,
        'uid':uid,
      };
}