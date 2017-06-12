
define("views/common/announcementContext/announcementContext",["base"],function(base){
	
	var pathname = window.location.pathname;
	var guid = pathname.split("/")[3];
	base.getJsonObject({guid:guid},"Announcement","AnnouncementMapper.selectByGuid").done(function(data){
		if(data.code="11111"){
			var realDate = data.data;
			if(realDate){
				$(".title").text(realDate.title);
				$(".context").append($("<span/>").text(realDate.context));
				$(".createDate").text(realDate.createDate);
			}
		}
	});
	
});