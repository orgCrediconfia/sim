/*
 * 13 Octubre 2003
 * Funciones para validar y elegir fechas de un calendario.
 * 
 * Funciones principales: 
 *
 * chkdate() 
 * Revisa la validez de una fecha 
 * recibe un string de fecha 
 * regresa true | false 
 * (necesita probarse)

*/


function lib_bwcheck(){ 
	//Browsercheck (needed)
	this.ver=navigator.appVersion
	this.agent=navigator.userAgent
	this.dom=document.getElementById?1:0
	this.opera5=this.agent.indexOf("Opera 5")>-1
	this.ie5=(this.ver.indexOf("MSIE 5")>-1 && this.dom && !this.opera5)?1:0;
	this.ie6=(this.ver.indexOf("MSIE 6")>-1 && this.dom && !this.opera5)?1:0;
	this.ie4=(document.all && !this.dom && !this.opera5)?1:0;
	this.ie=this.ie4||this.ie5||this.ie6
	this.mac=this.agent.indexOf("Mac")>-1
	this.ns6=(this.dom && parseInt(this.ver) >= 5) ?1:0;
	this.ns4=(document.layers && !this.dom)?1:0;
	this.bw=(this.ie6 || this.ie5 || this.ie4 || this.ns4 || this.ns6 || this.opera5)
	return this
}

var bw=new lib_bwcheck()

function MM_findObj(n, d){ 
	//v4.0
	var p,i,x; if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
	d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i < d.forms.length;i++) x=d.forms[i][n];
	for(i=0;!x&&d.layers&&i < d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function MM_showHideLayers(){ 
	//v3.0
	var i,p,v,obj,args=MM_showHideLayers.arguments;
	for (i=0; i < (args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
	if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v='hide')?'hidden':v; }
	obj.visibility=v;
	}
}
function layerWrite(id,nestref,text){
	if (bw.ns4)	{
		var lyr = (nestref)? eval('document.'+nestref+'.document.'+id+'.document') : document.layers[id].document
		lyr.open()
		lyr.write(text)
		lyr.close()
	}else if (bw.ns6){
		document.getElementById(id).innerHTML = text;
	}
	else if (bw.ie4||bw.ie5){
		document.all[id].innerHTML = text;
	}
}
var alert6,alert8,alert9;

alert6="La fecha de salida no puede ser posterior a la de regreso";
alert8="Fecha fuera de rango";
alert9="La fecha es invalida. Por favor introduzca la fecha en el formato dd/mm/aaaa";

var winx = null;
var showcalendar,showcalendar2,lastChar;
lastChar=0;


function validate(){
	var k = (bw.ns6) ? DnEvents.which : window.event.keyCode;
	
	if ( ((k< 65) || ((k >90)&&(k< 97)) || (k >122)) && (k!=233) && (k!=32) && (k!=225) && (k!=250) && (k!=237) && (k!=243) && (k!=209) && (k!=241) ){
		event.returnValue = false;
	}else{
		if ((lastChar==32)&&(k==32)){
			event.returnValue = false;	
		}
		lastChar=k;
	}	
}
//
// en lugar de ponerlo en onload lo pongo aquí para que no
// modifique el funcionamiento de la pag. principal. FAFA
//

function CloseWin(){ 
	if(winx){
		winx.close(); 
		winx = null;
	}else { 
		winx = null;
	}
}

// NO MODIFICAR EL ARREGLO DE NOMBRES DE MES: LOS NOMBRES EN INGLES SON PARA USO INTERNO DEL SCRIPT
var smallMonths = new Array ("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");
var bOculta = false;

function LayerFecha( onoff )
{
	bOculta = (onoff == "none");
	if (bw.dom || bw.ie5 || bw.ie4 ){
		if (bw.ns6){
			document.bookingEngine.returnDate.style.display=onoff;	
			document.getElementById('arrow2').style.visibility= (onoff== "")?"visible":"hidden";
			document.getElementById('returnDateTitle').style.visibility=(onoff== "")?"visible":"hidden";
		}else{
			document.bookingEngine.returnDate.style.display=onoff;	
			arrow2.style.display = onoff;
			returnDateTitle.style.display= onoff;
		}
	}
}

