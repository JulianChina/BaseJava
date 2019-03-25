package zAssistant.annotations;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.util.DeclarationVisitors;
import com.sun.mirror.util.SimpleDeclarationVisitor;
import zAssistant.annotations.database.Constraints;
import zAssistant.annotations.database.DBTable;
import zAssistant.annotations.database.SQLInteger;
import zAssistant.annotations.database.SQLString;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static zUtils.Print.print;

public class TableCreationProcessorFactory implements AnnotationProcessorFactory {
    @Override
    public Collection<String> supportedOptions() {
        return Collections.emptySet();
    }

    @Override
    public Collection<String> supportedAnnotationTypes() {
        return Arrays.asList("zAssistant.annotations.database.DBTable",
                "zAssistant.annotations.database.Constraints",
                "zAssistant.annotations.database.SQLString",
                "zAssistant.annotations.database.SQLInteger");
    }

    @Override
    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> atds, AnnotationProcessorEnvironment env) {
        return new TableCreationProcessor(env);
    }

    public static class TableCreationProcessor implements AnnotationProcessor {
        private final AnnotationProcessorEnvironment env;
        private String sql = "";

        public TableCreationProcessor(AnnotationProcessorEnvironment env) {
            this.env = env;
        }

        @Override
        public void process() {
            for (TypeDeclaration typeDecl : env.getSpecifiedTypeDeclarations()) {
                typeDecl.accept(DeclarationVisitors.getDeclarationScanner(new TableCreationVisitor(), DeclarationVisitors.NO_OP));
                sql = sql.substring(0, sql.length() - 1) + ");";
                print("creation SQL is : \n" + sql);
                sql = "";
            }
        }

        private class TableCreationVisitor extends SimpleDeclarationVisitor {
            public void visitClassDeclaration(ClassDeclaration d) {
                DBTable dbTable = d.getAnnotation(DBTable.class);
                if (dbTable != null) {
                    sql += "CREATE TABLE ";
                    sql += (dbTable.name().length() < 1) ? d.getSimpleName().toUpperCase() : dbTable.name();
                    sql += " (";
                }
            }
            public void visitFieldDeclaration(FieldDeclaration d) {
                String columnName = "";
                if (d.getAnnotation(SQLInteger.class) != null) {
                    SQLInteger sInt = d.getAnnotation(SQLInteger.class);
                    if (sInt.name().length() < 1) {
                        columnName = d.getSimpleName().toUpperCase();
                    } else {
                        columnName = sInt.name();
                    }
                    sql += "\n    " + columnName + " INT" + getConstraint(sInt.constraints()) + ",";
                }
                if (d.getAnnotation(SQLString.class) != null) {
                    SQLString sString = d.getAnnotation(SQLString.class);
                    if (sString.name().length() < 1) {
                        columnName = d.getSimpleName().toUpperCase();
                    } else {
                        columnName = sString.name();
                    }
                    sql += "\n    " + columnName + " VARCHAR(" + sString.value() + ")" + getConstraint(sString.constraints()) + ",";
                }
            }
            private String getConstraint(Constraints con) {
                String constraints = "";
                if (!con.allowNull()) {
                    constraints += " NOT NULL";
                }
                if (con.primaryKey()) {
                    constraints += " PRIMARY KEY";
                }
                if (con.unique()) {
                    constraints += " UNIQUE";
                }
                return constraints;
            }
        }
    }
}
