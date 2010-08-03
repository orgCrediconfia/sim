<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimCatalogoEstatusPrestamo">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de Etapas del pr&eacute;stamo" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoEstatusPrestamo'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdEstatusPrestamo' controlvalor='${requestScope.registro.campos["ID_ETAPA_PRESTAMO"]}' controllongitud='5' controllongitudmax='5' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomEstatusPrestamo' controlvalor='${requestScope.registro.campos["NOM_ESTATUS_PRESTAMO"]}' controllongitud='30' controllongitudmax='30' />
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='Texto' controlnombre='Descripcion' controlvalor='${requestScope.registro.campos["DESCRIPCION"]}' controllongitud='75' controllongitudmax='100' editarinicializado='true' obligatorio='true' controltexarealongmax='1000'/>
		<c:if test='${(requestScope.registro != null)}'>
			<Portal:FormaElemento etiqueta='Función' control='Texto' controlnombre='CveFuncion' controlvalor='${requestScope.registro.campos["CVE_FUNCION"]}' controllongitud='30' controllongitudmax='30' editarinicializado='false'/>
		</c:if>
		<Portal:FormaElemento etiqueta='Ninguna' control='variableoculta' controlnombre='BCancelado' controlvalor='${requestScope.registro.campos["B_CANCELADO"]}'/>
		<Portal:FormaElemento etiqueta='Ninguna' control='variableoculta' controlnombre='BAutorizarComite' controlvalor='${requestScope.registro.campos["B_AUTORIZAR_COMITE"]}'/>
		<Portal:FormaElemento etiqueta='Ninguna' control='variableoculta' controlnombre='BDesembolso' controlvalor='${requestScope.registro.campos["B_DESEMBOLSO"]}'/>
		<Portal:FormaElemento etiqueta='Ninguna' control='variableoculta' controlnombre='BLineaFondeo' controlvalor='${requestScope.registro.campos["B_LINEA_FONDEO"]}'/>
		<Portal:FormaElemento etiqueta='Ninguna' control='variableoculta' controlnombre='BEntregado' controlvalor='${requestScope.registro.campos["B_ENTREGADO"]}'/>
		
		<!--Ninguna bandera se inicializa-->
		<c:if test='${(requestScope.registro == null)}'>
			<tr> 
			   	<th>Autoriza comité</th>
				<td> 
					<input type='radio' name='opcion' value='AutorizarComite' onClick="javascript=Opcion(0)"/>
				</td>
			</tr>
			<tr> 
			   	<th>Desembolso</th>
				<td> 
					<input type='radio' name='opcion' value='Desembolso' onClick="javascript=Opcion(1)"/>
				</td>
			</tr>
			<tr> 
			   	<th>Líneas de fondeo</th>
				<td> 
					<input type='radio' name='opcion' value='LineaFondeo' onClick="javascript=Opcion(2)"/>
				</td>
			</tr>
			<tr> 
			   	<th>Cancelado</th>
				<td> 
					<input type='radio' name='opcion' value='Cancelado' onClick="javascript=Opcion(3)"/>
				</td>
			</tr>
			<tr> 
			   	<th>Entregado</th>
				<td> 
					<input type='radio' name='opcion' value='Entregado' onClick="javascript=Opcion(4)"/>
				</td>
			</tr>
			<tr> 
			   	<th>Ninguno</th>
				<td> 
					<input type='radio' name='opcion' value='Ninguno' onClick="javascript=Opcion(5)"/ checked >
				</td>
			</tr>
		</c:if>	
		
		<!--Muestra las banderas que tiene la función-->
		<c:if test='${(requestScope.registro != null)}'>
			<c:if test='${(requestScope.registro.campos["B_AUTORIZAR_COMITE"] != "V")}'>
				<tr> 
				   	<th>Autoriza comité</th>
					<td> 
						<input type='radio' name='opcion' value='AutorizarComite' onClick="javascript=Opcion(0)"/>
					</td>
				</tr>
			</c:if>
			<c:if test='${(requestScope.registro.campos["B_AUTORIZAR_COMITE"] != "F")}'>
				<tr> 
				   	<th>Autoriza comité</th>
					<td> 
						<input type='radio' name='opcion' value='AutorizarComite' onClick="javascript=Opcion(0)"/ checked >
					</td>
				</tr>
			</c:if>
			
			<c:if test='${(requestScope.registro.campos["B_DESEMBOLSO"] != "V")}'>
				<tr> 
				   	<th>Desembolso</th>
					<td> 
						<input type='radio' name='opcion' value='Desembolso' onClick="javascript=Opcion(1)"/>
					</td>
				</tr>
			</c:if>
			<c:if test='${(requestScope.registro.campos["B_DESEMBOLSO"] != "F")}'>
				<tr> 
				   	<th>Desembolso</th>
					<td> 
						<input type='radio' name='opcion' value='Desembolso' onClick="javascript=Opcion(1)"/ checked >
					</td>
				</tr>
			</c:if>
			
			<c:if test='${(requestScope.registro.campos["B_LINEA_FONDEO"] != "V")}'>
				<tr> 
				   	<th>Líneas de fondeo</th>
					<td> 
						<input type='radio' name='opcion' value='LineaFondeo' onClick="javascript=Opcion(2)"/>
					</td>
				</tr>
			</c:if>	
			<c:if test='${(requestScope.registro.campos["B_LINEA_FONDEO"] != "F")}'>
				<tr> 
				   	<th>Líneas de fondeo</th>
					<td> 
						<input type='radio' name='opcion' value='LineaFondeo' onClick="javascript=Opcion(2)"/ checked >
					</td>
				</tr>
			</c:if>	
			
			<c:if test='${(requestScope.registro.campos["B_CANCELADO"] != "V")}'>
				<tr> 
				   	<th>Cancelado</th>
					<td> 
						<input type='radio' name='opcion' value='Cancelado' onClick="javascript=Opcion(3)"/>
					</td>
				</tr>
			</c:if>	
			<c:if test='${(requestScope.registro.campos["B_CANCELADO"] != "F")}'>
				<tr> 
				   	<th>Cancelado</th>
					<td> 
						<input type='radio' name='opcion' value='Cancelado' onClick="javascript=Opcion(3)"/ checked >
					</td>
				</tr>
			</c:if>	
			
			<c:if test='${(requestScope.registro.campos["B_ENTREGADO"] != "V")}'>
				<tr> 
				   	<th>Entregado</th>
					<td> 
						<input type='radio' name='opcion' value='Entregado' onClick="javascript=Opcion(4)"/>
					</td>
				</tr>
			</c:if>	
			<c:if test='${(requestScope.registro.campos["B_ENTREGADO"] != "F")}'>
				<tr> 
				   	<th>Entregado</th>
					<td> 
						<input type='radio' name='opcion' value='Entregado' onClick="javascript=Opcion(4)"/ checked >
					</td>
				</tr>
			</c:if>	
			
			<c:if test='${(requestScope.registro.campos["B_AUTORIZAR_COMITE"] != "F") || (requestScope.registro.campos["B_DESEMBOLSO"] != "F") || (requestScope.registro.campos["B_LINEA_FONDEO"] != "F") || (requestScope.registro.campos["B_CANCELADO"] != "F") || (requestScope.registro.campos["B_ENTREGADO"] != "F")}'>
				<tr> 
				   	<th>Ninguno</th>
					<td> 
						<input type='radio' name='opcion' value='Ninguno' onClick="javascript=Opcion(5)"/>
					</td>
				</tr>
			</c:if>	
			<c:if test='${(requestScope.registro.campos["B_AUTORIZAR_COMITE"] != "V") && (requestScope.registro.campos["B_DESEMBOLSO"] != "V") && (requestScope.registro.campos["B_LINEA_FONDEO"] != "V") && (requestScope.registro.campos["B_CANCELADO"] != "V") && (requestScope.registro.campos["B_ENTREGADO"] != "V")}'>
				<tr> 
				   	<th>Ninguno</th>
					<td> 
						<input type='radio' name='opcion' value='Ninguno' onClick="javascript=Opcion(5)"/ checked >
					</td>
				</tr>
			</c:if>	
		</c:if>	
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
	
	<script>
		function Opcion(iNumElemento){
			if (iNumElemento==0){
				document.frmRegistro.BAutorizarComite.value = "V";
				document.frmRegistro.BDesembolso.value = "F";
				document.frmRegistro.BLineaFondeo.value = "F";
				document.frmRegistro.BCancelado.value = "F";
				document.frmRegistro.BEntregado.value = "F";
			}
			if (iNumElemento==1){
				document.frmRegistro.BAutorizarComite.value = "F";
				document.frmRegistro.BDesembolso.value = "V";
				document.frmRegistro.BLineaFondeo.value = "F";
				document.frmRegistro.BCancelado.value = "F";
				document.frmRegistro.BEntregado.value = "F";
			}
			if (iNumElemento==2){
				document.frmRegistro.BAutorizarComite.value = "F";
				document.frmRegistro.BDesembolso.value = "F";
				document.frmRegistro.BLineaFondeo.value = "V";
				document.frmRegistro.BCancelado.value = "F";
				document.frmRegistro.BEntregado.value = "F";
			}
			if (iNumElemento==3){
				document.frmRegistro.BAutorizarComite.value = "F";
				document.frmRegistro.BDesembolso.value = "F";
				document.frmRegistro.BLineaFondeo.value = "F";
				document.frmRegistro.BCancelado.value = "V";
				document.frmRegistro.BEntregado.value = "F";
			}
			if (iNumElemento==4){
				document.frmRegistro.BAutorizarComite.value = "F";
				document.frmRegistro.BDesembolso.value = "F";
				document.frmRegistro.BLineaFondeo.value = "F";
				document.frmRegistro.BCancelado.value = "F";
				document.frmRegistro.BEntregado.value = "V";
			}
			if (iNumElemento==5){
				document.frmRegistro.BAutorizarComite.value = "F";
				document.frmRegistro.BDesembolso.value = "F";
				document.frmRegistro.BLineaFondeo.value = "F";
				document.frmRegistro.BCancelado.value = "F";
				document.frmRegistro.BEntregado.value = "F";
			}
		}
	</script>
	
</Portal:Pagina>	