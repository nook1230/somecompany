function replaceParameter(params, paramName) {
	// params 중 원하는 파라미터를 제거한다
	params = params.substr(1);
	var splitParams = params.split('&');
	
	params = "?";
	
	for(var i = 0; i < splitParams.length; i++ ) {
		var str = splitParams[i];
		var s = str.split('=');
		
		if(s[0] != null && s[0] != paramName && s[0] != '') {
			params += (str + '&');
		}
	}
			
	return params;
}


function extractParameter(params, paramName) {
	// params 중 원하는 파라미터를 추출한다
	params = params.substr(1);
	var splitParams = params.split('&');
	
	params = "?";
	
	for(var i = 0; i < splitParams.length; i++ ) {
		var str = splitParams[i];
		var s = str.split('=');
		
		if(s[0] != null && s[0] == paramName) {
			if(s[1] != null)
				return s[1];
		}
	}
			
	return '';
}



function setNumberOfDaysOfMonth(id, number) {
	var selDays = document.getElementById(id);
	
	var opt;
	
	for(var i = selDays.options.length - 1; i >= 0 ; i--) {
		// 모두 지우고
		selDays.remove(i);
	}
	
	for(var i = 0; i < number; i++) {
		// number만큼의 option을 추가
		opt = new Option();
		opt.value = i + 1;
		opt.text = i + 1;
		
		selDays.add(opt);
	}
}

function getNumberOfDaysOfMonth(idYear, idMonth) {
	var selYear = document.getElementById(idYear);
	var selMonth = document.getElementById(idMonth);
	
	var year = selYear.options[selYear.selectedIndex].value;
	var month = selMonth.options[selMonth.selectedIndex].value;
	
	var longMonth = new Array(1, 3, 5, 7, 8, 10, 12);	// 날짜 수가 31일인 달
	
	if(month == 2) {
		if(year % 4 == 0)
			return 29;	// 윤달
		else
			return 28;
	}
	
	for(var i = 0; i < longMonth.length; i++) {
		if(longMonth[i] == month)
			return 31;
	}
	
	return 30;
}