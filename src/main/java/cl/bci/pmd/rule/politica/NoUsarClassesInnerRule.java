package cl.bci.pmd.rule.politica;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBodyDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class NoUsarClassesInnerRule extends AbstractJavaRule {
	
	public Object visit(ASTClassOrInterfaceBodyDeclaration astClassOrInterfaceBodyDeclaration, Object data) {
		if(astClassOrInterfaceBodyDeclaration.jjtGetChild(0) instanceof ASTClassOrInterfaceDeclaration){
			addViolation(data, astClassOrInterfaceBodyDeclaration , astClassOrInterfaceBodyDeclaration.getImage() );
		}
		return data;
	}
}