function changeFormat(someDate){
	var day,month,year
	var temp=someDate.split("/");
	day=temp[0];
	month=temp[1];
	year=temp[2];
	return (month+"/"+day+"/"+year);
}
	
function validaMeses(day1,month1,year1,day2,month2,year2){
	var aux;
	var dday1,dday2,mmonth1,mmonth2,yyear1,yyear2;
	dday1=parseInt(day1,10);
	dday2=parseInt(day2,10);
	mmonth1=parseInt(month1,10);
	mmonth2=parseInt(month2,10);
	yyear1=parseInt(year1,10);
	yyear2=parseInt(year2,10);
	
	if (yyear2>yyear1){
		mmonth2=mmonth2+12;
	}
	
	if (mmonth1==mmonth2){
		if (dday1<=dday2){
			return true;
		}
	}else{
		aux=mmonth2-mmonth1;
		if ((mmonth2-mmonth1<11)&&(mmonth2-mmonth1>0)){
			return true;
		}else{
			if ((mmonth2-mmonth1==11)&&(dday2<dday1)){
				return true;
			}
		}
	}
	return false;
}


function validateDate(type){
	var depDate,retDate;
	var depDateArray,retDateArray;
	var currDate;
	var tYear;
	currDate=new Date();
	
	if (type==1){
		if (checkdate(document.bookingEngine.departDate)){
			depDate=document.bookingEngine.departDate.value;	
			depDateArray=depDate.split("/");
			document.bookingEngine.departDay.value=depDateArray[0];			
			document.bookingEngine.departMonth.value=smallMonths[parseInt(depDateArray[1],10)-1];
			//	
			// Netscape empieza las fechas en 1900 por lo tanto 2001 == 101 + 1900
			// hay que agregar
			//	
			tYear = currDate.getYear()
			if (bw.ns4 || bw.ns6) {tYear += 1900;}{			
				if (validaMeses(currDate.getDate(),currDate.getMonth()+1,tYear,depDateArray[0],depDateArray[1],depDateArray[2])){
					return true;
				}else{
					alert(alert8);
				}
			}
		}
	} else if ((checkdate(document.bookingEngine.departDate))&&(checkdate(document.bookingEngine.returnDate))){
		depDate=document.bookingEngine.departDate.value;	
		retDate=document.bookingEngine.returnDate.value;
		depDateArray=depDate.split("/");
		retDateArray=retDate.split("/");
		document.bookingEngine.departDay.value=depDateArray[0];
		document.bookingEngine.departMonth.value=smallMonths[parseInt(depDateArray[1],10)-1];
		document.bookingEngine.returnDay.value=retDateArray[0];
		document.bookingEngine.returnMonth.value=smallMonths[parseInt(retDateArray[1],10)-1];
		tYear = currDate.getYear()
		
		if (bw.ns4 || bw.ns6) {
			tYear += 1900;
		}
		if ((validaMeses(currDate.getDate(),currDate.getMonth()+1,tYear,retDateArray[0],retDateArray[1],retDateArray[2]))&&(validaMeses(currDate.getDate(),currDate.getMonth()+1,tYear,depDateArray[0],depDateArray[1],depDateArray[2]))){
			return true;
		}else{
			alert(alert8);
		}
	}
	return false;
}

function checkdate(objName){
	var datefield = objName;
	if (chkdate(objName) == false) {
		datefield.select();
		alert(alert9);
		datefield.focus();
		return false;
	}else{	
		return true; 
	}
}

