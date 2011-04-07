<%@ page language="java" import="com.rapidsist.portal.configuracion.Usuario"%>
<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimPrestamo" precarga="SimPrestamoParticipante SimPrestamoEstatusHistorico SimPrestamoActividadRequisito SimPrestamoCargoComisionCliente SimPrestamoMontoCliente SimPrestamoAccesorioOrdenCliente SimPrestamoDocumentacion SimPrestamoGarantia SimPrestamoFlujoEfectivo">
	<Portal:PaginaNombre titulo="Pr&eacute;stamo" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimPrestamo'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<%Usuario usuario = (Usuario)session.getAttribute("Usuario");%>
		
		<input type="hidden" name="IdPrestamo" value='<c:out value='${requestScope.registro.campos["ID_PRESTAMO"]}'/>' />
		<c:if test='${(requestScope.registro.campos["ID_PRODUCTO"] != null)}'>
			<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CvePrestamo' controlvalor='${requestScope.registro.campos["CVE_PRESTAMO"]}' editarinicializado='false'/>
		</c:if>
		
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
		   	<a id="AsignarCliente" href="javascript:fAsignarCliente();">Asignar Cliente</a>			
		</Portal:FormaElemento>
		
		<Portal:FormaElemento etiqueta='Cliente' control='etiqueta-controlreferencia' controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' editarinicializado='false'/>
		<input type="hidden" name="IdPersona" value='<c:out value='${requestScope.registro.campos["ID_CLIENTE"]}'/>' />
		<input type="hidden" name="IdPersonaAlta" value='<c:out value='${requestScope.registro.campos["ID_PERSONA"]}'/>' />
		
		
	
		<c:if test='${(requestScope.registro != null)}'>
			<Portal:FormaElemento etiqueta='Sucursal' control='selector' controlnombre='IdSucursal' controlvalor='${requestScope.registro.campos["ID_SUCURSAL"]}' editarinicializado='false' obligatorio='true' campoclave="ID_SUCURSAL" campodescripcion="NOM_SUCURSAL" datosselector='${requestScope.ListaSucursal}'/>
		</c:if>
		
		<c:if test='${(requestScope.registro == null)}'>
			<Portal:FormaElemento etiqueta='Sucursal' control='etiqueta-controlreferencia' controlnombre='NomSucursal' controlvalor='${requestScope.registro.campos["NOM_SUCURSAL"]}' />
			<input type="hidden" name="IdSucursal" value='<c:out value='${requestScope.registro.campos["ID_SUCURSAL"]}'/>' />
		</c:if>
		
		<Portal:FormaElemento etiqueta='Asesor crédito' control='etiqueta-controlreferencia' controlnombre='NomAsesorCredito' controlvalor='${requestScope.registro.campos["NOMBRE_ASESOR"]}' />
		<input type="hidden" name="IdAsesorCredito" value='<c:out value='${requestScope.registro.campos["CVE_ASESOR_CREDITO"]}'/>' />
		
		<c:if test='${(requestScope.registro != null)}'>
		
			<Portal:FormaSeparador nombre="Producto"/>
			
			<%if (!usuario.sCvePerfilActual.equals("COORDINADOR")){%>
				<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
				    	<a id="AsignarProducto" href="javascript:fAsignarProducto();">Asignar Producto</a>			
				</Portal:FormaElemento>
			<%}%>
			
			<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controlreferencia' controlnombre='IdProducto' controlvalor='${requestScope.registro.campos["ID_PRODUCTO"]}' editarinicializado='false'/>
			<input type="hidden" name="IdProducto" value='<c:out value='${requestScope.registro.campos["ID_PRODUCTO"]}'/>' />
			<Portal:FormaElemento etiqueta='Nombre' control='etiqueta-controlreferencia' controlnombre='NomProducto' controlvalor='${requestScope.registro.campos["NOM_PRODUCTO"]}' controllongitud='25' controllongitudmax='30' editarinicializado='true' obligatorio='true'/>
			<Portal:FormaElemento etiqueta='Aplica a' control='etiqueta-controlreferencia' controlnombre='AplicaA' controlvalor='${requestScope.registro.campos["APLICA_A"]}' controllongitud='25' controllongitudmax='30' editarinicializado='true' obligatorio='true'/>
			<input type="hidden" name="AplicaA" value='<c:out value='${requestScope.registro.campos["APLICA_A"]}'/>' />
			
			
			<%if (!usuario.sCvePerfilActual.equals("COORDINADOR")){%>
				<Portal:FormaElemento etiqueta='Periodicidad' control='selector' controlnombre='IdPeriodicidadProducto' controlvalor='${requestScope.registro.campos["ID_PERIODICIDAD_PRODUCTO"]}' editarinicializado='true' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>
				<Portal:FormaElemento etiqueta='M&eacute;todo de c&aacute;lculo del producto' control='selector' controlnombre='CveMetodo' controlvalor='${requestScope.registro.campos["CVE_METODO"]}' editarinicializado='true' obligatorio='false' campoclave="CVE_METODO" campodescripcion="NOM_METODO" datosselector='${requestScope.ListaMetodo}'/>			
			<%}%>
			
			<%if (usuario.sCvePerfilActual.equals("COORDINADOR")){%>
				<Portal:FormaElemento etiqueta='Periodicidad' control='selector' controlnombre='IdPeriodicidadProducto' controlvalor='${requestScope.registro.campos["ID_PERIODICIDAD_PRODUCTO"]}' editarinicializado='false' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>
				<Portal:FormaElemento etiqueta='M&eacute;todo de c&aacute;lculo del producto' control='selector' controlnombre='CveMetodo' controlvalor='${requestScope.registro.campos["CVE_METODO"]}' editarinicializado='false' obligatorio='false' campoclave="CVE_METODO" campodescripcion="NOM_METODO" datosselector='${requestScope.ListaMetodo}'/>		
			<%}%>
	
			<c:if test='${(requestScope.registro.campos["ID_PERSONA"] != null)}'>	
			
				<Portal:FormaElemento etiqueta='N&uacute;mero de ciclo' control='etiqueta-controlreferencia' controlnombre='NumCiclo' controlvalor='${requestScope.registro.campos["NUM_CICLO"]}' controllongitud='3' controllongitudmax='3' editarinicializado='true' obligatorio='true'/>
				
				<input type="hidden" name="NumCiclo" value='<c:out value='${requestScope.registro.campos["NUM_CICLO"]}'/>' />
				
				<!--Cuando el usuario tiene un perfil de COORDINADOR el campo Plazo pueden ser editado hasta antes de la etapa de Autorización de montos por riesgo-->
				<%if (usuario.sCvePerfilActual.equals("COORDINADOR")){%>
					<c:if test='${(registroVerificarEtapa.campos["ID_ETAPA_PRESTAMO"] == null)}'>
						<Portal:FormaElemento etiqueta='Plazo' control='Texto' controlnombre='Plazo' controlvalor='${requestScope.registro.campos["PLAZO"]}' controllongitud='3' controllongitudmax='3' editarinicializado='false' obligatorio='true' validadato='numerico'/>
					</c:if>
					<c:if test='${(registroVerificarEtapa.campos["ID_ETAPA_PRESTAMO"] != null)}'>
						<Portal:FormaElemento etiqueta='Plazo' control='Texto' controlnombre='Plazo' controlvalor='${requestScope.registro.campos["PLAZO"]}' controllongitud='3' controllongitudmax='3' editarinicializado='true' obligatorio='true' validadato='numerico'/>
					</c:if>
				<%}%>
				
				<!--Cuando el usuario tiene un perfil diferente al de COORDINADOR el campo Plazo pueden ser editado-->
				<%if (!usuario.sCvePerfilActual.equals("COORDINADOR")){%>
					<Portal:FormaElemento etiqueta='Plazo' control='Texto' controlnombre='Plazo' controlvalor='${requestScope.registro.campos["PLAZO"]}' controllongitud='3' controllongitudmax='3' editarinicializado='true' obligatorio='true' validadato='numerico'/>
				<%}%>
				
				
				<%if (!usuario.sCvePerfilActual.equals("COORDINADOR")){%>
					<tr>
						<th>Forma de distribuci&oacute;n de pago</th>
						<td>
							<select name='IdFormaDistribucion' size='1'  >
								<option value='null'></option>
								<option value='1'  >Saldos - Accesorios y capital</option>
								<option value='2'   >Saldos - Capital y accesorio</option>
							</select>
						</td>
					</tr>
					<c:if test='${(requestScope.registro != null)}'>
						<c:if test='${(requestScope.registro.campos["ID_CLIENTE"] != null)}'>	 
							<script> BuscaSelectOpcion(document.frmRegistro.IdFormaDistribucion,'<c:out value='${requestScope.registro.campos["ID_FORMA_DISTRIBUCION"]}'/>'); </script>
						</c:if> 
					</c:if>
				<%}%>
				<%if (usuario.sCvePerfilActual.equals("COORDINADOR")){%>
					<Portal:FormaElemento etiqueta='Forma de distribuci&oacute;n de pago' control='etiqueta-controlreferencia' controlnombre='IdFormaDistribucion' controlvalor='${requestScope.registro.campos["FORMA_APLICACION"]}' controllongitud='25' controllongitudmax='30' editarinicializado='true' obligatorio='true'/>
				<%}%>
				
				<!--Cuando el usuario tiene un perfil diferente al COORDINADOR los campos Tipo de la tasa, valor de la tasa, periodicidad de la tasa y papeles pueden ser editados-->
				<%if (!usuario.sCvePerfilActual.equals("COORDINADOR")){%>
					<tr>
						<th>Tasa</th>
						<td>
							<select name='TipoTasa' size='1' onchange='fTasa();' >
								<option value='null'></option>
								<option value='No indexada'   >No indexada</option>
								<option value='Si indexada'   >S&iacute; indexada</option>
							</select>
						</td>
					</tr>
					<c:if test='${(requestScope.registro != null)}'>
						<c:if test='${(requestScope.registro.campos["ID_CLIENTE"] != null)}'>	
							<script> BuscaSelectOpcion(document.frmRegistro.TipoTasa,'<c:out value='${requestScope.registro.campos["TIPO_TASA"]}'/>'); </script>
						</c:if> 
					</c:if>
					<Portal:FormaElemento etiqueta='Valor de la tasa' control='Texto' controlnombre='ValorTasa' controlvalor='${requestScope.registro.campos["VALOR_TASA"]}' controllongitud='6' controllongitudmax='6' editarinicializado='true' obligatorio='false' validadato='cantidades'/>
					<Portal:FormaElemento etiqueta='Periodicidad de la tasa' control='selector' controlnombre='IdPeriodicidadTasa' controlvalor='${requestScope.registro.campos["ID_PERIODICIDAD_TASA"]}' editarinicializado='true' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>	
					<Portal:FormaElemento etiqueta='Papeles' control='selector' controlnombre='IdTasaReferencia' controlvalor='${requestScope.registro.campos["ID_TASA_REFERENCIA"]}' editarinicializado='true' obligatorio='false' campoclave="ID_TASA_REFERENCIA" campodescripcion="NOM_TASA_REFERENCIA" datosselector='${requestScope.ListaPapel}'/>
				<%}%>
				
				<!--Cuando el usuario tiene un perfil de COORDINADOR los campos Tipo de la tasa, valor de la tasa, periodicidad de la tasa y papeles pueden ser editados hasta antes de la etapa de Autorización de montos por riesgo-->
				<%if (usuario.sCvePerfilActual.equals("COORDINADOR")){%>
					<c:if test='${(registroVerificarEtapa.campos["ID_ETAPA_PRESTAMO"] == null)}'>
						<Portal:FormaElemento etiqueta='Tasa' control='Texto' controlnombre='TipoTasa' controlvalor='${requestScope.registro.campos["TIPO_TASA"]}' controllongitud='6' controllongitudmax='6' editarinicializado='false' obligatorio='false' validadato='cantidades'/>
						<Portal:FormaElemento etiqueta='Valor de la tasa' control='Texto' controlnombre='ValorTasa' controlvalor='${requestScope.registro.campos["VALOR_TASA"]}' controllongitud='6' controllongitudmax='6' editarinicializado='false' obligatorio='false' validadato='cantidades'/>
						<Portal:FormaElemento etiqueta='Periodicidad de la tasa' control='selector' controlnombre='IdPeriodicidadTasa' controlvalor='${requestScope.registro.campos["ID_PERIODICIDAD_TASA"]}' editarinicializado='false' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>	
						<Portal:FormaElemento etiqueta='Papeles' control='selector' controlnombre='IdTasaReferencia' controlvalor='${requestScope.registro.campos["ID_TASA_REFERENCIA"]}' editarinicializado='false' obligatorio='false' campoclave="ID_TASA_REFERENCIA" campodescripcion="NOM_TASA_REFERENCIA" datosselector='${requestScope.ListaPapel}'/>
					</c:if>
					<c:if test='${(registroVerificarEtapa.campos["ID_ETAPA_PRESTAMO"] != null)}'>
						<tr>
							<th>Tasa</th>
							<td>
								<select name='TipoTasa' size='1' onchange='fTasa();' >
									<option value='null'></option>
									<option value='No indexada'   >No indexada</option>
									<option value='Si indexada'   >S&iacute; indexada</option>
								</select>
							</td>
						</tr>
						<c:if test='${(requestScope.registro != null)}'>
							<c:if test='${(requestScope.registro.campos["ID_CLIENTE"] != null)}'>	
								<script> BuscaSelectOpcion(document.frmRegistro.TipoTasa,'<c:out value='${requestScope.registro.campos["TIPO_TASA"]}'/>');</script> 
							</c:if> 
						</c:if>
						<Portal:FormaElemento etiqueta='Valor de la tasa' control='Texto' controlnombre='ValorTasa' controlvalor='${requestScope.registro.campos["VALOR_TASA"]}' controllongitud='6' controllongitudmax='6' editarinicializado='true' obligatorio='false' validadato='cantidades'/>
						<Portal:FormaElemento etiqueta='Periodicidad de la tasa' control='selector' controlnombre='IdPeriodicidadTasa' controlvalor='${requestScope.registro.campos["ID_PERIODICIDAD_TASA"]}' editarinicializado='true' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>	
						<Portal:FormaElemento etiqueta='Papeles*' control='selector' controlnombre='IdTasaReferencia' controlvalor='${requestScope.registro.campos["ID_TASA_REFERENCIA"]}' editarinicializado='true' obligatorio='false' campoclave="ID_TASA_REFERENCIA" campodescripcion="NOM_TASA_REFERENCIA" datosselector='${requestScope.ListaPapel}'/>
					</c:if>
				<%}%>
				
				<Portal:FormaElemento etiqueta='Monto m&aacute;ximo' control='etiqueta-controlreferencia' controlnombre='MontoMaximo' controlvalor='${requestScope.registro.campos["MONTO_MAXIMO"]}' controllongitud='15' controllongitudmax='15' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
				<input type="hidden" name="MontoMaximo" value='<c:out value='${requestScope.registro.campos["MONTO_MAXIMO"]}'/>' />
				<input type="hidden" name="MontoMinimo" value='<c:out value='${requestScope.registro.campos["MONTO_MINIMO"]}'/>' />
				<Portal:FormaElemento etiqueta='Porcentaje de flujo de caja a financiar' control='etiqueta-controlreferencia' controlnombre='PorcFlujoCaja' controlvalor='${requestScope.registro.campos["PORC_FLUJO_CAJA"]}' controllongitud='6' controllongitudmax='6' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
				<input type="hidden" name="PorcFlujoCaja" value='<c:out value='${requestScope.registro.campos["PORC_FLUJO_CAJA"]}'/>' />
				
				</c:if>
				<c:if test='${(requestScope.registro.campos["ID_ETAPA_PRESTAMO"] != "16")}'>
					<c:if test='${(requestScope.registro.campos["ID_PRODUCTO"] != null)}'>
						<Portal:FormaElemento etiqueta='Estatus del pr&eacute;stamo' control='selector' controlnombre='IdEstatusPrestamo' controlvalor='${requestScope.registro.campos["ID_ETAPA_PRESTAMO"]}' editarinicializado='false' obligatorio='false' campoclave="ID_ETAPA_PRESTAMO" campodescripcion="NOM_ESTATUS_PRESTAMO" datosselector='${requestScope.ListaEstatusPrestamo}'/>	
						<input type="hidden" name="EstatusPrestamo" value='<c:out value='${requestScope.registro.campos["ID_ETAPA_PRESTAMO"]}'/>' />
					</c:if>
				</c:if>
				<c:if test='${(requestScope.registro.campos["ID_ETAPA_PRESTAMO"] == "16")}'>
					<Portal:FormaElemento etiqueta='Estatus del pr&eacute;stamo' control='Texto' controlnombre='IdEtapaPrestamo' controlvalor='${requestScope.registro.campos["NOM_ESTATUS_PRESTAMO"]}' controllongitud='6' controllongitudmax='6' editarinicializado='false' obligatorio='false'/>		
				</c:if>
		
		</c:if>
		
		<Portal:FormaBotones>
			<c:if test='${(requestScope.registro.campos["ID_ETAPA_PRESTAMO"] != "16")}'>
				<c:if test='${(requestScope.registro.campos["ID_PERSONA"] == null)}'>
					<input type='button' name='Aceptar' value='Aceptar' onClick='fAsignaClienteGrupo()'/>
				</c:if>
				<c:if test='${(requestScope.registro.campos["ID_PERSONA"] != null)}'>
					<c:if test='${(requestScope.registro.campos["ID_PRODUCTO"] == null)}'>
						<input type='button' name='Aceptar' value='Aceptar' onClick='fAceptar()'/>
					</c:if>
					<c:if test='${(requestScope.registro.campos["ID_PRODUCTO"] != null)}'>
						<input type='button' name='Aceptar' value='Aceptar' onClick='fModificar()'/>
					</c:if>
				</c:if>
				<c:if test='${(requestScope.registro.campos["ID_PRODUCTO"] != null)}'>
					<input type='button' name='Baja' value='Baja' onClick='fBaja()'/>
				</c:if>
			</c:if>
		</Portal:FormaBotones>
		
	</Portal:Forma>	
	
	<c:if test='${(requestScope.registro.campos["ID_PRODUCTO"] != null)}'>
		
		<Portal:TablaForma maestrodetallefuncion="SimPrestamoParticipante" nombre="Participantes del cr&eacute;dito" funcion="SimPrestamoParticipante" operacion="BA">
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='150' valor='Participante'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
			</Portal:TablaListaTitulos>	
			<c:forEach var="registro" items="${requestScope.ListaParticipante}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='150' valor=''>					
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NOM_TIPO_PERSONA"]}' funcion='SimPrestamoParticipante' operacion='CR' parametros='CveTipoPersona=${registro.campos["CVE_TIPO_PERSONA"]}&IdPrestamo=${registro.campos["ID_PRESTAMO"]}&IdProducto=${registro.campos["ID_PRODUCTO"]}'/>
					</Portal:Columna>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_COMPLETO"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
			</Portal:FormaBotones>		
		</Portal:TablaForma>
		
		<Portal:TablaForma maestrodetallefuncion="SimPrestamoActividadRequisito" nombre="Actividades o requisito del préstamo" funcion="SimPrestamoActividadRequisito" operacion="BA">
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
				<Portal:Columna tipovalor='texto' ancho='250' valor='Actividad o requisito'/>
				<Portal:Columna tipovalor='texto' ancho='250' valor='Estatus del préstamo'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de registro'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de realizada'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Estatus'/>	
			</Portal:TablaListaTitulos>	
			<c:forEach var="registro" items="${requestScope.ListaActividadRequisito}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_ACTIVIDAD_REQUISITO"]}' funcion='SimPrestamoActividadRequisito' operacion='CR' parametros='IdActividadRequisito=${registro.campos["ID_ACTIVIDAD_REQUISITO"]}&IdEstatusPrestamo=${registro.campos["ID_ETAPA_PRESTAMO"]}&IdPrestamo=${registro.campos["ID_PRESTAMO"]}&IdProducto=${registro.campos["ID_PRODUCTO"]}&AplicaA=${requestScope.registro.campos["APLICA_A"]}'/>
					</Portal:Columna>
					<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_ACTIVIDAD_REQUISITO"]}'/>
					<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_ESTATUS_PRESTAMO"]}'/>
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_REGISTRO"]}'/>
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_REALIZADA"]}'/>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["ESTATUS"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<c:if test='${(requestScope.registro.campos["ID_ETAPA_PRESTAMO"] != "16")}'>
					<c:if test='${(requestScope.registro.campos["ID_GRUPO"] == null)}'>
						<Portal:Boton tipo='url' etiqueta='Regresar etapa' url='/ProcesaCatalogo?Funcion=SimPrestamoActividadEtapaReproceso&OperacionCatalogo=AL&IdPrestamo=${requestScope.registro.campos["ID_PRESTAMO"]}&IdEtapaPrestamo=${requestScope.registro.campos["ID_ETAPA_PRESTAMO"]}'/>
					</c:if>
				</c:if>
			</Portal:FormaBotones>		
		</Portal:TablaForma>
		
		<Portal:TablaForma maestrodetallefuncion="SimPrestamoCargoComisionCliente" nombre="Cargos y comisiones del Cliente" funcion="SimPrestamoCargoComisionCliente" operacion="AL" parametros="&IdPrestamo=${param.IdPrestamo}&IdCliente=${param.IdCliente}">
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Cargo o comisi&oacute;n'/>
				<Portal:Columna tipovalor='texto' ancho='180' valor='Forma de aplicaci&oacute;n'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Cargo inicial'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Porcentaje del monto'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Cantidad fija'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Valor'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Unidad'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Periodicidad'/>
			</Portal:TablaListaTitulos>	
			<c:forEach var="registro" items="${requestScope.ListaCargoComision}">
				<Portal:TablaListaRenglon>
					<c:if test='${(registro.campos["CARGO_INICIAL"] != "0")}'>
						<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_CARGO_COMISION"]}'/>
						<input type='hidden' name='IdCargoComision' 	value='<c:out value='${registro.campos["ID_CARGO_COMISION"]}'/>'>
						<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_ACCESORIO"]}'/>	
						<Portal:FormaElemento etiqueta='' control='selector-horizontal' controlnombre='IdFormaAplicacion' controlvalor='${registro.campos["ID_FORMA_APLICACION"]}' editarinicializado='false' obligatorio='false' campoclave="ID_FORMA_APLICACION" campodescripcion="NOM_FORMA_APLICACION" datosselector='${requestScope.ListaFormaAplicacion}'/>	
						<Portal:FormaElemento etiqueta='' control='Texto-horizontal'    controlnombre='CargoInicial' controlvalor='${registro.campos["CARGO_INICIAL"]}' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
						<td>
							<input type='text' name='PorcentajeMonto' size='9' maxlength='9' value='<c:out value='${registro.campos["PORCENTAJE_MONTO"]}'/>' onkeydown="CantidadesMonetarias();"> &#37
							<script>fAgregaCampo('PorcentajeMonto', '');</script>
						</td>
						<Portal:FormaElemento etiqueta='' control='Texto-horizontal'    controlnombre='CantidadFija' controlvalor='${registro.campos["CANTIDAD_FIJA"]}' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
						<Portal:FormaElemento etiqueta='' control='Texto-horizontal'    controlnombre='Valor' controlvalor='${registro.campos["VALOR"]}' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
						<Portal:FormaElemento etiqueta='' control='selector-horizontal' controlnombre='IdUnidad' controlvalor='${registro.campos["ID_UNIDAD"]}' editarinicializado='true' obligatorio='false' campoclave="ID_UNIDAD" campodescripcion="NOM_UNIDAD" datosselector='${requestScope.ListaUnidad}'/>	
						<Portal:FormaElemento etiqueta='' control='selector-horizontal' controlnombre='IdPeriodicidad' controlvalor='${registro.campos["ID_PERIODICIDAD"]}' editarinicializado='true' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>	
					</c:if>	
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<c:if test='${(requestScope.registro.campos["ID_ETAPA_PRESTAMO"] != "16")}'>
					<Portal:Boton tipo='submit' etiqueta='Aceptar' />
				</c:if>
			</Portal:FormaBotones>		
		</Portal:TablaForma>
		
		<Portal:TablaForma maestrodetallefuncion="SimPrestamoMontoCliente" nombre="Montos por cliente" funcion="SimPrestamoMontoCliente" operacion="AL" parametros="IdPrestamo=${param.IdPrestamo}">
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
				<Portal:Columna tipovalor='texto' ancho='250' valor='Cliente'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Monto solicitado'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Monto autorizado'/>
			</Portal:TablaListaTitulos>	
			<c:forEach var="registro" items="${requestScope.ListaMontoCliente}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_CLIENTE"]}' funcion='SimPrestamoActividadRequisito' operacion='CR' parametros='IdActividadRequisito=${registro.campos["ID_ACTIVIDAD_REQUISITO"]}&IdPrestamo=${registro.campos["ID_PRESTAMO"]}&IdProducto=${param.IdProducto}'/>
					</Portal:Columna>
					<input type='hidden' name='IdCliente' value='<c:out value='${registro.campos["ID_CLIENTE"]}'/>'>
					<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_COMPLETO"]}'/>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''>		
						<input type='text' name='MontoSolicitado' size='22' maxlength='22' value='<c:out value='${registro.campos["MONTO_SOLICITADO"]}'/>'>
					</Portal:Columna>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["MONTO_AUTORIZADO"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<c:if test='${(requestScope.registro.campos["ID_ETAPA_PRESTAMO"] != "16")}'>
					<Portal:Boton tipo='submit' etiqueta='Aceptar' />
				</c:if>
			</Portal:FormaBotones>		
		</Portal:TablaForma>
		
		<Portal:TablaForma maestrodetallefuncion="SimPrestamoAccesorioOrdenCliente" nombre="Orden de Accesorios del Cliente" funcion="SimPrestamoAccesorioOrdenCliente" operacion="AL" parametros="IdPrestamo=${param.IdPrestamo}">
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
				<Portal:Columna tipovalor='texto' ancho='250' valor='Accesorio'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Orden'/>
			</Portal:TablaListaTitulos>	
			<c:forEach var="registro" items="${requestScope.ListaAccesorio}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_ACCESORIO"]}'/>
					<input type='hidden' name='IdAccesorio' value='<c:out value='${registro.campos["ID_ACCESORIO"]}'/>'>
					<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_ACCESORIO"]}'/>
					<Portal:Columna tipovalor='texto' ancho='100%' valor=''>		
						<input type='text' name='Orden' size='2' maxlength='3' value='<c:out value='${registro.campos["ORDEN"]}'/>'>
					</Portal:Columna>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<c:if test='${(requestScope.registro.campos["ID_ETAPA_PRESTAMO"] != "16")}'>
					<Portal:Boton tipo='submit' etiqueta='Aceptar' />
				</c:if>
			</Portal:FormaBotones>		
		</Portal:TablaForma>
		
		<c:if test='${(requestScope.registro.campos["ID_GRUPO"] == null)}'>
			<c:if test='${(registroEtapaDocumentos.campos["ID_ETAPA_PRESTAMO"] != null)}'>
				<Portal:TablaForma maestrodetallefuncion="SimPrestamoDocumentacion" nombre="Documentos" funcion="SimPrestamoDocumentacion" operacion="BA">
					<Portal:TablaListaTitulos>
						<Portal:Columna tipovalor='texto' ancho='80' valor='Clave'/>
						<Portal:Columna tipovalor='texto' ancho='250' valor='Documento'/>
						<Portal:Columna tipovalor='texto' ancho='100%' valor='Archivo'/>
					</Portal:TablaListaTitulos>	
					<c:forEach var="registro" items="${requestScope.ListaDocumentacion}">
						<Portal:TablaListaRenglon>
							<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["ID_DOCUMENTO"]}'/>
							<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_DOCUMENTO"]}'/>
							<td  width='100%' align='left'  nowrap  >
								<a id="AsignarGrupo" href="javascript:fReporte('<c:out value='${registro.campos["CVE_FUNCION"]}'/>');"><c:out value='${registro.campos["NOM_DOCUMENTO"]}'/></a>	
							</td>
						</Portal:TablaListaRenglon>
					</c:forEach>
					<Portal:FormaBotones>
					</Portal:FormaBotones>		
				</Portal:TablaForma>
			</c:if>	
		</c:if>
		
		<Portal:TablaForma maestrodetallefuncion="SimPrestamoGarantia" nombre="Garant&iacute;as" funcion="SimPrestamoGarantia" operacion="BA" parametros='IdPrestamo=${param.IdPrestamo}'>
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Garant&iacute;a'/>
			</Portal:TablaListaTitulos>
			<c:forEach var="registro" items="${requestScope.ListaGarantia}">		
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_GARANTIA"]}' />		
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_GARANTIA"]}'/>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["DESCRIPCION"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<c:if test='${(requestScope.registro.campos["ID_ETAPA_PRESTAMO"] != "16")}'>
					<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimPrestamoGarantia&OperacionCatalogo=CT&Filtro=Disponibles&IdPrestamo=${requestScope.registro.campos["ID_PRESTAMO"]}&IdPersona=${requestScope.registro.campos["ID_CLIENTE"]}'/>
					<Portal:Boton tipo='submit' etiqueta='Baja' />
				</c:if>
			</Portal:FormaBotones>				
		</Portal:TablaForma>
		
		<!--TABLA FORMA DE CAPTURA-->
		<form name="frmTablaForma" method="post" action="/portal/ProcesaCatalogo?Funcion=SimPrestamoFlujoEfectivo&OperacionCatalogo=AL&IdPrestamo=<c:out value='${registro.campos["ID_PRESTAMO"]}'/>" enctype="multipart/form-data">
		<h3>Archivos</h3>
		<table border='0'>
			<c:forEach var="registro" items="${requestScope.ListaArchivo}">	
				
				
				<tr>
				   <th><c:out value='${registro.campos["NOM_ARCHIVO"]}'/></th> 
				   <td>
				   	<a href="/portal/MuestraArchivo?IdPrestamo=<c:out value='${registro.campos["ID_PRESTAMO"]}'/>&IdArchivoFlujo=<c:out value='${registro.campos["ID_ARCHIVO_FLUJO"]}'/>&UrlArchivo=<c:out value='${registro.campos["URL_ARCHIVO"]}'/>"><c:out value='${registro.campos["URL_ARCHIVO"]}'/></a>
				   	
				   <td>
				   <td>
					<input type='hidden' name='UrlAnterior<c:out value='${registro.campos["ID_ARCHIVO_FLUJO"]}'/>' value='<c:out value='${registro.campos["URL_ARCHIVO"]}'/>' >
				   </td>
				</tr>
				<tr>
			   	   <th>Asignar archivo</th>
				   <td>
					<input type=FILE name='Url<c:out value='${registro.campos["ID_ARCHIVO_FLUJO"]}'/>' size='70' maxlength='100' value=''  >
				    </td>
				</tr>	
					
			</c:forEach>
		</table>
		<c:if test='${(requestScope.registro.campos["ID_ETAPA_PRESTAMO"] != "16")}'>
			<div class='SeccionBotones'>
				<input type="submit" name="btnSubmit" value="Alta"/>			
			</div>	
		</c:if>			
		</form>
		<br/>	
		
	</c:if>						
	<script>
	
		<c:if test='${(requestScope.registro.campos["ID_CLIENTE"] != null)}'>	
			if (document.frmRegistro.TipoTasa.value == "No indexada"){
				document.frmRegistro.IdTasaReferencia.disabled = true;
				document.frmRegistro.ValorTasa.disabled = false;
				document.frmRegistro.IdPeriodicidadTasa.disabled = false;
				document.frmRegistro.IdTasaReferencia.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.ValorTasa.style.backgroundColor = 'white';
				document.frmRegistro.IdPeriodicidadTasa.style.backgroundColor = 'white';
			}
			
			if (document.frmRegistro.TipoTasa.value == "Si indexada"){
				document.frmRegistro.IdTasaReferencia.disabled = false;
				document.frmRegistro.ValorTasa.disabled = true;
				document.frmRegistro.IdPeriodicidadTasa.disabled = true;
				document.frmRegistro.IdTasaReferencia.style.backgroundColor = 'white';
				document.frmRegistro.ValorTasa.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.IdPeriodicidadTasa.style.backgroundColor = '#CCCCCC';
			}
		</c:if>
		
		function fAsignaClienteGrupo(){
			if (document.frmRegistro.IdPersona.value == ""){
				alert("Debe asignar un cliente");
			}else {
					document.frmRegistro.action="ProcesaCatalogo?Funcion=SimPrestamo&OperacionCatalogo=AL&IdPersona="+document.frmRegistro.IdPersona.value;
					document.frmRegistro.submit();	
				
			}
		}
				
		function fAceptar(){
			if (document.frmRegistro.AplicaA.value == 'Individual'){
				if (document.frmRegistro.TipoTasa.value == "Si indexada"){
					if (document.frmRegistro.IdTasaReferencia.value != "null"){
						document.frmRegistro.IdTasaReferencia.disabled = false;
						document.frmRegistro.IdPeriodicidadTasa.disabled = false;
						document.frmRegistro.action="ProcesaCatalogo?Funcion=SimPrestamoProductoCiclo&OperacionCatalogo=AL&IdPersona="+document.frmRegistro.IdPersonaAlta.value;
						document.frmRegistro.submit();	
					}else {
						alert("Ingrese el papel de la tasa indexada");
					}				
				}else{
					document.frmRegistro.IdTasaReferencia.disabled = false;
					document.frmRegistro.IdPeriodicidadTasa.disabled = false;
					document.frmRegistro.action="ProcesaCatalogo?Funcion=SimPrestamoProductoCiclo&OperacionCatalogo=AL&IdPersona="+document.frmRegistro.IdPersonaAlta.value;
					document.frmRegistro.submit();	
				}
			}
		}
		
		function fModificar(){
			if (document.frmRegistro.TipoTasa.value == "Si indexada"){
				if (document.frmRegistro.IdTasaReferencia.value != "null"){
					document.frmRegistro.IdTasaReferencia.disabled = false;
					document.frmRegistro.IdPeriodicidadTasa.disabled = false;
					document.frmRegistro.action="ProcesaCatalogo?Funcion=SimPrestamoProductoCicloModificacion&OperacionCatalogo=AL&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&IdPersona="+document.frmRegistro.IdPersona.value;
					document.frmRegistro.submit();	
				}else {
					alert("Ingrese el papel de la tasa indexada");
				}				
			}else{
				document.frmRegistro.IdTasaReferencia.disabled = false;
				document.frmRegistro.IdPeriodicidadTasa.disabled = false;
				document.frmRegistro.action="ProcesaCatalogo?Funcion=SimPrestamoProductoCicloModificacion&OperacionCatalogo=AL&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&IdPersona="+document.frmRegistro.IdPersona.value;
				document.frmRegistro.submit();	
			}
		}
		
		function fTasa(){
			if(document.frmRegistro.TipoTasa.value == "No indexada"){
				document.frmRegistro.IdTasaReferencia.disabled = true;
				document.frmRegistro.ValorTasa.disabled = false;
				document.frmRegistro.IdPeriodicidadTasa.disabled = false;
				document.frmRegistro.IdTasaReferencia.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.ValorTasa.style.backgroundColor = 'white';
				document.frmRegistro.IdPeriodicidadTasa.style.backgroundColor = 'white';
			}else if(document.frmRegistro.TipoTasa.value == "Si indexada"){
				document.frmRegistro.IdTasaReferencia.disabled = false;
				document.frmRegistro.ValorTasa.disabled = true;
				document.frmRegistro.IdPeriodicidadTasa.disabled = true;
				document.frmRegistro.IdTasaReferencia.style.backgroundColor = 'white';
				document.frmRegistro.ValorTasa.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.IdPeriodicidadTasa.style.backgroundColor = '#CCCCCC';
			} 
		}
		
		function fAsignarCliente(){
			MM_openBrWindow('/portal/ProcesaCatalogo?Funcion=SimPrestamoAsignaCliente&OperacionCatalogo=IN&Filtro=Inicio&Ventana=Si','Acepta','status=yes,scrollbars=yes,resizable=yes,width=700,height=500');
		}
		
		function fAsignarProducto(){
			MM_openBrWindow('/portal/Aplicaciones/Sim/Prestamo/fSimAsiProCicCon.jsp?&IdSucursal='+document.frmRegistro.IdSucursal.value+'&IdCliente='+document.frmRegistro.IdPersonaAlta.value+'&Ventana=Si','AsignaProductoCiclo','status=yes,scrollbars=yes,resizable=yes,width=700,height=500');
		}
		
		function fAsignarParticipante(sCveTipoPersona){
			var CveTipoPersona = sCveTipoPersona;
				MM_openBrWindow('/portal/Aplicaciones/Sim/Personas/fSimCliCon.jsp?TipoPersona='+CveTipoPersona+'&Ventana=Si','Acepta','status=yes,scrollbars=yes,resizable=yes,width=700,height=500');
			
		}
		
		function fBaja(){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimPrestamo&OperacionCatalogo=BA&IdPrestamo="+document.frmRegistro.IdPrestamo.value;
			document.frmRegistro.submit();	
		}
		
		
		function fReporte(sFuncion){
			MM_openBrWindow('/portal/ProcesaReporte?Funcion='+sFuncion+'&TipoReporte=Pdf&IdPrestamo='+document.frmRegistro.IdPrestamo.value,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
		
	</script>
</Portal:Pagina>	