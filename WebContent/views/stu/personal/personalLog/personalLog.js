define(["base","common"],function(base,common){
	function PersonalLog(){
		this.url = window.location.pathname;
	}

	PersonalLog.prototype = {
		init : function(setting){
			this.$safeTab = $("#safeTab",setting);
			this.$userLogTab = $("#userLogTab",setting);
			this.$userLogPage = $("#userLogPage",setting);
			this.$safePage = $("#safePage",setting);
			this.$safePasswordForm = $('.changePasswordForm',setting);
			this.$safePhoneNumberForm = $('.changePhoneNumberForm',setting);
			this.getNavigationOnClick("userLog");
			this.$userLogTab.click();
		},
		choosePage : function(type){
			var that = this;
			if(type=="userLog"){
				that.$userLogPage.show();
				that.$safePage.hide();
				that.$userLogTab.addClass("active").siblings().removeClass("active");
				that.getNavigationOnClick("safe");
			}else{
				that.$safePage.show();
				that.$userLogPage.hide();
				that.$safeTab.addClass("active").siblings().removeClass("active");
				that.getNavigationOnClick("userLog");
			}
		},
		getNavigationOnClick : function(type){
			var that = this;
			if(type=="userLog"){
				that.$userLogTab.off("click").on("click",function(){
					that.choosePage(type);
					that.randerView(type);
				});
				that.$safeTab.off("click");
			}else{
				that.$safeTab.off("click").on("click",function(){
					base.p("请输入密码？",null,function(flag,str){
						if(flag){
							var info = {};
							info.userName = common.userName;
							info.password = str;
							base.getJsonObject(info,"User","UserMapper.select").done(function(data){
								var realDate = base.parseJsonObject(data);
								if(realDate){
									that.choosePage(type);
									that.randerView(type);
								}else{
									base.a("密码错误！","error");
								}
							});
						}
					});
				});
				that.$userLogTab.off("click");
			}
		},
		randerView : function(type){
			var that = this;
			if(type=="userLog"){
				var info = {};
				info.userName = common.userName;
				info.orderBy = "login_Date desc"
				base.paging(info,"UserLog","UserLogMapper.select",10,1,applyView,true,"#userLogPage .paging")
				function applyView(data){
					if(data.code==00000){
						base.a("查询错误！"+data.reason)
						base.log(data.reason)
						return;
					}
					var $dataTable = $("#userLogPage .table tbody").empty();
					$.each(data.data,function(key,val){
						var $tr = $('<tr><td data-type="userName"/><td data-type="loginDate"/><td data-type="onlineTime"/></tr>')
							.attr("data-info",JSON.stringify(val));
						$.each(val,function(k,v){
							$tr.children('[data-type='+k+']').text(v==null?"在线":v);
						});
						$dataTable.append($tr)
					});
					$dataTable.children().off("click").on("click",function(){
						var $this = $(this);
						$this.addClass("active").siblings().removeClass("active");
						var data = JSON.parse($this.attr("data-info"));
						var $more = $("#userLogPage [data-type='details']");
						$more.find("input").val("");
						$.each(data,function(k,v){
							if(k=="onlineTime"){
								var onlineTime = base.parseTime(v);
								$more.find("[data-type='"+k+"']").val(onlineTime==null?"在线":onlineTime);
							}else
								$more.find("[data-type='"+k+"']").val((v&&v!=0)?v:"未获取到！");
						});
						
					});
					$dataTable.children().eq(0).click();
				}
				}else{
					//表单验证
					that.$safePasswordForm.bootstrapValidator({
						feedbackIcons: {
				            valid: 'glyphicon glyphicon-ok',
				            invalid: 'glyphicon glyphicon-remove',
				            validating: 'glyphicon glyphicon-refresh'
				        },
				        fields: {
				           oldPassword: {
				               validators: {
				                   notEmpty: {
				                       message: '密码不能为空'
				                   },
				                   regexp: {
				                       regexp: /^[a-zA-Z0-9_]{3,16}$/,
				                       message: '密码格式不正确'
				                   }
				               }
				           },
				           password: {
				               validators: {
				            	   notEmpty: {
				                       message: '密码不能为空'
				                   },
				                   regexp: {
				                       regexp: /^[a-zA-Z0-9_]{3,16}$/,
				                       message: '密码只能为3-16为的数字、字母以及下划线组成'
				                   },
				                   different: {
				            		   field: 'oldPassword',
				            		   message: '新密码不能与原密码相同'
				            	   }
				               }
				           },
				           againPassword: {
				               validators: {
				            	   notEmpty: {
				                       message: '密码不能为空'
				                   },
				            	   identical: {
				            		   field: 'password',
				            		   message: '两次密码输入不一致'
				            	   },
				            	   different: {
				            		   field: 'oldPassword',
				            		   message: '新密码不能与原密码相同'
				            	   }
				               }
				           }
				       }
				   });
				//提交	
				$("[type='button']",that.$safePasswordForm).off("click").on("click",function(){
					var v = that.$safePasswordForm.data('bootstrapValidator');
					var flag = v.isValid()
					v.validate();
					if(flag){
						var info = {};
						info.userName = $("[name='password']",that.$safePasswordForm).val();
						info.password = $("[name='oldPassword']",that.$safePasswordForm).val();
						base.showMask();
						base.Communicator.send("PersonalLogService.changePassword",info,"User",{
							onSuccess : function(data){
								if(data.code==common.SUCCESS)
									base.a(data.data);
								else
									base.a(data.reason,"error");
								base.hideMask();
								that.$safePasswordForm[0].reset();
							}
						});
						v.resetForm();
					}
				})
				//跳转
				$("a",that.$safePasswordForm).off("click").on("click",function(){
					that.$safePasswordForm.fadeOut(2000,function(){
						that.$safePhoneNumberForm.fadeIn(2000);
					});
				});
				
				
				that.$safePhoneNumberForm.bootstrapValidator({
					feedbackIcons: {
			            valid: 'glyphicon glyphicon-ok',
			            invalid: 'glyphicon glyphicon-remove',
			            validating: 'glyphicon glyphicon-refresh'
			        },
			        fields: {
			        	oldPhoneNumber: {
			               validators: {
			                   notEmpty: {
			                       message: '手机号不能为空'
			                   },
			                   regexp: {
			                       regexp: /^1[0-9]{10}$/,
			                       message: '手机号格式不正确！'
			                   }
			               }
			           },
			           phoneNumber: {
			               validators: {
			            	   notEmpty: {
			                       message: '手机号不能为空'
			                   },
			                   regexp: {
			                       regexp: /^1[0-9]{10}$/,
			                       message: '手机号格式不正确！'
			                   },
			                   different: {
			            		   field: 'oldPhoneNumber',
			            		   message: '新号码不能与原号码相同'
			            	   }
			               }
			           },
			           code: {
			               validators: {
			            	   notEmpty: {
			                       message: '验证码不能为空！'
			                   },
			                   regexp: {
			                       regexp: /^[0-9]{6}$/,
			                       message: '验证码格式不正确！'
			                   },
			               }
			           }
			       }
				});
				//跳转
				$("a",that.$safePhoneNumberForm).off("click").on("click",function(){
					that.$safePhoneNumberForm.fadeOut(2000,function(){
						that.$safePasswordForm.fadeIn(2000);
					});
				});
				var v = that.$safePhoneNumberForm.data('bootstrapValidator');
				var $oldPhoneNumber = $("[name='oldPhoneNumber']",that.$safePhoneNumberForm);
				var $phoneNumber = $("[name='phoneNumber']",that.$safePhoneNumberForm);
				var $code = $("[name='code']",that.$safePhoneNumberForm).attr("disabled","disabled");
				var $sendCode = $(".sendCode",that.$safePhoneNumberForm).off("click").on("click",function(){sendMsg()});
				
				//发送验证码点击事件
				function sendMsg(){
					v.enableFieldValidators("code",false);
					v.validate();
					if(v.isValid()){
						var info = {};
						info.userName = $oldPhoneNumber.val();
						info.phoneNumber = $phoneNumber.val();
						base.showMask();
						base.Communicator.send("PersonalLogService.sendResetMsg",info,"User",{
							onSuccess : function(data){
								if(data.code==common.SUCCESS){
									base.a(data.data);
									$oldPhoneNumber.attr("disabled","disabled");
									$phoneNumber.attr("disabled","disabled");
									$code.removeAttr("disabled");
									v.enableFieldValidators("code",true);
									
									$sendCode.on("click",function(){sendMsg()});
									resetSendMsgHandler(60,0);
									$code.focus();
								}else{
									base.a(data.reason,"error");
									$oldPhoneNumber.focus();
								}
								v.resetForm();
								base.hideMask();
							}
						});
					}
				}
				//
				function resetSendMsgHandler(num,min){
					num--;
					$sendCode.attr("disabled","disabled");
					$oldPhoneNumber.attr("disabled","disabled");
					$phoneNumber.attr("disabled","disabled");
					$sendCode.val("重新发送("+num+")");
					if(num>min){
						setTimeout(function(){
							resetSendMsgHandler(num,min);
						},1000)
					}else{
						$sendCode.val("重新发送");
						$sendCode.removeAttr("disabled");
						$oldPhoneNumber.removeAttr("disabled");
						$phoneNumber.removeAttr("disabled");
						$sendCode.on("click",function(){sendMsg()})
					}
				}
				
				//提交（换号码）
				var $submitChangePhone = $(".submitChangePhone",that.$safePhoneNumberForm).off("click").on("click",function(){
					if($code.attr("disabled")){
						$oldPhoneNumber.focus();
						return;
					}
					v.validate();
					if(v.isValid()){
						var info = {};
						info.oldPhoneNumber = $oldPhoneNumber.val();
						info.newPhone = $phoneNumber.val();
						info.code = $code.val();
						base.showMask();
						base.Communicator.send("PersonalLogService.resetPhone",info,"map",{
							onSuccess : function(data){
								if(data.code==common.SUCCESS){
									base.a(data.data);
									$oldPhoneNumber.removeAttr("disabled");
									$phoneNumber.removeAttr("disabled");
									that.$safePhoneNumberForm[0].reset();
									$code.attr("disabled","disabled");
								}else{
									base.a(data.reason,"error");
									$code.focus();
								}
								v.resetForm();
								base.hideMask();
							}
						});
					}
				});
			}
		}
	}
	var personalLog = new PersonalLog();
	personalLog.init($("body"));
//	$('.defalutForm').bootstrapValidator('validate');

});