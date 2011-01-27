<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoGrupalCreditoIndividual" precarga="SimPrestamoGrupalCargoComision SimPrestamoGrupalMontoEtapa SimPrestamoDocumentoImprimir">
	<Portal:PaginaNombre titulo="Prestamo Grupal" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	
	
	<Portal:Forma tipo='catalogo' funcion='SimPrestamoGrupal' parametros='IdPrestamoGrupo=${param.IdPrestamoGrupo}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave del préstamo grupal' control='etiqueta-controloculto' controlnombre='CvePrestamoGrupo' controlvalor='${requestScope.registro.campos["CVE_PRESTAMO_GRUPO"]}' controllongitud='3' controllongitudmax='3' editarinicializado='true' obligatorio='true'/>
		<input type="hidden" name="IdPrestamoGrupo" value='<c:out value='${requestScope.registro.campos["ID_PRESTAMO_GRUPO"]}'/>' />
		<input type="hidden" name="IdGrupo" value='<c:out value='${requestScope.registro.campos["ID_GRUPO"]}'/>' />
		<input type="hidden" name="IdProducto" value='<c:out value='${requestScope.registro.campos["ID_PRODUCTO"]}'/>' />
		<Portal:FormaElemento etiqueta='N&uacute;mero de ciclo' control='etiqueta-controloculto' controlnombre='NumCiclo' controlvalor='${requestScope.registro.campos["NUM_CICLO"]}' controllongitud='3' controllongitudmax='3' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Plazo' control='Texto' controlnombre='Plazo' controlvalor='${requestScope.registro.campos["PLAZO"]}' controllongitud='3' controllongitudmax='3' editarinicializado='true' obligatorio='true' validadato='numerico'/>
		<Portal:FormaElemento etiqueta='Periodicidad del Producto' control='selector' controlnombre='IdPeriodicidad' controlvalor='${registro.campos["ID_PERIODICIDAD"]}' editarinicializado='true' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>
		
		<c:if test='${(requestScope.registro.campos["ID_TASA_REFERENCIA"] == null)}'>
			<Portal:FormaElemento etiqueta='Valor de la tasa' control='Texto' controlnombre='ValorTasa' controlvalor='${requestScope.registro.campos["VALOR_TASA"]}' controllongitud='9' controllongitudmax='9' editarinicializado='true' obligatorio='false' validadato='cantidades'/>
			<Portal:FormaElemento etiqueta='Periodicidad de la tasa' control='selector' controlnombre='IdPeriodicidadTasa' controlvalor='${requestScope.registro.campos["ID_PERIODICIDAD_TASA"]}' editarinicializado='true' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>
			<input type='hidden' name='IdTasaReferencia' value='hola'/>	
		</c:if>
		<c:if test='${(requestScope.registro.campos["ID_TASA_REFERENCIA"] != null)}'>
			<Portal:FormaElemento etiqueta='Valor de la tasa' control='Texto' controlnombre='ValorTasa' controlvalor='${requestScope.registro.campos["VALOR_TASA"]}' controllongitud='9' controllongitudmax='9' editarinicializado='false' obligatorio='false' validadato='cantidades'/>
			<Portal:FormaElemento etiqueta='Periodicidad de la tasa' control='selector' controlnombre='IdPeriodicidadTasa' controlvalor='${requestScope.registro.campos["ID_PERIODICIDAD_TASA"]}' editarinicializado='false' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>
			<Portal:FormaElemento etiqueta='Tasa de referencia' control='selector' controlnombre='IdTasaReferencia' controlvalor='${requestScope.registro.campos["ID_TASA_REFERENCIA"]}' editarinicializado='true' obligatorio='false' campoclave="ID_TASA_REFERENCIA" campodescripcion="NOM_TASA_REFERENCIA" datosselector='${requestScope.ListaPapel}'/>
		</c:if>
		<Portal:FormaElemento etiqueta='Monto m&aacute;ximo' control='Texto' controlnombre='MontoMaximo' controlvalor='${requestScope.registro.campos["MONTO_MAXIMO"]}' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		
		<Portal:FormaSeparador nombre="Recargos"/>
		<tr>
			<th>Recargos</th>
			<td>
				<select name="TipoRecargo" size="1" onchange="fTipoRecargo();">
					<option value='null'></option>
					<option value='3' >Interés moratorio</option>
					<option value='4' >Monto fijo por periodo</option>
					<option value='5' >Interés moratorio y Monto fijo por periodo</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>Tipo de tasa</th>
			<td>
				<select name='TipoTasaRecargo' size='1' onchange='fTipoTasa();'>
					<option value='null'></option>
					<option value='1'>Fija independiente</option>
					<option value='2'>Dependiente de la tasa de credito</option>
				</select>
			</td>
		</tr>
		
		<Portal:FormaElemento etiqueta='Tasa de recargo' control='Texto' controlnombre='TasaRecargo' controlvalor='${requestScope.registro.campos["TASA_RECARGO"]}' controllongitud='9' controllongitudmax='9' editarinicializado='true' obligatorio='false' validadato='cantidades' evento="onchange=fDeshabilita();"/>
		<Portal:FormaElemento etiqueta='Periodicidad' control='selector' controlnombre='IdPeriodicidadTasaRecargo' controlvalor='${requestScope.registro.campos["ID_PERIODICIDAD_TASA_RECARGO"]}' editarinicializado='true' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>	
		<Portal:FormaElemento etiqueta='Tasa de recargo Papeles' control='selector' controlnombre='IdTasaReferenciaRecargo' controlvalor='${requestScope.registro.campos["ID_TASA_REFERENCIA_RECARGO"]}' editarinicializado='true' obligatorio='false' campoclave="ID_TASA_REFERENCIA" campodescripcion="NOM_TASA_REFERENCIA" datosselector='${requestScope.ListaPapel}' evento="onchange=fTasaRecargo();"/>	
		<Portal:FormaElemento etiqueta='Factor sobre la tasa de interes' control='Texto' controlnombre='FactorTasaRecargo' controlvalor='${requestScope.registro.campos["FACTOR_TASA_RECARGO"]}' controllongitud='9' controllongitudmax='9' editarinicializado='true' obligatorio='false' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Monto fijo por periodo' control='Texto' controlnombre='MontoFijoPeriodo' controlvalor='${requestScope.registro.campos["MONTO_FIJO_PERIODO"]}' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='false' validadato='cantidades'/>
		
		<Portal:FormaBotones>
			<c:if test='${requestScope.registro.campos["B_CANCELADO"] == "F"}'>
				<c:if test='${requestScope.registro.campos["B_ENTREGADO"] == "F"}'>
					<input type='button' name='Modificar' value='Aceptar' onClick='fModificar()'/>
				</c:if>
			</c:if>
			<c:if test='${requestScope.registro.campos["B_CANCELADO"] == "V"}'>
			</c:if>
			<c:if test='${requestScope.registro.campos["B_CANCELADO"] == "F"}'>
				<c:if test='${requestScope.registro.campos["B_ENTREGADO"] == "V"}'>
				</c:if>
			</c:if>
		</Portal:FormaBotones>
	</Portal:Forma>
	
	<Portal:TablaForma maestrodetallefuncion="SimPrestamoGrupalCargoComision" nombre="Cargos y comisiones" funcion="SimPrestamoGrupalCargoComision" operacion="AL" parametros="&IdPrestamoGrupo=${param.IdPrestamoGrupo}&IdGrupo=${param.IdGrupo}&IdProducto=${param.IdProducto}">
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
			<c:if test='${requestScope.registro.campos["B_CANCELADO"] == "F"}'>
				<c:if test='${requestScope.registro.campos["B_ENTREGADO"] == "F"}'>
					<Portal:Boton tipo='submit' etiqueta='Modificar' />
				</c:if>
			</c:if>
			<c:if test='${requestScope.registro.campos["B_CANCELADO"] == "V"}'>
			</c:if>
			<c:if test='${requestScope.registro.campos["B_CANCELADO"] == "F"}'>
				<c:if test='${requestScope.registro.campos["B_ENTREGADO"] == "V"}'>
				</c:if>
			</c:if>
		</Portal:FormaBotones>		
	</Portal:TablaForma>

	<c:if test='${(registroEtapaDocumentos.campos["ID_ETAPA_PRESTAMO"] != null)}'>
	<Portal:TablaForma maestrodetallefuncion="SimPrestamoDocumentoImprimir" nombre="Documento a Imprimir" funcion="SimPrestamoGrupalCargoComision" operacion="AL" parametros="&IdPrestamoGrupo=${param.IdPrestamoGrupo}&IdGrupo=${param.IdGrupo}">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='1550' valor='Lista de documentos a imprimir'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaDocumentoImprimir}">
			<Portal:TablaListaRenglon>
				<td  width='1550' align='left'  nowrap  >
						<a id="AsignarGrupo" href="javascript:fReporte('<c:out value='${registro.campos["CVE_FUNCION"]}'/>');"><c:out value='${registro.campos["NOM_DOCUMENTO"]}'/></a>	
					</td>	
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	</c:if>	

	<Portal:TablaForma maestrodetallefuncion="SimPrestamoGrupalMontoEtapa" nombre="Montos y etapas de los créditos individuales" funcion="SimPrestamoGrupalCreditoIndividual" operacion="MO" parametros='IdPrestamoGrupo=${param.IdPrestamoGrupo}&IdGrupo=${param.IdGrupo}&IdProducto=${param.IdProducto}'>
		<Portal:FormaElemento etiqueta='Etapa Grupal' control='Texto' controlnombre='EtapaGrupal' controlvalor='${requestScope.registroEtapaGrupal.campos["ETAPA_GRUPAL"]}' controllongitud='3' controllongitudmax='3' editarinicializado='false'/>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Nombre del cliente'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Tipo de negocio'/>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Giro'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Monto solicitado'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Monto autorizado'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Etapa'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Avanzar etapa'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Comentario'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaMontoEtapa}">		
			<Portal:TablaListaRenglon>
				<c:if test='${(registro.campos["ID_ETAPA_PRESTAMO"] != "31")}'>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PRESTAMO"]}' funcion='SimPrestamo' operacion='CR' parametros='IdPrestamo=${registro.campos["ID_PRESTAMO"]}&Alta=No'/>
					</Portal:Columna>	
					<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["NOM_COMPLETO"]}'/>
						<input type='hidden' name='IdCliente' value='<c:out value='${registro.campos["ID_INTEGRANTE"]}'/>'>
						<input type='hidden' name='IdPrestamo' value='<c:out value='${registro.campos["ID_PRESTAMO"]}'/>'>	
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_TIPO_NEGOCIO"]}'/>
					<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["NOM_GIRO"]}'/>		
					<td>
					$ <input type='text' name='MontoSolicitado' size='18' maxlength='22' value='<c:out value='${registro.campos["MONTO_SOLICITADO"]}'/>'>
					</td>
					<Portal:Columna tipovalor='texto' ancho='100' valor='$ ${registro.campos["MONTO_AUTORIZADO"]}'/>
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_ESTATUS_PRESTAMO"]}'/>
					<input type='hidden' name='IdEtapaPrestamo' value='<c:out value='${registro.campos["ID_ETAPA_PRESTAMO"]}'/>'>	
					<Portal:Columna tipovalor='texto' ancho='70' valor='${registro.campos["B_AVANZAR"]}' control='checkbox' controlnombre='Avanzar${registro.campos["ID_INTEGRANTE"]}' />
					<td>
						<textarea name='Comentario' cols='20' rows='3'     onchange="fOnKeyUp();"  ><c:out value='${registro.campos["COMENTARIO"]}'/></textarea>		
						
					</td>
				</c:if>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<c:if test='${requestScope.registro.campos["B_CANCELADO"] == "F"}'>
				<c:if test='${requestScope.registro.campos["B_ENTREGADO"] == "F"}'>
					<c:if test='${requestScope.registro.campos["B_AUTORIZAR_COMITE"] == "F"}'>
						<c:if test='${requestScope.registro.campos["B_LINEA_FONDEO"] == "F"}'>
							<c:if test='${requestScope.registro.campos["B_DESEMBOLSO"] == "F"}'>
								<c:if test='${requestScope.registro.campos["ID_ETAPA_PRESTAMO"] == "1"}'>
									<Portal:Boton tipo='submit' nombre='Aceptar' etiqueta='Aceptar' />
									<Portal:Boton tipo='submit' nombre='ReconformaPrestamo' etiqueta='Reconforma Préstamo' />
									<Portal:Boton tipo='submit' nombre='RegresarEtapa' etiqueta='Regresar etapa grupal' />
									<Portal:Boton tipo='submit' nombre='AvanzarEtapaGrupal' etiqueta='Avanzar etapa grupal' />
									<Portal:Boton tipo='submit' nombre='CancelarPrestamo' etiqueta='Cancelar Préstamo' />
								</c:if>
							</c:if>
						</c:if>
					</c:if>
				</c:if>
			</c:if>
			<c:if test='${requestScope.registro.campos["B_CANCELADO"] == "F"}'>
				<c:if test='${requestScope.registro.campos["B_ENTREGADO"] == "F"}'>
					<c:if test='${requestScope.registro.campos["B_AUTORIZAR_COMITE"] == "F"}'>
						<c:if test='${requestScope.registro.campos["B_LINEA_FONDEO"] == "F"}'>
							<c:if test='${requestScope.registro.campos["B_DESEMBOLSO"] == "F"}'>
								<c:if test='${requestScope.registro.campos["ID_ETAPA_PRESTAMO"] != "1"}'>
									<Portal:Boton tipo='submit' nombre='Aceptar' etiqueta='Aceptar' />
									<Portal:Boton tipo='submit' nombre='RegresarEtapa' etiqueta='Regresar etapa grupal' />
									<Portal:Boton tipo='submit' nombre='AvanzarEtapaGrupal' etiqueta='Avanzar etapa grupal' />
									<Portal:Boton tipo='submit' nombre='CancelarPrestamo' etiqueta='Cancelar Préstamo' />
								</c:if>
							</c:if>
						</c:if>
					</c:if>
				</c:if>
			</c:if>
			<c:if test='${requestScope.registro.campos["B_CANCELADO"] == "V"}'>
			</c:if>
			<c:if test='${requestScope.registro.campos["B_CANCELADO"] == "F"}'>
				<c:if test='${requestScope.registro.campos["B_ENTREGADO"] == "V"}'>
				</c:if>
			</c:if>
			<c:if test='${requestScope.registro.campos["B_CANCELADO"] == "F"}'>
				<c:if test='${requestScope.registro.campos["B_ENTREGADO"] == "F"}'>
					<c:if test='${requestScope.registro.campos["B_AUTORIZAR_COMITE"] == "V"}'>
						<c:if test='${requestScope.registro.campos["B_LINEA_FONDEO"] == "F"}'>
							<c:if test='${requestScope.registro.campos["B_DESEMBOLSO"] == "F"}'>
								<Portal:Boton tipo='submit' nombre='Aceptar' etiqueta='Aceptar' />
								<Portal:Boton tipo='submit' nombre='RegresarEtapa' etiqueta='Regresar etapa grupal' />
								<Portal:Boton tipo='submit' nombre='CancelarPrestamo' etiqueta='Cancelar Préstamo' />
							</c:if>
						</c:if>
					</c:if>
				</c:if>
			</c:if>
			<c:if test='${requestScope.registro.campos["B_CANCELADO"] == "F"}'>
				<c:if test='${requestScope.registro.campos["B_ENTREGADO"] == "F"}'>
					<c:if test='${requestScope.registro.campos["B_AUTORIZAR_COMITE"] == "F"}'>
						<c:if test='${requestScope.registro.campos["B_LINEA_FONDEO"] == "V"}'>
							<c:if test='${requestScope.registro.campos["B_DESEMBOLSO"] == "F"}'>
								<Portal:Boton tipo='submit' nombre='Aceptar' etiqueta='Aceptar' />
								<Portal:Boton tipo='submit' nombre='RegresarEtapa' etiqueta='Regresar etapa grupal' />
								<Portal:Boton tipo='submit' nombre='CancelarPrestamo' etiqueta='Cancelar Préstamo' />
							</c:if>
						</c:if>
					</c:if>
				</c:if>
			</c:if>
			<c:if test='${requestScope.registro.campos["B_CANCELADO"] == "F"}'>
				<c:if test='${requestScope.registro.campos["B_ENTREGADO"] == "F"}'>
					<c:if test='${requestScope.registro.campos["B_AUTORIZAR_COMITE"] == "F"}'>
						<c:if test='${requestScope.registro.campos["B_LINEA_FONDEO"] == "F"}'>
							<c:if test='${requestScope.registro.campos["B_DESEMBOLSO"] == "V"}'>
								<Portal:Boton tipo='submit' nombre='Aceptar' etiqueta='Aceptar' />
								<Portal:Boton tipo='submit' nombre='RegresarEtapa' etiqueta='Regresar etapa grupal' />
								<Portal:Boton tipo='submit' nombre='CancelarPrestamo' etiqueta='Cancelar Préstamo' />
							</c:if>
						</c:if>
					</c:if>
				</c:if>
			</c:if>
			
		</Portal:FormaBotones>
	</Portal:TablaForma>
	
	<script>
		<c:if test='${(requestScope.registro != null)}'>  
			BuscaSelectOpcion(document.frmRegistro.TipoTasaRecargo,'<c:out value='${requestScope.registro.campos["TIPO_TASA_RECARGO"]}'/>');
			BuscaSelectOpcion(document.frmRegistro.TipoRecargo,'<c:out value='${requestScope.registro.campos["ID_TIPO_RECARGO"]}'/>'); 
		</c:if>
		
		if (document.frmRegistro.TipoRecargo.value == "3"){
		
			document.frmRegistro.MontoFijoPeriodo.value = "";
		
			document.frmRegistro.TipoTasaRecargo.disabled = false;
			document.frmRegistro.TasaRecargo.disabled = false;
			document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = false;
			document.frmRegistro.IdTasaReferenciaRecargo.disabled = false;
			document.frmRegistro.FactorTasaRecargo.disabled = false;
			document.frmRegistro.MontoFijoPeriodo.disabled = true;
			
			document.frmRegistro.TipoTasaRecargo.style.backgroundColor = 'white';
			document.frmRegistro.TasaRecargo.style.backgroundColor = 'white';
			document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = 'white';
			document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = 'white';
			document.frmRegistro.FactorTasaRecargo.style.backgroundColor = 'white';
			document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = '#CCCCCC';
			
		}else if(document.frmRegistro.TipoRecargo.value == "4"){
			
			document.frmRegistro.TipoTasaRecargo.value = "null";
			document.frmRegistro.TasaRecargo.value = "";
			document.frmRegistro.IdPeriodicidadTasaRecargo.value = "null";
			document.frmRegistro.IdTasaReferenciaRecargo.value = "null";
			document.frmRegistro.FactorTasaRecargo.value = "";
		
			document.frmRegistro.TipoTasaRecargo.disabled = true;
			document.frmRegistro.TasaRecargo.disabled = true;
			document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = true;
			document.frmRegistro.IdTasaReferenciaRecargo.disabled = true;
			document.frmRegistro.FactorTasaRecargo.disabled = true;
			document.frmRegistro.MontoFijoPeriodo.disabled = false;
			
			document.frmRegistro.TipoTasaRecargo.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.TasaRecargo.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.FactorTasaRecargo.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = 'white';
			
		}else if(document.frmRegistro.TipoRecargo.value == "5"){
		
			document.frmRegistro.TipoTasaRecargo.disabled = false;
			document.frmRegistro.TasaRecargo.disabled = false;
			document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = false;
			document.frmRegistro.IdTasaReferenciaRecargo.disabled = false;		
			document.frmRegistro.FactorTasaRecargo.disabled = false;
			document.frmRegistro.MontoFijoPeriodo.disabled = false;
			
			document.frmRegistro.TipoTasaRecargo.style.backgroundColor = 'white';
			document.frmRegistro.TasaRecargo.style.backgroundColor = 'white';
			document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = 'white';
			document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = 'white';
			document.frmRegistro.FactorTasaRecargo.style.backgroundColor = 'white';
			document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = 'white';
		}
		
		if (document.frmRegistro.TipoTasaRecargo.value == "1"){
			if(document.frmRegistro.TipoRecargo.value == "5"){
				if (document.frmRegistro.IdPeriodicidadTasaRecargo.value == "null"){
					
					document.frmRegistro.TasaRecargo.value = "";
					document.frmRegistro.IdPeriodicidadTasaRecargo.value = "null";
					document.frmRegistro.FactorTasaRecargo.value = "";
	
					document.frmRegistro.TasaRecargo.disabled = true;
					document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = true;
					document.frmRegistro.IdTasaReferenciaRecargo.disabled = false;
					document.frmRegistro.FactorTasaRecargo.disabled = true;
					document.frmRegistro.MontoFijoPeriodo.disabled = false;
					
					document.frmRegistro.TasaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = 'white';		
					document.frmRegistro.FactorTasaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = 'white';
				}else if (document.frmRegistro.IdTasaReferenciaRecargo.value == "null"){
				
					document.frmRegistro.IdTasaReferenciaRecargo.value = "null";
					document.frmRegistro.FactorTasaRecargo.value = "";
		
					document.frmRegistro.TasaRecargo.disabled = false;
					document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = false;
					document.frmRegistro.IdTasaReferenciaRecargo.disabled = true;
					document.frmRegistro.FactorTasaRecargo.disabled = true;
					document.frmRegistro.MontoFijoPeriodo.disabled = false;
					document.frmRegistro.TasaRecargo.style.backgroundColor = 'white';
					document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = 'white';
					document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.FactorTasaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = 'white';
				}
			}else{
			if (document.frmRegistro.IdPeriodicidadTasaRecargo.value == "null"){
					
					document.frmRegistro.TasaRecargo.value = "";
					document.frmRegistro.IdPeriodicidadTasaRecargo.value = "null";
					document.frmRegistro.FactorTasaRecargo.value = "";
					document.frmRegistro.MontoFijoPeriodo.value = "";
						
					document.frmRegistro.TasaRecargo.disabled = true;
					document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = true;
					document.frmRegistro.IdTasaReferenciaRecargo.disabled = false;
					document.frmRegistro.FactorTasaRecargo.disabled = true;
					document.frmRegistro.MontoFijoPeriodo.disabled = true;
					
					document.frmRegistro.TasaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = 'white';
					document.frmRegistro.FactorTasaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = '#CCCCCC';
				}else if (document.frmRegistro.IdTasaReferenciaRecargo.value == "null"){
				
					document.frmRegistro.IdTasaReferenciaRecargo.value = "null";
					document.frmRegistro.FactorTasaRecargo.value = "";
					document.frmRegistro.MontoFijoPeriodo.value = "";
		
					document.frmRegistro.TasaRecargo.disabled = false;
					document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = false;
					document.frmRegistro.IdTasaReferenciaRecargo.disabled = true;
					document.frmRegistro.FactorTasaRecargo.disabled = true;
					document.frmRegistro.MontoFijoPeriodo.disabled = true;
					document.frmRegistro.TasaRecargo.style.backgroundColor = 'white';
					document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = 'white';
					document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.FactorTasaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = '#CCCCCC';
				}
			}
		}else if(document.frmRegistro.TipoTasaRecargo.value == "2"){
			if(document.frmRegistro.TipoRecargo.value == "5"){
			
				document.frmRegistro.TasaRecargo.value = "";
				document.frmRegistro.IdPeriodicidadTasaRecargo.value = "null";
				document.frmRegistro.IdTasaReferenciaRecargo.value = "null";
				
				document.frmRegistro.TasaRecargo.disabled = true;
				document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = true;
				document.frmRegistro.IdTasaReferenciaRecargo.disabled = true;		
				document.frmRegistro.FactorTasaRecargo.disabled = false;
				document.frmRegistro.MontoFijoPeriodo.disabled = false;
				
				document.frmRegistro.TasaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.FactorTasaRecargo.style.backgroundColor = 'white';
				document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = 'white';
			}else{
			
				document.frmRegistro.TasaRecargo.value = "";
				document.frmRegistro.IdPeriodicidadTasaRecargo.value = "null";
				document.frmRegistro.IdTasaReferenciaRecargo.value = "null";
				document.frmRegistro.MontoFijoPeriodo.value = "";
			
				document.frmRegistro.TasaRecargo.disabled = true;
				document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = true;
				document.frmRegistro.IdTasaReferenciaRecargo.disabled = true;		
				document.frmRegistro.FactorTasaRecargo.disabled = false;
				document.frmRegistro.MontoFijoPeriodo.disabled = true;
				
				document.frmRegistro.TasaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.FactorTasaRecargo.style.backgroundColor = 'white';
				document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = '#CCCCCC';
			}
		}	
		
		function fDeshabilita(){
			document.frmRegistro.IdTasaReferencia.disabled = true;
			
			document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = '#CCCCCC';
			
		}
		
		function fTasaRecargo(){
			if(document.frmRegistro.TipoRecargo.value == "5"){
			
				document.frmRegistro.TasaRecargo.value = "";
				document.frmRegistro.IdPeriodicidadTasaRecargo.value = "null";
				document.frmRegistro.FactorTasaRecargo.value = "";
				
				document.frmRegistro.TasaRecargo.disabled = true;
				document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = true;
				document.frmRegistro.IdTasaReferenciaRecargo.disabled = false;
				document.frmRegistro.FactorTasaRecargo.disabled = true;
				document.frmRegistro.MontoFijoPeriodo.disabled = false;
				
				document.frmRegistro.TasaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = 'white';
				document.frmRegistro.FactorTasaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = 'white';
			}else{
			
				document.frmRegistro.TasaRecargo.value = "";
				document.frmRegistro.IdPeriodicidadTasaRecargo.value = "null";
				document.frmRegistro.FactorTasaRecargo.value = "";
				document.frmRegistro.MontoFijoPeriodo.value = "";
			
				document.frmRegistro.TasaRecargo.disabled = true;
				document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = true;
				document.frmRegistro.IdTasaReferenciaRecargo.disabled = false;
				document.frmRegistro.FactorTasaRecargo.disabled = true;
				document.frmRegistro.MontoFijoPeriodo.disabled = true;
				
				document.frmRegistro.TasaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = 'white';
				document.frmRegistro.FactorTasaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = '#CCCCCC';
			}
		}
		
		function fTipoTasa(){
			if (document.frmRegistro.TipoRecargo.value == "5"){
				if (document.frmRegistro.TipoTasaRecargo.value == "1"){
				
					document.frmRegistro.FactorTasaRecargo.value = "";
					
					document.frmRegistro.TasaRecargo.disabled = false;
					document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = false;
					document.frmRegistro.IdTasaReferenciaRecargo.disabled = false;
					document.frmRegistro.FactorTasaRecargo.disabled = true;
					document.frmRegistro.MontoFijoPeriodo.disabled = false;
					
					document.frmRegistro.TasaRecargo.style.backgroundColor = 'white';
					document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = 'white';
					document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = 'white';
					document.frmRegistro.FactorTasaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = 'white';
				}else if(document.frmRegistro.TipoTasaRecargo.value == "2"){
				
					document.frmRegistro.TasaRecargo.value = "null";
					document.frmRegistro.IdPeriodicidadTasaRecargo.value = "null";
					document.frmRegistro.IdTasaReferenciaRecargo.value = "null";
				
					document.frmRegistro.TasaRecargo.disabled = true;
					document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = true;
					document.frmRegistro.IdTasaReferenciaRecargo.disabled = true;
					document.frmRegistro.FactorTasaRecargo.disabled = false;
					document.frmRegistro.MontoFijoPeriodo.disabled = false;
					
					document.frmRegistro.TasaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.FactorTasaRecargo.style.backgroundColor = 'white';
					document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = 'white';
				}
			}else {
				if (document.frmRegistro.TipoTasaRecargo.value == "1"){
				
					document.frmRegistro.FactorTasaRecargo.value = "";
					document.frmRegistro.MontoFijoPeriodo.value = "";
				
				
					document.frmRegistro.TasaRecargo.disabled = false;
					document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = false;
					document.frmRegistro.IdTasaReferenciaRecargo.disabled = false;
					document.frmRegistro.FactorTasaRecargo.disabled = true;
					document.frmRegistro.MontoFijoPeriodo.disabled = true;
					
					document.frmRegistro.TasaRecargo.style.backgroundColor = 'white';
					document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = 'white';
					document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = 'white';
					document.frmRegistro.FactorTasaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = '#CCCCCC';
				}else if(document.frmRegistro.TipoTasaRecargo.value == "2"){
				
					document.frmRegistro.TasaRecargo.value = "";
					document.frmRegistro.IdPeriodicidadTasaRecargo.value = "null";
					document.frmRegistro.IdTasaReferenciaRecargo.value = "null";
					document.frmRegistro.MontoFijoPeriodo.value = "";
				
					document.frmRegistro.TasaRecargo.disabled = true;
					document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = true;
					document.frmRegistro.IdTasaReferenciaRecargo.disabled = true;
					document.frmRegistro.FactorTasaRecargo.disabled = false;
					document.frmRegistro.MontoFijoPeriodo.disabled = true;
					
					document.frmRegistro.TasaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = '#CCCCCC';
					document.frmRegistro.FactorTasaRecargo.style.backgroundColor = 'white';
					document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = '#CCCCCC';
				}
			}
		}
		
		function fTipoRecargo(){
			if (document.frmRegistro.TipoRecargo.value == "3"){
			
				document.frmRegistro.MontoFijoPeriodo.value = "";
			
				document.frmRegistro.TipoTasaRecargo.disabled = false;
				document.frmRegistro.TasaRecargo.disabled = false;
				document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = false;
				document.frmRegistro.IdTasaReferenciaRecargo.disabled = false;
				document.frmRegistro.FactorTasaRecargo.disabled = false;
				document.frmRegistro.MontoFijoPeriodo.disabled = true;
				
				document.frmRegistro.TipoTasaRecargo.style.backgroundColor = 'white';
				document.frmRegistro.TasaRecargo.style.backgroundColor = 'white';
				document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = 'white';
				document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = 'white';
				document.frmRegistro.FactorTasaRecargo.style.backgroundColor = 'white';
				document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = '#CCCCCC';
			}else if(document.frmRegistro.TipoRecargo.value == "4"){
			
				document.frmRegistro.TipoTasaRecargo.value = "null";
				document.frmRegistro.TasaRecargo.value = "";
				document.frmRegistro.IdPeriodicidadTasaRecargo.value = "null";
				document.frmRegistro.IdTasaReferenciaRecargo.value = "null";
				document.frmRegistro.FactorTasaRecargo.value = "";
			
				document.frmRegistro.TipoTasaRecargo.disabled = true;
				document.frmRegistro.TasaRecargo.disabled = true;
				document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = true;
				document.frmRegistro.IdTasaReferenciaRecargo.disabled = true;
				document.frmRegistro.FactorTasaRecargo.disabled = true;
				document.frmRegistro.MontoFijoPeriodo.disabled = false;
				
				document.frmRegistro.TipoTasaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.TasaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.FactorTasaRecargo.style.backgroundColor = '#CCCCCC';
				document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = 'white';
			}else if(document.frmRegistro.TipoRecargo.value == "5"){
			
				document.frmRegistro.TipoTasaRecargo.disabled = false;
				document.frmRegistro.TasaRecargo.disabled = false;
				document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = false;
				document.frmRegistro.IdTasaReferenciaRecargo.disabled = false;
				document.frmRegistro.FactorTasaRecargo.disabled = false;
				document.frmRegistro.MontoFijoPeriodo.disabled = false;
				
				document.frmRegistro.TipoTasaRecargo.style.backgroundColor = 'white';
				document.frmRegistro.TasaRecargo.style.backgroundColor = 'white';
				document.frmRegistro.IdPeriodicidadTasaRecargo.style.backgroundColor = 'white';
				document.frmRegistro.IdTasaReferenciaRecargo.style.backgroundColor = 'white';
				document.frmRegistro.FactorTasaRecargo.style.backgroundColor = 'white';
				document.frmRegistro.MontoFijoPeriodo.style.backgroundColor = 'white';
			}
		}
		
		var a=document.getElementsByName("CargoInicial");
		for (var i = 0; i < a.length; i++){
			if(a[i].value == ""){
				a[i].style.backgroundColor = '#CCCCCC'
			}
		}
		
		var b=document.getElementsByName("PorcentajeMonto");
		for (var i = 0; i < b.length; i++){
			if(b[i].value == ""){
				b[i].style.backgroundColor = '#CCCCCC'
			}
		}
		
		var c=document.getElementsByName("CantidadFija");
		for (var i = 0; i < c.length; i++){
			if(c[i].value == ""){
				c[i].style.backgroundColor = '#CCCCCC'
			}
		}
		
		var d=document.getElementsByName("Valor");
		for (var i = 0; i < d.length; i++){
			if(d[i].value == ""){
				d[i].style.backgroundColor = '#CCCCCC'
			}
		}
		
		var e=document.getElementsByName("IdUnidad");
		for (var i = 0; i < e.length; i++){
			if(e[i].value == "null"){
				e[i].style.backgroundColor = '#CCCCCC'
			}
		}
		
		var f=document.getElementsByName("IdPeriodicidad");
		for (var i = 0; i < f.length; i++){
			if(f[i].value == "null"){
				f[i].style.backgroundColor = '#CCCCCC'
			}
		}
		
		function fModificar(){
			document.frmRegistro.TipoRecargo.disabled = false;
			document.frmRegistro.TipoTasaRecargo.disabled = false;
			document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = false;
			document.frmRegistro.IdTasaReferenciaRecargo.disabled = false;
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimPrestamoGrupal&OperacionCatalogo=MO&IdPrestamoGrupo="+document.frmRegistro.IdPrestamoGrupo.value+"&IdProducto="+document.frmRegistro.IdProducto.value+"&NumCiclo="+document.frmRegistro.NumCiclo.value+"&Plazo="+document.frmRegistro.Plazo.value+"&IdPeriodicidad="+document.frmRegistro.IdPeriodicidad.value+"&ValorTasa="+document.frmRegistro.ValorTasa.value+"&MontoMaximo="+document.frmRegistro.MontoMaximo.value+"&IdTasaReferencia="+document.frmRegistro.IdTasaReferencia.value;
			document.frmRegistro.submit();	
		}
		
		function fReporte(sFuncion){
			MM_openBrWindow('/portal/ProcesaReporte?Funcion='+sFuncion+'&TipoReporte=Pdf&IdPrestamoGrupo='+document.frmRegistro.IdPrestamoGrupo.value+'&IdGrupo='+document.frmRegistro.IdGrupo.value+'&IdProducto='+document.frmRegistro.IdProducto.value+'&NumCiclo='+document.frmRegistro.NumCiclo.value,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
		}
		
	</script>
	
</Portal:Pagina>