function chkdate(objName){

	var strDatestyle = "EU"; //European date style
	var strDate;
	var strDateArray;
	var strDay;
	var strMonth;
	var strYear;
	var intday;
	var intMonth;
	var intYear;
	var booFound = false;
	var datefield = objName;
	var strSeparatorArray = new Array("-"," ","/",".");
	var intElementNr;
	var err = 0;
	var strMonthArray = new Array(12);
	
	strMonthArray[0] = "Jan";
	strMonthArray[1] = "Feb";
	strMonthArray[2] = "Mar";
	strMonthArray[3] = "Apr";
	strMonthArray[4] = "May";
	strMonthArray[5] = "Jun";
	strMonthArray[6] = "Jul";
	strMonthArray[7] = "Aug";
	strMonthArray[8] = "Sep";
	strMonthArray[9] = "Oct";
	strMonthArray[10] = "Nov";
	strMonthArray[11] = "Dec";
	strDate = datefield.value;
	
	if (strDate.length < 1) {
		return true;
	}else if(strDate.length < 10 ){
		return false;
	}
	
	for (intElementNr = 0; intElementNr < strSeparatorArray.length; intElementNr++) {
		if (strDate.indexOf(strSeparatorArray[intElementNr]) != -1) {
			strDateArray = strDate.split(strSeparatorArray[intElementNr]);
			if (strDateArray.length != 3) {
				err = 1;
				return false;
			}else{
				strDay = strDateArray[0];
				strMonth = strDateArray[1];
				strYear = strDateArray[2];
			}
			booFound = true;
		}
	}
	
	if (booFound == false) {
		if (strDate.length>5) {
			strDay = strDate.substr(0, 2);
			strMonth = strDate.substr(2, 2);
			strYear = strDate.substr(4);
		}
	}
	if (strYear.length == 2) {
		strYear = '20' + strYear;
	}
	// US style
	if (strDatestyle == "US") {
		strTemp = strDay;
		strDay = strMonth;
		strMonth = strTemp;
	}
	
	intday = parseInt(strDay, 10);
	if (isNaN(intday)){
		err = 2;
		return false;
	}
	
	intMonth = parseInt(strMonth, 10);
	if (isNaN(intMonth)){
		err = 3;
		return false;
	}
	
	intYear = parseInt(strYear, 10);
	if (isNaN(intYear)) {
		err = 4;
		return false;
	}
	if (intMonth > 12 || intMonth < 1) {
		err = 5;
		return false;
	}
	if ((intMonth == 1 || intMonth == 3 || intMonth == 5 || intMonth == 7 || intMonth == 8 || intMonth == 10 || intMonth == 12) && (intday > 31 || intday < 1)) {
		err = 6;
		return false;
	}
	if ((intMonth == 4 || intMonth == 6 || intMonth == 9 || intMonth == 11) && (intday > 30 || intday < 1)) {
		err = 7;
		return false;
	}
	if (intMonth == 2) {
		if (intday < 1) {
			err = 8;
			return false;
		}
		if (LeapYear(intYear) == true) {
			if (intday > 29) {
				err = 9;
				return false;
			}
		}else{
			if (intday > 28){
				err = 10;
				return false;
			}
		}
	}
	return true;
}
function LeapYear(intYear) {
	if (intYear % 100 == 0) {
		if (intYear % 400 == 0){ 
			return true; 
		}
	}else{
		if ((intYear % 4) == 0){
			return true; 
		}
	}
	return false;
}


/* PopUp Calendar v2.1
© PCI, Inc.,2000 Freeware
webmaster@personal-connections.com
+1 (925) 955 1624
Permission granted for unlimited use so far
as the copyright notice above remains intact. 
*/
/* Settings. Please read readme.html file for instructions*/
sDateTextSize = '7px';
sDateTextWeight = "small";

ppcDF = "d/m/Y";
ppcMN = new Array ("Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre");
ppcWN = new Array ("Domingo","Lunes","Martes","Miércoles","Jueves","Viernes","Sábado");
ppcWE = new Array ("Dom","Lun","Mar","Mie","Jue","Vie","Sab");

ppcER = new Array ( "Required DHTML functions are not supported in this browser.",
					"Target form field is not assigned or not accessible.",
					"Sorry, the chosen date is not acceptable. Please read instructions on the page.",
					"Unknown error occured while executing this script.");
ppcArrow = new Array ("blue","green");
ppcColors = new Array ("#666666","#CCCC00");
ppcTextColors = new Array ("white","black");

/* Do not edit below this line unless you are sure what are you doing! */
sCurDate = "";
sCurDateText = "";
var ppcIE=(navigator.appName == "Microsoft Internet Explorer");
var ppcNN=(bw.ns4 || bw.ns6);
var ppcSV=null; 
var ppcUC = false;
var ppcArrowInx = 0;
var ppcTipo;
var ppcTT="<table width=\"142\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" bordercolorlight=\"#CC0099\" bordercolordark=\"#663399\">\n";
var ppcCD=ppcTT;
var MyppcFT="<font style=\"font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 10px\" ";
var ppcFC=true;
var ppcINI=null;
var ppcFIN=null;
var ppcMesLimite;
var ppcTI=false;var ppcRL=null;var ppcXC=null;var ppcYC=null;
var ppcML= [31,28,31,30,31,30,31,31,30,31,30,31];
var ppcNow=new Date();
var ppcPtr=new Date();
var hoy = new Date(); 
var fin = new Date(); 
var x= 0, y = 0;
var ppcDias = 0;

