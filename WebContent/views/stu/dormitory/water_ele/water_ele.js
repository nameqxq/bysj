define(["base","common"],function(base,common){

	function Water_ele(){
		this.url = window.location.pathname;
	}
	
	Water_ele.prototype = {
		init : function(settings){
			
			this.$type2Div = settings.find(".type2").hide();
			
			this.$type1Div = settings.find(".type1");
			
			this.$type2Left = settings.find(".type2 .leftBtn");
			
			this.$type2Right = settings.find(".type2 .rightBtn");
			
			this.$type2Back = settings.find(".type2 .backBtn");
			
			this.$type2PayBack = settings.find(".type2 .payBack");
			
			this.$type2Pay = settings.find(".type2 .pay");
			
			this.$type2PayBtn = settings.find(".type2 .payBtn");
			
			this.$type2PayPage = settings.find(".type2 .payPage");
			
			this.$type2ShowPage = settings.find(".type2 .showPage");
			
			this.getType1OnClick();
		},
		getType1OnClick : function(){
			var water_ele = this;
			this.$type1Div.find(".check").off("click").on("click",function(){
				var thisId = $(this).parents(".thumbnail").attr("id");
				if(thisId=="eleDiv"){
					selector = ".eleMessage";
					water_ele.$type2Left.text("用电记录");
					water_ele.$type2Right.text("收费标准");
				}else if(thisId=="waterDiv"){
					selector = ".waterMessage";
					water_ele.$type2Left.text("用水记录");
					water_ele.$type2Right.text("收费标准");
				}else{
					selector = ".moneyMessage";
					water_ele.$type2Left.text("扣费记录");
					water_ele.$type2Right.text("充值记录");
				}
				if (common.dormitoryNumber){
					water_ele.$type1Div.fadeOut(3000,function(){
						water_ele.$type2Div.fadeIn(3000);
						water_ele.$type2Div.find(selector).fadeIn(3000)/*.siblings(".showDiv").hide()*/;
						water_ele.paging(selector,"left");
					});
					water_ele.getType2OnClick(selector);
				}else{
					base.a("请先绑定学生信息！","warning");
				}
			});
		},
		getType2OnClick : function(selector){
			var water_ele = this;
			water_ele.$type2Left.off("click").on("click",function(){
				$(this).parent().addClass("active").siblings().removeClass("active");
				water_ele.paging(selector,"left");
			});
			water_ele.$type2Right.off("click").on("click",function(){
				$(this).parent().addClass("active").siblings().removeClass("active");
				water_ele.paging(selector,"right");
			});
			water_ele.$type2PayBack.off("click").on("click",function(){
				water_ele.$type2PayPage.fadeOut(3000,function(){
					water_ele.$type2ShowPage.fadeIn(3000);
				});
			});
			water_ele.$type2Pay.off("click").on("click",function(){
				water_ele.$type2ShowPage.fadeOut(3000,function(){
					water_ele.$type2PayPage.fadeIn(3000);
				});
			});
			water_ele.$type2PayBtn.off("click").on("click",function(){
				var $this = $(this);
				var rmb;
				if($this.hasClass("inForm")){
					var $input = $this.prev().children("input");
					rmb = $input.val();
					rmb = parseInt(rmb);
					if(!rmb||rmb<1){
						$input.focus().val("");
						return false;
					}
				}else{
					rmb=$this.attr("data-info");
				}
				base.c("本次充值"+rmb+"元,是否继续？",null,function(flag){
					if(flag){
						base.Communicator.send("WaterEleService.pay",{payMoney:rmb},"MoneyLog",{
							onSuccess : function(data){
								if(data.code==common.SUCCESS){
									base.a("本次成功充值"+data.data+"元");
								}else{
									base.a(data.reason,"error");
									base.log(data.reason);
								}
								base.hideMask();
							},
						});
					}
				});
			});
			water_ele.$type2Back.off("click").on("click",function(){
				water_ele.$type2Div.fadeOut(3000,function(){
					water_ele.$type2Div.find(selector).hide();
					water_ele.$type2Left.parent().addClass("active");
					water_ele.$type2Right.parent().removeClass("active");
					water_ele.$type1Div.fadeIn(3000);
				});
			});
		},
		paging : function(selector,type){
			base.getAnnouncement(".announcement",5);
			var inType = "";
			var sqlName = "";
			var tr = "";
			var info = {};
			var orderBy = "";
			if(selector.indexOf("ele")>=0){
				if(type=="left"){
					inType = "EleLog";
					sqlName = "EleLogMapper.select";
					tr = '<tr><td data-type="dormitoryNumber"/><td data-type="eleNumber"/><td data-type="enteringTime"/><td data-type="enteringPerson"/></tr>';
					orderBy = "entering_time desc";
				}else{
					inType = "ElePrice";
					sqlName = "ElePriceMapper.select";
					tr = '<tr><td data-type="year"/><td data-type="month"/><td data-type="elePrice"/></tr>';
					orderBy = "year,month desc";
				}
			}else if(selector.indexOf("water")>=0){
				if(type=="left"){
					inType = "WaterLog";
					sqlName = "WaterLogMapper.select";
					tr = '<tr><td data-type="dormitoryNumber"/><td data-type="waterNumber"/><td data-type="enteringTime"/><td data-type="enteringPerson"/></tr>';
					orderBy = "entering_time desc";
				}else{
					inType = "ElePrice";
					sqlName = "ElePriceMapper.select";
					tr = '<tr><td data-type="year"/><td data-type="month"/><td data-type="waterPrice"/></tr>';
					orderBy = "year,month desc";
				}
			}else if(selector.indexOf("money")>=0){
				inType = "MoneyLog";
				sqlName = "MoneyLogMapper.select";
				tr = '<tr><td data-type="dormitoryNumber"/><td data-type="payDate"/><td data-type="payUser"/><td data-type="payMoney"/><td data-type="money"/></tr>';
				orderBy = 'pay_date desc';
				if(type=="left"){
					info.payType = '00';
				}else{
					info.payType = '10';
				}
			}
			info.dormitoryNumber = common.dormitoryNumber;
			info.orderBy = orderBy;
			base.paging(info,inType,sqlName,10,1,applyView,true,selector+" .paging")
			function applyView(data){
				if(data.code=="00000"){
					base.a(data.reason)
					base.log(data)
				}
				var $tbody = null;
				if(type=="left"){
					$tbody = $(selector+" .leftTable tbody").empty();
					$(selector+" .leftTable").show();
					$(selector+" .rightTable").hide();
				}else{
					$tbody = $(selector+" .rightTable tbody").empty();
					$(selector+" .leftTable").hide();
					$(selector+" .rightTable").show();
				}
				var dataArr = data.data;
//				var tr = '<tr><td data-type="dormitoryNumber"/><td data-type="eleNumber"/><td data-type="enteringTime"/><td data-type="enteringPerson"/></tr>';
				if(dataArr==null||dataArr.length==0){
					$tbody.append("<tr>未查询到！</tr>");
				}
				$.each(dataArr,function(key,val){
					var $tr = $(tr);
					$.each(val,function(k,v){
						$tr.children('[data-type='+k+']').text(v==""?"暂无记录":v);
					});
					$tbody.append($tr);
				});
			}
		}
	}

	var water_ele = new Water_ele();
	water_ele.init($("body"));
	
})