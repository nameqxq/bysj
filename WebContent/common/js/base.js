define(["BeAlert","paging"],function(BeAlert,Paging){
	/** 返回一个去掉首尾空格的数字 */
	trimNumber=function (numStr){
		numStr = trimString(numStr);
		if(numStr==""){return 0;}
		else if(isNaN(parseFloat(numStr))){return 0;}
		else{return parseFloat(numStr);}
	};
	/** 返回一个去掉首尾空格的字符串 */
	trimString=function (str){
		return (str==undefined || str==null) ? "" : (""+str).trim();
	};


	//显示遮罩层（loading）
	var showMask = function(target){  
		$(target?target:"body").append("<div class='mask'><div class='remark col-md-4 col-sm-4'><img alt='loading...' src='/image/loading.gif'/></div></div>")
//	  $(".mask").css("height",$("body").height());     
//	  $(".mask").css("width",$("body").width());     
	  $(".mask").show();     
	}  
	//隐藏遮罩层  
	var hideMask = function(){     
	  $(".mask").remove();     
	}  

	/**
	 * type : 'warning'|'error'|'success'|'info'|'question'
	 */
	var a = function(title,type,callback,msg){
		BeAlert.alert(title,msg,callback,{type:type?type:"success"});
	};
	var c = function(title,type,callback,msg){
		BeAlert.confirm(title,msg,callback,{type:type?type:"question"});
	}
	var p = function(title,type,callback,msg,inputType){
		BeAlert.prompt(title,msg,callback,
				{type:type?type:"question",inputType:inputType});
	}
	var log = function(obj) {
		console.log(obj);
	};
	var parseTime = function(time){
		var _time = parseInt(time);
		if(!_time)
			return time;
		var hours = Math.floor(_time/3600);
		var minutes = Math.floor((_time-hours*3600)/60);
		var seconds = _time - hours*3600 - minutes*60;
		var str = (hours<10?"0"+hours:hours) + "时" + (minutes<10?"0"+minutes:minutes) + "分" + (seconds<10?"0"+seconds:seconds) +"秒";
		return str;
	};

	$.fn.form2json = function() {
		var obj={};
	    function evalThem (str) {
	        var attributeName =  str.split("=")[0];
	        var attributeValue = str.split("=")[1];
	        if(!attributeValue){
	            attributeValue = '';
	        }
	        var array = attributeName.split(".");  
	        for (var i = 1; i < array.length; i++) {
	            var tmpArray = Array();  
	            tmpArray.push("obj");  
	            for (var j = 0; j < i; j++) {
	                tmpArray.push(array[j]);  
	            };  
	            var evalString = tmpArray.join(".");  
	            if(!eval(evalString)){  
	                eval(evalString+"={};");                  
	            }  
	        };  
	        if(attributeName != null && "" != attributeName && "null" != attributeName&&attributeValue!=''){
	        	obj[attributeName] = decodeURIComponent(attributeValue).replace(/'/, "\\'");
	        }
	    };
	    var properties = this.serialize().split("&");  
	    for (var i = 0; i < properties.length; i++) {  
	        evalThem(properties[i]);  
	    };  
	    return obj;  
	};
	/**
	 * 进行一次异步请求
	 * url  请求地址
	 * param 请求参数（普通js对象）
	 * ofn 回调函数
	 * method 请求类型（get/post），默认为post,非法值为post
	 */
	var doAjax = function(url,param,ofn,method,async){
		method = method=="get"?method:"post";
		param.webUrl = window.location.href;
		$.ajax({
				type:method,
				url: url,
				dataType: 'json',
				data: param,
				success:function(data){
					if ((typeof (ofn)=="object") && (ofn!=null)){
						if (typeof (ofn.onSuccess)=="function"){ofn.onSuccess(data);}
					}else{
						log(data.reason);
					}
				},
				error : function (xmlR, status, e){
					if (typeof (ofn)=="object"){
						if (typeof (ofn.onFail)=="function"){ofn.onFail(xmlR, status, e);}
					}else{
						a("["+url+"]访问失败，原因["+xmlR.responseText+"]","error");
					}
					hideMask();
				},
				cache:false,	
				async:async===true?async:false,	
		});
		
	}
	/**
	 * 进行一次异步请求
	 * url  请求地址
	 * form 表单选择器，接受单一表单
	 * ofn 回调函数
	 * method 请求类型（get/post），默认为post,非法值为post
	 * moreParam 更多的请求参数  ，Object类型
	 */
	var doFormAjax = function(url,form,ofn,method,moreParam){
		var param = $(form).form2json();
		if(moreParam!=null&&typeof moreParam =="object"){
			$.each(moreParam,function(key,val){
				param[key] = val;
			});
		}
		param.webUrl = window.location.href;
		method = method=="get"?method:"post";
		$.ajax({
				type:method,
				url: url,
				dataType: 'json',
				data: param,
				success:function(data){
					if ((typeof (ofn)=="object") && (ofn!=null)){
						if (typeof (ofn.onSuccess)=="function"){ofn.onSuccess(data);}
					}else{
						log(data.reason);
					}
				},
				error : function (xmlR, status, e){
					if (typeof (ofn)=="object"){
						if (typeof (ofn.onFail)=="function"){ofn.onFail(xmlR, status, e);}
					}else{
						a("["+url+"]访问失败，原因["+xmlR.responseText+"]","error");
					}
					hideMask();
				},
				cache:false,	
		});
		
	}
	/**
	 * 通信器对象
	 */
	var Communicator = {
			/**
			 * sServiceKey 准确的Service类名.目标方法名
			 * jInfo 入参，Json对象
			 * iType 入参类型
			 * jCallback 回调函数
			 * bAjaxMode 是否异步
			 */
			send : function (sServiceKey, jInfo,iType, jCallback, bAjaxMode){
				if (bAjaxMode!==false){bAjaxMode=true;}
				$.ajax({
					type : "POST", async : bAjaxMode, dataType : "json",
					url : "/common/ajax",
					data :{
						"serviceKey" : sServiceKey, "paramClass" :iType,"webUrl" :window.location.pathname, "param" : JSON.stringify(jInfo)
					}, success : function (data){
						if(data.code=='99999'){
							hideMask();
							a(data.reason,"error");
							return;
						}
						if ((typeof (jCallback)=="object") && (jCallback!=null)){
							if (typeof (jCallback.onSuccess)=="function"){jCallback.onSuccess(data);}
						}else{
							log(data.data);
							hideMask();
						}
					}, error : function (xmlR, status, e){
						if (typeof (jCallback)=="object"){
							if (typeof (jCallback.onFail)=="function"){jCallback.onFail(xmlR, status, e);}
						}else{
							a("["+sServiceKey+"]调用失败，原因["+xmlR.responseText+"]","error");
						}
						hideMask();
					}
				});
			},
			/**
			 * sServiceKey 准确的Service类名.目标方法名
			 * jInfo 入参，Json对象
			 * iType 入参类型
			 * jCallback 回调函数
			 * bAjaxMode 是否异步
			 * files 文件数组
			 */
			upload : function(sServiceKey, jInfo,iType, jCallback, bAjaxMode,files){
				var len=files.length;
				if(len==0){
					this.send(sServiceKey, jInfo,iType, jCallback, bAjaxMode);
					return;
				}
				var formData = new FormData();
				formData.append("serviceKey" , sServiceKey);
				formData.append("paramClass" ,iType);
				formData.append("webUrl" ,window.location.pathname);
				formData.append("param" , JSON.stringify(jInfo));
				for(var i=1,len=files.length;i<=len;i++){
					formData.append("file"+i,files[i-1]);
				}
				formData.append("haveFiles",true);
				var xhr = new XMLHttpRequest();
				xhr.open('POST', "/common/upload", bAjaxMode===false?false:true);

				xhr.setRequestHeader("Accept", "application/json");
				// 指定通信过程中状态改变时的回调函数
				xhr.onreadystatechange = function () {
				    // 通信成功时，状态值为4
				    var completed = 4;
				    if (xhr.readyState === completed) {
				        if (xhr.status === 200) {
				            // 处理服务器发送过来的数据
				            var data = JSON.parse(xhr.responseText);
				            if(data.code=='99999'){
				            	hideMask();
								a(data.reason,"error");
								return;
							}
				            if ((typeof (jCallback)=="object") && (jCallback!=null)){
								if (typeof (jCallback.onSuccess)=="function"){jCallback.onSuccess(data);}
							}else{
								log(data.data);
							}
				            //这里你可以随意的处理这个result对象了
				            //...
				        } else {// 处理错误
				        	if (typeof (jCallback)=="object"){
								if (typeof (jCallback.onFail)=="function"){jCallback.onFail(xhr, xhr.status);}
							}else{
								a("["+sServiceKey+"]调用失败，原因["+xhr.responseText+"]","error");
							}
							hideMask();
				        }
				    }
				};
				xhr.send(formData);
			}
		};

	
	var parseJsonObject = function(data){
		if(typeof data =="string")
			data = JSON.parse(data);
		if(data.code=="00000"){
			a(data.reason,"error")
			log(data.reason)
			return ;
		}
		else
			return data.data;
	}
	/** 获得一个count 参数:条件对象,sql语句名,回调函数你懂的.
	var getJsonCount=function(info,sSqlName,callback){
		log(window.location.href);
		info["webUrl"]=window.location.href;
		info.indexFlag=window.indexFlag;
	    return $.post("/ajax",{"sjsonObject":JSON.stringify(info),"sqlName":sSqlName,"requestType":"Count"},'json').done(function(d){if(d && d.status && d.status===-1)a(d.msg)});
	};
	 * */
	/** 获得一个json对象 参数:条件对象，对象名,sql语句名,回调函数你懂的.
	 * */
	var getJsonObject=function(info,iType,sSqlName,callback){
	    return $.get("/common/ajax",{
	    	"serviceKey" : "CommonServiceImpl.getJsonObject",
	    	"webUrl":window.location.pathname,
	    	"param":JSON.stringify(info),
	    	"sqlName":sSqlName,
	    	"paramClass":iType    	
	    		},'json').done(function(d){
	    			if(d.code=='99999'){
	    				hideMask();
						a(d.reason,"error");
						return;
					}
	    			if(d && d.code && d.code==00000)
	    				a(d.msg)}
	    		);
	};
	/** 获得一个json数组 参数:条件对象,bean对象类型,sql语句名,回调函数,每页大小,请求页码.
	 * */
	var getJsonArray=function(info,iType,sSqlName,callback,pageSize,pageNumber){
		var jinfo = {};
		jinfo.webUrl = window.location.pathname;
		jinfo.serviceKey = "CommonServiceImpl.getJsonArray";
		jinfo.sqlName = sSqlName;
		jinfo.paramClass = iType;
		trimString(pageSize)!==""&&(info.pageSize=pageSize);
		trimString(pageNumber)!==""&&(info.pageNumber=pageNumber);
		jinfo.param = JSON.stringify(info);
		return $.get("/common/ajax",jinfo,'json').done(function(d){
			if(d.code=='99999'){
				hideMask();
				a(d.reason,"error");
				return;
			}
			if(d && d.code && d.code==00000)a(d.msg)});
	};



	var ajaxForm = function(info){
		var formData = new FormData();
		$.each(info,function(k,v){
			formData.append(k,v);
		});
		
		var xhr = new XMLHttpRequest();
		xhr.open('POST', "/common/ajaxtest", true);

		xhr.setRequestHeader("Accept", "application/json");
		xhr.send(formData);
		// 指定通信过程中状态改变时的回调函数
		xhr.onreadystatechange = function () {
		    // 通信成功时，状态值为4
		    var completed = 4;
		    if (xhr.readyState === completed) {
		        if (xhr.status === 200) {
		            // 处理服务器发送过来的数据
		            var result = JSON.parse(xhr.responseText);
		            log(result);
		            //这里你可以随意的处理这个result对象了
		            //...
		        } else {// 处理错误
		            a('连接超时','error');
		        }
		    }
		};
	}


	/**分页
	 * info 查询参数
	 * iType 入参类型
	 * sSqlName sql
	 * pageSize 每页条数
	 * pageNumber 页码
	 * applyView  方法，处理数据
	 * toolbar 是否显示工具栏
	 * selector 选择器
	 * */
	paging = function(info,iType,sSqlName,pageSize,pageNumber,applyView,toolbar,selector){
		var target = selector?selector:'.paging';
		$(target).empty();
		showMask()
		getJsonArray(info,iType,sSqlName,null,pageSize,pageNumber).done(function(data){
//			var realData = JSON.parse(data);
			if(data.code=='99999'){
				hideMask();
				a(d.reason,"error");
				return;
			}
			var myPage = data.myPage;
			if(!myPage){
				a("查询出错，分页信息为空！","error");
				hideMask();
				return;
			}
			var total = myPage.total;
			var pages = myPage.pages;
			applyView.call(this,data);
			hideMask();
			if(pages<=1)
				return;
			var p = new Paging();
			p.init({
				target : target,
				pagesize : pageSize,
				count : total,
				current : pageNumber,
				hash : false,
				toolbar : toolbar===true?true:false,
				callback : function(page,size,count){
					showMask();
					getJsonArray(info,iType,sSqlName,null,size,page).done(function(data){
//						var realDataBak = JSON.parse(data);
						applyView.call(this,data);
						hideMask();
						if(data.myPage.pages!=pages){
							p.render({
								count:realDataBak.myPage.total,
								pagesize:size,
								current:page
							});
						}
					});
				}
			});
		});
	};
	
	var showTipBox = function(selector,applyView,timeOut){
		var $tipBox = $(selector),$tipBox_overlay;
//		var selectotClass = "tipBox-"+id==null?"":id;
		if(!$tipBox.hasClass("tipBox"))
			$tipBox.addClass("tipBox");
//		$tipBox.addClass(selectotClass);
		if(!$tipBox.next().hasClass('tipBox_overlay')){
			$tipBox_overlay = $("<div class='tipBox_overlay'/>").show();
			$tipBox.after($tipBox_overlay);
		}else
			$tipBox_overlay = $tipBox.next().show();
		if(typeof applyView ==='function')
			applyView.called(window);
		if(timeOut){
			$tipBox.fadeIn(timeOut,function(){
				popup($tipBox);
			});	
		}else{
			$tipBox.show();
			popup($tipBox);
		}
		$tipBox_overlay.off("click").on("click",function(){
			$tipBox.hide();
			$tipBox_overlay.hide();
		});
		
		function popup(popupName){ 
//			var _scrollHeight = $(document).scrollTop(),//获取当前窗口距离页面顶部高度 
			_windowHeight = $(window).height(),//获取当前窗口高度 
//			_windowWidth = $(window).width(),//获取当前窗口宽度 
			_popupHeight = popupName.height(),//获取弹出层高度 
//			_popupWeight = popupName.width();//获取弹出层宽度 
			_posiTop = (_windowHeight - _popupHeight)/2 /*+ _scrollHeight*/; 
//			_posiLeft = (_windowWidth - _popupWeight)/2; 
			popupName.css({/*"left": _posiLeft + "px",*/"top":_posiTop + "px"});//设置position 
		} 
	}
	
	var hideTipBox = function(selector){
		$(selector).hide();
		$(selector).next().hide();
	}
	//post请求一个url
	var postAction = function(url,info) {
	    //创建form表单
	    var temp_form = document.createElement("form");
	    temp_form.action =url;
	    //如需打开新窗口，form的target属性要设置为'_blank'
	    temp_form.target = "_blank";
	    temp_form.method = "post";
	    temp_form.style.display = "none";
	    $.each(info,function(k,v){
	    	var opt = document.createElement("textarea");
	    	opt.name = k;
	    	opt.value = v;
	    	temp_form.append(opt);
	    });
	    //添加参数
	    document.body.appendChild(temp_form);
	    //提交数据
	    temp_form.submit();
	    temp_form.remove();
	}
	
	
	//获取公告
	var getAnnouncement = function(selector,size){
		var id = parseInt(1000*Math.random())
		var $parent = $(selector)/*.addClass("carousel").addClass("slide").attr("id",'carousel-'+id);*/
		var $carousel = $('<div class="carousel slide" id="carousel-'+id+'"/>');
		var $ol = $('<ol class="carousel-indicators"/>');
		var $carouselInner = $('<div class="carousel-inner">');
		getJsonArray({aliveFlag : '1',orderBy: 'create_Date desc'},"Announcement","AnnouncementMapper.select",null,size,1).done(function(data){
			if(data.code=="11111"){
				$parent.empty();
				var realData = data.data;
				for(var i=0,len=realData.length;i<len&&i<5;i++){
					var d = realData[i];
					var $li = $('<li data-slide-to="'+i+'" data-target="#carousel-'+id+'">');
					$ol.append($li);
					var $item = $('<div class="item"><img alt="" src="/image/default'+i+'.jpg" /><div class="carousel-caption"><h2></h2><p></p></div></div>');
					$item.find("h2").text(d.title);
					$item.find("p").text(d.context);
					if(i==0){
						$li.addClass("active");
						$item.addClass("active");
					}
					$item.attr("data-info",d.guid).on("click",function(){
						postAction("/common/announcementContext/"+$(this).attr("data-info")+"/context");
					});
					$carouselInner.append($item);
				}
				$carousel.append($ol);
				$carousel.append($carouselInner);
				$carousel.append('<a class="left carousel-control" href="#carousel-'+id+'" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a> <a class="right carousel-control" href="#carousel-'+id+'" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>');
				$parent.append($carousel);
			}else{
				a("公告加载失败！","error");
			}
		});
	}
	
	
	var showRepairBillPicture = function(selector,data){
		var len=data.length;
		if(len===0){
			a("未上传图片！","warning")
			return;
		}
		var $selector = $(selector).empty();
		var id = parseInt(1000*Math.random());
		var $carousel = $('<div class="carousel slide" id="carousel-'+id+'"/>');
		var $ol = $('<ol class="carousel-indicators"/>');
		var $carouselInner = $('<div class="carousel-inner">');
		for(var i=0;i<len;i++){
			var path = data[i];
			var $li = $('<li data-slide-to="'+i+'" data-target="#carousel-'+id+'">');
			$ol.append($li);
			var $item = $('<div class="item"><img class="" alt="" src="'+path+'" /></div>');
			if(i==0){
				$li.addClass("active");
				$item.addClass("active");
			}
			$carouselInner.append($item);
		}
		$carousel.append($ol);
		$carousel.append($carouselInner);
		$carousel.append('<a class="left carousel-control" href="#carousel-'+id+'" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a> <a class="right carousel-control" href="#carousel-'+id+'" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>');
		var resetHeight = function(){
			setTimeout(function(){
				var popupName = $(selector);
				var _scrollHeight = $(document).scrollTop(),//获取当前窗口距离页面顶部高度 
				_windowHeight = $(window).height(),//获取当前窗口高度 
				_popupHeight = popupName.height(),//获取弹出层高度 
				_posiTop = (_windowHeight - _popupHeight)/2 + _scrollHeight; 
				popupName.css({"top":_posiTop + "px"});//设置position 
			},800);
		};
		$carousel.children("[data-slide='prev']").on("click",resetHeight)
		$carousel.children("[data-slide='next']").on("click",resetHeight)
		
		$selector.append($carousel);
//		setTimeout(function(){showTipBox(selector)},1000)
	}
	
	var addEle2Arr = function(arr,ele){
		if(arr==null||arr.length===undefined)
			return;
		if(ele==null)
			return;
		arr.push(ele);
	}
	/*<div class="carousel slide" id="carousel-450247">
	<ol class="carousel-indicators">
		<li class="active" data-slide-to="0" data-target="#carousel-450247">
		</li>
		<li data-slide-to="1" data-target="#carousel-450247">
		</li>
		<li data-slide-to="2" data-target="#carousel-450247">
		</li>
	</ol>
	<div class="carousel-inner">
		<div class="item active">
			<img alt="" src="v3/default.jpg" />
		</div>
		<div class="item">
			<img alt="" src="v3/default1.jpg" />
		</div>
		<div class="item">
			<img alt="" src="v3/default2.jpg" />
		</div>
	</div> <a class="left carousel-control" href="#carousel-450247" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a> <a class="right carousel-control" href="#carousel-450247" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
</div>*/
	return {
		trimNumber :trimNumber,
		trimString :trimString,
		showMask : showMask,
		hideMask :hideMask,
		a:a,
		c:c,
		p:p,
		log:log,
		parseTime : parseTime,
		doAjax : doAjax,
		doFormAjax : doFormAjax,
		Communicator : Communicator,
		getJsonObject : getJsonObject,
		parseJsonObject : parseJsonObject,
		getJsonArray : getJsonArray,
		ajaxForm : ajaxForm,
		paging : paging,
		showTipBox : showTipBox,
		hideTipBox : hideTipBox,
		postAction : postAction,
		getAnnouncement : getAnnouncement,
		showRepairBillPicture : showRepairBillPicture,
		addEle2Arr : addEle2Arr,
	};
})
