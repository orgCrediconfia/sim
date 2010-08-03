<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimCatalogoCargoComision">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de cargos y comisiones" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoCargoComision'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdCargoComision' controlvalor='${requestScope.registro.campos["ID_ACCESORIO"]}' controllongitud='5' controllongitudmax='5' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomCargoComision' controlvalor='${requestScope.registro.campos["NOM_ACCESORIO"]}' controllongitud='30' controllongitudmax='50' />
		
		<c:if test='${(requestScope.registro == null)}'>
			<tr> 
			   	<th>Beneficiarios</th>
				<td> 
					<input type='checkbox' name='BeneficiarioSi'/>S&iacute &nbsp;&nbsp;&nbsp;&nbsp; <input type='checkbox' name='BeneficiarioNo' />No
				</td>
			</tr>
		</c:if>
		
		<c:if test='${(requestScope.registro != null)}'>
			<c:if test='${(requestScope.registro.campos["BENEFICIARIO"] == "V")}'>
				<tr> 
			    	<th>Beneficiarios</th>
					<td> 
						<input type='checkbox' name='BeneficiarioSi'/ checked >S&iacute &nbsp;&nbsp;&nbsp;&nbsp; <input type='checkbox' name='BeneficiarioNo' />No
					</td>
				</tr>
			</c:if>
			<c:if test='${(requestScope.registro.campos["BENEFICIARIO"] == "F")}'>
				<tr> 
			    	<th>Beneficiarios</th>
					<td> 
						<input type='checkbox' name='BeneficiarioSi'/>S&iacute &nbsp;&nbsp;&nbsp;&nbsp; <input type='checkbox' name='BeneficiarioNo' / checked >No
					</td>
				</tr>
			</c:if>
		</c:if>
		
		<c:if test='${(requestScope.registro == null)}'>
			<tr> 
			   	<th>Accesorio</th>
				<td> 
					<input type='checkbox' name='AccesorioSi'/>S&iacute &nbsp;&nbsp;&nbsp;&nbsp; <input type='checkbox' name='AccesorioNo' />No
				</td>
			</tr>
		</c:if>
		
		<c:if test='${(requestScope.registro != null)}'>
			<c:if test='${(requestScope.registro.campos["B_ACCESORIO"] == "V")}'>
				<tr> 
			    	<th>Accesorio</th>
					<td> 
						<input type='checkbox' name='AccesorioSi'/ checked >S&iacute &nbsp;&nbsp;&nbsp;&nbsp; <input type='checkbox' name='AccesorioNo' />No
					</td>
				</tr>
			</c:if>
			<c:if test='${(requestScope.registro.campos["B_ACCESORIO"] == "F")}'>
				<tr> 
			    	<th>Accesorio</th>
					<td> 
						<input type='checkbox' name='AccesorioSi'/>S&iacute &nbsp;&nbsp;&nbsp;&nbsp; <input type='checkbox' name='AccesorioNo' / checked >No
					</td>
				</tr>
			</c:if>
		</c:if>
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>	
