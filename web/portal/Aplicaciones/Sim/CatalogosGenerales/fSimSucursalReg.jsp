<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoSucursal" precarga="SimSucursalTelefono SimSucursalAsesor SimSucursalSupervisor SimSucursalCaja">
	
	<Portal:PaginaNombre titulo="Catálogo de Sucursales" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoSucursal' parametros='IdDomicilio=${param.IdDomicilio}&IdSucursal=${param.IdSucursal}&IdDomicilioTribunal=${param.IdDomicilioTribunal}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Id Sucursal' control='etiqueta-controloculto' controlnombre='IdSucursal' controlvalor='${param.IdSucursal}' controllongitud='2' controllongitudmax='2' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomSucursal' controlvalor='${requestScope.registro.campos["NOM_SUCURSAL"]}' controllongitud='30' controllongitudmax='30' editarinicializado='true' obligatorio='true' />
		
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
		    <Portal:Url tipo='catalogo-ventana' url='/Aplicaciones/Sim/Usuarios/fSimUsuSucGer.jsp' nombreliga='Asignar Gerente' nomventana='VentanaAsignarGerente'/>
		</Portal:FormaElemento>
		<Portal:FormaElemento etiqueta='Gerente' control='etiqueta-controlreferencia' controlnombre='NomCompletoGerente' controlvalor='${requestScope.registro.campos["NOMBRE_GERENTE"]}' />
		<input type="hidden" name="CveUsuarioGerente" value='<c:out value='${requestScope.registro.campos["CVE_USUARIO_GERENTE"]}'/>' />
		
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
		    <Portal:Url tipo='catalogo-ventana' url='/Aplicaciones/Sim/Usuarios/fSimUsuSucCoo.jsp' nombreliga='Asignar Coordinador' nomventana='VentanaAsignarCoordinador'/>
		</Portal:FormaElemento>
		<Portal:FormaElemento etiqueta='Coordinador' control='etiqueta-controlreferencia' controlnombre='NomCompletoCoordinador' controlvalor='${requestScope.registro.campos["NOMBRE_COORDINADOR"]}' />
		<input type="hidden" name="CveUsuarioCoordinador" value='<c:out value='${requestScope.registro.campos["CVE_USUARIO_COORDINADOR"]}'/>' />
		
		<tr>
			<th>Tasa de Iva</th>
			<td>
				<input type='text' name='TasaIva' size='9' maxlength='9' value='<c:out value='${requestScope.registro.campos["TASA_IVA"]}'/>' onkeydown="CantidadesMonetarias();"> &#37
			</td>
		</tr>
		
		
		<Portal:FormaSeparador nombre="Direcci&oacute;n de Sucursal"/>				
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
		
		<c:if test='${(requestScope.registro != null)}'>
			<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>
				<Portal:Url tipo='catalogo' nombreliga='Dirección de Tribunales' funcion='SimCatalogoSucursalTribunales' operacion='CR' parametros='IdSucursal=${param.IdSucursal}&IdDomicilio=${param.IdDomicilio}&IdDomicilioTribunal=${param.IdDomicilioTribunal}'/>		
			</Portal:FormaElemento>
		</c:if>
		
		
		<input type="hidden" name="CodigoPostal" value='<c:out value='${registro.campos["CODIGO_POSTAL"]}' />' />
		<input type="hidden" name="IdReferPost" value='<c:out value='${requestScope.registro.campos["ID_REFER_POST"]}' />' />
		<input type="hidden" name="Colonia" value='<c:out value='${requestScope.registro.campos["NOM_ASENTAMIENTO"]}' />' />
		<input type="hidden" name="Municipio" value='<c:out value='${requestScope.registro.campos["NOM_DELEGACION"]}' />' />
		<input type="hidden" name="Ciudad" value='<c:out value='${requestScope.registro.campos["NOM_CIUDAD"]}' />' />
		<input type="hidden" name="Estado" value='<c:out value='${requestScope.registro.campos["NOM_ESTADO"]}' />' />
		<input type="hidden" name="TipoAsentamiento" value='<c:out value='${requestScope.registro.campos["TIPO_ASENTAMIENTO"]}' />' />
	    		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
		
	</Portal:Forma>	
	
	<Portal:TablaForma maestrodetallefuncion="SimSucursalTelefono" nombre="Teléfonos" funcion="SimSucursalTelefono" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Tel&eacute;fono'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Descripci&oacute;n'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaTelefono}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='150' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["TELEFONO"]}' funcion='SimSucursalTelefono' operacion='CR' parametros='IdTelefono=${registro.campos["ID_TELEFONO"]}&IdSucursal=${param.IdSucursal}&IdDomicilio=${param.IdDomicilio}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_DESC_TEL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimSucursalTelefono&OperacionCatalogo=IN&Filtro=Alta&IdSucursal=${registro.campos["ID_SUCURSAL"]}&IdDomicilio=${registro.campos["ID_DOMICILIO"]}'/>
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	<Portal:TablaForma maestrodetallefuncion="SimSucursalAsesor" nombre="Asesores" funcion="SimSucursalAsesor" operacion="BA" parametros='IdSucursal=${registro.campos["ID_SUCURSAL"]}&IdDomicilio=${registro.campos["ID_DOMICILIO"]}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Asesor'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaAsesores}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_ASESOR"]}_${registro.campos["CVE_USUARIO"]}' />		
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["CVE_USUARIO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_COMPLETO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimSucursalAsesor&OperacionCatalogo=CT&Filtro=Todos&IdSucursal=${registro.campos["ID_SUCURSAL"]}&IdDomicilio=${registro.campos["ID_DOMICILIO"]}'/>
			<Portal:Boton tipo='submit' etiqueta='Baja' />
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	<Portal:TablaForma maestrodetallefuncion="SimSucursalSupervisor" nombre="Supervisores" funcion="SimSucursalSupervisor" operacion="BA" parametros='IdSucursal=${registro.campos["ID_SUCURSAL"]}&IdDomicilio=${registro.campos["ID_DOMICILIO"]}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Supervisor'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaSupervisores}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_SUPERVISOR"]}' />		
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["ID_SUPERVISOR"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_COMPLETO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimSucursalSupervisor&OperacionCatalogo=CT&Filtro=Todos&IdSucursal=${registro.campos["ID_SUCURSAL"]}&IdDomicilio=${registro.campos["ID_DOMICILIO"]}'/>
			<Portal:Boton tipo='submit' etiqueta='Baja' />
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
	<Portal:TablaForma maestrodetallefuncion="SimSucursalCaja" nombre="Cajas" funcion="SimSucursalCaja" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='1000' valor='Caja'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaCajas}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='700' valor='CAJA ${registro.campos["ID_CAJA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Añadir una caja a la sucursal' url='/ProcesaCatalogo?Funcion=SimSucursalCaja&OperacionCatalogo=AL&IdSucursal=${registro.campos["ID_SUCURSAL"]}&IdDomicilio=${registro.campos["ID_DOMICILIO"]}'/>
		</Portal:FormaBotones>		
	</Portal:TablaForma>	
</Portal:Pagina>
