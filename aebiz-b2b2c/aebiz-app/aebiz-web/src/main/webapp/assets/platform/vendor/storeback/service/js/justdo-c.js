/**
 * Created by JKZ on 2015/6/9.
 */

(function() {
	window.IM = window.IM || {
        _appid : $("#appId").val(), // 应用I
        _onUnitAccount : 'KF10089', // 多渠道客服帐号，目前只支持1个
        _3rdServer : 'http://123.57.230.158:8886/authen/', // 3rdServer，主要用来虚拟用户服务器获取SIG

		/** 以下不要动，不需要改动 */
		_timeoutkey: null,
		_username: null,
		_user_account: null,  //用户登录账号
		_contact_type_c: 'C', // 代表联系人
		_contact_type_g: 'G', // 代表群组
		_contact_type_m: 'M', // 代表多渠道客服
		_onMsgReceiveListener: null,
		_onDeskMsgReceiveListener: null,
		_noticeReceiveListener: null,
		_onConnectStateChangeLisenter: null,
		_onCallMsgListener: null,
		_isMcm_active: false,
		_local_historyver: 0,
		_msgId: null, // 消息ID，查看图片时有用
		_pre_range: null, // pre的光标监控对象
		_pre_range_num: 0, // 计数，记录pre中当前光标位置，以childNodes为单位
		_fireMessage: 'fireMessage',
		_serverNo: 'XTOZ',
		_baiduMap: null,
		_loginType: 1, //登录类型: 1账号登录，3voip账号密码登录
		_Notification: null,
		_extopts: [], // 新建一个数组存放@的人
		_transfer : 12,
		_isNewSDK:false,
		/**
		 * 初始化
		 * 
		 * @private
		 */
		init: function() {
			// 初始化SDK
			var resp = RL_YTX.init(IM._appid);
			if (!resp) {
				alert('SDK初始化错误');
				return;
			};
			
			if (200 == resp.code) { // 初始化成功
				$('#navbar_login').show();
				$('#navbar_login_show').hide();

				// 重置页面高度变化
				IM.HTML_resetHei();

				window.onresize = function() {
					IM.HTML_resetHei();
				};

				// 初始化表情
				IM.initEmoji();
				// 初始化一些页面需要绑定的事件
				IM.initEvent();
				if ($.inArray(174004, resp.unsupport) > -1 || $.inArray(174009, resp.unsupport) > -1) { //不支持getUserMedia方法或者url转换
					IM.Check_usermedie_isDisable(); //拍照、录音、音视频呼叫都不支持

				} else if ($.inArray(174007, resp.unsupport) > -1) { //不支持发送附件
					IM.SendFile_isDisable();

				} else if ($.inArray(174008, resp.unsupport) > -1) { //不支持音视频呼叫，音视频不可用
					IM.SendVoiceAndVideo_isDisable();

				};
			} else if (174001 == resp.code) { // 不支持HTML5
				var r = confirm(resp.msg);
				if (r == true || r == false) {
					window.close();
				}
			} else if (170002 == resp.code) { //缺少必须参数
				console.log("错误码：170002,错误码描述" + resp.msg);
			} else {
				console.log('未知状态码');
			};
			IM._Notification = window.Notification || window.mozNotification || window.webkitNotification || window.msNotification || window.webkitNotifications;
			if (!!IM._Notification) {
				IM._Notification.requestPermission(function(permission) {
					if (IM._Notification.permission !== "granted") {
						IM._Notification.permission = "granted";
					}
				});
			}
		},

		/**
		 * 初始化一些页面需要绑定的事件
		 */
		initEvent: function() {
			$('#im_send_content').bind('paste', function() {
				IM.DO_pre_replace_content();
			});
			var s = RL_YTX.bindBeforeUnLoad(function(e){
				console.log(111);
			});
			s = RL_YTX.bindBeforeUnLoad(function(e){
				console.log(222);
			});
			RL_YTX.unbindBeforeUnLoad(s);
		},

		/**
		 * 初始化表情
		 */
		initEmoji: function() {
			var emoji_div = $('#emoji_div').find('div[class="popover-content"]');
			for (var i in emoji.show_data) {
				var c = emoji.show_data[i];
				var out = emoji.replace_unified(c[0][0]);

				var content_emoji = '<span style="cursor:pointer; margin: 0 2px 0 4px;" ' +
					'onclick="IM.DO_chooseEmoji(\'' + i + '\', \'' + c[0][0] + '\')" ' +
					'imtype="content_emoji">' + out + '</span>';
				emoji_div.append(content_emoji);
			}
		},

		/**
		 * 监控键盘
		 * 
		 * @param event
		 * @constructor
		 */
		_keyCode_1: 0,
		_keyCode_2: 0,
		EV_keyCode: function(event) {
			IM._keyCode_1 = IM._keyCode_2;
			IM._keyCode_2 = event.keyCode;
			// 17=Ctrl 13=Enter  16=shift 50=@

			if (17 == IM._keyCode_1 && 13 == IM._keyCode_2) {
				if ('none' == $('#navbar_login').css('display')) {
					IM.DO_sendMsg();
				}
			} else if (17 != IM._keyCode_1 && 13 == IM._keyCode_2) {
				if ('block' == $('#navbar_login').css('display')) {
					IM.DO_login();
				}
			} else if (16 == IM._keyCode_1 && 50 == IM._keyCode_2) { //chrome、火狐、opear 英文输入法
				//判断如果是群组的话才展示成员列表
				$('#im_contact_list').find('li').each(function() {
					if ($(this).attr('class').indexOf("active") > -1) {
						if ($(this).attr("contact_type") == IM._contact_type_g) {
							//展示成员列表
							var groupId = $(this).attr("contact_you");
							if (document.getElementById("im_send_content") == document.activeElement) {
								//传入startIndex
								var startIndex = window.getSelection().anchorOffset;
								IM.EV_getGroupMemberList(groupId, "memberList", startIndex);
							}
						}
					}
				});
			} else if (16 == IM._keyCode_1 && 229 == IM._keyCode_2) { //chrome中文输入法时返回229
				setTimeout(function() {
					var str = $("#im_send_content").text();
					var startIndex = window.getSelection().anchorOffset;
					if ("@" == str.substring(startIndex - 1, startIndex)) {
						//判断如果是群组的话才展示成员列表
						$('#im_contact_list').find('li').each(function() {
							if ($(this).attr('class').indexOf("active") > -1) {
								if ($(this).attr("contact_type") == IM._contact_type_g) {
									//展示成员列表
									var groupId = $(this).attr("contact_you");
									if (document.getElementById("im_send_content") == document.activeElement) {
										IM.EV_getGroupMemberList(groupId, "memberList", startIndex - 1);
									}
								}
							}
						});
					};
				}, 500)

			} else if (50 == IM._keyCode_2) {
				if (!!navigator.userAgent.match(/mobile/i)) { //判断是否移动端
					setTimeout(function() {
						var str = $("#im_send_content").text();
						if ("@" == str.substring(str.length - 1)) {
							$('#im_contact_list').find('li').each(function() {
								if ($(this).attr('class').indexOf("active") > -1) {
									if ($(this).attr("contact_type") == IM._contact_type_g) {
										//展示成员列表
										var groupId = $(this).attr("contact_you");
										if (document.getElementById("im_send_content") == document.activeElement) {
											IM.EV_getGroupMemberList(groupId, "memberList", '');
											$("#groupMemList_div").css("max-width", "100%");
										}
									}
								}
							});
						}
					}, 200);

				}
			} else if (8 == IM._keyCode_2) { //退格键
				$("#groupMemList_div").hide();
			} else if (16 == IM._keyCode_2) { //火狐中文输入模式
				var userAgent = navigator.userAgent.toLowerCase();
				if (userAgent.indexOf("firefox") > -1) {
					setTimeout(function() {
						//传入startIndex
						var startIndex = window.getSelection().anchorOffset;
						var str = $("#im_send_content").text();
						if ("@" == str.substring(startIndex - 1, startIndex)) {
							//判断如果是群组的话才展示成员列表
							$('#im_contact_list').find('li').each(function() {
								if ($(this).attr('class').indexOf("active") > -1) {
									if ($(this).attr("contact_type") == IM._contact_type_g) {
										//展示成员列表
										var groupId = $(this).attr("contact_you");
										if (document.getElementById("im_send_content") == document.activeElement) {
											IM.EV_getGroupMemberList(groupId, "memberList", startIndex - 1);
										}
									}
								}
							});
						}
					}, 200)
				}
			}
		},

		DO_login: function() {
			console.log("DO_login");
			var user_account = "";
			var pwd = "";
			if (IM._loginType == 1) {
				user_account = $('#navbar_user_account').val();
				if (IM.isNull(user_account)) {
					alert('请填写手机号后再登录');
					return;
				}
			} else if (IM._loginType == 3) {
				user_account = $('#voip_account').val();
				pwd = $('#voip_pwd').val();
				if (IM.isNull(user_account) || IM.isNull(pwd)) {
					alert('用户名与密码不能为空!');
					return;
				}
			}
			//校验登陆格式
			if (user_account.length > 64) {
				alert("长度不能超过64");
				return;
			}
			var regx1 = /^[^g|G].*$/; //不能以g开头
			if (regx1.exec(user_account) == null) {
				alert("不能以g或者G开头");
				return;
			}
			if (user_account.indexOf("@") > -1) {
				var regx2 = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
				if (regx2.exec(user_account) == null) {
					alert("用户名只能是数字或字母，如果使用邮箱，请检查邮箱格式");
					return;
				}
			} else {
				var regx3 = /^[A-Za-z0-9._-]+$/;
				if (regx3.exec(user_account) == null) {
					alert("用户名只能是数字字母点下划线")
					return;
				}
			}
			$('#navbar_user_account').attr("readonly", "readonly");

			IM._login(user_account, pwd);
		},

		/**
		 * 正式处理登录逻辑，此方法可供断线监听回调登录使用 获取时间戳，获取SIG，调用SDK登录方法
		 * 
		 * @param user_account
		 * @param pwd 密码
		 * @private
		 */
		_login: function(user_account, pwd) {
			var timestamp = IM._getTimeStamp();

			var flag = false; //是否从第三方服务器获取sig
			if (flag) {
				IM._privateLogin(user_account, timestamp, function(obj) {
					console.log('obj.sig:' + obj.sig);
					IM.EV_login(user_account, pwd, obj.sig, timestamp);
				}, function(obj) {
					$('#navbar_user_account').removeAttr("readonly");
					alert("错误码：" + obj.code + "; 错误描述：" + obj.msg);
				});
			} else {
				//仅用于本地测试，官方不推荐这种方式应用在生产环境
				//没有服务器获取sig值时，可以使用如下代码获取sig
//				var appToken = '17E24E5AFDB6D0C1EF32F3533494502B'; //使用是赋值为应用对应的appToken
				var appToken = $("#appToken").val();//开发token
				var sig = hex_md5(IM._appid + user_account + timestamp + appToken);
				console.log("本地计算sig：" + sig);
				IM.EV_login(user_account, pwd, sig, timestamp);
			}
		},

		/**
		 * SIG获取 去第三方（客服）服务器获取SIG信息 并将SIG返回，传给SDK中的登录方法做登录使用
		 * 
		 * @param user_account
		 * @param timestamp -- 时间戳要与SDK登录方法中使用的时间戳一致
		 * @param callback
		 * @param onError
		 * @private
		 */
		_privateLogin: function(user_account, timestamp, callback, onError) {
			console.log("_privateLogin");
			var data = {
				"appid": IM._appid,
				"username": user_account,
				"timestamp": timestamp
			};
			var url = IM._3rdServer + 'genSig';
			$.ajax({
				url: url,
				dataType: 'jsonp',
				data: data,
//				contentType:"application/json",
				jsonp: 'cb',
				success: function(result) {
					if (result.code != 000000) {
						var resp = {};
						resp.code = result.code;
						resp.msg = "Get SIG fail from 3rd server!...";
						// onError(resp);
						return;
					} else {
						var resp = {};
						resp.code = result.code;
						resp.sig = result.sig;
						callback(resp);
						return;
					}
				},
				error: function() {
					var resp = {};
					resp.msg = 'Get SIG fail from 3rd server!';
					// onError(resp);
				},
				timeout: 5000
			});
		},

		/**
		 * 事件，登录 去SDK中请求登录
		 * 
		 * @param user_account
		 * @param sig
		 * @param timestamp --
		 *            时间戳要与生成SIG参数的时间戳保持一致
		 * @constructor
		 */
		EV_login: function(user_account, pwd, sig, timestamp) {
			console.log("EV_login");

			var loginBuilder = new RL_YTX.LoginBuilder();
			loginBuilder.setType(IM._loginType);
			loginBuilder.setUserName(user_account);
			if (1 == IM._loginType) { //1是自定义账号，3是voip账号
				loginBuilder.setSig(sig);
			} else {
				loginBuilder.setPwd(pwd);
			}
			loginBuilder.setTimestamp(timestamp);

			RL_YTX.login(loginBuilder, function(obj) {
				console.log("EV_login succ...");

				IM._user_account = user_account;
				IM._username = user_account;
				// 注册PUSH监听
				IM._onMsgReceiveListener = RL_YTX.onMsgReceiveListener(
					function(obj) {
						IM.EV_onMsgReceiveListener(obj);
					});
				// 注册客服消息监听
				  IM._onDeskMsgReceiveListener = RL_YTX.onMCMMsgReceiveListener(
				          function(obj) {
				              IM.EV_onMsgReceiveListener(obj);
				          });
				// 注册群组通知事件监听
				IM._noticeReceiveListener = RL_YTX.onNoticeReceiveListener(
					function(obj) {
						IM.EV_noticeReceiveListener(obj);
					});
				// 服务器连接状态变更时的监听
				IM._onConnectStateChangeLisenter = RL_YTX.onConnectStateChangeLisenter(function(obj) {
					// obj.code;//变更状态 1 断开连接 2 重练中 3 重练成功 4 被踢下线 5 断开连接，需重新登录
					// 断线需要人工重连
					if (1 == obj.code) {
						console.log('onConnectStateChangeLisenter obj.code:' + obj.msg);
					} else if (2 == obj.code) {
						IM.HTML_showAlert('alert-warning',
							'网络状况不佳，正在试图重连服务器', 10 * 60 * 1000);
						$("#pop_videoView").hide();
					} else if (3 == obj.code) {
						IM.HTML_showAlert('alert-success', '连接成功');
					} else if (4 == obj.code) {
						IM.DO_logout();
						alert(obj.msg);
					} else if (5 == obj.code) {
						IM.HTML_showAlert('alert-warning',
							'网络状况不佳，正在试图重连服务器');
						$("#pop_videoView").hide();
						IM._login(IM._user_account);
					} else {
						console.log('onConnectStateChangeLisenter obj.code:' + obj.msg);
					}
				});
				/*音视频呼叫监听
				 obj.callId;//唯一消息标识  必有
				 obj.caller; //主叫号码  必有
				 obj.called; //被叫无值  必有
				 obj.callType;//0 音频 1 视频 2落地电话
				 obj.state;//1 对方振铃 2 呼叫中 3 被叫接受 4 呼叫失败 5 结束通话 6 呼叫到达
				 obj.reason//拒绝或取消的原因
				 obj.code//当前浏览器是否支持音视频功能
				 */
				IM._onCallMsgListener = RL_YTX.onCallMsgListener(
					function(obj) {
						IM.EV_onCallMsgListener(obj);
					});

				IM._onMsgNotifyReceiveListener = RL_YTX.onMsgNotifyReceiveListener(function(obj) {
					if (obj.msgType == 21) { //阅后即焚：接收方已删除阅后即焚消息
						console.log("接收方已删除阅后即焚消息obj.msgId=" + obj.msgId);
						var id = obj.sender + "_" + obj.msgId;
						$(document.getElementById(id)).remove();
					}
				});
				$('#navbar_user_account').removeAttr("readonly");

				$('#navbar_login').hide();
				$('#navbar_login_show').show();
				IM.EV_getMyInfo();
				IM.HTML_LJ_none();
				//登陆后拉取最近联系人列表
				IM.EV_getRecentConList();

				// 添加客服号到列表中
				IM.HTML_addContactToList(IM._onUnitAccount, IM._onUnitAccount,
					IM._contact_type_m, false, false, false, null, null,
					null);
			}, function(obj) {
				$('#navbar_user_account').removeAttr("readonly");
				alert("错误码： " + obj.code + "; 错误描述：" + obj.msg);
			});
		},
		EV_getRecentConList: function() {
			var recentConListBuilder = new RL_YTX.GetRecentContactListBuilder();
			recentConListBuilder.setTime(IM._getTimeStamp());//可以不填
			recentConListBuilder.setLimit(10);//可以不填

			RL_YTX.getRecentContactList(recentConListBuilder, function(succObj) {
				console.log("get recent Contact List success");
				alert("succObj" + succObj);
				//增加到左侧联系人列表中,zzx
				if (obj.contact.substring(0, 1) != "g") { //如果是普通联系人
					IM.HTML_addContactToList(obj.contact, obj.name, IM._contact_type_c, true, true, false, null, null, null);
				} else { //如果是群组或讨论组
					IM.HTML_addContactToList(obj.contact, obj.name, IM._contact_type_g, false, false, true, null, null, null);
				}
			}, function(errObj) {
				alert("错误码： " + errObj.code + "; 错误描述：" + errObj.msg);
			});
		},

		/**
		 * 事件，登出
		 * 
		 * @constructor
		 */
		EV_logout: function() {
			console.log("EV_logout");
			IM.DO_logout();
			RL_YTX.logout(function() {
				console.log("EV_logout succ...");
			}, function(obj) {
				alert("错误码： " + obj.code + "; 错误描述：" + obj.msg);
			});
		},

		/**
		 * 登出
		 * 
		 * @constructor
		 */
		DO_logout: function() {
			// 销毁PUSH监听
			IM._onMsgReceiveListener = null;
			// 注册客服消息监听
			IM._onDeskMsgReceiveListener = null;
			// 销毁注册群组通知事件监听
			IM._noticeReceiveListener = null;
			// 服务器连接状态变更时的监听
			IM._onConnectStateChangeLisenter = null;
			//呼叫监听
			IM._onCallMsgListener = null;
			//阅后即焚监听
			IM._onMsgNotifyReceiveListener = null;
			//$("#fireMessage").removeClass("active");
			// 清理左侧数据
			$('#im_contact_list').empty();
			// 清理右侧数据
			$('#im_content_list').empty();

			// 隐藏图片层
			IM.HTML_pop_photo_hide();

			// 隐藏拍照层
			IM.HTML_pop_takePicture_hide();

			//隐藏音视频呼叫遮罩层
			$("#pop_videoView").hide();

			//隐藏录音遮罩层，停掉录音流
			$("#pop_recorder").hide();

			// 隐藏群组详情页面
		    //IM.HTML_pop_hide();

			// 隐藏表情框
			$('#emoji_div').hide();

			// 隐藏提示框
			IM.HTML_closeAlert('all');

			// 联系人列表切换到沟通
			IM.DO_choose_contact_type('C');

			$('#navbar_login').show();
			$('#navbar_login_show').hide();
		},

		/**
		 * 事件，push消息的监听器，被动接收信息
		 * 
		 * @param obj
		 * @constructor
		 */
		EV_onMsgReceiveListener: function(obj) {
			console.log('Receive message sender:[' + obj.msgSender + ']...msgId:[' + obj.msgId + ']...content[' + obj.msgContent + ']');
			if(obj.msgType == IM._transfer){
				if (obj.msgDomain == 1) {
					// $("#notice").html(obj.msgSender + "正在输入……");
					return;
				} else if (obj.msgDomain == 0) {
					$("#notice").html("");
					return;
				} else if (obj.msgDomain == 2) {
					$("#notice").html(obj.msgSender + "正在录音");
					return;
				}
			}
			IM.DO_push_createMsgDiv(obj);

			// 播放铃声前，查看是否是群组，如果不是直接播放，如果是查看自定义提醒类型，根据类型判断是否播放声音
			var b_isGroupMsg = ('g' == obj.msgReceiver.substr(0, 1));
			if (b_isGroupMsg) {
				// 1提醒，2不提醒
				var isNotice = $(document.getElementById('im_contact_' + obj.msgReceiver)).attr('im_isnotice');
				if (2 != isNotice) {
					document.getElementById('im_ring').play();
				}
			} else {
				document.getElementById('im_ring').play();
			}
		},

		/**
		 * 拍照pop层隐藏
		 * 
		 * @constructor
		 */
		HTML_pop_takePicture_hide: function() {
			$('#pop_takePicture').hide();
			$("#video").attr("src", '');
			IM.HTML_LJ_none();
		},

		/**
		 * 样式，滤镜隐藏
		 * 
		 * @constructor
		 */
		HTML_LJ_none: function() {
			$('#lvjing').hide();
		},

		/**
		 * 事件，notice群组通知消息的监听器，被动接收消息
		 * 
		 * @param obj
		 * @constructor
		 */
	/*	EV_noticeReceiveListener: function(obj) {
			console.log('notice message groupId:[' + obj.groupId + ']...auditType[' + obj.auditType + ']...msgId:[' + obj.msgId + ']...');
			IM.DO_notice_createMsgDiv(obj);
			// 播放铃声
			document.getElementById('im_ring').play();

		},*/
		EV_onCallMsgListener: function(obj) {
			console.log("-------obj.callId = " + obj.callId);
			console.log("-------obj.caller = " + obj.caller);
			console.log("-------obj.called = " + obj.called);
			console.log("-------obj.callType = " + obj.callType);
			console.log("-------obj.state = " + obj.state);
			console.log("-------obj.reason = " + obj.reason);
			console.log("-------obj.code = " + obj.code);
//			console.log("-------obj.router = " + obj.router);
			var noticeMsg = ''; //桌面提醒消息
			if (obj.callType == 1) { //视频
				if (200 == obj.code) {
					if (obj.state == 1) { //收到振铃消息
						//本地播放振铃
						document.getElementById("voipCallRing").play();
					} else if (obj.state == 2) { //呼叫中

					} else if (obj.state == 3) { //被叫接受
						document.getElementById("voipCallRing").pause();
						$("#cancelVoipCall").text("结束");
						noticeMsg = "[接收视频通话]";
					} else if (obj.state == 4) { //呼叫失败 对主叫设定：自动取消，对方拒绝或者忙 
						$("#pop_videoView").hide();
						//alert("对方正在通话中");
						IM.HTML_showAlert('alert-error', '对方正在通话中');
						document.getElementById("voipCallRing").pause();
						noticeMsg = "[视频通话结束]";
					} else if (obj.state == 5) { //结束通话  或者主叫取消（对被叫而言）
						document.getElementById("voipCallRing").pause();
						$("#pop_videoView").hide();
						noticeMsg = "[视频通话结束]";
					} else if (obj.state == 6) { //呼叫到达
						//添加左侧联系人
						var im_contact = $('#im_contact_list').find('li[contact_type="' + IM._contact_type_c + '"][contact_you="' + obj.caller + '"]');
						if (im_contact.length <= 0) {
							IM.HTML_clean_im_contact_list();

							IM.HTML_addContactToList(obj.caller, obj.caller,
								IM._contact_type_c, true, true, false, null,
								null, null);

							IM.HTML_clean_im_content_list(obj.caller);
						};
						$("#videoView").show();
						$("#voiceCallDiv_audio").hide();
						IM.HTML_videoView(obj.callId, obj.caller, obj.called, 1);
						$("#cancelVoipCall").hide();
						$("#acceptVoipCall").show();
						$("#refuseVoipCall").show();
						//本地播放振铃
						document.getElementById("voipCallRing").play();
						noticeMsg = "[视频呼叫]";
					}
				} else {
					var str = '<pre>请求视频通话，请使用其他终端处理</pre>';
					IM.HTML_pushCall_addHTML(obj.caller, obj.callId, str);
				}
			} else if (obj.callType == 0 || obj.callType == 2) { //音频
				if (200 == obj.code) {
					if (obj.state == 1) { //收到振铃消息
						//本地播放振铃
						document.getElementById("voipCallRing").play();
					} else if (obj.state == 2) { //呼叫中

					} else if (obj.state == 3) { //被叫接受
						document.getElementById("voipCallRing").pause();
						$("#cancelVoiceCall").text("结束");
						noticeMsg = "[接收语音通话]";
					} else if (obj.state == 4) { //呼叫失败 是对主叫设定：主动取消，对方拒绝或者忙
						$("#pop_videoView").hide();
						document.getElementById("voipCallRing").pause();
						noticeMsg = "[语音通话结束]";
						//alert("对方正在通话中");
						IM.HTML_showAlert('alert-error', '对方正在通话中');
					} else if (obj.state == 5) { //结束通话  或者主叫取消（对被叫而言）
						document.getElementById("voipCallRing").pause();
						$("#pop_videoView").hide();
						noticeMsg = "[语音通话结束]";
					} else if (obj.state == 6) { //呼叫到达
						//添加左侧联系人
						var im_contact = $('#im_contact_list').find('li[contact_type="' + IM._contact_type_c + '"][contact_you="' + obj.caller + '"]');
						if (im_contact.length <= 0) {
							IM.HTML_clean_im_contact_list();

							IM.HTML_addContactToList(obj.caller, obj.caller,
								IM._contact_type_c, true, true, false, null,
								null, null);

							IM.HTML_clean_im_content_list(obj.caller);
						};
						$("#videoView").hide();
						$("#voiceCallDiv_audio").show();
						IM.HTML_videoView(obj.callId, obj.caller, obj.called, 0);
						$("#cancelVoiceCall").hide();
						$("#acceptVoiceCall").show();
						$("#refuseVoiceCall").show();
						//本地播放振铃
						document.getElementById("voipCallRing").play();
						noticeMsg = "[语音呼叫]";
					};
				} else {
					var str = '<pre>请求语音通话，请使用其他终端处理</pre>';
					IM.HTML_pushCall_addHTML(obj.caller, obj.callId, str);
				}
			}
			//桌面提醒通知
			if (!!noticeMsg) {
				// IM.DO_deskNotice(obj.caller, '', noticeMsg, '', false, true);
			}
		},

		/**
		 * 事件，发送消息
		 * 
		 * @param msgid
		 * @param text
		 * @param receiver
		 * @param isresend
		 * @constructor
		 */
		EV_sendTextMsg: function(oldMsgid, text, receiver, isresend, type) {
			console.log('send Text message: receiver:[' + receiver + ']...connent[' + text + ']...');

			var obj = new RL_YTX.MsgBuilder();
			obj.setText(text);
			obj.setType(1);
			obj.setReceiver(receiver);
			if (IM._extopts.length > 0) {
				obj.setAtAccounts(IM._extopts);
				obj.setType(11);
			}
			// if ($("#fireMessage").attr("class").indexOf("active") > -1) { //domain
			// 	obj.setDomain("fireMessage");
			// };
			if(!!type){
				obj.setType(type);
			}
			var msgId = RL_YTX.sendMsg(obj, function(obj) {
				$('#im_send_content').html('');
				$(document.getElementById(receiver + '_' + obj.msgClientNo)).show();
				$(document.getElementById(receiver + '_' + obj.msgClientNo)).find('span[imtype="resend"]').css('display', 'none');
				console.log('send Text message succ');
				if (isresend) {
					var msg = $(document.getElementById(receiver + '_' + obj.msgClientNo));
					$('#im_content_list').append(msg.prop("outerHTML"));
					msg.remove(); // 删掉原来的展示
				};
				$('#im_content_list').scrollTop($('#im_content_list')[0].scrollHeight);
			}, function(obj) {
				setTimeout(function() { //断线的时候如果不延迟会出现找不到标签的情况，延迟0.3秒可解决。
					if (170001 == obj.code) {
						$(document.getElementById(receiver + '_' + obj.msgClientNo)).remove();
						alert("发送消息内容超长，请分条发送");
					} else if (174002 == obj.code) {
						$(document.getElementById(receiver + '_' + obj.msgClientNo)).remove();
						alert("错误码： " + obj.code + "; 错误描述：" + obj.msg);
					} else {
						$('#im_send_content').html('');
						var msgf = $(document.getElementById(receiver + '_' + obj.msgClientNo));
						msgf.show();
						if (msgf.find('pre [msgtype="resendMsg"]').length == 0) {
							var resendStr = '<pre msgtype="resendMsg" style="display:none;">' + text + '</pre>'
							msgf.append(resendStr);
						}

						msgf.find('span[imtype="resend"]').css('display', 'block');
						alert("错误码： " + obj.code + "; 错误描述：" + obj.msg);
					}
				}, 300)
			});
			$(document.getElementById(receiver + '_' + oldMsgid)).attr("id", receiver + "_" + msgId);
			//@数组清空
			IM._extopts = [];
		},

		/**
		 * 事件，重发消息
		 * 
		 * @param id
		 *            右侧展示模块元素的id
		 * 
		 * @constructor
		 */
		EV_resendMsg: function(obj) {
			var msg = $(obj.parentElement);
			// 消息类型1:文本消息 2：语音消息 3：视频消息 4：图片消息 5：位置消息 6：文件
			var msgtype = msg.attr('im_msgtype');
			var receiver = msg.attr('content_you');
			var oldMsgid = msg.attr('id').substring(msg.attr('id').indexOf("_") + 1);
			if (1 == msgtype) { // 文本消息
				msg.find('span[imtype="resend"]').css('display', 'none');
				var text = msg.find('pre[msgtype="resendMsg"]').html();
				if (text.length > 2048) {
					IM.HTML_showAlert('alert-error', '消息长度不能超过2048');
					return false;
				}
				console.log('resend message: text[' + text + ']...receiver:[' + receiver + ']');
				IM.EV_sendTextMsg(oldMsgid, text, receiver, true);
			} else if (4 == msgtype || 7 == msgtype) {
				// 查找当前选中的contact_type值 1、IM上传 2、MCM上传
				var contact_type = msg.attr('content_type');
				var oFile = msg.find('input[imtype="msg_attach_resend"]')[0];
				if (!!oFile) {
					oFile = oFile.files[0];
					console.log('resend Attach message: msgtype[' + msgtype + ']...receiver:[' + receiver + ']');
					if (IM._contact_type_m == contact_type) {
						IM.EV_sendToDeskAttachMsg(oldMsgid, oFile, msgtype,
							receiver, true);
					} else {
						IM.EV_sendAttachMsg(oldMsgid, oFile, msgtype, receiver, true);
					}
				} else {
					oFile = msg.find("object").val();
					console.log('resend Attach message: msgtype[' + msgtype + ']...receiver:[' + receiver + ']');
					if (IM._contact_type_m == contact_type) {
						IM.EV_sendToDeskAttachMsg(oldMsgid, oFile,
							msgtype, receiver, true);
					} else {
						IM.EV_sendAttachMsg(oldMsgid, oFile,
							msgtype, receiver, true);
					};
				};

			} else if (2 == msgtype) { //语音

				var oFile = msg.find("object").val();
				if (IM._contact_type_m == contact_type) {
					IM.EV_sendToDeskAttachMsg(oldMsgid, oFile,
						msgtype, receiver, true);
				} else {
					IM.EV_sendAttachMsg(oldMsgid, oFile,
						msgtype, receiver, true);
				};
			} else {
				console.log('暂时不支持附件类型消息重发');
			}
		},

		/**
		 * 发送附件
		 * 
		 * @param msgid
		 * @param file --
		 *            file对象
		 * @param type --
		 *            附件类型 2 语音消息 3 视频消息 4 图片消息 5 位置消息 6 文件消息
		 * @param receiver --
		 *            接收者
		 * @constructor
		 */
		EV_sendAttachMsg: function(oldMsgid, file, type, receiver, isresend) {
			console.log('send Attach message: type[' + type + ']...receiver:[' + receiver + ']' + 'fileName:[' + file.fileName + ']');
			var obj = new RL_YTX.MsgBuilder();
			obj.setFile(file);
			obj.setType(type);
			obj.setReceiver(receiver);
			// if ($("#fireMessage").attr("class").indexOf("active") > -1) { //domain
			// 	obj.setDomain("fireMessage");
			// };
			var oldMsg = $(document.getElementById(receiver + '_' + oldMsgid));
			oldMsg.attr('msg', 'msg');
			oldMsg.css('display', 'block');
			if (4 == type) {
				oldMsg.attr('im_carousel', 'real');
				oldMsg.attr('im_msgtype', '4');
			}

			$('#im_content_list').scrollTop($('#im_content_list')[0].scrollHeight);

			var msgid = RL_YTX.sendMsg(obj, function(obj) {
				setTimeout(function() {
					var id = receiver + "_" + obj.msgClientNo;
					var msg = $(document.getElementById(id));
					msg.find('span[imtype="resend"]').css(
						'display', 'none');
					msg.find('div[class="bar"]').parent().css(
						'display', 'none');
					msg.find('span[imtype="msg_attach"]').css(
						'display', 'block');
					console.log('send Attach message succ');
					if (isresend) {
						$('#im_content_list').append(msg.prop("outerHTML"));
						msg.remove(); // 删掉原来的展示
					}
					$('#im_content_list').scrollTop($('#im_content_list')[0].scrollHeight);
				}, 100)

			}, function(obj) { // 失败
				setTimeout(function() {
					var msg = $(document.getElementById(receiver + "_" + obj.msgClientNo));
					msg.find('span[imtype="resend"]').css(
						'display', '');
					msg.find('div[class="bar"]').parent().css(
						'display', 'none');
					msg.find('span[imtype="msg_attach"]').css(
						'display', '');
					alert("错误码： " + obj.code + "; 错误描述：" + obj.msg);
				}, 100)
			}, function(sended, total, msgId) { // 进度条
				setTimeout(function() {
					var msg = $(document.getElementById(receiver + "_" + msgId));
					// console.log('send Attach message progress:' + (sended / total * 100 + '%'));
					// sended;//已发送字节数
					// total;//总字节数
					if (sended < total) {
						msg.find('div[class="bar"]').css(
							'width',
							(sended / total * 100 + '%'));
					} else {
						msg.find('div[class="bar"]').parent()
							.css('display', 'none');
						msg.find('span[imtype="msg_attach"]')
							.css('display', 'block');
					};
				}, 100)
			});
			oldMsg.attr("id", receiver + '_' + msgid);
			if (file instanceof Blob) {
				oldMsg.find("object").val(file);
			}
		},

		/**
		 * 发送附件
		 * 
		 * @param msgid
		 * @param file --
		 *            file对象
		 * @param type --
		 *            附件类型 2 语音消息 3 视频消息 4 图片消息 5 位置消息 6 文件消息
		 * @param receiver --
		 *            接收者
		 * @constructor
		 */
		EV_sendToDeskAttachMsg: function(oldMsgid, file, type, receiver, isresend) {
			console.log('send Attach message: type[' + type + ']...receiver:[' + receiver + ']');
			var obj = new RL_YTX.DeskMessageBuilder();
			obj.setFile(file);
			obj.setType(type);
			obj.setOsUnityAccount(receiver);

			/*if ($("#fireMessage").attr("class").indexOf("active") > -1) { //domain
				obj.setDomain("fireMessage");
			};*/
			var oldMsg = $(document.getElementById(receiver + '_' + oldMsgid));
			oldMsg.attr('msg', 'msg');
			oldMsg.css('display', 'block');
			$('#im_content_list').scrollTop($('#im_content_list')[0].scrollHeight);
			var msgid = RL_YTX.sendToDeskMessage(obj, function(obj) { // 成功
				setTimeout(function() {
					var msg = $(document.getElementById(receiver + "_" + obj.msgClientNo));
					msg.find('span[imtype="resend"]').css(
						'display', 'none');
					msg.find('div[class="bar"]').parent().css(
						'display', 'none');
					msg.find('span[imtype="msg_attach"]').css(
						'display', 'block');
					msg.attr('msg', 'msg');
					console.log('send Attach message succ');
					if (isresend) {
						$('#im_content_list').append(msg.prop("outerHTML"));
						msg.remove(); // 删掉原来的展示
					}
					$('#im_content_list').scrollTop($('#im_content_list')[0].scrollHeight);
				}, 100);
			}, function(obj) { // 失败
				setTimeout(function() {
					var msg = $(document.getElementById(receiver + "_" + obj.msgClientNo));
					msg.find('span[imtype="resend"]').css(
						'display', 'block');
					msg.find('div[class="bar"]').parent().css(
						'display', 'none');
					msg.find('span[imtype="msg_attach"]').css(
						'display', 'block');
					alert("错误码：" + obj.code + "; 错误描述：" + obj.msg);
				}, 100);
			}, function(sended, total, msgId) { // 进度条
				setTimeout(function() {
					var msg = $(document.getElementById(receiver + "_" + msgId));
					console.log('send Attach message progress:' + (sended / total * 100 + '%'));
					// sended;//已发送字节数
					// total;//总字节数
					if (sended < total) {
						msg.find('div[class="bar"]').css(
							'width',
							(sended / total * 100 + '%'));
					} else {
						msg.find('div[class="bar"]').parent()
							.css('display', 'none');
						msg.find('span[imtype="msg_attach"]')
							.css('display', 'block');
					}
				}, 100);
			});
			oldMsg.attr("id", receiver + '_' + msgid);
		},

		/**
		 * 事件，客服开始咨询
		 * 
		 * @param receiver --
		 *            客服号
		 * @constructor
		 */
		EV_startMcmMsg: function(receiver) {
			console.log('start MCM message...');
			var obj = new RL_YTX.DeskMessageStartBuilder();
			obj.setOsUnityAccount(receiver);
			obj.setUserData('');

			RL_YTX.startConsultationWithAgent(obj, function() {
				console.log('start MCM message succ...');
			}, function(obj) {
				alert("错误码： " + obj.code + "; 错误描述：" + obj.msg);
			});
		},

		/**
		 * 事件，客服停止咨询
		 * 
		 * @param receiver --
		 *            客服号
		 * @constructor
		 */
		EV_stopMcmMsg: function(receiver) {
			console.log('stop MCM message...');
			var obj = new RL_YTX.DeskMessageStopBuilder();
			obj.setOsUnityAccount(receiver);
			obj.setUserData('');

			RL_YTX.finishConsultationWithAgent(obj, function() {
				console.log('stop MCM message succ...');
			}, function(obj) {
				alert("错误码： " + obj.code + "; 错误描述：" + obj.msg);
			});
		},

		// /**
		//  * 事件，客服发送消息
		//  * 
		//  * @param msgid
		//  * @param text
		//  * @param receiver --
		//  *            客服号
		//  * @constructor
		//  */
		// EV_sendMcmMsg: function(oldMsgid, text, receiver, isresend) {
		// 	console.log('send MCM message...');
		// 	var obj = new RL_YTX.DeskMessageBuilder();
		// 	obj.setText(text);
		// 	obj.setUserData();
		// 	obj.setType(1);
		// 	obj.setOsUnityAccount(receiver);
		// 	/*if ($("#fireMessage").attr("class").indexOf("active") > -1) { //domain
		// 		obj.setDomain("fireMessage");
		// 	};*/

		// 	var msgid = RL_YTX.sendToDeskMessage(obj, function(obj) {
		// 		var msg = $(document.getElementById(receiver + "_" + obj.msgClientNo));
		// 		msg.find('span[imtype="resend"]').css('display', 'none');
		// 		console.log('send MCM message succ...');
		// 		if (isresend) {
		// 			$('#im_content_list').append(msg.prop("outerHTML"));
		// 			msg.remove(); // 删掉原来的展示
		// 		};
		// 		$('#im_content_list').scrollTop($('#im_content_list')[0].scrollHeight);
		// 	}, function(obj) {
		// 		var msgf = $(document.getElementById(receiver + '_' + obj.msgClientNo));
		// 		if (msgf.find('pre [msgtype="resendMsg"]').length == 0) {
		// 			var resendStr = '<pre msgtype="resendMsg" style="display:none;">' + text + '</pre>'
		// 			msgf.append(resendStr);
		// 		}
		// 		msgf.find('span[imtype="resend"]').css('display', 'block');
		// 		alert("错误码： " + obj.code + "; 错误描述：" + obj.msg);
		// 	});
		// 	$(document.getElementById(receiver + "_" + oldMsgid)).attr("id", receiver + "_" + msgid);
		// },

		/**
		 * 事件，主动拉取消息
		 * 
		 * @param sv
		 * @param ev
		 * @constructor
		 */
		/* EV_syncMsg : function(sv, ev) {
		     var obj = new RL_YTX.SyncMsgBuilder();
		     obj.setSVersion(sv);
		     obj.setEVersion(ev);

		     RL_YTX.syncMsg(obj, function(obj) {
		                 alert("错误码： " + obj.code+"; 错误描述："+obj.msg);
		             });
		 },*/

		/**
		 * 事件，获取登录者个人信息
		 * 
		 * @constructor
		 */
		EV_getMyInfo: function() {
			RL_YTX.getMyInfo(function(obj) {
				if (!!obj && !!obj.nickName) {
					IM._username = obj.nickName;
				};

				$('#navbar_login_show')
					.html('<span style="float: left;display: block;font-size: 20px;font-weight: 200;padding-top: 10px;padding-bottom: 10px;text-shadow: 0px 0px 0px;color:#eee" >您好:</span>' + '<a onclick="IM.DO_userMenu()" style="text-decoration: none;cursor:pointer;float: left;font-size: 20px;font-weight: 200;max-width:130px;' + 'padding-top: 10px;padding-right: 20px;padding-bottom: 10px;padding-left: 20px;text-shadow: 0px 0px 0px;color:#eee;word-break:keep-all;text-overflow:ellipsis;overflow: hidden;" >' + IM._username + '</a>' + '<span onclick="IM.EV_logout()" style="cursor:pointer;float: right;font-size: 20px;font-weight: 200;' + 'padding-top: 10px;padding-bottom: 10px;text-shadow: 0px 0px 0px;color:#eeeeee">退出</span>');

			}, function(obj) {
				if (520015 != obj.code) {
					alert("错误码： " + obj.code + "; 错误描述：" + obj.msg);
				}
			});
		},

		/**
		 * 添加PUSH消息，只做页面操作 供push和拉取消息后使用
		 * 
		 * @param obj
		 * @constructor
		 */
		DO_push_createMsgDiv: function(obj) {
			//判断是否是阅后即焚消息 
			var isFireMsg = false;
			/*if (IM._fireMessage == obj.msgDomain) {
				isFireMsg = true;
			}*/

			var b_isGroupMsg = ('g' == obj.msgReceiver.substr(0, 1));
			var inforSender = obj.msgSender; //消息发送者
			var name = obj.senderNickName || obj.msgSender; //显示的名称
			var you_sender = obj.msgSender; //消息接收者
			if (obj.msgSender != obj.msgReceiver & obj.msgReceiver != IM._user_account) {
				name = obj.msgReceiver;
				you_sender = obj.msgReceiver;
			}
			var isAtMsg = obj.isAtMsg ? "有人@你\n" : "";
			// push消息的联系人，是否是当前展示的联系人
			var b_current_contact_you = IM.DO_createMsgDiv_Help(you_sender,
				name, b_isGroupMsg);

			// 是否为mcm消息 0普通im消息 1 start消息 2 end消息 3发送mcm消息
			var you_msgContent = obj.msgContent;
			var content_type = null;
			//var version = obj.version;//改版
			var version = obj.msgId;
			var time = obj.msgDateCreated;
			if (0 == obj.mcmEvent) { // 0普通im消息
				// 点对点消息，或群组消息
				content_type = (b_isGroupMsg) ? IM._contact_type_g : IM._contact_type_c;
				var msgType = obj.msgType;
				var str = '';

				//消息类型1:文本消息 2：语音消息 3：视频消息 4：图片消息 5：位置消息 6：文件
				if (1 == msgType) {
					str = emoji.replace_unified(you_msgContent);
					if (isFireMsg) {
						str = '<pre fireMsg="yes">' + str + '</pre>';
					} else {
						str = '<pre>' + str + '</pre>';
					}
				} else if (2 == msgType) {
					//判断是否支持支持audio标签
					str = '<pre>您有一条语音消息,请用其他设备接收</pre>';
					/*
            		  var url = obj.msgFileUrl;
                      // str = '你接收了一条语音消息['+ url +']';
                      if(isFireMsg){
                           str = '<audio fireMsg="yes" style="display:none" controls="controls" src="' + url
                              + '">your browser does not surpport the audio element</audio>';
                      }else{
                           str = '<audio controls="controls" src="' + url
                              + '">your browser does not surpport the audio element</audio>';
                      }
                	*/

				} else if (3 == msgType) { // 3：视频消息

					var urlShow = obj.msgFileUrlThum; //小视频消息的缩略图地址
					var urlReal = obj.msgFileUrl;
					var windowWid = $(window).width();
					var imgWid = 0;
					var imgHei = 0;
					if (windowWid < 666) {
						imgWid = 100;
						imgHei = 150;
					} else {
						imgWid = 150;
						imgHei = 200;
					};
					var num = obj.msgFileSize;
					var size = 0;
					if (num < 1024) {
						size = num + "byte";
					} else if (num / 1024 >= 1 && num / Math.pow(1024, 2) < 1) {
						size = Number(num / 1024).toFixed(2) + "KB";
					} else if (num / Math.pow(1024, 2) >= 1 && num / Math.pow(1024, 3) < 1) {
						size = Number(num / Math.pow(1024, 2)).toFixed(2) + "MB";
					} else if (num / Math.pow(1024, 3) >= 1 && num / Math.pow(1024, 4) < 1) {
						size = Number(num / Math.pow(1024, 3)).toFixed(2) + "G";
					};
					if (isFireMsg) {
						str = '<div style="display:inline"><img fireMsg="yes" onclick="IM.DO_pop_phone(\'' + you_sender + '\', \'' + version + '\')" videourl="' + urlReal + '" src="' + urlShow + '" style="max-width:' + imgWid + 'px;max-height:' + imgHei + 'px;display:none;cursor:pointer" />' + '<span style="font-size: small;margin-left:15px;">' + size + '</span></div>';
					} else {
						str = '<div style="display:inline"><img onclick="IM.DO_pop_phone(\'' + you_sender + '\', \'' + version + '\')" videourl="' + urlReal + '" src="' + urlShow + '" style="cursor:pointer;max-width:' + imgWid + 'px;max-height:' + imgHei + 'px;" />' + '<span style="font-size: small;margin-left:15px;">' + size + '</span></div>';
					}

				} else if (4 == msgType) { // 4：图片消息
					var url = obj.msgFileUrl;
					var windowWid = $(window).width();
					var imgWid = 0;
					var imgHei = 0;
					if (windowWid < 666) {
						imgWid = 100;
						imgHei = 150;
					} else {
						imgWid = 150;
						imgHei = 200;
					}; //阅读即焚同步
					if (isFireMsg & inforSender != IM._user_account) {
						var str = '<img fireMsg="yes" src="' + url + '" style="cursor:pointer;max-width:' + imgWid + 'px; max-height:' + imgHei + 'px;display:none" onclick="IM.DO_pop_phone(\'' + you_sender + '\', \'' + version + '\')"/>';
					} else {
						var str = '<img imtype="msg_attach_src" src="' + url + '" style="cursor:pointer;max-width:' + imgWid + 'px; max-height:' + imgHei + 'px;" onclick="IM.DO_pop_phone(\'' + you_sender + '\', \'' + version + '\')"/>';
					}

				} else if (5 == msgType) { // 位置消息
					//str = '你接收了一条位置消息...';
					var jsonObj = eval('(' + you_msgContent + ')');
					var lat = jsonObj.lat; //纬度
					var lon = jsonObj.lon; //经度
					var title = jsonObj.title; //位置信息描述
					var windowWid = $(window).width();
					var imgWid = 0;
					var imgHei = 0;
					if (windowWid < 666) {
						imgWid = 100;
						imgHei = 150;
					} else {
						imgWid = 150;
						imgHei = 200;
					};
					var str = '<img src="img/baidu.png" style="cursor:pointer;max-width:' + imgWid + 'px; max-height:' + imgHei + 'px;" onclick="IM.DO_show_map(\'' + lat + '\', \'' + lon + '\', \'' + title + '\')"/>';

				} else if (6 == msgType) { //压缩文件  仅支持火狐和谷歌浏览器
					var url = obj.msgFileUrl;
					var num = obj.msgFileSize;
					var size = 0;
					if (num < 1024) {
						size = num + "byte";
					} else if (num / 1024 >= 1 && num / Math.pow(1024, 2) < 1) {
						size = Number(num / 1024).toFixed(2) + "KB";
					} else if (num / Math.pow(1024, 2) >= 1 && num / Math.pow(1024, 3) < 1) {
						size = Number(num / Math.pow(1024, 2)).toFixed(2) + "MB";
					} else if (num / Math.pow(1024, 3) >= 1 && num / Math.pow(1024, 4) < 1) {
						size = Number(num / Math.pow(1024, 3)).toFixed(2) + "G";
					};

					var fileName = obj.msgFileName;
					if (isFireMsg) {
						str = '<div style="display:inline"><a fireMsg="yes" href="' + url + '" target="_blank">' + '<span>' + '<img style="width:32px; height:32px; margin-right:5px; margin-left:5px;" src="/static/storeback/service/assets/img/attachment_icon.png" />' + '</span>' + '<span>' + fileName + '</span>' //+ '<span style="font-size: small;margin-left:15px;">'+size+'</span>'
							+ '</a>' + '<span style="font-size: small;margin-left:15px;">' + size + '</span></div>';
					} else {
						str = '<div style="display:inline"><a href="' + url + '" target="_blank">' + '<span>' + '<img style="width:32px; height:32px; margin-right:5px; margin-left:5px;" src="/static/storeback/service/assets/img/attachment_icon.png" />' + '</span>' + '<span>' + fileName + '</span>' //+ '<span style="font-size: small;margin-left:15px;">'+size+'</span>'
							+ '</a>' + '<span style="font-size: small;margin-left:15px;">' + size + '</span></div>';
					}
				} else if (7 == msgType) { // 非压缩文件
					var url = obj.msgFileUrl;
					var num = obj.msgFileSize;
					var size = 0;
					if (num < 1024) {
						size = num + "byte";
					} else if (num / 1024 >= 1 && num / Math.pow(1024, 2) < 1) {
						size = Number(num / 1024).toFixed(2) + "KB";
					} else if (num / Math.pow(1024, 2) >= 1 && num / Math.pow(1024, 3) < 1) {
						size = Number(num / Math.pow(1024, 2)).toFixed(2) + "MB";
					} else if (num / Math.pow(1024, 3) >= 1 && num / Math.pow(1024, 4) < 1) {
						size = Number(num / Math.pow(1024, 3)).toFixed(2) + "G";
					};

					var fileName = obj.msgFileName;

					if (isFireMsg) {
						str = '<div style="display:inline"><a fireMsg="yes" href="' + url + '" target="_blank">' + '<span>' + '<img style="width:32px; height:32px; margin-right:5px; margin-left:5px;" src="/static/storeback/service/assets/img/attachment_icon.png" />' + '</span>' + '<span>' + fileName + '</span>' //+ '<span style="font-size: small;margin-left:15px;">'+size+'</span>'
							+ '</a>' + '<span style="font-size: small;margin-left:15px;">' + size + '</span></div>';
					} else {
						str = '<div style="display:inline"><a href="' + url + '" target="_blank">' + '<span>' + '<img style="width:32px; height:32px; margin-right:5px; margin-left:5px;" src="/static/storeback/service/assets/img/attachment_icon.png" />' + '</span>' + '<span>' + fileName + '</span>' //+ '<span style="font-size: small;margin-left:15px;">'+size+'</span>'
							+ '</a>' + '<span style="font-size: small;margin-left:15px;">' + size + '</span></div>';
					}
				} else if (11 == msgType) {
					str = '<pre>' + you_msgContent + '</pre>';
					//zyh @群主
				}
				//zyh 需要一个消息的发送着
				IM.HTML_pushMsg_addHTML(msgType, you_sender, version,
					content_type, b_current_contact_you, name, str, inforSender, isAtMsg);

				//桌面提醒通知
				// IM.DO_deskNotice(you_sender, name, you_msgContent, msgType, isFireMsg, false, inforSender, isAtMsg);
			} else if (1 == obj.mcmEvent) { // 1 start消息
				IM.HTML_pushMsg_addHTML(obj.msgType, you_sender, version,
					IM._contact_type_m, b_current_contact_you, name,
					you_msgContent);
			} else if (2 == obj.mcmEvent) { // 2 end消息
				IM.HTML_pushMsg_addHTML(obj.msgType, you_sender, version,
					IM._contact_type_m, b_current_contact_you,
					name, "结束咨询");
			} else if (3 == obj.mcmEvent) { // 3发送mcm消息
				IM.HTML_pushMsg_addHTML(obj.msgType, you_sender, version,
					IM._contact_type_m, b_current_contact_you, name,
					you_msgContent);
			} else
			if (53 == obj.mcmEvent) { // 3发送mcm消息

				content_type = IM._contact_type_m;
				var msgType = obj.msgType;
				var str = '';

				//消息类型1:文本消息 2：语音消息 3：视频消息 4：图片消息 5：位置消息 6：文件
				if (1 == msgType) {
					msgType = 1;
					str = emoji.replace_unified(you_msgContent);
					if (isFireMsg) {
						str = '<pre fireMsg="yes">' + str + '</pre>';
					} else {
						str = '<pre>' + str + '</pre>';
					}
				} else if (4 == msgType) { // 4：图片消息
					var url = obj.msgFileUrl;
					var windowWid = $(window).width();
					var imgWid = 0;
					var imgHei = 0;
					if (windowWid < 666) {
						imgWid = 100;
						imgHei = 150;
					} else {
						imgWid = 150;
						imgHei = 200;
					};
					if (isFireMsg) {
						var str = '<img fireMsg="yes" src="' + url + '" style="max-width:' + imgWid + 'px; max-height:' + imgHei + 'px;display:none" onclick="IM.DO_pop_phone(\'' + you_sender + '\', \'' + version + '\')"/>';
					} else {
						var str = '<img src="' + url + '" style="max-width:' + imgWid + 'px; max-height:' + imgHei + 'px;" onclick="IM.DO_pop_phone(\'' + you_sender + '\', \'' + version + '\')"/>';
					}
				} else if (6 == msgType || 7 == msgType) { //统一按非压缩处理的
					var url = obj.msgFileUrl;
					var num = obj.msgFileSize;
					var size = 0;
					if (num < 1024) {
						size = num + "byte";
					} else if (num / 1024 >= 1 && num / Math.pow(1024, 2) < 1) {
						size = Number(num / 1024).toFixed(2) + "KB";
					} else if (num / Math.pow(1024, 2) >= 1 && num / Math.pow(1024, 3) < 1) {
						size = Number(num / Math.pow(1024, 2)).toFixed(2) + "MB";
					} else if (num / Math.pow(1024, 3) >= 1 && num / Math.pow(1024, 4) < 1) {
						size = Number(num / Math.pow(1024, 3)).toFixed(2) + "G";
					};
					var fileName = obj.msgFileName;
					if (isFireMsg) {
						str = '<a fireMsg="yes" style="display:none" href="' + url + '" target="_blank">' + '<span>' + '<img style="width:32px; height:32px; margin-right:5px; margin-left:5px;" src="/static/storeback/service/assets/img/attachment_icon.png" />' + '</span>' + '<span>' + fileName + '</span>' + '<span style="font-size: small;margin-left:15px;">' + size + '</span>' + '</a>';
					} else {
						str = '<a href="' + url + '" target="_blank">' + '<span>' + '<img style="width:32px; height:32px; margin-right:5px; margin-left:5px;" src="/static/storeback/service/assets/img/attachment_icon.png" />' + '</span>' + '<span>' + fileName + '</span>' + '<span style="font-size: small;margin-left:15px;">' + size + '</span>' + '</a>';
					}
				}
				IM.HTML_pushMsg_addHTML(msgType, you_sender, version,
					content_type, b_current_contact_you, name, str);
			}
		},

		DO_pop_phone: function(you_sender, version, obj) {
			var msgId = '';
			if (obj) {
				msgId = $(obj).parent().parent()[0].id;
			} else {
				msgId = you_sender + '_' + version;
			}
			IM._msgId = msgId;

			var msg = $(document.getElementById(msgId));
			debugger;

			var videoUrl = msg.find('img').attr("videourl");
			var str = '';
			var showHei = $("#lvjing").height() - 50; //客户端竖屏视频需要拖动滚动条才能露出控制按钮，所以减去50px
			if (!!videoUrl) {

				var type = videoUrl.substring(videoUrl.lastIndexOf(".") + 1);

				str = '<video controls="controls" preload="auto" height="' + showHei + 'px" style="position:relative;top:-20px;left:0px;">' +
					'<source src="' + videoUrl + '" type="video/' + type + '" /></video>';

			} else {
				var url = msg.find('img[imtype="msg_attach_src"]').attr('src');
				//msg.find('img[imtype="msg_attach_src"]').attr('src', url);
				str = '<a onclick="IM.HTML_pop_photo_hide()"><img src="' + url + '" /></a>';
			};
			$("#carousels").empty();
			$("#carousels").append(str);

			IM.HTML_pop_photo_show();
		},

		/**
		 * lat 纬度
		 * lon 经度
		 * title 位置信息描述
		 */
		DO_show_map: function(lat, lon, title) {
			$("#im_body").append("<div id='baiduMap' style='z-index:888899; margin-left: 10%;margin-right:10%; height: 550px; width: 80%;'></div>");
			$("#carousels").empty();
			var map = new BMap.Map("baiduMap"); // 创建地图实例 
			var point = new BMap.Point(lon, lat); // 创建点坐标 
			var marker = new BMap.Marker(point); // 创建标注    
			map.addOverlay(marker);
			map.centerAndZoom(point, 15);
			var opts = {
				width: 200,
				height: 100,
				enableMessage: true //设置允许信息窗发送短息
			};
			var infoWindow = new BMap.InfoWindow(title, opts); // 创建信息窗口对象 
			marker.addEventListener("click", function() {
				map.openInfoWindow(infoWindow, point); //开启信息窗口
			});

			IM._baiduMap = $("#baiduMap");
			$("#carousels").append(IM._baiduMap);
			$("#baiduMap").show();
			IM.HTML_pop_photo_show();
		},

		/**
		 * 向上选择图片，同一个对话框内
		 * 
		 * @constructor
		 */
		DO_pop_photo_up: function() {

			var msg = $(document.getElementById(IM._msgId));
			if (msg.length < 1) {
				return;
			};

			var index = -1;
			msg.parent().find('div[msg="msg"][im_carousel="real"]:visible').each(
				function() {
					index++;
					if (IM._msgId == $(this).attr('id')) {
						index--;
						return false;
					};
				});
			if (index < 0) {
				return;
			};
			var prevMsg = msg.parent().children('div[msg="msg"][im_carousel="real"]:visible').eq(index);
			if (prevMsg.length < 1) {
				return;
			};
			var str = '';
			var showHei = $("#lvjing").height() - 50; //客户端竖屏视频需要拖动滚动条才能露出控制按钮，所以减去50px
			if (prevMsg.attr("im_msgtype") == 4) {

				var src = prevMsg.find('img').attr('src');
				str = '<img src="' + src + '" />';
			} else {
				var videoUrl = prevMsg.find('img').attr("videourl");
				var type = videoUrl.substring(videoUrl.lastIndexOf(".") + 1);

				str = '<video controls="controls" preload="auto" height="' + showHei + 'px" style="position:relative;top:-20px;left:0px;">' +
					'<source src="' + videoUrl + '" type="video/' + type + '" /></video>';
			};
			IM._msgId = prevMsg.attr('id');
			$("#carousels").empty();
			$("#carousels").append(str);
			if ($("#carousels").find("img")) {
				$("#carousels").find("img").css('max-height', (showHei - 30) + "px").css(
					'max-width', ($(window).width() - 50) + "px");
			};
			var q = 1;

		},

		/**
		 * 向下选择图片,同一个对话框内
		 * 
		 * @constructor
		 */
		DO_pop_photo_down: function() {

			var msg = $(document.getElementById(IM._msgId));
			if (msg.length < 1) {
				return;
			}

			var index = -1;
			msg.parent().find('div[msg="msg"][im_carousel="real"]:visible').each(
				function() {
					index++;
					if (IM._msgId == $(this).attr('id')) {
						index++;
						return false;
					}
				});
			if (index < 0) {
				return;
			}
			var nextMsg = msg.parent().children('div[msg="msg"][im_carousel="real"]:visible').eq(index);
			if (nextMsg.length < 1) {
				return;
			}
			var showHei = $("#lvjing").height() - 50; //客户端竖屏视频需要拖动滚动条才能露出控制按钮，所以减去50px
			if (nextMsg.attr("im_msgtype") == 4) {
				var src = nextMsg.find('img').attr('src');
				str = '<img src="' + src + '" />';
			} else {
				var videoUrl = nextMsg.find('img').attr("videourl");
				var type = videoUrl.substring(videoUrl.lastIndexOf(".") + 1);

				str = '<video controls="controls" preload="auto" height="' + showHei + 'px" style="position:relative;top:-20px;left:0px;">' +
					'<source src="' + videoUrl + '" type="video/' + type + '" /></video>';
			};
			IM._msgId = nextMsg.attr('id');
			$("#carousels").empty();
			$("#carousels").append(str);
			if ($("#carousels").find("img")) {
				$("#carousels").find("img").css('max-height', (showHei - 30) + "px").css(
					'max-width', ($(window).width() - 50) + "px");
			}
		},

		/**
		 * 添加群组事件消息，只处理页面
		 * 
		 * @param obj:confirm 1拒绝 2同意  target 1讨论组 2群组
		 *            auditType (1申请加入群组，2邀请加入群组，3直接加入群组，4解散群组，5退出群组，6踢出群组，7确认申请加入，8确认邀请加入，
		 *                       9邀请加入群组的用户因本身群组个数超限加入失败(只发送给邀请者)10管理员修改群组信息，11用户修改群组成员名片12新增管理员变更通知)
		 * @constructor
		 */
		DO_notice_createMsgDiv: function(obj) {
			var you_sender = IM._serverNo;
			var groupId = obj.groupId;
			var name = '系统通知';
			var groupName = obj.groupName;
			var version = obj.msgId;

			var peopleId = obj.member;
			var people = (!!obj.memberName) ? obj.memberName : obj.member;
			var you_msgContent = '';
			var noticeContent = '';
			// 1,(1申请加入群组，2邀请加入群组，3直接加入群组，4解散群组，5退出群组，6踢出群组，7确认申请加入，8确认邀请加入，
			//9邀请加入群组的用户因本身群组个数超限加入失败(只发送给邀请者)10管理员修改群组信息，11用户修改群组成员名片12新增管理员变更通知)
			var auditType = obj.auditType;
			var groupTarget = (obj.target == 2) ? "群组" : "讨论组";
			if (1 == auditType) {
				you_msgContent = '[' + people + ']申请加入' + groupTarget + '[' + groupName + '] <span imtype="notice">{<a style="color: red; cursor: pointer;" onclick="IM.EV_confirmJoinGroup(\'' + you_sender + '\', \'' + version + '\', \'' + groupId + '\', \'' + peopleId + '\', 2)">同意</a>}{<a style="color: red; cursor: pointer;" onclick="IM.EV_confirmJoinGroup(\'' + you_sender + '\', \'' + version + '\', \'' + groupId + '\', \'' + peopleId + '\', 1)">拒绝</a>}</span>';
				noticeContent = '[' + people + ']申请加入' + groupTarget + '[' + groupName + '] ';
			} else if (2 == auditType) {
				if (1 == obj.confirm) {
					you_msgContent = '[' + groupName + ']管理员邀请您加入' + groupTarget;
					noticeContent = you_msgContent;
					// 在群组列表中添加群组项
					var current_contact_type = IM.HTML_find_contact_type();
					var isShow = false;
					if (IM._contact_type_g == current_contact_type) {
						isShow = true;
					}
					IM.HTML_addContactToList(groupId, groupName,
						IM._contact_type_g, false, isShow, false, null,
						null, null);
				} else {
					you_msgContent = '[' + groupName + ']管理员邀请您加入群组 <span imtype="notice">{<a style="color: red; cursor: pointer;" onclick="IM.EV_confirmInviteJoinGroup(\'' + you_sender + '\', \'' + groupName + '\', \'' + version + '\', \'' + obj.admin + '\', \'' + groupId + '\', 2)">同意</a>}{<a style="color: red; cursor: pointer;" onclick="IM.EV_confirmInviteJoinGroup(\'' + you_sender + '\', \'' + groupName + '\', \'' + version + '\', \'' + obj.admin + '\', \'' + groupId + '\', 1)">拒绝</a>}</span>';
					noticeContent = '[' + groupName + ']管理员邀请您加入群组;';
				}
			} else if (3 == auditType) {
				you_msgContent = '[' + people + ']直接加入群组[' + groupName + ']';
				noticeContent = you_msgContent;
				IM.DO_procesGroupNotice(auditType, groupId, peopleId, people);
			} else if (4 == auditType) {
				you_msgContent = '管理员解散了群组[' + groupName + ']';
				noticeContent = you_msgContent;
				// 将群组从列表中移除
				IM.HTML_remove_contact(groupId);
				IM.DO_procesGroupNotice(auditType, groupId, peopleId, people);
			} else if (5 == auditType) {
				you_msgContent = '[' + people + ']退出了' + groupTarget + '[' + groupName + ']';
				noticeContent = you_msgContent;
				IM.DO_procesGroupNotice(auditType, groupId, peopleId, people);
			} else if (6 == auditType) {
				you_msgContent = '[' + groupName + ']管理员将[' + people + ']踢出' + groupTarget;
				noticeContent = you_msgContent;
				// 将群组从列表中移除
				if (IM._user_account == people) {
					IM.HTML_remove_contact(groupId);
				}
				IM.DO_procesGroupNotice(auditType, groupId, peopleId, people);
			} else if (7 == auditType) {
				you_msgContent = '管理员同意[' + people + ']加入群组[' + groupName + ']的申请';
				noticeContent = you_msgContent;
				IM.DO_procesGroupNotice(auditType, groupId, peopleId, people);
			} else if (8 == auditType) {
				if (2 != obj.confirm) {
					you_msgContent = '[' + people + ']拒绝了群组[' + groupName + ']的邀请';
					noticeContent = you_msgContent;
				} else {
					you_msgContent = '[' + people + ']同意了管理员的邀请，加入群组[' + groupName + ']';
					noticeContent = you_msgContent;
					IM.DO_procesGroupNotice(auditType, groupId, peopleId,
						people);
				}
			} else if (10 == auditType) {

				people = (!!obj.adminName) ? obj.adminName : obj.admin;
				if (obj.target == 2) {
					you_msgContent = '管理员修改了群组[' + groupName + ']信息';
				} else {
					you_msgContent = '用户[' + people + ']修改了讨论组[' + groupName + ']信息';
				}

				noticeContent = you_msgContent;
				if (!!obj.groupName) {
					IM.HTML_addContactToList(groupId, obj.groupName,
						IM._contact_type_g, false, isShow, true, null,
						null, null);
				}
				IM.DO_procesGroupNotice(auditType, groupId, peopleId, people,
					obj.groupName, obj.ext);
			} else if (11 == auditType) {
				you_msgContent = '用户[' + people + ']修改群组成员名片';
				noticeContent = you_msgContent;
				// TODO obj.memberName有值，意味着要修改展示的名字
				IM.DO_procesGroupNotice(auditType, groupId, peopleId,
					obj.memberName, obj.groupName, obj.ext);
			} else if (12 == auditType) {
				you_msgContent = '用户[' + people + ']成为' + groupTarget + '[' + groupName + ']管理员12';
				noticeContent = you_msgContent;
				IM.DO_procesGroupNotice(auditType, groupId, peopleId,
					obj.memberName, obj.groupName, obj.ext);
			} else if (13 == auditType) {
				var role;
				var ext = JSON.parse(obj.ext);
				switch (ext.role) {
					case "1":
						role = "群主";
						break;
					case "2":
						role = "管理员";
						break;
					case "3":
						role = "成员";
						break;
					default:
						break;
				}
				you_msgContent = '用户[' + people + ']成为' + groupTarget + groupName + role;
				noticeContent = you_msgContent;
				IM.DO_procesGroupNotice(auditType, groupId, peopleId,
					obj.memberName, obj.groupName, obj.ext);
			} else {
				you_msgContent = '未知type[' + auditType + ']';
				noticeContent = you_msgContent;
			}

			// 添加左侧消息
			// 监听消息的联系人，是否是当前展示的联系人
			var b_current_contact_you = IM.DO_createMsgDiv_Help(you_sender,
				name, true);

			// 添加右侧消息
			IM.HTML_pushMsg_addHTML(1, you_sender, version, IM._contact_type_g,
				b_current_contact_you, groupName, you_msgContent);
			//桌面提醒通知
			// IM.DO_deskNotice('', '', noticeContent, '', false, false);
		},

		/**
		 * 群组pop层展示
		 *
		 * @constructor
		 */
		HTML_pop_photo_show: function() {
			IM.HTML_LJ_block('photo');

			var navbarHei = $('#navbar').height();
			var lvjingHei = $('#lvjing').height();
			var pop_photo = $('#pop_photo');

			pop_photo.find('img').css('max-height', lvjingHei - 30).css(
				'max-width', $(window).width() - 50);
			pop_photo.css('top', navbarHei);

			var d = $(window).scrollTop();
			// a+b=c
			var a = parseInt(pop_photo.find('div[imtype="pop_photo_top"]')
				.css('margin-top'));
			var b = parseInt(pop_photo.find('div[imtype="pop_photo_top"]')
				.css('height'));
			var c = $(window).height();

			if (a + b >= c) {
				d = 0;
			} else if (d + b >= c) {
				d = c - b - 20;
			}
			pop_photo.find('div[imtype="pop_photo_top"]').css('margin-top', d);
			$(window).scrollTop(d);

			pop_photo.show();
		},

		DO_checkPopShow: function(groupId) {
			if ($('#pop_group_' + groupId).length <= 0) {
				return false;
			}
			var display = $('#pop').css("display");
			if (display != 'block') {
				return false;
			}
			return true;
		},

		/**
		 * 删除联系人，包括左侧和右侧
		 * 
		 * @param id
		 * @constructor
		 */
		HTML_remove_contact: function(id) {
			// 删除左侧联系人列表
			$(document.getElementById('im_contact_' + id)).remove();
			// 删除右侧相应消息
			$('#im_content_list').find('div[content_you="' + id + '"]').each(
				function() {
					$(this).remove();
				});
		},

		/**
		 * 添加消息列表的辅助方法 消息的联系人(you_sender)，是否是当前展示的联系人
		 * 并处理左侧联系人列表的展示方式（新增条数，及提醒数字变化）
		 * 
		 * @param you_sender
		 * @param b_isGroupMsg --
		 *            true:group消息列表 false:点对点消息列表
		 * @returns {boolean} -- true:是当前展示的联系人；false:不是
		 * @constructor
		 */
		DO_createMsgDiv_Help: function(you_sender, name, b_isGroupMsg) {
			// 处理联系人列表，如果新联系人添加一条新的到im_contact_list，如果已经存在给出数字提示
			var b_current_contact_you = false; // push消息的联系人(you_sender)，是否是当前展示的联系人
			$('#im_contact_list').find('li').each(function() {
				if (you_sender == $(this).attr('contact_you')) {
					if ($(this).hasClass('active')) {
						b_current_contact_you = true;
					}
				}
			});

			// 新建时判断选中的contact_type是那个然后看是否需要显示
			var current_contact_type = IM.HTML_find_contact_type();
			var isShow = false;
			if (IM._contact_type_g == current_contact_type && b_isGroupMsg) {
				isShow = true;
			}
			if (IM._contact_type_c == current_contact_type && !b_isGroupMsg) {
				isShow = true;
			}
			IM.HTML_addContactToList(you_sender, name, (b_isGroupMsg) ? IM._contact_type_g : IM._contact_type_c, false, isShow, false, null,
				null, null);

			return b_current_contact_you;
		},

		/**
		 * 查找当前选中的contact_type值
		 * 
		 * @returns {*}
		 * @constructor
		 */
		HTML_find_contact_type: function() {
			// 在群组列表中添加群组项
			var current_contact_type = null;
			$('#im_contact_type').find('li').each(function() {
				if ($(this).hasClass('active')) {
					current_contact_type = $(this).attr('contact_type');
				}
			});
			return current_contact_type;
		},

		/**
		 * 样式，push监听到消息时添加右侧页面样式
		 * 
		 * @param msgtype --
		 *            消息类型1:文本消息 2：语音消息 3：视频消息 4：图片消息 5：位置消息 6：文件
		 * @param you_sender --
		 *            对方帐号：发出消息时对方帐号，接收消息时发送者帐号
		 * @param version --
		 *            消息版本号，本地发出时为long时间戳
		 * @param content_type --
		 *            C G or M
		 * @param b --
		 *            是否需要展示 true显示，false隐藏
		 * @param name --
		 *            显示对话框中消息发送者名字
		 * @param you_msgContent --
		 *            消息内容
		 * @constructor
		 */
		HTML_pushMsg_addHTML: function(msgtype, you_sender, version,
			content_type, b, name, you_msgContent, inforSender, isAtMsg) {

			var userPicUrl = null;
			if (IM.isNull(you_sender)) {
				userPicUrl = '../../../../static/storeback/service/img/avtar.gif';
			}
			if(you_sender == 'KF10089'){
				userPicUrl = '../../../../static/storeback/service/img/avtar.gif';
			}else{
				//userPicUrl = IM.getCustomerHeadPic(you_sender);
				userPicUrl = IM.getStoreLogo(you_sender);
			}
			var carou = '';
			if (msgtype == 4 || msgtype == 3) {
				carou = "real";
			};
			if (isAtMsg) {
				if (you_msgContent.indexOf('@' + IM._user_account) > -1) {
					you_msgContent = you_msgContent;
				}
			}
			if (inforSender != IM._user_account) {
				if (you_sender != IM._serverNo) {
					name = inforSender;
				}
				if (!inforSender || content_type.toUpperCase("G")) {
					inforSender = you_sender; //由于系统消息没有发送者，所以这里给定义为系统的发送者.群消息的发送者为群。
				}
				var str = '<div class="m_collect" msg="msg" im_carousel="' + carou + '" im_msgtype="' + msgtype + '" id="' + inforSender + '_' + version + '" content_type="' + content_type + '" content_you="' + you_sender + '" class="alert alert-left alert-info" style="display:' + ((b) ? 'block' : 'none') + '">' + '<code style=";text-overflow:ellipsis;overflow: hidden;">' + '<img src="' + userPicUrl + '">' + ':</code>&nbsp;' + you_msgContent + '</div>';
			} else {
				name = inforSender;
				var str = '<div class="m_my" contactor="sender" im_carousel="' + carou + '" msg="msg" im_msgtype="' + msgtype + '" id="' + you_sender + '_' + version + '" content_type="' + content_type + '" content_you="' + you_sender + '" class="alert alert-right alert-success" style="display:' + ((b) ? 'block' : 'none') + '">' + '<span imtype="resend" class="add-on" onclick="IM.EV_resendMsg(this)"' + 'style="display:none;cursor:pointer; position: relative; left: -40px; top: 0px;"><i class="icon-repeat"></i></span>' + '<code class="pull-right" style=";text-overflow:ellipsis;overflow: hidden;">&nbsp;:' + '<img src="../../../../static/storeback/service/img/avtar.gif">' + '</code>' + you_msgContent + '</div>';
			}
			$('#im_content_list').append(str);
			if (you_msgContent.indexOf("fireMsg") > -1) { //fireMsg="yes"
				var id = you_sender + "_" + version;
				$(document.getElementById(id)).find("code").next().hide();
				var windowWid = $(window).width();
				var imgWid = 0;
				var imgHei = 0;
				if (windowWid < 666) {
					imgWid = 100;
					imgHei = 150;
				} else {
					imgWid = 150;
					imgHei = 200;
				};
				var fireMsgStr = '<img style="cursor:pointer;max-width:' + imgWid + 'px; max-height:' + imgHei + 'px; margin-right:5px; margin-left:5px;" ' +
					'/static/storeback/service/sassets/img/rc="fireMessageImg.png" onclick="IM.DO_showFireMsg(\'' + id + '\',\'' + msgtype + '\')" />';
				$(document.getElementById(id)).append(fireMsgStr);
			}
			setTimeout(function() {
				$('#im_content_list').scrollTop($('#im_content_list')[0].scrollHeight);
			}, 100);

			// 右侧列表添加数字提醒
			// TODO 后期要添加提醒数字时，记得要先拿到旧值，再+1后写入新建的列表中
			var current_contact = $(document.getElementById('im_contact_' + you_sender));
			if (!current_contact.hasClass('active')) {
				var warn = current_contact.find('span[contact_style_type="warn"]');
				if ('99+' == warn.html()) {
					return;
				}
				var warnNum = parseInt((!!warn.html()) ? warn.html() : 0) + 1;
				if (warnNum > 99) {
					warn.html('99+');
				} else {
					warn.html(warnNum);
				}
				warn.show();

				var count = $("#msgCount");
				if('99+' == count.html()){
					return;
				}
				var countNum = parseInt((!!count.html())?count.html() : 0) + 1;
				if (countNum > 99) {
					count.html('99+');
				} else {
					count.html(countNum);
				}				
				count.show();
			}
			if (you_msgContent) $("#notice").html(""); //消息类型提示，发完消息接收方隐藏即可
		},
		HTML_pushCall_addHTML: function(you_sender, callId, you_msgContent) {
			// push消息的联系人，是否是当前展示的联系人

			var userPicUrl = null;
			if (IM.isNull(you_sender)) {
				userPicUrl = '../../../../static/storeback/service/img/avtar.gif';
			}
			if(you_sender == 'KF10089'){
				userPicUrl = '../../../../static/storeback/service/img/avtar.gif';
			}else{
				//userPicUrl = IM.getCustomerHeadPic(you_sender);
				userPicUrl = IM.getStoreLogo(you_sender);
			}

			var b = IM.DO_createMsgDiv_Help(you_sender, you_sender, false);
			var str = '<div class="m_collect" msg="msg" id="' + you_sender + '_' + callId + '" content_you="' + you_sender + '" class="alert alert-left alert-info" style="display:' + ((b) ? 'block' : 'none') + '">' + '<code style=";text-overflow:ellipsis;overflow: hidden;">' + '<img src="' + userPicUrl + '">' + ':</code>&nbsp;' + you_msgContent + '</div>';
			$('#im_content_list').append(str);

			setTimeout(function() {
				$('#im_content_list').scrollTop($('#im_content_list')[0].scrollHeight);
			}, 100);

			// 右侧列表添加数字提醒
			// TODO 后期要添加提醒数字时，记得要先拿到旧值，再+1后写入新建的列表中
			var current_contact = $(document.getElementById('im_contact_' + you_sender));
			if (!current_contact.hasClass('active')) {
				var warn = current_contact.find('span[contact_style_type="warn"]');
				if ('99+' == warn.html()) {
					return;
				}
				var warnNum = parseInt((!!warn.html()) ? warn.html() : 0) + 1;
				if (warnNum > 99) {
					warn.html('99+');
				} else {
					warn.html(warnNum);
				}
				warn.show();
				var count = $("#msgCount");
				if ('99+' == count.html()) {
					return;
				}
				var countNum = parseInt((!!count.html()) ? count.html() : 0) + 1;
				if (countNum > 99) {
					count.html('99+');
				} else {
					count.html(countNum);
				}
				count.show();
			}
		},
		HTML_pushMsg_addPreHTML: function(msgtype, you_sender, version,
			content_type, b, name, you_msgContent) {
			var userPicUrl = null;
			if (IM.isNull(you_sender)) {
				userPicUrl = '../../../../static/storeback/service/img/avtar.gif';
			}
			if(you_sender == 'KF10089'){
				userPicUrl = '../../../../static/storeback/service/img/avtar.gif';
			}else{
				//userPicUrl = IM.getCustomerHeadPic(you_sender);
				userPicUrl = IM.getStoreLogo(you_sender);
			}
			var carou = '';
			if (msgtype == 4 || msgtype == 3) {
				carou = "real";
			};
			var str = '<div class="m_collect" msg="msg" im_carousel="' + carou + '" im_msgtype="' + msgtype + '" id="' + you_sender + '_' + version + '" content_type="' + content_type + '" content_you="' + you_sender + '" class="alert alert-left alert-info" style="display:' + ((b) ? 'block' : 'none') + '">' + '<code style=";text-overflow:ellipsis;overflow: hidden;">' + '<img src="' + userPicUrl + '">' + ':</code>&nbsp;' + you_msgContent + '</div>';
			$('#im_content_list').prepend(str);

			setTimeout(function() {
				$('#im_content_list').scrollTop($('#im_content_list')[0].scrollHeight);
			}, 100);

			// 右侧列表添加数字提醒
			// TODO 后期要添加提醒数字时，记得要先拿到旧值，再+1后写入新建的列表中
			var current_contact = $(document.getElementById('im_contact_' + you_sender));
			if (!current_contact.hasClass('active')) {
				var warn = current_contact.find('span[contact_style_type="warn"]');
				if ('99+' == warn.html()) {
					return;
				}
				var warnNum = parseInt((!!warn.html()) ? warn.html() : 0) + 1;
				if (warnNum > 99) {
					warn.html('99+');
				} else {
					warn.html(warnNum);
				}
				warn.show();

				var count = $("#msgCount");
				if ('99+' == count.html()) {
					return;
				}
				var countNum = parseInt((!!count.html()) ? count.html() : 0) + 1;
				if (countNum > 99) {
					count.html('99+');
				} else {
					count.html(countNum);
				}
				count.show();
			}
		},

		/**
		 * 样式，发送消息时添加右侧页面样式
		 * 
		 * @param msg --
		 *            是否为临时消息 msg、temp_msg;msg
		 *            右侧对话消息display为block；temp_msg用于发送本地文件；需要点击确定的时候resendMsg方法中修改属性为block
		 * @param msgtype --
		 *            消息类型1:文本消息 2：语音消息 3：视频消息 4：图片消息 5：位置消息 6：文件
		 * @param msgid --
		 *            消息版本号，本地发出时均采用时间戳long
		 * @param content_type --
		 *            C G or M
		 * @param content_you --
		 *            对方帐号：发出消息时对方帐号，接收消息时发送者帐号
		 * @param im_send_content --
		 *            消息内容
		 * @constructor
		 */
		HTML_sendMsg_addHTML: function(msg, msgtype, msgid, content_type,
			content_you, im_send_content) {
			var storeUuid = $("#navbar_user_account").val();
			var logoUrl = null;
			if (IM.isNull(storeUuid)) {
				logoUrl = '../../../../static/storeback/service/img/avtar.gif';
			}else{
				//logoUrl = IM.getStoreLogo(storeUuid);
				logoUrl = IM.getCustomerHeadPic(storeUuid);
			}

			im_send_content = emoji.replace_unified(im_send_content);

			var display = ('temp_msg' == msg) ? 'none' : 'block';
			var carou = '';
			if (msgtype == 4 || msgtype == 3) {
				carou = "real";
			};
			var str = '<div class="my" contactor="sender" im_carousel="' + carou + '" msg="msg" im_msgtype="' + msgtype + '" id="' + content_you + '_' + msgid + '" content_type="' + content_type + '" content_you="' + content_you + '" class="alert alert-right alert-success" style="display:' + display + '">' + '<span imtype="resend" class="add-on" onclick="IM.EV_resendMsg(this)"' + ' style="display:none; cursor:pointer; position: relative; left: -40px; top: 0px;"><i class="icon-repeat"></i></span>' + '<code class="pull-right" style=";text-overflow:ellipsis;overflow: hidden;">' + '<img src="'+logoUrl+'">' +  '</code>' + im_send_content + '</div>';

			$('#im_content_list').append(str);

			$('#im_content_list')
				.scrollTop($('#im_content_list')[0].scrollHeight);

			return content_you + '_' + msgid;
		},

		HTML_sendMsg_addPreHTML: function(msg, msgtype, msgid, content_type, content_you, im_send_content) {
			if (!!im_send_content) {
				im_send_content = emoji.replace_unified(im_send_content);
			};

			var display = ('temp_msg' == msg) ? 'none' : 'block';
			var carou = '';
			if (msgtype == 4 || msgtype == 3) {
				carou = "real";
			};
			var str = '<div contactor="sender" im_carousel="' + carou + '" msg="msg" im_msgtype="' + msgtype + '" id="' + content_you + '_' + msgid + '" content_type="' + content_type + '" content_you="' + content_you + '" class="alert alert-right alert-success" style="display:' + display + '">' + '<span imtype="resend" class="add-on" onclick="IM.EV_resendMsg(this)"' + ' style="display:none; cursor:pointer; position: relative; left: -40px; top: 0px;"><i class="icon-repeat"></i></span>' + '<code class="pull-right" style=";text-overflow:ellipsis;overflow: hidden;">&nbsp;:' + IM._username + '</code>' + im_send_content + '</div>';

			$('#im_content_list').prepend(str);
			var hisStr = $("#getHistoryMsgDiv").prop('outerHTML');
			$("#getHistoryMsgDiv").remove();
			$('#im_content_list').prepend(hisStr);
			$('#im_send_content').html('');
			$('#im_content_list')
				.scrollTop($('#im_content_list')[0].scrollHeight);

			return content_you + '_' + msgid;
		},

		/**
		 * 选择联系人列表，并切换消息列表
		 * 
		 * @param contact_type
		 * @param contact_you
		 * @param isNew 是否是5.2.1之后的SDK
		 */
		DO_chooseContactList: function(contact_type, contact_you, isNewUserState) {
			newUserState = isNewUserState ? isNewUserState : false;
			IM.HTML_clean_im_contact_list();
			//$("#fireMessage").removeClass("active");
			var current_contact = document.getElementById("im_contact_" + contact_you);
			$(current_contact).addClass('active');
			var warn = $(current_contact).find('span[contact_style_type="warn"]');
			var count = $("#msgCount");
			count.hide();
			count.html(0);
			warn.hide();
			warn.html(0);
			/* 暂时屏蔽历史消息功能*/
           /* $("#getHistoryMsgDiv").html('<a href="#" onclick="IM.DO_getHistoryMessage();" style="font-size: small;position: relative;top: -30px;">查看更多消息</a>');
            $("#getHistoryMsgDiv").show();*/
          
			IM.HTML_clean_im_content_list(contact_you);

			//显示用户的状态
			if (IM._contact_type_c == contact_type) {
				//$("#fireMessage").show();3
				$("#voipInvite").show();
				$("#voiceInvite").show();
				$("#luodi").show();
				contact_you = contact_you.split(","); //专为数组传入
				IM.EV_getUserState(contact_you,newUserState); //这里需要见一个额开发任务获取用户状态的返回参数变了；由单个{}对象变成[{},...]这种数组对象了
			} else if (IM._contact_type_g == contact_type) {
				//$("#fireMessage").hide();
				$("#voipInvite").hide();
				$("#voiceInvite").hide();
				$("#luodi").hide();
			}
			// 如果当前选择的是客服列表直接发起咨询
			if (IM._contact_type_m == contact_type) {
				IM.EV_startMcmMsg(contact_you);
				IM._isMcm_active = true;
			} else {
				if (IM._isMcm_active) {
					IM.EV_stopMcmMsg(contact_you);
				}
			}
		},

		EV_getUserState: function(contact_you,isNew) {
			var current_contact = document.getElementById("im_contact_" + contact_you);
			var getUserStateBuilder = new RL_YTX.GetUserStateBuilder();
			getUserStateBuilder.setNewUserstate(isNew);
			getUserStateBuilder.setUseracc(contact_you);
			var onlineState = $(current_contact).find('span[contact_style_type="onlineState"]');
			RL_YTX.getUserState(getUserStateBuilder, function(obj) {
				if (obj[0].state == 1) { //1在线 2离线
					onlineState.html(" ");
					onlineState.show();
					onlineState.css("background-color", "#7ED153");
					onlineState.css("border-radius", "100%");
					onlineState.css("width", "10px");
					onlineState.css("height", "10px");
				} else if (obj[0].state == 2) {
					onlineState.html(" ");
					onlineState.show();
					onlineState.css("background-color", "#DEDEDE");
					onlineState.css("border-radius", "100%");
					onlineState.css("width", "10px");
					onlineState.css("height", "10px");
				} else {
					alert("错误码：" + obj[0].state + "; 错误描述：获得用户状态结果不合法")
				}
			}, function(obj) {
				if (174006 != obj.code) {
					alert("错误码：" + obj.code + "; 错误描述：" + obj.msg)

				}
			});	
		},

		/**
		 * 清理右侧消息列表
		 * 
		 * @param contact_you --
		 *            左侧联系人列表中的
		 */
		HTML_clean_im_content_list: function(contact_you) {

			$('#im_content_list').find('div[msg="msg"]').each(function() {
				if ($(this).attr('content_you') == contact_you) {
					$(this).show();
				} else {
					$(this).hide();
				}
			});

			$('#im_content_list').scrollTop($('#im_content_list')[0].scrollHeight);
		},

		/**
		 * 清理联系人列表样式
		 */
		HTML_clean_im_contact_list: function() {
			// 清除选中状态class
			$('#im_contact_list').find('li').each(function() {
				$(this).removeClass('active');
			});
		},

		/**
		 * 添加联系人到列表中
		 */
		DO_addContactToList: function() {
			var contactVal = $('#im_add').find('input[imtype="im_add_contact"]').val();
			if (!IM.DO_checkContact(contactVal)) { //校验联系人格式
				return;
			}
			var im_contact = $('#im_contact_list').find('li[contact_type="' + IM._contact_type_c + '"][contact_you="' + contactVal + '"]');
			if (im_contact.length <= 0) {
				IM.HTML_clean_im_contact_list(); //清除原来选中的li
				IM.HTML_addContactToList(contactVal, contactVal, IM._contact_type_c, true, true, false, null, null, null);
			}
			$('#im_add').find('input[imtype="im_add_contact"]').val('');

		},

		/**
		 * 检查联系名称规则是否合法
		 * 
		 * @param contactVal
		 * @returns {boolean}
		 * @constructor
		 */
		DO_checkContact: function(contactVal) { //zyhcontact
			if (!contactVal) {
				IM.HTML_showAlert('alert-warning', '请填写联系人');
				return false;
			} else if (contactVal.length > 64) {
				IM.HTML_showAlert('alert-error', '联系人长度不能超过64');
				return false;
			}
			if ('g' == contactVal.substr(0, 1)) {
				IM.HTML_showAlert('alert-error', '联系人不能以"g"开始');
				return false;
			}
			if (contactVal.indexOf("@") > -1) {
				var regx2 = /^([a-zA-Z0-9]{32}#)?[a-zA-Z0-9_-]{1,}@(([a-zA-z0-9]-*){1,}.){1,3}[a-zA-z-]{1,}$/;
				if (regx2.exec(contactVal) == null) {
					IM.HTML_showAlert('alert-error',
						'检查邮箱格式、如果是跨应用再检查应用Id长度是否为32且由数字或字母组成）');
					return false;
				}
			} else {
				var regx1 = /^[A-Za-z0-9._-]+$/; // /^[a-zA-Z\u4e00-\u9fa5]+$/满足大小写字母数字和ascii码值;
				if (regx1.exec(contactVal) == null) {
					IM.HTML_showAlert('alert-error',
						'只能是数字字母点下划线');
					return false;
				}
			}
			return true;
		},
		
		/**
		 * 根据uuid获取用户名称
		 */
		getCustomerName: function(customerUuid) {
			var jsContextPath= IM.getContextPath();
			var custName = null;
			$.ajaxSettings.async = false; 
			$.getJSON(ContextPath+"/storeback/customer/getCustomerInfoByUuid/"+customerUuid, {ranNum:Math.random()}, function(data) {
				if (data){
				   var customer = data.customerInfo;
				   var nickname = customer.nickName;
				   var customerName=customer.customerName+'';
				   if(nickname == "" || nickname == null){
			   			custName =  customerName;
				   }else{
				   		custName =  nickname;
				   }
				}else{
					console.log("error");
				}
			});
			return custName;
		},

		/**
		 * 根据uuid获取用户头像
		 */
		getCustomerHeadPic: function(customerUuid) {
			var jsContextPath= IM.getContextPath();
			var custImageUrl = null;
			$.ajaxSettings.async = false; 
			$.getJSON(ContextPath+"/imServiceComp/getCustomerInfo", {customerUuid:customerUuid,ranNum:Math.random()}, function(data) {
				if (data){
				   var imageUrl = data.profilePic;
				   if(imageUrl == "" || imageUrl == null){
			   			custImageUrl =  "../../../../static/storeback/service/img/avtar.gif";
				   }else{
				   		custImageUrl =  imageUrl;
				   }
				}else{
					console.log("error:unable to get customer head picture");
				}
			});
			return custImageUrl;
		},

		/**
		 * 根据uuid获取店铺logo
		 */
		getStoreLogo: function(storeUuid) {
			var jsContextPath= IM.getContextPath();
			var storeLogoUrl = null;
			$.ajaxSettings.async = false; 
			$.getJSON(ContextPath+"/imServiceComp/getStoreInfo", {storeUuid:storeUuid,ranNum:Math.random()}, function(data) {
				if (data){
				   var imageUrl = data.logoPath;
				   if(imageUrl == "" || imageUrl == null){
			   			storeLogoUrl =  "../../../../static/storeback/service/img/avtar.gif";
				   }else{
				   		storeLogoUrl =  imageUrl;
				   }
				}else{
					console.log("error:unable to get store logo ");
				}
			});
			return storeLogoUrl;
		},
		
		/**
		 * 根据uuid获取用户名称
		 */
		getStoreName: function(storeUuid) {
			var jsContextPath= IM.getContextPath();
			var storeName = null;
			$.ajaxSettings.async = false; 
			$.getJSON(ContextPath+"/imServiceComp/getStoreInfo", {storeUuid:storeUuid,ranNum:Math.random()}, function(data) {
				if (data){
					storeName = data.storeName;
				}else{
					console.log("error");
				}
			});
			return storeName;
		},

		getContextPath:function() {
		    var pathName = document.location.pathname;
		    var index = pathName.substr(1).indexOf("/");
		    var result = pathName.substr(0,index+1);
		    if(result == "/sysback"){
		    	return "";
		    }
		    return "";
		},

		/**
		 * 样式，添加左侧联系人列表项
		 * 
		 * @param contactVal
		 * @param contact_type
		 * @param b
		 *            true--class:active false--class:null
		 * @param bb
		 *            true--display:block false--display:none
		 * @param bbb
		 *            true--需要改名字 false--不需要改名字
		 * @param owner --
		 *            当前群组创建者（只有content_type==G时才有值）
		 * @param isNotice --
		 *            是否提醒 1：提醒；2：不提醒(只有content_type==G时才有值)
		 * @param target --
		 *            1表示讨论组 2表示群组
		 * @constructor
		 */
		HTML_addContactToList: function(contactVal, contactName, content_type,
			b, bb, bbb, owner, isNotice, target) {
			var old = $(document.getElementById('im_contact_' + contactVal));
			var cName = null;
			if (IM.isNull(contactName)) {
				cName = '';
			}
			if(contactName == 'KF10089'){
				cName = '';
			}else{
				//cName = IM.getCustomerName(contactName);
				cName = IM.getStoreName(contactName);
			}
			// 已存在，置顶，并更改数字
			if (!!old && old.length > 0) {
				// 如果名字不同，修改名字
				if (bbb) {
					old.find('span[contact_style_type="name"]').html(cName);
				}
				var str = old.prop('outerHTML');
				old.remove();
				$('#im_contact_list').prepend(str);
				return;
			}

			// 不存在创建个新的
			if (IM._contact_type_m == content_type) {
				var onUnitAccount = $(document.getElementById('im_contact_' + IM._onUnitAccount));
				if (IM._onUnitAccount == onUnitAccount.attr('contact_you')) {
					return;
				}
			}
			var active = '';
			if (b)
				active = 'active';
			var dis = 'none';
			if (bb)
				dis = 'block';

			var str = '<li onclick="IM.DO_chooseContactList(\'' + content_type + '\', \'' + contactVal + '\',' + 'true)" id="im_contact_' + contactVal + '" im_isnotice="' + isNotice + '" contact_type="' + content_type + '" contact_you="' + contactVal + '" class="' + active + '"  style="display:' + dis + '">' + '<a href="javascript:void(0);">' + '<span contact_style_type="name">' + cName + '</span>';
			if (IM._contact_type_g == content_type) {
				if (cName != "系统通知") {
					str += '<span class="pull-right" ><i class="icon-wrench"></i></span>';
				}
			}

			str += '<span contact_style_type="warn" class="badge  badge-warning pull-right" style="margin-top:3px; display:none;background-color:#fb6464;padding:3px 5px;border-radius:100%">0</span>' + '<span contact_style_type="onlineState" class="badge m_badge pull-right" style="margin-top:3px; margin-right:3px; display:none; "></span>' + '</a>' + '</li>';
			$('#im_contact_list').prepend(str);

			var ulWidth = $('#im_contact_list').width();
			var conListWidth = document.getElementById("im_contact").scrollWidth;
			if (conListWidth - ulWidth > 30) {
				$('#im_contact_list').find("li")[0].style.width = conListWidth + 45 + "px"; //防止用户名过长时用户状态或消息条数换行
			}
			if (b)
				IM.DO_chooseContactList(content_type, contactVal , true);
		},

		/**
		 * 图片pop层隐藏
		 * 
		 * @constructor
		 */
		HTML_pop_photo_hide: function() {
			IM._msgId = null;
			$('#pop_photo').hide();
			if ($('#pop_photo').find("video").length > 0) {
				if (!document.getElementById("pop_photo").querySelector('video').paused) {
					document.getElementById("pop_photo").querySelector('video').pause();
				}
			};
			IM.HTML_LJ_none();
		},

		/**
		 * 隐藏提示框
		 * 
		 * @param id
		 */
		HTML_closeAlert: function(id) {
			if ('all' == id) {
				IM.HTML_closeAlert('alert-error');
				IM.HTML_closeAlert('alert-info');
				IM.HTML_closeAlert('alert-warning');
				IM.HTML_closeAlert('alert-success');
			} else {
				$('#hero-unit').css('padding-top', '60px');
				$(document.getElementById(id)).parent().css('top', '0px');
				$(document.getElementById(id)).hide();
				$(document.getElementById(id)).parent().hide();
			}
		},

		/**
		 * 显示提示框
		 * 
		 * @param id
		 * @param str
		 */
		HTML_showAlert: function(id, str, time) {
			var t = 3 * 1000;
			if (!!time) {
				t = time;
			}
			clearTimeout(IM._timeoutkey);
			$('#alert-info').hide();
			$('#alert-warning').hide();
			$('#alert-error').hide();
			$('#alert-success').hide();
			$(document.getElementById(id + '_content')).html(str);
			$('#hero-unit').css('padding-top', '0px');
			$(document.getElementById(id)).parent().css('top', '-5px');
			$(document.getElementById(id)).show();
			$(document.getElementById(id)).parent().show();
			IM._timeoutkey = setTimeout(function() {
				IM.HTML_closeAlert(id);
			}, t);
		},

		/**
		 * 样式，因高度变化而重置页面布局
		 * 
		 * @constructor
		 */
		HTML_resetHei: function() {
			var windowHei = $(window).height();
			if (windowHei < 666) {
				windowHei = 666;
			}
			var windowWid = $(window).width();

			var width = Math.round(windowWid * 0.3);
			$("#videoView").css("width", width);
			var leftWidth = (windowWid - width) / 2;
			$("#videoView").css("left", leftWidth);
			if (windowWid < 666) {
				//移动端兼容发送菜单栏
				$("#contentEditDiv").css("top", "0px");
				$("#sendMenu").css("top", "0px");
			} else {
				$("#contentEditDiv").css("top", "-10px");
			}
			var navbarHei = $('#navbar').height() + 20 + 60 + 30 + 20 + 1;
			var contactTypeHei = $('#im_contact_type').height() + 20 + 6;
			var addContactHei = $('#im_add_contact').height() + 10 + 10;

			var hei = windowHei - navbarHei - contactTypeHei - addContactHei - 20;
			$(".scrollspy-contact-example").height(hei);
			$(".scrollspy-content-example").height(hei + contactTypeHei - 10 - 10 - 75);
			$('#im_content_list').scrollTop($('#im_content_list')[0].scrollHeight);
			// 绘制滤镜
			if ('block' == $('#pop_photo').css('display')) {
				IM.HTML_pop_photo_show();
			} else if ('block' == $('#pop').css('display')) {
				IM.HTML_pop_show();
			} else if ('block' == $('#lvjing').find('img').css('display')) {
				IM.HTML_LJ('black');
			} else if ('block' == $('#pop_takePicture').css('display')) {} else {
				IM.HTML_LJ('black');
			}
		},

		/**
		 * canvas绘制滤镜层（HTML5）
		 * 
		 * @param style
		 *            white, black
		 * @constructor
		 */
		HTML_LJ: function(style) {
			var lvjing = $('#lvjing');

			var windowWid = $(window).width();
			if (windowWid < 666) {
				$('#hero-unit').css('padding-left', 20);
				$('#hero-unit').css('padding-right', 20);
			} else {
				//$('#hero-unit').css('padding-left', 60);
				//$('#hero-unit').css('padding-right', 60);
			}

			var navbarHei = $('#navbar').height();
			var concentHei = ($('#hero-unit').height() + 20 + 60 + 30);
			var concentwid = ($('#hero-unit').width() + parseInt($('#hero-unit').css('padding-left')) + parseInt($('#hero-unit')
				.css('padding-right')));

			var lvjingImgHei = lvjing.find('img').height();
			if (0 == lvjingImgHei)
				lvjingImgHei = 198;

			lvjing.css('top', navbarHei);
			lvjing.css('left', 0);
			lvjing.css('width', '100%');
			lvjing.height(concentHei + 15);

			var canvas = document.getElementById("lvjing_canvas");
			canvas.clear;
			canvas.height = (concentHei + 15);
			canvas.width = concentwid;
			if (!canvas.getContext) {
				console.log("Canvas not supported. Please install a HTML5 compatible browser.");
				return;
			}

			var context = canvas.getContext("2d");
			context.clear;
			context.beginPath();
			context.moveTo(0, 0);
			context.lineTo(concentwid, 0);
			context.lineTo(concentwid, concentHei + 15);
			context.lineTo(0, concentHei + 15);
			context.closePath();
			context.globalAlpha = 0.4;
			if ('white' == style) {
				context.fillStyle = "rgb(200,200,200)";
				lvjing.find('img').hide();
			} else if ('photo' == style) {
				context.fillStyle = "rgb(20,20,20)";
				lvjing.find('img').hide();
			} else if ('black' == style) {
				context.fillStyle = "rgb(0,0,0)";
				var qr = lvjing.find('img');
				qr.css('top', concentHei / 2 - lvjingImgHei / 2);
				qr.css('left', concentwid / 2 - lvjingImgHei / 2);
				qr.show();
			}
			context.fill();
			context.stroke();

			var cha = navbarHei + 4;
			if (navbarHei > 45)
				cha = 0;
			$('#im_body').height(navbarHei + concentHei - 25);
			$('body').height(navbarHei + concentHei - 25);

			setTimeout(function() {
				$('#ClCache').parent().remove();
			}, 20);

		},

		/**
		 * 样式，滤镜隐藏
		 * 
		 * @constructor
		 */
		HTML_LJ_none: function() {
			$('#lvjing').hide();
		},

		/**
		 * 样式，滤镜显示
		 * 
		 * @constructor
		 */
		HTML_LJ_block: function(style) {
			IM.HTML_LJ(style);
			$('#lvjing').show();
		},

		/**
		 * 聊天模式选择
		 * 
		 * @param contact_type --
		 *            'C':代表联系人; 'G':代表群组; 'M':代表多渠道客服
		 * @constructor
		 */
		DO_choose_contact_type: function(contact_type) {
			$('#im_contact_type').find('li').each(function() {
				$(this).removeClass('active');
				if (contact_type == $(this).attr('contact_type')) {
					$(this).addClass('active');
				}
			});

			// 选择列表下内容
			$('#im_contact_list').find('li').each(function() {
				if (contact_type == $(this).attr('contact_type')) {
					$(this).show();
				} else {
					$(this).hide();
				}
			});

			// 切换样式
			var current_contact_type = IM.HTML_find_contact_type();
			var im_add = $('#im_add');
			if (IM._contact_type_c == current_contact_type) { // 点对点
				im_add.find('i').attr('class', '').addClass('icon-user');
				im_add.find('input[imtype="im_add_contact"]').show();
				im_add.find('input[imtype="im_add_group"]').hide();
				im_add.find('input[imtype="im_add_mcm"]').hide();
				im_add.find('button[imtype="im_add_btn_contact"]').show();
				im_add.find('div[imtype="im_add_btn_group"]').hide();
			}
		},
		
		_mouseoverStyle: function(obj) {
			$(obj).css("background-color", "#E9E9E4");
		},
		_mouseoutStyle: function(obj) {
			$(obj).css("background-color", "");
		},
		_selectGroupMem: function(obj, startIndex) {
			var member = $(obj).attr("id");
			var nickName = $(obj).text();
			IM._extopts.push(member);
			if (startIndex == '') {
				$("#im_send_content").append(nickName);
			} else {
				var currentTab = document.getElementById("im_send_content");
				IM.insertText(currentTab, nickName, startIndex);
			}
		},
		insertText: function(obj, nickName, startIndex) {
			var startPos = parseInt(startIndex) + 1;
			var endPos = startPos;
			var cursorPos = startPos;
			var tmpStr = obj.childNodes[0].data;
			obj.childNodes[0].data = tmpStr.substring(0, startPos) + nickName + tmpStr.substring(endPos, tmpStr.length);
			cursorPos += nickName.length;
		},

		/**
		 * 样式，发送消息
		 */
		DO_sendMsg: function() {
			var str = IM.DO_pre_replace_content_to_db();
			var urlReg = /((https?:\/\/)?[\w-]+(\.[\w-]+)+\.?(:\d+)?(\/\S*)?)/i;
			var urlStrArray = str.match(urlReg);
			if(null !=urlStrArray && urlStrArray.length > 0){
				var urlStr = urlStrArray[0];
				var urlStrWithTags = "<a href='"+urlStr+"' target=/'_blank/'>"+urlStr+"</a>";
				str = str.replace(urlStr,urlStrWithTags);
			}
			$('#im_send_content_copy').html(str);
			$('#im_send_content_copy').find('img[imtype="content_emoji"]').each(function() {
				var emoji_value_unicode = $(this).attr('emoji_value_unicode');
				$(this).replaceWith(emoji_value_unicode);
			});
			var im_send_content = $('#im_send_content_copy').html();
			// 清空pre中的内容
			$('#im_send_content_copy').html('');
			// 隐藏表情框
			$('#emoji_div').hide();
			var msgid = new Date().getTime();
			var content_type = '';
			var content_you = '';
			var b = false;
			$('#im_contact_list').find('li').each(function() {
				if ($(this).hasClass('active')) {
					content_type = $(this).attr('contact_type');
					content_you = $(this).attr('contact_you');
					b = true;
				}
			});
			if (!b) {
				// alert("暂无新消息！");
				return;
			};

			if (IM._serverNo == content_you) {
				alert("系统消息禁止回复");
				return;
			}

			if (im_send_content == undefined || im_send_content == null || im_send_content == '')
				return;
			// im_send_content = im_send_content.replace(/&lt;/g, '<').replace(
			// 		/&gt;/g, '>').replace(/&quot;/g, '"')
			// 	.replace(/&amp;/g, '&').replace(/&nbsp;/g, ' ');
			console.log('msgid[' + msgid + '] content_type[' + content_type + '] content_you[' + content_you + '] im_send_content[' + im_send_content + ']');
			var str = '<pre msgtype="content" style="white-space: pre-wrap; word-wrap:break-word">' + im_send_content + '</pre>';
			IM.HTML_sendMsg_addHTML('temp_msg', 1, msgid, content_type, content_you, str);
			// 发送消息至服务器
			IM.EV_sendTextMsg(msgid, im_send_content, content_you, false);
		},

		DO_im_image_file: function() {
			var msgid = new Date().getTime();
			var content_type = '';
			var content_you = '';
			var b = false;
			$('#im_contact_list').find('li').each(function() {
				if ($(this).hasClass('active')) {
					content_type = $(this).attr('contact_type');
					content_you = $(this).attr('contact_you');
					b = true;
				}
			});
			if (!b) {
//				alert("暂无新消息！");
				return;
			}
			if (IM._serverNo == content_you) {
				alert("系统消息禁止回复");
				return;
			}

			var windowWid = $(window).width();
			var imgWid = 0;
			var imgHei = 0;
			if (windowWid < 666) {
				imgWid = 100;
				imgHei = 150;
			} else {
				imgWid = 150;
				imgHei = 200;
			}
			var str = '<div class="progress progress-striped active">' + '<div class="bar" style="width: 20%;"></div>' + '</div>' + '<pre imtype="msg_attach" style="white-space: pre-wrap; word-wrap:break-word">' + '<img imtype="msg_attach_src" src="#" style="max-width:' + imgWid + 'px; max-height:' + imgHei + 'px;" onclick="IM.DO_pop_phone(\'' + content_you + '\', \'' + '' + '\',this)" />' + '<input imtype="msg_attach_resend" type="file" accept="image/*" style="display:none;margin: 0 auto;" onchange="IM.DO_im_image_file_up(\'' + content_you + '_' + msgid + '\', \'' + msgid + '\',null)">' + '</pre>';
			// 添加右侧消息
			var id = IM.HTML_sendMsg_addHTML('temp_msg', 4, msgid, content_type, content_you, str);
			$(document.getElementById(id)).find('input[imtype="msg_attach_resend"]').click();

		},

		/**
		 * 发送图片，页面选择完图片后出发
		 * 
		 * @param id --
		 *            dom元素消息体的id
		 * @param msgid
		 * @constructor
		 */
		DO_im_image_file_up: function(id, oldMsgid, img_blob) {
			var msg = $(document.getElementById(id));
			var oFile = msg.find('input[imtype="msg_attach_resend"]')[0];

			if (!!oFile) {
				oFile = oFile.files[0];
				console.log(oFile.name + ':' + oFile.type);
			} else {
				oFile = img_blob;
			}
			//如果是附件则本地显示
			window.URL = window.URL || window.webkitURL || window.mozURL || window.msURL;
			var url = window.URL.createObjectURL(oFile);
			msg.find('img[imtype="msg_attach_src"]').attr('src', url);

			var receiver = msg.attr('content_you');
			// 查找当前选中的contact_type值 1、IM上传 2、MCM上传
			var current_contact_type = IM.HTML_find_contact_type();
			if (IM._contact_type_m == current_contact_type) {
				IM.EV_sendToDeskAttachMsg(oldMsgid, oFile, 4, receiver);
			} else {
				IM.EV_sendAttachMsg(oldMsgid, oFile, 4, receiver);
			}
		},

		/**
		 * 发送本地附件
		 */
		DO_im_attachment_file: function() {
			var msgid = new Date().getTime();
			var content_type = '';
			var content_you = '';
			var b = false;
			$('#im_contact_list').find('li').each(function() {
				if ($(this).hasClass('active')) {
					content_type = $(this).attr('contact_type');
					content_you = $(this).attr('contact_you');
					b = true;
				}
			});
			if (!b) {
//				alert("暂无新消息！");
				$('#im_attachment_file').val('');
				return;
			};
			if (IM._serverNo == content_you) {
				alert("系统消息禁止回复");
				return;
			}

			var str = '<div class="progress progress-striped active">' + '<div class="bar" style="width: 40%;"></div>' + '</div>' + '<pre imtype="msg_attach" style="white-space: pre-wrap; word-wrap:break-word">' + '<a imtype="msg_attach_href" href="javascript:void(0);" target="_blank">' + '<span>' + '<img style="width:32px; height:32px; margin-right:5px; margin-left:5px;" src="/static/storeback/service/assets/img/attachment_icon.png" />' + '</span>' + '<span imtype="msg_attach_name"></span>' + '</a>' + '<span style="font-size: small;margin-left:15px;"></span>' + '<input imtype="msg_attach_resend" type="file" accept="" style="display:none;margin: 0 auto;" onchange="IM.DO_im_attachment_file_up(\'' + content_you + '_' + msgid + '\', \'' + msgid + '\')">' + '</pre>';
			// 添加右侧消息
			var id = IM.HTML_sendMsg_addHTML('temp_msg', 6, msgid,
				content_type, content_you, str);
			$(document.getElementById(id)).find('input[imtype="msg_attach_resend"]').click();

		},

		/**
		 * 打开本地文件时触发本方法
		 * 
		 * @param id --
		 *            dom元素消息体的id
		 * @param msgid
		 * @constructor
		 */
		DO_im_attachment_file_up: function(id, oldMsgid) {
			var msg = $(document.getElementById(id));
			var oFile = msg.find('input[imtype="msg_attach_resend"]')[0].files[0];
			var msgType = 0;
			console.log(oFile.name + ':' + oFile.type);
			window.URL = window.URL || window.webkitURL || window.mozURL || window.msURL;
			var url = window.URL.createObjectURL(oFile);
			var num = oFile.size;
			var size = 0;
			if (num < 1024) {
				size = num + "byte";
			} else if (num / 1024 >= 1 && num / Math.pow(1024, 2) < 1) {
				size = Number(num / 1024).toFixed(2) + "KB";
			} else if (num / Math.pow(1024, 2) >= 1 && num / Math.pow(1024, 3) < 1) {
				size = Number(num / Math.pow(1024, 2)).toFixed(2) + "MB";
			} else if (num / Math.pow(1024, 3) >= 1 && num / Math.pow(1024, 4) < 1) {
				size = Number(num / Math.pow(1024, 3)).toFixed(2) + "G";
			};
			var receiver = msg.attr('content_you');
			//判断如果该浏览器支持拍照，那么在这里做个附件图片和文件的区别化展示；
			if ($("#camera_button").find("i").hasClass("icon-picture")) {
				msg.find('a[imtype="msg_attach_href"]').attr('href', url);
				msg.find('span[imtype="msg_attach_name"]').html(oFile.name);
				msg.find('a[imtype="msg_attach_href"]').next().html(size);
				msgType = 7;
			} else {
				if ("image" == oFile.type.substring(0, oFile.type.indexOf("/"))) {
					var windowWid = $(window).width();
					var imgWid = 0;
					var imgHei = 0;
					if (windowWid < 666) {
						imgWid = 100;
						imgHei = 150;
					} else {
						imgWid = 150;
						imgHei = 200;
					}
					var str = '<img imtype="msg_attach_src" src="' + url + '" style="max-width:' + imgWid + 'px; max-height:' + imgHei + 'px;" ' + ' onclick="IM.DO_pop_phone(\'' + receiver + '\', \'' + '' + '\',this)" />';
					msg.find('a[imtype="msg_attach_href"]').replaceWith(str);

					msgType = 4;
				} else {
					msg.find('a[imtype="msg_attach_href"]').attr('href', url);
					msg.find('span[imtype="msg_attach_name"]').html(oFile.name);
					msg.find('a[imtype="msg_attach_href"]').next().html(size);
					msgType = 7;
				}
			}
			// 查找当前选中的contact_type值 1、IM上传 2、MCM上传
			var current_contact_type = IM.HTML_find_contact_type();
			if (IM._contact_type_m == current_contact_type) {
				IM.EV_sendToDeskAttachMsg(oldMsgid, oFile, msgType, receiver);
			} else {
				IM.EV_sendAttachMsg(oldMsgid, oFile, msgType, receiver);
			}

		},

		/**
		 * 选择表情
		 * 
		 * @param unified
		 * @param unicode
		 * @constructor
		 */
		DO_chooseEmoji: function(unified, unicode) {

			var content_emoji = '<img imtype="content_emoji" emoji_value_unicode="' + unicode + '" style="width:18px; height:18px; margin:0 1px 0 1px;" src="'+ContextPath+'/static/storeback/service/img/img-apple-64/' + unified + '.png"/>';

			if ($('#im_send_content').children().length <= 1) {

				$('#im_send_content').find('p').detach();
				$('#im_send_content').find('br').detach();
				$('#im_send_content').find('div').detach();
			}

			var brlen = $('#im_send_content').find('br').length - 1;
			$('#im_send_content').find('br').each(function(i) {
				if (i == brlen) {
					$(this).replaceWith('');
				}
			});
			var plen = $('#im_send_content').find('p').length - 1;
			$('#im_send_content').find('p').each(function(i) {
				if (i < plen) {
					$(this).replaceWith($(this).html() + '<br>');
				} else {
					$(this).replaceWith($(this).html());
				}
			});

			$('#im_send_content').find('div').each(function(i) {
				if ('<br>' == $(this).html()) {
					$(this).replaceWith('<br>');
				} else {
					$(this).replaceWith('<br>' + $(this).html());
				}
			});

			var im_send_content = $('#im_send_content').html();

			if ('<br>' == im_send_content) {
				im_send_content = '';
			} else {
				im_send_content = im_send_content.replace(/(<(br)[/]?>)+/g,
					'\u000A');
			}

			$('#im_send_content').html(im_send_content + content_emoji);
		},

		DO_pre_replace_content: function() {
			console.log('pre replace content...');
			setTimeout(function() {
				var str = IM.DO_pre_replace_content_to_db();
				$('#im_send_content').html(str);
			}, 20);
		},

		DO_pre_replace_content_to_db: function() {
			var str = $('#im_send_content').html();
			str = str.replace(/<(div|br|p)[/]?>/g, '\u000A');
			str = str.replace(/\u000A+/g, '\u000D');
			str = str.replace(/<[^img][^>]+>/g, ''); // 去掉所有的html标记
			str = str.replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(
				/&quot;/g, '"').replace(/&amp;/g, '&').replace(/&nbsp;/g,
				' ');
			if ('\u000D' == str) {
				str = '';
			}
			return str;
		},

		/**
		 * 隐藏或显示表情框
		 * 
		 * @constructor
		 */
		HTML_showOrHideEmojiDiv: function() {
			if ('none' == $('#emoji_div').css('display')) {
				$('#emoji_div').show();
			} else {
				$('#emoji_div').hide();
			}
		},

		/**
		 * 获取当前时间戳 YYYYMMddHHmmss
		 * 
		 * @returns {*}
		 */
		_getTimeStamp: function() {
			var now = new Date();
			var timestamp = now.getFullYear() + '' + ((now.getMonth() + 1) >= 10 ? "" + (now.getMonth() + 1) : "0" + (now.getMonth() + 1)) + (now.getDate() >= 10 ? now.getDate() : "0" + now.getDate()) + (now.getHours() >= 10 ? now.getHours() : "0" + now.getHours()) + (now.getMinutes() >= 10 ? now.getMinutes() : "0" + now.getMinutes()) + (now.getSeconds() >= 10 ? now.getSeconds() : "0" + now.getSeconds());
			return timestamp;
		},

		/**
		 * 修改用户信息
		 */
		DO_userMenu: function() {
			// 构建用户信息页面
			IM.DO_userpop_show();
			// 调用SDK方法获取user信息
			IM.EV_getMyMenu();
		},

		/**
		 * 构建用户信息页面
		 */
		DO_userpop_show: function() {
			var str = '<div class="modal" id="pop_MyInfo" style="position: relative; top: auto; left: auto; right: auto; margin: 0 auto 20px; z-index: 1; max-width: 100%;">' + '<div class="modal-header" >' + '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>' + '<h3>个人信息 </h3>' + '</div>' + '<div class="modal-body">' + '<table class="table table-bordered">' + '<tr>' + '<td>' + '<div class="pull-left" style="width: 25%;">昵称：</div>' + '<div class="pull-right" style="width: 75%;" imtype="im_pop_MyInfo_nick">' + '<input id="nickName" class="pull-right" type="text" style="width:95%;" value="" />' + '</div>' + '</td>' + '</tr>' + '<tr>' + '<td>' + '<div class="pull-left" style="width: 25%;">性别：</div>' + '<div class="pull-right" style="width: 75%;" imtype="im_pop_MyInfo_sex">' + '<input name="sex" type="radio" value="1" />男' + '<input name="sex" style="margin-left:20%;" type="radio" value="2" />女' + '</div>' + '</td>' + '</tr>' + '<tr>' + '<td>' + '<div class="pull-left" style="width: 25%;">出生日期：</div>' + '<div class="pull-right" style="width: 75%;" >' + '<input id="birth" size="16" type="text" value="" class="form_date" readonly="readonly">' + '</div></td>' + '</tr>' + '<tr>' + '<td>' + '<span class="pull-left" style="width: 25%;">个性签名：</span>' + '<span class="pull-right" style="width: 75%;" imtype="im_pop_MyInfo_sign">' + '<textarea id="sign" class="pull-left" style="width:95%;"></textarea>' + '</span>' + '</td>' + '</tr>' + '</table>' + '<div class="modal-footer">' + '<a href="javascript:void(0);" class="btn btn-primary" onclick="IM.EV_updateMyInfo()"> 保存修改 </a>' + '<a href="javascript:void(0);" class="btn" >取消</a>' + '</div></div>';
			$('#pop').find('div[class="row"]').html(str);
			// 时间控件
			$('.form_date').datetimepicker({
				language: 'zh-CN',
				pickTime: true,
				todayBtn: true,
				autoclose: true,
				minView: '2',
				forceParse: false,
				format: "yyyy-mm-dd"
			});
			IM.HTML_pop_show();
		},

		/**
		 * 获取当前用户个人信息
		 */
		EV_getMyMenu: function() {
			RL_YTX.getMyInfo(function(obj) {
				$("#nickName").val(obj.nickName);
				$("[name=sex]").each(function() {
					if ($(this).val() == obj.sex) {
						$(this).prop("checked", true);
					}
				});
				$("#birth").val(obj.birth);

				if (!!obj.sign) {
					$("#sign").text(obj.sign);
				}

			}, function(obj) {
				if (520015 != obj.code) {
					alert("错误码：" + obj.code + "; 错误描述：" + obj.msg)
				}
			});
		},

		/**
		 * 整合用户信息传给服务器
		 */
		EV_updateMyInfo: function() {

			var nickName = $("#nickName").val();
			if (nickName != IM._user_account) {
				if (nickName == "") {
					alert("昵称不能为空");
					return false;
				}
			}
			var sex = '';
			$("[name=sex]").each(function() {
				if (!!$(this).prop("checked")) {
					sex = $(this).val();
				}
			});
			var birth = $("#birth").val();
			var sign = $("#sign").val();
			if (sign.length > 100) {
				alert("签名长度不能超过100");
				return;
			}
			var uploadPersonInfoBuilder = new RL_YTX.UploadPersonInfoBuilder(
				nickName, sex, birth, sign);

			RL_YTX.uploadPerfonInfo(uploadPersonInfoBuilder, function(obj) {
				// IM.HTML_pop_hide();
				$('#navbar_login_show').find('span')[1].innerHTML = nickName;
				$('#navbar_login_show').html('<span style="float: left;display: block;font-size: 20px;font-weight: 200;padding-top: 10px;padding-bottom: 10px;text-shadow: 0px 0px 0px;color:#eee" >您好:</span>' + '<a onclick="IM.DO_userMenu()" style="text-decoration: none;cursor:pointer;float: left;font-size: 20px;font-weight: 200;max-width:130px;' + 'padding-top: 10px;padding-right: 20px;padding-bottom: 10px;padding-left: 20px;text-shadow: 0px 0px 0px;color:#eee;word-break:keep-all;text-overflow:ellipsis;overflow: hidden;" >' + nickName + '</a>' + '<span onclick="IM.EV_logout()" style="cursor:pointer;float: right;font-size: 20px;font-weight: 200;' + 'padding-top: 10px;padding-bottom: 10px;text-shadow: 0px 0px 0px;color:#eeeeee">退出</span>');
				IM._username = nickName;
			}, function(obj) {
				alert("错误码：" + obj.code + "; 错误描述：" + obj.msg)
			});
		},

		DO_getHistoryMessage: function() {
			var content_list = $('#im_content_list');
			var scrollTop = content_list.scrollTop();
			if (scrollTop == 0) {
				// 获取参数
				var firstMsg = null;
				for (var i = 0; i < content_list.children().length; i++) {
					var child = content_list.children()[i];
					if (child.nodeName == "DIV" && child.id != "getHistoryMsgDiv") { // 判断标签是不是div
						if (child.style.display != "none") {
							firstMsg = child;
							break;
						}
					}
				}
				IM.EV_getHistoryMessage(firstMsg);
			}
		},

		/**
		 * 获取历史消息GetHistoryMessageBuilder
		 * 
		 * @param talker
		 *            消息交互者或群组id
		 * @param pageSize
		 *            获取消息数目 默认为10 最大为50
		 * @param version
		 *            接收消息的消息版本号 分页条件
		 * @param msgId
		 *            发送消息的msgId 分页条件
		 * @param order
		 *            排序方式 1升序 2降序 默认为1
		 * @param callback --
		 *            function(obj){ var msg = obj[i]; //obj 为数组 msg.version;
		 *            //消息版本号 msg.msgType; //消息类型 msg.msgContent; //文本消息内容
		 *            msg.msgSender; //发送者 msg.msgReceiver; //接收者 msg.msgDomain;
		 *            //扩展字段 msg.msgFileName; //消息文件名 msg.msgFileUrl; //消息下载地址
		 *            msg.msgDateCreated; //服务器接收时间 msg.mcmEvent; //是否为mcm消息
		 *            0普通im消息 1 start消息 2 end消息 53发送mcm消息 }
		 * @param onError --
		 *            function(obj){ obj.code; //错误码; }
		 * @constructor
		 */
		EV_getHistoryMessage: function(firstMsg) {
			var getHistoryMessageBuilder = null;
			var pageSize = 20;
			var order = 2;
			var talker = null;
			if (!!firstMsg) {
				talker = $(firstMsg).attr("content_you"); // 接受者
				console.log("talker" + talker + "," + IM._user_account);
				var msgId = $(firstMsg).attr("id").substring(talker.length + 1); // 当前条为发送消息则提供参数msgId
				console.log(msgId);
				var sender = $(firstMsg).attr("contactor");
				if (sender != "sender") {
					getHistoryMessageBuilder = new RL_YTX.GetHistoryMessageBuilder(
						talker, pageSize, 1, msgId, order);
				} else {
					getHistoryMessageBuilder = new RL_YTX.GetHistoryMessageBuilder(
						talker, pageSize, 2, msgId, order);
				}
				console.log("talker=" + talker + ";pageSize=" + pageSize + ";msgId=" + msgId + ";(1升序2降序)order=" + order);
			} else {
				$('#im_contact_list').find('li').each(function() {
					if ($(this).hasClass('active')) {
						talker = $(this).attr('contact_you');
					}
				});
				getHistoryMessageBuilder = new RL_YTX.GetHistoryMessageBuilder(
					talker, pageSize, "", "", order);
				console.log("talker=" + talker + ";pageSize=" + pageSize + ";msgId=" + msgId + ";(1升序2降序)order=" + order);
			}

			// 调用接口
			RL_YTX.getHistoryMessage(getHistoryMessageBuilder,
				function(obj) {
					var windowWid = $(window).width();
					var imgWid = 0;
					var imgHei = 0;
					if (windowWid < 666) {
						imgWid = 100;
						imgHei = 150;
					} else {
						imgWid = 150;
						imgHei = 200;
					};

					for (var i = 0; i < obj.length; i++) {
						var msg = obj[i];
						var content_you = '';
						var version = msg.version;
						if (msg.msgSender == IM._user_account) {
							content_you = msg.msgReceiver;
						} else {
							content_you = msg.msgSender;
						};
						var str = '';
						if (msg.msgType == 1) { //1:文本消息 2：语音消息 3：视频消息 4：图片消息 5：位置消息 6：文件   msg.msgFileName; //消息文件名
							str = '<pre msgtype="content" style="white-space: pre-wrap; word-wrap:break-word">' + msg.msgContent + '</pre>';
						};
						if (msg.msgType == 2) { //zzx
							str = '<pre>您有一条语音消息,请用其他设备接收</pre>';
						};
						if (msg.msgType == 3) {
							str = '<img onclick="IM.DO_pop_phone(\'' + content_you + '\', \'' + version + '\')" ' +
								'videourl="' + msg.msgFileUrl + '" src="' + msg.msgFileUrlThum + '" ' +
								'style="max-width:' + imgWid + 'px;max-height:' + imgHei + 'px;" />';
						};
						if (msg.msgType == 4) {
							str = '<pre imtype="msg_attach" style="white-space: pre-wrap; word-wrap:break-word">' + '<img imtype="msg_attach_src" src="' + msg.msgFileUrl + '" style="max-width:' + imgWid + 'px;max-height:' + imgHei + 'px;" />' + '</pre>';
						};
						if (msg.msgType == 5) { //zzx
							var jsonObj = eval('(' + obj.msgContent + ')');
							var lat = jsonObj.lat; //纬度
							var lon = jsonObj.lon; //经度
							var title = jsonObj.title; //位置信息描述
							var windowWid = $(window).width();
							var imgWid = 0;
							var imgHei = 0;
							if (windowWid < 666) {
								imgWid = 100;
								imgHei = 150;
							} else {
								imgWid = 150;
								imgHei = 200;
							};
							var str = '<img src="img/baidu.png" style="cursor:pointer;max-width:' + imgWid + 'px; max-height:' + imgHei + 'px;" onclick="IM.DO_show_map(\'' + lat + '\', \'' + lon + '\', \'' + title + '\')"/>';
						};
						if (msg.msgType == 7) {
							str = '<pre imtype="msg_attach" style="white-space: pre-wrap; word-wrap:break-word">' + '<a imtype="msg_attach_href" href="' + msg.msgFileUrl + '" target="_blank">' + '<span>' + '<img style="width:32px; height:32px; margin-right:5px; margin-left:5px;" src="/static/storeback/service/assets/img/attachment_icon.png" />' + '</span>' + '<span imtype="msg_attach_name">' + msg.msgFileName + '</span>' + '</a>' + '</pre>';
						};
						if (!!msg && msg.msgSender == IM._user_account) {
							// 追加自己聊天记录
							IM.HTML_sendMsg_addPreHTML("msg", msg.msgType, null, null, msg.msgReceiver, str);
						} else {
							// 追加对方聊天记录
							IM.HTML_pushMsg_addPreHTML(msg.msgType, msg.msgReceiver, msg.version, null, true, msg.msgSender, str);
						}
					}
				},
				function(obj) {
					if (obj.code == "540016") {
						$("#getHistoryMsgDiv").html('<a href="javascript:void(0);" style="font-size: small;position: relative;top: -30px;">没有更多历史消息</a>');
					} else {
						alert("错误码：" + obj.code + "; 错误描述：" + obj.msg)
					}
				});
		},
			
		EV_cancel: function() {
			IM.Do_notice(0, "f");
			RL_YTX.audio.cancel();
			$("#pop_recorder").hide();
			$("#recorderAudio").attr("src", "");
		},
	
		/**
		 * 不支持usermedie
		 */
		Check_usermedie_isDisable: function() {

			$("#camera_button").removeAttr("onclick");
			$("#camera_button").html('<i class="icon-picture"></i>');
			$("#camera_button").click(function() {
				IM.DO_im_image_file();
			});

			$("#startRecorder").append('<span style="color:#FF0000; font-size: 16px; font-weight:bold;margin-left:-15px;">X</span>');
			$("#startRecorder").removeAttr("onclick");

			IM.SendVoiceAndVideo_isDisable();
		},

		/**
		 * 切换按钮
		 */
		Check_login: function() {
			$("div[name = 'loginType']").each(function() {
				var display = $(this).css("display");
				if (display == 'none') {
					IM._loginType = $(this).attr("id");
					$(this).show();
				} else {
					$(this).hide();
				}
			});
		},
		isNull: function(value) {
			if (value == '' || value == undefined || value == null) {
				return true;
			}
		},
		DO_cleanChatHis: function(groupId) {
			$('#im_content_list > div[content_you="' + groupId + '"]').each(function() {
				$(this).remove();
			});
		},
		/**
		 * 桌面提醒功能
		 * @param you_sender 消息发送者账号
		 * @param nickName 消息发送者昵称
		 * @param you_msgContent 接收到的内容
		 * @param msgType 消息类型
		 * @param isfrieMsg 是否阅后即焚消息
		 * @param isCallMsg 是否音视频呼叫消息
		 */
		DO_deskNotice: function(you_sender, nickName, you_msgContent, msgType, isfrieMsg, isCallMsg, inforSender, isAtMsg) {
			console.log("you_msgContent=" + you_msgContent + "；msgType=" + msgType + "；isCallMsg=" + isCallMsg);
			var title;
			var body = '';
			if (!!you_sender || !!nickName) {
				if ('g' == you_sender.substr(0, 1)) {
					title = "群消息";
					if (!!nickName) {
						body =  isAtMsg+inforSender + ":" ;
					} else {
						body = you_sender + ":";
					}
				} else {
					if (!!nickName) {
						title = nickName;
					} else {
						title = you_sender;
					}
				}
			} else {
				title = "系统通知";
				body = you_msgContent;
			}

			if (isfrieMsg) {
				body += "[阅后即焚消息]";
			} else if (isCallMsg) {
				body += you_msgContent;
			} else {
				if (1 == msgType) {
					emoji.showText = true;
					you_msgContent = emoji.replace_unified(you_msgContent);
					emoji.showText = false;
					body += you_msgContent;
				} else if (2 == msgType) {
					body += "[语音]";
				} else if (3 == msgType) {
					body += "[视频]";
				} else if (4 == msgType) {
					body += "[图片]";
				} else if (5 == msgType) {
					body += "[位置]";
				} else if (6 == msgType || 7 == msgType) {
					body += "[附件]";
				} else if (11 == msgType) {
					body += you_msgContent; //@群组，type为11的时候
				}
			}
			if (11 == msgType) {
				if (!IM._Notification) {
					return;
				}
			} else {
				if (!IM._Notification || !IM.checkWindowHidden()) {
					return;
				}
			}
			var instance = new IM._Notification(
				title, {
					body: body,
					icon: "http://h5demo.yuntongxun.coassets/img/m/logo-blue.png"
				}
			);

			instance.onclick = function() {
				// Something to do
			};
			instance.onerror = function() {
				// Something to do
				console.log('notification encounters an error');
			};
			instance.onshow = function() {
				// Something to do
				setTimeout(function() {
					//instance.onclose();
					instance.close();
				}, 3000);
			};
			instance.onclose = function() {
				// Something to do
				console.log('notification is closed');
			};
		},
		/**
		 * 获取hidden属性
		 */
		getBrowerPrefix: function() {
			return 'hidden' in document ? null : function() {
				var r = null;
				['webkit', 'moz', 'ms', 'o'].forEach(function(prefix) {
					if ((prefix + 'Hidden') in document) {
						return r = prefix;
					}
				});
				return r;
			}();
		},

		checkWindowHidden: function() {
			var prefix = IM.getBrowerPrefix();
			//不支持该属性
			if (!prefix) {
				return document['hidden'];
			}
			return document[prefix + 'Hidden'];
		},
		//输入框校验
		Do_boxCheck: function(flag) {
			var result = [];
			var im_send_content = IM.DO_pre_replace_content_to_db();
			if (im_send_content != "") {
				return;
			}
			var msgid = new Date().getTime();
			var content_type = '';
			var content_you = '';
			var b = false;
			$('#im_contact_list').find('li').each(function() {
				if ($(this).hasClass('active')) {
					content_type = $(this).attr('contact_type');
					content_you = $(this).attr('contact_you');
					b = true;
				}
			});
			if (flag != "f" & !b) {
//				alert("暂无新消息！");
				return;
			}
			result[0] = msgid;
			result[1] = im_send_content;
			result[2] = content_you;
			result[3] = content_type;
			return result;
		},
		/*
		 *发消息提示
		 *bOn domain 状态参数 0表示结束，1表示正在写，2表示正在录音
		 * t表示需要消息提示，f不需要消息提示
		 * */
		Do_notice: function(domain, bOn) {
			console.log("focus");
			var result = IM.Do_boxCheck(bOn);
			if (result != null) {
				// 发送消息至服务器
				if (IM._contact_type_c == result[3]) {
					IM.EV_sendTextMsg(result[0], domain, result[2], false, IM._transfer);
				}
			}
		}
	};
})();
