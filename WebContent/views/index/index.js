define("views/index/index",["base","bootstrapvalidator"],function(base,boot){
	var failMsg = '<div class="alert alert-warning" style="margin-bottom: -10px"><a href="#" class="close" data-dismiss="alert">&times;</a><label class="msg"></label></div>';
	var successMsg = '<div class="alert alert-success" ><a href="#" class="close" data-dismiss="alert">&times;</a><label class="msg"></label></div>';
	
	base.getAnnouncement(".announcement",5);
	//为前往注册按钮添加点击事件
	$("#goRegister").on("click",function(){
		$("#login").hide();
		$("#register").show().find('[name="userName"]')[0].focus();
	});
	//进入即让输入用户名/手机号的文本框获得焦点
	var js_loginUN = $("#login").find('[name="userName"]')[0];
	js_loginUN.focus();
	//为前往登录按钮添加点击事件
	$("#goLogin").on("click",function(){
		$("#register").hide();
		$("#login").show();
		js_loginUN.focus();
	});
	
	var loginHandler = function(){
		var userName = loginInput.eq(0).val();
		var password = loginInput.eq(1).val();
		var regRex1 = /^[a-zA-Z][a-zA-Z0-9_]{2,16}$/;
		var regRexForPhone = /^1[0-9]{10}$/;
		var regRex2 = /^[a-zA-Z0-9_]{3,16}$/;
		if((!regRex1.test(userName)&&
				!regRexForPhone.test(userName))||
					!regRex2.test(password)){
			var $alert = $("#loginFail");
			var $failMsg = null;
			if($alert.length!=0)
				$failMsg = $alert;
			else
				$failMsg = $(failMsg).attr("id","loginFail");
			$failMsg.find(".msg").text("用户名或密码格式不正确!");
			$("#loginBtn").before($failMsg);
			js_loginUN.focus();
			return;
		}
		base.doFormAjax("/login","#login form",{
			onSuccess:function(data){
				if(data.code=="00000"){
					var $alert = $("#loginFail");
					var $failMsg = null;
					if($alert.length!=0)
						$failMsg = $alert;
					else
						$failMsg = $(failMsg).attr("id","loginFail");
					$failMsg.find(".msg").text(data.reason);
					$("#loginBtn").before($failMsg);
					js_loginUN.focus();
				}else{
					var userType = data.data.usertypes;
					if(userType=='10')
						window.location.href ="/stu/personal/personalData";
					else if(userType=='20')
						window.location.href ="/admin/userManager/checkUser";
				}
			}
		});
	}
	//登录按钮
	$("#loginBtn").on("click",function(){
		loginHandler();
	});
	var loginInput = $("#login form input").on("keyup",function(event){
		if(event.keyCode===13)
			loginHandler();
	});
	
	//注册按钮
	$("#registerBtn").on("click",function(){
		var form = "#register form";
		var $form = $(form);
		var $flagFalse = $form.find('[data-info="false"]');
		if($flagFalse.length!=0){
			$flagFalse[0].focus();
			return;
		}else{
			base.doFormAjax("/register",form,{
				onSuccess:function(data){
					if(data.code=="00000"){
						var $alert = $("#registerFail");
						var $failMsg = null;
						if($alert.length!=0)
							$failMsg = $alert;
						else
							$failMsg = $(failMsg).attr("id","registerFail");
						$failMsg.find(".msg").text(data.reason);
						$("#registerBtn").before($failMsg);
					}else{
						window.location.href = "/stu/personal/personalData";
					}
				}
			},"post",{phoneNumber:$("#register [name='phoneNumber']").val()});
		}
			
	});
	
	//注册,userName栏失去焦点事件（异步校验用户名是否重复）
	var $registerUserName = $('#register [name="userName"]').on("blur",function(){
		var $this = $(this);
		var userName = $this.val();
		var regRex = /^[a-zA-Z][a-zA-Z0-9_]{2,16}$/;
		var $alert = $("#userNameMsg");
		if(regRex.test(userName))
			checkData("userName",userName);
		else{
			$this.attr("data-info","false");
			$registerPhoneNumber.attr("disabled","disabled");
			$registerSendMsg.attr("disabled","disabled")
			$('[name="code"]').attr("disabled","disabled");
			var $failMsg = null;
			if($alert.length!=0)
				$failMsg = $alert; 
			else
				$failMsg = $(failMsg);
			$failMsg.attr("id","userNameMsg").find(".msg").text("用户名只能为字母开头的3~16位的数字，字母，或下划线！");
			$this.after($failMsg);
			$this.parents(".form-group").removeClass("has-success").addClass("has-error");
		}	
	});
	//注册，密码栏事件
	var $registerPassword = $('#register [name="password"]').on("blur",passwordCheckHandler).on("keyup",passwordCheckHandler);
	//
	function passwordCheckHandler(){
		var $this = $(this);
		var password = $this.val();
		if(trimString($registerPasswordAgain.val())!="")
			$registerPasswordAgain.val("");
		var regRex = /^[a-zA-Z0-9_]{3,16}$/;
		var $alert = $("#passwordMsg");
		if(regRex.test(password)){
			$this.attr("data-info","true");
			$alert.remove();
			$this.parents(".form-group").removeClass("has-error").addClass("has-success");
		}else{
			$this.attr("data-info","false");
			var $failMsg = null;
			if($alert.length!=0)
				$failMsg = $alert; 
			else
				$failMsg = $(failMsg);
			$failMsg.attr("id","passwordMsg").find(".msg").text("密码格式错误！");
			$this.after($failMsg)
			$this.parents(".form-group").removeClass("has-success").addClass("has-error");
		}
	}
	//注册，确认密码栏失去焦点事件
	var $registerPasswordAgain = $('#register [name="passwordAgain"]').on("blur",passwordAgainCheckHandler).on("keyup",passwordAgainCheckHandler);
	//
	function passwordAgainCheckHandler(){
		var $this = $(this);
		var passwordAgain = $this.val();
		var password = $registerPassword.val();
		var $alert = $("#passwordAgainMsg");
		if(passwordAgain==password){
			$this.attr("data-info","true");
			$alert.remove();
			$this.parents(".form-group").removeClass("has-error").addClass("has-success");
		}else{
			$this.attr("data-info","false");
			var $failMsg = null;
			if($alert.length!=0)
				$failMsg = $alert; 
			else
				$failMsg = $(failMsg);
			$failMsg.attr("id","passwordAgainMsg").find(".msg").text("两次密码不一致！");
			$this.after($failMsg)
			$this.parents(".form-group").removeClass("has-success").addClass("has-error");
		}
	}
	//注册，手机号栏字符改变事件（异步校验用户名是否重复）
	var $registerPhoneNumber = $('#register [name="phoneNumber"]').on("keyup",phoneNumberCheckHandler).on("blur",phoneNumberCheckHandler);
	//
	function phoneNumberCheckHandler(){
		var $this = $(this);
		var phoneNumber = $this.val();
		var regExp = /^1[0-9]{10}$/;
		var $alert = $("#phoneNumberMsg");
		if(phoneNumber.length==11&&regExp.test(phoneNumber)){
			$alert.remove();
			checkData("phoneNumber",phoneNumber);
		}else{
			$this.attr("data-info","false");
			var $failMsg = null;
			if($alert.length!=0)
				$failMsg = $alert; 
			else 
				$failMsg = $(failMsg);
			$failMsg.attr("id","phoneNumberMsg").find(".msg").text("非法的手机号码！");
			$this.after($failMsg)
			$this.parents(".form-group").removeClass("has-success").addClass("has-error");
			$registerSendMsg.attr("disabled","disabled").hide();
			$registerCode.attr("disabled","disabled").hide();
		}
	}
	
	//发送验证码按钮
	var $registerSendMsg = $("#sendMsg").hide().on("click",sendMsg);
	
	// 
	function sendMsg(){
		var $form = $(this).parents("form");
		var info = {};
		info.userName = $form.find('[name="userName"]').val();
		info.phoneNumber = $form.find('[name="phoneNumber"]').val();
		base.log(info)
		base.doAjax("sendMsg",info,{
			onSuccess:function(data){
				var $alert = $("#sendMsgAlert");
				var $sendMsgAlert = null;
				if($alert.length!=0)
					$sendMsgAlert = $alert;
				else
					$sendMsgAlert = $(successMsg).attr("id","sendMsgAlert");
				if(data.code=="11111"){
					$sendMsgAlert.removeClass("alert-warning").addClass("alert-success");
					$sendMsgAlert.find(".msg").text(data.data);
					$('[name="code"]').removeAttr("disabled").focus();
					$registerSendMsg.off("click");
					registerSendMsgHandler(60,0);
				}else{
					$sendMsgAlert.removeClass("alert-success").addClass("alert-warning");
					$sendMsgAlert.find(".msg").text(data.code+","+data.reason);
					$registerCode.attr("disabled","disabled");
				}
				$("#sendMsg").after($sendMsgAlert);
			}
		});
	} 
	//发送验证码倒计时
	function registerSendMsgHandler(num,min){
		num--;
		$registerSendMsg.attr("disabled","disabled");
		$registerPhoneNumber.attr("disabled","disabled");
		$registerSendMsg.text("重新发送("+num+")");
		if(num>min){
			setTimeout(function(){
				registerSendMsgHandler(num,min);
			},1000)
		}else{
			$registerSendMsg.text("重新发送");
			$registerSendMsg.removeAttr("disabled");
			$registerPhoneNumber.removeAttr("disabled");
			$registerSendMsg.on("click",sendMsg)
		}
	}
	
	//验证码输入框事件
	var $registerCode = $('#register [name="code"]').hide().on("keyup",codeCheckHandler).on("blur",codeCheckHandler);
	//
	function codeCheckHandler(){
		var $this = $(this);
		var regRex = /^[0-9]{6}$/;
		if(regRex.test($this.val())){
			$this.attr("data-info","true");
			$this.parents(".form-group").removeClass("has-error").addClass("has-success");
		}else{
			$this.attr("data-info","false");
			$this.parents(".form-group").removeClass("has-success").addClass("has-error");
		}
	}
	//针对用户名/手机号是否重复对页面做出动态改变
	function changePage(key,result){
		if(key=="userName"){
			var $alert = $("#userNameMsg");
			if(result.code=="00000"){
				$registerPhoneNumber.attr("disabled","disabled");
				$registerSendMsg.attr("disabled","disabled")
				$('[name="code"]').attr("disabled","disabled");
				var $failMsg = null;
				if($alert.length!=0)
					$failMsg = $alert; 
				else 
					$failMsg = $(failMsg);
				$failMsg.attr("id","userNameMsg").find(".msg").text("用户名"+result.reason);
				$registerUserName.after($failMsg).attr("data-info","false").parents(".form-group").removeClass("has-success").addClass("has-error");
			}else if(result.code=="11111"){
				$alert.remove();
				$registerPhoneNumber.removeAttr("disabled");
				$registerUserName.attr("data-info","true").parents(".form-group").removeClass("has-error").addClass("has-success");
			}
		}else if(key=="phoneNumber"){
			var $alert = $("#phoneNumberMsg");
			if(result.code=="00000"){
				$registerSendMsg.attr("disabled","disabled");
				$('[name="code"]').attr("disabled","disabled");
				var $failMsg = null;
				if($alert.length!=0)
					$failMsg = $alert; 
				else 
					$failMsg = $(failMsg);
				$failMsg.attr("id","phoneNumberMsg").find(".msg").text("手机号"+result.reason);
				$registerSendMsg.hide();
				$registerCode.hide();
				$registerPhoneNumber.attr("data-info","false").after($failMsg).parents(".form-group").removeClass("has-success").addClass("has-error");
			}else if(result.code=="11111"){
				$alert.remove();
				$registerSendMsg.removeAttr("disabled").show();
				$registerCode.show();
				$registerPhoneNumber.attr("data-info","true").parents(".form-group").removeClass("has-error").addClass("has-success");
			}
		}
	}
	//校验用户名/手机号是否重复的方法
	function checkData(key,val){
		var info = {};
		if(key=="userName")
			info.userName = val;
		else if(key=="phoneNumber")
			info.phoneNumber = val;
		/*doAjax("checkData",info,{
			onSuccess:function(data){
				changePage(key,data);
			}
		});*/
		base.Communicator.send("IndexService.checkData",info,"User",{
			onSuccess:function(data){
				changePage(key,data);
			}
		},false)
	}
})