define(["base","common"],function(base,common){

	function Repair(){
		this.url = window.location.pathname;
	}

	Repair.prototype = {
		init : function(setting){
			this.$addRepairTab = $("#addRepairTab",setting);
			this.$repairLogTab = $("#repairLogTab",setting);
			
			this.$addRepairPage = $(".addRepairPage",setting);
			this.$repairLogPage = $(".repairLogPage",setting);
			this.$addForm = $("form",this.$addRepairPage);
			this.choosePage("add");
		},
		choosePage : function(type){
			var that = this;
			if(!common.studentId){
				base.a("请先绑定学生信息！","warning");
				return;
			}
			if(type=="log"){
				that.$repairLogPage.show();
				that.$addRepairPage.hide();
				that.$repairLogTab.addClass("active").siblings().removeClass("active");
				this.$repairLogTab.off("click")
				this.$addRepairTab.off("click").on("click",function(){
					that.choosePage("add");
				});
			}else{
				that.getImageEvent();
				that.getAddFormCheck();
				that.$addRepairPage.show();
				that.$repairLogPage.hide();
				that.$addRepairTab.addClass("active").siblings().removeClass("active");
				this.$addRepairTab.off("click")
				this.$repairLogTab.off("click").on("click",function(){
					that.choosePage("log");
				});
			}
			that.getPageOnClick(type);
			that.doPaging(type,{orderBy:"modifi_date desc"});
		},
		//添加图片事件
		getImageEvent : function(){
			var that = this;
			$(".add",that.$addRepairPage).off("click").on("click",function(){
				var $this = $(this);
				if($this.parents(".imgs").children(".havaImgDiv").length>=3){
					base.a("最多只能上传三张图片！",'warning');
					return;
				}
				$(this).next().click();
			})
			$(".upload",that.$addRepairPage).on("change",function(){
				var $this = $(this)
				var acceptedTypes = {
					'image/png': true,
					'image/jpeg': true,
					'image/gif': true
				};
				var file = this.files[0];
				if(file==null)
					return;
				if (acceptedTypes[file.type] === true){
					var reader = new FileReader();
					reader.onload = function (event) {
						var $prev = $this.prev().css("background","url("+event.target.result+") no-repeat center");
						$prev.removeClass("add").addClass("havaImgDiv");
						$this.addClass("haveImg");
						$prev.append($("<span class='del'/>").on("click",function(){
							var $parent = $(this).parent();
							$parent.next()[0].files=null;
							$parent.next().remove();
							$parent.remove();
						}));
						$prev.off("click");
						$this.parent().append('<div class="add col-md-2 col-sm-3"><br><br><br></div><input class="upload" type="file" name="files" style="display:none;"/>');
						that.getImageEvent();
					};
					reader.readAsDataURL(file);
				}else{
					this.files = null;
					base.a("只能上传图片~！","error");
				}
			});
		},
		//表单提交
		getAddFormCheck : function(){
			var that = this;
			$(that.$addForm).bootstrapValidator({
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
		           personName: {
		               validators: {
		            	   notEmpty: {
		                       message: '联系人不能为空！'
		                   },
		                   stringLength: {
		                       max: 5,
		                       message: '谁名字这么长啊。。。'
		                   },
		               }
		           },
		           phoneNumber: {
		               validators: {
		            	   notEmpty: {
		                       message: '电话号码不能为空！'
		                   },
		                   regexp: {
		                       regexp: /^1[0-9]{10}$/,
		                       message: '手机号格式不正确！'
		                   },
		               }
		           },
		           context: {
		               validators: {
		            	   notEmpty: {
		                       message: '描述不能为空！'
		                   },
		                   stringLength: {
		                       max: 200,
		                       message: '最多200个字。'
		                   },
		               }
		           }
		       }
			});
			
			//提交	
			$("[type='button']",that.$addForm).off("click").on("click",function(){
				var v = that.$addForm.data('bootstrapValidator');
				v.validate();
				var flag = v.isValid()
				if(flag){
					var info = {};
					info.title = $("[name='title']",that.$addForm).val();
					info.personName = $("[name='personName']",that.$addForm).val();
					info.phoneNumber = $("[name='phoneNumber']",that.$addForm).val();
					info.context = $("[name='context']",that.$addForm).val();
					$files = $("[type='file']",that.$addForm);
					files = [];
					$.each($files,function(index,dom){
						if(dom.files[0])
							files.push(dom.files[0]);
					});
					base.showMask();
					base.Communicator.upload("RepairService.submitRepairBill",info,"RepairBill",{
						onSuccess : function(data){
							if(data.code==common.SUCCESS){
								base.a(data.data);
								that.$addForm.fadeOut(2000,function(){
									that.$addForm[0].reset();
									$(".haveImg",that.$addForm).remove();
									that.$addForm.fadeIn(2000);
								});
							}else
								base.a(data.reason,"error");
							base.hideMask()
						}
					},null,files);
					v.resetForm();
				}
			})
		},
		doPaging : function(type,info){
			var that = this;
			if(type=="log"){
				var applyView = function(data){
					for(var i in data.data){
						var o = data.data[i];
						var picture = [];
						base.addEle2Arr(picture,o.image1);
						base.addEle2Arr(picture,o.image2);
						base.addEle2Arr(picture,o.image3);
						o.picture = JSON.stringify(picture);
						var status =o.status;
						var statusStr = "未知";
						var operation = "";
						switch(status){
							case "00":{
								statusStr = "审核未通过";
								break;
							}
							case "10":{
								statusStr = "待审核";
								break;
							}
							case "20":{
								statusStr = "维修中";
								operation = "完成"
								break;
							}
							case "30":{
								statusStr = "已维修";
								break;
							}
						}
						o.status = statusStr;
						o.operation = operation;
					}
					var $tbody = $(".repairLogPageTbody",that.$repairLogPage);
					var $html = $($("#repairLogPageTr").render(data));
					$tbody.empty().append($html);
					//完成操作
					$html.find(".operation").on("click",function(){
						var guid =  $(this).parent().children().eq(0).attr("data-info");
						$(".tipBox1").attr("data-info",guid);
						base.showTipBox(".tipBox1");
					});
					//标题
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
				
				info.dormitoryNumber = common.dormitoryNumber;
				base.paging(info,"RepairBill","RepairBillMapper.select",10,1,applyView,true);
				
			}
		},
		getPageOnClick : function(type){
			var that = this;
			if(type=="log"){
				var $form = $(".tipBox1 form");
				$form.bootstrapValidator({
					feedbackIcons: {
			            valid: 'glyphicon glyphicon-ok',
			            invalid: 'glyphicon glyphicon-remove',
			            validating: 'glyphicon glyphicon-refresh'
			        },
			        fields: {
			        	realRepairDate : {
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
			        	remark : {
			        		validators: {
			        			notEmpty : {
			        				message: '维修反馈不能为空！'
			        			},
			        			stringLength: {
				                       min: 2,
				                       max: 200,
				                       message: '维修人姓名只能为2-200个字符！'
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
//					base.log($form.find("[name='repairDate']").val())
					var date = new Date($form.find("[name='realRepairDate']").val());
					var flag = v.isValid();
					if(flag){
						base.hideTipBox(".tipBox1");
						var info = $form.form2json();
						info.guid = $(".tipBox1").attr("data-info");
						v.resetForm();
						if(Date.parse(date)-Date.parse(new Date())>0){
							base.a("不能选择当前时间之后的时间！","warning");
							return;
						}
						base.Communicator.send("RepairService.finish",info,"RepairBill",{
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
		}
	}
	var repair = new Repair();
	repair.init($("body"));
});
