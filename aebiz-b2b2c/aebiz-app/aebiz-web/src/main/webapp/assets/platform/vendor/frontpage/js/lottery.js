var lottery={
	index:-1,	//当前转动到哪个位置，起点位置
	count:0,	//总共有多少个位置
	timer:0,	//setTimeout的ID，用clearTimeout清除
	speed:20,	//初始转动速度
	times:0,	//转动次数
	cycle:50,	//转动基本次数：即至少需要转动多少次再进入抽奖环节
	prize:-1,	//中奖位置
	init:function(id){
		if ($("#"+id).find(".lottery-unit").length>0) {
			$lottery = $("#"+id);
			$units = $lottery.find(".lottery-unit");
			this.obj = $lottery;
			this.count = $units.length;
			$lottery.find(".lottery-unit-"+this.index).find("div").addClass("r_active");
		};
	},
	roll:function(){
		var index = this.index;
		var count = this.count;
		var lottery = this.obj;
		$(lottery).find(".lottery-unit-"+index).find("div").removeClass("r_active");
		index += 1;
		if (index>count-1) {
			index = 0;
		};
		$(lottery).find(".lottery-unit-"+index).find("div").addClass("r_active");
		this.index=index;
		return false;
	},
	stop:function(index){
		this.prize=index;
		return false;
	}
};

function roll(){
	lottery.times += 1;
	lottery.roll();
	if (lottery.times > lottery.cycle+10 && lottery.prize==lottery.index) {
		switch (lottery.prize) {
			case 0:
				$("#cjmodal .modal-body p").html("恭喜您抽中了18全网积分!");
				$("#cjmodal").modal();
				break;
			case 2:
				$("#cjmodal .modal-body p").html("恭喜您抽中了8全网积分！");				
				$("#cjmodal").modal();		
				break;	
			case 3:
				$("#cjmodal .modal-body p").html("恭喜您抽中了8全网积分！");				
				$("#cjmodal").modal();			
				break;	
			case 4:
				$("#cjmodal .modal-body p").html("恭喜您抽中了18全网积分！");				
				$("#cjmodal").modal();			
				break;			
			case 5:
				$("#cjmodal .modal-body p").html("恭喜您抽中了8全网积分！");				
				$("#cjmodal").modal();			
				break;				
			case 6:
				$("#cjmodal .modal-body p").html("恭喜您抽中了288全网积分！");				
				$("#cjmodal").modal();			
				break;				
			case 8:
				$("#cjmodal .modal-body p").html("恭喜您抽中了8全网积分！");				
				$("#cjmodal").modal();			
				break;					
			case 9:
				$("#cjmodal .modal-body p").html("恭喜您抽中了1888全网积分！");				
				$("#cjmodal").modal();			
				break;							 	 			 				 				 		 				 		
			case 10:
				$("#cjmodal .modal-body p").html("恭喜您抽中了18全网积分！");				
				$("#cjmodal").modal();
				break;
			case 11:
				$("#cjmodal .modal-body p").html("恭喜您抽中了8全网积分！");				
				$("#cjmodal").modal();			
				break;					 		
			default:
				$("#cjmodal .modal-body p").html("很遗憾，您没有中奖。");				
				$("#cjmodal").modal();			
				break;				
		};		
		clearTimeout(lottery.timer);
		lottery.prize=-1;
		lottery.times=0;
		click=false;
	}else{
		if (lottery.times<lottery.cycle) {
			lottery.speed -= 10;
		}else if(lottery.times==lottery.cycle) {
			var index = Math.random()*(lottery.count)|0;
			lottery.prize = index;
		}else{
			if (lottery.times > lottery.cycle+10 && ((lottery.prize==0 && lottery.index==7) || lottery.prize==lottery.index+1)) {
				lottery.speed += 110;
			}else{
				lottery.speed += 20;
			}
		}
		if (lottery.speed<40) {
			lottery.speed=40;
		};
		//console.log(lottery.times+'^^^^^^'+lottery.speed+'^^^^^^^'+lottery.prize);
		lottery.timer = setTimeout(roll,lottery.speed);
	}
	return false;
}