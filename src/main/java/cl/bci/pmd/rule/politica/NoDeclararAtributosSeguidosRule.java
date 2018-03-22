package cl.bci.pmd.rule.politica;

import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class NoDeclararAtributosSeguidosRule extends AbstractJavaRule {
	
	public Object visit(ASTFieldDeclaration astFieldDeclaration , Object data){
		if(astFieldDeclaration.jjtGetNumChildren() > 2){
			if(astFieldDeclaration.jjtGetChild(2) != null){
				addViolation(data, astFieldDeclaration , astFieldDeclaration.getImage() );
			}
		}
		return data;
	}

	public Object visit(ASTLocalVariableDeclaration astLocalVariableDeclaration , Object data){
		if(astLocalVariableDeclaration.jjtGetNumChildren() > 2 ){
			if( astLocalVariableDeclaration.jjtGetChild(2) != null){
				addViolation(data, astLocalVariableDeclaration , astLocalVariableDeclaration.getImage() );
			}
		}
		return data;
	}
}
