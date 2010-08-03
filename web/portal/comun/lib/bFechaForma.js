//***************************************************************
//ESTA BIBLIOTECA ES UTILIZADA POR LA FORMA DE FECHA, LA CUAL
//PRESENTA LOS CAMPOS DE DIA, MES y A¥O DE FORMA SEPARADA
//***************************************************************

var weekday_showing = true; //FALSO: OCULTA EL LISTBOX DE DIA DE LA SEMANA
var dayofweek_returned_as_number = false; //TRUE: HACE EL DIA DE LA SEMANA UN VALOR NUMERICO (0-6)
var month_returned_as_number = true; //TRUE: HACE EL MES UN VALOR NUMERICO (0-11)

function changeDays(numb,date_form) {
	mth = date_form.month.selectedIndex;
	sel = date_form.year.selectedIndex;
	yr = date_form.year.options[sel].text;
	if (numb != 1) {
  		numDays = numDaysIn(mth,yr);
  		date_form.day.options.length = numDays;
  		for (i=27;i<numDays;i++) {
   			date_form.day.options[i].text = i+1;
  		}
 	}
	day = date_form.day.selectedIndex+1;
	if (weekday_showing)
		date_form.dayofweek.selectedIndex = getWeekDay(mth,day,yr);
}

function numDaysIn(mth,yr) {
	if (mth==3 || mth==5 || mth==8 || mth==10) return 30;
	else if ((mth==1) && leapYear(yr)) return 29;
	else if (mth==1) return 28;
	else return 31;
}

function leapYear(yr) {
	if (((yr % 4 == 0) && yr % 100 != 0) || yr % 400 == 0)
		return true;
	else
		return false;
}

function arr() {
	this.length=arr.arguments.length;
	for (n=0;n<arr.arguments.length;n++) {
		this[n] = arr.arguments[n];
	}
}

function getWeekDay(mth,day,yr) {
	first_day = firstDayOfYear(yr);
	for (num=0;num<mth;num++) {
		first_day += numDaysIn(num,yr);
	}
	first_day += day-1;
	return first_day%7;
}

function firstDayOfYear(yr) {
	diff = yr - 401;
	return parseInt((1 + diff + (diff / 4) - (diff / 100) + (diff / 400)) % 7);
}

//CORRIGE EL ERROR 2 Y 3 DE NETSCAPE
function getFullYear(d) { // d is a date object
	yr = d.getYear();
	if (yr < 1000)
		yr+=1900;
	return yr;
}

function setFechaActual(date_form){
	var fFecha= new Date();
	date_form.day.selectedIndex = fFecha.getDate()-1;
	date_form.month.selectedIndex = fFecha.getMonth();
	for (var i=0; i <= date_form.year.size; i++){
		if (date_form.year.options(i).text == fFecha.getYear()){
			date_form.year.selectedIndex = i
		}
	}
}
