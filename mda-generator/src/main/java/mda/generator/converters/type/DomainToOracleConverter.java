package mda.generator.converters.type;

import mda.generator.beans.UmlDomain;
import mda.generator.exceptions.MdaGeneratorException;

/**
 * Convert JAVA types to ORACLE DB types
 * @author Fabien Crapart
 */
public class DomainToOracleConverter extends AbstractDomainToJavaConverter {

	@Override
	public String getDataBaseType(UmlDomain domain) {
		String javaType = getJavaType(domain);
		String dbType;
		
		switch(javaType) {
		case "String":
			// Max length defined, we stay in varchar2 if size <= 4000
			if(domain.getMaxLength()!=null && !"0".equals(domain.getMaxLength())&& Integer.parseInt(domain.getMaxLength()) <= 4000) {
				dbType="VARCHAR2("+domain.getMaxLength()+")";
			} else { // CLOB for other cases
				dbType="CLOB";
			}
			break;
		case "Boolean" :
			dbType= "NUMBER(1)";
			break;
		case "Short":
			dbType= "NUMBER(2)";
			break;
		case "Char":
			dbType= "CHAR(1 CHAR)";
			break;
		case "Integer" :
			dbType= "NUMBER(4)";
			break;
		case "Long":
			dbType= "NUMBER(12)";
			break;
		case "java.time.LocalDate":
			dbType= "DATE";
			break;
		case "java.time.LocalDateTime":
			dbType= "DATE";
			break;
		case "Float":
			dbType= "NUMBER(4,2)";
			break;
		case "Double":
			dbType= "NUMBER(12,2)";
			break;
		case "java.math.BigDecimal":
			if(domain.getScale() == null && domain.getPrecision() == null) {
				dbType = "NUMBER";
			} else if(domain.getScale() == null) {
				// Nombre total de chiffres (= precision)
				final int precision = Integer.parseInt(domain.getPrecision());

				if(precision <= 0) {
					throw new MdaGeneratorException("Impossible de convertir le domaine " + domain.getName()
						+ " avec une precision inférieure au égale à 0.");
				}
				dbType = "NUMBER(" + precision + ')';
			} else {
				if(domain.getPrecision() == null) {
					throw new MdaGeneratorException("Impossible de convertir le domaine " + domain.getName()
						+ " avec scale non null et precision null");
				}
				// Nombre de chiffres après la virgule (=scale)
				int scale = Integer.parseInt(domain.getScale());
				// Nombre total de chiffres (= precision)
				final int precision = Integer.parseInt(domain.getPrecision());

				if(precision <= 0) {
					throw new MdaGeneratorException("Impossible de convertir le domaine " + domain.getName()
						+ " avec une precision inférieure au égale à 0.");
				}
				if(scale > precision) {
					throw new MdaGeneratorException("Impossible de convertir le domaine " + domain.getName()
						+ " avec une scale > precision");
				}
				if(scale < 0) {
					scale = 0;
				}

				dbType = "NUMBER(" + precision + "," + scale + ")";
			}
			break;
		case "Byte[]": 
			dbType= "RAW(" + (domain.getMaxLength()==null || "0".equals(domain.getMaxLength())?"1":domain.getMaxLength())+ ")";
			break;
		case "Byte": 
			dbType= "RAW(1)"; 
			break;
		case "java.sql.Blob":
			dbType= "BLOB";
			break;
		default :
			dbType = "Unknown";
			break;
		}
		
		return dbType;
	}

}
