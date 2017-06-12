define(["base","common"],function(base,common){
	
	function Announcement(){
		this.url = window.location.pathname;
	}
	
	Announcement.prototype = {
			init : function(setting){
				this.$addTab = $("#addTab",setting);
				this.$logTab = $("#logTab",setting);
				this.$addPage = $(".addPage",setting);
				this.$logPage = $(".logPage",setting);
				
				this.getTabOnClick("add");
			},
			getTabOnClick : function(type){
				var that = this;
				if(type=="add"){
					that.$addTab.addClass("active").off("click").siblings().removeClass("active");
					that.$logTab.off("click").on("click",function(){
						that.getTabOnClick("log");
					});
				}else{
					that.$logTab.addClass("active").off("click").siblings().removeClass("active");
					that.$addTab.off("click").on("click",function(){
						that.getTabOnClick("add");
					});
				}
				that.showPage(type);
			},
			showPage : function(type){
				var that = this;
				if(type=="add"){
					that.$addPage.show();
					that.$logPage.hide();
					var $form = $("form",that.$addPage);
					//数据校验
					$form.bootstrapValidator({
						feedbackIcons: {
				            valid: 'glyphicon glyphicon-ok',
				            invalid: 'glyphicon glyphicon-remove',
				            validating: 'glyphicon glyphicon-refresh'
				        },
				        fields: {
				           title: {
				               validators: {
				                   notEmpty: {
				                       message: '标题不能为空！'
				                   },
				                   stringLength: {
				                       min: 4,
				                       max: 20,
				                       message: '标题只能为4-20个字符。。'
				                   },
				               }
				           },
				           context: {
				               validators: {
				                   notEmpty: {
				                       message: '内容不能为空！'
				                   },
				                   stringLength: {
				                       min: 20,
				                       max: 500,
				                       message: '内容请尽量保持20~500个字符...'
				                   },
				               }
				           }
				        }
					});
					//为提交添加点击事件
					$(".btn",$form).off("click").on("click",function(){
						var v = $form.data('bootstrapValidator');
						v.validate();
						var flag = v.isValid()
						if(flag){
							base.showMask();
							var info = {};
							info.title = $("[name='title']",that.$form).val();
							info.context = $("[name='context']",that.$form).val();
							base.Communicator.send("AnnouncementService.add",info,"Announcement",{
								onSuccess: function(data){
									if(data.code==common.SUCCESS){
										base.a(data.data);
										$form.fadeOut(2000,function(){
											$form[0].reset();
											$form.fadeIn(2000);
										});
									}else
										base.a(data.reason,"error");
									base.hideMask();
								}
							});
							v.resetForm();
						}
					});
				}else{
					that.$logPage.show();
					that.$addPage.hide();
					var getQueryInfo = function(){
						var info = {};
						info.title = $("[name='title']",$form).val();
						info.context = $("[name='context']",$form).val();
						info.createPerson = $("[name='createPerson']",$form).val();
						info.orderBy = "alive_flag desc ,create_date desc";
						return info;
					}
					var $form = $("form",that.$logPage);
					//查询
					$(".search",$form).off("click").on("click",function(){
						that.doPaging(type,getQueryInfo());
					});
					that.doPaging(type,getQueryInfo());
				}	
			},
			doPaging: function(type,info){
				var that = this;
				if(type=="log"){
					var applyView = function(data){
						var $html = $($("#logPageTr").render(data));
						var $tbody = $("tbody",that.$logPage);
						$tbody.empty().append($html)
						$html.find(".showContext").off('click').on("click",function(){
							var $this = $(this);
							var guid = $this.parent().children(".title").attr("data-info");
							base.postAction("/common/announcementContext/"+guid+"/context");
						});
						//作废按钮
						$(".zf",$html).off("click").on("click",function(){
							var $this = $(this);
							base.p("确认作废？",null,function(flag,str){
								if(flag){
									var guid = $this.parents("tr").children(".title").attr("data-info");
									var info = {};
									info.guid = guid;
									info.remark = str;
									base.Communicator.send("AnnouncementService.cancellation",info,"Announcement",{
										onSuccess : function(data){
											if(data.code==common.SUCCESS){
												$this.off("click").removeClass("zf").addClass("showContext");
												$this.find("a").text(str).attr("title",str);
												base.a(data.data);
											}
										}
									});
								}
							},"作废是不可逆的，请输入作废理由！","text")
							
						});
					}
					base.paging(info,"Announcement","AnnouncementMapper.selectLike",10,1,applyView,true,".logPage .paging")
				}
			}
	}
	var announcement = new Announcement();
	announcement.init($("body"));
})