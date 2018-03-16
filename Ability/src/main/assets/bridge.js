/**
 * Created by chenlinfang on 2017/10/24.
 ＊国创移动应用能力平台js封装
 */

(function() {
	function connectWebViewJavascriptBridge(callback) {
		if(window.WebViewJavascriptBridge) {
			callback(WebViewJavascriptBridge)
		} else {
			document.addEventListener(
				'WebViewJavascriptBridgeReady',
				function() {
					callback(WebViewJavascriptBridge)
				},
				false
			);
		}
	}

	connectWebViewJavascriptBridge(function(bridge) {
		bridge.init(function(message, responseCallback) {
			var data = {
				'Javascript Responds': 'Wee!'
			};
			alert(message);
			responseCallback(data);
		});
	})

	var mplat = {

		getUserInfos: function(callback) {
			/**
			 * 获取用户信息方法
			 * @param {Object} options : param(参数) callback（回调函数，返回data ）
			 */
			window.WebViewJavascriptBridge.callHandler(
				'getUserInfos', {
					"params": "获取用户数据"
				},
				function(responseData) {
					if(callback != null)
						callback(responseData)
				}
			);
		},

		takePhoto:function(options){
			alert("options");
			console.log("options:"+options);
              		var params = options.params;
              		var str = "";
              		if(params !=null) {
              		    str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
              		   }
              			window.WebViewJavascriptBridge.callHandler(
              				'takePhoto', {
              					'params': str
              				},
              				function(responseData) {
              				if(options.callback)
              					options.callback(responseData);
              				}
              			);
		},
		scanGetCode: function(callback) {
        			/**
        			 * 获取用户信息方法
        			 * @param {Object} options : param(参数) callback（回调函数，返回data ）
        			 */
        			window.WebViewJavascriptBridge.callHandler(
        				'scanGetCode', {
        					"params": "获取用户数据"
        				},
        				function(responseData) {
        					if(callback != null)
        						callback(responseData)
        				}
        			);
        		},

        pickPic: function(options) {
        			console.log("options:"+options);
                      		var params = options.params;
                      		var str = "";
                      		if(params !=null) {
                      		    str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                      		   }
                      			window.WebViewJavascriptBridge.callHandler(
                      				'pickPic', {
                      					'params': str
                      				},
                      				function(responseData) {
                      				if(options.callback)
                      					options.callback(responseData);
                      				}
                      			);


                      		},
        showLoading: function(options) {
                            var params = options.params;
                            var str = "";
                            if(params !=null) {
                                str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                               }
                             window.WebViewJavascriptBridge.callHandler(
                 				'showLoading', {
                					"params": str
                				}
                			);
                		},

       cancelLoading: function(options) {
                                   window.WebViewJavascriptBridge.callHandler('cancelLoading');
                      		},
       getLocationInfo: function(options) {
                                    window.WebViewJavascriptBridge.callHandler('getLocationInfo');
                            },

       telephoneCall: function(options) {
                                   var params = options.params;
                                   var str = "";
                                   if(params !=null) {
                                       str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                                      }
                                    window.WebViewJavascriptBridge.callHandler(
                        				'telephoneCall', {
                       					"params": str
                       				}
                       			);
                       		},

       shortMessage: function(options) {
                                   var params = options.params;
                                   var str = "";
                                    if(params !=null) {
                                        str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                                    }
                                    window.WebViewJavascriptBridge.callHandler(
                                    'shortMessage',{
                                        "params": str
                                    });
                            },
       addressBook: function(options) {
                                   window.WebViewJavascriptBridge.callHandler('addressBook');

                           },
       getPhoneDeviceName: function(options) {
       	window.WebViewJavascriptBridge.callHandler(
       				'getPhoneDeviceName',
       				function(responseData) {
       					if(options.callback != null)
       						options.callback(responseData)
       				}
       			);
              },
       createTableInfo: function(options) {
              var params = options.params;
                var str = "";
                if(params !=null) {
                    str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                   }
                 window.WebViewJavascriptBridge.callHandler(
                    'createTableInfo', {
                        "params": str
                    },
        function(responseData) {
            if(options.callback != null)
                options.callback(responseData)
                 }
                );
         },
          insertInfo: function(options) {
                       var params = options.params;
                         var str = "";
                         if(params !=null) {
                             str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                            }
                          window.WebViewJavascriptBridge.callHandler(
                             'insertInfo', {
                                 "params": str
                             },
                 function(responseData) {
                     if(options.callback != null)
                         options.callback(responseData)
                          }
                         );
                  },
       selectInfos: function(options) {
                             var params = options.params;
                               var str = "";
                               if(params !=null) {
                                   str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                                  }
                                window.WebViewJavascriptBridge.callHandler(
                                   'selectInfos', {
                                       "params": str
                                   },
                       function(responseData) {
                           if(options.callback != null)
                               options.callback(responseData)
                                }
                               );
                        },

        deleteInfo: function(options) {
                             var params = options.params;
                               var str = "";
                               if(params !=null) {
                                   str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                                  }
                                window.WebViewJavascriptBridge.callHandler(
                                   'deleteInfo', {
                                       "params": str
                                   },
                       function(responseData) {
                           if(options.callback != null)
                               options.callback(responseData)
                                }
                               );
                        },
     updateInfo: function(options) {
                                 var params = options.params;
                                   var str = "";
                                   if(params !=null) {
                                       str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                                      }
                                    window.WebViewJavascriptBridge.callHandler(
                                       'updateInfo', {
                                           "params": str
                                       },
          function(responseData) {
                               if(options.callback != null)
                                   options.callback(responseData)
                                    }
                                   );
                            },


     dropTable: function(options) {
                                      var params = options.params;
                                        var str = "";
                                        if(params !=null) {
                                            str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                                           }
                                         window.WebViewJavascriptBridge.callHandler(
                                            'dropTable', {
                                                "params": str
                                            },
               function(responseData) {
                                    if(options.callback != null)
                                        options.callback(responseData)
                                         }
                                        );
                                 },
    lockScreen: function(options) {
                                   var params = options.params;
                                   var str = "";
                                   if(params !=null) {
                                       str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                                      }
                                    window.WebViewJavascriptBridge.callHandler(
                                       'lockScreen', {
                                           "params": str
                                       }
                                   );
    		},
    screenShot: function(options) {
                                   var params = options.params;
                                   var str = "";
                                   if(params !=null) {
                                       str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                                      }
                                    window.WebViewJavascriptBridge.callHandler(
                                       'screenShot', {
                                           "params": str
                                       },
           function(responseData) {
                                if(options.callback != null)
                                    options.callback(responseData)
                                     }
                                    );
        		},
    changeScreenIntensity: function(options) {
                                       var params = options.params;
                                       var str = "";
                                       if(params !=null) {
                                           str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                                          }
                                        window.WebViewJavascriptBridge.callHandler(
                                           'changeScreenIntensity', {
                                               "params": str
                                           });
                    },
     //录音测试
      recordVoice: function(options) {
                                         var params = options.params;
                                          //录音最短时间
                                          var str = "";
                                            if(params !=null) {
                                                       str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                                            }
                                            window.WebViewJavascriptBridge.callHandler(
                                                   'recordVoice', {
                                                      "params": str
                                                   },
                                                 function(responseData) {
                                                     if(options.callback != null)
                                                       options.callback(responseData)
                                                 }
                                            );
                },
       //播放
        playVoice: function(options) {
                                      var params = options.params;
                                        //录音最短时间
                                       var str = "";
                                        if(params !=null) {
                                          str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                                        }
                                         window.WebViewJavascriptBridge.callHandler(
                                                  'playVoice', {
                                                        "params": str
                                                    }
                                         );
        },


        //上传
        uploadVoice: function(options) {
                 var params = options.params;
                //录音最短时间
                 var str = "";
                 if(params !=null) {
                      str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                  }
                  window.WebViewJavascriptBridge.callHandler(
                            'uploadVoice', {
                                         "params": str
                             }
                   );
        },
        //录制视频
        recordVideo: function(options) {
                                                 var params = options.params;
                                                  //录音最短时间
                                                  var str = "";
                                                    if(params !=null) {
                                                               str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                                                    }
                                                    window.WebViewJavascriptBridge.callHandler(
                                                           'recordVideo', {
                                                              "params": str
                                                           },
                                                         function(responseData) {
                                                             if(options.callback != null)
                                                               options.callback(responseData)
                                                         }
                                                    );
                        },
      // 播放视频
       playVideo: function(options) {
                                                        var params = options.params;
                                                         //录音最短时间
                                                         var str = "";
                                                           if(params !=null) {
                                                                      str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                                                           }
                                                           window.WebViewJavascriptBridge.callHandler(
                                                                  'playVideo', {
                                                                     "params": str
                                                                  });
                               },
    //获取包名
      getPackInfor: function(options) {
                                                            var params = options.params;
                                                             var str = "";
                                                               if(params !=null) {
                                                                          str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                                                               }
                                                               window.WebViewJavascriptBridge.callHandler(
                                                                      'getPackInfor', {
                                                                         "params": str
                                                                      },
                                                                    function(responseData) {
                                                                        if(options.callback != null)
                                                                          options.callback(responseData)
                                                                    }
                                                               );
                                   },
    //获取wifi信息
getWifiInfor: function(options) {
                                                            var params = options.params;
                                                             var str = "";
                                                               if(params !=null) {
                                                                          str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                                                               }
                                                               window.WebViewJavascriptBridge.callHandler(
                                                                      'getWifiInfor', {
                                                                         "params": str
                                                                      },
                                                                    function(responseData) {
                                                                        if(options.callback != null)
                                                                          options.callback(responseData)
                                                                    }
                                                               );
                                   },
       //分享
      shareTest: function(options) {
                                                                  var params = options.params;
                                                                   var str = "";
                                                                     if(params !=null) {
                                                                                str = typeof(params) == "object" ? JSON.stringify(params) : params+str;
                                                                     }
                                                                     window.WebViewJavascriptBridge.callHandler(
                                                                            'shareTest', {
                                                                               "params": str
                                                                            },
                                                                          function(responseData) {
                                                                              if(options.callback != null)
                                                                                options.callback(responseData)
                                                                          }
                                                                     );
                                         },

	}
	window.Mplat = mplat;
})()