
define("views/common/repairBillContext/repairBillContext",["base","bootstrap"],function(base,b){
	
	var pathname = window.location.pathname;
	var guid = pathname.split("/")[3];
	base.getJsonObject({guid:guid},"RepairBill","RepairBillMapper.selectByGuid").done(function(data){
		if(data.code="11111"){
			var realData = data.data;
			if(realData){
				$(".title").text(realData.title);
				$(".context").append($("<span/>").text(realData.context));
				$(".createDate").text(realData.createDate);
				$(".personName").text(realData.personName);
				$(".phoneNumber").text(realData.phoneNumber);
				$(".dormitoryNumber").text(realData.dormitoryNumber);
				
				var status = realData.status;
				if(status=="00"){
					var $refuse = $(".refuse").show();
					$refuse.find(".remark").text(realData.remark);
					$refuse.find(".refuseDate").text(realData.refuseDate);
				}else if(status=="20"){
					var $processedIng = $(".processedIng").show();
					$processedIng.find(".handlerPerson").text(realData.handlerPerson);
					$processedIng.find(".handlerPhone").text(realData.handlerPhone);
					$processedIng.find(".repairDate").text(realData.repairDate);
				}else if(status=="30"){
					var $processedIng = $(".processedIng").show();
					$processedIng.find(".handlerPerson").text(realData.handlerPerson);
					$processedIng.find(".handlerPhone").text(realData.handlerPhone);
					$processedIng.find(".repairDate").text(realData.repairDate);
					var $processed = $(".processed").show();
					$processed.find(".remark").text(realData.remark);
					$processed.find(".realRepairDate").text(realData.realRepairDate);
					$processed.find(".modifiDate").text(realData.modifiDate);
				}
				var picture = [];
				base.addEle2Arr(picture,realData.image1);
				base.addEle2Arr(picture,realData.image2);
				base.addEle2Arr(picture,realData.image3);
				var len = picture.length;
				var $ol = $(".carousel-indicators");
				var $imageDiv = $(".carousel-inner")
				for(var i=0;i<len;i++){
					var $li = $('<li data-slide-to="'+i+'" data-target="#carousel-80483"/>')
					var $image = $('<div class="item"><img src="'+picture[i]+'" /></div>');
					$ol.append($li);
					$imageDiv.append($image);
					if(i==0){
						$li.addClass("active");
						$image.addClass("active");
					}
				}
				if(len!=0)
					$("#carousel-80483").show();
			}
		}
	});
	
});