define(["base","common"],function(base,common){

	var failMsg = '<div class="alert alert-warning" style="margin-bottom: -10px"><a href="#" class="close" data-dismiss="alert">&times;</a><label class="msg"></label></div>';
	//保存查询到的stu对象
	var stu = {};
	var $userMessagePage = $(".userMessagePage");
	var $userModification = $(".userMessagePage .modification");
	var $userDisplay = $(".userMessagePage .display");
	
	var $stuMessagePage = $(".stuMessagePage");
	var $stuModification = $(".stuMessagePage .modification");
	var $stuDisplay = $(".stuMessagePage .display");
	
	var $dorMessagePage = $(".dorMessagePage");
	
	var $stuPageLi = $(".stuPage").parent();
	var $dorPageLi = $(".dorPage").parent();
	//解析数据
	function parseData(data){
		var user = base.parseJsonObject(data);
		if(user!=null){	
//			$stuPageLi.addClass("disabled");
			$dorPageLi.addClass("disabled");
			getUserEvent();
			changeUserTablePage(1);
			var $key = null;
			var $display = null;
			var $modification = null;
			$.each(user,function(key,val){
				if(val==null)
					return;
				$key = $(".userMessagePage ."+key);
				$display = $key.children(".display");
				$modification = $key.children(".modification");
				if(key=="gender"){
					if(val==1){
						$display.text("男");
						$modification.children(":last").children().attr("checked",true);
					}else{
						$display.text("女");
						$modification.children(":first").children().attr("checked",true);
					}
				}else if(key=="email"){
					$display.text(val);
					var split = val.split("@");
					$modification.children(":first").val(split[0]);
					$modification.children(":last").val(split[1]);
				}else{
					$display.text(val);
					$modification.text(val).val(val);
				}
			});
			var stu = user.student;
			parseStudent(stu);
			var dor = user.dormitory;
			if(dor)
				parseDormitory(dor);
		}
			
	}
	//解析学生信息
	function parseStudent(stu,type){
		getStuEvent();
		$stuPageLi.removeClass("disabled");
		if(stu&&stu.id)
			$(".userMessagePage .studentId .display").text(stu.id);
		var $key = null;
		var $display = null;
		var $modification = null;
		$.each(stu,function(key,val){
			if(val==null)
				return;
			$key = $(".stuMessagePage ."+key);
			$display = $key.children(".display");
			$modification = $key.find(".modification");
			if(key=="gender"){
				if(val==1){
					if(type=="display")
						$display.text("男");
					else if(type=="modification")
						$modification.children(":last").children().attr("checked",true);
					else{
						$display.text("男");
						$modification.children(":last").children().attr("checked",true);
					}
				}else{
					if(type=="display")
						$display.text("女");
					else if(type=="modification")
						$modification.children(":first").children().attr("checked",true);
					else{
						$display.text("女");
						$modification.children(":first").children().attr("checked",true);
					}
				}
			}else{
				if(type=="display")
					$display.text(val);
//				if(key=="dormitoryNumber")
				else if(type=="modification")
					$modification.text(val).val(val);
				else{
					$display.text(val);
					$modification.text(val).val(val);
				}
					
			}
		});
	}
	
	//解析宿舍信息
	function parseDormitory(dor){
		getDorEvent();
		$dorPageLi.removeClass("disabled");
		$(".userMessagePage .dormitoryNumber .display").text(dor.dormitoryNumber);
		$(".stuMessagePage .dormitoryNumber .display,.stuMessagePage .dormitoryNumber .modification")
														.text(dor.dormitoryNumber);
		$.each(dor,function(key,val){
			$(".dorMessagePage ."+key).children().text(val);
		});
	
	}
	applyView();
	//渲染页面
	function applyView(){
		var info = {};
		info.userName = common.userName;
		info.phoneNumber = common.phoneNumber;
		base.getJsonObject(info,"User","UserMapper.select").done(function(data){
			parseData(data);
		});
	}
	
	//改变user Table页样式
	function changeUserTablePage(type){
		$userMessagePage.show();
		$stuMessagePage.hide();
		$dorMessagePage.hide();
		$(".tablePage .userPage").parent().addClass("active").siblings().removeClass("active");
		if(type==1){
			$userModification.hide();
			$userDisplay.show();
		}else if(type==2){
			$userModification.show();
//			$userModification.find()
			$(".userMessagePage .modification.input-group").css("display","table");
			$userDisplay.hide();
		}
	}
	//获取user 页面事件
	function getUserEvent(){
		$(".userPage").off("click").on("click",function(){
			changeUserTablePage(1);
		});
		$("#changeUserMessage").off("click").on("click",function(){
			changeUserTablePage(2);
		});
		$("#goBackUserMessage").off("click").on("click",function(){
			changeUserTablePage(1);
		});
		$("#submitUserMessage").off("click").on("click",function(){
			base.c("确认保存用户信息？",null,function(flag){
				if(flag){
					savaUserHandler(1);
				}
			});
		});
		$(".saveUser").off("click").on("click",function(){
			base.c("确认保存用户信息？",null,function(flag){
				if(flag){
					savaUserHandler(2);
				}
			});
		});
		//保存信息
		function savaUserHandler(type){
			var info = {};
			info.userName = $userMessagePage.find(".userName .modification").val();
			info.phoneNumber = $userMessagePage.find(".phoneNumber .modification").val();
			info.age = $userMessagePage.find(".age .modification").val();
			info.gender = $userMessagePage.find(".gender .modification [checked='checked']").val();
			var $prefix = $userMessagePage.find(".email .modification :first");
			var prefix = trimString($prefix.val());
			var $suffix = $userMessagePage.find(".email .modification :last");
			var suffix = trimString($suffix.val());
			if(prefix==""&&suffix!=""){
				$prefix.focus();
				return;
			}
			if(prefix!=""&&suffix==""){
				$suffix.focus();
				return;
			}
			if(prefix!=""&&suffix!="")
			info.email = prefix+"@"+suffix;
			base.Communicator.send("PersonalDataService.saveUser",info,"User",{
				onSuccess : function(data){
						parseData(data);
					if(type==2){
//						parseStudent({});
						$("#changeStuMessage").click();
					}
				},
				onFail : function(){
				}
			});
		}
		
	}
	//改变stu Table页样式
	function changeStuTablePage(type){
		$userMessagePage.hide();
		$stuMessagePage.show();
		$dorMessagePage.hide();
		$(".tablePage .stuPage").parent().addClass("active").siblings().removeClass("active");
		if(type==1){
			$stuModification.hide();
			$stuDisplay.show();
		}else if(type==2){
			$stuModification.show();
			$stuDisplay.hide();
		}
	}
	//获取stu 页面事件
	function getStuEvent(){
		$(".stuPage").off("click").on("click",function(){
			changeStuTablePage(1);
		});
		$("#changeStuMessage").off("click").on("click",function(){
			changeStuTablePage(2);
		});
		$("#goBackStuMessage").off("click").on("click",function(){
			changeStuTablePage(1);
		});
		
		var $idInput = $(".stuMessagePage .id .modification").off("blur").on("blur",idInputHandler).off("keyup").on("keyup",idInputHandler);
		function idInputHandler(){
			var $this = $(this);
			var stuId = $this.val();
			var regRex = /^[0-9]{8}$/;
			if(!regRex.test(stuId))
				$this.parents(".form-group").removeClass("has-success").addClass("has-error");
			else
				$this.parents(".form-group").removeClass("has-error").addClass("has-success");
		}
		var $personNumberInput = $(".stuMessagePage .personNumber .modification").off("blur").on("blur",personNumberInputHandler).off("keyup").on("keyup",personNumberInputHandler);
		function personNumberInputHandler(){
			var $this = $(this);
			var personNumber = $this.val();
			var regRex = /^[0-9]{17}([0-9]|X|x){1}$/;
			if(!regRex.test(personNumber)){
				$this.parents(".form-group").removeClass("has-success").addClass("has-error");
				return;
			}
			if($idInput.parents(".form-group").hasClass("has-success")){
				base.getJsonObject({id:$idInput.val(),personNumber:personNumber},"Student","StudentMapper.select").done(function(data){
					var stu = base.parseJsonObject(data);
					if(stu){
						$this.parents(".form-group").removeClass("has-error").addClass("has-success");
						$("#stuFail").remove();
						parseStudent(stu,"modification");
					}else{
						var $stuFail = $("#stuFail");
						if($stuFail.length==0)
							$stuFail = $(failMsg).attr("id","stuFail");
						$stuFail.find(".msg").text("未找到符合条件的学生！");
						$(".stuMessagePage").prepend($stuFail);
						$this.parents(".form-group").removeClass("has-success").addClass("has-error");
					}
				});
			}
		}
		
		//保存学生信息
		var $submitStuMessage = $("#submitStuMessage").off("click").on("click",function(){
			if($idInput.parent().hasClass("has-error")){
				$idInput.focus();
				return;
			}
			if($personNumberInput.parent().hasClass("has-error")){
				$personNumberInput.focus();
				return;
			}
			base.c("确认保存修改？",null,function(flag){
				if(flag){
					var info = {};
					info.id = $(".id [name='id']").val();
					base.Communicator.send("PersonalDataService.saveStudent",info,"Student",{
						onSuccess:function(data){
							if(data.code==common.FAIL){
								var $stuFail = $("#stuFail");
								if($stuFail.length==0)
									$stuFail = $(failMsg).attr("id","stuFail");
								$stuFail.find(".msg").text(data.reason);
								$(".stuMessagePage").prepend($stuFail);
								$idInput.parents(".form-group").removeClass("has-success").addClass("has-error").focus();
								$personNumberInput.parents(".form-group").removeClass("has-success").addClass("has-error");
							}else{
								$("#stuFail").remove();
								applyView();
							}
						}
					});
				}
			});
		});
	}
	//改变dor Table页样式
	function changeDorTablePage(type){
		$userMessagePage.hide();
		$stuMessagePage.hide();
		$dorMessagePage.show();
		$(".tablePage .dorPage").parent().addClass("active").siblings().removeClass("active");
	}
	//获取dor 页面事件
	function getDorEvent(){
		$(".dorPage").off("click").on("click",function(){
			changeDorTablePage(1);
		});
	}
});