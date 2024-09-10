package mda.generator.converters.type;


import mda.generator.beans.UmlDomain;
import mda.generator.exceptions.MdaGeneratorException;


/**
 * Convert JAVA types to POSTGRESQL types
 * @author Damien Guérard
 */
public class DomainToPostgresConverter extends AbstractDomainToJavaConverter {

	@Override
	public String getDataBaseType(UmlDomain domain) {
		String dbType;
		if (domain.getTypeName() != null && domain.getTypeName().startsWith("varchar")) {
			dbType = "VARCHAR(" + domain.getMaxLength() + ')';
		} else {

			String javaType = getJavaType(domain);

			switch (javaType) {
			case "String":
				if (domain.getMaxLength() != null && Integer.parseInt(domain.getMaxLength()) > 0) {
					dbType = "VARCHAR(" + domain.getMaxLength() + ')';
				} else {
					dbType = "TEXT";
				}
				break;
			case "Boolean":
				dbType = "BOOLEAN";
				break;
			case "Short":
				dbType = "SMALLINT";
				break;
			case "Char":
				dbType = "CHARACTER(1)";
				break;
			case "Integer":
				dbType = "INTEGER";
				break;
			case "Long":
				dbType = "BIGINT";
				break;
			case "java.time.LocalDate":
				dbType = "DATE";
				break;
			case "java.time.LocalDateTime":
				dbType = "TIMESTAMP";
				break;
			case "Float":
				dbType = "REAL";
				break;
			case "Double":
				dbType = "DOUBLE PRECISION";
				break;
			case "java.math.BigDecimal":
				if(domain.getScale() == null && domain.getPrecision() == null) {
					dbType = "NUMERIC";
				} else if(domain.getScale() == null) {
					// Nombre total de chiffres (= precision)
					final int precision = Integer.parseInt(domain.getPrecision());

					if(precision <= 0) {
						throw new MdaGeneratorException("Impossible de convertir le domaine " + domain.getName()
							+ " avec une precision inférieure au égale à 0.");
					}
					dbType = "NUMERIC(" + precision + ')';
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

					dbType = "NUMERIC(" + precision + "," + scale + ")";
				}
				break;
			case "byte[]":
			case "java.sql.Blob":
				dbType = "BYTEA";
				break;
			case "Byte":
				dbType = "BIT(1)";
				break;

			default:
				dbType = "Unknown";
				break;
			}
		}

		return dbType;
	}

}
