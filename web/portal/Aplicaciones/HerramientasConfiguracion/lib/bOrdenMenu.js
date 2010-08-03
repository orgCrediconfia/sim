//Librería que sirve para elementos de una lista que se deseen ordenar a base de acciones como "sube elemento" o "baja elemento"

//configuracion
elemMenu = new Array(); //Arreglo de elementos que se despliegan en pantalla
valoMenu = new Array(); //Arreglo de valores correspondientes a los elementos desplegados
posyMenu = new Array(); //Arreglo de posiciones en la pantalla correspondientes a los valores
layeMenu = new Array(); //Nombres de Layer correspondientes a los valores
var separacion = 25;    //Número de pixeles de separación entre layers
var numElem = 0 ;		//Número de elementos de la lista
var indiceSelec = 0;    //Número de Indice Seleccionado 
var posySelec = 0;      //Posición del elemento Seleccionado 
var indiceRegistroAnterior = 0; //Número de Indice del elemento anterior
var indiceRegistroSiguiente = 0; //Número de Indice del elemento siguiente
var sBgColorSelec        = "#000000";
var sColorFuenteSelec    = "#FFFFFF";
var sBgColorUnSelec      = "#FFFFFF";
var sColorFuenteUnSelec  = "#000000";

var iPosSuperior = 270;	//POSICION SUPERIOR EN DONDE SE EMPIEZAN A COLOCAR LAS OPCIONES

//Deteccion navegadores
var ns4arriba = (document.layers) ? 1 : 0;
var ie4arriba = (document.all) ? 1 : 0;
//variable indice comodín
var i;

//Funciones para cambiar el color de fondo de leyendas
function setColorFondoSelecionado(color){
    sBgColorSelec=color;
}
function setColorFondoDeselecionado(color){
    sBgColorUnSelec=color;
}
function setColorFuenteSelecionado(color){
    sColorFuenteSelec=color;
}
function setColorFuenteDeselecionado(color){
    sColorFuenteUnSelec=color;
}
function setSeparacion(isepa){
    separacion=isepa;
}
function setPosicionSuperior(iPosSup){
	iPosSuperior = iPosSup;
}


//Cambia el color del elemento seleccionado
function CambiaColor(indice) {
     if (ns4arriba) {
     	cambiacolor_explorer(indice);
     } 
	 else if (ie4arriba) {
     	cambiacolor_explorer(indice);
	 }
}

//Sube el elemento seleccionado a la posición inmediata anterior
function subeElemento() {
     if (ns4arriba) {
     	sube_explorer();
     }
	 else if (ie4arriba) {
     	sube_explorer();
	 }
}

//Baja el elemento seleccionado a la posición inmediata siguiente
function bajaElemento() {
     if (ns4arriba) {
     	baja_explorer();
     }
	 else if (ie4arriba) {
     	baja_explorer();
	 }
}

//Función de despliegue inicial de las opciones precargadas, corre solo la primera vez
function muestraOpcMenu() {
     for (i = 0; i < numElem; ++ i) {
	    if (ns4arriba) {
     		if (i == 0) {
     			document.write("<layer name=\"dot"+ i +"\" left=\"15\" ");
     			document.write("top=\"15\" visibility=\"show\">");
     			document.write(elemMenu[i]+"</layer>");
     		} else {
     			document.write("<layer name=\"dot"+ i +"\" left=\"15\" ");
     			document.write("top=\"15\" visibility=\"show\">");
     			document.write(elemMenu[i]+"</layer>");
     		}
     	} else if (ie4arriba) {
     		if (i == 0) {
     			document.write("<div id=\"dot"+ i +"\" style=\"POSITION: ");
     			document.write("absolute; Z-INDEX: "+ i +"; VISIBILITY: ");
     			document.write("visible; TOP: 15px; LEFT: 15px; background: "+sBgColorSelec+"; COLOR:"+sColorFuenteSelec+"; cursor: hand;\" onclick=\"CambiaColor("+i+");\">");
     			document.write(elemMenu[i]+"</div>");
     		} else {
     			document.write("<div id=\"dot"+ i +"\" style=\"POSITION: ");
     			document.write("absolute; Z-INDEX: "+ i +"; VISIBILITY: ");
     			document.write("visible; TOP: 15px; LEFT: 15px; background: "+sBgColorUnSelec+"; COLOR:"+sColorFuenteUnSelec+"; cursor: hand;\" onclick=\"CambiaColor("+i+");\">");
     			document.write(elemMenu[i]+"</div>");
     		}			
     	}
     }
     
     if (ns4arriba) {
		inicia_netscape();
     } 
	 else if (ie4arriba) {
		inicia_explorer();
     }
	 posySelec = separacion + iPosSuperior; 
}

//Barrido Inicial desde Netscape
function inicia_netscape() {
	for (i = 0; i < numElem; ++ i) {
		document.layers["dot"+i].style.top = ((i*1)+1)*(separacion*1);
		document.layers["dot"+i].style.left = 150;
	}
}

//Barrido Inicial desde Explorer
function inicia_explorer() {
	for (i = 0; i < numElem; ++ i) {
		document.all["dot"+i].style.pixelTop = (((i*1)+1)*(separacion*1)) + iPosSuperior;
		document.all["dot"+i].style.pixelLeft = 170;
		posyMenu[i]=document.all["dot"+i].style.pixelTop;
		layeMenu[i]="dot"+i;		
	}
}


