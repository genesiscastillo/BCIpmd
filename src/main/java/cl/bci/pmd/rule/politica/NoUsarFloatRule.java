package cl.bci.pmd.rule.politica;

import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class NoUsarFloatRule extends AbstractJavaRule {
	
	public Object visit(ASTFieldDeclaration astFieldDeclaration , Object data){
		if(astFieldDeclaration.jjtGetChild(0) instanceof ASTType){
			ASTType astType = (ASTType)astFieldDeclaration.jjtGetChild(0);
			if(astType.getTypeImage().equals("float")){
				addViolation(data, astType , astType.getTypeImage() );
			}
		}
		return data;
	}
	public Object visit(ASTLocalVariableDeclaration astLocalVariableDeclaration, Object data){
		if(astLocalVariableDeclaration.jjtGetChild(0) instanceof ASTType){
			ASTType astType = (ASTType)astLocalVariableDeclaration.jjtGetChild(0);
			if(astType.getTypeImage().equals("float")){
				addViolation(data, astType , astType.getTypeImage() );
			}
		}
		return data;
	}

}
