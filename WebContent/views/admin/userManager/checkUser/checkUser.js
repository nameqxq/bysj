define(["base","common"],function(base,common){
	function CheckUser(){
		this.url = window.location.pathname;
	} 
	
	CheckUser.prototype = {
		init : function(setting){
			this.$userTab = $("#userTab",setting);
			this.$userPage = $("#userPage",setting);
			this.$stuTab = $("#stuTab",setting);
			this.$stuPage = $("#stuPage",setting);
			
			this.$tipBox1 = $(".tipBox1",setting);
			this.$tipBox2 = $(".tipBox2",setting);
			this.$tipBox3 = $(".tipBox3",setting);
			this.getTabOnClick("user");
			this.getCommonEvent();
		},
		getTabOnClick : function(type){
			var that = this;
			if(type=="user"){
				that.$userTab.addClass("active").off("click").siblings().removeClass("active");
				that.$stuTab.off("click").on("click",function(){
					that.getTabOnClick("stu");
				});
			}else{
				that.$stuTab.addClass("active").off("click").siblings().removeClass("active");
				that.$userTab.off("click").on("click",function(){
					that.getTabOnClick("user");
				});
			}
			that.showPage(type);
		},
		showPage:function(type){
			var that = this;
			if(type=="user"){
				that.$userPage.show();
				that.$stuPage.hide();
			}else{
				that.$userPage.hide();
				that.$stuPage.show();
			}
			that.doPaging(type,{});
			that.getPageEvent(type);
		},
		doPaging : function(type,info){
			var that = this;
			var inType = "";
			var sqlName = "";
			var renderView = null;
			selector = "";
			if(type=="user"){
				info.orderBy = "create_date desc";
				inType = "User";
				sqlName = "UserMapper.selectForLike";
				selector = "#userPage .paging"; 
				renderView = function(data){
					var $html = $($("#userPageTr").render(data));
					$(".userPageTbody").empty().append($html);
					$html.children('[data-type="userName"]').off("click").on("click",function(){
						var userName = $(this).attr("data-info");
						var info = {};
						info.userName = userName
						info.orderBy = "login_Date desc"
						var renderViewForUserLog = function(data){
							console.log(data)
							var html = $("#TipBoxTr").render(data);
							$(".tipBoxTbody",that.$tipBox3).empty().append(html);
							base.showTipBox(".tipBox3");
						}
						base.paging(info,"UserLog","UserLogMapper.select",5,1,renderViewForUserLog,false,".tipBox3 .paging");
					});
				}
			}else{
				info.orderBy = "id";
				inType = "Student";
				sqlName = "StudentMapper.selectForLike";
				selector = "#stuPage .paging"; 
				renderView = function(data){
					var $html = $($("#stuPageTr").render(data));
					$(".stuPageTbody").empty().append($html);
				}
			}
			base.paging(info,inType,sqlName,5,1,renderView,true,selector);
		},
		getPageEvent : function(type){
			var that = this;
			var $search = null;
			var $form = null;
			if(type=="user"){
				$search = $(".search",that.$userPage);
				$form = $("form",that.$userPage);
			}else{
				$search = $(".search",that.$stuPage);
				$form = $("form",that.$stuPage);
			}
			$form[0].reset();
			$form.off("submit").on("submit",function(){
				return false;
			})
			$(".search",$form).off("click").on("click",function(){
				var info = $form.form2json();
				that.doPaging(type,info);
			});
		},
		getCommonEvent: function(){
			var that = this;
			$(".entering").off("click").on("click",function(){
				base.showTipBox(".tipBox1");
				$(".stuUpLoad,.dorUpLoad",that.$tipBox1).off("click").on("click",function(){
					base.hideTipBox(".tipBox1");
					base.showMask();

					document.body.onfocus = roar
					
					function roar(){
					    if($("[type='file']")[0].value.length==0&&$("[type='file']")[1].value.length==0)
					    	base.hideMask();
					    document.body.onfocus = null
					}
					$(this).next().click();
				});
				
				$("[type='file']",that.$tipBox1).off("change").on("change",function(){
					var $this = $(this)
					var acceptedTypes = {
						'application/vnd.ms-excel': true,
						'application/x-xls': true,
						'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': true
					};
					var file = this.files[0];
					if(file==null){
						base.hideMask();
						base.hideTipBox(".tipBox1");
						return;
					}
					var fileFor = $this.attr("data-type");
					$this.val("");
					if (acceptedTypes[file.type] === true){
						base.Communicator.upload("CheckUserService.enteringData",{fileFor:fileFor},'map',{
							onSuccess : function(data){
								if(data.code == common.SUCCESS){
									base.hideTipBox(".tipBox1");
									base.a(data.data,null,function(flag){
										location.reload();
									});
								}else{
									base.a(data.reason,"error")
									base.hideTipBox(".tipBox1");
									base.log(data.reason)
								}
								base.hideMask();
							}
						},false,[file]);
					}else{
						base.hideMask();
						base.hideTipBox(".tipBox1");
						base.a("请上传.xls或者.xlsx格式的Excel文件！","error");
					}
				});
			});
			$(".downLoad").off("click").on("click",function(){
				base.showTipBox(".tipBox2");
				$(".stuDownLoad",that.$tipBox2).off("click").on("click",function(){
					base.postAction("/common/download",{fileName:'templateForStu'});
				});
				$(".dorDownLoad",that.$tipBox2).off("click").on("click",function(){
					base.postAction("/common/download",{fileName:'templateForDor'});
				});
			});
		},
	}
	
	var checkUser = new CheckUser();
	checkUser.init($("body"));
})