function restoreLayers(e) {
	if (ppcNN) {
		with (window.document) {
			open("text/html");
			write("<html><head><title>Restoring the layer structure...</title></head>");
			write("<body bgcolor=\"#FFFFFF\" onLoad=\"history.go(-1)\">");
			write("</body></html>");
			close();
		}
	}
}
function gotoXYCal(x,y){
	if (ppcIE) {
		var obj = document.all['PopUpCalendar'];
		obj.style.left = x;
		obj.style.top = y;
	}
	else if (bw.ns6) {
		var obj = document.getElementById('PopUpCalendar');
		obj.left = x;
		obj.top = y;
	} 
	else if (bw.ns4) {
		var obj = document.layers['PopUpCalendar'];
		obj.left = x;
		obj.top = y;
	}
} 
// no se use directamente FAFA
function getCalendar(target, color,rules,bAntes, px, py ) {
	var iMonth, iDay;
	var strDateArray;
	ppcSV = target;
	ppcRL = rules; 
	ppcArrowInx = color;
	ppcTipo = (bAntes == null)? 0:bAntes;
	ppcAnte = (bAntes == 1)? true:false;
	
	x = px; 
	y = py;
	hoy=new Date();
	if (ppcFIN ==null){
		if (hoy.getMonth() != 0) {
			if (hoy.getDate() != 1){
				fin.setFullYear(getFullYear(hoy)+1,hoy.getMonth()-1, hoy.getDate()-1);
			}else{
				fin.setFullYear(getFullYear(hoy)+1,hoy.getMonth()-2, ppcML[hoy.getMonth()-2]);
			}
		}else {
			fin.setFullYear(getFullYear(hoy),11, hoy.getDate()-1 );
		}
	}else {
		strDateArray = ppcFIN.split('/');
		if (ppcDF.charAt(0) == 'd') {// d/m/y ?
			iDay = parseInt(strDateArray[0],10);
			iMonth = parseInt(strDateArray[1],10) -1;
		}else{ // m/d/y
			iMonth = parseInt(strDateArray[0],10) -1;
			iDay = parseInt(strDateArray[1],10);
		}
		fin.setFullYear(parseInt(strDateArray[2],10),iMonth , iDay);
	}

	if (ppcINI!=null){
		strDateArray = ppcINI.split('/');
		if (ppcDF.charAt(0) == 'd') {// d/m/y ?
			iDay = parseInt(strDateArray[0],10);
			iMonth = parseInt(strDateArray[1],10) -1;
		}else{ // m/d/y
			iMonth = parseInt(strDateArray[0],10) -1;
			iDay = parseInt(strDateArray[1],10);
		}
		hoy.setFullYear(parseInt(strDateArray[2],10),iMonth , iDay);
		if (ppcDias != 0){
			fin = AddDays(hoy, ppcDias);
		}
	}
	if ((x != null) && (y != null) ) {
		gotoXYCal(x,y);
	}

	if (ppcSV.value == ""){
		if (ppcINI==null) setTimeout("setCalendar(null, null, null)",15);
		else setTimeout("setCalendar("+parseInt(strDateArray[2],10)+","+iMonth+", "+iDay+")",25);
	}else{
		strDateArray = ppcSV.value.split('/');
		if (ppcDF.charAt(0) == 'd') {// d/m/y ?
			iDay = parseInt(strDateArray[0],10);
			iMonth = parseInt(strDateArray[1],10) -1;
		}else{ // m/d/y
			iMonth = parseInt(strDateArray[0],10) -1;
			iDay = parseInt(strDateArray[1],10);
		}
		setTimeout("setCalendar("+parseInt(strDateArray[2],10)+","+iMonth+", "+iDay+")",25);
	}
	if ((ppcSV != null)&&(ppcSV)) {
		if (ppcIE) {
			var obj = document.all['PopUpCalendar'];
			obj.style.visibility = 'visible';
		} else if (bw.ns6) {
			var obj = document.getElementById('PopUpCalendar');
			obj.style.visibility = 'visible';
		} else if (bw.ns4) {
			var obj = document.layers['PopUpCalendar'];
			obj.visibility = 'show';
		} else {showError(ppcER[0]);
		}
	}
	else {showError(ppcER[1]);
	}
}
//----------usar estos
function getCalendarForFIN(target, fechaFIN, color,rules,bAntes, px, py ) 
{
	ppcINI = null;
	ppcFIN = fechaFIN;
	ppcDias = 0;
	getCalendar(target, color,rules,bAntes, px, py );
}

