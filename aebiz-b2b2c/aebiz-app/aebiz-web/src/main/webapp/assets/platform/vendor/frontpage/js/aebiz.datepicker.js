	// datepick 调用日历插件
  $(function(){
    if( $('.datepick').length > 0 ) {
			var dtp = $('.datepick').datepicker()
			.on('changeDate', function(e) {
				dtp.datepicker('hide');
				$(this).blur();
			});	
		};
  });