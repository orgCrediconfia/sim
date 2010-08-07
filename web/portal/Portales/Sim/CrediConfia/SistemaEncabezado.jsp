<%@ page language="java" import="com.rapidsist.portal.configuracion.Usuario"%>
<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<%Usuario usuario = (Usuario)session.getAttribute("Usuario");%>

			<!---------------------------------->
			<!--INICIA SECCION ENCABEZADO -->
			<!---------------------------------->
			<table id="Encabezado">
				<tr>
					<td id="logo">&nbsp;
					</td>
					<td width="20">
					</td>
					<td>
						
					</td>
					<td width="20">
					</td>
					
						<td>
							<img src='<c:out value='${pageContext.request.contextPath}'/>/Portales/Sim/CrediConfia/img/CrediConfia.bmp' onclick="location=''" border="0"> 
						</td>
					
					<td width="150" valign="top" nowrap="nowrap" id="EncabezadoCuerpo">
						<div align="right">
							<p><a href="<%=request.getContextPath() %>/Inicio">Inicio</a>
							  <%if (!usuario.bValidado){%>
								| <a href="<%=request.getContextPath() %>/Autentificacion">Entrar</a>
							    <%}%>
							    <%if (usuario.bValidado){%>
								| <a href="<%=request.getContextPath() %>/BorraSesion">Salir</a>
							    <%}%>
						  </p>
							
						</div>
					</td>
				</tr>
			</table>
			
			<Portal:SelectorAplicaciones/>
		
			<Portal:BarraNavegacionPaginas/>
	
			<!---------------------------------->
			<!--FIN SECCION ENCABEZADO -->
			<!---------------------------------->