function getCalendarForINI(target, fechaINI, color,rules,bAntes, px, py ) 
{
	ppcINI = fechaINI;
	ppcFIN = null;
	ppcDias = 0;
	getCalendar(target, color,rules,bAntes, px, py );
}

function getCalendarForINI_FIN(target, fechaINI, dias, color,rules,bAntes, px, py ) 
{
	ppcINI = fechaINI;
	ppcFIN = null;
	ppcDias = dias;
	getCalendar(target, color,rules,bAntes, px, py );
}

function getCalendarForINI(target, fechaINI, color,rules,bAntes, px, py ) 
{
	ppcINI = fechaINI;
	ppcFIN = null;
	ppcDias = 0;
	getCalendar(target, color,rules,bAntes, px, py );
}

function getCalendarFor(target, color,rules,bAntes, px, py ) 
{
	ppcINI = null;
	ppcFIN = null;
	getCalendar(target, color,rules,bAntes, px, py );
}

function switchMonth(param) {
	var tmp = param.split("|");
	setCalendar(tmp[0],tmp[1]);
}

function moveMonth(dir) {
	var tmp,dptrYear,dptrMonth;
	tmp = sCurDate.split("|");
	dptrYear = tmp[0];
	dptrMonth = tmp[1];
	
	if (dir == 'back' ){
		
		dptrMonth--;
		if (dptrMonth < 0) {
		dptrYear--; 
		dptrMonth = 11;
	}
	
	}
	else {
		if (ppcTipo == 0){
			if ( (dptrMonth == fin.getMonth()) && (dptrYear == getFullYear(fin)) ) {return;}
		}
		
		if (ppcTipo == 1){
			if ( (dptrMonth == hoy.getMonth()) && (dptrYear == getFullYear(hoy)) ) {return;}
		}
		
		dptrMonth++;
		if (11 < dptrMonth) {
			dptrYear++; 
			dptrMonth = 0;
		}
	
	}
	setTimeout("setCalendar("+dptrYear+","+dptrMonth+", 1)",15);
}

function selectDate(param) {
	var arr = param.split("|");
	var year = arr[0];
	var month = arr[1];
	var date = arr[2];
	var ptr = parseInt(date);
	ppcPtr.setDate(ptr);
	if ((ppcSV != null)&&(ppcSV)) {
		ppcSV.value = dateFormat(year,month,date); 
		if ( ppcRL != null ) { //FAFA
			var iDay, iMonth;
			var arrRL = ppcRL.value.split("/");
			if (ppcDF.charAt(0) == 'd') {// d/m/y ?
				iDay = parseInt(arrRL[0],10);
				iMonth = parseInt(arrRL[1],10) -1;
			}else{ // m/d/y
				iMonth = parseInt(arrRL[0],10) -1;
				iDay = parseInt(arrRL[1],10);
			}
		
			if ( (parseInt(arrRL[2],10) != year ) ||
				(iMonth < month ) ||
				(iMonth == month ) && (iDay < date ) ) 
			{ 
					ppcRL.value = ppcSV.value
			}
		} 
		ppcNow.setFullYear(year, month, date);
		ppcPtr.setFullYear(year, month, 1);		
		setTimeout("updateContent()",5);
		window.close();
	}
	else {
		showError(ppcER[1]);
	}
}

function setCalendar(year,month,day) {
	if (year == null) {year = getFullYear(ppcNow);}
	if (month == null) {month = ppcNow.getMonth();}
	if (month == 1) {ppcML[1] = (isLeap(year)) ? 29 : 28;}
	if (day == null ) {day = 1;}else{ppcNow.setFullYear(year, month, day); }
	
	setSelectList(year,month);
	ppcPtr.setFullYear(year, month, 1);
	
	updateContent();
}

