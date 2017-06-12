define(["base","common"],function(base,common){
	function Repair(){
		this.url = window.location.pathname;
	}
	
	Repair.prototype = {
			init : function(setting){
				this.$pendingTab = $("#pendingTab",setting);
				this.$processedIngTab = $("#processedIngTab",setting);
				this.$processedTab = $("#processedTab",setting);
				this.$pendingPage = $(".pendingPage",setting);
				this.$processedIngPage = $(".processedIngPage",setting);
				this.$processedPage = $(".processedPage",setting);
				this.$tipBox1 = $(".tipBox1",setting)
				this.getTabOnClick("pending");
			},
			getTabOnClick : function(type){
				var that = this;
				if(type=="pending"){
					that.$pendingTab.addClass("active").off("click").siblings().removeClass("active");
					that.$processedTab.off("click").on("click",function(){
						that.getTabOnClick("processed");
					});
					that.$processedIngTab.off("click").on("click",function(){
						that.getTabOnClick("processedIng");
					});
				}else if(type=="processed"){
					that.$processedTab.addClass("active").off("click").siblings().removeClass("active");
					that.$pendingTab.off("click").on("click",function(){
						that.getTabOnClick("pending");
					});
					that.$processedIngTab.off("click").on("click",function(){
						that.getTabOnClick("processedIng");
					});
				}else{
					that.$processedIngTab.addClass("active").off("click").siblings().removeClass("active");
					that.$pendingTab.off("click").on("click",function(){
						that.getTabOnClick("pending");
					});
					that.$processedTab.off("click").on("click",function(){
						that.getTabOnClick("processed");
					});
				}
				that.showPage(type);
			},
			//页面展示
			showPage : function(type){
				var that = this;
				if(type=="pending"){
					that.$pendingPage.show();
					that.$processedPage.hide();
					that.$processedIngPage.hide();
				}else if(type=="processed"){
					that.$pendingPage.hide();
					that.$processedPage.show();
					that.$processedIngPage.hide();
				}else{
					that.$pendingPage.hide();
					that.$processedPage.hide();
					that.$processedIngPage.show();
				}
				that.doPaging(type,{orderBy:"modifi_Date desc"});
				that.getPageOnClick(type);
			},
			//获取页面点击事件
			getPageOnClick : function(type){
				var that = this;
				if(type=="pending"){
					var $form = $("form",that.$tipBox1);
					$form.bootstrapValidator({
						feedbackIcons: {
				            valid: 'glyphicon glyphicon-ok',
				            invalid: 'glyphicon glyphicon-remove',
				            validating: 'glyphicon glyphicon-refresh'
				        },
				        fields: {
				        	handlerPerson : {
				        		validators: {
				        			notEmpty : {
				        				message: '维修人不能为空！'
				        			},
				        			stringLength: {
					                       min: 2,
					                       max: 6,
					                       message: '维修人姓名只能为3-6个字符！'
					                },
				        		}
				        	},
				        	handlerPhone : {
				        		validators: {
				        			notEmpty : {
				        				 message: '手机号不能为空。'
				        			},
				        			regexp: {
					                       regexp: /^1\d{10}$/,
					                       message: '手机号格式错误。'
					                },
				        		}
				        	},
				        	repairDate : {
				        		validators: {
				        			notEmpty : {
				        				 message: '预计维修日期不能为空。'
				        			},
				        			regexp: {
					                       regexp: /^\d{4}-\d{2}-\d{2}$/,
					                       message: '日期格式错误(xxxx-xx-xx)。'
					                },
				        		}
				        	},
				        }
					});
					$form.off("submit").on("submit",function(){
						return false;
					})
					$(".submit",$form).off("click").on("click",function(){
						var v = $form.data('bootstrapValidator');
						v.validate();
//						base.log($form.find("[name='repairDate']").val())
						var date = new Date($form.find("[name='repairDate']").val());
						var flag = v.isValid();
						if(flag){
							base.hideTipBox(".tipBox1");
							var info = $form.form2json();
							info.guid = that.$tipBox1.attr("data-info");
							v.resetForm();
							if(Date.parse(date)-Date.parse(new Date())<0){
								base.a("只能选择当前时间之后的时间！","warning");
								return;
							}
							base.Communicator.send("RepairService.pass",info,"RepairBill",{
								onSuccess : function(data){
									if(data.code==common.SUCCESS){
										base.a(data.data,null,function(){
											window.location.reload();
										});
									}
								}
							});
						}
					});
				}
			},
			//获取信息
			doPaging : function(type,info){
				var that = this;
				if(type=="pending"){
					var applyView1 = function(data){
						for(var i in data.data){
							var o = data.data[i];
							var picture = [];
							base.addEle2Arr(picture,o.image1);
							base.addEle2Arr(picture,o.image2);
							base.addEle2Arr(picture,o.image3);
							o.picture = JSON.stringify(picture);
						}
						var $tbody = $(".pendingPageTbody",that.$pengdingPage);
						var $html = $($("#pendingPageTr").render(data));
						$tbody.empty().append($html);
						$html.find("[data-type='title']").on("click",function(){
							var guid =  $(this).prev().attr("data-info");
							base.postAction("/common/repairBillContext/"+guid+"/context");
						});
						//查看按钮
						$html.find("[data-type='picture']").on("click",function(){
							var picture = JSON.parse($(this).attr("data-info"));
							base.showRepairBillPicture(".tipBox2",picture);
							base.showTipBox(".tipBox2",null,500);
						});
						//通过按钮
						$html.find("[data-type='dispose'] .tg").on("click",function(){
							var guid =  $(this).parents("tr").children().eq(0).attr("data-info");
							that.$tipBox1.attr("data-info",guid);
							base.showTipBox(".tipBox1");
						});
						//拒绝按钮
						$html.find("[data-type='dispose'] .jj").on("click",function(){
							var $this = $(this);
							base.p("确认拒绝？",null,function(flag,str){
								if(flag){
									var guid = $this.parents("tr").children(".dormitoryNumber").attr("data-info");
									var info = {};
									info.guid = guid;
									info.remark = str;
									base.Communicator.send("RepairService.cancellation",info,"RepairBill",{
										onSuccess : function(data){
											if(data.code==common.SUCCESS){
												base.a(data.data,function(){
													window.location.reload();
												});
											}
										}
									});
								}
							},"拒绝是不可逆的，请输入拒绝理由！","text")
						});
					}
					info.status = "10";
					base.paging(info,"RepairBill","RepairBillMapper.select",10,1,applyView1,true);
				}else if(type=="processedIng"){
					var applyView2 = function(data){
						for(var i in data.data){
							var o = data.data[i];
							var picture = [];
							base.addEle2Arr(picture,o.image1);
							base.addEle2Arr(picture,o.image2);
							base.addEle2Arr(picture,o.image3);
							o.picture = JSON.stringify(picture);
							var status =o.status;
							o.status = status=="00"?"审核未通过":status=="10"?"待审核":status=="20"?"维修中":status=="30"?"已维修":"未知";
						}
						var $tbody = $(".processedIngPageTbody",that.$processedIngPage);
						var $html = $($("#processedIngPageTr").render(data));
						$tbody.empty().append($html);
						$html.find("[data-type='title']").on("click",function(){
							var guid =  $(this).prev().attr("data-info");
							base.postAction("/common/repairBillContext/"+guid+"/context");
						});
						//查看按钮
						$html.find("[data-type='picture']").on("click",function(){
							var picture = JSON.parse($(this).attr("data-info"));
							base.showRepairBillPicture(".tipBox2",picture);
							base.showTipBox(".tipBox2",null,500);
						});
					}
					info.status = "20"
					base.paging(info,"RepairBill","RepairBillMapper.select",10,1,applyView2,true);
				}else{
					var applyView3 = function(data){
						for(var i in data.data){
							var o = data.data[i];
							var picture = [];
							base.addEle2Arr(picture,o.image1);
							base.addEle2Arr(picture,o.image2);
							base.addEle2Arr(picture,o.image3);
							o.picture = JSON.stringify(picture);
							var status =o.status;
							o.status = status=="00"?"审核未通过":status=="10"?"待审核":status=="20"?"维修中":status=="30"?"已维修":"未知";
						}
						var $tbody = $(".processedPageTbody",that.$processedPage);
						var $html = $($("#processedPageTr").render(data));
						$tbody.empty().append($html);
						$html.find("[data-type='title']").on("click",function(){
							var guid =  $(this).prev().attr("data-info");
							base.postAction("/common/repairBillContext/"+guid+"/context");
						});
						//查看按钮
						$html.find("[data-type='picture']").on("click",function(){
							var picture = JSON.parse($(this).attr("data-info"));
							base.showRepairBillPicture(".tipBox2",picture);
							base.showTipBox(".tipBox2",null,500);
						});
					}
					info.status = ["00","30"];
					base.paging(info,"PageMap","RepairBillMapper.selectStatusIn",10,1,applyView3,true);
				}
			}
	}
	
	
	var repair = new Repair();
	repair.init($("body"));
})	