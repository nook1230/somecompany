function isEffectiveNumber(check, notice) {
	if(isNaN(check) || check.length == 0) {
		if(notice != null)
			alert(notice);
		
		return false;
	} else { 
		return true;
	}
}

function isEffectiveOrEmptyNumber(check, notice) {
	if(isNaN(check) && check.length != 0) {
		if(notice != null)
			alert(notice);
		
		return false;
	} else { 
		return true;
	}
}
		
function isNotEmptyString(check, notice) {
	if(check.length == 0) {
		if(notice != null)
			alert(notice);
		
		return false;
	} else {
		return true;
	}
}

function notEmptyStringCheckLength(check, maxLen, notice) {
	if(check.length == 0 || check.length > maxLen) {
		if(notice != null)
			alert(notice);
		
		return false;
	} else {
		return true;
	}
}


function stringCheckLength(check, minLen, maxLen, notice) {
	if(check.length < minLen || check.length > maxLen) {
		if(notice != null)
			alert(notice);
		
		return false;
	} else {
		return true;
	}
}

function regexTest(check, regex, notice) {
	if(!regex.test(check)) {
		if(notice != null)
			alert(notice);
		
		return false;
	} else {
		return true;
	}
}

function isEqual(str1, str2, notice) {
	if(str1 != str2) {
		if(notice != null)
			alert(notice);
		
		return false;
	} else {
		return true;
	}
}

function gteq(num1, num2, notice) {
	if(num1 < num2) {
		if(notice != null)
			alert(notice);
		
		return false;
	} else {
		return true;
	}
}