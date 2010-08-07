<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="HerramientasConfiguracionCatalogoColor">

	<Portal:PaginaNombre titulo="Catálogo de colores" subtitulo=""/>
	
	<BODY onload=capture()>
	
    <TABLE border=0 cellPadding=0 cellSpacing=0>
       <TBODY>
         <TR>
	       <TD>
	        <IMG border=0 height=0 src="" width=140> 
	         <IMG border=0 height=256  src="/portal/Aplicaciones/HerramientasConfiguracion/img/colorwheel.jpg" width=256> 
	 		<IMG border=0 height=0 src="" width=140> 
	       </TD>
         </TR>
	     <TR>
	       <TD align=center><BR>
		      <FORM name=f>
		       	 <INPUT name=t size=27> 
		       	 <P><center>Color elegido</P>
		       	 <INPUT name=codigo size=27> 
		      </FORM>
           </TD>
         </TR>
       </TBODY>
    </TABLE>
	
	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionCatalogoColor' url="ProcesaCatalogo?Funcion=HerramientasConfiguracionCatalogoColor&OperacionCatalogo=MO">
																
		<Portal:FormaSeparador nombre="Colores"/>
		<Portal:FormaElemento etiqueta='Empresa' control='Texto' controlnombre='CveGpoEmpresa' controlvalor='${requestScope.registro.campos["CVE_GPO_EMPRESA"]}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Portal' control='Texto' controlnombre='CvePortal' controlvalor='${requestScope.registro.campos["CVE_PORTAL"]}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true' />											 
		<Portal:FormaElemento etiqueta='Color general del menú' control='Texto' controlnombre='NormalColor' controlvalor='${requestScope.registro.campos["NORMAL_COLOR"]}' controllongitud='15' controllongitudmax='20' editarinicializado='true' obligatorio='true' evento='onmousedown="fCapturarNormalColor()"'/>
		<Portal:FormaElemento etiqueta='Color del menú cuando es señalado por el ratón' control='Texto' controlnombre='OnColor' controlvalor='${requestScope.registro.campos["ON_COLOR"]}' controllongitud='15' controllongitudmax='20' editarinicializado='true' obligatorio='true' evento='onmousedown="fCapturarOnColor()"'/>
		<Portal:FormaElemento etiqueta='Color del menú cuando se le da click con el ratón' control='Texto' controlnombre='DownColor' controlvalor='${requestScope.registro.campos["DOWN_COLOR"]}' controllongitud='15' controllongitudmax='20' editarinicializado='true' obligatorio='true' evento='onmousedown="fCapturarDownColor()"'/>
		<Portal:FormaElemento etiqueta='Color de la sombra superior' control='Texto' controlnombre='BorderColorDark' controlvalor='${requestScope.registro.campos["BORDER_COLOR_DARK"]}' controllongitud='15' controllongitudmax='20' editarinicializado='true' obligatorio='true' evento='onmousedown="fCapturarBorderColorDark()"'/>
		<Portal:FormaElemento etiqueta='Color de la sombra inferior' control='Texto' controlnombre='BorderColorLight' controlvalor='${requestScope.registro.campos["BORDER_COLOR_LIGHT"]}' controllongitud='15' controllongitudmax='20' editarinicializado='true' obligatorio='true' evento='onmousedown="fCapturarBorderColorLight()"'/>
		<Portal:FormaElemento etiqueta='Color general de la letra' control='Texto' controlnombre='FontDark' controlvalor='${requestScope.registro.campos["FONT_DARK"]}' controllongitud='15' controllongitudmax='20' editarinicializado='true' obligatorio='true' evento='onmousedown="fCapturarFontDark()"'/>
		<Portal:FormaElemento etiqueta='Color de la letra cuando es elegido con el ratón' control='Texto' controlnombre='FontLight' controlvalor='${requestScope.registro.campos["FONT_LIGHT"]}' controllongitud='15' controllongitudmax='20' editarinicializado='true' obligatorio='true' evento='onmousedown="fCapturarFontLight()"'/>

		<input type="hidden" name="Color" value=''/>
		
		<Portal:FormaBotones>
					<input type="button"  value="Aceptar" onclick='javascript:fAceptar();'>
		</Portal:FormaBotones>
						
		
	</Portal:Forma>
	
	
	<script>
		
		
		function fAceptar(){
				document.frmRegistro.submit();
				setTimeout("window.close()",100);
		}
		
		addary = new Array();           //red
		addary[0] = new Array(0,1,0);   //red green
		addary[1] = new Array(-1,0,0);  //green
		addary[2] = new Array(0,0,1);   //green blue
		addary[3] = new Array(0,-1,0);  //blue
		addary[4] = new Array(1,0,0);   //red blue
		addary[5] = new Array(0,0,-1);  //red
		addary[6] = new Array(255,1,1);
		clrary = new Array(360);
		for(i = 0; i < 6; i++)
		for(j = 0; j < 60; j++) {
			clrary[60 * i + j] = new Array(3);
			for(k = 0; k < 3; k++) {
				clrary[60 * i + j][k] = addary[6][k];
				addary[6][k] += (addary[i][k] * 4);
        	}
		}
		
		var tunIex=navigator.appName=="Microsoft Internet Explorer"?true:false;
		function capture() {
		
			document.frmRegistro.NormalColor.style.backgroundColor='<c:out value='${registro.campos["NORMAL_COLOR"]}'/>';
			document.frmRegistro.OnColor.style.backgroundColor='<c:out value='${registro.campos["ON_COLOR"]}'/>';
			document.frmRegistro.DownColor.style.backgroundColor='<c:out value='${registro.campos["DOWN_COLOR"]}'/>';
			document.frmRegistro.BorderColorDark.style.backgroundColor='<c:out value='${registro.campos["BORDER_COLOR_DARK"]}'/>';
			document.frmRegistro.BorderColorLight.style.backgroundColor='<c:out value='${registro.campos["BORDER_COLOR_LIGHT"]}'/>';
			document.frmRegistro.FontLight.style.backgroundColor='<c:out value='${registro.campos["FONT_LIGHT"]}'/>';
			document.frmRegistro.FontDark.style.backgroundColor='<c:out value='${registro.campos["FONT_DARK"]}'/>';
			
<<<<<<< .mine
			if(document.layers) {
				layobj = document.layers['ContenidoPagina'];
				layobj.document.captureEvents(Event.MOUSEMOVE);
				layobj.document.onmousemove = moved;
				
				
				layobj.document.ondblclick = campo;
=======
			var layobj = document.getElementById("ContenidoPagina");
			
			if(!tunIex) {
			layobj.addEventListener("mousemove", moved, true);
			layobj.addEventListener("campo", campo, true);
			ondblclick=campo;
>>>>>>> .r4327
			}
			else {
			layobj.document.onmousemove = moved;
			layobj.document.ondblclick = campo;
  		 	}			
		}
		
		function moved(e) {
			y = 4 * ((!tunIex)?e.layerX:event.offsetX);
			x = 4 * ((!tunIex)?e.layerY:event.offsetY);
			sx = x - 512;
			sy = y - 512;
			qx = (sx < 0)?0:1;
			qy = (sy < 0)?0:1;
			q = 2 * qy + qx;
			quad = new Array(-180,360,180,0);
			xa = Math.abs(sx);
			ya = Math.abs(sy);
			d = ya * 45 / xa;
			if(ya > xa) d = 90 - (xa * 45 / ya);
			deg = Math.floor(Math.abs(quad[q] - d));
			n = 0;
			sx = Math.abs(x - 512);
			sy = Math.abs(y - 512);
			r = Math.sqrt((sx * sx) + (sy * sy));
			if(x == 512 & y == 512) {
				c = "000000";
			}
			else {
				for(i = 0; i < 3; i++) {
					r2 = clrary[deg][i] * r / 256;
					if(r > 256) r2 += Math.floor(r - 256);
					if(r2 > 255) r2 = 255;
					n = 256 * n + Math.floor(r2);
				}
				c = n.toString(16);
				while(c.length < 6) c = "0" + c;
			}
			if(!tunIex){
			document.f.t.value = "#" + c;
			document.getElementById("ContenidoPagina").style.backgroundColor = "#" + c;
			}
			else{	
			document.all["ContenidoPagina"].document.f.t.value = "#" + c;
			document.all["ContenidoPagina"].style.backgroundColor = "#" + c;
			}
		    return false;
	    }
	    
	   
	    function campo(e) {
			y = 4 * ((!tunIex)?e.layerX:event.offsetX);
			x = 4 * ((!tunIex)?e.layerY:event.offsetY);
			sx = x - 512;
			sy = y - 512;
			qx = (sx < 0)?0:1;
			qy = (sy < 0)?0:1;
			q = 2 * qy + qx;
			quad = new Array(-180,360,180,0);
			xa = Math.abs(sx);
			ya = Math.abs(sy);
			d = ya * 45 / xa;
			if(ya > xa) d = 90 - (xa * 45 / ya);
			deg = Math.floor(Math.abs(quad[q] - d));
			n = 0;
			sx = Math.abs(x - 512);
			sy = Math.abs(y - 512);
			r = Math.sqrt((sx * sx) + (sy * sy));
			if(x == 512 & y == 512) {
				c = "000000";
			}
			else {
				for(i = 0; i < 3; i++) {
					r2 = clrary[deg][i] * r / 256;
					if(r > 256) r2 += Math.floor(r - 256);
					if(r2 > 255) r2 = 255;
					n = 256 * n + Math.floor(r2);
				}
				c = n.toString(16);
				while(c.length < 6) c = "0" + c;
			}
			if(!tunIex) {
						document.frmRegistro.Color.value = "#" + c;
						document.f.codigo.value = "#" + c;
						valorcolor = document.frmRegistro.Color.value;
			}
			else{
						document.frmRegistro.Color.value = "#" + c;
						document.f.codigo.value = "#" + c;
						valorcolor = document.frmRegistro.Color.value;		
			}		
		return false;
		}
	
		function fCapturarNormalColor(){
			if(document.f.codigo.value==''){
				document.frmRegistro.NormalColor.style.backgroundColor='<c:out value='${registro.campos["NORMAL_COLOR"]}'/>';
			}else{
				document.frmRegistro.NormalColor.value=valorcolor;
				document.frmRegistro.NormalColor.style.backgroundColor=valorcolor;
			}
		}
		
		function fCapturarOnColor(){
			if(document.f.codigo.value==''){
				document.frmRegistro.OnColor.style.backgroundColor='<c:out value='${registro.campos["ON_COLOR"]}'/>';	
			}else{
				document.frmRegistro.OnColor.value=valorcolor;
				document.frmRegistro.OnColor.style.backgroundColor=valorcolor;
			}
		}
		
		function fCapturarDownColor(){
			if(document.f.codigo.value==''){
				document.frmRegistro.DownColor.style.backgroundColor='<c:out value='${registro.campos["DOWN_COLOR"]}'/>';	
			}else{
				document.frmRegistro.DownColor.value=valorcolor;
				document.frmRegistro.DownColor.style.backgroundColor=valorcolor;
			}
		}
		
		function fCapturarBorderColorDark(){
			if(document.f.codigo.value==''){
				document.frmRegistro.BorderColorDark.style.backgroundColor='<c:out value='${registro.campos["BORDER_COLOR_DARK"]}'/>';	
			}else{
				document.frmRegistro.BorderColorDark.value=valorcolor;
				document.frmRegistro.BorderColorDark.style.backgroundColor=valorcolor;
			}
		}
		
		function fCapturarBorderColorLight(){
			if(document.f.codigo.value==''){
				document.frmRegistro.BorderColorLight.style.backgroundColor='<c:out value='${registro.campos["BORDER_COLOR_LIGHT"]}'/>';	
			}else{
				document.frmRegistro.BorderColorLight.value=valorcolor;
				document.frmRegistro.BorderColorLight.style.backgroundColor=valorcolor;
			}
		}
		
		function fCapturarFontLight(){
			if(document.f.codigo.value==''){
				document.frmRegistro.FontLight.style.backgroundColor='<c:out value='${registro.campos["FONT_LIGHT"]}'/>';	
			}else{
				document.frmRegistro.FontLight.value=valorcolor;
				document.frmRegistro.FontLight.style.backgroundColor=valorcolor;
			}
		}
		
		function fCapturarFontDark(){
			if(document.f.codigo.value==''){
				document.frmRegistro.FontDark.style.backgroundColor='<c:out value='${registro.campos["FONT_DARK"]}'/>';	
			}else{
				document.frmRegistro.FontDark.value=valorcolor;
				document.frmRegistro.FontDark.style.backgroundColor=valorcolor;
			}
		}
	</script>			
	
</Portal:Pagina>