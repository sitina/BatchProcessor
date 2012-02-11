package net.sitina.bp.modules;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import net.sitina.bp.api.BatchProcessorException;
import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

public class CZSODatabaseStorageModule extends Module {

	private static final String CZSO_URL_BASE = "http://registry.czso.cz/irsw/";

	private static final String DATABASE_DRIVER = "databaseDriver";

	private static final String CONNECTION_STRING = "connectionString";

	private static final String USER_PROPERTY = "user";

	private static final String PASSWORD_PROPERTY = "password";
	
	private static final String QUERY = "INSERT INTO czechcompanies (url, ico, name, kind, registration, employees, activity, typeOfCompany, region) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

	private String user;
	
	private String password;
	
	private String connectionString;
	
	private String databaseDriver;
	
	private Connection connection;
	
	public CZSODatabaseStorageModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
		super(in, out, config, instanceNumber);
		loadConfiguration();
		
		try {
			connection = DriverManager.getConnection(connectionString, user, password);
		} catch (SQLException e) {
			log.error("Problem connecting to DB", e);
		}
	}

	@Override
	protected void process(String item) {
		CompanyRecord record = getCompanyRecord(item);		

		try {
			Class.forName(databaseDriver);
			
			PreparedStatement stmt = connection.prepareStatement(QUERY);
			
			stmt.setString(1, record.getUrl());
			stmt.setLong(2, record.getCompanyID());
			stmt.setString(3, record.getName());
			stmt.setString(4, record.getKind());
			stmt.setDate(5, record.getRegistration());
			stmt.setString(6, record.getEmployees());
			stmt.setString(7, record.getActivity());
			stmt.setString(8, record.getTypeOfCompany());
			stmt.setString(9, record.getRegion());
			
			stmt.execute();
			
		} catch (Exception e) {
			if (record != null) {
				out.putItem(e.getMessage() + ";" + record.getUrl().toString());
			} else {
				out.putItem(e.getMessage() + ";" + item);
			}
			log.error("Error inserting record for item '" + item + "'", e);
		}
	}
	
	@Override
	protected void cleanup() {
		try {
			if (connection != null && connection.isValid(1000) && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			log.error("Problem closing connection", e);
		}
	}

	@Override
	protected void loadConfiguration() {
		if (configuration.containsKey(DATABASE_DRIVER)) {
			databaseDriver = configuration.getStringProperty(DATABASE_DRIVER);
		}
		if (configuration.containsKey(CONNECTION_STRING)) {
			connectionString = configuration.getStringProperty(CONNECTION_STRING);
		}
		if (configuration.containsKey(USER_PROPERTY)) {
			user = configuration.getStringProperty(USER_PROPERTY);
		}
		if (configuration.containsKey(PASSWORD_PROPERTY)) {
			password = configuration.getStringProperty(PASSWORD_PROPERTY);
		}
		
	}
	
	private CompanyRecord getCompanyRecord(String input) {
		CompanyRecord record = new CompanyRecord();
		
		CZSOStringTokenizer strTok = new CZSOStringTokenizer(input, ";");
		
		record.setUrl(updateCZSOURL(getValue(strTok)));
		record.setCompanyID(getLong(getValue(strTok)));
		record.setName(getValue(strTok));
		record.setKind(getValue(strTok));
		record.setRegistration(getDate(getValue(strTok)));
		getValue(strTok); // skip this item (date of company close)
		record.setEmployees(getValue(strTok));
		record.setActivity(getValue(strTok));
		record.setTypeOfCompany(getValue(strTok));
		record.setRegion(getValue(strTok));
		
		if (record.getCompanyID() == null) {
			throw new BatchProcessorException("It is impossible to save company without ID, record '" + input + "'");
		}
		
		return record;
	}
	
	private String updateCZSOURL(String originalURL) {
		String result = originalURL.substring(originalURL.indexOf("detail"));
		result = result.replace("-", "?");
		result = result.replace(".html", "");
		return CZSO_URL_BASE + result;
	}
	
	private String getValue(CZSOStringTokenizer strTok) {
		if (strTok.hasMoreTokens()) {
			return strTok.nextToken();
		} else {
			return null;
		}
	}
	
	private Date getDate(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
			return new Date(sdf.parse(date).getTime());
		} catch (Exception e) {
			return null;
		}
	}
	
	private Long getLong(String number) {
		try {
			return Long.valueOf(number);
		} catch (Exception e) {
			return null;
		}
	}
	
	private class CompanyRecord {
		
		private String url;
		
		private Long companyID;
		
		private String name;

		private String kind;
		
		private Date registration;

		private String employees;
		
		private String activity;
		
		private String typeOfCompany;
		
		private String region;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public Long getCompanyID() {
			return companyID;
		}

		public void setCompanyID(Long companyID) {
			this.companyID = companyID;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getKind() {
			return kind;
		}

		public void setKind(String kind) {
			this.kind = kind;
		}

		public Date getRegistration() {
			return registration;
		}

		public void setRegistration(Date registration) {
			this.registration = registration;
		}

		public String getEmployees() {
			return employees;
		}

		public void setEmployees(String employees) {
			this.employees = employees;
		}

		public String getActivity() {
			return activity;
		}

		public void setActivity(String activity) {
			this.activity = activity;
		}

		public String getTypeOfCompany() {
			return typeOfCompany;
		}

		public void setTypeOfCompany(String typeOfCompany) {
			this.typeOfCompany = typeOfCompany;
		}

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}
		
	}

	private class CZSOStringTokenizer {
		
		private String text;
		
		private String delimiter;
		
		public CZSOStringTokenizer(String text, String delimiter) {
			this.text = text;
			this.delimiter = delimiter;
		}
		
		public boolean hasMoreTokens() {
			return text.contains(delimiter);
		}
		
		public String nextToken() {
			String result = text.substring(0, text.indexOf(delimiter));
			text = text.substring(text.indexOf(delimiter) + 1);
			return result;
		}
	}
	
}
