/**
 * 用来引入适当的topLine以及leftTree
 */
define(["base","bootstrapvalidator","jsrender"],function(base,v,jd){
	var userName,
	buildingNumber,
	layerNumber,
	dormitoryNumber,
	phoneNumber,
	email ,
	createDate ,
	age,
	usertypes,
	studentId;
	base.doAjax("/onlineUserInfo",{},{
		onSuccess : function(data){
			var user = data.data;
			if(!user)
				return;
			userName = user.userName;
			phoneNumber = user.phoneNumber
			email = user.email
			createDate = user.createDate
			age = user.age
			usertypes = user.usertypes
			if(user.student)
				studentId = user.student.id;
			if(user.dormitory){
				dormitoryNumber = user.dormitory.dormitoryNumber
				buildingNumber = user.dormitory.buildingNumber
				layerNumber = user.dormitory.layerNumber
			}
			renderTopline();
		}
	},"get",false);
	function renderTopline(){
		$.get("/common/html/topLine.html",appendTopLine);
		function appendTopLine(data){
			var $topLine = $('<div/>').html(data).find('#topLine');
			$topLine.find("#userName").text(userName);
			$topLine.find("#logout").on("click",function(){
				base.c("确认要退出么？",null,function(flag){
					if(flag){
						base.doAjax("/logout",{},{
							onSuccess:function(data){
								if(data.code=="00000")
									base.a(data.reason,"error");
								else
									window.location.href="/index";
							}
						});
					}
				});
			});
			$('#topLine').append($topLine.children());
		}
		var leftTree = (usertypes=='10'?"/common/html/stuLeftTree.html":"/common/html/adminLeftTree.html");
		$('#leftTree').empty().load(leftTree,appendLeftTree);
		function appendLeftTree(){
			var pathname = window.location.pathname;
			base.getJsonObject({url:pathname},"UrlCode","UrlCodeMapper.select").done(function(data){
				var urlCode = base.parseJsonObject(data);
					$('#leftTree').find('#'+urlCode.code).attr("href","javaScript:void(0)").parent().addClass("active").siblings().removeClass("acctive");
			});
		}
		$('body').off("unload").on("unload",function(){
			base.doAjax("/logout",{},{
				onSuccess:function(data){
					if(data.code=="00000")
						a(data.reason);
					else
						window.location.href="/index";
				}
			});
		});
	}
	return{
		userName : userName,
		buildingNumber : buildingNumber,
		layerNumber : layerNumber,
		dormitoryNumber : dormitoryNumber,
		phoneNumber : phoneNumber,
		email : email,
		createDate :createDate,
		age : age,
		usertypes : usertypes,
		studentId : studentId,
		SUCCESS : '11111',
		FIAL : '00000'
	}
});
	