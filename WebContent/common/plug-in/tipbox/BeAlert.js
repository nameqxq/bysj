/**
 * Created by Luker on 2016/10/31.
 */
define(["$"],function($){
	var tmp = {};
    (function () {
        var BeAlert = {
            defaultConfig: {
//                width: 320,
                height: 170,
                timer: 0,
                type: 'warning',
                showConfirmButton: true,
                showCancelButton: false,
                showInput: false,
                inputType: "password",
                confirmButtonText: '确认',
                cancelButtonText: '取消'
            },
            html: '<div class="BeAlert_box col-md-4 col-sm-4">' +
			            '<div class="BeAlert_image"></div>' +
			            '<div class="BeAlert_title"></div>' +
			            '<div class="BeAlert_message">'+
			            '</div>' +
			            '<div class="form-group inputDiv col-md-8 col-sm-8"><br/><input type="password" class="form-control"/></div>'+
			            '<div class="BeAlert_button">' +
				            '<button class="btn btn-primary BeAlert_confirm"></button>' +
				            '<button class="btn BeAlert_cancel"></button>' +
			            '</div>' +
			       '</div>',
            overlay: '<div class="BeAlert_overlay"></div>',
            open: function (title, message, callback, o) {
                var opts = {}, that = this;
                $.extend(opts, that.defaultConfig, o);
                $('body').append(that.html).append(that.overlay);
                var box = $('.BeAlert_box');
                box.css({
                    'width': opts.width + 'px',
                    'min-height': opts.height + 'px',
                    'margin-left': -(opts.width / 2) + 'px'
                });
                /*var submitHandler = function(){
               	 var val = "";
                    if(opts.showInput){
                    	var $input = showInputDiv.children("input");
                    	val = $input.val();
                    	if(trimString(val)==""){
                    		$input.val("");
                    		return;
                    	}
                    }
                    that.close();
                    typeof callback === 'function' && callback(true,val);
               }*/
                
                var $BeAlertBox = $('.BeAlert_box');
                $('.BeAlert_image',$BeAlertBox).addClass(opts.type);
                title && $('.BeAlert_title',$BeAlertBox).html(title).show(),
                message && $('.BeAlert_message',$BeAlertBox).html(message).show();
                var confirmBtn = $('.BeAlert_confirm',$BeAlertBox), 
                	cancelBtn = $('.BeAlert_cancel',$BeAlertBox),
                	showInputDiv = $(".inputDiv",$BeAlertBox);
                opts.showConfirmButton && confirmBtn.text(opts.confirmButtonText).show(),
                opts.showCancelButton && cancelBtn.text(opts.cancelButtonText).show();
                opts.showInput && showInputDiv.show().children("input").attr("type",opts.inputType)
                	.focus().unbind("keyup").bind("keyup",function(event){
                		if(event.keyCode===13)
                			submitHandler();
                	});
                $('.BeAlert_overlay').unbind('click').bind('click', function () {
                    that.close();
                    typeof callback === 'function' && callback(false);
                });
                
             
                confirmBtn.unbind('click').bind('click', function () {
                	 var val = "";
                     if(opts.showInput){
                     	var $input = showInputDiv.children("input");
                     	val = $input.val();
                     	if(trimString(val)==""){
                     		$input.focus().val("");
                     		return;
                     	}
                     	that.close();
                     	typeof callback === 'function' && callback(true,val);
                     }else{
                    	that.close();
                    	typeof callback === 'function' && callback(true);
                     }
                });
                cancelBtn.unbind('click').bind('click', function () {
                    that.close();
                    typeof callback === 'function' && callback(false);
                });
                var h = box.height();
                box.css({
                    'margin-top': -(Math.max(h, opts.height) / 2 + 100) + 'px'
                });
            },
            close: function () {
                $(".BeAlert_overlay,.BeAlert_box").remove();
            }
        };
        tmp.alert = function (title, message, callback, opts) {
            BeAlert.open(title, message, callback, opts);
        };
        tmp.confirm = function (title, message, callback, opts) {
            opts = $.extend({type: 'question', showCancelButton: true}, opts);
            BeAlert.open(title, message, callback, opts);
        }
        tmp.prompt = function(title, message, callback, opts) {
            opts = $.extend({type: 'question', showCancelButton: true, showInput:true}, opts);
            BeAlert.open(title, message, callback, opts);
        }
    })();
    
    return tmp;
})