function updateContent() {
	generateContent();
	if (ppcIE) {
		document.all['monthDays'].innerHTML = ppcCD;
	}
	else if(bw.ns6){ 
		document.getElementById('monthDays').innerHTML = ppcCD; 
	}
	else if (bw.ns4) {
		with (document.layers['PopUpCalendar'].document.layers['monthDays'].document) {
			open("text/html");
			write("<html>\n<head>\n<title>DynDoc</title>\n</head>\n<body bgcolor=\"#FFFFFF\">\n");
			write(ppcCD);
			write("</body>\n</html>");
			close();
		}
	}
	else {
		showError(ppcER[0]);
	}
	ppcCD = ppcTT;
}

function generateContent() {
	var year = getFullYear(ppcPtr);
	var month = ppcPtr.getMonth();
	var date = 1;
	var day = ppcPtr.getDay();
	var len = ppcML[month];
	var bgr,cnt,tmp = "";
	var j,i = 0;
	var FT = MyppcFT +"color=\"#000000\"> ";
	ppcCD +="<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td colspan=\"7\"><table width=\"152\" border=\"0\" cellpadding=\"0\" cellspacing=\"2\">";
	ppcCD +="<tr class=\"bodytextB\">";
	ppcCD +="<td><a href=\"javascript:moveMonth('back');\"><img src=\"/portal/comun/img/calendario/calendarpicker_"+ppcArrow[ppcArrowInx]+"_left.gif\" border=\"0\" width=\"11\" height=\"11\"></a></td>";
	ppcCD +="<td bgcolor=\"" + ppcColors[ppcArrowInx] + "\" width=\"130\" align=\"center\"><font color=\"" + ppcTextColors[ppcArrowInx] + "\" size=\"2\" >"+sCurDateText+"</font></td>";
	ppcCD +="<td><a href=\"javascript:\" onclick=\"bCierra=false;moveMonth('forward');\"><img src=\"/portal/comun/img/calendario/calendarpicker_"+ppcArrow[ppcArrowInx]+"_right.gif\" border=\"0\" width=\"11\" height=\"11\"></a></td></tr>";
	ppcCD +="</table></td></tr>";
	
	for (j = 0; j < 7; ++j) {
		tmp += "<td align='center'><font color='" + ppcTextColors[ppcArrowInx] + "' size=\"2\">" + ppcWE[j]+"</font></td>";
	} 
	ppcCD += "<TR class=\"bodytextB\" align='center' bgcolor=\"" + ppcColors[ppcArrowInx] + "\">\n" + tmp + "</tr>\n";
	tmp = "";
	
	for (j = 0; j < 7; ++j) {
		if (date > len) {
			break;
		}
		for (i = 0; i < 7; ++i) {
			if (((j == 0)&&(i < day))||(date > len)) {
				tmp += makeCell(year,month,0);
			}else {
				tmp += makeCell(year,month,date);++date;
			}
		}
		ppcCD += "<tr class='overviewtxt'>\n" + tmp + "</tr>\n";tmp = "";
	}
	ppcCD += "</table>\n"; 
}

//datos del calendario como tal
function makeCell(year,month,date) {
	var param = "\'"+year+"|"+month+"|"+date+"\'";
	var ppcFT = MyppcFT;
	var ColorNO = "color=\"#000000\"> ";
	var ColorSI = "color=\"#0000FF\"> ";
	var color; 
	var conLiga;
	
	if (ppcTipo!=2)
	{
		conLiga = true; 
		color = ColorSI;	
	}
	else {
		conLiga = true; 
		color = ColorSI;
	}
	
	ppcFT = ppcFT+color;
	var td1 = "<td bgcolor='#DDDDDD' align='center' valign='middle' ";
	var td2 = (ppcIE || bw.ns6) ? "</font></span></td>\n" : "</font></a></td>\n";
	var evt = "onMouseUp=\"this.style.backgroundColor=\'#FFCC99\';selectDate("+param+")\" ";
	var ext = "<span Style=\"cursor: hand; cursor:pointer;\">";
	var lck = "<span Style=\"cursor: default\">";
	var lnk = "<a href=\"javascript:selectDate("+param+");\" onMouseOver=\"window.status=\' \';return true;\">";
	var cellValue = (date != 0) ? date+"" : "&nbsp;";
	
	if ((ppcNow.getDate() == date)&&(ppcNow.getMonth() == month)&&(getFullYear(ppcNow) == year)) {
		cellValue = "<b>"+cellValue+"</b>";
	}
	
	var cellCode = "";
	
	if ( (date == 0) || !conLiga){
		if (ppcIE || bw.ns6) {
			cellCode = td1+"Style=\"cursor: default\">"+lck+ppcFT+cellValue+td2;
		}else {
			cellCode = td1+">"+ppcFT+cellValue+"</font></td>\n";
		}
	}else {
		if (ppcIE || bw.ns6) {
			cellCode = td1+evt+"Style=\"cursor: hand\">"+ext+ppcFT+cellValue+td2;
		}else {
			if (date < 10) {cellValue = "&nbsp;" + cellValue + "&nbsp;";}
			cellCode = td1+">"+lnk+ppcFT+cellValue+td2;
		}
	}
	return cellCode;
}

