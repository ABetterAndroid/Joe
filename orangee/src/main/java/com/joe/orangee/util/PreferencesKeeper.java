/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.joe.orangee.util;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.joe.orangee.model.User;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class PreferencesKeeper {
    public static final String PREFERENCES_NAME = "orangee_pref";

    private static final String KEY_UID           = "uid";
    private static final String KEY_ACCESS_TOKEN  = "access_token";
    private static final String KEY_EXPIRES_IN    = "expires_in";
    private static final String KEY_START_MARK    = "start_mark";
    public static final String KEY_OFFSET_Y    = "offset_y";
    public static final String KEY_LISTVIEW_POSTION    = "listview_postion";
   
    public static final String KEY_USER_UID="uid";
    public static final String KEY_USER_NAME="name";
    public static final String KEY_USER_AVATAR="avatar";
    public static final String KEY_USER_COVER="cover";
    public static final String KEY_USER_LOCATION="location";
    public static final String KEY_USER_DESCRIPTION="description";
    public static final String KEY_USER_GENDER="gender";
    public static final String KEY_USER_FOLLOWERS_COUNT="followers_count";
    public static final String KEY_USER_FRIENDS_COUNT="friends_count";
    public static final String KEY_USER_STATUSES_COUNT="statuses_count";
    public static final String KEY_USER_FOLLOWING="following";
    public static final String KEY_USER_VERIFIED="verified";
    public static final String KEY_USER_VERIFIED_REASON="verified_reason";
    public static final String KEY_USER_FOLLOW_ME="follow_me";
    
    /**
     * 保存 Token 对象到 SharedPreferences。
     * 
     * @param context 应用程序上下文环境
     * @param token   Token 对象
     */
    public static void writeAccessToken(Context context, Oauth2AccessToken token) {
        if (null == context || null == token) {
            return;
        }
        
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(KEY_UID, token.getUid());
        editor.putString(KEY_ACCESS_TOKEN, token.getToken());
        editor.putLong(KEY_EXPIRES_IN, token.getExpiresTime());
        editor.commit();
    }

    /**
     * 从 SharedPreferences 读取 Token 信息。
     * 
     * @param context 应用程序上下文环境
     * 
     * @return 返回 Token 对象
     */
    public static Oauth2AccessToken readAccessToken(Context context) {
        if (null == context) {
            return null;
        }
        
        Oauth2AccessToken token = new Oauth2AccessToken();
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        token.setUid(pref.getString(KEY_UID, ""));
        token.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
        token.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));
        return token;
    }

    /**
     * 清空 SharedPreferences 中 Token信息。
     * 
     * @param context 应用程序上下文环境
     */
    public static void clearToken(Context context) {
        if (null == context) {
            return;
        }
        
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.remove(KEY_UID);
        editor.remove(KEY_ACCESS_TOKEN);
        editor.remove(KEY_EXPIRES_IN);
        editor.commit();
    }
    
    /**
     * 清空 SharedPreferences 中 Token信息。
     * 
     * @param context 应用程序上下文环境
     */
    public static void clear(Context context) {
        if (null == context) {
            return;
        }
        
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
    
    /**
     * 标记非首次启动
     * 
     * @param context
     */
    public static void writeMark(Context context){
    	if (null == context) {
            return;
        }
        
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putInt(KEY_START_MARK, 1);
        editor.commit();
    }
    
    /**
     * 读取是否为首次启动
     * 
     * @param context
     */
    public static int readMark(Context context){
    	 if (null == context) {
             return 0;
         }
         
         SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
         return pref.getInt(KEY_START_MARK, 0);
    }
    
    
    /**
     * 存储列表滚动位置
     * @since 2014-11-6
     * @param context
     * @param cacheStr
     */
    public static void writeListViewPosition(Context context, int offsetY, int position){
    	if (null == context) {
            return;
        }
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putInt(KEY_OFFSET_Y, offsetY);
        editor.putInt(KEY_LISTVIEW_POSTION, position);
        editor.commit();
    }
    /**
     * 读取Y偏移
     * @param context
     * @return
     */
    public static int readOffsetY(Context context){
    	 if (null == context) {
             return 0;
         }
         SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
         return pref.getInt(KEY_OFFSET_Y, 0);
    }
    /**
     * 读取位置
     * @param context
     * @return
     */
    public static int readPosition(Context context){
   	 if (null == context) {
            return 0;
        }
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        return pref.getInt(KEY_LISTVIEW_POSTION, 0);
   }
    
    /**
     * 写入用户信息
     * @user qiaorongzhu
     * @since 2014-11-28
     * @param context
     * @param user
     */
    public static void writeUserInfo(Context context, User user){
    	if (null == context) {
            return;
        }
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(KEY_USER_UID, user.getUid());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_AVATAR, user.getAvatar());
        editor.putString(KEY_USER_COVER, user.getCover());
        editor.putString(KEY_USER_LOCATION, user.getLocation());
        editor.putString(KEY_USER_DESCRIPTION, user.getDescription());
        editor.putString(KEY_USER_GENDER, user.getGender());
        editor.putInt(KEY_USER_FOLLOWERS_COUNT, user.getFollowers_count());
        editor.putInt(KEY_USER_FRIENDS_COUNT, user.getFriends_count());
        editor.putInt(KEY_USER_STATUSES_COUNT, user.getStatuses_count());
        editor.putBoolean(KEY_USER_FOLLOWING, user.isFollowing());
        editor.putBoolean(KEY_USER_VERIFIED, user.isVerified());
        editor.putString(KEY_USER_VERIFIED_REASON, user.getVerified_reason());
        editor.putBoolean(KEY_USER_FOLLOW_ME, user.isFollow_me());
        editor.commit();
    }
    /**
     * 读取用户信息
     * @user qiaorongzhu
     * @since 2014-11-28
     * @param context
     * @return
     */
    public static User readUserInfo(Context context){
      	 if (null == context) {
      		 return null;
          }
      	 SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
      	 if ("".equals(pref.getString(KEY_USER_NAME, ""))) {
      		return null;
		}
      	 User user=new User();
      	 user.setUid(pref.getString(KEY_USER_UID, ""));
      	 user.setName(pref.getString(KEY_USER_NAME, ""));
      	 user.setAvatar(pref.getString(KEY_USER_AVATAR, ""));
      	 user.setCover(pref.getString(KEY_USER_COVER, ""));
      	 user.setLocation(pref.getString(KEY_USER_LOCATION, ""));
      	 user.setDescription(pref.getString(KEY_USER_DESCRIPTION, ""));
      	 user.setGender(pref.getString(KEY_USER_GENDER, ""));
      	 user.setFollowers_count(pref.getInt(KEY_USER_FOLLOWERS_COUNT, 0));
      	 user.setFriends_count(pref.getInt(KEY_USER_FRIENDS_COUNT, 0));
      	 user.setStatuses_count(pref.getInt(KEY_USER_STATUSES_COUNT, 0));
      	 user.setFollowing(pref.getBoolean(KEY_USER_FOLLOWING, false));
      	 user.setVerified(pref.getBoolean(KEY_USER_VERIFIED, false));
      	 user.setVerified_reason(pref.getString(KEY_USER_VERIFIED_REASON, ""));
      	 user.setFollow_me(pref.getBoolean(KEY_USER_FOLLOW_ME, false));
       return user;
      }
    
}
