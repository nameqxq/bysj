var pathname = window.location.pathname;
var split = pathname.split('/');
var flag = split[2];
var endWith = split[split.length-1];
var	nowPath = "";
if(flag=="announcementContext")
	nowPath = "views/common/announcementContext/announcementContext";
else if(flag == "repairBillContext")
	nowPath = "views/common/repairBillContext/repairBillContext";
else
	nowPath = "views"+pathname+"/"+endWith;	
require.config({
	urlArgs: "bust=" +  (new Date()).getTime(),
    paths : {
    	"$" : "common/jQuery/jquery-3.1.1.min",
        "base" : "common/js/base",
        "common" : "common/js/common",
        "bootstrap" : "common/bootstrap-3.3.7-dist/js/bootstrap",
        "bootstrapvalidator" : "common/plug-in/bootstrapvalidator/js/bootstrapValidator",
//        "bootstrapvalidatorLanguage" : "common/plug-in/bootstrapvalidator/js/zh_CN",
        "jsrender" : "common/plug-in/jsrender/js/jsrender",
//        "query" : "common/plug-in/paging/js/query",
        "paging" : "common/plug-in/paging/js/paging",
        "BeAlert" : "common/plug-in/tipbox/BeAlert",
    },
});

//require(["bootstrapvalidator"]);
require([nowPath]);
