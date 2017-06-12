define(["base","common"],function(base,common){
	
	function Water_ele(){
		this.url = window.location.pathname;
	}
	
	Water_ele.prototype = {
			init : function(setting){
				this.$dormitoryTab = $("#dormitoryTab",setting);
				this.$moneyTab = $("#moneyTab",setting);
				this.$dormitoryPage = $("#dormitoryPage",setting);
				this.$moneyPage = $("#moneyPage",setting);
				this.getTabOnClick("dor");
			},
			getTabOnClick : function(type){
				var that = this;
				if(type=="dor"){
					that.$dormitoryTab.addClass("active").off("click").siblings().removeClass("active");
					that.$moneyTab.off("click").on("click",function(){
						that.getTabOnClick("money");
					});
				}else{
					that.$moneyTab.addClass("active").off("click").siblings().removeClass("active");
					that.$dormitoryTab.off("click").on("click",function(){
						that.getTabOnClick("dor");
					});
				}
				that.showPage(type);
			},
			//页面展示
			showPage : function(type){
				var that = this;
				if(type=="dor"){
					that.$dormitoryPage.show();
					that.$moneyPage.hide();
				}else{
					that.$dormitoryPage.hide();
					that.$moneyPage.show();
				}
				that.doPaging(type,{});
				that.getPageOnClick(type);
			},
			//获取页面点击事件
			getPageOnClick : function(type){
				var that = this;
				if(type=="dor"){
					var $search = $(".search",that.$dormitoryPage);
					var $form = $("form",that.$dormitoryPage);
					$form.bootstrapValidator({
						feedbackIcons: {
				            valid: 'glyphicon glyphicon-ok',
				            invalid: 'glyphicon glyphicon-remove',
				            validating: 'glyphicon glyphicon-refresh'
				        },
				        fields: {
				        	dormitoryNumber : {
				        		validators: {
				        			regexp: {
					                       regexp: /^(\d{2}#){2}\d{2}$/,
					                       message: '宿舍号格式不正确。'
					                   },
				        		}
				        	},
				        	buildingNumber : {
				        		validators: {
				        			regexp: {
					                       regexp: /^\d{2}$/,
					                       message: '宿舍楼号格式错误。'
					                   },
				        		}
				        	},
				        	layerNumber : {
				        		validators: {
				        			regexp: {
					                       regexp: /^\d{2}$/,
					                       message: '宿舍楼层号格式错误。'
					                   },
				        		}
				        	},
				        }
					});
					$form.off("submit").on("submit",function(){
						return false;
					})
					$(".search",$form).off("click").on("click",function(){
						var v = $form.data('bootstrapValidator');
						v.validate();
						var flag = v.isValid();
						if(flag){
							var info = $form.form2json();
							that.doPaging(type,info);
							v.resetForm();
						}
					});
					//模板下载
					$(".downLoad",that.$dormitoryPage).off("click").on("click",function(){
						base.postAction("/common/download",{fileName:'template'});
					});
					//信息录入
					$(".entering",that.$dormitoryPage).off("click").on("click",function(){
						base.showMask();
						document.body.onfocus = roar;
						
						var $upload = $(".uploadExcel",that.$dormitoryPage);
						$upload.val("");

						function roar(){
							if($upload[0].value.length==0)
								base.hideMask();
							document.body.onfocus = null
						}
						$(this).next().click();
						
					});
					
					 
					$(".uploadExcel",that.$dormitoryPage).off("change").on("change",function(){
						var $this = $(this)
						var acceptedTypes = {
							'application/vnd.ms-excel': true,
							'application/x-xls': true,
							'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': true
						};
						var file = this.files[0];
						if(file==null){
							base.hideMask();
							return;
						}
//						$this.val("")
						if (acceptedTypes[file.type] === true){
							base.Communicator.upload("WaterEleService.enteringData",null,null,{
								onSuccess : function(data){
									if(data.code == common.SUCCESS){
										base.a(data.data,null,function(flag){
											location.reload();
										});
									}else{
										base.a(data.reason,"error")
										base.log(data.reason)
									}
									base.hideMask();
								}
							},false,[file]);
						}else{
							base.hideMask();
							base.a("请上传.xls格式或者.xlsx的Excel文件！","error");
						}
					});
					
					//短信提醒
					$(".sendMsg",that.$dormitoryPage).off("click").on("click",function(){
						base.c("短信提醒",null,function(flag){
							if(!flag)
								return;
							base.showMask();
							base.Communicator.send("WaterEleService.sendMsg",{min:"20"},"map",{
								onSuccess:function(data){
									if(data.code==common.SUCCESS)
										base.a(data.data);
									else
										base.a(data.reason,"error");
									base.hideMask();
								}
							});
						},"将对宿舍余额不足20元的绑定用户进行短信提醒，是否继续？")
					});
				}else{
					var now = new Date();
					var nowYear = now.getFullYear();
					var nowMonth = now.getMonth()+1;
					
					$(".add",that.$moneyPage).off("click").on("click",function(){
						var $moneyPageTbody = $(".moneyPageTbody");
						if($("[data-type='add']",$moneyPageTbody).length!=0){
							base.a("请先完成添加！","warning");
							return;
						}
						
						var $nextTr = $("."+nowYear+(nowMonth+1),$moneyPageTbody);
						var $nowTr = $("."+nowYear+nowMonth,$moneyPageTbody);
						
						var data = {};
						data.year = nowYear;
						if($nowTr.length==0){
							data.month = nowMonth;
						}else{
							data.month = nowMonth+1;
						}
						if($nextTr.length!=0){
							base.a("下月费用信息已添加，请勿重复添加！","warning");
							return;
						}						
						var $html = $($("#moneyPageTrAdd").render(data));
						$moneyPageTbody.append($html);
						
						$("a",$html).off("click").on("click",function(){
							base.showMask();
							var info = {};
							var elePrice = base.trimString($(".ele",$html).val());
							var waterPrice = base.trimString($(".water",$html).val());
							if(elePrice==""||waterPrice==""){
								base.a("价格不能为空！","error");
								base.hideMask();
								return;
							}
							if(Number(elePrice)<0||Number(waterPrice)<0){
								base.a("价格不能小于0！","error");
								base.hideMask();
								return;
							}
							info.elePrice = elePrice;
							info.waterPrice = waterPrice;
							info.year = nowYear;
							info.month = $html.children(".month").text();
							base.Communicator.send("WaterEleService.addElePrice",info,"ElePrice",{
								onSuccess : function(data){
									if(data.code==common.SUCCESS){
										base.a(data.data,null,function(){
											location.reload();
										});
									}else{
										base.a(data.reason,"error");
									}
									base.hideMask();
								}
							});
						});
						
					});
				}
			},
			//数据查询
			doPaging : function(type,info){
				var that = this;
				if(type=="dor"){
					var renderView = function (data){
						var $dormitoryPageTbody = $(".dormitoryPageTbody");
						var html = $("#dormitoryPageTr").render(data);
						$dormitoryPageTbody.empty().append(html);
						$dormitoryPageTbody.children().children().off("click").on("click",function(){
							var $this = $(this);
							var $tipBoxTable = $(".tipBox table");
							var $th = $("th",$tipBoxTable);
							var dataType = $this.attr("data-type");
							var info = {};
							info.dormitoryNumber = $this.siblings(".dormitoryNumber").text();
							var sqlName = "";
							var inType = "";
							if(dataType=="water"){
								inType = "WaterLog";
								sqlName = "WaterLogMapper.select";
								info.orderBy = "entering_Time desc";
								$th.eq(0).text("宿舍号");
								$th.eq(1).text("水表读数");
								$th.eq(2).text("录入时间");
								$th.eq(3).text("录入人");
							}else if(dataType == "ele"){
								inType = "EleLog";
								sqlName = "EleLogMapper.select";
								info.orderBy = "entering_Time desc";
								$th.eq(0).text("宿舍号");
								$th.eq(1).text("电表读数");
								$th.eq(2).text("录入时间");
								$th.eq(3).text("录入人");
							}else if(dataType == "money"){
								inType = "MoneyLog";
								sqlName = "MoneyLogMapper.select";
								info.orderBy = "pay_Date desc";
								$th.eq(0).text("宿舍号");
								$th.eq(1).text("金额");
								$th.eq(2).text("录入时间");
								$th.eq(3).text("类型");
							}else{
								inType = "Dormitory";
								info.dormitoryNumber = $this.text();
								sqlName = "UserMapper.selectToDor";
								info.orderBy = "create_date desc";
								$th.eq(0).text("用户名");
								$th.eq(1).text("手机号");
								$th.eq(2).text("学号");
								$th.eq(3).text("创建时间");
							}
							var renderViewForTipBox = function(data){
								if(data.data&&data.data.length!=0)
									data.type=dataType;
								else
									data.type='no';
								var tipBoxTr = $("#tipBoxTr").render(data);
								$("tbody",$tipBoxTable).empty().append(tipBoxTr);
								base.showTipBox(".tipBox")
							} 
							//水电钱点击事件
							base.paging(info,inType,sqlName,5,1,renderViewForTipBox,false,".tipBox .paging");
						});
						
						
						/*base.showTipBox(".tipBox");*/
					}
					base.paging(info,"Dormitory","DormitoryMapper.select",10,1,renderView,true,"#dormitoryPage .paging");
				}else{
					
					var changeHleper = function($nowTr,nowYear,nowMonth){
						var $epTd = $nowTr.children(".elePrice");
						var $wpTd = $nowTr.children(".waterPrice");
						var epTdHtml = $("#moneyPageTrInput").render({type:"ele",data:$epTd.children("span").text()});
						var wpTdHtml = $("#moneyPageTrInput").render({type:"water",data:$wpTd.children("span").text()});
						$epTd.append(epTdHtml);
						$wpTd.append(wpTdHtml);

						//当前月份，保存
						var $saveChange = $("<a class='saveChange'>保存</a>").hide();
						//为当前月份 修改 添加点击事件
						var $change = $("a",$nowTr).addClass("change").off("click").on("click",function(){
							$("span",$nowTr).hide();
							$("input",$nowTr).show();
							$(this).hide();
							$saveChange.show();
						});
						$wpTd.next().append($saveChange);
						//当前月份，保存点击事件
						$saveChange.off("click").on("click",function(){
							base.c("是否保存修改？",null,function(flag){
								if(!flag){
									$("span",$nowTr).show();
									$("input",$nowTr).hide();
									$saveChange.hide();
									$change.show();
									return;
								}
								base.showMask();
								$("span",$nowTr).show();
								$("input",$nowTr).hide();
								$saveChange.hide();
								$change.show();
								var info = {};
								var elePrice = base.trimString($(".ele",$nowTr).val());
								var waterPrice = base.trimString($(".water",$nowTr).val());
								if(elePrice==""||waterPrice==""){
									base.a("价格不能为空！","error");
									base.hideMask();
									return;
								}
								if(Number(elePrice)<0||Number(waterPrice)<0){
									base.a("价格不能小于0！","error");
									base.hideMask();
									return;
								}
								info.elePrice = elePrice;
								info.waterPrice = waterPrice;
								info.year = nowYear;
								info.month = nowMonth;
								base.Communicator.send("WaterEleService.updateElePrice",info,"ElePrice",{
									onSuccess : function(data){
										if(data.code==common.SUCCESS){
											base.a(data.data);
											$epTd.children("span").text(elePrice);
											$wpTd.children("span").text(waterPrice);
										}else{
											base.a(data.reason,"error");
										}
										base.hideMask();
									}
								});
							});
						});
					};
					
					var renderView = function(data){
						var $moneyPageTbody = $(".moneyPageTbody");
						var html = $("#moneyPageTr").render(data);
						$moneyPageTbody.empty().append(html);
						
						var now = new Date();
						var nowYear = now.getFullYear();
						var nowMonth = now.getMonth()+1;
						var $nowTr = $("."+nowYear+nowMonth,$moneyPageTbody);
						var $nextTr = $("."+nowYear+(nowMonth+1),$moneyPageTbody);
						
						//为所有  修改 添加事件
						$("a",$moneyPageTbody).off("click").on("click",function(){
							base.a("只能修改本月或下月水电收费标准！","warning");
						});
						changeHleper($nowTr,nowYear,nowMonth);
						if($nextTr.length==1)
							changeHleper($nextTr,nowYear,nowMonth+1);
					}
					var info = {}
					info.orderBy = "year desc, month desc";
					base.paging(info,"ElePrice","ElePriceMapper.select",10,1,renderView,true,"#moneyPage .paging");
				}
			}
			
	}
	
	var waterEle = new Water_ele();
	waterEle.init($("body"));
})