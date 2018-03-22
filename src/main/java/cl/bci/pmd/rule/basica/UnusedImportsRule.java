package cl.bci.pmd.rule.basica;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.Comment;
import net.sourceforge.pmd.lang.java.ast.DummyJavaNode;
import net.sourceforge.pmd.lang.java.ast.FormalComment;
import net.sourceforge.pmd.lang.java.ast.TypeNode;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.rule.ImportWrapper;

public class UnusedImportsRule extends AbstractJavaRule {

    protected Set<ImportWrapper> imports = new HashSet<ImportWrapper>();

    /*
     * Patterns to match the following constructs:
     *
     * @see package.class#member(param, param) label {@linkplain
     * package.class#member(param, param) label} {@link
     * package.class#member(param, param) label} {@link package.class#field}
     * {@value package.class#field}
     * 
     * @throws package.class label
     */
    private static final Pattern SEE_PATTERN = Pattern
            .compile("@see\\s+(\\p{Alpha}\\w*)(?:#\\w*(?:\\(([\\w\\s,]*)\\))?)?");

    private static final Pattern LINK_PATTERNS = Pattern
            .compile("\\{@link(?:plain)?\\s+(\\p{Alpha}\\w*)(?:#\\w*(?:\\(([.\\w\\s,]*)\\))?)?[\\s\\}]");

    private static final Pattern VALUE_PATTERN = Pattern.compile("\\{@value\\s+(\\p{Alpha}\\w*)[\\s#\\}]");

    private static final Pattern THROWS_PATTERN = Pattern.compile("@throws\\s+(\\p{Alpha}\\w*)");

    private static final Pattern[] PATTERNS = { SEE_PATTERN, LINK_PATTERNS, VALUE_PATTERN, THROWS_PATTERN };

    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        imports.clear();
        super.visit(node, data);
        visitComments(node);

        /*
         * special handling for Bug 2606609 : False "UnusedImports" positive in
         * package-info.java package annotations are processed before the import
         * clauses so they need to be examined again later on.
         */
        if (node.jjtGetNumChildren() > 0 && node.jjtGetChild(0) instanceof ASTPackageDeclaration) {
            visit((ASTPackageDeclaration) node.jjtGetChild(0), data);
        }
        for (ImportWrapper wrapper : imports) {
            addViolation(data, wrapper.getNode(), wrapper.getFullName());
        }
        return data;
    }

    private void visitComments(ASTCompilationUnit node) {
        if (imports.isEmpty()) {
            return;
        }
        for (Comment comment : node.getComments()) {
            if (!(comment instanceof FormalComment)) {
                continue;
            }
            for (Pattern p : PATTERNS) {
                Matcher m = p.matcher(comment.getImage());
                while (m.find()) {
                    String s = m.group(1);
                    imports.remove(new ImportWrapper(s, s, new DummyJavaNode(-1)));

                    if (m.groupCount() > 1) {
                        s = m.group(2);
                        if (s != null) {
                            String[] params = s.split("\\s*,\\s*");
                            for (String param : params) {
                                final int firstDot = param.indexOf('.');
                                final String expectedImportName;
                                if (firstDot == -1) {
                                    expectedImportName = param;
                                } else {
                                    expectedImportName = param.substring(0, firstDot);
                                }
                                imports.remove(new ImportWrapper(param, expectedImportName, new DummyJavaNode(-1)));
                            }
                        }
                    }

                    if (imports.isEmpty()) {
                        return;
                    }
                }
            }
        }
    }

    @Override
    public Object visit(ASTImportDeclaration node, Object data) {
        if (node.isImportOnDemand()) {
            ASTName importedType = (ASTName) node.jjtGetChild(0);
            imports.add(new ImportWrapper(importedType.getImage(), null, node, node.getType(), node.isStatic()));
        } else {
            if (!node.isImportOnDemand()) {
                ASTName importedType = (ASTName) node.jjtGetChild(0);
                String className;
                if (isQualifiedName(importedType)) {
                    int lastDot = importedType.getImage().lastIndexOf('.') + 1;
                    className = importedType.getImage().substring(lastDot);
                } else {
                    className = importedType.getImage();
                }
                imports.add(new ImportWrapper(importedType.getImage(), className, node));
            }
        }
        return data;
    }

    @Override
    public Object visit(ASTClassOrInterfaceType node, Object data) {
        check(node);
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTName node, Object data) {
        check(node);
        return data;
    }

    protected void check(Node node) {
        if (imports.isEmpty()) {
            return;
        }
        ImportWrapper candidate = getImportWrapper(node);
        Iterator<ImportWrapper> it = imports.iterator();
        while (it.hasNext()) {
            ImportWrapper i = it.next();
            if (i.matches(candidate)) {
                it.remove();
                return;
            }
        }
        if (TypeNode.class.isAssignableFrom(node.getClass()) && ((TypeNode) node).getType() != null) {
            Class<?> c = ((TypeNode) node).getType();
            if (c.getPackage() != null) {
                candidate = new ImportWrapper(c.getPackage().getName(), null);
                if (imports.contains(candidate)) {
                    imports.remove(candidate);
                }
            }
        }
    }

    protected ImportWrapper getImportWrapper(Node node) {
        String name;
        if (!isQualifiedName(node)) {
            name = node.getImage();
        } else {
            name = node.getImage().substring(0, node.getImage().indexOf('.'));
        }
        return new ImportWrapper(node.getImage(), name);
    }
}