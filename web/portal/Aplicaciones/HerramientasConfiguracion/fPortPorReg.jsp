<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionPortalDefault">
	<Portal:PaginaNombre titulo="Portales Default" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionPortalDefault'>

		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave Grupo Empresa' control='etiqueta-controloculto' controlnombre='CveGpoEmpresa' controlvalor='${param.CveGpoEmpresa}' />
		<Portal:FormaElemento etiqueta='Clave Portal Default' control='Texto' controlnombre='CvePortalDefault' controlvalor='${requestScope.registro.campos["CVE_PORTAL_DEFAULT"]}' controllongitud='30' controllongitudmax='30' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Url por Default' control='Texto' controlnombre='UrlDirDefault' controlvalor='${requestScope.registro.campos["URL_DIR_DEFAULT"]}' controllongitud='50' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Clave Aplicaci&oacute;n' control='selector' controlnombre='CveAplicacion' controlvalor='${requestScope.registro.campos["CVE_APLICACION"]}' editarinicializado='true' obligatorio='true' campoclave="CVE_APLICACION" campodescripcion="CVE_APLICACION" datosselector='${requestScope.ListaAplicaciones}' evento="onchange=\" frmRegistro.CveUsuario.value=\'\'; document.getElementById(\'CveUsuario\').innerHTML=\'\'; document.getElementById(\'NomCompleto\').innerHTML=\'\'; \"" />

		<!--TRABAJANDO-->
		
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
			<!--<Portal:Url tipo='catalogo-ventana' url='/ProcesaCatalogo?Funcion=GeneralesUsuario&OperacionCatalogo=CT&Filtro=UsuarioAplicacion&CveGpoEmpresa=${param.CveGpoEmpresa}&CveAplicacion=' nombreliga='Usuario que tiene asignada la aplicaci&oacute;n seleccionada' nomventana='VentanaCp'/> -->
			<a id="VenUsuario" href="javascript:MM_openBrWindow('/portal/ProcesaCatalogo?Funcion=GeneralesUsuario&OperacionCatalogo=CT&Filtro=UsuarioAplicacion&CveGpoEmpresa=&CveAplicacion=&Ventana=Si','VentanaCp','scrollbars=yes,resizable=yes,width=700,height=400') ">Usuario que tiene asignada la aplicaci&oacute;n seleccionada</a>		
		</Portal:FormaElemento>
		
		<Portal:FormaElemento etiqueta='Clave Usuario' control='etiqueta-controlreferencia' controlnombre='CveUsuario' controlvalor='${requestScope.registro.campos["CVE_USUARIO"]}' />
		<Portal:FormaElemento etiqueta='Nombre' control='etiqueta-controlreferencia' controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' />
		
		<!--TRABAJANDO-->

		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
			<Portal:Url tipo='catalogo-ventana' url='/ProcesaCatalogo?Funcion=HerramientasConfiguracionPortal&OperacionCatalogo=CT&Filtro=PortalesEstilos&CveGpoEmpresa=${param.CveGpoEmpresa}' nombreliga='Asginaci&oacute;n del portal y el estilo' nomventana='VentanaCp'/>
		</Portal:FormaElemento>
		<Portal:FormaElemento etiqueta='Clave Portal' control='etiqueta-controlreferencia' controlnombre='CvePortal' controlvalor='${requestScope.registro.campos["CVE_PORTAL"]}' />
		<Portal:FormaElemento etiqueta='Estilo' control='etiqueta-controlreferencia' controlnombre='CveEstilo' controlvalor='${requestScope.registro.campos["CVE_ESTILO"]}' />

		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
		<input type="hidden" name="CveUsuario" value='<c:out value='${requestScope.registro.campos["CVE_USUARIO"]}'/>' />
		<input type="hidden" name="CvePortal" value='<c:out value='${requestScope.registro.campos["CVE_PORTAL"]}'/>' />
		<input type="hidden" name="CveEstilo" value='<c:out value='${requestScope.registro.campos["CVE_ESTILO"]}'/>' />				
	</Portal:Forma>
	
	<script> 
		fAgregaCampo('CveUsuario', 'Clave de usuario');
		fAgregaCampo('CvePortal', 'Clave de portal');
	</script>
	
	<script>
		//Combo
		combo = document.frmRegistro.CveAplicacion;
			
		//Modificamos el onchange del combo
		combo.onchange = fLimpiaCampos;
		
		//Modificamos el onclick del link
		VenUsuario.onclick = fAgregaUrl;
		
		function fAgregaUrl(){
			
			//Hallamos el value del combo
			sValue = combo.options[combo.selectedIndex].value;
			
			
			sEmpresa = document.frmRegistro.CveGpoEmpresa.value;
			
			//Agregamos a la cadena el valor de la empresa
			sCadena = VenUsuario.href;
			sPunto  = sCadena.indexOf("&CveGpoEmpresa=");
			sPrimeraCadena = sCadena.substring(0, sPunto+15);
			sCadenaValor = sPrimeraCadena + sEmpresa ;
			//Agregamos a la cadena el valor de la aplicacion
			sCadenaParcial = sCadenaValor+'&CveAplicacion='+ sValue ;
			sSegundaCadena = sCadena.substring(sCadena.indexOf("&Ventana"));
			sCadena = sCadenaParcial + sSegundaCadena ;
			
			VenUsuario.href = sCadena;
			
		}
		function fLimpiaCampos(){
		
			document.frmRegistro.CveUsuario.value= '';
			document.getElementById('CveUsuario').innerHTML='';
			document.getElementById('NomCompleto').innerHTML='';
		
		}
	</script>

</Portal:Pagina>
