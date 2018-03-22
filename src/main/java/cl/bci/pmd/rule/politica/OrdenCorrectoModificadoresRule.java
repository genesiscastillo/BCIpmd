package cl.bci.pmd.rule.politica;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBody;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBodyDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.Comment;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class OrdenCorrectoModificadoresRule extends AbstractJavaRule {
	
	public Object visit(ASTClassOrInterfaceBody astClassOrInterfaceBody ,	Object data){
		List<Modificadores> setModificadores = new ArrayList<Modificadores>();
		List<ASTClassOrInterfaceBodyDeclaration> astClassOrInterfaceBodyDeclarations = astClassOrInterfaceBody.findChildrenOfType(ASTClassOrInterfaceBodyDeclaration.class);
		for(ASTClassOrInterfaceBodyDeclaration astClassOrInterfaceBodyDeclaration : astClassOrInterfaceBodyDeclarations)	{
			ASTFieldDeclaration astFieldDeclaration = astClassOrInterfaceBodyDeclaration.getFirstChildOfType(ASTFieldDeclaration.class);
			if(astFieldDeclaration != null){
				if(astFieldDeclaration.isDefault() && !setModificadores.contains(Modificadores.DEFAULT)){
					setModificadores.add(Modificadores.DEFAULT);
				}
				else if(astFieldDeclaration.isPublic() && !setModificadores.contains(Modificadores.PUBLIC)){
					setModificadores.add(Modificadores.PUBLIC);
				}
				else if(astFieldDeclaration.isProtected() && !setModificadores.contains(Modificadores.PROTECTED)){
					setModificadores.add(Modificadores.PROTECTED);
				}
				else if(astFieldDeclaration.isProtected() && !setModificadores.contains(Modificadores.PRIVATE)){
					setModificadores.add(Modificadores.PRIVATE);
				}
			}
		}
		boolean status = true;
		Integer ordenInicial = 0;
		for(int l =0; l < setModificadores.size() ; l++){
			if(status){
				status = false;
				ordenInicial = setModificadores.get(l).getOrden();
			}
			if(ordenInicial == setModificadores.get(l).getOrden()){
				ordenInicial++;
			}else{
				addViolation(data, astClassOrInterfaceBody , astClassOrInterfaceBody.getImage() );
			}
		}
		return data;
	}

	public 	enum Modificadores {
		DEFAULT(1),
		PUBLIC(2),
		PROTECTED(3),
		PRIVATE(4);
		
		Integer orden;
		Modificadores(Integer _orden){
			this.orden = _orden;
		}
		public Integer getOrden(){
			return this.orden;
		}
	}
	
}
