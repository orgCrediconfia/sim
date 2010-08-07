<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimProductoCiclo">
	<Portal:PaginaNombre titulo="Ciclo" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimProductoCiclo' parametros='IdProducto=${param.IdProducto}&IdCiclo=${param.IdCiclo}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<input type="hidden" name="IdProducto" value='<c:out value='${param.IdProducto}'/>' />
		<Portal:FormaElemento etiqueta='N&uacute;mero de ciclo' control='etiqueta-controloculto' controlnombre='NumCiclo' controlvalor='${requestScope.registro.campos["NUM_CICLO"]}' controllongitud='3' controllongitudmax='3' editarinicializado='true' obligatorio='true'/>
		<tr>
			<th>Forma de distribuci&oacute;n de pago</th>
			<td>
				<select name='FormaDistribucionPago' size='1'  >
					<option value='null'></option>
					<option value='1'  >Saldos - Accesorios y capital</option>
					<option value='2'   >Saldos - Capital y accesorio</option>
				</select>
			</td>
		</tr>
		<Portal:FormaElemento etiqueta='Plazo' control='Texto' controlnombre='Plazo' controlvalor='${requestScope.registro.campos["PLAZO"]}' controllongitud='3' controllongitudmax='3' editarinicializado='true' obligatorio='true' validadato='numerico'/>
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
		<Portal:FormaElemento etiqueta='Valor de la tasa' control='Texto' controlnombre='ValorTasa' controlvalor='${requestScope.registro.campos["VALOR_TASA"]}' controllongitud='9' controllongitudmax='9' editarinicializado='true' obligatorio='false' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Periodicidad de la tasa' control='selector' controlnombre='IdPeriodicidadTasa' controlvalor='${requestScope.registro.campos["ID_PERIODICIDAD_TASA"]}' editarinicializado='true' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>	
		<Portal:FormaElemento etiqueta='Tasa de referencia' control='selector' controlnombre='IdTasaReferencia' controlvalor='${requestScope.registro.campos["ID_TASA_REFERENCIA"]}' editarinicializado='true' obligatorio='false' campoclave="ID_TASA_REFERENCIA" campodescripcion="NOM_TASA_REFERENCIA" datosselector='${requestScope.ListaPapel}'/>	
		<Portal:FormaElemento etiqueta='Monto m&aacute;ximo' control='Texto' controlnombre='MontoMaximo' controlvalor='${requestScope.registro.campos["MONTO_MAXIMO"]}' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Porcentaje de flujo de caja a financiar' control='Texto' controlnombre='PorcFlujoCaja' controlvalor='${requestScope.registro.campos["PORC_FLUJO_CAJA"]}' controllongitud='9' controllongitudmax='9' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		
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
			<input type="button" name="Aceptar"  value="Aceptar" onclick='javascript:fAceptar()'>
			<input type="button" name="Baja"  value="Baja" onclick='javascript:fBaja()'>
		</Portal:FormaBotones>
		
	</Portal:Forma>
	
	<script>
	
		<c:if test='${(requestScope.registro != null)}'>
			BuscaSelectOpcion(document.frmRegistro.FormaDistribucionPago,'<c:out value='${requestScope.registro.campos["ID_FORMA_DISTRIBUCION"]}'/>'); 
			BuscaSelectOpcion(document.frmRegistro.TipoTasa,'<c:out value='${requestScope.registro.campos["TIPO_TASA"]}'/>'); 
			BuscaSelectOpcion(document.frmRegistro.TipoTasaRecargo,'<c:out value='${requestScope.registro.campos["TIPO_TASA_RECARGO"]}'/>'); 
		</c:if>
		
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
			document.frmRegistro.ValorTasa.style.backgroundColor = 'white';
			document.frmRegistro.IdPeriodicidadTasa.style.backgroundColor = 'white';
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
		
		BuscaSelectOpcion(document.frmRegistro.TipoRecargo,'<c:out value='${requestScope.registro.campos["ID_TIPO_RECARGO"]}'/>'); 
		BuscaSelectOpcion(document.frmRegistro.TipoTasa,'<c:out value='${requestScope.registro.campos["TIPO_TASA"]}'/>'); 
			
		if (document.frmRegistro.TipoRecargo.value == "3"){
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
		
		function fAceptar(){
			document.frmRegistro.TipoRecargo.disabled = false;
			document.frmRegistro.TipoTasaRecargo.disabled = false;
			document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = false;
			document.frmRegistro.IdTasaReferenciaRecargo.disabled = false;
			document.frmRegistro.submit();
		}
		
		function fBaja(){
			document.frmRegistro.TipoRecargo.disabled = false;
			document.frmRegistro.TipoTasaRecargo.disabled = false;
			document.frmRegistro.IdPeriodicidadTasaRecargo.disabled = false;
			document.frmRegistro.IdTasaReferenciaRecargo.disabled = false;
			
			var sResult=confirm('¿Desea borrar el registro?');
			if (sResult){
				document.frmRegistro.action="ProcesaCatalogo?Funcion=SimProductoCiclo&OperacionCatalogo=BA&IdProducto="+document.frmRegistro.IdProducto.value+"&NumCiclo="+document.frmRegistro.NumCiclo.value;
				document.frmRegistro.submit();
			}
		}
	
	</script>
	
</Portal:Pagina>	