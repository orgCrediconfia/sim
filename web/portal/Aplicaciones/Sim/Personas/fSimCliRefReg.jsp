<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimClienteReferencia" precarga="SimReferenciaTelefono">

	<Portal:PaginaNombre titulo="Referencias del Cliente" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimClienteReferencia' parametros='IdPersona=${param.IdPersona}&IdReferencia=${param.IdReferencia}&IdDomicilio=${param.IdDomicilio}&IdExConyuge=${param.IdExConyuge}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<Portal:FormaElemento etiqueta='Apellido Paterno' control='Texto' controlnombre='ApPaterno' controlvalor='${requestScope.registro.campos["AP_PATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Apellido Materno' control='Texto' controlnombre='ApMaterno' controlvalor='${requestScope.registro.campos["AP_MATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Primer Nombre' control='Texto' controlnombre='Nombre1' controlvalor='${requestScope.registro.campos["NOMBRE_1"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Segundo Nombre' control='Texto' controlnombre='Nombre2' controlvalor='${requestScope.registro.campos["NOMBRE_2"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Nombre Completo' control='Texto' controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' controllongitud='60' controllongitudmax='256' editarinicializado='true' obligatorio='true' />
		<tr>
			<th>Tipo de Referencia</th>
			<td>
				<select name='TipoReferencia' size='1'>
					<option value='TELEFONICA'   >TELEFONICA</option>
					<option value='VECINAL'   >VECINAL</option>
				</select>
			</td>
		</tr>
		<script> BuscaSelectOpcion(document.frmRegistro.TipoReferencia,'<c:out value='${requestScope.registro.campos["TIPO_REFERENCIA"]}'/>'); </script>
		
		
		<Portal:FormaSeparador nombre="Direcci&oacute;n"/>				
		<Portal:FormaElemento etiqueta='Calle' control='Texto' controlnombre='Calle' controlvalor='${requestScope.registro.campos["CALLE"]}' controllongitud='100' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='N&uacute;mero interior' control='Texto' controlnombre='NumeroInt' controlvalor='${requestScope.registro.campos["NUMERO_INT"]}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='N&uacute;mero exterior' control='Texto' controlnombre='NumeroExt' controlvalor='${requestScope.registro.campos["NUMERO_EXT"]}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
		    <Portal:Url tipo='catalogo-ventana' url='/ProcesaCatalogo?Funcion=SimCatalogoCodigoPostal&OperacionCatalogo=IN&Filtro=Inicio' nombreliga='C&oacute;digo Postal' nomventana='VentanaCodigoPostal'/>
		</Portal:FormaElemento>
		
		<Portal:FormaElemento etiqueta='C.P.' control='etiqueta-controlreferencia' controlnombre='CodigoPostal' controlvalor='${requestScope.registro.campos["CODIGO_POSTAL"]}'/>
		<Portal:FormaElemento etiqueta='Colonia' control='etiqueta-controlreferencia' controlnombre='Colonia' controlvalor='${requestScope.registro.campos["NOM_ASENTAMIENTO"]}'/>
		<Portal:FormaElemento etiqueta='Municipio' control='etiqueta-controlreferencia' controlnombre='Municipio' controlvalor='${requestScope.registro.campos["NOM_DELEGACION"]}'/>
		<Portal:FormaElemento etiqueta='Ciudad' control='etiqueta-controlreferencia' controlnombre='Ciudad' controlvalor='${requestScope.registro.campos["NOM_CIUDAD"]}'/>
		<Portal:FormaElemento etiqueta='Estado' control='etiqueta-controlreferencia' controlnombre='Estado' controlvalor='${requestScope.registro.campos["NOM_ESTADO"]}'/>
		
		<input type="hidden" name="CodigoPostal" value='<c:out value='${registro.campos["CODIGO_POSTAL"]}' />' />
		<input type="hidden" name="IdReferPost" value='<c:out value='${requestScope.registro.campos["ID_REFER_POST"]}' />' />
		<input type="hidden" name="Colonia" value='<c:out value='${requestScope.registro.campos["NOM_ASENTAMIENTO"]}' />' />
		<input type="hidden" name="Municipio" value='<c:out value='${requestScope.registro.campos["NOM_DELEGACION"]}' />' />
		<input type="hidden" name="Ciudad" value='<c:out value='${requestScope.registro.campos["NOM_CIUDAD"]}' />' />
		<input type="hidden" name="Estado" value='<c:out value='${requestScope.registro.campos["NOM_ESTADO"]}' />' />
		<input type="hidden" name="TipoAsentamiento" value='<c:out value='${requestScope.registro.campos["TIPO_ASENTAMIENTO"]}' />' />
	    	
		<c:if test='${requestScope.registro != null}'>
		    <Portal:Calendario2 etiqueta='Fecha de Verificaci&oacute;n' contenedor='frmRegistro' controlnombre='FechaVerificacion' controlvalor='${requestScope.registro.campos["FECHA_VERIFICACION"]}'  esfechasis='true'/>
			
			<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
			    <Portal:Url tipo='catalogo-ventana' url='/Aplicaciones/Sim/Usuarios/fSimUsuSucCoo.jsp' nombreliga='Asignar usuario' nomventana='VentanaAsignarUsuario'/>
			</Portal:FormaElemento>
			<Portal:FormaElemento etiqueta='Verificado por' control='etiqueta-controlreferencia' controlnombre='NomCompletoCoordinador' controlvalor='${requestScope.registro.campos["NOM_COMPLETO_VERIFICADOR"]}'/>
			<input type="hidden" name="CveUsuarioCoordinador" value='<c:out value='${requestScope.registro.campos["CVE_VERIFICADOR"]}'/>' />
			
			<Portal:FormaElemento etiqueta='Resultado de la verficaci&oacute;n' control='selector' controlnombre='IdResultadoVerificacion' controlvalor='${requestScope.registro.campos["ID_RESULTADO_VERIFICACION"]}' editarinicializado='true' obligatorio='true' campoclave="ID_RESULTADO_VERIFICACION" campodescripcion="NOM_RESULTADO_VERIFICACION" datosselector='${requestScope.ListaResultado}'/>			
		</c:if>	
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
	
	<Portal:TablaForma maestrodetallefuncion="SimReferenciaTelefono" nombre="Teléfonos" funcion="SimReferenciaTelefono" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Tel&eacute;fono'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Descripci&oacute;n'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaTelefono}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='150' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["TELEFONO"]}' funcion='SimReferenciaTelefono' operacion='CR' parametros='IdTelefono=${registro.campos["ID_TELEFONO"]}&IdReferencia=${param.IdReferencia}&IdPersona=${param.IdPersona}&IdDomicilio=${param.IdDomicilio}&IdExConyuge=${param.IdExConyuge}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_DESC_TEL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimReferenciaTelefono&OperacionCatalogo=IN&Filtro=Alta&IdReferencia=${registro.campos["ID_REFERENCIA"]}&IdPersona=${registro.campos["ID_PERSONA"]}&IdDomicilio=${registro.campos["ID_DOMICILIO"]}&IdExConyuge=${param.IdExConyuge}'/>
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	
	<script>
		document.frmRegistro.ApPaterno.onblur = fLlenaNombre;
		document.frmRegistro.ApMaterno.onblur = fLlenaNombre;
		document.frmRegistro.Nombre1.onblur = fLlenaNombre;
		document.frmRegistro.Nombre2.onblur = fLlenaNombre;
		function fLlenaNombre(){
			sNomCompleto = document.frmRegistro.Nombre1.value +' '+ document.frmRegistro.Nombre2.value +' '+ document.frmRegistro.ApPaterno.value +' '+ document.frmRegistro.ApMaterno.value;
			document.frmRegistro.NomCompleto.value = sNomCompleto;
		}
	</script>
	
</Portal:Pagina>	