function setSelectList(year,month) {
	var i = 0;
	var obj = null;
	sCurDate = year + "|" + month;
	sCurDateText = ppcMN[month] + " " + year;
} 

function showError(message) { 
	window.alert("[ PopUp Calendar ]\n\n" + message);
}

function isLeap(year) {
	if ((year%400==0)||((year%4==0)&&(year%100!=0))) {
		return true;
	}else {
		return false;
	}
}

function getFullYear(obj) {
	if (ppcNN) {
		return obj.getYear() + 1900;
	}else {
		return obj.getFullYear();
	}
}
function isEvenOrOdd(date) {
	if (date - 21 > 0) {
		return "e";
	}else if (date - 14 > 0) {
			return "o";
	}else if (date - 7 > 0) {
		return "e";
	}else {
		return "o";}
	}
	
function dateFormat(year,month,date) {
	if (ppcDF == null) {
		ppcDF = "m/d/Y";
	}
	var day = ppcPtr.getDay();
	var crt = "";
	var str = "";
	var chars = ppcDF.length;
	for (var i = 0; i < chars; ++i) {
		crt = ppcDF.charAt(i);
		switch (crt) {
			case "M": str += ppcMN[month]; 
				break;
			case "m": str += (month < 9) ? ("0"+(++month)) : ++month; 
				break;
			case "Y": str += year; 
				break;
			case "y": str += year.substring(2); 
				break;
			case "d": str += ((ppcDF.indexOf("m")!=-1)&&(date< 10)) ? ("0"+date) : date; 
				break;
			case "W": str += ppcWN[day]; 
				break;
			default: str += crt;
		}
	}
	return unescape(str);
}

function AddDays(fecha, dias)
{
	var year = getFullYear(fecha);
	var month = fecha.getMonth();
	var date = fecha.getDate();
	var diasMes;
	
	if (month == 1) {
		ppcML[1] = (isLeap(year)) ? 29 : 28;
	}
	date += dias;
	diasMes = ppcML[month];
	
	if (date > diasMes){
		if (month == 11) {
			month = 0;
			year += 1;
		}else {
			month += 1;
		}
		date -= diasMes;
	}
	return new Date(year,month, date);
}

function getDay(someDate)
{
	var temp=someDate.split("/");
	if (ppcDF.charAt(0) == 'd'){
		return parseInt(temp[0],10);
	}else{
		return parseInt(temp[1],10);
	}
}

function getMonth(someDate){
	var temp=someDate.split("/");
	if (ppcDF.charAt(0) == 'd'){
		return parseInt(temp[1],10);
	}else{
		return parseInt(temp[0],10);
	}
}

function getYear(someDate){
	var temp=someDate.split("/");
	return parseInt(temp[2],10);
}

function MyStrToDate(someDate){
	var day,month,year
	var temp=someDate.split("/");
	if (ppcDF.charAt(0) == 'd'){
		day=parseInt(temp[0],10);
		month=parseInt(temp[1],10)-1;
		year=parseInt(temp[2],10);
	}
	else{
		month=parseInt(temp[0],10)-1;
		day=parseInt(temp[1],10);
		year=parseInt(temp[2],10);
	}
	alert("El año es: "+ year);
	alert("La fecha es: "+ Date.UTC(year, month, day));
	return Date.UTC(year, month, day); 
}	
