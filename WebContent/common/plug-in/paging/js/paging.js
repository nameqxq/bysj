/*
 * Created with Sublime Text 3.
 * demo地址: http://www.lovewebgames.com/jsmodule/index.html
 * github: https://github.com/tianxiangbing/paging
 * User: 田想兵
 * Date: 2015-06-11
 * Time: 16:27:55
 * Contact: 55342775@qq.com
 * Desc: 确保代码最新及时修复bug，请去github上下载最新源码 https://github.com/tianxiangbing/paging
 */
define(['$'], function($){
	var paging = function($) {
		$.fn.Paging = function(settings) {
			var arr = [];
			$(this).each(function() {
				var options = $.extend({
					target: $(this)
				}, settings);
				var lz = new Paging();
				lz.init(options);
				arr.push(lz);
			});
			return arr;
		};
	
		function Paging() {
			var rnd = Math.random().toString().replace('.', '');
			this.id = 'Paging_' + rnd;
		}
		Paging.prototype = {
			init: function(settings) {
				this.settings = $.extend({
					callback: null,
					pagesize: 10,
					current: 1,
					prevTpl: "上一页",
					nextTpl: "下一页",
					firstTpl: "首页",
					lastTpl: "末页",
					ellipseTpl: "...",
					toolbar: false,
					hash:true,
					pageSizeList: [5, 10, 15, 20]
				}, settings);
				this.target = $(this.settings.target);
				this.container = $('<div id="' + this.id + '" class="ui-paging-container"/>');
				this.target.append(this.container);
				this.render(this.settings);
				this.format();
				this.bindEvent();
			},
			render: function(ops) {
				this.count = ops.count || this.settings.count;
				this.pagesize = ops.pagesize || this.settings.pagesize;
				this.current = ops.current || this.settings.current;
				this.pagecount = Math.ceil(this.count / this.pagesize);
				this.format();
			},
			bindEvent: function() {
				var _this = this;
				this.container.on('click', 'li.js-page-action,li.ui-pager', function(e) {
					if ($(this).hasClass('disabled') || $(this).hasClass('active')) {
						return false;
					}
					if ($(this).hasClass('js-page-action')) {
						if ($(this).hasClass('js-page-first')) {
							_this.current = 1;
						}
						if ($(this).hasClass('js-page-prev')) {
							_this.current = Math.max(1, _this.current - 1);
						}
						if ($(this).hasClass('js-page-next')) {
							_this.current = Math.min(_this.pagecount, _this.current + 1);
						}
						if ($(this).hasClass('js-page-last')) {
							_this.current = _this.pagecount;
						}
					} else if ($(this).data('page')) {
						_this.current = parseInt($(this).data('page'));
					}
					_this.go();
				});
				/*
				$(window).on('hashchange',function(){
					var page=  parseInt(Query.getHash('page'));
					if(_this.current !=page){
						_this.go(page||1);
					}
				})
				 */
			},
			go: function(p) {
				var _this = this;
				this.current = p || this.current;
				this.current = Math.max(1, _this.current);
				this.current = Math.min(this.current, _this.pagecount);
				this.format();
				if(this.settings.hash){
					Query.setHash({
						page:this.current
					});
				}
				this.settings.callback && this.settings.callback(this.current, this.pagesize, this.pagecount);
			},
			changePagesize: function(ps) {
				this.render({
					pagesize: ps
				});
				this.settings.callback && this.settings.callback(this.current, this.pagesize, this.pagecount);
			},
			format: function() {
				var html = '<ul class="pagination">'
				html += '<li class="js-page-first js-page-action ui-pager" ><span>' + this.settings.firstTpl + '</span></li>';
				html += '<li class="js-page-prev js-page-action ui-pager"><span>' + this.settings.prevTpl + '</span></li>';
				if (this.pagecount > 6) {
					html += '<li data-page="1" class="ui-pager"><span>1</span></li>';
					if (this.current <= 2) {
						html += '<li data-page="2" class="ui-pager"><span>2</span></li>';
						html += '<li data-page="3" class="ui-pager"><span>3</span></li>';
						html += '<li class="ui-paging-ellipse"><span>' + this.settings.ellipseTpl + '</span></li>';
					} else
					if (this.current > 2 && this.current <= this.pagecount - 2) {
						if(this.current >3){
							html += '<li><span>' + this.settings.ellipseTpl + '</span></li>';
						}
						html += '<li data-page="' + (this.current - 1) + '" class="ui-pager"><span>' + (this.current - 1) + '</span></li>';
						html += '<li data-page="' + this.current + '" class="ui-pager"><span>' + this.current + '</span></li>';
						html += '<li data-page="' + (this.current + 1) + '" class="ui-pager"><span>' + (this.current + 1) + '</span></li>';
						if(this.current < this.pagecount-2){
							html += '<li class="ui-paging-ellipse" class="ui-pager"><span>' + this.settings.ellipseTpl + '</span></li>';
						}
					} else {
						html += '<li class="ui-paging-ellipse" ><span>' + this.settings.ellipseTpl + '</span></li>';
						for (var i = this.pagecount - 2; i < this.pagecount; i++) {
							html += '<li data-page="' + i + '" class="ui-pager"><span>' + i + '</span></li>'
						}
					}
					html += '<li data-page="' + this.pagecount + '" class="ui-pager"><span>' + this.pagecount + '</span></li>';
				} else {
					for (var i = 1; i <= this.pagecount; i++) {
						html += '<li data-page="' + i + '" class="ui-pager"><span>' + i + '</span></li>'
					}
				}
				html += '<li class="js-page-next js-page-action ui-pager"><span>' + this.settings.nextTpl + '</span></li>';
				html += '<li class="js-page-last js-page-action ui-pager"><span>' + this.settings.lastTpl + '</span></li>';
				html += '</ul>';
				this.container.html(html);
				if (this.current == 1) {
					$('.js-page-prev', this.container).addClass('disabled');
					$('.js-page-first', this.container).addClass('disabled');
				}
				if (this.current == this.pagecount) {
					$('.js-page-next', this.container).addClass('disabled');
					$('.js-page-last', this.container).addClass('disabled');
				}
				this.container.find('li[data-page="' + this.current + '"]').addClass('active').siblings().removeClass('active');
				if (this.settings.toolbar) {
					this.bindToolbar();
				}
			},
			bindToolbar: function() {
				var _this = this;
				var html = $('<li class="ui-paging-toolbar">'+
						'<div class="btn-group">'+
						'<button class="showSelect btn btn-default"></button>'+
						'<button data-toggle="dropdown" class="btn btn-default dropdown-toggle"><span class="caret"></span></button>'+
						'<ul class="dropdown-menu ui-select-pagesize"></ul></div>'+
						'<div class="col-sm-2 col-md-2"><input type="text" class="form-control"/></div><a class="jump" href="javascript:void(0)">跳转</a></li>');
				var sel = $('.ui-select-pagesize', html);
				var str = '';
				for (var i = 0, l = this.settings.pageSizeList.length; i < l; i++) {
	//				str += '<option value="' + this.settings.pageSizeList[i] + '">' + this.settings.pageSizeList[i] + '条/页</option>';
					str += '<li class="select" value="' + this.settings.pageSizeList[i] + '"><a href="javaScript:void(0)">' + this.settings.pageSizeList[i] + '条/页</a></li>';
				}
				sel.html(str);
				var showSelect = $(".showSelect",html).text(this.pagesize+'条/页');
	//			sel.val(this.pagesize);
				$('input', html).val(this.current);
				$('input', html).click(function() {
					$(this).select();
				}).keydown(function(e) {
					if (e.keyCode == 13) {
						var current = parseInt($(this).val()) || 1;
						_this.go(current);
					}
				});
				$('.jump', html).click(function() {
					var current = parseInt($(this).prev().children().val()) || 1;
					_this.go(current);
				});
				$(".select",html).click(function(){
					var nowPageSize = parseInt($(this).attr("value"))||10;
					showSelect.text(nowPageSize+'条/页');
					_this.changePagesize(nowPageSize);
				});
				/*sel.change(function() {
					_this.changePagesize($(this).val());
				});*/
				this.container.children('ul').append(html);
			}
		}
		return Paging;
	};
	return paging($);
})