//Cambia Color en Explorer
function cambiacolor_explorer(indice) {
	for (i = 0; i < numElem; ++ i) {	
	    if (indice==i){
				document.all["dot"+i].style.color = sColorFuenteSelec;
				document.all["dot"+i].style.background = sBgColorSelec;
				indiceSelec = i;
				posySelec = document.all["dot"+i].style.pixelTop;	
		} else {
				document.all["dot"+i].style.color = sColorFuenteUnSelec;
				document.all["dot"+i].style.background = sBgColorUnSelec;
		}
	}
}

//Funcion para subir elementos en Explorer
function sube_explorer(){
	if (((posySelec*1)-separacion)> iPosSuperior ){
		sLayerAnterior = obtenAnterior_explorer(posySelec);
		sLayerActual   = obtenActual_explorer(posySelec);
		
		elemAnterior = elemMenu[indiceRegistroAnterior];
		valoAnterior = valoMenu[indiceRegistroAnterior];	
		posyAnterior = posyMenu[indiceRegistroAnterior];
		layeAnterior = layeMenu[indiceRegistroAnterior];
		
		elemMenu[indiceRegistroAnterior] = elemMenu[indiceSelec];
		valoMenu[indiceRegistroAnterior] = valoMenu[indiceSelec];	
		posyMenu[indiceRegistroAnterior] = posyAnterior;		
		layeMenu[indiceRegistroAnterior] = layeMenu[indiceSelec];	
		
		elemMenu[indiceSelec] = elemAnterior;
		valoMenu[indiceSelec] = valoAnterior;
		posyMenu[indiceSelec] = posyMenu[indiceSelec];
		layeMenu[indiceSelec] = layeAnterior;
			
		document.all[sLayerActual].style.pixelTop = (posySelec*1)-separacion;
		document.all[sLayerAnterior].style.pixelTop = (posySelec*1);
		//indiceSelec = 0;
		posySelec = (posySelec*1)-separacion;
	}
	else{
	    alert("no se puede subir más el elemento");
	}
}

//Funcion para bajar elementos en Explorer
function baja_explorer(){
	if (((posySelec*1) + separacion) < ( iPosSuperior + (separacion*(numElem+1)))){
		sLayerSiguiente = obtenSiguiente_explorer(posySelec);
		sLayerActual   = obtenActual_explorer(posySelec);
		
		elemSiguiente = elemMenu[indiceRegistroSiguiente];
		valoSiguiente = valoMenu[indiceRegistroSiguiente];	
		posySiguiente = posyMenu[indiceRegistroSiguiente];
		layeSiguiente = layeMenu[indiceRegistroSiguiente];
		
		elemMenu[indiceRegistroSiguiente] = elemMenu[indiceSelec];
		valoMenu[indiceRegistroSiguiente] = valoMenu[indiceSelec];	
		posyMenu[indiceRegistroSiguiente] = posySiguiente;		
		layeMenu[indiceRegistroSiguiente] = layeMenu[indiceSelec];	
		
		elemMenu[indiceSelec] = elemSiguiente;
		valoMenu[indiceSelec] = valoSiguiente;
		posyMenu[indiceSelec] = posyMenu[indiceSelec];
		layeMenu[indiceSelec] = layeSiguiente;
			
		document.all[sLayerActual].style.pixelTop = (posySelec*1)+separacion;
		document.all[sLayerSiguiente].style.pixelTop = (posySelec*1);	
		posySelec = (posySelec*1)+separacion;
	}else{
    	alert("no se puede bajar más el elemento");
	}   	
}


//funcion para obtener el elemento anterior en explorer
function obtenAnterior_explorer(posyActual){
    posyTemporal = posyActual;
	layeSeleccionado ="";
	for (i = 0; i < numElem; ++ i) {
	    if (((posyTemporal*1)-separacion) == (posyMenu[i]*1)){
		    posyTemporal = posyMenu[i]*1;
			layeSeleccionado = layeMenu[i];
			indiceRegistroAnterior = i;
		}
	}
	return layeSeleccionado;
}

//funcion para obtener el elemento siguiente
function obtenSiguiente_explorer(posyActual){
    posyTemporal = posyActual;
	layeSeleccionado ="";
	for (i = 0; i < numElem; i++) {
	    if (((posyTemporal*1)+separacion)==(posyMenu[i]*1)){
		    posyTemporal = posyMenu[i]*1;
			layeSeleccionado = layeMenu[i];
			indiceRegistroSiguiente = i;			
			break;
		}
	}
	return layeSeleccionado;
}

//Obtiene el nombre del indice actual selecionado en Explorer
function obtenActual_explorer(posyActual){
    posyTemporal = posyActual;
	layeSeleccionado ="";
	for (i = 0; i < numElem; ++ i) {
	    if ((posyTemporal*1)==(posyMenu[i]*1)){
			layeSeleccionado = layeMenu[i];
			indiceSelec = i;
		}
	}
	return layeSeleccionado;
}

//Función que agrega Items a lasa opciones de menu
function agregaOpcion(titulo,valor) {
	elemMenu[numElem] = titulo;
	valoMenu[numElem] = valor;
	numElem++;
}

//Función que retorna un arreglo ordenado con los nuevos valores.
function getArregloOrdenado(titulo,valor) {
	return valoMenu;
}
